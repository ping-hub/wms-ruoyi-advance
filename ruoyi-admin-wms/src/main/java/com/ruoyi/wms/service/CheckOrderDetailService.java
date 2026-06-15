package com.ruoyi.wms.service;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.wms.domain.entity.Box;
import com.ruoyi.wms.domain.entity.InventoryDetail;
import com.ruoyi.wms.domain.vo.InventoryDetailVo;
import com.ruoyi.wms.domain.vo.ItemSkuVo;
import com.ruoyi.wms.mapper.InventoryDetailMapper;
import com.ruoyi.wms.mapper.BoxMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.ruoyi.wms.domain.vo.CheckOrderDetailVo;
import com.ruoyi.wms.domain.entity.CheckOrderDetail;
import com.ruoyi.wms.mapper.CheckOrderDetailMapper;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 库存盘点单据详情Service业务层处理
 *
 * @author ping
 * @date 2024-08-13
 */
@RequiredArgsConstructor
@Service
public class CheckOrderDetailService extends ServiceImpl<CheckOrderDetailMapper, CheckOrderDetail> {

    private final CheckOrderDetailMapper checkOrderDetailMapper;
    private final ItemSkuService itemSkuService;
    private final InventoryDetailMapper inventoryDetailMapper;
    private final BoxMapper boxMapper;

    @Transactional
    public void saveDetails(List<CheckOrderDetail> list) {
        if (CollUtil.isEmpty(list)) {
            return;
        }
        fillEntityByInventoryDetail(list);
        saveOrUpdateBatch(list);
    }

    public List<CheckOrderDetailVo> queryByCheckOrderId(Long checkOrderId) {
        LambdaQueryWrapper<CheckOrderDetail> lqw = Wrappers.lambdaQuery();
        lqw.eq(CheckOrderDetail::getCheckOrderId, checkOrderId);
        List<CheckOrderDetailVo> details = checkOrderDetailMapper.selectVoList(lqw);
        if (CollUtil.isEmpty(details)) {
            return Collections.emptyList();
        }
        hydrateDetails(details);
        Set<Long> skuIds = details
            .stream()
            .map(CheckOrderDetailVo::getSkuId)
            .collect(Collectors.toSet());
        Map<Long, ItemSkuVo> itemSkuMap = itemSkuService.queryVosByIds(skuIds)
            .stream()
            .collect(Collectors.toMap(ItemSkuVo::getId, Function.identity()));
        List<Long> inventoryDetailIds = details.stream().map(CheckOrderDetailVo::getInventoryDetailId).toList();
        Map<Long, BigDecimal> remainQuantityMap = inventoryDetailMapper.selectVoBatchIds(inventoryDetailIds)
            .stream().collect(Collectors.toMap(InventoryDetailVo::getId, InventoryDetailVo::getRemainQuantity));
        details.forEach(it -> {
            it.setItemSku(itemSkuMap.get(it.getSkuId()));
            it.setRemainQuantity(remainQuantityMap.getOrDefault(it.getInventoryDetailId(), BigDecimal.ZERO));
        });
        enrichTrackingInfo(details);
        return details;
    }

    @Transactional
    public void deleteByCheckOrderIds(Collection<Long> checkOrderIds) {
        if (CollUtil.isEmpty(checkOrderIds)) {
            return;
        }
        LambdaQueryWrapper<CheckOrderDetail> lqw = Wrappers.lambdaQuery();
        lqw.in(CheckOrderDetail::getCheckOrderId, checkOrderIds);
        checkOrderDetailMapper.delete(lqw);
    }

    private void enrichTrackingInfo(List<CheckOrderDetailVo> details) {
        if (CollUtil.isEmpty(details)) {
            return;
        }
        Set<Long> boxIds = details.stream().map(CheckOrderDetailVo::getBoxId).filter(Objects::nonNull).collect(Collectors.toSet());
        Map<Long, Box> boxMap = boxIds.isEmpty()
            ? java.util.Collections.emptyMap()
            : boxMapper.selectBatchIds(boxIds).stream().collect(Collectors.toMap(Box::getId, Function.identity()));
        details.forEach(detail -> {
            if (detail.getInstanceCode() != null) {
                detail.setInstanceCode(detail.getInstanceCode());
            }
            Box box = boxMap.get(detail.getBoxId());
            if (box != null) {
                detail.setBoxCode(box.getBoxCode());
            }
        });
    }

