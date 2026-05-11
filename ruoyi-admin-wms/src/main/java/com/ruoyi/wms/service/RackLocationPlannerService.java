package com.ruoyi.wms.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.wms.domain.entity.Box;
import com.ruoyi.wms.domain.entity.InventoryDetail;
import com.ruoyi.wms.domain.entity.ItemInstance;
import com.ruoyi.wms.domain.entity.Location;
import com.ruoyi.wms.domain.entity.Rack;
import com.ruoyi.wms.domain.vo.LocationHealthCheckResultVo;
import com.ruoyi.wms.domain.vo.LocationRebuildResultVo;
import com.ruoyi.wms.mapper.BoxMapper;
import com.ruoyi.wms.mapper.InventoryDetailMapper;
import com.ruoyi.wms.mapper.ItemInstanceMapper;
import com.ruoyi.wms.mapper.LocationMapper;
import com.ruoyi.wms.mapper.RackMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 货架货位规划服务
 */
@Service
@RequiredArgsConstructor
public class RackLocationPlannerService {

    private static final String DEFAULT_LOCATION_STATUS = "enabled";
    private static final String DEFAULT_LOCATION_TYPE = "normal";

    private final RackMapper rackMapper;
    private final LocationMapper locationMapper;
    private final InventoryDetailMapper inventoryDetailMapper;
    private final BoxMapper boxMapper;
    private final ItemInstanceMapper itemInstanceMapper;

    public void generateLocationsForNewRack(Rack rack) {
        List<Location> plannedLocations = buildPlannedLocations(rack);
        plannedLocations.forEach(locationMapper::insert);
    }

    public void syncLocationsAfterRackUpdate(Rack beforeRack, Rack afterRack) {
        List<Location> existingLocations = listByRackId(afterRack.getId());
        if (CollUtil.isEmpty(existingLocations)) {
            generateLocationsForNewRack(afterRack);
            return;
        }

        List<Location> outOfRangeLocations = existingLocations.stream()
            .filter(location -> !isWithinRange(location, afterRack))
            .toList();
        validateOutOfRangeLocationsCanBeRemoved(outOfRangeLocations);

        List<Location> inRangeLocations = existingLocations.stream()
            .filter(location -> isWithinRange(location, afterRack))
            .toList();
        Map<String, Location> existingGridMap = inRangeLocations.stream()
            .collect(Collectors.toMap(this::toGridKey, location -> location, (left, right) -> left, LinkedHashMap::new));

        for (Location location : inRangeLocations) {
            Location update = buildSyncUpdate(beforeRack, afterRack, location);
            if (update != null) {
                locationMapper.updateById(update);
            }
        }

        outOfRangeLocations.forEach(location -> locationMapper.deleteById(location.getId()));

        List<Location> missingLocations = buildMissingLocations(afterRack, existingGridMap.keySet());
        missingLocations.forEach(locationMapper::insert);
    }

    public LocationRebuildResultVo rebuildByRack(Long rackId) {
        Rack rack = requireRack(rackId);
        List<Location> existingLocations = listByRackId(rackId);
        Map<String, List<Location>> gridMap = existingLocations.stream()
            .collect(Collectors.groupingBy(this::toGridKey, LinkedHashMap::new, Collectors.toList()));
        Map<String, List<Location>> codeMap = existingLocations.stream()
            .filter(location -> StrUtil.isNotBlank(location.getLocationCode()))
            .collect(Collectors.groupingBy(Location::getLocationCode, LinkedHashMap::new, Collectors.toList()));

        LocationRebuildResultVo resultVo = new LocationRebuildResultVo();
        resultVo.setRackId(rack.getId());
        resultVo.setRackCode(rack.getRackCode());
        resultVo.setRackName(rack.getRackName());
        resultVo.setExpectedLocationCount(calcExpectedCount(rack));

        int existingCount = 0;
        Set<String> existedGridKeys = new LinkedHashSet<>();
        for (int row = 1; row <= rack.getRowCount(); row++) {
            for (int column = 1; column <= rack.getColumnCount(); column++) {
                String gridKey = toGridKey(row, column);
                List<Location> locations = gridMap.get(gridKey);
                if (CollUtil.isNotEmpty(locations)) {
                    existingCount++;
                    existedGridKeys.add(gridKey);
                }
            }
        }
        resultVo.setExistingLocationCount(existingCount);

        List<Location> missingLocations = buildMissingLocations(rack, existedGridKeys);
        missingLocations.forEach(locationMapper::insert);
        resultVo.setCreatedLocationCount(missingLocations.size());

        int blockedCount = 0;
        for (Map.Entry<String, List<Location>> entry : gridMap.entrySet()) {
            List<Location> duplicatedLocations = entry.getValue();
            if (duplicatedLocations.size() > 1) {
                blockedCount += duplicatedLocations.size() - 1;
                resultVo.getMessages().add("发现重复格子货位：" + entry.getKey());
            }
        }
        for (Map.Entry<String, List<Location>> entry : codeMap.entrySet()) {
            List<Location> duplicatedLocations = entry.getValue();
            if (duplicatedLocations.size() > 1) {
                resultVo.getMessages().add("发现重复货位编码：" + entry.getKey());
            }
        }
        List<Location> outOfRangeLocations = existingLocations.stream()
            .filter(location -> !isWithinRange(location, rack))
            .toList();
        if (CollUtil.isNotEmpty(outOfRangeLocations)) {
            blockedCount += outOfRangeLocations.size();
            resultVo.getMessages().add("发现超出货架规划范围的货位：" + outOfRangeLocations.stream()
                .map(Location::getLocationCode)
                .filter(StrUtil::isNotBlank)
                .collect(Collectors.joining("、")));
        }
        resultVo.setBlockedLocationCount(blockedCount);
        return resultVo;
    }

