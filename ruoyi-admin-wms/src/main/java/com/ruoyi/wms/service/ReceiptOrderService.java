package com.ruoyi.wms.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
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
import com.ruoyi.system.service.SysDictTypeService;
import com.ruoyi.wms.domain.bo.InventoryBo;
import com.ruoyi.wms.domain.bo.ReceiptOrderBo;
import com.ruoyi.wms.domain.bo.ReceiptOrderDetailBo;
import com.ruoyi.wms.domain.entity.InventoryDetail;
import com.ruoyi.wms.domain.entity.InventoryHistory;
import com.ruoyi.wms.domain.entity.ReceiptOrder;
import com.ruoyi.wms.domain.entity.ReceiptOrderDetail;
import com.ruoyi.wms.domain.vo.ReceiptOrderVo;
import com.ruoyi.wms.mapper.ReceiptOrderDetailMapper;
import com.ruoyi.wms.mapper.ReceiptOrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;

/**
 * 入库单Service业务层处理
 *
 * @author zcc
 * @date 2024-07-19
 */
@RequiredArgsConstructor
@Service
public class ReceiptOrderService {

    private final ReceiptOrderMapper receiptOrderMapper;
    private final ReceiptOrderDetailService receiptOrderDetailService;
    private final ReceiptOrderDetailMapper receiptOrderDetailMapper;
    private final InventoryService inventoryService;
    private final InventoryDetailService inventoryDetailService;
    private final InventoryHistoryService inventoryHistoryService;
    private final SysDictTypeService dictTypeService;
    private final ItemInstanceService itemInstanceService;

    /**
     * 查询入库单
     */
    public ReceiptOrderVo queryById(Long id){
        ReceiptOrderVo receiptOrderVo = receiptOrderMapper.selectVoById(id);
        Assert.notNull(receiptOrderVo, "入库单不存在");
        receiptOrderVo.setDetails(receiptOrderDetailService.queryByReceiptOrderId(id));
        return receiptOrderVo;
    }

    /**
     * 查询入库单列表
     */
    public TableDataInfo<ReceiptOrderVo> queryPageList(ReceiptOrderBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ReceiptOrder> lqw = buildQueryWrapper(bo);
        Page<ReceiptOrderVo> result = receiptOrderMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询入库单列表
     */
    public List<ReceiptOrderVo> queryList(ReceiptOrderBo bo) {
        LambdaQueryWrapper<ReceiptOrder> lqw = buildQueryWrapper(bo);
        return receiptOrderMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<ReceiptOrder> buildQueryWrapper(ReceiptOrderBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<ReceiptOrder> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getReceiptOrderNo()), ReceiptOrder::getReceiptOrderNo, bo.getReceiptOrderNo());
        lqw.eq(bo.getReceiptOrderType() != null, ReceiptOrder::getReceiptOrderType, bo.getReceiptOrderType());
        lqw.eq(bo.getMerchantId() != null, ReceiptOrder::getMerchantId, bo.getMerchantId());
        lqw.eq(StringUtils.isNotBlank(bo.getOrderNo()), ReceiptOrder::getOrderNo, bo.getOrderNo());
        lqw.eq(bo.getPayableAmount() != null, ReceiptOrder::getPayableAmount, bo.getPayableAmount());
        lqw.eq(bo.getReceiptOrderStatus() != null, ReceiptOrder::getReceiptOrderStatus, bo.getReceiptOrderStatus());
        lqw.orderByDesc(BaseEntity::getCreateTime);
        return lqw;
    }

    /**
     * 暂存入库单
     */
    @Transactional
    public void insertByBo(ReceiptOrderBo bo) {
        // 校验入库单号唯一性
        validateReceiptOrderNo(bo.getReceiptOrderNo());
        // 创建入库单
        ReceiptOrder add = MapstructUtils.convert(bo, ReceiptOrder.class);
        receiptOrderMapper.insert(add);
        bo.setId(add.getId());
        List<ReceiptOrderDetailBo> detailBoList = bo.getDetails();
        List<ReceiptOrderDetail> addDetailList = MapstructUtils.convert(detailBoList, ReceiptOrderDetail.class);
        addDetailList.forEach(it -> {
            it.setReceiptOrderId(add.getId());
        });
        // 创建入库单明细
        receiptOrderDetailService.saveDetails(addDetailList);
    }

