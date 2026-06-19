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
import com.ruoyi.common.core.utils.MapstructUtils;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.ruoyi.wms.domain.bo.BoxBo;
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
    private final CodeRuleService codeRuleService;
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

    public Box getOrCreateForReceipt(String boxCode, Long warehouseId, Long areaId, Long rackId, Long locationId) {
        Assert.isTrue(StrUtil.isNotBlank(boxCode), "箱码不能为空");
        Box box = queryEntityByCode(boxCode);
        if (box == null) {
            Box add = new Box();
            add.setBoxCode(boxCode);
            add.setBoxName(boxCode);
            add.setBoxStatus(ServiceConstants.BoxStatus.PACKED);
            add.setWarehouseId(warehouseId);
            add.setAreaId(areaId);
            add.setRackId(rackId);
            add.setLocationId(locationId);
            add.setItemCount(0);
            boxMapper.insert(add);
            return add;
        }
        Assert.isFalse(ServiceConstants.BoxStatus.DISABLED.equals(box.getBoxStatus()), "箱体已停用，无法入库绑定");
        int itemCount = countItemsByBoxId(box.getId());
        LocationContext targetLocation = new LocationContext(warehouseId, areaId, rackId, locationId);
        if (itemCount > 0) {
            Assert.isTrue(sameLocation(box, targetLocation), "箱码" + boxCode + "当前已在其他位置存在装箱关系，请先处理原状态");
        }
        Box update = new Box();
        update.setId(box.getId());
        update.setBoxStatus(ServiceConstants.BoxStatus.PACKED);
        update.setWarehouseId(warehouseId);
        update.setAreaId(areaId);
        update.setRackId(rackId);
        update.setLocationId(locationId);
        boxMapper.updateById(update);
        box.setBoxStatus(ServiceConstants.BoxStatus.PACKED);
        box.setWarehouseId(warehouseId);
        box.setAreaId(areaId);
        box.setRackId(rackId);
        box.setLocationId(locationId);
        return box;
    }

    private Box queryEntityByCode(String boxCode) {
        LambdaQueryWrapper<Box> lqw = Wrappers.lambdaQuery();
        lqw.eq(Box::getBoxCode, boxCode);
        return boxMapper.selectOne(lqw);
    }

    public List<BoxVo> queryByLocationId(Long locationId) {
        LambdaQueryWrapper<Box> lqw = Wrappers.lambdaQuery();
        lqw.eq(Box::getLocationId, locationId);
        lqw.orderByDesc(Box::getCreateTime);
        List<BoxVo> list = boxMapper.selectVoList(lqw);
        enrich(list, true);
        return list;
    }

    public Map<String, Long> queryItemBoxMap(Set<String> instanceCodes) {
        if (CollUtil.isEmpty(instanceCodes)) {
            return java.util.Collections.emptyMap();
        }
        List<ItemInstanceVo> list = itemInstanceService.queryVosByInstanceCodes(instanceCodes);
        return list.stream()
            .filter(item -> item.getBoxId() != null)
            .collect(Collectors.toMap(ItemInstanceVo::getInstanceCode, ItemInstanceVo::getBoxId, (a, b) -> a));
    }

    public Set<Long> queryItemIdsByBoxId(Long boxId) {
        ItemInstanceBo bo = new ItemInstanceBo();
        bo.setBoxId(boxId);
        return itemInstanceService.queryList(bo).stream()
            .map(ItemInstanceVo::getId)
            .collect(Collectors.toSet());
    }

    public BoxVo queryByInstanceCode(String instanceCode) {
        ItemInstanceVo item = itemInstanceService.queryByCode(instanceCode);
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
        // 先清空箱内器材的 boxId（解除装箱关系，不影响器材自身位置）
        clearItemsBoxId(id);
        boxMapper.deleteById(id);
    }

    private void clearItemsBoxId(Long boxId) {
        itemInstanceService.lambdaUpdate()
            .eq(ItemInstance::getBoxId, boxId)
            .set(ItemInstance::getBoxId, null)
            .update();
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

    private int countItemsByBoxId(Long boxId) {
        ItemInstanceBo bo = new ItemInstanceBo();
        bo.setBoxId(boxId);
        return itemInstanceService.queryList(bo).size();
    }

    private String generateBoxCode() {
        String code = codeRuleService.generateCode("box");
        return code != null ? code : "BOX" + IdUtil.getSnowflakeNextIdStr();
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
        Map<Long, Warehouse> warehouseMap = warehouseIds.isEmpty() ? java.util.Collections.emptyMap() :
            warehouseMapper.selectBatchIds(warehouseIds).stream().collect(Collectors.toMap(Warehouse::getId, Function.identity()));
        Map<Long, Area> areaMap = areaIds.isEmpty() ? java.util.Collections.emptyMap() :
            areaMapper.selectBatchIds(areaIds).stream().collect(Collectors.toMap(Area::getId, Function.identity()));
        Map<Long, Rack> rackMap = rackIds.isEmpty() ? java.util.Collections.emptyMap() :
            rackMapper.selectBatchIds(rackIds).stream().collect(Collectors.toMap(Rack::getId, Function.identity()));
        Map<Long, Location> locationMap = locationIds.isEmpty() ? java.util.Collections.emptyMap() :
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
