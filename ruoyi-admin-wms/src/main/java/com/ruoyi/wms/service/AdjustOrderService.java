package com.ruoyi.wms.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.constant.ServiceConstants;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.exception.base.BaseException;
import com.ruoyi.common.core.utils.MapstructUtils;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.mybatis.core.domain.BaseEntity;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.ruoyi.common.satoken.utils.LoginHelper;
import com.ruoyi.wms.domain.bo.AdjustOrderBo;
import com.ruoyi.wms.domain.bo.AdjustOrderDetailBo;
import com.ruoyi.wms.domain.bo.InventoryBo;
import com.ruoyi.wms.domain.bo.InventoryDetailBo;
import com.ruoyi.wms.domain.entity.AdjustOrder;
import com.ruoyi.wms.domain.entity.AdjustOrderDetail;
import com.ruoyi.wms.domain.entity.Box;
import com.ruoyi.wms.domain.entity.InventoryDetail;
import com.ruoyi.wms.domain.entity.InventoryHistory;
import com.ruoyi.wms.domain.vo.AdjustOrderVo;
import com.ruoyi.wms.mapper.AdjustOrderMapper;
import com.ruoyi.wms.mapper.InventoryDetailMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 库存调整主表 Service
 */
@RequiredArgsConstructor
@Service
public class AdjustOrderService {

    private final AdjustOrderMapper adjustOrderMapper;
    private final AdjustOrderDetailService adjustOrderDetailService;
    private final InventoryService inventoryService;
    private final InventoryDetailService inventoryDetailService;
    private final InventoryDetailMapper inventoryDetailMapper;
    private final InventoryHistoryService inventoryHistoryService;
    private final ItemInstanceService itemInstanceService;
    private final BoxService boxService;

    public AdjustOrderVo queryById(Long id) {
        AdjustOrderVo adjustOrderVo = adjustOrderMapper.selectVoById(id);
        if (adjustOrderVo == null) {
            throw new BaseException("调整单不存在");
        }
        adjustOrderVo.setDetails(adjustOrderDetailService.queryByAdjustOrderId(id));
        return adjustOrderVo;
    }

    public TableDataInfo<AdjustOrderVo> queryPageList(AdjustOrderBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<AdjustOrder> lqw = buildQueryWrapper(bo);
        Page<AdjustOrderVo> result = adjustOrderMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    public List<AdjustOrderVo> queryList(AdjustOrderBo bo) {
        LambdaQueryWrapper<AdjustOrder> lqw = buildQueryWrapper(bo);
        return adjustOrderMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<AdjustOrder> buildQueryWrapper(AdjustOrderBo bo) {
        LambdaQueryWrapper<AdjustOrder> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getAdjustOrderNo()), AdjustOrder::getAdjustOrderNo, bo.getAdjustOrderNo());
        lqw.eq(bo.getWarehouseId() != null, AdjustOrder::getWarehouseId, bo.getWarehouseId());
        lqw.eq(bo.getAreaId() != null, AdjustOrder::getAreaId, bo.getAreaId());
        lqw.eq(bo.getRackId() != null, AdjustOrder::getRackId, bo.getRackId());
        lqw.eq(bo.getLocationId() != null, AdjustOrder::getLocationId, bo.getLocationId());
        lqw.eq(bo.getAdjustStatus() != null, AdjustOrder::getAdjustStatus, bo.getAdjustStatus());
        lqw.like(StringUtils.isNotBlank(bo.getAdjustReason()), AdjustOrder::getAdjustReason, bo.getAdjustReason());
        lqw.orderByDesc(BaseEntity::getCreateTime);
        return lqw;
    }

    @Transactional
    public void insertByBo(AdjustOrderBo bo) {
        validateAdjustOrderNo(bo.getAdjustOrderNo());
        fillDetailDefaults(bo);
        AdjustOrder add = MapstructUtils.convert(bo, AdjustOrder.class);
        adjustOrderMapper.insert(add);
        bo.setId(add.getId());
        List<AdjustOrderDetail> detailList = MapstructUtils.convert(bo.getDetails(), AdjustOrderDetail.class);
        detailList.forEach(it -> it.setAdjustOrderId(add.getId()));
        adjustOrderDetailService.saveDetails(detailList);
    }

