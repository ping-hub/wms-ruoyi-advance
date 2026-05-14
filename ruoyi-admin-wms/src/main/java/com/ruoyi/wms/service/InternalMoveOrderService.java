package com.ruoyi.wms.service;

import cn.hutool.core.collection.CollUtil;
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
import com.ruoyi.wms.domain.bo.InternalMoveOrderBo;
import com.ruoyi.wms.domain.bo.InternalMoveOrderDetailBo;
import com.ruoyi.wms.domain.bo.InventoryBo;
import com.ruoyi.wms.domain.bo.InventoryDetailBo;
import com.ruoyi.wms.domain.entity.Box;
import com.ruoyi.wms.domain.entity.InternalMoveOrder;
import com.ruoyi.wms.domain.entity.InternalMoveOrderDetail;
import com.ruoyi.wms.domain.entity.InventoryDetail;
import com.ruoyi.wms.domain.entity.InventoryHistory;
import com.ruoyi.wms.domain.entity.ItemInstance;
import com.ruoyi.wms.domain.vo.InternalMoveOrderVo;
import com.ruoyi.wms.domain.vo.MovementOrderVo;
import com.ruoyi.wms.domain.vo.InventoryDetailVo;
import com.ruoyi.wms.mapper.InternalMoveOrderMapper;
import com.ruoyi.wms.mapper.InventoryDetailMapper;
import jakarta.validation.constraints.NotEmpty;
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
import java.util.Set;

/**
 * 库内移库单 Service 业务层处理
 */
@RequiredArgsConstructor
@Service
public class InternalMoveOrderService {

    private final InternalMoveOrderMapper internalMoveOrderMapper;
    private final InternalMoveOrderDetailService internalMoveOrderDetailService;
    private final InventoryService inventoryService;
    private final InventoryDetailService inventoryDetailService;
    private final InventoryDetailMapper inventoryDetailMapper;
    private final InventoryHistoryService inventoryHistoryService;
    private final ItemInstanceService itemInstanceService;
    private final BoxService boxService;

    public InternalMoveOrderVo queryById(Long id) {
        InternalMoveOrderVo internalMoveOrderVo = internalMoveOrderMapper.selectVoById(id);
        if (internalMoveOrderVo == null) {
            throw new BaseException("库内移库单不存在");
        }
        internalMoveOrderVo.setDetails(internalMoveOrderDetailService.queryByInternalMoveOrderId(id));
        return internalMoveOrderVo;
    }

    public TableDataInfo<InternalMoveOrderVo> queryPageList(InternalMoveOrderBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<InternalMoveOrder> lqw = buildQueryWrapper(bo);
        Page<InternalMoveOrderVo> result = internalMoveOrderMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    public List<InternalMoveOrderVo> queryList(InternalMoveOrderBo bo) {
        LambdaQueryWrapper<InternalMoveOrder> lqw = buildQueryWrapper(bo);
        return internalMoveOrderMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<InternalMoveOrder> buildQueryWrapper(InternalMoveOrderBo bo) {
        LambdaQueryWrapper<InternalMoveOrder> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getInternalMoveOrderNo()), InternalMoveOrder::getInternalMoveOrderNo, bo.getInternalMoveOrderNo());
        lqw.eq(bo.getSourceWarehouseId() != null, InternalMoveOrder::getSourceWarehouseId, bo.getSourceWarehouseId());
        lqw.eq(bo.getSourceAreaId() != null, InternalMoveOrder::getSourceAreaId, bo.getSourceAreaId());
        lqw.eq(bo.getSourceRackId() != null, InternalMoveOrder::getSourceRackId, bo.getSourceRackId());
        lqw.eq(bo.getSourceLocationId() != null, InternalMoveOrder::getSourceLocationId, bo.getSourceLocationId());
        lqw.eq(bo.getTargetWarehouseId() != null, InternalMoveOrder::getTargetWarehouseId, bo.getTargetWarehouseId());
        lqw.eq(bo.getTargetAreaId() != null, InternalMoveOrder::getTargetAreaId, bo.getTargetAreaId());
        lqw.eq(bo.getTargetRackId() != null, InternalMoveOrder::getTargetRackId, bo.getTargetRackId());
        lqw.eq(bo.getTargetLocationId() != null, InternalMoveOrder::getTargetLocationId, bo.getTargetLocationId());
        lqw.eq(bo.getInternalMoveStatus() != null, InternalMoveOrder::getInternalMoveStatus, bo.getInternalMoveStatus());
        lqw.orderByDesc(BaseEntity::getCreateTime);
        return lqw;
    }

