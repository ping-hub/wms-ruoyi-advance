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


    private final ShipmentOrderMapper shipmentOrderMapper;
    private final CodeRuleService codeRuleService;
    private final ShipmentOrderDetailService shipmentOrderDetailService;
    private final InventoryService inventoryService;
    private final InventoryDetailMapper inventoryDetailMapper;
    private final InventoryHistoryService inventoryHistoryService;
    private final InventoryDetailService inventoryDetailService;
    private final ItemInstanceService itemInstanceService;
    private final ItemSkuService itemSkuService;
    private final LocationService locationService;
    private final WorkflowService workflowService;
    private final CheckOrderService checkOrderService;

    /**
     * 查询出库单
     */
    public ShipmentOrderVo queryById(Long id){
        ShipmentOrderVo shipmentOrderVo = shipmentOrderMapper.selectVoById(id);
        if (shipmentOrderVo == null) {
            throw new BaseException("出库单不存在");
        }
        shipmentOrderVo.setDetails(shipmentOrderDetailService.queryByShipmentOrderId(shipmentOrderVo.getId()));
        shipmentOrderVo.setWorkflowLogs(workflowService.getLogs("shipment", shipmentOrderVo.getId()));
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
        // 创建出库单（记录申请人）
        bo.setApplicantId(LoginHelper.getUserId());
        bo.setApplicantName(LoginHelper.getUsername());
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
        String code = codeRuleService.generateCode("shipment");
        return code != null ? code : "CK" + IdUtil.getSnowflakeNextIdStr();
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
        // 删除明细
        if (CollUtil.isNotEmpty(detailIds)) {
            shipmentOrderDetailService.deleteByIds(detailIds);
        }
        shipmentOrderMapper.deleteById(id);
    }

    public void validateIdBeforeDelete(Long id) {
        ShipmentOrderVo shipmentOrderVo = queryById(id);
        if (shipmentOrderVo == null) {
            throw new BaseException("出库单不存在");
        }
        Integer status = shipmentOrderVo.getShipmentOrderStatus();
        if (ServiceConstants.ShipmentOrderStatus.INVALID.equals(status)) {
            throw new ServiceException("出库单【" + shipmentOrderVo.getShipmentOrderNo() + "】已作废，无法删除！", HttpStatus.CONFLICT.value());
        }
        if (ServiceConstants.ShipmentOrderStatus.FINISH.equals(status)) {
            throw new ServiceException("出库单【" + shipmentOrderVo.getShipmentOrderNo() + "】已出库，无法删除！", HttpStatus.CONFLICT.value());
        }
        if (ServiceConstants.ShipmentOrderStatus.APPROVED.equals(status)) {
            throw new ServiceException("出库单【" + shipmentOrderVo.getShipmentOrderNo() + "】已审批，无法删除！", HttpStatus.CONFLICT.value());
        }
        if (ServiceConstants.ShipmentOrderStatus.PENDING_APPROVAL.equals(status)) {
            throw new ServiceException("出库单【" + shipmentOrderVo.getShipmentOrderNo() + "】待审批中，无法删除！", HttpStatus.CONFLICT.value());
        }
    }

    /**
     * 出库
     * @param bo
     */
    @Transactional
    public void shipment(ShipmentOrderBo bo) {
        // 0.校验状态必须为已审批
        if (bo.getId() != null) {
            ShipmentOrder existing = shipmentOrderMapper.selectById(bo.getId());
            Assert.notNull(existing, "出库单不存在");
            Assert.isTrue(ServiceConstants.ShipmentOrderStatus.APPROVED.equals(existing.getShipmentOrderStatus()),
                "出库单未审批通过，不能执行出库");
            if (existing.getExecutorId() != null && !existing.getExecutorId().equals(LoginHelper.getUserId())) {
                throw new ServiceException("您不是指定的出库操作人，无权执行出库");
            }
        }
        // 0.1 盘点冻结校验
        if (bo.getDetails() != null && bo.getWarehouseId() != null) {
            Set<String> checked = new HashSet<>();
            for (var d : bo.getDetails()) {
                String key = bo.getWarehouseId() + "_" + d.getAreaId() + "_" + d.getRackId();
                if (checked.add(key)) {
                    checkOrderService.assertNoActiveCheckOrder(bo.getWarehouseId(), d.getAreaId(), d.getRackId());
                }
            }
        }
        // 1.校验器材明细不能为空！
        validateBeforeShipment(bo);
        Map<Long, InventoryDetail> inventoryDetailMap = queryInventoryDetailMap(bo.getDetails());
        // 2.按仓库/库区/货架/货位/规格合并器材明细数量
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
        // 9.记录执行人 + 写流程日志
        ShipmentOrder updateExecutor = new ShipmentOrder();
        updateExecutor.setId(bo.getId());
        updateExecutor.setExecutorId(LoginHelper.getUserId());
        updateExecutor.setExecutorName(LoginHelper.getUsername());
        updateExecutor.setExecuteTime(LocalDateTime.now());
        updateExecutor.setShipmentOrderStatus(ServiceConstants.ShipmentOrderStatus.FINISH);
        shipmentOrderMapper.updateById(updateExecutor);
        workflowService.logOperation("shipment", bo.getId(), "execute", "执行出库", null, "executed");
    }

    /**
     * 按仓库/库区/货架/货位/规格合并器材明细数量
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
            throw new BaseException("器材明细不能为空！");
        }
        if (bo.getId() != null) {
            ShipmentOrder shipmentOrder = shipmentOrderMapper.selectById(bo.getId());
            Assert.notNull(shipmentOrder, "出库单不存在");
            Assert.isTrue(ServiceConstants.ShipmentOrderStatus.APPROVED.equals(shipmentOrder.getShipmentOrderStatus()),
                "出库单未审批通过，不能执行出库");
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
        // 收集所有需要出库的实例 ID，批量更新（避免逐条 SQL）
        Set<Long> instanceIds = new HashSet<>();
        for (ShipmentOrderDetailBo detail : details) {
            if (detail.getInstanceCode() == null) {
                continue;
            }
            ItemInstance itemInstance = itemMap.get(detail.getInstanceCode());
            Assert.notNull(itemInstance, "单品实例不存在");
            instanceIds.add(itemInstance.getId());
        }
        if (CollUtil.isNotEmpty(instanceIds)) {
            itemInstanceService.batchMarkOutbound(instanceIds, targetStatus);
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

    // ==================== 审批流程方法 ====================

    /**
     * 提交审批（草稿/已驳回 → 待审批）
     *
     * @param id           出库单ID
     * @param approverId   指定审批人ID
     * @param approverName 指定审批人姓名
     */
    @Transactional
    public void submitForApproval(Long id, Long approverId, String approverName) {
        ShipmentOrder order = shipmentOrderMapper.selectById(id);
        Assert.notNull(order, "出库单不存在");
        Assert.isTrue(
            ServiceConstants.ShipmentOrderStatus.DRAFT.equals(order.getShipmentOrderStatus())
                || ServiceConstants.ShipmentOrderStatus.REJECTED.equals(order.getShipmentOrderStatus()),
            "只有草稿或已驳回状态的出库单才能提交审批"
        );
        ShipmentOrder update = new ShipmentOrder();
        update.setId(id);
        update.setShipmentOrderStatus(ServiceConstants.ShipmentOrderStatus.PENDING_APPROVAL);
        update.setSubmitTime(LocalDateTime.now());
        update.setApproverId(approverId);
        update.setApproverName(approverName);
        shipmentOrderMapper.updateById(update);
        workflowService.logOperation("shipment", id, "submit", "提交审批",
            approverName != null ? "指定审批人：" + approverName : null, "submitted");
    }

    /**
     * 审批通过（待审批 → 已审批）
     */
    @Transactional
    public void approve(Long id, String remark, Long executorId, String executorName) {
        ShipmentOrder order = shipmentOrderMapper.selectById(id);
        Assert.notNull(order, "出库单不存在");
        Assert.isTrue(ServiceConstants.ShipmentOrderStatus.PENDING_APPROVAL.equals(order.getShipmentOrderStatus()),
            "只有待审批状态的出库单才能审批");

        ShipmentOrder update = new ShipmentOrder();
        update.setId(id);
        update.setShipmentOrderStatus(ServiceConstants.ShipmentOrderStatus.APPROVED);
        update.setApproverId(LoginHelper.getUserId());
        update.setApproverName(LoginHelper.getUsername());
        update.setApproveTime(LocalDateTime.now());
        update.setApproveRemark(remark);
        update.setExecutorId(executorId);
        update.setExecutorName(executorName);
        shipmentOrderMapper.updateById(update);
        String logRemark = remark;
        if (executorName != null) {
            logRemark = (logRemark != null ? logRemark + "; " : "") + "指定操作人：" + executorName;
        }
        workflowService.logOperation("shipment", id, "approve", "审批通过", logRemark, "approved");
    }

    /**
     * 驳回（待审批 → 已驳回）
     */
    @Transactional
    public void reject(Long id, String remark) {
        ShipmentOrder order = shipmentOrderMapper.selectById(id);
        Assert.notNull(order, "出库单不存在");
        Assert.isTrue(ServiceConstants.ShipmentOrderStatus.PENDING_APPROVAL.equals(order.getShipmentOrderStatus()),
            "只有待审批状态的出库单才能驳回");

        ShipmentOrder update = new ShipmentOrder();
        update.setId(id);
        update.setShipmentOrderStatus(ServiceConstants.ShipmentOrderStatus.REJECTED);
        update.setApproverId(LoginHelper.getUserId());
        update.setApproverName(LoginHelper.getUsername());
        update.setApproveTime(LocalDateTime.now());
        update.setApproveRemark(remark);
        shipmentOrderMapper.updateById(update);
        workflowService.logOperation("shipment", id, "reject", "驳回", remark, "rejected");
    }

    /**
     * 作废（草稿/已驳回 → 作废）
     */
    @Transactional
    public void voidOrder(Long id) {
        ShipmentOrder order = shipmentOrderMapper.selectById(id);
        Assert.notNull(order, "出库单不存在");
        Assert.isTrue(
            ServiceConstants.ShipmentOrderStatus.DRAFT.equals(order.getShipmentOrderStatus())
                || ServiceConstants.ShipmentOrderStatus.REJECTED.equals(order.getShipmentOrderStatus()),
            "只有草稿或已驳回状态的出库单才能作废"
        );
        ShipmentOrder update = new ShipmentOrder();
        update.setId(id);
        update.setShipmentOrderStatus(ServiceConstants.ShipmentOrderStatus.INVALID);
        shipmentOrderMapper.updateById(update);
        workflowService.logOperation("shipment", id, "void", "作废", null, "voided");
    }

}
