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
 * 温湿度/烟感监测数据 wms_env_monitor_data
 */
@Data
@TableName("wms_env_monitor_data")
public class WmsEnvMonitorData implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 采集器唯一编号 */
    private String collectId;

    /** 传感器唯一编号 */
    private String deviceId;

    /** 传感器类型：温度/湿度/烟雾 */
    private String deviceType;

    /** 在线状态 0:在线 1:离线 */
    private Integer online;

    /** 采集器上报时间 */
    private LocalDateTime collectTime;

    /** 温度（℃） */
    private BigDecimal temp;

    /** 湿度（%RH） */
    private BigDecimal hum;

    /** 烟雾浓度值 */
    private BigDecimal smokeVal;

    /** 报警状态JSON */
    private String alarStatus;

    /** 服务端接收时间 */
    private LocalDateTime receiveTime;

    /** 创建时间 */
    private LocalDateTime createTime;
}
