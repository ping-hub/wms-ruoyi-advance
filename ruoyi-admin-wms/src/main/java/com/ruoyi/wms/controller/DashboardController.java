package com.ruoyi.wms.controller;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.web.core.BaseController;
import com.ruoyi.wms.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 智慧仓储可视化看板统计接口
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/wms/dashboard")
public class DashboardController extends BaseController {

    private final DashboardService dashboardService;

    /**
     * 安全运行天数
     */
    @GetMapping("/safeDays")
    public R<Object> safeDays() {
        return R.ok(dashboardService.getSafeDays());
    }

    /**
     * 物资来源统计（按入库类型分组）
     */
    @GetMapping("/sourceStats")
    public R<Object> sourceStats() {
        return R.ok(dashboardService.getSourceStats());
    }

    /**
     * 物资种类统计（含核心指标：总类/总数量/总价值）
     */
    @GetMapping("/categoryStats")
    public R<Object> categoryStats() {
        return R.ok(dashboardService.getCategoryStats());
    }

    /**
     * 出入库趋势（默认近7天）
     */
    @GetMapping("/inoutTrend")
    public R<Object> inoutTrend(@RequestParam(defaultValue = "7") int days) {
        return R.ok(dashboardService.getInoutTrend(days));
    }

    /**
     * 质量等级统计
     */
    @GetMapping("/qualityStats")
    public R<Object> qualityStats() {
        return R.ok(dashboardService.getQualityStats());
    }

    /**
     * 质保期预警统计（已到期 / 本月到期 / 下月到期）
     */
    @GetMapping("/warrantyStats")
    public R<Object> warrantyStats() {
        return R.ok(dashboardService.getWarrantyStats());
    }

    /**
     * 质保期预警明细列表（已到期+本月到期+下月到期）
     */
    @GetMapping("/warrantyWarningList")
    public R<Object> warrantyWarningList(
            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        return R.ok(dashboardService.getWarrantyWarningList(pageNum, pageSize));
    }

}
