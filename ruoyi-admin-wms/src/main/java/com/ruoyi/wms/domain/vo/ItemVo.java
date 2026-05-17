package com.ruoyi.wms.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.wms.domain.entity.Item;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;


@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = Item.class)
public class ItemVo implements Serializable {

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
    private String itemCode;

    /**
     * 名称
     */
    @ExcelProperty(value = "名称")
    private String itemName;

    /**
     * 分类
     */
    @ExcelProperty(value = "分类")
    private String itemCategory;

    /**
     * 单位类别
     */
    @ExcelProperty(value = "单位类别")
    private String unit;

    /**
     * 等级
     */
    @ExcelProperty(value = "等级")
    private String level;

    /**
     * 装备名称
     */
    @ExcelProperty(value = "装备名称")
    private String equipmentName;

    /**
     * 器材类型
     */
    @ExcelProperty(value = "器材类型")
    private String equipmentType;

    /**
     * 启用状态
     */
    @ExcelProperty(value = "启用状态")
    private String status;

    /**
     * 规格型号文本
     */
    @ExcelProperty(value = "规格型号文本")
    private String modelText;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

    /**
     * 类别
     */
    private ItemCategoryVo itemCategoryInfo;

    private List<ItemSkuVo> sku;
}
