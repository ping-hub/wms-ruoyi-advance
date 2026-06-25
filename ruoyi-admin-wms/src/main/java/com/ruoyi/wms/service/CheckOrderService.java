package com.ruoyi.wms.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.constant.ServiceConstants;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.exception.base.BaseException;
import com.ruoyi.common.core.utils.MapstructUtils;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.mybatis.core.domain.BaseEntity;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.ruoyi.common.satoken.utils.LoginHelper;
import com.ruoyi.wms.domain.bo.CheckOrderBo;
import com.ruoyi.wms.domain.bo.CheckOrderDetailBo;
import com.ruoyi.wms.domain.entity.*;
import com.ruoyi.wms.domain.vo.CheckOrderVo;
import com.ruoyi.wms.domain.vo.ItemInstanceVo;
import com.ruoyi.wms.domain.vo.ItemSkuVo;
import com.ruoyi.wms.mapper.CheckOrderMapper;
import com.ruoyi.wms.mapper.InventoryMapper;
import com.ruoyi.wms.mapper.ItemInstanceMapper;
import com.ruoyi.wms.mapper.LocationMapper;
import com.ruoyi.wms.domain.vo.LocationVo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 库存盘点单据Service业务层处理（两级流程：申请 → 执行）
 *
 * @author ping
 * @date 2024-08-13
 */
@RequiredArgsConstructor
@Service
public class CheckOrderService {


    private final CheckOrderMapper checkOrderMapper;
    private final CodeRuleService codeRuleService;
    private final CheckOrderDetailService checkOrderDetailService;
    private final CheckOrderInstanceService checkOrderInstanceService;
    private final InventoryMapper inventoryMapper;
    private final ItemInstanceMapper itemInstanceMapper;
    private final LocationMapper locationMapper;
    private final ItemSkuService itemSkuService;
    private final WorkflowService workflowService;

    /**
     * 查询库存盘点单据（含明细 + 实例差异）
     */
    public CheckOrderVo queryById(Long id) {
        CheckOrderVo checkOrderVo = checkOrderMapper.selectVoById(id);
        if (checkOrderVo == null) {
            throw new BaseException("盘库单不存在");
        }
        checkOrderVo.setDetails(checkOrderDetailService.queryByCheckOrderId(id));
        checkOrderVo.setInstances(checkOrderInstanceService.queryByCheckOrderId(id));
        checkOrderVo.setWorkflowLogs(workflowService.getLogs("check", id));
        return checkOrderVo;
    }

    /**
     * 查询库存盘点单据列表
     */
    public TableDataInfo<CheckOrderVo> queryPageList(CheckOrderBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<CheckOrder> lqw = buildQueryWrapper(bo);
        Page<CheckOrderVo> result = checkOrderMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询库存盘点单据列表
     */
    public List<CheckOrderVo> queryList(CheckOrderBo bo) {
        LambdaQueryWrapper<CheckOrder> lqw = buildQueryWrapper(bo);
        return checkOrderMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<CheckOrder> buildQueryWrapper(CheckOrderBo bo) {
        LambdaQueryWrapper<CheckOrder> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getCheckOrderNo()), CheckOrder::getCheckOrderNo, bo.getCheckOrderNo());
        lqw.eq(bo.getCheckOrderStatus() != null, CheckOrder::getCheckOrderStatus, bo.getCheckOrderStatus());
        lqw.eq(bo.getCheckOrderTotal() != null, CheckOrder::getCheckOrderTotal, bo.getCheckOrderTotal());
        lqw.eq(bo.getWarehouseId() != null, CheckOrder::getWarehouseId, bo.getWarehouseId());
        lqw.eq(bo.getAreaId() != null, CheckOrder::getAreaId, bo.getAreaId());
        lqw.eq(bo.getRackId() != null, CheckOrder::getRackId, bo.getRackId());
        lqw.eq(StringUtils.isNotBlank(bo.getCheckScopeType()), CheckOrder::getCheckScopeType, bo.getCheckScopeType());
        // "我的盘点"：申请人或执行人是当前用户
        if (StringUtils.isNotBlank(bo.getMyNickName())) {
            lqw.and(w -> w.eq(CheckOrder::getApplicantName, bo.getMyNickName())
                           .or()
                           .eq(CheckOrder::getExecutorName, bo.getMyNickName()));
        }
        lqw.orderByDesc(BaseEntity::getCreateTime);
        return lqw;
    }

    /**
     * 新增库存盘点单据（申请阶段，不保存明细）
     */
    @Transactional
    public void insertByBo(CheckOrderBo bo) {
        bo.setCheckOrderNo(StrUtil.blankToDefault(bo.getCheckOrderNo(), generateCheckOrderNo()));
        validateCheckOrderNo(bo.getCheckOrderNo());
        // 默认草稿状态 + 记录申请人
        bo.setCheckOrderStatus(ServiceConstants.CheckOrderStatus.DRAFT);
        bo.setApplicantId(LoginHelper.getUserId());
        bo.setApplicantName(LoginHelper.getUsername());
        CheckOrder add = MapstructUtils.convert(bo, CheckOrder.class);
        checkOrderMapper.insert(add);
        bo.setId(add.getId());
        workflowService.logOperation("check", add.getId(), "create", "创建盘点单", null, "created");
    }

    private void validateCheckOrderNo(String checkOrderNo) {
        LambdaQueryWrapper<CheckOrder> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(CheckOrder::getCheckOrderNo, checkOrderNo);
        if (checkOrderMapper.exists(lambdaQueryWrapper)) {
            throw new BaseException("系统生成的盘点单号重复，请稍后重试");
        }
    }

    private String generateCheckOrderNo() {
        String code = codeRuleService.generateCode("check");
        return code != null ? code : "PK" + IdUtil.getSnowflakeNextIdStr();
    }

