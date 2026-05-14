package com.ruoyi.wms.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("wms_item_qr_code_serial")
public class ItemQrCodeSerial {

    @TableId(value = "item_key")
    private String itemKey;

    private Long currentValue;
}
