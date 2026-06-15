package com.ruoyi.wms.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
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
import com.ruoyi.wms.domain.bo.*;
import com.ruoyi.wms.domain.entity.InventoryDetail;
import com.ruoyi.wms.domain.entity.InventoryHistory;
import com.ruoyi.wms.domain.entity.ItemInstance;
import com.ruoyi.wms.domain.entity.ShipmentOrder;
import com.ruoyi.wms.domain.entity.ShipmentOrderDetail;
import com.ruoyi.wms.domain.vo.ItemSkuVo;
import com.ruoyi.wms.domain.vo.ShipmentOrderDetailVo;
import com.ruoyi.wms.domain.vo.ShipmentOrderVo;
import com.ruoyi.wms.mapper.InventoryDetailMapper;
import com.ruoyi.wms.mapper.ShipmentOrderMapper;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 出库单Service业务层处理
 *
 * @author ping
 * @date 2024-08-01
 */
@RequiredArgsConstructor
@Service
public class ShipmentOrderService {

    @Value("${warehouse}")
    private String warehouse;

    private final ShipmentOrderMapper shipmentOrderMapper;
    private final ShipmentOrderDetailService shipmentOrderDetailService;
    private final InventoryService inventoryService;
    private final InventoryDetailMapper inventoryDetailMapper;
    private final InventoryHistoryService inventoryHistoryService;
    private final InventoryDetailService inventoryDetailService;
    private final ItemInstanceService itemInstanceService;
    private final ItemSkuService itemSkuService;
    private final LocationService locationService;

    /**
     * 查询出库单
     */
    public ShipmentOrderVo queryById(Long id){
        ShipmentOrderVo shipmentOrderVo = shipmentOrderMapper.selectVoById(id);
        if (shipmentOrderVo == null) {
            throw new BaseException("出库单不存在");
        }
        shipmentOrderVo.setDetails(shipmentOrderDetailService.queryByShipmentOrderId(shipmentOrderVo.getId()));
        return shipmentOrderVo;
    }

    /**
     * 查询出库单列表
     */
    public TableDataInfo<ShipmentOrderVo> queryPageList(ShipmentOrderBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ShipmentOrder> lqw = buildQueryWrapper(bo);
        Page<ShipmentOrderVo> result = shipmentOrderMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询出库单列表
     */
    public List<ShipmentOrderVo> queryList(ShipmentOrderBo bo) {
        LambdaQueryWrapper<ShipmentOrder> lqw = buildQueryWrapper(bo);
        return shipmentOrderMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<ShipmentOrder> buildQueryWrapper(ShipmentOrderBo bo) {
        LambdaQueryWrapper<ShipmentOrder> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getShipmentOrderNo()), ShipmentOrder::getShipmentOrderNo, bo.getShipmentOrderNo());
        lqw.eq(StringUtils.isNotBlank(bo.getShipmentOrderType()), ShipmentOrder::getShipmentOrderType, bo.getShipmentOrderType());
        lqw.like(StringUtils.isNotBlank(bo.getBasisNo()), ShipmentOrder::getBasisNo, bo.getBasisNo());
        lqw.eq(StringUtils.isNotBlank(bo.getDispatchMode()), ShipmentOrder::getDispatchMode, bo.getDispatchMode());
        lqw.like(StringUtils.isNotBlank(bo.getNoticeOrg()), ShipmentOrder::getNoticeOrg, bo.getNoticeOrg());
        lqw.like(StringUtils.isNotBlank(bo.getReceiveUnit()), ShipmentOrder::getReceiveUnit, bo.getReceiveUnit());
        lqw.eq(bo.getReceivableAmount() != null, ShipmentOrder::getReceivableAmount, bo.getReceivableAmount());
        lqw.eq(bo.getTotalQuantity() != null, ShipmentOrder::getTotalQuantity, bo.getTotalQuantity());
        lqw.eq(bo.getShipmentOrderStatus() != null, ShipmentOrder::getShipmentOrderStatus, bo.getShipmentOrderStatus());
        lqw.orderByDesc(BaseEntity::getCreateTime);
        return lqw;
    }

