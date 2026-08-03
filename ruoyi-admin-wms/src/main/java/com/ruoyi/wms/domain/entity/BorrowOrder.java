package com.ruoyi.wms.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.mybatis.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 器材借用单对象 wms_borrow_order
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wms_borrow_order")
public class BorrowOrder extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id")
    private Long id;

    /** 借用单号 */
    private String borrowOrderNo;

    /** 借用人 */
    private String borrower;

    /** 借用时间 */
    private LocalDateTime borrowTime;

    /** 计划归还日期 */
    private LocalDate planReturnDate;

    /** 借用期限(天) */
    private Integer borrowPeriodDays;

    /** 发货单位 */
    private String fromUnit;

    /** 收货单位 */
    private String toUnit;

    /** 发货人 */
    private String fromPerson;

    /** 收货人 */
    private String toPerson;

    /** 状态：0=草稿, 1=借出中, 2=已归还, -1=已作废 */
    private Integer borrowOrderStatus;

    /** 实际归还时间 */
    private LocalDateTime returnTime;

    /** 器材数量 */
    private Integer totalQuantity;

    /** 备注 */
    private String remark;
}
