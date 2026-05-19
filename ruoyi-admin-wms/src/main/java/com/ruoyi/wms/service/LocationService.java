package com.ruoyi.wms.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.ruoyi.wms.domain.bo.BoxBo;
import com.ruoyi.wms.domain.bo.ItemInstanceBo;
import com.ruoyi.wms.domain.bo.LocationBo;
import com.ruoyi.wms.domain.entity.Area;
import com.ruoyi.wms.domain.entity.Box;
import com.ruoyi.wms.domain.entity.ItemInstance;
import com.ruoyi.wms.domain.entity.Location;
import com.ruoyi.wms.domain.entity.Rack;
import com.ruoyi.wms.domain.entity.Warehouse;
import com.ruoyi.wms.domain.vo.BoxVo;
import com.ruoyi.wms.domain.vo.ItemInstanceVo;
import com.ruoyi.wms.domain.vo.LocationItemSummaryVo;
import com.ruoyi.wms.domain.vo.LocationHealthCheckResultVo;
import com.ruoyi.wms.domain.vo.LocationRebuildResultVo;
import com.ruoyi.wms.domain.vo.LocationSummaryVo;
import com.ruoyi.wms.domain.vo.LocationStockVo;
import com.ruoyi.wms.domain.vo.LocationVo;
import com.ruoyi.wms.domain.vo.RackGridCellVo;
import com.ruoyi.wms.domain.vo.RackGridVo;
import com.ruoyi.wms.domain.vo.RackVo;
import com.ruoyi.wms.mapper.AreaMapper;
import com.ruoyi.wms.mapper.LocationMapper;
import com.ruoyi.wms.mapper.RackMapper;
import com.ruoyi.wms.mapper.WarehouseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
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
    private final RackLocationPlannerService rackLocationPlannerService;

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
        throw new com.ruoyi.common.core.exception.ServiceException("货位由货架自动规划生成，不支持普通手工新增，请通过货架维护或重建功能生成");
    }

    public void updateByBo(LocationBo bo) {
        validateBoBeforeUpdate(bo);
        Location existed = locationMapper.selectById(bo.getId());
        Assert.notNull(existed, "货位不存在");
        Location update = new Location();
        update.setId(bo.getId());
        update.setLocationStatus(bo.getLocationStatus());
        update.setLength(bo.getLength());
        update.setWidth(bo.getWidth());
        update.setHeight(bo.getHeight());
        update.setOccupiedFlag(bo.getOccupiedFlag());
        update.setSortNo(bo.getSortNo());
        update.setRemark(bo.getRemark());
        locationMapper.updateById(update);
    }

    public void deleteById(Long id) {
        validateBeforeDelete(id);
        locationMapper.deleteById(id);
    }

    public LocationRebuildResultVo rebuildByRack(Long rackId) {
        return rackLocationPlannerService.rebuildByRack(rackId);
    }

    public LocationHealthCheckResultVo healthCheckByRack(Long rackId) {
        return rackLocationPlannerService.healthCheckByRack(rackId);
    }

    public LocationStockVo queryStockById(Long id) {
        LocationVo locationVo = queryById(id);
        Assert.notNull(locationVo, "货位不存在");
        ItemInstanceBo itemInstanceBo = new ItemInstanceBo();
        itemInstanceBo.setLocationId(id);
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

    public RackGridVo queryRackGrid(Long rackId) {
        RackVo rackVo = rackMapper.selectVoById(rackId);
        Assert.notNull(rackVo, "货架不存在");
        LocationBo locationBo = new LocationBo();
        locationBo.setRackId(rackId);
        List<LocationVo> locations = queryList(locationBo);

        BoxBo boxBo = new BoxBo();
        boxBo.setRackId(rackId);
        List<BoxVo> boxes = boxService.queryList(boxBo);

        ItemInstanceBo itemInstanceBo = new ItemInstanceBo();
        itemInstanceBo.setRackId(rackId);
        List<ItemInstanceVo> itemInstances = itemInstanceService.queryList(itemInstanceBo);

        Map<Long, Integer> boxCountMap = boxes.stream()
            .filter(box -> box.getLocationId() != null)
            .collect(Collectors.toMap(BoxVo::getLocationId, box -> 1, Integer::sum));
        Map<Long, Integer> totalItemCountMap = itemInstances.stream()
            .filter(item -> item.getLocationId() != null)
            .collect(Collectors.toMap(ItemInstanceVo::getLocationId, item -> 1, Integer::sum));
        Map<Long, Integer> directItemCountMap = itemInstances.stream()
            .filter(item -> item.getLocationId() != null && item.getBoxId() == null)
            .collect(Collectors.toMap(ItemInstanceVo::getLocationId, item -> 1, Integer::sum));

        RackGridVo gridVo = new RackGridVo();
        gridVo.setRackId(rackVo.getId());
        gridVo.setRackCode(rackVo.getRackCode());
        gridVo.setRackName(rackVo.getRackName());
        gridVo.setWarehouseId(rackVo.getWarehouseId());
        gridVo.setWarehouseName(rackVo.getWarehouseName());
        gridVo.setAreaId(rackVo.getAreaId());
        gridVo.setAreaName(rackVo.getAreaName());
        gridVo.setRowCount(rackVo.getRowCount());
        gridVo.setColumnCount(rackVo.getColumnCount());
        gridVo.setLength(rackVo.getLength());
        gridVo.setWidth(rackVo.getWidth());
        gridVo.setHeight(rackVo.getHeight());
        gridVo.setOrderNum(rackVo.getOrderNum());
        gridVo.setCells(locations.stream()
            .sorted(java.util.Comparator.comparing(LocationVo::getRowNo, java.util.Comparator.nullsLast(Integer::compareTo))
                .thenComparing(LocationVo::getColumnNo, java.util.Comparator.nullsLast(Integer::compareTo))
                .thenComparing(LocationVo::getSortNo, java.util.Comparator.nullsLast(Long::compareTo))
                .thenComparing(LocationVo::getId))
            .map(location -> {
                RackGridCellVo cellVo = new RackGridCellVo();
                cellVo.setRowNo(location.getRowNo());
                cellVo.setColumnNo(location.getColumnNo());
                cellVo.setLocationId(location.getId());
                cellVo.setLocationCode(location.getLocationCode());
                cellVo.setLocationName(location.getLocationName());
                cellVo.setLocationStatus(location.getLocationStatus());
                cellVo.setOccupiedFlag(location.getOccupiedFlag());
                cellVo.setBoxCount(boxCountMap.getOrDefault(location.getId(), 0));
                cellVo.setDirectItemCount(directItemCountMap.getOrDefault(location.getId(), 0));
                cellVo.setItemInstanceCount(totalItemCountMap.getOrDefault(location.getId(), 0));
                return cellVo;
            })
            .toList());
        return gridVo;
    }

    public LocationSummaryVo querySummaryById(Long id) {
        LocationVo locationVo = queryById(id);
        Assert.notNull(locationVo, "货位不存在");

        BoxBo boxBo = new BoxBo();
        boxBo.setLocationId(id);
        List<BoxVo> boxes = boxService.queryList(boxBo);

        ItemInstanceBo itemInstanceBo = new ItemInstanceBo();
        itemInstanceBo.setLocationId(id);
        List<ItemInstanceVo> itemInstances = itemInstanceService.queryList(itemInstanceBo);

        LocationSummaryVo summaryVo = new LocationSummaryVo();
        summaryVo.setLocationId(locationVo.getId());
        summaryVo.setLocationCode(locationVo.getLocationCode());
        summaryVo.setLocationName(locationVo.getLocationName());
        summaryVo.setWarehouseId(locationVo.getWarehouseId());
        summaryVo.setWarehouseName(locationVo.getWarehouseName());
        summaryVo.setAreaId(locationVo.getAreaId());
        summaryVo.setAreaName(locationVo.getAreaName());
        summaryVo.setRackId(locationVo.getRackId());
        summaryVo.setRackName(locationVo.getRackName());
        summaryVo.setLocationStatus(locationVo.getLocationStatus());
        summaryVo.setRowNo(locationVo.getRowNo());
        summaryVo.setColumnNo(locationVo.getColumnNo());
        summaryVo.setLength(locationVo.getLength());
        summaryVo.setWidth(locationVo.getWidth());
        summaryVo.setHeight(locationVo.getHeight());
        summaryVo.setOccupiedFlag(locationVo.getOccupiedFlag());
        summaryVo.setBoxCount(boxes.size());
        summaryVo.setItemInstanceCount(itemInstances.size());
        summaryVo.setDirectItemCount((int) itemInstances.stream()
            .filter(item -> item.getBoxId() == null)
            .count());
        summaryVo.setBoxes(boxes.stream()
            .sorted(Comparator.comparing(BoxVo::getBoxCode, Comparator.nullsLast(String::compareTo)))
            .toList());
        summaryVo.setItemInstances(itemInstances.stream()
            .sorted(Comparator.comparing(ItemInstanceVo::getInstanceCode, Comparator.nullsLast(String::compareTo)))
            .toList());
        summaryVo.setItemSummaries(itemInstances.stream()
            .collect(Collectors.groupingBy(
                item -> item.getItemId() + "_" + item.getSkuId(),
                Collectors.toList()
            ))
            .values()
            .stream()
            .map(items -> {
                ItemInstanceVo first = items.get(0);
                LocationItemSummaryVo itemSummaryVo = new LocationItemSummaryVo();
                itemSummaryVo.setItemId(first.getItemId());
                itemSummaryVo.setItemName(first.getItemName());
                itemSummaryVo.setSkuId(first.getSkuId());
                itemSummaryVo.setSkuName(first.getSkuName());
                itemSummaryVo.setQuantity(items.size());
                return itemSummaryVo;
            })
            .sorted(java.util.Comparator.comparing(LocationItemSummaryVo::getItemName, java.util.Comparator.nullsLast(String::compareTo))
                .thenComparing(LocationItemSummaryVo::getSkuName, java.util.Comparator.nullsLast(String::compareTo)))
            .toList());
        return summaryVo;
    }

    public void refreshOccupiedFlagsByLocationIds(Collection<Long> locationIds) {
        if (CollUtil.isEmpty(locationIds)) {
            return;
        }
        Set<Long> ids = locationIds.stream()
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
        if (CollUtil.isEmpty(ids)) {
            return;
        }

        Map<Long, Integer> boxCountMap = boxService.list(Wrappers.lambdaQuery(Box.class)
                .in(Box::getLocationId, ids))
            .stream()
            .filter(box -> box.getLocationId() != null)
            .collect(Collectors.toMap(Box::getLocationId, box -> 1, Integer::sum));

        Map<Long, Integer> itemCountMap = itemInstanceService.list(Wrappers.lambdaQuery(ItemInstance.class)
                .in(ItemInstance::getLocationId, ids))
            .stream()
            .filter(item -> item.getLocationId() != null)
            .collect(Collectors.toMap(ItemInstance::getLocationId, item -> 1, Integer::sum));

        Set<Long> occupiedIds = ids.stream()
            .filter(id -> boxCountMap.getOrDefault(id, 0) + itemCountMap.getOrDefault(id, 0) > 0)
            .collect(Collectors.toSet());
        Set<Long> freeIds = ids.stream()
            .filter(id -> !occupiedIds.contains(id))
            .collect(Collectors.toSet());

        if (CollUtil.isNotEmpty(occupiedIds)) {
            locationMapper.update(null, Wrappers.lambdaUpdate(Location.class)
                .in(Location::getId, occupiedIds)
                .set(Location::getOccupiedFlag, 1));
        }
        if (CollUtil.isNotEmpty(freeIds)) {
            locationMapper.update(null, Wrappers.lambdaUpdate(Location.class)
                .in(Location::getId, freeIds)
                .set(Location::getOccupiedFlag, 0));
        }
    }

    private LambdaQueryWrapper<Location> buildQueryWrapper(LocationBo bo) {
        LambdaQueryWrapper<Location> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getId() != null, Location::getId, bo.getId());
        lqw.eq(StrUtil.isNotBlank(bo.getLocationCode()), Location::getLocationCode, bo.getLocationCode());
        lqw.like(StrUtil.isNotBlank(bo.getLocationName()), Location::getLocationName, bo.getLocationName());
        lqw.eq(bo.getWarehouseId() != null, Location::getWarehouseId, bo.getWarehouseId());
        lqw.eq(bo.getAreaId() != null, Location::getAreaId, bo.getAreaId());
        lqw.eq(bo.getRackId() != null, Location::getRackId, bo.getRackId());
        lqw.eq(StrUtil.isNotBlank(bo.getLocationStatus()), Location::getLocationStatus, bo.getLocationStatus());
        lqw.eq(bo.getRowNo() != null, Location::getRowNo, bo.getRowNo());
        lqw.eq(bo.getColumnNo() != null, Location::getColumnNo, bo.getColumnNo());
        lqw.eq(bo.getOccupiedFlag() != null, Location::getOccupiedFlag, bo.getOccupiedFlag());
        lqw.orderByAsc(Location::getSortNo).orderByDesc(Location::getCreateTime);
        return lqw;
    }

    private void validateBoBeforeUpdate(LocationBo bo) {
        validateLocationRelation(bo);
        validateMaintenanceBoundary(bo);
        validateLocationDimensions(bo);
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

    private void validateMaintenanceBoundary(LocationBo bo) {
        Location existed = locationMapper.selectById(bo.getId());
        Assert.notNull(existed, "货位不存在");
        Assert.isTrue(Objects.equals(existed.getWarehouseId(), bo.getWarehouseId()), "货位所属仓库不允许手工修改");
        Assert.isTrue(Objects.equals(existed.getAreaId(), bo.getAreaId()), "货位所属库区不允许手工修改");
        Assert.isTrue(Objects.equals(existed.getRackId(), bo.getRackId()), "货位所属货架不允许手工修改");
        Assert.isTrue(Objects.equals(existed.getRowNo(), bo.getRowNo()), "货位行号不允许手工修改");
        Assert.isTrue(Objects.equals(existed.getColumnNo(), bo.getColumnNo()), "货位列号不允许手工修改");
        Assert.isTrue(Objects.equals(existed.getLocationCode(), bo.getLocationCode()), "货位编码不允许手工修改");
        Assert.isTrue(Objects.equals(existed.getLocationName(), bo.getLocationName()), "货位名称不允许手工修改");
    }

    private void validateLocationDimensions(LocationBo bo) {
        if (bo.getLength() != null) {
            Assert.isTrue(bo.getLength().signum() > 0, "货位长度必须大于0");
        }
        if (bo.getWidth() != null) {
            Assert.isTrue(bo.getWidth().signum() > 0, "货位宽度必须大于0");
        }
        if (bo.getHeight() != null) {
            Assert.isTrue(bo.getHeight().signum() > 0, "货位高度必须大于0");
        }
    }

    private void validateBeforeDelete(Long id) {
        Assert.notNull(locationMapper.selectById(id), "货位不存在");
        ItemInstanceBo itemInstanceBo = new ItemInstanceBo();
        itemInstanceBo.setLocationId(id);
        Assert.isTrue(CollUtil.isEmpty(itemInstanceService.queryList(itemInstanceBo)), "货位下仍有单品实例占用，无法删除");
        Assert.isTrue(CollUtil.isEmpty(boxService.queryByLocationId(id)), "货位下仍有箱体占用，无法删除");
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
        Map<Long, Warehouse> warehouseMap = warehouseIds.isEmpty() ? java.util.Collections.emptyMap() :
            warehouseMapper.selectBatchIds(warehouseIds).stream().collect(Collectors.toMap(Warehouse::getId, Function.identity()));
        Map<Long, Area> areaMap = areaIds.isEmpty() ? java.util.Collections.emptyMap() :
            areaMapper.selectBatchIds(areaIds).stream().collect(Collectors.toMap(Area::getId, Function.identity()));
        Map<Long, Rack> rackMap = rackIds.isEmpty() ? java.util.Collections.emptyMap() :
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
