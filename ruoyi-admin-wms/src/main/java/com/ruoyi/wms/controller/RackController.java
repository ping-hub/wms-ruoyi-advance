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
import com.ruoyi.wms.domain.bo.RackBo;
import com.ruoyi.wms.domain.vo.RackVo;
import com.ruoyi.wms.service.RackService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/wms/rack")
public class RackController extends BaseController {

    private final RackService rackService;

    @SaCheckPermission("wms:rack:list")
    @GetMapping("/list")
    public TableDataInfo<RackVo> list(RackBo bo, PageQuery pageQuery) {
        return rackService.queryPageList(bo, pageQuery);
    }

    @SaCheckPermission("wms:rack:list")
    @GetMapping("/listNoPage")
    public R<List<RackVo>> listNoPage(RackBo bo) {
        return R.ok(rackService.queryList(bo));
    }

    @SaCheckPermission("wms:rack:list")
    @Log(title = "货架", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(RackBo bo, HttpServletResponse response) {
        List<RackVo> list = rackService.queryList(bo);
        ExcelUtil.exportExcel(list, "货架", RackVo.class, response);
    }

    @SaCheckPermission("wms:rack:list")
    @GetMapping("/{id}")
    public R<RackVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(rackService.queryById(id));
    }

    @SaCheckPermission("wms:rack:edit")
    @Log(title = "货架", businessType = BusinessType.INSERT)
    @RepeatSubmit
    @PostMapping
    public R<Void> add(@Validated(AddGroup.class) @RequestBody RackBo bo) {
        rackService.insertByBo(bo);
        return R.ok();
    }

    @SaCheckPermission("wms:rack:edit")
    @Log(title = "货架", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PutMapping
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody RackBo bo) {
        rackService.updateByBo(bo);
        return R.ok();
    }

    @SaCheckPermission("wms:rack:edit")
    @Log(title = "货架", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public R<Void> remove(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        rackService.deleteById(id);
        return R.ok();
    }
}
