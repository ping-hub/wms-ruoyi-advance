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
import com.ruoyi.wms.domain.entity.Warehouse;
import com.ruoyi.wms.domain.vo.BatchPrintQrCodeDetailVo;
import com.ruoyi.wms.domain.vo.BatchPrintQrCodeResultVo;
import com.ruoyi.wms.domain.vo.ItemCategoryVo;
import com.ruoyi.wms.domain.vo.ItemSkuVo;
import com.ruoyi.wms.domain.vo.ItemVo;
import com.ruoyi.wms.mapper.ItemCategoryMapper;
import com.ruoyi.wms.mapper.ItemMapper;
import com.ruoyi.wms.mapper.WarehouseMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Log4j2
public class ItemService {

    private final ItemMapper itemMapper;
    private final ItemSkuService itemSkuService;
    private final ItemCategoryMapper itemCategoryMapper;
    private final ItemInstanceService itemInstanceService;
    private final CodeRuleService codeRuleService;
    private final WarehouseMapper warehouseMapper;

    /**
     * 查询物料
     */

    public ItemVo queryById(Long id) {
        ItemVo item = itemMapper.selectVoById(id);
        if (item != null) {
            enrichItemVos(List.of(item));
        }
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
        List<ItemVo> itemVos = itemMapper.selectVoList(lambdaQueryWrapper);
        enrichItemVos(itemVos);
        return itemVos;
    }

    /**
     * 查询物料列表
     */

    public TableDataInfo<ItemVo> queryPageList(ItemBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<Item> lqw = buildQueryWrapper(bo);
        Page<ItemVo> result = itemMapper.selectVoPage(pageQuery.build(), lqw);
        enrichItemVos(result.getRecords());
        return TableDataInfo.build(result);
    }

    /**
     * 查询物料列表
     */

    public List<ItemVo> queryList(ItemBo bo) {
        LambdaQueryWrapper<Item> lqw = buildQueryWrapper(bo);
        List<ItemVo> itemVos = itemMapper.selectVoList(lqw);
        enrichItemVos(itemVos);
        return itemVos;
    }

    @Transactional
    public BatchPrintQrCodeResultVo batchPrintQrCode(BatchPrintQrCodeBo bo) {
        Assert.notNull(bo, "打印参数不能为空");
        Assert.notNull(bo.getRow(), "器材信息不能为空");
        Assert.notNull(bo.getRow().getId(), "器材ID不能为空");
        Assert.notNull(bo.getSkuId(), "打印规格不能为空");
        Assert.notNull(bo.getQrCodeCount(), "二维码个数不能为空");
        Assert.isTrue(bo.getQrCodeCount() > 0, "二维码个数必须大于0");
        Assert.isTrue(StrUtil.isNotBlank(bo.getWarehouseCode()), "仓库编码不能为空");

        Item item = itemMapper.selectById(bo.getRow().getId());
        Assert.notNull(item, "器材不存在");

        // 根据仓库编码查找仓库，获取 warehouseId
        Warehouse warehouse = warehouseMapper.selectOne(
            Wrappers.<Warehouse>lambdaQuery().eq(Warehouse::getWarehouseCode, bo.getWarehouseCode()));
        Assert.notNull(warehouse, "仓库不存在，编码：" + bo.getWarehouseCode());

        ItemBo row = bo.getRow();
        ItemSkuVo sku = resolvePrintSku(row.getId(), bo.getSkuId());

        int count = bo.getQrCodeCount();
        List<ItemInstance> itemInstances = new ArrayList<>(count);
        List<BatchPrintQrCodeDetailVo> printPayloads = new ArrayList<>(count);

        // 使用四段式编码：prefix-一级分类编码-二级分类编码-seq，按(一级分类+二级分类)独立编号
        Assert.notBlank(item.getItemCategory(), "器材未绑定分类，无法生成识别码");
        String[] categoryCodes = resolveCategoryCodes(Long.valueOf(item.getItemCategory()));
        List<String> codes = codeRuleService.generateBatchItemInstanceCodes(
            count, categoryCodes[0], categoryCodes[1]);
        boolean useFallback = CollUtil.isEmpty(codes);

        for (int i = 0; i < count; i++) {
            // 优先使用批量编码结果，降级用雪花ID
            String instanceCode = useFallback
                ? "II" + cn.hutool.core.util.IdUtil.getSnowflakeNextIdStr()
                : codes.get(i);

            ItemInstance itemInstance = new ItemInstance();
            itemInstance.setInstanceCode(instanceCode);
            itemInstance.setItemId(row.getId());
            itemInstance.setSkuId(sku.getId());
            itemInstance.setInstanceStatus(ServiceConstants.ItemInstanceStatus.PENDING_RECEIPT);
            itemInstance.setRemark(StrUtil.blankToDefault(row.getRemark(), item.getRemark()));
            // 绑定仓库：打印时即确定器材所属仓库
            itemInstance.setWarehouseId(warehouse.getId());
            // 从器材表回填分类ID
            itemInstance.setItemCategory(item.getItemCategory());
            itemInstances.add(itemInstance);

            BatchPrintQrCodeDetailVo payload = new BatchPrintQrCodeDetailVo();
            payload.setInstanceCode(instanceCode);
            payload.setQrCodeValue(instanceCode);
            payload.setQrContent(instanceCode);
            printPayloads.add(payload);
        }

        if (CollUtil.isNotEmpty(itemInstances)) {
            itemInstanceService.saveBatch(itemInstances);
        }

        BatchPrintQrCodeResultVo result = new BatchPrintQrCodeResultVo();
        result.setQrCodeCount(printPayloads.size());
        result.setRow(row);
        result.setDetails(printPayloads);
        return result;
    }

