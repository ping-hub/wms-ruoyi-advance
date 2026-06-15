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
 * @author ping
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
     * 器材实例编码
     */
    private String instanceCode;
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

    /**
     * 单价
     */
    private BigDecimal unitPrice;

    /**
     * 总价
     */
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
     * 入库记录id
     */
    private Long inventoryDetailId;

    /**
     * 单品实例ID
     */

    /**
     * 箱体ID
     */
    private Long boxId;

    /**
     * 备注
     */
    private String remark;

}
