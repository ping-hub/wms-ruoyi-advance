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
import com.ruoyi.wms.domain.vo.MovementOrderDetailVo;
import com.ruoyi.wms.domain.vo.MovementOrderVo;
import com.ruoyi.wms.mapper.InventoryDetailMapper;
import com.ruoyi.wms.mapper.MovementOrderMapper;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
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


    private final MovementOrderMapper movementOrderMapper;
    private final CodeRuleService codeRuleService;
    private final MovementOrderDetailService movementOrderDetailService;
    private final InventoryService inventoryService;
    private final InventoryDetailService inventoryDetailService;
    private final InventoryDetailMapper inventoryDetailMapper;
    private final InventoryHistoryService inventoryHistoryService;
    private final ItemSkuService itemSkuService;
    private final ItemInstanceService itemInstanceService;
    private final CheckOrderService checkOrderService;
    private final ShipmentOrderService shipmentOrderService;


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
        lqw.eq(StringUtils.isNotBlank(bo.getTransferScope()), MovementOrder::getTransferScope, bo.getTransferScope());
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
        // 4.暂存锁定器材实例
        reserveMovementInstances(add.getId());
    }

    private void validateMovementOrderNo(String movementOrderNo) {
        LambdaQueryWrapper<MovementOrder> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(MovementOrder::getMovementOrderNo, movementOrderNo);
        if (movementOrderMapper.exists(lambdaQueryWrapper)) {
            throw new BaseException("系统生成的调拨单号重复，请稍后重试");
        }
    }

    private String generateMovementOrderNo() {
        String code = codeRuleService.generateCode("movement");
        return code != null ? code : "DB" + IdUtil.getSnowflakeNextIdStr();
    }

    /**
     * 修改调拨单
     */
    @Transactional
    public void updateByBo(MovementOrderBo bo) {
        validateIdBeforeUpdate(bo.getId());
        normalizeMovementDetails(bo.getDetails());
        // 0.释放旧的调拨暂存锁定
        releaseMovementInstances(bo.getId());
        // 1.更新调拨单
        fillHeaderLocationByDetails(bo);
        MovementOrder update = MapstructUtils.convert(bo, MovementOrder.class);
        movementOrderMapper.updateById(update);
        // 2.保存调拨单明细
        List<MovementOrderDetail> detailList = MapstructUtils.convert(bo.getDetails(), MovementOrderDetail.class);
        detailList.forEach(it -> it.setMovementOrderId(bo.getId()));
        movementOrderDetailService.saveDetails(detailList);
        // 3.重新暂存锁定器材实例
        reserveMovementInstances(bo.getId());
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
        List<Long> detailIds = movementOrderDetailService.queryByMovementOrderId(id).stream()
            .map(MovementOrderDetailVo::getId)
            .filter(Objects::nonNull)
            .toList();
        // 释放调拨暂存锁定
        if (CollUtil.isNotEmpty(detailIds)) {
            itemInstanceService.releaseMovementReservationsByDetailIds(detailIds);
        }
        // 删除明细
        if (CollUtil.isNotEmpty(detailIds)) {
            movementOrderDetailService.deleteByIds(detailIds);
        }
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
        List<Long> detailIds = ids.stream()
            .flatMap(id -> movementOrderDetailService.queryByMovementOrderId(id).stream())
            .map(MovementOrderDetailVo::getId)
            .filter(Objects::nonNull)
            .toList();
        // 释放调拨暂存锁定
        if (CollUtil.isNotEmpty(detailIds)) {
            itemInstanceService.releaseMovementReservationsByDetailIds(detailIds);
        }
        // 删除明细
        if (CollUtil.isNotEmpty(detailIds)) {
            movementOrderDetailService.deleteByIds(detailIds);
        }
        movementOrderMapper.deleteBatchIds(ids);
    }

    /**
     * 调拨执行
     * 库内调拨：更新Inventory汇总 + 更新InventoryDetail位置 + 一条移库流水 + 实例位置变更
     * 库外调拨：实例标记"已调拨" + 自动创建出库单（不操作库存）
     *
     * @param bo 调拨单
     */
    @Transactional
    public void move(MovementOrderBo bo) {
        // 0.1 盘点冻结校验（源位置 + 目标位置）
        if (bo.getDetails() != null) {
            Set<String> checked = new HashSet<>();
            for (var d : bo.getDetails()) {
                String srcKey = d.getSourceWarehouseId() + "_" + d.getSourceAreaId() + "_" + d.getSourceRackId();
                if (checked.add(srcKey)) {
                    checkOrderService.assertNoActiveCheckOrder(d.getSourceWarehouseId(), d.getSourceAreaId(), d.getSourceRackId());
                }
                if (d.getTargetWarehouseId() != null) {
                    String tgtKey = d.getTargetWarehouseId() + "_" + d.getTargetAreaId() + "_" + d.getTargetRackId();
                    if (checked.add(tgtKey)) {
                        checkOrderService.assertNoActiveCheckOrder(d.getTargetWarehouseId(), d.getTargetAreaId(), d.getTargetRackId());
                    }
                }
            }
        }

        boolean isExternal = "库外调拨".equals(bo.getTransferScope());

        // 1.校验器材明细不能为空！
        validateBeforeMove(bo);

        // 2.校验库存记录（仅库内调拨）
        if (!isExternal) {
            List<InventoryDetailBo> inventoryDetailBoList = convertMovementOrderDetailToInventoryDetail(bo.getDetails());
            inventoryDetailService.validateRemainQuantity(inventoryDetailBoList);
        }

        // 3.保存调拨单和调拨单明细
        if (Objects.isNull(bo.getId())) {
            insertByBo(bo);
        } else {
            updateByBo(bo);
        }

        // 4.释放调拨暂存锁定
        releaseMovementInstances(bo.getId());

        if (isExternal) {
            // ===== 库外调拨 =====
            Map<Long, InventoryDetail> inventoryDetailMap = queryInventoryDetailMap(bo.getDetails());

            // 5.实例状态→"已调拨"，清空位置（锁定，防止被其他操作占用）
            syncExternalMovementInstances(bo, inventoryDetailMap);

            // 6.自动创建出库单（草稿态），由出库单走审批后执行库存操作
            shipmentOrderService.createFromMovement(bo);
        } else {
            // ===== 库内调拨 =====
            Map<Long, InventoryDetail> inventoryDetailMap = queryInventoryDetailMap(bo.getDetails());

            // 5.更新Inventory汇总表：源位减、目标位加
            List<InventoryBo> mergedShipmentInventoryList = mergeShipmentDetailByPlaceAndItem(bo.getDetails(), inventoryDetailMap);
            mergedShipmentInventoryList.forEach(it -> it.setQuantity(it.getQuantity().negate()));
            inventoryService.updateInventoryQuantity(mergedShipmentInventoryList);

            List<InventoryBo> mergedReceiptInventoryList = mergeReceiptDetailByPlaceAndItem(bo.getDetails(), inventoryDetailMap);
            inventoryService.updateInventoryQuantity(mergedReceiptInventoryList);

            // 6.更新InventoryDetail位置字段（不扣减remainQuantity，不新建记录）
            updateInventoryDetailPosition(bo);

            // 7.创建一条移库流水
            createMovementHistory(bo, inventoryDetailMap);

            // 8.同步器材实例位置（状态保持"在库"）
            syncMovementInstances(bo, inventoryDetailMap);
        }
    }

    /**
     * 暂存时锁定器材实例（按调拨单ID查询已保存明细）
     */
    private void reserveMovementInstances(Long movementOrderId) {
        List<MovementOrderDetailVo> savedDetails = movementOrderDetailService.queryByMovementOrderId(movementOrderId);
        if (CollUtil.isEmpty(savedDetails)) {
            return;
        }
        List<MovementOrderDetailBo> boList = savedDetails.stream().map(vo -> {
            MovementOrderDetailBo detailBo = new MovementOrderDetailBo();
            detailBo.setId(vo.getId());
            detailBo.setInstanceCode(vo.getInstanceCode());
            return detailBo;
        }).toList();
        itemInstanceService.reserveForMovementDetails(boList);
    }

    /**
     * 释放调拨暂存锁定（按调拨单ID查询明细ID）
     */
    private void releaseMovementInstances(Long movementOrderId) {
        List<Long> detailIds = movementOrderDetailService.queryByMovementOrderId(movementOrderId).stream()
            .map(MovementOrderDetailVo::getId)
            .filter(Objects::nonNull)
            .toList();
        if (CollUtil.isNotEmpty(detailIds)) {
            itemInstanceService.releaseMovementReservationsByDetailIds(detailIds);
        }
    }

    private void validateBeforeMove(MovementOrderBo bo) {
        if (CollUtil.isEmpty(bo.getDetails())) {
            throw new BaseException("器材明细不能为空！");
        }
    }

    /**
     * 按源仓库/库区/货架/货位/规格合并移出数量
     * @param movementOrderDetailBoList 明细
     * @param inventoryDetailMap 库存明细映射
     */
    public List<InventoryBo> mergeShipmentDetailByPlaceAndItem(@NotEmpty List<MovementOrderDetailBo> movementOrderDetailBoList,
                                                               Map<Long, InventoryDetail> inventoryDetailMap) {
        Map<String, InventoryBo> mergedShipmentMap = new HashMap<>();
        movementOrderDetailBoList.forEach(detail -> {
            // 直接使用调拨明细的源位置（与 createInventoryHistory 保持一致）
            String mergedShipmentKey = detail.getSourceWarehouseId() + "_" + detail.getSourceAreaId()
                + "_" + detail.getSourceRackId() + "_" + detail.getSourceLocationId()
                + "_" + detail.getSkuId();
            if (mergedShipmentMap.containsKey(mergedShipmentKey)) {
                InventoryBo mergedInventoryBo = mergedShipmentMap.get(mergedShipmentKey);
                mergedInventoryBo.setQuantity(mergedInventoryBo.getQuantity().add(detail.getQuantity()));
            } else {
                InventoryBo mergedInventoryBo = new InventoryBo();
                mergedInventoryBo.setWarehouseId(detail.getSourceWarehouseId());
                mergedInventoryBo.setAreaId(detail.getSourceAreaId());
                mergedInventoryBo.setRackId(detail.getSourceRackId());
                mergedInventoryBo.setLocationId(detail.getSourceLocationId());
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
     * @param inventoryDetailMap 库存明细映射
     */
    public List<InventoryBo> mergeReceiptDetailByPlaceAndItem(@NotEmpty List<MovementOrderDetailBo> movementOrderDetailBoList,
                                                              Map<Long, InventoryDetail> inventoryDetailMap) {
        Map<String, InventoryBo> mergedReceiptMap = new HashMap<>();
        movementOrderDetailBoList.forEach(detail -> {
            String mergedReceiptKey = detail.getTargetWarehouseId() + "_" + detail.getTargetAreaId()
                + "_" + detail.getTargetRackId() + "_" + detail.getTargetLocationId() + "_" + detail.getSkuId();
            if (mergedReceiptMap.containsKey(mergedReceiptKey)) {
                InventoryBo mergedInventoryBo = mergedReceiptMap.get(mergedReceiptKey);
                mergedInventoryBo.setQuantity(mergedInventoryBo.getQuantity().add(detail.getQuantity()));
            } else {
                InventoryBo mergedInventoryBo = new InventoryBo();
                mergedInventoryBo.setWarehouseId(detail.getTargetWarehouseId());
                mergedInventoryBo.setAreaId(detail.getTargetAreaId());
                mergedInventoryBo.setRackId(detail.getTargetRackId());
                mergedInventoryBo.setLocationId(detail.getTargetLocationId());
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
        boolean isExternal = "库外调拨".equals(bo.getTransferScope());
        List<InventoryHistory> addInventoryHistoryList = new LinkedList<>();
        bo.getDetails().forEach(detail -> {
            InventoryDetail sourceInventoryDetail = inventoryDetailMap.get(detail.getInventoryDetailId());
            // 出库流水（库内库外都记录）
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
            if (!isExternal) {
                // 入库流水（仅库内调拨）
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
            }
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
                detail.getTargetLocationId()
            );
        }
    }


    /**
     * 库外调拨：将实例状态改为"已调拨"，清空位置
     */
    private void syncExternalMovementInstances(MovementOrderBo bo, Map<Long, InventoryDetail> inventoryDetailMap) {
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
            itemInstanceService.transferOut(
                itemInstance.getId()
            );
        }
    }


    /**
     * 库内调拨：更新InventoryDetail的位置字段（warehouseId/areaId/rackId/locationId）
     * 不扣减remainQuantity，不新建记录，ID保持不变
     */
    private void updateInventoryDetailPosition(MovementOrderBo bo) {
        for (MovementOrderDetailBo detail : bo.getDetails()) {
            InventoryDetail update = new InventoryDetail();
            update.setId(detail.getInventoryDetailId());
            update.setWarehouseId(detail.getTargetWarehouseId());
            update.setAreaId(detail.getTargetAreaId());
            update.setRackId(detail.getTargetRackId());
            update.setLocationId(detail.getTargetLocationId());
            inventoryDetailService.updateByBo(MapstructUtils.convert(update, InventoryDetailBo.class));
        }
    }

    /**
     * 库内调拨：创建一条移库流水记录
     */
    private void createMovementHistory(MovementOrderBo bo, Map<Long, InventoryDetail> inventoryDetailMap) {
        List<InventoryHistory> historyList = new LinkedList<>();
        bo.getDetails().forEach(detail -> {
            InventoryDetail sourceInventoryDetail = inventoryDetailMap.get(detail.getInventoryDetailId());
            InventoryHistory history = new InventoryHistory();
            history.setWarehouseId(detail.getSourceWarehouseId());
            history.setAreaId(detail.getSourceAreaId());
            history.setRackId(detail.getSourceRackId());
            history.setLocationId(detail.getSourceLocationId());
            history.setSkuId(detail.getSkuId());
            history.setQuantity(detail.getQuantity().negate());
            history.setOrderId(bo.getId());
            history.setOrderNo(bo.getMovementOrderNo());
            history.setOrderType(ServiceConstants.InventoryHistoryOrderType.MOVEMENT);
            history.setInstanceCode(resolveItemInstanceCode(detail, sourceInventoryDetail));
            history.setUnitPrice(detail.getUnitPrice());
            history.setLineAmount(detail.getLineAmount());
            historyList.add(history);
        });
        inventoryHistoryService.saveBatch(historyList);
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
        // 加载实例map用于填充qualityGrade
        java.util.Set<String> instanceCodes = details.stream()
            .map(MovementOrderDetailBo::getInstanceCode)
            .filter(Objects::nonNull)
            .collect(java.util.stream.Collectors.toSet());
        Map<String, ItemInstance> instMap = instanceCodes.isEmpty() ? java.util.Collections.emptyMap()
            : itemInstanceService.queryByInstanceCodes(instanceCodes).stream()
                .collect(java.util.stream.Collectors.toMap(ItemInstance::getInstanceCode, java.util.function.Function.identity()));
        details.forEach(detail -> {
            ItemSkuVo itemSku = skuMap.get(detail.getSkuId());
            if (itemSku == null) {
                throw new BaseException("规格不存在");
            }
            detail.setSkuName(itemSku.getSkuName());
            detail.setProductIdentifier(itemSku.getProductIdentifier());
            if (itemSku.getItem() != null) {
                detail.setItemCode(itemSku.getItem().getItemCode());
                detail.setItemName(itemSku.getItem().getItemName());
                detail.setUnit(itemSku.getItem().getUnit());
            }
            ItemInstance inst = detail.getInstanceCode() != null ? instMap.get(detail.getInstanceCode()) : null;
            if (inst != null) {
                detail.setQualityGrade(inst.getQualityGrade());
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
