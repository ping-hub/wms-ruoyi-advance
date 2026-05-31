package com.ruoyi.wms.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 库存统计看板摘要
 *
 * @author SOLO
 */
@Data
public class InventoryBoardSummaryVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 当前库存数
     */
    private BigDecimal currentQuantity;

    /**
     * 可用库存总数
     */
    private BigDecimal availableQuantity;

    /**
     * 在库仓库数
     */
    private Long warehouseCount;

    /**
     * 在库库区数
     */
    private Long areaCount;

    /**
     * 异常库存记录数
     */
    private Long abnormalCount;
}
