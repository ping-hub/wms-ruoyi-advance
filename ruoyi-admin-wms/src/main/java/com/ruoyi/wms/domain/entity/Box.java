package com.ruoyi.wms.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.mybatis.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

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
     * 关联出库单据ID
     */
    private Long outboundOrderId;

    /**
     * 关联单据类型：SHIPMENT/BORROW_ORDER
     */
    private String outboundOrderType;

    /**
     * 出库时间
     */
    private java.time.LocalDateTime outboundTime;

    /**
     * 备注
     */
    private String remark;
}
