package com.ruoyi.wms.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class LocationSummaryVo implements Serializable {

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

    private String locationStatus;

    private String locationType;

    private Integer rowNo;

    private Integer columnNo;

    private BigDecimal length;

    private BigDecimal width;

    private BigDecimal height;

    private BigDecimal maxWeight;

    private Integer occupiedFlag;

    private Integer boxCount;

    private Integer itemInstanceCount;

    private Integer directItemCount;

    private List<BoxVo> boxes = new ArrayList<>();

    private List<ItemInstanceVo> itemInstances = new ArrayList<>();

    private List<LocationItemSummaryVo> itemSummaries = new ArrayList<>();
}
