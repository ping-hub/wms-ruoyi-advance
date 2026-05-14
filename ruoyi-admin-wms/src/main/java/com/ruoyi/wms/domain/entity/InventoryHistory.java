package com.ruoyi.wms.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.mybatis.core.domain.BaseHistoryEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 库存记录对象 wms_inventory_history
 *
 * @author zcc
 * @date 2024-07-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wms_inventory_history")
public class InventoryHistory extends BaseHistoryEntity {

    @Serial
    private static final long serialVersionUID=1L;

    /**
     *
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 操作id（出库、入库、库存移动表单id）
     */
    private Long orderId;
    /**
     * 操作单号（入库、出库、移库、盘库单号）
     */
    private String orderNo;
    /**
     * 操作类型
     */
    private Integer orderType;
    /**
     * 物料ID
     */
    private Long skuId;
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
     * 产品标识
     */
    private String productMark;

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
     * 所在单位
     */
    private String belongUnit;

    /**
     * 库存变化
     */
    private BigDecimal quantity;
    /**
     * 备注
     */
    private String remark;
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
     * 变化前数量
     */
    private BigDecimal beforeQuantity;

    /**
     * 变化后数量
     */
    private BigDecimal afterQuantity;

    /**
     * 业务动作类型
     */
    private String operationType;

    /**
     * 操作人
     */
    private String operatorName;

}
