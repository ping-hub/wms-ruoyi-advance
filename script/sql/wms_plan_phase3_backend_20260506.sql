SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- =========================================================
-- WMS 四阶段实施计划 - 第三阶段后端增量脚本
-- 目标：流转闭环与追踪增强
-- =========================================================

ALTER TABLE `wms_movement_order_detail`
    ADD COLUMN `item_instance_id` bigint DEFAULT NULL COMMENT '单品实例ID' AFTER `inventory_detail_id`,
    ADD COLUMN `box_id` bigint DEFAULT NULL COMMENT '箱体ID' AFTER `item_instance_id`;

CREATE INDEX `idx_wms_movement_order_detail_item_instance`
    ON `wms_movement_order_detail` (`item_instance_id`);

CREATE INDEX `idx_wms_movement_order_detail_box`
    ON `wms_movement_order_detail` (`box_id`);

CREATE INDEX `idx_wms_borrow_record_item_status`
    ON `wms_borrow_record` (`item_instance_id`, `borrow_status`, `borrow_time`);

SET FOREIGN_KEY_CHECKS = 1;
