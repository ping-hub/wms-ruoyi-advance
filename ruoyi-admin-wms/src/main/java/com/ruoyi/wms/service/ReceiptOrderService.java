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
import com.ruoyi.wms.domain.bo.*;
import com.ruoyi.wms.domain.entity.InventoryDetail;
import com.ruoyi.wms.domain.entity.InventoryHistory;
import com.ruoyi.wms.domain.entity.ReceiptOrder;
import com.ruoyi.wms.domain.entity.ReceiptOrderDetail;
import com.ruoyi.wms.domain.vo.ItemSkuVo;
import com.ruoyi.wms.domain.vo.ItemSnVo;
import com.ruoyi.wms.domain.vo.ReceiptOrderDetailVo;
import com.ruoyi.wms.domain.vo.ReceiptOrderVo;
import com.ruoyi.wms.mapper.ReceiptOrderDetailMapper;
import com.ruoyi.wms.mapper.ReceiptOrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;

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
    private final ItemSnService itemSnService;
    private final OrderSnService orderSnService;
    private final ItemSkuService itemSkuService;

    /**
     * 查询入库单
     */
    public ReceiptOrderVo queryById(Long id){
        ReceiptOrderVo receiptOrderVo = receiptOrderMapper.selectVoById(id);
        Assert.notNull(receiptOrderVo, "入库单不存在");
        receiptOrderVo.setDetails(receiptOrderDetailService.queryByReceiptOrderId(id));
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

        // 将生成的对象回写到BO对象中
        for (int i = 0; i < detailBoList.size() && i < addDetailList.size(); i++) {
            BeanUtils.copyProperties(addDetailList.get(i), detailBoList.get(i));
        }
    }

    /**
     * 入库：
     * 1.校验
     * 2.保存入库单和入库单明细
     * 3.保存库存明细
     * 4.保存SN记录（如果启用SN模式）
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

        // 3.保存库存明细
        this.saveInventoryDetails(bo);

        // 4.保存SN记录（如果启用SN模式）
        this.saveItemSns(bo);

        // 5.增加库存
        List<InventoryBo> inventoryList = convertInventoryList(bo.getDetails());
        inventoryService.updateInventoryQuantity(inventoryList);

        // 6.保存库存记录
        this.saveInventoryHistory(bo);
    }

    private void validateBeforeReceive(ReceiptOrderBo bo) {
        if (CollUtil.isEmpty(bo.getDetails())) {
            throw new BaseException("商品明细不能为空");
        }
    }

    private void saveInventoryHistory(ReceiptOrderBo bo){
        List<InventoryHistory> inventoryHistoryList = new LinkedList<>();
        bo.getDetails().forEach(detail -> {
            InventoryHistory inventoryHistory = new InventoryHistory();
            inventoryHistory.setOrderId(bo.getId());
            inventoryHistory.setOrderNo(bo.getReceiptOrderNo());
            inventoryHistory.setOrderType(ServiceConstants.InventoryHistoryOrderType.RECEIPT);
            inventoryHistory.setSkuId(detail.getSkuId());
            inventoryHistory.setQuantity(detail.getQuantity());
            inventoryHistory.setWarehouseId(detail.getWarehouseId());
            inventoryHistory.setAreaId(detail.getAreaId());
            inventoryHistory.setBatchNo(detail.getBatchNo());
            inventoryHistory.setProductionDate(detail.getProductionDate());
            inventoryHistory.setExpirationDate(detail.getExpirationDate());
            inventoryHistory.setAmount(detail.getAmount());
            inventoryHistoryList.add(inventoryHistory);
        });
        inventoryHistoryService.saveBatch(inventoryHistoryList);
    }

    private void saveInventoryDetails(ReceiptOrderBo bo){

        List<InventoryDetail> inventoryDetailList = MapstructUtils.convert(bo.getDetails(), InventoryDetail.class);

        inventoryDetailList.forEach(inventoryDetail -> {
            inventoryDetail.setReceiptOrderId(bo.getId());
            inventoryDetail.setOrderNo(bo.getOrderNo());
            inventoryDetail.setType(ServiceConstants.InventoryDetailType.RECEIPT);
            inventoryDetail.setRemainQuantity(inventoryDetail.getQuantity());
        });
        inventoryDetailService.saveBatch(inventoryDetailList);
    }

    /**
     * 保存SN记录（支持SN模式的入库）
     */
    private void saveItemSns(ReceiptOrderBo bo) {
        List<ItemSnBo> itemSnBoList = new ArrayList<>();
        List<OrderSnBo> orderSnBoList = new ArrayList<>();

        for (ReceiptOrderDetailBo detail : bo.getDetails()) {
            // 如果没有启用SN模式或没有SN码，跳过
            if (Boolean.FALSE.equals(detail.getSnEnabled()) ||
                CollUtil.isEmpty(detail.getSnCodes())) {
                continue;
            }

            // 校验SKU是否启用SN管理
            ItemSkuVo itemSku = itemSkuService.queryById(detail.getSkuId());
            if (itemSku == null || itemSku.getSnEnabled() == null || itemSku.getSnEnabled() != 1) {
                throw new BaseException("SKU【" + itemSku.getSkuName() + "】未启用SN管理，无法录入SN码");
            }

            // 校验SN数量与数量是否一致
            int snCount = detail.getSnCodes().size();
            int quantity = detail.getQuantity().intValue();
            if (snCount != quantity) {
                throw new BaseException("SKU【" + itemSku.getSkuName() + "】SN数量(" + snCount +
                    ")与入库数量(" + quantity + ")不一致");
            }

            // 创建SN记录
            for (String snCode : detail.getSnCodes()) {
                ItemSnBo itemSnBo = new ItemSnBo();
                itemSnBo.setSnCode(snCode);
                itemSnBo.setSkuId(detail.getSkuId());
                itemSnBo.setItemId(itemSku.getItemId());
                itemSnBo.setWarehouseId(detail.getWarehouseId());
                itemSnBo.setAreaId(detail.getAreaId());
                itemSnBo.setStatus(0); // 在库
                itemSnBo.setBatchNo(detail.getBatchNo());
                itemSnBo.setProductionDate(detail.getProductionDate() != null ?
                    detail.getProductionDate().toLocalDate() : null);
                itemSnBo.setExpirationDate(detail.getExpirationDate() != null ?
                    detail.getExpirationDate().toLocalDate() : null);
                itemSnBo.setReceiptOrderId(bo.getId());
                // inventoryDetailId需要等saveInventoryDetails完成后才能设置，这里暂时先创建
                itemSnBoList.add(itemSnBo);
            }
        }

        // 批量插入SN记录
        if (CollUtil.isNotEmpty(itemSnBoList)) {
            itemSnService.batchInsert(itemSnBoList);

            // 批量创建单据SN关联记录
            // 这里需要重新查询插入的SN记录来获取ID
            for (ItemSnBo itemSnBo : itemSnBoList) {
                ItemSnVo itemSnVo =
                    itemSnService.queryBySnCode(itemSnBo.getSnCode());
                if (itemSnVo != null) {
                    // 找到对应的入库明细ID
                    for (ReceiptOrderDetailBo detail : bo.getDetails()) {
                        if (itemSnBo.getSkuId().equals(detail.getSkuId()) &&
                            CollUtil.isNotEmpty(detail.getSnCodes()) &&
                            detail.getSnCodes().contains(itemSnBo.getSnCode())) {
                            OrderSnBo orderSnBo = new OrderSnBo();
                            orderSnBo.setOrderType(ServiceConstants.InventoryHistoryOrderType.RECEIPT);
                            orderSnBo.setOrderId(bo.getId());
                            orderSnBo.setOrderDetailId(detail.getId());
                            orderSnBo.setSnId(itemSnVo.getId());
                            orderSnBo.setSnCode(itemSnVo.getSnCode());
                            orderSnBoList.add(orderSnBo);
                            break;
                        }
                    }
                }
            }

            // 批量插入单据SN关联记录
            if (CollUtil.isNotEmpty(orderSnBoList)) {
                orderSnService.batchInsert(orderSnBoList);
            }
        }
    }

    /**
     * 合并入库单详情 合并key：warehouseId_areaId_skuId
     * @param orderDetailBoList
     * @return
     */
    private List<InventoryBo> convertInventoryList(List<ReceiptOrderDetailBo> orderDetailBoList) {
        Function<ReceiptOrderDetailBo, String> keyFunction = it -> it.getWarehouseId() + "_" + it.getAreaId() + "_" + it.getSkuId();
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
        List<ReceiptOrderDetailBo> detailBoList = bo.getDetails();
        List<ReceiptOrderDetail> detailList = MapstructUtils.convert(detailBoList, ReceiptOrderDetail.class);
        detailList.forEach(it -> it.setReceiptOrderId(bo.getId()));
        receiptOrderDetailService.saveDetails(detailList);

        // 将生成的对象回写到BO对象中
        for (int i = 0; i < detailBoList.size() && i < detailList.size(); i++) {
            BeanUtils.copyProperties(detailList.get(i), detailBoList.get(i));
        }
    }

    /**
     * 入库单作废
     * @param id
     */
    public void editToInvalid(Long id) {
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
        receiptOrderMapper.deleteBatchIds(ids);
    }

    public void validateReceiptOrderNo(String receiptOrderNo) {
        LambdaQueryWrapper<ReceiptOrder> receiptOrderLqw = Wrappers.lambdaQuery();
        receiptOrderLqw.eq(ReceiptOrder::getReceiptOrderNo, receiptOrderNo);
        ReceiptOrder receiptOrder = receiptOrderMapper.selectOne(receiptOrderLqw);
        Assert.isNull(receiptOrder, "入库单号重复，请手动修改");
    }
}
