package com.ruoyi.wms.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 流程操作日志视图对象
 */
@Data
public class WorkflowLogVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String entityType;
    private Long entityId;
    private String stepName;
    private String stepLabel;
    private Long operatorId;
    private String operatorName;
    private LocalDateTime operateTime;
    private String remark;
    private String result;
}
