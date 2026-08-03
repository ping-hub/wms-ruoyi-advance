package com.ruoyi.wms.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.ruoyi.common.satoken.utils.LoginHelper;
import com.ruoyi.system.service.SysConfigService;
import com.ruoyi.wms.domain.entity.BorrowOrder;
import com.ruoyi.wms.domain.entity.CheckOrder;
import com.ruoyi.wms.domain.entity.ShipmentOrder;
import com.ruoyi.wms.domain.entity.Warehouse;
import com.ruoyi.wms.domain.vo.MyTaskVo;
import com.ruoyi.wms.mapper.BorrowOrderMapper;
import com.ruoyi.wms.mapper.CheckOrderMapper;
import com.ruoyi.wms.mapper.ShipmentOrderMapper;
import com.ruoyi.wms.mapper.WarehouseMapper;
import com.ruoyi.wms.service.InventoryWarningRuleService;
import com.ruoyi.wms.mapper.DashboardMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 我的待办服务
 *
 * @author ping
 * @date 2026-06-17
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MyTaskService {

    private final ShipmentOrderMapper shipmentOrderMapper;
    private final CheckOrderMapper checkOrderMapper;
    private final BorrowOrderMapper borrowOrderMapper;
    private final WarehouseMapper warehouseMapper;
    private final SysConfigService sysConfigService;
    private final DashboardMapper dashboardMapper;
    private final InventoryWarningRuleService inventoryWarningRuleService;

    /** 借用单超时预警天数阈值（sys_config key） */
    private static final String BORROW_TIMEOUT_CONFIG_KEY = "wms.borrow.timeout.warning.days";
    private static final int BORROW_TIMEOUT_DEFAULT_DAYS = 3;

    /**
     * 查询当前用户的待办列表（分页）
     *
     * @param status    状态：pending=待办 / done=已办结
     * @param taskType  待办类型筛选：pending_approval / pending_execute / pending_review / rejected / borrow_timeout / null=全部
     * @param orderNo   单号模糊搜索
     * @param pageQuery 分页参数
     */
    public TableDataInfo<MyTaskVo> queryMyTasks(String status, String taskType, String orderNo, PageQuery pageQuery) {
        boolean isDone = "done".equals(status);
        Long userId = LoginHelper.getUserId();
        String username = LoginHelper.getUsername();
        List<MyTaskVo> allTasks = new ArrayList<>();
        Set<Long> allWarehouseIds = new HashSet<>();

        // ========== 出库单 ==========
        LambdaQueryWrapper<ShipmentOrder> sw = new LambdaQueryWrapper<>();
        if (isDone) {
            // 已办结：我参与的且已完成的单据（status=3）
            sw.and(w -> w
                .or(o -> o.eq(ShipmentOrder::getApplicantId, userId))
                .or(o -> o.eq(ShipmentOrder::getApproverId, userId))
                .or(o -> o.eq(ShipmentOrder::getExecutorId, userId))
            );
            sw.eq(ShipmentOrder::getShipmentOrderStatus, 3);
        } else {
            // 阶段1：出库单为一步完成模式，无中间待办状态，跳过待办查询
            sw.eq(ShipmentOrder::getShipmentOrderStatus, -999); // 永不匹配
        }
        if (orderNo != null && !orderNo.isEmpty()) {
            sw.like(ShipmentOrder::getShipmentOrderNo, orderNo);
        }
        sw.orderByDesc(isDone ? ShipmentOrder::getUpdateTime : ShipmentOrder::getCreateTime);
        sw.last("LIMIT 500"); // 防止单用户数据量过大导致内存膨胀
        List<ShipmentOrder> shipmentOrders = shipmentOrderMapper.selectList(sw);
        for (ShipmentOrder order : shipmentOrders) {
            MyTaskVo vo = new MyTaskVo();
            vo.setOrderType("shipment");
            vo.setOrderTypeLabel("出库单");
            vo.setOrderId(order.getId());
            vo.setOrderNo(order.getShipmentOrderNo());
            vo.setApplicantName(order.getApplicantName());
            vo.setCreateTime(order.getCreateTime());
            vo.setRemark(order.getRemark());
            int orderStatus = order.getShipmentOrderStatus();
            if (isDone) {
                vo.setTaskType("done");
                vo.setTaskLabel("已完成");
                vo.setActionLabel("查看");
                vo.setFinishTime(order.getUpdateTime());
            } else if (orderStatus == 1) {
                vo.setTaskType("pending_approval");
                vo.setTaskLabel("待审批");
                vo.setActionLabel("去审批");
            } else if (orderStatus == 2) {
                vo.setTaskType("pending_execute");
                vo.setTaskLabel("待出库");
                vo.setActionLabel("去出库");
            } else if (orderStatus == -2) {
                vo.setTaskType("rejected");
                vo.setTaskLabel("已驳回待修改");
                vo.setActionLabel("去修改");
            }
            allTasks.add(vo);
        }

        // ========== 盘点单 ==========
        LambdaQueryWrapper<CheckOrder> cw = new LambdaQueryWrapper<>();
        if (isDone) {
            // 已办结：我参与的且已完成的单据（status=3）
            cw.and(w -> w
                .or(o -> o.eq(CheckOrder::getCreateBy, username))
                .or(o -> o.eq(CheckOrder::getExecutorId, userId))
                .or(o -> o.eq(CheckOrder::getReviewerId, userId))
            );
            cw.eq(CheckOrder::getCheckOrderStatus, 3);
        } else {
            // 阶段1：盘点单为一步完成模式，无中间待办状态，跳过待办查询
            cw.eq(CheckOrder::getCheckOrderStatus, -999); // 永不匹配
        }
        if (orderNo != null && !orderNo.isEmpty()) {
            cw.like(CheckOrder::getCheckOrderNo, orderNo);
        }
        cw.orderByDesc(isDone ? CheckOrder::getUpdateTime : CheckOrder::getCreateTime);
        cw.last("LIMIT 500"); // 防止单用户数据量过大导致内存膨胀
        List<CheckOrder> checkOrders = checkOrderMapper.selectList(cw);
        for (CheckOrder order : checkOrders) {
            MyTaskVo vo = new MyTaskVo();
            vo.setOrderType("check");
            vo.setOrderTypeLabel("盘点单");
            vo.setOrderId(order.getId());
            vo.setOrderNo(order.getCheckOrderNo());
            vo.setApplicantName(order.getApplicantName());
            if (order.getWarehouseId() != null) allWarehouseIds.add(order.getWarehouseId());
            vo.setCreateTime(order.getCreateTime());
            vo.setRemark(order.getRemark());
            int orderStatus = order.getCheckOrderStatus();
            if (isDone) {
                vo.setTaskType("done");
                vo.setTaskLabel("已完成");
                vo.setActionLabel("查看");
                vo.setFinishTime(order.getUpdateTime());
            } else if (orderStatus == 1) {
                vo.setTaskType("pending_execute");
                vo.setTaskLabel("待盘点");
                vo.setActionLabel("去盘点");
            } else if (orderStatus == 2) {
                vo.setTaskType("pending_review");
                vo.setTaskLabel("待复核");
                vo.setActionLabel("去复核");
            } else if (orderStatus == -2) {
                vo.setTaskType("rejected");
                vo.setTaskLabel("已驳回待修改");
                vo.setActionLabel("去修改");
            }
            allTasks.add(vo);
        }

        // ========== 借用单（超时预警） ==========
        LambdaQueryWrapper<BorrowOrder> bw = new LambdaQueryWrapper<>();
        if (isDone) {
            // 已办结：我是创建人的已归还/已作废借用单
            bw.and(w -> w
                .or(o -> o.eq(BorrowOrder::getCreateBy, username))
            );
            bw.in(BorrowOrder::getBorrowOrderStatus, 2, -1); // 已归还、已作废
        } else {
            // 读取超时阈值
            int warningDays = getBorrowTimeoutDays();
            bw.and(w -> {
                // 场景：我是创建人，借用中 + 计划归还日期距今 ≤ warningDays 天（含已超期）
                if (taskType == null || "borrow_timeout".equals(taskType)) {
                    w.or(o -> o.eq(BorrowOrder::getCreateBy, username)
                        .eq(BorrowOrder::getBorrowOrderStatus, 1)
                        .le(BorrowOrder::getPlanReturnDate, LocalDate.now().plusDays(warningDays))
                    );
                }
            });
        }
        if (orderNo != null && !orderNo.isEmpty()) {
            bw.like(BorrowOrder::getBorrowOrderNo, orderNo);
        }
        bw.orderByDesc(isDone ? BorrowOrder::getUpdateTime : BorrowOrder::getCreateTime);
        bw.last("LIMIT 500");
        List<BorrowOrder> borrowOrders = borrowOrderMapper.selectList(bw);
        for (BorrowOrder order : borrowOrders) {
            MyTaskVo vo = new MyTaskVo();
            vo.setOrderType("borrowOrder");
            vo.setOrderTypeLabel("借用单");
            vo.setOrderId(order.getId());
            vo.setOrderNo(order.getBorrowOrderNo());
            vo.setApplicantName(order.getCreateBy());
            vo.setCreateTime(order.getCreateTime());
            vo.setRemark(order.getRemark());
            if (isDone) {
                vo.setTaskType("done");
                vo.setTaskLabel("已完成");
                vo.setActionLabel("查看");
                vo.setFinishTime(order.getUpdateTime());
            } else {
                // 计算剩余天数
                LocalDate planDate = order.getPlanReturnDate();
                long daysRemaining = planDate != null ? ChronoUnit.DAYS.between(LocalDate.now(), planDate) : 0;
                vo.setTaskType("borrow_timeout");
                if (daysRemaining < 0) {
                    vo.setTaskLabel("已超时" + Math.abs(daysRemaining) + "天");
                } else if (daysRemaining == 0) {
                    vo.setTaskLabel("今日到期");
                } else {
                    vo.setTaskLabel("即将超时（剩" + daysRemaining + "天）");
                }
                vo.setActionLabel("去处理");
            }
            allTasks.add(vo);
        }

        // ========== 合并排序 + 手动分页 ==========
        allTasks.sort((a, b) -> {
            if (isDone) {
                // 已办结按办结时间倒序
                if (a.getFinishTime() == null && b.getFinishTime() == null) return 0;
                if (a.getFinishTime() == null) return 1;
                if (b.getFinishTime() == null) return -1;
                return b.getFinishTime().compareTo(a.getFinishTime());
            } else {
                if (a.getCreateTime() == null && b.getCreateTime() == null) return 0;
                if (a.getCreateTime() == null) return 1;
                if (b.getCreateTime() == null) return -1;
                return b.getCreateTime().compareTo(a.getCreateTime());
            }
        });

        // 批量获取仓库名称
        Map<Long, String> warehouseNameMap = Map.of();
        if (!allWarehouseIds.isEmpty()) {
            List<Warehouse> warehouses = warehouseMapper.selectBatchIds(allWarehouseIds);
            warehouseNameMap = warehouses.stream()
                .collect(Collectors.toMap(Warehouse::getId, Warehouse::getWarehouseName, (a, b) -> a));
        }
        Map<Long, String> finalWarehouseNameMap = warehouseNameMap;
        Map<Long, Long> checkWarehouseMap = checkOrders.stream()
            .filter(c -> c.getWarehouseId() != null)
            .collect(java.util.stream.Collectors.toMap(CheckOrder::getId, CheckOrder::getWarehouseId, (a, b) -> a));
        allTasks.forEach(vo -> {
            Long wid = null;
            if ("check".equals(vo.getOrderType())) {
                wid = checkWarehouseMap.get(vo.getOrderId());
            }
            vo.setWarehouseName(wid != null ? finalWarehouseNameMap.getOrDefault(wid, "-") : "-");
        });

        // 手动分页
        long total = allTasks.size();
        int pageNum = pageQuery.getPageNum() != null ? pageQuery.getPageNum() : 1;
        int pageSize = pageQuery.getPageSize() != null ? pageQuery.getPageSize() : 10;
        int fromIndex = (pageNum - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, allTasks.size());
        List<MyTaskVo> pagedList = fromIndex < allTasks.size()
            ? allTasks.subList(fromIndex, toIndex)
            : new ArrayList<>();

        return new TableDataInfo<>(pagedList, total);
    }

    /**
     * 统计待办/已办结数量（看板用）
     *
     * @return Map: pendingCount, doneCount, percent
     */
    public java.util.Map<String, Object> getMyTasksSummary() {
        Long userId = LoginHelper.getUserId();
        String username = LoginHelper.getUsername();

        // 阶段1：出库单/盘点单为一步完成模式，无中间待办状态
        long pendingShipment = 0;
        long pendingCheck = 0;

        // 借用单超时待办
        int warningDays = getBorrowTimeoutDays();
        LambdaQueryWrapper<BorrowOrder> pendingBw = new LambdaQueryWrapper<>();
        pendingBw.eq(BorrowOrder::getCreateBy, username);
        pendingBw.eq(BorrowOrder::getBorrowOrderStatus, 1);
        pendingBw.le(BorrowOrder::getPlanReturnDate, LocalDate.now().plusDays(warningDays));
        long pendingBorrow = borrowOrderMapper.selectCount(pendingBw);

        long pendingCount = pendingShipment + pendingCheck + pendingBorrow;

        // 已办结数量：出库单已完成 + 盘点单已完成 + 借用单已完成
        LambdaQueryWrapper<ShipmentOrder> doneSw = new LambdaQueryWrapper<>();
        doneSw.and(w -> w
            .or(o -> o.eq(ShipmentOrder::getApplicantId, userId))
            .or(o -> o.eq(ShipmentOrder::getApproverId, userId))
            .or(o -> o.eq(ShipmentOrder::getExecutorId, userId))
        );
        doneSw.eq(ShipmentOrder::getShipmentOrderStatus, 3);
        long doneShipment = shipmentOrderMapper.selectCount(doneSw);

        LambdaQueryWrapper<CheckOrder> doneCw = new LambdaQueryWrapper<>();
        doneCw.and(w -> w
            .or(o -> o.eq(CheckOrder::getCreateBy, username))
            .or(o -> o.eq(CheckOrder::getExecutorId, userId))
            .or(o -> o.eq(CheckOrder::getReviewerId, userId))
        );
        doneCw.eq(CheckOrder::getCheckOrderStatus, 3);
        long doneCheck = checkOrderMapper.selectCount(doneCw);

        LambdaQueryWrapper<BorrowOrder> doneBw = new LambdaQueryWrapper<>();
        doneBw.eq(BorrowOrder::getCreateBy, username);
        doneBw.in(BorrowOrder::getBorrowOrderStatus, 2, -1);
        long doneBorrow = borrowOrderMapper.selectCount(doneBw);

        long doneCount = doneShipment + doneCheck + doneBorrow;

        // 计算百分比
        long total = pendingCount + doneCount;
        int percent = total > 0 ? (int) Math.round((double) doneCount / total * 100) : 0;

        java.util.Map<String, Object> result = new java.util.HashMap<>();
        result.put("pendingCount", pendingCount);
        result.put("doneCount", doneCount);
        result.put("percent", percent);
        return result;
    }

    /**
     * 读取借用单超时预警天数阈值
     */
    private int getBorrowTimeoutDays() {
        try {
            String configValue = sysConfigService.selectConfigByKey(BORROW_TIMEOUT_CONFIG_KEY);
            if (configValue != null && !configValue.isBlank()) {
                return Integer.parseInt(configValue.trim());
            }
        } catch (Exception e) {
            log.warn("读取 {} 配置失败，使用默认值 {}: {}", BORROW_TIMEOUT_CONFIG_KEY, BORROW_TIMEOUT_DEFAULT_DAYS, e.getMessage());
        }
        return BORROW_TIMEOUT_DEFAULT_DAYS;
    }

    /**
     * 三类预警分类统计
     * borrow: 借用单预警+超期数，inventory: 库存预警数，warranty: 质保期预警数
     */
    public java.util.Map<String, Object> getWarningCategoryStats() {
        LocalDate today = LocalDate.now();
        int warningDays = getBorrowTimeoutDays();

        // 借用单预警（借用中 且 即将到期或已超期）
        LambdaQueryWrapper<BorrowOrder> warnWrapper = new LambdaQueryWrapper<>();
        warnWrapper.eq(BorrowOrder::getBorrowOrderStatus, 1);
        warnWrapper.le(BorrowOrder::getPlanReturnDate, today.plusDays(warningDays));
        long borrowCount = borrowOrderMapper.selectCount(warnWrapper);

        // 库存预警
        Map<String, Object> invSummary = inventoryWarningRuleService.getWarningSummary();
        long inventoryCount = (invSummary.get("total") instanceof Number)
                ? ((Number) invSummary.get("total")).longValue() : 0L;

        // 质保期预警（已到期+本月+下月）
        LocalDate nextMonthStart = today.withDayOfMonth(1).plusMonths(1);
        LocalDate nextNextMonthStart = today.withDayOfMonth(1).plusMonths(2);
        long expired = dashboardMapper.countByWarrantyExpiry("expired", today, nextMonthStart, nextNextMonthStart);
        long thisMonth = dashboardMapper.countByWarrantyExpiry("expiringThisMonth", today, nextMonthStart, nextNextMonthStart);
        long nextMonth = dashboardMapper.countByWarrantyExpiry("expiringNextMonth", today, nextMonthStart, nextNextMonthStart);
        long warrantyCount = expired + thisMonth + nextMonth;

        java.util.Map<String, Object> result = new java.util.LinkedHashMap<>();
        result.put("borrow", borrowCount);
        result.put("inventory", inventoryCount);
        result.put("warranty", warrantyCount);
        return result;
    }

}
