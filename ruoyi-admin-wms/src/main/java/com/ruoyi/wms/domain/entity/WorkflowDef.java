package com.ruoyi.wms.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.mybatis.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 流程定义对象 wms_workflow_def
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wms_workflow_def")
public class WorkflowDef extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id")
    private Long id;

    /** 业务类型（shipment/receipt/movement/check） */
    private String entityType;

    /** 步骤序号 */
    private Integer stepOrder;

    /** 步骤标识 */
    private String stepName;

    /** 步骤显示名 */
    private String stepLabel;

    /** 执行前状态 */
    private Integer fromStatus;

    /** 执行后状态 */
    private Integer toStatus;

    /** 所需权限码 */
    private String permCode;

    /** 是否启用（1启用 0禁用） */
    private Integer enabled;
}
