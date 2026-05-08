package com.ruoyi.wms.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.mybatis.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * 库存调整主表对象 wms_adjust_order
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wms_adjust_order")
public class AdjustOrder extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id")
    private Long id;

    private String adjustOrderNo;

    private LocalDateTime adjustDate;

    private Long warehouseId;

    private Long areaId;

    private Long rackId;

    private Long locationId;

    private String adjustReason;

    private Integer adjustStatus;

    private String handlerName;

    private String remark;
}
