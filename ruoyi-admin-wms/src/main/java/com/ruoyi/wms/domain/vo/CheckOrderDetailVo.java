package com.ruoyi.wms.domain.vo;

import java.math.BigDecimal;
import com.ruoyi.wms.domain.entity.CheckOrderDetail;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.common.excel.annotation.ExcelDictFormat;
import com.ruoyi.common.excel.convert.ExcelDictConvert;
import lombok.Data;
import io.github.linpeilie.annotations.AutoMapper;

import java.io.Serializable;
import java.io.Serial;
import java.time.LocalDateTime;

/**
 * 库存盘点单据详情视图对象 wms_check_order_detail
 *
 * @author zcc
 * @date 2024-08-13
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = CheckOrderDetail.class)
public class CheckOrderDetailVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Long id;

    /**
     * 盘点单id
     */
    @ExcelProperty(value = "盘点单id")
    private Long checkOrderId;

    /**
     * 规格id
     */
    @ExcelProperty(value = "规格id")
    private Long skuId;

    /**
     * 库存数量
     */
    @ExcelProperty(value = "库存数量")
    private BigDecimal quantity;

    /**
     * 盘点数量
     */
    @ExcelProperty(value = "盘点数量")
    private BigDecimal checkQuantity;

    /**
     * 差异数量
     */
    @ExcelProperty(value = "差异数量")
    private BigDecimal differenceQuantity;

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
     * 货架
     */
    @ExcelProperty(value = "货架")
    private Long rackId;

    /**
     * 货位
     */
    @ExcelProperty(value = "货位")
    private Long locationId;

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
     * 过期日期
     */
    @ExcelProperty(value = "过期日期")
    private LocalDateTime expirationDate;

    /**
     * 入库时间
     */
    @ExcelProperty(value = "入库时间")
    private LocalDateTime receiptTime;

    /**
     * 入库记录id
     */
    @ExcelProperty(value = "入库记录id")
    private Long inventoryDetailId;

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
     * 物品明细ID
     */
    @ExcelProperty(value = "物品明细ID")
    private Long itemInstanceId;

    /**
     * 单品码
     */
    @ExcelProperty(value = "单品码")
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
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

    private ItemSkuVo itemSku;

    private BigDecimal remainQuantity;

}
