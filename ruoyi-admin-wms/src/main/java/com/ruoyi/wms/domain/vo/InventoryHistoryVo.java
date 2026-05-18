package com.ruoyi.wms.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.excel.annotation.ExcelDictFormat;
import com.ruoyi.common.excel.convert.ExcelDictConvert;
import com.ruoyi.wms.domain.entity.InventoryHistory;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 库存记录视图对象 wms_inventory_history
 *
 * @author zcc
 * @date 2024-07-22
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = InventoryHistory.class)
public class InventoryHistoryVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Long id;

    /**
     * 操作id（出库、入库、库存移动表单id）
     */
    @ExcelProperty(value = "操作id", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "出=库、入库、库存移动表单id")
    private Long orderId;

    /**
     * 操作单号（入库、出库、移库、盘库单号）
     */
    @ExcelProperty(value = "操作单号")
    private String orderNo;

    /**
     * 操作类型
     */
    @ExcelProperty(value = "操作类型")
    private Integer orderType;

    /**
     * 物料ID
     */
    @ExcelProperty(value = "物料ID")
    private Long skuId;

    /**
     * 生产日期
     */
    @ExcelProperty(value = "生产日期")
    private LocalDateTime productionDate;

    /**
     * 过期时间
     */
    @ExcelProperty(value = "过期时间")
    private LocalDateTime expirationDate;

    /**
     * 金额
     */
    @ExcelProperty(value = "金额")
    private BigDecimal amount;

    /**
     * 器材编码
     */
    @ExcelProperty(value = "器材编码")
    private String equipmentCode;

    /**
     * 器材名称
     */
    @ExcelProperty(value = "器材名称")
    private String itemName;

    /**
     * 规格型号
     */
    @ExcelProperty(value = "规格型号")
    private String specModel;

    /**
     * 单位
     */
    @ExcelProperty(value = "计量单位")
    private String unit;

    /**
     * 单价
     */
    @ExcelProperty(value = "单价")
    private BigDecimal unitPrice;

    /**
     * 行金额
     */
    @ExcelProperty(value = "总价")
    private BigDecimal lineAmount;

    /**
     * 所在单位
     */
    @ExcelProperty(value = "所在单位")
    private String belongUnit;

    /**
     * 库存变化
     */
    @ExcelProperty(value = "库存变化")
    private BigDecimal quantity;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

    /**
     * 所属仓库
     */
    @ExcelProperty(value = "所属仓库")
    private Long warehouseId;

    /**
     * 所属库区
     */
    @ExcelProperty(value = "所属库区")
    private Long areaId;

    /**
     * 所属货架
     */
    @ExcelProperty(value = "所属货架")
    private Long rackId;

    /**
     * 所属货位
     */
    @ExcelProperty(value = "所属货位")
    private Long locationId;

    /**
     * 物品明细ID
     */
    @ExcelProperty(value = "物品明细ID")
    private Long itemInstanceId;

    /**
     * 箱体ID
     */
    @ExcelProperty(value = "箱体ID")
    private Long boxId;

    /**
     * 变化前数量
     */
    @ExcelProperty(value = "变化前数量")
    private BigDecimal beforeQuantity;

    /**
     * 变化后数量
     */
    @ExcelProperty(value = "变化后数量")
    private BigDecimal afterQuantity;

    /**
     * 业务动作类型
     */
    @ExcelProperty(value = "业务动作类型")
    private String operationType;

    /**
     * 操作人
     */
    @ExcelProperty(value = "操作人")
    private String operatorName;

    private ItemSkuVo itemSku;

    private ItemVo item;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}