    @Transactional
    public void insertByBo(InternalMoveOrderBo bo) {
        validateInternalMoveOrderNo(bo.getInternalMoveOrderNo());
        fillHeaderLocationByDetails(bo);
        InternalMoveOrder add = MapstructUtils.convert(bo, InternalMoveOrder.class);
        internalMoveOrderMapper.insert(add);
        bo.setId(add.getId());
        List<InternalMoveOrderDetail> addDetailList = MapstructUtils.convert(bo.getDetails(), InternalMoveOrderDetail.class);
        addDetailList.forEach(it -> it.setInternalMoveOrderId(add.getId()));
        internalMoveOrderDetailService.saveDetails(addDetailList);
    }

    private void validateInternalMoveOrderNo(String internalMoveOrderNo) {
        LambdaQueryWrapper<InternalMoveOrder> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(InternalMoveOrder::getInternalMoveOrderNo, internalMoveOrderNo);
        if (internalMoveOrderMapper.exists(wrapper)) {
            throw new BaseException("库内移库单号重复，请手动修改");
        }
    }

    @Transactional
    public void updateByBo(InternalMoveOrderBo bo) {
        fillHeaderLocationByDetails(bo);
        InternalMoveOrder update = MapstructUtils.convert(bo, InternalMoveOrder.class);
        internalMoveOrderMapper.updateById(update);
        List<InternalMoveOrderDetail> detailList = MapstructUtils.convert(bo.getDetails(), InternalMoveOrderDetail.class);
        detailList.forEach(it -> it.setInternalMoveOrderId(bo.getId()));
        internalMoveOrderDetailService.saveDetails(detailList);
    }

    public void deleteById(Long id) {
        validateIdBeforeDelete(id);
        internalMoveOrderMapper.deleteById(id);
    }

    private void validateIdBeforeDelete(Long id) {
        InternalMoveOrderVo internalMoveOrderVo = queryById(id);
        if (internalMoveOrderVo == null) {
            throw new BaseException("库内移库单不存在");
        }
        if (ServiceConstants.InternalMoveOrderStatus.FINISH.equals(internalMoveOrderVo.getInternalMoveStatus())) {
            throw new ServiceException("库内移库单【" + internalMoveOrderVo.getInternalMoveOrderNo() + "】已执行，无法删除！");
        }
    }

    public void deleteByIds(Collection<Long> ids) {
        internalMoveOrderMapper.deleteBatchIds(ids);
    }

    @Transactional
    public void move(InternalMoveOrderBo bo) {
        validateBeforeMove(bo);
        List<InventoryDetailBo> inventoryDetailBoList = convertDetailToInventoryDetail(bo.getDetails());
        inventoryDetailService.validateRemainQuantity(inventoryDetailBoList);
        if (Objects.isNull(bo.getId())) {
            insertByBo(bo);
        } else {
            updateByBo(bo);
        }

        List<InventoryBo> mergedShipmentInventoryList = mergeShipmentDetailByPlaceAndItem(bo.getDetails());
        List<InventoryBo> mergedReceiptInventoryList = mergeReceiptDetailByPlaceAndItem(bo.getDetails());
        mergedShipmentInventoryList.forEach(it -> it.setQuantity(it.getQuantity().negate()));
        inventoryService.updateInventoryQuantity(mergedShipmentInventoryList);
        inventoryService.updateInventoryQuantity(mergedReceiptInventoryList);

        inventoryDetailMapper.deductInventoryDetailQuantity(inventoryDetailBoList, LoginHelper.getUsername(), LocalDateTime.now());
        addInventoryDetail(bo);
        createInventoryHistory(bo);
        syncMovedObjects(bo);
    }

