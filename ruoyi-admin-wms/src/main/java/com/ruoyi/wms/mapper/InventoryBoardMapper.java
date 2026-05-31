package com.ruoyi.wms.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.wms.domain.bo.InventoryBoardBo;
import com.ruoyi.wms.domain.vo.InventoryBoardChartVo;
import com.ruoyi.wms.domain.vo.InventoryBoardListVo;
import com.ruoyi.wms.domain.vo.InventoryBoardSummaryVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 库存统计看板Mapper接口
 *
 * @author SOLO
 */
public interface InventoryBoardMapper {

    InventoryBoardSummaryVo selectSummary(@Param("bo") InventoryBoardBo bo);

    List<InventoryBoardChartVo> selectChartList(@Param("bo") InventoryBoardBo bo);

    Page<InventoryBoardListVo> selectPageList(Page<InventoryBoardListVo> page, @Param("bo") InventoryBoardBo bo);
}
