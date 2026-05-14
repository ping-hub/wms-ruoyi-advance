package com.ruoyi.wms.domain.vo;

import com.ruoyi.wms.domain.bo.ItemBo;
import lombok.Data;

import java.util.List;

@Data
public class BatchPrintQrCodeResultVo {

    private String itemKey;

    private Integer qrCodeCount;

    private ItemBo row;

    private List<BatchPrintQrCodeDetailVo> details;
}
