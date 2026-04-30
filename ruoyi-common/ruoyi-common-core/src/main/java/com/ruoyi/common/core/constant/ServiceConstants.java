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

    }

    /**
     * 出库单状态
     */
    public class ShipmentOrderStatus {
        public static final Integer INVALID = -1;
        public static final Integer PENDING = 0;
        public static final Integer FINISH = 1;
    }

    /**
     * 库存记录操作类型
     */
    public class InventoryHistoryOrderType {
        public static final Integer RECEIPT = 1;
        public static final Integer SHIPMENT = 2;
        public static final Integer MOVEMENT = 3;
        public static final Integer CHECK = 4;
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
        public static final String IN_STOCK = "in_stock";
        public static final String IN_BOX = "in_box";
        public static final String BORROWED = "borrowed";
        public static final String OUTBOUND = "outbound";
        public static final String DISABLED = "disabled";
    }

    /**
     * 单品实例来源类型
     */
    public class ItemInstanceSourceType {
        public static final String RECEIPT = "receipt";
    }

    /**
     * 箱体状态
     */
    public class BoxStatus {
        public static final String IDLE = "idle";
        public static final String PACKED = "packed";
        public static final String DISABLED = "disabled";
    }
}
