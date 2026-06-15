package com.ruoyi.wms.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.mybatis.core.mapper.BaseMapperPlus;
import com.ruoyi.wms.domain.bo.InventoryHistoryBo;
import com.ruoyi.wms.domain.entity.InventoryHistory;
import com.ruoyi.wms.domain.vo.InventoryHistoryVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 库存记录Mapper接口
 *
 * @author ping
 * @date 2024-07-22
 */
public interface InventoryHistoryMapper extends BaseMapperPlus<InventoryHistory, InventoryHistoryVo> {

    Page<InventoryHistoryVo> selectVoPageByBo(Page<Object> page, @Param("bo") InventoryHistoryBo bo);

    List<InventoryHistoryVo> selectVoListByBo(@Param("bo") InventoryHistoryBo bo);
}
