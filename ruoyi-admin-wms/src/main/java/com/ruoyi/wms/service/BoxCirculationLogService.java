package com.ruoyi.wms.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.satoken.utils.LoginHelper;
import com.ruoyi.wms.domain.entity.BoxCirculationLog;
import com.ruoyi.wms.mapper.BoxCirculationLogMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class BoxCirculationLogService extends ServiceImpl<BoxCirculationLogMapper, BoxCirculationLog> {

    /**
     * 记录一条箱体流转事件
     */
    public void logEvent(Long boxId, String boxCode, String eventType,
                         Long relatedOrderId, String relatedOrderType,
                         String fromLocationName, String toLocationName,
                         String remark) {
        BoxCirculationLog log = new BoxCirculationLog();
        log.setBoxId(boxId);
        log.setBoxCode(boxCode);
        log.setEventType(eventType);
        log.setRelatedOrderId(relatedOrderId);
        log.setRelatedOrderType(relatedOrderType);
        log.setFromLocationName(fromLocationName);
        log.setToLocationName(toLocationName);
        log.setRemark(remark);
        log.setCreateBy(LoginHelper.isLogin() ? LoginHelper.getUsername() : "system");
        log.setCreateTime(LocalDateTime.now());
        baseMapper.insert(log);
    }
}
