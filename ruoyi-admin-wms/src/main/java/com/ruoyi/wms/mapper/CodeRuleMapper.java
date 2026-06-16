package com.ruoyi.wms.mapper;

import com.ruoyi.common.mybatis.core.mapper.BaseMapperPlus;
import com.ruoyi.wms.domain.entity.CodeRule;
import com.ruoyi.wms.domain.vo.CodeRuleVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface CodeRuleMapper extends BaseMapperPlus<CodeRule, CodeRuleVo> {

    /**
     * 原子递增序号并返回新值（行锁保证并发安全）
     */
    @Update("UPDATE wms_code_rule SET current_seq = current_seq + #{step}, update_time = CURRENT_TIMESTAMP WHERE id = #{id}")
    int incrementSeq(@Param("id") Long id, @Param("step") long step);
}
