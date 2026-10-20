package com.ruoyi.wms.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.mybatis.core.mapper.BaseMapperPlus;
import com.ruoyi.wms.domain.bo.EnvDataQueryBo;
import com.ruoyi.wms.domain.entity.WmsEnvData;
import com.ruoyi.wms.domain.vo.EnvDataVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 温湿度监控-温湿度数据 Mapper
 */
public interface WmsEnvDataMapper extends BaseMapperPlus<WmsEnvData, EnvDataVo> {

    /**
     * 批量插入
     */
    int insertBatch(@Param("list") List<WmsEnvData> list);

    /**
     * 查询每个设备的最新一条数据（关联设备配置表）
     */
    List<EnvDataVo> selectLatestList();

    /**
     * 历史数据分页查询（关联设备配置表）
     */
    Page<EnvDataVo> selectDataPage(Page<EnvDataVo> page, @Param("query") EnvDataQueryBo query);
}
