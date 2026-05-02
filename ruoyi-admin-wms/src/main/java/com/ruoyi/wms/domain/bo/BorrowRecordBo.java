package com.ruoyi.wms.domain.bo;

import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import com.ruoyi.common.mybatis.core.domain.BaseEntity;
import com.ruoyi.wms.domain.entity.BorrowRecord;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = BorrowRecord.class, reverseConvertGenerate = false)
public class BorrowRecordBo extends BaseEntity {

    @NotNull(message = "不能为空", groups = {EditGroup.class})
    private Long id;

    /**
     * 单品实例ID
     */
    @NotNull(message = "单品实例不能为空", groups = {AddGroup.class})
    private Long itemInstanceId;

    /**
     * 借还状态
     */
    private String borrowStatus;

    /**
     * 借用人
     */
    @NotBlank(message = "借用人不能为空", groups = {AddGroup.class})
    private String borrower;

    /**
     * 借用时间
     */
    private LocalDateTime borrowTime;

    /**
     * 归还时间
     */
    private LocalDateTime returnTime;

    /**
     * 借用备注
     */
    private String borrowRemark;

    /**
     * 归还备注
     */
    private String returnRemark;
}
