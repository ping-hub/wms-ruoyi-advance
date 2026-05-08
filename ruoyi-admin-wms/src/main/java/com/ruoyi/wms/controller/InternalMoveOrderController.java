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
import com.ruoyi.wms.domain.bo.InternalMoveOrderBo;
import com.ruoyi.wms.domain.vo.InternalMoveOrderVo;
import com.ruoyi.wms.service.InternalMoveOrderService;
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
 * 库内移库单
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/wms/internalMoveOrder")
public class InternalMoveOrderController extends BaseController {

    private final InternalMoveOrderService internalMoveOrderService;
    private final InventoryDetailService inventoryDetailService;

    @SaCheckPermission("wms:internalMove:list")
    @GetMapping("/list")
    public TableDataInfo<InternalMoveOrderVo> list(InternalMoveOrderBo bo, PageQuery pageQuery) {
        return internalMoveOrderService.queryPageList(bo, pageQuery);
    }

    @SaCheckPermission("wms:internalMove:list")
    @Log(title = "库内移库单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(InternalMoveOrderBo bo, HttpServletResponse response) {
        List<InternalMoveOrderVo> list = internalMoveOrderService.queryList(bo);
        ExcelUtil.exportExcel(list, "库内移库单", InternalMoveOrderVo.class, response);
    }

    @SaCheckPermission("wms:internalMove:list")
    @GetMapping("/{id}")
    public R<InternalMoveOrderVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(internalMoveOrderService.queryById(id));
    }

    @SaCheckPermission("wms:internalMove:edit")
    @Log(title = "库内移库单", businessType = BusinessType.INSERT)
    @RepeatSubmit
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody InternalMoveOrderBo bo) {
        bo.setInternalMoveStatus(ServiceConstants.InternalMoveOrderStatus.PENDING);
        internalMoveOrderService.insertByBo(bo);
        return R.ok();
    }

    @SaCheckPermission("wms:internalMove:edit")
    @Log(title = "库内移库单", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody InternalMoveOrderBo bo) {
        internalMoveOrderService.updateByBo(bo);
        return R.ok();
    }

    @SaCheckPermission("wms:internalMove:execute")
    @Log(title = "库内移库单", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PostMapping("/move")
    public R<Void> move(@Validated(AddGroup.class) @RequestBody InternalMoveOrderBo bo) {
        bo.setInternalMoveStatus(ServiceConstants.InternalMoveOrderStatus.FINISH);
        internalMoveOrderService.move(bo);
        inventoryDetailService.clearDataWithZeroRemainQuantity();
        return R.ok();
    }

    @SaCheckPermission("wms:internalMove:remove")
    @Log(title = "库内移库单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public R<Void> remove(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        internalMoveOrderService.deleteById(id);
        return R.ok();
    }
}
