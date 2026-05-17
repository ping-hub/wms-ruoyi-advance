package com.ruoyi.wms.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.mybatis.core.domain.BaseEntity;
import com.ruoyi.common.mybatis.core.domain.PlaceAndItem;
import com.ruoyi.wms.domain.bo.InventoryDetailBo;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 库存详情对象 wms_inventory_detail
 *
 * @author zcc
 * @date 2024-07-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wms_inventory_detail")
@AutoMapper(target = InventoryDetailBo.class, reverseConvertGenerate = false)
public class InventoryDetail extends BaseEntity implements PlaceAndItem {

    @Serial
    private static final long serialVersionUID=1L;

    /**
     *
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 入库单id
     */
    private Long receiptOrderId;
    /**
     * 入库单类型
     */
    private String receiptOrderType;
    /**
     * 单号
     */
    private String orderNo;
    /**
     * 类型 1：入库 2：移库 3：盘库
     */
    private Integer type;
    /**
     * sku的ID
     */
    private Long skuId;
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
     * 物品明细ID
     */
    private Long itemInstanceId;

    /**
     * 箱体ID
     */
    private Long boxId;

    /**
     * 来源单据类型
     */
    private String sourceOrderType;

    /**
     * 来源单据ID
     */
    private Long sourceOrderId;

    /**
     * 行号
     */
    private Integer lineNo;
    /**
     * 入库数量
     */
    private BigDecimal quantity;
    /**
     * 生产日期
     */
    private LocalDateTime productionDate;
    /**
     * 过期时间
     */
    private LocalDateTime expirationDate;
    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 器材编码
     */
    private String equipmentCode;

    /**
     * 规格型号
     */
    private String specModel;

    /**
     * 单价
     */
    private BigDecimal unitPrice;

    /**
     * 行金额
     */
    private BigDecimal lineAmount;

    /**
     * 所在单位
     */
    private String belongUnit;

    /**
     * 备注
     */
    private String remark;
    /**
     * 剩余数量
     */
    private BigDecimal remainQuantity;

}
