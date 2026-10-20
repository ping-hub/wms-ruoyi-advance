package com.ruoyi.wms.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 温湿度监控-温湿度数据表 wms_env_data
 * 定时轮询入库，一台设备一次采集一条
 */
@Data
@TableName("wms_env_data")
public class WmsEnvData implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 智能主机设备ID */
    private String deviceId;

    /** 在线状态：1在线 0异常（点位 index1） */
    private Integer online;

    /** 温度值 ℃（点位 index2） */
    private BigDecimal temperature;

    /** 湿度值 %RH（点位 index3） */
    private BigDecimal humidity;

    /** 采集时间（设备点位上报时间） */
    private LocalDateTime collectTime;

    /** 服务端入库时间 */
    private LocalDateTime receiveTime;
}
