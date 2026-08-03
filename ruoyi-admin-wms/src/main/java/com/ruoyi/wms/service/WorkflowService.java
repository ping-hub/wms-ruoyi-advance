package com.ruoyi.wms.service;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.common.satoken.utils.LoginHelper;
import com.ruoyi.wms.domain.entity.WorkflowLog;
import com.ruoyi.wms.domain.vo.WorkflowLogVo;
import com.ruoyi.wms.mapper.WorkflowLogMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * 工作流日志服务（阶段1精简版）
 * <p>
 * 阶段1仅保留日志写入和查询功能，流程步骤控制逻辑已移除。
 * 阶段2将重写为表驱动工作流引擎。
 */
@Service
@RequiredArgsConstructor
public class WorkflowService {

    private final WorkflowLogMapper workflowLogMapper;




    /**
     * 记录流程操作日志
     *
     * @param entityType 业务类型
     * @param entityId   单据ID
     * @param stepName   步骤标识
     * @param stepLabel  步骤显示名
     * @param remark     审批意见
     * @param result     操作结果
     */
    public void logOperation(String entityType, Long entityId, String stepName,
                             String stepLabel, String remark, String result) {
        WorkflowLog log = new WorkflowLog();
        log.setEntityType(entityType);
        log.setEntityId(entityId);
        log.setStepName(stepName);
        log.setStepLabel(stepLabel);
        log.setOperatorId(LoginHelper.getUserId());
        log.setOperatorName(LoginHelper.getUsername());
        log.setOperateTime(LocalDateTime.now());
        log.setRemark(remark);
        log.setResult(result);
        workflowLogMapper.insert(log);
    }

    /**
     * 查询单据的审批历史
     *
     * @param entityType 业务类型
     * @param entityId   单据ID
     * @return 操作日志列表
     */
    public List<WorkflowLogVo> getLogs(String entityType, Long entityId) {
        List<WorkflowLog> logs = workflowLogMapper.selectList(
            Wrappers.<WorkflowLog>lambdaQuery()
                .eq(WorkflowLog::getEntityType, entityType)
                .eq(WorkflowLog::getEntityId, entityId)
                .orderByAsc(WorkflowLog::getOperateTime)
        );
        if (CollUtil.isEmpty(logs)) {
            return Collections.emptyList();
        }
        return logs.stream().map(this::toVo).toList();
    }


    private WorkflowLogVo toVo(WorkflowLog log) {
        WorkflowLogVo vo = new WorkflowLogVo();
        vo.setId(log.getId());
        vo.setEntityType(log.getEntityType());
        vo.setEntityId(log.getEntityId());
        vo.setStepName(log.getStepName());
        vo.setStepLabel(log.getStepLabel());
        vo.setOperatorId(log.getOperatorId());
        vo.setOperatorName(log.getOperatorName());
        vo.setOperateTime(log.getOperateTime());
        vo.setRemark(log.getRemark());
        vo.setResult(log.getResult());
        return vo;
    }
}
