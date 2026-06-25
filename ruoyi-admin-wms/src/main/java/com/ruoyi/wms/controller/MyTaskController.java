package com.ruoyi.wms.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.ruoyi.common.web.core.BaseController;
import com.ruoyi.wms.domain.vo.MyTaskVo;
import com.ruoyi.wms.service.MyTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 我的待办
 *
 * @author ping
 * @date 2026-06-17
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/wms/myTasks")
public class MyTaskController extends BaseController {

    private final MyTaskService myTaskService;

    /**
     * 查询我的待办列表
     *
     * @param taskType  待办类型：pending_approval / pending_execute / rejected / null=全部
     * @param orderNo   单号模糊搜索
     */
    @SaCheckPermission("wms:myTasks:list")
    @GetMapping("/list")
    public TableDataInfo<MyTaskVo> list(
        @RequestParam(required = false) String status,
        @RequestParam(required = false) String taskType,
        @RequestParam(required = false) String orderNo,
        PageQuery pageQuery) {
        return myTaskService.queryMyTasks(status, taskType, orderNo, pageQuery);
    }

    /**
     * 我的待办统计（看板用）
     *
     * @return pendingCount, doneCount, percent
     */
    @SaCheckPermission("wms:myTasks:list")
    @GetMapping("/summary")
    public com.ruoyi.common.core.domain.R<java.util.Map<String, Object>> summary() {
        return com.ruoyi.common.core.domain.R.ok(myTaskService.getMyTasksSummary());
    }
}
