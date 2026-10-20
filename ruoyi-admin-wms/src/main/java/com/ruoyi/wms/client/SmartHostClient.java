package com.ruoyi.wms.client;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.ContentType;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ruoyi.wms.config.properties.EnvMonitorProperties;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * 智能主机 HTTP 客户端
 * <p>
 * 登录：POST /api/central/auth/login?account=xxx&password=&lt;md5&gt;
 * 实时数据：POST /api/central/device/point/value/get，Header 携带 token，Body {"id": 设备id}
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SmartHostClient {

    private final EnvMonitorProperties properties;

    private static final String LOGIN_PATH = "/api/central/auth/login";
    private static final String POINT_VALUE_PATH = "/api/central/device/point/value/get";

    /** 请求超时时间（毫秒） */
    private static final int TIMEOUT_MS = 10_000;

    /**
     * 登录获取 token（每次轮询前调用，以最新返回的 token 为准）
     */
    public String login() {
        EnvMonitorProperties.Client client = properties.getClient();
        Assert.isTrue(StrUtil.isNotBlank(client.getBaseUrl()), "智能主机地址未配置：wms.env-monitor.client.base-url");
        Assert.isTrue(StrUtil.isNotBlank(client.getAccount()), "智能主机账号未配置：wms.env-monitor.client.account");
        Assert.isTrue(StrUtil.isNotBlank(client.getPassword()), "智能主机密码未配置：wms.env-monitor.client.password");
        // password 需要传 MD5 后的值
        String passwordMd5 = SecureUtil.md5(client.getPassword());
        String url = client.getBaseUrl() + LOGIN_PATH
            + "?account=" + client.getAccount() + "&password=" + passwordMd5;
        String body = HttpRequest.post(url).timeout(TIMEOUT_MS).execute().body();
        JSONObject json = JSONUtil.parseObj(body);
        Integer code = json.getInt("code");
        if (code == null || code != 0) {
            throw new IllegalStateException("智能主机登录失败，响应：" + body);
        }
        JSONObject data = json.getJSONObject("data");
        String token = data != null ? data.getStr("token") : null;
        if (StrUtil.isBlank(token)) {
            throw new IllegalStateException("智能主机登录成功但未返回 token，响应：" + body);
        }
        log.info("[智能主机] 登录成功，获取到新 token");
        return token;
    }

    /**
     * 查询设备实时点位值
     *
     * @param deviceId 设备id（智能主机配置工具中查看的设备ID）
     * @param token    登录获取的 token
     */
    public List<PointValue> getPointValues(String deviceId, String token) {
        String url = properties.getClient().getBaseUrl() + POINT_VALUE_PATH;
        // 设备id 为数字时按数字传递
        Object idValue = NumberUtil.isLong(deviceId) ? Long.valueOf(deviceId) : deviceId;
        String body = HttpRequest.post(url)
            .header("token", token)
            .header(Header.CONTENT_TYPE.getValue(), ContentType.JSON.getValue())
            .body(JSONUtil.createObj().set("id", idValue).toString())
            .timeout(TIMEOUT_MS)
            .execute()
            .body();
        JSONObject json = JSONUtil.parseObj(body);
        Integer code = json.getInt("code");
        if (code == null || code != 0) {
            throw new IllegalStateException("查询设备[" + deviceId + "]实时数据失败，响应：" + body);
        }
        JSONArray data = json.getJSONArray("data");
        if (data == null) {
            return Collections.emptyList();
        }
        return data.toList(PointValue.class);
    }

    /**
     * 设备点位值
     */
    @Data
    public static class PointValue {

        /** 点位序号：1在线状态 2温度 3湿度 */
        private Integer index;

        /** 采集间隔（毫秒） */
        private Long interval;

        /** 采集时间 yyyy-MM-dd HH:mm:ss */
        private String time;

        /** 点位值 */
        private BigDecimal value;
    }
}
