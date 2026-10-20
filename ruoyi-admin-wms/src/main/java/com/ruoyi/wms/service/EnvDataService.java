package com.ruoyi.wms.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.ruoyi.wms.client.SmartHostClient;
import com.ruoyi.wms.domain.bo.EnvDataQueryBo;
import com.ruoyi.wms.domain.entity.WmsEnvData;
import com.ruoyi.wms.domain.vo.EnvDataVo;
import com.ruoyi.wms.mapper.WmsEnvDataMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 温湿度监控-温湿度数据 Service
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class EnvDataService extends ServiceImpl<WmsEnvDataMapper, WmsEnvData> {

    private final WmsEnvDataMapper envDataMapper;

    private static final DateTimeFormatter POINT_TIME_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /** 在线状态（点位 index1） */
    private static final int POINT_ONLINE = 1;
    /** 温度（点位 index2） */
    private static final int POINT_TEMPERATURE = 2;
    /** 湿度（点位 index3） */
    private static final int POINT_HUMIDITY = 3;

    /**
     * 将设备实时点位值解析为入库实体
     * <p>
     * 点位约定：index1=在线状态（1在线 0异常）、index2=温度、index3=湿度
     *
     * @param deviceId 设备ID
     * @param points   设备实时点位值列表
     * @return 入库实体，点位为空时返回 null
     */
    public WmsEnvData buildFromPoints(String deviceId, List<SmartHostClient.PointValue> points) {
        if (CollUtil.isEmpty(points)) {
            return null;
        }
        WmsEnvData data = new WmsEnvData();
        data.setDeviceId(deviceId);
        for (SmartHostClient.PointValue point : points) {
            if (point == null || point.getIndex() == null) {
                continue;
            }
            switch (point.getIndex()) {
                case POINT_ONLINE -> data.setOnline(point.getValue() != null ? point.getValue().intValue() : null);
                case POINT_TEMPERATURE -> data.setTemperature(point.getValue());
                case POINT_HUMIDITY -> data.setHumidity(point.getValue());
                default -> {
                    // 其他点位暂不纳入
                }
            }
        }
        // 采集时间取点位上报时间（同一设备各点位时间一致）
        points.stream()
            .filter(p -> p != null && StrUtil.isNotBlank(p.getTime()))
            .map(SmartHostClient.PointValue::getTime)
            .findFirst()
            .ifPresent(time -> data.setCollectTime(parsePointTime(time)));
        return data;
    }

    private LocalDateTime parsePointTime(String time) {
        try {
            return LocalDateTime.parse(time.trim(), POINT_TIME_FMT);
        } catch (Exception e) {
            log.warn("采集时间解析失败，使用当前时间: {}", time);
            return LocalDateTime.now();
        }
    }

    /**
     * 批量入库
     */
    public void saveBatchData(List<WmsEnvData> list) {
        envDataMapper.insertBatch(list);
    }

    /**
     * 查询每个设备的最新一条数据（关联设备配置表）
     */
    public List<EnvDataVo> latest() {
        return envDataMapper.selectLatestList();
    }

    /**
     * 历史数据分页查询（关联设备配置表）
     */
    public TableDataInfo<EnvDataVo> queryPageList(EnvDataQueryBo bo, PageQuery pageQuery) {
        Page<EnvDataVo> result = envDataMapper.selectDataPage(pageQuery.build(), bo);
        return TableDataInfo.build(result);
    }
}
