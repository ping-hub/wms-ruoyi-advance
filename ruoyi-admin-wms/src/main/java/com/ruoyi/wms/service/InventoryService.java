package com.ruoyi.wms.service;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.utils.MapstructUtils;
import com.ruoyi.common.core.utils.ValidatorUtils;
import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.ruoyi.wms.domain.bo.InventoryBo;
import com.ruoyi.wms.domain.entity.Inventory;
import com.ruoyi.wms.domain.vo.InventoryVo;
import com.ruoyi.wms.mapper.InventoryMapper;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.Set;

/**
 * 库存Service业务层处理
 *
 * @author ping
 * @date 2024-07-19
 */
@RequiredArgsConstructor
@Service
public class InventoryService extends ServiceImpl<InventoryMapper, Inventory> {

    private final InventoryMapper inventoryMapper;

    /**
     * 查询库存
     */
    public InventoryVo queryById(Long id){
        return inventoryMapper.selectVoById(id);
    }

    /**
     * 查询库存列表
     */
    public List<InventoryVo> queryList(InventoryBo bo) {
        LambdaQueryWrapper<Inventory> lqw = buildQueryWrapper(bo);
        return inventoryMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<Inventory> buildQueryWrapper(InventoryBo bo) {
        LambdaQueryWrapper<Inventory> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(bo.getSkuId() != null, Inventory::getSkuId, bo.getSkuId());
        wrapper.eq(bo.getWarehouseId() != null, Inventory::getWarehouseId, bo.getWarehouseId());
        wrapper.eq(bo.getAreaId() != null, Inventory::getAreaId, bo.getAreaId());
        wrapper.eq(bo.getRackId() != null, Inventory::getRackId, bo.getRackId());
        wrapper.eq(bo.getLocationId() != null, Inventory::getLocationId, bo.getLocationId());
        wrapper.eq(bo.getQuantity() != null, Inventory::getQuantity, bo.getQuantity());
        return wrapper;
    }

    /**
     * 修改库存（仅限管理后台手动维护）
     */
    public void updateByBo(InventoryBo bo) {
        Inventory update = MapstructUtils.convert(bo, Inventory.class);
        inventoryMapper.updateById(update);
    }

    /**
     * 批量删除库存
     */
    public void deleteByIds(Collection<Long> ids) {
        inventoryMapper.deleteBatchIds(ids);
    }

    /**
     * 批量更新库存数量（原子SQL，支持多实例部署并发安全）
     * 逻辑：已有记录 → quantity += delta；无记录 → 插入新记录；结果归零 → 删除
     * 优化：合并为 3~4 次 SQL（批量SELECT + 批量UPDATE + 批量INSERT + 批量DELETE），替代逐条循环的 2~3N 次
     * @param list 库存变动列表（quantity 正=入库/归还，负=出库/借出）
     */
    @Transactional
    public void updateInventoryQuantity(List<InventoryBo> list) {
        if (CollUtil.isEmpty(list)) return;
        list.forEach(inventoryBo -> ValidatorUtils.validate(inventoryBo, AddGroup.class));

        // 0. 按唯一键合并同key的变动记录，避免批量INSERT时duplicate key冲突
        list = mergeByInventoryKey(list);

        // 1. 一次 SELECT 查出已存在的库存记录
        List<Inventory> existingList = queryExistingInventories(list);
        Set<String> existingKeys = existingList.stream()
            .map(this::buildInventoryKey)
            .collect(java.util.stream.Collectors.toSet());

        // 2. 已有记录 → 一次批量 UPDATE（行锁保障并发安全）
        List<InventoryBo> existingBos = list.stream()
            .filter(bo -> existingKeys.contains(buildBoKey(bo)))
            .toList();
        if (!existingBos.isEmpty()) {
            inventoryMapper.batchAtomicIncrement(existingBos);
        }

        // 3. 无记录且增量为正 → 批量 INSERT
        List<InventoryBo> newBos = list.stream()
            .filter(bo -> !existingKeys.contains(buildBoKey(bo))
                && bo.getQuantity().compareTo(java.math.BigDecimal.ZERO) > 0)
            .toList();
        if (!newBos.isEmpty()) {
            List<Inventory> insertList = newBos.stream()
                .map(bo -> MapstructUtils.convert(bo, Inventory.class))
                .toList();
            saveBatch(insertList);
        }

        // 4. 一次批量 DELETE 清理归零/负数记录
        inventoryMapper.batchDeleteZeroQuantity(list);
    }

    /**
     * 批量查询已存在的库存记录
     */
    private List<Inventory> queryExistingInventories(List<InventoryBo> list) {
        if (list.size() <= 100) {
            LambdaQueryWrapper<Inventory> lqw = Wrappers.lambdaQuery();
            lqw.or();
            for (InventoryBo bo : list) {
                lqw.or(w -> {
                    w.eq(Inventory::getWarehouseId, bo.getWarehouseId())
                     .eq(Inventory::getAreaId, bo.getAreaId())
                     .eq(Inventory::getSkuId, bo.getSkuId());
                    if (bo.getRackId() != null) w.eq(Inventory::getRackId, bo.getRackId());
                    else w.isNull(Inventory::getRackId);
                    if (bo.getLocationId() != null) w.eq(Inventory::getLocationId, bo.getLocationId());
                    else w.isNull(Inventory::getLocationId);
                });
            }
            return inventoryMapper.selectList(lqw);
        }
        // 超过 100 条时分批查询，避免 SQL 过长
        List<Inventory> result = new java.util.ArrayList<>();
        for (int i = 0; i < list.size(); i += 100) {
            result.addAll(queryExistingInventories(list.subList(i, Math.min(i + 100, list.size()))));
        }
        return result;
    }

    private String buildInventoryKey(Inventory inv) {
        return inv.getWarehouseId() + "_" + inv.getAreaId() + "_" + inv.getRackId() + "_" + inv.getLocationId() + "_" + inv.getSkuId();
    }

    private String buildBoKey(InventoryBo bo) {
        return bo.getWarehouseId() + "_" + bo.getAreaId() + "_" + bo.getRackId() + "_" + bo.getLocationId() + "_" + bo.getSkuId();
    }

    /**
     * 按唯一键 (warehouseId, areaId, rackId, locationId, skuId) 合并变动记录，
     * 将同key的多条记录quantity求和为一条，避免批量INSERT时duplicate key冲突。
     */
    private List<InventoryBo> mergeByInventoryKey(List<InventoryBo> list) {
        java.util.LinkedHashMap<String, InventoryBo> merged = new java.util.LinkedHashMap<>();
        for (InventoryBo bo : list) {
            String key = buildBoKey(bo);
            InventoryBo existing = merged.get(key);
            if (existing != null) {
                existing.setQuantity(existing.getQuantity().add(bo.getQuantity()));
            } else {
                InventoryBo copy = new InventoryBo();
                copy.setWarehouseId(bo.getWarehouseId());
                copy.setAreaId(bo.getAreaId());
                copy.setRackId(bo.getRackId());
                copy.setLocationId(bo.getLocationId());
                copy.setSkuId(bo.getSkuId());
                copy.setQuantity(bo.getQuantity());
                merged.put(key, copy);
            }
        }
        return new java.util.ArrayList<>(merged.values());
    }

    /**
     * 单条库存增减（供借出/归还等业务调用，原子操作，并发安全）
     * @param delta 正=增加（归还），负=减少（借出）
     */
    @Transactional
    public void adjustQuantityBySkuAndPlace(Long warehouseId, Long areaId, Long rackId,
                                            Long locationId, Long skuId, java.math.BigDecimal delta) {
        if (delta == null || delta.compareTo(java.math.BigDecimal.ZERO) == 0) return;
        int rows = inventoryMapper.atomicIncrement(warehouseId, areaId, rackId, locationId, skuId, delta);
        if (rows == 0 && delta.compareTo(java.math.BigDecimal.ZERO) > 0) {
            Inventory inventory = new Inventory();
            inventory.setWarehouseId(warehouseId);
            inventory.setAreaId(areaId);
            inventory.setRackId(rackId);
            inventory.setLocationId(locationId);
            inventory.setSkuId(skuId);
            inventory.setQuantity(delta);
            inventoryMapper.insert(inventory);
        }
        inventoryMapper.deleteZeroQuantity(warehouseId, areaId, rackId, locationId, skuId);
    }

    /**
     * 校验规格是否有库存
     * @param skuIds
     * @return
     */
    public boolean existsBySkuIds(@NotEmpty Collection<Long> skuIds) {
        LambdaQueryWrapper<Inventory> lqw = Wrappers.lambdaQuery();
        lqw.in(Inventory::getSkuId, skuIds);
        return inventoryMapper.exists(lqw);
    }

    /**
     * 校验该库区是否有库存
     * @param areaIds
     * @return
     */
    public boolean existsByAreaIds(@NotEmpty Collection<Long> areaIds) {
        LambdaQueryWrapper<Inventory> lqw = Wrappers.lambdaQuery();
        lqw.in(Inventory::getAreaId, areaIds);
        return inventoryMapper.exists(lqw);
    }

    public TableDataInfo<InventoryVo> queryAreaBoardList(InventoryBo bo, PageQuery pageQuery) {
        Page<InventoryVo> result = inventoryMapper.queryAreaBoardList(pageQuery.build(), bo);
        return TableDataInfo.build(result);
    }

    /**
     * 库存汇总查询：按 sku_id + warehouse_id + area_id 聚合数量，服务端分页
     */
    public TableDataInfo<InventoryVo> querySummaryList(InventoryBo bo, PageQuery pageQuery) {
        Page<InventoryVo> result = inventoryMapper.querySummaryList(pageQuery.build(), bo);
        return TableDataInfo.build(result);
    }

}