    /**
     * 从分类ID向上追溯，获取一级、二级分类编码。
     * 器材只挂在二级及以下分类，此方法保证返回有效的两级编码。
     */
    private String[] resolveCategoryCodes(Long categoryId) {
        ItemCategory current = itemCategoryMapper.selectById(categoryId);
        Assert.notNull(current, "器材分类不存在，ID：" + categoryId);

        // 从当前节点向上追溯到一级和二级
        ItemCategory level2 = current;
        ItemCategory level1 = null;

        if (current.getParentId() != null && current.getParentId() != 0L) {
            ItemCategory parent = itemCategoryMapper.selectById(current.getParentId());
            if (parent != null) {
                level1 = parent;
                // 继续向上直到找到一级（parentId为0或null）
                while (level1.getParentId() != null && level1.getParentId() != 0L) {
                    level2 = level1;
                    level1 = itemCategoryMapper.selectById(level1.getParentId());
                }
            }
        }

        if (level1 == null) {
            // 兜底：当前节点本身就是一级（器材不应挂在一级）
            return new String[]{current.getCategoryCode(), current.getCategoryCode()};
        }
        return new String[]{level1.getCategoryCode(), level2.getCategoryCode()};
    }

    private LambdaQueryWrapper<Item> buildQueryWrapper(ItemBo bo) {
        LambdaQueryWrapper<Item> lqw = Wrappers.lambdaQuery();
        lqw.eq(StrUtil.isNotBlank(bo.getItemCode()), Item::getItemCode, bo.getItemCode());
        // 主键集合
        lqw.in(!CollUtil.isEmpty(bo.getIds()), Item::getId, bo.getIds());
        lqw.like(StrUtil.isNotBlank(bo.getItemName()), Item::getItemName, bo.getItemName());
        if (!StrUtil.isBlank(bo.getItemCategory())) {
            Long parentId = Long.valueOf(bo.getItemCategory());
            List<Long> subIdList = this.buildSubItemCategoryIdList(parentId);
            subIdList.add(Long.valueOf(bo.getItemCategory()));
            lqw.in(Item::getItemCategory, subIdList);
        }
        lqw.eq(StrUtil.isNotBlank(bo.getUnit()), Item::getUnit, bo.getUnit());
        lqw.like(StrUtil.isNotBlank(bo.getEquipmentName()), Item::getEquipmentName, bo.getEquipmentName());
        lqw.eq(StrUtil.isNotBlank(bo.getStatus()), Item::getStatus, bo.getStatus());
        return lqw;
    }

