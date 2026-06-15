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
import com.ruoyi.wms.domain.bo.MovementOrderDetailBo;
import com.ruoyi.wms.domain.vo.MovementOrderDetailVo;
import com.ruoyi.wms.service.MovementOrderDetailService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 调拨单明细
 *
 * @author ping
 * @date 2024-08-09
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/wms/movementOrderDetail")
public class MovementOrderDetailController extends BaseController {

    private final MovementOrderDetailService movementOrderDetailService;

    /**
     * 查询调拨单明细列表
     */
    @SaCheckPermission("wms:movement:all")
    @GetMapping("/list")
    public TableDataInfo<MovementOrderDetailVo> list(MovementOrderDetailBo bo, PageQuery pageQuery) {
        return movementOrderDetailService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出调拨单明细列表
     */
    @SaCheckPermission("wms:movement:all")
    @Log(title = "调拨单明细", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MovementOrderDetailBo bo, HttpServletResponse response) {
        List<MovementOrderDetailVo> list = movementOrderDetailService.queryList(bo);
        ExcelUtil.exportExcel(list, "调拨单明细", MovementOrderDetailVo.class, response);
    }

    /**
     * 获取调拨单明细详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("wms:movement:all")
    @GetMapping("/{id}")
    public R<MovementOrderDetailVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(movementOrderDetailService.queryById(id));
    }

    /**
     * 新增调拨单明细
     */
    @SaCheckPermission("wms:movement:all")
    @Log(title = "调拨单明细", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody MovementOrderDetailBo bo) {
        movementOrderDetailService.insertByBo(bo);
        return R.ok();
    }

    /**
     * 修改调拨单明细
     */
    @SaCheckPermission("wms:movement:all")
    @Log(title = "调拨单明细", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody MovementOrderDetailBo bo) {
        movementOrderDetailService.updateByBo(bo);
        return R.ok();
    }

    /**
     * 删除调拨单明细
     *
     * @param ids 主键串
     */
    @SaCheckPermission("wms:movement:all")
    @Log(title = "调拨单明细", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        movementOrderDetailService.deleteByIds(List.of(ids));
        return R.ok();
    }

    /**
     * 根据调拨单 id 查询调拨单明细列表
     */
    @SaCheckPermission("wms:movement:all")
    @GetMapping("/list/{movementOrderId}")
    public R<List<MovementOrderDetailVo>> listByMovementOrderId(@NotNull @PathVariable Long movementOrderId) {
        return R.ok(movementOrderDetailService.queryByMovementOrderId(movementOrderId));
    }
}
