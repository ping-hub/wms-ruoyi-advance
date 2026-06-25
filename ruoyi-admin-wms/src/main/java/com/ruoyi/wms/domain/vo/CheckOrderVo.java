package com.ruoyi.wms.domain.vo;

import java.math.BigDecimal;

import com.ruoyi.common.mybatis.core.domain.BaseVo;
import com.ruoyi.wms.domain.entity.CheckOrder;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.common.excel.annotation.ExcelDictFormat;
import com.ruoyi.common.excel.convert.ExcelDictConvert;
import lombok.Data;
import io.github.linpeilie.annotations.AutoMapper;

import java.io.Serializable;
import java.io.Serial;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 库存盘点单据视图对象 wms_check_order
 *
 * @author ping
 * @date 2024-08-13
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = CheckOrder.class)
public class CheckOrderVo extends BaseVo {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Long id;

    /**
     * 盘点单号
     */
    @ExcelProperty(value = "盘点单号")
    private String checkOrderNo;

    /**
     * 盘点单状态：-2已驳回 -1作废 0草稿 1待盘点 2待复核 3已完成
     */
    @ExcelProperty(value = "盘点单状态")
    private Integer checkOrderStatus;

    /**
     * 盈亏数
     */
    @ExcelProperty(value = "盈亏数")
    private BigDecimal checkOrderTotal;

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
     * 盘点范围类型
     */
    @ExcelProperty(value = "盘点范围类型")
    private String checkScopeType;

    /**
     * 盘点日期
     */
    @ExcelProperty(value = "盘点日期")
    private LocalDateTime checkDate;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

    // ========== 流程字段 ==========

    private Long applicantId;
    private String applicantName;
    private LocalDateTime submitTime;
    private Long executorId;
    private String executorName;
    private Long reviewerId;
    private String reviewerName;
    private LocalDateTime executeTime;
    private String approveRemark;

    private List<CheckOrderDetailVo> details;

    private List<CheckOrderInstanceVo> instances;

    /** 流程操作日志 */
    private List<WorkflowLogVo> workflowLogs;
}
