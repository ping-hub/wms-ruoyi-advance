package com.ruoyi.wms.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.common.mybatis.core.domain.BaseVo;
import com.ruoyi.wms.domain.entity.InventoryDetail;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 库存详情视图对象 wms_inventory_detail
 *
 * @author zcc
 * @date 2024-07-22
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = InventoryDetail.class)
public class InventoryDetailVo extends BaseVo {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Long id;

    /**
     * 入库单id
     */
    @ExcelProperty(value = "入库单id")
    private Long receiptOrderId;

    /**
     * 入库单类型
     */
    @ExcelProperty(value = "入库单类型")
    private String receiptOrderType;

    /**
     * 单号
     */
    @ExcelProperty(value = "单号")
    private String orderNo;

    /**
     * 类型 1：入库 2：移库 3：盘库
     */
    @ExcelProperty(value = "类型 1：入库 2：移库 3：盘库")
    private Integer type;

    /**
     * sku的ID
     */
    @ExcelProperty(value = "sku的ID")
    private Long skuId;

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
     * 来源单据类型
     */
    @ExcelProperty(value = "来源单据类型")
    private String sourceOrderType;

    /**
     * 来源单据ID
     */
    @ExcelProperty(value = "来源单据ID")
    private Long sourceOrderId;

    /**
     * 行号
     */
    @ExcelProperty(value = "行号")
    private Integer lineNo;

    /**
     * 入库数量
     */
    @ExcelProperty(value = "入库数量")
    private BigDecimal quantity;

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
     * 装备名称
     */
    @ExcelProperty(value = "装备名称")
    private String equipmentName;

    /**
     * 规格型号
     */
    @ExcelProperty(value = "规格型号")
    private String specModel;

    /**
     * 产品标识
     */
    @ExcelProperty(value = "产品标识")
    private String productMark;

    /**
     * 质量等级
     */
    @ExcelProperty(value = "质量等级")
    private String qualityGrade;

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
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

    /**
     * 剩余数量
     */
    @ExcelProperty(value = "剩余数量")
    private BigDecimal remainQuantity;

    private ItemSkuVo itemSku;

    private ItemVo item;


}
