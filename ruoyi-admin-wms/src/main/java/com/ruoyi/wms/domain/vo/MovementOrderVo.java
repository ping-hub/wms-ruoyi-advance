package com.ruoyi.wms.domain.vo;

import java.math.BigDecimal;

import com.ruoyi.common.mybatis.core.domain.BaseVo;
import com.ruoyi.wms.domain.entity.MovementOrder;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.common.excel.annotation.ExcelDictFormat;
import com.ruoyi.common.excel.convert.ExcelDictConvert;
import lombok.Data;
import io.github.linpeilie.annotations.AutoMapper;

import java.io.Serial;
import java.time.LocalDate;
import java.util.List;

/**
 * 调拨单视图对象 wms_movement_order
 *
 * @author ping
 * @date 2024-08-09
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = MovementOrder.class)
public class MovementOrderVo extends BaseVo{

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Long id;

    /**
     * 编号
     */
    @ExcelProperty(value = "编号")
    private String movementOrderNo;

    /**
     * 调拨类型
     */
    @ExcelProperty(value = "调拨类型")
    private String movementType;

    /**
     * 调拨依据
     */
    @ExcelProperty(value = "调拨依据")
    private String dispatchBasis;

    /**
     * 调拨目的
     */
    @ExcelProperty(value = "调拨目的")
    private String dispatchPurpose;

    /**
     * 调拨方式
     */
    @ExcelProperty(value = "调拨方式")
    private String dispatchMode;

    /**
     * 发货单位
     */
    @ExcelProperty(value = "发货单位")
    private String fromUnit;

    /**
     * 收货单位
     */
    @ExcelProperty(value = "收货单位")
    private String toUnit;

    /**
     * 发站
     */
    @ExcelProperty(value = "发站")
    private String fromStation;

    /**
     * 到站
     */
    @ExcelProperty(value = "到站")
    private String toStation;

    /**
     * 发货地址
     */
    @ExcelProperty(value = "发货地址")
    private String fromAddress;

    /**
     * 收货地址
     */
    @ExcelProperty(value = "收货地址")
    private String toAddress;

    /**
     * 通信地址
     */
    @ExcelProperty(value = "通信地址")
    private String contactAddress;

    /**
     * 调拨日期
     */
    @ExcelProperty(value = "调拨日期")
    private LocalDate dispatchDate;

    /**
     * 有效日期
     */
    @ExcelProperty(value = "有效日期")
    private LocalDate effectiveDate;

    /**
     * 发出日期
     */
    @ExcelProperty(value = "发出日期")
    private LocalDate issueDate;

    /**
     * 源仓库
     */
    @ExcelProperty(value = "源仓库")
    private Long sourceWarehouseId;

    /**
     * 源库区
     */
    @ExcelProperty(value = "源库区")
    private Long sourceAreaId;

    /**
     * 目标仓库
     */
    @ExcelProperty(value = "目标仓库")
    private Long targetWarehouseId;

    /**
     * 目标库区
     */
    @ExcelProperty(value = "目标库区")
    private Long targetAreaId;

    /**
     * 状态
     */
    @ExcelProperty(value = "状态")
    private Integer movementOrderStatus;

    /**
     * 总数量
     */
    @ExcelProperty(value = "总数量")
    private BigDecimal totalQuantity;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

    List<MovementOrderDetailVo> details;
}
