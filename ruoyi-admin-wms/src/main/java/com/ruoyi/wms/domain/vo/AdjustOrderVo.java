package com.ruoyi.wms.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.common.mybatis.core.domain.BaseVo;
import com.ruoyi.wms.domain.entity.AdjustOrder;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 库存调整主表视图对象 wms_adjust_order
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = AdjustOrder.class)
public class AdjustOrderVo extends BaseVo {

    @Serial
    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "ID")
    private Long id;

    @ExcelProperty(value = "调整单号")
    private String adjustOrderNo;

    @ExcelProperty(value = "调整日期")
    private LocalDateTime adjustDate;

    @ExcelProperty(value = "仓库")
    private Long warehouseId;

    @ExcelProperty(value = "库区")
    private Long areaId;

    @ExcelProperty(value = "货架")
    private Long rackId;

    @ExcelProperty(value = "货位")
    private Long locationId;

    @ExcelProperty(value = "调整原因")
    private String adjustReason;

    @ExcelProperty(value = "状态")
    private Integer adjustStatus;

    @ExcelProperty(value = "经手人")
    private String handlerName;

    @ExcelProperty(value = "备注")
    private String remark;

    private List<AdjustOrderDetailVo> details;
}
