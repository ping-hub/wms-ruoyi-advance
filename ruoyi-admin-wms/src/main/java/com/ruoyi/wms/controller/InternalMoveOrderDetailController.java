package com.ruoyi.wms.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.ruoyi.common.web.core.BaseController;
import com.ruoyi.wms.domain.bo.InternalMoveOrderDetailBo;
import com.ruoyi.wms.domain.vo.InternalMoveOrderDetailVo;
import com.ruoyi.wms.service.InternalMoveOrderDetailService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 库内移库单明细
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/wms/internalMoveOrderDetail")
public class InternalMoveOrderDetailController extends BaseController {

    private final InternalMoveOrderDetailService internalMoveOrderDetailService;

    @SaCheckPermission("wms:internalMove:list")
    @GetMapping("/list")
    public TableDataInfo<InternalMoveOrderDetailVo> list(InternalMoveOrderDetailBo bo, PageQuery pageQuery) {
        return internalMoveOrderDetailService.queryPageList(bo, pageQuery);
    }

    @SaCheckPermission("wms:internalMove:list")
    @GetMapping("/listNoPage")
    public R<List<InternalMoveOrderDetailVo>> listNoPage(InternalMoveOrderDetailBo bo) {
        return R.ok(internalMoveOrderDetailService.queryList(bo));
    }

    @SaCheckPermission("wms:internalMove:list")
    @GetMapping("/{id}")
    public R<InternalMoveOrderDetailVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(internalMoveOrderDetailService.queryById(id));
    }

    @SaCheckPermission("wms:internalMove:remove")
    @Log(title = "库内移库单明细", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        internalMoveOrderDetailService.deleteByIds(List.of(ids));
        return R.ok();
    }
}
