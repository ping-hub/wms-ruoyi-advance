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
     * 品牌
     */
    @ExcelProperty(value = "品牌")
    private Long itemBrand;

    /**
     * 物品类型
     */
    @ExcelProperty(value = "物品类型")
    private String itemType;

    /**
     * 追踪模式
     */
    @ExcelProperty(value = "追踪模式")
    private String trackingMode;

    /**
     * 是否允许装箱
     */
    @ExcelProperty(value = "允许装箱")
    private Integer allowBox;

    /**
     * 规格等级
     */
    @ExcelProperty(value = "规格等级")
    private String specLevel;

    /**
     * 装备名称
     */
    @ExcelProperty(value = "装备名称")
    private String equipmentName;

    /**
     * 默认质量等级
     */
    @ExcelProperty(value = "默认质量等级")
    private String defaultQualityGrade;

    /**
     * 产品标识规则
     */
    @ExcelProperty(value = "产品标识规则")
    private String productMarkRule;

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