    /**
     * 入库：
     * 1.校验
     * 2.保存入库单和入库单明细
     * 3.保存库存明细
     * 4.增加库存
     * 5.保存库存记录
     */
    @Transactional
    public void receive(ReceiptOrderBo bo) {
        // 1. 校验
        validateBeforeReceive(bo);

        // 2. 保存入库单和入库单明细
        if (Objects.isNull(bo.getId())) {
            insertByBo(bo);
        } else {
            updateByBo(bo);
        }

        // 3.保存库存明细
        this.saveInventoryDetails(bo);

        // 4.增加库存
        List<InventoryBo> inventoryList = convertInventoryList(bo.getDetails());
        inventoryService.updateInventoryQuantity(inventoryList);

        // 5.保存库存记录
        this.saveInventoryHistory(bo);

        // 6.按明细生成单品实例
        itemInstanceService.generateByReceiptOrder(receiptOrderMapper.selectById(bo.getId()),
            receiptOrderDetailService.queryEntitiesByReceiptOrderId(bo.getId()),
            bo.getReceiveUnit());
    }

    private void validateBeforeReceive(ReceiptOrderBo bo) {
        if (CollUtil.isEmpty(bo.getDetails())) {
            throw new BaseException("商品明细不能为空");
        }
        if (bo.getId() != null) {
            ReceiptOrder receiptOrder = receiptOrderMapper.selectById(bo.getId());
            Assert.notNull(receiptOrder, "入库单不存在");
            Assert.isFalse(ServiceConstants.ReceiptOrderStatus.FINISH.equals(receiptOrder.getReceiptOrderStatus()), "入库单已完成入库");
            Assert.isTrue(itemInstanceService.countByReceiptOrderId(bo.getId()) == 0, "入库单已生成单品实例，请勿重复入库");
        }
    }

    private void saveInventoryHistory(ReceiptOrderBo bo){
        List<InventoryHistory> inventoryHistoryList = new LinkedList<>();
        bo.getDetails().forEach(detail -> {
            InventoryHistory inventoryHistory = new InventoryHistory();
            inventoryHistory.setOrderId(bo.getId());
            inventoryHistory.setOrderNo(bo.getReceiptOrderNo());
            inventoryHistory.setOrderType(ServiceConstants.InventoryHistoryOrderType.RECEIPT);
            inventoryHistory.setSkuId(detail.getSkuId());
            inventoryHistory.setQuantity(detail.getQuantity());
            inventoryHistory.setWarehouseId(detail.getWarehouseId());
            inventoryHistory.setAreaId(detail.getAreaId());
            inventoryHistory.setRackId(detail.getRackId());
            inventoryHistory.setLocationId(detail.getLocationId());
            inventoryHistory.setBatchNo(detail.getBatchNo());
            inventoryHistory.setProductionDate(detail.getProductionDate());
            inventoryHistory.setExpirationDate(detail.getExpirationDate());
            inventoryHistory.setAmount(detail.getAmount());
            inventoryHistory.setEquipmentCode(detail.getEquipmentCode());
            inventoryHistory.setSpecModel(detail.getSpecModel());
            inventoryHistory.setProductMark(detail.getProductMark());
            inventoryHistory.setQualityGrade(detail.getQualityGrade());
            inventoryHistory.setUnitPrice(detail.getUnitPrice());
            inventoryHistory.setLineAmount(detail.getLineAmount());
            inventoryHistory.setBelongUnit(bo.getReceiveUnit());
            inventoryHistoryList.add(inventoryHistory);
        });
        inventoryHistoryService.saveBatch(inventoryHistoryList);
    }

    private void saveInventoryDetails(ReceiptOrderBo bo){

        List<InventoryDetail> inventoryDetailList = MapstructUtils.convert(bo.getDetails(), InventoryDetail.class);

        inventoryDetailList.forEach(inventoryDetail -> {
            inventoryDetail.setReceiptOrderId(bo.getId());
            inventoryDetail.setOrderNo(bo.getOrderNo());
            inventoryDetail.setType(ServiceConstants.InventoryDetailType.RECEIPT);
            inventoryDetail.setRemainQuantity(inventoryDetail.getQuantity());
            ReceiptOrderDetailBo detail = bo.getDetails().stream()
                .filter(it -> Objects.equals(it.getSkuId(), inventoryDetail.getSkuId())
                    && Objects.equals(it.getWarehouseId(), inventoryDetail.getWarehouseId())
                    && Objects.equals(it.getAreaId(), inventoryDetail.getAreaId())
                    && Objects.equals(it.getRackId(), inventoryDetail.getRackId())
                    && Objects.equals(it.getLocationId(), inventoryDetail.getLocationId())
                    && Objects.equals(it.getBatchNo(), inventoryDetail.getBatchNo()))
                .findFirst()
                .orElse(null);
            if (detail != null) {
                inventoryDetail.setRackId(detail.getRackId());
                inventoryDetail.setLocationId(detail.getLocationId());
                inventoryDetail.setEquipmentCode(detail.getEquipmentCode());
                inventoryDetail.setSpecModel(detail.getSpecModel());
                inventoryDetail.setProductMark(detail.getProductMark());
                inventoryDetail.setQualityGrade(detail.getQualityGrade());
                inventoryDetail.setUnitPrice(detail.getUnitPrice());
                inventoryDetail.setLineAmount(detail.getLineAmount());
                inventoryDetail.setBelongUnit(bo.getReceiveUnit());
            }
        });
        inventoryDetailService.saveBatch(inventoryDetailList);
    }

