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
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.MapstructUtils;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.ruoyi.wms.domain.bo.ItemInstanceBo;
import com.ruoyi.wms.domain.entity.Area;
import com.ruoyi.wms.domain.entity.ItemInstance;
import com.ruoyi.wms.domain.entity.Location;
import com.ruoyi.wms.domain.entity.Rack;
import com.ruoyi.wms.domain.entity.ReceiptOrder;
import com.ruoyi.wms.domain.entity.ReceiptOrderDetail;
import com.ruoyi.wms.domain.entity.Warehouse;
import com.ruoyi.wms.domain.vo.ItemInstanceVo;
import com.ruoyi.wms.domain.vo.ItemSkuVo;
import com.ruoyi.wms.domain.vo.ItemVo;
import com.ruoyi.wms.mapper.AreaMapper;
import com.ruoyi.wms.mapper.ItemInstanceMapper;
import com.ruoyi.wms.mapper.LocationMapper;
import com.ruoyi.wms.mapper.RackMapper;
import com.ruoyi.wms.mapper.ReceiptOrderDetailMapper;
import com.ruoyi.wms.mapper.WarehouseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ItemInstanceService extends ServiceImpl<ItemInstanceMapper, ItemInstance> {

    private final ItemInstanceMapper itemInstanceMapper;
    private final ItemSkuService itemSkuService;
    private final WarehouseMapper warehouseMapper;
    private final AreaMapper areaMapper;
    private final RackMapper rackMapper;
    private final LocationMapper locationMapper;
    private final ReceiptOrderDetailMapper receiptOrderDetailMapper;

    public ItemInstanceVo queryById(Long id) {
        ItemInstanceVo vo = itemInstanceMapper.selectVoById(id);
        enrich(List.of(vo));
        return vo;
    }

    public ItemInstanceVo queryByCode(String instanceCode) {
        LambdaQueryWrapper<ItemInstance> lqw = Wrappers.lambdaQuery();
        lqw.eq(ItemInstance::getInstanceCode, instanceCode);
        ItemInstanceVo vo = itemInstanceMapper.selectVoOne(lqw);
        enrich(List.of(vo));
        return vo;
    }

    public TableDataInfo<ItemInstanceVo> queryPageList(ItemInstanceBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ItemInstance> lqw = buildQueryWrapper(bo);
        Page<ItemInstanceVo> result = itemInstanceMapper.selectVoPage(pageQuery.build(), lqw);
        enrich(result.getRecords());
        return TableDataInfo.build(result);
    }

    public List<ItemInstanceVo> queryList(ItemInstanceBo bo) {
        LambdaQueryWrapper<ItemInstance> lqw = buildQueryWrapper(bo);
        List<ItemInstanceVo> list = itemInstanceMapper.selectVoList(lqw);
        enrich(list);
        return list;
    }

    @Transactional
    public void insertByBo(ItemInstanceBo bo) {
        fillAndValidateBeforeSave(bo);
        itemInstanceMapper.insert(MapstructUtils.convert(bo, ItemInstance.class));
    }

    @Transactional
    public void updateByBo(ItemInstanceBo bo) {
        fillAndValidateBeforeSave(bo);
        itemInstanceMapper.updateById(MapstructUtils.convert(bo, ItemInstance.class));
    }

    public void updateStatus(Long id, String targetStatus) {
        Assert.isTrue(StrUtil.isNotBlank(targetStatus), "目标状态不能为空");
        ItemInstance itemInstance = itemInstanceMapper.selectById(id);
        Assert.notNull(itemInstance, "单品实例不存在");
        ItemInstance update = new ItemInstance();
        update.setId(id);
        update.setInstanceStatus(targetStatus);
        itemInstanceMapper.updateById(update);
    }

    public void updateLocation(ItemInstanceBo bo) {
        Assert.notNull(bo.getId(), "单品实例ID不能为空");
        ItemInstance itemInstance = itemInstanceMapper.selectById(bo.getId());
        Assert.notNull(itemInstance, "单品实例不存在");
        fillLocationFields(bo);
        ItemInstance update = new ItemInstance();
        update.setId(bo.getId());
        update.setWarehouseId(bo.getWarehouseId());
        update.setAreaId(bo.getAreaId());
        update.setRackId(bo.getRackId());
        update.setLocationId(bo.getLocationId());
        itemInstanceMapper.updateById(update);
    }

    public void deleteById(Long id) {
        itemInstanceMapper.deleteById(id);
    }

    public long countByReceiptOrderId(Long receiptOrderId) {
        LambdaQueryWrapper<ItemInstance> lqw = Wrappers.lambdaQuery();
        lqw.eq(ItemInstance::getSourceType, ServiceConstants.ItemInstanceSourceType.RECEIPT);
        lqw.eq(ItemInstance::getSourceOrderId, receiptOrderId);
        return itemInstanceMapper.selectCount(lqw);
    }

    @Transactional
    public void generateByReceiptOrder(ReceiptOrder receiptOrder, List<ReceiptOrderDetail> detailList) {
        if (CollUtil.isEmpty(detailList)) {
            return;
        }
        Assert.isTrue(countByReceiptOrderId(receiptOrder.getId()) == 0, "该入库单已生成单品实例，请勿重复入库");
        Set<Long> skuIds = detailList.stream().map(ReceiptOrderDetail::getSkuId).filter(Objects::nonNull).collect(Collectors.toSet());
        Map<Long, ItemSkuVo> skuMap = itemSkuService.queryVosByIds(skuIds).stream()
            .collect(Collectors.toMap(ItemSkuVo::getId, Function.identity()));
        List<ItemInstance> addList = new ArrayList<>();
        for (ReceiptOrderDetail detail : detailList) {
            ItemSkuVo skuVo = skuMap.get(detail.getSkuId());
            Assert.notNull(skuVo, "入库单明细规格不存在");
            ItemVo item = skuVo.getItem();
            Assert.notNull(item, "规格未关联物品定义");
            boolean shouldGenerate = Integer.valueOf(1).equals(detail.getGenerateItemInstance())
                || StrUtil.equals(item.getTrackingMode(), "instance");
            if (!shouldGenerate) {
                continue;
            }
            int instanceCount = convertInstanceCount(detail.getQuantity(), skuVo.getSkuName());
            for (int i = 0; i < instanceCount; i++) {
                ItemInstance itemInstance = new ItemInstance();
                itemInstance.setInstanceCode(generateInstanceCode());
                itemInstance.setItemId(item.getId());
                itemInstance.setSkuId(detail.getSkuId());
                itemInstance.setInstanceStatus(ServiceConstants.ItemInstanceStatus.IN_STOCK);
                itemInstance.setInBox(0);
                itemInstance.setBorrowed(0);
                itemInstance.setWarehouseId(detail.getWarehouseId());
                itemInstance.setAreaId(detail.getAreaId());
                itemInstance.setRackId(null);
                itemInstance.setLocationId(null);
                itemInstance.setSourceType(ServiceConstants.ItemInstanceSourceType.RECEIPT);
                itemInstance.setSourceOrderId(receiptOrder.getId());
                itemInstance.setSourceOrderNo(receiptOrder.getReceiptOrderNo());
                itemInstance.setReceiptOrderDetailId(detail.getId());
                itemInstance.setBatchNo(detail.getBatchNo());
                itemInstance.setProductionDate(detail.getProductionDate());
                itemInstance.setExpirationDate(detail.getExpirationDate());
                itemInstance.setRemark(detail.getRemark());
                addList.add(itemInstance);
            }
            ReceiptOrderDetail update = new ReceiptOrderDetail();
            update.setId(detail.getId());
            update.setGeneratedInstanceQuantity(instanceCount);
            if (detail.getGenerateItemInstance() == null) {
                update.setGenerateItemInstance(1);
            }
            receiptOrderDetailMapper.updateById(update);
        }
        if (CollUtil.isNotEmpty(addList)) {
            saveBatch(addList);
        }
    }

    private LambdaQueryWrapper<ItemInstance> buildQueryWrapper(ItemInstanceBo bo) {
        LambdaQueryWrapper<ItemInstance> lqw = Wrappers.lambdaQuery();
        lqw.eq(StrUtil.isNotBlank(bo.getInstanceCode()), ItemInstance::getInstanceCode, bo.getInstanceCode());
        lqw.eq(bo.getItemId() != null, ItemInstance::getItemId, bo.getItemId());
        lqw.eq(bo.getSkuId() != null, ItemInstance::getSkuId, bo.getSkuId());
        lqw.eq(StrUtil.isNotBlank(bo.getInstanceStatus()), ItemInstance::getInstanceStatus, bo.getInstanceStatus());
        lqw.eq(bo.getInBox() != null, ItemInstance::getInBox, bo.getInBox());
        lqw.eq(bo.getBorrowed() != null, ItemInstance::getBorrowed, bo.getBorrowed());
        lqw.eq(bo.getWarehouseId() != null, ItemInstance::getWarehouseId, bo.getWarehouseId());
        lqw.eq(bo.getAreaId() != null, ItemInstance::getAreaId, bo.getAreaId());
        lqw.eq(bo.getRackId() != null, ItemInstance::getRackId, bo.getRackId());
        lqw.eq(bo.getLocationId() != null, ItemInstance::getLocationId, bo.getLocationId());
        lqw.eq(bo.getSourceOrderId() != null, ItemInstance::getSourceOrderId, bo.getSourceOrderId());
        lqw.eq(bo.getReceiptOrderDetailId() != null, ItemInstance::getReceiptOrderDetailId, bo.getReceiptOrderDetailId());
        lqw.orderByDesc(ItemInstance::getCreateTime);
        return lqw;
    }

    private void fillAndValidateBeforeSave(ItemInstanceBo bo) {
        if (StrUtil.isBlank(bo.getInstanceCode())) {
            bo.setInstanceCode(generateInstanceCode());
        }
        if (StrUtil.isBlank(bo.getInstanceStatus())) {
            bo.setInstanceStatus(ServiceConstants.ItemInstanceStatus.IN_STOCK);
        }
        if (bo.getInBox() == null) {
            bo.setInBox(0);
        }
        if (bo.getBorrowed() == null) {
            bo.setBorrowed(0);
        }
        validateInstanceCodeUnique(bo);
        fillLocationFields(bo);
    }

    private void validateInstanceCodeUnique(ItemInstanceBo bo) {
        LambdaQueryWrapper<ItemInstance> lqw = Wrappers.lambdaQuery();
        lqw.eq(ItemInstance::getInstanceCode, bo.getInstanceCode());
        lqw.ne(bo.getId() != null, ItemInstance::getId, bo.getId());
        Assert.isTrue(itemInstanceMapper.selectCount(lqw) == 0, "单品码重复");
    }

    private void fillLocationFields(ItemInstanceBo bo) {
        if (bo.getLocationId() != null) {
            Location location = locationMapper.selectById(bo.getLocationId());
            Assert.notNull(location, "货位不存在");
            bo.setRackId(location.getRackId());
            bo.setAreaId(location.getAreaId());
            bo.setWarehouseId(location.getWarehouseId());
            return;
        }
        if (bo.getRackId() != null) {
            Rack rack = rackMapper.selectById(bo.getRackId());
            Assert.notNull(rack, "货架不存在");
            if (bo.getAreaId() != null) {
                Assert.isTrue(Objects.equals(bo.getAreaId(), rack.getAreaId()), "货架与库区不匹配");
            } else {
                bo.setAreaId(rack.getAreaId());
            }
            if (bo.getWarehouseId() != null) {
                Assert.isTrue(Objects.equals(bo.getWarehouseId(), rack.getWarehouseId()), "货架与仓库不匹配");
            } else {
                bo.setWarehouseId(rack.getWarehouseId());
            }
            return;
        }
        if (bo.getAreaId() != null) {
            Area area = areaMapper.selectById(bo.getAreaId());
            Assert.notNull(area, "库区不存在");
            if (bo.getWarehouseId() != null) {
                Assert.isTrue(Objects.equals(bo.getWarehouseId(), area.getWarehouseId()), "库区与仓库不匹配");
            } else {
                bo.setWarehouseId(area.getWarehouseId());
            }
            return;
        }
        if (bo.getWarehouseId() != null) {
            Warehouse warehouse = warehouseMapper.selectById(bo.getWarehouseId());
            Assert.notNull(warehouse, "仓库不存在");
        }
    }

    private int convertInstanceCount(BigDecimal quantity, String skuName) {
        Assert.notNull(quantity, "入库数量不能为空");
        Assert.isTrue(quantity.compareTo(BigDecimal.ZERO) > 0, "入库数量必须大于0");
        try {
            int count = quantity.intValueExact();
            Assert.isTrue(count > 0, "实例数量必须大于0");
            return count;
        } catch (ArithmeticException e) {
            throw new ServiceException("规格" + skuName + "生成单品实例时，数量必须为整数", HttpStatus.CONFLICT.value());
        }
    }

    private String generateInstanceCode() {
        return "II" + IdUtil.getSnowflakeNextIdStr();
    }

    private void enrich(List<ItemInstanceVo> list) {
        if (CollUtil.isEmpty(list)) {
            return;
        }
        List<ItemInstanceVo> validList = list.stream().filter(Objects::nonNull).toList();
        if (CollUtil.isEmpty(validList)) {
            return;
        }
        Set<Long> skuIds = validList.stream().map(ItemInstanceVo::getSkuId).filter(Objects::nonNull).collect(Collectors.toSet());
        Set<Long> warehouseIds = validList.stream().map(ItemInstanceVo::getWarehouseId).filter(Objects::nonNull).collect(Collectors.toSet());
        Set<Long> areaIds = validList.stream().map(ItemInstanceVo::getAreaId).filter(Objects::nonNull).collect(Collectors.toSet());
        Set<Long> rackIds = validList.stream().map(ItemInstanceVo::getRackId).filter(Objects::nonNull).collect(Collectors.toSet());
        Set<Long> locationIds = validList.stream().map(ItemInstanceVo::getLocationId).filter(Objects::nonNull).collect(Collectors.toSet());
        Map<Long, ItemSkuVo> skuMap = itemSkuService.queryVosByIds(skuIds).stream().collect(Collectors.toMap(ItemSkuVo::getId, Function.identity()));
        Map<Long, Warehouse> warehouseMap = warehouseIds.isEmpty() ? Map.of() :
            warehouseMapper.selectBatchIds(warehouseIds).stream().collect(Collectors.toMap(Warehouse::getId, Function.identity()));
        Map<Long, Area> areaMap = areaIds.isEmpty() ? Map.of() :
            areaMapper.selectBatchIds(areaIds).stream().collect(Collectors.toMap(Area::getId, Function.identity()));
        Map<Long, Rack> rackMap = rackIds.isEmpty() ? Map.of() :
            rackMapper.selectBatchIds(rackIds).stream().collect(Collectors.toMap(Rack::getId, Function.identity()));
        Map<Long, Location> locationMap = locationIds.isEmpty() ? Map.of() :
            locationMapper.selectBatchIds(locationIds).stream().collect(Collectors.toMap(Location::getId, Function.identity()));
        validList.forEach(vo -> {
            ItemSkuVo skuVo = skuMap.get(vo.getSkuId());
            if (skuVo != null) {
                vo.setSkuName(skuVo.getSkuName());
                if (skuVo.getItem() != null) {
                    vo.setItemName(skuVo.getItem().getItemName());
                }
            }
            Warehouse warehouse = warehouseMap.get(vo.getWarehouseId());
            if (warehouse != null) {
                vo.setWarehouseName(warehouse.getWarehouseName());
            }
            Area area = areaMap.get(vo.getAreaId());
            if (area != null) {
                vo.setAreaName(area.getAreaName());
            }
            Rack rack = rackMap.get(vo.getRackId());
            if (rack != null) {
                vo.setRackName(rack.getRackName());
            }
            Location location = locationMap.get(vo.getLocationId());
            if (location != null) {
                vo.setLocationName(location.getLocationName());
            }
        });
    }
}