    /**
     * 暂存出库单
     */
    @Transactional
    public void insertByBo(ShipmentOrderBo bo) {
        normalizeShipmentDetails(bo.getDetails());
        bo.setShipmentOrderNo(StrUtil.blankToDefault(bo.getShipmentOrderNo(), generateShipmentOrderNo()));
        // 校验出库单号唯一性
        validateShipmentOrderNo(bo.getShipmentOrderNo());
        // 创建出库单
        ShipmentOrder add = MapstructUtils.convert(bo, ShipmentOrder.class);
        shipmentOrderMapper.insert(add);
        bo.setId(add.getId());
        List<ShipmentOrderDetailBo> detailBoList = bo.getDetails();
        List<ShipmentOrderDetail> addDetailList = MapstructUtils.convert(detailBoList, ShipmentOrderDetail.class);
        addDetailList.forEach(it -> it.setShipmentOrderId(add.getId()));
        shipmentOrderDetailService.saveDetails(addDetailList);
        for (int i = 0; i < Math.min(detailBoList.size(), addDetailList.size()); i++) {
            detailBoList.get(i).setId(addDetailList.get(i).getId());
        }
        if (CollUtil.isNotEmpty(detailBoList)) {
            itemInstanceService.reserveForShipmentDetails(detailBoList);
        }
    }

    public void validateShipmentOrderNo(String shipmentOrderNo) {
        LambdaQueryWrapper<ShipmentOrder> receiptOrderLqw = Wrappers.lambdaQuery();
        receiptOrderLqw.eq(ShipmentOrder::getShipmentOrderNo, shipmentOrderNo);
        ShipmentOrder shipmentOrder = shipmentOrderMapper.selectOne(receiptOrderLqw);
        Assert.isNull(shipmentOrder, "系统生成的出库单号重复，请稍后重试");
    }

    private String generateShipmentOrderNo() {
        return "CK" + warehouse + IdUtil.getSnowflakeNextIdStr();
    }


    /**
     * 修改出库单
     */
    @Transactional
    public void updateByBo(ShipmentOrderBo bo) {
        normalizeShipmentDetails(bo.getDetails());
        // 更新出库单
        ShipmentOrder update = MapstructUtils.convert(bo, ShipmentOrder.class);
        shipmentOrderMapper.updateById(update);
        // 保存出库单明细
        List<ShipmentOrderDetailVo> existedDetails = shipmentOrderDetailService.queryByShipmentOrderId(bo.getId());
        List<Long> incomingIds = bo.getDetails().stream()
            .map(ShipmentOrderDetailBo::getId)
            .filter(Objects::nonNull)
            .toList();
        List<Long> existedIds = existedDetails.stream()
            .map(ShipmentOrderDetailVo::getId)
            .filter(Objects::nonNull)
            .toList();
        List<Long> deleteIds = existedIds.stream()
            .filter(id -> !incomingIds.contains(id))
            .toList();
        if (CollUtil.isNotEmpty(deleteIds)) {
            shipmentOrderDetailService.deleteByIds(deleteIds);
        }
        List<ShipmentOrderDetail> detailList = MapstructUtils.convert(bo.getDetails(), ShipmentOrderDetail.class);
        detailList.forEach(it -> it.setShipmentOrderId(bo.getId()));
        shipmentOrderDetailService.saveDetails(detailList);
        for (int i = 0; i < Math.min(bo.getDetails().size(), detailList.size()); i++) {
            bo.getDetails().get(i).setId(detailList.get(i).getId());
        }
        itemInstanceService.releaseShipmentReservationsByDetailIds(existedIds);
        if (CollUtil.isNotEmpty(bo.getDetails())) {
            itemInstanceService.reserveForShipmentDetails(bo.getDetails());
        }
    }

    /**
     * 批量删除出库单
     */
    public void deleteById(Long id) {
        validateIdBeforeDelete(id);
        List<Long> detailIds = shipmentOrderDetailService.queryByShipmentOrderId(id).stream()
            .map(ShipmentOrderDetailVo::getId)
            .filter(Objects::nonNull)
            .toList();
        itemInstanceService.releaseShipmentReservationsByDetailIds(detailIds);
        shipmentOrderMapper.deleteById(id);
    }

