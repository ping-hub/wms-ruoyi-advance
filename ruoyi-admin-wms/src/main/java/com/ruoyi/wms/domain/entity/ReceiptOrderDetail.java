package com.ruoyi.wms.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.mybatis.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 入库单详情对象 wms_receipt_order_detail
 *
 * @author ping
 * @date 2024-07-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wms_receipt_order_detail")
public class ReceiptOrderDetail extends BaseEntity {

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
     * 入库单号
     */
    private Long receiptOrderId;
    /**
     * 规格id
     */
    private Long skuId;
    /**
     * 入库数量
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
     * 备注
     */
    private String remark;

    /**
     * 箱码
     */
    private String boxCode;

    /**
     * 所属仓库
     */
    private Long warehouseId;
    /**
     * 所属库区
     */
    private Long areaId;

    /**
     * 所属货架
     */
    private Long rackId;

    /**
     * 所属货位
     */
    private Long locationId;

}