    @Transactional
    public void updateByBo(AdjustOrderBo bo) {
        fillDetailDefaults(bo);
        AdjustOrder update = MapstructUtils.convert(bo, AdjustOrder.class);
        adjustOrderMapper.updateById(update);
        List<Long> incomingIds = bo.getDetails().stream()
            .map(AdjustOrderDetailBo::getId)
            .filter(Objects::nonNull)
            .toList();
        List<Long> existedIds = adjustOrderDetailService.queryByAdjustOrderId(bo.getId()).stream()
            .map(it -> it.getId())
            .filter(Objects::nonNull)
            .toList();
        List<Long> deleteIds = existedIds.stream().filter(id -> !incomingIds.contains(id)).toList();
        if (CollUtil.isNotEmpty(deleteIds)) {
            adjustOrderDetailService.deleteByIds(deleteIds);
        }
        List<AdjustOrderDetail> detailList = MapstructUtils.convert(bo.getDetails(), AdjustOrderDetail.class);
        detailList.forEach(it -> it.setAdjustOrderId(bo.getId()));
        adjustOrderDetailService.saveDetails(detailList);
    }

    @Transactional
    public void adjust(AdjustOrderBo bo) {
        validateBeforeAdjust(bo);
        if (Objects.isNull(bo.getId())) {
            insertByBo(bo);
        } else {
            updateByBo(bo);
        }

        List<InventoryDetailBo> deductList = buildDeductInventoryDetailList(bo.getDetails());
        if (CollUtil.isNotEmpty(deductList)) {
            inventoryDetailService.validateRemainQuantity(deductList);
            inventoryDetailMapper.deductInventoryDetailQuantity(deductList, LoginHelper.getUsername(), LocalDateTime.now());
        }

        List<InventoryDetail> addInventoryDetails = buildAddInventoryDetails(bo);
        if (CollUtil.isNotEmpty(addInventoryDetails)) {
            inventoryDetailService.saveBatch(addInventoryDetails);
        }

        List<InventoryBo> inventoryBos = mergeInventoryChanges(bo.getDetails());
        if (CollUtil.isNotEmpty(inventoryBos)) {
            inventoryService.updateInventoryQuantity(inventoryBos);
        }

        createInventoryHistory(bo);
        syncAdjustObjects(bo.getDetails());
    }

    public void deleteById(Long id) {
        validateIdBeforeDelete(id);
        adjustOrderMapper.deleteById(id);
    }

    public void deleteByIds(Collection<Long> ids) {
        adjustOrderMapper.deleteBatchIds(ids);
    }

    private void validateAdjustOrderNo(String adjustOrderNo) {
        LambdaQueryWrapper<AdjustOrder> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(AdjustOrder::getAdjustOrderNo, adjustOrderNo);
        if (adjustOrderMapper.exists(wrapper)) {
            throw new BaseException("调整单号重复，请手动修改");
        }
    }

    private void validateIdBeforeDelete(Long id) {
        AdjustOrderVo adjustOrderVo = queryById(id);
        if (ServiceConstants.AdjustOrderStatus.FINISH.equals(adjustOrderVo.getAdjustStatus())) {
            throw new ServiceException("调整单【" + adjustOrderVo.getAdjustOrderNo() + "】已执行，无法删除");
        }
    }

    private void validateBeforeAdjust(AdjustOrderBo bo) {
        if (CollUtil.isEmpty(bo.getDetails())) {
            throw new BaseException("调整明细不能为空");
        }
        fillDetailDefaults(bo);
        if (bo.getId() != null) {
            AdjustOrder adjustOrder = adjustOrderMapper.selectById(bo.getId());
            Assert.notNull(adjustOrder, "调整单不存在");
            Assert.isFalse(ServiceConstants.AdjustOrderStatus.FINISH.equals(adjustOrder.getAdjustStatus()), "调整单已执行");
        }
        boolean allZero = bo.getDetails().stream()
            .allMatch(detail -> detail.getDifferenceQuantity() == null || detail.getDifferenceQuantity().compareTo(BigDecimal.ZERO) == 0);
        Assert.isFalse(allZero, "调整差异不能全部为0");
        bo.getDetails().forEach(detail -> {
            Assert.notNull(detail.getInventoryDetailId(), "调整明细缺少库存明细ID");
            Assert.notNull(detail.getWarehouseId(), "调整明细缺少仓库");
            Assert.notNull(detail.getAreaId(), "调整明细缺少库区");
        });
    }

