package com.ruoyi.wms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.ruoyi.wms.domain.bo.LedgerBo;
import com.ruoyi.wms.domain.vo.LedgerVo;
import com.ruoyi.wms.mapper.LedgerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class LedgerService {

    private final LedgerMapper ledgerMapper;

    public TableDataInfo<LedgerVo> queryPageList(LedgerBo bo, PageQuery pageQuery) {
        Page<LedgerVo> result = ledgerMapper.selectPageByBo(pageQuery.build(), bo);
        return TableDataInfo.build(result);
    }

    public List<LedgerVo> queryList(LedgerBo bo) {
        return ledgerMapper.selectListByBo(bo);
    }
}
