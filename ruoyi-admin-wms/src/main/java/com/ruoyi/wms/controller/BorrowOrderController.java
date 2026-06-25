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
import com.ruoyi.wms.domain.bo.BorrowOrderBo;
import com.ruoyi.wms.domain.vo.BorrowOrderVo;
import com.ruoyi.wms.service.BorrowOrderService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 器材借用单
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/wms/borrowOrder")
public class BorrowOrderController extends BaseController {

    private final BorrowOrderService borrowOrderService;

    /**
     * 查询借用单列表
     */
    @SaCheckPermission("wms:borrowOrder:list")
    @GetMapping("/list")
    public TableDataInfo<BorrowOrderVo> list(BorrowOrderBo bo, PageQuery pageQuery) {
        return borrowOrderService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出借用单列表
     */
    @SaCheckPermission("wms:borrowOrder:list")
    @Log(title = "借用单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(BorrowOrderBo bo, HttpServletResponse response) {
        List<BorrowOrderVo> list = borrowOrderService.queryList(bo);
        ExcelUtil.exportExcel(list, "器材借用单", BorrowOrderVo.class, response);
    }

    /**
     * 获取借用单详情
     */
    @SaCheckPermission("wms:borrowOrder:list")
    @GetMapping("/{id}")
    public R<BorrowOrderVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(borrowOrderService.queryById(id));
    }

    /**
     * 新增借用单
     */
    @SaCheckPermission("wms:borrowOrder:list")
    @Log(title = "借用单", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Long> add(@Validated(AddGroup.class) @RequestBody BorrowOrderBo bo) {
        borrowOrderService.insertByBo(bo);
        return R.ok(bo.getId());
    }

    /**
     * 修改借用单
     */
    @SaCheckPermission("wms:borrowOrder:list")
    @Log(title = "借用单", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody BorrowOrderBo bo) {
        borrowOrderService.updateByBo(bo);
        return R.ok();
    }

    /**
     * 确认借出
     */
    @SaCheckPermission("wms:borrowOrder:list")
    @Log(title = "借用单", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping("/confirm/{id}")
    public R<Void> confirmBorrow(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        BorrowOrderBo bo = new BorrowOrderBo();
        bo.setId(id);
        borrowOrderService.confirmBorrow(bo);
        return R.ok();
    }

    /**
     * 全部归还
     */
    @SaCheckPermission("wms:borrowOrder:list")
    @Log(title = "借用单", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping("/return/{id}")
    public R<Void> returnAll(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        borrowOrderService.returnAll(id);
        return R.ok();
    }

    /**
     * 作废借用单
     */
    @SaCheckPermission("wms:borrowOrder:list")
    @Log(title = "借用单", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping("/void/{id}")
    public R<Void> voidOrder(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        borrowOrderService.voidOrder(id);
        return R.ok();
    }

    /**
     * 删除借用单
     */
    @SaCheckPermission("wms:borrowOrder:list")
    @Log(title = "借用单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public R<Void> remove(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        borrowOrderService.deleteById(id);
        return R.ok();
    }
}
