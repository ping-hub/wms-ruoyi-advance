package com.ruoyi.wms.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class BorrowWarningStatsVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long borrowingCount;

    private Long overdueCount;
}
