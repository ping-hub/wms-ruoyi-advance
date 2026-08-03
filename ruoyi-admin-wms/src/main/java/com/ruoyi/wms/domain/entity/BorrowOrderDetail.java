package com.ruoyi.wms.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.mybatis.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * 器材借用单明细对象 wms_borrow_order_detail
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wms_borrow_order_detail")
public class BorrowOrderDetail extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id")
    private Long id;

    /** 借用单ID */
    private Long borrowOrderId;

    /** 器材识别码 */
    private String instanceCode;

    /** 规格ID */
    private Long skuId;

    /** 器材编码 */
    private String itemCode;

    /** 器材名称 */
    private String itemName;

    /** 规格型号 */
    private String skuName;

    /** 计量单位 */
    private String unit;

    /** 产品标识 */
    private String productIdentifier;

    /** 质量等级 */
    private String qualityGrade;

    /** 借出前仓库ID */
    private Long warehouseId;

    /** 借出前库区ID */
    private Long areaId;

    /** 借出前货架ID */
    private Long rackId;

    /** 借出前货位ID */
    private Long locationId;

    /** 归还状态：0=未归还, 1=已归还 */
    private Integer returnStatus;

    /** 归还时间 */
    private LocalDateTime returnTime;

    /** 备注 */
    private String remark;
}
