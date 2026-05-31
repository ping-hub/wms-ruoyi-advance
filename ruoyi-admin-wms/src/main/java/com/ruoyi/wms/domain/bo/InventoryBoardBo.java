package com.ruoyi.wms.domain.bo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 库存统计看板查询对象
 *
 * @author SOLO
 */
@Data
public class InventoryBoardBo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 仓库ID
     */
    private Long warehouseId;

    /**
     * 器材名称
     */
    private String itemName;

    /**
     * 器材类型
     */
    private String equipmentType;

    /**
     * 规格型号
     */
    private String skuName;
}
