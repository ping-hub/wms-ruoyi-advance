package com.ruoyi.wms.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.common.mybatis.core.domain.BaseVo;
import com.ruoyi.wms.domain.entity.InternalMoveOrder;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.List;

/**
 * 库内移库单视图对象 wms_internal_move_order
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = InternalMoveOrder.class)
public class InternalMoveOrderVo extends BaseVo {

    @Serial
    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "主键")
    private Long id;

    @ExcelProperty(value = "库内移库单号")
    private String internalMoveOrderNo;

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

    @ExcelProperty(value = "移库原因")
    private String moveReason;

    @ExcelProperty(value = "状态")
    private Integer internalMoveStatus;

    @ExcelProperty(value = "总数量")
    private BigDecimal totalQuantity;

    @ExcelProperty(value = "备注")
    private String remark;

    private List<InternalMoveOrderDetailVo> details;
}
