package com.ruoyi.wms.service;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.satoken.utils.LoginHelper;
import com.ruoyi.wms.domain.entity.WorkflowDef;
import com.ruoyi.wms.domain.entity.WorkflowLog;
import com.ruoyi.wms.domain.vo.WorkflowLogVo;
import com.ruoyi.wms.mapper.WorkflowDefMapper;
import com.ruoyi.wms.mapper.WorkflowLogMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * 审批流程中心服务（类比 CodeRuleService）
 * <p>
 * 核心逻辑：
 * 1. 查 wms_workflow_def 找到当前状态可执行的步骤
 * 2. 校验权限 + 业务约束
 * 3. 执行状态变更 + 写日志
 * <p>
 * 未来升级为自定义流程时，只需修改 wms_workflow_def 数据 + 加配置页面，
 * 本服务零改动。
 */
@Service
@RequiredArgsConstructor
public class WorkflowService {

    private final WorkflowDefMapper workflowDefMapper;
    private final WorkflowLogMapper workflowLogMapper;

    /**
     * 查询当前状态可执行的操作步骤
     *
     * @param entityType    业务类型
     * @param currentStatus 当前状态
     * @return 可用步骤列表
     */
    public List<WorkflowDef> getAvailableSteps(String entityType, Integer currentStatus) {
        return workflowDefMapper.selectList(
            Wrappers.<WorkflowDef>lambdaQuery()
                .eq(WorkflowDef::getEntityType, entityType)
                .eq(WorkflowDef::getFromStatus, currentStatus)
                .eq(WorkflowDef::getEnabled, 1)
                .orderByAsc(WorkflowDef::getStepOrder)
        );
    }

    /**
     * 查找指定步骤定义
     *
     * @param entityType 业务类型
     * @param stepName   步骤标识
     * @param fromStatus 当前状态
     * @return 步骤定义
     */
    public WorkflowDef findStep(String entityType, String stepName, Integer fromStatus) {
        return workflowDefMapper.selectOne(
            Wrappers.<WorkflowDef>lambdaQuery()
                .eq(WorkflowDef::getEntityType, entityType)
                .eq(WorkflowDef::getStepName, stepName)
                .eq(WorkflowDef::getFromStatus, fromStatus)
                .eq(WorkflowDef::getEnabled, 1)
        );
    }

    /**
     * 获取流程执行后目标状态
     *
     * @param entityType    业务类型
     * @param stepName      步骤标识
     * @param currentStatus 当前状态
     * @return 目标状态
     */
    public Integer getTargetStatus(String entityType, String stepName, Integer currentStatus) {
        WorkflowDef step = findStep(entityType, stepName, currentStatus);
        if (step == null) {
            throw new ServiceException("当前状态不支持该操作");
        }
        return step.getToStatus();
    }

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

    /**
     * 校验审批人不能是申请人（防止自己审批自己）
     *
     * @param applicantId 申请人ID
     */
    public void validateNotSelfApproval(Long applicantId) {
        if (applicantId != null && applicantId.equals(LoginHelper.getUserId())) {
            throw new ServiceException("审批人不能审批自己提交的单据");
        }
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
