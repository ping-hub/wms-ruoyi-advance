SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- =========================================================
-- WMS 四阶段实施计划 - 第二阶段后端增量脚本
-- 目标：总账可用与台账落地
-- =========================================================

ALTER TABLE `wms_inventory_detail`
    ADD COLUMN `equipment_code` varchar(64) DEFAULT NULL COMMENT '器材编码' AFTER `amount`,
    ADD COLUMN `spec_model` varchar(255) DEFAULT NULL COMMENT '规格型号' AFTER `equipment_code`,
    ADD COLUMN `product_mark` varchar(128) DEFAULT NULL COMMENT '产品标识' AFTER `spec_model`,
    ADD COLUMN `quality_grade` varchar(64) DEFAULT NULL COMMENT '质量等级' AFTER `product_mark`,
    ADD COLUMN `unit_price` decimal(18,2) DEFAULT NULL COMMENT '单价' AFTER `quality_grade`,
    ADD COLUMN `line_amount` decimal(18,2) DEFAULT NULL COMMENT '行金额' AFTER `unit_price`,
    ADD COLUMN `belong_unit` varchar(128) DEFAULT NULL COMMENT '所在单位' AFTER `line_amount`;

ALTER TABLE `wms_inventory_history`
    ADD COLUMN `equipment_code` varchar(64) DEFAULT NULL COMMENT '器材编码' AFTER `amount`,
    ADD COLUMN `spec_model` varchar(255) DEFAULT NULL COMMENT '规格型号' AFTER `equipment_code`,
    ADD COLUMN `product_mark` varchar(128) DEFAULT NULL COMMENT '产品标识' AFTER `spec_model`,
    ADD COLUMN `quality_grade` varchar(64) DEFAULT NULL COMMENT '质量等级' AFTER `product_mark`,
    ADD COLUMN `unit_price` decimal(18,2) DEFAULT NULL COMMENT '单价' AFTER `quality_grade`,
    ADD COLUMN `line_amount` decimal(18,2) DEFAULT NULL COMMENT '行金额' AFTER `unit_price`,
    ADD COLUMN `belong_unit` varchar(128) DEFAULT NULL COMMENT '所在单位' AFTER `line_amount`;

CREATE INDEX `idx_wms_inventory_detail_ledger_core`
    ON `wms_inventory_detail` (`equipment_code`, `product_mark`, `quality_grade`, `belong_unit`);

CREATE INDEX `idx_wms_inventory_detail_spec_time`
    ON `wms_inventory_detail` (`spec_model`(100), `create_time`);

CREATE INDEX `idx_wms_inventory_history_ledger_core`
    ON `wms_inventory_history` (`equipment_code`, `product_mark`, `quality_grade`, `belong_unit`);

CREATE INDEX `idx_wms_inventory_history_time`
    ON `wms_inventory_history` (`create_time`, `order_type`);

SET FOREIGN_KEY_CHECKS = 1;
