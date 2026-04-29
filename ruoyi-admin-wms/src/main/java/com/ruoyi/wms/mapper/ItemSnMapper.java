package com.ruoyi.wms.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.mybatis.core.mapper.BaseMapperPlus;
import com.ruoyi.wms.domain.entity.ItemSn;
import com.ruoyi.wms.domain.vo.ItemSnVo;

import java.util.List;

/**
 * 商品序列号Mapper接口
 *
 * @author ruoyi
 */
public interface ItemSnMapper extends BaseMapperPlus<ItemSn, ItemSnVo> {

    /**
     * 根据SKU ID查询在库的SN列表
     */
    default List<ItemSnVo> selectListBySkuId(Long skuId) {
        return selectVoList(new LambdaQueryWrapper<ItemSn>()
            .eq(ItemSn::getSkuId, skuId)
            .eq(ItemSn::getStatus, 0)
            .orderByAsc(ItemSn::getSnCode));
    }

    /**
     * 根据仓库和库区查询在库SN列表
     */
    default List<ItemSnVo> selectListByWarehouseAndArea(Long warehouseId, Long areaId) {
        LambdaQueryWrapper<ItemSn> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ItemSn::getStatus, 0)
            .eq(ItemSn::getWarehouseId, warehouseId);
        if (areaId != null) {
            wrapper.eq(ItemSn::getAreaId, areaId);
        }
        wrapper.orderByAsc(ItemSn::getSnCode);
        return selectVoList(wrapper);
    }

    /**
     * 根据SN码精确查询
     */
    default ItemSnVo selectBySnCode(String snCode) {
        return selectVoOne(new LambdaQueryWrapper<ItemSn>()
            .eq(ItemSn::getSnCode, snCode)
            .last("limit 1"));
    }

    /**
     * 批量查询SN码是否存在
     */
    default List<ItemSn> selectListBySnCodes(List<String> snCodes) {
        return selectList(new LambdaQueryWrapper<ItemSn>()
            .in(ItemSn::getSnCode, snCodes));
    }

}
