package com.ruoyi.wms.service;

import cn.hutool.core.collection.CollUtil;
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
import com.ruoyi.wms.domain.bo.InventoryBo;
import com.ruoyi.wms.domain.bo.InventoryDetailBo;
import com.ruoyi.wms.domain.bo.MovementOrderBo;
import com.ruoyi.wms.domain.bo.MovementOrderDetailBo;
import com.ruoyi.wms.domain.entity.InventoryDetail;
import com.ruoyi.wms.domain.entity.ItemInstance;
import com.ruoyi.wms.domain.entity.InventoryHistory;
import com.ruoyi.wms.domain.entity.MovementOrder;
import com.ruoyi.wms.domain.entity.MovementOrderDetail;
import com.ruoyi.wms.domain.vo.ItemSkuVo;
import com.ruoyi.wms.domain.vo.MovementOrderVo;
import com.ruoyi.wms.mapper.InventoryDetailMapper;
import com.ruoyi.wms.mapper.MovementOrderMapper;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 调拨单 Service 业务层处理
 *
 * @author ping
 * @date 2024-08-09
 */
@RequiredArgsConstructor
@Service
public class MovementOrderService {

    @Value("${warehouse}")
    private String warehouse;

    private final MovementOrderMapper movementOrderMapper;
    private final MovementOrderDetailService movementOrderDetailService;
    private final InventoryService inventoryService;
    private final InventoryDetailService inventoryDetailService;
    private final InventoryDetailMapper inventoryDetailMapper;
    private final InventoryHistoryService inventoryHistoryService;
    private final ItemSkuService itemSkuService;
    private final ItemInstanceService itemInstanceService;


    /**
     * 查询调拨单
     */
    public MovementOrderVo queryById(Long id) {
        MovementOrderVo movementOrderVo = movementOrderMapper.selectVoById(id);
        if (movementOrderVo == null) {
            throw new BaseException("调拨单不存在");
        }
        movementOrderVo.setDetails(movementOrderDetailService.queryByMovementOrderId(id));
        return movementOrderVo;
    }

    /**
     * 查询调拨单列表
     */
    public TableDataInfo<MovementOrderVo> queryPageList(MovementOrderBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<MovementOrder> lqw = buildQueryWrapper(bo);
        Page<MovementOrderVo> result = movementOrderMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询调拨单列表
     */
    public List<MovementOrderVo> queryList(MovementOrderBo bo) {
        LambdaQueryWrapper<MovementOrder> lqw = buildQueryWrapper(bo);
        return movementOrderMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<MovementOrder> buildQueryWrapper(MovementOrderBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MovementOrder> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getMovementOrderNo()), MovementOrder::getMovementOrderNo, bo.getMovementOrderNo());
        lqw.eq(StringUtils.isNotBlank(bo.getMovementType()), MovementOrder::getMovementType, bo.getMovementType());
        lqw.eq(StringUtils.isNotBlank(bo.getDispatchBasis()), MovementOrder::getDispatchBasis, bo.getDispatchBasis());
        lqw.eq(StringUtils.isNotBlank(bo.getDispatchMode()), MovementOrder::getDispatchMode, bo.getDispatchMode());
        lqw.eq(bo.getSourceWarehouseId() != null, MovementOrder::getSourceWarehouseId, bo.getSourceWarehouseId());
        lqw.eq(bo.getSourceAreaId() != null, MovementOrder::getSourceAreaId, bo.getSourceAreaId());
        lqw.eq(bo.getTargetWarehouseId() != null, MovementOrder::getTargetWarehouseId, bo.getTargetWarehouseId());
        lqw.eq(bo.getTargetAreaId() != null, MovementOrder::getTargetAreaId, bo.getTargetAreaId());
        lqw.eq(bo.getMovementOrderStatus() != null, MovementOrder::getMovementOrderStatus, bo.getMovementOrderStatus());
        lqw.eq(bo.getTotalQuantity() != null, MovementOrder::getTotalQuantity, bo.getTotalQuantity());
        lqw.orderByDesc(BaseEntity::getCreateTime);
        return lqw;
    }

