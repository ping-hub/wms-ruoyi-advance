package com.ruoyi.wms.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.web.core.BaseController;
import com.ruoyi.wms.domain.vo.BoxTraceVo;
import com.ruoyi.wms.domain.vo.ItemTraceVo;
import com.ruoyi.wms.service.TraceService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/wms/trace")
public class TraceController extends BaseController {

    private final TraceService traceService;

    @SaCheckPermission("wms:itemInstance:list")
    @GetMapping("/item/{instanceCode}")
    public R<ItemTraceVo> itemTrace(@NotBlank(message = "单品码不能为空") @PathVariable String instanceCode) {
        return R.ok(traceService.queryItemTraceByCode(instanceCode));
    }

    @SaCheckPermission("wms:box:list")
    @GetMapping("/box/{boxCode}")
    public R<BoxTraceVo> boxTrace(@NotBlank(message = "箱码不能为空") @PathVariable String boxCode) {
        return R.ok(traceService.queryBoxTraceByCode(boxCode));
    }
}