    private void validateBeforeMove(InternalMoveOrderBo bo) {
        if (CollUtil.isEmpty(bo.getDetails())) {
            throw new BaseException("库内移库明细不能为空");
        }
        if (!Objects.equals(bo.getSourceWarehouseId(), bo.getTargetWarehouseId())) {
            throw new BaseException("库内移库仅允许同仓移库");
        }
        Map<Long, InventoryDetailVo> inventoryDetailMap = inventoryDetailMapper.selectVoBatchIds(
            bo.getDetails().stream().map(InternalMoveOrderDetailBo::getInventoryDetailId).toList()
        ).stream().collect(java.util.stream.Collectors.toMap(InventoryDetailVo::getId, java.util.function.Function.identity()));

        for (InternalMoveOrderDetailBo detail : bo.getDetails()) {
            if (!Objects.equals(detail.getSourceWarehouseId(), detail.getTargetWarehouseId())) {
                throw new BaseException("库内移库明细仅允许同仓移库");
            }
            if (detail.getQuantity() == null || detail.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
                throw new BaseException("库内移库数量必须大于0");
            }
            if (samePosition(detail.getSourceWarehouseId(), detail.getSourceAreaId(), detail.getSourceRackId(), detail.getSourceLocationId(),
                detail.getTargetWarehouseId(), detail.getTargetAreaId(), detail.getTargetRackId(), detail.getTargetLocationId())) {
                throw new BaseException("源位置与目标位置不能完全一致");
            }
            if (detail.getItemInstanceId() != null && detail.getBoxId() != null) {
                throw new BaseException("库内移库明细不能同时选择单品实例和箱体");
            }

            InventoryDetailVo inventoryDetail = inventoryDetailMap.get(detail.getInventoryDetailId());
            if (inventoryDetail == null) {
                throw new BaseException("存在不存在的库存明细");
            }
            if (!Objects.equals(inventoryDetail.getSkuId(), detail.getSkuId())) {
                throw new BaseException("库存明细与规格不匹配");
            }
            if (!samePosition(inventoryDetail.getWarehouseId(), inventoryDetail.getAreaId(), inventoryDetail.getRackId(), inventoryDetail.getLocationId(),
                detail.getSourceWarehouseId(), detail.getSourceAreaId(), detail.getSourceRackId(), detail.getSourceLocationId())) {
                throw new BaseException("库存明细当前位置与源位置不一致");
            }

            validateItemInstance(detail);
            validateBox(detail);
        }
    }

    private void validateItemInstance(InternalMoveOrderDetailBo detail) {
        if (detail.getItemInstanceId() == null) {
            return;
        }
        ItemInstance itemInstance = itemInstanceService.getById(detail.getItemInstanceId());
        if (itemInstance == null) {
            throw new BaseException("存在不存在的单品实例");
        }
        if (!Objects.equals(itemInstance.getSkuId(), detail.getSkuId())) {
            throw new BaseException("单品实例与移库规格不匹配");
        }
        if (Integer.valueOf(1).equals(itemInstance.getBorrowed())) {
            throw new BaseException("已借出单品不能移库");
        }
        if (ServiceConstants.ItemInstanceStatus.OUTBOUND.equals(itemInstance.getInstanceStatus())) {
            throw new BaseException("已出库单品不能移库");
        }
        if (Integer.valueOf(1).equals(itemInstance.getInBox())) {
            throw new BaseException("箱内单品请按箱体整箱移库");
        }
        if (!samePosition(itemInstance.getWarehouseId(), itemInstance.getAreaId(), itemInstance.getRackId(), itemInstance.getLocationId(),
            detail.getSourceWarehouseId(), detail.getSourceAreaId(), detail.getSourceRackId(), detail.getSourceLocationId())) {
            throw new BaseException("单品实例当前位置与源位置不一致");
        }
    }

