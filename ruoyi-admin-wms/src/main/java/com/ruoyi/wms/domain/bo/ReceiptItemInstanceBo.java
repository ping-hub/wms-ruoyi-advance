package com.ruoyi.wms.domain.bo;

import lombok.Data;

/**
 * 入库时录入的器材实例
 */
@Data
public class ReceiptItemInstanceBo {

    /**
     * 器材实例ID
     */
    private Long id;

    /**
     * 器材识别码
     */
    private String instanceCode;

    /**
     * 箱码，可为空
     */
    private String boxCode;

    /**
     * 备注
     */
    private String remark;
}
