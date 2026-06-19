package com.ruoyi.common.license.config;

import com.ruoyi.common.license.interceptor.LicenseInterceptor;
import com.ruoyi.common.license.service.LicenseService;
import com.ruoyi.common.license.service.LicenseStatusHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * License 自动配置
 * - 注册拦截器
 * - 启动时加载 License
 * - 注册 Redis Pub/Sub 监听器
 */
@Slf4j
@AutoConfiguration
@EnableScheduling
@RequiredArgsConstructor
public class LicenseConfig implements WebMvcConfigurer {

    private final LicenseInterceptor licenseInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(licenseInterceptor)
            .addPathPatterns("/**")
            .excludePathPatterns(
                // License 相关接口始终放行
                "/license/**",
                // 登录接口
                "/login",
                "/captchaImage",
                // 静态资源
                "/*.html",
                "/**/*.html",
                "/**/*.css",
                "/**/*.js",
                "/favicon.ico",
                // API 文档
                "/*/api-docs",
                "/*/api-docs/**",
                // actuator
                "/actuator",
                "/actuator/**"
            );
    }

    /**
     * 应用启动后加载 License
     */
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady(ApplicationReadyEvent event) {
        LicenseService licenseService = event.getApplicationContext().getBean(LicenseService.class);
        licenseService.loadLicenseOnStartup();
    }

    /**
     * 创建 Redis 消息监听容器（如不存在）
     */
    @Bean
    @ConditionalOnMissingBean(RedisMessageListenerContainer.class)
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        return container;
    }

    /**
     * 注册 License 刷新监听器
     */
    @Bean
    public LicenseRedisListenerRegistrar licenseRedisListenerRegistrar(
            RedisMessageListenerContainer container,
            LicenseStatusHolder statusHolder) {
        return new LicenseRedisListenerRegistrar(container, statusHolder);
    }

    /**
     * Redis 监听器注册辅助类（避免循环依赖）
     */
    static class LicenseRedisListenerRegistrar {
        LicenseRedisListenerRegistrar(RedisMessageListenerContainer container,
                                      LicenseStatusHolder statusHolder) {
            MessageListener listener = (message, pattern) -> {
                String body = new String(message.getBody());
                statusHolder.onRefreshMessage(body);
            };
            container.addMessageListener(listener, statusHolder.getRefreshTopic());
            log.info("License Redis Pub/Sub 监听器已注册");
        }

        private static final org.slf4j.Logger log =
            org.slf4j.LoggerFactory.getLogger(LicenseRedisListenerRegistrar.class);
    }
}
