package com.ruoyi.wms.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.mybatis.core.mapper.BaseMapperPlus;
import com.ruoyi.wms.domain.entity.OrderSn;
import com.ruoyi.wms.domain.vo.OrderSnVo;

import java.util.List;

/**
 * 单据SN关联Mapper接口
 *
 * @author ruoyi
 */
public interface OrderSnMapper extends BaseMapperPlus<OrderSn, OrderSnVo> {

    /**
     * 根据单据类型和单据ID查询SN列表
     */
    default List<OrderSnVo> selectListByOrder(Integer orderType, Long orderId) {
        return selectVoList(new LambdaQueryWrapper<OrderSn>()
            .eq(OrderSn::getOrderType, orderType)
            .eq(OrderSn::getOrderId, orderId)
            .orderByAsc(OrderSn::getSnCode));
    }

    /**
     * 根据单据明细ID查询SN列表
     */
    default List<OrderSnVo> selectListByOrderDetail(Integer orderType, Long orderDetailId) {
        return selectVoList(new LambdaQueryWrapper<OrderSn>()
            .eq(OrderSn::getOrderType, orderType)
            .eq(OrderSn::getOrderDetailId, orderDetailId)
            .orderByAsc(OrderSn::getSnCode));
    }

    /**
     * 根据SN ID查询关联记录
     */
    default List<OrderSnVo> selectListBySnId(Long snId) {
        return selectVoList(new LambdaQueryWrapper<OrderSn>()
            .eq(OrderSn::getSnId, snId)
            .orderByDesc(OrderSn::getCreateTime));
    }

}
