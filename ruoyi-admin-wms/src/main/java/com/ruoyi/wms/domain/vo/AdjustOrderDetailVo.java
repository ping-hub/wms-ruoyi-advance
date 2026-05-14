package com.ruoyi.wms.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.wms.domain.entity.AdjustOrderDetail;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 库存调整明细视图对象 wms_adjust_order_detail
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = AdjustOrderDetail.class)
public class AdjustOrderDetailVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "ID")
    private Long id;

    @ExcelProperty(value = "调整单ID")
    private Long adjustOrderId;

    @ExcelProperty(value = "行号")
    private Integer lineNo;

    @ExcelProperty(value = "规格ID")
    private Long skuId;

    @ExcelProperty(value = "器材编码")
    private String equipmentCode;

    @ExcelProperty(value = "规格型号")
    private String specModel;

    @ExcelProperty(value = "产品标识")
    private String productMark;

    @ExcelProperty(value = "质量等级")
    private String qualityGrade;

    @ExcelProperty(value = "仓库")
    private Long warehouseId;

    @ExcelProperty(value = "库区")
    private Long areaId;

    @ExcelProperty(value = "货架")
    private Long rackId;

    @ExcelProperty(value = "货位")
    private Long locationId;

    @ExcelProperty(value = "物品明细ID")
    private Long itemInstanceId;

    @ExcelProperty(value = "单品码")
    private String instanceCode;

    @ExcelProperty(value = "箱体ID")
    private Long boxId;

    @ExcelProperty(value = "箱码")
    private String boxCode;

    @ExcelProperty(value = "库存明细ID")
    private Long inventoryDetailId;

    @ExcelProperty(value = "调整前数量")
    private BigDecimal beforeQuantity;

    @ExcelProperty(value = "调整后数量")
    private BigDecimal afterQuantity;

    @ExcelProperty(value = "差异数量")
    private BigDecimal differenceQuantity;

    @ExcelProperty(value = "生产日期")
    private LocalDateTime productionDate;

    @ExcelProperty(value = "过期日期")
    private LocalDateTime expirationDate;

    @ExcelProperty(value = "备注")
    private String remark;

    private ItemSkuVo itemSku;

    private InventoryDetailVo inventoryDetail;

    private BigDecimal remainQuantity;
}
