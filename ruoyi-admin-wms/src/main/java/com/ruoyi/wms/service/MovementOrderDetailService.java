package com.ruoyi.wms.service;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.utils.MapstructUtils;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.wms.domain.vo.InventoryDetailVo;
import com.ruoyi.wms.domain.vo.ItemSkuVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.ruoyi.wms.domain.bo.MovementOrderDetailBo;
import com.ruoyi.wms.domain.vo.MovementOrderDetailVo;
import com.ruoyi.wms.domain.entity.MovementOrderDetail;
import com.ruoyi.wms.mapper.MovementOrderDetailMapper;
import com.ruoyi.wms.mapper.RackMapper;
import com.ruoyi.wms.mapper.LocationMapper;
import com.ruoyi.wms.domain.entity.Rack;
import com.ruoyi.wms.domain.entity.Location;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 调拨单明细 Service 业务层处理
 *
 * @author ping
 * @date 2024-08-09
 */
@RequiredArgsConstructor
@Service
public class MovementOrderDetailService extends ServiceImpl<MovementOrderDetailMapper, MovementOrderDetail> {

    private final MovementOrderDetailMapper movementOrderDetailMapper;
    private final ItemSkuService itemSkuService;
    private final InventoryDetailService inventoryDetailService;
    private final RackMapper rackMapper;
    private final LocationMapper locationMapper;
    /**
     * 查询调拨单明细
     */
    public MovementOrderDetailVo queryById(Long id){
        return movementOrderDetailMapper.selectVoById(id);
    }

