package com.ruoyi.wms.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.common.mybatis.core.domain.BaseVo;
import com.ruoyi.wms.domain.entity.ReceiptOrder;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 入库单视图对象 wms_receipt_order
 *
 * @author zcc
 * @date 2024-07-19
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = ReceiptOrder.class)
public class ReceiptOrderVo extends BaseVo {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Long id;

    /**
     * 入库单号
     */
    @ExcelProperty(value = "入库单号")
    private String receiptOrderNo;

    /**
     * 入库类型
     */
    @ExcelProperty(value = "入库类型")
    private Long receiptOrderType;

    /**
     * 供应商
     */
    @ExcelProperty(value = "供应商")
    private Long merchantId;

    /**
     * 订单号
     */
    @ExcelProperty(value = "订单号")
    private String orderNo;

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
     * 入库日期
     */
    @ExcelProperty(value = "入库日期")
    private LocalDate receiptDate;

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
     * 商品总数
     */
    @ExcelProperty(value = "商品总数")
    private BigDecimal totalQuantity;

    /**
     * 订单金额
     */
    @ExcelProperty(value = "订单金额")
    private BigDecimal payableAmount;

    /**
     * 入库状态
     */
    @ExcelProperty(value = "入库状态")
    private Integer receiptOrderStatus;

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

    private List<ReceiptOrderDetailVo> details;
}
