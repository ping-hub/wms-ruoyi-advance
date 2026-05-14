package com.ruoyi.wms.domain.vo;

import lombok.Data;

@Data
public class BatchPrintQrCodeDetailVo {

    private Long serialValue;

    private String instanceCode;

    private String qrCodeValue;

    private String qrContent;
}
