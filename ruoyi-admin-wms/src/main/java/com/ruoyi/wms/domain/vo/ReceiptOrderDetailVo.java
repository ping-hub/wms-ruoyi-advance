package com.ruoyi.wms.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import com.ruoyi.wms.domain.entity.ReceiptOrderDetail;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 入库单详情视图对象 wms_receipt_order_detail
 *
 * @author ping
 * @date 2024-07-19
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = ReceiptOrderDetail.class)
public class ReceiptOrderDetailVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Long id;
    /**
     * 器材实例编码
     */
    private String instanceCode;

    /**
     * 入库单号
     */
    @ExcelProperty(value = "入库单号")
    private Long receiptOrderId;

    /**
     * 规格id
     */
    @ExcelProperty(value = "规格id")
    private Long skuId;

    /**
     * 入库数量
     */
    @ExcelProperty(value = "入库数量")
    private BigDecimal quantity;

    /**
     * 器材编码
     */
    @ExcelProperty(value = "器材编码")
    private String itemCode;

    /**
     * 器材名称
     */
    @ExcelProperty(value = "器材名称")
    private String itemName;

    /**
     * 规格型号
     */
    @ExcelProperty(value = "规格型号")
    private String skuName;

    /**
     * 计量单位
     */
    @ExcelProperty(value = "计量单位")
    private String unit;

    /**
     * 产品标识
     */
    @ExcelProperty(value = "产品标识")
    private String productIdentifier;

    /**
     * 质量等级
     */
    @ExcelProperty(value = "质量等级")
    private String qualityGrade;

    /**
     * 单价
     */
    @ExcelProperty(value = "单价")
    private BigDecimal unitPrice;

    /**
     * 总价
     */
    @ExcelProperty(value = "总价")
    private BigDecimal lineAmount;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

    /**
     * 箱码
     */
    @ExcelProperty(value = "箱码")
    /**
     * 器材实例编码
     */

    private String boxCode;

    /**
     * 所属仓库
     */
    @ExcelProperty(value = "所属仓库")
    private Long warehouseId;

    /**
     * 所属库区
     */
    @ExcelProperty(value = "所属库区")
    private Long areaId;

    /**
     * 所属货架
     */
    @ExcelProperty(value = "所属货架")
    private Long rackId;

    /**
     * 所属货位
     */
    @ExcelProperty(value = "所属货位")
    private Long locationId;

    private ItemSkuVo itemSku;

    private List<ReceiptItemInstanceVo> receiptItemInstances;
}
