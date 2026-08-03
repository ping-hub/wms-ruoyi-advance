package com.ruoyi.wms.domain.bo;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BatchPrintQrCodeBo {

    @Valid
    @NotNull(message = "器材信息不能为空")
    private ItemBo row;

    @NotNull(message = "打印规格不能为空")
    private Long skuId;

    @NotNull(message = "二维码个数不能为空")
    @Min(value = 1, message = "二维码个数必须大于0")
    private Integer qrCodeCount;

    @NotBlank(message = "仓库编码不能为空")
    private String warehouseCode;
}
