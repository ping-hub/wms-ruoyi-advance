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
import com.ruoyi.wms.domain.bo.BoxBo;
import com.ruoyi.wms.domain.bo.BoxOperationBo;
import com.ruoyi.wms.domain.vo.BoxVo;
import com.ruoyi.wms.service.BoxService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/wms/box")
public class BoxController extends BaseController {

    private final BoxService boxService;

    @SaCheckPermission("wms:box:list")
    @GetMapping("/list")
    public TableDataInfo<BoxVo> list(BoxBo bo, PageQuery pageQuery) {
        return boxService.queryPageList(bo, pageQuery);
    }

    @SaCheckPermission("wms:box:list")
    @GetMapping("/listNoPage")
    public R<List<BoxVo>> listNoPage(BoxBo bo) {
        return R.ok(boxService.queryList(bo));
    }

    @SaCheckPermission("wms:box:list")
    @Log(title = "箱体", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(BoxBo bo, HttpServletResponse response) {
        ExcelUtil.exportExcel(boxService.queryList(bo), "箱体", BoxVo.class, response);
    }

    @SaCheckPermission("wms:box:list")
    @GetMapping("/{id}")
    public R<BoxVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(boxService.queryById(id));
    }

    @SaCheckPermission("wms:box:list")
    @GetMapping("/code/{boxCode}")
    public R<BoxVo> getByCode(@NotBlank(message = "箱码不能为空") @PathVariable String boxCode) {
        return R.ok(boxService.queryByCode(boxCode));
    }

    @SaCheckPermission("wms:box:edit")
    @Log(title = "箱体", businessType = BusinessType.INSERT)
    @RepeatSubmit
    @PostMapping
    public R<Void> add(@Validated(AddGroup.class) @RequestBody BoxBo bo) {
        boxService.insertByBo(bo);
        return R.ok();
    }

    @SaCheckPermission("wms:box:edit")
    @Log(title = "箱体", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PutMapping
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody BoxBo bo) {
        boxService.updateByBo(bo);
        return R.ok();
    }

    @SaCheckPermission("wms:box:edit")
    @Log(title = "装箱", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PostMapping("/pack")
    public R<Void> pack(@Valid @RequestBody BoxOperationBo bo) {
        boxService.pack(bo);
        return R.ok();
    }

    @SaCheckPermission("wms:box:edit")
    @Log(title = "拆箱", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PostMapping("/unpack")
    public R<Void> unpack(@Valid @RequestBody BoxOperationBo bo) {
        boxService.unpack(bo);
        return R.ok();
    }

    @SaCheckPermission("wms:box:edit")
    @Log(title = "箱体", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public R<Void> remove(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        boxService.deleteById(id);
        return R.ok();
    }
}
