package com.ruoyi.wms.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.mybatis.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * 单品实例对象 wms_item_instance
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wms_item_instance")
public class ItemInstance extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id")
    private Long id;

    /**
     * 单品码
     */
    private String instanceCode;

    /**
     * 物品ID
     */
    private Long itemId;

    /**
     * 规格ID
     */
    private Long skuId;

    /**
     * 单品状态
     */
    private String instanceStatus;

    /**
     * 是否在箱内
     */
    private Integer inBox;

    /**
     * 是否已借出
     */
    private Integer borrowed;

    /**
     * 所属仓库
     */
    private Long warehouseId;

    /**
     * 所属库区
     */
    private Long areaId;

    /**
     * 所属货架
     */
    private Long rackId;

    /**
     * 所属货位
     */
    private Long locationId;

    /**
     * 来源类型
     */
    private String sourceType;

    /**
     * 来源单据类型
     */
    private String sourceOrderType;

    /**
     * 来源单据ID
     */
    private Long sourceOrderId;

    /**
     * 来源单据号
     */
    private String sourceOrderNo;

    /**
     * 来源入库单明细ID
     */
    private Long receiptOrderDetailId;

    /**
     * 产品标识
     */
    private String productMark;

    /**
     * 质量等级
     */
    private String qualityGrade;

    /**
     * 所在单位
     */
    private String belongUnit;

    /**
     * 批号
     */
    private String batchNo;

    /**
     * 生产日期
     */
    private LocalDateTime productionDate;

    /**
     * 过期日期
     */
    private LocalDateTime expirationDate;

    /**
     * 备注
     */
    private String remark;
}
