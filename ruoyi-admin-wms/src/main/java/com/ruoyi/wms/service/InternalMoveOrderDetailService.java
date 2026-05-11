package com.ruoyi.wms.service;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.utils.MapstructUtils;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.ruoyi.wms.domain.bo.InternalMoveOrderDetailBo;
import com.ruoyi.wms.domain.entity.Box;
import com.ruoyi.wms.domain.entity.InternalMoveOrderDetail;
import com.ruoyi.wms.domain.entity.ItemInstance;
import com.ruoyi.wms.domain.vo.InternalMoveOrderDetailVo;
import com.ruoyi.wms.domain.vo.InventoryDetailVo;
import com.ruoyi.wms.domain.vo.ItemSkuVo;
import com.ruoyi.wms.mapper.BoxMapper;
import com.ruoyi.wms.mapper.InternalMoveOrderDetailMapper;
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
 * 库内移库单明细 Service 业务层处理
 */
@RequiredArgsConstructor
@Service
public class InternalMoveOrderDetailService extends ServiceImpl<InternalMoveOrderDetailMapper, InternalMoveOrderDetail> {

    private final InternalMoveOrderDetailMapper internalMoveOrderDetailMapper;
    private final ItemSkuService itemSkuService;
    private final InventoryDetailMapper inventoryDetailMapper;
    private final ItemInstanceMapper itemInstanceMapper;
    private final BoxMapper boxMapper;

    public InternalMoveOrderDetailVo queryById(Long id) {
        return internalMoveOrderDetailMapper.selectVoById(id);
    }

