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
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.ruoyi.wms.domain.bo.BorrowRecordBo;
import com.ruoyi.wms.domain.entity.Area;
import com.ruoyi.wms.domain.entity.BorrowRecord;
import com.ruoyi.wms.domain.entity.InventoryHistory;
import com.ruoyi.wms.domain.entity.ItemInstance;
import com.ruoyi.wms.domain.entity.Location;
import com.ruoyi.wms.domain.entity.Rack;
import com.ruoyi.wms.domain.entity.Warehouse;
import com.ruoyi.wms.domain.vo.BorrowWarningStatsVo;
import com.ruoyi.wms.domain.vo.BorrowRecordVo;
import com.ruoyi.wms.domain.vo.ItemInstanceVo;
import com.ruoyi.wms.mapper.AreaMapper;
import com.ruoyi.wms.mapper.BorrowRecordMapper;
import com.ruoyi.wms.mapper.LocationMapper;
import com.ruoyi.wms.mapper.RackMapper;
import com.ruoyi.wms.mapper.WarehouseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BorrowRecordService extends ServiceImpl<BorrowRecordMapper, BorrowRecord> {

    private final BorrowRecordMapper borrowRecordMapper;
    private final ItemInstanceService itemInstanceService;
    private final WarehouseMapper warehouseMapper;
    private final AreaMapper areaMapper;
    private final RackMapper rackMapper;
    private final LocationMapper locationMapper;
    private final InventoryHistoryService inventoryHistoryService;

    public BorrowRecordVo queryById(Long id) {
        BorrowRecordVo vo = borrowRecordMapper.selectVoById(id);
        if (vo == null) {
            return null;
        }
        enrich(List.of(vo));
        return vo;
    }

    public BorrowRecordVo queryCurrentByItemInstanceId(Long itemInstanceId) {
        LambdaQueryWrapper<BorrowRecord> lqw = Wrappers.lambdaQuery();
        lqw.eq(BorrowRecord::getItemInstanceId, itemInstanceId);
        lqw.eq(BorrowRecord::getBorrowStatus, ServiceConstants.BorrowStatus.BORROWED);
        lqw.orderByDesc(BorrowRecord::getBorrowTime);
        lqw.last("limit 1");
        BorrowRecordVo vo = borrowRecordMapper.selectVoOne(lqw);
        if (vo == null) {
            return null;
        }
        enrich(List.of(vo));
        return vo;
    }

    public TableDataInfo<BorrowRecordVo> queryPageList(BorrowRecordBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<BorrowRecord> lqw = buildQueryWrapper(bo);
        Page<BorrowRecordVo> result = borrowRecordMapper.selectVoPage(pageQuery.build(), lqw);
        enrich(result.getRecords());
        return TableDataInfo.build(result);
    }

    public List<BorrowRecordVo> queryList(BorrowRecordBo bo) {
        LambdaQueryWrapper<BorrowRecord> lqw = buildQueryWrapper(bo);
        List<BorrowRecordVo> list = borrowRecordMapper.selectVoList(lqw);
        enrich(list);
        return list;
    }

    public BorrowWarningStatsVo queryWarningStats() {
        BorrowWarningStatsVo statsVo = new BorrowWarningStatsVo();

        LambdaQueryWrapper<BorrowRecord> borrowingWrapper = Wrappers.lambdaQuery();
        borrowingWrapper.eq(BorrowRecord::getBorrowStatus, ServiceConstants.BorrowStatus.BORROWED);
        statsVo.setBorrowingCount(borrowRecordMapper.selectCount(borrowingWrapper));

        LambdaQueryWrapper<BorrowRecord> overdueWrapper = Wrappers.lambdaQuery();
        overdueWrapper.eq(BorrowRecord::getBorrowStatus, ServiceConstants.BorrowStatus.BORROWED);
        overdueWrapper.and(wrapper -> wrapper
            .eq(BorrowRecord::getOverdueFlag, 1)
            .or()
            .lt(BorrowRecord::getPlanReturnDate, java.time.LocalDate.now()));
        statsVo.setOverdueCount(borrowRecordMapper.selectCount(overdueWrapper));

        return statsVo;
    }

    @Transactional
    public void borrow(BorrowRecordBo bo) {
        ItemInstance itemInstance = requireBorrowableItem(bo.getItemInstanceId());
        Assert.isNull(findActiveRecordEntity(bo.getItemInstanceId()), "该单品实例已处于借出状态");
        BorrowRecord add = new BorrowRecord();
        add.setItemInstanceId(itemInstance.getId());
        add.setBorrowStatus(ServiceConstants.BorrowStatus.BORROWED);
        add.setBorrower(bo.getBorrower());
        add.setFromUnit(bo.getFromUnit());
        add.setToUnit(bo.getToUnit());
        add.setFromPerson(bo.getFromPerson());
        add.setToPerson(bo.getToPerson());
        add.setDocDate(bo.getDocDate());
        add.setBorrowNo(StrUtil.blankToDefault(bo.getBorrowNo(), generateBorrowNo()));
        add.setPlanReturnDate(bo.getPlanReturnDate());
        add.setInstanceCode(StrUtil.blankToDefault(bo.getInstanceCode(), itemInstance.getInstanceCode()));
        add.setBorrowTime(bo.getBorrowTime() == null ? LocalDateTime.now() : bo.getBorrowTime());
        add.setBorrowRemark(bo.getBorrowRemark());
        add.setOriginalWarehouseId(itemInstance.getWarehouseId());
        add.setOriginalAreaId(itemInstance.getAreaId());
        add.setOriginalRackId(itemInstance.getRackId());
        add.setOriginalLocationId(itemInstance.getLocationId());
        fillOverdueFields(add, add.getBorrowTime(), null);
        borrowRecordMapper.insert(add);
        itemInstanceService.markBorrowed(itemInstance.getId());
        createBorrowHistory(add, itemInstance);
    }

    @Transactional
    public void returnItem(BorrowRecordBo bo) {
        BorrowRecord borrowRecord = resolveActiveRecord(bo);
        validateOriginalLocationStillAvailable(borrowRecord);
        itemInstanceService.restoreFromBorrow(
            borrowRecord.getItemInstanceId(),
            borrowRecord.getOriginalWarehouseId(),
            borrowRecord.getOriginalAreaId(),
            borrowRecord.getOriginalRackId(),
            borrowRecord.getOriginalLocationId()
        );
        BorrowRecord update = new BorrowRecord();
        update.setId(borrowRecord.getId());
        update.setBorrowStatus(ServiceConstants.BorrowStatus.RETURNED);
        update.setReturnTime(bo.getReturnTime() == null ? LocalDateTime.now() : bo.getReturnTime());
        update.setReturnRemark(bo.getReturnRemark());
        update.setReturnedWarehouseId(borrowRecord.getOriginalWarehouseId());
        update.setReturnedAreaId(borrowRecord.getOriginalAreaId());
        update.setReturnedRackId(borrowRecord.getOriginalRackId());
        update.setReturnedLocationId(borrowRecord.getOriginalLocationId());
        fillOverdueFields(update, borrowRecord.getBorrowTime(), update.getReturnTime() == null ? LocalDateTime.now() : update.getReturnTime());
        borrowRecordMapper.updateById(update);
        borrowRecord.setReturnTime(update.getReturnTime());
        borrowRecord.setReturnRemark(update.getReturnRemark());
        ItemInstance itemInstance = itemInstanceService.getById(borrowRecord.getItemInstanceId());
        if (itemInstance != null) {
            createReturnHistory(borrowRecord, itemInstance, update.getReturnTime());
        }
    }

    private LambdaQueryWrapper<BorrowRecord> buildQueryWrapper(BorrowRecordBo bo) {
        LambdaQueryWrapper<BorrowRecord> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getItemInstanceId() != null, BorrowRecord::getItemInstanceId, bo.getItemInstanceId());
        lqw.eq(StrUtil.isNotBlank(bo.getBorrowStatus()), BorrowRecord::getBorrowStatus, bo.getBorrowStatus());
        lqw.eq(StrUtil.isNotBlank(bo.getBorrowNo()), BorrowRecord::getBorrowNo, bo.getBorrowNo());
        lqw.eq(StrUtil.isNotBlank(bo.getInstanceCode()), BorrowRecord::getInstanceCode, bo.getInstanceCode());
        lqw.like(StrUtil.isNotBlank(bo.getBorrower()), BorrowRecord::getBorrower, bo.getBorrower());
        lqw.like(StrUtil.isNotBlank(bo.getFromUnit()), BorrowRecord::getFromUnit, bo.getFromUnit());
        lqw.like(StrUtil.isNotBlank(bo.getToUnit()), BorrowRecord::getToUnit, bo.getToUnit());
        lqw.eq(bo.getDocDate() != null, BorrowRecord::getDocDate, bo.getDocDate());
        lqw.eq(bo.getPlanReturnDate() != null, BorrowRecord::getPlanReturnDate, bo.getPlanReturnDate());
        if (bo.getOverdueFlag() != null) {
            if (Integer.valueOf(1).equals(bo.getOverdueFlag())) {
                lqw.and(wrapper -> wrapper
                    .eq(BorrowRecord::getOverdueFlag, 1)
                    .or()
                    .eq(BorrowRecord::getBorrowStatus, ServiceConstants.BorrowStatus.BORROWED)
                    .lt(BorrowRecord::getPlanReturnDate, java.time.LocalDate.now()));
            } else {
                lqw.and(wrapper -> wrapper
                    .and(inner -> inner.isNull(BorrowRecord::getPlanReturnDate)
                        .or()
                        .eq(BorrowRecord::getBorrowStatus, ServiceConstants.BorrowStatus.RETURNED)
                        .or()
                        .ge(BorrowRecord::getPlanReturnDate, java.time.LocalDate.now()))
                    .and(inner -> inner.isNull(BorrowRecord::getOverdueFlag)
                        .or()
                        .eq(BorrowRecord::getOverdueFlag, 0)));
            }
        }
        lqw.orderByDesc(BorrowRecord::getBorrowTime);
        return lqw;
    }

    private ItemInstance requireBorrowableItem(Long itemInstanceId) {
        ItemInstance itemInstance = itemInstanceService.getById(itemInstanceId);
        Assert.notNull(itemInstance, "单品实例不存在");
        Assert.isTrue(itemInstance.getBoxId() == null, "单品实例在箱内，不能直接借出");
        Assert.isFalse(ServiceConstants.ItemInstanceStatus.BORROWED.equals(itemInstance.getInstanceStatus()), "单品实例已借出");
        Assert.isTrue(ServiceConstants.ItemInstanceStatus.IN_STOCK.equals(itemInstance.getInstanceStatus()), "仅在库单品可以借出");
        return itemInstance;
    }

    private BorrowRecord resolveActiveRecord(BorrowRecordBo bo) {
        if (bo.getId() != null) {
            BorrowRecord borrowRecord = borrowRecordMapper.selectById(bo.getId());
            Assert.notNull(borrowRecord, "借还记录不存在");
            Assert.isTrue(ServiceConstants.BorrowStatus.BORROWED.equals(borrowRecord.getBorrowStatus()), "该借还记录已归还");
            return borrowRecord;
        }
        Assert.notNull(bo.getItemInstanceId(), "归还时借还记录ID或单品实例ID至少传一个");
        BorrowRecord borrowRecord = findActiveRecordEntity(bo.getItemInstanceId());
        Assert.notNull(borrowRecord, "当前单品不存在未归还借用记录");
        return borrowRecord;
    }

    private BorrowRecord findActiveRecordEntity(Long itemInstanceId) {
        LambdaQueryWrapper<BorrowRecord> lqw = Wrappers.lambdaQuery();
        lqw.eq(BorrowRecord::getItemInstanceId, itemInstanceId);
        lqw.eq(BorrowRecord::getBorrowStatus, ServiceConstants.BorrowStatus.BORROWED);
        lqw.orderByDesc(BorrowRecord::getBorrowTime);
        lqw.last("limit 1");
        return borrowRecordMapper.selectOne(lqw);
    }

    private void validateOriginalLocationStillAvailable(BorrowRecord borrowRecord) {
        if (borrowRecord.getOriginalLocationId() != null) {
            Location location = locationMapper.selectById(borrowRecord.getOriginalLocationId());
            Assert.notNull(location, "原货位已不存在，无法自动归还到原位");
            Assert.isTrue(Objects.equals(location.getRackId(), borrowRecord.getOriginalRackId()), "原货位结构已变化，无法自动归还");
            Assert.isTrue(Objects.equals(location.getAreaId(), borrowRecord.getOriginalAreaId()), "原货位所属库区已变化，无法自动归还");
            Assert.isTrue(Objects.equals(location.getWarehouseId(), borrowRecord.getOriginalWarehouseId()), "原货位所属仓库已变化，无法自动归还");
        }
    }

    private void enrich(List<BorrowRecordVo> list) {
        if (CollUtil.isEmpty(list)) {
            return;
        }
        List<BorrowRecordVo> validList = list.stream().filter(Objects::nonNull).toList();
        if (CollUtil.isEmpty(validList)) {
            return;
        }
        Set<Long> itemInstanceIds = validList.stream().map(BorrowRecordVo::getItemInstanceId).filter(Objects::nonNull).collect(Collectors.toSet());
        Set<Long> originalWarehouseIds = validList.stream().map(BorrowRecordVo::getOriginalWarehouseId).filter(Objects::nonNull).collect(Collectors.toSet());
        Set<Long> originalAreaIds = validList.stream().map(BorrowRecordVo::getOriginalAreaId).filter(Objects::nonNull).collect(Collectors.toSet());
        Set<Long> originalRackIds = validList.stream().map(BorrowRecordVo::getOriginalRackId).filter(Objects::nonNull).collect(Collectors.toSet());
        Set<Long> originalLocationIds = validList.stream().map(BorrowRecordVo::getOriginalLocationId).filter(Objects::nonNull).collect(Collectors.toSet());
        Set<Long> returnedWarehouseIds = validList.stream().map(BorrowRecordVo::getReturnedWarehouseId).filter(Objects::nonNull).collect(Collectors.toSet());
        Set<Long> returnedAreaIds = validList.stream().map(BorrowRecordVo::getReturnedAreaId).filter(Objects::nonNull).collect(Collectors.toSet());
        Set<Long> returnedRackIds = validList.stream().map(BorrowRecordVo::getReturnedRackId).filter(Objects::nonNull).collect(Collectors.toSet());
        Set<Long> returnedLocationIds = validList.stream().map(BorrowRecordVo::getReturnedLocationId).filter(Objects::nonNull).collect(Collectors.toSet());
        Map<Long, ItemInstanceVo> itemMap = itemInstanceService.queryVosByIds(itemInstanceIds).stream()
            .collect(Collectors.toMap(ItemInstanceVo::getId, Function.identity()));
        Map<Long, Warehouse> warehouseMap = mergeWarehouseMap(originalWarehouseIds, returnedWarehouseIds);
        Map<Long, Area> areaMap = mergeAreaMap(originalAreaIds, returnedAreaIds);
        Map<Long, Rack> rackMap = mergeRackMap(originalRackIds, returnedRackIds);
        Map<Long, Location> locationMap = mergeLocationMap(originalLocationIds, returnedLocationIds);
        validList.forEach(vo -> {
            ItemInstanceVo item = itemMap.get(vo.getItemInstanceId());
            if (item != null) {
                vo.setInstanceCode(item.getInstanceCode());
                vo.setItemName(item.getItemName());
                vo.setSkuName(item.getSkuName());
            }
            fillOverdueFields(vo, vo.getBorrowTime(), vo.getReturnTime());
            fillWarehouseName(vo, warehouseMap);
            fillAreaName(vo, areaMap);
            fillRackName(vo, rackMap);
            fillLocationName(vo, locationMap);
        });
    }

    private Map<Long, Warehouse> mergeWarehouseMap(Set<Long> firstIds, Set<Long> secondIds) {
        Set<Long> ids = CollUtil.newHashSet();
        ids.addAll(firstIds);
        ids.addAll(secondIds);
        if (ids.isEmpty()) {
            return java.util.Collections.emptyMap();
        }
        return warehouseMapper.selectBatchIds(ids).stream().collect(Collectors.toMap(Warehouse::getId, Function.identity()));
    }

    private Map<Long, Area> mergeAreaMap(Set<Long> firstIds, Set<Long> secondIds) {
        Set<Long> ids = CollUtil.newHashSet();
        ids.addAll(firstIds);
        ids.addAll(secondIds);
        if (ids.isEmpty()) {
            return java.util.Collections.emptyMap();
        }
        return areaMapper.selectBatchIds(ids).stream().collect(Collectors.toMap(Area::getId, Function.identity()));
    }

    private Map<Long, Rack> mergeRackMap(Set<Long> firstIds, Set<Long> secondIds) {
        Set<Long> ids = CollUtil.newHashSet();
        ids.addAll(firstIds);
        ids.addAll(secondIds);
        if (ids.isEmpty()) {
            return java.util.Collections.emptyMap();
        }
        return rackMapper.selectBatchIds(ids).stream().collect(Collectors.toMap(Rack::getId, Function.identity()));
    }

    private Map<Long, Location> mergeLocationMap(Set<Long> firstIds, Set<Long> secondIds) {
        Set<Long> ids = CollUtil.newHashSet();
        ids.addAll(firstIds);
        ids.addAll(secondIds);
        if (ids.isEmpty()) {
            return java.util.Collections.emptyMap();
        }
        return locationMapper.selectBatchIds(ids).stream().collect(Collectors.toMap(Location::getId, Function.identity()));
    }

    private void fillWarehouseName(BorrowRecordVo vo, Map<Long, Warehouse> warehouseMap) {
        Warehouse originalWarehouse = warehouseMap.get(vo.getOriginalWarehouseId());
        if (originalWarehouse != null) {
            vo.setOriginalWarehouseName(originalWarehouse.getWarehouseName());
        }
        Warehouse returnedWarehouse = warehouseMap.get(vo.getReturnedWarehouseId());
        if (returnedWarehouse != null) {
            vo.setReturnedWarehouseName(returnedWarehouse.getWarehouseName());
        }
    }

    private void fillAreaName(BorrowRecordVo vo, Map<Long, Area> areaMap) {
        Area originalArea = areaMap.get(vo.getOriginalAreaId());
        if (originalArea != null) {
            vo.setOriginalAreaName(originalArea.getAreaName());
        }
        Area returnedArea = areaMap.get(vo.getReturnedAreaId());
        if (returnedArea != null) {
            vo.setReturnedAreaName(returnedArea.getAreaName());
        }
    }

    private void fillRackName(BorrowRecordVo vo, Map<Long, Rack> rackMap) {
        Rack originalRack = rackMap.get(vo.getOriginalRackId());
        if (originalRack != null) {
            vo.setOriginalRackName(originalRack.getRackName());
        }
        Rack returnedRack = rackMap.get(vo.getReturnedRackId());
        if (returnedRack != null) {
            vo.setReturnedRackName(returnedRack.getRackName());
        }
    }

    private void fillLocationName(BorrowRecordVo vo, Map<Long, Location> locationMap) {
        Location originalLocation = locationMap.get(vo.getOriginalLocationId());
        if (originalLocation != null) {
            vo.setOriginalLocationName(originalLocation.getLocationName());
        }
        Location returnedLocation = locationMap.get(vo.getReturnedLocationId());
        if (returnedLocation != null) {
            vo.setReturnedLocationName(returnedLocation.getLocationName());
        }
    }

    private void createBorrowHistory(BorrowRecord borrowRecord, ItemInstance itemInstance) {
        InventoryHistory history = new InventoryHistory();
        history.setOrderId(borrowRecord.getId());
        history.setOrderNo(StrUtil.blankToDefault(borrowRecord.getBorrowNo(), String.valueOf(borrowRecord.getId())));
        history.setOrderType(ServiceConstants.InventoryHistoryOrderType.BORROW);
        history.setSkuId(itemInstance.getSkuId());
        history.setQuantity(java.math.BigDecimal.ONE.negate());
        history.setWarehouseId(borrowRecord.getOriginalWarehouseId());
        history.setAreaId(borrowRecord.getOriginalAreaId());
        history.setRackId(borrowRecord.getOriginalRackId());
        history.setLocationId(borrowRecord.getOriginalLocationId());
        history.setItemInstanceId(borrowRecord.getItemInstanceId());
        history.setBoxId(itemInstance.getBoxId());
        history.setOperationType("borrow");
        history.setOperatorName(StrUtil.blankToDefault(borrowRecord.getBorrower(), borrowRecord.getCreateBy()));
        history.setRemark(StrUtil.blankToDefault(borrowRecord.getBorrowRemark(), "借出登记"));
        history.setCreateTime(borrowRecord.getBorrowTime());
        inventoryHistoryService.save(history);
    }

    private void createReturnHistory(BorrowRecord borrowRecord, ItemInstance itemInstance, LocalDateTime returnTime) {
        InventoryHistory history = new InventoryHistory();
        history.setOrderId(borrowRecord.getId());
        history.setOrderNo(StrUtil.blankToDefault(borrowRecord.getBorrowNo(), String.valueOf(borrowRecord.getId())));
        history.setOrderType(ServiceConstants.InventoryHistoryOrderType.RETURN);
        history.setSkuId(itemInstance.getSkuId());
        history.setQuantity(java.math.BigDecimal.ONE);
        history.setWarehouseId(borrowRecord.getOriginalWarehouseId());
        history.setAreaId(borrowRecord.getOriginalAreaId());
        history.setRackId(borrowRecord.getOriginalRackId());
        history.setLocationId(borrowRecord.getOriginalLocationId());
        history.setItemInstanceId(borrowRecord.getItemInstanceId());
        history.setBoxId(itemInstance.getBoxId());
        history.setOperationType("return");
        history.setOperatorName(StrUtil.blankToDefault(borrowRecord.getBorrower(), borrowRecord.getUpdateBy()));
        history.setRemark(StrUtil.blankToDefault(borrowRecord.getReturnRemark(), "归还登记"));
        history.setCreateTime(returnTime);
        inventoryHistoryService.save(history);
    }

    private void fillOverdueFields(BorrowRecord borrowRecord, LocalDateTime borrowTime, LocalDateTime returnTime) {
        if (borrowRecord.getPlanReturnDate() == null) {
            borrowRecord.setOverdueFlag(0);
            borrowRecord.setOverdueDays(0);
            return;
        }
        LocalDateTime compareTime = returnTime == null ? LocalDateTime.now() : returnTime;
        int overdueDays = (int) java.time.temporal.ChronoUnit.DAYS.between(
            borrowRecord.getPlanReturnDate().atStartOfDay(),
            compareTime
        );
        borrowRecord.setOverdueFlag(overdueDays > 0 ? 1 : 0);
        borrowRecord.setOverdueDays(Math.max(overdueDays, 0));
        if (borrowTime != null && compareTime.isBefore(borrowTime)) {
            borrowRecord.setOverdueFlag(0);
            borrowRecord.setOverdueDays(0);
        }
    }

    private void fillOverdueFields(BorrowRecordVo borrowRecord, LocalDateTime borrowTime, LocalDateTime returnTime) {
        if (borrowRecord.getPlanReturnDate() == null) {
            borrowRecord.setOverdueFlag(0);
            borrowRecord.setOverdueDays(0);
            return;
        }
        LocalDateTime compareTime = returnTime == null ? LocalDateTime.now() : returnTime;
        int overdueDays = (int) java.time.temporal.ChronoUnit.DAYS.between(
            borrowRecord.getPlanReturnDate().atStartOfDay(),
            compareTime
        );
        borrowRecord.setOverdueFlag(overdueDays > 0 ? 1 : 0);
        borrowRecord.setOverdueDays(Math.max(overdueDays, 0));
        if (borrowTime != null && compareTime.isBefore(borrowTime)) {
            borrowRecord.setOverdueFlag(0);
            borrowRecord.setOverdueDays(0);
        }
    }

    private String generateBorrowNo() {
        return "BR" + IdUtil.getSnowflakeNextIdStr();
    }
}
