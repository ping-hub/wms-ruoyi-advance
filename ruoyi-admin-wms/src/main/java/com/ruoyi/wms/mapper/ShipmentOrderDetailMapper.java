package com.ruoyi.wms.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.wms.domain.bo.ShipmentOrderDetailBo;
import com.ruoyi.wms.domain.entity.ShipmentOrderDetail;
import com.ruoyi.wms.domain.vo.ShipmentOrderDetailVo;
import com.ruoyi.common.mybatis.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Param;

/**
 * 出库单详情Mapper接口
 *
 * @author ping
 * @date 2024-08-01
 */
public interface ShipmentOrderDetailMapper extends BaseMapperPlus<ShipmentOrderDetail, ShipmentOrderDetailVo> {

    /**
     * 出库明细跨表分页查询（JOIN wms_shipment_order）
     */
    Page<ShipmentOrderDetailVo> queryDetailPage(Page<ShipmentOrderDetailVo> page, @Param("bo") ShipmentOrderDetailBo bo);
}
