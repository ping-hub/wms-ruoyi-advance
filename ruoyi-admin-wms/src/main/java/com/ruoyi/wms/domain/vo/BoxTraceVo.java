package com.ruoyi.wms.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class BoxTraceVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private BoxVo box;
    private List<ShipmentOrderDetailVo> shipmentDetails;
    private LocationStockVo currentLocation;
    private List<MovementOrderDetailVo> movementDetails;
}
