package com.ruoyi.wms.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.validate.EditGroup;
import com.ruoyi.common.idempotent.annotation.RepeatSubmit;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.web.core.BaseController;
import com.ruoyi.wms.domain.bo.CodeRuleBo;
import com.ruoyi.wms.domain.vo.CodeRuleVo;
import com.ruoyi.wms.service.CodeRuleService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/wms/codeRule")
public class CodeRuleController extends BaseController {

    private final CodeRuleService codeRuleService;

    /** 查询编码规则列表 */
    @SaCheckPermission("wms:codeRule:list")
    @GetMapping("/list")
    public R<List<CodeRuleVo>> list() {
        return R.ok(codeRuleService.queryList());
    }

    /** 查询编码规则详细 */
    @SaCheckPermission("wms:codeRule:list")
    @GetMapping("/{id}")
    public R<CodeRuleVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(codeRuleService.queryById(id));
    }

    /** 根据编码类型查询规则 */
    @SaCheckPermission("wms:codeRule:list")
    @GetMapping("/type/{ruleType}")
    public R<CodeRuleVo> getByType(@PathVariable String ruleType) {
        return R.ok(codeRuleService.queryByType(ruleType));
    }

    /** 预览编码效果 */
    @SaCheckPermission("wms:codeRule:list")
    @GetMapping("/preview/{ruleType}")
    public R<String> preview(@PathVariable String ruleType) {
        String code = codeRuleService.generateCode(ruleType);
        return R.ok(code);
    }

    /** 修改编码规则 */
    @SaCheckPermission("wms:codeRule:edit")
    @Log(title = "编码规则", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PutMapping
    public R<Void> edit(@Valid @RequestBody CodeRuleBo bo) {
        codeRuleService.updateByBo(bo);
        return R.ok();
    }
}
