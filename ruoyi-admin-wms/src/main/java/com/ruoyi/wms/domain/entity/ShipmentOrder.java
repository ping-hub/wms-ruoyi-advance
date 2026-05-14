package com.ruoyi.wms.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.mybatis.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.io.Serial;
import java.time.LocalDate;

/**
 * 出库单对象 wms_shipment_order
 *
 * @author zcc
 * @date 2024-08-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wms_shipment_order")
public class ShipmentOrder extends BaseEntity {

    @Serial
    private static final long serialVersionUID=1L;

    /**
     *
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 出库单号，系统自动生成
     */
    private String shipmentOrderNo;
    /**
     * 出库类型
     */
    private Long shipmentOrderType;
    /**
     * 出库订单
     */
    private String orderNo;

    private String basisNo;

    private String dispatchMode;

    private String noticeOrg;

    private String receiveUnit;

    private LocalDate purchaseDate;

    private LocalDate shipmentDate;

    private String purchaserName;

    private String acceptorName;

    private String keeperName;
    /**
     * 客户
     */
    private Long merchantId;
    /**
     * 订单金额
     */
    private BigDecimal receivableAmount;
    /**
     * 出库数量
     */
    private BigDecimal totalQuantity;
    /**
     * 出库单状态
     */
    private Integer shipmentOrderStatus;
    /**
     * 仓库id
     */
    private Long warehouseId;
    /**
     * 库区id
     */
    private Long areaId;
    /**
     * 备注
     */
    private String remark;

}
