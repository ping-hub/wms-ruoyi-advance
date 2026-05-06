package com.ruoyi.wms.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.ruoyi.common.excel.utils.ExcelUtil;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.ruoyi.common.web.core.BaseController;
import com.ruoyi.wms.domain.bo.LedgerBo;
import com.ruoyi.wms.domain.vo.LedgerVo;
import com.ruoyi.wms.service.LedgerService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/wms/ledger")
public class LedgerController extends BaseController {

    private final LedgerService ledgerService;

    @SaCheckPermission("wms:ledger:list")
    @GetMapping("/list")
    public TableDataInfo<LedgerVo> list(LedgerBo bo, PageQuery pageQuery) {
        return ledgerService.queryPageList(bo, pageQuery);
    }

    @SaCheckPermission("wms:ledger:export")
    @Log(title = "器材总账", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(LedgerBo bo, HttpServletResponse response) {
        List<LedgerVo> list = ledgerService.queryList(bo);
        ExcelUtil.exportExcel(list, "器材总账", LedgerVo.class, response);
    }
}
