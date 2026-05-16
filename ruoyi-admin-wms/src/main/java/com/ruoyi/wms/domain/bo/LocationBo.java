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

import java.math.BigDecimal;

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
     * 行号
     */
    private Integer rowNo;

    /**
     * 列号
     */
    private Integer columnNo;

    /**
     * 长
     */
    private BigDecimal length;

    /**
     * 宽
     */
    private BigDecimal width;

    /**
     * 高
     */
    private BigDecimal height;

    /**
     * 是否占用
     */
    private Integer occupiedFlag;

    /**
     * 排序
     */
    private Long sortNo;

    /**
     * 备注
     */
    private String remark;
}
