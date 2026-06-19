package com.ruoyi.common.license.controller;

import com.ruoyi.common.license.domain.LicenseStatusVo;
import com.ruoyi.common.license.service.LicenseService;
import com.ruoyi.common.license.service.MachineCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * License 管理接口
 * 所有 /license/** 接口始终放行，不受 License 拦截器限制
 */
@RestController
@RequestMapping("/license")
@RequiredArgsConstructor
public class LicenseController {

    private final LicenseService licenseService;
    private final MachineCodeService machineCodeService;

    /**
     * 获取当前服务器机器码
     * 用于客户告知管理员以颁发 License
     */
    @GetMapping("/machineCode")
    public Map<String, Object> getMachineCode() {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("machineCode", machineCodeService.getMachineCode());
        return result;
    }

    /**
     * 查询当前 License 状态
     */
    @GetMapping("/status")
    public Map<String, Object> getStatus() {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("data", licenseService.getStatus());
        return result;
    }

    /**
     * 激活 License
     *
     * @param body 请求体，包含 licenseContent 字段
     * @return 激活结果
     */
    @PostMapping("/activate")
    public Map<String, Object> activate(@RequestBody Map<String, String> body) {
        Map<String, Object> result = new HashMap<>();
        String licenseContent = body.get("licenseContent");
        if (licenseContent == null || licenseContent.trim().isEmpty()) {
            result.put("code", 500);
            result.put("msg", "License 内容不能为空");
            return result;
        }
        boolean success = licenseService.activate(licenseContent.trim());
        if (success) {
            result.put("code", 200);
            result.put("msg", "License 激活成功");
        } else {
            result.put("code", 500);
            result.put("msg", "License 激活失败：签名无效、已过期或机器码不匹配");
        }
        return result;
    }
}
