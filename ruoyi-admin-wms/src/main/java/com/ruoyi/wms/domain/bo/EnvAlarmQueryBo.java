package com.ruoyi.wms.domain.bo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 温湿度监控-告警查询参数
 */
@Data
public class EnvAlarmQueryBo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 智能主机设备ID */
    private String deviceId;

    /** 告警类型名称（ext_info.alarmType） */
    private String alarmType;

    /** 告警状态（按位取值）：1产生 2已结束 4已确认 8用户手动结束 */
    private Integer state;

    /** 开始时间（告警产生时间） */
    private LocalDateTime beginTime;

    /** 结束时间（告警产生时间） */
    private LocalDateTime endTime;
}
