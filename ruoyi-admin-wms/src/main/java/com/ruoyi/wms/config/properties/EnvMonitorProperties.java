package com.ruoyi.wms.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 温湿度监控（智能主机对接）配置属性
 */
@Data
@ConfigurationProperties(prefix = "wms.env-monitor")
public class EnvMonitorProperties {

    /**
     * 定时轮询任务配置
     */
    private Job job = new Job();

    /**
     * 智能主机客户端配置
     */
    private Client client = new Client();

    @Data
    public static class Job {

        /**
         * 是否开启定时轮询任务（true 才注册任务）
         */
        private boolean enabled = false;

        /**
         * 轮询周期 cron 表达式，默认每分钟
         */
        private String cron = "0 */1 * * * ?";
    }

    @Data
    public static class Client {

        /**
         * 智能主机地址，如 http://192.168.1.100:32520
         */
        private String baseUrl;

        /**
         * 登录账号
         */
        private String account;

        /**
         * 登录密码（明文存储，调用登录接口时自动 MD5 后传输）
         */
        private String password;
    }
}
