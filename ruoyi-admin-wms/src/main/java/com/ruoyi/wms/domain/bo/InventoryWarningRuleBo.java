package com.ruoyi.wms.domain.bo;

import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import com.ruoyi.common.mybatis.core.domain.BaseEntity;
import com.ruoyi.wms.domain.entity.InventoryWarningRule;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 库存预警规则业务对象 wms_inventory_warning_rule
 *
 * @author ping
 * @date 2025-06-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = InventoryWarningRule.class, reverseConvertGenerate = false)
public class InventoryWarningRuleBo extends BaseEntity {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 规则名称
     */
    @NotBlank(message = "规则名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String ruleName;

    /**
     * 规则类型：item=器材维度, category=分类维度
     */
    @NotBlank(message = "规则类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String ruleType;

    /**
     * 器材ID（器材维度时必填）
     */
    private Long itemId;

    /**
     * 器材分类ID（分类维度时必填）
     */
    private Long itemCategoryId;

    /**
     * 严重不足阈值
     */
    private BigDecimal criticalStock;

    /**
     * 安全库存数量（偏低阈值）
     */
    @NotNull(message = "安全库存不能为空", groups = { AddGroup.class, EditGroup.class })
    private BigDecimal safetyStock;

    /**
     * 关注阈值
     */
    private BigDecimal noticeStock;

    /**
     * 是否启用（1启用/0停用）
     */
    private String enabled;

    /**
     * 备注
     */
    private String remark;

    /**
     * 器材名称（查询条件用）
     */
    private String itemName;

    /**
     * 器材分类名称（查询条件用）
     */
    private String itemCategoryName;
}
