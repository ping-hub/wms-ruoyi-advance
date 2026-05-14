package com.ruoyi.wms.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.constant.ServiceConstants;
import com.ruoyi.common.core.utils.MapstructUtils;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.ruoyi.wms.domain.bo.BatchPrintQrCodeBo;
import com.ruoyi.wms.domain.bo.ItemBo;
import com.ruoyi.wms.domain.bo.ItemSkuBo;
import com.ruoyi.wms.domain.entity.Item;
import com.ruoyi.wms.domain.entity.ItemCategory;
import com.ruoyi.wms.domain.entity.ItemInstance;
import com.ruoyi.wms.domain.entity.ItemSku;
import com.ruoyi.wms.domain.vo.BatchPrintQrCodeDetailVo;
import com.ruoyi.wms.domain.vo.BatchPrintQrCodeResultVo;
import com.ruoyi.wms.domain.vo.ItemCategoryVo;
import com.ruoyi.wms.domain.vo.ItemSkuVo;
import com.ruoyi.wms.domain.vo.ItemVo;
import com.ruoyi.wms.mapper.ItemCategoryMapper;
import com.ruoyi.wms.mapper.ItemMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Log4j2
public class ItemService {

    private static final String BATCH_PRINT_ITEM_KEY = "ITEM";

    private final ItemMapper itemMapper;
    private final ItemSkuService itemSkuService;
    private final ItemCategoryMapper itemCategoryMapper;
    private final InventoryService inventoryService;
    private final ItemInstanceService itemInstanceService;
    private final ItemQrCodeSerialService itemQrCodeSerialService;

    /**
     * 查询物料
     */

    public ItemVo queryById(Long id) {
        ItemVo item = itemMapper.selectVoById(id);
        item.setSku(itemSkuService.queryListByItemId(id));
        return item;
    }

    /**
     * 查询物料
     *
     * @param itemIds ids
     */

    public List<ItemVo> queryById(List<Long> itemIds) {
        if (CollUtil.isEmpty(itemIds)) {
            return CollUtil.newArrayList();
        }
        LambdaQueryWrapper<Item> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.in(Item::getId, itemIds);
        return itemMapper.selectVoList(lambdaQueryWrapper);
    }

    /**
     * 查询物料列表
     */

    public TableDataInfo<ItemVo> queryPageList(ItemBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<Item> lqw = buildQueryWrapper(bo);
        Page<ItemVo> result = itemMapper.selectVoPage(pageQuery.build(), lqw);
        List<ItemVo> itemVoList = result.getRecords();
        if (!CollUtil.isEmpty(itemVoList)) {
            LambdaQueryWrapper<ItemCategory> itemTypeWrapper = new LambdaQueryWrapper<>();
            itemTypeWrapper.in(ItemCategory::getId, itemVoList.stream().map(ItemVo::getItemCategory).collect(Collectors.toSet()));
            Map<Long, ItemCategoryVo> itemCategoryVoMap = itemCategoryMapper.selectVoList(itemTypeWrapper).stream().collect(Collectors.toMap(ItemCategoryVo::getId, Function.identity()));
            itemVoList.forEach(itemVo -> {
                itemVo.setItemCategoryInfo(itemCategoryVoMap.get(Long.valueOf(itemVo.getItemCategory())));
            });
        }
        return TableDataInfo.build(result);
    }

    /**
     * 查询物料列表
     */

    public List<ItemVo> queryList(ItemBo bo) {
        LambdaQueryWrapper<Item> lqw = buildQueryWrapper(bo);
        return itemMapper.selectVoList(lqw);
    }

