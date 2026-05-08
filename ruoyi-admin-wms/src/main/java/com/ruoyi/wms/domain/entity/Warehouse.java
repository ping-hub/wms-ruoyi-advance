package com.ruoyi.wms.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.mybatis.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wms_warehouse")
public class Warehouse extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 编号
     */
    private String warehouseCode;

    /**
     * 名称
     */
    private String warehouseName;
    /**
     * 排序
     */
    private Long orderNum;

    /**
     * 启用状态
     */
    private String status;

    /**
     * 仓库类型
     */
    private String warehouseType;

    /**
     * 地址
     */
    private String address;

    /**
     * 负责人
     */
    private String managerName;

    /**
     * 负责人电话
     */
    private String managerPhone;

    /**
     * 备注
     */
    private String remark;


}