    private void validateBox(InternalMoveOrderDetailBo detail) {
        if (detail.getBoxId() == null) {
            return;
        }
        Box box = boxService.getById(detail.getBoxId());
        if (box == null) {
            throw new BaseException("存在不存在的箱体");
        }
        if (ServiceConstants.BoxStatus.OUTBOUND.equals(box.getBoxStatus())) {
            throw new BaseException("已出库箱体不能移库");
        }
        if (!samePosition(box.getWarehouseId(), box.getAreaId(), box.getRackId(), box.getLocationId(),
            detail.getSourceWarehouseId(), detail.getSourceAreaId(), detail.getSourceRackId(), detail.getSourceLocationId())) {
            throw new BaseException("箱体当前位置与源位置不一致");
        }
        Set<Long> itemIds = boxService.queryItemIdsByBoxId(detail.getBoxId());
        if (CollUtil.isEmpty(itemIds)) {
            return;
        }
        List<ItemInstance> items = itemInstanceService.queryByIds(itemIds);
        for (ItemInstance item : items) {
            if (Integer.valueOf(1).equals(item.getBorrowed())) {
                throw new BaseException("箱内存在已借出单品，不能移库");
            }
            if (ServiceConstants.ItemInstanceStatus.OUTBOUND.equals(item.getInstanceStatus())) {
                throw new BaseException("箱内存在已出库单品，不能移库");
            }
        }
    }

    public List<InventoryBo> mergeShipmentDetailByPlaceAndItem(@NotEmpty List<InternalMoveOrderDetailBo> details) {
        Map<String, InventoryBo> mergedMap = new HashMap<>();
        details.forEach(detail -> {
            String key = detail.getSourceWarehouseId() + "_" + detail.getSourceAreaId()
                + "_" + detail.getSourceRackId() + "_" + detail.getSourceLocationId() + "_" + detail.getSkuId();
            InventoryBo inventoryBo = mergedMap.computeIfAbsent(key, it -> {
                InventoryBo inventory = new InventoryBo();
                inventory.setWarehouseId(detail.getSourceWarehouseId());
                inventory.setAreaId(detail.getSourceAreaId());
                inventory.setRackId(detail.getSourceRackId());
                inventory.setLocationId(detail.getSourceLocationId());
                inventory.setSkuId(detail.getSkuId());
                inventory.setQuantity(BigDecimal.ZERO);
                return inventory;
            });
            inventoryBo.setQuantity(inventoryBo.getQuantity().add(detail.getQuantity()));
        });
        return new ArrayList<>(mergedMap.values());
    }

    public List<InventoryBo> mergeReceiptDetailByPlaceAndItem(@NotEmpty List<InternalMoveOrderDetailBo> details) {
        Map<String, InventoryBo> mergedMap = new HashMap<>();
        details.forEach(detail -> {
            String key = detail.getTargetWarehouseId() + "_" + detail.getTargetAreaId()
                + "_" + detail.getTargetRackId() + "_" + detail.getTargetLocationId() + "_" + detail.getSkuId();
            InventoryBo inventoryBo = mergedMap.computeIfAbsent(key, it -> {
                InventoryBo inventory = new InventoryBo();
                inventory.setWarehouseId(detail.getTargetWarehouseId());
                inventory.setAreaId(detail.getTargetAreaId());
                inventory.setRackId(detail.getTargetRackId());
                inventory.setLocationId(detail.getTargetLocationId());
                inventory.setSkuId(detail.getSkuId());
                inventory.setQuantity(BigDecimal.ZERO);
                return inventory;
            });
            inventoryBo.setQuantity(inventoryBo.getQuantity().add(detail.getQuantity()));
        });
        return new ArrayList<>(mergedMap.values());
    }

    public List<InventoryDetailBo> convertDetailToInventoryDetail(List<InternalMoveOrderDetailBo> details) {
        return details.stream().map(detail -> {
            InventoryDetailBo inventoryDetailBo = new InventoryDetailBo();
            inventoryDetailBo.setId(detail.getInventoryDetailId());
            inventoryDetailBo.setShipmentQuantity(detail.getQuantity());
            return inventoryDetailBo;
        }).toList();
    }

