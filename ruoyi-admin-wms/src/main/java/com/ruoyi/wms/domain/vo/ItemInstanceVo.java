package com.ruoyi.wms.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.wms.domain.entity.ItemInstance;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = ItemInstance.class)
public class ItemInstanceVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "")
    private Long id;

    @ExcelProperty(value = "器材实例编码")
    private String instanceCode;

    @ExcelProperty(value = "物品ID")
    private Long itemId;

    @ExcelProperty(value = "物品名称")
    private String itemName;

    @ExcelProperty(value = "物品编码")
    private String itemCode;

    @ExcelProperty(value = "规格ID")
    private Long skuId;

    @ExcelProperty(value = "规格名称")
    private String skuName;

    @ExcelProperty(value = "计量单位")
    private String unit;

    @ExcelProperty(value = "产品标识")
    private String productIdentifier;

    @ExcelProperty(value = "质量等级")
    private String qualityGrade;

    @ExcelProperty(value = "单品状态")
    private String instanceStatus;

    @ExcelProperty(value = "所属仓库")
    private Long warehouseId;

    @ExcelProperty(value = "仓库名称")
    private String warehouseName;

    @ExcelProperty(value = "所属库区")
    private Long areaId;

    @ExcelProperty(value = "库区名称")
    private String areaName;

    @ExcelProperty(value = "所属货架")
    private Long rackId;

    @ExcelProperty(value = "货架名称")
    private String rackName;

    @ExcelProperty(value = "所属货位")
    private Long locationId;

    @ExcelProperty(value = "货位名称")
    private String locationName;

    @ExcelProperty(value = "来源类型")
    private String sourceType;

    @ExcelProperty(value = "来源单据类型")
    private String sourceOrderType;

    @ExcelProperty(value = "箱体ID")
    private Long boxId;

    @ExcelProperty(value = "箱码")
    private String boxCode;

    @ExcelProperty(value = "来源单据ID")
    private Long sourceOrderId;

    @ExcelProperty(value = "来源单据号")
    private String sourceOrderNo;

    @ExcelProperty(value = "来源入库单明细ID")
    private Long receiptOrderDetailId;

    @ExcelProperty(value = "来源出库单明细ID")
    private Long shipmentOrderDetailId;

    private String currentBusinessType;

    private String currentBusinessNo;

    @ExcelProperty(value = "备注")
    private String remark;
}
