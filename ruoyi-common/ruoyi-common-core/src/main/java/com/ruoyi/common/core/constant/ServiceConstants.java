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
     * 出库单状态
     * 0=草稿, 3=已出库, -1=作废
     * 中间状态（待审批/已审批/已驳回）由 wms_workflow_def 表驱动，不硬编码
     */
    public class ShipmentOrderStatus {
        public static final Integer INVALID = -1;
        public static final Integer DRAFT = 0;
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
     * 0=草稿, 3=已完成, -1=作废
     * 中间状态（待盘点/待复核/已驳回）由 wms_workflow_def 表驱动，不硬编码
     */
    public class CheckOrderStatus {
        public static final Integer INVALID = -1;
        public static final Integer DRAFT = 0;
        public static final Integer FINISH = 3;
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

    /**
     * 借用单状态
     * 0=草稿, 1=借出中, 2=已归还, -1=已作废
     */
    public class BorrowOrderStatus {
        public static final Integer INVALID = -1;
        public static final Integer DRAFT = 0;
        public static final Integer BORROWING = 1;
        public static final Integer RETURNED = 2;
    }

    /**
     * 借用单明细归还状态
     */
    public class BorrowDetailReturnStatus {
        public static final Integer NOT_RETURNED = 0;
        public static final Integer RETURNED = 1;
    }
}
