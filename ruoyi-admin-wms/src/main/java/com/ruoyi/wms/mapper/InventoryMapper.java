package com.ruoyi.wms.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.mybatis.core.mapper.BaseMapperPlus;
import com.ruoyi.wms.domain.bo.InventoryBo;
import com.ruoyi.wms.domain.entity.Inventory;
import com.ruoyi.wms.domain.vo.InventoryVo;
import org.apache.ibatis.annotations.Param;

/**
 * 库存Mapper接口
 *
 * @author ping
 * @date 2024-07-19
 */
public interface InventoryMapper extends BaseMapperPlus<Inventory, InventoryVo> {

    Page<InventoryVo> queryAreaBoardList(Page<InventoryVo> page, @Param("bo") InventoryBo bo);

    /**
     * 原子增加库存数量（行锁保障并发安全）
     * @return 影响行数（0=记录不存在，1=更新成功）
     */
    int atomicIncrement(@Param("warehouseId") Long warehouseId,
                        @Param("areaId") Long areaId,
                        @Param("rackId") Long rackId,
                        @Param("locationId") Long locationId,
                        @Param("skuId") Long skuId,
                        @Param("delta") java.math.BigDecimal delta);

    /**
     * 删除数量为0的库存记录
     */
    int deleteZeroQuantity(@Param("warehouseId") Long warehouseId,
                           @Param("areaId") Long areaId,
                           @Param("rackId") Long rackId,
                           @Param("locationId") Long locationId,
                           @Param("skuId") Long skuId);
}
