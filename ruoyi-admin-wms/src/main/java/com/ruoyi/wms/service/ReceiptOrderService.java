package com.ruoyi.wms.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
import com.ruoyi.wms.domain.vo.ItemSkuVo;
import com.ruoyi.wms.domain.vo.ReceiptItemInstanceVo;
import com.ruoyi.wms.domain.vo.ReceiptOrderVo;
import com.ruoyi.wms.mapper.ReceiptOrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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

    @Value("${warehouse}")
    private String warehouse;

    private final ReceiptOrderMapper receiptOrderMapper;
    private final ReceiptOrderDetailService receiptOrderDetailService;
    private final InventoryService inventoryService;
    private final InventoryDetailService inventoryDetailService;
    private final InventoryHistoryService inventoryHistoryService;
    private final ItemInstanceService itemInstanceService;
    private final ItemSkuService itemSkuService;
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
        LambdaQueryWrapper<ReceiptOrder> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getReceiptOrderNo()), ReceiptOrder::getReceiptOrderNo, bo.getReceiptOrderNo());
        lqw.eq(StringUtils.isNotBlank(bo.getReceiptOrderType()), ReceiptOrder::getReceiptOrderType, bo.getReceiptOrderType());
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
        normalizeReceiptDetails(bo.getDetails());
        bo.setReceiptOrderNo(StrUtil.blankToDefault(bo.getReceiptOrderNo(), generateReceiptOrderNo()));
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
            receiptBoxMap);
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
        normalizeReceiptDetails(bo.getDetails());
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
        if (ServiceConstants.ReceiptOrderStatus.INVALID.equals(receiptOrderVo.getReceiptOrderStatus())) {
            throw new ServiceException("入库单【" + receiptOrderVo.getReceiptOrderNo() + "】已作废，无法删除！", HttpStatus.CONFLICT.value());
        }
        if (ServiceConstants.ReceiptOrderStatus.FINISH.equals(receiptOrderVo.getReceiptOrderStatus())) {
            throw new ServiceException("入库单【" + receiptOrderVo.getReceiptOrderNo() + "】已入库，无法删除！", HttpStatus.CONFLICT.value());
        }
    }

    /**
     * 批量删除入库单
     */
    public void deleteByIds(Collection<Long> ids) {
        if (CollUtil.isNotEmpty(ids)) {
            ids.stream().filter(Objects::nonNull).forEach(this::validateIdBeforeDelete);
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
        Assert.isNull(receiptOrder, "系统生成的入库单号重复，请稍后重试");
    }

    private String generateReceiptOrderNo() {
        return "RK" + warehouse + IdUtil.getSnowflakeNextIdStr();
    }

    private void attachReceiptInstances(ReceiptOrderVo receiptOrderVo) {
        if (CollUtil.isEmpty(receiptOrderVo.getDetails())) {
            return;
        }
        Set<Long> detailIds = receiptOrderVo.getDetails().stream().map(com.ruoyi.wms.domain.vo.ReceiptOrderDetailVo::getId).filter(Objects::nonNull).collect(Collectors.toSet());
        Map<Long, List<ItemInstanceVo>> itemMapByDetailId = itemInstanceService.queryVoMapByReceiptDetailIds(detailIds);
        receiptOrderVo.getDetails().forEach(detail -> {
            List<ItemInstanceVo> itemInstanceVos = itemMapByDetailId.getOrDefault(detail.getId(), List.of());
            List<ReceiptItemInstanceVo> receiptItemInstances = itemInstanceVos.stream()
                .map(this::toReceiptItemInstanceVo)
                .collect(Collectors.toList());
            if (CollUtil.isNotEmpty(receiptItemInstances) && StringUtils.isNotBlank(detail.getBoxCode())) {
                receiptItemInstances.forEach(item -> {
                    if (StringUtils.isBlank(item.getBoxCode())) {
                        item.setBoxCode(detail.getBoxCode());
                    }
                });
            }
            detail.setReceiptItemInstances(receiptItemInstances);
        });
    }

    private ReceiptItemInstanceVo toReceiptItemInstanceVo(ItemInstanceVo itemInstanceVo) {
        ReceiptItemInstanceVo vo = new ReceiptItemInstanceVo();
        vo.setId(itemInstanceVo.getId());
        vo.setInstanceCode(itemInstanceVo.getInstanceCode());
        vo.setBoxId(itemInstanceVo.getBoxId());
        vo.setBoxCode(itemInstanceVo.getBoxCode());
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
        inventoryDetail.setUnitPrice(detail.getUnitPrice());
        inventoryDetail.setLineAmount(detail.getLineAmount());
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
        inventoryHistory.setUnitPrice(detail.getUnitPrice());
        inventoryHistory.setLineAmount(detail.getLineAmount());
        inventoryHistory.setRemark(itemInstance == null ? detail.getRemark() : itemInstance.getRemark());
        return inventoryHistory;
    }

    private void normalizeReceiptDetails(List<ReceiptOrderDetailBo> details) {
        if (CollUtil.isEmpty(details)) {
            return;
        }
        Map<Long, ItemSkuVo> skuMap = itemSkuService.queryVosByIds(details.stream()
            .map(ReceiptOrderDetailBo::getSkuId)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet()))
            .stream()
            .collect(Collectors.toMap(ItemSkuVo::getId, Function.identity()));
        details.forEach(detail -> {
            ItemSkuVo itemSku = skuMap.get(detail.getSkuId());
            Assert.notNull(itemSku, "规格不存在");
            fillReceiptSnapshot(detail, itemSku);
            if (StringUtils.isBlank(detail.getBoxCode())
                && CollUtil.isNotEmpty(detail.getReceiptItemInstances())) {
                detail.setBoxCode(StrUtil.trim(detail.getReceiptItemInstances().get(0).getBoxCode()));
            }
            if (detail.getQuantity() == null) {
                detail.setQuantity(BigDecimal.ONE);
            }
            BigDecimal lineAmount = calcLineAmount(detail.getQuantity(), detail.getUnitPrice());
            detail.setLineAmount(lineAmount);
        });
    }

    private void fillReceiptSnapshot(ReceiptOrderDetailBo detail, ItemSkuVo itemSku) {
        detail.setSkuName(itemSku.getSkuName());
        detail.setProductIdentifier(itemSku.getProductIdentifier());
        detail.setQualityGrade(itemSku.getQualityGrade());
        if (itemSku.getItem() != null) {
            detail.setItemCode(itemSku.getItem().getItemCode());
            detail.setItemName(itemSku.getItem().getItemName());
            detail.setUnit(itemSku.getItem().getUnit());
        }
    }

    private BigDecimal calcLineAmount(BigDecimal quantity, BigDecimal unitPrice) {
        if (quantity == null || unitPrice == null) {
            return BigDecimal.ZERO;
        }
        return quantity.multiply(unitPrice).setScale(2, java.math.RoundingMode.HALF_UP);
    }
}
