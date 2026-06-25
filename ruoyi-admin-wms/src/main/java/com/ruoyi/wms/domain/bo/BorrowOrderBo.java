package com.ruoyi.wms.domain.bo;

import com.ruoyi.wms.domain.entity.BorrowOrder;
import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import com.ruoyi.common.mybatis.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import io.github.linpeilie.annotations.AutoMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 器材借用单业务对象 wms_borrow_order
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = BorrowOrder.class, reverseConvertGenerate = false)
public class BorrowOrderBo extends BaseEntity {

    @NotNull(message = "id不能为空", groups = { EditGroup.class })
    private Long id;

    /** 借用单号 */
    private String borrowOrderNo;

    /** 借用人 */
    @NotBlank(message = "借用人不能为空", groups = { AddGroup.class, EditGroup.class })
    private String borrower;

    /** 借用时间 */
    private LocalDateTime borrowTime;

    /** 计划归还日期 */
    @NotNull(message = "计划归还日期不能为空", groups = { AddGroup.class, EditGroup.class })
    private LocalDate planReturnDate;

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
    private List<BorrowOrderDetailBo> details;
}
