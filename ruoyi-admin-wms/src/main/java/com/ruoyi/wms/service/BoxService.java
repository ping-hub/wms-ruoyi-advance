package com.ruoyi.wms.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.constant.ServiceConstants;
import com.ruoyi.common.core.utils.MapstructUtils;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.ruoyi.wms.domain.bo.BoxBo;
import com.ruoyi.wms.domain.bo.ItemInstanceBo;
import com.ruoyi.wms.domain.entity.*;
import com.ruoyi.wms.domain.vo.BoxVo;
import com.ruoyi.wms.domain.vo.ItemInstanceVo;
import com.ruoyi.wms.mapper.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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
    private final ItemInstanceMapper itemInstanceMapper;
    private final CodeRuleService codeRuleService;
    private final ItemInstanceService itemInstanceService;
    private final ItemSkuService itemSkuService;
    private final WarehouseMapper warehouseMapper;
    private final AreaMapper areaMapper;
    private final RackMapper rackMapper;
    private final LocationMapper locationMapper;
    @Autowired
    @Lazy
    private LocationService locationService;
    private final BoxCirculationLogService boxCirculationLogService;

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
        refreshLocationStatus(bo.getLocationId());
    }

    @Transactional
    public void updateByBo(BoxBo bo) {
        fillAndValidateBeforeSave(bo);
        Box oldBox = bo.getId() != null ? boxMapper.selectById(bo.getId()) : null;
        boxMapper.updateById(MapstructUtils.convert(bo, Box.class));
        refreshLocationStatus(bo.getLocationId(), oldBox != null ? oldBox.getLocationId() : null);
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
            refreshLocationStatus(locationId);
            return add;
        }
        Assert.isFalse(ServiceConstants.BoxStatus.DISABLED.equals(box.getBoxStatus()), "箱体已停用，无法入库绑定");
        // 若箱子当前为出库状态，触发归还流程（写 RETURN 日志，清出库关联）
        if (ServiceConstants.BoxStatus.OUTBOUND.equals(box.getBoxStatus())) {
            markReturn(box.getId());
        }
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
        refreshLocationStatus(locationId, box.getLocationId());
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
        markOutbound(boxId, null, null);
    }

    @Transactional
    public void markOutbound(Long boxId, Long orderId, String orderType) {
        Box oldBox = boxMapper.selectById(boxId);
        if (oldBox == null) return;
        if (ServiceConstants.BoxStatus.OUTBOUND.equals(oldBox.getBoxStatus())) return; // 已出库，幂等
        Box update = new Box();
        update.setId(boxId);
        update.setBoxStatus(ServiceConstants.BoxStatus.OUTBOUND);
        update.setItemCount(countItemsByBoxId(boxId));
        update.setOutboundOrderId(orderId);
        update.setOutboundOrderType(orderType);
        update.setOutboundTime(java.time.LocalDateTime.now());
        update.setItemCount(0);
        update.setWarehouseId(null);
        update.setAreaId(null);
        update.setRackId(null);
        update.setLocationId(null);
        boxMapper.updateById(update);
        // 清空箱内所有器材的 boxId（箱子物理上已离开，剩余器材变为散件）
        LambdaUpdateWrapper<ItemInstance> clearWrapper = Wrappers.lambdaUpdate();
        clearWrapper.eq(ItemInstance::getBoxId, boxId);
        clearWrapper.set(ItemInstance::getBoxId, null);
        itemInstanceMapper.update(null, clearWrapper);
        refreshLocationStatus(oldBox.getLocationId());
        boxCirculationLogService.logEvent(
            boxId, oldBox.getBoxCode(), "OUTBOUND",
            orderId, orderType,
            buildLocationDesc(oldBox), null,
            null
        );
    }


    /**
     * 箱子归还：OUTBOUND → IDLE/PACKED，清除出库关联，写 RETURN 日志
     * 注：不恢复位置，调用方需随后调用 moveTo() 指定归还位置
     */
    @Transactional
    public void markReturn(Long boxId) {
        Box oldBox = boxMapper.selectById(boxId);
        if (oldBox == null) return;
        if (!ServiceConstants.BoxStatus.OUTBOUND.equals(oldBox.getBoxStatus())) {
            return; // 非出库状态无需归还
        }
        int itemCount = (int) countItemsByBoxId(boxId);
        Box update = new Box();
        update.setId(boxId);
        update.setBoxStatus(itemCount > 0 ? ServiceConstants.BoxStatus.PACKED : ServiceConstants.BoxStatus.IDLE);
        update.setOutboundOrderId(null);
        update.setOutboundOrderType(null);
        update.setOutboundTime(null);
        boxMapper.updateById(update);
        boxCirculationLogService.logEvent(
            boxId, oldBox.getBoxCode(), "RETURN",
            oldBox.getOutboundOrderId(), oldBox.getOutboundOrderType(),
            null, null,
            "关联出库单：" + oldBox.getOutboundOrderType() + "#" + oldBox.getOutboundOrderId()
        );
    }

    public void moveTo(Long boxId, Long warehouseId, Long areaId, Long rackId, Long locationId) {
        Box oldBox = boxMapper.selectById(boxId);
        Box update = new Box();
        update.setId(boxId);
        update.setBoxStatus(ServiceConstants.BoxStatus.PACKED);
        update.setItemCount(countItemsByBoxId(boxId));
        update.setWarehouseId(warehouseId);
        update.setAreaId(areaId);
        update.setRackId(rackId);
        update.setLocationId(locationId);
        boxMapper.updateById(update);
        refreshLocationStatus(locationId, oldBox != null ? oldBox.getLocationId() : null);
    }

    /**
     * 库内移位：更新箱子位置，并可选同步移动箱内在库器材
     */
    @Transactional
    public void relocate(Long boxId, Long targetRackId, Long targetLocationId, boolean syncItems) {
        Box box = boxMapper.selectById(boxId);
        cn.hutool.core.lang.Assert.notNull(box, "箱体不存在");
        cn.hutool.core.lang.Assert.isTrue(
            !ServiceConstants.BoxStatus.OUTBOUND.equals(box.getBoxStatus()),
            "箱子当前为出库状态，无法移位"
        );
        String fromDesc = buildLocationDesc(box);

        // 解析目标位置所属仓库/库区
        Long newWarehouseId = box.getWarehouseId();
        Long newAreaId      = box.getAreaId();
        Long newRackId      = targetRackId;
        Long newLocationId  = targetLocationId;
        if (targetLocationId != null) {
            Location loc = locationMapper.selectById(targetLocationId);
            cn.hutool.core.lang.Assert.notNull(loc, "目标货位不存在");
            newRackId     = loc.getRackId();
            newAreaId     = loc.getAreaId();
            newWarehouseId = loc.getWarehouseId();
        } else if (targetRackId != null) {
            Rack rack = rackMapper.selectById(targetRackId);
            cn.hutool.core.lang.Assert.notNull(rack, "目标货架不存在");
            newAreaId      = rack.getAreaId();
            newWarehouseId = rack.getWarehouseId();
        }
        cn.hutool.core.lang.Assert.isTrue(
            newWarehouseId.equals(box.getWarehouseId()),
            "移位不允许跨仓库，请使用调拨单"
        );

        Box update = new Box();
        update.setId(boxId);
        update.setWarehouseId(newWarehouseId);
        update.setAreaId(newAreaId);
        update.setRackId(newRackId);
        update.setLocationId(newLocationId);
        boxMapper.updateById(update);

        // 同步箱内在库器材位置
        if (syncItems) {
            itemInstanceService.lambdaUpdate()
                .eq(ItemInstance::getBoxId, boxId)
                .eq(ItemInstance::getInstanceStatus, "在库")
                .set(ItemInstance::getWarehouseId, newWarehouseId)
                .set(ItemInstance::getAreaId, newAreaId)
                .set(ItemInstance::getRackId, newRackId)
                .set(ItemInstance::getLocationId, newLocationId)
                .update();
        }

        refreshLocationStatus(newLocationId, box.getLocationId());
        String toDesc = buildLocationNameByIds(newWarehouseId, newAreaId, newRackId, newLocationId);
        boxCirculationLogService.logEvent(
            boxId, box.getBoxCode(), "RELOCATE",
            null, null,
            fromDesc, toDesc,
            syncItems ? "含箱内器材同步移位" : "仅箱子移位，器材位置不变"
        );
    }

    private String buildLocationDesc(Box box) {
        return buildLocationNameByIds(box.getWarehouseId(), box.getAreaId(), box.getRackId(), box.getLocationId());
    }

    private String buildLocationNameByIds(Long warehouseId, Long areaId, Long rackId, Long locationId) {
        java.util.List<String> parts = new java.util.ArrayList<>();
        if (warehouseId != null) {
            Warehouse w = warehouseMapper.selectById(warehouseId);
            if (w != null) parts.add(w.getWarehouseName());
        }
        if (areaId != null) {
            Area a = areaMapper.selectById(areaId);
            if (a != null) parts.add(a.getAreaName());
        }
        if (rackId != null) {
            Rack r = rackMapper.selectById(rackId);
            if (r != null) parts.add(r.getRackName());
        }
        if (locationId != null) {
            Location l = locationMapper.selectById(locationId);
            if (l != null) parts.add(l.getLocationName());
        }
        return String.join(" / ", parts);
    }

    public void deleteById(Long id) {
        Box oldBox = boxMapper.selectById(id);
        // 先清空箱内器材的 boxId（解除装箱关系，不影响器材自身位置）
        clearItemsBoxId(id);
        boxMapper.deleteById(id);
        refreshLocationStatus(oldBox != null ? oldBox.getLocationId() : null);
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

    private void refreshLocationStatus(Long... locationIds) {
        List<Long> ids = java.util.Arrays.stream(locationIds)
            .filter(Objects::nonNull)
            .distinct()
            .toList();
        if (CollUtil.isNotEmpty(ids)) {
            locationService.refreshOccupiedFlagsByLocationIds(ids);
        }
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
