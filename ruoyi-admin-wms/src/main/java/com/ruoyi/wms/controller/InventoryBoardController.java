package com.ruoyi.wms.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.ruoyi.common.web.core.BaseController;
import com.ruoyi.wms.domain.bo.InventoryBoardBo;
import com.ruoyi.wms.domain.vo.InventoryBoardChartVo;
import com.ruoyi.wms.domain.vo.InventoryBoardListVo;
import com.ruoyi.wms.domain.vo.InventoryBoardSummaryVo;
import com.ruoyi.wms.service.InventoryBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 库存统计看板
 *
 * @author SOLO
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/wms/inventoryBoard")
public class InventoryBoardController extends BaseController {

    private final InventoryBoardService inventoryBoardService;

    @SaCheckPermission("wms:inventoryBoard:view")
    @GetMapping("/summary")
    public R<InventoryBoardSummaryVo> summary(InventoryBoardBo bo) {
        return R.ok(inventoryBoardService.querySummary(bo));
    }

    @SaCheckPermission("wms:inventoryBoard:view")
    @GetMapping("/chart")
    public R<List<InventoryBoardChartVo>> chart(InventoryBoardBo bo) {
        return R.ok(inventoryBoardService.queryChartList(bo));
    }

    @SaCheckPermission("wms:inventoryBoard:view")
    @GetMapping("/list")
    public TableDataInfo<InventoryBoardListVo> list(InventoryBoardBo bo, PageQuery pageQuery) {
        return inventoryBoardService.queryPageList(bo, pageQuery);
    }
}
