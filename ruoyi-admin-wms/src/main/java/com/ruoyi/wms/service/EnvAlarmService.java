package com.ruoyi.wms.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.JsonNode;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.ruoyi.wms.domain.bo.EnvAlarmQueryBo;
import com.ruoyi.wms.domain.entity.WmsEnvAlarm;
import com.ruoyi.wms.domain.vo.EnvAlarmVo;
import com.ruoyi.wms.mapper.WmsEnvAlarmMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 温湿度监控-告警 Service
 * <p>
 * 告警由智能主机订阅推送产生、结束或确认时主动推送过来，推送即插入，不去重
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class EnvAlarmService extends ServiceImpl<WmsEnvAlarmMapper, WmsEnvAlarm> {

    private final WmsEnvAlarmMapper envAlarmMapper;

    /**
     * 保存告警推送报文
     *
     * @param payload 推送的原始 JSON 报文
     */
    public void savePush(JsonNode payload) {
        WmsEnvAlarm alarm = new WmsEnvAlarm();
        alarm.setAlarmId(text(payload, "id"));
        alarm.setType(intValue(payload, "type"));
        alarm.setDeviceId(text(payload, "sid"));
        alarm.setSpi(intValue(payload, "spi"));
        alarm.setLevel(intValue(payload, "level"));
        alarm.setAlarmDesc(text(payload, "desc"));
        alarm.setEngDesc(text(payload, "engDesc"));
        alarm.setState(intValue(payload, "state"));
        alarm.setSTime(parseTime(text(payload, "s_time")));
        alarm.setETime(parseTime(text(payload, "e_time")));
        alarm.setCTime(parseTime(text(payload, "c_time")));
        alarm.setSOption(jsonText(payload, "s_option"));
        alarm.setEOption(jsonText(payload, "e_option"));
        alarm.setCOption(jsonText(payload, "c_option"));
        alarm.setCUser(text(payload, "c_user"));
        alarm.setParent(text(payload, "parent"));
        alarm.setPushTime(parseTime(text(payload, "time")));
        alarm.setIsDischarge(intValue(payload, "isDischarge"));
        alarm.setExtInfo(jsonText(payload, "extInfo"));
        alarm.setRawJson(payload.toString());
        envAlarmMapper.insert(alarm);
    }

    private String text(JsonNode node, String field) {
        JsonNode value = node.get(field);
        return value != null && !value.isNull() ? value.asText() : null;
    }

    private Integer intValue(JsonNode node, String field) {
        JsonNode value = node.get(field);
        return value != null && value.isNumber() ? value.asInt() : null;
    }

    /**
     * 嵌套对象整体存 JSON 字符串，原始值不丢失
     */
    private String jsonText(JsonNode node, String field) {
        JsonNode value = node.get(field);
        return value != null && !value.isNull() ? value.toString() : null;
    }

    /**
     * 解析推送报文时间（ISO 格式，如 2026-09-21T15:23:45.257251591+08:00）
     */
    private LocalDateTime parseTime(String text) {
        if (StrUtil.isBlank(text)) {
            return null;
        }
        try {
            return OffsetDateTime.parse(text).toLocalDateTime();
        } catch (Exception e) {
            try {
                return LocalDateTime.parse(text, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            } catch (Exception ex) {
                log.warn("告警时间解析失败: {}", text);
                return null;
            }
        }
    }

    /**
     * 告警分页查询（关联设备配置表）
     */
    public TableDataInfo<EnvAlarmVo> queryPageList(EnvAlarmQueryBo bo, PageQuery pageQuery) {
        Page<EnvAlarmVo> result = envAlarmMapper.selectAlarmPage(pageQuery.build(), bo);
        return TableDataInfo.build(result);
    }
}
