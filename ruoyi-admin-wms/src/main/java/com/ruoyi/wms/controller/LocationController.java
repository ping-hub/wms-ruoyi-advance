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
import com.ruoyi.wms.domain.bo.LocationBo;
import com.ruoyi.wms.domain.vo.LocationStockVo;
import com.ruoyi.wms.domain.vo.LocationVo;
import com.ruoyi.wms.service.LocationService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/wms/location")
public class LocationController extends BaseController {

    private final LocationService locationService;

    @SaCheckPermission("wms:location:list")
    @GetMapping("/list")
    public TableDataInfo<LocationVo> list(LocationBo bo, PageQuery pageQuery) {
        return locationService.queryPageList(bo, pageQuery);
    }

    @SaCheckPermission("wms:location:list")
    @GetMapping("/listNoPage")
    public R<List<LocationVo>> listNoPage(LocationBo bo) {
        return R.ok(locationService.queryList(bo));
    }

    @SaCheckPermission("wms:location:list")
    @Log(title = "货位", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(LocationBo bo, HttpServletResponse response) {
        List<LocationVo> list = locationService.queryList(bo);
        ExcelUtil.exportExcel(list, "货位", LocationVo.class, response);
    }

    @SaCheckPermission("wms:location:list")
    @GetMapping("/{id}")
    public R<LocationVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(locationService.queryById(id));
    }

    @SaCheckPermission("wms:location:list")
    @GetMapping("/stock/{id}")
    public R<LocationStockVo> getStock(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(locationService.queryStockById(id));
    }

    @SaCheckPermission("wms:location:edit")
    @Log(title = "货位", businessType = BusinessType.INSERT)
    @RepeatSubmit
    @PostMapping
    public R<Void> add(@Validated(AddGroup.class) @RequestBody LocationBo bo) {
        locationService.insertByBo(bo);
        return R.ok();
    }

    @SaCheckPermission("wms:location:edit")
    @Log(title = "货位", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PutMapping
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody LocationBo bo) {
        locationService.updateByBo(bo);
        return R.ok();
    }

    @SaCheckPermission("wms:location:edit")
    @Log(title = "货位", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public R<Void> remove(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        locationService.deleteById(id);
        return R.ok();
    }
}
