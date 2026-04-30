SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- =========================================================
-- WMS 一期菜单、权限、字典初始化脚本
-- 覆盖模块:
-- 1. 货架管理
-- 2. 货位管理
-- 3. 物品定义扩展相关字典
-- =========================================================

-- ----------------------------
-- 1. 字典类型
-- ----------------------------
INSERT INTO `sys_dict_type` (`dict_id`, `dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
    (1900000000000001001, '货架状态', 'wms_rack_status', '1', 'admin', NOW(), 'admin', NOW(), '货架状态字典'),
    (1900000000000001002, '货架类型', 'wms_rack_type', '1', 'admin', NOW(), 'admin', NOW(), '货架类型字典'),
    (1900000000000001003, '货位状态', 'wms_location_status', '1', 'admin', NOW(), 'admin', NOW(), '货位状态字典'),
    (1900000000000001004, '货位类型', 'wms_location_type', '1', 'admin', NOW(), 'admin', NOW(), '货位类型字典'),
    (1900000000000001005, '物品类型', 'wms_item_type', '1', 'admin', NOW(), 'admin', NOW(), '物品类型字典'),
    (1900000000000001006, '追踪模式', 'wms_tracking_mode', '1', 'admin', NOW(), 'admin', NOW(), '追踪模式字典'),
    (1900000000000001007, '规格等级', 'wms_spec_level', '1', 'admin', NOW(), 'admin', NOW(), '规格等级字典');

-- ----------------------------
-- 2. 字典数据
-- ----------------------------
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
    (1900000000000002001, 1, '启用', 'enabled', 'wms_rack_status', '', 'primary', 'Y', '1', 'admin', NOW(), 'admin', NOW(), '货架启用'),
    (1900000000000002002, 2, '停用', 'disabled', 'wms_rack_status', '', 'danger', 'N', '1', 'admin', NOW(), 'admin', NOW(), '货架停用'),
    (1900000000000002003, 1, '标准货架', 'standard', 'wms_rack_type', '', 'default', 'Y', '1', 'admin', NOW(), 'admin', NOW(), '标准货架'),
    (1900000000000002004, 2, '重型货架', 'heavy', 'wms_rack_type', '', 'warning', 'N', '1', 'admin', NOW(), 'admin', NOW(), '重型货架'),
    (1900000000000002005, 3, '流利式货架', 'flow', 'wms_rack_type', '', 'success', 'N', '1', 'admin', NOW(), 'admin', NOW(), '流利式货架'),
    (1900000000000002006, 1, '启用', 'enabled', 'wms_location_status', '', 'primary', 'Y', '1', 'admin', NOW(), 'admin', NOW(), '货位启用'),
    (1900000000000002007, 2, '停用', 'disabled', 'wms_location_status', '', 'danger', 'N', '1', 'admin', NOW(), 'admin', NOW(), '货位停用'),
    (1900000000000002008, 3, '占用', 'occupied', 'wms_location_status', '', 'warning', 'N', '1', 'admin', NOW(), 'admin', NOW(), '货位占用'),
    (1900000000000002009, 1, '普通货位', 'normal', 'wms_location_type', '', 'default', 'Y', '1', 'admin', NOW(), 'admin', NOW(), '普通货位'),
    (1900000000000002010, 2, '拣货位', 'pick', 'wms_location_type', '', 'success', 'N', '1', 'admin', NOW(), 'admin', NOW(), '拣货位'),
    (1900000000000002011, 3, '暂存位', 'buffer', 'wms_location_type', '', 'warning', 'N', '1', 'admin', NOW(), 'admin', NOW(), '暂存位'),
    (1900000000000002012, 1, '普通物品', 'normal', 'wms_item_type', '', 'default', 'Y', '1', 'admin', NOW(), 'admin', NOW(), '普通物品'),
    (1900000000000002013, 2, '设备资产', 'equipment', 'wms_item_type', '', 'primary', 'N', '1', 'admin', NOW(), 'admin', NOW(), '设备资产'),
    (1900000000000002014, 3, '生鲜耗材', 'fresh', 'wms_item_type', '', 'success', 'N', '1', 'admin', NOW(), 'admin', NOW(), '生鲜耗材'),
    (1900000000000002015, 1, '批次追踪', 'batch', 'wms_tracking_mode', '', 'default', 'Y', '1', 'admin', NOW(), 'admin', NOW(), '按批次追踪'),
    (1900000000000002016, 2, '单品追踪', 'instance', 'wms_tracking_mode', '', 'primary', 'N', '1', 'admin', NOW(), 'admin', NOW(), '按单品追踪'),
    (1900000000000002017, 1, '单层规格', 'single', 'wms_spec_level', '', 'default', 'Y', '1', 'admin', NOW(), 'admin', NOW(), '单层规格'),
    (1900000000000002018, 2, '物品层级', 'item', 'wms_spec_level', '', 'primary', 'N', '1', 'admin', NOW(), 'admin', NOW(), '物品层级'),
    (1900000000000002019, 3, 'SKU层级', 'sku', 'wms_spec_level', '', 'success', 'N', '1', 'admin', NOW(), 'admin', NOW(), 'SKU层级');

-- ----------------------------
-- 3. 菜单
-- 父菜单使用现有“基础资料”菜单:
-- 1808758090157985794
-- ----------------------------
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query_param`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
    (1900000000000003001, '货架管理', 1808758090157985794, 5, 'rack', 'wms/basic/rack/index', NULL, 0, 0, 'C', '1', '1', 'wms:rack:list', 'documentation', 'admin', NOW(), 'admin', NOW(), '货架管理菜单'),
    (1900000000000003002, '货位管理', 1808758090157985794, 6, 'location', 'wms/basic/location/index', NULL, 0, 0, 'C', '1', '1', 'wms:location:list', 'documentation', 'admin', NOW(), 'admin', NOW(), '货位管理菜单');

-- ----------------------------
-- 4. 按钮权限
-- ----------------------------
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query_param`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
    (1900000000000003101, '货架查询', 1900000000000003001, 1, '#', NULL, NULL, 0, 0, 'F', '1', '1', 'wms:rack:list', '#', 'admin', NOW(), 'admin', NOW(), '货架查询权限'),
    (1900000000000003102, '货架编辑', 1900000000000003001, 2, '#', NULL, NULL, 0, 0, 'F', '1', '1', 'wms:rack:edit', '#', 'admin', NOW(), 'admin', NOW(), '货架编辑权限'),
    (1900000000000003201, '货位查询', 1900000000000003002, 1, '#', NULL, NULL, 0, 0, 'F', '1', '1', 'wms:location:list', '#', 'admin', NOW(), 'admin', NOW(), '货位查询权限'),
    (1900000000000003202, '货位编辑', 1900000000000003002, 2, '#', NULL, NULL, 0, 0, 'F', '1', '1', 'wms:location:edit', '#', 'admin', NOW(), 'admin', NOW(), '货位编辑权限');

SET FOREIGN_KEY_CHECKS = 1;
