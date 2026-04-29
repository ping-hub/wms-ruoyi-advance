package com.ruoyi.wms.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.wms.domain.entity.OrderSn;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 单据SN关联视图对象
 *
 * @author ruoyi
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = OrderSn.class)
public class OrderSnVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ExcelProperty(value = "主键ID")
    private Long id;

    /**
     * 单据类型: 1-入库 2-出库 3-移库 4-盘点
     */
    @ExcelProperty(value = "单据类型")
    private Integer orderType;

    /**
     * 单据ID
     */
    @ExcelProperty(value = "单据ID")
    private Long orderId;

    /**
     * 单据明细ID
     */
    @ExcelProperty(value = "单据明细ID")
    private Long orderDetailId;

    /**
     * SN ID
     */
    @ExcelProperty(value = "SN ID")
    private Long snId;

    /**
     * SN码
     */
    @ExcelProperty(value = "SN码")
    private String snCode;

}
