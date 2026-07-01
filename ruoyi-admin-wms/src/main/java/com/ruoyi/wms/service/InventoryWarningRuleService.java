package com.ruoyi.wms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.utils.MapstructUtils;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.ruoyi.wms.domain.bo.InventoryWarningRuleBo;
import com.ruoyi.wms.domain.entity.InventoryWarningRule;
import com.ruoyi.wms.domain.vo.InventoryWarningRuleVo;
import com.ruoyi.wms.mapper.InventoryWarningRuleMapper;
import com.ruoyi.common.core.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * 库存预警规则 Service
 *
 * @author ping
 * @date 2025-06-23
 */
@RequiredArgsConstructor
@Service
public class InventoryWarningRuleService extends ServiceImpl<InventoryWarningRuleMapper, InventoryWarningRule> {

    private final InventoryWarningRuleMapper inventoryWarningRuleMapper;

    /**
     * 查询单条规则
     */
    public InventoryWarningRuleVo queryById(Long id) {
        return inventoryWarningRuleMapper.selectVoById(id);
    }

    /**
     * 分页查询规则列表
     */
    public TableDataInfo<InventoryWarningRuleVo> queryPageList(InventoryWarningRuleBo bo, PageQuery pageQuery) {
        int pageNum = pageQuery.getPageNum() != null ? pageQuery.getPageNum() : 1;
        int pageSize = pageQuery.getPageSize() != null ? pageQuery.getPageSize() : 10;
        int offset = (pageNum - 1) * pageSize;

        List<InventoryWarningRuleVo> list = inventoryWarningRuleMapper.selectRuleListPage(
            bo.getRuleName(), bo.getItemId(), bo.getEnabled(), bo.getItemName(),
            bo.getRuleType(), bo.getItemCategoryName(), offset, pageSize);
        for (InventoryWarningRuleVo vo : list) {
            vo.setWarningLevel(calcWarningLevel(vo.getCurrentStock(), vo.getCriticalStock(), vo.getSafetyStock(), vo.getNoticeStock()));
        }
        long total = inventoryWarningRuleMapper.countRuleList(
            bo.getRuleName(), bo.getItemId(), bo.getEnabled(), bo.getItemName(),
            bo.getRuleType(), bo.getItemCategoryName());
        Page<InventoryWarningRuleVo> page = new Page<>(pageNum, pageSize);
        page.setRecords(list);
        page.setTotal(total);
        return TableDataInfo.build(page);
    }

    /**
     * 查询规则列表（不分页，供导出等使用）
     */
    public List<InventoryWarningRuleVo> queryList(InventoryWarningRuleBo bo) {
        List<InventoryWarningRuleVo> list = inventoryWarningRuleMapper.selectRuleList(
            bo.getRuleName(), bo.getItemId(), bo.getEnabled(), bo.getItemName(),
            bo.getRuleType(), bo.getItemCategoryName());
        for (InventoryWarningRuleVo vo : list) {
            vo.setWarningLevel(calcWarningLevel(vo.getCurrentStock(), vo.getCriticalStock(), vo.getSafetyStock(), vo.getNoticeStock()));
        }
        return list;
    }

    /**
     * 新增规则
     */
    public void insertByBo(InventoryWarningRuleBo bo) {
        validateRule(bo, null);
        InventoryWarningRule entity = MapstructUtils.convert(bo, InventoryWarningRule.class);
        if (entity.getEnabled() == null) {
            entity.setEnabled("1");
        }
        fillDefaultThresholds(entity);
        inventoryWarningRuleMapper.insert(entity);
    }

    /**
     * 修改规则
     */
    public void updateByBo(InventoryWarningRuleBo bo) {
        validateRule(bo, bo.getId());
        InventoryWarningRule entity = MapstructUtils.convert(bo, InventoryWarningRule.class);
        fillDefaultThresholds(entity);
        inventoryWarningRuleMapper.updateById(entity);
    }

    /**
     * 校验规则：根据 ruleType 校验必填字段 + 唯一性
     */
    private void validateRule(InventoryWarningRuleBo bo, Long excludeId) {
        String ruleType = bo.getRuleType();
        if ("item".equals(ruleType)) {
            if (bo.getItemId() == null) {
                throw new ServiceException("器材维度规则必须选择器材");
            }
            Long count = inventoryWarningRuleMapper.countByItemId(bo.getItemId(), excludeId);
            if (count != null && count > 0) {
                throw new ServiceException("该器材已存在预警规则，不允许重复创建");
            }
        } else if ("category".equals(ruleType)) {
            if (bo.getItemCategoryId() == null) {
                throw new ServiceException("分类维度规则必须选择器材分类");
            }
            Long count = inventoryWarningRuleMapper.countByItemCategoryId(bo.getItemCategoryId(), excludeId);
            if (count != null && count > 0) {
                throw new ServiceException("该器材分类已存在预警规则，不允许重复创建");
            }
        }
    }

