package com.ruoyi.wms.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 货位重建结果
 */
@Data
public class LocationRebuildResultVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long rackId;

    private String rackCode;

    private String rackName;

    /**
     * 应有货位数
     */
    private Integer expectedLocationCount;

    /**
     * 已存在的有效货位数
     */
    private Integer existingLocationCount;

    /**
     * 新生成数
     */
    private Integer createdLocationCount;

    /**
     * 被阻止处理数
     */
    private Integer blockedLocationCount;

    /**
     * 异常/警告信息
     */
    private List<String> messages = new ArrayList<>();
}
