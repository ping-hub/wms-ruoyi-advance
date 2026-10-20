package com.ruoyi.wms.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.wms.domain.entity.WmsEnvDevice;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 温湿度监控-设备配置视图对象
 */
@Data
@AutoMapper(target = WmsEnvDevice.class)
public class EnvDeviceVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    /** 智能主机设备ID（如 162） */
    private String deviceId;

    /** 设备名称 */
    private String deviceName;

    /** 所属仓库 */
    private String warehouse;

    /** 设备类型：温度/湿度/烟感等 */
    private String deviceType;

    /** 位置 */
    private String location;

    /** 是否启用：1启用 0停用 */
    private Integer enabled;

    /** 备注 */
    private String remark;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
