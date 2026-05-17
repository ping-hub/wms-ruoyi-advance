package com.ruoyi.wms.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * @author zcc
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
    private BigDecimal unitPrice;

    /**
     * 兼容保留：当前库表已无此字段
     */
    @TableField(exist = false)
    private BigDecimal lineAmount;
    /**
     * 生产日期
     */
    private LocalDateTime productionDate;
    /**
     * 过期时间
     */
    private LocalDateTime expirationDate;
    /**
     * 备注
     */
    private String remark;
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

    /**
     * 是否生成单品实例
     */
    private Integer generateItemInstance;

    /**
     * 已生成单品实例数量
     */
    private Integer generatedInstanceQuantity;
}
