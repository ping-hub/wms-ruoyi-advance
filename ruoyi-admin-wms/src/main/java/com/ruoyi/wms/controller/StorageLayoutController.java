package com.ruoyi.wms.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.wms.domain.bo.AreaBo;
import com.ruoyi.wms.domain.bo.LocationBo;
import com.ruoyi.wms.domain.bo.RackBo;
import com.ruoyi.wms.domain.bo.WarehouseBo;
import com.ruoyi.wms.domain.vo.AreaVo;
import com.ruoyi.wms.domain.vo.LocationSummaryVo;
import com.ruoyi.wms.domain.vo.LocationVo;
import com.ruoyi.wms.domain.vo.RackGridVo;
import com.ruoyi.wms.domain.vo.RackVo;
import com.ruoyi.wms.domain.vo.StorageLayoutNodeVo;
import com.ruoyi.wms.domain.vo.WarehouseVo;
import com.ruoyi.wms.service.AreaService;
import com.ruoyi.wms.service.LocationService;
import com.ruoyi.wms.service.RackService;
import com.ruoyi.wms.service.StorageLayoutQueryService;
import com.ruoyi.wms.service.WarehouseService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/wms/layout")
public class StorageLayoutController {

    private final WarehouseService warehouseService;
    private final AreaService areaService;
    private final RackService rackService;
    private final LocationService locationService;
    private final StorageLayoutQueryService storageLayoutQueryService;

    @SaCheckPermission("wms:warehouse:list")
    @GetMapping("/warehouses")
    public R<List<WarehouseVo>> warehouses(WarehouseBo bo) {
        return R.ok(warehouseService.queryList(bo));
    }

    @SaCheckPermission("wms:warehouse:list")
    @GetMapping("/areas")
    public R<List<AreaVo>> areas(AreaBo bo) {
        return R.ok(areaService.queryList(bo));
    }

    @SaCheckPermission("wms:rack:list")
    @GetMapping("/racks")
    public R<List<RackVo>> racks(RackBo bo) {
        return R.ok(rackService.queryList(bo));
    }

    @SaCheckPermission("wms:location:list")
    @GetMapping("/locations")
    public R<List<LocationVo>> locations(LocationBo bo) {
        return R.ok(locationService.queryList(bo));
    }

    @SaCheckPermission("wms:warehouse:list")
    @GetMapping("/tree")
    public R<List<StorageLayoutNodeVo>> tree(Long warehouseId, Long areaId, Long rackId) {
        return R.ok(storageLayoutQueryService.queryLayoutTree(warehouseId, areaId, rackId));
    }

    @SaCheckPermission("wms:rack:list")
    @GetMapping("/rack/{rackId}/grid")
    public R<RackGridVo> rackGrid(@NotNull(message = "货架不能为空") @PathVariable Long rackId) {
        return R.ok(locationService.queryRackGrid(rackId));
    }

    @SaCheckPermission("wms:location:list")
    @GetMapping("/location/{locationId}/summary")
    public R<LocationSummaryVo> locationSummary(@NotNull(message = "货位不能为空") @PathVariable Long locationId) {
        return R.ok(locationService.querySummaryById(locationId));
    }

    @SaCheckPermission("wms:location:list")
    @GetMapping("/internal/checkScope")
    public R<List<StorageLayoutNodeVo>> checkScope(Long warehouseId, Long areaId, Long rackId) {
        return R.ok(storageLayoutQueryService.queryLayoutTree(warehouseId, areaId, rackId));
    }

    @SaCheckPermission("wms:location:list")
    @GetMapping("/internal/moveTargets")
    public R<List<LocationVo>> moveTargets(LocationBo bo) {
        return R.ok(locationService.queryList(bo));
    }

    @SaCheckPermission("wms:location:list")
    @GetMapping("/internal/receiptTargets")
    public R<List<LocationVo>> receiptTargets(LocationBo bo) {
        return R.ok(locationService.queryList(bo));
    }

    @SaCheckPermission("wms:location:list")
    @GetMapping("/internal/adjustLocations")
    public R<List<LocationVo>> adjustLocations(LocationBo bo) {
        return R.ok(locationService.queryList(bo));
    }
}