    @Transactional
    public BatchPrintQrCodeResultVo batchPrintQrCode(BatchPrintQrCodeBo bo) {
        Assert.notNull(bo, "打印参数不能为空");
        Assert.notNull(bo.getRow(), "器材信息不能为空");
        Assert.notNull(bo.getRow().getId(), "器材ID不能为空");
        Assert.notNull(bo.getQrCodeCount(), "二维码个数不能为空");
        Assert.isTrue(bo.getQrCodeCount() > 0, "二维码个数必须大于0");

        Item item = itemMapper.selectById(bo.getRow().getId());
        Assert.notNull(item, "器材不存在");

        ItemBo row = bo.getRow();
        ItemSkuVo sku = resolvePrintSku(row);
        String itemKey = BATCH_PRINT_ITEM_KEY;

        List<Long> serialValues = itemQrCodeSerialService.allocateSerialValues(itemKey, StrUtil.EMPTY, bo.getQrCodeCount());
        LocalDateTime now = LocalDateTime.now();
        List<ItemInstance> itemInstances = new ArrayList<>(serialValues.size());
        List<BatchPrintQrCodeDetailVo> printPayloads = new ArrayList<>(serialValues.size());

        for (Long serialValue : serialValues) {
            String instanceCode = itemKey + serialValue;
            String qrCodeValue = instanceCode;
            String qrContent = buildQrCodeContent(qrCodeValue);

            ItemInstance itemInstance = new ItemInstance();
            itemInstance.setInstanceCode(instanceCode);
            itemInstance.setItemId(row.getId());
            itemInstance.setSkuId(sku.getId());
            itemInstance.setInstanceStatus(ServiceConstants.ItemInstanceStatus.IN_STOCK);
            itemInstance.setInBox(0);
            itemInstance.setBorrowed(0);
            itemInstance.setSourceType(ServiceConstants.ItemInstanceSourceType.MANUAL);
            itemInstance.setSourceOrderType(ServiceConstants.ItemInstanceSourceType.MANUAL);
            itemInstance.setSourceOrderNo("BATCH_PRINT");
            itemInstance.setProductMark(StrUtil.blankToDefault(row.getProductMark(), item.getProductMark()));
            itemInstance.setRemark(StrUtil.blankToDefault(row.getRemark(), item.getRemark()));
            itemInstance.setLastOperationType("batch_print");
            itemInstance.setLastOperationTime(now);
            itemInstances.add(itemInstance);

            BatchPrintQrCodeDetailVo payload = new BatchPrintQrCodeDetailVo();
            payload.setInstanceCode(instanceCode);
            payload.setSerialValue(serialValue);
            payload.setQrCodeValue(qrCodeValue);
            payload.setQrContent(qrContent);
            printPayloads.add(payload);
        }

        if (CollUtil.isNotEmpty(itemInstances)) {
            itemInstanceService.saveBatch(itemInstances);
        }

        // TODO 调用实际打印接口，使用 printPayloads 执行批量二维码打印。
        log.info("Prepared {} qr-code print payloads for itemId={}", printPayloads.size(), row.getId());
        BatchPrintQrCodeResultVo result = new BatchPrintQrCodeResultVo();
        result.setItemKey(itemKey);
        result.setQrCodeCount(printPayloads.size());
        result.setRow(row);
        result.setDetails(printPayloads);
        return result;
    }

    private LambdaQueryWrapper<Item> buildQueryWrapper(ItemBo bo) {
        LambdaQueryWrapper<Item> lqw = Wrappers.lambdaQuery();
        lqw.eq(StrUtil.isNotBlank(bo.getItemCode()), Item::getItemCode, bo.getItemCode());
        // 主键集合
        lqw.in(!CollUtil.isEmpty(bo.getIds()), Item::getId, bo.getIds());
        lqw.like(StrUtil.isNotBlank(bo.getItemName()), Item::getItemName, bo.getItemName());
        if (!StrUtil.isBlank(bo.getItemCategory())){
            Long parentId = Long.valueOf(bo.getItemCategory());
            List<Long> subIdList = this.buildSubItemCategoryIdList(parentId);
            subIdList.add(Long.valueOf(bo.getItemCategory()));
            lqw.in(Item::getItemCategory, subIdList);
        }
        lqw.eq(StrUtil.isNotBlank(bo.getUnit()), Item::getUnit, bo.getUnit());
        lqw.eq(StrUtil.isNotBlank(bo.getSpecLevel()), Item::getSpecLevel, bo.getSpecLevel());
        lqw.like(StrUtil.isNotBlank(bo.getEquipmentName()), Item::getEquipmentName, bo.getEquipmentName());
        lqw.eq(StrUtil.isNotBlank(bo.getEquipmentType()), Item::getEquipmentType, bo.getEquipmentType());
        lqw.eq(StrUtil.isNotBlank(bo.getStatus()), Item::getStatus, bo.getStatus());
        lqw.eq(StrUtil.isNotBlank(bo.getProductMark()), Item::getProductMark, bo.getProductMark());
        lqw.like(StrUtil.isNotBlank(bo.getModelText()), Item::getModelText, bo.getModelText());
        return lqw;
    }

