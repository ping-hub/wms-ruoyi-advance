package com.ruoyi.wms.domain.vo;

import com.ruoyi.wms.domain.entity.CodeRule;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@AutoMapper(target = CodeRule.class)
public class CodeRuleVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

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
}
