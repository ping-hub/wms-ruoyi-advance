package com.ruoyi.wms.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.constant.ServiceConstants;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.MapstructUtils;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.ruoyi.wms.domain.bo.BoxBo;
import com.ruoyi.wms.domain.bo.BoxOperationBo;
import com.ruoyi.wms.domain.bo.ItemInstanceBo;
import com.ruoyi.wms.domain.entity.Area;
import com.ruoyi.wms.domain.entity.Box;
import com.ruoyi.wms.domain.entity.ItemInstance;
import com.ruoyi.wms.domain.entity.Location;
import com.ruoyi.wms.domain.entity.Rack;
import com.ruoyi.wms.domain.entity.Warehouse;
import com.ruoyi.wms.domain.vo.BoxVo;
import com.ruoyi.wms.domain.vo.ItemInstanceVo;
import com.ruoyi.wms.domain.vo.ItemSkuVo;
import com.ruoyi.wms.mapper.AreaMapper;
import com.ruoyi.wms.mapper.BoxMapper;
import com.ruoyi.wms.mapper.LocationMapper;
import com.ruoyi.wms.mapper.RackMapper;
import com.ruoyi.wms.mapper.WarehouseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BoxService extends ServiceImpl<BoxMapper, Box> {

    private final BoxMapper boxMapper;
    private final ItemInstanceService itemInstanceService;
    private final ItemSkuService itemSkuService;
    private final WarehouseMapper warehouseMapper;
    private final AreaMapper areaMapper;
    private final RackMapper rackMapper;
    private final LocationMapper locationMapper;

    public BoxVo queryById(Long id) {
        BoxVo boxVo = boxMapper.selectVoById(id);
        if (boxVo == null) {
            return null;
        }
        enrich(List.of(boxVo), true);
        return boxVo;
    }

    public BoxVo queryByCode(String boxCode) {
        LambdaQueryWrapper<Box> lqw = Wrappers.lambdaQuery();
        lqw.eq(Box::getBoxCode, boxCode);
        BoxVo boxVo = boxMapper.selectVoOne(lqw);
        if (boxVo == null) {
            return null;
        }
        enrich(List.of(boxVo), true);
        return boxVo;
    }

    public TableDataInfo<BoxVo> queryPageList(BoxBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<Box> lqw = buildQueryWrapper(bo);
        Page<BoxVo> result = boxMapper.selectVoPage(pageQuery.build(), lqw);
        enrich(result.getRecords(), false);
        return TableDataInfo.build(result);
    }

    public List<BoxVo> queryList(BoxBo bo) {
        LambdaQueryWrapper<Box> lqw = buildQueryWrapper(bo);
        List<BoxVo> list = boxMapper.selectVoList(lqw);
        enrich(list, false);
        return list;
    }

    @Transactional
    public void insertByBo(BoxBo bo) {
        fillAndValidateBeforeSave(bo);
        boxMapper.insert(MapstructUtils.convert(bo, Box.class));
    }

    @Transactional
    public void updateByBo(BoxBo bo) {
        fillAndValidateBeforeSave(bo);
        boxMapper.updateById(MapstructUtils.convert(bo, Box.class));
    }

    @Transactional
    public void pack(BoxOperationBo bo) {
        Box box = requireBox(bo.getBoxId());
        Assert.isFalse(ServiceConstants.BoxStatus.DISABLED.equals(box.getBoxStatus()), "箱体已停用，无法装箱");
        Assert.isFalse(ServiceConstants.BoxStatus.OUTBOUND.equals(box.getBoxStatus()), "已出库箱体无法装箱");
        Set<Long> itemIds = Set.copyOf(bo.getItemInstanceIds());
        List<ItemInstanceVo> items = itemInstanceService.queryVosByIds(itemIds);
        Assert.isTrue(items.size() == itemIds.size(), "存在不存在的单品实例");
        Assert.isTrue(items.stream().noneMatch(item -> item.getBoxId() != null), "存在已装箱的单品实例");
        Set<Long> skuIds = items.stream().map(ItemInstanceVo::getSkuId).filter(Objects::nonNull).collect(Collectors.toSet());
        Map<Long, ItemSkuVo> skuMap = itemSkuService.queryVosByIds(skuIds).stream()
            .collect(Collectors.toMap(ItemSkuVo::getId, Function.identity()));
        LocationContext targetLocation = resolvePackTargetLocation(box, items);
        for (ItemInstanceVo item : items) {
            validateItemBeforePack(item, skuMap.get(item.getSkuId()));
            validateItemLocationForPack(item, targetLocation);
        }
        if (!sameLocation(box, targetLocation)) {
            Box update = new Box();
            update.setId(box.getId());
            update.setWarehouseId(targetLocation.warehouseId());
            update.setAreaId(targetLocation.areaId());
            update.setRackId(targetLocation.rackId());
            update.setLocationId(targetLocation.locationId());
            boxMapper.updateById(update);
            box = requireBox(box.getId());
        }
        for (ItemInstanceVo item : items) {
            itemInstanceService.markInBox(item.getId(), box);
        }
        syncBoxSnapshot(box.getId(), ServiceConstants.BoxStatus.PACKED);
    }

    @Transactional
    public void unpack(BoxOperationBo bo) {
        Box box = requireBox(bo.getBoxId());
        Set<Long> itemIds = Set.copyOf(bo.getItemInstanceIds());
        List<ItemInstance> items = itemInstanceService.queryByIds(itemIds);
        Assert.isTrue(items.size() == itemIds.size(), "存在不存在的单品实例");
        Assert.isTrue(items.stream().allMatch(item -> Objects.equals(item.getBoxId(), bo.getBoxId())), "存在不属于当前箱体的单品实例");
        for (ItemInstance item : items) {
            itemInstanceService.restoreFromBox(item.getId(), box);
        }
        syncBoxSnapshot(box.getId(), countItemsByBoxId(box.getId()) > 0 ? ServiceConstants.BoxStatus.PACKED : ServiceConstants.BoxStatus.IDLE);
    }

    public List<BoxVo> queryByLocationId(Long locationId) {
        LambdaQueryWrapper<Box> lqw = Wrappers.lambdaQuery();
        lqw.eq(Box::getLocationId, locationId);
        lqw.orderByDesc(Box::getCreateTime);
        List<BoxVo> list = boxMapper.selectVoList(lqw);
        enrich(list, true);
        return list;
    }

    public Map<Long, Long> queryItemBoxMap(Set<Long> itemInstanceIds) {
        if (CollUtil.isEmpty(itemInstanceIds)) {
            return Map.of();
        }
        List<ItemInstanceVo> list = itemInstanceService.queryVosByIds(itemInstanceIds);
        return list.stream()
            .filter(item -> item.getBoxId() != null)
            .collect(Collectors.toMap(ItemInstanceVo::getId, ItemInstanceVo::getBoxId, (a, b) -> a));
    }

    public Set<Long> queryItemIdsByBoxId(Long boxId) {
        ItemInstanceBo bo = new ItemInstanceBo();
        bo.setBoxId(boxId);
        return itemInstanceService.queryList(bo).stream()
            .map(ItemInstanceVo::getId)
            .collect(Collectors.toSet());
    }

    public BoxVo queryByItemInstanceId(Long itemInstanceId) {
        ItemInstanceVo item = itemInstanceService.queryById(itemInstanceId);
        if (item == null || item.getBoxId() == null) {
            return null;
        }
        return queryById(item.getBoxId());
    }

    public void markOutbound(Long boxId) {
        Box update = new Box();
        update.setId(boxId);
        update.setBoxStatus(ServiceConstants.BoxStatus.OUTBOUND);
        update.setItemCount(countItemsByBoxId(boxId));
        update.setWarehouseId(null);
        update.setAreaId(null);
        update.setRackId(null);
        update.setLocationId(null);
        boxMapper.updateById(update);
    }

    public void moveTo(Long boxId, Long warehouseId, Long areaId, Long rackId, Long locationId) {
        Box update = new Box();
        update.setId(boxId);
        update.setBoxStatus(ServiceConstants.BoxStatus.PACKED);
        update.setItemCount(countItemsByBoxId(boxId));
        update.setWarehouseId(warehouseId);
        update.setAreaId(areaId);
        update.setRackId(rackId);
        update.setLocationId(locationId);
        boxMapper.updateById(update);
    }

    public void deleteById(Long id) {
        Assert.isTrue(countItemsByBoxId(id) == 0, "箱体内仍有单品，无法删除");
        boxMapper.deleteById(id);
    }

    private LambdaQueryWrapper<Box> buildQueryWrapper(BoxBo bo) {
        LambdaQueryWrapper<Box> lqw = Wrappers.lambdaQuery();
        lqw.eq(StrUtil.isNotBlank(bo.getBoxCode()), Box::getBoxCode, bo.getBoxCode());
        lqw.like(StrUtil.isNotBlank(bo.getBoxName()), Box::getBoxName, bo.getBoxName());
        lqw.eq(StrUtil.isNotBlank(bo.getBoxStatus()), Box::getBoxStatus, bo.getBoxStatus());
        lqw.eq(bo.getWarehouseId() != null, Box::getWarehouseId, bo.getWarehouseId());
        lqw.eq(bo.getAreaId() != null, Box::getAreaId, bo.getAreaId());
        lqw.eq(bo.getRackId() != null, Box::getRackId, bo.getRackId());
        lqw.eq(bo.getLocationId() != null, Box::getLocationId, bo.getLocationId());
        lqw.orderByDesc(Box::getCreateTime);
        return lqw;
    }

    private void fillAndValidateBeforeSave(BoxBo bo) {
        if (StrUtil.isBlank(bo.getBoxCode())) {
            bo.setBoxCode(generateBoxCode());
        }
        if (StrUtil.isBlank(bo.getBoxStatus())) {
            bo.setBoxStatus(ServiceConstants.BoxStatus.IDLE);
        }
        validateBoxCodeUnique(bo);
        fillLocationFields(bo);
    }

    private void validateBoxCodeUnique(BoxBo bo) {
        LambdaQueryWrapper<Box> lqw = Wrappers.lambdaQuery();
        lqw.eq(Box::getBoxCode, bo.getBoxCode());
        lqw.ne(bo.getId() != null, Box::getId, bo.getId());
        Assert.isTrue(boxMapper.selectCount(lqw) == 0, "箱码重复");
    }

    private void fillLocationFields(BoxBo bo) {
        if (bo.getLocationId() != null) {
            Location location = locationMapper.selectById(bo.getLocationId());
            Assert.notNull(location, "货位不存在");
            bo.setRackId(location.getRackId());
            bo.setAreaId(location.getAreaId());
            bo.setWarehouseId(location.getWarehouseId());
            return;
        }
        if (bo.getRackId() != null) {
            Rack rack = rackMapper.selectById(bo.getRackId());
            Assert.notNull(rack, "货架不存在");
            bo.setAreaId(rack.getAreaId());
            bo.setWarehouseId(rack.getWarehouseId());
            return;
        }
        if (bo.getAreaId() != null) {
            Area area = areaMapper.selectById(bo.getAreaId());
            Assert.notNull(area, "库区不存在");
            bo.setWarehouseId(area.getWarehouseId());
            return;
        }
        if (bo.getWarehouseId() != null) {
            Assert.notNull(warehouseMapper.selectById(bo.getWarehouseId()), "仓库不存在");
        }
    }

    private void validateItemBeforePack(ItemInstanceVo item, ItemSkuVo skuVo) {
        Assert.isFalse(Integer.valueOf(1).equals(item.getInBox()), "单品实例已在箱体中");
        Assert.isFalse(Integer.valueOf(1).equals(item.getBorrowed()), "单品实例已借出，无法装箱");
        Assert.isFalse(ServiceConstants.ItemInstanceStatus.DISABLED.equals(item.getInstanceStatus()), "停用单品无法装箱");
        Assert.isFalse(ServiceConstants.ItemInstanceStatus.OUTBOUND.equals(item.getInstanceStatus()), "已出库单品无法装箱");
        Assert.notNull(skuVo, "单品实例规格不存在");
        Assert.notNull(skuVo.getItem(), "规格未关联物品定义");
        Assert.isTrue(Integer.valueOf(1).equals(skuVo.getItem().getAllowBox()), "物品未开启装箱，不允许装箱");
    }

    private void validateItemLocationForPack(ItemInstanceVo item, LocationContext targetLocation) {
        Assert.isTrue(Objects.equals(item.getWarehouseId(), targetLocation.warehouseId()), "待装箱单品不在同一仓库，无法装入同一箱体");
        Assert.isTrue(Objects.equals(item.getAreaId(), targetLocation.areaId()), "待装箱单品不在同一库区，无法装入同一箱体");
        Assert.isTrue(Objects.equals(item.getRackId(), targetLocation.rackId()), "待装箱单品不在同一货架，无法装入同一箱体");
        Assert.isTrue(Objects.equals(item.getLocationId(), targetLocation.locationId()), "待装箱单品不在同一货位，无法装入同一箱体");
    }

    private Box requireBox(Long boxId) {
        Box box = boxMapper.selectById(boxId);
        Assert.notNull(box, "箱体不存在");
        return box;
    }

    private void updateBoxStatus(Long boxId, String boxStatus) {
        Box update = new Box();
        update.setId(boxId);
        update.setBoxStatus(boxStatus);
        boxMapper.updateById(update);
    }

    private void syncBoxSnapshot(Long boxId, String boxStatus) {
        Box update = new Box();
        update.setId(boxId);
        update.setBoxStatus(boxStatus);
        update.setItemCount(countItemsByBoxId(boxId));
        boxMapper.updateById(update);
    }

    private int countItemsByBoxId(Long boxId) {
        ItemInstanceBo bo = new ItemInstanceBo();
        bo.setBoxId(boxId);
        return itemInstanceService.queryList(bo).size();
    }

    private String generateBoxCode() {
        return "BOX" + IdUtil.getSnowflakeNextIdStr();
    }

    private LocationContext resolvePackTargetLocation(Box box, List<ItemInstanceVo> items) {
        Assert.isTrue(CollUtil.isNotEmpty(items), "待装箱单品不能为空");
        if (countItemsByBoxId(box.getId()) > 0) {
            return new LocationContext(box.getWarehouseId(), box.getAreaId(), box.getRackId(), box.getLocationId());
        }
        ItemInstanceVo firstItem = items.get(0);
        return new LocationContext(firstItem.getWarehouseId(), firstItem.getAreaId(), firstItem.getRackId(), firstItem.getLocationId());
    }

    private boolean sameLocation(Box box, LocationContext location) {
        return Objects.equals(box.getWarehouseId(), location.warehouseId())
            && Objects.equals(box.getAreaId(), location.areaId())
            && Objects.equals(box.getRackId(), location.rackId())
            && Objects.equals(box.getLocationId(), location.locationId());
    }

    private record LocationContext(Long warehouseId, Long areaId, Long rackId, Long locationId) {
    }

    private void enrich(List<BoxVo> list, boolean loadItems) {
        if (CollUtil.isEmpty(list)) {
            return;
        }
        List<BoxVo> validList = list.stream().filter(Objects::nonNull).toList();
        if (CollUtil.isEmpty(validList)) {
            return;
        }
        Set<Long> warehouseIds = validList.stream().map(BoxVo::getWarehouseId).filter(Objects::nonNull).collect(Collectors.toSet());
        Set<Long> areaIds = validList.stream().map(BoxVo::getAreaId).filter(Objects::nonNull).collect(Collectors.toSet());
        Set<Long> rackIds = validList.stream().map(BoxVo::getRackId).filter(Objects::nonNull).collect(Collectors.toSet());
        Set<Long> locationIds = validList.stream().map(BoxVo::getLocationId).filter(Objects::nonNull).collect(Collectors.toSet());
        Set<Long> boxIds = validList.stream().map(BoxVo::getId).filter(Objects::nonNull).collect(Collectors.toSet());
        Map<Long, Warehouse> warehouseMap = warehouseIds.isEmpty() ? Map.of() :
            warehouseMapper.selectBatchIds(warehouseIds).stream().collect(Collectors.toMap(Warehouse::getId, Function.identity()));
        Map<Long, Area> areaMap = areaIds.isEmpty() ? Map.of() :
            areaMapper.selectBatchIds(areaIds).stream().collect(Collectors.toMap(Area::getId, Function.identity()));
        Map<Long, Rack> rackMap = rackIds.isEmpty() ? Map.of() :
            rackMapper.selectBatchIds(rackIds).stream().collect(Collectors.toMap(Rack::getId, Function.identity()));
        Map<Long, Location> locationMap = locationIds.isEmpty() ? Map.of() :
            locationMapper.selectBatchIds(locationIds).stream().collect(Collectors.toMap(Location::getId, Function.identity()));
        ItemInstanceBo itemInstanceBo = new ItemInstanceBo();
        List<ItemInstanceVo> boxItems = boxIds.isEmpty() ? List.of() : itemInstanceService.queryList(itemInstanceBo).stream()
            .filter(item -> item.getBoxId() != null && boxIds.contains(item.getBoxId()))
            .toList();
        Map<Long, List<ItemInstanceVo>> itemMapByBoxId = boxItems.stream().collect(Collectors.groupingBy(ItemInstanceVo::getBoxId));
        validList.forEach(boxVo -> {
            Warehouse warehouse = warehouseMap.get(boxVo.getWarehouseId());
            if (warehouse != null) {
                boxVo.setWarehouseName(warehouse.getWarehouseName());
            }
            Area area = areaMap.get(boxVo.getAreaId());
            if (area != null) {
                boxVo.setAreaName(area.getAreaName());
            }
            Rack rack = rackMap.get(boxVo.getRackId());
            if (rack != null) {
                boxVo.setRackName(rack.getRackName());
            }
            Location location = locationMap.get(boxVo.getLocationId());
            if (location != null) {
                boxVo.setLocationName(location.getLocationName());
            }
            List<ItemInstanceVo> currentItems = itemMapByBoxId.getOrDefault(boxVo.getId(), List.of());
            boxVo.setItemCount(currentItems.size());
            if (loadItems) {
                boxVo.setItems(currentItems);
            }
        });
    }
}
