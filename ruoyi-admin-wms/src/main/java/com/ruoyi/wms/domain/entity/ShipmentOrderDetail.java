package com.ruoyi.wms.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.ruoyi.common.mybatis.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * 出库单详情对象 wms_shipment_order_detail
 *
 * @author zcc
 * @date 2024-08-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wms_shipment_order_detail")
public class ShipmentOrderDetail extends BaseEntity {

    @Serial
    private static final long serialVersionUID=1L;

    /**
     *
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 出库单
     */
    private Long shipmentOrderId;
    /**
     * 规格id
     */
    private Long skuId;
    /**
     * 数量
     */
    private BigDecimal quantity;
    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 兼容保留：当前库表已无此字段
     */
    @TableField(exist = false)
    private String equipmentCode;

    /**
     * 兼容保留：当前库表已无此字段
     */
    @TableField(exist = false)
    private String specModel;

    /**
     * 兼容保留：当前库表已无此字段
     */
    @TableField(exist = false)
    private String productMark;

    /**
     * 兼容保留：当前库表已无此字段
     */
    @TableField(exist = false)
    private String qualityGrade;

    /**
     * 兼容保留：当前库表已无此字段
     */
    @TableField(exist = false)
    private BigDecimal unitPrice;

    /**
     * 兼容保留：当前库表已无此字段
     */
    @TableField(exist = false)
    private BigDecimal lineAmount;
    /**
     * 所属仓库
     */
    private Long warehouseId;
    /**
     * 所属库区
     */
    private Long areaId;
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

    /**
     * 备注
     */
    private String remark;

}
