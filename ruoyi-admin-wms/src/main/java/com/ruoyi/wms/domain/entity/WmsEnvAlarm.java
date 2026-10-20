package com.ruoyi.wms.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 温湿度监控-告警表 wms_env_alarm
 * 智能主机订阅推送入库，推送即插入，不去重
 */
@Data
@TableName("wms_env_alarm")
public class WmsEnvAlarm implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 告警id（推送报文 id） */
    private String alarmId;

    /** 告警类型id */
    private Integer type;

    /** 告警设备id（推送报文 sid），关联 wms_env_device.device_id */
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
    private LocalDateTime sTime;

    /** 告警结束时间 */
    private LocalDateTime eTime;

    /** 告警确认时间 */
    private LocalDateTime cTime;

    /** 产生信息（JSON字符串：typeName/value） */
    private String sOption;

    /** 结束信息（JSON字符串：value） */
    private String eOption;

    /** 确认信息（JSON字符串） */
    private String cOption;

    /** 确认人 */
    private String cUser;

    /** 父级id */
    private String parent;

    /** 数据推送时间 */
    private LocalDateTime pushTime;

    /** 是否放电 */
    private Integer isDischarge;

    /** 扩展信息（JSON字符串：device设备名/point点名/alarmType告警类型名称） */
    private String extInfo;

    /** 原始报文整包存档 */
    private String rawJson;

    /** 服务端入库时间 */
    private LocalDateTime receiveTime;
}
