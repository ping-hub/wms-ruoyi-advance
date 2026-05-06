SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- =========================================================
-- WMS 四阶段实施计划 - 第一阶段后端增量脚本
-- 目标：口径对齐与单据补全
-- =========================================================

ALTER TABLE `wms_item`
    ADD COLUMN `equipment_name` varchar(128) DEFAULT NULL COMMENT '装备名称' AFTER `spec_level`,
    ADD COLUMN `default_quality_grade` varchar(64) DEFAULT NULL COMMENT '默认质量等级' AFTER `equipment_name`,
    ADD COLUMN `product_mark_rule` varchar(128) DEFAULT NULL COMMENT '产品标识规则' AFTER `default_quality_grade`,
    ADD COLUMN `model_text` varchar(255) DEFAULT NULL COMMENT '规格型号文本' AFTER `product_mark_rule`;

ALTER TABLE `wms_item_sku`
    ADD COLUMN `spec_model` varchar(255) DEFAULT NULL COMMENT '规格型号' AFTER `sku_code`,
    ADD COLUMN `default_unit_price` decimal(18,2) DEFAULT NULL COMMENT '默认单价' AFTER `spec_model`;

ALTER TABLE `wms_item_instance`
    ADD COLUMN `product_mark` varchar(128) DEFAULT NULL COMMENT '产品标识' AFTER `receipt_order_detail_id`,
    ADD COLUMN `quality_grade` varchar(64) DEFAULT NULL COMMENT '质量等级' AFTER `product_mark`,
    ADD COLUMN `belong_unit` varchar(128) DEFAULT NULL COMMENT '所在单位' AFTER `quality_grade`,
    ADD COLUMN `source_order_type` varchar(32) DEFAULT NULL COMMENT '来源单据类型' AFTER `belong_unit`;

ALTER TABLE `wms_receipt_order`
    ADD COLUMN `basis_no` varchar(128) DEFAULT NULL COMMENT '调拨根据' AFTER `order_no`,
    ADD COLUMN `dispatch_mode` varchar(64) DEFAULT NULL COMMENT '调拨方式' AFTER `basis_no`,
    ADD COLUMN `notice_org` varchar(128) DEFAULT NULL COMMENT '通知机关' AFTER `dispatch_mode`,
    ADD COLUMN `receive_unit` varchar(128) DEFAULT NULL COMMENT '收物单位' AFTER `notice_org`,
    ADD COLUMN `purchase_date` date DEFAULT NULL COMMENT '采购日期' AFTER `receive_unit`,
    ADD COLUMN `receipt_date` date DEFAULT NULL COMMENT '入库日期' AFTER `purchase_date`,
    ADD COLUMN `purchaser_name` varchar(64) DEFAULT NULL COMMENT '采购配发人' AFTER `receipt_date`,
    ADD COLUMN `acceptor_name` varchar(64) DEFAULT NULL COMMENT '验收人' AFTER `purchaser_name`,
    ADD COLUMN `keeper_name` varchar(64) DEFAULT NULL COMMENT '保管员' AFTER `acceptor_name`;

ALTER TABLE `wms_receipt_order_detail`
    ADD COLUMN `equipment_code` varchar(64) DEFAULT NULL COMMENT '器材编码' AFTER `amount`,
    ADD COLUMN `spec_model` varchar(255) DEFAULT NULL COMMENT '规格型号' AFTER `equipment_code`,
    ADD COLUMN `product_mark` varchar(128) DEFAULT NULL COMMENT '产品标识' AFTER `spec_model`,
    ADD COLUMN `quality_grade` varchar(64) DEFAULT NULL COMMENT '质量等级' AFTER `product_mark`,
    ADD COLUMN `unit_price` decimal(18,2) DEFAULT NULL COMMENT '单价' AFTER `quality_grade`,
    ADD COLUMN `line_amount` decimal(18,2) DEFAULT NULL COMMENT '总价' AFTER `unit_price`;

ALTER TABLE `wms_shipment_order`
    ADD COLUMN `basis_no` varchar(128) DEFAULT NULL COMMENT '调拨根据' AFTER `order_no`,
    ADD COLUMN `dispatch_mode` varchar(64) DEFAULT NULL COMMENT '调拨方式' AFTER `basis_no`,
    ADD COLUMN `notice_org` varchar(128) DEFAULT NULL COMMENT '通知机关' AFTER `dispatch_mode`,
    ADD COLUMN `receive_unit` varchar(128) DEFAULT NULL COMMENT '收物单位' AFTER `notice_org`,
    ADD COLUMN `purchase_date` date DEFAULT NULL COMMENT '采购日期' AFTER `receive_unit`,
    ADD COLUMN `shipment_date` date DEFAULT NULL COMMENT '出库日期' AFTER `purchase_date`,
    ADD COLUMN `purchaser_name` varchar(64) DEFAULT NULL COMMENT '采购配发人' AFTER `shipment_date`,
    ADD COLUMN `acceptor_name` varchar(64) DEFAULT NULL COMMENT '验收人' AFTER `purchaser_name`,
    ADD COLUMN `keeper_name` varchar(64) DEFAULT NULL COMMENT '保管员' AFTER `acceptor_name`;