    public LocationHealthCheckResultVo healthCheckByRack(Long rackId) {
        Rack rack = requireRack(rackId);
        List<Location> locations = listByRackId(rackId);
        Map<String, List<Location>> gridMap = locations.stream()
            .collect(Collectors.groupingBy(this::toGridKey, LinkedHashMap::new, Collectors.toList()));
        Map<String, List<Location>> codeMap = locations.stream()
            .filter(location -> StrUtil.isNotBlank(location.getLocationCode()))
            .collect(Collectors.groupingBy(Location::getLocationCode, LinkedHashMap::new, Collectors.toList()));

        int missingCount = 0;
        for (int row = 1; row <= rack.getRowCount(); row++) {
            for (int column = 1; column <= rack.getColumnCount(); column++) {
                if (!gridMap.containsKey(toGridKey(row, column))) {
                    missingCount++;
                }
            }
        }

        long duplicateGridCount = gridMap.values().stream()
            .filter(group -> group.size() > 1)
            .mapToLong(group -> group.size() - 1L)
            .sum();
        long duplicateCodeCount = codeMap.values().stream()
            .filter(group -> group.size() > 1)
            .mapToLong(group -> group.size() - 1L)
            .sum();
        long outOfRangeCount = locations.stream()
            .filter(location -> !isWithinRange(location, rack))
            .count();

        LocationHealthCheckResultVo resultVo = new LocationHealthCheckResultVo();
        resultVo.setRackId(rack.getId());
        resultVo.setRackCode(rack.getRackCode());
        resultVo.setRackName(rack.getRackName());
        resultVo.setExpectedLocationCount(calcExpectedCount(rack));
        resultVo.setActualLocationCount(locations.size());
        resultVo.setMissingLocationCount(missingCount);
        resultVo.setDuplicateGridCount((int) duplicateGridCount);
        resultVo.setDuplicateCodeCount((int) duplicateCodeCount);
        resultVo.setOutOfRangeCount((int) outOfRangeCount);

        if (missingCount > 0) {
            resultVo.getMessages().add("存在缺失货位，请执行按货架重建");
        }
        if (duplicateGridCount > 0) {
            resultVo.getMessages().add("存在重复行列货位，需要人工治理");
        }
        if (duplicateCodeCount > 0) {
            resultVo.getMessages().add("存在重复货位编码，需要人工治理");
        }
        if (outOfRangeCount > 0) {
            resultVo.getMessages().add("存在超出货架规划范围的异常货位");
        }
        return resultVo;
    }

