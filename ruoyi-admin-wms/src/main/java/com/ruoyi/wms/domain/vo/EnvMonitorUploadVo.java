package com.ruoyi.wms.domain.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 采集器上报报文 VO
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

    /** 温度 */
    private Double temp;

    /** 湿度 */
    private Double hum;

    /** 烟雾浓度 */
    @JsonProperty("smokeVal")
    private Double smokeVal;

    /** 报警状态数组 [0,1] */
    @JsonProperty("alarStatus")
    private List<Integer> alarStatus;
}
