package com.ruoyi.wms.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.common.mybatis.core.domain.PlaceAndItem;
import com.ruoyi.wms.domain.entity.ShipmentOrderDetail;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 出库单详情视图对象 wms_shipment_order_detail
 *
 * @author ping
 * @date 2024-08-01
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = ShipmentOrderDetail.class)
public class ShipmentOrderDetailVo implements Serializable, PlaceAndItem {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Long id;

    /**
     * 出库单
     */
    @ExcelProperty(value = "出库单")
    private Long shipmentOrderId;

    /**
     * 规格id
     */
    @ExcelProperty(value = "规格id")
    private Long skuId;

    /**
     * 数量
     */
    @ExcelProperty(value = "数量")
    private BigDecimal quantity;

    /**
     * 器材编码
     */
    @ExcelProperty(value = "器材编码")
    private String itemCode;

    /**
     * 器材名称
     */
    @ExcelProperty(value = "器材名称")
    private String itemName;

    /**
     * 规格型号
     */
    @ExcelProperty(value = "规格型号")
    private String skuName;

    /**
     * 计量单位
     */
    @ExcelProperty(value = "计量单位")
    private String unit;

    /**
     * 产品标识
     */
    @ExcelProperty(value = "产品标识")
    private String productIdentifier;

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
     * 总价
     */
    @ExcelProperty(value = "总价")
    private BigDecimal lineAmount;

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
     * 入库记录id
     */
    @ExcelProperty(value = "入库记录id")
    private Long inventoryDetailId;

    /**
     * 器材实例编码
     */
    @ExcelProperty(value = "器材实例编码")
    private String instanceCode;

    /**
     * 箱体ID
     */
    @ExcelProperty(value = "箱体ID")
    private Long boxId;

    /**
     * 箱码
     */
    @ExcelProperty(value = "箱码")
    private String boxCode;


    /**
     * 货架ID
     */
    private Long rackId;

    /**
     * 货架名称
     */
    private String rackName;

    /**
     * 货位ID
     */
    private Long locationId;

    /**
     * 货位名称
     */
    private String locationName;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

    private ItemSkuVo itemSku;

    private BigDecimal remainQuantity;

    /**
     * 关联出库单（仅列表查询时填充）
     */
    private ShipmentOrderVo shipmentOrder;
}
