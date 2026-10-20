package com.ruoyi.wms.job;

import cn.hutool.core.collection.CollUtil;
import com.ruoyi.wms.client.SmartHostClient;
import com.ruoyi.wms.domain.entity.WmsEnvData;
import com.ruoyi.wms.domain.entity.WmsEnvDevice;
import com.ruoyi.wms.service.EnvDataService;
import com.ruoyi.wms.service.EnvDeviceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 温湿度定时轮询任务
 * <p>
 * 每轮执行：读取启用的设备清单 -> 登录获取最新 token -> 逐个设备查询实时点位 -> 批量入库。
 * 开关：wms.env-monitor.job.enabled；周期：wms.env-monitor.job.cron（yml 可配）。
 * 单台设备查询失败不影响其他设备。
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "wms.env-monitor.job", name = "enabled", havingValue = "true")
public class EnvMonitorPollJob {

    private final EnvDeviceService envDeviceService;
    private final EnvDataService envDataService;
    private final SmartHostClient smartHostClient;

    @Scheduled(cron = "${wms.env-monitor.job.cron:0 */1 * * * ?}")
    public void poll() {
        List<WmsEnvDevice> devices = envDeviceService.listEnabled();
        if (CollUtil.isEmpty(devices)) {
            log.info("[温湿度轮询] 无启用的设备，跳过本轮");
            return;
        }
        // 每轮调用前获取最新 token，本轮所有设备共用
        String token;
        try {
            token = smartHostClient.login();
        } catch (Exception e) {
            log.error("[温湿度轮询] 智能主机登录失败，本轮轮询终止", e);
            return;
        }
        List<WmsEnvData> batch = new ArrayList<>(devices.size());
        for (WmsEnvDevice device : devices) {
            try {
                List<SmartHostClient.PointValue> points = smartHostClient.getPointValues(device.getDeviceId(), token);
                WmsEnvData data = envDataService.buildFromPoints(device.getDeviceId(), points);
                if (data != null) {
                    batch.add(data);
                }
            } catch (Exception e) {
                log.error("[温湿度轮询] 查询设备[{}-{}]失败，继续处理其他设备",
                    device.getDeviceId(), device.getDeviceName(), e);
            }
        }
        if (CollUtil.isNotEmpty(batch)) {
            envDataService.saveBatchData(batch);
            log.info("[温湿度轮询] 本轮入库 {} 条", batch.size());
        }
    }
}