    /**
     * 修改启用/停用状态
     */
    public void changeStatus(Long id, String enabled) {
        InventoryWarningRule entity = new InventoryWarningRule();
        entity.setId(id);
        entity.setEnabled(enabled);
        inventoryWarningRuleMapper.updateById(entity);
    }

    /**
     * 批量删除规则
     */
    public void deleteByIds(Collection<Long> ids) {
        inventoryWarningRuleMapper.deleteBatchIds(ids);
    }

    /**
     * 获取预警汇总数据（供仪表盘使用）
     */
    public Map<String, Object> getWarningSummary() {
        List<Map<String, Object>> rows = inventoryWarningRuleMapper.selectWarningSummary();

        int criticalCount = 0;
        int warningCount = 0;
        int noticeCount = 0;
        List<Map<String, Object>> items = new ArrayList<>();

        for (Map<String, Object> row : rows) {
            BigDecimal safetyStock = toBigDecimal(row.get("safetyStock"));
            BigDecimal criticalStock = toBigDecimal(row.get("criticalStock"));
            BigDecimal noticeStock = toBigDecimal(row.get("noticeStock"));
            BigDecimal currentStock = toBigDecimal(row.get("currentStock"));
            String level = calcWarningLevel(currentStock, criticalStock, safetyStock, noticeStock);

            if ("critical".equals(level)) criticalCount++;
            else if ("warning".equals(level)) warningCount++;
            else if ("notice".equals(level)) noticeCount++;
            else continue;

            Map<String, Object> item = new LinkedHashMap<>();
            item.put("ruleId", row.get("ruleId"));
            item.put("ruleName", row.get("ruleName"));
            item.put("ruleType", row.get("ruleType"));
            item.put("itemId", row.get("itemId"));
            item.put("itemCategoryId", row.get("itemCategoryId"));
            item.put("itemName", row.get("itemName"));
            item.put("itemCode", row.get("itemCode"));
            item.put("itemCategoryName", row.get("itemCategoryName"));
            item.put("criticalStock", criticalStock);
            item.put("safetyStock", safetyStock);
            item.put("noticeStock", noticeStock);
            item.put("currentStock", currentStock);
            item.put("warningLevel", level);
            items.add(item);
        }

        int total = criticalCount + warningCount + noticeCount;

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("total", total);
        result.put("critical", criticalCount);
        result.put("warning", warningCount);
        result.put("notice", noticeCount);
        result.put("items", items);
        return result;
    }

    /**
     * 根据用户配置的三个具体数量阈值计算预警等级
     */
    private String calcWarningLevel(BigDecimal currentStock, BigDecimal criticalStock,
                                     BigDecimal safetyStock, BigDecimal noticeStock) {
        if (currentStock == null) currentStock = BigDecimal.ZERO;

        if (criticalStock != null && currentStock.compareTo(criticalStock) <= 0) {
            return "critical";
        } else if (safetyStock != null && currentStock.compareTo(safetyStock) <= 0) {
            return "warning";
        } else if (noticeStock != null && currentStock.compareTo(noticeStock) <= 0) {
            return "notice";
        } else {
            return "normal";
        }
    }

    /**
     * 新增/修改时，若 criticalStock 或 noticeStock 未填，自动根据 safetyStock 填充默认值
     */
    private void fillDefaultThresholds(InventoryWarningRule entity) {
        if (entity.getSafetyStock() == null) return;
        if (entity.getCriticalStock() == null) {
            entity.setCriticalStock(entity.getSafetyStock().multiply(new BigDecimal("0.5")).setScale(0, java.math.RoundingMode.HALF_UP));
        }
        if (entity.getNoticeStock() == null) {
            entity.setNoticeStock(entity.getSafetyStock().multiply(new BigDecimal("1.5")).setScale(0, java.math.RoundingMode.HALF_UP));
        }
    }

    private BigDecimal toBigDecimal(Object val) {
        if (val == null) return null;
        if (val instanceof BigDecimal) return (BigDecimal) val;
        if (val instanceof Number) return BigDecimal.valueOf(((Number) val).doubleValue());
        try { return new BigDecimal(val.toString()); } catch (Exception e) { return null; }
    }
}