    /**
     * 修改库存盘点单据
     */
    @Transactional
    public void updateByBo(CheckOrderBo bo) {
        CheckOrder update = MapstructUtils.convert(bo, CheckOrder.class);
        checkOrderMapper.updateById(update);
        // 如果有明细则保存（盘点完成阶段）
        if (CollUtil.isNotEmpty(bo.getDetails())) {
            List<CheckOrderDetail> detailList = MapstructUtils.convert(bo.getDetails(), CheckOrderDetail.class);
            detailList.forEach(it -> it.setCheckOrderId(bo.getId()));
            checkOrderDetailService.saveDetails(detailList);
            // 暂存实例差异（前端传入 scannedInstanceCodes 时生效）
            saveDraftInstances(bo.getId(), bo.getDetails());
        }
        // 扫码驱动暂存：保存已扫描编码到 instance 表（resultType='scanned'）
        if (CollUtil.isNotEmpty(bo.getScannedInstanceCodes())) {
            saveScannedCodes(bo.getId(), bo.getScannedInstanceCodes());
        }
    }

    /**
     * 保存扫码暂存数据（resultType='scanned'）
     */
    private void saveScannedCodes(Long checkOrderId, List<String> scannedCodes) {
        // 只删除 scanned 类型，保留其他类型（如暂存的 loss/gain）
        LambdaQueryWrapper<CheckOrderInstance> delLqw = Wrappers.lambdaQuery();
        delLqw.eq(CheckOrderInstance::getCheckOrderId, checkOrderId);
        delLqw.eq(CheckOrderInstance::getResultType, "scanned");
        checkOrderInstanceService.remove(delLqw);

        if (scannedCodes.isEmpty()) return;

        List<CheckOrderInstance> instances = scannedCodes.stream().map(code -> {
            CheckOrderInstance ci = new CheckOrderInstance();
            ci.setCheckOrderId(checkOrderId);
            ci.setInstanceCode(code);
            ci.setResultType("scanned");
            return ci;
        }).toList();
        checkOrderInstanceService.saveInstances(instances);
    }

    /**
     * 开始盘点：从 wms_inventory 按 sku_id 汇总加载账面库存
     */
    @Transactional
    public Map<String, Object> startCheck(Long id) {
        CheckOrder checkOrder = checkOrderMapper.selectById(id);
        Assert.notNull(checkOrder, "盘点单不存在");
        Assert.isTrue(ServiceConstants.CheckOrderStatus.PENDING_CHECK.equals(checkOrder.getCheckOrderStatus()),
            "仅待盘点状态可以开始盘点");

        // 根据盘点范围使用 SQL 聚合查询（GROUP BY sku_id），避免全量加载后内存分组
        List<Map<String, Object>> skuSummary = inventoryMapper.selectSkuQuantitySummary(
            checkOrder.getWarehouseId(), checkOrder.getAreaId(), checkOrder.getRackId());
        Map<Long, BigDecimal> skuQuantityMap = new LinkedHashMap<>();
        long totalInstanceCount = 0;
        for (Map<String, Object> row : skuSummary) {
            Long skuId = ((Number) row.get("skuId")).longValue();
            BigDecimal qty = row.get("totalQuantity") instanceof BigDecimal
                ? (BigDecimal) row.get("totalQuantity") : BigDecimal.valueOf(((Number) row.get("totalQuantity")).doubleValue());
            skuQuantityMap.put(skuId, qty);
            totalInstanceCount += qty.longValue();
        }

        // 删除旧明细（如果有）
        checkOrderDetailService.deleteByCheckOrderIds(Collections.singletonList(id));
        // 删除旧实例差异记录（避免重新盘点时残留脏数据）
        checkOrderInstanceService.deleteByCheckOrderIds(Collections.singletonList(id));

        // 生成 SKU 级明细
        List<CheckOrderDetail> details = new ArrayList<>();
        skuQuantityMap.forEach((skuId, quantity) -> {
            CheckOrderDetail detail = new CheckOrderDetail();
            detail.setCheckOrderId(id);
            detail.setSkuId(skuId);
            detail.setQuantity(quantity);
            details.add(detail);
        });
        checkOrderDetailService.saveDetails(details);

        // 返回轻量信息（不返回全量明细）
        Map<String, Object> result = new HashMap<>();
        result.put("skuCount", skuQuantityMap.size());
        result.put("totalInstanceCount", totalInstanceCount);
        return result;
    }

    /**
     * 验码：校验扫码结果是否属于盘点范围，实时返回盘盈信息
     */
    public Map<String, Object> verify(Long checkOrderId, List<String> instanceCodes) {
        CheckOrder checkOrder = checkOrderMapper.selectById(checkOrderId);
        Assert.notNull(checkOrder, "盘点单不存在");

        // 查询盘点范围内的所有实例编码（缓存友好：只查 instanceCode 和 skuId）
        LambdaQueryWrapper<ItemInstance> lqw = Wrappers.lambdaQuery();
        lqw.select(ItemInstance::getInstanceCode, ItemInstance::getSkuId, ItemInstance::getItemId);
        if (checkOrder.getWarehouseId() != null) lqw.eq(ItemInstance::getWarehouseId, checkOrder.getWarehouseId());
        if (checkOrder.getAreaId() != null) lqw.eq(ItemInstance::getAreaId, checkOrder.getAreaId());
        if (checkOrder.getRackId() != null) lqw.eq(ItemInstance::getRackId, checkOrder.getRackId());
        lqw.in(ItemInstance::getInstanceCode, instanceCodes);
        List<ItemInstance> matched = itemInstanceMapper.selectList(lqw);

        Set<String> matchedCodes = matched.stream().map(ItemInstance::getInstanceCode).collect(Collectors.toSet());

        // 盘盈：扫到了但不在账面范围内
        List<String> surplusCodes = instanceCodes.stream()
            .filter(code -> !matchedCodes.contains(code))
            .toList();

        Map<String, Object> result = new HashMap<>();
        result.put("matched", matchedCodes.size());
        result.put("surplus", surplusCodes.size());
        result.put("surplusCodes", surplusCodes);
        return result;
    }

