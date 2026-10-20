package com.ruoyi.wms.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.mybatis.core.mapper.BaseMapperPlus;
import com.ruoyi.wms.domain.bo.EnvAlarmQueryBo;
import com.ruoyi.wms.domain.entity.WmsEnvAlarm;
import com.ruoyi.wms.domain.vo.EnvAlarmVo;
import org.apache.ibatis.annotations.Param;

/**
 * 温湿度监控-告警 Mapper
 */
public interface WmsEnvAlarmMapper extends BaseMapperPlus<WmsEnvAlarm, EnvAlarmVo> {

    /**
     * 告警分页查询（关联设备配置表）
     */
    Page<EnvAlarmVo> selectAlarmPage(Page<EnvAlarmVo> page, @Param("query") EnvAlarmQueryBo query);
}
