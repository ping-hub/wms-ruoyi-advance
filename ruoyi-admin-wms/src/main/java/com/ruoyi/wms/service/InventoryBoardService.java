package com.ruoyi.wms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.ruoyi.wms.domain.bo.InventoryBoardBo;
import com.ruoyi.wms.domain.vo.InventoryBoardChartVo;
import com.ruoyi.wms.domain.vo.InventoryBoardListVo;
import com.ruoyi.wms.domain.vo.InventoryBoardSummaryVo;
import com.ruoyi.wms.mapper.InventoryBoardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * 库存统计看板Service
 *
 * @author SOLO
 */
@Service
@RequiredArgsConstructor
public class InventoryBoardService {

    private final InventoryBoardMapper inventoryBoardMapper;

    public InventoryBoardSummaryVo querySummary(InventoryBoardBo bo) {
        InventoryBoardSummaryVo summary = inventoryBoardMapper.selectSummary(bo);
        if (summary == null) {
            summary = new InventoryBoardSummaryVo();
        }
        if (summary.getCurrentQuantity() == null) {
            summary.setCurrentQuantity(BigDecimal.ZERO);
        }
        if (summary.getAvailableQuantity() == null) {
            summary.setAvailableQuantity(BigDecimal.ZERO);
        }
        if (summary.getWarehouseCount() == null) {
            summary.setWarehouseCount(0L);
        }
        if (summary.getAreaCount() == null) {
            summary.setAreaCount(0L);
        }
        if (summary.getAbnormalCount() == null) {
            summary.setAbnormalCount(0L);
        }
        return summary;
    }

    public List<InventoryBoardChartVo> queryChartList(InventoryBoardBo bo) {
        return inventoryBoardMapper.selectChartList(bo);
    }

    public TableDataInfo<InventoryBoardListVo> queryPageList(InventoryBoardBo bo, PageQuery pageQuery) {
        Page<InventoryBoardListVo> result = inventoryBoardMapper.selectPageList(pageQuery.build(), bo);
        return TableDataInfo.build(result);
    }
}