    private void fillDetailDefaults(AdjustOrderBo bo) {
        if (bo.getAdjustDate() == null) {
            bo.setAdjustDate(LocalDateTime.now());
        }
        if (CollUtil.isEmpty(bo.getDetails())) {
            return;
        }
        int lineNo = 1;
        for (AdjustOrderDetailBo detail : bo.getDetails()) {
            if (detail.getLineNo() == null) {
                detail.setLineNo(lineNo);
            }
            if (detail.getWarehouseId() == null) {
                detail.setWarehouseId(bo.getWarehouseId());
            }
            if (detail.getAreaId() == null) {
                detail.setAreaId(bo.getAreaId());
            }
            if (detail.getRackId() == null) {
                detail.setRackId(bo.getRackId());
            }
            if (detail.getLocationId() == null) {
                detail.setLocationId(bo.getLocationId());
            }
            if (detail.getDifferenceQuantity() == null && detail.getBeforeQuantity() != null && detail.getAfterQuantity() != null) {
                detail.setDifferenceQuantity(detail.getAfterQuantity().subtract(detail.getBeforeQuantity()));
            }
            lineNo++;
        }
    }

    private List<InventoryDetailBo> buildDeductInventoryDetailList(List<AdjustOrderDetailBo> details) {
        List<InventoryDetailBo> list = new ArrayList<>();
        for (AdjustOrderDetailBo detail : details) {
            if (detail.getDifferenceQuantity() == null || detail.getDifferenceQuantity().compareTo(BigDecimal.ZERO) >= 0) {
                continue;
            }
            InventoryDetailBo inventoryDetailBo = new InventoryDetailBo();
            inventoryDetailBo.setId(detail.getInventoryDetailId());
            inventoryDetailBo.setSkuId(detail.getSkuId());
            inventoryDetailBo.setWarehouseId(detail.getWarehouseId());
            inventoryDetailBo.setAreaId(detail.getAreaId());
            inventoryDetailBo.setQuantity(detail.getDifferenceQuantity());
            inventoryDetailBo.setBatchNo(detail.getBatchNo());
            inventoryDetailBo.setProductionDate(detail.getProductionDate());
            inventoryDetailBo.setExpirationDate(detail.getExpirationDate());
            inventoryDetailBo.setShipmentQuantity(detail.getDifferenceQuantity().abs());
            list.add(inventoryDetailBo);
        }
        return list;
    }

    private List<InventoryDetail> buildAddInventoryDetails(AdjustOrderBo bo) {
        List<InventoryDetail> list = new ArrayList<>();
        for (AdjustOrderDetailBo detail : bo.getDetails()) {
            if (detail.getDifferenceQuantity() == null || detail.getDifferenceQuantity().compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }
            InventoryDetail addInventoryDetail = new InventoryDetail();
            addInventoryDetail.setReceiptOrderId(bo.getId());
            addInventoryDetail.setOrderNo(bo.getAdjustOrderNo());
            addInventoryDetail.setType(ServiceConstants.InventoryDetailType.ADJUST);
            addInventoryDetail.setSkuId(detail.getSkuId());
            addInventoryDetail.setWarehouseId(detail.getWarehouseId());
            addInventoryDetail.setAreaId(detail.getAreaId());
            addInventoryDetail.setRackId(detail.getRackId());
            addInventoryDetail.setLocationId(detail.getLocationId());
            addInventoryDetail.setItemInstanceId(detail.getItemInstanceId());
            addInventoryDetail.setBoxId(detail.getBoxId());
            addInventoryDetail.setSourceOrderType("adjust");
            addInventoryDetail.setSourceOrderId(bo.getId());
            addInventoryDetail.setLineNo(detail.getLineNo());
            addInventoryDetail.setQuantity(detail.getDifferenceQuantity());
            addInventoryDetail.setBatchNo(detail.getBatchNo());
            addInventoryDetail.setProductionDate(detail.getProductionDate());
            addInventoryDetail.setExpirationDate(detail.getExpirationDate());
            addInventoryDetail.setEquipmentCode(detail.getEquipmentCode());
            addInventoryDetail.setSpecModel(detail.getSpecModel());
            addInventoryDetail.setProductMark(detail.getProductMark());
            addInventoryDetail.setQualityGrade(detail.getQualityGrade());
            addInventoryDetail.setRemainQuantity(detail.getDifferenceQuantity());
            addInventoryDetail.setRemark(detail.getRemark());
            list.add(addInventoryDetail);
        }
        return list;
    }

