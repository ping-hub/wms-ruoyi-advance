package com.ruoyi.wms.service;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.exception.base.BaseException;
import com.ruoyi.common.core.utils.MapstructUtils;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.ruoyi.wms.domain.entity.ItemInstance;
import com.ruoyi.wms.domain.vo.ItemSkuVo;
import com.ruoyi.wms.mapper.ItemInstanceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.ruoyi.wms.domain.bo.InventoryDetailBo;
import com.ruoyi.wms.domain.entity.InventoryDetail;
import com.ruoyi.wms.domain.entity.Location;
import com.ruoyi.wms.domain.entity.Rack;
import com.ruoyi.wms.domain.vo.InventoryDetailVo;
import com.ruoyi.wms.mapper.InventoryDetailMapper;
import com.ruoyi.wms.mapper.LocationMapper;
import com.ruoyi.wms.mapper.RackMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 库存详情Service业务层处理
 *
 * @author zcc
 * @date 2024-07-22
 */
@RequiredArgsConstructor
@Service
public class InventoryDetailService extends ServiceImpl<InventoryDetailMapper, InventoryDetail> {

    private final InventoryDetailMapper inventoryDetailMapper;
    private final ItemSkuService itemSkuService;
    private final ItemInstanceMapper itemInstanceMapper;
    private final RackMapper rackMapper;
    private final LocationMapper locationMapper;

    /**
     * 查询库存详情
     */
    public InventoryDetailVo queryById(Long id){
        return inventoryDetailMapper.selectVoById(id);
    }

    /**
     * 查询库存详情列表
     */
    public TableDataInfo<InventoryDetailVo> queryPageList(InventoryDetailBo bo, PageQuery pageQuery) {
        Page<InventoryDetailVo> result = inventoryDetailMapper.selectPageByBo(pageQuery.build(), bo);
        enrich(result.getRecords());
        return TableDataInfo.build(result);
    }

    /**
     * 查询库存详情列表
     */
    public List<InventoryDetailVo> queryList(InventoryDetailBo bo) {
        List<InventoryDetailVo> vos = inventoryDetailMapper.selectListByBo(bo);
        enrich(vos);
        return vos;
    }

    public List<InventoryDetailVo> queryVoListByIds(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        List<InventoryDetailVo> vos = MapstructUtils.convert(inventoryDetailMapper.selectBatchIds(ids), InventoryDetailVo.class);
        enrich(vos);
        return vos;
    }

