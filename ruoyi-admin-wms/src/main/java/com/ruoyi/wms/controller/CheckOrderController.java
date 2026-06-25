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
import com.ruoyi.wms.domain.bo.CheckOrderBo;
import com.ruoyi.wms.domain.vo.CheckOrderVo;
import com.ruoyi.wms.domain.vo.ItemInstanceVo;
import com.ruoyi.wms.service.CheckOrderService;
import com.ruoyi.system.domain.bo.SysUserBo;
import com.ruoyi.system.domain.vo.SysUserVo;
import com.ruoyi.system.service.SysUserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 库存盘点单据
 *
 * @author ping
 * @date 2024-08-13
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/wms/checkOrder")
public class CheckOrderController extends BaseController {

    private final CheckOrderService checkOrderService;
    private final SysUserService sysUserService;

    /**
     * 查询库存盘点单据列表
     */
    @SaCheckPermission("wms:check:all")
    @GetMapping("/list")
    public TableDataInfo<CheckOrderVo> list(CheckOrderBo bo, PageQuery pageQuery) {
        return checkOrderService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出库存盘点单据列表
     */
    @SaCheckPermission("wms:check:all")
    @Log(title = "库存盘点单据", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(CheckOrderBo bo, HttpServletResponse response) {
        List<CheckOrderVo> list = checkOrderService.queryList(bo);
        ExcelUtil.exportExcel(list, "库存盘点单据", CheckOrderVo.class, response);
    }

    /**
     * 获取库存盘点单据详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("wms:check:all")
    @GetMapping("/{id}")
    public R<CheckOrderVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(checkOrderService.queryById(id));
    }

    /**
     * 新增库存盘点单据（草稿状态）
     */
    @SaCheckPermission("wms:check:all")
    @Log(title = "库存盘点单据", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Long> add(@Validated(AddGroup.class) @RequestBody CheckOrderBo bo) {
        checkOrderService.insertByBo(bo);
        return R.ok(bo.getId());
    }

    /**
     * 修改库存盘点单据
     */
    @SaCheckPermission("wms:check:all")
    @Log(title = "库存盘点单据", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody CheckOrderBo bo) {
        checkOrderService.updateByBo(bo);
        return R.ok();
    }

    /**
     * 开始盘点（生成SKU级明细，返回轻量统计信息）
     */
    @SaCheckPermission("wms:check:all")
    @Log(title = "库存盘点单据", businessType = BusinessType.UPDATE)
    @PostMapping("/startCheck/{id}")
    public R<Map<String, Object>> startCheck(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(checkOrderService.startCheck(id));
    }

    /**
     * 懒加载指定SKU的在库实例列表
     */
    @SaCheckPermission("wms:check:all")
    @GetMapping("/instances/{checkOrderId}")
    public R<List<ItemInstanceVo>> getInstances(
            @PathVariable Long checkOrderId,
            @RequestParam Long skuId) {
        return R.ok(checkOrderService.getInstancesBySku(checkOrderId, skuId));
    }

    /**
     * 验码：校验扫码结果是否属于盘点范围，实时返回盘盈信息
     */
    @SaCheckPermission("wms:check:all")
    @PostMapping("/verify/{checkOrderId}")
    public R<Map<String, Object>> verify(
            @PathVariable Long checkOrderId,
            @RequestBody List<String> instanceCodes) {
        return R.ok(checkOrderService.verify(checkOrderId, instanceCodes));
    }

    /**
     * 完成盘点（保存差异+实例明细，不调整库存）
     * App端仍可直接调用此接口一步完成盘点
     */
    @SaCheckPermission("wms:check:all")
    @Log(title = "库存盘点单据", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PostMapping("/check")
    public R<Void> check(@RequestBody CheckOrderBo bo) {
        checkOrderService.check(bo);
        return R.ok();
    }

    /**
     * 离线盘点快照：一次性返回全量盘点数据（供App端离线使用）
     */
    @SaCheckPermission("wms:check:all")
    @GetMapping("/offline/snapshot/{id}")
    public R<Map<String, Object>> getOfflineSnapshot(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(checkOrderService.getOfflineSnapshot(id));
    }

    /**
     * 删除库存盘点单据
     *
     * @param id 主键
     */
    @SaCheckPermission("wms:check:all")
    @Log(title = "库存盘点单据", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public R<Void> remove(@NotNull(message = "主键不能为空")
                          @PathVariable Long id) {
        checkOrderService.deleteById(id);
        return R.ok();
    }

    // ==================== 流程端点 ====================

    /**
     * 提交盘点（草稿/已驳回 → 待盘点）
     */
    @SaCheckPermission("wms:check:submit")
    @Log(title = "盘点单", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping("/submit/{id}")
    public R<Void> submit(@NotNull(message = "主键不能为空") @PathVariable Long id,
                          @RequestParam(required = false) Long executorId,
                          @RequestParam(required = false) String executorName) {
        checkOrderService.submitForApproval(id, executorId, executorName);
        return R.ok();
    }

    /**
     * 完成盘点并提交复核（待盘点 → 待复核）
     */
    @SaCheckPermission("wms:check:execute")
    @Log(title = "盘点单", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PostMapping("/complete")
    public R<Void> complete(@RequestBody CheckOrderBo bo,
                            @RequestParam(required = false) Long reviewerId,
                            @RequestParam(required = false) String reviewerName) {
        checkOrderService.completeCheck(bo, reviewerId, reviewerName);
        return R.ok();
    }

    /**
     * 复核通过（待复核 → 已完成）
     */
    @SaCheckPermission("wms:check:approve")
    @Log(title = "盘点单", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping("/approve/{id}")
    public R<Void> approve(@NotNull(message = "主键不能为空") @PathVariable Long id,
                           @RequestParam(required = false) String remark) {
        checkOrderService.approve(id, remark);
        return R.ok();
    }

    /**
     * 驳回（待盘点/待复核 → 已驳回）
     */
    @SaCheckPermission("wms:check:execute")
    @Log(title = "盘点单", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping("/reject/{id}")
    public R<Void> reject(@NotNull(message = "主键不能为空") @PathVariable Long id,
                          @RequestParam(required = false) String remark) {
        checkOrderService.reject(id, remark);
        return R.ok();
    }

    /**
     * 作废（草稿/已驳回 → 作废）
     */
    @SaCheckPermission("wms:check:all")
    @Log(title = "盘点单", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping("/void/{id}")
    public R<Void> voidOrder(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        checkOrderService.voidOrder(id);
        return R.ok();
    }

    /**
     * 获取用户下拉列表（轻量级，仅需登录，无需系统管理权限）
     * 用于盘点人/复核人选择等场景
     */
    @GetMapping("/userSelectList")
    public R<List<SysUserVo>> userSelectList() {
        SysUserBo bo = new SysUserBo();
        bo.setStatus("1");
        return R.ok(sysUserService.selectUserList(bo));
    }
}
