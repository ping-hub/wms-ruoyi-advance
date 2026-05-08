package com.ruoyi.wms.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.wms.domain.entity.InternalMoveOrderDetail;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 库内移库单明细视图对象 wms_internal_move_order_detail
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = InternalMoveOrderDetail.class)
public class InternalMoveOrderDetailVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "主键")
    private Long id;

    @ExcelProperty(value = "库内移库单Id")
    private Long internalMoveOrderId;

    @ExcelProperty(value = "规格id")
    private Long skuId;

    @ExcelProperty(value = "数量")
    private BigDecimal quantity;

    @ExcelProperty(value = "器材编码")
    private String equipmentCode;

    @ExcelProperty(value = "规格型号")
    private String specModel;

    @ExcelProperty(value = "产品标识")
    private String productMark;

    @ExcelProperty(value = "质量等级")
    private String qualityGrade;

    @ExcelProperty(value = "单价")
    private BigDecimal unitPrice;

    @ExcelProperty(value = "行金额")
    private BigDecimal lineAmount;

    @ExcelProperty(value = "备注")
    private String remark;

    @ExcelProperty(value = "批号")
    private String batchNo;

    @ExcelProperty(value = "生产日期")
    private LocalDateTime productionDate;

    @ExcelProperty(value = "过期时间")
    private LocalDateTime expirationDate;

    @ExcelProperty(value = "源仓库")
    private Long sourceWarehouseId;

    @ExcelProperty(value = "源库区")
    private Long sourceAreaId;

    @ExcelProperty(value = "源货架")
    private Long sourceRackId;

    @ExcelProperty(value = "源货位")
    private Long sourceLocationId;

    @ExcelProperty(value = "目标仓库")
    private Long targetWarehouseId;

    @ExcelProperty(value = "目标库区")
    private Long targetAreaId;

    @ExcelProperty(value = "目标货架")
    private Long targetRackId;

    @ExcelProperty(value = "目标货位")
    private Long targetLocationId;

    @ExcelProperty(value = "库存明细ID")
    private Long inventoryDetailId;

    @ExcelProperty(value = "单品实例ID")
    private Long itemInstanceId;

    @ExcelProperty(value = "单品码")
    private String instanceCode;

    @ExcelProperty(value = "箱体ID")
    private Long boxId;

    @ExcelProperty(value = "箱码")
    private String boxCode;

    private ItemSkuVo itemSku;

    private InventoryDetailVo inventoryDetail;

    private BigDecimal remainQuantity;
}
