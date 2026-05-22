package com.ruoyi.wms.domain.bo;

import com.ruoyi.wms.domain.entity.MovementOrder;
import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import com.ruoyi.common.mybatis.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import io.github.linpeilie.annotations.AutoMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 调拨单业务对象 wms_movement_order
 *
 * @author zcc
 * @date 2024-08-09
 */

@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = MovementOrder.class, reverseConvertGenerate = false)
public class MovementOrderBo extends BaseEntity {

    /**
     *
     */
    @NotNull(message = "不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 调拨单号
     */
    @NotBlank(message = "调拨单号不能为空", groups = { EditGroup.class })
    private String movementOrderNo;

    /**
     * 调拨类型
     */
    private String movementType;

    /**
     * 调拨依据
     */
    private String dispatchBasis;

    /**
     * 调拨目的
     */
    private String dispatchPurpose;

    /**
     * 调拨方式
     */
    private String dispatchMode;

    /**
     * 发货单位
     */
    private String fromUnit;

    /**
     * 收货单位
     */
    private String toUnit;

    /**
     * 发站
     */
    private String fromStation;

    /**
     * 到站
     */
    private String toStation;

    /**
     * 发货地址
     */
    private String fromAddress;

    /**
     * 收货地址
     */
    private String toAddress;

    /**
     * 通信地址
     */
    private String contactAddress;

    /**
     * 调拨日期
     */
    private LocalDate dispatchDate;

    /**
     * 有效日期
     */
    private LocalDate effectiveDate;

    /**
     * 发出日期
     */
    private LocalDate issueDate;

    /**
     * 源仓库
     */
    @NotNull(message = "源仓库不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long sourceWarehouseId;

    /**
     * 源库区
     */
    private Long sourceAreaId;

    /**
     * 目标仓库
     */
    @NotNull(message = "目标仓库不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long targetWarehouseId;

    /**
     * 目标库区
     */
    private Long targetAreaId;

    /**
     * 状态
     */
    private Integer movementOrderStatus;

    /**
     * 总数量
     */
    private BigDecimal totalQuantity;

    /**
     * 备注
     */
    private String remark;

    List<MovementOrderDetailBo> details;

}
