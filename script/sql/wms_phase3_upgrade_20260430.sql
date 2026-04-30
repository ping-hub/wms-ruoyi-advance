SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- =========================================================
-- WMS 三期升级脚本
-- 覆盖模块:
-- 1. 箱体模块
-- 2. 装箱关系模块
-- 3. 箱体状态字典
-- 4. 箱体接口权限
-- =========================================================

-- ----------------------------
-- 1. 箱体表
-- ----------------------------
DROP TABLE IF EXISTS `wms_box`;
CREATE TABLE `wms_box` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `box_code` varchar(64) NOT NULL COMMENT '箱码',
    `box_name` varchar(64) DEFAULT NULL COMMENT '箱体名称',
    `box_status` varchar(32) DEFAULT 'idle' COMMENT '箱体状态',
    `warehouse_id` bigint(20) DEFAULT NULL COMMENT '所属仓库',
    `area_id` bigint(20) DEFAULT NULL COMMENT '所属库区',
    `rack_id` bigint(20) DEFAULT NULL COMMENT '所属货架',
    `location_id` bigint(20) DEFAULT NULL COMMENT '所属货位',
    `remark` varchar(255) DEFAULT NULL COMMENT '备注',
    `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
    `create_time` datetime(3) DEFAULT NULL COMMENT '创建时间',
    `update_by` varchar(64) DEFAULT NULL COMMENT '修改人',
    `update_time` datetime(3) DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_wms_box_code` (`box_code`) USING BTREE,
    KEY `idx_wms_box_location_id` (`location_id`) USING BTREE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '箱体';

-- ----------------------------
-- 2. 箱体与单品关系表
-- ----------------------------
DROP TABLE IF EXISTS `wms_box_item_rel`;
CREATE TABLE `wms_box_item_rel` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `box_id` bigint(20) NOT NULL COMMENT '箱体ID',
    `item_instance_id` bigint(20) NOT NULL COMMENT '单品实例ID',
    `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
    `create_time` datetime(3) DEFAULT NULL COMMENT '创建时间',
    `update_by` varchar(64) DEFAULT NULL COMMENT '修改人',
    `update_time` datetime(3) DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_wms_box_item_rel_instance_id` (`item_instance_id`) USING BTREE,
    UNIQUE KEY `uk_wms_box_item_rel_box_item` (`box_id`, `item_instance_id`) USING BTREE,
    KEY `idx_wms_box_item_rel_box_id` (`box_id`) USING BTREE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '箱体与单品关系';

-- ----------------------------
-- 3. 箱体状态字典
-- ----------------------------
INSERT INTO `sys_dict_type`
(`dict_id`, `dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES
    (1900000000000005001, '箱体状态', 'wms_box_status', '1', 'admin', NOW(), 'admin', NOW(), '箱体状态字典');

INSERT INTO `sys_dict_data`
(`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES
    (1900000000000005101, 1, '空箱', 'idle', 'wms_box_status', '', 'default', 'Y', '1', 'admin', NOW(), 'admin', NOW(), '空箱状态'),
    (1900000000000005102, 2, '已装箱', 'packed', 'wms_box_status', '', 'primary', 'N', '1', 'admin', NOW(), 'admin', NOW(), '已装箱状态'),
    (1900000000000005103, 3, '停用', 'disabled', 'wms_box_status', '', 'danger', 'N', '1', 'admin', NOW(), 'admin', NOW(), '停用状态');

-- ----------------------------
-- 4. 箱体菜单与权限
-- 父菜单使用现有“基础资料”菜单:
-- 1808758090157985794
-- ----------------------------
INSERT INTO `sys_menu`
(`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query_param`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES
    (1900000000000005201, '箱体管理', 1808758090157985794, 7, 'box', 'wms/basic/box/index', NULL, 0, 0, 'C', '1', '1', 'wms:box:list', 'documentation', 'admin', NOW(), 'admin', NOW(), '箱体管理菜单'),
    (1900000000000005202, '箱体查询', 1900000000000005201, 1, '#', NULL, NULL, 0, 0, 'F', '1', '1', 'wms:box:list', '#', 'admin', NOW(), 'admin', NOW(), '箱体查询权限'),
    (1900000000000005203, '箱体编辑', 1900000000000005201, 2, '#', NULL, NULL, 0, 0, 'F', '1', '1', 'wms:box:edit', '#', 'admin', NOW(), 'admin', NOW(), '箱体编辑权限');

SET FOREIGN_KEY_CHECKS = 1;
