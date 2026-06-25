package com.ruoyi.wms.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.mybatis.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;

/**
 * 库存预警规则对象 wms_inventory_warning_rule
 *
 * @author ping
 * @date 2025-06-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wms_inventory_warning_rule")
public class InventoryWarningRule extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 规则名称
     */
    private String ruleName;

    /**
     * 器材ID（关联 wms_item.id）
     */
    private Long itemId;

    /**
     * 严重不足阈值（当前库存<=此值判定为严重不足）
     */
    private BigDecimal criticalStock;

    /**
     * 安全库存数量（当前库存<=此值判定为库存偏低）
     */
    private BigDecimal safetyStock;

    /**
     * 关注阈值（当前库存<=此值判定为关注）
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
}