    private void enrich(List<InventoryDetailVo> vos) {
        if (CollUtil.isEmpty(vos)) {
            return;
        }
        Set<Long> skuIds = vos.stream().map(InventoryDetailVo::getSkuId).filter(Objects::nonNull).collect(Collectors.toSet());
        Set<Long> itemInstanceIds = vos.stream().map(InventoryDetailVo::getItemInstanceId).filter(Objects::nonNull).collect(Collectors.toSet());
        Set<Long> rackIds = vos.stream().map(InventoryDetailVo::getRackId).filter(Objects::nonNull).collect(Collectors.toSet());
        Set<Long> locationIds = vos.stream().map(InventoryDetailVo::getLocationId).filter(Objects::nonNull).collect(Collectors.toSet());
        Map<Long, ItemSkuVo> itemSkuMap = itemSkuService.queryVosByIds(skuIds).stream().collect(Collectors.toMap(ItemSkuVo::getId, Function.identity()));
        Map<Long, String> instanceCodeMap = itemInstanceIds.isEmpty()
            ? Collections.emptyMap()
            : itemInstanceMapper.selectBatchIds(itemInstanceIds).stream().collect(Collectors.toMap(ItemInstance::getId, ItemInstance::getInstanceCode));
        Map<Long, String> rackNameMap = rackIds.isEmpty()
            ? Collections.emptyMap()
            : rackMapper.selectBatchIds(rackIds).stream().collect(Collectors.toMap(Rack::getId, Rack::getRackName));
        Map<Long, String> locationNameMap = locationIds.isEmpty()
            ? Collections.emptyMap()
            : locationMapper.selectBatchIds(locationIds).stream().collect(Collectors.toMap(Location::getId, Location::getLocationName));
        vos.forEach(it -> {
            ItemSkuVo itemSku = itemSkuMap.get(it.getSkuId());
            it.setItemSku(itemSku);
            it.setInstanceCode(instanceCodeMap.get(it.getItemInstanceId()));
            if (StringUtils.isBlank(it.getRackName())) {
                it.setRackName(rackNameMap.get(it.getRackId()));
            }
            if (StringUtils.isBlank(it.getLocationName())) {
                it.setLocationName(locationNameMap.get(it.getLocationId()));
            }
            if (itemSku != null) {
                it.setItem(itemSku.getItem());
                if (StringUtils.isBlank(it.getSkuName())) {
                    it.setSkuName(itemSku.getSkuName());
                }
                if (StringUtils.isBlank(it.getProductIdentifier())) {
                    it.setProductIdentifier(itemSku.getProductIdentifier());
                }
                if (StringUtils.isBlank(it.getQualityGrade())) {
                    it.setQualityGrade(itemSku.getQualityGrade());
                }
                if (StringUtils.isBlank(it.getItemName()) && itemSku.getItem() != null) {
                    it.setItemName(itemSku.getItem().getItemName());
                }
                if (itemSku.getItem() != null) {
                    if (StringUtils.isBlank(it.getItemCode())) {
                        it.setItemCode(itemSku.getItem().getItemCode());
                    }
                    if (StringUtils.isBlank(it.getUnit())) {
                        it.setUnit(itemSku.getItem().getUnit());
                    }
                }
            }
        });
    }

    /**
     * 新增库存详情
     */
    public void insertByBo(InventoryDetailBo bo) {
        InventoryDetail add = MapstructUtils.convert(bo, InventoryDetail.class);
        inventoryDetailMapper.insert(add);
    }

    /**
     * 修改库存详情
     */
    public void updateByBo(InventoryDetailBo bo) {
        InventoryDetail update = MapstructUtils.convert(bo, InventoryDetail.class);
        inventoryDetailMapper.updateById(update);
    }

    /**
     * 批量删除库存详情
     */
    public void deleteByIds(Collection<Long> ids) {
        inventoryDetailMapper.deleteBatchIds(ids);
    }

    /**
     * 校验入库记录剩余数
     * @param inventoryDetailBoList
     */
    public void validateRemainQuantity(List<InventoryDetailBo> inventoryDetailBoList) {
        if (CollUtil.isEmpty(inventoryDetailBoList)) {
            return;
        }
        List<InventoryDetail> inventoryDetailList = inventoryDetailMapper.selectBatchIds(inventoryDetailBoList.stream().map(InventoryDetailBo::getId).toList());
        if (CollUtil.isEmpty(inventoryDetailList)) {
            throw new BaseException("库存不足");
        }
        Map<Long, BigDecimal> remainQuantityMap = inventoryDetailList
            .stream()
            .collect(Collectors.toMap(InventoryDetail::getId, InventoryDetail::getRemainQuantity));
        boolean validResult = inventoryDetailBoList
            .stream()
            .anyMatch(inventoryDetailBo ->
                !remainQuantityMap.containsKey(inventoryDetailBo.getId())
                    || remainQuantityMap.get(inventoryDetailBo.getId()).compareTo(inventoryDetailBo.getShipmentQuantity()) < 0
            );
        if (validResult) {
            throw new BaseException("库存不足");
        }
    }

    public void clearDataWithZeroRemainQuantity() {
        LambdaQueryWrapper<InventoryDetail> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(InventoryDetail::getRemainQuantity, 0);
        inventoryDetailMapper.delete(wrapper);
    }
}
