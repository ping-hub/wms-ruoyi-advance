package com.ruoyi.common.license.service;

import com.ruoyi.common.license.LicenseConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.NetworkInterface;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Enumeration;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 机器码采集服务
 * 跨平台兼容：CentOS / 统信UOS / 麒麟Kylin / macOS / x86 / ARM
 */
@Slf4j
@Service
public class MachineCodeService {

    private volatile String cachedMachineCode;

    /**
     * 获取机器码（带缓存）
     */
    public String getMachineCode() {
        if (cachedMachineCode != null) {
            return cachedMachineCode;
        }
        synchronized (this) {
            if (cachedMachineCode != null) {
                return cachedMachineCode;
            }
            cachedMachineCode = doGenerateMachineCode();
            return cachedMachineCode;
        }
    }

    /**
     * 生成机器码
     * 优先级1：CPU指纹 + MAC地址 + 主机名 → SHA256
     * 优先级2（兜底）：从本地文件读取 / 生成UUID持久化
     */
    private String doGenerateMachineCode() {
        try {
            String cpuFingerprint = getCpuFingerprint();
            String macAddress = getFirstMacAddress();
            String hostname = getHostname();

            log.info("机器码采集 - CPU指纹: {}, MAC: {}, 主机名: {}",
                cpuFingerprint != null ? "成功" : "失败",
                macAddress != null ? "成功" : "失败",
                hostname != null ? "成功" : "失败");

            // 至少有两项采集成功则使用硬件信息
            int successCount = 0;
            if (cpuFingerprint != null && !cpuFingerprint.isEmpty()) successCount++;
            if (macAddress != null && !macAddress.isEmpty()) successCount++;
            if (hostname != null && !hostname.isEmpty()) successCount++;

            if (successCount >= 2) {
                String raw = (cpuFingerprint != null ? cpuFingerprint : "")
                    + "|" + (macAddress != null ? macAddress : "")
                    + "|" + (hostname != null ? hostname : "");
                return sha256Hex(raw);
            }
        } catch (Exception e) {
            log.warn("硬件信息采集失败，使用兜底方案: {}", e.getMessage());
        }

        // 兜底方案：持久化UUID
        return getOrCreatePersistentMachineId();
    }

    /**
     * 获取 CPU 指纹
     * 兼容 x86(Intel/AMD) 和 ARM(鲲鹏/飞腾) 架构
     */
    private String getCpuFingerprint() {
        try {
            File cpuInfo = new File("/proc/cpuinfo");
            if (!cpuInfo.exists()) {
                // macOS 开发环境，使用系统属性
                return System.getProperty("os.arch", "")
                    + ":" + System.getProperty("os.name", "");
            }
            try (BufferedReader reader = new BufferedReader(new FileReader(cpuInfo))) {
                return reader.lines()
                    .filter(line ->
                        line.startsWith("Serial")
                        || line.startsWith("Model")
                        || line.contains("model name")
                        || line.contains("CPU part")
                        || line.contains("CPU variant")
                        || line.contains("Hardware"))
                    .collect(Collectors.joining(";"));
            }
        } catch (Exception e) {
            log.debug("读取 /proc/cpuinfo 失败: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 获取第一块非回环物理网卡的 MAC 地址
     */
    private String getFirstMacAddress() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface ni = interfaces.nextElement();
                // 排除回环、虚拟、未激活的接口
                if (ni.isLoopback() || ni.isVirtual() || !ni.isUp()) {
                    continue;
                }
                // 排除常见的虚拟网卡
                String name = ni.getName().toLowerCase();
                if (name.contains("docker") || name.contains("veth")
                    || name.contains("br-") || name.contains("vmnet")) {
                    continue;
                }
                byte[] mac = ni.getHardwareAddress();
                if (mac != null && mac.length > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (byte b : mac) {
                        sb.append(String.format("%02X", b));
                    }
                    return sb.toString();
                }
            }
        } catch (Exception e) {
            log.debug("获取 MAC 地址失败: {}", e.getMessage());
        }
        return null;
    }

    /**
     * 获取主机名
     */
    private String getHostname() {
        try {
            // 优先读取 /etc/hostname（Linux 通用）
            File hostnameFile = new File("/etc/hostname");
            if (hostnameFile.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(hostnameFile))) {
                    String hostname = reader.readLine();
                    if (hostname != null && !hostname.trim().isEmpty()) {
                        return hostname.trim();
                    }
                }
            }
            // 兜底用 Java 方式获取
            return java.net.InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            log.debug("获取主机名失败: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 兜底方案：从本地文件读取机器码，不存在则生成新的
     */
    private String getOrCreatePersistentMachineId() {
        File idFile = new File(LicenseConstants.MACHINE_ID_FILE);
        try {
            if (idFile.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(idFile))) {
                    String id = reader.readLine();
                    if (id != null && !id.trim().isEmpty()) {
                        log.info("从持久化文件读取机器码: {}", id.trim());
                        return id.trim();
                    }
                }
            }
            // 生成新的 UUID
            String newId = UUID.randomUUID().toString().replace("-", "").toUpperCase();
            try (FileWriter writer = new FileWriter(idFile)) {
                writer.write(newId);
            }
            log.info("生成并持久化新机器码: {}", newId);
            return newId;
        } catch (Exception e) {
            log.error("机器码持久化失败，使用临时UUID: {}", e.getMessage());
            return UUID.randomUUID().toString().replace("-", "").toUpperCase();
        }
    }

    /**
     * SHA256 哈希
     */
    private String sha256Hex(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("SHA-256 计算失败", e);
        }
    }
}
