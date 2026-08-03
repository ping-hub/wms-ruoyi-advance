package com.ruoyi.wms.mapper;

import com.ruoyi.common.mybatis.core.mapper.BaseMapperPlus;
import com.ruoyi.wms.domain.entity.ItemCodeSeq;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 器材实例编码序号 Mapper
 */
public interface ItemCodeSeqMapper extends BaseMapperPlus<ItemCodeSeq, Void> {

    /**
     * 查询并锁定行（FOR UPDATE），行不存在时返回 null
     */
    @Select("SELECT current_seq FROM wms_item_code_seq WHERE level1_code = #{level1Code} AND level2_code = #{level2Code} FOR UPDATE")
    Long selectForUpdate(@Param("level1Code") String level1Code,
                         @Param("level2Code") String level2Code);

    /**
     * 插入新行
     */
    @Insert("INSERT INTO wms_item_code_seq (level1_code, level2_code, current_seq, create_time, update_time) VALUES (#{level1Code}, #{level2Code}, #{step}, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)")
    int insertSeq(@Param("level1Code") String level1Code,
                  @Param("level2Code") String level2Code,
                  @Param("step") long step);

    /**
     * 原子递增并返回新值
     */
    @Update("UPDATE wms_item_code_seq SET current_seq = current_seq + #{step}, update_time = CURRENT_TIMESTAMP WHERE level1_code = #{level1Code} AND level2_code = #{level2Code}")
    int incrementSeq(@Param("level1Code") String level1Code,
                     @Param("level2Code") String level2Code,
                     @Param("step") long step);
}