    /**
     * 从快照中获取指定SKU的账面实例（用于已完成的盘点单）
     */
    private List<ItemInstanceVo> getStoredBookInstancesBySku(Long checkOrderId, Long skuId) {
        LambdaQueryWrapper<CheckOrderInstance> lqw = Wrappers.lambdaQuery();
        lqw.eq(CheckOrderInstance::getCheckOrderId, checkOrderId);
        lqw.eq(CheckOrderInstance::getSkuId, skuId);
        lqw.eq(CheckOrderInstance::getResultType, "book");
        List<CheckOrderInstance> instances = checkOrderInstanceService.list(lqw);

        // 获取SKU信息用于填充展示字段
        Set<Long> skuIds = Collections.singleton(skuId);
        Map<Long, ItemSkuVo> skuMap = itemSkuService.queryVosByIds(skuIds).stream()
            .collect(Collectors.toMap(ItemSkuVo::getId, Function.identity()));

        return instances.stream().map(inst -> {
            ItemInstanceVo vo = new ItemInstanceVo();
            vo.setId(inst.getId());
            vo.setInstanceCode(inst.getInstanceCode());
            vo.setSkuId(inst.getSkuId());
            vo.setInstanceStatus(null);
            ItemSkuVo skuVo = skuMap.get(inst.getSkuId());
            if (skuVo != null) {
                vo.setSkuName(skuVo.getSkuName());
                vo.setItemName(skuVo.getItem() != null ? skuVo.getItem().getItemName() : null);
            }
            return vo;
        }).toList();
    }

    /**
     * 懒加载指定SKU的实例列表（已完成盘点单返回快照，待盘点返回实时数据）
     */
    public List<ItemInstanceVo> getInstancesBySku(Long checkOrderId, Long skuId) {
        CheckOrder checkOrder = checkOrderMapper.selectById(checkOrderId);
        Assert.notNull(checkOrder, "盘点单不存在");

        // 已完成或待复核的盘点单：返回存储的账面实例快照，不再查询实时数据
        if (ServiceConstants.CheckOrderStatus.FINISH.equals(checkOrder.getCheckOrderStatus())
            || ServiceConstants.CheckOrderStatus.PENDING_REVIEW.equals(checkOrder.getCheckOrderStatus())) {
            return getStoredBookInstancesBySku(checkOrderId, skuId);
        }

        LambdaQueryWrapper<ItemInstance> lqw = Wrappers.lambdaQuery();
        lqw.eq(ItemInstance::getSkuId, skuId);
        if (checkOrder.getWarehouseId() != null) {
            lqw.eq(ItemInstance::getWarehouseId, checkOrder.getWarehouseId());
        }
        if (checkOrder.getAreaId() != null) {
            lqw.eq(ItemInstance::getAreaId, checkOrder.getAreaId());
        }
        if (checkOrder.getRackId() != null) {
            lqw.eq(ItemInstance::getRackId, checkOrder.getRackId());
        }
        List<ItemInstance> instances = itemInstanceMapper.selectList(lqw);

        // 轻量转换（只填充展示字段）
        Set<Long> skuIds = instances.stream().map(ItemInstance::getSkuId).filter(Objects::nonNull).collect(Collectors.toSet());
        Map<Long, ItemSkuVo> skuMap = skuIds.isEmpty() ? Collections.emptyMap() :
            itemSkuService.queryVosByIds(skuIds).stream().collect(Collectors.toMap(ItemSkuVo::getId, Function.identity()));

        return instances.stream().map(inst -> {
            ItemInstanceVo vo = new ItemInstanceVo();
            vo.setId(inst.getId());
            vo.setInstanceCode(inst.getInstanceCode());
            vo.setSkuId(inst.getSkuId());
            vo.setItemId(inst.getItemId());
            vo.setInstanceStatus(inst.getInstanceStatus());
            ItemSkuVo skuVo = skuMap.get(inst.getSkuId());
            if (skuVo != null) {
                vo.setSkuName(skuVo.getSkuName());
                vo.setItemName(skuVo.getItem() != null ? skuVo.getItem().getItemName() : null);
            }
            return vo;
        }).toList();
    }

