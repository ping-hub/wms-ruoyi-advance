package com.ruoyi.wms.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.constant.ServiceConstants;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.exception.base.BaseException;
import com.ruoyi.common.core.utils.MapstructUtils;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.mybatis.core.domain.BaseEntity;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.ruoyi.system.service.SysDictTypeService;
import com.ruoyi.wms.domain.bo.InventoryBo;
import com.ruoyi.wms.domain.bo.ReceiptItemInstanceBo;
import com.ruoyi.wms.domain.bo.ReceiptOrderBo;
import com.ruoyi.wms.domain.bo.ReceiptOrderDetailBo;
import com.ruoyi.wms.domain.entity.Box;
import com.ruoyi.wms.domain.entity.InventoryDetail;
import com.ruoyi.wms.domain.entity.InventoryHistory;
import com.ruoyi.wms.domain.entity.ItemInstance;
import com.ruoyi.wms.domain.entity.ReceiptOrder;
import com.ruoyi.wms.domain.entity.ReceiptOrderDetail;
import com.ruoyi.wms.domain.vo.ItemInstanceVo;
import com.ruoyi.wms.domain.vo.ReceiptItemInstanceVo;
import com.ruoyi.wms.domain.vo.ReceiptOrderVo;
import com.ruoyi.wms.mapper.ReceiptOrderDetailMapper;
import com.ruoyi.wms.mapper.ReceiptOrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 入库单Service业务层处理
 *
 * @author zcc
 * @date 2024-07-19
 */
@RequiredArgsConstructor
@Service
public class ReceiptOrderService {

    private final ReceiptOrderMapper receiptOrderMapper;
    private final ReceiptOrderDetailService receiptOrderDetailService;
    private final ReceiptOrderDetailMapper receiptOrderDetailMapper;
    private final InventoryService inventoryService;
    private final InventoryDetailService inventoryDetailService;
    private final InventoryHistoryService inventoryHistoryService;
    private final SysDictTypeService dictTypeService;
    private final ItemInstanceService itemInstanceService;
    private final BoxService boxService;
    private final LocationService locationService;

    /**
     * 查询入库单
     */
    public ReceiptOrderVo queryById(Long id){
        ReceiptOrderVo receiptOrderVo = receiptOrderMapper.selectVoById(id);
        Assert.notNull(receiptOrderVo, "入库单不存在");
        receiptOrderVo.setDetails(receiptOrderDetailService.queryByReceiptOrderId(id));
        attachReceiptInstances(receiptOrderVo);
        return receiptOrderVo;
    }

    /**
     * 查询入库单列表
     */
    public TableDataInfo<ReceiptOrderVo> queryPageList(ReceiptOrderBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ReceiptOrder> lqw = buildQueryWrapper(bo);
        Page<ReceiptOrderVo> result = receiptOrderMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询入库单列表
     */
    public List<ReceiptOrderVo> queryList(ReceiptOrderBo bo) {
        LambdaQueryWrapper<ReceiptOrder> lqw = buildQueryWrapper(bo);
        return receiptOrderMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<ReceiptOrder> buildQueryWrapper(ReceiptOrderBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<ReceiptOrder> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getReceiptOrderNo()), ReceiptOrder::getReceiptOrderNo, bo.getReceiptOrderNo());
        lqw.eq(bo.getReceiptOrderType() != null, ReceiptOrder::getReceiptOrderType, bo.getReceiptOrderType());
        lqw.eq(bo.getMerchantId() != null, ReceiptOrder::getMerchantId, bo.getMerchantId());
        lqw.eq(StringUtils.isNotBlank(bo.getOrderNo()), ReceiptOrder::getOrderNo, bo.getOrderNo());
        lqw.like(StringUtils.isNotBlank(bo.getBasisNo()), ReceiptOrder::getBasisNo, bo.getBasisNo());
        lqw.eq(StringUtils.isNotBlank(bo.getDispatchMode()), ReceiptOrder::getDispatchMode, bo.getDispatchMode());
        lqw.like(StringUtils.isNotBlank(bo.getNoticeOrg()), ReceiptOrder::getNoticeOrg, bo.getNoticeOrg());
        lqw.like(StringUtils.isNotBlank(bo.getReceiveUnit()), ReceiptOrder::getReceiveUnit, bo.getReceiveUnit());
        lqw.eq(bo.getPayableAmount() != null, ReceiptOrder::getPayableAmount, bo.getPayableAmount());
        lqw.eq(bo.getReceiptOrderStatus() != null, ReceiptOrder::getReceiptOrderStatus, bo.getReceiptOrderStatus());
        lqw.orderByDesc(BaseEntity::getCreateTime);
        return lqw;
    }

