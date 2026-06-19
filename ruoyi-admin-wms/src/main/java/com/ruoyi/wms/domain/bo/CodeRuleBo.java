package com.ruoyi.wms.domain.bo;

import com.ruoyi.common.core.validate.EditGroup;
import com.ruoyi.wms.domain.entity.CodeRule;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@AutoMapper(target = CodeRule.class, reverseConvertGenerate = false)
public class CodeRuleBo {

    @NotNull(message = "主键不能为空", groups = {EditGroup.class})
    private Long id;

    private String ruleType;
    private String ruleName;
    private String enabled;
    private String prefix;
    private String suffixType;
    private String separator;
    private Integer seqLength;
    private String seqMethod;
    private Long currentSeq;
    private String useItemCodeAsPrefix;
}
