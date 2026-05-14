package com.ruoyi.wms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.wms.domain.entity.ItemQrCodeSerial;
import org.apache.ibatis.annotations.Param;

public interface ItemQrCodeSerialMapper extends BaseMapper<ItemQrCodeSerial> {

    int initIfAbsent(@Param("itemKey") String itemKey);

    ItemQrCodeSerial selectByItemKeyForUpdate(@Param("itemKey") String itemKey);

    int updateCurrentValue(@Param("itemKey") String itemKey, @Param("currentValue") Long currentValue);
}
