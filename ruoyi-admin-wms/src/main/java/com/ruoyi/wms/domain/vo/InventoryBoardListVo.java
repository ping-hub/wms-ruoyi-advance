package com.ruoyi.wms.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 库存统计看板列表
 *
 * @author SOLO
 */
@Data
public class InventoryBoardListVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String warehouseName;

    private String areaName;

    private String itemName;

    private String skuName;

    private String itemCode;

    private String productIdentifier;

    private String qualityGrade;

    private BigDecimal quantity;

    private Date updateTime;
}