    /**
     * 新增调拨单
     */
    @Transactional
    public void insertByBo(MovementOrderBo bo) {
        normalizeMovementDetails(bo.getDetails());
        bo.setMovementOrderNo(StrUtil.blankToDefault(bo.getMovementOrderNo(), generateMovementOrderNo()));
        // 1.校验调拨单号唯一性
        validateMovementOrderNo(bo.getMovementOrderNo());
        fillHeaderLocationByDetails(bo);
        // 2.创建调拨单
        MovementOrder add = MapstructUtils.convert(bo, MovementOrder.class);
        movementOrderMapper.insert(add);
        bo.setId(add.getId());
        // 3.创建调拨单明细
        List<MovementOrderDetail> addDetailList = MapstructUtils.convert(bo.getDetails(), MovementOrderDetail.class);
        addDetailList.forEach(it -> {
            it.setMovementOrderId(add.getId());
        });
        movementOrderDetailService.saveDetails(addDetailList);
    }

    private void validateMovementOrderNo(String movementOrderNo) {
        LambdaQueryWrapper<MovementOrder> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(MovementOrder::getMovementOrderNo, movementOrderNo);
        if (movementOrderMapper.exists(lambdaQueryWrapper)) {
            throw new BaseException("系统生成的调拨单号重复，请稍后重试");
        }
    }

    private String generateMovementOrderNo() {
        return "DB" + warehouse + IdUtil.getSnowflakeNextIdStr();
    }

    /**
     * 修改调拨单
     */
    @Transactional
    public void updateByBo(MovementOrderBo bo) {
        validateIdBeforeUpdate(bo.getId());
        normalizeMovementDetails(bo.getDetails());
        // 1.更新调拨单
        fillHeaderLocationByDetails(bo);
        MovementOrder update = MapstructUtils.convert(bo, MovementOrder.class);
        movementOrderMapper.updateById(update);
        // 2.保存调拨单明细
        List<MovementOrderDetail> detailList = MapstructUtils.convert(bo.getDetails(), MovementOrderDetail.class);
        detailList.forEach(it -> it.setMovementOrderId(bo.getId()));
        movementOrderDetailService.saveDetails(detailList);
    }

    private void validateIdBeforeUpdate(Long id) {
        MovementOrderVo movementOrderVo = queryById(id);
        if (ServiceConstants.MovementOrderStatus.FINISH.equals(movementOrderVo.getMovementOrderStatus())) {
            throw new ServiceException("调拨单【" + movementOrderVo.getMovementOrderNo() + "】已执行，无法修改！");
        }
    }

    /**
     * 删除调拨单
     * @param id
     */
    public void deleteById(Long id) {
        validateIdBeforeDelete(id);
        movementOrderMapper.deleteById(id);
    }

    private void validateIdBeforeDelete(Long id) {
        MovementOrderVo movementOrderVo = queryById(id);
        if (movementOrderVo == null) {
            throw new BaseException("调拨单不存在");
        }
        if (ServiceConstants.MovementOrderStatus.INVALID.equals(movementOrderVo.getMovementOrderStatus())) {
            throw new ServiceException("调拨单【" + movementOrderVo.getMovementOrderNo() + "】已作废，无法删除！", HttpStatus.CONFLICT.value());
        }
        if (ServiceConstants.MovementOrderStatus.FINISH.equals(movementOrderVo.getMovementOrderStatus())) {
            throw new ServiceException("调拨单【" + movementOrderVo.getMovementOrderNo() + "】已执行，无法删除！", HttpStatus.CONFLICT.value());
        }
    }

