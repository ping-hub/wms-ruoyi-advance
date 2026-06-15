package com.ruoyi.wms.domain.vo;

import com.ruoyi.wms.domain.entity.CheckOrderInstance;
import lombok.Data;
import io.github.linpeilie.annotations.AutoMapper;

import java.io.Serial;
import java.io.Serializable;

/**
 * 盘点实例差异明细视图对象 wms_check_order_instance
 *
 * @author ping
 * @date 2026-06-12
 */
@Data
@AutoMapper(target = CheckOrderInstance.class)
public class CheckOrderInstanceVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long checkOrderId;
    private Long checkOrderDetailId;
    private Long skuId;
    private String instanceCode;
    private String instanceItemName;
    private String resultType;
    private String remark;
}
