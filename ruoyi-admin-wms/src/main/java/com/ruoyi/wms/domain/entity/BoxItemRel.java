package com.ruoyi.wms.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.mybatis.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 箱体与单品关系对象 wms_box_item_rel
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wms_box_item_rel")
public class BoxItemRel extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id")
    private Long id;

    /**
     * 箱体ID
     */
    private Long boxId;

    /**
     * 单品实例ID
     */
    private Long itemInstanceId;
}