    /**
     * 批量删除调拨单
     */
    public void deleteByIds(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return;
        }
        ids.forEach(this::validateIdBeforeDelete);
        movementOrderMapper.deleteBatchIds(ids);
    }

    /**
     * 调拨执行
     * @param bo
     */
    @Transactional
    public void move(MovementOrderBo bo) {

        List<InventoryDetailBo> inventoryDetailBoList = convertMovementOrderDetailToInventoryDetail(bo.getDetails());
        Map<Long, InventoryDetail> inventoryDetailMap = queryInventoryDetailMap(bo.getDetails());

        // 1.校验商品明细不能为空！
        validateBeforeMove(bo);

        // 2.校验库存记录
        inventoryDetailService.validateRemainQuantity(inventoryDetailBoList);

        // 3.保存调拨单和调拨单明细
        if (Objects.isNull(bo.getId())) {
            insertByBo(bo);
        } else {
            updateByBo(bo);
        }
        // 4.更新库存Inventory
        List<InventoryBo> mergedShipmentInventoryList = mergeShipmentDetailByPlaceAndItem(bo.getDetails());
        List<InventoryBo> mergedReceiptInventoryList = mergeReceiptDetailByPlaceAndItem(bo.getDetails());
        mergedShipmentInventoryList.forEach(mergedShipmentInventory -> mergedShipmentInventory.setQuantity(mergedShipmentInventory.getQuantity().negate()));
        inventoryService.updateInventoryQuantity(mergedShipmentInventoryList);
        inventoryService.updateInventoryQuantity(mergedReceiptInventoryList);

        // 5.更新库存明细InventoryDetail: deductInventoryDetailQuantity移出扣减库存，addInventoryDetail为移入增加库存
        inventoryDetailMapper.deductInventoryDetailQuantity(inventoryDetailBoList, LoginHelper.getUsername(), LocalDateTime.now());
        addInventoryDetail(bo, inventoryDetailMap);

        // 6.创建库存记录流水
        createInventoryHistory(bo, inventoryDetailMap);

        // 7.同步器材实例位置与来源单据
        syncMovementInstances(bo, inventoryDetailMap);

    }

    private void validateBeforeMove(MovementOrderBo bo) {
        if (CollUtil.isEmpty(bo.getDetails())) {
            throw new BaseException("商品明细不能为空！");
        }
    }

    /**
     * 按源仓库/库区/规格合并移出数量
     * @param movementOrderDetailBoList 明细
     */
    public List<InventoryBo> mergeShipmentDetailByPlaceAndItem(@NotEmpty List<MovementOrderDetailBo> movementOrderDetailBoList) {
        Map<String, InventoryBo> mergedShipmentMap = new HashMap<>();
        movementOrderDetailBoList.forEach(detail -> {
            String mergedShipmentKey = detail.getSourceWarehouseId() + "_" + detail.getSourceAreaId()
                + "_" + detail.getSkuId();
            if (mergedShipmentMap.containsKey(mergedShipmentKey)) {
                InventoryBo mergedInventoryBo = mergedShipmentMap.get(mergedShipmentKey);
                mergedInventoryBo.setQuantity(mergedInventoryBo.getQuantity().add(detail.getQuantity()));
            } else {
                InventoryBo mergedInventoryBo = new InventoryBo();
                mergedInventoryBo.setWarehouseId(detail.getSourceWarehouseId());
                mergedInventoryBo.setAreaId(detail.getSourceAreaId());
                mergedInventoryBo.setSkuId(detail.getSkuId());
                mergedInventoryBo.setQuantity(detail.getQuantity());
                mergedShipmentMap.put(mergedShipmentKey, mergedInventoryBo);
            }
        });

        return new ArrayList<>(mergedShipmentMap.values());
    }

    /**
     * 按目标仓库/库区/货架/货位/规格合并移入数量
     * @param movementOrderDetailBoList 明细
     */
    public List<InventoryBo> mergeReceiptDetailByPlaceAndItem(@NotEmpty List<MovementOrderDetailBo> movementOrderDetailBoList) {
        Map<String, InventoryBo> mergedReceiptMap = new HashMap<>();
        movementOrderDetailBoList.forEach(detail -> {
            String mergedReceiptKey = detail.getTargetWarehouseId() + "_" + detail.getTargetAreaId() + "_" + detail.getSkuId();
            if (mergedReceiptMap.containsKey(mergedReceiptKey)) {
                InventoryBo mergedInventoryBo = mergedReceiptMap.get(mergedReceiptKey);
                mergedInventoryBo.setQuantity(mergedInventoryBo.getQuantity().add(detail.getQuantity()));
            } else {
                InventoryBo mergedInventoryBo = new InventoryBo();
                mergedInventoryBo.setWarehouseId(detail.getTargetWarehouseId());
                mergedInventoryBo.setAreaId(detail.getTargetAreaId());
                mergedInventoryBo.setSkuId(detail.getSkuId());
                mergedInventoryBo.setQuantity(detail.getQuantity());
                mergedReceiptMap.put(mergedReceiptKey, mergedInventoryBo);
            }
        });
        return new ArrayList<>(mergedReceiptMap.values());
    }

    public List<InventoryDetailBo> convertMovementOrderDetailToInventoryDetail(List<MovementOrderDetailBo> movementOrderDetailBoList) {
        return movementOrderDetailBoList
            .stream()
            .map(detail -> {
                InventoryDetailBo inventoryDetailBo = new InventoryDetailBo();
                inventoryDetailBo.setId(detail.getInventoryDetailId());
                inventoryDetailBo.setShipmentQuantity(detail.getQuantity());
                return inventoryDetailBo;
            }).toList();
    }

    private Map<Long, InventoryDetail> queryInventoryDetailMap(List<MovementOrderDetailBo> details) {
        Set<Long> inventoryDetailIds = details.stream()
            .map(MovementOrderDetailBo::getInventoryDetailId)
            .filter(Objects::nonNull)
            .collect(java.util.stream.Collectors.toSet());
        if (CollUtil.isEmpty(inventoryDetailIds)) {
            return java.util.Collections.emptyMap();
        }
        return inventoryDetailMapper.selectBatchIds(inventoryDetailIds).stream()
            .collect(java.util.stream.Collectors.toMap(InventoryDetail::getId, java.util.function.Function.identity()));
    }

    /**
     * 调拨完成创建入库记录
     * @param bo
     */
    @Transactional
    public void addInventoryDetail(MovementOrderBo bo, Map<Long, InventoryDetail> inventoryDetailMap) {
        List<InventoryDetail> addInventoryDetailList = bo.getDetails().stream().map(it -> {
            InventoryDetail sourceInventoryDetail = inventoryDetailMap.get(it.getInventoryDetailId());
            InventoryDetail addInventoryDetail = new InventoryDetail();
            addInventoryDetail.setReceiptOrderId(bo.getId());
            addInventoryDetail.setOrderNo(bo.getMovementOrderNo());
            addInventoryDetail.setType(ServiceConstants.InventoryDetailType.MOVEMENT);
            addInventoryDetail.setSkuId(it.getSkuId());
            addInventoryDetail.setWarehouseId(it.getTargetWarehouseId());
            addInventoryDetail.setAreaId(it.getTargetAreaId());
            addInventoryDetail.setRackId(it.getTargetRackId());
            addInventoryDetail.setLocationId(it.getTargetLocationId());
            addInventoryDetail.setInstanceCode(resolveItemInstanceCode(it, sourceInventoryDetail));
            addInventoryDetail.setBoxId(null);
            addInventoryDetail.setSourceOrderType(ServiceConstants.ItemInstanceSourceType.MOVEMENT);
            addInventoryDetail.setSourceOrderId(bo.getId());
            addInventoryDetail.setQuantity(it.getQuantity());
            addInventoryDetail.setUnitPrice(it.getUnitPrice());
            addInventoryDetail.setLineAmount(it.getLineAmount());
            addInventoryDetail.setRemainQuantity(it.getQuantity());
            return addInventoryDetail;
        }).toList();
        inventoryDetailService.saveBatch(addInventoryDetailList);
    }

    /**
     * 调拨完成创建库存记录
     * @param bo
     */
    @Transactional
    public void createInventoryHistory(MovementOrderBo bo, Map<Long, InventoryDetail> inventoryDetailMap) {
        List<InventoryHistory> addInventoryHistoryList = new LinkedList<>();
        bo.getDetails().forEach(detail -> {
            InventoryDetail sourceInventoryDetail = inventoryDetailMap.get(detail.getInventoryDetailId());
            InventoryHistory shipmentInventoryHistory = new InventoryHistory();
            shipmentInventoryHistory.setWarehouseId(detail.getSourceWarehouseId());
            shipmentInventoryHistory.setAreaId(detail.getSourceAreaId());
            shipmentInventoryHistory.setRackId(detail.getSourceRackId());
            shipmentInventoryHistory.setLocationId(detail.getSourceLocationId());
            shipmentInventoryHistory.setSkuId(detail.getSkuId());
            shipmentInventoryHistory.setQuantity(detail.getQuantity().negate());
            shipmentInventoryHistory.setOrderId(bo.getId());
            shipmentInventoryHistory.setOrderNo(bo.getMovementOrderNo());
            shipmentInventoryHistory.setOrderType(ServiceConstants.InventoryHistoryOrderType.MOVEMENT);
            shipmentInventoryHistory.setInstanceCode(resolveItemInstanceCode(detail, sourceInventoryDetail));
            shipmentInventoryHistory.setUnitPrice(detail.getUnitPrice());
            shipmentInventoryHistory.setLineAmount(detail.getLineAmount());
            addInventoryHistoryList.add(shipmentInventoryHistory);
            InventoryHistory receiptInventoryHistory = new InventoryHistory();
            receiptInventoryHistory.setWarehouseId(detail.getTargetWarehouseId());
            receiptInventoryHistory.setAreaId(detail.getTargetAreaId());
            receiptInventoryHistory.setRackId(detail.getTargetRackId());
            receiptInventoryHistory.setLocationId(detail.getTargetLocationId());
            receiptInventoryHistory.setSkuId(detail.getSkuId());
            receiptInventoryHistory.setQuantity(detail.getQuantity());
            receiptInventoryHistory.setOrderId(bo.getId());
            receiptInventoryHistory.setOrderNo(bo.getMovementOrderNo());
            receiptInventoryHistory.setOrderType(ServiceConstants.InventoryHistoryOrderType.MOVEMENT);
            receiptInventoryHistory.setInstanceCode(resolveItemInstanceCode(detail, sourceInventoryDetail));
            receiptInventoryHistory.setUnitPrice(detail.getUnitPrice());
            receiptInventoryHistory.setLineAmount(detail.getLineAmount());
            addInventoryHistoryList.add(receiptInventoryHistory);
        });
        inventoryHistoryService.saveBatch(addInventoryHistoryList);
    }

    private void syncMovementInstances(MovementOrderBo bo, Map<Long, InventoryDetail> inventoryDetailMap) {
        // Collect all instanceCodes from details and inventory details
        Set<String> instanceCodes = new HashSet<>();
        for (MovementOrderDetailBo detail : bo.getDetails()) {
            if (StringUtils.isNotBlank(detail.getInstanceCode())) {
                instanceCodes.add(detail.getInstanceCode());
            }
            InventoryDetail sourceInventoryDetail = inventoryDetailMap.get(detail.getInventoryDetailId());
            if (sourceInventoryDetail != null && StringUtils.isNotBlank(sourceInventoryDetail.getInstanceCode())) {
                instanceCodes.add(sourceInventoryDetail.getInstanceCode());
            }
        }
        if (CollUtil.isEmpty(instanceCodes)) {
            return;
        }
        // Batch load ItemInstances by instanceCode to get actual IDs
        Map<String, ItemInstance> itemInstanceByCode = itemInstanceService.queryByInstanceCodes(instanceCodes)
            .stream()
            .collect(Collectors.toMap(ItemInstance::getInstanceCode, Function.identity()));

        for (MovementOrderDetailBo detail : bo.getDetails()) {
            InventoryDetail sourceInventoryDetail = inventoryDetailMap.get(detail.getInventoryDetailId());
            String instanceCode = resolveItemInstanceCode(detail, sourceInventoryDetail);
            if (instanceCode == null) {
                continue;
            }
            ItemInstance itemInstance = itemInstanceByCode.get(instanceCode);
            if (itemInstance == null) {
                continue;
            }
            itemInstanceService.moveByMovement(
                itemInstance.getId(),
                detail.getTargetWarehouseId(),
                detail.getTargetAreaId(),
                detail.getTargetRackId(),
                detail.getTargetLocationId(),
                bo.getId(),
                bo.getMovementOrderNo()
            );
        }
    }

    private String resolveItemInstanceCode(MovementOrderDetailBo detail, InventoryDetail sourceInventoryDetail) {
        if (StringUtils.isNotBlank(detail.getInstanceCode())) {
            return detail.getInstanceCode();
        }
        return sourceInventoryDetail == null ? null : sourceInventoryDetail.getInstanceCode();
    }

    private void fillHeaderLocationByDetails(MovementOrderBo bo) {
        if (CollUtil.isEmpty(bo.getDetails())) {
            return;
        }
    }

    private void normalizeMovementDetails(List<MovementOrderDetailBo> details) {
        if (CollUtil.isEmpty(details)) {
            return;
        }
        Map<Long, ItemSkuVo> skuMap = itemSkuService.queryVosByIds(details.stream()
            .map(MovementOrderDetailBo::getSkuId)
            .filter(Objects::nonNull)
            .collect(java.util.stream.Collectors.toSet()))
            .stream()
            .collect(java.util.stream.Collectors.toMap(ItemSkuVo::getId, java.util.function.Function.identity()));
        details.forEach(detail -> {
            ItemSkuVo itemSku = skuMap.get(detail.getSkuId());
            if (itemSku == null) {
                throw new BaseException("规格不存在");
            }
            detail.setSkuName(itemSku.getSkuName());
            detail.setProductIdentifier(itemSku.getProductIdentifier());
            detail.setQualityGrade(itemSku.getQualityGrade());
            if (itemSku.getItem() != null) {
                detail.setItemCode(itemSku.getItem().getItemCode());
                detail.setItemName(itemSku.getItem().getItemName());
                detail.setUnit(itemSku.getItem().getUnit());
            }
            detail.setLineAmount(calcLineAmount(detail.getQuantity(), detail.getUnitPrice()));
        });
    }

    private BigDecimal calcLineAmount(BigDecimal quantity, BigDecimal unitPrice) {
        if (quantity == null || unitPrice == null) {
            return BigDecimal.ZERO;
        }
        return quantity.multiply(unitPrice).setScale(2, java.math.RoundingMode.HALF_UP);
    }
}
