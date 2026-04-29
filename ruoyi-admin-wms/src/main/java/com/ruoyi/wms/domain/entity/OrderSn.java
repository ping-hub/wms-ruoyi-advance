package com.ruoyi.wms.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.mybatis.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 单据SN关联实体
 *
 * @author ruoyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wms_order_sn")
public class OrderSn extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 单据类型: 1-入库 2-出库 3-移库 4-盘点
     */
    private Integer orderType;

    /**
     * 单据ID
     */
    private Long orderId;

    /**
     * 单据明细ID
     */
    private Long orderDetailId;

    /**
     * SN ID
     */
    private Long snId;

    /**
     * SN码
     */
    private String snCode;

}
