package com.ruoyi.wms.mapper;

import com.ruoyi.common.mybatis.core.mapper.BaseMapperPlus;
import com.ruoyi.wms.domain.entity.Inventory;
import com.ruoyi.wms.domain.vo.InventoryVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 仓储看板统计 Mapper
 */
@Mapper
public interface DashboardMapper extends BaseMapperPlus<Inventory, InventoryVo> {

    /**
     * 按入库类型分组统计单据数量
     */
    List<Map<String, Object>> countReceiptByType();

    /**
     * 按器材分类统计库存数量和金额
     */
    List<Map<String, Object>> countInventoryByCategory();

    /**
     * 按日期和出入库类型统计数量（近N天趋势）
     */
    List<Map<String, Object>> countInoutByDate(@Param("startDate") LocalDate startDate);

    /**
     * 按质量等级统计实例数量
     */
    List<Map<String, Object>> countByQualityGrade();

    /**
     * 统计总库存数量
     */
    BigDecimal sumInventoryQuantity();

    /**
     * 统计总库存价值（基于入库历史最近单价）
     */
    BigDecimal sumInventoryValue();
}
