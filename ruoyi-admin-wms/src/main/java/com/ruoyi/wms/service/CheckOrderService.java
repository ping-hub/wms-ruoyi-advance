package com.ruoyi.wms.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.ruoyi.common.core.constant.ServiceConstants;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.exception.base.BaseException;
import com.ruoyi.common.core.utils.MapstructUtils;
import com.ruoyi.common.mybatis.core.domain.BaseEntity;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.core.utils.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.wms.domain.bo.CheckOrderDetailBo;
import com.ruoyi.wms.domain.entity.*;
import com.ruoyi.wms.domain.vo.*;
import com.ruoyi.wms.mapper.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.ruoyi.wms.domain.bo.CheckOrderBo;
import com.ruoyi.wms.domain.entity.CheckOrder;
import com.ruoyi.wms.mapper.CheckOrderMapper;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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

    @Value("${warehouse}")
    private String warehouse;

    private final CheckOrderMapper checkOrderMapper;
    private final CodeRuleService codeRuleService;
    private final CheckOrderDetailService checkOrderDetailService;
    private final CheckOrderInstanceService checkOrderInstanceService;
    private final InventoryMapper inventoryMapper;
    private final ItemInstanceMapper itemInstanceMapper;
    private final ItemSkuService itemSkuService;

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
        CheckOrder add = MapstructUtils.convert(bo, CheckOrder.class);
        checkOrderMapper.insert(add);
        // 申请阶段不保存明细
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
        return code != null ? code : "PK" + warehouse + IdUtil.getSnowflakeNextIdStr();
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
        }
    }

    /**
     * 开始盘点：从 wms_inventory 按 sku_id 汇总加载账面库存
     */
    @Transactional
    public CheckOrderVo startCheck(Long id) {
        CheckOrder checkOrder = checkOrderMapper.selectById(id);
        Assert.notNull(checkOrder, "盘点单不存在");
        Assert.isTrue(ServiceConstants.CheckOrderStatus.PENDING.equals(checkOrder.getCheckOrderStatus()),
            "仅待盘点状态可以开始盘点");

        // 根据盘点范围查询库存
        LambdaQueryWrapper<Inventory> invLqw = Wrappers.lambdaQuery();
        if (checkOrder.getWarehouseId() != null) {
            invLqw.eq(Inventory::getWarehouseId, checkOrder.getWarehouseId());
        }
        if (checkOrder.getAreaId() != null) {
            invLqw.eq(Inventory::getAreaId, checkOrder.getAreaId());
        }
        if (checkOrder.getRackId() != null) {
            invLqw.eq(Inventory::getRackId, checkOrder.getRackId());
        }
        List<Inventory> inventories = inventoryMapper.selectList(invLqw);

        // 按 skuId 汇总 quantity
        Map<Long, BigDecimal> skuQuantityMap = inventories.stream()
            .filter(inv -> inv.getSkuId() != null && inv.getQuantity() != null)
            .collect(Collectors.groupingBy(
                Inventory::getSkuId,
                Collectors.reducing(BigDecimal.ZERO, Inventory::getQuantity, BigDecimal::add)
            ));

        // 删除旧明细（如果有）
        checkOrderDetailService.deleteByCheckOrderIds(Collections.singletonList(id));

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

        return queryById(id);
    }

    /**
     * 懒加载指定SKU的在库实例列表
     */
    public List<ItemInstanceVo> getInstancesBySku(Long checkOrderId, Long skuId) {
        CheckOrder checkOrder = checkOrderMapper.selectById(checkOrderId);
        Assert.notNull(checkOrder, "盘点单不存在");

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
     * 完成盘点（保存差异+实例明细，不调整库存）
     */
    @Transactional
    public void check(CheckOrderBo bo) {
        List<CheckOrderDetailBo> details = bo.getDetails();
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

        // 更新盘点单盈亏总数
        CheckOrder updateOrder = new CheckOrder();
        updateOrder.setId(bo.getId());
        updateOrder.setCheckOrderTotal(totalProfitAndLoss);
        updateOrder.setCheckOrderStatus(ServiceConstants.CheckOrderStatus.FINISH);
        checkOrderMapper.updateById(updateOrder);

        // 保存实例差异明细
        saveInstanceDifferences(bo.getId(), details);
    }

    /**
     * 保存实例差异明细：集合运算找出盘亏/盘盈实例
     */
    private void saveInstanceDifferences(Long checkOrderId, List<CheckOrderDetailBo> details) {
        // 先删除旧实例差异
        checkOrderInstanceService.deleteByCheckOrderIds(Collections.singletonList(checkOrderId));

        CheckOrder checkOrder = checkOrderMapper.selectById(checkOrderId);
        List<CheckOrderInstance> allInstances = new ArrayList<>();

        for (CheckOrderDetailBo detail : details) {
            if (detail.getProfitAndLoss() == null || detail.getProfitAndLoss().compareTo(BigDecimal.ZERO) == 0) {
                continue;
            }

            // 查询该SKU在盘点范围内的账面实例编码
            LambdaQueryWrapper<ItemInstance> bookLqw = Wrappers.lambdaQuery();
            bookLqw.eq(ItemInstance::getSkuId, detail.getSkuId());
            if (checkOrder.getWarehouseId() != null) {
                bookLqw.eq(ItemInstance::getWarehouseId, checkOrder.getWarehouseId());
            }
            if (checkOrder.getAreaId() != null) {
                bookLqw.eq(ItemInstance::getAreaId, checkOrder.getAreaId());
            }
            if (checkOrder.getRackId() != null) {
                bookLqw.eq(ItemInstance::getRackId, checkOrder.getRackId());
            }
            List<ItemInstance> bookInstances = itemInstanceMapper.selectList(bookLqw);
            Map<String, ItemInstance> bookCodeMap = bookInstances.stream()
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
                ci.setInstanceCode(inst != null ? inst.getInstanceCode() : null);
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
                ci.setInstanceCode(null);
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
        if (ServiceConstants.CheckOrderStatus.INVALID.equals(checkOrderVo.getCheckOrderStatus())) {
            throw new ServiceException("盘库单【" + checkOrderVo.getCheckOrderNo() + "】已作废，无法删除！", HttpStatus.CONFLICT.value());
        }
        if (ServiceConstants.CheckOrderStatus.FINISH.equals(checkOrderVo.getCheckOrderStatus())) {
            throw new ServiceException("盘库单【" + checkOrderVo.getCheckOrderNo() + "】已盘点完成，无法删除！", HttpStatus.CONFLICT.value());
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
}
