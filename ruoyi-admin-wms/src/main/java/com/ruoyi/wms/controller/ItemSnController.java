package com.ruoyi.wms.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import com.ruoyi.common.excel.utils.ExcelUtil;
import com.ruoyi.common.idempotent.annotation.RepeatSubmit;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.ruoyi.common.web.core.BaseController;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.wms.domain.bo.ItemSnBo;
import com.ruoyi.wms.domain.vo.ItemSnVo;
import com.ruoyi.wms.service.ItemSnService;

import java.util.List;

/**
 * 商品序列号Controller
 *
 * @author ruoyi
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/wms/itemSn")
public class ItemSnController extends BaseController {

    private final ItemSnService itemSnService;

    /**
     * 查询商品序列号列表
     */
    @GetMapping("/list")
    @SaCheckPermission("wms:itemSn:list")
    public TableDataInfo<ItemSnVo> list(ItemSnBo bo, PageQuery pageQuery) {
        return itemSnService.queryPageList(bo, pageQuery);
    }

    /**
     * 查询商品序列号列表（不分页）
     */
    @GetMapping("/listNoPage")
    @SaCheckPermission("wms:itemSn:list")
    public R<List<ItemSnVo>> list(ItemSnBo bo) {
        return R.ok(itemSnService.queryList(bo));
    }

    /**
     * 导出商品序列号列表
     */
    @Log(title = "商品序列号", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @SaCheckPermission("wms:itemSn:list")
    public void export(ItemSnBo bo, HttpServletResponse response) {
        List<ItemSnVo> list = itemSnService.queryList(bo);
        ExcelUtil.exportExcel(list, "商品序列号", ItemSnVo.class, response);
    }

    /**
     * 获取商品序列号详细信息
     *
     * @param id 主键
     */
    @GetMapping("/{id}")
    @SaCheckPermission("wms:itemSn:query")
    public R<ItemSnVo> getInfo(@NotNull(message = "主键不能为空")
                                       @PathVariable Long id) {
        return R.ok(itemSnService.queryById(id));
    }

    /**
     * 根据SN码查询详细信息
     *
     * @param snCode SN码
     */
    @GetMapping("/sn/{snCode}")
    @SaCheckPermission("wms:itemSn:query")
    public R<ItemSnVo> getInfoBySnCode(@NotNull(message = "SN码不能为空")
                                                @PathVariable String snCode) {
        return R.ok(itemSnService.queryBySnCode(snCode));
    }

    /**
     * 根据SKU ID查询在库SN列表
     *
     * @param skuId SKU ID
     */
    @GetMapping("/sku/{skuId}")
    @SaCheckPermission("wms:itemSn:query")
    public R<List<ItemSnVo>> listBySkuId(@NotNull(message = "SKU ID不能为空")
                                                 @PathVariable Long skuId) {
        return R.ok(itemSnService.queryListBySkuId(skuId));
    }

    /**
     * 根据仓库和库区查询在库SN列表
     *
     * @param warehouseId 仓库ID
     * @param areaId 库区ID（可选）
     */
    @GetMapping("/warehouse/{warehouseId}")
    @SaCheckPermission("wms:itemSn:query")
    public R<List<ItemSnVo>> listByWarehouseAndArea(
        @NotNull(message = "仓库ID不能为空") @PathVariable Long warehouseId,
        @RequestParam(required = false) Long areaId) {
        return R.ok(itemSnService.queryListByWarehouseAndArea(warehouseId, areaId));
    }

    /**
     * 新增商品序列号
     */
    @Log(title = "商品序列号", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    @SaCheckPermission("wms:itemSn:add")
    public R<Void> add(@Validated(AddGroup.class) @RequestBody ItemSnBo bo) {
        return toAjax(itemSnService.insertByBo(bo));
    }

    /**
     * 批量新增商品序列号
     */
    @Log(title = "商品序列号", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping("/batch")
    @SaCheckPermission("wms:itemSn:add")
    public R<Void> batchAdd(@RequestBody List<ItemSnBo> boList) {
        itemSnService.batchInsert(boList);
        return R.ok();
    }

    /**
     * 修改商品序列号
     */
    @Log(title = "商品序列号", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    @SaCheckPermission("wms:itemSn:edit")
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody ItemSnBo bo) {
        return toAjax(itemSnService.updateByBo(bo));
    }

    /**
     * 删除商品序列号
     *
     * @param id 主键
     */
    @Log(title = "商品序列号", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    @SaCheckPermission("wms:itemSn:remove")
    public R<Void> remove(@NotNull(message = "主键不能为空")
                                  @PathVariable Long id) {
        return toAjax(itemSnService.deleteWithValidById(id));
    }

    /**
     * 批量删除商品序列号
     *
     * @param ids 主键集合
     */
    @Log(title = "商品序列号", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    @SaCheckPermission("wms:itemSn:remove")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                                  @PathVariable Long[] ids) {
        return toAjax(itemSnService.deleteWithValidByIds(List.of(ids)));
    }

    /**
     * 批量更新SN状态
     *
     * @param snIds SN ID列表
     * @param status 状态
     * @param shipmentOrderId 出库单ID（可选）
     * @param shipmentOrderDetailId 出库明细ID（可选）
     */
    @Log(title = "商品序列号", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping("/batchStatus")
    @SaCheckPermission("wms:itemSn:edit")
    public R<Void> batchUpdateStatus(
        @RequestBody @NotNull(message = "参数不能为空") ItemSnBo bo) {
        itemSnService.batchUpdateStatus(bo.getIds(), bo.getStatus(),
            bo.getShipmentOrderId(), bo.getShipmentOrderDetailId());
        return R.ok();
    }

    /**
     * 批量更新SN库位
     *
     * @param snIds SN ID列表
     * @param warehouseId 仓库ID
     * @param areaId 库区ID
     */
    @Log(title = "商品序列号", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping("/batchWarehouse")
    @SaCheckPermission("wms:itemSn:edit")
    public R<Void> batchUpdateWarehouse(
        @RequestBody @NotNull(message = "参数不能为空") ItemSnBo bo) {
        itemSnService.batchUpdateWarehouse(bo.getIds(), bo.getWarehouseId(), bo.getAreaId());
        return R.ok();
    }

}
