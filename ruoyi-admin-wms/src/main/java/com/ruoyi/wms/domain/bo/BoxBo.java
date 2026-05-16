package com.ruoyi.wms.domain.bo;

import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import com.ruoyi.common.mybatis.core.domain.BaseEntity;
import com.ruoyi.wms.domain.entity.Box;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = Box.class, reverseConvertGenerate = false)
public class BoxBo extends BaseEntity {

    @NotNull(message = "不能为空", groups = {EditGroup.class})
    private Long id;

    /**
     * 箱码
     */
    private String boxCode;

    /**
     * 箱体名称
     */
    private String boxName;

    /**
     * 箱体状态
     */
    private String boxStatus;

    /**
     * 所属仓库
     */
    private Long warehouseId;

    /**
     * 所属库区
     */
    private Long areaId;

    /**
     * 所属货架
     */
    private Long rackId;

    /**
     * 所属货位
     */
    private Long locationId;

    /**
     * 箱内数量快照
     */
    private Integer itemCount;

    /**
     * 备注
     */
    private String remark;
}
