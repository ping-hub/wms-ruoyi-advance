package com.ruoyi.wms.service;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.utils.MapstructUtils;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.wms.domain.bo.BorrowOrderDetailBo;
import com.ruoyi.wms.domain.entity.Area;
import com.ruoyi.wms.domain.entity.BorrowOrderDetail;
import com.ruoyi.wms.domain.entity.Location;
import com.ruoyi.wms.domain.entity.Rack;
import com.ruoyi.wms.domain.entity.Warehouse;
import com.ruoyi.wms.domain.vo.BorrowOrderDetailVo;
import com.ruoyi.wms.domain.vo.ItemSkuVo;
import com.ruoyi.wms.mapper.AreaMapper;
import com.ruoyi.wms.mapper.BorrowOrderDetailMapper;
import com.ruoyi.wms.mapper.LocationMapper;
import com.ruoyi.wms.mapper.RackMapper;
import com.ruoyi.wms.mapper.WarehouseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 器材借用单明细Service
 */
@RequiredArgsConstructor
@Service
public class BorrowOrderDetailService extends ServiceImpl<BorrowOrderDetailMapper, BorrowOrderDetail> {

    private final BorrowOrderDetailMapper borrowOrderDetailMapper;
    private final ItemSkuService itemSkuService;
    private final WarehouseMapper warehouseMapper;
    private final AreaMapper areaMapper;
    private final RackMapper rackMapper;
    private final LocationMapper locationMapper;

    public BorrowOrderDetailVo queryById(Long id) {
        BorrowOrderDetailVo vo = borrowOrderDetailMapper.selectVoById(id);
        enrich(List.of(vo));
        return vo;
    }

    public List<BorrowOrderDetailVo> queryByBorrowOrderId(Long borrowOrderId) {
        LambdaQueryWrapper<BorrowOrderDetail> lqw = Wrappers.lambdaQuery();
        lqw.eq(BorrowOrderDetail::getBorrowOrderId, borrowOrderId);
        List<BorrowOrderDetailVo> list = borrowOrderDetailMapper.selectVoList(lqw);
        enrich(list);
        return list;
    }

    @Transactional
    public void saveDetails(List<BorrowOrderDetail> list) {
        if (CollUtil.isEmpty(list)) {
            return;
        }
        saveOrUpdateBatch(list);
    }

    public void deleteByIds(Collection<Long> ids) {
        borrowOrderDetailMapper.deleteBatchIds(ids);
    }

    private void enrich(List<BorrowOrderDetailVo> list) {
        if (CollUtil.isEmpty(list)) {
            return;
        }
        // Fill SKU snapshot fields
        Set<Long> skuIds = list.stream().map(BorrowOrderDetailVo::getSkuId).filter(Objects::nonNull).collect(Collectors.toSet());
        Map<Long, ItemSkuVo> skuMap = skuIds.isEmpty() ? Map.of() :
            itemSkuService.queryVosByIds(skuIds).stream()
                .collect(Collectors.toMap(ItemSkuVo::getId, Function.identity()));
        list.forEach(vo -> {
            ItemSkuVo sku = skuMap.get(vo.getSkuId());
            if (sku != null) {
                if (StringUtils.isBlank(vo.getSkuName())) vo.setSkuName(sku.getSkuName());
                if (StringUtils.isBlank(vo.getProductIdentifier())) vo.setProductIdentifier(sku.getProductIdentifier());
                if (sku.getItem() != null) {
                    if (StringUtils.isBlank(vo.getItemCode())) vo.setItemCode(sku.getItem().getItemCode());
                    if (StringUtils.isBlank(vo.getItemName())) vo.setItemName(sku.getItem().getItemName());
                    if (StringUtils.isBlank(vo.getUnit())) vo.setUnit(sku.getItem().getUnit());
                }
            }
        });
        // Fill location names
        Set<Long> warehouseIds = list.stream().map(BorrowOrderDetailVo::getWarehouseId).filter(Objects::nonNull).collect(Collectors.toSet());
        Set<Long> areaIds = list.stream().map(BorrowOrderDetailVo::getAreaId).filter(Objects::nonNull).collect(Collectors.toSet());
        Set<Long> rackIds = list.stream().map(BorrowOrderDetailVo::getRackId).filter(Objects::nonNull).collect(Collectors.toSet());
        Set<Long> locationIds = list.stream().map(BorrowOrderDetailVo::getLocationId).filter(Objects::nonNull).collect(Collectors.toSet());
        Map<Long, Warehouse> warehouseMap = warehouseIds.isEmpty() ? Map.of() :
            warehouseMapper.selectBatchIds(warehouseIds).stream().collect(Collectors.toMap(Warehouse::getId, Function.identity()));
        Map<Long, Area> areaMap = areaIds.isEmpty() ? Map.of() :
            areaMapper.selectBatchIds(areaIds).stream().collect(Collectors.toMap(Area::getId, Function.identity()));
        Map<Long, Rack> rackMap = rackIds.isEmpty() ? Map.of() :
            rackMapper.selectBatchIds(rackIds).stream().collect(Collectors.toMap(Rack::getId, Function.identity()));
        Map<Long, Location> locationMap = locationIds.isEmpty() ? Map.of() :
            locationMapper.selectBatchIds(locationIds).stream().collect(Collectors.toMap(Location::getId, Function.identity()));
        list.forEach(vo -> {
            Warehouse w = warehouseMap.get(vo.getWarehouseId());
            if (w != null) vo.setWarehouseName(w.getWarehouseName());
            Area a = areaMap.get(vo.getAreaId());
            if (a != null) vo.setAreaName(a.getAreaName());
            Rack r = rackMap.get(vo.getRackId());
            if (r != null) vo.setRackName(r.getRackName());
            Location l = locationMap.get(vo.getLocationId());
            if (l != null) vo.setLocationName(l.getLocationName());
        });
    }
}
