package com.ruoyi.wms.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.wms.domain.entity.ItemSn;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * 商品序列号视图对象
 *
 * @author ruoyi
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = ItemSn.class)
public class ItemSnVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ExcelProperty(value = "主键ID")
    private Long id;

    /**
     * SN码/序列号
     */
    @ExcelProperty(value = "SN码")
    private String snCode;

    /**
     * SKU ID
     */
    @ExcelProperty(value = "SKU ID")
    private Long skuId;

    /**
     * 商品ID
     */
    @ExcelProperty(value = "商品ID")
    private Long itemId;

    /**
     * 当前仓库ID
     */
    @ExcelProperty(value = "仓库ID")
    private Long warehouseId;

    /**
     * 当前库区ID
     */
    @ExcelProperty(value = "库区ID")
    private Long areaId;

    /**
     * 关联的库存明细ID
     */
    @ExcelProperty(value = "库存明细ID")
    private Long inventoryDetailId;

    /**
     * 状态: 0-在库 1-已出库 2-损坏 3-冻结
     */
    @ExcelProperty(value = "状态")
    private Integer status;

    /**
     * 批号
     */
    @ExcelProperty(value = "批号")
    private String batchNo;

    /**
     * 生产日期
     */
    @ExcelProperty(value = "生产日期")
    private LocalDate productionDate;

    /**
     * 过期日期
     */
    @ExcelProperty(value = "过期日期")
    private LocalDate expirationDate;

    /**
     * 来源入库单ID
     */
    @ExcelProperty(value = "入库单ID")
    private Long receiptOrderId;

    /**
     * 来源入库明细ID
     */
    @ExcelProperty(value = "入库明细ID")
    private Long receiptOrderDetailId;

    /**
     * 出库去向单ID
     */
    @ExcelProperty(value = "出库单ID")
    private Long shipmentOrderId;

    /**
     * 出库明细ID
     */
    @ExcelProperty(value = "出库明细ID")
    private Long shipmentOrderDetailId;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

    // 扩展字段
    /**
     * SKU名称
     */
    private String skuName;

    /**
     * 商品名称
     */
    private String itemName;

    /**
     * 仓库名称
     */
    private String warehouseName;

    /**
     * 库区名称
     */
    private String areaName;

    /**
     * 状态名称
     */
    private String statusName;

    /**
     * 入库单号
     */
    private String receiptOrderNo;

    /**
     * 出库单号
     */
    private String shipmentOrderNo;

}
