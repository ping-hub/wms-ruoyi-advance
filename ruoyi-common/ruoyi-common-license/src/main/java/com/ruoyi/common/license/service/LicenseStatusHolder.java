package com.ruoyi.common.license.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ruoyi.common.license.LicenseConstants;
import com.ruoyi.common.license.domain.LicenseInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicReference;

/**
 * License 状态内存缓存
 * - 内存级读取（纳秒级性能）
 * - 定时从 Redis 刷新
 * - 接收 Pub/Sub 通知立即刷新
 */
@Slf4j
@Component
public class LicenseStatusHolder {

    private final AtomicReference<LicenseInfo> currentLicense = new AtomicReference<>();
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper()
        .registerModule(new JavaTimeModule());

    public LicenseStatusHolder(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 获取当前内存中的 License 信息
     */
    public LicenseInfo getCurrentLicense() {
        return currentLicense.get();
    }

    /**
     * License 是否有效（未过期）
     */
    public boolean isValid() {
        LicenseInfo info = currentLicense.get();
        if (info == null) {
            return false;
        }
        return info.getExpiresAt() != null && LocalDateTime.now().isBefore(info.getExpiresAt());
    }

    /**
     * 更新内存状态
     */
    public void updateStatus(LicenseInfo info) {
        currentLicense.set(info);
        log.info("License 内存状态已更新: {}", info != null ? "有效(" + info.getIssuedTo() + ")" : "未激活");
    }

    /**
     * 将 License 信息同步到 Redis（多节点共享）
     */
    public void syncToRedis(LicenseInfo info) {
        try {
            if (info == null) {
                redisTemplate.delete(LicenseConstants.REDIS_LICENSE_STATUS);
            } else {
                String json = objectMapper.writeValueAsString(info);
                redisTemplate.opsForValue().set(LicenseConstants.REDIS_LICENSE_STATUS, json);
            }
            log.info("License 状态已同步至 Redis");
        } catch (Exception e) {
            log.error("License 同步 Redis 失败: {}", e.getMessage());
        }
    }

    /**
     * 发布刷新通知（Pub/Sub），通知所有节点刷新内存缓存
     */
    public void publishRefresh() {
        try {
            redisTemplate.convertAndSend(LicenseConstants.REDIS_LICENSE_CHANNEL, "refresh");
            log.info("License 刷新通知已发布");
        } catch (Exception e) {
            log.warn("License 刷新通知发布失败: {}", e.getMessage());
        }
    }

    /**
     * 从 Redis 加载 License 状态到内存（定时刷新，每5分钟）
     */
    @Scheduled(fixedRate = 300000, initialDelay = 60000)
    public void refreshFromRedis() {
        try {
            String json = redisTemplate.opsForValue().get(LicenseConstants.REDIS_LICENSE_STATUS);
            if (json == null || json.isEmpty()) {
                // Redis 中没有状态，保持当前内存状态
                return;
            }
            LicenseInfo info = objectMapper.readValue(json, LicenseInfo.class);
            LicenseInfo current = currentLicense.get();
            // 只有数据有变化才更新
            if (current == null || !current.equals(info)) {
                updateStatus(info);
                log.info("License 状态已从 Redis 刷新");
            }
        } catch (Exception e) {
            log.debug("从 Redis 刷新 License 状态失败: {}", e.getMessage());
        }
    }

    /**
     * 接收 Pub/Sub 刷新通知，立即刷新
     */
    public void onRefreshMessage(String message) {
        log.info("收到 License 刷新通知: {}", message);
        refreshFromRedis();
    }

    /**
     * 获取 Redis Channel Topic（用于注册监听器）
     */
    public ChannelTopic getRefreshTopic() {
        return new ChannelTopic(LicenseConstants.REDIS_LICENSE_CHANNEL);
    }
}
