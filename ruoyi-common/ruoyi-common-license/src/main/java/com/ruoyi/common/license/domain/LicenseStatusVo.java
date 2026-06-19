package com.ruoyi.common.license.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * License 状态响应对象
 */
@Data
public class LicenseStatusVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 是否已激活且有效 */
    private boolean valid;

    /** 颁发对象 */
    private String issuedTo;

    /** 颁发时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime issuedAt;

    /** 到期时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expiresAt;

    /** 剩余天数（负数表示已过期） */
    private long remainingDays;

    /** License 类型 */
    private String type;

    /** 是否绑定机器码 */
    private boolean machineBound;

    /** 状态描述 */
    private String statusDesc;
}
