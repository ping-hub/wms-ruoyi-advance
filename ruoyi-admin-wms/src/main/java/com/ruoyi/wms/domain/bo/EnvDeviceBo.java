package com.ruoyi.wms.domain.bo;

import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import com.ruoyi.common.mybatis.core.domain.BaseEntity;
import com.ruoyi.wms.domain.entity.WmsEnvDevice;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 温湿度监控-设备配置业务对象
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = WmsEnvDevice.class, reverseConvertGenerate = false)
public class EnvDeviceBo extends BaseEntity {

    @NotNull(message = "主键不能为空", groups = {EditGroup.class})
    private Long id;

    /** 智能主机设备ID（如 162） */
    @NotBlank(message = "设备ID不能为空", groups = {AddGroup.class, EditGroup.class})
    private String deviceId;

    /** 设备名称 */
    @NotBlank(message = "设备名称不能为空", groups = {AddGroup.class, EditGroup.class})
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
}
