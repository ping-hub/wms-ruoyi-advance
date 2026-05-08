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
import org.springframework.transaction.annotation.Transactional;

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
    private final RackLocationPlannerService rackLocationPlannerService;

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

    @Transactional
    public void insertByBo(RackBo bo) {
        validateBoBeforeSave(bo);
        Rack rack = MapstructUtils.convert(bo, Rack.class);
        rackMapper.insert(rack);
        rackLocationPlannerService.generateLocationsForNewRack(rack);
    }

    @Transactional
    public void updateByBo(RackBo bo) {
        validateBoBeforeSave(bo);
        Rack beforeRack = rackMapper.selectById(bo.getId());
        Assert.notNull(beforeRack, "货架不存在");
        Rack afterRack = MapstructUtils.convert(bo, Rack.class);
        rackMapper.updateById(afterRack);
        rackLocationPlannerService.syncLocationsAfterRackUpdate(beforeRack, afterRack);
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
        validatePlanningFields(bo);
        validateRelationChange(bo);
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

    private void validatePlanningFields(RackBo bo) {
        Assert.notNull(bo.getRowCount(), "货架行数不能为空");
        Assert.notNull(bo.getColumnCount(), "货架列数不能为空");
        Assert.isTrue(bo.getRowCount() > 0, "货架行数必须大于0");
        Assert.isTrue(bo.getColumnCount() > 0, "货架列数必须大于0");
        if (bo.getLength() != null) {
            Assert.isTrue(bo.getLength().signum() > 0, "货架长度必须大于0");
        }
        if (bo.getWidth() != null) {
            Assert.isTrue(bo.getWidth().signum() > 0, "货架宽度必须大于0");
        }
        if (bo.getHeight() != null) {
            Assert.isTrue(bo.getHeight().signum() > 0, "货架高度必须大于0");
        }
    }

    private void validateRelationChange(RackBo bo) {
        if (bo.getId() == null) {
            return;
        }
        Rack existedRack = rackMapper.selectById(bo.getId());
        Assert.notNull(existedRack, "货架不存在");
        boolean warehouseChanged = !Objects.equals(existedRack.getWarehouseId(), bo.getWarehouseId());
        boolean areaChanged = !Objects.equals(existedRack.getAreaId(), bo.getAreaId());
        if ((warehouseChanged || areaChanged) && locationMapper.selectCount(
            Wrappers.<com.ruoyi.wms.domain.entity.Location>lambdaQuery().eq(com.ruoyi.wms.domain.entity.Location::getRackId, bo.getId())
        ) > 0) {
            throw new ServiceException("已规划货位的货架不支持直接变更所属仓库或库区，请先清理并重建");
        }
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
