package com.ruoyi.wms.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class LocationItemSummaryVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long itemId;

    private String itemName;

    private Long skuId;

    private String skuName;

    private Integer quantity;
}