    private Location buildSyncUpdate(Rack beforeRack, Rack afterRack, Location location) {
        Location update = new Location();
        boolean changed = false;
        update.setId(location.getId());

        String locationCode = buildLocationCode(afterRack, location.getRowNo(), location.getColumnNo());
        if (!Objects.equals(location.getLocationCode(), locationCode)) {
            update.setLocationCode(locationCode);
            changed = true;
        }
        String locationName = buildLocationName(afterRack, location.getRowNo(), location.getColumnNo());
        if (!Objects.equals(location.getLocationName(), locationName)) {
            update.setLocationName(locationName);
            changed = true;
        }
        if (!Objects.equals(location.getWarehouseId(), afterRack.getWarehouseId())) {
            update.setWarehouseId(afterRack.getWarehouseId());
            changed = true;
        }
        if (!Objects.equals(location.getAreaId(), afterRack.getAreaId())) {
            update.setAreaId(afterRack.getAreaId());
            changed = true;
        }
        if (!Objects.equals(location.getRackId(), afterRack.getId())) {
            update.setRackId(afterRack.getId());
            changed = true;
        }

        BigDecimal oldVolume = calcVolume(beforeRack.getLength(), beforeRack.getWidth(), beforeRack.getHeight());
        BigDecimal newVolume = calcVolume(afterRack.getLength(), afterRack.getWidth(), afterRack.getHeight());
        if (shouldSyncDerivedValue(location.getLength(), beforeRack.getLength()) && !Objects.equals(location.getLength(), afterRack.getLength())) {
            update.setLength(afterRack.getLength());
            changed = true;
        }
        if (shouldSyncDerivedValue(location.getWidth(), beforeRack.getWidth()) && !Objects.equals(location.getWidth(), afterRack.getWidth())) {
            update.setWidth(afterRack.getWidth());
            changed = true;
        }
        if (shouldSyncDerivedValue(location.getHeight(), beforeRack.getHeight()) && !Objects.equals(location.getHeight(), afterRack.getHeight())) {
            update.setHeight(afterRack.getHeight());
            changed = true;
        }
        if (shouldSyncDerivedValue(location.getVolume(), oldVolume) && !Objects.equals(location.getVolume(), newVolume)) {
            update.setVolume(newVolume);
            changed = true;
        }
        Long expectedSortNo = buildSortNo(location.getRowNo(), location.getColumnNo());
        if ((location.getSortNo() == null || Objects.equals(location.getSortNo(), expectedSortNo))
            && !Objects.equals(location.getSortNo(), expectedSortNo)) {
            update.setSortNo(expectedSortNo);
            changed = true;
        }
        return changed ? update : null;
    }

    private List<Location> buildMissingLocations(Rack rack, Collection<String> existingGridKeys) {
        List<Location> missingLocations = new ArrayList<>();
        for (int row = 1; row <= rack.getRowCount(); row++) {
            for (int column = 1; column <= rack.getColumnCount(); column++) {
                String gridKey = toGridKey(row, column);
                if (existingGridKeys.contains(gridKey)) {
                    continue;
                }
                missingLocations.add(buildGeneratedLocation(rack, row, column));
            }
        }
        return missingLocations;
    }

    private void validateOutOfRangeLocationsCanBeRemoved(List<Location> outOfRangeLocations) {
        if (CollUtil.isEmpty(outOfRangeLocations)) {
            return;
        }
        Set<Long> occupiedLocationIds = queryOccupiedLocationIds(outOfRangeLocations.stream().map(Location::getId).collect(Collectors.toSet()));
        if (CollUtil.isNotEmpty(occupiedLocationIds)) {
            String occupiedCodes = outOfRangeLocations.stream()
                .filter(location -> occupiedLocationIds.contains(location.getId()))
                .map(location -> StrUtil.blankToDefault(location.getLocationCode(), String.valueOf(location.getId())))
                .collect(Collectors.joining("、"));
            throw new ServiceException("货架缩减后将裁剪存在占用的货位：" + occupiedCodes + "，请先清理库存、箱体或单品");
        }
    }

    public Set<Long> queryOccupiedLocationIds(Set<Long> locationIds) {
        if (CollUtil.isEmpty(locationIds)) {
            return Set.of();
        }
        Set<Long> occupiedLocationIds = new LinkedHashSet<>();

        LambdaQueryWrapper<InventoryDetail> inventoryWrapper = Wrappers.lambdaQuery();
        inventoryWrapper.in(InventoryDetail::getLocationId, locationIds);
        inventoryWrapper.gt(InventoryDetail::getRemainQuantity, BigDecimal.ZERO);
        occupiedLocationIds.addAll(inventoryDetailMapper.selectList(inventoryWrapper).stream()
            .map(InventoryDetail::getLocationId)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet()));

