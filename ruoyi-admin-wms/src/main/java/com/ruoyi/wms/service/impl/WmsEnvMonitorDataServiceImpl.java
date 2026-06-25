package com.ruoyi.wms.service.impl;

import com.ruoyi.wms.domain.entity.WmsEnvMonitorData;
import com.ruoyi.wms.domain.vo.EnvMonitorUploadVo;
import com.ruoyi.wms.mapper.WmsEnvMonitorDataMapper;
import com.ruoyi.wms.service.IWmsEnvMonitorDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 环境监测数据 Service 实现
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class WmsEnvMonitorDataServiceImpl implements IWmsEnvMonitorDataService {

    private final WmsEnvMonitorDataMapper mapper;

    private static final DateTimeFormatter TS_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public int upload(List<EnvMonitorUploadVo> voList) {
        if (voList == null || voList.isEmpty()) {
            return 0;
        }
        List<WmsEnvMonitorData> entities = new ArrayList<>(voList.size());
        for (EnvMonitorUploadVo vo : voList) {
            WmsEnvMonitorData e = new WmsEnvMonitorData();
            e.setCollectId(vo.getCollectId());
            e.setDeviceId(vo.getDeviceId());
            e.setDeviceType(vo.getDeviceType());
            e.setOnline(vo.getOnline());
            // 解析采集时间
            if (vo.getTimestamp() != null && !vo.getTimestamp().isBlank()) {
                try {
                    e.setCollectTime(LocalDateTime.parse(vo.getTimestamp().trim(), TS_FMT));
                } catch (Exception ex) {
                    log.warn("采集时间解析失败，使用当前时间: {}", vo.getTimestamp());
                    e.setCollectTime(LocalDateTime.now());
                }
            } else {
                e.setCollectTime(LocalDateTime.now());
            }
            e.setTemp(vo.getTemp() != null ? BigDecimal.valueOf(vo.getTemp()) : null);
            e.setHum(vo.getHum() != null ? BigDecimal.valueOf(vo.getHum()) : null);
            e.setSmokeVal(vo.getSmokeVal() != null ? BigDecimal.valueOf(vo.getSmokeVal()) : null);
            // alarStatus 列表序列化为 JSON 字符串存储
            if (vo.getAlarStatus() != null) {
                e.setAlarStatus(vo.getAlarStatus().toString());
            }
            entities.add(e);
        }
        return mapper.insertBatch(entities);
    }

    @Override
    public List<Map<String, Object>> getLatest() {
        return mapper.selectLatestByDevice();
    }
}
