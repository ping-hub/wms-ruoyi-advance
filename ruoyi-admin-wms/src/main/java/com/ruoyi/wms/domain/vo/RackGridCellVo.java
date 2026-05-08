package com.ruoyi.wms.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class RackGridCellVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer rowNo;

    private Integer columnNo;

    private Long locationId;

    private String locationCode;

    private String locationName;

    private String locationStatus;

    private Integer occupiedFlag;

    private Integer boxCount;

    private Integer directItemCount;

    private Integer itemInstanceCount;
}
