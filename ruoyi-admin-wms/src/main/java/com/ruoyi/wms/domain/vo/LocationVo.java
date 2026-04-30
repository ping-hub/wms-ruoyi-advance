package com.ruoyi.wms.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.wms.domain.entity.Location;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

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
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;
}
