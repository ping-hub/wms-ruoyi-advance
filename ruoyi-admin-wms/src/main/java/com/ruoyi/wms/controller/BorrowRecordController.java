package com.ruoyi.wms.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.excel.utils.ExcelUtil;
import com.ruoyi.common.idempotent.annotation.RepeatSubmit;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.ruoyi.common.web.core.BaseController;
import com.ruoyi.wms.domain.bo.BorrowRecordBo;
import com.ruoyi.wms.domain.vo.BorrowWarningStatsVo;
import com.ruoyi.wms.domain.vo.BorrowRecordVo;
import com.ruoyi.wms.service.BorrowRecordService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/wms/borrowRecord")
public class BorrowRecordController extends BaseController {

    private final BorrowRecordService borrowRecordService;

    @SaCheckPermission("wms:borrowRecord:list")
    @GetMapping("/list")
    public TableDataInfo<BorrowRecordVo> list(BorrowRecordBo bo, PageQuery pageQuery) {
        return borrowRecordService.queryPageList(bo, pageQuery);
    }

    @SaCheckPermission("wms:borrowRecord:list")
    @GetMapping("/listNoPage")
    public R<List<BorrowRecordVo>> listNoPage(BorrowRecordBo bo) {
        return R.ok(borrowRecordService.queryList(bo));
    }

    @SaCheckPermission("wms:borrowRecord:list")
    @Log(title = "借还记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(BorrowRecordBo bo, HttpServletResponse response) {
        ExcelUtil.exportExcel(borrowRecordService.queryList(bo), "借还记录", BorrowRecordVo.class, response);
    }

    @SaCheckPermission("wms:borrowRecord:list")
    @GetMapping("/{id}")
    public R<BorrowRecordVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(borrowRecordService.queryById(id));
    }

    @SaCheckPermission("wms:borrowRecord:list")
    @GetMapping("/current/{instanceCode}")
    public R<BorrowRecordVo> getCurrent(@NotNull(message = "单品实例不能为空") @PathVariable String instanceCode) {
        return R.ok(borrowRecordService.queryCurrentByInstanceCode(instanceCode));
    }

    @SaCheckPermission("wms:borrowRecord:list")
    @GetMapping("/warning/stats")
    public R<BorrowWarningStatsVo> warningStats() {
        return R.ok(borrowRecordService.queryWarningStats());
    }

    @SaCheckPermission("wms:borrowRecord:edit")
    @Log(title = "借出登记", businessType = BusinessType.INSERT)
    @RepeatSubmit
    @PostMapping("/borrow")
    public R<Void> borrow(@Validated(AddGroup.class) @RequestBody BorrowRecordBo bo) {
        borrowRecordService.borrow(bo);
        return R.ok();
    }

    @SaCheckPermission("wms:borrowRecord:edit")
    @Log(title = "归还登记", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PostMapping("/return")
    public R<Void> returnItem(@RequestBody BorrowRecordBo bo) {
        borrowRecordService.returnItem(bo);
        return R.ok();
    }
}
