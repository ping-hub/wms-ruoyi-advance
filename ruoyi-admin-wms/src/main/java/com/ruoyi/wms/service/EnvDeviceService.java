package com.ruoyi.wms.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.MapstructUtils;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.ruoyi.wms.domain.bo.EnvDeviceBo;
import com.ruoyi.wms.domain.entity.WmsEnvDevice;
import com.ruoyi.wms.domain.vo.EnvDeviceVo;
import com.ruoyi.wms.mapper.WmsEnvDeviceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 温湿度监控-设备配置 Service
 */
@RequiredArgsConstructor
@Service
public class EnvDeviceService extends ServiceImpl<WmsEnvDeviceMapper, WmsEnvDevice> {

    private final WmsEnvDeviceMapper envDeviceMapper;

    /**
     * 分页查询设备配置列表
     */
    public TableDataInfo<EnvDeviceVo> queryPageList(EnvDeviceBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<WmsEnvDevice> lqw = buildQueryWrapper(bo);
        Page<EnvDeviceVo> result = envDeviceMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询设备配置列表（不分页）
     */
    public List<EnvDeviceVo> queryList(EnvDeviceBo bo) {
        LambdaQueryWrapper<WmsEnvDevice> lqw = buildQueryWrapper(bo);
        return envDeviceMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<WmsEnvDevice> buildQueryWrapper(EnvDeviceBo bo) {
        LambdaQueryWrapper<WmsEnvDevice> lqw = Wrappers.lambdaQuery();
        lqw.eq(StrUtil.isNotBlank(bo.getDeviceId()), WmsEnvDevice::getDeviceId, bo.getDeviceId());
        lqw.like(StrUtil.isNotBlank(bo.getDeviceName()), WmsEnvDevice::getDeviceName, bo.getDeviceName());
        lqw.like(StrUtil.isNotBlank(bo.getWarehouse()), WmsEnvDevice::getWarehouse, bo.getWarehouse());
        lqw.eq(StrUtil.isNotBlank(bo.getDeviceType()), WmsEnvDevice::getDeviceType, bo.getDeviceType());
        lqw.eq(StrUtil.isNotBlank(bo.getLocation()), WmsEnvDevice::getLocation, bo.getLocation());
        lqw.eq(bo.getEnabled() != null, WmsEnvDevice::getEnabled, bo.getEnabled());
        lqw.orderByAsc(WmsEnvDevice::getId);
        return lqw;
    }

    /**
     * 查询启用的设备清单（定时任务轮询用）
     */
    public List<WmsEnvDevice> listEnabled() {
        return envDeviceMapper.selectList(Wrappers.<WmsEnvDevice>lambdaQuery()
            .eq(WmsEnvDevice::getEnabled, 1));
    }

    /**
     * 查询设备配置详情
     */
    public EnvDeviceVo queryById(Long id) {
        return envDeviceMapper.selectVoById(id);
    }

    /**
     * 新增设备配置
     */
    public void insertByBo(EnvDeviceBo bo) {
        validateDeviceIdUnique(bo);
        WmsEnvDevice add = MapstructUtils.convert(bo, WmsEnvDevice.class);
        if (add.getEnabled() == null) {
            add.setEnabled(1);
        }
        envDeviceMapper.insert(add);
    }

    /**
     * 修改设备配置
     */
    public void updateByBo(EnvDeviceBo bo) {
        validateDeviceIdUnique(bo);
        WmsEnvDevice update = MapstructUtils.convert(bo, WmsEnvDevice.class);
        envDeviceMapper.updateById(update);
    }

    /**
     * 启用/停用设备
     */
    public void changeStatus(Long id, Integer enabled) {
        envDeviceMapper.update(null, new LambdaUpdateWrapper<WmsEnvDevice>()
            .eq(WmsEnvDevice::getId, id)
            .set(WmsEnvDevice::getEnabled, enabled));
    }

    /**
     * 批量删除设备配置
     */
    public void deleteByIds(List<Long> ids) {
        envDeviceMapper.deleteBatchIds(ids);
    }

    /**
     * 校验设备ID唯一（device_id 与外部数据关联，不可重复）
     */
    private void validateDeviceIdUnique(EnvDeviceBo bo) {
        if (StrUtil.isBlank(bo.getDeviceId())) {
            return;
        }
        Long count = envDeviceMapper.selectCount(Wrappers.<WmsEnvDevice>lambdaQuery()
            .eq(WmsEnvDevice::getDeviceId, bo.getDeviceId())
            .ne(bo.getId() != null, WmsEnvDevice::getId, bo.getId()));
        if (count != null && count > 0) {
            throw new ServiceException("设备ID[" + bo.getDeviceId() + "]已存在，不能重复配置");
        }
    }
}