    /**
     * 离线盘点快照：一次性返回全量盘点数据（供App端下载后离线使用）
     * 包含：SKU列表 + 各SKU的实例清单 + 全量 instanceCode 白名单
     */
    public Map<String, Object> getOfflineSnapshot(Long checkOrderId) {
        CheckOrder checkOrder = checkOrderMapper.selectById(checkOrderId);
        Assert.notNull(checkOrder, "盘点单不存在");

        // 查询SKU级明细（startCheck 时已生成）
        LambdaQueryWrapper<CheckOrderDetail> detailLqw = Wrappers.lambdaQuery();
        detailLqw.eq(CheckOrderDetail::getCheckOrderId, checkOrderId);
        List<CheckOrderDetail> details = checkOrderDetailService.list(detailLqw);
        if (CollUtil.isEmpty(details)) {
            throw new BaseException("盘点单尚未生成明细，请先点击开始盘点");
        }

        // 批量加载SKU信息
        Set<Long> skuIds = details.stream().map(CheckOrderDetail::getSkuId)
            .filter(Objects::nonNull).collect(Collectors.toSet());
        Map<Long, ItemSkuVo> skuVoMap = skuIds.isEmpty() ? Collections.emptyMap() :
            itemSkuService.queryVosByIds(skuIds).stream()
                .collect(Collectors.toMap(ItemSkuVo::getId, Function.identity()));

        // 查询盘点范围内所有实例
        LambdaQueryWrapper<ItemInstance> instLqw = Wrappers.lambdaQuery();
        if (checkOrder.getWarehouseId() != null) instLqw.eq(ItemInstance::getWarehouseId, checkOrder.getWarehouseId());
        if (checkOrder.getAreaId() != null) instLqw.eq(ItemInstance::getAreaId, checkOrder.getAreaId());
        if (checkOrder.getRackId() != null) instLqw.eq(ItemInstance::getRackId, checkOrder.getRackId());
        List<ItemInstance> allInstances = itemInstanceMapper.selectList(instLqw);

        // 批量加载货位名称
        Set<Long> locationIds = allInstances.stream()
            .map(ItemInstance::getLocationId).filter(Objects::nonNull).collect(Collectors.toSet());
        Map<Long, String> locationNameMap = Collections.emptyMap();
        if (!locationIds.isEmpty()) {
            locationNameMap = locationMapper.selectVoBatchIds(locationIds).stream()
                .collect(Collectors.toMap(LocationVo::getId, LocationVo::getLocationName, (a, b) -> a));
        }

        // 按 skuId 分组构建快照 SKU 列表
        Map<Long, List<ItemInstance>> instancesBySku = allInstances.stream()
            .filter(i -> i.getSkuId() != null)
            .collect(Collectors.groupingBy(ItemInstance::getSkuId));

        List<String> allInstanceCodes = new ArrayList<>();
        List<Map<String, Object>> skuList = new ArrayList<>();

        for (CheckOrderDetail detail : details) {
            Map<String, Object> skuMap = new LinkedHashMap<>();
            skuMap.put("skuId", detail.getSkuId());
            ItemSkuVo skuVo = skuVoMap.get(detail.getSkuId());
            skuMap.put("skuName", skuVo != null ? skuVo.getSkuName() : null);
            skuMap.put("itemName", skuVo != null && skuVo.getItem() != null ? skuVo.getItem().getItemName() : null);
            skuMap.put("itemCode", skuVo != null && skuVo.getItem() != null ? skuVo.getItem().getItemCode() : null);
            skuMap.put("bookQuantity", detail.getQuantity());

            List<ItemInstance> skuInstances = instancesBySku.getOrDefault(detail.getSkuId(), Collections.emptyList());
            List<Map<String, Object>> instList = new ArrayList<>();
            for (ItemInstance inst : skuInstances) {
                Map<String, Object> instMap = new LinkedHashMap<>();
                instMap.put("instanceId", inst.getId());
                instMap.put("instanceCode", inst.getInstanceCode());
                instMap.put("locationName", inst.getLocationId() != null
                    ? locationNameMap.getOrDefault(inst.getLocationId(), "") : "");
                instList.add(instMap);
                if (inst.getInstanceCode() != null) {
                    allInstanceCodes.add(inst.getInstanceCode());
                }
            }
            skuMap.put("instances", instList);
            skuList.add(skuMap);
        }

        // 仓库名称由前端通过 wmsStore.warehouseMap 解析

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("checkOrderId", checkOrderId);
        result.put("checkOrderNo", checkOrder.getCheckOrderNo());
        result.put("warehouseId", checkOrder.getWarehouseId());
        // warehouseName 由前端 wmsStore.warehouseMap 解析，不再后端返回
        result.put("scopeLabel", checkOrder.getCheckScopeType());
        result.put("totalInstanceCount", allInstanceCodes.size());
        result.put("allInstanceCodes", allInstanceCodes);
        result.put("skus", skuList);
        return result;
    }

    /**
     * 完成盘点（保存差异+实例明细，不调整库存）
     * 支持两种模式：
     * 1. 扫码驱动（App端）：bo.scannedInstanceCodes 为全量已扫码，后端自动按SKU分组计算差异
     * 2. 明细驱动（Web端）：bo.details 为前端组装好的SKU级明细
     *
     * 仅保存盘点数据，不改变状态。状态转移由 completeCheck/approve 等流程方法完成。
     */
    @Transactional
    public void check(CheckOrderBo bo) {
        List<CheckOrderDetailBo> details = bo.getDetails();

        // 扫码驱动模式：根据全量已扫码自动计算SKU级差异
        if (CollUtil.isNotEmpty(bo.getScannedInstanceCodes()) && CollUtil.isEmpty(details)) {
            details = computeDetailsFromScannedCodes(bo.getId(), bo.getScannedInstanceCodes());
            bo.setDetails(details);
        }

        Assert.notEmpty(details, "盘点明细不能为空");

        // 保存盘点单
        if (Objects.isNull(bo.getId())) {
            insertByBo(bo);
        } else {
            updateByBo(bo);
        }

        // 计算盈亏数
        BigDecimal totalProfitAndLoss = BigDecimal.ZERO;
        for (CheckOrderDetailBo detail : details) {
            BigDecimal checkQty = detail.getCheckQuantity() != null ? detail.getCheckQuantity() : BigDecimal.ZERO;
            BigDecimal bookQty = detail.getQuantity() != null ? detail.getQuantity() : BigDecimal.ZERO;
            BigDecimal diff = checkQty.subtract(bookQty);
            detail.setDifferenceQuantity(diff);
            detail.setProfitAndLoss(diff);
            totalProfitAndLoss = totalProfitAndLoss.add(diff);
        }

        // 更新盘点单盈亏总数（仅保存数据，不改变状态）
        CheckOrder updateOrder = new CheckOrder();
        updateOrder.setId(bo.getId());
        updateOrder.setCheckOrderTotal(totalProfitAndLoss);
        checkOrderMapper.updateById(updateOrder);

        // 保存实例差异明细
        saveInstanceDifferences(bo.getId(), details);
    }