    private List<Long> buildSubItemCategoryIdList(Long parentId) {
        List<Long> result = new ArrayList<>();
        LambdaQueryWrapper<ItemCategory> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ItemCategory::getParentId, parentId);
        List<ItemCategory> children = itemCategoryMapper.selectList(wrapper);
        for (ItemCategory child : children) {
            result.add(child.getId());
            result.addAll(buildSubItemCategoryIdList(child.getId()));
        }
        return result;
    }

    private void enrichItemVos(List<ItemVo> itemVos) {
        if (CollUtil.isEmpty(itemVos)) {
            return;
        }
        List<ItemVo> validItemVos = itemVos.stream().filter(java.util.Objects::nonNull).toList();
        if (CollUtil.isEmpty(validItemVos)) {
            return;
        }
        Set<Long> itemIds = validItemVos.stream()
            .map(ItemVo::getId)
            .filter(java.util.Objects::nonNull)
            .collect(Collectors.toSet());
        Map<Long, List<ItemSkuVo>> skuMap = itemSkuService.queryVoListByItemIds(itemIds).stream()
            .collect(Collectors.groupingBy(ItemSkuVo::getItemId));

        Set<Long> categoryIds = validItemVos.stream()
            .map(ItemVo::getItemCategory)
            .filter(StrUtil::isNotBlank)
            .map(Long::valueOf)
            .collect(Collectors.toSet());
        Map<Long, ItemCategoryVo> itemCategoryVoMap = categoryIds.isEmpty()
            ? java.util.Collections.emptyMap()
            : itemCategoryMapper.selectVoList(new LambdaQueryWrapper<ItemCategory>().in(ItemCategory::getId, categoryIds))
            .stream()
            .collect(Collectors.toMap(ItemCategoryVo::getId, Function.identity()));

        validItemVos.forEach(itemVo -> {
            itemVo.setSku(skuMap.getOrDefault(itemVo.getId(), java.util.Collections.emptyList()));
            if (StrUtil.isNotBlank(itemVo.getItemCategory())) {
                itemVo.setItemCategoryInfo(itemCategoryVoMap.get(Long.valueOf(itemVo.getItemCategory())));
            }
        });
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
        itemSkuService.setItemId(bo.getSku(), item.getId());
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
        itemSkuService.setItemId(bo.getSku(), bo.getId());
        deleteRemovedSku(bo);
        itemSkuService.saveOrUpdateBatchByBo(bo.getSku());
    }

    private void deleteRemovedSku(ItemBo bo) {
        List<Long> existingSkuIds = itemSkuService.queryByItemIds(List.of(bo.getId())).stream()
            .map(ItemSku::getId)
            .toList();
        if (CollUtil.isEmpty(existingSkuIds)) {
            return;
        }
        Set<Long> incomingSkuIds = bo.getSku().stream()
            .map(ItemSkuBo::getId)
            .filter(java.util.Objects::nonNull)
            .collect(Collectors.toSet());
        List<Long> removedSkuIds = existingSkuIds.stream()
            .filter(id -> !incomingSkuIds.contains(id))
            .toList();
        if (CollUtil.isNotEmpty(removedSkuIds)) {
            itemSkuService.deleteByIds(removedSkuIds);
        }
    }

    /**
     * 保存前的数据校验
     */
    private void validateBoBeforeSave(ItemBo itemBo) {
        normalizeBoBeforeSave(itemBo);
        validateItemName(itemBo);
        validateItemSkuName(itemBo.getSku());
    }

    private void normalizeBoBeforeSave(ItemBo itemBo) {
        itemBo.setItemCode(StrUtil.trim(itemBo.getItemCode()));
        itemBo.setItemName(StrUtil.trim(itemBo.getItemName()));
        itemBo.setItemCategory(StrUtil.trim(itemBo.getItemCategory()));
        if (CollUtil.isNotEmpty(itemBo.getSku())) {
            itemBo.getSku().forEach(sku -> {
                sku.setSkuName(StrUtil.trim(sku.getSkuName()));
                sku.setProductIdentifier(StrUtil.trim(sku.getProductIdentifier()));
            });
        }
    }

    private void validateItemName(ItemBo item) {
        LambdaQueryWrapper<Item> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Item::getItemName, item.getItemName());
        queryWrapper.ne(item.getId() != null, Item::getId, item.getId());
        Assert.isTrue(itemMapper.selectCount(queryWrapper) == 0, "器材名称重复");
    }

    private void validateItemSkuName(List<ItemSkuBo> skuVoList) {
        Assert.isTrue(CollUtil.isNotEmpty(skuVoList), "至少维护一个器材规格");
        Assert.isTrue(
            skuVoList.stream().allMatch(sku -> StrUtil.isNotBlank(sku.getSkuName())),
            "器材规格名称不能为空"
        );
        Assert.isTrue(
            skuVoList.stream().map(ItemSkuBo::getSkuName).distinct().count() == skuVoList.size(),
            "器材规格重复"
        );
    }

    private ItemSkuVo resolvePrintSku(Long itemId, Long skuId) {
        ItemSkuVo sku = itemSkuService.queryById(skuId);
        Assert.notNull(sku, "打印规格不存在");
        Assert.isTrue(itemId.equals(sku.getItemId()), "打印规格与器材不匹配");
        Assert.isTrue(StrUtil.isBlank(sku.getStatus()) || "1".equals(sku.getStatus()), "停用规格不能打印二维码");
        return sku;
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
