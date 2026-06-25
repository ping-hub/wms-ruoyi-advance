package com.ruoyi.wms.mapper;

import com.ruoyi.wms.domain.entity.WmsEnvMonitorData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 环境监测数据 Mapper
 */
@Mapper
public interface WmsEnvMonitorDataMapper {

    /**
     * 批量插入监测数据
     */
    int insertBatch(@Param("list") List<WmsEnvMonitorData> list);

    /**
     * 查询每个传感器的最新一条数据
     */
    List<Map<String, Object>> selectLatestByDevice();
}
