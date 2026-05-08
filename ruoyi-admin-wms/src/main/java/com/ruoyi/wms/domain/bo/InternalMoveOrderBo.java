package com.ruoyi.wms.domain.bo;

import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import com.ruoyi.common.mybatis.core.domain.BaseEntity;
import com.ruoyi.wms.domain.entity.InternalMoveOrder;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

/**
 * 库内移库单业务对象 wms_internal_move_order
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = InternalMoveOrder.class, reverseConvertGenerate = false)
public class InternalMoveOrderBo extends BaseEntity {

    @NotNull(message = "不能为空", groups = { EditGroup.class })
    private Long id;

    @NotBlank(message = "库内移库单号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String internalMoveOrderNo;

    @NotNull(message = "源仓库不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long sourceWarehouseId;

    private Long sourceAreaId;

    private Long sourceRackId;

    private Long sourceLocationId;

    @NotNull(message = "目标仓库不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long targetWarehouseId;

    private Long targetAreaId;

    private Long targetRackId;

    private Long targetLocationId;

    private String moveReason;

    private Integer internalMoveStatus;

    private BigDecimal totalQuantity;

    private String remark;

    private List<InternalMoveOrderDetailBo> details;
}