    /**
     * 暂存入库单
     */
    @Transactional
    public void insertByBo(ReceiptOrderBo bo) {
        // 校验入库单号唯一性
        validateReceiptOrderNo(bo.getReceiptOrderNo());
        // 创建入库单
        ReceiptOrder add = MapstructUtils.convert(bo, ReceiptOrder.class);
        receiptOrderMapper.insert(add);
        bo.setId(add.getId());
        List<ReceiptOrderDetailBo> detailBoList = bo.getDetails();
        List<ReceiptOrderDetail> addDetailList = MapstructUtils.convert(detailBoList, ReceiptOrderDetail.class);
        addDetailList.forEach(it -> {
            it.setReceiptOrderId(add.getId());
        });
        // 创建入库单明细
        receiptOrderDetailService.saveDetails(addDetailList);
        for (int i = 0; i < Math.min(detailBoList.size(), addDetailList.size()); i++) {
            detailBoList.get(i).setId(addDetailList.get(i).getId());
        }
        if (CollUtil.isNotEmpty(detailBoList)) {
            validateReceiptInstances(detailBoList);
            itemInstanceService.reserveForReceiptDetails(detailBoList);
        }
    }

    /**
     * 入库：
     * 1.校验
     * 2.保存入库单和入库单明细
     * 3.录入器材实例与箱体绑定
     * 4.保存库存明细
     * 5.增加库存
     * 6.保存库存记录
     */
    @Transactional
    public void receive(ReceiptOrderBo bo) {
        // 1. 校验
        validateBeforeReceive(bo);

        // 2. 保存入库单和入库单明细
        if (Objects.isNull(bo.getId())) {
            insertByBo(bo);
        } else {
            updateByBo(bo);
        }

        ReceiptOrder receiptOrder = receiptOrderMapper.selectById(bo.getId());
        Assert.notNull(receiptOrder, "入库单不存在");

        // 3.录入器材实例与箱体绑定
        Map<String, Box> receiptBoxMap = prepareReceiptBoxes(bo.getDetails());
        List<ItemInstance> receivedInstances = itemInstanceService.receiveByReceiptOrder(receiptOrder, bo.getDetails(),
            bo.getReceiveUnit(), receiptBoxMap);
        receiptBoxMap.values().forEach(box -> boxService.moveTo(box.getId(), box.getWarehouseId(), box.getAreaId(), box.getRackId(), box.getLocationId()));
        Set<Long> locationIds = new HashSet<>();
        receivedInstances.forEach(item -> {
            if (item.getLocationId() != null) {
                locationIds.add(item.getLocationId());
            }
        });
        receiptBoxMap.values().forEach(box -> {
            if (box.getLocationId() != null) {
                locationIds.add(box.getLocationId());
            }
        });
        locationService.refreshOccupiedFlagsByLocationIds(locationIds);

        // 4.保存库存明细
        this.saveInventoryDetails(bo, receivedInstances);

        // 5.增加库存
        List<InventoryBo> inventoryList = convertInventoryList(bo.getDetails());
        inventoryService.updateInventoryQuantity(inventoryList);

        // 6.保存库存记录
        this.saveInventoryHistory(bo, receivedInstances);
    }

    private void validateBeforeReceive(ReceiptOrderBo bo) {
        if (CollUtil.isEmpty(bo.getDetails())) {
            throw new BaseException("商品明细不能为空");
        }
        if (bo.getId() != null) {
            ReceiptOrder receiptOrder = receiptOrderMapper.selectById(bo.getId());
            Assert.notNull(receiptOrder, "入库单不存在");
            Assert.isFalse(ServiceConstants.ReceiptOrderStatus.FINISH.equals(receiptOrder.getReceiptOrderStatus()), "入库单已完成入库");
            Assert.isTrue(itemInstanceService.countByReceiptOrderId(bo.getId()) == 0, "入库单已生成单品实例，请勿重复入库");
        }
        validateReceiptInstances(bo.getDetails());
    }

