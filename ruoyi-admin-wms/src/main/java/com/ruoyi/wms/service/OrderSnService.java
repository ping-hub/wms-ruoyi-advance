package com.ruoyi.wms.service;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.MapstructUtils;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.ruoyi.wms.domain.bo.OrderSnBo;
import com.ruoyi.wms.domain.entity.OrderSn;
import com.ruoyi.wms.domain.vo.ItemSnVo;
import com.ruoyi.wms.domain.vo.OrderSnVo;
import com.ruoyi.wms.domain.vo.ItemSkuVo;
import com.ruoyi.wms.mapper.OrderSnMapper;
import com.ruoyi.wms.service.ItemSnService;
import com.ruoyi.wms.service.ItemSkuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 单据SN关联Service
 *
 * @author ruoyi
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class OrderSnService {

    private final OrderSnMapper orderSnMapper;
    private final ItemSnService itemSnService;

    /**
     * 查询单据SN关联
     */
    public OrderSnVo queryById(Long id) {
        return orderSnMapper.selectVoById(id);
    }

    /**
     * 分页查询单据SN关联
     */
    public TableDataInfo<OrderSnVo> queryPageList(OrderSnBo bo, com.ruoyi.common.mybatis.core.page.PageQuery pageQuery) {
        LambdaQueryWrapper<OrderSn> lqw = buildQueryWrapper(bo);
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<OrderSnVo> result =
            orderSnMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询单据SN关联列表
     */
    public List<OrderSnVo> queryList(OrderSnBo bo) {
        LambdaQueryWrapper<OrderSn> lqw = buildQueryWrapper(bo);
        return orderSnMapper.selectVoList(lqw);
    }

    /**
     * 构建查询条件
     */
    private LambdaQueryWrapper<OrderSn> buildQueryWrapper(OrderSnBo bo) {
        LambdaQueryWrapper<OrderSn> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getOrderType() != null, OrderSn::getOrderType, bo.getOrderType());
        lqw.eq(bo.getOrderId() != null, OrderSn::getOrderId, bo.getOrderId());
        lqw.eq(bo.getOrderDetailId() != null, OrderSn::getOrderDetailId, bo.getOrderDetailId());
        lqw.eq(bo.getSnId() != null, OrderSn::getSnId, bo.getSnId());
        lqw.orderByAsc(OrderSn::getSnCode);
        return lqw;
    }

    /**
     * 新增单据SN关联
     */
    public Boolean insertByBo(OrderSnBo bo) {
        OrderSn add = MapstructUtils.convert(bo, OrderSn.class);
        validEntityBeforeSave(add);
        boolean flag = orderSnMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 批量新增单据SN关联
     */
    @Transactional(rollbackFor = Exception.class)
    public void batchInsert(List<OrderSnBo> snList) {
        if (CollUtil.isEmpty(snList)) {
            return;
        }

        List<OrderSn> entities = MapstructUtils.convert(snList, OrderSn.class);
        entities.forEach(this::validEntityBeforeSave);
        entities.forEach(orderSnMapper::insert);
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(OrderSn entity) {
        // 校验单据是否存在
        validateOrderExists(entity.getOrderType(), entity.getOrderId(), entity.getOrderDetailId());

        // 校验SN是否存在且状态允许
        validateSnExists(entity.getSnId(), entity.getSnCode());
    }

    /**
     * 校验单据是否存在
     */
    private void validateOrderExists(Integer orderType, Long orderId, Long orderDetailId) {
        // 这里可以根据实际业务需要调用不同的单据服务进行校验
        // 例如：入库单、出库单、移库单等
        // 当前版本仅做基本校验
        if (orderType == null) {
            throw new ServiceException("单据类型不能为空");
        }
        if (orderId == null) {
            throw new ServiceException("单据ID不能为空");
        }
        if (orderDetailId == null) {
            throw new ServiceException("单据明细ID不能为空");
        }
    }

    /**
     * 校验SN是否存在且状态允许
     */
    private void validateSnExists(Long snId, String snCode) {
        if (snId == null && snCode == null) {
            throw new ServiceException("SN ID和SN码不能同时为空");
        }

        if (snId != null) {
            ItemSnVo sn = itemSnService.queryById(snId);
            if (sn == null) {
                throw new ServiceException("SN码不存在: " + snId);
            }
            // 校验SN状态是否允许关联到单据
            if (sn.getStatus() == 1) {
                throw new ServiceException("SN码已出库，无法关联到新单据: " + sn.getSnCode());
            }
        } else {
            ItemSnVo sn = itemSnService.queryBySnCode(snCode);
            if (sn == null) {
                throw new ServiceException("SN码不存在: " + snCode);
            }
            // 校验SN状态是否允许关联到单据
            if (sn.getStatus() == 1) {
                throw new ServiceException("SN码已出库，无法关联到新单据: " + snCode);
            }
        }
    }

    /**
     * 修改单据SN关联
     */
    public Boolean updateByBo(OrderSnBo bo) {
        OrderSn update = MapstructUtils.convert(bo, OrderSn.class);
        validEntityBeforeSave(update);
        return orderSnMapper.updateById(update) > 0;
    }

    /**
     * 删除单据SN关联
     */
    public Boolean deleteWithValidById(Long id) {
        return orderSnMapper.deleteById(id) > 0;
    }

    /**
     * 批量删除单据SN关联
     */
    public Boolean deleteWithValidByIds(List<Long> ids) {
        return orderSnMapper.deleteBatchIds(ids) > 0;
    }

    /**
     * 根据单据类型和单据ID查询SN列表
     */
    public List<OrderSnVo> queryListByOrder(Integer orderType, Long orderId) {
        return orderSnMapper.selectListByOrder(orderType, orderId);
    }

    /**
     * 根据单据明细ID查询SN列表
     */
    public List<OrderSnVo> queryListByOrderDetail(Integer orderType, Long orderDetailId) {
        return orderSnMapper.selectListByOrderDetail(orderType, orderDetailId);
    }

    /**
     * 根据SN ID查询关联记录
     */
    public List<OrderSnVo> queryListBySnId(Long snId) {
        return orderSnMapper.selectListBySnId(snId);
    }

}