    /**
     * 扫码驱动：根据全量已扫描编码，自动按SKU分组，计算每个SKU的实盘数量和差异
     */
    private List<CheckOrderDetailBo> computeDetailsFromScannedCodes(Long checkOrderId, List<String> scannedCodes) {
        CheckOrder checkOrder = checkOrderMapper.selectById(checkOrderId);
        Assert.notNull(checkOrder, "盘点单不存在");

        // 查询已有的SKU级明细（startCheck 时生成的）
        LambdaQueryWrapper<CheckOrderDetail> detailLqw = Wrappers.lambdaQuery();
        detailLqw.eq(CheckOrderDetail::getCheckOrderId, checkOrderId);
        List<CheckOrderDetail> existingDetails = checkOrderDetailService.list(detailLqw);
        if (CollUtil.isEmpty(existingDetails)) {
            return new ArrayList<>();
        }

        // 查询盘点范围内所有账面实例，建立 instanceCode → skuId 映射
        LambdaQueryWrapper<ItemInstance> lqw = Wrappers.lambdaQuery();
        lqw.select(ItemInstance::getInstanceCode, ItemInstance::getSkuId);
        if (checkOrder.getWarehouseId() != null) lqw.eq(ItemInstance::getWarehouseId, checkOrder.getWarehouseId());
        if (checkOrder.getAreaId() != null) lqw.eq(ItemInstance::getAreaId, checkOrder.getAreaId());
        if (checkOrder.getRackId() != null) lqw.eq(ItemInstance::getRackId, checkOrder.getRackId());
        List<ItemInstance> bookInstances = itemInstanceMapper.selectList(lqw);

        Map<String, Long> codeToSkuMap = bookInstances.stream()
            .filter(i -> i.getInstanceCode() != null && i.getSkuId() != null)
            .collect(Collectors.toMap(ItemInstance::getInstanceCode, ItemInstance::getSkuId, (a, b) -> a));

        // 统计已扫码编码按SKU分组
        Map<Long, Set<String>> scannedBySku = new HashMap<>();
        Set<String> scannedSet = new HashSet<>(scannedCodes);
        for (String code : scannedCodes) {
            Long skuId = codeToSkuMap.get(code);
            if (skuId != null) {
                scannedBySku.computeIfAbsent(skuId, k -> new HashSet<>()).add(code);
            }
        }

        // 构建明细Bo列表
        List<CheckOrderDetailBo> result = new ArrayList<>();
        for (CheckOrderDetail detail : existingDetails) {
            CheckOrderDetailBo bo = new CheckOrderDetailBo();
            bo.setId(detail.getId());
            bo.setCheckOrderId(checkOrderId);
            bo.setSkuId(detail.getSkuId());
            bo.setQuantity(detail.getQuantity());
            bo.setWarehouseId(checkOrder.getWarehouseId());
            bo.setAreaId(detail.getAreaId());
            bo.setRackId(detail.getRackId());

            // 实盘数量 = 该SKU被扫到的编码数
            Set<String> skuScanned = scannedBySku.getOrDefault(detail.getSkuId(), Collections.emptySet());
            // 加上盘盈（扫到了但不属于该SKU账面范围的编码不会被计入，盘盈单独处理）
            bo.setCheckQuantity(BigDecimal.valueOf(skuScanned.size()));

            // 把该SKU对应的已扫描编码传下去（供 saveInstanceDifferences 使用）
            bo.setScannedInstanceCodes(new ArrayList<>(skuScanned));
            result.add(bo);
        }

        // 处理盘盈：扫到了但不属于任何账面SKU的编码
        Set<String> allBookCodes = codeToSkuMap.keySet();
        Set<String> gainCodes = scannedSet.stream()
            .filter(code -> !allBookCodes.contains(code))
            .collect(Collectors.toSet());
        if (!gainCodes.isEmpty()) {
            // 盘盈编码作为特殊的"虚拟SKU"明细记录
            // 暂时记录在 checkOrderInstance 中（resultType='gain'），不关联具体SKU
            // 在 saveInstanceDifferences 中处理
        }

        return result;
    }

    /**
     * 保存实例差异明细：集合运算找出盘亏/盘盈实例，同时存储账面实例快照
     */
    private void saveInstanceDifferences(Long checkOrderId, List<CheckOrderDetailBo> details) {
        // 先删除旧实例记录（含快照和差异）
        checkOrderInstanceService.deleteByCheckOrderIds(Collections.singletonList(checkOrderId));

        CheckOrder checkOrder = checkOrderMapper.selectById(checkOrderId);
        List<CheckOrderInstance> allInstances = new ArrayList<>();

        // 收集所有SKU ID，为每个SKU存储账面实例快照
        Set<Long> allSkuIds = details.stream().map(CheckOrderDetailBo::getSkuId).collect(Collectors.toSet());

        // 查询所有SKU在盘点范围内的账面实例
        LambdaQueryWrapper<ItemInstance> bookLqw = Wrappers.lambdaQuery();
        bookLqw.in(ItemInstance::getSkuId, allSkuIds);
        if (checkOrder.getWarehouseId() != null) {
            bookLqw.eq(ItemInstance::getWarehouseId, checkOrder.getWarehouseId());
        }
        if (checkOrder.getAreaId() != null) {
            bookLqw.eq(ItemInstance::getAreaId, checkOrder.getAreaId());
        }
        if (checkOrder.getRackId() != null) {
            bookLqw.eq(ItemInstance::getRackId, checkOrder.getRackId());
        }
        List<ItemInstance> allBookInstances = itemInstanceMapper.selectList(bookLqw);

        // 按skuId分组
        Map<Long, List<ItemInstance>> bookInstancesBySku = allBookInstances.stream()
            .filter(i -> i.getSkuId() != null)
            .collect(Collectors.groupingBy(ItemInstance::getSkuId));

        // 为所有SKU存储账面实例快照（resultType='book'）
        for (CheckOrderDetailBo detail : details) {
            List<ItemInstance> skuBookInstances = bookInstancesBySku.getOrDefault(detail.getSkuId(), Collections.emptyList());
            for (ItemInstance inst : skuBookInstances) {
                if (inst.getInstanceCode() == null) continue;
                CheckOrderInstance ci = new CheckOrderInstance();
                ci.setCheckOrderId(checkOrderId);
                ci.setCheckOrderDetailId(detail.getId());
                ci.setSkuId(detail.getSkuId());
                ci.setInstanceCode(inst.getInstanceCode());
                ci.setInstanceItemName(inst.getInstanceCode());
                ci.setResultType("book");
                allInstances.add(ci);
            }
        }

        // 计算并存储差异实例
        for (CheckOrderDetailBo detail : details) {
            if (detail.getProfitAndLoss() == null || detail.getProfitAndLoss().compareTo(BigDecimal.ZERO) == 0) {
                continue;
            }

            List<ItemInstance> skuBookInstances = bookInstancesBySku.getOrDefault(detail.getSkuId(), Collections.emptyList());
            Map<String, ItemInstance> bookCodeMap = skuBookInstances.stream()
                .filter(i -> i.getInstanceCode() != null)
                .collect(Collectors.toMap(ItemInstance::getInstanceCode, Function.identity(), (a, b) -> a));

            // 前端提交的已扫描编码
            Set<String> scannedCodes = CollUtil.isNotEmpty(detail.getScannedInstanceCodes())
                ? new HashSet<>(detail.getScannedInstanceCodes()) : Collections.emptySet();
            Set<String> bookCodes = bookCodeMap.keySet();

            // 盘亏：账面有 - 已扫描
            Set<String> lossCodes = new HashSet<>(bookCodes);
            lossCodes.removeAll(scannedCodes);
            for (String code : lossCodes) {
                ItemInstance inst = bookCodeMap.get(code);
                CheckOrderInstance ci = new CheckOrderInstance();
                ci.setCheckOrderId(checkOrderId);
                ci.setCheckOrderDetailId(detail.getId());
                ci.setSkuId(detail.getSkuId());
                ci.setInstanceCode(code);
                ci.setInstanceItemName(inst != null ? inst.getInstanceCode() : null);
                ci.setResultType("loss");
                allInstances.add(ci);
            }

            // 盘盈：已扫描 - 账面有
            Set<String> gainCodes = new HashSet<>(scannedCodes);
            gainCodes.removeAll(bookCodes);
            for (String code : gainCodes) {
                CheckOrderInstance ci = new CheckOrderInstance();
                ci.setCheckOrderId(checkOrderId);
                ci.setCheckOrderDetailId(detail.getId());
                ci.setSkuId(detail.getSkuId());
                ci.setInstanceCode(code);
                ci.setResultType("gain");
                allInstances.add(ci);
            }
        }

        checkOrderInstanceService.saveInstances(allInstances);
    }

