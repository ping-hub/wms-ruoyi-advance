package com.ruoyi.wms.domain.bo;

import com.ruoyi.wms.domain.entity.CheckOrderInstance;
import com.ruoyi.common.mybatis.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.github.linpeilie.annotations.AutoMapper;

/**
 * 盘点实例差异明细业务对象 wms_check_order_instance
 *
 * @author ping
 * @date 2026-06-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = CheckOrderInstance.class, reverseConvertGenerate = false)
public class CheckOrderInstanceBo extends BaseEntity {

    private Long id;
    private Long checkOrderId;
    private Long checkOrderDetailId;
    private Long skuId;
    private String instanceCode;
    private String instanceItemName;
    private String resultType;
    private String remark;
}
