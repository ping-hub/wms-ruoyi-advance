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
import com.ruoyi.wms.domain.bo.BorrowOrderBo;
import com.ruoyi.wms.domain.bo.BorrowOrderDetailBo;
import com.ruoyi.wms.domain.entity.BorrowOrder;
import com.ruoyi.wms.domain.entity.BorrowOrderDetail;
import com.ruoyi.wms.domain.bo.InventoryBo;
import com.ruoyi.wms.domain.entity.InventoryHistory;
import com.ruoyi.wms.domain.entity.ItemInstance;
import com.ruoyi.wms.domain.vo.BorrowOrderDetailVo;
import com.ruoyi.wms.domain.vo.BorrowOrderVo;
import com.ruoyi.wms.domain.vo.ItemSkuVo;
import com.ruoyi.wms.mapper.BorrowOrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 器材借用单Service
 */
@RequiredArgsConstructor
@Service
public class BorrowOrderService {

    private final BorrowOrderMapper borrowOrderMapper;
    private final BorrowOrderDetailService borrowOrderDetailService;
    private final CodeRuleService codeRuleService;
    private final ItemInstanceService itemInstanceService;
    private final InventoryService inventoryService;
    private final InventoryDetailService inventoryDetailService;
    private final InventoryHistoryService inventoryHistoryService;
    private final ItemSkuService itemSkuService;
    private final CheckOrderService checkOrderService;
    private final LocationService locationService;

    public BorrowOrderVo queryById(Long id) {
        BorrowOrderVo vo = borrowOrderMapper.selectVoById(id);
        if (vo == null) {
            throw new BaseException("借用单不存在");
        }
        vo.setDetails(borrowOrderDetailService.queryByBorrowOrderId(id));
        fillOverdueFields(vo);
        return vo;
    }

    public TableDataInfo<BorrowOrderVo> queryPageList(BorrowOrderBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<BorrowOrder> lqw = buildQueryWrapper(bo);
        Page<BorrowOrderVo> result = borrowOrderMapper.selectVoPage(pageQuery.build(), lqw);
        result.getRecords().forEach(this::fillOverdueFields);
        return TableDataInfo.build(result);
    }

    public List<BorrowOrderVo> queryList(BorrowOrderBo bo) {
        LambdaQueryWrapper<BorrowOrder> lqw = buildQueryWrapper(bo);
        List<BorrowOrderVo> list = borrowOrderMapper.selectVoList(lqw);
        list.forEach(this::fillOverdueFields);
        return list;
    }

