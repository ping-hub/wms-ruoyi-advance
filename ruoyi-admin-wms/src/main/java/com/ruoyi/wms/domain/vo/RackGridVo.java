package com.ruoyi.wms.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class RackGridVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long rackId;

    private String rackCode;

    private String rackName;

    private Long warehouseId;

    private String warehouseName;

    private Long areaId;

    private String areaName;

    private Integer rowCount;

    private Integer columnCount;

    private List<RackGridCellVo> cells = new ArrayList<>();
}
