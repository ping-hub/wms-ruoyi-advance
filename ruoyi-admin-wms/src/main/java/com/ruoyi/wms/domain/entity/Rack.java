package com.ruoyi.wms.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.mybatis.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;

/**
 * 货架对象 wms_rack
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wms_rack")
public class Rack extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id")
    private Long id;

    /**
     * 货架编码
     */
    private String rackCode;

    /**
     * 货架名称
     */
    private String rackName;

    /**
     * 所属仓库
     */
    private Long warehouseId;

    /**
     * 所属库区
     */
    private Long areaId;

    /**
     * 货架状态
     */
    private String rackStatus;

    /**
     * 行数
     */
    private Integer rowCount;

    /**
     * 列数
     */
    private Integer columnCount;

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
     * 排序
     */
    private Long orderNum;

    /**
     * 备注
     */
    private String remark;
}
