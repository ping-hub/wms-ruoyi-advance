package com.ruoyi.common.license.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * License 信息结构体
 * 对应 License 文件中的 JSON 内容
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LicenseInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 颁发对象 */
    private String issuedTo;

    /** 颁发时间 */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime issuedAt;

    /** 到期时间 */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime expiresAt;

    /** 绑定的机器码（为空表示不绑定） */
    private String machineCode;

    /** License 类型 */
    private String type;
}
