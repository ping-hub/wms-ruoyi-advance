package com.ruoyi.wms.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.MapstructUtils;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.ruoyi.wms.domain.bo.ItemSnBo;
import com.ruoyi.wms.domain.entity.ItemSn;
import com.ruoyi.wms.domain.entity.OrderSn;
import com.ruoyi.wms.domain.vo.ItemSnVo;
import com.ruoyi.wms.domain.vo.OrderSnVo;
import com.ruoyi.wms.domain.vo.ItemSkuVo;
import com.ruoyi.wms.domain.vo.WarehouseVo;
import com.ruoyi.wms.domain.vo.AreaVo;
import com.ruoyi.wms.mapper.ItemSnMapper;
import com.ruoyi.wms.mapper.OrderSnMapper;
import com.ruoyi.wms.mapper.ItemSkuMapper;
import com.ruoyi.wms.service.ItemSkuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 商品序列号Service
 *
 * @author ruoyi
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class ItemSnService {

    private final ItemSnMapper itemSnMapper;
    private final OrderSnMapper orderSnMapper;
    private final ItemSkuService itemSkuService;
    private final ItemSkuMapper itemSkuMapper;

    /**
     * 查询商品序列号
     */
    public ItemSnVo queryById(Long id) {
        return itemSnMapper.selectVoById(id);
    }

    /**
     * 分页查询商品序列号
     */
    public TableDataInfo<ItemSnVo> queryPageList(ItemSnBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ItemSn> lqw = buildQueryWrapper(bo);
        Page<ItemSnVo> result = itemSnMapper.selectVoPage(pageQuery.build(), lqw);

        // 填充关联信息
        fillRelationInfo(result.getRecords());

        return TableDataInfo.build(result);
    }

    /**
     * 查询商品序列号列表
     */
    public List<ItemSnVo> queryList(ItemSnBo bo) {
        LambdaQueryWrapper<ItemSn> lqw = buildQueryWrapper(bo);
        List<ItemSnVo> result = itemSnMapper.selectVoList(lqw);

        // 填充关联信息
        fillRelationInfo(result);

        return result;
    }

    /**
     * 构建查询条件
     */
    private LambdaQueryWrapper<ItemSn> buildQueryWrapper(ItemSnBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<ItemSn> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getQuerySkuId() != null, ItemSn::getSkuId, bo.getQuerySkuId());
        lqw.eq(bo.getQueryItemId() != null, ItemSn::getItemId, bo.getQueryItemId());
        lqw.eq(bo.getQueryWarehouseId() != null, ItemSn::getWarehouseId, bo.getQueryWarehouseId());
        lqw.eq(bo.getQueryAreaId() != null, ItemSn::getAreaId, bo.getQueryAreaId());
        lqw.eq(bo.getQueryStatus() != null, ItemSn::getStatus, bo.getQueryStatus());
        lqw.like(StrUtil.isNotBlank(bo.getQuerySnCode()), ItemSn::getSnCode, bo.getQuerySnCode());
        lqw.like(StrUtil.isNotBlank(bo.getQueryBatchNo()), ItemSn::getBatchNo, bo.getQueryBatchNo());
        lqw.eq(bo.getQueryReceiptOrderId() != null, ItemSn::getReceiptOrderId, bo.getQueryReceiptOrderId());
        lqw.orderByAsc(ItemSn::getSnCode);
        return lqw;
    }

    /**
     * 填充关联信息
     */
    private void fillRelationInfo(List<ItemSnVo> list) {
        if (CollUtil.isEmpty(list)) {
            return;
        }

        // 填充SKU信息
        List<Long> skuIds = list.stream()
            .map(ItemSnVo::getSkuId)
            .distinct()
            .collect(Collectors.toList());
        Map<Long, ItemSkuVo> skuMap = itemSkuService.queryVosByIds(skuIds).stream()
            .collect(Collectors.toMap(ItemSkuVo::getId, Function.identity()));
        list.forEach(vo -> {
            ItemSkuVo skuVo = skuMap.get(vo.getSkuId());
            if (skuVo != null) {
                vo.setSkuName(skuVo.getSkuName());
            }
        });

        // 填充状态名称
        list.forEach(vo -> {
            vo.setStatusName(getStatusName(vo.getStatus()));
        });
    }

    /**
     * 获取状态名称
     */
    public String getStatusName(Integer status) {
        if (status == null) {
            return "";
        }
        switch (status) {
            case 0:
                return "在库";
            case 1:
                return "已出库";
            case 2:
                return "损坏";
            case 3:
                return "冻结";
            default:
                return "未知";
        }
    }

    /**
     * 新增商品序列号
     */
    public Boolean insertByBo(ItemSnBo bo) {
        // 校验SN码唯一性
        validateSnCodeUnique(null, bo.getSnCode());

        ItemSn add = MapstructUtils.convert(bo, ItemSn.class);
        validEntityBeforeSave(add);
        boolean flag = itemSnMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改商品序列号
     */
    public Boolean updateByBo(ItemSnBo bo) {
        // 校验SN码唯一性
        validateSnCodeUnique(bo.getId(), bo.getSnCode());

        ItemSn update = MapstructUtils.convert(bo, ItemSn.class);
        validEntityBeforeSave(update);
        return itemSnMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(ItemSn entity) {
        // 校验SKU是否存在
        if (itemSkuService == null) {
            throw new ServiceException("SKU服务未初始化");
        }
        com.ruoyi.wms.domain.vo.ItemSkuVo sku = itemSkuService.queryById(entity.getSkuId());
        if (sku == null) {
            throw new ServiceException("SKU不存在: " + entity.getSkuId());
        }

        // 校验仓库是否存在
        if (entity.getWarehouseId() != null) {
            com.ruoyi.wms.domain.vo.WarehouseVo warehouse = itemSkuService.queryWarehouseById(entity.getWarehouseId());
            if (warehouse == null) {
                throw new ServiceException("仓库不存在: " + entity.getWarehouseId());
            }

            // 校验库区是否存在
            if (entity.getAreaId() != null) {
                com.ruoyi.wms.domain.vo.AreaVo area = itemSkuService.queryAreaById(entity.getAreaId());
                if (area == null) {
                    throw new ServiceException("库区不存在: " + entity.getAreaId());
                }
                // 校验库区是否属于该仓库
                if (!area.getWarehouseId().equals(entity.getWarehouseId())) {
                    throw new ServiceException("库区不属于指定仓库");
                }
            }
        }
    }

    /**
     * 校验SN码唯一性
     */
    private void validateSnCodeUnique(Long id, String snCode) {
        Assert.notNull(snCode, "SN码不能为空");

        LambdaQueryWrapper<ItemSn> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ItemSn::getSnCode, snCode);
        if (id != null) {
            wrapper.ne(ItemSn::getId, id);
        }

        ItemSn exist = itemSnMapper.selectOne(wrapper);
        if (exist != null) {
            throw new ServiceException("SN码已存在: " + snCode);
        }
    }

    /**
     * 批量校验SN码唯一性
     */
    public void validateSnCodesUnique(List<String> snCodes) {
        if (CollUtil.isEmpty(snCodes)) {
            return;
        }

        List<ItemSn> existList = itemSnMapper.selectListBySnCodes(snCodes);
        if (CollUtil.isNotEmpty(existList)) {
            String existSnCodes = existList.stream()
                .map(ItemSn::getSnCode)
                .distinct()
                .collect(Collectors.joining(", "));
            throw new ServiceException("以下SN码已存在: " + existSnCodes);
        }
    }

    /**
     * 批量创建SN记录（入库时使用）
     */
    @Transactional(rollbackFor = Exception.class)
    public void batchInsert(List<ItemSnBo> snList) {
        if (CollUtil.isEmpty(snList)) {
            return;
        }

        // 批量校验SN码唯一性
        List<String> snCodes = snList.stream()
            .map(ItemSnBo::getSnCode)
            .collect(Collectors.toList());
        validateSnCodesUnique(snCodes);

        // 批量插入
        List<ItemSn> entities = MapstructUtils.convert(snList, ItemSn.class);
        entities.forEach(this::validEntityBeforeSave);
        entities.forEach(itemSnMapper::insert);
    }

    /**
     * 批量更新SN状态（出库时使用）
     */
    @Transactional(rollbackFor = Exception.class)
    public void batchUpdateStatus(List<Long> snIds, Integer status, Long shipmentOrderId,
                                  Long shipmentOrderDetailId) {
        if (CollUtil.isEmpty(snIds)) {
            return;
        }

        List<ItemSn> snList = itemSnMapper.selectBatchIds(snIds);
        for (ItemSn sn : snList) {
            sn.setStatus(status);
            if (shipmentOrderId != null) {
                sn.setShipmentOrderId(shipmentOrderId);
            }
            if (shipmentOrderDetailId != null) {
                sn.setShipmentOrderDetailId(shipmentOrderDetailId);
            }
            itemSnMapper.updateById(sn);
        }
    }

    /**
     * 批量更新SN库位（移库时使用）
     */
    @Transactional(rollbackFor = Exception.class)
    public void batchUpdateWarehouse(List<Long> snIds, Long warehouseId, Long areaId) {
        if (CollUtil.isEmpty(snIds)) {
            return;
        }

        List<ItemSn> snList = itemSnMapper.selectBatchIds(snIds);
        for (ItemSn sn : snList) {
            sn.setWarehouseId(warehouseId);
            sn.setAreaId(areaId);
            itemSnMapper.updateById(sn);
        }
    }

    /**
     * 根据SKU ID查询在库的SN列表
     */
    public List<ItemSnVo> queryListBySkuId(Long skuId) {
        return itemSnMapper.selectListBySkuId(skuId);
    }

    /**
     * 根据仓库和库区查询在库SN列表
     */
    public List<ItemSnVo> queryListByWarehouseAndArea(Long warehouseId, Long areaId) {
        return itemSnMapper.selectListByWarehouseAndArea(warehouseId, areaId);
    }

    /**
     * 根据SN码精确查询
     */
    public ItemSnVo queryBySnCode(String snCode) {
        return itemSnMapper.selectBySnCode(snCode);
    }

    /**
     * 根据SN码列表批量查询
     */
    public List<ItemSn> queryListBySnCodes(List<String> snCodes) {
        return itemSnMapper.selectListBySnCodes(snCodes);
    }

    /**
     * 删除商品序列号
     */
    public Boolean deleteWithValidById(Long id) {
        // 校验SN是否在库
        ItemSnVo sn = queryById(id);
        if (sn == null) {
            throw new ServiceException("SN码不存在");
        }
        if (sn.getStatus() != 0) {
            throw new ServiceException("SN码不在库中，无法删除: " + sn.getSnCode());
        }
        return itemSnMapper.deleteById(id) > 0;
    }

    /**
     * 批量删除商品序列号
     */
    public Boolean deleteWithValidByIds(List<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return false;
        }

        // 校验所有SN是否都在库中
        List<ItemSnVo> snList = queryList(new ItemSnBo());
        Map<Long, ItemSnVo> snMap = snList.stream()
            .filter(sn -> ids.contains(sn.getId()))
            .collect(Collectors.toMap(ItemSnVo::getId, Function.identity()));

        List<Long> invalidIds = ids.stream()
            .filter(id -> !snMap.containsKey(id) || snMap.get(id).getStatus() != 0)
            .collect(Collectors.toList());

        if (invalidIds.size() > 0) {
            List<String> snCodes = invalidIds.stream()
                .map(id -> snMap.getOrDefault(id, new ItemSnVo()).getSnCode())
                .collect(Collectors.toList());
            throw new ServiceException("以下SN码不在库中或不存在，无法删除: " + String.join(", ", snCodes));
        }

        return itemSnMapper.deleteBatchIds(ids) > 0;
    }

}
