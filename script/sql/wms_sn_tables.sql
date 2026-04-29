-- WMS一物一码/SN模式数据库表结构
-- 执行此SQL请在wms数据库中执行

-- 1. 修改wms_item_sku表，增加SN启用字段
ALTER TABLE `wms_item_sku` ADD COLUMN `sn_enabled` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否启用SN管理: 0-否 1-是' AFTER `selling_price`;

-- 2. 创建商品序列号表
CREATE TABLE IF NOT EXISTS `wms_item_sn` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `sn_code` varchar(100) NOT NULL COMMENT 'SN码/序列号',
  `sku_id` bigint(20) NOT NULL COMMENT 'SKU ID',
  `item_id` bigint(20) NOT NULL COMMENT '商品ID',
  `warehouse_id` bigint(20) NOT NULL COMMENT '当前仓库ID',
  `area_id` bigint(20) NOT NULL COMMENT '当前库区ID',
  `inventory_detail_id` bigint(20) DEFAULT NULL COMMENT '关联的库存明细ID',
  `status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '状态: 0-在库 1-已出库 2-损坏 3-冻结',
  `batch_no` varchar(50) DEFAULT NULL COMMENT '批号',
  `production_date` date DEFAULT NULL COMMENT '生产日期',
  `expiration_date` date DEFAULT NULL COMMENT '过期日期',
  `receipt_order_id` bigint(20) DEFAULT NULL COMMENT '来源入库单ID',
  `receipt_order_detail_id` bigint(20) DEFAULT NULL COMMENT '来源入库明细ID',
  `shipment_order_id` bigint(20) DEFAULT NULL COMMENT '出库去向单ID',
  `shipment_order_detail_id` bigint(20) DEFAULT NULL COMMENT '出库明细ID',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_sn_code` (`sn_code`),
  KEY `idx_sku_id` (`sku_id`),
  KEY `idx_item_id` (`item_id`),
  KEY `idx_warehouse_area` (`warehouse_id`, `area_id`),
  KEY `idx_inventory_detail_id` (`inventory_detail_id`),
  KEY `idx_status` (`status`),
  KEY `idx_receipt_order_id` (`receipt_order_id`),
  KEY `idx_shipment_order_id` (`shipment_order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品序列号表';

-- 3. 创建单据SN关联表
CREATE TABLE IF NOT EXISTS `wms_order_sn` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_type` tinyint(1) NOT NULL COMMENT '单据类型: 1-入库 2-出库 3-移库 4-盘点',
  `order_id` bigint(20) NOT NULL COMMENT '单据ID',
  `order_detail_id` bigint(20) NOT NULL COMMENT '单据明细ID',
  `sn_id` bigint(20) NOT NULL COMMENT 'SN ID',
  `sn_code` varchar(100) NOT NULL COMMENT 'SN码',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_order` (`order_type`, `order_id`, `order_detail_id`),
  KEY `idx_sn_id` (`sn_id`),
  KEY `idx_sn_code` (`sn_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='单据SN关联表';

-- 4. 插入菜单权限（根据实际情况调整parent_id和order_num）
-- 注意：需要先查询wms基础数据管理的父级菜单ID
INSERT INTO `sys_menu` VALUES
(NULL, '商品序列号管理', 2100, 5, 'wms:itemSn:list', 'itemSn', '', 1, 0, 'C', '0', '0', 'wms:itemSn:list', 'list', 'admin', NOW(), '', NULL, '商品序列号管理菜单'),
(NULL, '商品序列号查询', 2101, 1, 'wms:itemSn:query', '#', '', 1, 0, 'F', '0', '0', 'wms:itemSn:query', '#', 'admin', NOW(), '', NULL, ''),
(NULL, '商品序列号新增', 2102, 2, 'wms:itemSn:add', '#', '', 1, 0, 'F', '0', '0', 'wms:itemSn:add', '#', 'admin', NOW(), '', NULL, ''),
(NULL, '商品序列号修改', 2103, 3, 'wms:itemSn:edit', '#', '', 1, 0, 'F', '0', '0', 'wms:itemSn:edit', '#', 'admin', NOW(), '', NULL, ''),
(NULL, '商品序列号删除', 2104, 4, 'wms:itemSn:remove', '#', '', 1, 0, 'F', '0', '0', 'wms:itemSn:remove', '#', 'admin', NOW(), '', NULL, ''),
(NULL, '商品序列号导出', 2105, 5, 'wms:itemSn:export', '#', '', 1, 0, 'F', '0', '0', 'wms:itemSn:export', '#', 'admin', NOW(), '', NULL, '');

-- 5. 插入单据SN关联管理菜单权限（可选，通常不需要单独页面）
INSERT INTO `sys_menu` VALUES
(NULL, '单据SN关联管理', 2110, 6, 'wms:orderSn:list', 'orderSn', '', 1, 0, 'C', '0', '0', 'wms:orderSn:list', 'list', 'admin', NOW(), '', NULL, '单据SN关联管理菜单'),
(NULL, '单据SN关联查询', 2111, 1, 'wms:orderSn:query', '#', '', 1, 0, 'F', '0', '0', 'wms:orderSn:query', '#', 'admin', NOW(), '', NULL, ''),
(NULL, '单据SN关联新增', 2112, 2, 'wms:orderSn:add', '#', '', 1, 0, 'F', '0', '0', 'wms:orderSn:add', '#', 'admin', NOW(), '', NULL, ''),
(NULL, '单据SN关联修改', 2113, 3, 'wms:orderSn:edit', '#', '', 1, 0, 'F', '0', '0', 'wms:orderSn:edit', '#', 'admin', NOW(), '', NULL, ''),
(NULL, '单据SN关联删除', 2114, 4, 'wms:orderSn:remove', '#', '', 1, 0, 'F', '0', '0', 'wms:orderSn:remove', '#', 'admin', NOW(), '', NULL, ''),
(NULL, '单据SN关联导出', 2115, 5, 'wms:orderSn:export', '#', '', 1, 0, 'F', '0', '0', 'wms:orderSn:export', '#', 'admin', NOW(), '', NULL, '');
