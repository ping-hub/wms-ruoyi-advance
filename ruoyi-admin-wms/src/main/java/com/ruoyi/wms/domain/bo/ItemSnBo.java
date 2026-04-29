package com.ruoyi.wms.domain.bo;

import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import com.ruoyi.common.mybatis.core.domain.BaseEntity;
import com.ruoyi.wms.domain.entity.ItemSn;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.List;

/**
 * 商品序列号业务对象
 *
 * @author ruoyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = ItemSn.class, reverseConvertGenerate = false)
public class ItemSnBo extends BaseEntity {

    /**
     * 主键ID
     */
    @NotNull(message = "不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 主键集合
     */
    private List<Long> ids;

    /**
     * SN码/序列号
     */
    @NotBlank(message = "SN码不能为空", groups = { AddGroup.class })
    private String snCode;

    /**
     * SKU ID
     */
    @NotNull(message = "SKU ID不能为空", groups = { AddGroup.class })
    private Long skuId;

    /**
     * 商品ID
     */
    private Long itemId;

    /**
     * 当前仓库ID
     */
    private Long warehouseId;

    /**
     * 当前库区ID
     */
    private Long areaId;

    /**
     * 关联的库存明细ID
     */
    private Long inventoryDetailId;

    /**
     * 状态: 0-在库 1-已出库 2-损坏 3-冻结
     */
    private Integer status;

    /**
     * 批号
     */
    private String batchNo;

    /**
     * 生产日期
     */
    private LocalDate productionDate;

    /**
     * 过期日期
     */
    private LocalDate expirationDate;

    /**
     * 来源入库单ID
     */
    private Long receiptOrderId;

    /**
     * 来源入库明细ID
     */
    private Long receiptOrderDetailId;

    /**
     * 出库去向单ID
     */
    private Long shipmentOrderId;

    /**
     * 出库明细ID
     */
    private Long shipmentOrderDetailId;

    /**
     * 备注
     */
    private String remark;

    // 查询条件
    /**
     * SKU ID查询
     */
    private Long querySkuId;

    /**
     * 商品ID查询
     */
    private Long queryItemId;

    /**
     * 仓库ID查询
     */
    private Long queryWarehouseId;

    /**
     * 库区ID查询
     */
    private Long queryAreaId;

    /**
     * 状态查询
     */
    private Integer queryStatus;

    /**
     * SN码模糊查询
     */
    private String querySnCode;

    /**
     * 批号模糊查询
     */
    private String queryBatchNo;

    /**
     * 入库单ID查询
     */
    private Long queryReceiptOrderId;

    /**
     * SN码列表（批量查询）
     */
    private List<String> snCodes;

}