    @Transactional
    public void addInventoryDetail(InternalMoveOrderBo bo) {
        List<InventoryDetail> addInventoryDetailList = bo.getDetails().stream().map(detail -> {
            InventoryDetail addInventoryDetail = new InventoryDetail();
            addInventoryDetail.setReceiptOrderId(bo.getId());
            addInventoryDetail.setOrderNo(bo.getInternalMoveOrderNo());
            addInventoryDetail.setType(ServiceConstants.InventoryDetailType.INTERNAL_MOVE);
            addInventoryDetail.setSkuId(detail.getSkuId());
            addInventoryDetail.setWarehouseId(detail.getTargetWarehouseId());
            addInventoryDetail.setAreaId(detail.getTargetAreaId());
            addInventoryDetail.setRackId(detail.getTargetRackId());
            addInventoryDetail.setLocationId(detail.getTargetLocationId());
            addInventoryDetail.setItemInstanceId(detail.getItemInstanceId());
            addInventoryDetail.setBoxId(detail.getBoxId());
            addInventoryDetail.setQuantity(detail.getQuantity());
            addInventoryDetail.setProductionDate(detail.getProductionDate());
            addInventoryDetail.setExpirationDate(detail.getExpirationDate());
            addInventoryDetail.setEquipmentCode(detail.getEquipmentCode());
            addInventoryDetail.setSpecModel(detail.getSpecModel());
            addInventoryDetail.setProductMark(detail.getProductMark());
            addInventoryDetail.setQualityGrade(detail.getQualityGrade());
            addInventoryDetail.setUnitPrice(detail.getUnitPrice());
            addInventoryDetail.setLineAmount(detail.getLineAmount());
            addInventoryDetail.setRemainQuantity(detail.getQuantity());
            addInventoryDetail.setSourceOrderType("internal_move");
            addInventoryDetail.setSourceOrderId(bo.getId());
            return addInventoryDetail;
        }).toList();
        inventoryDetailService.saveBatch(addInventoryDetailList);
    }

    @Transactional
    public void createInventoryHistory(InternalMoveOrderBo bo) {
        List<InventoryHistory> addInventoryHistoryList = new LinkedList<>();
        bo.getDetails().forEach(detail -> {
            InventoryHistory shipmentInventoryHistory = new InventoryHistory();
            shipmentInventoryHistory.setWarehouseId(detail.getSourceWarehouseId());
            shipmentInventoryHistory.setAreaId(detail.getSourceAreaId());
            shipmentInventoryHistory.setRackId(detail.getSourceRackId());
            shipmentInventoryHistory.setLocationId(detail.getSourceLocationId());
            shipmentInventoryHistory.setItemInstanceId(detail.getItemInstanceId());
            shipmentInventoryHistory.setBoxId(detail.getBoxId());
            shipmentInventoryHistory.setSkuId(detail.getSkuId());
            shipmentInventoryHistory.setQuantity(detail.getQuantity().negate());
            shipmentInventoryHistory.setProductionDate(detail.getProductionDate());
            shipmentInventoryHistory.setExpirationDate(detail.getExpirationDate());
            shipmentInventoryHistory.setOrderId(bo.getId());
            shipmentInventoryHistory.setOrderNo(bo.getInternalMoveOrderNo());
            shipmentInventoryHistory.setOrderType(ServiceConstants.InventoryHistoryOrderType.INTERNAL_MOVE);
            shipmentInventoryHistory.setEquipmentCode(detail.getEquipmentCode());
            shipmentInventoryHistory.setSpecModel(detail.getSpecModel());
            shipmentInventoryHistory.setProductMark(detail.getProductMark());
            shipmentInventoryHistory.setQualityGrade(detail.getQualityGrade());
            shipmentInventoryHistory.setUnitPrice(detail.getUnitPrice());
            shipmentInventoryHistory.setLineAmount(detail.getLineAmount());
            addInventoryHistoryList.add(shipmentInventoryHistory);

            InventoryHistory receiptInventoryHistory = new InventoryHistory();
            receiptInventoryHistory.setWarehouseId(detail.getTargetWarehouseId());
            receiptInventoryHistory.setAreaId(detail.getTargetAreaId());
            receiptInventoryHistory.setRackId(detail.getTargetRackId());
            receiptInventoryHistory.setLocationId(detail.getTargetLocationId());
            receiptInventoryHistory.setItemInstanceId(detail.getItemInstanceId());
            receiptInventoryHistory.setBoxId(detail.getBoxId());
            receiptInventoryHistory.setSkuId(detail.getSkuId());
            receiptInventoryHistory.setQuantity(detail.getQuantity());
            receiptInventoryHistory.setProductionDate(detail.getProductionDate());
            receiptInventoryHistory.setExpirationDate(detail.getExpirationDate());
            receiptInventoryHistory.setOrderId(bo.getId());
            receiptInventoryHistory.setOrderNo(bo.getInternalMoveOrderNo());
            receiptInventoryHistory.setOrderType(ServiceConstants.InventoryHistoryOrderType.INTERNAL_MOVE);
            receiptInventoryHistory.setEquipmentCode(detail.getEquipmentCode());
            receiptInventoryHistory.setSpecModel(detail.getSpecModel());
            receiptInventoryHistory.setProductMark(detail.getProductMark());
            receiptInventoryHistory.setQualityGrade(detail.getQualityGrade());
            receiptInventoryHistory.setUnitPrice(detail.getUnitPrice());
            receiptInventoryHistory.setLineAmount(detail.getLineAmount());
            addInventoryHistoryList.add(receiptInventoryHistory);
        });
        inventoryHistoryService.saveBatch(addInventoryHistoryList);
    }

