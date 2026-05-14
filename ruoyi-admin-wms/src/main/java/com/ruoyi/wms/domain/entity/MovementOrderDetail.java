package com.ruoyi.wms.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.ruoyi.common.mybatis.core.domain.BaseEntity;

import java.math.BigDecimal;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * 调拨单明细对象 wms_movement_order_detail
 *
 * @author zcc
 * @date 2024-08-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wms_movement_order_detail")
public class MovementOrderDetail extends BaseEntity {

    @Serial
    private static final long serialVersionUID=1L;

    /**
     *
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 调拨单Id
     */
    private Long movementOrderId;
    /**
     * 规格id
     */
    private Long skuId;
    /**
     * 数量
     */
    private BigDecimal quantity;

    private String equipmentCode;

    private String specModel;

    private String productMark;

    private String qualityGrade;

    private BigDecimal unitPrice;

    private BigDecimal lineAmount;

    /**
     * 备注
     */
    private String remark;
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
     * 生产日期
     */
    private LocalDateTime productionDate;
    /**
     * 过期时间
     */
    private LocalDateTime expirationDate;
    /**
     * 入库记录id
     */
    private Long inventoryDetailId;

    /**
     * 单品实例ID
     */
    private Long itemInstanceId;

    /**
     * 箱体ID
     */
    private Long boxId;

}
