package com.ruoyi.wms.service;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.utils.MapstructUtils;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.ruoyi.wms.domain.bo.AdjustOrderDetailBo;
import com.ruoyi.wms.domain.entity.AdjustOrderDetail;
import com.ruoyi.wms.domain.entity.Box;
import com.ruoyi.wms.domain.entity.ItemInstance;
import com.ruoyi.wms.domain.vo.AdjustOrderDetailVo;
import com.ruoyi.wms.domain.vo.InventoryDetailVo;
import com.ruoyi.wms.domain.vo.ItemSkuVo;
import com.ruoyi.wms.mapper.AdjustOrderDetailMapper;
import com.ruoyi.wms.mapper.BoxMapper;
import com.ruoyi.wms.mapper.InventoryDetailMapper;
import com.ruoyi.wms.mapper.ItemInstanceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 库存调整明细 Service
 */
@RequiredArgsConstructor
@Service
public class AdjustOrderDetailService extends ServiceImpl<AdjustOrderDetailMapper, AdjustOrderDetail> {

    private final AdjustOrderDetailMapper adjustOrderDetailMapper;
    private final ItemSkuService itemSkuService;
    private final InventoryDetailMapper inventoryDetailMapper;
    private final ItemInstanceMapper itemInstanceMapper;
    private final BoxMapper boxMapper;

    public AdjustOrderDetailVo queryById(Long id) {
        return adjustOrderDetailMapper.selectVoById(id);
    }

    public TableDataInfo<AdjustOrderDetailVo> queryPageList(AdjustOrderDetailBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<AdjustOrderDetail> lqw = buildQueryWrapper(bo);
        Page<AdjustOrderDetailVo> result = adjustOrderDetailMapper.selectVoPage(pageQuery.build(), lqw);
        enrich(result.getRecords());
        return TableDataInfo.build(result);
    }

    public List<AdjustOrderDetailVo> queryList(AdjustOrderDetailBo bo) {
        LambdaQueryWrapper<AdjustOrderDetail> lqw = buildQueryWrapper(bo);
        List<AdjustOrderDetailVo> details = adjustOrderDetailMapper.selectVoList(lqw);
        enrich(details);
        return details;
    }

    private LambdaQueryWrapper<AdjustOrderDetail> buildQueryWrapper(AdjustOrderDetailBo bo) {
        LambdaQueryWrapper<AdjustOrderDetail> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getAdjustOrderId() != null, AdjustOrderDetail::getAdjustOrderId, bo.getAdjustOrderId());
        lqw.eq(bo.getSkuId() != null, AdjustOrderDetail::getSkuId, bo.getSkuId());
        lqw.eq(bo.getWarehouseId() != null, AdjustOrderDetail::getWarehouseId, bo.getWarehouseId());
        lqw.eq(bo.getAreaId() != null, AdjustOrderDetail::getAreaId, bo.getAreaId());
        lqw.eq(bo.getRackId() != null, AdjustOrderDetail::getRackId, bo.getRackId());
        lqw.eq(bo.getLocationId() != null, AdjustOrderDetail::getLocationId, bo.getLocationId());
        lqw.eq(bo.getInventoryDetailId() != null, AdjustOrderDetail::getInventoryDetailId, bo.getInventoryDetailId());
        lqw.eq(bo.getItemInstanceId() != null, AdjustOrderDetail::getItemInstanceId, bo.getItemInstanceId());
        lqw.eq(bo.getBoxId() != null, AdjustOrderDetail::getBoxId, bo.getBoxId());
        return lqw;
    }

    public void insertByBo(AdjustOrderDetailBo bo) {
        adjustOrderDetailMapper.insert(MapstructUtils.convert(bo, AdjustOrderDetail.class));
    }

    public void updateByBo(AdjustOrderDetailBo bo) {
        adjustOrderDetailMapper.updateById(MapstructUtils.convert(bo, AdjustOrderDetail.class));
    }

    public void deleteByIds(Collection<Long> ids) {
        adjustOrderDetailMapper.deleteBatchIds(ids);
    }

    @Transactional
    public void saveDetails(List<AdjustOrderDetail> list) {
        if (CollUtil.isEmpty(list)) {
            return;
        }
        saveOrUpdateBatch(list);
    }

    public List<AdjustOrderDetailVo> queryByAdjustOrderId(Long adjustOrderId) {
        AdjustOrderDetailBo bo = new AdjustOrderDetailBo();
        bo.setAdjustOrderId(adjustOrderId);
        List<AdjustOrderDetailVo> details = queryList(bo);
        if (CollUtil.isEmpty(details)) {
            return Collections.emptyList();
        }
        return details;
    }

    private void enrich(List<AdjustOrderDetailVo> details) {
        if (CollUtil.isEmpty(details)) {
            return;
        }
        Set<Long> skuIds = details.stream().map(AdjustOrderDetailVo::getSkuId).filter(Objects::nonNull).collect(Collectors.toSet());
        Map<Long, ItemSkuVo> itemSkuMap = itemSkuService.queryVosByIds(skuIds)
            .stream()
            .collect(Collectors.toMap(ItemSkuVo::getId, Function.identity()));
        List<Long> inventoryDetailIds = details.stream().map(AdjustOrderDetailVo::getInventoryDetailId).filter(Objects::nonNull).toList();
        Map<Long, BigDecimal> remainQuantityMap = inventoryDetailIds.isEmpty()
            ? java.util.Collections.emptyMap()
            : inventoryDetailMapper.selectVoBatchIds(inventoryDetailIds)
                .stream()
                .collect(Collectors.toMap(InventoryDetailVo::getId, InventoryDetailVo::getRemainQuantity));
        Set<Long> itemInstanceIds = details.stream().map(AdjustOrderDetailVo::getItemInstanceId).filter(Objects::nonNull).collect(Collectors.toSet());
        Set<Long> boxIds = details.stream().map(AdjustOrderDetailVo::getBoxId).filter(Objects::nonNull).collect(Collectors.toSet());
        Map<Long, ItemInstance> itemInstanceMap = itemInstanceIds.isEmpty()
            ? java.util.Collections.emptyMap()
            : itemInstanceMapper.selectBatchIds(itemInstanceIds).stream().collect(Collectors.toMap(ItemInstance::getId, Function.identity()));
        Map<Long, Box> boxMap = boxIds.isEmpty()
            ? java.util.Collections.emptyMap()
            : boxMapper.selectBatchIds(boxIds).stream().collect(Collectors.toMap(Box::getId, Function.identity()));
        details.forEach(detail -> {
            detail.setItemSku(itemSkuMap.get(detail.getSkuId()));
            detail.setRemainQuantity(remainQuantityMap.getOrDefault(detail.getInventoryDetailId(), BigDecimal.ZERO));
            ItemInstance itemInstance = itemInstanceMap.get(detail.getItemInstanceId());
            if (itemInstance != null) {
                detail.setInstanceCode(itemInstance.getInstanceCode());
            }
            Box box = boxMap.get(detail.getBoxId());
            if (box != null) {
                detail.setBoxCode(box.getBoxCode());
            }
        });
    }
}

