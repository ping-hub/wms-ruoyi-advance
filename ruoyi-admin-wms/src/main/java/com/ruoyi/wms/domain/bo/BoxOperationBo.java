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
     * 单品实例ID列表
     */
    @NotEmpty(message = "单品实例不能为空")
    private List<Long> itemInstanceIds;
}
