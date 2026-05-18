package com.ruoyi.wms.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.wms.domain.entity.Rack;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = Rack.class)
public class RackVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "")
    private Long id;

    /**
     * 货架编码
     */
    @ExcelProperty(value = "货架编码")
    private String rackCode;

    /**
     * 货架名称
     */
    @ExcelProperty(value = "货架名称")
    private String rackName;

    /**
     * 所属仓库
     */
    @ExcelProperty(value = "所属仓库ID")
    private Long warehouseId;

    /**
     * 所属仓库名称
     */
    @ExcelProperty(value = "所属仓库")
    private String warehouseName;

    /**
     * 所属库区
     */
    @ExcelProperty(value = "所属库区ID")
    private Long areaId;

    /**
     * 所属库区名称
     */
    @ExcelProperty(value = "所属库区")
    private String areaName;

    /**
     * 货架状态
     */
    @ExcelProperty(value = "货架状态")
    private String rackStatus;

    /**
     * 行数
     */
    @ExcelProperty(value = "行数")
    private Integer rowCount;

    /**
     * 列数
     */
    @ExcelProperty(value = "列数")
    private Integer columnCount;

    /**
     * 长
     */
    @ExcelProperty(value = "长")
    private BigDecimal length;

    /**
     * 宽
     */
    @ExcelProperty(value = "宽")
    private BigDecimal width;

    /**
     * 高
     */
    @ExcelProperty(value = "高")
    private BigDecimal height;

    /**
     * 排序
     */
    @ExcelProperty(value = "排序")
    private Long orderNum;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;
}
