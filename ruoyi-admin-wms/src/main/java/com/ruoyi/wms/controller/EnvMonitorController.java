package com.ruoyi.wms.controller;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.web.core.BaseController;
import com.ruoyi.wms.domain.vo.EnvMonitorUploadVo;
import com.ruoyi.wms.service.IWmsEnvMonitorDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 环境监测数据接口
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/wms/envMonitor")
public class EnvMonitorController extends BaseController {

    private final IWmsEnvMonitorDataService envMonitorDataService;

    /**
     * 采集器上报数据（无需登录，已加入白名单）
     * POST /wms/envMonitor/upload
     * Content-Type: application/json
     * Body: JSON 数组
     */
    @PostMapping("/upload")
    public R<String> upload(@RequestBody List<EnvMonitorUploadVo> dataList) {
        if (dataList == null || dataList.isEmpty()) {
            return R.fail("上报数据不能为空");
        }
        int count = envMonitorDataService.upload(dataList);
        log.info("环境监测数据入库成功，共 {} 条", count);
        return R.ok("入库成功，共 " + count + " 条");
    }

    /**
     * 查询每个传感器的最新数据（需要登录）
     * GET /wms/envMonitor/latest
     */
    @GetMapping("/latest")
    public R<Object> latest() {
        return R.ok(envMonitorDataService.getLatest());
    }
}
