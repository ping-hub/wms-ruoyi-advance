package com.ruoyi.wms.domain.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 采集器上报报文 VO
 * 新JSON格式：每条记录 = 一个传感器的单次读数
 */
@Data
public class EnvMonitorUploadVo {

    /** 采集器唯一编号 */
    private String collectId;

    /** 传感器唯一编号 */
    private String deviceId;

    /** 传感器类型：温度/湿度/烟雾 */
    private String deviceType;

    /** 在线状态 0:在线 1:离线 */
    private Integer online;

    /** 采集时间戳，格式 yyyy-MM-dd HH:mm:ss */
    private String timestamp;

    /** 传感器数值（温度℃/湿度%RH/烟雾浓度） */
    @JsonProperty("deviceValue")
    private Double deviceValue;

    /** 报警状态：0正常，1告警 */
    @JsonProperty("alarStatus")
    private Integer alarStatus;
}
