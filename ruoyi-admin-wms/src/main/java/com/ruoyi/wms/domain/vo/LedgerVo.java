package com.ruoyi.wms.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ExcelIgnoreUnannotated
public class LedgerVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "器材编码")
    private String equipmentCode;

    @ExcelProperty(value = "器材名称")
    private String itemName;

    @ExcelProperty(value = "规格型号")
    private String specModel;

    @ExcelProperty(value = "产品标识")
    private String productMark;

    @ExcelProperty(value = "装备名称")
    private String equipmentName;

    @ExcelProperty(value = "质量等级")
    private String qualityGrade;

    @ExcelProperty(value = "单价")
    private BigDecimal unitPrice;

    @ExcelProperty(value = "数量")
    private BigDecimal quantity;

    @ExcelProperty(value = "计量单位")
    private String unit;

    @ExcelProperty(value = "总价")
    private BigDecimal lineAmount;

    @ExcelProperty(value = "所在单位")
    private String belongUnit;
}
