package com.ruoyi.wms.service;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.wms.domain.entity.CheckOrderInstance;
import com.ruoyi.wms.domain.vo.CheckOrderInstanceVo;
import com.ruoyi.wms.mapper.CheckOrderInstanceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 盘点实例差异明细Service
 *
 * @author ping
 * @date 2026-06-12
 */
@RequiredArgsConstructor
@Service
public class CheckOrderInstanceService extends ServiceImpl<CheckOrderInstanceMapper, CheckOrderInstance> {

    /**
     * 按盘点单ID查询实例差异明细
     */
    public List<CheckOrderInstanceVo> queryByCheckOrderId(Long checkOrderId) {
        LambdaQueryWrapper<CheckOrderInstance> lqw = Wrappers.lambdaQuery();
        lqw.eq(CheckOrderInstance::getCheckOrderId, checkOrderId);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 批量保存实例差异明细
     */
    @Transactional
    public void saveInstances(List<CheckOrderInstance> list) {
        if (CollUtil.isEmpty(list)) {
            return;
        }
        saveBatch(list);
    }

    /**
     * 按盘点单ID删除实例差异明细
     */
    @Transactional
    public void deleteByCheckOrderIds(Collection<Long> checkOrderIds) {
        if (CollUtil.isEmpty(checkOrderIds)) {
            return;
        }
        LambdaQueryWrapper<CheckOrderInstance> lqw = Wrappers.lambdaQuery();
        lqw.in(CheckOrderInstance::getCheckOrderId, checkOrderIds);
        baseMapper.delete(lqw);
    }
}
