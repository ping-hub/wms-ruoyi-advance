package com.ruoyi.wms.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.wms.domain.entity.BorrowRecord;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = BorrowRecord.class)
public class BorrowRecordVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "")
    private Long id;

    @ExcelProperty(value = "单品实例ID")
    private Long itemInstanceId;

    @ExcelProperty(value = "单品码")
    private String instanceCode;

    @ExcelProperty(value = "物品名称")
    private String itemName;

    @ExcelProperty(value = "规格名称")
    private String skuName;

    @ExcelProperty(value = "借还状态")
    private String borrowStatus;

    @ExcelProperty(value = "借用人")
    private String borrower;

    @ExcelProperty(value = "发货单位")
    private String fromUnit;

    @ExcelProperty(value = "收货单位")
    private String toUnit;

    @ExcelProperty(value = "发货人")
    private String fromPerson;

    @ExcelProperty(value = "收货人")
    private String toPerson;

    @ExcelProperty(value = "单据日期")
    private LocalDate docDate;

    @ExcelProperty(value = "产品标识")
    private String productMark;

    @ExcelProperty(value = "质量等级")
    private String qualityGrade;

    @ExcelProperty(value = "借用时间")
    private LocalDateTime borrowTime;

    @ExcelProperty(value = "归还时间")
    private LocalDateTime returnTime;

    @ExcelProperty(value = "借用备注")
    private String borrowRemark;

    @ExcelProperty(value = "归还备注")
    private String returnRemark;

    @ExcelProperty(value = "原仓库")
    private Long originalWarehouseId;

    private String originalWarehouseName;

    @ExcelProperty(value = "原库区")
    private Long originalAreaId;

    private String originalAreaName;

    @ExcelProperty(value = "原货架")
    private Long originalRackId;

    private String originalRackName;

    @ExcelProperty(value = "原货位")
    private Long originalLocationId;

    private String originalLocationName;

    @ExcelProperty(value = "归还仓库")
    private Long returnedWarehouseId;

    private String returnedWarehouseName;

    @ExcelProperty(value = "归还库区")
    private Long returnedAreaId;

    private String returnedAreaName;

    @ExcelProperty(value = "归还货架")
    private Long returnedRackId;

    private String returnedRackName;

    @ExcelProperty(value = "归还货位")
    private Long returnedLocationId;

    private String returnedLocationName;
}
