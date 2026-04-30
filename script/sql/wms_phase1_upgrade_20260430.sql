SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- =========================================================
-- WMS 一期升级脚本
-- 覆盖模块:
-- 1. 空间结构模块: 货架、货位
-- 2. 物品定义扩展模块: item 扩展字段
-- 执行方式:
-- 1. 建议先备份数据库
-- 2. 在目标库中直接执行本脚本
-- =========================================================

-- ----------------------------
-- 1. 物品定义扩展
-- ----------------------------
ALTER TABLE `wms_item`
    ADD COLUMN `item_type` varchar(32) DEFAULT NULL COMMENT '物品类型' AFTER `item_brand`,
    ADD COLUMN `tracking_mode` varchar(32) DEFAULT NULL COMMENT '追踪模式' AFTER `item_type`,
    ADD COLUMN `allow_box` tinyint(4) DEFAULT 0 COMMENT '是否允许装箱' AFTER `tracking_mode`,
    ADD COLUMN `spec_level` varchar(32) DEFAULT NULL COMMENT '规格等级' AFTER `allow_box`;

ALTER TABLE `wms_item`
    ADD UNIQUE INDEX `uk_wms_item_code` (`item_code`);

UPDATE `wms_item`
SET
    `item_type` = COALESCE(`item_type`, 'normal'),
    `tracking_mode` = COALESCE(`tracking_mode`, 'batch'),
    `allow_box` = COALESCE(`allow_box`, 0),
    `spec_level` = COALESCE(`spec_level`, 'single')
WHERE 1 = 1;

-- ----------------------------
-- 2. 货架表
-- ----------------------------
DROP TABLE IF EXISTS `wms_rack`;
CREATE TABLE `wms_rack` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `rack_code` varchar(32) DEFAULT NULL COMMENT '货架编码',
    `rack_name` varchar(60) NOT NULL COMMENT '货架名称',
    `warehouse_id` bigint(20) NOT NULL COMMENT '所属仓库',
    `area_id` bigint(20) NOT NULL COMMENT '所属库区',
    `rack_status` varchar(32) DEFAULT 'enabled' COMMENT '货架状态',
    `rack_type` varchar(32) DEFAULT 'standard' COMMENT '货架类型',
    `remark` varchar(255) DEFAULT NULL COMMENT '备注',
    `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
    `create_time` datetime(3) DEFAULT NULL COMMENT '创建时间',
    `update_by` varchar(64) DEFAULT NULL COMMENT '修改人',
    `update_time` datetime(3) DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_wms_rack_code` (`rack_code`) USING BTREE,
    KEY `idx_wms_rack_warehouse_id` (`warehouse_id`) USING BTREE,
    KEY `idx_wms_rack_area_id` (`area_id`) USING BTREE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '货架';

-- ----------------------------
-- 3. 货位表
-- ----------------------------
DROP TABLE IF EXISTS `wms_location`;
CREATE TABLE `wms_location` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `location_code` varchar(32) DEFAULT NULL COMMENT '货位编码',
    `location_name` varchar(60) NOT NULL COMMENT '货位名称',
    `warehouse_id` bigint(20) NOT NULL COMMENT '所属仓库',
    `area_id` bigint(20) NOT NULL COMMENT '所属库区',
    `rack_id` bigint(20) NOT NULL COMMENT '所属货架',
    `location_status` varchar(32) DEFAULT 'enabled' COMMENT '货位状态',
    `location_type` varchar(32) DEFAULT 'normal' COMMENT '货位类型',
    `remark` varchar(255) DEFAULT NULL COMMENT '备注',
    `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
    `create_time` datetime(3) DEFAULT NULL COMMENT '创建时间',
    `update_by` varchar(64) DEFAULT NULL COMMENT '修改人',
    `update_time` datetime(3) DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_wms_location_code` (`location_code`) USING BTREE,
    KEY `idx_wms_location_warehouse_id` (`warehouse_id`) USING BTREE,
    KEY `idx_wms_location_area_id` (`area_id`) USING BTREE,
    KEY `idx_wms_location_rack_id` (`rack_id`) USING BTREE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '货位';

-- ----------------------------
-- 4. 初始化示例数据
-- 说明:
-- 仅在仓库/库区样例数据存在时执行
-- 当前脚本假定原始演示库中存在以下数据:
-- 仓库: 1828364740028174337
-- 库区: 1829397566185992193, 1829397707726974978
-- ----------------------------
INSERT INTO `wms_rack` (
    `id`, `rack_code`, `rack_name`, `warehouse_id`, `area_id`,
    `rack_status`, `rack_type`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`
) VALUES
    (1900000000000000001, 'RACK-A1-01', 'A1一号货架', 1828364740028174337, 1829397566185992193, 'enabled', 'standard', 'A1库区标准货架', 'admin', NOW(3), 'admin', NOW(3)),
    (1900000000000000002, 'RACK-B1-01', 'B1一号货架', 1828364740028174337, 1829397707726974978, 'enabled', 'standard', 'B1库区标准货架', 'admin', NOW(3), 'admin', NOW(3));

INSERT INTO `wms_location` (
    `id`, `location_code`, `location_name`, `warehouse_id`, `area_id`, `rack_id`,
    `location_status`, `location_type`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`
) VALUES
    (1900000000000000101, 'LOC-A1-R1-01', 'A1-R1-01', 1828364740028174337, 1829397566185992193, 1900000000000000001, 'enabled', 'normal', 'A1库区一号货架一层货位', 'admin', NOW(3), 'admin', NOW(3)),
    (1900000000000000102, 'LOC-A1-R1-02', 'A1-R1-02', 1828364740028174337, 1829397566185992193, 1900000000000000001, 'enabled', 'normal', 'A1库区一号货架二层货位', 'admin', NOW(3), 'admin', NOW(3)),
    (1900000000000000103, 'LOC-B1-R1-01', 'B1-R1-01', 1828364740028174337, 1829397707726974978, 1900000000000000002, 'enabled', 'normal', 'B1库区一号货架一层货位', 'admin', NOW(3), 'admin', NOW(3));

SET FOREIGN_KEY_CHECKS = 1;
