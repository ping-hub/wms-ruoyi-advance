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
import com.ruoyi.wms.domain.bo.ItemInstanceBo;
import com.ruoyi.wms.domain.vo.ItemInstanceVo;
import com.ruoyi.wms.service.ItemInstanceService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/wms/itemInstance")
public class ItemInstanceController extends BaseController {

    private final ItemInstanceService itemInstanceService;

    @SaCheckPermission("wms:itemInstance:list")
    @GetMapping("/list")
    public TableDataInfo<ItemInstanceVo> list(ItemInstanceBo bo, PageQuery pageQuery) {
        return itemInstanceService.queryPageList(bo, pageQuery);
    }

    @SaCheckPermission("wms:itemInstance:list")
    @GetMapping("/listNoPage")
    public R<List<ItemInstanceVo>> listNoPage(ItemInstanceBo bo) {
        return R.ok(itemInstanceService.queryList(bo));
    }

    @SaCheckPermission("wms:itemInstance:list")
    @Log(title = "单品实例", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(ItemInstanceBo bo, HttpServletResponse response) {
        ExcelUtil.exportExcel(itemInstanceService.queryList(bo), "单品实例", ItemInstanceVo.class, response);
    }

    @SaCheckPermission("wms:itemInstance:list")
    @GetMapping("/{id}")
    public R<ItemInstanceVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id, ItemInstanceBo bo) {
        ItemInstanceVo vo = itemInstanceService.queryById(id);
        itemInstanceService.validateSelectRules(vo, bo);
        return R.ok(vo);
    }

    @SaCheckPermission("wms:itemInstance:list")
    @GetMapping("/code/{instanceCode}")
    public R<ItemInstanceVo> getByCode(@NotBlank(message = "单品码不能为空") @PathVariable String instanceCode, ItemInstanceBo bo) {
        ItemInstanceVo vo = itemInstanceService.queryByCode(instanceCode);
        itemInstanceService.validateSelectRules(vo, bo);
        return R.ok(vo);
    }

    @SaCheckPermission("wms:itemInstance:edit")
    @Log(title = "单品实例", businessType = BusinessType.INSERT)
    @RepeatSubmit
    @PostMapping
    public R<Void> add(@Validated(AddGroup.class) @RequestBody ItemInstanceBo bo) {
        itemInstanceService.insertByBo(bo);
        return R.ok();
    }

    @SaCheckPermission("wms:itemInstance:edit")
    @Log(title = "单品实例", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PutMapping
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody ItemInstanceBo bo) {
        itemInstanceService.updateByBo(bo);
        return R.ok();
    }

    @SaCheckPermission("wms:itemInstance:edit")
    @Log(title = "单品实例状态", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PutMapping("/status")
    public R<Void> updateStatus(@RequestBody ItemInstanceBo bo) {
        itemInstanceService.updateStatus(bo.getId(), bo.getTargetStatus());
        return R.ok();
    }

    @SaCheckPermission("wms:itemInstance:edit")
    @Log(title = "单品实例位置", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PutMapping("/location")
    public R<Void> updateLocation(@RequestBody ItemInstanceBo bo) {
        itemInstanceService.updateLocation(bo);
        return R.ok();
    }

    @SaCheckPermission("wms:itemInstance:edit")
    @Log(title = "单品实例", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public R<Void> remove(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        itemInstanceService.deleteById(id);
        return R.ok();
    }
}