    private void saveInventoryHistory(ReceiptOrderBo bo, List<ItemInstance> receivedInstances){
        List<InventoryHistory> inventoryHistoryList = new LinkedList<>();
        Map<Long, List<ItemInstance>> itemMapByDetailId = receivedInstances.stream()
            .filter(it -> it.getReceiptOrderDetailId() != null)
            .collect(java.util.stream.Collectors.groupingBy(ItemInstance::getReceiptOrderDetailId));
        bo.getDetails().forEach(detail -> {
            List<ItemInstance> itemInstances = itemMapByDetailId.get(detail.getId());
            if (CollUtil.isEmpty(itemInstances)) {
                inventoryHistoryList.add(buildInventoryHistory(bo, detail, null));
                return;
            }
            itemInstances.forEach(itemInstance -> inventoryHistoryList.add(buildInventoryHistory(bo, detail, itemInstance)));
        });
        inventoryHistoryService.saveBatch(inventoryHistoryList);
    }

    private void saveInventoryDetails(ReceiptOrderBo bo, List<ItemInstance> receivedInstances){
        List<InventoryDetail> inventoryDetailList = new ArrayList<>();
        Map<Long, List<ItemInstance>> itemMapByDetailId = receivedInstances.stream()
            .filter(it -> it.getReceiptOrderDetailId() != null)
            .collect(java.util.stream.Collectors.groupingBy(ItemInstance::getReceiptOrderDetailId));
        bo.getDetails().forEach(detail -> {
            List<ItemInstance> itemInstances = itemMapByDetailId.get(detail.getId());
            if (CollUtil.isEmpty(itemInstances)) {
                inventoryDetailList.add(buildInventoryDetail(bo, detail, null));
                return;
            }
            itemInstances.forEach(itemInstance -> inventoryDetailList.add(buildInventoryDetail(bo, detail, itemInstance)));
        });
        inventoryDetailService.saveBatch(inventoryDetailList);
    }

    /**
     * 合并入库单详情
     * 合并key：warehouseId_areaId_rackId_locationId_skuId
     * @param orderDetailBoList 明细
     * @return 合并后的库存变更
     */
    private List<InventoryBo> convertInventoryList(List<ReceiptOrderDetailBo> orderDetailBoList) {
        Function<ReceiptOrderDetailBo, String> keyFunction = it -> it.getWarehouseId() + "_" + it.getAreaId() + "_" + it.getRackId() + "_" + it.getLocationId() + "_" + it.getSkuId();
        Map<String, InventoryBo> inventoryMap = new HashMap<>();
        orderDetailBoList.forEach(orderDetailBo -> {
            String key = keyFunction.apply(orderDetailBo);
            if (inventoryMap.containsKey(key)) {
                InventoryBo mergedItem = inventoryMap.get(key);
                mergedItem.setQuantity(mergedItem.getQuantity().add(orderDetailBo.getQuantity()));
            } else {
                InventoryBo inventory = new InventoryBo();
                inventory.setSkuId(orderDetailBo.getSkuId());
                inventory.setWarehouseId(orderDetailBo.getWarehouseId());
                inventory.setAreaId(orderDetailBo.getAreaId());
                inventory.setRackId(orderDetailBo.getRackId());
                inventory.setLocationId(orderDetailBo.getLocationId());
                inventory.setQuantity(orderDetailBo.getQuantity());
                inventoryMap.put(key, inventory);
            }
        });
        return new ArrayList<>(inventoryMap.values());
    }

