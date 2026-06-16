package com.ruoyi.wms.domain.bo;

import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import com.ruoyi.common.mybatis.core.domain.BaseEntity;
import com.ruoyi.wms.domain.entity.ReceiptOrder;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 入库单业务对象 wms_receipt_order
 *
 * @author ping
 * @date 2024-07-19
 */

@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = ReceiptOrder.class, reverseConvertGenerate = false)
public class ReceiptOrderBo extends BaseEntity {

    /**
     *
     */
    @NotNull(message = "不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 入库单号
     */
    @NotBlank(message = "入库单号不能为空", groups = { EditGroup.class })
    private String receiptOrderNo;

    /**
     * 入库类型
     */
    @NotBlank(message = "入库类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String receiptOrderType;

    /**
     * 调拨根据
     */
    private String basisNo;

    /**
     * 调拨方式
     */
    private String dispatchMode;

    /**
     * 通知机关
     */
    private String noticeOrg;

    /**
     * 收物单位
     */
    private String receiveUnit;

    /**
     * 采购日期
     */
    private LocalDate purchaseDate;

    /**
     * 入库日期
     */
    private LocalDate receiptDate;

    /**
     * 器材总数
     */
    private BigDecimal totalQuantity;

    /**
     * 订单金额
     */
    private BigDecimal payableAmount;

    /**
     * 入库状态
     */
    private Integer receiptOrderStatus;

    /**
     * 仓库id
     */
    @NotNull(message = "仓库不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long warehouseId;

    /**
     * 库区id
     */
    private Long areaId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 器材信息
     */
    private List<ReceiptOrderDetailBo> details;
}
