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
import com.ruoyi.wms.domain.bo.InventoryWarningRuleBo;
import com.ruoyi.wms.domain.vo.InventoryWarningRuleVo;
import com.ruoyi.wms.service.InventoryWarningRuleService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 库存预警规则
 *
 * @author ping
 * @date 2025-06-23
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/wms/inventoryWarning")
public class InventoryWarningRuleController extends BaseController {

    private final InventoryWarningRuleService inventoryWarningRuleService;

    /**
     * 查询预警规则列表
     */
    @SaCheckPermission("wms:inventoryWarning:list")
    @GetMapping("/rule/list")
    public TableDataInfo<InventoryWarningRuleVo> list(InventoryWarningRuleBo bo, PageQuery pageQuery) {
        return inventoryWarningRuleService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出预警规则列表
     */
    @SaCheckPermission("wms:inventoryWarning:list")
    @Log(title = "库存预警规则", businessType = BusinessType.EXPORT)
    @PostMapping("/rule/export")
    public void export(InventoryWarningRuleBo bo, HttpServletResponse response) {
        List<InventoryWarningRuleVo> list = inventoryWarningRuleService.queryList(bo);
        ExcelUtil.exportExcel(list, "库存预警规则", InventoryWarningRuleVo.class, response);
    }

    /**
     * 获取预警规则详细信息
     */
    @SaCheckPermission("wms:inventoryWarning:list")
    @GetMapping("/rule/{id}")
    public R<InventoryWarningRuleVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(inventoryWarningRuleService.queryById(id));
    }

    /**
     * 新增预警规则
     */
    @SaCheckPermission("wms:inventoryWarning:list")
    @Log(title = "库存预警规则", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping("/rule")
    public R<Void> add(@Validated(AddGroup.class) @RequestBody InventoryWarningRuleBo bo) {
        inventoryWarningRuleService.insertByBo(bo);
        return R.ok();
    }

    /**
     * 修改预警规则
     */
    @SaCheckPermission("wms:inventoryWarning:list")
    @Log(title = "库存预警规则", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping("/rule")
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody InventoryWarningRuleBo bo) {
        inventoryWarningRuleService.updateByBo(bo);
        return R.ok();
    }

    /**
     * 修改预警规则状态
     */
    @SaCheckPermission("wms:inventoryWarning:list")
    @Log(title = "库存预警规则", businessType = BusinessType.UPDATE)
    @PutMapping("/rule/changeStatus")
    public R<Void> changeStatus(@RequestBody InventoryWarningRuleBo bo) {
        inventoryWarningRuleService.changeStatus(bo.getId(), bo.getEnabled());
        return R.ok();
    }

    /**
     * 删除预警规则
     */
    @SaCheckPermission("wms:inventoryWarning:list")
    @Log(title = "库存预警规则", businessType = BusinessType.DELETE)
    @DeleteMapping("/rule/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        inventoryWarningRuleService.deleteByIds(List.of(ids));
        return R.ok();
    }

    /**
     * 预警汇总数据（供仪表盘使用，无需按钮权限）
     */
    @SaCheckPermission("wms:inventoryWarning:list")
    @GetMapping("/summary")
    public R<Object> summary() {
        return R.ok(inventoryWarningRuleService.getWarningSummary());
    }
}
