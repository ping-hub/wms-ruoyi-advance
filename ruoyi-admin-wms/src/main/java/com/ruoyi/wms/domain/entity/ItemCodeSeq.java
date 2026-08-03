package com.ruoyi.wms.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.mybatis.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 器材实例编码序列表 wms_item_code_seq
 * 按 (一级分类编码 + 二级分类编码) 组合维护独立递增序号
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wms_item_code_seq")
public class ItemCodeSeq extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 一级分类编码 */
    private String level1Code;

    /** 二级分类编码 */
    private String level2Code;

    /** 当前序号 */
    private Long currentSeq;
}
