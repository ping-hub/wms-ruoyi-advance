package com.ruoyi.common.core.constant;

public class ServiceConstants {
    /**
     * 入库单状态
     */
    public class ReceiptOrderStatus {
        public static final Integer INVALID = -1;
        public static final Integer PENDING = 0;
        public static final Integer FINISH = 1;
    }

    /**
     * 入库记录类型
     */
    public class InventoryDetailType{
        public static final Integer RECEIPT = 1;
        public static final Integer MOVEMENT = 2;
        public static final Integer CHECK = 3;
        public static final Integer ADJUST = 4;

    }

    /**
     * 出库单状态（审批流程）
     * 0=草稿, 1=待审批, 2=已审批, 3=已出库, -1=作废, -2=已驳回
     */
    public class ShipmentOrderStatus {
        public static final Integer REJECTED = -2;
        public static final Integer INVALID = -1;
        public static final Integer DRAFT = 0;
        public static final Integer PENDING_APPROVAL = 1;
        public static final Integer APPROVED = 2;
        public static final Integer FINISH = 3;
    }

    /**
     * 库存记录操作类型
     */
    public class InventoryHistoryOrderType {
        public static final Integer RECEIPT = 1;
        public static final Integer SHIPMENT = 2;
        public static final Integer MOVEMENT = 3;
        public static final Integer CHECK = 4;
        public static final Integer BORROW = 5;
        public static final Integer RETURN = 6;
        public static final Integer ADJUST = 7;
    }

    /**
     * 移库单状态
     */
    public class MovementOrderStatus {
        public static final Integer INVALID = -1;
        public static final Integer PENDING = 0;
        public static final Integer FINISH = 1;
    }

    /**
     * 盘库单状态
     */
    public class CheckOrderStatus {
        public static final Integer INVALID = -1;
        public static final Integer PENDING = 0;
        public static final Integer FINISH = 1;
    }

    /**
     * 单品实例状态
     */
    public class ItemInstanceStatus {
        public static final String PENDING_RECEIPT = "待入库";
        public static final String IN_STOCK = "在库";
        public static final String OUTBOUND = "出库";
        public static final String LOSS = "盘亏";
        public static final String BORROWED = "借出";
        public static final String SCRAPPED = "报废";
        public static final String TRANSFERRED = "已调拨";
    }

    /**
     * 入库单类型
     */
    public class ReceiptOrderType {
        public static final String PURCHASE = "采购入库";
        public static final String RETURN = "归还入库";
        public static final String MOVEMENT = "调拨入库";
    }

    /**
     * 出库单类型
     */
    public class ShipmentOrderType {
        public static final String BORROW = "借用出库";
        public static final String MOVEMENT = "调拨出库";
        public static final String SCRAP = "报废出库";
    }

    /**
     * 单品实例来源类型
     */
    public class ItemInstanceSourceType {
        public static final String RECEIPT = "入库单";
        public static final String SHIPMENT = "出库单";
        public static final String MOVEMENT = "调拨单";
        public static final String BORROW = "借还单";
        public static final String MANUAL = "批量打印";
        public static final String CHECK = "盘点单";
    }


    /**
     * 箱体状态
     */
    public class BoxStatus {
        public static final String IDLE = "idle";
        public static final String PACKED = "packed";
        public static final String OUTBOUND = "outbound";
        public static final String DISABLED = "disabled";
    }

    /**
     * 借还状态
     */
    public class BorrowStatus {
        public static final String BORROWED = "borrowed";
        public static final String RETURNED = "returned";
    }
}
