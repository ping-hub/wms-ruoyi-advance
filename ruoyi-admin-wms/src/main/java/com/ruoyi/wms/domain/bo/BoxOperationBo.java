package com.ruoyi.wms.domain.bo;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class BoxOperationBo {

    /**
     * 箱体ID
     */
    @NotNull(message = "箱体不能为空")
    private Long boxId;

    /**
     * 器材实例编码列表
     */
    @NotEmpty(message = "器材实例编码不能为空")
    private List<String> instanceCodes;
}
