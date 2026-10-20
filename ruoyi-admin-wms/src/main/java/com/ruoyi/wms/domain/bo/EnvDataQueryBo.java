package com.ruoyi.wms.domain.bo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 温湿度监控-历史数据查询参数
 */
@Data
public class EnvDataQueryBo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 智能主机设备ID */
    private String deviceId;

    /** 开始时间（采集时间） */
    private LocalDateTime beginTime;

    /** 结束时间（采集时间） */
    private LocalDateTime endTime;
}
