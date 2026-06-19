package com.ruoyi.common.license.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ruoyi.common.license.LicenseConstants;
import com.ruoyi.common.license.domain.LicenseInfo;
import com.ruoyi.common.license.domain.LicenseStatusVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

/**
 * License 核心服务
 * 负责 License 文件的验签、校验、激活
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LicenseService {

    private final MachineCodeService machineCodeService;
    private final LicenseStatusHolder statusHolder;

    @Value("${license.file-path:./license/license.lic}")
    private String licenseFilePath;

    private final ObjectMapper objectMapper = new ObjectMapper()
        .registerModule(new JavaTimeModule());

    /**
     * 启动时加载 License
     */
    public void loadLicenseOnStartup() {
        try {
            File file = new File(licenseFilePath);
            if (!file.exists()) {
                log.warn("License 文件不存在: {}，系统进入未激活状态", licenseFilePath);
                statusHolder.updateStatus(null);
                return;
            }
            LicenseInfo info = readAndVerifyLicenseFile(file);
            if (info == null) {
                log.warn("License 文件校验失败，系统进入未激活状态");
                statusHolder.updateStatus(null);
                return;
            }
            // 校验有效期
            if (isExpired(info)) {
                log.warn("License 已过期（到期时间: {}），系统进入过期只读状态", info.getExpiresAt());
                statusHolder.updateStatus(info);
                return;
            }
            // 校验机器码
            if (!verifyMachineCode(info)) {
                log.warn("License 机器码不匹配，系统进入未激活状态");
                statusHolder.updateStatus(null);
                return;
            }
            log.info("License 校验通过: 颁发对象={}, 到期时间={}", info.getIssuedTo(), info.getExpiresAt());
            statusHolder.updateStatus(info);
        } catch (Exception e) {
            log.error("License 加载异常: {}", e.getMessage(), e);
            statusHolder.updateStatus(null);
        }
    }

    /**
     * 激活 License（接收前端上传的内容）
     *
     * @param licenseContent License 文件内容（Base64 编码）
     * @return 激活结果
     */
    public boolean activate(String licenseContent) {
        try {
            LicenseInfo info = parseAndVerify(licenseContent);
            if (info == null) {
                log.error("License 激活失败：签名验证不通过");
                return false;
            }
            if (isExpired(info)) {
                log.error("License 激活失败：License 已过期");
                return false;
            }
            if (!verifyMachineCode(info)) {
                log.error("License 激活失败：机器码不匹配");
                return false;
            }
            // 保存文件到本地
            saveLicenseFile(licenseContent);
            // 更新内存状态
            statusHolder.updateStatus(info);
            // 写入 Redis
            statusHolder.syncToRedis(info);
            // 通知其他节点刷新
            statusHolder.publishRefresh();
            log.info("License 激活成功: 颁发对象={}, 到期时间={}", info.getIssuedTo(), info.getExpiresAt());
            return true;
        } catch (Exception e) {
            log.error("License 激活异常: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 获取当前 License 状态
     */
    public LicenseStatusVo getStatus() {
        LicenseStatusVo vo = new LicenseStatusVo();
        LicenseInfo info = statusHolder.getCurrentLicense();

        if (info == null) {
            vo.setValid(false);
            vo.setStatusDesc("未激活");
            return vo;
        }

        boolean expired = isExpired(info);
        boolean machineMatch = verifyMachineCode(info);

        vo.setValid(!expired && machineMatch);
        vo.setIssuedTo(info.getIssuedTo());
        vo.setIssuedAt(info.getIssuedAt());
        vo.setExpiresAt(info.getExpiresAt());
        vo.setType(info.getType());
        vo.setMachineBound(info.getMachineCode() != null && !info.getMachineCode().isEmpty());

        if (vo.isValid()) {
            long days = ChronoUnit.DAYS.between(LocalDateTime.now(), info.getExpiresAt());
            vo.setRemainingDays(days);
            vo.setStatusDesc(days <= 30 ? "即将过期（" + days + "天）" : "有效（剩余" + days + "天）");
        } else if (expired) {
            long days = ChronoUnit.DAYS.between(info.getExpiresAt(), LocalDateTime.now());
            vo.setRemainingDays(-days);
            vo.setStatusDesc("已过期（" + days + "天前）");
        } else {
            vo.setStatusDesc("机器码不匹配");
        }
        return vo;
    }

    /**
     * 读取并验证 License 文件
     */
    private LicenseInfo readAndVerifyLicenseFile(File file) {
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] bytes = fis.readAllBytes();
            String content = new String(bytes, StandardCharsets.UTF_8).trim();
            return parseAndVerify(content);
        } catch (IOException e) {
            log.error("读取 License 文件失败: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 解析并验证 License 内容（RSA 签名验证）
     */
    private LicenseInfo parseAndVerify(String licenseContent) {
        try {
            // License 格式：BASE64(data) + "." + BASE64(signature)
            int dotIdx = licenseContent.lastIndexOf('.');
            if (dotIdx <= 0 || dotIdx >= licenseContent.length() - 1) {
                log.error("License 格式错误：缺少签名分隔符");
                return null;
            }

            String dataBase64 = licenseContent.substring(0, dotIdx);
            String signatureBase64 = licenseContent.substring(dotIdx + 1);

            // RSA 验签
            byte[] dataBytes = Base64.getDecoder().decode(dataBase64);
            byte[] signatureBytes = Base64.getDecoder().decode(signatureBase64);

            PublicKey publicKey = getPublicKey();
            Signature sig = Signature.getInstance("SHA256withRSA");
            sig.initVerify(publicKey);
            sig.update(dataBytes);

            if (!sig.verify(signatureBytes)) {
                log.error("License 签名验证失败");
                return null;
            }

            // 解析 JSON
            String json = new String(dataBytes, StandardCharsets.UTF_8);
            return objectMapper.readValue(json, LicenseInfo.class);
        } catch (Exception e) {
            log.error("License 解析失败: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 校验 License 是否过期
     */
    private boolean isExpired(LicenseInfo info) {
        if (info.getExpiresAt() == null) {
            return true;
        }
        return LocalDateTime.now().isAfter(info.getExpiresAt());
    }

    /**
     * 校验机器码
     * 如果 License 未绑定机器码（machineCode 为空），则跳过校验
     */
    private boolean verifyMachineCode(LicenseInfo info) {
        String licenseMachine = info.getMachineCode();
        if (licenseMachine == null || licenseMachine.trim().isEmpty()) {
            return true; // 未绑定机器码，跳过校验
        }
        String currentMachine = machineCodeService.getMachineCode();
        return licenseMachine.equalsIgnoreCase(currentMachine);
    }

    /**
     * 保存 License 文件到本地
     */
    private void saveLicenseFile(String content) {
        try {
            File file = new File(licenseFilePath);
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            try (FileWriter writer = new java.io.FileWriter(file)) {
                writer.write(content);
            }
            log.info("License 文件已保存至: {}", licenseFilePath);
        } catch (Exception e) {
            log.error("保存 License 文件失败: {}", e.getMessage());
        }
    }

    /**
     * 获取 RSA 公钥
     */
    private PublicKey getPublicKey() throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(LicenseConstants.PUBLIC_KEY);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(spec);
    }
}
