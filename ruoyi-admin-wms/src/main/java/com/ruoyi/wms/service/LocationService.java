package com.ruoyi.wms.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.utils.MapstructUtils;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.ruoyi.wms.domain.bo.ItemInstanceBo;
import com.ruoyi.wms.domain.bo.LocationBo;
import com.ruoyi.wms.domain.entity.Area;
import com.ruoyi.wms.domain.entity.Location;
import com.ruoyi.wms.domain.entity.Rack;
import com.ruoyi.wms.domain.entity.Warehouse;
import com.ruoyi.wms.domain.vo.BoxVo;
import com.ruoyi.wms.domain.vo.ItemInstanceVo;
import com.ruoyi.wms.domain.vo.LocationStockVo;
import com.ruoyi.wms.domain.vo.LocationVo;
import com.ruoyi.wms.mapper.AreaMapper;
import com.ruoyi.wms.mapper.LocationMapper;
import com.ruoyi.wms.mapper.RackMapper;
import com.ruoyi.wms.mapper.WarehouseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class LocationService extends ServiceImpl<LocationMapper, Location> {

    private final LocationMapper locationMapper;
    private final RackMapper rackMapper;
    private final AreaMapper areaMapper;
    private final WarehouseMapper warehouseMapper;
    private final ItemInstanceService itemInstanceService;
    private final BoxService boxService;

    public LocationVo queryById(Long id) {
        LocationVo locationVo = locationMapper.selectVoById(id);
        enrich(List.of(locationVo));
        return locationVo;
    }

    public TableDataInfo<LocationVo> queryPageList(LocationBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<Location> lqw = buildQueryWrapper(bo);
        Page<LocationVo> result = locationMapper.selectVoPage(pageQuery.build(), lqw);
        enrich(result.getRecords());
        return TableDataInfo.build(result);
    }

    public List<LocationVo> queryList(LocationBo bo) {
        LambdaQueryWrapper<Location> lqw = buildQueryWrapper(bo);
        List<LocationVo> list = locationMapper.selectVoList(lqw);
        enrich(list);
        return list;
    }

    public void insertByBo(LocationBo bo) {
        validateBoBeforeSave(bo);
        locationMapper.insert(MapstructUtils.convert(bo, Location.class));
    }

    public void updateByBo(LocationBo bo) {
        validateBoBeforeSave(bo);
        locationMapper.updateById(MapstructUtils.convert(bo, Location.class));
    }

    public void deleteById(Long id) {
        locationMapper.deleteById(id);
    }

    public LocationStockVo queryStockById(Long id) {
        LocationVo locationVo = queryById(id);
        Assert.notNull(locationVo, "货位不存在");
        ItemInstanceBo itemInstanceBo = new ItemInstanceBo();
        itemInstanceBo.setLocationId(id);
        itemInstanceBo.setInBox(0);
        List<ItemInstanceVo> itemInstances = itemInstanceService.queryList(itemInstanceBo);
        List<BoxVo> boxes = boxService.queryByLocationId(id);
        LocationStockVo stockVo = new LocationStockVo();
        stockVo.setLocationId(locationVo.getId());
        stockVo.setLocationCode(locationVo.getLocationCode());
        stockVo.setLocationName(locationVo.getLocationName());
        stockVo.setWarehouseId(locationVo.getWarehouseId());
        stockVo.setWarehouseName(locationVo.getWarehouseName());
        stockVo.setAreaId(locationVo.getAreaId());
        stockVo.setAreaName(locationVo.getAreaName());
        stockVo.setRackId(locationVo.getRackId());
        stockVo.setRackName(locationVo.getRackName());
        stockVo.setDirectItemCount(itemInstances.size());
        stockVo.setBoxCount(boxes.size());
        stockVo.setItemInstances(itemInstances);
        stockVo.setBoxes(boxes);
        return stockVo;
    }

    private LambdaQueryWrapper<Location> buildQueryWrapper(LocationBo bo) {
        LambdaQueryWrapper<Location> lqw = Wrappers.lambdaQuery();
        lqw.eq(StrUtil.isNotBlank(bo.getLocationCode()), Location::getLocationCode, bo.getLocationCode());
        lqw.like(StrUtil.isNotBlank(bo.getLocationName()), Location::getLocationName, bo.getLocationName());
        lqw.eq(bo.getWarehouseId() != null, Location::getWarehouseId, bo.getWarehouseId());
        lqw.eq(bo.getAreaId() != null, Location::getAreaId, bo.getAreaId());
        lqw.eq(bo.getRackId() != null, Location::getRackId, bo.getRackId());
        lqw.eq(StrUtil.isNotBlank(bo.getLocationStatus()), Location::getLocationStatus, bo.getLocationStatus());
        lqw.eq(StrUtil.isNotBlank(bo.getLocationType()), Location::getLocationType, bo.getLocationType());
        lqw.orderByDesc(Location::getCreateTime);
        return lqw;
    }

    private void validateBoBeforeSave(LocationBo bo) {
        validateLocationRelation(bo);
        validateLocationNameAndCode(bo);
    }

    private void validateLocationRelation(LocationBo bo) {
        Area area = areaMapper.selectById(bo.getAreaId());
        Assert.notNull(area, "所属库区不存在");
        Assert.isTrue(Objects.equals(area.getWarehouseId(), bo.getWarehouseId()), "货位所属库区与仓库不匹配");
        Rack rack = rackMapper.selectById(bo.getRackId());
        Assert.notNull(rack, "所属货架不存在");
        Assert.isTrue(Objects.equals(rack.getWarehouseId(), bo.getWarehouseId()), "货位所属货架与仓库不匹配");
        Assert.isTrue(Objects.equals(rack.getAreaId(), bo.getAreaId()), "货位所属货架与库区不匹配");
    }

    private void validateLocationNameAndCode(LocationBo bo) {
        LambdaQueryWrapper<Location> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Location::getRackId, bo.getRackId());
        queryWrapper.eq(Location::getLocationName, bo.getLocationName());
        queryWrapper.ne(bo.getId() != null, Location::getId, bo.getId());
        Assert.isTrue(locationMapper.selectCount(queryWrapper) == 0, "同一货架下货位名称重复");
        if (StrUtil.isBlank(bo.getLocationCode())) {
            return;
        }
        queryWrapper.clear();
        queryWrapper.eq(Location::getLocationCode, bo.getLocationCode());
        queryWrapper.ne(bo.getId() != null, Location::getId, bo.getId());
        Assert.isTrue(locationMapper.selectCount(queryWrapper) == 0, "货位编码重复");
    }

    private void enrich(List<LocationVo> list) {
        if (CollUtil.isEmpty(list)) {
            return;
        }
        List<LocationVo> validList = list.stream().filter(Objects::nonNull).toList();
        if (CollUtil.isEmpty(validList)) {
            return;
        }
        Set<Long> warehouseIds = validList.stream().map(LocationVo::getWarehouseId).filter(Objects::nonNull).collect(Collectors.toSet());
        Set<Long> areaIds = validList.stream().map(LocationVo::getAreaId).filter(Objects::nonNull).collect(Collectors.toSet());
        Set<Long> rackIds = validList.stream().map(LocationVo::getRackId).filter(Objects::nonNull).collect(Collectors.toSet());
        Map<Long, Warehouse> warehouseMap = warehouseIds.isEmpty() ? Map.of() :
            warehouseMapper.selectBatchIds(warehouseIds).stream().collect(Collectors.toMap(Warehouse::getId, Function.identity()));
        Map<Long, Area> areaMap = areaIds.isEmpty() ? Map.of() :
            areaMapper.selectBatchIds(areaIds).stream().collect(Collectors.toMap(Area::getId, Function.identity()));
        Map<Long, Rack> rackMap = rackIds.isEmpty() ? Map.of() :
            rackMapper.selectBatchIds(rackIds).stream().collect(Collectors.toMap(Rack::getId, Function.identity()));
        validList.forEach(locationVo -> {
            Warehouse warehouse = warehouseMap.get(locationVo.getWarehouseId());
            if (warehouse != null) {
                locationVo.setWarehouseName(warehouse.getWarehouseName());
            }
            Area area = areaMap.get(locationVo.getAreaId());
            if (area != null) {
                locationVo.setAreaName(area.getAreaName());
            }
            Rack rack = rackMap.get(locationVo.getRackId());
            if (rack != null) {
                locationVo.setRackCode(rack.getRackCode());
                locationVo.setRackName(rack.getRackName());
            }
        });
    }
}
