package com.ruoyi.wms.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 我的待办视图对象
 *
 * @author ping
 * @date 2026-06-17
 */
@Data
public class MyTaskVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 单据类型标识：shipment / receipt / movement / check */
    private String orderType;

    /** 单据类型中文标签 */
    private String orderTypeLabel;

    /** 单据ID */
    private Long orderId;

    /** 单号 */
    private String orderNo;

    /** 待办类型标识：pending_approval / pending_execute / rejected */
    private String taskType;

    /** 待办事项中文标签 */
    private String taskLabel;

    /** 操作按钮文案 */
    private String actionLabel;

    /** 申请人 */
    private String applicantName;

    /** 仓库名称 */
    private String warehouseName;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 办结时间 */
    private LocalDateTime finishTime;

    /** 备注 */
    private String remark;
}
