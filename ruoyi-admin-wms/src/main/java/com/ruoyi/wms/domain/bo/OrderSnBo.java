package com.ruoyi.wms.domain.bo;

import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import com.ruoyi.common.mybatis.core.domain.BaseEntity;
import com.ruoyi.wms.domain.entity.OrderSn;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 单据SN关联业务对象
 *
 * @author ruoyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = OrderSn.class, reverseConvertGenerate = false)
public class OrderSnBo extends BaseEntity {

    /**
     * 主键ID
     */
    @NotNull(message = "不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 主键集合
     */
    private List<Long> ids;

    /**
     * 单据类型: 1-入库 2-出库 3-移库 4-盘点
     */
    @NotNull(message = "单据类型不能为空", groups = { AddGroup.class })
    private Integer orderType;

    /**
     * 单据ID
     */
    @NotNull(message = "单据ID不能为空", groups = { AddGroup.class })
    private Long orderId;

    /**
     * 单据明细ID
     */
    @NotNull(message = "单据明细ID不能为空", groups = { AddGroup.class })
    private Long orderDetailId;

    /**
     * SN ID
     */
    @NotNull(message = "SN ID不能为空", groups = { AddGroup.class })
    private Long snId;

    /**
     * SN码
     */
    private String snCode;

}
