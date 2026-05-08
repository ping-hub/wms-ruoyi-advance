package com.ruoyi.wms.domain.bo;

import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import com.ruoyi.common.mybatis.core.domain.BaseEntity;
import com.ruoyi.wms.domain.entity.InternalMoveOrderDetail;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 库内移库单明细业务对象 wms_internal_move_order_detail
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = InternalMoveOrderDetail.class, reverseConvertGenerate = false)
public class InternalMoveOrderDetailBo extends BaseEntity {

    @NotNull(message = "不能为空", groups = { EditGroup.class })
    private Long id;

    @NotNull(message = "库内移库单Id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long internalMoveOrderId;

    @NotNull(message = "规格id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long skuId;

    @NotNull(message = "数量不能为空", groups = { AddGroup.class, EditGroup.class })
    private BigDecimal quantity;

    private String equipmentCode;

    private String specModel;

    private String productMark;

    private String qualityGrade;

    private BigDecimal unitPrice;

    private BigDecimal lineAmount;

    private String remark;

    private String batchNo;

    private LocalDateTime productionDate;

    private LocalDateTime expirationDate;

    @NotNull(message = "源仓库不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long sourceWarehouseId;

    @NotNull(message = "源库区不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long sourceAreaId;

    private Long sourceRackId;

    private Long sourceLocationId;

    @NotNull(message = "目标仓库不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long targetWarehouseId;

    @NotNull(message = "目标库区不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long targetAreaId;

    private Long targetRackId;

    private Long targetLocationId;

    @NotNull(message = "库存明细ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long inventoryDetailId;

    private Long itemInstanceId;

    private Long boxId;
}
