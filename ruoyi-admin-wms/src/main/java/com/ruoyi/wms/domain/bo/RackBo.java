package com.ruoyi.wms.domain.bo;

import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import com.ruoyi.common.mybatis.core.domain.BaseEntity;
import com.ruoyi.wms.domain.entity.Rack;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = Rack.class, reverseConvertGenerate = false)
public class RackBo extends BaseEntity {

    @NotNull(message = "不能为空", groups = {EditGroup.class})
    private Long id;

    /**
     * 货架编码
     */
    @NotBlank(message = "货架编码不能为空", groups = {AddGroup.class, EditGroup.class})
    private String rackCode;

    /**
     * 货架名称
     */
    @NotBlank(message = "货架名称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String rackName;

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
     * 货架状态
     */
    private String rackStatus;

    /**
     * 行数
     */
    @NotNull(message = "行数不能为空", groups = {AddGroup.class, EditGroup.class})
    @Min(value = 1, message = "行数必须大于0", groups = {AddGroup.class, EditGroup.class})
    private Integer rowCount;

    /**
     * 列数
     */
    @NotNull(message = "列数不能为空", groups = {AddGroup.class, EditGroup.class})
    @Min(value = 1, message = "列数必须大于0", groups = {AddGroup.class, EditGroup.class})
    private Integer columnCount;

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
     * 排序
     */
    private Long orderNum;

    /**
     * 备注
     */
    private String remark;
}