ALTER TABLE `wms_shipment_order_detail`
    ADD COLUMN `equipment_code` varchar(64) DEFAULT NULL COMMENT '器材编码' AFTER `amount`,
    ADD COLUMN `spec_model` varchar(255) DEFAULT NULL COMMENT '规格型号' AFTER `equipment_code`,
    ADD COLUMN `product_mark` varchar(128) DEFAULT NULL COMMENT '产品标识' AFTER `spec_model`,
    ADD COLUMN `quality_grade` varchar(64) DEFAULT NULL COMMENT '质量等级' AFTER `product_mark`,
    ADD COLUMN `unit_price` decimal(18,2) DEFAULT NULL COMMENT '单价' AFTER `quality_grade`,
    ADD COLUMN `line_amount` decimal(18,2) DEFAULT NULL COMMENT '总价' AFTER `unit_price`;

ALTER TABLE `wms_movement_order`
    ADD COLUMN `movement_type` varchar(32) DEFAULT NULL COMMENT '调拨类型' AFTER `movement_order_no`,
    ADD COLUMN `dispatch_basis` varchar(128) DEFAULT NULL COMMENT '调拨依据' AFTER `movement_type`,
    ADD COLUMN `dispatch_purpose` varchar(255) DEFAULT NULL COMMENT '调拨目的' AFTER `dispatch_basis`,
    ADD COLUMN `support_no` varchar(128) DEFAULT NULL COMMENT '物资保障号' AFTER `dispatch_purpose`,
    ADD COLUMN `dispatch_mode` varchar(64) DEFAULT NULL COMMENT '调拨方式' AFTER `support_no`,
    ADD COLUMN `from_unit` varchar(128) DEFAULT NULL COMMENT '发货单位' AFTER `dispatch_mode`,
    ADD COLUMN `to_unit` varchar(128) DEFAULT NULL COMMENT '收货单位' AFTER `from_unit`,
    ADD COLUMN `from_station` varchar(128) DEFAULT NULL COMMENT '发站' AFTER `to_unit`,
    ADD COLUMN `to_station` varchar(128) DEFAULT NULL COMMENT '到站' AFTER `from_station`,
    ADD COLUMN `from_address` varchar(255) DEFAULT NULL COMMENT '发货地址' AFTER `to_station`,
    ADD COLUMN `to_address` varchar(255) DEFAULT NULL COMMENT '收货地址' AFTER `from_address`,
    ADD COLUMN `contact_address` varchar(255) DEFAULT NULL COMMENT '通信地址' AFTER `to_address`,
    ADD COLUMN `dispatch_date` date DEFAULT NULL COMMENT '调拨日期' AFTER `contact_address`,
    ADD COLUMN `effective_date` date DEFAULT NULL COMMENT '有效日期' AFTER `dispatch_date`,
    ADD COLUMN `issue_date` date DEFAULT NULL COMMENT '发出日期' AFTER `effective_date`,
    ADD COLUMN `from_handler` varchar(64) DEFAULT NULL COMMENT '发货经手人' AFTER `issue_date`,
    ADD COLUMN `to_handler` varchar(64) DEFAULT NULL COMMENT '收货经手人' AFTER `from_handler`;

ALTER TABLE `wms_movement_order_detail`
    ADD COLUMN `equipment_code` varchar(64) DEFAULT NULL COMMENT '器材编码' AFTER `quantity`,
    ADD COLUMN `spec_model` varchar(255) DEFAULT NULL COMMENT '规格型号' AFTER `equipment_code`,
    ADD COLUMN `product_mark` varchar(128) DEFAULT NULL COMMENT '产品标识' AFTER `spec_model`,
    ADD COLUMN `quality_grade` varchar(64) DEFAULT NULL COMMENT '质量等级' AFTER `product_mark`,
    ADD COLUMN `unit_price` decimal(18,2) DEFAULT NULL COMMENT '单价' AFTER `quality_grade`,
    ADD COLUMN `line_amount` decimal(18,2) DEFAULT NULL COMMENT '总价' AFTER `unit_price`;

ALTER TABLE `wms_borrow_record`
    ADD COLUMN `from_unit` varchar(128) DEFAULT NULL COMMENT '发货单位' AFTER `borrower`,
    ADD COLUMN `to_unit` varchar(128) DEFAULT NULL COMMENT '收货单位' AFTER `from_unit`,
    ADD COLUMN `from_person` varchar(64) DEFAULT NULL COMMENT '发货人' AFTER `to_unit`,
    ADD COLUMN `to_person` varchar(64) DEFAULT NULL COMMENT '收货人' AFTER `from_person`,
    ADD COLUMN `doc_date` date DEFAULT NULL COMMENT '单据日期' AFTER `to_person`,
    ADD COLUMN `product_mark` varchar(128) DEFAULT NULL COMMENT '产品标识' AFTER `doc_date`,
    ADD COLUMN `quality_grade` varchar(64) DEFAULT NULL COMMENT '质量等级' AFTER `product_mark`;

CREATE INDEX `idx_wms_item_instance_product_mark` ON `wms_item_instance` (`product_mark`);
CREATE INDEX `idx_wms_receipt_order_receipt_date` ON `wms_receipt_order` (`receipt_date`);
CREATE INDEX `idx_wms_shipment_order_shipment_date` ON `wms_shipment_order` (`shipment_date`);
CREATE INDEX `idx_wms_movement_order_dispatch_date` ON `wms_movement_order` (`dispatch_date`);

SET FOREIGN_KEY_CHECKS = 1;
