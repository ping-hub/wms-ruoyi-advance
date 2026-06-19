package com.ruoyi.wms.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.ruoyi.common.mybatis.core.domain.BaseEntity;

import java.math.BigDecimal;
import java.io.Serial;
import java.time.LocalDate;

/**
 * 调拨单对象 wms_movement_order
 *
 * @author ping
 * @date 2024-08-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wms_movement_order")
public class MovementOrder extends BaseEntity {

    @Serial
    private static final long serialVersionUID=1L;

    /**
     *
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 编号
     */
    private String movementOrderNo;

    private String movementType;

    /**
     * 调拨范围（库内调拨/库外调拨）
     */
    private String transferScope;

    private String dispatchBasis;

    private String dispatchPurpose;

    private String dispatchMode;

    private String fromUnit;

    private String toUnit;

    private String fromStation;

    private String toStation;

    private String fromAddress;

    private String toAddress;

    private String contactAddress;

    private LocalDate dispatchDate;

    private LocalDate effectiveDate;

    private LocalDate issueDate;
    /**
     * 源仓库
     */
    private Long sourceWarehouseId;
    /**
     * 源库区
     */
    private Long sourceAreaId;

    /**
     * 目标仓库
     */
    private Long targetWarehouseId;
    /**
     * 目标库区
     */
    private Long targetAreaId;

    /**
     * 状态
     */
    private Integer movementOrderStatus;
    /**
     * 总数量
     */
    private BigDecimal totalQuantity;
    /**
     * 备注
     */
    private String remark;

}
