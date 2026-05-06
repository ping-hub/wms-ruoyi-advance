package com.ruoyi.wms.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class ItemTraceVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private ItemInstanceVo itemInstance;
    private BoxVo currentBox;
    private LocationStockVo currentLocation;
    private BorrowRecordVo currentBorrowRecord;
    private List<BorrowRecordVo> borrowRecords;
    private List<ShipmentOrderDetailVo> shipmentDetails;
    private List<MovementOrderDetailVo> movementDetails;
}
