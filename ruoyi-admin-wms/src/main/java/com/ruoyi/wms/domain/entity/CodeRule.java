package com.ruoyi.wms.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.mybatis.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 编码规则对象 wms_code_rule
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wms_code_rule")
public class CodeRule extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id")
    private Long id;

    /** 编码类型标识 */
    private String ruleType;

    /** 编码类型名称 */
    private String ruleName;

    /** 是否启用（0启用 1关闭） */
    private String enabled;

    /** 前缀 */
    private String prefix;

    /** 后缀类型（ymd/ym/y） */
    private String suffixType;

    /** 分隔符 */
    private String separator;

    /** 序号位数 */
    private Integer seqLength;

    /** 序号生成方式（sequential/random） */
    private String seqMethod;

    /** 当前序号 */
    private Long currentSeq;
}