    /**
     * 查询调拨单明细列表
     */
    public TableDataInfo<MovementOrderDetailVo> queryPageList(MovementOrderDetailBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<MovementOrderDetail> lqw = buildQueryWrapper(bo);
        Page<MovementOrderDetailVo> result = movementOrderDetailMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询调拨单明细列表
     */
    public List<MovementOrderDetailVo> queryList(MovementOrderDetailBo bo) {
        LambdaQueryWrapper<MovementOrderDetail> lqw = buildQueryWrapper(bo);
        return movementOrderDetailMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<MovementOrderDetail> buildQueryWrapper(MovementOrderDetailBo bo) {
        LambdaQueryWrapper<MovementOrderDetail> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getMovementOrderId() != null, MovementOrderDetail::getMovementOrderId, bo.getMovementOrderId());
        lqw.eq(bo.getSkuId() != null, MovementOrderDetail::getSkuId, bo.getSkuId());
        lqw.eq(bo.getQuantity() != null, MovementOrderDetail::getQuantity, bo.getQuantity());
        lqw.eq(bo.getSourceWarehouseId() != null, MovementOrderDetail::getSourceWarehouseId, bo.getSourceWarehouseId());
        lqw.eq(bo.getSourceAreaId() != null, MovementOrderDetail::getSourceAreaId, bo.getSourceAreaId());
        lqw.eq(bo.getTargetWarehouseId() != null, MovementOrderDetail::getTargetWarehouseId, bo.getTargetWarehouseId());
        lqw.eq(bo.getTargetAreaId() != null, MovementOrderDetail::getTargetAreaId, bo.getTargetAreaId());
        lqw.eq(bo.getInventoryDetailId() != null, MovementOrderDetail::getInventoryDetailId, bo.getInventoryDetailId());
        return lqw;
    }

    /**
     * 新增调拨单明细
     */
    public void insertByBo(MovementOrderDetailBo bo) {
        MovementOrderDetail add = MapstructUtils.convert(bo, MovementOrderDetail.class);
        movementOrderDetailMapper.insert(add);
    }

    /**
     * 修改调拨单明细
     */
    public void updateByBo(MovementOrderDetailBo bo) {
        MovementOrderDetail update = MapstructUtils.convert(bo, MovementOrderDetail.class);
        movementOrderDetailMapper.updateById(update);
    }

    /**
     * 批量删除调拨单明细
     */
    public void deleteByIds(Collection<Long> ids) {
        movementOrderDetailMapper.deleteBatchIds(ids);
    }

    @Transactional
    public void saveDetails(List<MovementOrderDetail> list) {
        if (CollUtil.isEmpty(list)) {
            return;
        }
        saveOrUpdateBatch(list);
    }

    /**
     * 根据调拨单 id 查询调拨单明细
     * @param movementOrderId
     * @return
     */
    public List<MovementOrderDetailVo> queryByMovementOrderId(Long movementOrderId) {
        MovementOrderDetailBo bo = new MovementOrderDetailBo();
        bo.setMovementOrderId(movementOrderId);
        List<MovementOrderDetailVo> details = queryList(bo);
        if (CollUtil.isEmpty(details)) {
            return Collections.emptyList();
        }
        Set<Long> skuIds = details
            .stream()
            .map(MovementOrderDetailVo::getSkuId)
            .collect(Collectors.toSet());
        Map<Long, ItemSkuVo> itemSkuMap = itemSkuService.queryVosByIds(skuIds)
            .stream()
            .collect(Collectors.toMap(ItemSkuVo::getId, Function.identity()));
        List<Long> inventoryDetailIds = details.stream().map(MovementOrderDetailVo::getInventoryDetailId).toList();
        Map<Long, InventoryDetailVo> inventoryDetailMap = inventoryDetailService.queryVoListByIds(inventoryDetailIds)
            .stream().collect(Collectors.toMap(InventoryDetailVo::getId, Function.identity()));
        // 批量解析源货架/源货位名称（从明细自身的 sourceRackId/sourceLocationId 解析）
        Set<Long> sourceRackIds = details.stream().map(MovementOrderDetailVo::getSourceRackId).filter(Objects::nonNull).collect(Collectors.toSet());
        Set<Long> sourceLocationIds = details.stream().map(MovementOrderDetailVo::getSourceLocationId).filter(Objects::nonNull).collect(Collectors.toSet());
        Map<Long, String> rackNameMap = sourceRackIds.isEmpty()
            ? Collections.emptyMap()
            : rackMapper.selectBatchIds(sourceRackIds).stream().collect(Collectors.toMap(Rack::getId, Rack::getRackName));
        Map<Long, String> locationNameMap = sourceLocationIds.isEmpty()
            ? Collections.emptyMap()
            : locationMapper.selectBatchIds(sourceLocationIds).stream().collect(Collectors.toMap(Location::getId, Location::getLocationName));

        details.forEach(detail -> {
            ItemSkuVo itemSku = itemSkuMap.get(detail.getSkuId());
            detail.setItemSku(itemSku);
            fillSnapshotFields(detail, itemSku);
            // 从明细自身的源位置ID解析名称
            if (detail.getSourceRackId() != null) {
                detail.setSourceRackName(rackNameMap.get(detail.getSourceRackId()));
            }
            if (detail.getSourceLocationId() != null) {
                detail.setSourceLocationName(locationNameMap.get(detail.getSourceLocationId()));
            }
            InventoryDetailVo inventoryDetail = inventoryDetailMap.get(detail.getInventoryDetailId());
            if (inventoryDetail != null) {
                detail.setInventoryDetail(inventoryDetail);
                detail.setRemainQuantity(inventoryDetail.getRemainQuantity());
                if (StringUtils.isBlank(detail.getInstanceCode())) {
                    detail.setInstanceCode(inventoryDetail.getInstanceCode());
                }
            } else {
                detail.setRemainQuantity(BigDecimal.ZERO);
            }
        });
        return details;
    }

    private void fillSnapshotFields(MovementOrderDetailVo detail, ItemSkuVo itemSku) {
        if (detail == null || itemSku == null) {
            return;
        }
        if (StringUtils.isBlank(detail.getSkuName())) {
            detail.setSkuName(itemSku.getSkuName());
        }
        if (StringUtils.isBlank(detail.getProductIdentifier())) {
            detail.setProductIdentifier(itemSku.getProductIdentifier());
        }
        if (StringUtils.isBlank(detail.getQualityGrade())) {
            detail.setQualityGrade(itemSku.getQualityGrade());
        }
        if (itemSku.getItem() == null) {
            return;
        }
        if (StringUtils.isBlank(detail.getItemCode())) {
            detail.setItemCode(itemSku.getItem().getItemCode());
        }
        if (StringUtils.isBlank(detail.getItemName())) {
            detail.setItemName(itemSku.getItem().getItemName());
        }
        if (StringUtils.isBlank(detail.getUnit())) {
            detail.setUnit(itemSku.getItem().getUnit());
        }
    }
}
