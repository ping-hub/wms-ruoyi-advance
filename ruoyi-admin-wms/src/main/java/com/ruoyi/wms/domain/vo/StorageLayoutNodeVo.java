package com.ruoyi.wms.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class StorageLayoutNodeVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String nodeType;

    private Long id;

    private Long parentId;

    private String code;

    private String name;

    private String status;

    private Long orderNum;

    private Integer rowNo;

    private Integer columnNo;

    private Integer occupiedFlag;

    private List<StorageLayoutNodeVo> children = new ArrayList<>();
}