    public TableDataInfo<InternalMoveOrderDetailVo> queryPageList(InternalMoveOrderDetailBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<InternalMoveOrderDetail> lqw = buildQueryWrapper(bo);
        Page<InternalMoveOrderDetailVo> result = internalMoveOrderDetailMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    public List<InternalMoveOrderDetailVo> queryList(InternalMoveOrderDetailBo bo) {
        LambdaQueryWrapper<InternalMoveOrderDetail> lqw = buildQueryWrapper(bo);
        return internalMoveOrderDetailMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<InternalMoveOrderDetail> buildQueryWrapper(InternalMoveOrderDetailBo bo) {
        LambdaQueryWrapper<InternalMoveOrderDetail> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getInternalMoveOrderId() != null, InternalMoveOrderDetail::getInternalMoveOrderId, bo.getInternalMoveOrderId());
        lqw.eq(bo.getSkuId() != null, InternalMoveOrderDetail::getSkuId, bo.getSkuId());
        lqw.eq(bo.getSourceWarehouseId() != null, InternalMoveOrderDetail::getSourceWarehouseId, bo.getSourceWarehouseId());
        lqw.eq(bo.getSourceAreaId() != null, InternalMoveOrderDetail::getSourceAreaId, bo.getSourceAreaId());
        lqw.eq(bo.getSourceRackId() != null, InternalMoveOrderDetail::getSourceRackId, bo.getSourceRackId());
        lqw.eq(bo.getSourceLocationId() != null, InternalMoveOrderDetail::getSourceLocationId, bo.getSourceLocationId());
        lqw.eq(bo.getTargetWarehouseId() != null, InternalMoveOrderDetail::getTargetWarehouseId, bo.getTargetWarehouseId());
        lqw.eq(bo.getTargetAreaId() != null, InternalMoveOrderDetail::getTargetAreaId, bo.getTargetAreaId());
        lqw.eq(bo.getTargetRackId() != null, InternalMoveOrderDetail::getTargetRackId, bo.getTargetRackId());
        lqw.eq(bo.getTargetLocationId() != null, InternalMoveOrderDetail::getTargetLocationId, bo.getTargetLocationId());
        lqw.eq(bo.getInventoryDetailId() != null, InternalMoveOrderDetail::getInventoryDetailId, bo.getInventoryDetailId());
        lqw.eq(bo.getItemInstanceId() != null, InternalMoveOrderDetail::getItemInstanceId, bo.getItemInstanceId());
        lqw.eq(bo.getBoxId() != null, InternalMoveOrderDetail::getBoxId, bo.getBoxId());
        lqw.like(StringUtils.isNotBlank(bo.getEquipmentCode()), InternalMoveOrderDetail::getEquipmentCode, bo.getEquipmentCode());
        return lqw;
    }

    @Transactional
    public void saveDetails(List<InternalMoveOrderDetail> list) {
        if (CollUtil.isEmpty(list)) {
            return;
        }
        saveOrUpdateBatch(list);
    }

    public void deleteByIds(Collection<Long> ids) {
        internalMoveOrderDetailMapper.deleteBatchIds(ids);
    }

    public List<InternalMoveOrderDetailVo> queryByInternalMoveOrderId(Long internalMoveOrderId) {
        InternalMoveOrderDetailBo bo = new InternalMoveOrderDetailBo();
        bo.setInternalMoveOrderId(internalMoveOrderId);
        List<InternalMoveOrderDetailVo> details = queryList(bo);
        if (CollUtil.isEmpty(details)) {
            return Collections.emptyList();
        }
        enrichDetails(details);
        return details;
    }

    public List<InternalMoveOrderDetailVo> queryByItemInstanceId(Long itemInstanceId) {
        InternalMoveOrderDetailBo bo = new InternalMoveOrderDetailBo();
        bo.setItemInstanceId(itemInstanceId);
        List<InternalMoveOrderDetailVo> details = queryList(bo);
        enrichTrackingInfo(details);
        return details;
    }

    public List<InternalMoveOrderDetailVo> queryByBoxId(Long boxId) {
        InternalMoveOrderDetailBo bo = new InternalMoveOrderDetailBo();
        bo.setBoxId(boxId);
        List<InternalMoveOrderDetailVo> details = queryList(bo);
        enrichTrackingInfo(details);
        return details;
    }

    private void enrichDetails(List<InternalMoveOrderDetailVo> details) {
        Set<Long> skuIds = details.stream().map(InternalMoveOrderDetailVo::getSkuId).filter(Objects::nonNull).collect(Collectors.toSet());
        Map<Long, ItemSkuVo> itemSkuMap = itemSkuService.queryVosByIds(skuIds).stream()
            .collect(Collectors.toMap(ItemSkuVo::getId, Function.identity()));
        List<Long> inventoryDetailIds = details.stream()
            .map(InternalMoveOrderDetailVo::getInventoryDetailId)
            .filter(Objects::nonNull)
            .toList();
        Map<Long, BigDecimal> remainQuantityMap = inventoryDetailIds.isEmpty() ? java.util.Collections.emptyMap() :
            inventoryDetailMapper.selectVoBatchIds(inventoryDetailIds).stream()
                .collect(Collectors.toMap(InventoryDetailVo::getId, InventoryDetailVo::getRemainQuantity));
        details.forEach(detail -> {
            detail.setItemSku(itemSkuMap.get(detail.getSkuId()));
            detail.setRemainQuantity(remainQuantityMap.getOrDefault(detail.getInventoryDetailId(), BigDecimal.ZERO));
        });
        enrichTrackingInfo(details);
    }

    private void enrichTrackingInfo(List<InternalMoveOrderDetailVo> details) {
        if (CollUtil.isEmpty(details)) {
            return;
        }
        Set<Long> itemInstanceIds = details.stream().map(InternalMoveOrderDetailVo::getItemInstanceId).filter(Objects::nonNull).collect(Collectors.toSet());
        Set<Long> boxIds = details.stream().map(InternalMoveOrderDetailVo::getBoxId).filter(Objects::nonNull).collect(Collectors.toSet());
        Map<Long, ItemInstance> itemInstanceMap = itemInstanceIds.isEmpty() ? java.util.Collections.emptyMap() :
            itemInstanceMapper.selectBatchIds(itemInstanceIds).stream().collect(Collectors.toMap(ItemInstance::getId, Function.identity()));
        Map<Long, Box> boxMap = boxIds.isEmpty() ? java.util.Collections.emptyMap() :
            boxMapper.selectBatchIds(boxIds).stream().collect(Collectors.toMap(Box::getId, Function.identity()));
        details.forEach(detail -> {
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

