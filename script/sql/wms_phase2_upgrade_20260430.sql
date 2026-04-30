SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- =========================================================
-- WMS 二期升级脚本
-- 覆盖模块:
-- 1. 单品实例模块
-- 2. 入库联动生成单品实例
-- =========================================================

-- ----------------------------
-- 1. 扩展入库单明细
-- ----------------------------
ALTER TABLE `wms_receipt_order_detail`
    ADD COLUMN `generate_item_instance` tinyint(4) DEFAULT 0 COMMENT '是否生成单品实例' AFTER `area_id`,
    ADD COLUMN `generated_instance_quantity` int(11) DEFAULT 0 COMMENT '已生成单品实例数量' AFTER `generate_item_instance`;

UPDATE `wms_receipt_order_detail`
SET
    `generate_item_instance` = COALESCE(`generate_item_instance`, 0),
    `generated_instance_quantity` = COALESCE(`generated_instance_quantity`, 0)
WHERE 1 = 1;

-- ----------------------------
-- 2. 创建单品实例表
-- ----------------------------
DROP TABLE IF EXISTS `wms_item_instance`;
CREATE TABLE `wms_item_instance` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `instance_code` varchar(64) NOT NULL COMMENT '单品码',
    `item_id` bigint(20) NOT NULL COMMENT '物品ID',
    `sku_id` bigint(20) NOT NULL COMMENT '规格ID',
    `instance_status` varchar(32) DEFAULT 'in_stock' COMMENT '单品状态',
    `in_box` tinyint(4) DEFAULT 0 COMMENT '是否在箱内',
    `borrowed` tinyint(4) DEFAULT 0 COMMENT '是否已借出',
    `warehouse_id` bigint(20) DEFAULT NULL COMMENT '所属仓库',
    `area_id` bigint(20) DEFAULT NULL COMMENT '所属库区',
    `rack_id` bigint(20) DEFAULT NULL COMMENT '所属货架',
    `location_id` bigint(20) DEFAULT NULL COMMENT '所属货位',
    `source_type` varchar(32) DEFAULT NULL COMMENT '来源类型',
    `source_order_id` bigint(20) DEFAULT NULL COMMENT '来源单据ID',
    `source_order_no` varchar(64) DEFAULT NULL COMMENT '来源单据号',
    `receipt_order_detail_id` bigint(20) DEFAULT NULL COMMENT '来源入库单明细ID',
    `batch_no` varchar(64) DEFAULT NULL COMMENT '批号',
    `production_date` datetime(3) DEFAULT NULL COMMENT '生产日期',
    `expiration_date` datetime(3) DEFAULT NULL COMMENT '过期日期',
    `remark` varchar(255) DEFAULT NULL COMMENT '备注',
    `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
    `create_time` datetime(3) DEFAULT NULL COMMENT '创建时间',
    `update_by` varchar(64) DEFAULT NULL COMMENT '修改人',
    `update_time` datetime(3) DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_wms_item_instance_code` (`instance_code`) USING BTREE,
    KEY `idx_wms_item_instance_item_id` (`item_id`) USING BTREE,
    KEY `idx_wms_item_instance_sku_id` (`sku_id`) USING BTREE,
    KEY `idx_wms_item_instance_source_order_id` (`source_order_id`) USING BTREE,
    KEY `idx_wms_item_instance_receipt_detail_id` (`receipt_order_detail_id`) USING BTREE,
    KEY `idx_wms_item_instance_location_id` (`location_id`) USING BTREE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '单品实例';

-- ----------------------------
-- 3. 单品实例状态字典
-- ----------------------------
INSERT INTO `sys_dict_type`
(`dict_id`, `dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES
    (1900000000000004001, '单品实例状态', 'wms_item_instance_status', '1', 'admin', NOW(), 'admin', NOW(), '单品实例状态字典');

INSERT INTO `sys_dict_data`
(`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES
    (1900000000000004101, 1, '在库', 'in_stock', 'wms_item_instance_status', '', 'primary', 'Y', '1', 'admin', NOW(), 'admin', NOW(), '已入库且可管理'),
    (1900000000000004102, 2, '在箱', 'in_box', 'wms_item_instance_status', '', 'warning', 'N', '1', 'admin', NOW(), 'admin', NOW(), '已装箱'),
    (1900000000000004103, 3, '借出', 'borrowed', 'wms_item_instance_status', '', 'danger', 'N', '1', 'admin', NOW(), 'admin', NOW(), '已借出'),
    (1900000000000004104, 4, '出库', 'outbound', 'wms_item_instance_status', '', 'info', 'N', '1', 'admin', NOW(), 'admin', NOW(), '已出库'),
    (1900000000000004105, 5, '停用', 'disabled', 'wms_item_instance_status', '', 'default', 'N', '1', 'admin', NOW(), 'admin', NOW(), '已停用');

SET FOREIGN_KEY_CHECKS = 1;