    private List<Long> buildSubItemCategoryIdList(Long parentId) {
        LambdaQueryWrapper<ItemCategory> itemTypeWrapper = new LambdaQueryWrapper<>();
        itemTypeWrapper.eq(ItemCategory::getParentId, parentId);
        return itemCategoryMapper.selectList(itemTypeWrapper).stream().map(ItemCategory::getId).collect(Collectors.toList());
    }

    /**
     * 新增物料
     *
     * @param bo
     */
    @Transactional
    public void insertByForm(ItemBo bo) {
        validateBoBeforeSave(bo);
        Item item = MapstructUtils.convert(bo, Item.class);
        itemMapper.insert(item);
        itemSkuService.setItemId(bo.getSku(),item.getId());
        itemSkuService.saveOrUpdateBatchByBo(bo.getSku());
    }

    /**
     * 修改物料
     *
     * @param bo
     */
    @Transactional
    public void updateByForm(ItemBo bo) {
        validateBoBeforeSave(bo);
        itemMapper.updateById(MapstructUtils.convert(bo, Item.class));
        itemSkuService.setItemId(bo.getSku(),bo.getId());
        itemSkuService.saveOrUpdateBatchByBo(bo.getSku());
    }

    /**
     * 保存前的数据校验
     */
    private void validateBoBeforeSave(ItemBo itemBo) {
        validateItemName(itemBo);
        validateItemCode(itemBo);
        validateItemSkuName(itemBo.getSku());
    }

    private void validateItemName(ItemBo item) {
        LambdaQueryWrapper<Item> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Item::getItemName, item.getItemName());
        queryWrapper.ne(item.getId() != null, Item::getId, item.getId());
        Assert.isTrue(itemMapper.selectCount(queryWrapper) == 0, "商品名称重复");
    }

    private void validateItemCode(ItemBo item) {
        if (StrUtil.isBlank(item.getItemCode())) {
            return;
        }
        LambdaQueryWrapper<Item> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Item::getItemCode, item.getItemCode());
        queryWrapper.ne(item.getId() != null, Item::getId, item.getId());
        Assert.isTrue(itemMapper.selectCount(queryWrapper) == 0, "商品编码重复");
    }

    private void validateItemSkuName(List<ItemSkuBo> skuVoList) {
         Assert.isTrue(
             skuVoList.stream().map(ItemSkuBo::getSkuName).distinct().count() == skuVoList.size(),
             "商品规格重复"
         );
    }

    private ItemSkuVo resolvePrintSku(ItemBo row) {
        List<ItemSkuVo> skuList = itemSkuService.queryListByItemId(row.getId());
        Assert.isTrue(CollUtil.isNotEmpty(skuList), "当前器材未维护规格，无法批量打印二维码");

        List<ItemSkuVo> activeSkuList = skuList.stream()
            .filter(sku -> StrUtil.isBlank(sku.getStatus()) || "1".equals(sku.getStatus()))
            .toList();
        List<ItemSkuVo> candidateList = CollUtil.isNotEmpty(activeSkuList) ? activeSkuList : skuList;
        if (candidateList.size() == 1) {
            return candidateList.get(0);
        }

        if (StrUtil.isNotBlank(row.getModelText())) {
            List<ItemSkuVo> matched = candidateList.stream()
                .filter(sku -> StrUtil.equals(row.getModelText(), sku.getSpecModel())
                    || StrUtil.equals(row.getModelText(), sku.getSkuName()))
                .toList();
            if (matched.size() == 1) {
                return matched.get(0);
            }
        }

        throw new IllegalArgumentException("当前器材存在多个规格，无法自动识别打印规格，请补充明确规格信息");
    }

    private String buildQrCodeContent(String qrCodeValue) {
        return qrCodeValue;
    }



    /**
     * 批量删除物料
     */
    @Transactional
    public void deleteById(Long id) {
        List<Long> skuIds = itemSkuService.queryByItemIds(List.of(id)).stream().map(ItemSku::getId).toList();
        itemMapper.deleteById(id);
        itemSkuService.deleteByIds(skuIds);
    }

}
