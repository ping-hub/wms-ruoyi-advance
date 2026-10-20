package com.ruoyi.wms.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.wms.domain.entity.WmsEnvData;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 温湿度监控-温湿度数据视图对象（含设备配置信息）
 */
@Data
@AutoMapper(target = WmsEnvData.class)
public class EnvDataVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    /** 智能主机设备ID */
    private String deviceId;

    /** 在线状态：1在线 0异常 */
    private Integer online;

    /** 温度值 ℃ */
    private BigDecimal temperature;

    /** 湿度值 %RH */
    private BigDecimal humidity;

    /** 采集时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime collectTime;

    /** 入库时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime receiveTime;

    // ------- 以下为关联设备配置表（wms_env_device）带出的维度信息 -------

    /** 设备名称 */
    private String deviceName;

    /** 所属仓库 */
    private String warehouse;

    /** 设备类型 */
    private String deviceType;

    /** 位置 */
    private String location;
}
