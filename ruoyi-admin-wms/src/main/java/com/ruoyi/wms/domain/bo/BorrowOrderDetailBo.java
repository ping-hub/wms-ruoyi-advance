package com.ruoyi.wms.domain.bo;

import com.ruoyi.wms.domain.entity.BorrowOrderDetail;
import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import com.ruoyi.common.mybatis.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import io.github.linpeilie.annotations.AutoMapper;

import java.time.LocalDateTime;

/**
 * 器材借用单明细业务对象 wms_borrow_order_detail
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = BorrowOrderDetail.class, reverseConvertGenerate = false)
public class BorrowOrderDetailBo extends BaseEntity {

    @NotNull(message = "id不能为空", groups = { EditGroup.class })
    private Long id;

    /** 借用单ID */
    private Long borrowOrderId;

    /** 器材实例编码 */
    @NotBlank(message = "器材实例编码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String instanceCode;

    /** 规格ID */
    @NotNull(message = "规格ID不能为空", groups = { AddGroup.class, EditGroup.class })
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

    /** 归还状态 */
    private Integer returnStatus;

    /** 归还时间 */
    private LocalDateTime returnTime;

    /** 备注 */
    private String remark;
}
