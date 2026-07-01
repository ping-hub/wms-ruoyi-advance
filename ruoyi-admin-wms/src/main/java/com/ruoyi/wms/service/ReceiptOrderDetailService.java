package com.ruoyi.wms.service;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.utils.MapstructUtils;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.ruoyi.wms.domain.bo.ReceiptOrderDetailBo;
import com.ruoyi.wms.domain.vo.ItemSkuVo;
import com.ruoyi.wms.mapper.ReceiptOrderDetailMapper;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.wms.domain.entity.ReceiptOrderDetail;
import com.ruoyi.wms.domain.vo.ReceiptOrderDetailVo;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 入库单详情Service业务层处理
 *
 * @author ping
 * @date 2024-07-19
 */
@RequiredArgsConstructor
@Service
public class ReceiptOrderDetailService extends ServiceImpl<ReceiptOrderDetailMapper, ReceiptOrderDetail> {

    private final ReceiptOrderDetailMapper receiptOrderDetailMapper;
    private final ItemSkuService itemSkuService;

    /**
     * 查询入库单详情
     */
    public ReceiptOrderDetailVo queryById(Long id){
        return receiptOrderDetailMapper.selectVoById(id);
    }

    /**
     * 查询入库单详情列表（跨表分页，支持入库单号/器材/时间筛选）
     */
    public TableDataInfo<ReceiptOrderDetailVo> queryPageList(ReceiptOrderDetailBo bo, PageQuery pageQuery) {
        Page<ReceiptOrderDetailVo> page = receiptOrderDetailMapper.queryDetailPage(pageQuery.build(), bo);
        // 补充 itemSku 快照字段
        if (CollUtil.isNotEmpty(page.getRecords())) {
            Set<Long> skuIds = page.getRecords().stream()
                .map(ReceiptOrderDetailVo::getSkuId).filter(Objects::nonNull).collect(Collectors.toSet());
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
     * 查询入库单详情列表
     */
    public List<ReceiptOrderDetailVo> queryList(ReceiptOrderDetailBo bo) {
        LambdaQueryWrapper<ReceiptOrderDetail> lqw = buildQueryWrapper(bo);
        return receiptOrderDetailMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<ReceiptOrderDetail> buildQueryWrapper(ReceiptOrderDetailBo bo) {
        LambdaQueryWrapper<ReceiptOrderDetail> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getReceiptOrderId() != null, ReceiptOrderDetail::getReceiptOrderId, bo.getReceiptOrderId());
        lqw.eq(bo.getSkuId() != null, ReceiptOrderDetail::getSkuId, bo.getSkuId());
        lqw.eq(bo.getQuantity() != null, ReceiptOrderDetail::getQuantity, bo.getQuantity());
        lqw.eq(bo.getWarehouseId() != null, ReceiptOrderDetail::getWarehouseId, bo.getWarehouseId());
        lqw.eq(bo.getAreaId() != null, ReceiptOrderDetail::getAreaId, bo.getAreaId());
        lqw.eq(bo.getRackId() != null, ReceiptOrderDetail::getRackId, bo.getRackId());
        lqw.eq(bo.getLocationId() != null, ReceiptOrderDetail::getLocationId, bo.getLocationId());
        return lqw;
    }

    /**
     * 新增入库单详情
     */
    public void insertByBo(ReceiptOrderDetailBo bo) {
        ReceiptOrderDetail add = MapstructUtils.convert(bo, ReceiptOrderDetail.class);
        receiptOrderDetailMapper.insert(add);
    }

    /**
     * 修改入库单详情
     */
    public void updateByBo(ReceiptOrderDetailBo bo) {
        ReceiptOrderDetail update = MapstructUtils.convert(bo, ReceiptOrderDetail.class);
        receiptOrderDetailMapper.updateById(update);
    }

    /**
     * 批量删除入库单详情
     */
    public void deleteByIds(Collection<Long> ids) {
        receiptOrderDetailMapper.deleteBatchIds(ids);
    }

    @Transactional
    public void saveDetails(List<ReceiptOrderDetail> list) {
        if (CollUtil.isEmpty(list)) {
            return;
        }
        saveOrUpdateBatch(list);
    }

    public List<ReceiptOrderDetailVo> queryByReceiptOrderId(Long receiptOrderId) {
        ReceiptOrderDetailBo bo = new ReceiptOrderDetailBo();
        bo.setReceiptOrderId(receiptOrderId);
        List<ReceiptOrderDetailVo> details = queryList(bo);
        if (CollUtil.isEmpty(details)) {
            return Collections.emptyList();
        }
        Set<Long> skuIds = details
            .stream()
            .map(ReceiptOrderDetailVo::getSkuId)
            .collect(Collectors.toSet());
        Map<Long, ItemSkuVo> itemSkuMap = itemSkuService.queryVosByIds(skuIds)
            .stream()
            .collect(Collectors.toMap(ItemSkuVo::getId, Function.identity()));
        details.forEach(detail -> {
            ItemSkuVo itemSku = itemSkuMap.get(detail.getSkuId());
            detail.setItemSku(itemSku);
            fillSnapshotFields(detail, itemSku);
        });
        return details;
    }

    public List<ReceiptOrderDetail> queryEntitiesByReceiptOrderId(Long receiptOrderId) {
        LambdaQueryWrapper<ReceiptOrderDetail> lqw = Wrappers.lambdaQuery();
        lqw.eq(ReceiptOrderDetail::getReceiptOrderId, receiptOrderId);
        lqw.orderByAsc(ReceiptOrderDetail::getId);
        return receiptOrderDetailMapper.selectList(lqw);
    }

    private void fillSnapshotFields(ReceiptOrderDetailVo detail, ItemSkuVo itemSku) {
        if (detail == null || itemSku == null) {
            return;
        }
        if (StringUtils.isBlank(detail.getSkuName())) {
            detail.setSkuName(itemSku.getSkuName());
        }
        if (StringUtils.isBlank(detail.getProductIdentifier())) {
            detail.setProductIdentifier(itemSku.getProductIdentifier());
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
