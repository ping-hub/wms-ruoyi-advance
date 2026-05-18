package com.ruoyi.wms.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.common.mybatis.core.domain.BaseVo;
import com.ruoyi.wms.domain.entity.ShipmentOrder;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 出库单视图对象 wms_shipment_order
 *
 * @author zcc
 * @date 2024-08-01
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = ShipmentOrder.class)
public class ShipmentOrderVo extends BaseVo{

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Long id;

    /**
     * 出库单号，系统自动生成
     */
    @ExcelProperty(value = "出库单号，系统自动生成")
    private String shipmentOrderNo;

    /**
     * 出库类型
     */
    @ExcelProperty(value = "出库类型")
    private String shipmentOrderType;

    /**
     * 出库订单
     */
    @ExcelProperty(value = "出库订单")
    private String orderNo;

    /**
     * 客户
     */
    @ExcelProperty(value = "客户")
    private Long merchantId;

    /**
     * 调拨根据
     */
    @ExcelProperty(value = "调拨根据")
    private String basisNo;

    /**
     * 调拨方式
     */
    @ExcelProperty(value = "调拨方式")
    private String dispatchMode;

    /**
     * 通知机关
     */
    @ExcelProperty(value = "通知机关")
    private String noticeOrg;

    /**
     * 收物单位
     */
    @ExcelProperty(value = "收物单位")
    private String receiveUnit;

    /**
     * 采购日期
     */
    @ExcelProperty(value = "采购日期")
    private LocalDate purchaseDate;

    /**
     * 出库日期
     */
    @ExcelProperty(value = "出库日期")
    private LocalDate shipmentDate;

    /**
     * 采购配发人
     */
    @ExcelProperty(value = "采购配发人")
    private String purchaserName;

    /**
     * 验收人
     */
    @ExcelProperty(value = "验收人")
    private String acceptorName;

    /**
     * 保管员
     */
    @ExcelProperty(value = "保管员")
    private String keeperName;

    /**
     * 订单金额
     */
    @ExcelProperty(value = "订单金额")
    private BigDecimal receivableAmount;

    /**
     * 出库数量
     */
    @ExcelProperty(value = "出库数量")
    private BigDecimal totalQuantity;

    /**
     * 出库单状态
     */
    @ExcelProperty(value = "出库单状态")
    private Integer shipmentOrderStatus;

    /**
     * 仓库id
     */
    @ExcelProperty(value = "仓库id")
    private Long warehouseId;

    /**
     * 库区id
     */
    @ExcelProperty(value = "库区id")
    private Long areaId;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

    List<ShipmentOrderDetailVo> details;
}
