SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- =========================================================
-- WMS 四期升级脚本
-- 覆盖模块:
-- 1. 借还记录模块
-- 2. 借还状态字典
-- 3. 借还接口权限
-- =========================================================

-- ----------------------------
-- 1. 借还记录表
-- ----------------------------
DROP TABLE IF EXISTS `wms_borrow_record`;
CREATE TABLE `wms_borrow_record` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `item_instance_id` bigint(20) NOT NULL COMMENT '单品实例ID',
    `borrow_status` varchar(32) NOT NULL COMMENT '借还状态',
    `borrower` varchar(64) NOT NULL COMMENT '借用人',
    `borrow_time` datetime(3) NOT NULL COMMENT '借用时间',
    `return_time` datetime(3) DEFAULT NULL COMMENT '归还时间',
    `borrow_remark` varchar(255) DEFAULT NULL COMMENT '借用备注',
    `return_remark` varchar(255) DEFAULT NULL COMMENT '归还备注',
    `original_warehouse_id` bigint(20) DEFAULT NULL COMMENT '借出前仓库',
    `original_area_id` bigint(20) DEFAULT NULL COMMENT '借出前库区',
    `original_rack_id` bigint(20) DEFAULT NULL COMMENT '借出前货架',
    `original_location_id` bigint(20) DEFAULT NULL COMMENT '借出前货位',
    `returned_warehouse_id` bigint(20) DEFAULT NULL COMMENT '归还后仓库',
    `returned_area_id` bigint(20) DEFAULT NULL COMMENT '归还后库区',
    `returned_rack_id` bigint(20) DEFAULT NULL COMMENT '归还后货架',
    `returned_location_id` bigint(20) DEFAULT NULL COMMENT '归还后货位',
    `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
    `create_time` datetime(3) DEFAULT NULL COMMENT '创建时间',
    `update_by` varchar(64) DEFAULT NULL COMMENT '修改人',
    `update_time` datetime(3) DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_wms_borrow_record_item_instance_id` (`item_instance_id`) USING BTREE,
    KEY `idx_wms_borrow_record_status` (`borrow_status`) USING BTREE,
    KEY `idx_wms_borrow_record_borrow_time` (`borrow_time`) USING BTREE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '借还记录';

-- ----------------------------
-- 2. 借还状态字典
-- ----------------------------
INSERT INTO `sys_dict_type`
(`dict_id`, `dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES
    (1900000000000006001, '借还状态', 'wms_borrow_status', '1', 'admin', NOW(), 'admin', NOW(), '借还状态字典');

INSERT INTO `sys_dict_data`
(`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES
    (1900000000000006101, 1, '借出中', 'borrowed', 'wms_borrow_status', '', 'warning', 'Y', '1', 'admin', NOW(), 'admin', NOW(), '当前已借出'),
    (1900000000000006102, 2, '已归还', 'returned', 'wms_borrow_status', '', 'success', 'N', '1', 'admin', NOW(), 'admin', NOW(), '当前已归还');

-- ----------------------------
-- 3. 借还菜单与权限
-- 父菜单使用现有“业务作业”菜单:
-- 1808758090157985795
-- ----------------------------
INSERT INTO `sys_menu`
(`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query_param`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES
    (1900000000000006201, '借还管理', 1808758090157985795, 6, 'borrow-record', 'wms/business/borrow-record/index', NULL, 0, 0, 'C', '1', '1', 'wms:borrowRecord:list', 'documentation', 'admin', NOW(), 'admin', NOW(), '借还管理菜单'),
    (1900000000000006202, '借还查询', 1900000000000006201, 1, '#', NULL, NULL, 0, 0, 'F', '1', '1', 'wms:borrowRecord:list', '#', 'admin', NOW(), 'admin', NOW(), '借还查询权限'),
    (1900000000000006203, '借还编辑', 1900000000000006201, 2, '#', NULL, NULL, 0, 0, 'F', '1', '1', 'wms:borrowRecord:edit', '#', 'admin', NOW(), 'admin', NOW(), '借还编辑权限');

SET FOREIGN_KEY_CHECKS = 1;
