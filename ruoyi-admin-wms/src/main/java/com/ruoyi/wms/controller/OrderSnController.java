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
import com.ruoyi.wms.domain.bo.OrderSnBo;
import com.ruoyi.wms.domain.vo.OrderSnVo;
import com.ruoyi.wms.service.OrderSnService;

import java.util.List;

/**
 * 单据SN关联Controller
 *
 * @author ruoyi
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/wms/orderSn")
public class OrderSnController extends BaseController {

    private final OrderSnService orderSnService;

    /**
     * 查询单据SN关联列表
     */
    @GetMapping("/list")
    @SaCheckPermission("wms:orderSn:list")
    public TableDataInfo<OrderSnVo> list(OrderSnBo bo, PageQuery pageQuery) {
        return orderSnService.queryPageList(bo, pageQuery);
    }

    /**
     * 查询单据SN关联列表（不分页）
     */
    @GetMapping("/listNoPage")
    @SaCheckPermission("wms:orderSn:list")
    public R<List<OrderSnVo>> list(OrderSnBo bo) {
        return R.ok(orderSnService.queryList(bo));
    }

    /**
     * 导出单据SN关联列表
     */
    @Log(title = "单据SN关联", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @SaCheckPermission("wms:orderSn:list")
    public void export(OrderSnBo bo, HttpServletResponse response) {
        List<OrderSnVo> list = orderSnService.queryList(bo);
        ExcelUtil.exportExcel(list, "单据SN关联", OrderSnVo.class, response);
    }

    /**
     * 获取单据SN关联详细信息
     *
     * @param id 主键
     */
    @GetMapping("/{id}")
    @SaCheckPermission("wms:orderSn:query")
    public R<OrderSnVo> getInfo(@NotNull(message = "主键不能为空")
                                        @PathVariable Long id) {
        return R.ok(orderSnService.queryById(id));
    }

    /**
     * 根据单据类型和单据ID查询SN列表
     *
     * @param orderType 单据类型: 1-入库 2-出库 3-移库 4-盘点
     * @param orderId 单据ID
     */
    @GetMapping("/order/{orderType}/{orderId}")
    @SaCheckPermission("wms:orderSn:query")
    public R<List<OrderSnVo>> listByOrder(
        @NotNull(message = "单据类型不能为空") @PathVariable Integer orderType,
        @NotNull(message = "单据ID不能为空") @PathVariable Long orderId) {
        return R.ok(orderSnService.queryListByOrder(orderType, orderId));
    }

    /**
     * 根据单据明细ID查询SN列表
     *
     * @param orderType 单据类型: 1-入库 2-出库 3-移库 4-盘点
     * @param orderDetailId 单据明细ID
     */
    @GetMapping("/detail/{orderType}/{orderDetailId}")
    @SaCheckPermission("wms:orderSn:query")
    public R<List<OrderSnVo>> listByOrderDetail(
        @NotNull(message = "单据类型不能为空") @PathVariable Integer orderType,
        @NotNull(message = "单据明细ID不能为空") @PathVariable Long orderDetailId) {
        return R.ok(orderSnService.queryListByOrderDetail(orderType, orderDetailId));
    }

    /**
     * 根据SN ID查询关联记录
     *
     * @param snId SN ID
     */
    @GetMapping("/sn/{snId}")
    @SaCheckPermission("wms:orderSn:query")
    public R<List<OrderSnVo>> listBySnId(
        @NotNull(message = "SN ID不能为空") @PathVariable Long snId) {
        return R.ok(orderSnService.queryListBySnId(snId));
    }

    /**
     * 新增单据SN关联
     */
    @Log(title = "单据SN关联", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    @SaCheckPermission("wms:orderSn:add")
    public R<Void> add(@Validated(AddGroup.class) @RequestBody OrderSnBo bo) {
        return toAjax(orderSnService.insertByBo(bo));
    }

    /**
     * 批量新增单据SN关联
     */
    @Log(title = "单据SN关联", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping("/batch")
    @SaCheckPermission("wms:orderSn:add")
    public R<Void> batchAdd(@RequestBody List<OrderSnBo> boList) {
        orderSnService.batchInsert(boList);
        return R.ok();
    }

    /**
     * 修改单据SN关联
     */
    @Log(title = "单据SN关联", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    @SaCheckPermission("wms:orderSn:edit")
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody OrderSnBo bo) {
        return toAjax(orderSnService.updateByBo(bo));
    }

    /**
     * 删除单据SN关联
     *
     * @param id 主键
     */
    @Log(title = "单据SN关联", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    @SaCheckPermission("wms:orderSn:remove")
    public R<Void> remove(@NotNull(message = "主键不能为空")
                                 @PathVariable Long id) {
        return toAjax(orderSnService.deleteWithValidById(id));
    }

    /**
     * 批量删除单据SN关联
     *
     * @param ids 主键集合
     */
    @Log(title = "单据SN关联", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    @SaCheckPermission("wms:orderSn:remove")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                                 @PathVariable Long[] ids) {
        return toAjax(orderSnService.deleteWithValidByIds(List.of(ids)));
    }

}
