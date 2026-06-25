package com.ruoyi.wms.mapper;

import com.ruoyi.common.mybatis.core.mapper.BaseMapperPlus;
import com.ruoyi.wms.domain.entity.InventoryWarningRule;
import com.ruoyi.wms.domain.vo.InventoryWarningRuleVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 库存预警规则 Mapper
 *
 * @author ping
 * @date 2025-06-23
 */
@Mapper
public interface InventoryWarningRuleMapper extends BaseMapperPlus<InventoryWarningRule, InventoryWarningRuleVo> {

    /**
     * 查询规则列表（不分页）
     */
    List<InventoryWarningRuleVo> selectRuleList(@Param("ruleName") String ruleName,
                                                 @Param("itemId") Long itemId,
                                                 @Param("enabled") String enabled,
                                                 @Param("itemName") String itemName);

    /**
     * 分页查询规则列表
     */
    List<InventoryWarningRuleVo> selectRuleListPage(@Param("ruleName") String ruleName,
                                                     @Param("itemId") Long itemId,
                                                     @Param("enabled") String enabled,
                                                     @Param("itemName") String itemName,
                                                     @Param("offset") int offset,
                                                     @Param("limit") int limit);

    /**
     * 统计规则数量
     */
    int countRuleList(@Param("ruleName") String ruleName,
                      @Param("itemId") Long itemId,
                      @Param("enabled") String enabled,
                      @Param("itemName") String itemName);

    /**
     * 查询所有启用的规则及其实际库存（供预警计算使用）
     */
    List<Map<String, Object>> selectWarningSummary();
}
