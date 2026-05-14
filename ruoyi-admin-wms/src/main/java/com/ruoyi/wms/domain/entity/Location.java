package com.ruoyi.wms.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.mybatis.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;

/**
 * 货位对象 wms_location
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wms_location")
public class Location extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id")
    private Long id;

    /**
     * 货位编码
     */
    private String locationCode;

    /**
     * 货位名称
     */
    private String locationName;

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
     * 货位状态
     */
    private String locationStatus;

    /**
     * 货位类型
     */
    private String locationType;

    /**
     * 行号
     */
    private Integer rowNo;

    /**
     * 列号
     */
    private Integer columnNo;

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
     * 承重
     */
    private BigDecimal maxWeight;

    /**
     * 是否占用
     */
    private Integer occupiedFlag;

    /**
     * 排序
     */
    private Long sortNo;

    /**
     * 备注
     */
    private String remark;
}
