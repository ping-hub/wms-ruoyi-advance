package com.ruoyi.wms.domain.bo;

import com.ruoyi.common.mybatis.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class LedgerBo extends BaseEntity {

    /**
     * 器材编码
     */
    private String equipmentCode;

    /**
     * 器材名称
     */
    private String itemName;

    /**
     * 规格型号
     */
    private String specModel;

    /**
     * 产品标识
     */
    private String productMark;

    /**
     * 装备名称
     */
    private String equipmentName;

    /**
     * 质量等级
     */
    private String qualityGrade;

    /**
     * 所在单位
     */
    private String belongUnit;

    /**
     * 单价
     */
    private BigDecimal unitPrice;

    /**
     * 开始时间
     */
    private LocalDateTime createStartTime;

    /**
     * 结束时间
     */
    private LocalDateTime createEndTime;
}
