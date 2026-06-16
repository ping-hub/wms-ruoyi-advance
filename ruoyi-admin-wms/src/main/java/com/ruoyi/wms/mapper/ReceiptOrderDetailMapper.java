package com.ruoyi.wms.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.mybatis.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.wms.domain.bo.ReceiptOrderDetailBo;
import com.ruoyi.wms.domain.entity.ReceiptOrderDetail;
import com.ruoyi.wms.domain.vo.ReceiptOrderDetailVo;

import java.util.List;

/**
 * 入库单详情Mapper接口
 *
 * @author ping
 * @date 2024-07-19
 */
public interface ReceiptOrderDetailMapper extends BaseMapperPlus<ReceiptOrderDetail, ReceiptOrderDetailVo> {

    /**
     * 入库明细跨表分页查询（JOIN wms_receipt_order）
     */
    Page<ReceiptOrderDetailVo> queryDetailPage(Page<ReceiptOrderDetailVo> page, @Param("bo") ReceiptOrderDetailBo bo);
}
