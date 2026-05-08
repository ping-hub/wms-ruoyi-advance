package com.ruoyi.wms.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.mybatis.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 借还记录对象 wms_borrow_record
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wms_borrow_record")
public class BorrowRecord extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id")
    private Long id;

    /**
     * 单品实例ID
     */
    private Long itemInstanceId;

    /**
     * 借还状态
     */
    private String borrowStatus;

    /**
     * 借用人
     */
    private String borrower;

    /**
     * 发货单位
     */
    private String fromUnit;

    /**
     * 收货单位
     */
    private String toUnit;

    /**
     * 发货人
     */
    private String fromPerson;

    /**
     * 收货人
     */
    private String toPerson;

    /**
     * 单据日期
     */
    private LocalDate docDate;

    /**
     * 产品标识
     */
    private String productMark;

    /**
     * 质量等级
     */
    private String qualityGrade;

    /**
     * 借用单号
     */
    private String borrowNo;

    /**
     * 计划归还日期
     */
    private LocalDate planReturnDate;

    /**
     * 是否逾期
     */
    private Integer overdueFlag;

    /**
     * 逾期天数
     */
    private Integer overdueDays;

    /**
     * 物品码快照
     */
    private String instanceCode;

    /**
     * 借用时间
     */
    private LocalDateTime borrowTime;

    /**
     * 归还时间
     */
    private LocalDateTime returnTime;

    /**
     * 借用备注
     */
    private String borrowRemark;

    /**
     * 归还备注
     */
    private String returnRemark;

    /**
     * 借出前仓库
     */
    private Long originalWarehouseId;

    /**
     * 借出前库区
     */
    private Long originalAreaId;

    /**
     * 借出前货架
     */
    private Long originalRackId;

    /**
     * 借出前货位
     */
    private Long originalLocationId;

    /**
     * 归还后仓库
     */
    private Long returnedWarehouseId;

    /**
     * 归还后库区
     */
    private Long returnedAreaId;

    /**
     * 归还后货架
     */
    private Long returnedRackId;

    /**
     * 归还后货位
     */
    private Long returnedLocationId;
}