    /**
     * 修改入库单
     */
    @Transactional
    public void updateByBo(ReceiptOrderBo bo) {
        // 更新入库单
        ReceiptOrder update = MapstructUtils.convert(bo, ReceiptOrder.class);
        receiptOrderMapper.updateById(update);
        // 保存入库单明细
        List<ReceiptOrderDetail> existedDetails = receiptOrderDetailService.queryEntitiesByReceiptOrderId(bo.getId());
        List<Long> incomingIds = bo.getDetails().stream()
            .map(ReceiptOrderDetailBo::getId)
            .filter(Objects::nonNull)
            .toList();
        List<Long> existedIds = existedDetails.stream()
            .map(ReceiptOrderDetail::getId)
            .filter(Objects::nonNull)
            .toList();
        List<Long> deleteIds = existedIds.stream()
            .filter(id -> !incomingIds.contains(id))
            .toList();
        if (CollUtil.isNotEmpty(deleteIds)) {
            receiptOrderDetailService.deleteByIds(deleteIds);
        }
        List<ReceiptOrderDetail> detailList = MapstructUtils.convert(bo.getDetails(), ReceiptOrderDetail.class);
        detailList.forEach(it -> it.setReceiptOrderId(bo.getId()));
        receiptOrderDetailService.saveDetails(detailList);
        for (int i = 0; i < Math.min(bo.getDetails().size(), detailList.size()); i++) {
            bo.getDetails().get(i).setId(detailList.get(i).getId());
        }
        itemInstanceService.releaseReceiptReservationsByDetailIds(existedIds);
        if (CollUtil.isNotEmpty(bo.getDetails())) {
            validateReceiptInstances(bo.getDetails());
            itemInstanceService.reserveForReceiptDetails(bo.getDetails());
        }
    }

    /**
     * 入库单作废
     * @param id
     */
    public void editToInvalid(Long id) {
        validateIdBeforeDelete(id);
        List<Long> detailIds = receiptOrderDetailService.queryEntitiesByReceiptOrderId(id).stream()
            .map(ReceiptOrderDetail::getId)
            .filter(Objects::nonNull)
            .toList();
        itemInstanceService.releaseReceiptReservationsByDetailIds(detailIds);
        LambdaUpdateWrapper<ReceiptOrder> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(ReceiptOrder::getId, id);
        wrapper.set(ReceiptOrder::getReceiptOrderStatus, ServiceConstants.ReceiptOrderStatus.INVALID);
        receiptOrderMapper.update(null, wrapper);
    }

    /**
     * 删除入库单
     */
    public void deleteById(Long id) {
        validateIdBeforeDelete(id);
        List<Long> detailIds = receiptOrderDetailService.queryEntitiesByReceiptOrderId(id).stream()
            .map(ReceiptOrderDetail::getId)
            .filter(Objects::nonNull)
            .toList();
        itemInstanceService.releaseReceiptReservationsByDetailIds(detailIds);
        receiptOrderMapper.deleteById(id);
    }

    private void validateIdBeforeDelete(Long id) {
        ReceiptOrderVo receiptOrderVo = queryById(id);
        Assert.notNull(receiptOrderVo, "入库单不存在");
        if (ServiceConstants.ReceiptOrderStatus.FINISH.equals(receiptOrderVo.getReceiptOrderStatus())) {
            throw new ServiceException("入库单【" + receiptOrderVo.getReceiptOrderNo() + "】已入库，无法删除！", HttpStatus.CONFLICT.value());
        }
    }

    /**
     * 批量删除入库单
     */
    public void deleteByIds(Collection<Long> ids) {
        if (CollUtil.isNotEmpty(ids)) {
            List<Long> detailIds = ids.stream()
                .filter(Objects::nonNull)
                .flatMap(id -> receiptOrderDetailService.queryEntitiesByReceiptOrderId(id).stream())
                .map(ReceiptOrderDetail::getId)
                .filter(Objects::nonNull)
                .toList();
            itemInstanceService.releaseReceiptReservationsByDetailIds(detailIds);
        }
        receiptOrderMapper.deleteBatchIds(ids);
    }

    public void validateReceiptOrderNo(String receiptOrderNo) {
        LambdaQueryWrapper<ReceiptOrder> receiptOrderLqw = Wrappers.lambdaQuery();
        receiptOrderLqw.eq(ReceiptOrder::getReceiptOrderNo, receiptOrderNo);
        ReceiptOrder receiptOrder = receiptOrderMapper.selectOne(receiptOrderLqw);
        Assert.isNull(receiptOrder, "入库单号重复，请手动修改");
    }

    private void attachReceiptInstances(ReceiptOrderVo receiptOrderVo) {
        if (CollUtil.isEmpty(receiptOrderVo.getDetails())) {
            return;
        }
        Set<Long> detailIds = receiptOrderVo.getDetails().stream().map(com.ruoyi.wms.domain.vo.ReceiptOrderDetailVo::getId).filter(Objects::nonNull).collect(Collectors.toSet());
        Map<Long, List<ItemInstanceVo>> itemMapByDetailId = itemInstanceService.queryVoMapByReceiptDetailIds(detailIds);
        receiptOrderVo.getDetails().forEach(detail -> {
            List<ItemInstanceVo> itemInstanceVos = itemMapByDetailId.getOrDefault(detail.getId(), List.of());
            detail.setReceiptItemInstances(itemInstanceVos.stream().map(this::toReceiptItemInstanceVo).toList());
        });
    }

