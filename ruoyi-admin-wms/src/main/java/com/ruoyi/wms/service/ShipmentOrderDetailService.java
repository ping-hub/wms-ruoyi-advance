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
import com.ruoyi.wms.domain.entity.InventoryDetail;
import com.ruoyi.wms.domain.entity.Box;
import com.ruoyi.wms.domain.vo.ItemSkuVo;
import com.ruoyi.wms.mapper.InventoryDetailMapper;
import com.ruoyi.wms.mapper.InventoryMapper;
import com.ruoyi.wms.mapper.BoxMapper;
import com.ruoyi.wms.mapper.RackMapper;
import com.ruoyi.wms.mapper.LocationMapper;
import com.ruoyi.wms.domain.entity.Rack;
import com.ruoyi.wms.domain.entity.Location;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.ruoyi.wms.domain.bo.ShipmentOrderDetailBo;
import com.ruoyi.wms.domain.vo.ShipmentOrderDetailVo;
import com.ruoyi.wms.domain.entity.ShipmentOrderDetail;
import com.ruoyi.wms.mapper.ShipmentOrderDetailMapper;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 出库单详情Service业务层处理
 *
 * @author ping
 * @date 2024-08-01
 */
@RequiredArgsConstructor
@Service
public class ShipmentOrderDetailService extends ServiceImpl<ShipmentOrderDetailMapper, ShipmentOrderDetail> {

    private final ShipmentOrderDetailMapper shipmentOrderDetailMapper;
    private final ItemSkuService itemSkuService;
    private final InventoryMapper inventoryMapper;
    private final InventoryDetailMapper inventoryDetailMapper;
    private final BoxMapper boxMapper;
    private final RackMapper rackMapper;
    private final LocationMapper locationMapper;

    /**
     * 查询出库单详情
     */
    public ShipmentOrderDetailVo queryById(Long id){
        ShipmentOrderDetailVo vo = shipmentOrderDetailMapper.selectVoById(id);
        enrich(List.of(vo));
        return vo;
    }

    /**
     * 查询出库单详情列表（跨表分页，支持出库单号/器材/时间筛选）
     */
    public TableDataInfo<ShipmentOrderDetailVo> queryPageList(ShipmentOrderDetailBo bo, PageQuery pageQuery) {
        Page<ShipmentOrderDetailVo> page = shipmentOrderDetailMapper.queryDetailPage(pageQuery.build(), bo);
        // 补充 itemSku 快照字段
        if (CollUtil.isNotEmpty(page.getRecords())) {
            Set<Long> skuIds = page.getRecords().stream()
                .map(ShipmentOrderDetailVo::getSkuId).filter(Objects::nonNull).collect(Collectors.toSet());
            Map<Long, ItemSkuVo> itemSkuMap = skuIds.isEmpty() ? Map.of() :
                itemSkuService.queryVosByIds(skuIds).stream()
                    .collect(Collectors.toMap(ItemSkuVo::getId, Function.identity()));
            page.getRecords().forEach(detail -> {
                ItemSkuVo itemSku = itemSkuMap.get(detail.getSkuId());
                detail.setItemSku(itemSku);
                fillSnapshotFields(detail, itemSku);
            });
        }
        return TableDataInfo.build(page);
    }

    /**
     * 查询出库单详情列表
     */
    public List<ShipmentOrderDetailVo> queryList(ShipmentOrderDetailBo bo) {
        LambdaQueryWrapper<ShipmentOrderDetail> lqw = buildQueryWrapper(bo);
        List<ShipmentOrderDetailVo> list = shipmentOrderDetailMapper.selectVoList(lqw);
        enrich(list);
        return list;
    }

