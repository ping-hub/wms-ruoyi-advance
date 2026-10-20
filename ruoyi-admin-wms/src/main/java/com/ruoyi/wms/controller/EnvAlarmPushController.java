package com.ruoyi.wms.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.wms.service.EnvAlarmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 智能主机告警推送接收接口
 * <p>
 * 智能主机在告警产生、结束或确认时主动推送告警 JSON 到订阅的 url，
 * 本接口接收报文并入库（推送即插入，不去重，记录入库时间）。
 * 无需登录（已加入 security.excludes 白名单）。
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/wms/envMonitor/v2/alarm")
public class EnvAlarmPushController {

    private final EnvAlarmService envAlarmService;

    /**
     * 接收告警推送
     * POST /wms/envMonitor/v2/alarm/push
     * Content-Type: application/json
     */
    @PostMapping("/push")
    public R<String> push(@RequestBody JsonNode payload) {
        envAlarmService.savePush(payload);
        log.info("[告警推送] 告警入库成功，alarmId={}", payload != null ? payload.path("id").asText("") : "");
        return R.ok("success");
    }
}
