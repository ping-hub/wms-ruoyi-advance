package com.ruoyi.wms.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.mybatis.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.time.LocalDate;

/**
 * 商品序列号实体
 *
 * @author ruoyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wms_item_sn")
public class ItemSn extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * SN码/序列号
     */
    private String snCode;

    /**
     * SKU ID
     */
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

}
