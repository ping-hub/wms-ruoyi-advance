package com.ruoyi.wms.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import com.ruoyi.wms.domain.entity.ItemSku;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;



@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = ItemSku.class)
public class ItemSkuVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Long id;

    /**
     * 规格名称
     */
    @ExcelProperty(value = "规格名称")
    private String skuName;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Long itemId;

    /**
     * sku条码
     */
    @ExcelProperty(value = "sku条码")
    private String barcode;

    /**
     * sku编码
     */
    @ExcelProperty(value = "sku编码")
    private String skuCode;

    /**
     * 规格型号
     */
    @ExcelProperty(value = "规格型号")
    private String specModel;

    /**
     * 默认单价
     */
    @ExcelProperty(value = "默认单价")
    private BigDecimal defaultUnitPrice;

    /**
     * 默认质量等级
     */
    @ExcelProperty(value = "默认质量等级")
    private String defaultQualityGrade;

    /**
     * 启用状态
     */
    @ExcelProperty(value = "启用状态")
    private String status;

    /**
     * 体积
     */
    @ExcelProperty(value = "体积")
    private BigDecimal volume;

    /**
     * 长(cm)
     */
    @ExcelProperty(value = "长(cm)")
    private BigDecimal length;

    /**
     * 宽(cm)
     */
    @ExcelProperty(value = "宽(cm)")
    private BigDecimal width;

    /**
     * 高(cm)
     */
    @ExcelProperty(value = "高(cm)")
    private BigDecimal height;

    /**
     * 毛重(kg)
     */
    @ExcelProperty(value = "毛重(kg)")
    private BigDecimal grossWeight;

    /**
     * 净重(kg)
     */
    @ExcelProperty(value = "净重(kg)")
    private BigDecimal netWeight;

    /**
     * 成本价(元)
     */
    @ExcelProperty(value = "成本价(元)")
    private BigDecimal costPrice;

    /**
     * 销售价(元)
     */
    @ExcelProperty(value = "销售价(元)")
    private BigDecimal sellingPrice;

    private ItemVo item;

}
