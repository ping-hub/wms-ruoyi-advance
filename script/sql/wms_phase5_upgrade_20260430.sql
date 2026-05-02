SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- =========================================================
-- WMS 五期升级脚本
-- 覆盖模块:
-- 1. 出库单明细关联单品实例/箱体
-- 2. 箱体增加出库状态
-- 3. 追踪查询权限
-- =========================================================

-- ----------------------------
-- 1. 扩展出库单明细
-- ----------------------------
ALTER TABLE `wms_shipment_order_detail`
    ADD COLUMN `item_instance_id` bigint(20) DEFAULT NULL COMMENT '单品实例ID' AFTER `inventory_detail_id`,
    ADD COLUMN `box_id` bigint(20) DEFAULT NULL COMMENT '箱体ID' AFTER `item_instance_id`;

CREATE INDEX `idx_wms_shipment_detail_item_instance_id` ON `wms_shipment_order_detail` (`item_instance_id`);
CREATE INDEX `idx_wms_shipment_detail_box_id` ON `wms_shipment_order_detail` (`box_id`);

-- ----------------------------
-- 2. 箱体状态补充“已出库”
-- ----------------------------
INSERT INTO `sys_dict_data`
(`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES
    (1900000000000007101, 4, '已出库', 'outbound', 'wms_box_status', '', 'warning', 'N', '1', 'admin', NOW(), 'admin', NOW(), '箱体已整箱出库');

-- ----------------------------
-- 3. 追踪查询权限
-- 仅补功能权限，不新增前端菜单
-- ----------------------------
INSERT INTO `sys_menu`
(`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query_param`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES
    (1900000000000007201, '单品追踪查询', 1900000000000005201, 10, '#', NULL, NULL, 0, 0, 'F', '1', '1', 'wms:itemInstance:list', '#', 'admin', NOW(), 'admin', NOW(), '单品追踪接口权限'),
    (1900000000000007202, '箱码追踪查询', 1900000000000005201, 11, '#', NULL, NULL, 0, 0, 'F', '1', '1', 'wms:box:list', '#', 'admin', NOW(), 'admin', NOW(), '箱码追踪接口权限');

SET FOREIGN_KEY_CHECKS = 1;
