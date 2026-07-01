package com.ruoyi.wms.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.mybatis.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.io.Serial;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 出库单对象 wms_shipment_order
 *
 * @author ping
 * @date 2024-08-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wms_shipment_order")
public class ShipmentOrder extends BaseEntity {

    @Serial
    private static final long serialVersionUID=1L;

    /**
     *
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 出库单号，系统自动生成
     */
    private String shipmentOrderNo;
    /**
     * 出库类型
     */
    private String shipmentOrderType;

    private String basisNo;

    private String dispatchMode;

    private String noticeOrg;

    private String receiveUnit;

    private LocalDate purchaseDate;

    private LocalDate shipmentDate;

    /**
     * 订单金额
     */
    private BigDecimal receivableAmount;
    /**
     * 出库数量
     */
    private BigDecimal totalQuantity;
    /**
     * 出库单状态
     */
    private Integer shipmentOrderStatus;
    /**
     * 备注
     */
    private String remark;

    // ========== 审批流程字段 ==========

    /** 申请人ID */
    private Long applicantId;

    /** 申请人姓名 */
    private String applicantName;

    /** 提交时间 */
    private LocalDateTime submitTime;

    /** 审批人ID */
    private Long approverId;

    /** 审批人姓名 */
    private String approverName;

    /** 审批时间 */
    private LocalDateTime approveTime;

    /** 审批意见 */
    private String approveRemark;

    /** 执行人ID */
    private Long executorId;

    /** 执行人姓名 */
    private String executorName;

    /** 执行时间 */
    private LocalDateTime executeTime;

    /** 关联调拨单ID（库外调拨自动生成出库单时记录） */
    private Long movementOrderId;

}
