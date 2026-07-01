package com.ruoyi.wms.domain.bo;

import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import com.ruoyi.common.mybatis.core.domain.BaseEntity;
import java.time.LocalDate;
import com.ruoyi.wms.domain.entity.ItemInstance;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = ItemInstance.class, reverseConvertGenerate = false)
public class ItemInstanceBo extends BaseEntity {

    @NotNull(message = "不能为空", groups = {EditGroup.class})
    private Long id;

    /**
     * 器材实例编码
     */
    private String instanceCode;

    /**
     * 物品ID
     */
    private Long itemId;

    /**
     * 器材分类
     */
    private String itemCategory;

    /**
     * 规格ID
     */
    private Long skuId;

    /**
     * 单品状态
     */
    private String instanceStatus;

    /**
     * 所属仓库
     */
    private Long warehouseId;

    /**
     * 所属库区
     */
    private Long areaId;

    /**
     * 所属货架
     */
    private Long rackId;

    /**
     * 所属货位
     */
    private Long locationId;

    /**
     * 来源类型
     */
    private String sourceType;

    /**
     * 当前所在箱体ID
     */
    private Long boxId;

    /**
     * 箱码
     */
    private String boxCode;

    /**
     * 质量等级
     */
    private String qualityGrade;

    /**
     * 质保期
     */
    private LocalDate warrantyPeriod;

    /**
     * 来源入库单明细ID
     */
    private Long receiptOrderDetailId;

    /**
     * 来源出库单明细ID
     */
    private Long shipmentOrderDetailId;

    /**
     * 来源调拨单明细ID（调拨暂存占用）
     */
    private Long movementOrderDetailId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否仅查询未入库实例
     */
    private Boolean unreceivedOnly;

    /**
     * 是否仅查询未出库占用实例（排除暂存出库单占用）
     */
    private Boolean unshippedOnly;

    // ========== 关联表查询条件（非本表字段，仅用于查询过滤） ==========

    /**
     * 器材名称（模糊查询，关联 wms_item.item_name）
     */
    private String itemName;

    /**
     * 器材编码（模糊查询，关联 wms_item.item_code）
     */
    private String itemCode;

    /**
     * 规格名称（模糊查询，关联 wms_item_sku.sku_name）
     */
    private String skuName;

    /**
     * 用于更新状态
     */
    @NotBlank(message = "单品状态不能为空", groups = {AddGroup.class})
    private String targetStatus;
}
