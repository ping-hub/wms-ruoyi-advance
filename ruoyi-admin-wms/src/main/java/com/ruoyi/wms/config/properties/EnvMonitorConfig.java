package com.ruoyi.wms.config.properties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 温湿度监控配置
 */
@Configuration
@EnableScheduling
@EnableConfigurationProperties(EnvMonitorProperties.class)
public class EnvMonitorConfig {
}
