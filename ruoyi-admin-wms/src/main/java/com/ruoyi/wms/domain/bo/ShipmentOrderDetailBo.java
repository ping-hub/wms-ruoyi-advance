package com.ruoyi.wms.domain.bo;

import com.ruoyi.common.mybatis.core.domain.PlaceAndItem;
import com.ruoyi.wms.domain.entity.ShipmentOrderDetail;
import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import com.ruoyi.common.mybatis.core.domain.BaseEntity;
import io.github.linpeilie.annotations.AutoMappers;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import io.github.linpeilie.annotations.AutoMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 出库单详情业务对象 wms_shipment_order_detail
 *
 * @author ping
 * @date 2024-08-01
 */

@Data
@EqualsAndHashCode(callSuper = true)
@AutoMappers({
    @AutoMapper(target = ShipmentOrderDetail.class, reverseConvertGenerate = false),
    @AutoMapper(target = InventoryBo.class, reverseConvertGenerate = false)
})
public class ShipmentOrderDetailBo extends BaseEntity implements PlaceAndItem {

    /**
     *
     */
    @NotNull(message = "不能为空", groups = { EditGroup.class })
    private Long id;
    /**
     * 器材实例编码
     */
    private String instanceCode;

    /**
     * 出库单
     */
    @NotNull(message = "出库单不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long shipmentOrderId;

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
     * 总价
     */
    private BigDecimal lineAmount;

    /**
     * 所属仓库
     */
    @NotNull(message = "所属仓库不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long warehouseId;

    /**
     * 所属库区
     */
    @NotNull(message = "所属库区不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long areaId;

    /**
     * 入库记录id
     */
    @NotNull(message = "入库记录id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long inventoryDetailId;

    /**
     * 单品实例ID
     */

    /**
     * 箱体ID
     */
    private Long boxId;


    /**
     * 备注
     */
    private String remark;

    /**
     * 出库单号（用于跨表筛选）
     */
    private String shipmentOrderNo;

    /**
     * 创建时间范围起始
     */
    private String startTime;

    /**
     * 创建时间范围结束
     */
    private String endTime;
}
