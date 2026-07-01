package com.ruoyi.wms.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class BorrowOrderWarningStatsVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 借出中的借用单数量 */
    private Long borrowingCount;

    /** 即将超时的借用单数量（today ≤ planReturnDate ≤ today+warningDays） */
    private Long warningCount;

    /** 已超期的借用单数量（planReturnDate < today） */
    private Long overdueCount;
}
