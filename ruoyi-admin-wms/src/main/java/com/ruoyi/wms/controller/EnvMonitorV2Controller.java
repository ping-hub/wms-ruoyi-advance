package com.ruoyi.wms.controller;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.ruoyi.common.web.core.BaseController;
import com.ruoyi.wms.domain.bo.EnvAlarmQueryBo;
import com.ruoyi.wms.domain.bo.EnvDataQueryBo;
import com.ruoyi.wms.domain.vo.EnvAlarmVo;
import com.ruoyi.wms.domain.vo.EnvDataVo;
import com.ruoyi.wms.service.EnvAlarmService;
import com.ruoyi.wms.service.EnvDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 温湿度监控 V2 查询接口
 * <p>
 * 温湿度数据与告警数据分表存储、分接口取值，均关联设备配置表带出设备维度信息
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/wms/envMonitor/v2")
public class EnvMonitorV2Controller extends BaseController {

    private final EnvDataService envDataService;
    private final EnvAlarmService envAlarmService;

    /**
     * 查询每个设备的最新温湿度数据（需要登录）
     * GET /wms/envMonitor/v2/latest
     */
    @GetMapping("/latest")
    public R<List<EnvDataVo>> latest() {
        return R.ok(envDataService.latest());
    }

    /**
     * 温湿度历史数据分页查询（需要登录）
     * GET /wms/envMonitor/v2/data/list
     */
    @GetMapping("/data/list")
    public TableDataInfo<EnvDataVo> dataList(EnvDataQueryBo bo, PageQuery pageQuery) {
        return envDataService.queryPageList(bo, pageQuery);
    }

    /**
     * 告警分页查询（需要登录）
     * GET /wms/envMonitor/v2/alarm/list
     */
    @GetMapping("/alarm/list")
    public TableDataInfo<EnvAlarmVo> alarmList(EnvAlarmQueryBo bo, PageQuery pageQuery) {
        return envAlarmService.queryPageList(bo, pageQuery);
    }
}
