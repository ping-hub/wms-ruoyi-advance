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
import com.ruoyi.system.service.SysConfigService;
import com.ruoyi.wms.domain.bo.BorrowOrderBo;
import com.ruoyi.wms.domain.bo.BorrowOrderDetailBo;
import com.ruoyi.wms.domain.entity.Box;
import com.ruoyi.wms.domain.entity.BorrowOrder;
import com.ruoyi.wms.domain.entity.BorrowOrderDetail;
import com.ruoyi.wms.domain.bo.InventoryBo;
import com.ruoyi.wms.domain.entity.InventoryHistory;
import com.ruoyi.wms.domain.entity.ItemInstance;
import com.ruoyi.wms.domain.vo.BorrowOrderDetailVo;
import com.ruoyi.wms.domain.vo.BorrowOrderVo;
import com.ruoyi.wms.domain.vo.BorrowOrderWarningStatsVo;
import com.ruoyi.wms.domain.vo.ItemSkuVo;
import com.ruoyi.wms.mapper.BorrowOrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
    private final BoxService boxService;
    private final com.ruoyi.wms.service.BoxCirculationLogService boxCirculationLogService;
    private final SysConfigService sysConfigService;

    public BorrowOrderVo queryById(Long id) {
        BorrowOrderVo vo = borrowOrderMapper.selectVoById(id);
        if (vo == null) {
            throw new BaseException("借用单不存在");
        }
        vo.setDetails(borrowOrderDetailService.queryByBorrowOrderId(id));
        fillWarningFields(vo);
        return vo;
    }

    public TableDataInfo<BorrowOrderVo> queryPageList(BorrowOrderBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<BorrowOrder> lqw = buildQueryWrapper(bo);
        Page<BorrowOrderVo> result = borrowOrderMapper.selectVoPage(pageQuery.build(), lqw);
        result.getRecords().forEach(this::fillWarningFields);
        return TableDataInfo.build(result);
    }

    public List<BorrowOrderVo> queryList(BorrowOrderBo bo) {
        LambdaQueryWrapper<BorrowOrder> lqw = buildQueryWrapper(bo);
        List<BorrowOrderVo> list = borrowOrderMapper.selectVoList(lqw);
        list.forEach(this::fillWarningFields);
        return list;
    }

    /**
     * 查询借用单预警统计
     */
    public BorrowOrderWarningStatsVo queryWarningStats() {
        BorrowOrderWarningStatsVo statsVo = new BorrowOrderWarningStatsVo();
        LocalDate today = LocalDate.now();
        int warningDays = getBorrowWarningDays();

        // 借出中的借用单数量
        LambdaQueryWrapper<BorrowOrder> borrowingWrapper = Wrappers.lambdaQuery();
        borrowingWrapper.eq(BorrowOrder::getBorrowOrderStatus, ServiceConstants.BorrowOrderStatus.BORROWING);
        statsVo.setBorrowingCount(borrowOrderMapper.selectCount(borrowingWrapper));

        // 已超期的借用单数量（借出中 且 planReturnDate < today）
        LambdaQueryWrapper<BorrowOrder> overdueWrapper = Wrappers.lambdaQuery();
        overdueWrapper.eq(BorrowOrder::getBorrowOrderStatus, ServiceConstants.BorrowOrderStatus.BORROWING);
        overdueWrapper.lt(BorrowOrder::getPlanReturnDate, today);
        statsVo.setOverdueCount(borrowOrderMapper.selectCount(overdueWrapper));

        // 即将超时的借用单数量（借出中 且 today ≤ planReturnDate ≤ today+warningDays）
        LambdaQueryWrapper<BorrowOrder> warningWrapper = Wrappers.lambdaQuery();
        warningWrapper.eq(BorrowOrder::getBorrowOrderStatus, ServiceConstants.BorrowOrderStatus.BORROWING);
        warningWrapper.ge(BorrowOrder::getPlanReturnDate, today);
        warningWrapper.le(BorrowOrder::getPlanReturnDate, today.plusDays(warningDays));
        statsVo.setWarningCount(borrowOrderMapper.selectCount(warningWrapper));

        return statsVo;
    }

    /**
     * 借用单预警明细列表（借出中 且 即将到期或已超期）
     */
    public TableDataInfo<BorrowOrderVo> queryWarningList(BorrowOrderBo bo, PageQuery pageQuery) {
        LocalDate today = LocalDate.now();
        int warningDays = getBorrowWarningDays();
        LambdaQueryWrapper<BorrowOrder> lqw = buildQueryWrapper(bo);
        lqw.eq(BorrowOrder::getBorrowOrderStatus, ServiceConstants.BorrowOrderStatus.BORROWING);
        lqw.le(BorrowOrder::getPlanReturnDate, today.plusDays(warningDays));
        lqw.orderByAsc(BorrowOrder::getPlanReturnDate);
        Page<BorrowOrderVo> result = borrowOrderMapper.selectVoPage(pageQuery.build(), lqw);
        result.getRecords().forEach(this::fillWarningFields);
        return TableDataInfo.build(result);
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
        List<BorrowOrderDetailVo> details = borrowOrderDetailService.queryByBorrowOrderId(bo.getId());
        Assert.notEmpty(details, "器材明细不能为空");
        Set<String> instanceCodes = details.stream()
            .map(BorrowOrderDetailVo::getInstanceCode).filter(Objects::nonNull).collect(Collectors.toSet());
        Map<String, ItemInstance> itemMap = instanceCodes.isEmpty() ? Map.of() :
            itemInstanceService.queryByInstanceCodes(instanceCodes).stream()
                .collect(Collectors.toMap(ItemInstance::getInstanceCode, Function.identity()));

        List<InventoryBo> inventoryAdjustments = new ArrayList<>();
        List<ItemInstance> instanceUpdates = new ArrayList<>();
        Set<Long> affectedBoxIds = new HashSet<>();
        // 提前计算随箱出库的箱子（用于过滤 BOX_REMOVE 日志和箱子状态同步）
        Set<Long> outboundBoxIds = (bo.getDetails() != null) ? bo.getDetails().stream()
            .filter(d -> d.getBoxId() != null && Boolean.TRUE.equals(d.getBoxOutbound()))
            .map(BorrowOrderDetailBo::getBoxId)
            .collect(Collectors.toSet()) : java.util.Collections.emptySet();
        List<BorrowOrderDetail> detailUpdates = new ArrayList<>();
        List<InventoryHistory> historyList = new ArrayList<>();

        for (BorrowOrderDetailVo detail : details) {
            ItemInstance itemInstance = itemMap.get(detail.getInstanceCode());
            Assert.notNull(itemInstance, "器材不存在：" + detail.getInstanceCode());
            Assert.isTrue(ServiceConstants.ItemInstanceStatus.IN_STOCK.equals(itemInstance.getInstanceStatus()),
                "器材 " + detail.getInstanceCode() + " 不在库，无法借出");
            Assert.isTrue(itemInstance.getShipmentOrderDetailId() == null, "器材 " + detail.getInstanceCode() + " 已被出库单占用");
            Assert.isTrue(itemInstance.getMovementOrderDetailId() == null, "器材 " + detail.getInstanceCode() + " 已被调拨单占用");
            Assert.isTrue(findActiveBorrowRecord(detail.getInstanceCode()) == null,
                "器材 " + detail.getInstanceCode() + " 已处于借出状态");
            checkOrderService.assertNoActiveCheckOrder(itemInstance.getWarehouseId(), itemInstance.getAreaId(), itemInstance.getRackId());

            InventoryBo adj = new InventoryBo();
            adj.setWarehouseId(itemInstance.getWarehouseId());
            adj.setAreaId(itemInstance.getAreaId());
            adj.setRackId(itemInstance.getRackId());
            adj.setLocationId(itemInstance.getLocationId());
            adj.setSkuId(itemInstance.getSkuId());
            adj.setQuantity(BigDecimal.ONE.negate());
            inventoryAdjustments.add(adj);

            if (itemInstance.getBoxId() != null) {
                affectedBoxIds.add(itemInstance.getBoxId());
                // 记录器材移出箱体日志（随箱出库的不记录，由 markOutbound 管理）
                if (!outboundBoxIds.contains(itemInstance.getBoxId())) {
                    boxCirculationLogService.logEvent(
                        itemInstance.getBoxId(), null, "BOX_REMOVE",
                        bo.getId(), "BORROW_ORDER",
                        null, null,
                        "器材 " + itemInstance.getInstanceCode() + " 借用移出箱体"
                    );
                }
            }
            itemInstance.setInstanceStatus(ServiceConstants.ItemInstanceStatus.BORROWED);
            itemInstance.setBoxId(null);
            itemInstance.setWarehouseId(null);
            itemInstance.setAreaId(null);
            itemInstance.setRackId(null);
            itemInstance.setLocationId(null);
            instanceUpdates.add(itemInstance);

            BorrowOrderDetail updateDetail = new BorrowOrderDetail();
            updateDetail.setId(detail.getId());
            updateDetail.setReturnStatus(ServiceConstants.BorrowDetailReturnStatus.NOT_RETURNED);
            detailUpdates.add(updateDetail);

            historyList.add(buildBorrowHistory(existing, detail, itemInstance));
        }

        inventoryService.updateInventoryQuantity(inventoryAdjustments);
        if (!instanceUpdates.isEmpty()) itemInstanceService.updateBatchById(instanceUpdates);
        affectedBoxIds.stream().filter(bid -> !outboundBoxIds.contains(bid))
            .forEach(boxId -> itemInstanceService.syncBoxAfterItemLeave(boxId));
        // 箱子随借用单出库：outboundBoxIds 已在循环前计算
        outboundBoxIds.forEach(boxId -> boxService.markOutbound(boxId, bo.getId(), "BORROW_ORDER"));
        if (!detailUpdates.isEmpty()) borrowOrderDetailService.updateBatchById(detailUpdates);
        if (!historyList.isEmpty()) inventoryHistoryService.saveBatch(historyList);

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
        Set<String> returnInstanceCodes = details.stream()
            .filter(d -> !ServiceConstants.BorrowDetailReturnStatus.RETURNED.equals(d.getReturnStatus()))
            .map(BorrowOrderDetailVo::getInstanceCode).filter(Objects::nonNull).collect(Collectors.toSet());
        Map<String, ItemInstance> returnItemMap = returnInstanceCodes.isEmpty() ? Map.of() :
            itemInstanceService.queryByInstanceCodes(returnInstanceCodes).stream()
                .collect(Collectors.toMap(ItemInstance::getInstanceCode, Function.identity()));

        List<InventoryBo> inventoryAdjustments = new ArrayList<>();
        List<ItemInstance> instanceUpdates = new ArrayList<>();
        List<BorrowOrderDetail> detailUpdates = new ArrayList<>();
        List<InventoryHistory> historyList = new ArrayList<>();

        for (BorrowOrderDetailVo detail : details) {
            if (ServiceConstants.BorrowDetailReturnStatus.RETURNED.equals(detail.getReturnStatus())) {
                continue;
            }
            ItemInstance itemInstance = returnItemMap.get(detail.getInstanceCode());
            Assert.notNull(itemInstance, "器材不存在：" + detail.getInstanceCode());

            InventoryBo adj = new InventoryBo();
            adj.setWarehouseId(detail.getWarehouseId());
            adj.setAreaId(detail.getAreaId());
            adj.setRackId(detail.getRackId());
            adj.setLocationId(detail.getLocationId());
            adj.setSkuId(itemInstance.getSkuId());
            adj.setQuantity(BigDecimal.ONE);
            inventoryAdjustments.add(adj);

            itemInstance.setInstanceStatus(ServiceConstants.ItemInstanceStatus.IN_STOCK);
            itemInstance.setWarehouseId(detail.getWarehouseId());
            itemInstance.setAreaId(detail.getAreaId());
            itemInstance.setRackId(detail.getRackId());
            itemInstance.setLocationId(detail.getLocationId());
            itemInstance.setBoxId(null);
            instanceUpdates.add(itemInstance);

            BorrowOrderDetail updateDetail = new BorrowOrderDetail();
            updateDetail.setId(detail.getId());
            updateDetail.setReturnStatus(ServiceConstants.BorrowDetailReturnStatus.RETURNED);
            updateDetail.setReturnTime(now);
            detailUpdates.add(updateDetail);

            historyList.add(buildReturnHistory(existing, detail, itemInstance));
        }

        inventoryService.updateInventoryQuantity(inventoryAdjustments);
        if (!instanceUpdates.isEmpty()) itemInstanceService.updateBatchById(instanceUpdates);
        if (!detailUpdates.isEmpty()) borrowOrderDetailService.updateBatchById(detailUpdates);
        if (!historyList.isEmpty()) inventoryHistoryService.saveBatch(historyList);

        Set<Long> locationIds = details.stream()
            .map(BorrowOrderDetailVo::getLocationId).filter(Objects::nonNull).collect(Collectors.toSet());
        if (!locationIds.isEmpty()) {
            locationService.refreshOccupiedFlagsByLocationIds(locationIds);
        }
        // 箱子随借用单归还：查找由本借用单标记为出库的箱子
        List<Box> borrowBoxes = boxService.list(Wrappers.<Box>lambdaQuery()
            .eq(Box::getOutboundOrderId, orderId)
            .eq(Box::getOutboundOrderType, "BORROW_ORDER"));
        borrowBoxes.forEach(box -> boxService.markReturn(box.getId()));

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
        Set<String> instanceCodes = details.stream().map(BorrowOrderDetailBo::getInstanceCode).filter(Objects::nonNull).collect(Collectors.toSet());
        Map<String, ItemInstance> instMap = instanceCodes.isEmpty() ? Map.of() :
            itemInstanceService.queryByInstanceCodes(instanceCodes).stream()
                .collect(Collectors.toMap(ItemInstance::getInstanceCode, Function.identity()));
        details.forEach(detail -> {
            ItemSkuVo sku = skuMap.get(detail.getSkuId());
            if (sku != null) {
                detail.setSkuName(sku.getSkuName());
                detail.setProductIdentifier(sku.getProductIdentifier());
                if (sku.getItem() != null) {
                    detail.setItemCode(sku.getItem().getItemCode());
                    detail.setItemName(sku.getItem().getItemName());
                    detail.setUnit(sku.getItem().getUnit());
                }
            }
            ItemInstance inst = detail.getInstanceCode() != null ? instMap.get(detail.getInstanceCode()) : null;
            if (inst != null) {
                detail.setQualityGrade(inst.getQualityGrade());
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
        return null;
    }

    /**
     * 填充预警字段：warningFlag 0=正常, 1=预警(即将超时), 2=超时(已超期)
     */
    private void fillWarningFields(BorrowOrderVo vo) {
        if (vo.getPlanReturnDate() == null || !ServiceConstants.BorrowOrderStatus.BORROWING.equals(vo.getBorrowOrderStatus())) {
            vo.setWarningFlag(0);
            vo.setOverdueDays(0);
            return;
        }
        LocalDate today = LocalDate.now();
        int warningDays = getBorrowWarningDays();
        int daysUntilReturn = (int) ChronoUnit.DAYS.between(today, vo.getPlanReturnDate());

        if (daysUntilReturn < 0) {
            // 已超期：planReturnDate < today
            vo.setWarningFlag(2);
            vo.setOverdueDays(Math.abs(daysUntilReturn));
        } else if (daysUntilReturn <= warningDays) {
            // 预警：today ≤ planReturnDate ≤ today+warningDays
            vo.setWarningFlag(1);
            vo.setOverdueDays(0);
        } else {
            // 正常
            vo.setWarningFlag(0);
            vo.setOverdueDays(0);
        }
    }

    /**
     * 读取借用单超时预警天数阈值（与 MyTaskService 共用 sys_config key）
     */
    private int getBorrowWarningDays() {
        try {
            String configValue = sysConfigService.selectConfigByKey("wms.borrow.timeout.warning.days");
            if (configValue != null && !configValue.isBlank()) {
                return Integer.parseInt(configValue.trim());
            }
        } catch (Exception e) {
            // ignore, use default
        }
        return 3;
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
