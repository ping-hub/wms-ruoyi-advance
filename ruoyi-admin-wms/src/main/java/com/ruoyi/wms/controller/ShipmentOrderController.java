package com.ruoyi.wms.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import com.ruoyi.common.excel.utils.ExcelUtil;
import com.ruoyi.common.idempotent.annotation.RepeatSubmit;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.ruoyi.common.web.core.BaseController;
import com.ruoyi.system.domain.bo.SysUserBo;
import com.ruoyi.system.domain.vo.SysUserVo;
import com.ruoyi.system.service.SysUserService;
import com.ruoyi.wms.domain.bo.ShipmentOrderBo;
import com.ruoyi.wms.domain.bo.ShipmentOrderDetailBo;
import com.ruoyi.wms.domain.vo.ShipmentOrderVo;
import com.ruoyi.wms.service.InventoryDetailService;
import com.ruoyi.wms.service.ShipmentOrderService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * 出库单
 *
 * @author ping
 * @date 2024-08-01
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/wms/shipmentOrder")
public class ShipmentOrderController extends BaseController {

    private final ShipmentOrderService shipmentOrderService;
    private final InventoryDetailService inventoryDetailService;
    private final SysUserService sysUserService;

    /**
     * 查询出库单列表
     */
    @SaCheckPermission("wms:shipment:all")
    @GetMapping("/list")
    public TableDataInfo<ShipmentOrderVo> list(ShipmentOrderBo bo, PageQuery pageQuery) {
        return shipmentOrderService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出出库单列表
     */
    @SaCheckPermission("wms:shipment:all")
    @Log(title = "出库单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(ShipmentOrderBo bo, HttpServletResponse response) {
        List<ShipmentOrderVo> list = shipmentOrderService.queryList(bo);
        ExcelUtil.exportExcel(list, "出库单", ShipmentOrderVo.class, response);
    }

    /**
     * 获取出库单详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("wms:shipment:all")
    @GetMapping("/{id}")
    public R<ShipmentOrderVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(shipmentOrderService.queryById(id));
    }

    /**
     * 新增出库单（返回ID，供后续提交审批使用）
     */
    @SaCheckPermission("wms:shipment:all")
    @Log(title = "出库单", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Long> add(@Validated(AddGroup.class) @RequestBody ShipmentOrderBo bo) {
        shipmentOrderService.insertByBo(bo);
        return R.ok(bo.getId());
    }

    /**
     * 修改出库单
     */
    @SaCheckPermission("wms:shipment:all")
    @Log(title = "出库单", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody ShipmentOrderBo bo) {
        shipmentOrderService.updateByBo(bo);
        return R.ok();
    }

    /**
     * 执行出库（状态必须为已审批=2）
     */
    @SaCheckPermission("wms:shipment:execute")
    @Log(title = "出库单", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping("/shipment")
    public R<Void> shipment(@Validated(AddGroup.class) @RequestBody ShipmentOrderBo bo) {
        shipmentOrderService.shipment(bo);
        List<Long> affectedIds = bo.getDetails().stream().map(ShipmentOrderDetailBo::getInventoryDetailId).filter(Objects::nonNull).toList();
        inventoryDetailService.clearByIdsWithZeroRemainQuantity(affectedIds);
        return R.ok();
    }

    /**
     * 提交审批（草稿/已驳回 → 待审批）
     */
    @SaCheckPermission("wms:shipment:submit")
    @Log(title = "出库单", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping("/submit/{id}")
    public R<Void> submit(@NotNull(message = "主键不能为空") @PathVariable Long id,
                          @RequestParam(required = false) Long approverId,
                          @RequestParam(required = false) String approverName) {
        shipmentOrderService.submitForApproval(id, approverId, approverName);
        return R.ok();
    }

    /**
     * 审批通过（待审批 → 已审批）
     */
    @SaCheckPermission("wms:shipment:approve")
    @Log(title = "出库单", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping("/approve/{id}")
    public R<Void> approve(@NotNull(message = "主键不能为空") @PathVariable Long id,
                           @RequestParam(required = false) String remark,
                           @RequestParam(required = false) Long executorId,
                           @RequestParam(required = false) String executorName) {
        shipmentOrderService.approve(id, remark, executorId, executorName);
        return R.ok();
    }

    /**
     * 驳回（待审批 → 已驳回）
     */
    @SaCheckPermission("wms:shipment:approve")
    @Log(title = "出库单", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping("/reject/{id}")
    public R<Void> reject(@NotNull(message = "主键不能为空") @PathVariable Long id,
                          @RequestParam(required = false) String remark) {
        shipmentOrderService.reject(id, remark);
        return R.ok();
    }

    /**
     * 作废（草稿/已驳回 → 作废）
     */
    @SaCheckPermission("wms:shipment:all")
    @Log(title = "出库单", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping("/void/{id}")
    public R<Void> voidOrder(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        shipmentOrderService.voidOrder(id);
        return R.ok();
    }

    /**
     * 删除出库单
     *
     * @param id 主键
     */
    @SaCheckPermission("wms:shipment:all")
    @Log(title = "出库单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public R<Void> remove(@NotNull(message = "主键不能为空")
                          @PathVariable Long id) {
        shipmentOrderService.deleteById(id);
        return R.ok();
    }

    /**
     * 获取用户下拉列表（轻量级，仅需登录，无需系统管理权限）
     * 用于审批人选择等场景
     */
    @GetMapping("/userSelectList")
    public R<List<SysUserVo>> userSelectList() {
        SysUserBo bo = new SysUserBo();
        bo.setStatus("1");
        return R.ok(sysUserService.selectUserList(bo));
    }
}
