package com.ruoyi.wms.service;

import com.ruoyi.system.service.SysConfigService;
import com.ruoyi.wms.mapper.DashboardMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 仓储看板统计 Service
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class DashboardService {

    private final DashboardMapper dashboardMapper;
    private final SysConfigService sysConfigService;

    /**
     * 安全运行天数
     * 优先读取系统参数 wms.dashboard.startDate（格式 yyyy-MM-dd），缺省 2025-01-01
     */
    public Map<String, Object> getSafeDays() {
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        try {
            String configValue = sysConfigService.selectConfigByKey("wms.dashboard.startDate");
            if (configValue != null && !configValue.isBlank()) {
                startDate = LocalDate.parse(configValue.trim());
            }
        } catch (Exception e) {
            log.warn("读取 wms.dashboard.startDate 配置失败，使用默认值 2025-01-01: {}", e.getMessage());
        }
        long days = ChronoUnit.DAYS.between(startDate, LocalDate.now());
        Map<String, Object> result = new HashMap<>();
        result.put("days", days);
        result.put("startDate", startDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
        return result;
    }

    /**
     * 物资来源统计（按入库类型分组）
     * 返回：[{name: "采购入库", value: 123}, ...]
     */
    public List<Map<String, Object>> getSourceStats() {
        List<Map<String, Object>> rows = dashboardMapper.countReceiptByType();
        if (rows == null || rows.isEmpty()) {
            return Collections.emptyList();
        }
        return rows.stream().map(row -> {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("name", String.valueOf(row.get("name")));
            item.put("value", toLong(row.get("value")));
            return item;
        }).collect(Collectors.toList());
    }

    /**
     * 物资种类统计（含分类明细 + 核心指标）
     * 返回：{categories: [...], totalCategories: N, totalQuantity: N, totalValue: N}
     */
    public Map<String, Object> getCategoryStats() {
        List<Map<String, Object>> rows = dashboardMapper.countInventoryByCategory();
        List<Map<String, Object>> categories = new ArrayList<>();

        BigDecimal totalQuantity = BigDecimal.ZERO;
        BigDecimal totalValue = BigDecimal.ZERO;

        if (rows != null) {
            for (Map<String, Object> row : rows) {
                Map<String, Object> item = new LinkedHashMap<>();
                item.put("name", String.valueOf(row.get("name")));
                BigDecimal qty = toBigDecimal(row.get("quantity"));
                BigDecimal val = toBigDecimal(row.get("value"));
                item.put("quantity", qty);
                item.put("value", val);
                categories.add(item);
                totalQuantity = totalQuantity.add(qty);
                totalValue = totalValue.add(val);
            }
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("categories", categories);
        result.put("totalCategories", categories.size());
        result.put("totalQuantity", totalQuantity);
        result.put("totalValue", totalValue);
        return result;
    }

    /**
     * 出入库趋势（近 N 天）
     * 返回：{dates: [...], inQty: [...], outQty: [...]}
     */
    public Map<String, Object> getInoutTrend(int days) {
        LocalDate startDate = LocalDate.now().minusDays(days);
        List<Map<String, Object>> rows = dashboardMapper.countInoutByDate(startDate);

        // 构建完整日期序列（补齐无数据的天）
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MM-dd");
        Map<String, long[]> dateMap = new LinkedHashMap<>();
        for (int i = days - 1; i >= 0; i--) {
            String d = LocalDate.now().minusDays(i).format(fmt);
            dateMap.put(d, new long[]{0L, 0L}); // [入库, 出库]
        }

        if (rows != null) {
            for (Map<String, Object> row : rows) {
                String date = String.valueOf(row.get("date"));
                int orderType = toInt(row.get("orderType"));
                long qty = toLong(row.get("quantity"));
                long[] arr = dateMap.get(date);
                if (arr != null) {
                    if (orderType == 1) arr[0] = qty; // 入库
                    else if (orderType == 2) arr[1] = qty; // 出库
                }
            }
        }

        List<String> dates = new ArrayList<>(dateMap.keySet());
        List<Long> inQty = dates.stream().map(d -> dateMap.get(d)[0]).collect(Collectors.toList());
        List<Long> outQty = dates.stream().map(d -> dateMap.get(d)[1]).collect(Collectors.toList());

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("dates", dates);
        result.put("inQty", inQty);
        result.put("outQty", outQty);
        return result;
    }

    /**
     * 质量等级统计
     * 返回：[{name: "优等品", value: 123}, ...]
     */
    public List<Map<String, Object>> getQualityStats() {
        List<Map<String, Object>> rows = dashboardMapper.countByQualityGrade();
        if (rows == null || rows.isEmpty()) {
            return Collections.emptyList();
        }
        return rows.stream().map(row -> {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("name", String.valueOf(row.get("name")));
            item.put("value", toLong(row.get("value")));
            return item;
        }).collect(Collectors.toList());
    }

    // ========== 工具方法 ==========

    private long toLong(Object val) {
        if (val == null) return 0L;
        if (val instanceof Number) return ((Number) val).longValue();
        try { return Long.parseLong(val.toString()); } catch (Exception e) { return 0L; }
    }

    private int toInt(Object val) {
        if (val == null) return 0;
        if (val instanceof Number) return ((Number) val).intValue();
        try { return Integer.parseInt(val.toString()); } catch (Exception e) { return 0; }
    }

    private BigDecimal toBigDecimal(Object val) {
        if (val == null) return BigDecimal.ZERO;
        if (val instanceof BigDecimal) return (BigDecimal) val;
        if (val instanceof Number) return BigDecimal.valueOf(((Number) val).doubleValue());
        try { return new BigDecimal(val.toString()); } catch (Exception e) { return BigDecimal.ZERO; }
    }
}
