package com.ruoyi.wms.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import com.ruoyi.common.idempotent.annotation.RepeatSubmit;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.ruoyi.common.web.core.BaseController;
import com.ruoyi.wms.domain.bo.EnvDeviceBo;
import com.ruoyi.wms.domain.vo.EnvDeviceVo;
import com.ruoyi.wms.service.EnvDeviceService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 温湿度监控-设备配置维护接口
 * <p>
 * 设备清单作为维度表维护，定时任务按启用设备轮询，页面取值与数据/告警表关联
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/wms/envDevice")
public class EnvDeviceController extends BaseController {

    private final EnvDeviceService envDeviceService;

    /**
     * 查询设备配置列表
     */
    @SaCheckPermission("wms:envDevice:list")
    @GetMapping("/list")
    public TableDataInfo<EnvDeviceVo> list(EnvDeviceBo bo, PageQuery pageQuery) {
        return envDeviceService.queryPageList(bo, pageQuery);
    }

    /**
     * 查询设备配置列表（不分页）
     */
    @SaCheckPermission("wms:envDevice:list")
    @GetMapping("/listNoPage")
    public R<List<EnvDeviceVo>> listNoPage(EnvDeviceBo bo) {
        return R.ok(envDeviceService.queryList(bo));
    }

    /**
     * 获取设备配置详细信息
     */
    @SaCheckPermission("wms:envDevice:list")
    @GetMapping("/{id}")
    public R<EnvDeviceVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(envDeviceService.queryById(id));
    }

    /**
     * 新增设备配置
     */
    @SaCheckPermission("wms:envDevice:edit")
    @Log(title = "温湿度监控设备", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody EnvDeviceBo bo) {
        envDeviceService.insertByBo(bo);
        return R.ok();
    }

    /**
     * 修改设备配置
     */
    @SaCheckPermission("wms:envDevice:edit")
    @Log(title = "温湿度监控设备", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody EnvDeviceBo bo) {
        envDeviceService.updateByBo(bo);
        return R.ok();
    }

    /**
     * 启用/停用设备
     */
    @SaCheckPermission("wms:envDevice:edit")
    @Log(title = "温湿度监控设备", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public R<Void> changeStatus(@NotNull(message = "主键不能为空") @RequestParam Long id,
                                @NotNull(message = "状态不能为空") @RequestParam Integer enabled) {
        envDeviceService.changeStatus(id, enabled);
        return R.ok();
    }

    /**
     * 批量删除设备配置
     */
    @SaCheckPermission("wms:envDevice:edit")
    @Log(title = "温湿度监控设备", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable List<Long> ids) {
        envDeviceService.deleteByIds(ids);
        return R.ok();
    }
}
