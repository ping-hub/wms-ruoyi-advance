package com.ruoyi.wms.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.ruoyi.common.mybatis.core.domain.BaseEntity;

import java.math.BigDecimal;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * 库存盘点单据详情对象 wms_check_order_detail
 *
 * @author ping
 * @date 2024-08-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wms_check_order_detail")
public class CheckOrderDetail extends BaseEntity {

    @Serial
    private static final long serialVersionUID=1L;

    /**
     *
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 器材识别码
     */
    private String instanceCode;
    /**
     * 盘点单id
     */
    private Long checkOrderId;
    /**
     * 规格id
     */
    private Long skuId;
    /**
     * 库存数量
     */
    private BigDecimal quantity;
    /**
     * 盘点数量
     */
    private BigDecimal checkQuantity;
    /**
     * 差异数量
     */
    private BigDecimal differenceQuantity;
    /**
     * 盈亏数（正=盘盈，负=盘亏）
     */
    private BigDecimal profitAndLoss;
    /**
     * 入库时间
     */
    private LocalDateTime receiptTime;
    /**
     * 所属仓库
     */
    private Long warehouseId;
    /**
     * 所属库区
     */
    private Long areaId;
    /**
     * 货架
     */
    private Long rackId;
    /**
     * 货位
     */
    private Long locationId;
    /**
     * 入库记录id
     */
    private Long inventoryDetailId;
    /**
     * 物品明细ID
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