    /**
     * 暂存实例差异：根据前端传入的 scannedInstanceCodes 对比当前账面实例，
     * 计算盘亏/盘盈并写入 wms_check_order_instance（每次覆盖旧记录）。
     * 未传入 scannedInstanceCodes 的 SKU 不做处理。
     */
    private void saveDraftInstances(Long checkOrderId, List<CheckOrderDetailBo> details) {
        // 检查是否有任何 SKU 携带 scannedInstanceCodes
        boolean hasScanned = details.stream()
            .anyMatch(d -> d.getScannedInstanceCodes() != null);
        if (!hasScanned) {
            return;
        }

        // 覆盖旧记录（draft 和 final 均清除，由后续操作重新写入）
        checkOrderInstanceService.deleteByCheckOrderIds(Collections.singletonList(checkOrderId));

        CheckOrder checkOrder = checkOrderMapper.selectById(checkOrderId);

        // 收集需要处理的 SKU
        Set<Long> skuIds = details.stream()
            .filter(d -> d.getScannedInstanceCodes() != null)
            .map(CheckOrderDetailBo::getSkuId)
            .collect(Collectors.toSet());
        if (skuIds.isEmpty()) return;

        // 查询当前账面实例
        LambdaQueryWrapper<ItemInstance> bookLqw = Wrappers.lambdaQuery();
        bookLqw.in(ItemInstance::getSkuId, skuIds);
        if (checkOrder.getWarehouseId() != null) {
            bookLqw.eq(ItemInstance::getWarehouseId, checkOrder.getWarehouseId());
        }
        if (checkOrder.getAreaId() != null) {
            bookLqw.eq(ItemInstance::getAreaId, checkOrder.getAreaId());
        }
        if (checkOrder.getRackId() != null) {
            bookLqw.eq(ItemInstance::getRackId, checkOrder.getRackId());
        }
        List<ItemInstance> allBookInstances = itemInstanceMapper.selectList(bookLqw);
        Map<Long, List<ItemInstance>> bookInstancesBySku = allBookInstances.stream()
            .filter(i -> i.getSkuId() != null)
            .collect(Collectors.groupingBy(ItemInstance::getSkuId));

        List<CheckOrderInstance> allInstances = new ArrayList<>();

        for (CheckOrderDetailBo detail : details) {
            if (detail.getScannedInstanceCodes() == null) continue;

            List<ItemInstance> skuBookInstances = bookInstancesBySku.getOrDefault(detail.getSkuId(), Collections.emptyList());
            Map<String, ItemInstance> bookCodeMap = skuBookInstances.stream()
                .filter(i -> i.getInstanceCode() != null)
                .collect(Collectors.toMap(ItemInstance::getInstanceCode, Function.identity(), (a, b) -> a));

            Set<String> scannedCodes = new HashSet<>(detail.getScannedInstanceCodes());
            Set<String> bookCodes = bookCodeMap.keySet();

            // 盘亏：账面有 - 已扫描
            Set<String> lossCodes = new HashSet<>(bookCodes);
            lossCodes.removeAll(scannedCodes);
            for (String code : lossCodes) {
                ItemInstance inst = bookCodeMap.get(code);
                CheckOrderInstance ci = new CheckOrderInstance();
                ci.setCheckOrderId(checkOrderId);
                ci.setCheckOrderDetailId(detail.getId());
                ci.setSkuId(detail.getSkuId());
                ci.setInstanceCode(code);
                ci.setInstanceItemName(inst != null ? inst.getInstanceCode() : null);
                ci.setResultType("loss");
                allInstances.add(ci);
            }

            // 盘盈：已扫描 - 账面有
            Set<String> gainCodes = new HashSet<>(scannedCodes);
            gainCodes.removeAll(bookCodes);
            for (String code : gainCodes) {
                CheckOrderInstance ci = new CheckOrderInstance();
                ci.setCheckOrderId(checkOrderId);
                ci.setCheckOrderDetailId(detail.getId());
                ci.setSkuId(detail.getSkuId());
                ci.setInstanceCode(code);
                ci.setResultType("gain");
                allInstances.add(ci);
            }
        }

        checkOrderInstanceService.saveInstances(allInstances);
    }

