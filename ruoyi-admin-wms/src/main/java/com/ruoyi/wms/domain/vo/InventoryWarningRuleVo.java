package com.ruoyi.wms.domain.vo;

import com.ruoyi.wms.domain.entity.InventoryWarningRule;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 库存预警规则视图对象 wms_inventory_warning_rule
 *
 * @author ping
 * @date 2025-06-23
 */
@Data
@AutoMapper(target = InventoryWarningRule.class)
public class InventoryWarningRuleVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 规则名称
     */
    private String ruleName;

    /**
     * 规则类型：item/category
     */
    private String ruleType;

    /**
     * 器材ID
     */
    private Long itemId;

    /**
     * 器材分类ID
     */
    private Long itemCategoryId;

    /**
     * 器材分类名称（关联查询）
     */
    private String itemCategoryName;

    /**
     * 器材名称（关联查询）
     */
    private String itemName;

    /**
     * 器材编码（关联查询）
     */
    private String itemCode;

    /**
     * 严重不足阈值
     */
    private BigDecimal criticalStock;

    /**
     * 安全库存数量（偏低阈值）
     */
    private BigDecimal safetyStock;

    /**
     * 关注阈值
     */
    private BigDecimal noticeStock;

    /**
     * 当前实际库存（计算字段，非持久化）
     */
    private BigDecimal currentStock;

    /**
     * 预警等级：critical/warning/notice/normal
     */
    private String warningLevel;

    /**
     * 是否启用（1启用/0停用）
     */
    private String enabled;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private java.util.Date createTime;

    /**
     * 更新时间
     */
    private java.util.Date updateTime;
}
