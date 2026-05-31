package com.ruoyi.wms.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 库存统计看板柱状图数据
 *
 * @author SOLO
 */
@Data
public class InventoryBoardChartVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 维度名称
     */
    private String label;

    /**
     * 数量
     */
    private BigDecimal quantity;
}
