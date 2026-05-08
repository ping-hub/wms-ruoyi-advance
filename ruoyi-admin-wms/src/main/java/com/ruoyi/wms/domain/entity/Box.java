package com.ruoyi.wms.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.mybatis.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;

/**
 * 箱体对象 wms_box
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wms_box")
public class Box extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id")
    private Long id;

    /**
     * 箱码
     */
    private String boxCode;

    /**
     * 箱体名称
     */
    private String boxName;

    /**
     * 箱体状态
     */
    private String boxStatus;

    /**
     * 长
     */
    private BigDecimal length;

    /**
     * 宽
     */
    private BigDecimal width;

    /**
     * 高
     */
    private BigDecimal height;

    /**
     * 容积
     */
    private BigDecimal volume;

    /**
     * 承重
     */
    private BigDecimal maxWeight;

    /**
     * 所属仓库
     */
    private Long warehouseId;

    /**
     * 所属库区
     */
    private Long areaId;

    /**
     * 所属货架
     */
    private Long rackId;

    /**
     * 所属货位
     */
    private Long locationId;

    /**
     * 箱内数量快照
     */
    private Integer itemCount;

    /**
     * 备注
     */
    private String remark;
}
