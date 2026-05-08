package com.ruoyi.wms.service;

import cn.hutool.core.collection.CollUtil;
import com.ruoyi.wms.domain.bo.AreaBo;
import com.ruoyi.wms.domain.bo.LocationBo;
import com.ruoyi.wms.domain.bo.RackBo;
import com.ruoyi.wms.domain.bo.WarehouseBo;
import com.ruoyi.wms.domain.vo.AreaVo;
import com.ruoyi.wms.domain.vo.LocationVo;
import com.ruoyi.wms.domain.vo.StorageLayoutNodeVo;
import com.ruoyi.wms.domain.vo.RackVo;
import com.ruoyi.wms.domain.vo.WarehouseVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StorageLayoutQueryService {

    private final WarehouseService warehouseService;
    private final AreaService areaService;
    private final RackService rackService;
    private final LocationService locationService;

    public List<StorageLayoutNodeVo> queryLayoutTree(Long warehouseId, Long areaId, Long rackId) {
        WarehouseBo warehouseBo = new WarehouseBo();
        warehouseBo.setId(warehouseId);
        List<WarehouseVo> warehouses = warehouseService.queryList(warehouseBo);
        List<StorageLayoutNodeVo> result = new ArrayList<>();
        for (WarehouseVo warehouse : warehouses) {
            StorageLayoutNodeVo warehouseNode = toWarehouseNode(warehouse);
            warehouseNode.setChildren(queryAreaNodes(warehouse.getId(), areaId, rackId));
            result.add(warehouseNode);
        }
        return result;
    }

    public List<StorageLayoutNodeVo> queryAreaNodes(Long warehouseId, Long areaId, Long rackId) {
        AreaBo areaBo = new AreaBo();
        areaBo.setWarehouseId(warehouseId);
        areaBo.setId(areaId);
        List<AreaVo> areas = areaService.queryList(areaBo);
        List<StorageLayoutNodeVo> result = new ArrayList<>();
        for (AreaVo area : areas) {
            StorageLayoutNodeVo areaNode = toAreaNode(area);
            areaNode.setChildren(queryRackNodes(area.getWarehouseId(), area.getId(), rackId));
            result.add(areaNode);
        }
        return result;
    }

    public List<StorageLayoutNodeVo> queryRackNodes(Long warehouseId, Long areaId, Long rackId) {
        RackBo rackBo = new RackBo();
        rackBo.setWarehouseId(warehouseId);
        rackBo.setAreaId(areaId);
        rackBo.setId(rackId);
        List<RackVo> racks = rackService.queryList(rackBo);
        List<StorageLayoutNodeVo> result = new ArrayList<>();
        for (RackVo rack : racks) {
            StorageLayoutNodeVo rackNode = toRackNode(rack);
            rackNode.setChildren(queryLocationNodes(rack.getWarehouseId(), rack.getAreaId(), rack.getId()));
            result.add(rackNode);
        }
        return result;
    }

    public List<StorageLayoutNodeVo> queryLocationNodes(Long warehouseId, Long areaId, Long rackId) {
        LocationBo locationBo = new LocationBo();
        locationBo.setWarehouseId(warehouseId);
        locationBo.setAreaId(areaId);
        locationBo.setRackId(rackId);
        List<LocationVo> locations = locationService.queryList(locationBo);
        if (CollUtil.isEmpty(locations)) {
            return List.of();
        }
        return locations.stream()
            .sorted(Comparator.comparing(LocationVo::getRowNo, Comparator.nullsLast(Integer::compareTo))
                .thenComparing(LocationVo::getColumnNo, Comparator.nullsLast(Integer::compareTo))
                .thenComparing(LocationVo::getSortNo, Comparator.nullsLast(Long::compareTo))
                .thenComparing(LocationVo::getId))
            .map(this::toLocationNode)
            .toList();
    }

    private StorageLayoutNodeVo toWarehouseNode(WarehouseVo warehouse) {
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

    private StorageLayoutNodeVo toAreaNode(AreaVo area) {
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

    private StorageLayoutNodeVo toRackNode(RackVo rack) {
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

    private StorageLayoutNodeVo toLocationNode(LocationVo location) {
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
