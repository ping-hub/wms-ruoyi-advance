package com.ruoyi.wms.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.wms.domain.entity.WmsEnvAlarm;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 温湿度监控-告警视图对象（含设备配置信息）
 */
@Data
@AutoMapper(target = WmsEnvAlarm.class)
public class EnvAlarmVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    /** 告警id（推送报文 id） */
    private String alarmId;

    /** 告警类型id */
    private Integer type;

    /** 告警设备id（推送报文 sid） */
    private String deviceId;

    /** 告警点索引 */
    private Integer spi;

    /** 告警等级 */
    private Integer level;

    /** 告警描述 */
    private String alarmDesc;

    /** 英文描述 */
    private String engDesc;

    /** 告警状态（按位取值）：1产生 2已结束 4已确认 8用户手动结束 */
    private Integer state;

    /** 告警产生时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sTime;

    /** 告警结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eTime;

    /** 告警确认时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime cTime;

    /** 产生信息（JSON字符串） */
    private String sOption;

    /** 结束信息（JSON字符串） */
    private String eOption;

    /** 确认信息（JSON字符串） */
    private String cOption;

    /** 确认人 */
    private String cUser;

    /** 父级id */
    private String parent;

    /** 数据推送时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime pushTime;

    /** 是否放电 */
    private Integer isDischarge;

    /** 扩展信息（JSON字符串） */
    private String extInfo;

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
