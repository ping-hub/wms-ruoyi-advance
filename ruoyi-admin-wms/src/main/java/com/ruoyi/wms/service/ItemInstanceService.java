package com.ruoyi.wms.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.constant.ServiceConstants;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.MapstructUtils;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.ruoyi.wms.domain.bo.ItemInstanceBo;
import com.ruoyi.wms.domain.bo.ReceiptItemInstanceBo;
import com.ruoyi.wms.domain.bo.ReceiptOrderDetailBo;
import com.ruoyi.wms.domain.bo.ShipmentOrderDetailBo;
import com.ruoyi.wms.domain.entity.Area;
import com.ruoyi.wms.domain.entity.Box;
import com.ruoyi.wms.domain.entity.ItemInstance;
import com.ruoyi.wms.domain.entity.Location;
import com.ruoyi.wms.domain.entity.Rack;
import com.ruoyi.wms.domain.entity.ReceiptOrder;
import com.ruoyi.wms.domain.entity.ReceiptOrderDetail;
import com.ruoyi.wms.domain.entity.Warehouse;
import com.ruoyi.wms.domain.vo.ItemInstanceImportVo;
import com.ruoyi.wms.domain.vo.ItemInstanceVo;
import com.ruoyi.wms.domain.vo.ItemSkuVo;
import com.ruoyi.wms.domain.vo.ItemVo;
import com.ruoyi.wms.mapper.AreaMapper;
import com.ruoyi.wms.mapper.BoxMapper;
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
    private final BoxMapper boxMapper;
    private final ReceiptOrderDetailMapper receiptOrderDetailMapper;

    public ItemInstanceVo queryById(Long id) {
        ItemInstanceVo vo = itemInstanceMapper.selectVoById(id);
        if (vo == null) {
            return null;
        }
        enrich(List.of(vo));
        return vo;
    }

    public ItemInstanceVo queryByCode(String instanceCode) {
        LambdaQueryWrapper<ItemInstance> lqw = Wrappers.lambdaQuery();
        lqw.eq(ItemInstance::getInstanceCode, instanceCode);
        ItemInstanceVo vo = itemInstanceMapper.selectVoOne(lqw);
        if (vo == null) {
            return null;
        }
        enrich(List.of(vo));
        return vo;
    }

    public void validateSelectRules(ItemInstanceVo vo, ItemInstanceBo bo) {
        if (vo == null || bo == null) {
            return;
        }
        if (Boolean.TRUE.equals(bo.getUnshippedOnly())) {
            validateSelectableForShipment(vo);
        }
        if (Boolean.TRUE.equals(bo.getUnreceivedOnly())) {
            validateSelectableForReceipt(vo);
        }
    }

    private void validateSelectableForShipment(ItemInstanceVo vo) {
        Assert.notNull(vo, "单品实例不存在");
        String instanceCode = StrUtil.blankToDefault(vo.getInstanceCode(), "");
        Assert.isTrue(ServiceConstants.ItemInstanceStatus.IN_STOCK.equals(vo.getInstanceStatus()),
            "单品实例" + instanceCode + "当前状态不可出库，仅支持在库实例");
        Assert.isTrue(vo.getShipmentOrderDetailId() == null, "单品实例" + instanceCode + "已被其他出库单暂存占用");
    }

    private void validateSelectableForReceipt(ItemInstanceVo vo) {
        Assert.notNull(vo, "单品实例不存在");
        String instanceCode = StrUtil.blankToDefault(vo.getInstanceCode(), "");
        Assert.isTrue(ServiceConstants.ItemInstanceStatus.PENDING_RECEIPT.equals(vo.getInstanceStatus()),
            "单品实例" + instanceCode + "当前状态不可入库，仅支持待入库实例");
        Assert.isTrue(vo.getBoxId() == null, "单品实例" + instanceCode + "已装箱，无法用于入库");
        Assert.isTrue(vo.getReceiptOrderDetailId() == null, "单品实例" + instanceCode + "已被其他入库单暂存占用");
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
    public String importData(List<ItemInstanceImportVo> list) {
        if (CollUtil.isEmpty(list)) {
            return "导入成功，共 0 条数据。";
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (ItemInstanceImportVo item : list) {
            String instanceCode = StrUtil.trim(item.getInstanceCode());
            try {
                Assert.isTrue(StrUtil.isNotBlank(instanceCode), "单品码不能为空");
                ItemInstanceVo existed = queryByCode(instanceCode);
                if (existed == null) {
                    Assert.notNull(item.getItemId(), "新增单品必须提供物品ID");
                    Assert.notNull(item.getSkuId(), "新增单品必须提供规格ID");
                    Assert.isTrue(StrUtil.isBlank(item.getBoxCode()), "新增待入库单品不支持直接导入箱码");

                    ItemInstanceBo bo = new ItemInstanceBo();
                    bo.setInstanceCode(instanceCode);
                    bo.setItemId(item.getItemId());
                    bo.setSkuId(item.getSkuId());
                    bo.setRemark(item.getRemark());
                    insertByBo(bo);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、单品码 ").append(instanceCode).append(" 导入成功");
                    continue;
                }

                ItemInstanceBo bo = new ItemInstanceBo();
                bo.setId(existed.getId());
                bo.setBoxCode(item.getBoxCode());
                bo.setRemark(item.getRemark());
                updateByBo(bo);
                successNum++;
                successMsg.append("<br/>").append(successNum).append("、单品码 ").append(instanceCode).append(" 更新成功");
            } catch (Exception e) {
                failureNum++;
                failureMsg.append("<br/>").append(failureNum).append("、单品码 ")
                    .append(StrUtil.blankToDefault(instanceCode, "空值"))
                    .append(" 导入失败：").append(e.getMessage());
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据处理异常，错误如下：");
            throw new ServiceException(failureMsg.toString());
        }
        successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        return successMsg.toString();
    }

    @Transactional
    public void updateByBo(ItemInstanceBo bo) {
        Assert.notNull(bo.getId(), "单品实例ID不能为空");
        ItemInstance existed = itemInstanceMapper.selectById(bo.getId());
        Assert.notNull(existed, "单品实例不存在");

        ItemInstance update = new ItemInstance();
        update.setId(existed.getId());
        update.setRemark(bo.getRemark());

        Long targetBoxId = resolveLedgerUpdateBoxId(existed, bo.getBoxCode());
        if (targetBoxId != null) {
            update.setBoxId(targetBoxId);
            update.setInstanceStatus(ServiceConstants.ItemInstanceStatus.IN_STOCK);
        }

        itemInstanceMapper.updateById(update);

        if (targetBoxId != null) {
            syncBoxSnapshot(targetBoxId, ServiceConstants.BoxStatus.PACKED);
        }
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

    public void moveTo(Long id, Long warehouseId, Long areaId) {
        LambdaUpdateWrapper<ItemInstance> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(ItemInstance::getId, id);
        wrapper.set(ItemInstance::getInstanceStatus, ServiceConstants.ItemInstanceStatus.IN_STOCK);
        wrapper.set(ItemInstance::getBoxId, null);
        wrapper.set(ItemInstance::getWarehouseId, warehouseId);
        wrapper.set(ItemInstance::getAreaId, areaId);
        itemInstanceMapper.update(null, wrapper);
    }

    public void markLoss(Long id) {
        LambdaUpdateWrapper<ItemInstance> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(ItemInstance::getId, id);
        wrapper.set(ItemInstance::getInstanceStatus, ServiceConstants.ItemInstanceStatus.LOSS);
        wrapper.set(ItemInstance::getBoxId, null);
        wrapper.set(ItemInstance::getWarehouseId, null);
        wrapper.set(ItemInstance::getAreaId, null);
        wrapper.set(ItemInstance::getRackId, null);
        wrapper.set(ItemInstance::getLocationId, null);
        itemInstanceMapper.update(null, wrapper);
    }

    public void deleteById(Long id) {
        itemInstanceMapper.deleteById(id);
    }

    public void markInBox(Long id, Box box) {
        LambdaUpdateWrapper<ItemInstance> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(ItemInstance::getId, id);
        wrapper.set(ItemInstance::getInstanceStatus, ServiceConstants.ItemInstanceStatus.IN_STOCK);
        wrapper.set(ItemInstance::getBoxId, box.getId());
        wrapper.set(ItemInstance::getWarehouseId, box.getWarehouseId());
        wrapper.set(ItemInstance::getAreaId, box.getAreaId());
        wrapper.set(ItemInstance::getRackId, box.getRackId());
        wrapper.set(ItemInstance::getLocationId, box.getLocationId());
        itemInstanceMapper.update(null, wrapper);
    }

    public void markBorrowed(Long id) {
        LambdaUpdateWrapper<ItemInstance> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(ItemInstance::getId, id);
        wrapper.set(ItemInstance::getInstanceStatus, ServiceConstants.ItemInstanceStatus.BORROWED);
        wrapper.set(ItemInstance::getBoxId, null);
        wrapper.set(ItemInstance::getWarehouseId, null);
        wrapper.set(ItemInstance::getAreaId, null);
        wrapper.set(ItemInstance::getRackId, null);
        wrapper.set(ItemInstance::getLocationId, null);
        itemInstanceMapper.update(null, wrapper);
    }

    public void restoreFromBorrow(Long id, Long warehouseId, Long areaId, Long rackId, Long locationId) {
        LambdaUpdateWrapper<ItemInstance> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(ItemInstance::getId, id);
        wrapper.set(ItemInstance::getInstanceStatus, ServiceConstants.ItemInstanceStatus.IN_STOCK);
        wrapper.set(ItemInstance::getBoxId, null);
        wrapper.set(ItemInstance::getWarehouseId, warehouseId);
        wrapper.set(ItemInstance::getAreaId, areaId);
        wrapper.set(ItemInstance::getRackId, rackId);
        wrapper.set(ItemInstance::getLocationId, locationId);
        itemInstanceMapper.update(null, wrapper);
    }

    public void markOutbound(Long id, String targetStatus) {
        LambdaUpdateWrapper<ItemInstance> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(ItemInstance::getId, id);
        wrapper.set(ItemInstance::getInstanceStatus,
            StrUtil.blankToDefault(targetStatus, ServiceConstants.ItemInstanceStatus.OUTBOUND));
        wrapper.set(ItemInstance::getBoxId, null);
        wrapper.set(ItemInstance::getWarehouseId, null);
        wrapper.set(ItemInstance::getAreaId, null);
        wrapper.set(ItemInstance::getRackId, null);
        wrapper.set(ItemInstance::getLocationId, null);
        itemInstanceMapper.update(null, wrapper);
    }

    public List<ItemInstance> queryByIds(Set<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return List.of();
        }
        return itemInstanceMapper.selectBatchIds(ids);
    }

    public List<ItemInstanceVo> queryVosByIds(Set<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return List.of();
        }
        List<ItemInstanceVo> list = itemInstanceMapper.selectVoBatchIds(ids);
        enrich(list);
        return list;
    }

    public Map<Long, List<ItemInstanceVo>> queryVoMapByReceiptDetailIds(Set<Long> receiptOrderDetailIds) {
        if (CollUtil.isEmpty(receiptOrderDetailIds)) {
            return Map.of();
        }
        LambdaQueryWrapper<ItemInstance> lqw = Wrappers.lambdaQuery();
        lqw.in(ItemInstance::getReceiptOrderDetailId, receiptOrderDetailIds);
        lqw.orderByAsc(ItemInstance::getId);
        List<ItemInstanceVo> list = itemInstanceMapper.selectVoList(lqw);
        enrich(list);
        return list.stream()
            .filter(it -> it.getReceiptOrderDetailId() != null)
            .collect(Collectors.groupingBy(ItemInstanceVo::getReceiptOrderDetailId));
    }

    public long countByReceiptOrderId(Long receiptOrderId) {
        LambdaQueryWrapper<ItemInstance> lqw = Wrappers.lambdaQuery();
        lqw.eq(ItemInstance::getSourceType, ServiceConstants.ItemInstanceSourceType.RECEIPT);
        lqw.eq(ItemInstance::getSourceOrderId, receiptOrderId);
        return itemInstanceMapper.selectCount(lqw);
    }

    public void reserveForReceiptDetails(List<ReceiptOrderDetailBo> detailList) {
        if (CollUtil.isEmpty(detailList)) {
            return;
        }
        Set<Long> instanceIds = new java.util.HashSet<>();
        Set<String> instanceCodes = new java.util.HashSet<>();
        detailList.forEach(detail -> {
            if (CollUtil.isEmpty(detail.getReceiptItemInstances())) {
                return;
            }
            detail.getReceiptItemInstances().forEach(item -> {
                if (item.getId() != null) {
                    instanceIds.add(item.getId());
                }
                String instanceCode = StrUtil.trim(item.getInstanceCode());
                if (StrUtil.isNotBlank(instanceCode)) {
                    instanceCodes.add(instanceCode);
                }
            });
        });
        if (instanceIds.isEmpty() && instanceCodes.isEmpty()) {
            return;
        }
        Map<Long, ItemInstance> itemInstanceMap = queryByIds(instanceIds).stream()
            .collect(Collectors.toMap(ItemInstance::getId, Function.identity()));
        Map<String, ItemInstance> itemInstanceCodeMap = queryByCodes(instanceCodes).stream()
            .collect(Collectors.toMap(ItemInstance::getInstanceCode, Function.identity()));
        List<ItemInstance> updateList = new ArrayList<>();
        for (ReceiptOrderDetailBo detail : detailList) {
            if (CollUtil.isEmpty(detail.getReceiptItemInstances())) {
                continue;
            }
            for (ReceiptItemInstanceBo receiptItemInstance : detail.getReceiptItemInstances()) {
                String instanceCode = StrUtil.trim(receiptItemInstance.getInstanceCode());
                ItemInstance itemInstance = resolveReceiptItemInstance(receiptItemInstance, itemInstanceMap, itemInstanceCodeMap);
                validateAvailableForReceipt(itemInstance, instanceCode, detail.getId());
                Assert.isTrue(Objects.equals(itemInstance.getSkuId(), detail.getSkuId()), "器材实例编码" + itemInstance.getInstanceCode() + "与当前明细规格不匹配");
                ItemInstance update = new ItemInstance();
                update.setId(itemInstance.getId());
                update.setReceiptOrderDetailId(detail.getId());
                updateList.add(update);
            }
        }
        if (CollUtil.isNotEmpty(updateList)) {
            updateBatchById(updateList);
        }
    }

    public void releaseReceiptReservationsByDetailIds(java.util.Collection<Long> detailIds) {
        if (CollUtil.isEmpty(detailIds)) {
            return;
        }
        LambdaUpdateWrapper<ItemInstance> wrapper = Wrappers.lambdaUpdate();
        wrapper.in(ItemInstance::getReceiptOrderDetailId, detailIds);
        wrapper.isNull(ItemInstance::getWarehouseId);
        wrapper.isNull(ItemInstance::getAreaId);
        wrapper.isNull(ItemInstance::getRackId);
        wrapper.isNull(ItemInstance::getLocationId);
        wrapper.isNull(ItemInstance::getBoxId);
        wrapper.set(ItemInstance::getReceiptOrderDetailId, null);
        itemInstanceMapper.update(null, wrapper);
    }

    @Transactional
    public List<ItemInstance> receiveByReceiptOrder(ReceiptOrder receiptOrder, List<ReceiptOrderDetailBo> detailList,
                                                    Map<String, Box> receiptBoxMap) {
        if (CollUtil.isEmpty(detailList)) {
            return List.of();
        }
        Assert.isTrue(countByReceiptOrderId(receiptOrder.getId()) == 0, "该入库单已生成单品实例，请勿重复入库");
        Set<Long> instanceIds = new java.util.HashSet<>();
        Set<String> instanceCodes = new java.util.HashSet<>();
        detailList.forEach(detail -> {
            if (CollUtil.isEmpty(detail.getReceiptItemInstances())) {
                return;
            }
            detail.getReceiptItemInstances().forEach(item -> {
                if (item.getId() != null) {
                    instanceIds.add(item.getId());
                }
                String instanceCode = StrUtil.trim(item.getInstanceCode());
                if (StrUtil.isNotBlank(instanceCode)) {
                    instanceCodes.add(instanceCode);
                }
            });
        });
        Map<Long, ItemInstance> itemInstanceMap = queryByIds(instanceIds).stream()
            .collect(Collectors.toMap(ItemInstance::getId, Function.identity()));
        Map<String, ItemInstance> itemInstanceCodeMap = queryByCodes(instanceCodes).stream()
            .collect(Collectors.toMap(ItemInstance::getInstanceCode, Function.identity()));
        List<ItemInstance> updateList = new ArrayList<>();
        for (ReceiptOrderDetailBo detail : detailList) {
            List<ReceiptItemInstanceBo> receiptItemInstances = detail.getReceiptItemInstances();
            Assert.isTrue(CollUtil.isNotEmpty(receiptItemInstances), "请先录入器材实例");
            for (ReceiptItemInstanceBo receiptItemInstance : receiptItemInstances) {
                String instanceCode = StrUtil.trim(receiptItemInstance.getInstanceCode());
                ItemInstance itemInstance = resolveReceiptItemInstance(receiptItemInstance, itemInstanceMap, itemInstanceCodeMap);
                validateAvailableForReceipt(itemInstance, instanceCode, detail.getId());
                Assert.isTrue(Objects.equals(itemInstance.getSkuId(), detail.getSkuId()), "器材实例编码" + itemInstance.getInstanceCode() + "与当前明细规格不匹配");
                Box box = StrUtil.isBlank(receiptItemInstance.getBoxCode()) ? null :
                    receiptBoxMap.get(StrUtil.trim(receiptItemInstance.getBoxCode()));
                if (StrUtil.isNotBlank(receiptItemInstance.getBoxCode())) {
                    Assert.notNull(box, "箱码" + receiptItemInstance.getBoxCode() + "未完成预处理");
                }
                itemInstance.setInstanceStatus(ServiceConstants.ItemInstanceStatus.IN_STOCK);
                itemInstance.setWarehouseId(detail.getWarehouseId());
                itemInstance.setAreaId(detail.getAreaId());
                itemInstance.setRackId(detail.getRackId());
                itemInstance.setLocationId(detail.getLocationId());
                itemInstance.setSourceType(ServiceConstants.ItemInstanceSourceType.RECEIPT);
                itemInstance.setSourceOrderType(ServiceConstants.ItemInstanceSourceType.RECEIPT);
                itemInstance.setSourceOrderId(receiptOrder.getId());
                itemInstance.setSourceOrderNo(receiptOrder.getReceiptOrderNo());
                itemInstance.setReceiptOrderDetailId(detail.getId());
                itemInstance.setBoxId(box == null ? null : box.getId());
                itemInstance.setRemark(StrUtil.blankToDefault(receiptItemInstance.getRemark(), detail.getRemark()));
                updateList.add(itemInstance);
            }
            ReceiptOrderDetail update = new ReceiptOrderDetail();
            update.setId(detail.getId());
            receiptOrderDetailMapper.updateById(update);
        }
        if (CollUtil.isNotEmpty(updateList)) {
            updateBatchById(updateList);
        }
        return updateList;
    }

    private LambdaQueryWrapper<ItemInstance> buildQueryWrapper(ItemInstanceBo bo) {
        LambdaQueryWrapper<ItemInstance> lqw = Wrappers.lambdaQuery();
        lqw.eq(StrUtil.isNotBlank(bo.getInstanceCode()), ItemInstance::getInstanceCode, bo.getInstanceCode());
        lqw.eq(bo.getItemId() != null, ItemInstance::getItemId, bo.getItemId());
        lqw.eq(bo.getSkuId() != null, ItemInstance::getSkuId, bo.getSkuId());
        lqw.eq(StrUtil.isNotBlank(bo.getInstanceStatus()), ItemInstance::getInstanceStatus, bo.getInstanceStatus());
        lqw.eq(bo.getWarehouseId() != null, ItemInstance::getWarehouseId, bo.getWarehouseId());
        lqw.eq(bo.getAreaId() != null, ItemInstance::getAreaId, bo.getAreaId());
        lqw.eq(bo.getRackId() != null, ItemInstance::getRackId, bo.getRackId());
        lqw.eq(bo.getLocationId() != null, ItemInstance::getLocationId, bo.getLocationId());
        lqw.eq(StrUtil.isNotBlank(bo.getSourceType()), ItemInstance::getSourceType, bo.getSourceType());
        lqw.eq(StrUtil.isNotBlank(bo.getSourceOrderType()), ItemInstance::getSourceOrderType, bo.getSourceOrderType());
        lqw.eq(bo.getBoxId() != null, ItemInstance::getBoxId, bo.getBoxId());
        lqw.eq(bo.getSourceOrderId() != null, ItemInstance::getSourceOrderId, bo.getSourceOrderId());
        lqw.eq(bo.getReceiptOrderDetailId() != null, ItemInstance::getReceiptOrderDetailId, bo.getReceiptOrderDetailId());
        lqw.eq(bo.getShipmentOrderDetailId() != null, ItemInstance::getShipmentOrderDetailId, bo.getShipmentOrderDetailId());
        if (Boolean.TRUE.equals(bo.getUnreceivedOnly())) {
            lqw.eq(ItemInstance::getInstanceStatus, ServiceConstants.ItemInstanceStatus.PENDING_RECEIPT);
            lqw.isNull(ItemInstance::getWarehouseId);
            lqw.isNull(ItemInstance::getAreaId);
            lqw.isNull(ItemInstance::getRackId);
            lqw.isNull(ItemInstance::getLocationId);
            lqw.isNull(ItemInstance::getBoxId);
            lqw.isNull(ItemInstance::getReceiptOrderDetailId);
        }
        if (Boolean.TRUE.equals(bo.getUnshippedOnly())) {
            lqw.isNull(ItemInstance::getShipmentOrderDetailId);
        }
        lqw.orderByDesc(ItemInstance::getCreateTime);
        return lqw;
    }

    public void reserveForShipmentDetails(List<ShipmentOrderDetailBo> detailList) {
        if (CollUtil.isEmpty(detailList)) {
            return;
        }
        Set<Long> instanceIds = detailList.stream()
            .map(ShipmentOrderDetailBo::getItemInstanceId)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
        if (CollUtil.isEmpty(instanceIds)) {
            return;
        }
        Map<Long, ItemInstance> itemInstanceMap = queryByIds(instanceIds).stream()
            .collect(Collectors.toMap(ItemInstance::getId, Function.identity()));
        List<ItemInstance> updateList = new ArrayList<>();
        for (ShipmentOrderDetailBo detail : detailList) {
            if (detail.getItemInstanceId() == null) {
                continue;
            }
            ItemInstance itemInstance = itemInstanceMap.get(detail.getItemInstanceId());
            Assert.notNull(itemInstance, "单品实例不存在");
            validateAvailableForShipment(itemInstance, detail.getId());
            Assert.isTrue(Objects.equals(itemInstance.getSkuId(), detail.getSkuId()), "单品实例" + itemInstance.getInstanceCode() + "与当前明细规格不匹配");
            ItemInstance update = new ItemInstance();
            update.setId(itemInstance.getId());
            update.setShipmentOrderDetailId(detail.getId());
            updateList.add(update);
        }
        if (CollUtil.isNotEmpty(updateList)) {
            updateBatchById(updateList);
        }
    }

    public void releaseShipmentReservationsByDetailIds(java.util.Collection<Long> detailIds) {
        if (CollUtil.isEmpty(detailIds)) {
            return;
        }
        LambdaUpdateWrapper<ItemInstance> wrapper = Wrappers.lambdaUpdate();
        wrapper.in(ItemInstance::getShipmentOrderDetailId, detailIds);
        wrapper.notIn(ItemInstance::getInstanceStatus,
            java.util.List.of(ServiceConstants.ItemInstanceStatus.OUTBOUND, ServiceConstants.ItemInstanceStatus.SCRAPPED));
        wrapper.set(ItemInstance::getShipmentOrderDetailId, null);
        itemInstanceMapper.update(null, wrapper);
    }

    private void fillAndValidateBeforeSave(ItemInstanceBo bo) {
        if (StrUtil.isBlank(bo.getInstanceCode())) {
            bo.setInstanceCode(generateInstanceCode());
        }
        if (StrUtil.isBlank(bo.getInstanceStatus())) {
            bo.setInstanceStatus(ServiceConstants.ItemInstanceStatus.PENDING_RECEIPT);
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

    private Long resolveLedgerUpdateBoxId(ItemInstance itemInstance, String boxCode) {
        String trimmedBoxCode = StrUtil.trim(boxCode);
        if (StrUtil.isBlank(trimmedBoxCode)) {
            return null;
        }
        Assert.isTrue(itemInstance.getBoxId() == null, "当前单品实例已绑定箱码，不支持修改箱码");
        Assert.isTrue(ServiceConstants.ItemInstanceStatus.IN_STOCK.equals(itemInstance.getInstanceStatus()), "仅在库实例可以补录箱码");
        Assert.notNull(itemInstance.getWarehouseId(), "当前单品实例缺少仓库信息，无法补录箱码");
        Assert.notNull(itemInstance.getAreaId(), "当前单品实例缺少库区信息，无法补录箱码");
        Assert.notNull(itemInstance.getRackId(), "当前单品实例缺少货架信息，无法补录箱码");
        Assert.notNull(itemInstance.getLocationId(), "当前单品实例缺少货位信息，无法补录箱码");

        Box box = queryBoxEntityByCode(trimmedBoxCode);
        if (box == null) {
            Box add = new Box();
            add.setBoxCode(trimmedBoxCode);
            add.setBoxName(trimmedBoxCode);
            add.setBoxStatus(ServiceConstants.BoxStatus.PACKED);
            add.setWarehouseId(itemInstance.getWarehouseId());
            add.setAreaId(itemInstance.getAreaId());
            add.setRackId(itemInstance.getRackId());
            add.setLocationId(itemInstance.getLocationId());
            add.setItemCount(0);
            boxMapper.insert(add);
            return add.getId();
        }

        Assert.isFalse(ServiceConstants.BoxStatus.DISABLED.equals(box.getBoxStatus()), "箱体已停用，无法补录箱码");
        Assert.isFalse(ServiceConstants.BoxStatus.OUTBOUND.equals(box.getBoxStatus()), "箱体已出库，无法补录箱码");
        if (countItemsByBoxId(box.getId()) > 0) {
            Assert.isTrue(sameLocation(box, itemInstance), "箱码" + trimmedBoxCode + "当前已在其他位置存在装箱关系，请先处理原状态");
        }

        Box update = new Box();
        update.setId(box.getId());
        update.setBoxStatus(ServiceConstants.BoxStatus.PACKED);
        update.setWarehouseId(itemInstance.getWarehouseId());
        update.setAreaId(itemInstance.getAreaId());
        update.setRackId(itemInstance.getRackId());
        update.setLocationId(itemInstance.getLocationId());
        boxMapper.updateById(update);
        return box.getId();
    }

    private Box queryBoxEntityByCode(String boxCode) {
        LambdaQueryWrapper<Box> lqw = Wrappers.lambdaQuery();
        lqw.eq(Box::getBoxCode, boxCode);
        return boxMapper.selectOne(lqw);
    }

    private long countItemsByBoxId(Long boxId) {
        LambdaQueryWrapper<ItemInstance> lqw = Wrappers.lambdaQuery();
        lqw.eq(ItemInstance::getBoxId, boxId);
        return itemInstanceMapper.selectCount(lqw);
    }

    private boolean sameLocation(Box box, ItemInstance itemInstance) {
        return Objects.equals(box.getWarehouseId(), itemInstance.getWarehouseId())
            && Objects.equals(box.getAreaId(), itemInstance.getAreaId())
            && Objects.equals(box.getRackId(), itemInstance.getRackId())
            && Objects.equals(box.getLocationId(), itemInstance.getLocationId());
    }

    private void syncBoxSnapshot(Long boxId, String boxStatus) {
        Box update = new Box();
        update.setId(boxId);
        update.setBoxStatus(boxStatus);
        update.setItemCount((int) countItemsByBoxId(boxId));
        boxMapper.updateById(update);
    }

    private String generateInstanceCode() {
        return "II" + IdUtil.getSnowflakeNextIdStr();
    }

    private List<ItemInstance> queryByCodes(Set<String> instanceCodes) {
        if (CollUtil.isEmpty(instanceCodes)) {
            return List.of();
        }
        LambdaQueryWrapper<ItemInstance> lqw = Wrappers.lambdaQuery();
        lqw.in(ItemInstance::getInstanceCode, instanceCodes);
        return itemInstanceMapper.selectList(lqw);
    }

    private ItemInstance resolveReceiptItemInstance(ReceiptItemInstanceBo receiptItemInstance,
                                                    Map<Long, ItemInstance> itemInstanceMap,
                                                    Map<String, ItemInstance> itemInstanceCodeMap) {
        if (receiptItemInstance.getId() != null) {
            ItemInstance itemInstance = itemInstanceMap.get(receiptItemInstance.getId());
            Assert.notNull(itemInstance, "器材实例不存在");
            return itemInstance;
        }
        String instanceCode = StrUtil.trim(receiptItemInstance.getInstanceCode());
        Assert.isTrue(StrUtil.isNotBlank(instanceCode), "器材实例编码不能为空");
        ItemInstance itemInstance = itemInstanceCodeMap.get(instanceCode);
        Assert.notNull(itemInstance, "器材实例编码" + instanceCode + "不存在");
        return itemInstance;
    }

    private void validateAvailableForReceipt(ItemInstance itemInstance, String instanceCode, Long currentReceiptDetailId) {
        String displayCode = StrUtil.blankToDefault(instanceCode, itemInstance.getInstanceCode());
        Assert.notNull(itemInstance.getId(), "器材实例编码" + displayCode + "无效");
        Assert.isTrue(ServiceConstants.ItemInstanceStatus.PENDING_RECEIPT.equals(itemInstance.getInstanceStatus()),
            "器材实例编码" + displayCode + "当前状态不可入库，仅支持待入库实例");
        Assert.isTrue(itemInstance.getBoxId() == null, "器材实例编码" + displayCode + "已绑定箱体");
        boolean reservedByCurrentDetail = Objects.equals(itemInstance.getReceiptOrderDetailId(), currentReceiptDetailId);
        Assert.isTrue(itemInstance.getWarehouseId() == null
                && itemInstance.getAreaId() == null
                && itemInstance.getRackId() == null
                && itemInstance.getLocationId() == null
                && (itemInstance.getReceiptOrderDetailId() == null || reservedByCurrentDetail),
            "器材实例编码" + displayCode + "已入库或已被入库单占用");
    }

    private void validateAvailableForShipment(ItemInstance itemInstance, Long currentShipmentDetailId) {
        Assert.notNull(itemInstance.getId(), "单品实例无效");
        Assert.isTrue(ServiceConstants.ItemInstanceStatus.IN_STOCK.equals(itemInstance.getInstanceStatus()),
            "单品实例" + itemInstance.getInstanceCode() + "当前状态不可出库，仅支持在库实例");
        boolean reservedByCurrentDetail = Objects.equals(itemInstance.getShipmentOrderDetailId(), currentShipmentDetailId);
        Assert.isTrue(itemInstance.getShipmentOrderDetailId() == null || reservedByCurrentDetail,
            "单品实例" + itemInstance.getInstanceCode() + "已被其他出库单占用");
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
        Set<Long> boxIds = validList.stream().map(ItemInstanceVo::getBoxId).filter(Objects::nonNull).collect(Collectors.toSet());
        Map<Long, ItemSkuVo> skuMap = itemSkuService.queryVosByIds(skuIds).stream().collect(Collectors.toMap(ItemSkuVo::getId, Function.identity()));
        Map<Long, Warehouse> warehouseMap = warehouseIds.isEmpty() ? java.util.Collections.emptyMap() :
            warehouseMapper.selectBatchIds(warehouseIds).stream().collect(Collectors.toMap(Warehouse::getId, Function.identity()));
        Map<Long, Area> areaMap = areaIds.isEmpty() ? java.util.Collections.emptyMap() :
            areaMapper.selectBatchIds(areaIds).stream().collect(Collectors.toMap(Area::getId, Function.identity()));
        Map<Long, Rack> rackMap = rackIds.isEmpty() ? java.util.Collections.emptyMap() :
            rackMapper.selectBatchIds(rackIds).stream().collect(Collectors.toMap(Rack::getId, Function.identity()));
        Map<Long, Location> locationMap = locationIds.isEmpty() ? java.util.Collections.emptyMap() :
            locationMapper.selectBatchIds(locationIds).stream().collect(Collectors.toMap(Location::getId, Function.identity()));
        Map<Long, Box> boxMap = boxIds.isEmpty() ? java.util.Collections.emptyMap() :
            boxMapper.selectBatchIds(boxIds).stream().collect(Collectors.toMap(Box::getId, Function.identity()));
        validList.forEach(vo -> {
            ItemSkuVo skuVo = skuMap.get(vo.getSkuId());
            if (skuVo != null) {
                vo.setSkuName(skuVo.getSkuName());
                vo.setProductIdentifier(skuVo.getProductIdentifier());
                vo.setQualityGrade(skuVo.getQualityGrade());
                if (skuVo.getItem() != null) {
                    vo.setItemName(skuVo.getItem().getItemName());
                    vo.setItemCode(skuVo.getItem().getItemCode());
                    vo.setUnit(skuVo.getItem().getUnit());
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
            Box box = boxMap.get(vo.getBoxId());
            if (box != null) {
                vo.setBoxCode(box.getBoxCode());
            }
        });
    }
}
