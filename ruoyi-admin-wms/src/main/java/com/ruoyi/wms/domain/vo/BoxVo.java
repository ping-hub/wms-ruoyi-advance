package com.ruoyi.wms.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.wms.domain.entity.Box;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = Box.class)
public class BoxVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "")
    private Long id;

    @ExcelProperty(value = "箱码")
    private String boxCode;

    @ExcelProperty(value = "箱体名称")
    private String boxName;

    @ExcelProperty(value = "箱体状态")
    private String boxStatus;

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

    @ExcelProperty(value = "箱内数量")
    private Integer itemCount;

    @ExcelProperty(value = "备注")
    private String remark;

    private List<ItemInstanceVo> items;
}
