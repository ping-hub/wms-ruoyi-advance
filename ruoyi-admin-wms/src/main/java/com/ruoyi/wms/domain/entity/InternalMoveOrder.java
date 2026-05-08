package com.ruoyi.wms.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.mybatis.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;

/**
 * 库内移库单对象 wms_internal_move_order
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wms_internal_move_order")
public class InternalMoveOrder extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id")
    private Long id;

    /**
     * 库内移库单号
     */
    private String internalMoveOrderNo;

    /**
     * 源仓库
     */
    private Long sourceWarehouseId;

    /**
     * 源库区
     */
    private Long sourceAreaId;

    /**
     * 源货架
     */
    private Long sourceRackId;

    /**
     * 源货位
     */
    private Long sourceLocationId;

    /**
     * 目标仓库
     */
    private Long targetWarehouseId;

    /**
     * 目标库区
     */
    private Long targetAreaId;

    /**
     * 目标货架
     */
    private Long targetRackId;

    /**
     * 目标货位
     */
    private Long targetLocationId;

    /**
     * 移库原因
     */
    private String moveReason;

    /**
     * 单据状态
     */
    private Integer internalMoveStatus;

    /**
     * 总数量
     */
    private BigDecimal totalQuantity;

    /**
     * 备注
     */
    private String remark;
}
