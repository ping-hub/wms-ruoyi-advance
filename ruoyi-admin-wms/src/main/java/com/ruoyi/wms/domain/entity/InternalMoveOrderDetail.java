package com.ruoyi.wms.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.mybatis.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 库内移库单明细对象 wms_internal_move_order_detail
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wms_internal_move_order_detail")
public class InternalMoveOrderDetail extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id")
    private Long id;

    private Long internalMoveOrderId;

    private Long skuId;

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

    private Long sourceWarehouseId;

    private Long sourceAreaId;

    private Long sourceRackId;

    private Long sourceLocationId;

    private Long targetWarehouseId;

    private Long targetAreaId;

    private Long targetRackId;

    private Long targetLocationId;

    private Long inventoryDetailId;

    private Long itemInstanceId;

    private Long boxId;
}
