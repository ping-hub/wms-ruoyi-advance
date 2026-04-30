package com.ruoyi.wms.domain.bo;

import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import com.ruoyi.common.mybatis.core.domain.BaseEntity;
import com.ruoyi.wms.domain.entity.Location;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = Location.class, reverseConvertGenerate = false)
public class LocationBo extends BaseEntity {

    @NotNull(message = "不能为空", groups = {EditGroup.class})
    private Long id;

    /**
     * 货位编码
     */
    private String locationCode;

    /**
     * 货位名称
     */
    @NotBlank(message = "货位名称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String locationName;

    /**
     * 所属仓库
     */
    @NotNull(message = "所属仓库不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long warehouseId;

    /**
     * 所属库区
     */
    @NotNull(message = "所属库区不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long areaId;

    /**
     * 所属货架
     */
    @NotNull(message = "所属货架不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long rackId;

    /**
     * 货位状态
     */
    private String locationStatus;

    /**
     * 货位类型
     */
    private String locationType;

    /**
     * 备注
     */
    private String remark;
}
