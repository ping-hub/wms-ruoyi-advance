package com.ruoyi.wms.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.wms.domain.entity.ItemInstance;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = ItemInstance.class)
public class ItemInstanceVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "")
    private Long id;

    @ExcelProperty(value = "单品码")
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

    @ExcelProperty(value = "单品状态")
    private String instanceStatus;

    @ExcelProperty(value = "是否在箱内")
    private Integer inBox;

    @ExcelProperty(value = "是否已借出")
    private Integer borrowed;

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

    @ExcelProperty(value = "所在单位")
    private String belongUnit;

    @ExcelProperty(value = "当前责任单位")
    private String currentOwnerUnit;

    @ExcelProperty(value = "最后一次业务动作")
    private String lastOperationType;

    @ExcelProperty(value = "最后动作时间")
    private LocalDateTime lastOperationTime;

    @ExcelProperty(value = "生产日期")
    private LocalDateTime productionDate;

    @ExcelProperty(value = "过期日期")
    private LocalDateTime expirationDate;

    @ExcelProperty(value = "备注")
    private String remark;
}