    private ReceiptItemInstanceVo toReceiptItemInstanceVo(ItemInstanceVo itemInstanceVo) {
        ReceiptItemInstanceVo vo = new ReceiptItemInstanceVo();
        vo.setId(itemInstanceVo.getId());
        vo.setInstanceCode(itemInstanceVo.getInstanceCode());
        vo.setBoxId(itemInstanceVo.getBoxId());
        vo.setBoxCode(itemInstanceVo.getBoxCode());
        vo.setProductMark(itemInstanceVo.getProductMark());
        vo.setQualityGrade(itemInstanceVo.getQualityGrade());
        vo.setRemark(itemInstanceVo.getRemark());
        return vo;
    }

    private void validateReceiptInstances(List<ReceiptOrderDetailBo> details) {
        Set<String> instanceKeySet = new HashSet<>();
        for (ReceiptOrderDetailBo detail : details) {
            int instanceCount = convertInstanceCount(detail.getQuantity());
            List<ReceiptItemInstanceBo> receiptItemInstances = detail.getReceiptItemInstances();
            Assert.isTrue(CollUtil.isNotEmpty(receiptItemInstances), "请先选择器材实例");
            Assert.isTrue(receiptItemInstances.size() == instanceCount, "器材实例数量与入库数量不一致");
            for (ReceiptItemInstanceBo receiptItemInstance : receiptItemInstances) {
                String instanceCode = cn.hutool.core.util.StrUtil.trim(receiptItemInstance.getInstanceCode());
                Long instanceId = receiptItemInstance.getId();
                Assert.isTrue(instanceId != null || cn.hutool.core.util.StrUtil.isNotBlank(instanceCode), "器材实例不能为空");
                String uniqueKey = instanceId != null ? "ID:" + instanceId : "CODE:" + instanceCode;
                Assert.isTrue(instanceKeySet.add(uniqueKey), "器材实例存在重复：" + (instanceId != null ? instanceId : instanceCode));
            }
        }
    }

    private int convertInstanceCount(java.math.BigDecimal quantity) {
        Assert.notNull(quantity, "入库数量不能为空");
        Assert.isTrue(quantity.compareTo(java.math.BigDecimal.ZERO) > 0, "入库数量必须大于0");
        try {
            return quantity.intValueExact();
        } catch (ArithmeticException e) {
            throw new ServiceException("录入器材实例时，入库数量必须为整数", HttpStatus.CONFLICT.value());
        }
    }

    private Map<String, Box> prepareReceiptBoxes(List<ReceiptOrderDetailBo> details) {
        Map<String, Box> boxMap = new HashMap<>();
        for (ReceiptOrderDetailBo detail : details) {
            if (CollUtil.isEmpty(detail.getReceiptItemInstances())) {
                continue;
            }
            for (ReceiptItemInstanceBo receiptItemInstance : detail.getReceiptItemInstances()) {
                String boxCode = cn.hutool.core.util.StrUtil.trim(receiptItemInstance.getBoxCode());
                if (cn.hutool.core.util.StrUtil.isBlank(boxCode)) {
                    continue;
                }
                Box box = boxMap.get(boxCode);
                if (box == null) {
                    box = boxService.getOrCreateForReceipt(boxCode, detail.getWarehouseId(), detail.getAreaId(), detail.getRackId(), detail.getLocationId());
                    boxMap.put(boxCode, box);
                } else {
                    Assert.isTrue(Objects.equals(box.getWarehouseId(), detail.getWarehouseId())
                            && Objects.equals(box.getAreaId(), detail.getAreaId())
                            && Objects.equals(box.getRackId(), detail.getRackId())
                            && Objects.equals(box.getLocationId(), detail.getLocationId()),
                        "同一箱码在本次入库中必须落在同一位置");
                }
            }
        }
        return boxMap;
    }