    private void hydrateDetails(List<CheckOrderDetailVo> details) {
        if (CollUtil.isEmpty(details)) {
            return;
        }
        Set<Long> inventoryDetailIds = details.stream()
            .map(CheckOrderDetailVo::getInventoryDetailId)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
        if (CollUtil.isEmpty(inventoryDetailIds)) {
            return;
        }
        Map<Long, InventoryDetail> inventoryDetailMap = inventoryDetailMapper.selectBatchIds(inventoryDetailIds)
            .stream()
            .collect(Collectors.toMap(InventoryDetail::getId, Function.identity()));
        details.forEach(detail -> {
            InventoryDetail inventoryDetail = inventoryDetailMap.get(detail.getInventoryDetailId());
            if (inventoryDetail == null) {
                return;
            }
            if (detail.getSkuId() == null) {
                detail.setSkuId(inventoryDetail.getSkuId());
            }
            if (detail.getWarehouseId() == null) {
                detail.setWarehouseId(inventoryDetail.getWarehouseId());
            }
            if (detail.getAreaId() == null) {
                detail.setAreaId(inventoryDetail.getAreaId());
            }
            if (detail.getRackId() == null) {
                detail.setRackId(inventoryDetail.getRackId());
            }
            if (detail.getLocationId() == null) {
                detail.setLocationId(inventoryDetail.getLocationId());
            }
            if (detail.getReceiptTime() == null) {
                detail.setReceiptTime(inventoryDetail.getCreateTime());
            }
            if (detail.getInstanceCode() == null) {
                detail.setInstanceCode(inventoryDetail.getInstanceCode());
            }
            if (detail.getBoxId() == null) {
                detail.setBoxId(inventoryDetail.getBoxId());
            }
            if (detail.getRemainQuantity() == null) {
                detail.setRemainQuantity(inventoryDetail.getRemainQuantity());
            }
        });
    }

    private void fillEntityByInventoryDetail(List<CheckOrderDetail> list) {
        Set<Long> inventoryDetailIds = list.stream()
            .map(CheckOrderDetail::getInventoryDetailId)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
        if (CollUtil.isEmpty(inventoryDetailIds)) {
            return;
        }
        Map<Long, InventoryDetail> inventoryDetailMap = inventoryDetailMapper.selectBatchIds(inventoryDetailIds)
            .stream()
            .collect(Collectors.toMap(InventoryDetail::getId, Function.identity()));
        list.forEach(detail -> {
            InventoryDetail inventoryDetail = inventoryDetailMap.get(detail.getInventoryDetailId());
            if (inventoryDetail == null) {
                return;
            }
            if (detail.getSkuId() == null) {
                detail.setSkuId(inventoryDetail.getSkuId());
            }
            if (detail.getWarehouseId() == null) {
                detail.setWarehouseId(inventoryDetail.getWarehouseId());
            }
            if (detail.getAreaId() == null) {
                detail.setAreaId(inventoryDetail.getAreaId());
            }
            if (detail.getRackId() == null) {
                detail.setRackId(inventoryDetail.getRackId());
            }
            if (detail.getLocationId() == null) {
                detail.setLocationId(inventoryDetail.getLocationId());
            }
            if (detail.getReceiptTime() == null) {
                detail.setReceiptTime(inventoryDetail.getCreateTime());
            }
            if (detail.getInstanceCode() == null) {
                detail.setInstanceCode(inventoryDetail.getInstanceCode());
            }
            if (detail.getBoxId() == null) {
                detail.setBoxId(inventoryDetail.getBoxId());
            }
        });
    }
}
