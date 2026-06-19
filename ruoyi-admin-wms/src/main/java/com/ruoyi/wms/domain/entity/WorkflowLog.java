package com.ruoyi.wms.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 流程操作日志对象 wms_workflow_log
 */
@Data
@TableName("wms_workflow_log")
public class WorkflowLog implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id")
    private Long id;

    /** 业务类型 */
    private String entityType;

    /** 单据ID */
    private Long entityId;

    /** 步骤标识 */
    private String stepName;

    /** 步骤显示名 */
    private String stepLabel;

    /** 操作人ID */
    private Long operatorId;

    /** 操作人姓名 */
    private String operatorName;

    /** 操作时间 */
    private LocalDateTime operateTime;

    /** 审批意见 */
    private String remark;

    /** 操作结果 */
    private String result;
}