    private InventoryDetail buildInventoryDetail(ReceiptOrderBo bo, ReceiptOrderDetailBo detail, ItemInstance itemInstance) {
        InventoryDetail inventoryDetail = new InventoryDetail();
        inventoryDetail.setReceiptOrderId(bo.getId());
        inventoryDetail.setOrderNo(bo.getOrderNo());
        inventoryDetail.setType(ServiceConstants.InventoryDetailType.RECEIPT);
        inventoryDetail.setSkuId(detail.getSkuId());
        inventoryDetail.setWarehouseId(detail.getWarehouseId());
        inventoryDetail.setAreaId(detail.getAreaId());
        inventoryDetail.setRackId(detail.getRackId());
        inventoryDetail.setLocationId(detail.getLocationId());
        inventoryDetail.setQuantity(itemInstance == null ? detail.getQuantity() : java.math.BigDecimal.ONE);
        inventoryDetail.setRemainQuantity(itemInstance == null ? detail.getQuantity() : java.math.BigDecimal.ONE);
        inventoryDetail.setItemInstanceId(itemInstance == null ? null : itemInstance.getId());
        inventoryDetail.setBoxId(itemInstance == null ? null : itemInstance.getBoxId());
        inventoryDetail.setProductionDate(detail.getProductionDate());
        inventoryDetail.setExpirationDate(detail.getExpirationDate());
        inventoryDetail.setAmount(itemInstance == null ? detail.getAmount() : detail.getUnitPrice());
        inventoryDetail.setEquipmentCode(itemInstance == null ? detail.getEquipmentCode() : itemInstance.getInstanceCode());
        inventoryDetail.setSpecModel(detail.getSpecModel());
        inventoryDetail.setProductMark(itemInstance == null ? detail.getProductMark() : itemInstance.getProductMark());
        inventoryDetail.setQualityGrade(itemInstance == null ? detail.getQualityGrade() : itemInstance.getQualityGrade());
        inventoryDetail.setUnitPrice(detail.getUnitPrice());
        inventoryDetail.setLineAmount(itemInstance == null ? detail.getLineAmount() : detail.getUnitPrice());
        inventoryDetail.setBelongUnit(bo.getReceiveUnit());
        inventoryDetail.setRemark(itemInstance == null ? detail.getRemark() : itemInstance.getRemark());
        return inventoryDetail;
    }

    private InventoryHistory buildInventoryHistory(ReceiptOrderBo bo, ReceiptOrderDetailBo detail, ItemInstance itemInstance) {
        InventoryHistory inventoryHistory = new InventoryHistory();
        inventoryHistory.setOrderId(bo.getId());
        inventoryHistory.setOrderNo(bo.getReceiptOrderNo());
        inventoryHistory.setOrderType(ServiceConstants.InventoryHistoryOrderType.RECEIPT);
        inventoryHistory.setSkuId(detail.getSkuId());
        inventoryHistory.setQuantity(itemInstance == null ? detail.getQuantity() : java.math.BigDecimal.ONE);
        inventoryHistory.setWarehouseId(detail.getWarehouseId());
        inventoryHistory.setAreaId(detail.getAreaId());
        inventoryHistory.setRackId(detail.getRackId());
        inventoryHistory.setLocationId(detail.getLocationId());
        inventoryHistory.setItemInstanceId(itemInstance == null ? null : itemInstance.getId());
        inventoryHistory.setBoxId(itemInstance == null ? null : itemInstance.getBoxId());
        inventoryHistory.setProductionDate(detail.getProductionDate());
        inventoryHistory.setExpirationDate(detail.getExpirationDate());
        inventoryHistory.setAmount(itemInstance == null ? detail.getAmount() : detail.getUnitPrice());
        inventoryHistory.setEquipmentCode(itemInstance == null ? detail.getEquipmentCode() : itemInstance.getInstanceCode());
        inventoryHistory.setSpecModel(detail.getSpecModel());
        inventoryHistory.setProductMark(itemInstance == null ? detail.getProductMark() : itemInstance.getProductMark());
        inventoryHistory.setQualityGrade(itemInstance == null ? detail.getQualityGrade() : itemInstance.getQualityGrade());
        inventoryHistory.setUnitPrice(detail.getUnitPrice());
        inventoryHistory.setLineAmount(itemInstance == null ? detail.getLineAmount() : detail.getUnitPrice());
        inventoryHistory.setBelongUnit(bo.getReceiveUnit());
        inventoryHistory.setRemark(itemInstance == null ? detail.getRemark() : itemInstance.getRemark());
        return inventoryHistory;
    }
}
