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
 * 库存调整明细对象 wms_adjust_order_detail
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wms_adjust_order_detail")
public class AdjustOrderDetail extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id")
    private Long id;

    private Long adjustOrderId;

    private Integer lineNo;

    private Long skuId;

    private String equipmentCode;

    private String specModel;

    private String productMark;

    private String qualityGrade;

    private Long warehouseId;

    private Long areaId;

    private Long rackId;

    private Long locationId;

    private Long itemInstanceId;

    private Long boxId;

    private Long inventoryDetailId;

    private BigDecimal beforeQuantity;

    private BigDecimal afterQuantity;

    private BigDecimal differenceQuantity;

    private String batchNo;

    private LocalDateTime productionDate;

    private LocalDateTime expirationDate;

    private String remark;
}