    private List<InventoryBo> mergeInventoryChanges(List<AdjustOrderDetailBo> details) {
        Map<String, InventoryBo> mergedMap = new HashMap<>();
        for (AdjustOrderDetailBo detail : details) {
            if (detail.getDifferenceQuantity() == null || detail.getDifferenceQuantity().compareTo(BigDecimal.ZERO) == 0) {
                continue;
            }
            String key = detail.getWarehouseId() + "_" + detail.getAreaId() + "_" + detail.getSkuId();
            if (mergedMap.containsKey(key)) {
                InventoryBo merged = mergedMap.get(key);
                merged.setQuantity(merged.getQuantity().add(detail.getDifferenceQuantity()));
                continue;
            }
            InventoryBo inventoryBo = new InventoryBo();
            inventoryBo.setWarehouseId(detail.getWarehouseId());
            inventoryBo.setAreaId(detail.getAreaId());
            inventoryBo.setSkuId(detail.getSkuId());
            inventoryBo.setQuantity(detail.getDifferenceQuantity());
            mergedMap.put(key, inventoryBo);
        }
        return new ArrayList<>(mergedMap.values());
    }

    private void createInventoryHistory(AdjustOrderBo bo) {
        List<InventoryHistory> inventoryHistoryList = new LinkedList<>();
        for (AdjustOrderDetailBo detail : bo.getDetails()) {
            if (detail.getDifferenceQuantity() == null || detail.getDifferenceQuantity().compareTo(BigDecimal.ZERO) == 0) {
                continue;
            }
            InventoryHistory inventoryHistory = new InventoryHistory();
            inventoryHistory.setOrderId(bo.getId());
            inventoryHistory.setOrderNo(bo.getAdjustOrderNo());
            inventoryHistory.setOrderType(ServiceConstants.InventoryHistoryOrderType.ADJUST);
            inventoryHistory.setSkuId(detail.getSkuId());
            inventoryHistory.setWarehouseId(detail.getWarehouseId());
            inventoryHistory.setAreaId(detail.getAreaId());
            inventoryHistory.setRackId(detail.getRackId());
            inventoryHistory.setLocationId(detail.getLocationId());
            inventoryHistory.setItemInstanceId(detail.getItemInstanceId());
            inventoryHistory.setBoxId(detail.getBoxId());
            inventoryHistory.setBatchNo(detail.getBatchNo());
            inventoryHistory.setProductionDate(detail.getProductionDate());
            inventoryHistory.setExpirationDate(detail.getExpirationDate());
            inventoryHistory.setEquipmentCode(detail.getEquipmentCode());
            inventoryHistory.setSpecModel(detail.getSpecModel());
            inventoryHistory.setProductMark(detail.getProductMark());
            inventoryHistory.setQualityGrade(detail.getQualityGrade());
            inventoryHistory.setQuantity(detail.getDifferenceQuantity());
            inventoryHistory.setBeforeQuantity(detail.getBeforeQuantity());
            inventoryHistory.setAfterQuantity(detail.getAfterQuantity());
            inventoryHistory.setOperationType("adjust");
            inventoryHistory.setOperatorName(LoginHelper.getUsername());
            inventoryHistory.setRemark(detail.getRemark());
            inventoryHistoryList.add(inventoryHistory);
        }
        inventoryHistoryService.saveBatch(inventoryHistoryList);
    }

    private void syncAdjustObjects(List<AdjustOrderDetailBo> details) {
        for (AdjustOrderDetailBo detail : details) {
            if (detail.getDifferenceQuantity() == null || detail.getDifferenceQuantity().compareTo(BigDecimal.ZERO) == 0) {
                continue;
            }
            if (detail.getItemInstanceId() != null) {
                if (detail.getDifferenceQuantity().compareTo(BigDecimal.ZERO) < 0) {
                    itemInstanceService.markDisabled(detail.getItemInstanceId());
                } else if (detail.getBoxId() != null) {
                    Box box = boxService.getById(detail.getBoxId());
                    if (box != null) {
                        itemInstanceService.markInBox(detail.getItemInstanceId(), box);
                    }
                } else {
                    itemInstanceService.moveTo(detail.getItemInstanceId(), detail.getWarehouseId(), detail.getAreaId(), detail.getRackId(), detail.getLocationId());
                }
            }
            if (detail.getBoxId() != null && detail.getDifferenceQuantity().compareTo(BigDecimal.ZERO) > 0 && detail.getItemInstanceId() == null) {
                boxService.moveTo(detail.getBoxId(), detail.getWarehouseId(), detail.getAreaId(), detail.getRackId(), detail.getLocationId());
            }
        }
    }
}
