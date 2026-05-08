package com.ruoyi.wms.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.wms.domain.entity.Warehouse;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;


@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = Warehouse.class)
public class WarehouseVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Long id;

    /**
     * 编号
     */
    @ExcelProperty(value = "编号")
    private String warehouseCode;

    /**
     * 名称
     */
    @ExcelProperty(value = "名称")
    private String warehouseName;

    /**
     * 启用状态
     */
    @ExcelProperty(value = "启用状态")
    private String status;

    /**
     * 仓库类型
     */
    @ExcelProperty(value = "仓库类型")
    private String warehouseType;

    /**
     * 地址
     */
    @ExcelProperty(value = "地址")
    private String address;

    /**
     * 负责人
     */
    @ExcelProperty(value = "负责人")
    private String managerName;

    /**
     * 负责人电话
     */
    @ExcelProperty(value = "负责人电话")
    private String managerPhone;
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

    private List<AreaVo> areaList;
}
