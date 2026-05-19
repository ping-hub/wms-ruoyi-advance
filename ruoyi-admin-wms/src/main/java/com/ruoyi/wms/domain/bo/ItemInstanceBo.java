package com.ruoyi.wms.domain.bo;

import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import com.ruoyi.common.mybatis.core.domain.BaseEntity;
import com.ruoyi.wms.domain.entity.ItemInstance;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

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
     * 来源单据类型
     */
    private String sourceOrderType;

    /**
     * 当前所在箱体ID
     */
    private Long boxId;

    /**
     * 箱码
     */
    private String boxCode;

    /**
     * 来源单据ID
     */
    private Long sourceOrderId;

    /**
     * 来源单据号
     */
    private String sourceOrderNo;

    /**
     * 来源入库单明细ID
     */
    private Long receiptOrderDetailId;

    /**
     * 来源出库单明细ID
     */
    private Long shipmentOrderDetailId;

    /**
     * 最后一次业务动作
     */
    private String lastOperationType;

    /**
     * 最后动作时间
     */
    private LocalDateTime lastOperationTime;

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

    /**
     * 用于更新状态
     */
    @NotBlank(message = "单品状态不能为空", groups = {AddGroup.class})
    private String targetStatus;
}
