package com.ruoyi.wms.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.ruoyi.common.satoken.utils.LoginHelper;
import com.ruoyi.wms.domain.entity.CheckOrder;
import com.ruoyi.wms.domain.entity.ShipmentOrder;
import com.ruoyi.wms.domain.entity.Warehouse;
import com.ruoyi.wms.domain.vo.MyTaskVo;
import com.ruoyi.wms.mapper.CheckOrderMapper;
import com.ruoyi.wms.mapper.ShipmentOrderMapper;
import com.ruoyi.wms.mapper.WarehouseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
@Service
@RequiredArgsConstructor
public class MyTaskService {

    private final ShipmentOrderMapper shipmentOrderMapper;
    private final CheckOrderMapper checkOrderMapper;
    private final WarehouseMapper warehouseMapper;

    /**
     * 查询当前用户的待办列表（分页）
     *
     * @param taskType  待办类型筛选：pending_approval / pending_execute / pending_review / rejected / null=全部
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
            sw.and(w -> {
                // 场景1：我是审批人，单子待审批（status=1）
                if (taskType == null || "pending_approval".equals(taskType)) {
                    w.or(o -> o.eq(ShipmentOrder::getApproverId, userId)
                        .eq(ShipmentOrder::getShipmentOrderStatus, 1));
                }
                // 场景2：我是操作人，单子待出库（status=2）
                if (taskType == null || "pending_execute".equals(taskType)) {
                    w.or(o -> o.eq(ShipmentOrder::getExecutorId, userId)
                        .eq(ShipmentOrder::getShipmentOrderStatus, 2));
                }
                // 场景3：我是申请人，单子被驳回（status=-2）
                if (taskType == null || "rejected".equals(taskType)) {
                    w.or(o -> o.eq(ShipmentOrder::getApplicantId, userId)
                        .eq(ShipmentOrder::getShipmentOrderStatus, -2));
                }
            });
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
            if (order.getWarehouseId() != null) allWarehouseIds.add(order.getWarehouseId());
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
            cw.and(w -> {
                // 场景1：我是盘点人，单子待盘点（status=1）
                if (taskType == null || "pending_execute".equals(taskType)) {
                    w.or(o -> o.eq(CheckOrder::getExecutorId, userId)
                        .eq(CheckOrder::getCheckOrderStatus, 1));
                }
                // 场景2：我是复核人，单子待复核（status=2）
                if (taskType == null || "pending_review".equals(taskType)) {
                    w.or(o -> o.eq(CheckOrder::getReviewerId, userId)
                        .eq(CheckOrder::getCheckOrderStatus, 2));
                }
                // 场景3：我是创建人，单子被驳回（status=-2）
                if (taskType == null || "rejected".equals(taskType)) {
                    w.or(o -> o.eq(CheckOrder::getCreateBy, username)
                        .eq(CheckOrder::getCheckOrderStatus, -2));
                }
            });
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
        // 建立 orderId → warehouseId 的 HashMap，避免 O(N*M) 嵌套循环
        Map<Long, Long> shipmentWarehouseMap = shipmentOrders.stream()
            .filter(s -> s.getWarehouseId() != null)
            .collect(java.util.stream.Collectors.toMap(ShipmentOrder::getId, ShipmentOrder::getWarehouseId, (a, b) -> a));
        Map<Long, Long> checkWarehouseMap = checkOrders.stream()
            .filter(c -> c.getWarehouseId() != null)
            .collect(java.util.stream.Collectors.toMap(CheckOrder::getId, CheckOrder::getWarehouseId, (a, b) -> a));
        allTasks.forEach(vo -> {
            Long wid = null;
            if ("shipment".equals(vo.getOrderType())) {
                wid = shipmentWarehouseMap.get(vo.getOrderId());
            } else if ("check".equals(vo.getOrderType())) {
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

        // 待办数量：出库单待办 + 盘点单待办
        LambdaQueryWrapper<ShipmentOrder> pendingSw = new LambdaQueryWrapper<>();
        pendingSw.and(w -> w
            .or(o -> o.eq(ShipmentOrder::getApproverId, userId).eq(ShipmentOrder::getShipmentOrderStatus, 1))
            .or(o -> o.eq(ShipmentOrder::getExecutorId, userId).eq(ShipmentOrder::getShipmentOrderStatus, 2))
            .or(o -> o.eq(ShipmentOrder::getApplicantId, userId).eq(ShipmentOrder::getShipmentOrderStatus, -2))
        );
        long pendingShipment = shipmentOrderMapper.selectCount(pendingSw);

        LambdaQueryWrapper<CheckOrder> pendingCw = new LambdaQueryWrapper<>();
        pendingCw.and(w -> w
            .or(o -> o.eq(CheckOrder::getExecutorId, userId).eq(CheckOrder::getCheckOrderStatus, 1))
            .or(o -> o.eq(CheckOrder::getReviewerId, userId).eq(CheckOrder::getCheckOrderStatus, 2))
            .or(o -> o.eq(CheckOrder::getCreateBy, username).eq(CheckOrder::getCheckOrderStatus, -2))
        );
        long pendingCheck = checkOrderMapper.selectCount(pendingCw);
        long pendingCount = pendingShipment + pendingCheck;

        // 已办结数量：出库单已完成 + 盘点单已完成
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
        long doneCount = doneShipment + doneCheck;

        // 计算百分比
        long total = pendingCount + doneCount;
        int percent = total > 0 ? (int) Math.round((double) doneCount / total * 100) : 0;

        java.util.Map<String, Object> result = new java.util.HashMap<>();
        result.put("pendingCount", pendingCount);
        result.put("doneCount", doneCount);
        result.put("percent", percent);
        return result;
    }
}