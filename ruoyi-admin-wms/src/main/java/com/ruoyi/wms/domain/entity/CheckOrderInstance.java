package com.ruoyi.wms.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.ruoyi.common.mybatis.core.domain.BaseEntity;

import java.io.Serial;

/**
 * 盘点实例差异明细对象 wms_check_order_instance
 *
 * @author ping
 * @date 2026-06-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wms_check_order_instance")
public class CheckOrderInstance extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id")
    private Long id;

    /** 盘点单ID */
    private Long checkOrderId;

    /** 关联盘点明细行ID（SKU行） */
    private Long checkOrderDetailId;

    /** 器材规格ID */
    private Long skuId;

    /** 器材实例编码 */
    private String instanceCode;

    /** 器材名称（冗余展示） */
    private String instanceItemName;

    /** 差异类型：loss=盘亏，gain=盘盈 */
    private String resultType;

    /** 备注 */
    private String remark;
}
