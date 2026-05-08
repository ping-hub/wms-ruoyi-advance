package com.ruoyi.wms.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 货位体检结果
 */
@Data
public class LocationHealthCheckResultVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long rackId;

    private String rackCode;

    private String rackName;

    private Integer expectedLocationCount;

    private Integer actualLocationCount;

    private Integer missingLocationCount;

    private Integer duplicateGridCount;

    private Integer duplicateCodeCount;

    private Integer outOfRangeCount;

    private List<String> messages = new ArrayList<>();
}
