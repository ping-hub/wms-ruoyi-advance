package com.ruoyi.wms.service;

import cn.hutool.core.collection.CollUtil;
import com.ruoyi.wms.domain.entity.Area;
import com.ruoyi.wms.domain.entity.Location;
import com.ruoyi.wms.domain.entity.Rack;
import com.ruoyi.wms.domain.entity.Warehouse;
import com.ruoyi.wms.domain.vo.StorageLayoutNodeVo;
import com.ruoyi.wms.mapper.AreaMapper;
import com.ruoyi.wms.mapper.LocationMapper;
import com.ruoyi.wms.mapper.RackMapper;
import com.ruoyi.wms.mapper.WarehouseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StorageLayoutQueryService {

    private final WarehouseMapper warehouseMapper;
    private final AreaMapper areaMapper;
    private final RackMapper rackMapper;
    private final LocationMapper locationMapper;

    /**
     * 批量查询仓储布局树（4次查询代替 N+1）
     */
    public List<StorageLayoutNodeVo> queryLayoutTree(Long warehouseId, Long areaId, Long rackId) {
        // 1. 批量查所有仓库
        List<Warehouse> allWarehouses = warehouseMapper.selectList(null);
        if (CollUtil.isEmpty(allWarehouses)) {
            return List.of();
        }
        // 按过滤条件筛选仓库
        if (warehouseId != null) {
            allWarehouses = allWarehouses.stream().filter(w -> w.getId().equals(warehouseId)).toList();
        }
        if (CollUtil.isEmpty(allWarehouses)) {
            return List.of();
        }
        Set<Long> warehouseIds = allWarehouses.stream().map(Warehouse::getId).collect(Collectors.toSet());

        // 2. 批量查所有相关库区（1次查询）
        List<Area> allAreas = areaMapper.selectList(null);
        Map<Long, List<Area>> areasByWarehouse = allAreas.stream()
            .filter(a -> warehouseIds.contains(a.getWarehouseId()))
            .filter(a -> areaId == null || a.getId().equals(areaId))
            .collect(Collectors.groupingBy(Area::getWarehouseId));

        Set<Long> areaIds = areasByWarehouse.values().stream()
            .flatMap(Collection::stream).map(Area::getId).collect(Collectors.toSet());

        // 3. 批量查所有相关货架（1次查询）
        List<Rack> allRacks = rackMapper.selectList(null);
        Map<Long, List<Rack>> racksByArea = allRacks.stream()
            .filter(r -> areaIds.contains(r.getAreaId()))
            .filter(r -> rackId == null || r.getId().equals(rackId))
            .collect(Collectors.groupingBy(Rack::getAreaId));

        Set<Long> rackIds = racksByArea.values().stream()
            .flatMap(Collection::stream).map(Rack::getId).collect(Collectors.toSet());

        // 4. 批量查所有相关货位（1次查询）
        List<Location> allLocations = rackIds.isEmpty() ? List.of() : locationMapper.selectList(null);
        Map<Long, List<Location>> locationsByRack = allLocations.stream()
            .filter(l -> rackIds.contains(l.getRackId()))
            .collect(Collectors.groupingBy(Location::getRackId));

        // 5. 内存中组装树
        List<StorageLayoutNodeVo> result = new ArrayList<>();
        for (Warehouse warehouse : allWarehouses) {
            StorageLayoutNodeVo warehouseNode = toWarehouseNode(warehouse);
            List<Area> areas = areasByWarehouse.getOrDefault(warehouse.getId(), List.of());
            List<StorageLayoutNodeVo> areaNodes = new ArrayList<>();
            for (Area area : areas) {
                StorageLayoutNodeVo areaNode = toAreaNode(area);
                List<Rack> racks = racksByArea.getOrDefault(area.getId(), List.of());
                List<StorageLayoutNodeVo> rackNodes = new ArrayList<>();
                for (Rack rack : racks) {
                    StorageLayoutNodeVo rackNode = toRackNode(rack);
                    List<Location> locations = locationsByRack.getOrDefault(rack.getId(), List.of());
                    rackNode.setChildren(locations.stream()
                        .sorted(Comparator.comparing(Location::getRowNo, Comparator.nullsLast(Integer::compareTo))
                            .thenComparing(Location::getColumnNo, Comparator.nullsLast(Integer::compareTo))
                            .thenComparing(l -> l.getSortNo() != null ? l.getSortNo() : 0L))
                        .map(this::toLocationNode)
                        .toList());
                    rackNodes.add(rackNode);
                }
                areaNode.setChildren(rackNodes);
                areaNodes.add(areaNode);
            }
            warehouseNode.setChildren(areaNodes);
            result.add(warehouseNode);
        }
        return result;
    }

    private StorageLayoutNodeVo toWarehouseNode(Warehouse warehouse) {
        StorageLayoutNodeVo node = new StorageLayoutNodeVo();
        node.setNodeType("warehouse");
        node.setId(warehouse.getId());
        node.setParentId(0L);
        node.setCode(warehouse.getWarehouseCode());
        node.setName(warehouse.getWarehouseName());
        node.setStatus(warehouse.getStatus());
        node.setOrderNum(warehouse.getOrderNum());
        return node;
    }

    private StorageLayoutNodeVo toAreaNode(Area area) {
        StorageLayoutNodeVo node = new StorageLayoutNodeVo();
        node.setNodeType("area");
        node.setId(area.getId());
        node.setParentId(area.getWarehouseId());
        node.setCode(area.getAreaCode());
        node.setName(area.getAreaName());
        node.setStatus(area.getStatus());
        node.setOrderNum(area.getOrderNum());
        return node;
    }

    private StorageLayoutNodeVo toRackNode(Rack rack) {
        StorageLayoutNodeVo node = new StorageLayoutNodeVo();
        node.setNodeType("rack");
        node.setId(rack.getId());
        node.setParentId(rack.getAreaId());
        node.setCode(rack.getRackCode());
        node.setName(rack.getRackName());
        node.setStatus(rack.getRackStatus());
        node.setOrderNum(rack.getOrderNum());
        return node;
    }

    private StorageLayoutNodeVo toLocationNode(Location location) {
        StorageLayoutNodeVo node = new StorageLayoutNodeVo();
        node.setNodeType("location");
        node.setId(location.getId());
        node.setParentId(location.getRackId());
        node.setCode(location.getLocationCode());
        node.setName(location.getLocationName());
        node.setStatus(location.getLocationStatus());
        node.setOrderNum(location.getSortNo());
        node.setRowNo(location.getRowNo());
        node.setColumnNo(location.getColumnNo());
        node.setOccupiedFlag(location.getOccupiedFlag());
        return node;
    }
}
