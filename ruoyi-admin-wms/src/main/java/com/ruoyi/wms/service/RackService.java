package com.ruoyi.wms.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.MapstructUtils;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.ruoyi.wms.domain.bo.RackBo;
import com.ruoyi.wms.domain.entity.Area;
import com.ruoyi.wms.domain.entity.Rack;
import com.ruoyi.wms.domain.entity.Warehouse;
import com.ruoyi.wms.domain.vo.RackVo;
import com.ruoyi.wms.mapper.AreaMapper;
import com.ruoyi.wms.mapper.LocationMapper;
import com.ruoyi.wms.mapper.RackMapper;
import com.ruoyi.wms.mapper.WarehouseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RackService extends ServiceImpl<RackMapper, Rack> {

    private final RackMapper rackMapper;
    private final AreaMapper areaMapper;
    private final WarehouseMapper warehouseMapper;
    private final LocationMapper locationMapper;

    public RackVo queryById(Long id) {
        RackVo rackVo = rackMapper.selectVoById(id);
        enrich(List.of(rackVo));
        return rackVo;
    }

    public TableDataInfo<RackVo> queryPageList(RackBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<Rack> lqw = buildQueryWrapper(bo);
        Page<RackVo> result = rackMapper.selectVoPage(pageQuery.build(), lqw);
        enrich(result.getRecords());
        return TableDataInfo.build(result);
    }

    public List<RackVo> queryList(RackBo bo) {
        LambdaQueryWrapper<Rack> lqw = buildQueryWrapper(bo);
        List<RackVo> list = rackMapper.selectVoList(lqw);
        enrich(list);
        return list;
    }

    public void insertByBo(RackBo bo) {
        validateBoBeforeSave(bo);
        rackMapper.insert(MapstructUtils.convert(bo, Rack.class));
    }

    public void updateByBo(RackBo bo) {
        validateBoBeforeSave(bo);
        rackMapper.updateById(MapstructUtils.convert(bo, Rack.class));
    }

    public void deleteById(Long id) {
        LambdaQueryWrapper<com.ruoyi.wms.domain.entity.Location> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(com.ruoyi.wms.domain.entity.Location::getRackId, id);
        if (locationMapper.selectCount(queryWrapper) > 0) {
            throw new ServiceException("货架下已存在货位，无法删除！", HttpStatus.CONFLICT.value());
        }
        rackMapper.deleteById(id);
    }

    public void deleteByIds(Collection<Long> ids) {
        rackMapper.deleteBatchIds(ids);
    }

    private LambdaQueryWrapper<Rack> buildQueryWrapper(RackBo bo) {
        LambdaQueryWrapper<Rack> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getId() != null, Rack::getId, bo.getId());
        lqw.eq(StrUtil.isNotBlank(bo.getRackCode()), Rack::getRackCode, bo.getRackCode());
        lqw.like(StrUtil.isNotBlank(bo.getRackName()), Rack::getRackName, bo.getRackName());
        lqw.eq(bo.getWarehouseId() != null, Rack::getWarehouseId, bo.getWarehouseId());
        lqw.eq(bo.getAreaId() != null, Rack::getAreaId, bo.getAreaId());
        lqw.eq(StrUtil.isNotBlank(bo.getRackStatus()), Rack::getRackStatus, bo.getRackStatus());
        lqw.eq(StrUtil.isNotBlank(bo.getRackType()), Rack::getRackType, bo.getRackType());
        lqw.orderByAsc(Rack::getOrderNum).orderByDesc(Rack::getCreateTime);
        return lqw;
    }

    private void validateBoBeforeSave(RackBo bo) {
        validateRackRelation(bo);
        validateRackNameAndCode(bo);
    }

    private void validateRackRelation(RackBo bo) {
        Area area = areaMapper.selectById(bo.getAreaId());
        Assert.notNull(area, "所属库区不存在");
        Assert.isTrue(Objects.equals(area.getWarehouseId(), bo.getWarehouseId()), "货架所属库区与仓库不匹配");
    }

    private void validateRackNameAndCode(RackBo bo) {
        LambdaQueryWrapper<Rack> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Rack::getAreaId, bo.getAreaId());
        queryWrapper.eq(Rack::getRackName, bo.getRackName());
        queryWrapper.ne(bo.getId() != null, Rack::getId, bo.getId());
        Assert.isTrue(rackMapper.selectCount(queryWrapper) == 0, "同一库区下货架名称重复");
        if (StrUtil.isBlank(bo.getRackCode())) {
            return;
        }
        queryWrapper.clear();
        queryWrapper.eq(Rack::getWarehouseId, bo.getWarehouseId());
        queryWrapper.eq(Rack::getAreaId, bo.getAreaId());
        queryWrapper.eq(Rack::getRackCode, bo.getRackCode());
        queryWrapper.ne(bo.getId() != null, Rack::getId, bo.getId());
        Assert.isTrue(rackMapper.selectCount(queryWrapper) == 0, "货架编码重复");
    }

    private void enrich(List<RackVo> list) {
        if (CollUtil.isEmpty(list)) {
            return;
        }
        List<RackVo> validList = list.stream().filter(Objects::nonNull).toList();
        if (CollUtil.isEmpty(validList)) {
            return;
        }
        Set<Long> warehouseIds = validList.stream().map(RackVo::getWarehouseId).filter(Objects::nonNull).collect(Collectors.toSet());
        Set<Long> areaIds = validList.stream().map(RackVo::getAreaId).filter(Objects::nonNull).collect(Collectors.toSet());
        Map<Long, Warehouse> warehouseMap = warehouseIds.isEmpty() ? Map.of() :
            warehouseMapper.selectBatchIds(warehouseIds).stream().collect(Collectors.toMap(Warehouse::getId, Function.identity()));
        Map<Long, Area> areaMap = areaIds.isEmpty() ? Map.of() :
            areaMapper.selectBatchIds(areaIds).stream().collect(Collectors.toMap(Area::getId, Function.identity()));
        validList.forEach(rackVo -> {
            Warehouse warehouse = warehouseMap.get(rackVo.getWarehouseId());
            if (warehouse != null) {
                rackVo.setWarehouseName(warehouse.getWarehouseName());
            }
            Area area = areaMap.get(rackVo.getAreaId());
            if (area != null) {
                rackVo.setAreaName(area.getAreaName());
            }
        });
    }
}