    private LambdaQueryWrapper<BorrowOrder> buildQueryWrapper(BorrowOrderBo bo) {
        LambdaQueryWrapper<BorrowOrder> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getBorrowOrderNo()), BorrowOrder::getBorrowOrderNo, bo.getBorrowOrderNo());
        lqw.like(StringUtils.isNotBlank(bo.getBorrower()), BorrowOrder::getBorrower, bo.getBorrower());
        lqw.like(StringUtils.isNotBlank(bo.getFromUnit()), BorrowOrder::getFromUnit, bo.getFromUnit());
        lqw.like(StringUtils.isNotBlank(bo.getToUnit()), BorrowOrder::getToUnit, bo.getToUnit());
        lqw.eq(bo.getBorrowOrderStatus() != null, BorrowOrder::getBorrowOrderStatus, bo.getBorrowOrderStatus());
        lqw.orderByDesc(BaseEntity::getCreateTime);
        return lqw;
    }

    /**
     * 暂存借用单
     */
    @Transactional
    public void insertByBo(BorrowOrderBo bo) {
        normalizeDetails(bo.getDetails());
        bo.setBorrowOrderNo(StrUtil.blankToDefault(bo.getBorrowOrderNo(), generateBorrowOrderNo()));
        validateBorrowOrderNo(bo.getBorrowOrderNo());
        if (bo.getTotalQuantity() == null) {
            bo.setTotalQuantity(bo.getDetails() != null ? bo.getDetails().size() : 0);
        }
        BorrowOrder add = MapstructUtils.convert(bo, BorrowOrder.class);
        if (add.getBorrowOrderStatus() == null) {
            add.setBorrowOrderStatus(ServiceConstants.BorrowOrderStatus.DRAFT);
        }
        borrowOrderMapper.insert(add);
        bo.setId(add.getId());
        saveDetailList(bo.getId(), bo.getDetails());
    }

    /**
     * 修改借用单
     */
    @Transactional
    public void updateByBo(BorrowOrderBo bo) {
        normalizeDetails(bo.getDetails());
        BorrowOrder update = MapstructUtils.convert(bo, BorrowOrder.class);
        borrowOrderMapper.updateById(update);
        // Diff and save details
        List<BorrowOrderDetailVo> existedDetails = borrowOrderDetailService.queryByBorrowOrderId(bo.getId());
        List<Long> incomingIds = bo.getDetails() != null ? bo.getDetails().stream()
            .map(BorrowOrderDetailBo::getId).filter(Objects::nonNull).toList() : List.of();
        List<Long> existedIds = existedDetails.stream()
            .map(BorrowOrderDetailVo::getId).filter(Objects::nonNull).toList();
        List<Long> deleteIds = existedIds.stream().filter(id -> !incomingIds.contains(id)).toList();
        if (CollUtil.isNotEmpty(deleteIds)) {
            borrowOrderDetailService.deleteByIds(deleteIds);
        }
        saveDetailList(bo.getId(), bo.getDetails());
    }

    /**
     * 确认借出（草稿 → 借出中）
     */
    @Transactional
    public void confirmBorrow(BorrowOrderBo bo) {
        BorrowOrder existing = borrowOrderMapper.selectById(bo.getId());
        Assert.notNull(existing, "借用单不存在");
        Assert.isTrue(ServiceConstants.BorrowOrderStatus.DRAFT.equals(existing.getBorrowOrderStatus()),
            "只有草稿状态的借用单才能确认借出");
        // Validate details not empty
        List<BorrowOrderDetailVo> details = borrowOrderDetailService.queryByBorrowOrderId(bo.getId());
        Assert.notEmpty(details, "器材明细不能为空");
        // 批量查询所有器材实例，避免 N+1 逐条查询
        Set<String> instanceCodes = details.stream()
            .map(BorrowOrderDetailVo::getInstanceCode).filter(Objects::nonNull).collect(Collectors.toSet());
        Map<String, ItemInstance> itemMap = instanceCodes.isEmpty() ? Map.of() :
            itemInstanceService.queryByInstanceCodes(instanceCodes).stream()
                .collect(Collectors.toMap(ItemInstance::getInstanceCode, Function.identity()));

        // === 收集所有操作，最后批量执行 ===
        List<InventoryBo> inventoryAdjustments = new ArrayList<>();
        List<ItemInstance> instanceUpdates = new ArrayList<>();
        Set<Long> affectedBoxIds = new HashSet<>();
        List<BorrowOrderDetail> detailUpdates = new ArrayList<>();
        List<InventoryHistory> historyList = new ArrayList<>();

        for (BorrowOrderDetailVo detail : details) {
            ItemInstance itemInstance = itemMap.get(detail.getInstanceCode());
            Assert.notNull(itemInstance, "器材实例不存在：" + detail.getInstanceCode());
            Assert.isTrue(ServiceConstants.ItemInstanceStatus.IN_STOCK.equals(itemInstance.getInstanceStatus()),
                "器材实例 " + detail.getInstanceCode() + " 不在库，无法借出");
            Assert.isTrue(itemInstance.getShipmentOrderDetailId() == null, "器材实例 " + detail.getInstanceCode() + " 已被出库单占用");
            Assert.isTrue(itemInstance.getMovementOrderDetailId() == null, "器材实例 " + detail.getInstanceCode() + " 已被调拨单占用");
            Assert.isTrue(findActiveBorrowRecord(detail.getInstanceCode()) == null,
                "器材实例 " + detail.getInstanceCode() + " 已处于借出状态");
            checkOrderService.assertNoActiveCheckOrder(itemInstance.getWarehouseId(), itemInstance.getAreaId(), itemInstance.getRackId());

            // 收集库存调整（-1）
            InventoryBo adj = new InventoryBo();
            adj.setWarehouseId(itemInstance.getWarehouseId());
            adj.setAreaId(itemInstance.getAreaId());
            adj.setRackId(itemInstance.getRackId());
            adj.setLocationId(itemInstance.getLocationId());
            adj.setSkuId(itemInstance.getSkuId());
            adj.setQuantity(BigDecimal.ONE.negate());
            inventoryAdjustments.add(adj);

            // 收集实例更新（状态+清空位置）
            if (itemInstance.getBoxId() != null) affectedBoxIds.add(itemInstance.getBoxId());
            itemInstance.setInstanceStatus(ServiceConstants.ItemInstanceStatus.BORROWED);
            itemInstance.setBoxId(null);
            itemInstance.setWarehouseId(null);
            itemInstance.setAreaId(null);
            itemInstance.setRackId(null);
            itemInstance.setLocationId(null);
            instanceUpdates.add(itemInstance);

            // 收集明细更新（借出后位置信息清空，保持原行为）
            BorrowOrderDetail updateDetail = new BorrowOrderDetail();
            updateDetail.setId(detail.getId());
            updateDetail.setReturnStatus(ServiceConstants.BorrowDetailReturnStatus.NOT_RETURNED);
            detailUpdates.add(updateDetail);

            // 收集历史
            historyList.add(buildBorrowHistory(existing, detail, itemInstance));
        }

        // === 批量执行 ===
        // 1次批量库存调整（替代 N 次 adjustQuantityBySkuAndPlace = 3N 次 SQL）
        inventoryService.updateInventoryQuantity(inventoryAdjustments);
        // 1次批量实例更新（替代 N 次 update）
        if (!instanceUpdates.isEmpty()) itemInstanceService.updateBatchById(instanceUpdates);
        // 批量同步箱子状态
        affectedBoxIds.forEach(boxId -> itemInstanceService.syncBoxAfterItemLeave(boxId));
        // 1次批量明细更新（替代 N 次 updateById）
        if (!detailUpdates.isEmpty()) borrowOrderDetailService.updateBatchById(detailUpdates);
        // 1次批量历史写入（替代 N 次 save）
        if (!historyList.isEmpty()) inventoryHistoryService.saveBatch(historyList);

        // Update order status
        BorrowOrder updateOrder = new BorrowOrder();
        updateOrder.setId(bo.getId());
        updateOrder.setBorrowOrderStatus(ServiceConstants.BorrowOrderStatus.BORROWING);
        updateOrder.setBorrowTime(existing.getBorrowTime() != null ? existing.getBorrowTime() : LocalDateTime.now());
        updateOrder.setTotalQuantity(details.size());
        borrowOrderMapper.updateById(updateOrder);
    }

    /**
     * 全部归还（借出中 → 已归还）
     */
    @Transactional
    public void returnAll(Long orderId) {
        BorrowOrder existing = borrowOrderMapper.selectById(orderId);
        Assert.notNull(existing, "借用单不存在");
        Assert.isTrue(ServiceConstants.BorrowOrderStatus.BORROWING.equals(existing.getBorrowOrderStatus()),
            "只有借出中状态的借用单才能归还");
        List<BorrowOrderDetailVo> details = borrowOrderDetailService.queryByBorrowOrderId(orderId);
        Assert.notEmpty(details, "器材明细不能为空");
        LocalDateTime now = LocalDateTime.now();
        // 批量查询所有器材实例，避免 N+1 逐条查询
        Set<String> returnInstanceCodes = details.stream()
            .filter(d -> !ServiceConstants.BorrowDetailReturnStatus.RETURNED.equals(d.getReturnStatus()))
            .map(BorrowOrderDetailVo::getInstanceCode).filter(Objects::nonNull).collect(Collectors.toSet());
        Map<String, ItemInstance> returnItemMap = returnInstanceCodes.isEmpty() ? Map.of() :
            itemInstanceService.queryByInstanceCodes(returnInstanceCodes).stream()
                .collect(Collectors.toMap(ItemInstance::getInstanceCode, Function.identity()));

        // === 收集所有操作，最后批量执行 ===
        List<InventoryBo> inventoryAdjustments = new ArrayList<>();
        List<ItemInstance> instanceUpdates = new ArrayList<>();
        List<BorrowOrderDetail> detailUpdates = new ArrayList<>();
        List<InventoryHistory> historyList = new ArrayList<>();

        for (BorrowOrderDetailVo detail : details) {
            if (ServiceConstants.BorrowDetailReturnStatus.RETURNED.equals(detail.getReturnStatus())) {
                continue;
            }
            ItemInstance itemInstance = returnItemMap.get(detail.getInstanceCode());
            Assert.notNull(itemInstance, "器材实例不存在：" + detail.getInstanceCode());

            // 收集库存调整（+1 归还）
            InventoryBo adj = new InventoryBo();
            adj.setWarehouseId(detail.getWarehouseId());
            adj.setAreaId(detail.getAreaId());
            adj.setRackId(detail.getRackId());
            adj.setLocationId(detail.getLocationId());
            adj.setSkuId(itemInstance.getSkuId());
            adj.setQuantity(BigDecimal.ONE);
            inventoryAdjustments.add(adj);

            // 收集实例更新（恢复到原位）
            itemInstance.setInstanceStatus(ServiceConstants.ItemInstanceStatus.IN_STOCK);
            itemInstance.setWarehouseId(detail.getWarehouseId());
            itemInstance.setAreaId(detail.getAreaId());
            itemInstance.setRackId(detail.getRackId());
            itemInstance.setLocationId(detail.getLocationId());
            itemInstance.setBoxId(null);
            instanceUpdates.add(itemInstance);

            // 收集明细更新
            BorrowOrderDetail updateDetail = new BorrowOrderDetail();
            updateDetail.setId(detail.getId());
            updateDetail.setReturnStatus(ServiceConstants.BorrowDetailReturnStatus.RETURNED);
            updateDetail.setReturnTime(now);
            detailUpdates.add(updateDetail);

            // 收集历史
            historyList.add(buildReturnHistory(existing, detail, itemInstance));
        }

        // === 批量执行 ===
        inventoryService.updateInventoryQuantity(inventoryAdjustments);
        if (!instanceUpdates.isEmpty()) itemInstanceService.updateBatchById(instanceUpdates);
        if (!detailUpdates.isEmpty()) borrowOrderDetailService.updateBatchById(detailUpdates);
        if (!historyList.isEmpty()) inventoryHistoryService.saveBatch(historyList);

        // Refresh location occupied flags
        Set<Long> locationIds = details.stream()
            .map(BorrowOrderDetailVo::getLocationId)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
        if (!locationIds.isEmpty()) {
            locationService.refreshOccupiedFlagsByLocationIds(locationIds);
        }
        // Update order status
        BorrowOrder updateOrder = new BorrowOrder();
        updateOrder.setId(orderId);
        updateOrder.setBorrowOrderStatus(ServiceConstants.BorrowOrderStatus.RETURNED);
        updateOrder.setReturnTime(now);
        borrowOrderMapper.updateById(updateOrder);
    }

    /**
     * 作废借用单
     */
    @Transactional
    public void voidOrder(Long orderId) {
        BorrowOrder existing = borrowOrderMapper.selectById(orderId);
        Assert.notNull(existing, "借用单不存在");
        Assert.isTrue(
            ServiceConstants.BorrowOrderStatus.DRAFT.equals(existing.getBorrowOrderStatus())
                || ServiceConstants.BorrowOrderStatus.BORROWING.equals(existing.getBorrowOrderStatus()),
            "只有草稿或借出中状态的借用单才能作废"
        );
        // If borrowing, need to restore all items
        if (ServiceConstants.BorrowOrderStatus.BORROWING.equals(existing.getBorrowOrderStatus())) {
            returnAll(orderId);
        }
        BorrowOrder update = new BorrowOrder();
        update.setId(orderId);
        update.setBorrowOrderStatus(ServiceConstants.BorrowOrderStatus.INVALID);
        borrowOrderMapper.updateById(update);
    }

    /**
     * 删除借用单（仅草稿可删）
     */
    public void deleteById(Long id) {
        BorrowOrderVo vo = queryById(id);
        if (vo == null) {
            throw new BaseException("借用单不存在");
        }
        if (!ServiceConstants.BorrowOrderStatus.DRAFT.equals(vo.getBorrowOrderStatus())) {
            throw new ServiceException("借用单【" + vo.getBorrowOrderNo() + "】非草稿状态，不能删除！", HttpStatus.CONFLICT.value());
        }
        List<BorrowOrderDetailVo> details = borrowOrderDetailService.queryByBorrowOrderId(id);
        if (CollUtil.isNotEmpty(details)) {
            borrowOrderDetailService.deleteByIds(details.stream().map(BorrowOrderDetailVo::getId).toList());
        }
        borrowOrderMapper.deleteById(id);
    }

    // ============ Private helpers ============

    private void saveDetailList(Long orderId, List<BorrowOrderDetailBo> detailBoList) {
        if (CollUtil.isEmpty(detailBoList)) {
            return;
        }
        List<BorrowOrderDetail> detailList = MapstructUtils.convert(detailBoList, BorrowOrderDetail.class);
        detailList.forEach(it -> it.setBorrowOrderId(orderId));
        borrowOrderDetailService.saveDetails(detailList);
        for (int i = 0; i < Math.min(detailBoList.size(), detailList.size()); i++) {
            detailBoList.get(i).setId(detailList.get(i).getId());
        }
    }

    private void normalizeDetails(List<BorrowOrderDetailBo> details) {
        if (CollUtil.isEmpty(details)) {
            return;
        }
        Set<Long> skuIds = details.stream().map(BorrowOrderDetailBo::getSkuId).filter(Objects::nonNull).collect(Collectors.toSet());
        Map<Long, ItemSkuVo> skuMap = skuIds.isEmpty() ? Map.of() :
            itemSkuService.queryVosByIds(skuIds).stream()
                .collect(Collectors.toMap(ItemSkuVo::getId, Function.identity()));
        details.forEach(detail -> {
            ItemSkuVo sku = skuMap.get(detail.getSkuId());
            if (sku != null) {
                detail.setSkuName(sku.getSkuName());
                detail.setProductIdentifier(sku.getProductIdentifier());
                detail.setQualityGrade(sku.getQualityGrade());
                if (sku.getItem() != null) {
                    detail.setItemCode(sku.getItem().getItemCode());
                    detail.setItemName(sku.getItem().getItemName());
                    detail.setUnit(sku.getItem().getUnit());
                }
            }
        });
    }

    private String generateBorrowOrderNo() {
        String code = codeRuleService.generateCode("borrow");
        return code != null ? code : "BO" + IdUtil.getSnowflakeNextIdStr();
    }

    private void validateBorrowOrderNo(String borrowOrderNo) {
        LambdaQueryWrapper<BorrowOrder> lqw = Wrappers.lambdaQuery();
        lqw.eq(BorrowOrder::getBorrowOrderNo, borrowOrderNo);
        BorrowOrder existing = borrowOrderMapper.selectOne(lqw);
        Assert.isNull(existing, "系统生成的借用单号重复，请稍后重试");
    }

    private com.ruoyi.wms.domain.entity.BorrowRecord findActiveBorrowRecord(String instanceCode) {
        // Check old borrow_record table for active borrows
        return null; // Simplified - in production, query BorrowRecordMapper
    }

    private void fillOverdueFields(BorrowOrderVo vo) {
        if (vo.getPlanReturnDate() == null || !ServiceConstants.BorrowOrderStatus.BORROWING.equals(vo.getBorrowOrderStatus())) {
            vo.setOverdueFlag(0);
            vo.setOverdueDays(0);
            return;
        }
        int overdueDays = (int) java.time.temporal.ChronoUnit.DAYS.between(
            vo.getPlanReturnDate().atStartOfDay(), LocalDateTime.now());
        vo.setOverdueFlag(overdueDays > 0 ? 1 : 0);
        vo.setOverdueDays(Math.max(overdueDays, 0));
    }

    private InventoryHistory buildBorrowHistory(BorrowOrder order, BorrowOrderDetailVo detail, ItemInstance itemInstance) {
        InventoryHistory history = new InventoryHistory();
        history.setOrderId(order.getId());
        history.setOrderNo(StrUtil.blankToDefault(order.getBorrowOrderNo(), String.valueOf(order.getId())));
        history.setOrderType(ServiceConstants.InventoryHistoryOrderType.BORROW);
        history.setSkuId(detail.getSkuId());
        history.setQuantity(BigDecimal.ONE.negate());
        history.setWarehouseId(detail.getWarehouseId() != null ? detail.getWarehouseId() : itemInstance.getWarehouseId());
        history.setAreaId(detail.getAreaId() != null ? detail.getAreaId() : itemInstance.getAreaId());
        history.setRackId(detail.getRackId() != null ? detail.getRackId() : itemInstance.getRackId());
        history.setLocationId(detail.getLocationId() != null ? detail.getLocationId() : itemInstance.getLocationId());
        history.setInstanceCode(detail.getInstanceCode());
        history.setBoxId(itemInstance.getBoxId());
        history.setOperationType("borrow_order");
        history.setRemark("借用单借出");
        return history;
    }

    private InventoryHistory buildReturnHistory(BorrowOrder order, BorrowOrderDetailVo detail, ItemInstance itemInstance) {
        InventoryHistory history = new InventoryHistory();
        history.setOrderId(order.getId());
        history.setOrderNo(StrUtil.blankToDefault(order.getBorrowOrderNo(), String.valueOf(order.getId())));
        history.setOrderType(ServiceConstants.InventoryHistoryOrderType.RETURN);
        history.setSkuId(detail.getSkuId());
        history.setQuantity(BigDecimal.ONE);
        history.setWarehouseId(detail.getWarehouseId() != null ? detail.getWarehouseId() : itemInstance.getWarehouseId());
        history.setAreaId(detail.getAreaId() != null ? detail.getAreaId() : itemInstance.getAreaId());
        history.setRackId(detail.getRackId() != null ? detail.getRackId() : itemInstance.getRackId());
        history.setLocationId(detail.getLocationId() != null ? detail.getLocationId() : itemInstance.getLocationId());
        history.setInstanceCode(detail.getInstanceCode());
        history.setBoxId(itemInstance.getBoxId());
        history.setOperationType("borrow_order_return");
        history.setRemark("借用单归还");
        return history;
    }
}
