package com.ruoyi.wms.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 箱体流转日志 wms_box_circulation_log
 */
@Data
@TableName("wms_box_circulation_log")
public class BoxCirculationLog implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id")
    private Long id;

    /** 箱体ID */
    private Long boxId;

    /** 事件发生时箱码快照 */
    private String boxCode;

    /** 事件类型：OUTBOUND / RETURN / RELOCATE */
    private String eventType;

    /** 关联单据ID */
    private Long relatedOrderId;

    /** 关联单据类型：SHIPMENT / BORROW_ORDER */
    private String relatedOrderType;

    /** 移位前位置描述 */
    private String fromLocationName;

    /** 移位后位置描述 */
    private String toLocationName;

    /** 备注 */
    private String remark;

    /** 操作人 */
    private String createBy;

    /** 操作时间 */
    private LocalDateTime createTime;
}
