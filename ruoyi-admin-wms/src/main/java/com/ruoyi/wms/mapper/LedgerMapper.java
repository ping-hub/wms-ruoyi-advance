package com.ruoyi.wms.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.wms.domain.bo.LedgerBo;
import com.ruoyi.wms.domain.vo.LedgerVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LedgerMapper {

    Page<LedgerVo> selectPageByBo(Page<LedgerVo> page, @Param("bo") LedgerBo bo);

    List<LedgerVo> selectListByBo(@Param("bo") LedgerBo bo);
}
