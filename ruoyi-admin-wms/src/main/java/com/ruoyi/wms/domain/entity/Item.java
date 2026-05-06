package com.ruoyi.wms.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.mybatis.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wms_item")
public class Item extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 编号
     */
    private String itemCode;

    /**
     * 名称
     */
    private String itemName;

    /**
     * 分类
     */
    private String itemCategory;

    /**
     * 单位类别
     */
    private String unit;

    /**
     * 品牌
     */
    private Long itemBrand;

    /**
     * 物品类型
     */
    private String itemType;

    /**
     * 追踪模式
     */
    private String trackingMode;

    /**
     * 是否允许装箱
     */
    private Integer allowBox;

    /**
     * 规格等级
     */
    private String specLevel;

    /**
     * 装备名称
     */
    private String equipmentName;

    /**
     * 默认质量等级
     */
    private String defaultQualityGrade;

    /**
     * 产品标识规则
     */
    private String productMarkRule;

    /**
     * 规格型号文本
     */
    private String modelText;

    /**
     * 备注
     */
    private String remark;


}