    private LambdaQueryWrapper<ShipmentOrderDetail> buildQueryWrapper(ShipmentOrderDetailBo bo) {
        LambdaQueryWrapper<ShipmentOrderDetail> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getShipmentOrderId() != null, ShipmentOrderDetail::getShipmentOrderId, bo.getShipmentOrderId());
        lqw.eq(bo.getSkuId() != null, ShipmentOrderDetail::getSkuId, bo.getSkuId());
        lqw.eq(bo.getQuantity() != null, ShipmentOrderDetail::getQuantity, bo.getQuantity());
        lqw.eq(bo.getWarehouseId() != null, ShipmentOrderDetail::getWarehouseId, bo.getWarehouseId());
        lqw.eq(bo.getAreaId() != null, ShipmentOrderDetail::getAreaId, bo.getAreaId());
        return lqw;
    }

    /**
     * 新增出库单详情
     */
    public void insertByBo(ShipmentOrderDetailBo bo) {
        ShipmentOrderDetail add = MapstructUtils.convert(bo, ShipmentOrderDetail.class);
        shipmentOrderDetailMapper.insert(add);
    }

    /**
     * 修改出库单详情
     */
    public void updateByBo(ShipmentOrderDetailBo bo) {
        ShipmentOrderDetail update = MapstructUtils.convert(bo, ShipmentOrderDetail.class);
        shipmentOrderDetailMapper.updateById(update);
    }

    /**
     * 批量删除出库单详情
     */
    public void deleteByIds(Collection<Long> ids) {
        shipmentOrderDetailMapper.deleteBatchIds(ids);
    }

    @Transactional
    public void saveDetails(List<ShipmentOrderDetail> list) {
        if (CollUtil.isEmpty(list)) {
            return;
        }
        saveOrUpdateBatch(list);
    }

    public List<ShipmentOrderDetailVo> queryByShipmentOrderId(Long shipmentOrderId) {
        ShipmentOrderDetailBo bo = new ShipmentOrderDetailBo();
        bo.setShipmentOrderId(shipmentOrderId);
        List<ShipmentOrderDetailVo> details = queryList(bo);
        if (CollUtil.isEmpty(details)) {
            return Collections.EMPTY_LIST;
        }
        // 查规格
        Set<Long> skuIds = details
            .stream()
            .map(ShipmentOrderDetailVo::getSkuId)
            .collect(Collectors.toSet());
        Map<Long, ItemSkuVo> itemSkuMap = itemSkuService.queryVosByIds(skuIds)
            .stream()
            .collect(Collectors.toMap(ItemSkuVo::getId, Function.identity()));
        // 查剩余库存 & 货位/货架信息
        List<Long> inventoryDetailIds = details
            .stream()
            .map(ShipmentOrderDetailVo::getInventoryDetailId)
            .toList();
        List<InventoryDetail> inventoryDetails = inventoryDetailMapper.selectBatchIds(inventoryDetailIds);
        Map<Long, InventoryDetail> inventoryDetailMap = inventoryDetails
            .stream()
            .collect(Collectors.toMap(InventoryDetail::getId, Function.identity()));
        // 填充剩余库存 & 货位/货架ID
        details.forEach(detail -> {
            InventoryDetail inv = inventoryDetailMap.get(detail.getInventoryDetailId());
            detail.setRemainQuantity(inv != null ? inv.getRemainQuantity() : BigDecimal.ZERO);
            if (inv != null) {
                if (detail.getRackId() == null) detail.setRackId(inv.getRackId());
                if (detail.getLocationId() == null) detail.setLocationId(inv.getLocationId());
            }
        });
        // 查货架/货位名称
        Set<Long> rackIds = details.stream().map(ShipmentOrderDetailVo::getRackId).filter(Objects::nonNull).collect(Collectors.toSet());
        Set<Long> locationIds = details.stream().map(ShipmentOrderDetailVo::getLocationId).filter(Objects::nonNull).collect(Collectors.toSet());
        Map<Long, String> rackNameMap = rackIds.isEmpty()
            ? Collections.emptyMap()
            : rackMapper.selectBatchIds(rackIds).stream().collect(Collectors.toMap(Rack::getId, Rack::getRackName));
        Map<Long, String> locationNameMap = locationIds.isEmpty()
            ? Collections.emptyMap()
            : locationMapper.selectBatchIds(locationIds).stream().collect(Collectors.toMap(Location::getId, Location::getLocationName));
        details.forEach(detail -> {
            if (StringUtils.isBlank(detail.getRackName())) detail.setRackName(rackNameMap.get(detail.getRackId()));
            if (StringUtils.isBlank(detail.getLocationName())) detail.setLocationName(locationNameMap.get(detail.getLocationId()));
        });
        enrich(details, itemSkuMap);
        return details;
    }

    public List<ShipmentOrderDetailVo> queryByInstanceCode(String instanceCode) {
        ShipmentOrderDetailBo bo = new ShipmentOrderDetailBo();
        bo.setInstanceCode(instanceCode);
        return queryList(bo);
    }

    public List<ShipmentOrderDetailVo> queryByBoxId(Long boxId) {
        ShipmentOrderDetailBo bo = new ShipmentOrderDetailBo();
        bo.setBoxId(boxId);
        return queryList(bo);
    }

    private void enrich(List<ShipmentOrderDetailVo> details) {
        if (CollUtil.isEmpty(details)) {
            return;
        }
        Set<Long> skuIds = details.stream().map(ShipmentOrderDetailVo::getSkuId).filter(Objects::nonNull).collect(Collectors.toSet());
        Map<Long, ItemSkuVo> itemSkuMap = itemSkuService.queryVosByIds(skuIds).stream()
            .collect(Collectors.toMap(ItemSkuVo::getId, Function.identity()));
        enrich(details, itemSkuMap);
    }

    private void enrich(List<ShipmentOrderDetailVo> details, Map<Long, ItemSkuVo> itemSkuMap) {
        if (CollUtil.isEmpty(details)) {
            return;
        }
        Set<Long> boxIds = details.stream().map(ShipmentOrderDetailVo::getBoxId).filter(Objects::nonNull).collect(Collectors.toSet());
        Map<Long, Box> boxMap = boxIds.isEmpty() ? Map.of() :
            boxMapper.selectBatchIds(boxIds).stream().collect(Collectors.toMap(Box::getId, Function.identity()));
        details.forEach(detail -> {
            ItemSkuVo itemSku = itemSkuMap.get(detail.getSkuId());
            detail.setItemSku(itemSku);
            fillSnapshotFields(detail, itemSku);
            if (detail.getInstanceCode() != null) {
                detail.setInstanceCode(detail.getInstanceCode());
            }
            Box box = detail.getBoxId() == null ? null : boxMap.get(detail.getBoxId());
            if (box != null) {
                detail.setBoxCode(box.getBoxCode());
            }
        });
    }

    private void fillSnapshotFields(ShipmentOrderDetailVo detail, ItemSkuVo itemSku) {
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
