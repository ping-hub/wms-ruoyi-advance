package com.ruoyi.wms.service;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.ruoyi.wms.domain.entity.ItemQrCodeSerial;
import com.ruoyi.wms.mapper.ItemQrCodeSerialMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemQrCodeSerialService {

    private static final String ITEM_KEY_SEPARATOR = "-";

    private final ItemQrCodeSerialMapper itemQrCodeSerialMapper;

    public List<Long> allocateSerialValues(String itemName, String specName, int count) {
        Assert.isTrue(count > 0, "二维码个数必须大于0");
        String itemKey = buildItemKey(itemName, specName);
        itemQrCodeSerialMapper.initIfAbsent(itemKey);
        ItemQrCodeSerial serial = itemQrCodeSerialMapper.selectByItemKeyForUpdate(itemKey);
        Assert.notNull(serial, "二维码序列不存在");
        long startValue = serial.getCurrentValue() == null ? 1_000_000_001L : serial.getCurrentValue();
        long endValue = startValue + count - 1;
        itemQrCodeSerialMapper.updateCurrentValue(itemKey, endValue + 1);
        List<Long> values = new ArrayList<>(count);
        for (long value = startValue; value <= endValue; value++) {
            values.add(value);
        }
        return values;
    }

    public String buildItemKey(String itemName, String specName) {
        if (StrUtil.isBlank(specName)) {
            return itemName;
        }
        return itemName + ITEM_KEY_SEPARATOR + specName;
    }
}