    @Transactional
    public void deleteById(Long id) {
        validateIdBeforeDelete(id);
        checkOrderInstanceService.deleteByCheckOrderIds(Collections.singletonList(id));
        checkOrderDetailService.deleteByCheckOrderIds(Collections.singletonList(id));
        checkOrderMapper.deleteById(id);
    }

    private void validateIdBeforeDelete(Long id) {
        CheckOrderVo checkOrderVo = queryById(id);
        if (checkOrderVo == null) {
            throw new BaseException("盘库单不存在");
        }
        Integer status = checkOrderVo.getCheckOrderStatus();
        if (ServiceConstants.CheckOrderStatus.INVALID.equals(status)) {
            throw new ServiceException("盘库单【" + checkOrderVo.getCheckOrderNo() + "】已作废，无法删除！", HttpStatus.CONFLICT.value());
        }
        if (ServiceConstants.CheckOrderStatus.FINISH.equals(status)) {
            throw new ServiceException("盘库单【" + checkOrderVo.getCheckOrderNo() + "】已盘点完成，无法删除！", HttpStatus.CONFLICT.value());
        }
        if (ServiceConstants.CheckOrderStatus.PENDING_CHECK.equals(status)) {
            throw new ServiceException("盘库单【" + checkOrderVo.getCheckOrderNo() + "】待盘点中，无法删除！", HttpStatus.CONFLICT.value());
        }
        if (ServiceConstants.CheckOrderStatus.PENDING_REVIEW.equals(status)) {
            throw new ServiceException("盘库单【" + checkOrderVo.getCheckOrderNo() + "】待复核中，无法删除！", HttpStatus.CONFLICT.value());
        }
    }

