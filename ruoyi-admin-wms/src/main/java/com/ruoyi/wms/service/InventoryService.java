package com.ruoyi.wms.service;

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
     * @param list 库存变动列表（quantity 正=入库/归还，负=出库/借出）
     */
    @Transactional
    public void updateInventoryQuantity(List<InventoryBo> list) {
        list.forEach(inventoryBo -> {
            ValidatorUtils.validate(inventoryBo, AddGroup.class);
        });
        list.forEach(bo -> {
            int rows = inventoryMapper.atomicIncrement(
                bo.getWarehouseId(), bo.getAreaId(),
                bo.getRackId(), bo.getLocationId(),
                bo.getSkuId(), bo.getQuantity());
            if (rows == 0 && bo.getQuantity().compareTo(java.math.BigDecimal.ZERO) > 0) {
                // 记录不存在且增量为正：插入新记录
                Inventory inventory = MapstructUtils.convert(bo, Inventory.class);
                inventoryMapper.insert(inventory);
            }
            // 归零或负数：删除该记录（明细与流水保留）
            inventoryMapper.deleteZeroQuantity(
                bo.getWarehouseId(), bo.getAreaId(),
                bo.getRackId(), bo.getLocationId(),
                bo.getSkuId());
        });
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
}
