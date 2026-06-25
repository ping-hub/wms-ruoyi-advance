package com.ruoyi.wms.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.mybatis.core.mapper.BaseMapperPlus;
import com.ruoyi.wms.domain.bo.InventoryBo;
import com.ruoyi.wms.domain.entity.Inventory;
import com.ruoyi.wms.domain.vo.InventoryVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

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
     * 按 sku_id 汇总库存数量（SQL 聚合，避免全量加载后内存分组）
     */
    List<Map<String, Object>> selectSkuQuantitySummary(
        @Param("warehouseId") Long warehouseId,
        @Param("areaId") Long areaId,
        @Param("rackId") Long rackId);

    /**
     * 删除数量为0的库存记录
     */
    int deleteZeroQuantity(@Param("warehouseId") Long warehouseId,
                           @Param("areaId") Long areaId,
                           @Param("rackId") Long rackId,
                           @Param("locationId") Long locationId,
                           @Param("skuId") Long skuId);

    /**
     * 批量原子增加库存数量（一次 SQL 更新多条记录，行锁保障并发安全）
     * @param list 库存变动列表（quantity 为增量，正=入库/归还，负=出库/借出）
     * @return 实际更新的行数
     */
    int batchAtomicIncrement(@Param("list") List<InventoryBo> list);

    /**
     * 批量删除数量为0或负数的库存记录（单次 SQL）
     * @param list 本次操作涉及的库存条目
     * @return 删除的行数
     */
    int batchDeleteZeroQuantity(@Param("list") List<InventoryBo> list);
}
