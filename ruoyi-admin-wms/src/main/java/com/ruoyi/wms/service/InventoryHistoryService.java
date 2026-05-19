package com.ruoyi.wms.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.utils.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.utils.MapstructUtils;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.ruoyi.wms.domain.bo.InventoryHistoryBo;
import com.ruoyi.wms.domain.entity.InventoryHistory;
import com.ruoyi.wms.domain.vo.ItemSkuVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.ruoyi.wms.domain.vo.InventoryHistoryVo;
import com.ruoyi.wms.mapper.InventoryHistoryMapper;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 库存记录Service业务层处理
 *
 * @author zcc
 * @date 2024-07-22
 */
@RequiredArgsConstructor
@Service
public class InventoryHistoryService extends ServiceImpl<InventoryHistoryMapper, InventoryHistory> {

    private final InventoryHistoryMapper inventoryHistoryMapper;
    private final ItemSkuService itemSkuService;

    /**
     * 查询库存记录
     */
    public InventoryHistoryVo queryById(Long id){
        return inventoryHistoryMapper.selectVoById(id);
    }

    /**
     * 查询库存记录列表
     */
    public TableDataInfo<InventoryHistoryVo> queryPageList(InventoryHistoryBo bo, PageQuery pageQuery) {
        Page<InventoryHistoryVo> result = inventoryHistoryMapper.selectVoPageByBo(pageQuery.build(), bo);
        enrich(result.getRecords());
        return TableDataInfo.build(result);
    }

    /**
     * 查询库存记录列表
     */
    public List<InventoryHistoryVo> queryList(InventoryHistoryBo bo) {
        List<InventoryHistoryVo> list = inventoryHistoryMapper.selectVoListByBo(bo);
        enrich(list);
        return list;
    }

    public List<InventoryHistoryVo> queryByItemInstanceId(Long itemInstanceId) {
        InventoryHistoryBo bo = new InventoryHistoryBo();
        bo.setItemInstanceId(itemInstanceId);
        return queryList(bo);
    }

    public List<InventoryHistoryVo> queryByBoxId(Long boxId) {
        InventoryHistoryBo bo = new InventoryHistoryBo();
        bo.setBoxId(boxId);
        return queryList(bo);
    }

    private void enrich(List<InventoryHistoryVo> vos) {
        if (vos == null || vos.isEmpty()) {
            return;
        }
        Set<Long> skuIds = vos.stream().map(InventoryHistoryVo::getSkuId).filter(Objects::nonNull).collect(Collectors.toSet());
        Map<Long, ItemSkuVo> itemSkuMap = itemSkuService.queryVosByIds(skuIds).stream().collect(Collectors.toMap(ItemSkuVo::getId, Function.identity()));
        vos.forEach(it -> {
            ItemSkuVo itemSku = itemSkuMap.get(it.getSkuId());
            it.setItemSku(itemSku);
            if (itemSku != null) {
                it.setItem(itemSku.getItem());
                if (StringUtils.isBlank(it.getItemName()) && itemSku.getItem() != null) {
                    it.setItemName(itemSku.getItem().getItemName());
                }
                if (StringUtils.isBlank(it.getUnit()) && itemSku.getItem() != null) {
                    it.setUnit(itemSku.getItem().getUnit());
                }
            }
        });
    }

    /**
     * 新增库存记录
     */
    public void insertByBo(InventoryHistoryBo bo) {
        InventoryHistory add = MapstructUtils.convert(bo, InventoryHistory.class);
        inventoryHistoryMapper.insert(add);
    }

    /**
     * 修改库存记录
     */
    public void updateByBo(InventoryHistoryBo bo) {
        InventoryHistory update = MapstructUtils.convert(bo, InventoryHistory.class);
        inventoryHistoryMapper.updateById(update);
    }

    /**
     * 批量删除库存记录
     */
    public void deleteByIds(Collection<Long> ids) {
        inventoryHistoryMapper.deleteBatchIds(ids);
    }
}
