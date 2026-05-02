package com.ruoyi.wms.service;

import cn.hutool.core.lang.Assert;
import com.ruoyi.wms.domain.bo.BorrowRecordBo;
import com.ruoyi.wms.domain.vo.BoxTraceVo;
import com.ruoyi.wms.domain.vo.BoxVo;
import com.ruoyi.wms.domain.vo.ItemInstanceVo;
import com.ruoyi.wms.domain.vo.ItemTraceVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TraceService {

    private final ItemInstanceService itemInstanceService;
    private final BoxService boxService;
    private final BorrowRecordService borrowRecordService;
    private final ShipmentOrderDetailService shipmentOrderDetailService;

    public ItemTraceVo queryItemTraceByCode(String instanceCode) {
        ItemInstanceVo itemInstance = itemInstanceService.queryByCode(instanceCode);
        Assert.notNull(itemInstance, "单品实例不存在");
        BorrowRecordBo borrowRecordBo = new BorrowRecordBo();
        borrowRecordBo.setItemInstanceId(itemInstance.getId());
        ItemTraceVo traceVo = new ItemTraceVo();
        traceVo.setItemInstance(itemInstance);
        traceVo.setCurrentBox(boxService.queryByItemInstanceId(itemInstance.getId()));
        traceVo.setCurrentBorrowRecord(borrowRecordService.queryCurrentByItemInstanceId(itemInstance.getId()));
        traceVo.setBorrowRecords(borrowRecordService.queryList(borrowRecordBo));
        traceVo.setShipmentDetails(shipmentOrderDetailService.queryByItemInstanceId(itemInstance.getId()));
        return traceVo;
    }

    public BoxTraceVo queryBoxTraceByCode(String boxCode) {
        BoxVo box = boxService.queryByCode(boxCode);
        Assert.notNull(box, "箱体不存在");
        BoxTraceVo traceVo = new BoxTraceVo();
        traceVo.setBox(box);
        traceVo.setShipmentDetails(shipmentOrderDetailService.queryByBoxId(box.getId()));
        return traceVo;
    }
}