    /**
     * 批量删除库存盘点单据
     */
    @Transactional
    public void deleteByIds(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return;
        }
        ids.forEach(this::validateIdBeforeDelete);
        checkOrderInstanceService.deleteByCheckOrderIds(ids);
        checkOrderDetailService.deleteByCheckOrderIds(ids);
        checkOrderMapper.deleteBatchIds(ids);
    }


    /**
     * 盘点冻结校验：检查指定位置是否存在活跃的盘点单（待盘点/盘点中）
     * 用于出入库、调拨、借用等操作执行前的拦截
     *
     * @param warehouseId 仓库ID
     * @param areaId      库区ID（可为null）
     * @param rackId      货架ID（可为null）
     */
    public void assertNoActiveCheckOrder(Long warehouseId, Long areaId, Long rackId) {
        if (warehouseId == null) {
            return;
        }
        LambdaQueryWrapper<CheckOrder> lqw = Wrappers.lambdaQuery();
        lqw.eq(CheckOrder::getWarehouseId, warehouseId);
        lqw.in(CheckOrder::getCheckOrderStatus,
            List.of(ServiceConstants.CheckOrderStatus.PENDING_CHECK,
                    ServiceConstants.CheckOrderStatus.PENDING_REVIEW));
        List<CheckOrder> activeOrders = checkOrderMapper.selectList(lqw);
        if (CollUtil.isEmpty(activeOrders)) {
            return;
        }
        for (CheckOrder co : activeOrders) {
            if (isLocationInScope(co, warehouseId, areaId, rackId)) {
                throw new ServiceException("仓库范围正在盘点（盘点单号：" + co.getCheckOrderNo()
                    + "），禁止出入库操作", HttpStatus.CONFLICT.value());
            }
        }
    }

    /**
     * 批量校验多个位置的盘点冻结（适用于调拨等涉及多个位置的场景）
     */
    public void assertNoActiveCheckOrders(List<long[]> locations) {
        if (CollUtil.isEmpty(locations)) {
            return;
        }
        for (long[] loc : locations) {
            Long wid = loc.length > 0 && loc[0] != 0 ? loc[0] : null;
            Long aid = loc.length > 1 && loc[1] != 0 ? loc[1] : null;
            Long rid = loc.length > 2 && loc[2] != 0 ? loc[2] : null;
            assertNoActiveCheckOrder(wid, aid, rid);
        }
    }

    /**
     * 判断操作位置是否在盘点单的范围内
     * 盘点范围层级：仓库 > 库区 > 货架（null 表示该层级不限）
     */
    private boolean isLocationInScope(CheckOrder co, Long warehouseId, Long areaId, Long rackId) {
        // 仓库不匹配 → 不在范围
        if (!Objects.equals(co.getWarehouseId(), warehouseId)) {
            return false;
        }
        // 盘点单只指定了仓库级 → 覆盖整个仓库
        if (co.getAreaId() == null && co.getRackId() == null) {
            return true;
        }
        // 盘点单指定了库区 → 检查库区匹配
        if (co.getAreaId() != null) {
            if (!Objects.equals(co.getAreaId(), areaId)) {
                return false;
            }
        }
        // 盘点单指定了货架 → 检查货架匹配
        if (co.getRackId() != null) {
            if (!Objects.equals(co.getRackId(), rackId)) {
                return false;
            }
        }
        return true;
    }

    // ==================== 审批流程方法 ====================

    /**
     * 提交盘点（草稿/已驳回 → 待盘点）
     * 同时指定盘点人（executor）
     *
     * @param id           盘点单ID
     * @param executorId   盘点人ID
     * @param executorName 盘点人姓名
     */
    @Transactional
    public void submitForApproval(Long id, Long executorId, String executorName) {
        CheckOrder order = checkOrderMapper.selectById(id);
        Assert.notNull(order, "盘点单不存在");
        Assert.isTrue(
            ServiceConstants.CheckOrderStatus.DRAFT.equals(order.getCheckOrderStatus())
                || ServiceConstants.CheckOrderStatus.REJECTED.equals(order.getCheckOrderStatus()),
            "只有草稿或已驳回状态的盘点单才能提交"
        );
        CheckOrder update = new CheckOrder();
        update.setId(id);
        update.setCheckOrderStatus(ServiceConstants.CheckOrderStatus.PENDING_CHECK);
        update.setSubmitTime(LocalDateTime.now());
        update.setExecutorId(executorId);
        update.setExecutorName(executorName);
        update.setApproveRemark(null); // 清除上次驳回原因
        checkOrderMapper.updateById(update);
        workflowService.logOperation("check", id, "submit", "提交盘点",
            executorName != null ? "指定盘点人：" + executorName : null, "submitted");
    }

    /**
     * 完成盘点（待盘点 → 待复核）
     * 保存盘点数据 + 状态转移 + 指定复核人
     *
     * @param bo           盘点数据（含明细）
     * @param reviewerId   复核人ID
     * @param reviewerName 复核人姓名
     */
    @Transactional
    public void completeCheck(CheckOrderBo bo, Long reviewerId, String reviewerName) {
        CheckOrder order = checkOrderMapper.selectById(bo.getId());
        Assert.notNull(order, "盘点单不存在");
        Assert.isTrue(ServiceConstants.CheckOrderStatus.PENDING_CHECK.equals(order.getCheckOrderStatus()),
            "只有待盘点状态的盘点单才能完成盘点");
        // 保存盘点数据（不改变状态）
        check(bo);
        // 状态转移 + 指定复核人
        CheckOrder update = new CheckOrder();
        update.setId(bo.getId());
        update.setCheckOrderStatus(ServiceConstants.CheckOrderStatus.PENDING_REVIEW);
        update.setExecuteTime(LocalDateTime.now());
        update.setReviewerId(reviewerId);
        update.setReviewerName(reviewerName);
        checkOrderMapper.updateById(update);
        workflowService.logOperation("check", bo.getId(), "complete_check", "完成盘点",
            reviewerName != null ? "指定复核人：" + reviewerName : null, "checked");
    }

    /**
     * 复核通过（待复核 → 已完成）
     */
    @Transactional
    public void approve(Long id, String remark) {
        CheckOrder order = checkOrderMapper.selectById(id);
        Assert.notNull(order, "盘点单不存在");
        Assert.isTrue(ServiceConstants.CheckOrderStatus.PENDING_REVIEW.equals(order.getCheckOrderStatus()),
            "只有待复核状态的盘点单才能复核");
        CheckOrder update = new CheckOrder();
        update.setId(id);
        update.setCheckOrderStatus(ServiceConstants.CheckOrderStatus.FINISH);
        update.setApproveRemark(remark);
        checkOrderMapper.updateById(update);
        workflowService.logOperation("check", id, "approve", "复核通过", remark, "approved");
    }

    /**
     * 驳回（待盘点/待复核 → 已驳回）
     */
    @Transactional
    public void reject(Long id, String remark) {
        CheckOrder order = checkOrderMapper.selectById(id);
        Assert.notNull(order, "盘点单不存在");
        Assert.isTrue(
            ServiceConstants.CheckOrderStatus.PENDING_CHECK.equals(order.getCheckOrderStatus())
                || ServiceConstants.CheckOrderStatus.PENDING_REVIEW.equals(order.getCheckOrderStatus()),
            "只有待盘点或待复核状态的盘点单才能驳回"
        );
        String action = ServiceConstants.CheckOrderStatus.PENDING_CHECK.equals(order.getCheckOrderStatus())
            ? "reject_from_check" : "reject_from_review";
        String actionLabel = ServiceConstants.CheckOrderStatus.PENDING_CHECK.equals(order.getCheckOrderStatus())
            ? "盘点驳回" : "复核驳回";
        // 待复核驳回 -> 回到待盘点；待盘点驳回 -> 已驳回
        int targetStatus = ServiceConstants.CheckOrderStatus.PENDING_REVIEW.equals(order.getCheckOrderStatus())
            ? ServiceConstants.CheckOrderStatus.PENDING_CHECK
            : ServiceConstants.CheckOrderStatus.REJECTED;
        CheckOrder update = new CheckOrder();
        update.setId(id);
        update.setCheckOrderStatus(targetStatus);
        update.setApproveRemark(remark);
        checkOrderMapper.updateById(update);
        // 驳回至已驳回时，清除旧的盘点明细和实例差异数据（库存快照已失效，重新提交后需重新加载）
        if (ServiceConstants.CheckOrderStatus.REJECTED == targetStatus) {
            checkOrderDetailService.deleteByCheckOrderIds(java.util.Collections.singletonList(id));
            checkOrderInstanceService.deleteByCheckOrderIds(java.util.Collections.singletonList(id));
        }
        workflowService.logOperation("check", id, action, actionLabel, remark, "rejected");
    }

    /**
     * 作废（草稿/已驳回 → 作废）
     */
    @Transactional
    public void voidOrder(Long id) {
        CheckOrder order = checkOrderMapper.selectById(id);
        Assert.notNull(order, "盘点单不存在");
        Assert.isTrue(
            ServiceConstants.CheckOrderStatus.DRAFT.equals(order.getCheckOrderStatus())
                || ServiceConstants.CheckOrderStatus.REJECTED.equals(order.getCheckOrderStatus()),
            "只有草稿或已驳回状态的盘点单才能作废"
        );
        CheckOrder update = new CheckOrder();
        update.setId(id);
        update.setCheckOrderStatus(ServiceConstants.CheckOrderStatus.INVALID);
        checkOrderMapper.updateById(update);
        workflowService.logOperation("check", id, "void", "作废", null, "voided");
    }
}
