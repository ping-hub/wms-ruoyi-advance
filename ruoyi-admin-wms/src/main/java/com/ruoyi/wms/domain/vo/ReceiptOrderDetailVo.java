package com.ruoyi.wms.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import com.ruoyi.wms.domain.entity.ReceiptOrderDetail;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 入库单详情视图对象 wms_receipt_order_detail
 *
 * @author zcc
 * @date 2024-07-19
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = ReceiptOrderDetail.class)
public class ReceiptOrderDetailVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Long id;

    /**
     * 入库单号
     */
    @ExcelProperty(value = "入库单号")
    private Long receiptOrderId;

    /**
     * 规格id
     */
    @ExcelProperty(value = "规格id")
    private Long skuId;

    /**
     * 入库数量
     */
    @ExcelProperty(value = "入库数量")
    private BigDecimal quantity;

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
     * 单价
     */
    @ExcelProperty(value = "单价")
    private BigDecimal unitPrice;

    /**
     * 行金额
     */
    @ExcelProperty(value = "行金额")
    private BigDecimal lineAmount;

    /**
     * 批号
     */
    @ExcelProperty(value = "批号")
    private String batchNo;

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
     * 是否生成单品实例
     */
    @ExcelProperty(value = "生成单品实例")
    private Integer generateItemInstance;

    /**
     * 已生成单品实例数量
     */
    @ExcelProperty(value = "已生成实例数量")
    private Integer generatedInstanceQuantity;

    private ItemSkuVo itemSku;
}
