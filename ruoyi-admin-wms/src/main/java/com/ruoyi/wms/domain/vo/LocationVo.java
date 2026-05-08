package com.ruoyi.wms.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.wms.domain.entity.Location;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = Location.class)
public class LocationVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "")
    private Long id;

    /**
     * 货位编码
     */
    @ExcelProperty(value = "货位编码")
    private String locationCode;

    /**
     * 货位名称
     */
    @ExcelProperty(value = "货位名称")
    private String locationName;

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
     * 所属货架
     */
    @ExcelProperty(value = "所属货架ID")
    private Long rackId;

    /**
     * 所属货架编码
     */
    @ExcelProperty(value = "货架编码")
    private String rackCode;

    /**
     * 所属货架名称
     */
    @ExcelProperty(value = "所属货架")
    private String rackName;

    /**
     * 货位状态
     */
    @ExcelProperty(value = "货位状态")
    private String locationStatus;

    /**
     * 货位类型
     */
    @ExcelProperty(value = "货位类型")
    private String locationType;

    /**
     * 行号
     */
    @ExcelProperty(value = "行号")
    private Integer rowNo;

    /**
     * 列号
     */
    @ExcelProperty(value = "列号")
    private Integer columnNo;

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
     * 容积
     */
    @ExcelProperty(value = "容积")
    private BigDecimal volume;

    /**
     * 承重
     */
    @ExcelProperty(value = "承重")
    private BigDecimal maxWeight;

    /**
     * 是否占用
     */
    @ExcelProperty(value = "是否占用")
    private Integer occupiedFlag;

    /**
     * 排序
     */
    @ExcelProperty(value = "排序")
    private Long sortNo;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;
}
