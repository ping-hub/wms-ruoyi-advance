package com.ruoyi.wms.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class LocationStockVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long locationId;
    private String locationCode;
    private String locationName;
    private Long warehouseId;
    private String warehouseName;
    private Long areaId;
    private String areaName;
    private Long rackId;
    private String rackName;
    private Integer directItemCount;
    private Integer boxCount;
    private List<ItemInstanceVo> itemInstances;
    private List<BoxVo> boxes;
}
