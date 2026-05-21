package com.ruoyi.wms.domain.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ItemInstanceImportVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "单品码")
    private String instanceCode;

    @ExcelProperty(value = "物品ID")
    private Long itemId;

    @ExcelProperty(value = "规格ID")
    private Long skuId;

    @ExcelProperty(value = "箱码")
    private String boxCode;

    @ExcelProperty(value = "备注")
    private String remark;
}