    /**
     * 合并入库单详情
     * 合并key：warehouseId_areaId_rackId_locationId_skuId
     * @param orderDetailBoList 明细
     * @return 合并后的库存变更
     */
    private List<InventoryBo> convertInventoryList(List<ReceiptOrderDetailBo> orderDetailBoList) {
        Function<ReceiptOrderDetailBo, String> keyFunction = it -> it.getWarehouseId() + "_" + it.getAreaId() + "_" + it.getRackId() + "_" + it.getLocationId() + "_" + it.getSkuId();
        Map<String, InventoryBo> inventoryMap = new HashMap<>();
        orderDetailBoList.forEach(orderDetailBo -> {
            String key = keyFunction.apply(orderDetailBo);
            if (inventoryMap.containsKey(key)) {
                InventoryBo mergedItem = inventoryMap.get(key);
                mergedItem.setQuantity(mergedItem.getQuantity().add(orderDetailBo.getQuantity()));
            } else {
                InventoryBo inventory = new InventoryBo();
                inventory.setSkuId(orderDetailBo.getSkuId());
                inventory.setWarehouseId(orderDetailBo.getWarehouseId());
                inventory.setAreaId(orderDetailBo.getAreaId());
                inventory.setRackId(orderDetailBo.getRackId());
                inventory.setLocationId(orderDetailBo.getLocationId());
                inventory.setQuantity(orderDetailBo.getQuantity());
                inventoryMap.put(key, inventory);
            }
        });
        return new ArrayList<>(inventoryMap.values());
    }

    /**
     * 修改入库单
     */
    @Transactional
    public void updateByBo(ReceiptOrderBo bo) {
        // 更新入库单
        ReceiptOrder update = MapstructUtils.convert(bo, ReceiptOrder.class);
        receiptOrderMapper.updateById(update);
        // 保存入库单明细
        List<Long> incomingIds = bo.getDetails().stream()
            .map(ReceiptOrderDetailBo::getId)
            .filter(Objects::nonNull)
            .toList();
        List<Long> existedIds = receiptOrderDetailService.queryEntitiesByReceiptOrderId(bo.getId()).stream()
            .map(ReceiptOrderDetail::getId)
            .filter(Objects::nonNull)
            .toList();
        List<Long> deleteIds = existedIds.stream()
            .filter(id -> !incomingIds.contains(id))
            .toList();
        if (CollUtil.isNotEmpty(deleteIds)) {
            receiptOrderDetailService.deleteByIds(deleteIds);
        }
        List<ReceiptOrderDetail> detailList = MapstructUtils.convert(bo.getDetails(), ReceiptOrderDetail.class);
        detailList.forEach(it -> it.setReceiptOrderId(bo.getId()));
        receiptOrderDetailService.saveDetails(detailList);
    }

    /**
     * 入库单作废
     * @param id
     */
    public void editToInvalid(Long id) {
        LambdaUpdateWrapper<ReceiptOrder> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(ReceiptOrder::getId, id);
        wrapper.set(ReceiptOrder::getReceiptOrderStatus, ServiceConstants.ReceiptOrderStatus.INVALID);
        receiptOrderMapper.update(null, wrapper);
    }

    /**
     * 删除入库单
     */
    public void deleteById(Long id) {
        validateIdBeforeDelete(id);
        receiptOrderMapper.deleteById(id);
    }

    private void validateIdBeforeDelete(Long id) {
        ReceiptOrderVo receiptOrderVo = queryById(id);
        Assert.notNull(receiptOrderVo, "入库单不存在");
        if (ServiceConstants.ReceiptOrderStatus.FINISH.equals(receiptOrderVo.getReceiptOrderStatus())) {
            throw new ServiceException("入库单【" + receiptOrderVo.getReceiptOrderNo() + "】已入库，无法删除！", HttpStatus.CONFLICT.value());
        }
    }

    /**
     * 批量删除入库单
     */
    public void deleteByIds(Collection<Long> ids) {
        receiptOrderMapper.deleteBatchIds(ids);
    }

    public void validateReceiptOrderNo(String receiptOrderNo) {
        LambdaQueryWrapper<ReceiptOrder> receiptOrderLqw = Wrappers.lambdaQuery();
        receiptOrderLqw.eq(ReceiptOrder::getReceiptOrderNo, receiptOrderNo);
        ReceiptOrder receiptOrder = receiptOrderMapper.selectOne(receiptOrderLqw);
        Assert.isNull(receiptOrder, "入库单号重复，请手动修改");
    }
}