    private void fillHeaderLocationByDetails(InternalMoveOrderBo bo) {
        if (CollUtil.isEmpty(bo.getDetails())) {
            return;
        }
        InternalMoveOrderDetailBo firstDetail = bo.getDetails().get(0);
        boolean sameSource = bo.getDetails().stream().allMatch(detail ->
            Objects.equals(detail.getSourceAreaId(), firstDetail.getSourceAreaId())
                && Objects.equals(detail.getSourceRackId(), firstDetail.getSourceRackId())
                && Objects.equals(detail.getSourceLocationId(), firstDetail.getSourceLocationId())
        );
        boolean sameTarget = bo.getDetails().stream().allMatch(detail ->
            Objects.equals(detail.getTargetAreaId(), firstDetail.getTargetAreaId())
                && Objects.equals(detail.getTargetRackId(), firstDetail.getTargetRackId())
                && Objects.equals(detail.getTargetLocationId(), firstDetail.getTargetLocationId())
        );
        if (bo.getSourceAreaId() == null && sameSource) {
            bo.setSourceAreaId(firstDetail.getSourceAreaId());
        }
        if (bo.getSourceRackId() == null && sameSource) {
            bo.setSourceRackId(firstDetail.getSourceRackId());
        }
        if (bo.getSourceLocationId() == null && sameSource) {
            bo.setSourceLocationId(firstDetail.getSourceLocationId());
        }
        if (bo.getTargetAreaId() == null && sameTarget) {
            bo.setTargetAreaId(firstDetail.getTargetAreaId());
        }
        if (bo.getTargetRackId() == null && sameTarget) {
            bo.setTargetRackId(firstDetail.getTargetRackId());
        }
        if (bo.getTargetLocationId() == null && sameTarget) {
            bo.setTargetLocationId(firstDetail.getTargetLocationId());
        }
    }

    private void syncMovedObjects(InternalMoveOrderBo bo) {
        for (InternalMoveOrderDetailBo detail : bo.getDetails()) {
            if (detail.getItemInstanceId() != null) {
                itemInstanceService.moveTo(detail.getItemInstanceId(), detail.getTargetWarehouseId(), detail.getTargetAreaId(), detail.getTargetRackId(), detail.getTargetLocationId());
            }
            if (detail.getBoxId() != null) {
                boxService.moveTo(detail.getBoxId(), detail.getTargetWarehouseId(), detail.getTargetAreaId(), detail.getTargetRackId(), detail.getTargetLocationId());
                Box box = boxService.getById(detail.getBoxId());
                Set<Long> itemIds = boxService.queryItemIdsByBoxId(detail.getBoxId());
                for (Long itemId : itemIds) {
                    itemInstanceService.moveTo(itemId, detail.getTargetWarehouseId(), detail.getTargetAreaId(), detail.getTargetRackId(), detail.getTargetLocationId());
                    itemInstanceService.markInBox(itemId, box);
                }
            }
        }
    }

    private boolean samePosition(Long warehouseId, Long areaId, Long rackId, Long locationId,
                                 Long targetWarehouseId, Long targetAreaId, Long targetRackId, Long targetLocationId) {
        return Objects.equals(warehouseId, targetWarehouseId)
            && Objects.equals(areaId, targetAreaId)
            && Objects.equals(rackId, targetRackId)
            && Objects.equals(locationId, targetLocationId);
    }
}
