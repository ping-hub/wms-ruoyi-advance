package com.ruoyi.wms.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.ruoyi.common.core.constant.ServiceConstants;
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
import com.ruoyi.wms.domain.bo.AdjustOrderBo;
import com.ruoyi.wms.domain.vo.AdjustOrderVo;
import com.ruoyi.wms.service.AdjustOrderService;
import com.ruoyi.wms.service.InventoryDetailService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 库存调整
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/wms/adjustOrder")
public class AdjustOrderController extends BaseController {

    private final AdjustOrderService adjustOrderService;
    private final InventoryDetailService inventoryDetailService;

    @SaCheckPermission("wms:adjust:all")
    @GetMapping("/list")
    public TableDataInfo<AdjustOrderVo> list(AdjustOrderBo bo, PageQuery pageQuery) {
        return adjustOrderService.queryPageList(bo, pageQuery);
    }

    @SaCheckPermission("wms:adjust:all")
    @Log(title = "库存调整", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(AdjustOrderBo bo, HttpServletResponse response) {
        List<AdjustOrderVo> list = adjustOrderService.queryList(bo);
        ExcelUtil.exportExcel(list, "库存调整", AdjustOrderVo.class, response);
    }

    @SaCheckPermission("wms:adjust:all")
    @GetMapping("/{id}")
    public R<AdjustOrderVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(adjustOrderService.queryById(id));
    }

    @SaCheckPermission("wms:adjust:all")
    @Log(title = "库存调整", businessType = BusinessType.INSERT)
    @RepeatSubmit
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody AdjustOrderBo bo) {
        bo.setAdjustStatus(ServiceConstants.AdjustOrderStatus.PENDING);
        adjustOrderService.insertByBo(bo);
        return R.ok();
    }

    @SaCheckPermission("wms:adjust:all")
    @Log(title = "库存调整", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PostMapping("/adjust")
    public R<Void> adjust(@Validated(AddGroup.class) @RequestBody AdjustOrderBo bo) {
        bo.setAdjustStatus(ServiceConstants.AdjustOrderStatus.FINISH);
        adjustOrderService.adjust(bo);
        inventoryDetailService.clearDataWithZeroRemainQuantity();
        return R.ok();
    }

    @SaCheckPermission("wms:adjust:all")
    @Log(title = "库存调整", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody AdjustOrderBo bo) {
        adjustOrderService.updateByBo(bo);
        return R.ok();
    }

    @SaCheckPermission("wms:adjust:all")
    @Log(title = "库存调整", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public R<Void> remove(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        adjustOrderService.deleteById(id);
        return R.ok();
    }
}
