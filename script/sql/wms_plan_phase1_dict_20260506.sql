SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- =========================================================
-- WMS 四阶段实施计划 - 第一阶段字典脚本
-- =========================================================

INSERT INTO `sys_dict_type` (`dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `remark`) VALUES
('质量等级', 'wms_quality_grade', '1', 'admin', NOW(), '第一阶段质量等级字典'),
('调拨方式', 'wms_dispatch_mode', '1', 'admin', NOW(), '第一阶段调拨方式字典'),
('调拨类型', 'wms_movement_type', '1', 'admin', NOW(), '第一阶段调拨类型字典'),
('调拨依据类型', 'wms_basis_type', '1', 'admin', NOW(), '第一阶段调拨依据字典');

INSERT INTO `sys_dict_data` (`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `is_default`, `status`, `create_by`, `create_time`, `remark`) VALUES
(1, '一等品', 'grade_a', 'wms_quality_grade', 'Y', '1', 'admin', NOW(), '质量等级默认值'),
(2, '二等品', 'grade_b', 'wms_quality_grade', 'N', '1', 'admin', NOW(), '质量等级扩展值'),
(1, '公路', 'road', 'wms_dispatch_mode', 'Y', '1', 'admin', NOW(), '调拨方式'),
(2, '铁路', 'railway', 'wms_dispatch_mode', 'N', '1', 'admin', NOW(), '调拨方式'),
(3, '航空', 'air', 'wms_dispatch_mode', 'N', '1', 'admin', NOW(), '调拨方式'),
(1, '通装', 'common', 'wms_movement_type', 'Y', '1', 'admin', NOW(), '调拨类型'),
(2, '专装', 'special', 'wms_movement_type', 'N', '1', 'admin', NOW(), '调拨类型'),
(1, '调拨通知', 'dispatch_notice', 'wms_basis_type', 'Y', '1', 'admin', NOW(), '调拨依据'),
(2, '任务命令', 'task_order', 'wms_basis_type', 'N', '1', 'admin', NOW(), '调拨依据');

SET FOREIGN_KEY_CHECKS = 1;
