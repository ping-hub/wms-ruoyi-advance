package com.ruoyi.wms.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 入库明细回显的器材实例
 */
@Data
public class ReceiptItemInstanceVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String instanceCode;

    private Long boxId;

    private String boxCode;

    private String productMark;

    private String qualityGrade;

    private String remark;
}
