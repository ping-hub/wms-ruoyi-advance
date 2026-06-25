package com.ruoyi.wms.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.ruoyi.common.mybatis.core.domain.BaseEntity;

import java.math.BigDecimal;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * 库存盘点单据对象 wms_check_order
 *
 * @author ping
 * @date 2024-08-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wms_check_order")
public class CheckOrder extends BaseEntity {

    @Serial
    private static final long serialVersionUID=1L;

    /**
     *
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 盘点单号
     */
    private String checkOrderNo;
    /**
     * 盘点单状态：-2已驳回 -1作废 0草稿 1待盘点 2待复核 3已完成
     */
    private Integer checkOrderStatus;
    /**
     * 盈亏数
     */
    private BigDecimal checkOrderTotal;
    /**
     * 所属仓库
     */
    private Long warehouseId;
    /**
     * 所属库区
     */
    private Long areaId;
    /**
     * 货架
     */
    private Long rackId;
    /**
     * 盘点范围类型
     */
    private String checkScopeType;
    /**
     * 盘点日期
     */
    private LocalDateTime checkDate;
    /**
     * 备注
     */
    private String remark;

    // ========== 流程字段 ==========

    /** 申请人ID */
    private Long applicantId;

    /** 申请人姓名 */
    private String applicantName;

    /** 提交时间 */
    private LocalDateTime submitTime;

    /** 盘点人ID */
    private Long executorId;

    /** 当前执行人姓名 */
    private String executorName;

    /** 复核人ID */
    private Long reviewerId;

    /** 复核人姓名 */
    private String reviewerName;

    /** 执行时间 */
    private LocalDateTime executeTime;

    /** 驳回/审批意见 */
    private String approveRemark;

}