        LambdaQueryWrapper<Box> boxWrapper = Wrappers.lambdaQuery();
        boxWrapper.in(Box::getLocationId, locationIds);
        occupiedLocationIds.addAll(boxMapper.selectList(boxWrapper).stream()
            .map(Box::getLocationId)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet()));

        LambdaQueryWrapper<ItemInstance> itemWrapper = Wrappers.lambdaQuery();
        itemWrapper.in(ItemInstance::getLocationId, locationIds);
        occupiedLocationIds.addAll(itemInstanceMapper.selectList(itemWrapper).stream()
            .map(ItemInstance::getLocationId)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet()));
        return occupiedLocationIds;
    }

    public List<Location> buildPlannedLocations(Rack rack) {
        List<Location> plannedLocations = new ArrayList<>();
        for (int row = 1; row <= rack.getRowCount(); row++) {
            for (int column = 1; column <= rack.getColumnCount(); column++) {
                plannedLocations.add(buildGeneratedLocation(rack, row, column));
            }
        }
        return plannedLocations;
    }

    public List<Location> listByRackId(Long rackId) {
        LambdaQueryWrapper<Location> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Location::getRackId, rackId);
        queryWrapper.orderByAsc(Location::getRowNo, Location::getColumnNo, Location::getSortNo, Location::getId);
        return locationMapper.selectList(queryWrapper);
    }

    private Rack requireRack(Long rackId) {
        Rack rack = rackMapper.selectById(rackId);
        Assert.notNull(rack, "货架不存在");
        Assert.notNull(rack.getRowCount(), "货架未维护行数，无法规划货位");
        Assert.notNull(rack.getColumnCount(), "货架未维护列数，无法规划货位");
        return rack;
    }

    private Location buildGeneratedLocation(Rack rack, int row, int column) {
        Location location = new Location();
        location.setWarehouseId(rack.getWarehouseId());
        location.setAreaId(rack.getAreaId());
        location.setRackId(rack.getId());
        location.setRowNo(row);
        location.setColumnNo(column);
        location.setLocationCode(buildLocationCode(rack, row, column));
        location.setLocationName(buildLocationName(rack, row, column));
        location.setLocationStatus(DEFAULT_LOCATION_STATUS);
        location.setLocationType(DEFAULT_LOCATION_TYPE);
        location.setLength(rack.getLength().divide(BigDecimal.valueOf(rack.getColumnCount()), BigDecimal.ROUND_HALF_UP));
        location.setWidth(rack.getWidth());
        location.setHeight(rack.getHeight().divide(BigDecimal.valueOf(rack.getRowCount()), BigDecimal.ROUND_HALF_UP));
        location.setVolume(calcVolume(rack.getLength(), rack.getWidth(), rack.getHeight()));
        location.setOccupiedFlag(0);
        location.setSortNo(buildSortNo(row, column));
        return location;
    }

    private String buildLocationCode(Rack rack, int row, int column) {
        String rackCode = StrUtil.blankToDefault(rack.getRackCode(), "RACK" + rack.getId());
        return rackCode + "-R" + row + "-C" + column;
    }

    private String buildLocationName(Rack rack, int row, int column) {
        String rackName = StrUtil.blankToDefault(rack.getRackName(), "货架" + rack.getId());
        return rackName + "-" + row + "-" + column;
    }

    private Long buildSortNo(Integer row, Integer column) {
        return row == null || column == null ? null : row * 1000L + column;
    }

    private BigDecimal calcVolume(BigDecimal length, BigDecimal width, BigDecimal height) {
        if (length == null || width == null || height == null) {
            return null;
        }
        return length.multiply(width).multiply(height);
    }

    private boolean isWithinRange(Location location, Rack rack) {
        return location.getRowNo() != null && location.getColumnNo() != null
            && location.getRowNo() >= 1 && location.getRowNo() <= rack.getRowCount()
            && location.getColumnNo() >= 1 && location.getColumnNo() <= rack.getColumnCount();
    }

    private int calcExpectedCount(Rack rack) {
        return rack.getRowCount() * rack.getColumnCount();
    }

    private boolean shouldSyncDerivedValue(BigDecimal currentValue, BigDecimal oldDerivedValue) {
        return currentValue == null || Objects.equals(currentValue, oldDerivedValue);
    }

    private String toGridKey(Location location) {
        return toGridKey(location.getRowNo(), location.getColumnNo());
    }

    private String toGridKey(Integer rowNo, Integer columnNo) {
        return rowNo + "_" + columnNo;
    }
}
