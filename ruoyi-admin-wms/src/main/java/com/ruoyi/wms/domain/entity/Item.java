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
     * 规格等级
     */
    private String specLevel;

    /**
     * 装备名称
     */
    private String equipmentName;

    /**
     * 器材类型
     */
    private String equipmentType;

    /**
     * 启用状态
     */
    private String status;

    /**
     * 产品标识
     * 兼容保留
     */
    private String productMark;

    /**
     * 规格型号文本
     * 兼容保留
     */
    private String modelText;

    /**
     * 备注
     */
    private String remark;


}