    public void validateIdBeforeDelete(Long id) {
        ShipmentOrderVo shipmentOrderVo = queryById(id);
        if (shipmentOrderVo == null) {
            throw new BaseException("出库单不存在");
        }
        if (ServiceConstants.ShipmentOrderStatus.INVALID.equals(shipmentOrderVo.getShipmentOrderStatus())) {
            throw new ServiceException("出库单【" + shipmentOrderVo.getShipmentOrderNo() + "】已作废，无法删除！", HttpStatus.CONFLICT.value());
        }
        if (ServiceConstants.ShipmentOrderStatus.FINISH.equals(shipmentOrderVo.getShipmentOrderStatus())) {
            throw new ServiceException("出库单【" + shipmentOrderVo.getShipmentOrderNo() + "】已出库，无法删除！", HttpStatus.CONFLICT.value());
        }
    }

    /**
     * 出库
     * @param bo
     */
    @Transactional
    public void shipment(ShipmentOrderBo bo) {
        // 1.校验商品明细不能为空！
        validateBeforeShipment(bo);
        Map<Long, InventoryDetail> inventoryDetailMap = queryInventoryDetailMap(bo.getDetails());
        // 2.按仓库/库区/货架/货位/规格合并商品明细数量
        List<InventoryBo> mergedInventoryBoList = mergeShipmentOrderDetailByPlaceAndItem(bo.getDetails(), inventoryDetailMap);
        // 3.校验库存明细
        List<InventoryDetailBo> inventoryDetailBoList = convertShipmentOrderDetailToInventoryDetail(bo.getDetails());
        inventoryDetailService.validateRemainQuantity(inventoryDetailBoList);
        // 4. 保存入库单和入库单明细
        if (Objects.isNull(bo.getId())) {
            insertByBo(bo);
        } else {
            updateByBo(bo);
        }
        // 5.更新库存：Inventory表
        mergedInventoryBoList.forEach(mergedInventoryBo -> mergedInventoryBo.setQuantity(mergedInventoryBo.getQuantity().negate()));
        inventoryService.updateInventoryQuantity(mergedInventoryBoList);
        // 6.更新库存明细：InventoryHistory表
        inventoryDetailMapper.deductInventoryDetailQuantity(inventoryDetailBoList, LoginHelper.getUsername(), LocalDateTime.now());
        // 7.创建库存记录
        saveInventoryHistory(bo, inventoryDetailMap);
        // 8.同步单品实例状态
        syncShipmentObjects(bo.getDetails(), bo.getShipmentOrderType());
        locationService.refreshOccupiedFlagsByLocationIds(inventoryDetailMap.values().stream()
            .map(InventoryDetail::getLocationId)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet()));
    }

    /**
     * 按仓库/库区/货架/货位/规格合并商品明细数量
     * @param shipmentOrderDetailBoList 明细
     * @param inventoryDetailMap 库存明细映射
     * @return 合并后的库存变更
     */
    public List<InventoryBo> mergeShipmentOrderDetailByPlaceAndItem(@NotEmpty List<ShipmentOrderDetailBo> shipmentOrderDetailBoList,
                                                                    Map<Long, InventoryDetail> inventoryDetailMap) {
        Map<String, InventoryBo> mergedMap = new HashMap<>();
        shipmentOrderDetailBoList.forEach(detail -> {
            InventoryDetail inventoryDetail = inventoryDetailMap.get(detail.getInventoryDetailId());
            Long warehouseId = inventoryDetail != null ? inventoryDetail.getWarehouseId() : detail.getWarehouseId();
            Long areaId = inventoryDetail != null ? inventoryDetail.getAreaId() : detail.getAreaId();
            Long rackId = inventoryDetail != null ? inventoryDetail.getRackId() : null;
            Long locationId = inventoryDetail != null ? inventoryDetail.getLocationId() : null;
            String mergedKey = warehouseId + "_" + areaId + "_" + rackId + "_" + locationId + "_" + detail.getSkuId();
            if (mergedMap.containsKey(mergedKey)) {
                InventoryBo mergedInventoryBo = mergedMap.get(mergedKey);
                mergedInventoryBo.setQuantity(mergedInventoryBo.getQuantity().add(detail.getQuantity()));
                return;
            }
            InventoryBo mergedInventoryBo = new InventoryBo();
            mergedInventoryBo.setWarehouseId(warehouseId);
            mergedInventoryBo.setAreaId(areaId);
            mergedInventoryBo.setRackId(rackId);
            mergedInventoryBo.setLocationId(locationId);
            mergedInventoryBo.setSkuId(detail.getSkuId());
            mergedInventoryBo.setQuantity(detail.getQuantity());
            mergedMap.put(mergedKey, mergedInventoryBo);
        });
        return new ArrayList<>(mergedMap.values());
    }


    public List<InventoryDetailBo> convertShipmentOrderDetailToInventoryDetail(List<ShipmentOrderDetailBo> shipmentOrderDetailBoList) {
        return shipmentOrderDetailBoList
            .stream()
            .map(detail -> {
                InventoryDetailBo inventoryDetailBo = new InventoryDetailBo();
                inventoryDetailBo.setId(detail.getInventoryDetailId());
                inventoryDetailBo.setShipmentQuantity(detail.getQuantity());
                return inventoryDetailBo;
            }).toList();
    }

    private void saveInventoryHistory(ShipmentOrderBo bo, Map<Long, InventoryDetail> inventoryDetailMap){
        List<InventoryHistory> inventoryHistoryList = new LinkedList<>();
        bo.getDetails().forEach(detail -> {
            InventoryDetail inventoryDetail = inventoryDetailMap.get(detail.getInventoryDetailId());
            InventoryHistory inventoryHistory = new InventoryHistory();
            inventoryHistory.setOrderId(bo.getId());
            inventoryHistory.setOrderNo(bo.getShipmentOrderNo());
            inventoryHistory.setOrderType(ServiceConstants.InventoryHistoryOrderType.SHIPMENT);
            inventoryHistory.setSkuId(detail.getSkuId());
            inventoryHistory.setQuantity(detail.getQuantity().negate());
            inventoryHistory.setWarehouseId(inventoryDetail != null ? inventoryDetail.getWarehouseId() : detail.getWarehouseId());
            inventoryHistory.setAreaId(inventoryDetail != null ? inventoryDetail.getAreaId() : detail.getAreaId());
            inventoryHistory.setRackId(inventoryDetail != null ? inventoryDetail.getRackId() : null);
            inventoryHistory.setLocationId(inventoryDetail != null ? inventoryDetail.getLocationId() : null);
            inventoryHistory.setInstanceCode(detail.getInstanceCode() != null ? detail.getInstanceCode() :
                (inventoryDetail != null ? inventoryDetail.getInstanceCode() : null));
            inventoryHistory.setBoxId(detail.getBoxId() != null ? detail.getBoxId() :
                (inventoryDetail != null ? inventoryDetail.getBoxId() : null));
            inventoryHistory.setUnitPrice(detail.getUnitPrice());
            inventoryHistory.setLineAmount(detail.getLineAmount());
            inventoryHistoryList.add(inventoryHistory);
        });
        inventoryHistoryService.saveBatch(inventoryHistoryList);
    }

    private Map<Long, InventoryDetail> queryInventoryDetailMap(List<ShipmentOrderDetailBo> details) {
        Set<Long> inventoryDetailIds = details.stream()
            .map(ShipmentOrderDetailBo::getInventoryDetailId)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
        if (CollUtil.isEmpty(inventoryDetailIds)) {
            return java.util.Collections.emptyMap();
        }
        return inventoryDetailMapper.selectBatchIds(inventoryDetailIds).stream()
            .collect(Collectors.toMap(InventoryDetail::getId, java.util.function.Function.identity()));
    }

    private void validateBeforeShipment(ShipmentOrderBo bo) {
        if (CollUtil.isEmpty(bo.getDetails())) {
            throw new BaseException("商品明细不能为空！");
        }
        if (bo.getId() != null) {
            ShipmentOrder shipmentOrder = shipmentOrderMapper.selectById(bo.getId());
            Assert.notNull(shipmentOrder, "出库单不存在");
            Assert.isFalse(ServiceConstants.ShipmentOrderStatus.FINISH.equals(shipmentOrder.getShipmentOrderStatus()), "出库单已完成出库");
        }
        validateTrackedShipmentDetails(bo.getDetails());
    }

    private void validateTrackedShipmentDetails(List<ShipmentOrderDetailBo> details) {
        Set<String> instanceCodes = details.stream()
            .map(ShipmentOrderDetailBo::getInstanceCode)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
        if (CollUtil.isEmpty(instanceCodes)) {
            return;
        }
        Map<String, ItemInstance> itemMap = itemInstanceService.queryByInstanceCodes(instanceCodes).stream()
            .collect(Collectors.toMap(ItemInstance::getInstanceCode, java.util.function.Function.identity()));
        for (ShipmentOrderDetailBo detail : details) {
            if (detail.getInstanceCode() == null) {
                continue;
            }
            ItemInstance itemInstance = itemMap.get(detail.getInstanceCode());
            Assert.notNull(itemInstance, "存在不存在的单品实例");
            Assert.isTrue(Objects.equals(itemInstance.getSkuId(), detail.getSkuId()), "单品实例与出库规格不匹配");
            Assert.isTrue(detail.getQuantity() != null && detail.getQuantity().compareTo(java.math.BigDecimal.ONE) == 0, "按单品实例出库时，数量必须为1");
            Assert.isFalse(ServiceConstants.ItemInstanceStatus.BORROWED.equals(itemInstance.getInstanceStatus()), "已借出单品不能出库");
            Assert.isTrue(ServiceConstants.ItemInstanceStatus.IN_STOCK.equals(itemInstance.getInstanceStatus()), "仅在库单品可以出库");
        }
    }

    private void syncShipmentObjects(List<ShipmentOrderDetailBo> details, String shipmentOrderType) {
        Set<String> instanceCodes = details.stream()
            .map(ShipmentOrderDetailBo::getInstanceCode)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
        if (CollUtil.isEmpty(instanceCodes)) {
            return;
        }
        String targetStatus = ServiceConstants.ShipmentOrderType.SCRAP.equals(shipmentOrderType)
            ? ServiceConstants.ItemInstanceStatus.SCRAPPED
            : ServiceConstants.ItemInstanceStatus.OUTBOUND;
        Map<String, ItemInstance> itemMap = itemInstanceService.queryByInstanceCodes(instanceCodes).stream()
            .collect(Collectors.toMap(ItemInstance::getInstanceCode, java.util.function.Function.identity()));
        for (ShipmentOrderDetailBo detail : details) {
            if (detail.getInstanceCode() == null) {
                continue;
            }
            ItemInstance itemInstance = itemMap.get(detail.getInstanceCode());
            Assert.notNull(itemInstance, "单品实例不存在");
            itemInstanceService.markOutbound(itemInstance.getId(), targetStatus);
        }
    }

    private void normalizeShipmentDetails(List<ShipmentOrderDetailBo> details) {
        if (CollUtil.isEmpty(details)) {
            return;
        }
        Map<Long, ItemSkuVo> skuMap = itemSkuService.queryVosByIds(details.stream()
            .map(ShipmentOrderDetailBo::getSkuId)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet()))
            .stream()
            .collect(Collectors.toMap(ItemSkuVo::getId, java.util.function.Function.identity()));
        details.forEach(detail -> {
            ItemSkuVo itemSku = skuMap.get(detail.getSkuId());
            Assert.notNull(itemSku, "规格不存在");
            fillShipmentSnapshot(detail, itemSku);
            BigDecimal lineAmount = calcLineAmount(detail.getQuantity(), detail.getUnitPrice());
            detail.setLineAmount(lineAmount);
        });
    }

    private void fillShipmentSnapshot(ShipmentOrderDetailBo detail, ItemSkuVo itemSku) {
        detail.setSkuName(itemSku.getSkuName());
        detail.setProductIdentifier(itemSku.getProductIdentifier());
        detail.setQualityGrade(itemSku.getQualityGrade());
        if (itemSku.getItem() != null) {
            detail.setItemCode(itemSku.getItem().getItemCode());
            detail.setItemName(itemSku.getItem().getItemName());
            detail.setUnit(itemSku.getItem().getUnit());
        }
    }

    private BigDecimal calcLineAmount(BigDecimal quantity, BigDecimal unitPrice) {
        if (quantity == null || unitPrice == null) {
            return BigDecimal.ZERO;
        }
        return quantity.multiply(unitPrice).setScale(2, java.math.RoundingMode.HALF_UP);
    }
}
