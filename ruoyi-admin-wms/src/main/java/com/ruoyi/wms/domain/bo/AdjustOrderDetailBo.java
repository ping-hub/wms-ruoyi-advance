package com.ruoyi.wms.domain.bo;

import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import com.ruoyi.common.mybatis.core.domain.BaseEntity;
import com.ruoyi.common.mybatis.core.domain.PlaceAndItem;
import com.ruoyi.wms.domain.entity.AdjustOrderDetail;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 库存调整明细业务对象 wms_adjust_order_detail
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = AdjustOrderDetail.class, reverseConvertGenerate = false)
public class AdjustOrderDetailBo extends BaseEntity implements PlaceAndItem {

    @NotNull(message = "不能为空", groups = { EditGroup.class })
    private Long id;

    @NotNull(message = "调整单ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long adjustOrderId;

    private Integer lineNo;

    @NotNull(message = "规格ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long skuId;

    private String equipmentCode;

    private String specModel;

    private String productMark;

    private String qualityGrade;

    @NotNull(message = "仓库不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long warehouseId;

    private Long areaId;

    private Long rackId;

    private Long locationId;

    private Long itemInstanceId;

    private Long boxId;

    @NotNull(message = "库存明细ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long inventoryDetailId;

    @NotNull(message = "调整前数量不能为空", groups = { AddGroup.class, EditGroup.class })
    private BigDecimal beforeQuantity;

    @NotNull(message = "调整后数量不能为空", groups = { AddGroup.class, EditGroup.class })
    private BigDecimal afterQuantity;

    private BigDecimal differenceQuantity;

    private LocalDateTime productionDate;

    private LocalDateTime expirationDate;

    private String remark;
}
