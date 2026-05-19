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

    /**
     * 器材编码
     */
    private String itemCode;

    /**
     * 器材名称
     */
    private String itemName;

    /**
     * 规格型号
     */
    private String skuName;

    /**
     * 计量单位
     */
    private String unit;

    /**
     * 产品标识
     */
    private String productIdentifier;

    /**
     * 质量等级
     */
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
     * 目标仓库
     */
    private Long targetWarehouseId;
    /**
     * 目标库区
     */
    private Long targetAreaId;

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
