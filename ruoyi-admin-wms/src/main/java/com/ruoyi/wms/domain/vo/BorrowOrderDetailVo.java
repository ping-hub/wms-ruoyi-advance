package com.ruoyi.wms.domain.vo;

import com.ruoyi.wms.domain.entity.BorrowOrderDetail;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * 器材借用单明细视图对象 wms_borrow_order_detail
 */
@Data
@AutoMapper(target = BorrowOrderDetail.class)
public class BorrowOrderDetailVo {

    @Serial
    private static final long serialVersionUID = 1L;

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

    /** 仓库名称 */
    private String warehouseName;

    /** 库区名称 */
    private String areaName;

    /** 借出前货架ID */
    private Long rackId;

    /** 货架名称 */
    private String rackName;

    /** 借出前货位ID */
    private Long locationId;

    /** 货位名称 */
    private String locationName;

    /** 归还状态 */
    private Integer returnStatus;
    /** 所属箱ID（从 ItemInstance 带入，不持久化） */
    @com.baomidou.mybatisplus.annotation.TableField(exist = false)
    private Long boxId;
    /** 是否随箱出库（前端传入，不持久化） */
    @com.baomidou.mybatisplus.annotation.TableField(exist = false)
    private Boolean boxOutbound;

    /** 归还时间 */
    private LocalDateTime returnTime;

    /** 备注 */
    private String remark;
}
