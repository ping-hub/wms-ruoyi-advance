package com.ruoyi.wms.domain.bo;

import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import com.ruoyi.common.mybatis.core.domain.BaseEntity;
import com.ruoyi.wms.domain.entity.AdjustOrder;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 库存调整主表业务对象 wms_adjust_order
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = AdjustOrder.class, reverseConvertGenerate = false)
public class AdjustOrderBo extends BaseEntity {

    @NotNull(message = "不能为空", groups = { EditGroup.class })
    private Long id;

    @NotBlank(message = "调整单号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String adjustOrderNo;

    private LocalDateTime adjustDate;

    @NotNull(message = "仓库不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long warehouseId;

    private Long areaId;

    private Long rackId;

    private Long locationId;

    private String adjustReason;

    private Integer adjustStatus;

    private String handlerName;

    private String remark;

    private List<AdjustOrderDetailBo> details;
}
