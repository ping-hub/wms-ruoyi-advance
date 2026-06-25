package com.ruoyi.wms.service;

import com.ruoyi.wms.domain.vo.EnvMonitorUploadVo;

import java.util.List;
import java.util.Map;

/**
 * 环境监测数据 Service
 */
public interface IWmsEnvMonitorDataService {

    /**
     * 接收采集器上报数据，批量入库
     */
    int upload(List<EnvMonitorUploadVo> voList);

    /**
     * 查询每个传感器的最新数据
     */
    List<Map<String, Object>> getLatest();
}
