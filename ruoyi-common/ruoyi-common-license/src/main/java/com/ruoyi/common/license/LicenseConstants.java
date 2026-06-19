package com.ruoyi.common.license;

/**
 * License 常量类
 * RSA 公钥硬编码在此，不可通过外部配置修改
 */
public final class LicenseConstants {

    private LicenseConstants() {}

    /**
     * RSA 公钥（Base64 编码，去掉头尾标识）
     * 与颁发工具使用的私钥配对
     */
    public static final String PUBLIC_KEY =
        "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAus5mx3wWhPHbp5/OzLW1"
        + "+ljj4l6JkEVQB/NMeGGCv2O3S0ASkSZ/7BJxiwXyrFxpY9QnNeLVW6WlkCSgZPq3"
        + "z7TyTHNfSL9kgS9H40gvh1Soz74yOMVRRom6UoBNjwfTwyaTuwswE/pcyeUX39OM"
        + "APwvjOKVeO3dsL/wE9EFHpjLohzE895T+qjPl9JP3V57iFwmz13bECJ3supILk1Q"
        + "uk2a7a+cUKL8L2quBybAg+4iIyNjac82ELqil/B+bq490LBZ4LtkX2bVksUteiyI"
        + "T6tj8p3+GTkv0C5qwyvudc2jedWz41gwvgv0WUqGyUfjHPAgYG7O68yLwhbLHStw"
        + "fwIDAQAB";

    /** License 校验失败的错误码 */
    public static final int CODE_LICENSE_INVALID = 501;

    /** License 机器码不匹配的错误码 */
    public static final int CODE_MACHINE_MISMATCH = 502;

    /** License 状态消息 */
    public static final String MSG_LICENSE_INVALID = "License未激活或已过期，请激活有效的License";
    public static final String MSG_MACHINE_MISMATCH = "License机器码不匹配，请联系管理员重新颁发";

    /** Redis key：License 状态 */
    public static final String REDIS_LICENSE_STATUS = "license:status";

    /** Redis Pub/Sub channel：License 刷新通知 */
    public static final String REDIS_LICENSE_CHANNEL = "license:refresh";

    /** 本地机器码持久化文件名 */
    public static final String MACHINE_ID_FILE = ".machine-id";
}
