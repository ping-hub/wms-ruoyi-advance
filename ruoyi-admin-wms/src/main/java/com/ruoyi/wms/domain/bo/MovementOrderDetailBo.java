package com.ruoyi.wms.domain.bo;

import com.ruoyi.common.mybatis.core.domain.PlaceAndItem;
import com.ruoyi.wms.domain.entity.MovementOrderDetail;
import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import com.ruoyi.common.mybatis.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import io.github.linpeilie.annotations.AutoMapper;

import java.math.BigDecimal;

/**
 * 调拨单明细业务对象 wms_movement_order_detail
 *
 * @author ping
 * @date 2024-08-09
 */

@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = MovementOrderDetail.class, reverseConvertGenerate = false)
public class MovementOrderDetailBo extends BaseEntity implements PlaceAndItem {

    /**
     *
     */
    @NotNull(message = "不能为空", groups = { EditGroup.class })
    private Long id;
    /**
     * 器材识别码
     */
    private String instanceCode;

    /**
     * 调拨单Id
     */
    @NotNull(message = "调拨单Id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long movementOrderId;

    /**
     * 规格id
     */
    @NotNull(message = "规格id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long skuId;

    /**
     * 数量
     */
    @NotNull(message = "数量不能为空", groups = { AddGroup.class, EditGroup.class })
    private BigDecimal quantity;

    /**
     * 器材编码
     */
    private String itemCode;

    /**
     * 器材名称
     */
    private String itemName;

    /**
     * 规格型号
     */
    private String skuName;

    /**
     * 计量单位
     */
    private String unit;

    /**
     * 产品标识
     */
    private String productIdentifier;

    /**
     * 质量等级
     */
    private String qualityGrade;

    /**
     * 单价
     */
    private BigDecimal unitPrice;

    /**
     * 行金额
     */
    private BigDecimal lineAmount;

    /**
     * 备注
     */
    private String remark;

    /**
     * 源仓库
     */
    @NotNull(message = "源仓库不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long sourceWarehouseId;

    /**
     * 源库区
     */
    @NotNull(message = "源库区不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long sourceAreaId;

    /**
     * 源货架
     */
    private Long sourceRackId;

    /**
     * 源货位
     */
    private Long sourceLocationId;

    /**
     * 目标仓库
     */
    @NotNull(message = "目标仓库不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long targetWarehouseId;

    /**
     * 目标库区
     */
    @NotNull(message = "目标库区不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long targetAreaId;

    /**
     * 目标货架
     */
    private Long targetRackId;

    /**
     * 目标货位
     */
    private Long targetLocationId;

    /**
     * 入库记录id
     */
    @NotNull(message = "入库记录id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long inventoryDetailId;

    /**
     * 器材实例ID
     */

    @Override
    public Long getWarehouseId() {
        return this.getSourceWarehouseId();
    }

    @Override
    public Long getAreaId() {
        return this.getSourceAreaId();
    }
}
