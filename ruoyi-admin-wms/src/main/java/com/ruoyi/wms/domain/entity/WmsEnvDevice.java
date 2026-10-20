package com.ruoyi.wms.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.mybatis.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 温湿度监控-设备配置表 wms_env_device
 * 维度表：页面取值关联用，可通过页面维护
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wms_env_device")
public class WmsEnvDevice extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 智能主机设备ID（如 162），与推送报文 sid / 轮询设备 id 对应 */
    private String deviceId;

    /** 设备名称（如 弱电间7温湿度传感器） */
    private String deviceName;

    /** 所属仓库 */
    private String warehouse;

    /** 设备类型：温度/湿度/烟感等 */
    private String deviceType;

    /** 位置 */
    private String location;

    /** 是否启用：1启用 0停用（停用后不参与定时轮询） */
    private Integer enabled;

    /** 备注 */
    private String remark;
}
