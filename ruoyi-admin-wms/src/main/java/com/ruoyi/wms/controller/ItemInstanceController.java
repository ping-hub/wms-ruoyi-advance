package com.ruoyi.wms.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import com.ruoyi.common.excel.core.ExcelResult;
import com.ruoyi.common.excel.utils.ExcelUtil;
import com.ruoyi.common.idempotent.annotation.RepeatSubmit;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.ruoyi.common.web.core.BaseController;
import com.ruoyi.wms.domain.bo.ItemInstanceBo;
import com.ruoyi.wms.domain.vo.ItemInstanceImportVo;
import com.ruoyi.wms.domain.vo.ItemInstanceVo;
import com.ruoyi.wms.service.ItemInstanceService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    @Log(title = "器材", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(ItemInstanceBo bo, HttpServletResponse response) {
        ExcelUtil.exportExcel(itemInstanceService.queryList(bo), "器材", ItemInstanceVo.class, response);
    }

    @SaCheckPermission("wms:itemInstance:list")
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil.exportExcel(new ArrayList<>(), "单品台账导入模板", ItemInstanceImportVo.class, response);
    }

    @SaCheckPermission("wms:itemInstance:edit")
    @Log(title = "器材", businessType = BusinessType.IMPORT)
    @PostMapping(value = "/importData", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public R<Void> importData(@RequestPart("file") MultipartFile file) throws Exception {
        ExcelResult<ItemInstanceImportVo> excelResult = ExcelUtil.importExcel(file.getInputStream(), ItemInstanceImportVo.class, true);
        return R.ok(itemInstanceService.importData(excelResult.getList()));
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
    public R<ItemInstanceVo> getByCode(@NotBlank(message = "器材识别码不能为空") @PathVariable String instanceCode, ItemInstanceBo bo) {
        ItemInstanceVo vo = itemInstanceService.queryByCode(instanceCode);
        itemInstanceService.validateSelectRules(vo, bo);
        return R.ok(vo);
    }

    @SaCheckPermission("wms:itemInstance:edit")
    @Log(title = "器材", businessType = BusinessType.INSERT)
    @RepeatSubmit
    @PostMapping
    public R<Void> add(@Validated(AddGroup.class) @RequestBody ItemInstanceBo bo) {
        itemInstanceService.insertByBo(bo);
        return R.ok();
    }

    @SaCheckPermission("wms:itemInstance:edit")
    @Log(title = "器材", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PutMapping
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody ItemInstanceBo bo) {
        itemInstanceService.updateByBo(bo);
        return R.ok();
    }

    @SaCheckPermission("wms:itemInstance:edit")
    @Log(title = "器材状态", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PutMapping("/status")
    public R<Void> updateStatus(@RequestBody ItemInstanceBo bo) {
        itemInstanceService.updateStatus(bo.getId(), bo.getTargetStatus());
        return R.ok();
    }

    @SaCheckPermission("wms:itemInstance:edit")
    @Log(title = "器材位置", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PutMapping("/location")
    public R<Void> updateLocation(@RequestBody ItemInstanceBo bo) {
        itemInstanceService.updateLocation(bo);
        return R.ok();
    }


    @SaCheckPermission("wms:itemInstance:edit")
    @Log(title = "散件移位", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PutMapping("/batchRelocate")
    public R<Void> batchRelocate(@RequestBody Map<String, Object> body) {
        @SuppressWarnings("unchecked")
        List<Number> rawIds = (List<Number>) body.get("ids");
        if (rawIds == null || rawIds.isEmpty()) {
            return R.ok();
        }
        List<Long> ids = rawIds.stream().map(Number::longValue).collect(java.util.stream.Collectors.toList());
        Long warehouseId = body.get("warehouseId") != null ? ((Number) body.get("warehouseId")).longValue() : null;
        Long areaId     = body.get("areaId")     != null ? ((Number) body.get("areaId")).longValue()     : null;
        Long rackId     = body.get("rackId")     != null ? ((Number) body.get("rackId")).longValue()     : null;
        Long locationId = body.get("locationId") != null ? ((Number) body.get("locationId")).longValue() : null;
        itemInstanceService.batchRelocate(ids, warehouseId, areaId, rackId, locationId);
        return R.ok();
    }

    @SaCheckPermission("wms:itemInstance:edit")
    @Log(title = "质保期重置", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PutMapping("/batchResetWarranty")
    public R<Void> batchResetWarranty(@RequestBody List<Long> ids) {
        itemInstanceService.batchResetWarranty(ids);
        return R.ok();
    }

    @SaCheckPermission("wms:itemInstance:edit")
    @Log(title = "器材", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public R<Void> remove(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        itemInstanceService.deleteById(id);
        return R.ok();
    }
}
