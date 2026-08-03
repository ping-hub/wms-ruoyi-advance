package com.ruoyi.wms.domain.vo;

import com.ruoyi.common.mybatis.core.domain.BaseVo;
import com.ruoyi.wms.domain.entity.BorrowOrder;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 器材借用单视图对象 wms_borrow_order
 */
@Data
@AutoMapper(target = BorrowOrder.class)
public class BorrowOrderVo extends BaseVo {

    @Serial
    private static final long serialVersionUID = 1L;

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

    /** 状态 */
    private Integer borrowOrderStatus;

    /** 实际归还时间 */
    private LocalDateTime returnTime;

    /** 器材数量 */
    private Integer totalQuantity;

    /** 备注 */
    private String remark;

    /** 器材明细 */
    private List<BorrowOrderDetailVo> details;

    /** 预警标记：0=正常, 1=预警(即将超时), 2=超时(已超期) */
    private Integer warningFlag;

    /** 超期天数（动态计算，仅超时时有值） */
    private Integer overdueDays;
}
