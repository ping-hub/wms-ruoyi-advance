package com.ruoyi.wms.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.ruoyi.common.satoken.utils.LoginHelper;
import com.ruoyi.wms.domain.entity.ShipmentOrder;
import com.ruoyi.wms.domain.entity.Warehouse;
import com.ruoyi.wms.domain.vo.MyTaskVo;
import com.ruoyi.wms.mapper.ShipmentOrderMapper;
import com.ruoyi.wms.mapper.WarehouseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    private final WarehouseMapper warehouseMapper;

    /**
     * 查询当前用户的待办列表（分页）
     *
     * @param taskType  待办类型筛选：pending_approval / pending_execute / rejected / null=全部
     * @param orderNo   单号模糊搜索
     * @param pageQuery 分页参数
     */
    public TableDataInfo<MyTaskVo> queryMyTasks(String taskType, String orderNo, PageQuery pageQuery) {
        Long userId = LoginHelper.getUserId();

        // 构建出库单查询条件
        LambdaQueryWrapper<ShipmentOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(w -> {
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
        if (orderNo != null && !orderNo.isEmpty()) {
            wrapper.like(ShipmentOrder::getShipmentOrderNo, orderNo);
        }
        wrapper.orderByDesc(ShipmentOrder::getCreateTime);

        // 分页查询
        Page<ShipmentOrder> page = shipmentOrderMapper.selectPage(pageQuery.build(), wrapper);

        // 批量获取仓库名称
        Set<Long> warehouseIds = page.getRecords().stream()
            .map(ShipmentOrder::getWarehouseId)
            .filter(id -> id != null)
            .collect(Collectors.toSet());
        Map<Long, String> warehouseNameMap = Map.of();
        if (!warehouseIds.isEmpty()) {
            List<Warehouse> warehouses = warehouseMapper.selectBatchIds(warehouseIds);
            warehouseNameMap = warehouses.stream()
                .collect(Collectors.toMap(Warehouse::getId, Warehouse::getWarehouseName, (a, b) -> a));
        }

        // 转换为 MyTaskVo
        Map<Long, String> finalWarehouseNameMap = warehouseNameMap;
        List<MyTaskVo> voList = new ArrayList<>();
        for (ShipmentOrder order : page.getRecords()) {
            MyTaskVo vo = new MyTaskVo();
            vo.setOrderType("shipment");
            vo.setOrderTypeLabel("出库单");
            vo.setOrderId(order.getId());
            vo.setOrderNo(order.getShipmentOrderNo());
            vo.setApplicantName(order.getApplicantName());
            vo.setWarehouseName(order.getWarehouseId() != null
                ? finalWarehouseNameMap.getOrDefault(order.getWarehouseId(), "-") : "-");
            vo.setCreateTime(order.getCreateTime());
            vo.setRemark(order.getRemark());

            // 根据状态映射待办类型
            int status = order.getShipmentOrderStatus();
            if (status == 1) {
                vo.setTaskType("pending_approval");
                vo.setTaskLabel("待审批");
                vo.setActionLabel("去审批");
            } else if (status == 2) {
                vo.setTaskType("pending_execute");
                vo.setTaskLabel("待出库");
                vo.setActionLabel("去出库");
            } else if (status == -2) {
                vo.setTaskType("rejected");
                vo.setTaskLabel("已驳回待修改");
                vo.setActionLabel("去修改");
            }
            voList.add(vo);
        }

        TableDataInfo<MyTaskVo> result = new TableDataInfo<>(voList, page.getTotal());
        return result;
    }
}
