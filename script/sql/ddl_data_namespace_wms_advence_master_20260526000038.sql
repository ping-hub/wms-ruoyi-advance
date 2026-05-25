

-- Name: wms_advence_master; Type: Schema; Schema:  ;

CREATE SCHEMA wms_advence_master;



SET search_path = wms_advence_master ;



-- Name: gen_table; Type: Table; Schema: wms_advence_master;

SET search_path = wms_advence_master;
CREATE TABLE gen_table (
    table_id bigint NOT NULL,
    table_name varchar(200) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar,
    table_comment varchar(500) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar,
    sub_table_name varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    sub_table_fk_name varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    class_name varchar(100) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar,
    tpl_category varchar(200) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT 'crud'::varchar,
    package_name varchar(100) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    module_name varchar(30) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    business_name varchar(30) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    function_name varchar(50) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    function_author varchar(50) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    gen_type character(1) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT '0'::bpchar,
    gen_path varchar(200) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT '/'::varchar,
    options varchar(1000) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    create_by varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar,
    create_time timestamp(0) without time zone DEFAULT NULL::timestamp without time zone,
    update_by varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar,
    update_time timestamp(0) without time zone DEFAULT NULL::timestamp without time zone,
    remark varchar(500) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar
)
CHARACTER SET = "UTF8" COLLATE = "utf8mb4_general_ci"
WITH (orientation=row, compression=no, fillfactor=80);
COMMENT ON TABLE gen_table IS '代码生成业务表';
COMMENT ON COLUMN gen_table.table_id IS '编号';
COMMENT ON COLUMN gen_table.table_name IS '表名称';
COMMENT ON COLUMN gen_table.table_comment IS '表描述';
COMMENT ON COLUMN gen_table.sub_table_name IS '关联子表的表名';
COMMENT ON COLUMN gen_table.sub_table_fk_name IS '子表关联的外键名';
COMMENT ON COLUMN gen_table.class_name IS '实体类名称';
COMMENT ON COLUMN gen_table.tpl_category IS '使用的模板（crud单表操作 tree树表操作）';
COMMENT ON COLUMN gen_table.package_name IS '生成包路径';
COMMENT ON COLUMN gen_table.module_name IS '生成模块名';
COMMENT ON COLUMN gen_table.business_name IS '生成业务名';
COMMENT ON COLUMN gen_table.function_name IS '生成功能名';
COMMENT ON COLUMN gen_table.function_author IS '生成功能作者';
COMMENT ON COLUMN gen_table.gen_type IS '生成代码方式（0zip压缩包 1自定义路径）';
COMMENT ON COLUMN gen_table.gen_path IS '生成路径（不填默认项目路径）';
COMMENT ON COLUMN gen_table.options IS '其它生成选项';
COMMENT ON COLUMN gen_table.create_by IS '创建者';
COMMENT ON COLUMN gen_table.create_time IS '创建时间';
COMMENT ON COLUMN gen_table.update_by IS '更新者';
COMMENT ON COLUMN gen_table.update_time IS '更新时间';
COMMENT ON COLUMN gen_table.remark IS '备注';
ALTER TABLE gen_table ADD CONSTRAINT gen_table_pk PRIMARY KEY USING btree  (table_id);

--Data for  Name: gen_table; Type: Table; Schema: wms_advence_master;



-- Name: gen_table_column; Type: Table; Schema: wms_advence_master;

SET search_path = wms_advence_master;
CREATE TABLE gen_table_column (
    column_id bigint NOT NULL,
    table_id bigint,
    column_name varchar(200) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    column_comment varchar(500) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    column_type varchar(100) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    java_type varchar(500) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    java_field varchar(200) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    is_pk character(1) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::bpchar,
    is_increment character(1) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::bpchar,
    is_required character(1) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::bpchar,
    is_insert character(1) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::bpchar,
    is_edit character(1) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::bpchar,
    is_list character(1) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::bpchar,
    is_query character(1) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::bpchar,
    query_type varchar(200) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT 'EQ'::varchar,
    html_type varchar(200) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    dict_type varchar(200) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar,
    sort integer,
    create_by varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar,
    create_time timestamp(0) without time zone DEFAULT NULL::timestamp without time zone,
    update_by varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar,
    update_time timestamp(0) without time zone DEFAULT NULL::timestamp without time zone
)
CHARACTER SET = "UTF8" COLLATE = "utf8mb4_general_ci"
WITH (orientation=row, compression=no, fillfactor=80);
COMMENT ON TABLE gen_table_column IS '代码生成业务表字段';
COMMENT ON COLUMN gen_table_column.column_id IS '编号';
COMMENT ON COLUMN gen_table_column.table_id IS '归属表编号';
COMMENT ON COLUMN gen_table_column.column_name IS '列名称';
COMMENT ON COLUMN gen_table_column.column_comment IS '列描述';
COMMENT ON COLUMN gen_table_column.column_type IS '列类型';
COMMENT ON COLUMN gen_table_column.java_type IS 'JAVA类型';
COMMENT ON COLUMN gen_table_column.java_field IS 'JAVA字段名';
COMMENT ON COLUMN gen_table_column.is_pk IS '是否主键（1是）';
COMMENT ON COLUMN gen_table_column.is_increment IS '是否自增（1是）';
COMMENT ON COLUMN gen_table_column.is_required IS '是否必填（1是）';
COMMENT ON COLUMN gen_table_column.is_insert IS '是否为插入字段（1是）';
COMMENT ON COLUMN gen_table_column.is_edit IS '是否编辑字段（1是）';
COMMENT ON COLUMN gen_table_column.is_list IS '是否列表字段（1是）';
COMMENT ON COLUMN gen_table_column.is_query IS '是否查询字段（1是）';
COMMENT ON COLUMN gen_table_column.query_type IS '查询方式（等于、不等于、大于、小于、范围）';
COMMENT ON COLUMN gen_table_column.html_type IS '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）';
COMMENT ON COLUMN gen_table_column.dict_type IS '字典类型';
COMMENT ON COLUMN gen_table_column.sort IS '排序';
COMMENT ON COLUMN gen_table_column.create_by IS '创建者';
COMMENT ON COLUMN gen_table_column.create_time IS '创建时间';
COMMENT ON COLUMN gen_table_column.update_by IS '更新者';
COMMENT ON COLUMN gen_table_column.update_time IS '更新时间';
ALTER TABLE gen_table_column ADD CONSTRAINT gen_table_column_pk PRIMARY KEY USING btree  (column_id);

--Data for  Name: gen_table_column; Type: Table; Schema: wms_advence_master;



-- Name: sys_config; Type: Table; Schema: wms_advence_master;

SET search_path = wms_advence_master;
CREATE TABLE sys_config (
    config_id bigint NOT NULL,
    config_name varchar(100) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar,
    config_key varchar(100) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar,
    config_value varchar(500) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar,
    config_type character(1) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT 'N'::bpchar,
    create_by varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar,
    create_time timestamp(0) without time zone DEFAULT NULL::timestamp without time zone,
    update_by varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar,
    update_time timestamp(0) without time zone DEFAULT NULL::timestamp without time zone,
    remark varchar(500) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar
)
CHARACTER SET = "UTF8" COLLATE = "utf8mb4_general_ci"
WITH (orientation=row, compression=no, fillfactor=80);
COMMENT ON TABLE sys_config IS '参数配置表';
COMMENT ON COLUMN sys_config.config_id IS '参数主键';
COMMENT ON COLUMN sys_config.config_name IS '参数名称';
COMMENT ON COLUMN sys_config.config_key IS '参数键名';
COMMENT ON COLUMN sys_config.config_value IS '参数键值';
COMMENT ON COLUMN sys_config.config_type IS '系统内置（Y是 N否）';
COMMENT ON COLUMN sys_config.create_by IS '创建者';
COMMENT ON COLUMN sys_config.create_time IS '创建时间';
COMMENT ON COLUMN sys_config.update_by IS '更新者';
COMMENT ON COLUMN sys_config.update_time IS '更新时间';
COMMENT ON COLUMN sys_config.remark IS '备注';
ALTER TABLE sys_config ADD CONSTRAINT sys_config_pk PRIMARY KEY USING btree  (config_id);

--Data for  Name: sys_config; Type: Table; Schema: wms_advence_master;

INSERT INTO wms_advence_master.sys_config (config_id,config_name,config_key,config_value,config_type,create_by,create_time,update_by,update_time,remark)
 VALUES (1,'主框架页-默认皮肤样式名称','sys.index.skinName','skin-blue','Y','admin','2024-06-13 16:06:37','',null,'蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow');
INSERT INTO wms_advence_master.sys_config (config_id,config_name,config_key,config_value,config_type,create_by,create_time,update_by,update_time,remark)
 VALUES (2,'用户管理-账号初始密码','sys.user.initPassword','123456','Y','admin','2024-06-13 16:06:37','',null,'初始化密码 123456');
INSERT INTO wms_advence_master.sys_config (config_id,config_name,config_key,config_value,config_type,create_by,create_time,update_by,update_time,remark)
 VALUES (3,'主框架页-侧边栏主题','sys.index.sideTheme','theme-light','Y','admin','2024-06-13 16:06:37','admin','2024-07-16 11:25:33','深色主题theme-dark，浅色主题theme-light');
INSERT INTO wms_advence_master.sys_config (config_id,config_name,config_key,config_value,config_type,create_by,create_time,update_by,update_time,remark)
 VALUES (4,'账号自助-验证码开关','sys.account.captchaEnabled','true','Y','admin','2024-06-13 16:06:37','',null,'是否开启验证码功能（true开启，false关闭）');
INSERT INTO wms_advence_master.sys_config (config_id,config_name,config_key,config_value,config_type,create_by,create_time,update_by,update_time,remark)
 VALUES (5,'账号自助-是否开启用户注册功能','sys.account.registerUser','false','Y','admin','2024-06-13 16:06:37','',null,'是否开启注册用户功能（true开启，false关闭）');
INSERT INTO wms_advence_master.sys_config (config_id,config_name,config_key,config_value,config_type,create_by,create_time,update_by,update_time,remark)
 VALUES (11,'OSS预览列表资源开关','sys.oss.previewListResource','true','Y','admin','2024-06-13 16:06:37','',null,'true:开启, false:关闭');


-- Name: sys_dept; Type: Table; Schema: wms_advence_master;

SET search_path = wms_advence_master;
CREATE TABLE sys_dept (
    dept_id bigint NOT NULL,
    parent_id bigint DEFAULT 0::bigint,
    ancestors varchar(500) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar,
    dept_name varchar(30) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar,
    order_num integer DEFAULT 0,
    leader varchar(20) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    phone varchar(11) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    email varchar(50) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    status character(1) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT '0'::bpchar,
    del_flag character(1) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT '0'::bpchar,
    create_by varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar,
    create_time timestamp(0) without time zone DEFAULT NULL::timestamp without time zone,
    update_by varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar,
    update_time timestamp(0) without time zone DEFAULT NULL::timestamp without time zone
)
CHARACTER SET = "UTF8" COLLATE = "utf8mb4_general_ci"
WITH (orientation=row, compression=no, fillfactor=80);
COMMENT ON TABLE sys_dept IS '部门表';
COMMENT ON COLUMN sys_dept.dept_id IS '部门id';
COMMENT ON COLUMN sys_dept.parent_id IS '父部门id';
COMMENT ON COLUMN sys_dept.ancestors IS '祖级列表';
COMMENT ON COLUMN sys_dept.dept_name IS '部门名称';
COMMENT ON COLUMN sys_dept.order_num IS '显示顺序';
COMMENT ON COLUMN sys_dept.leader IS '负责人';
COMMENT ON COLUMN sys_dept.phone IS '联系电话';
COMMENT ON COLUMN sys_dept.email IS '邮箱';
COMMENT ON COLUMN sys_dept.status IS '部门状态（0正常 1停用）';
COMMENT ON COLUMN sys_dept.del_flag IS '删除标志（0代表存在 2代表删除）';
COMMENT ON COLUMN sys_dept.create_by IS '创建者';
COMMENT ON COLUMN sys_dept.create_time IS '创建时间';
COMMENT ON COLUMN sys_dept.update_by IS '更新者';
COMMENT ON COLUMN sys_dept.update_time IS '更新时间';
ALTER TABLE sys_dept ADD CONSTRAINT sys_dept_pk PRIMARY KEY USING btree  (dept_id);

--Data for  Name: sys_dept; Type: Table; Schema: wms_advence_master;

INSERT INTO wms_advence_master.sys_dept (dept_id,parent_id,ancestors,dept_name,order_num,leader,phone,email,status,del_flag,create_by,create_time,update_by,update_time)
 VALUES (100,0,'0','若依科技',0,'若依','15888888888','ry@qq.com','1','0','admin','2024-06-13 16:06:25','',null);
INSERT INTO wms_advence_master.sys_dept (dept_id,parent_id,ancestors,dept_name,order_num,leader,phone,email,status,del_flag,create_by,create_time,update_by,update_time)
 VALUES (101,100,'0,100','深圳总公司',1,'若依','15888888888','ry@qq.com','1','0','admin','2024-06-13 16:06:25','',null);
INSERT INTO wms_advence_master.sys_dept (dept_id,parent_id,ancestors,dept_name,order_num,leader,phone,email,status,del_flag,create_by,create_time,update_by,update_time)
 VALUES (102,100,'0,100','长沙分公司',2,'若依','15888888888','ry@qq.com','1','0','admin','2024-06-13 16:06:25','',null);
INSERT INTO wms_advence_master.sys_dept (dept_id,parent_id,ancestors,dept_name,order_num,leader,phone,email,status,del_flag,create_by,create_time,update_by,update_time)
 VALUES (103,101,'0,100,101','研发部门',1,'若依','15888888888','ry@qq.com','1','0','admin','2024-06-13 16:06:25','',null);
INSERT INTO wms_advence_master.sys_dept (dept_id,parent_id,ancestors,dept_name,order_num,leader,phone,email,status,del_flag,create_by,create_time,update_by,update_time)
 VALUES (104,101,'0,100,101','市场部门',2,'若依','15888888888','ry@qq.com','1','0','admin','2024-06-13 16:06:25','',null);
INSERT INTO wms_advence_master.sys_dept (dept_id,parent_id,ancestors,dept_name,order_num,leader,phone,email,status,del_flag,create_by,create_time,update_by,update_time)
 VALUES (105,101,'0,100,101','测试部门',3,'若依','15888888888','ry@qq.com','1','0','admin','2024-06-13 16:06:25','',null);
INSERT INTO wms_advence_master.sys_dept (dept_id,parent_id,ancestors,dept_name,order_num,leader,phone,email,status,del_flag,create_by,create_time,update_by,update_time)
 VALUES (106,101,'0,100,101','财务部门',4,'若依','15888888888','ry@qq.com','1','0','admin','2024-06-13 16:06:25','',null);
INSERT INTO wms_advence_master.sys_dept (dept_id,parent_id,ancestors,dept_name,order_num,leader,phone,email,status,del_flag,create_by,create_time,update_by,update_time)
 VALUES (107,101,'0,100,101','运维部门',5,'若依','15888888888','ry@qq.com','1','0','admin','2024-06-13 16:06:25','',null);
INSERT INTO wms_advence_master.sys_dept (dept_id,parent_id,ancestors,dept_name,order_num,leader,phone,email,status,del_flag,create_by,create_time,update_by,update_time)
 VALUES (108,102,'0,100,102','市场部门',1,'若依','15888888888','ry@qq.com','1','0','admin','2024-06-13 16:06:25','',null);
INSERT INTO wms_advence_master.sys_dept (dept_id,parent_id,ancestors,dept_name,order_num,leader,phone,email,status,del_flag,create_by,create_time,update_by,update_time)
 VALUES (109,102,'0,100,102','财务部门',2,'若依','15888888888','ry@qq.com','1','0','admin','2024-06-13 16:06:25','',null);
INSERT INTO wms_advence_master.sys_dept (dept_id,parent_id,ancestors,dept_name,order_num,leader,phone,email,status,del_flag,create_by,create_time,update_by,update_time)
 VALUES (1811589666899832833,102,'0,100,102','测试部门2',0,'负责人','','','1','0','admin','2024-07-12 10:33:29','admin','2024-07-12 10:33:29');


-- Name: sys_dict_data; Type: Table; Schema: wms_advence_master;

SET search_path = wms_advence_master;
CREATE TABLE sys_dict_data (
    dict_code bigint AUTO_INCREMENT NOT NULL,
    dict_sort integer DEFAULT 0,
    dict_label varchar(100) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar,
    dict_value varchar(100) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar,
    dict_type varchar(100) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar,
    css_class varchar(100) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    list_class varchar(100) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    is_default character(1) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT 'N'::bpchar,
    status character(1) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT '0'::bpchar,
    create_by varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar,
    create_time timestamp(0) without time zone DEFAULT NULL::timestamp without time zone,
    update_by varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar,
    update_time timestamp(0) without time zone DEFAULT NULL::timestamp without time zone,
    remark varchar(500) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    CONSTRAINT sys_dict_data_pk PRIMARY KEY (dict_code)
) AUTO_INCREMENT = 1900000000000010933
CHARACTER SET = "UTF8" COLLATE = "utf8mb4_general_ci"
WITH (orientation=row, compression=no, fillfactor=80);
COMMENT ON TABLE sys_dict_data IS '字典数据表';
COMMENT ON COLUMN sys_dict_data.dict_code IS '字典编码';
COMMENT ON COLUMN sys_dict_data.dict_sort IS '字典排序';
COMMENT ON COLUMN sys_dict_data.dict_label IS '字典标签';
COMMENT ON COLUMN sys_dict_data.dict_value IS '字典键值';
COMMENT ON COLUMN sys_dict_data.dict_type IS '字典类型';
COMMENT ON COLUMN sys_dict_data.css_class IS '样式属性（其他样式扩展）';
COMMENT ON COLUMN sys_dict_data.list_class IS '表格回显样式';
COMMENT ON COLUMN sys_dict_data.is_default IS '是否默认（Y是 N否）';
COMMENT ON COLUMN sys_dict_data.status IS '状态（0停用 1正常）';
COMMENT ON COLUMN sys_dict_data.create_by IS '创建者';
COMMENT ON COLUMN sys_dict_data.create_time IS '创建时间';
COMMENT ON COLUMN sys_dict_data.update_by IS '更新者';
COMMENT ON COLUMN sys_dict_data.update_time IS '更新时间';
COMMENT ON COLUMN sys_dict_data.remark IS '备注';

--Data for  Name: sys_dict_data; Type: Table; Schema: wms_advence_master;

INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1,1,'男','0','sys_user_sex','','','Y','1','admin','2024-06-13 16:06:36','',null,'性别男');
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (2,2,'女','1','sys_user_sex','','','N','1','admin','2024-06-13 16:06:36','',null,'性别女');
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (3,3,'未知','2','sys_user_sex','','','N','1','admin','2024-06-13 16:06:36','',null,'性别未知');
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (4,1,'显示','1','sys_show_hide','','primary','Y','1','admin','2024-06-13 16:06:36','admin','2024-07-10 16:34:54','显示菜单');
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (5,2,'隐藏','0','sys_show_hide','','danger','N','1','admin','2024-06-13 16:06:36','admin','2024-07-10 16:35:07','隐藏菜单');
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (6,1,'正常','1','sys_normal_disable','','primary','Y','1','admin','2024-06-13 16:06:36','admin','2024-07-10 14:30:58','正常状态');
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (7,2,'停用','0','sys_normal_disable','','danger','N','1','admin','2024-06-13 16:06:36','admin','2024-07-10 14:31:06','停用状态');
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (12,1,'是','Y','sys_yes_no','','primary','Y','1','admin','2024-06-13 16:06:36','',null,'系统默认是');
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (13,2,'否','N','sys_yes_no','','danger','N','1','admin','2024-06-13 16:06:36','',null,'系统默认否');
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (14,1,'通知','1','sys_notice_type','','warning','Y','1','admin','2024-06-13 16:06:36','',null,'通知');
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (15,2,'公告','2','sys_notice_type','','success','N','1','admin','2024-06-13 16:06:36','',null,'公告');
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (16,1,'正常','1','sys_notice_status','','primary','Y','1','admin','2024-06-13 16:06:36','admin','2024-07-10 17:24:35','正常状态');
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (17,2,'关闭','0','sys_notice_status','','danger','N','1','admin','2024-06-13 16:06:36','admin','2024-07-10 17:24:44','关闭状态');
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (18,1,'新增','1','sys_oper_type','','info','N','1','admin','2024-06-13 16:06:36','',null,'新增操作');
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (19,2,'修改','2','sys_oper_type','','info','N','1','admin','2024-06-13 16:06:36','',null,'修改操作');
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (20,3,'删除','3','sys_oper_type','','danger','N','1','admin','2024-06-13 16:06:37','',null,'删除操作');
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (21,4,'授权','4','sys_oper_type','','primary','N','1','admin','2024-06-13 16:06:37','',null,'授权操作');
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (22,5,'导出','5','sys_oper_type','','warning','N','1','admin','2024-06-13 16:06:37','',null,'导出操作');
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (23,6,'导入','6','sys_oper_type','','warning','N','1','admin','2024-06-13 16:06:37','',null,'导入操作');
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (24,7,'强退','7','sys_oper_type','','danger','N','1','admin','2024-06-13 16:06:37','',null,'强退操作');
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (25,8,'生成代码','8','sys_oper_type','','warning','N','1','admin','2024-06-13 16:06:37','',null,'生成操作');
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (26,9,'清空数据','9','sys_oper_type','','danger','N','1','admin','2024-06-13 16:06:37','',null,'清空操作');
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (27,1,'失败','0','sys_common_status','','danger','N','1','admin','2024-06-13 16:06:37','admin','2024-07-15 10:50:52','正常状态');
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (28,2,'成功','1','sys_common_status','','success','N','1','admin','2024-06-13 16:06:37','admin','2024-07-15 10:51:05','停用状态');
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (29,99,'其他','0','sys_oper_type','','info','N','1','admin','2024-06-13 16:06:36','',null,'其他操作');
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1813153852862160897,0,'未入库','0','wms_receipt_status',null,'info','N','1','admin','2024-07-16 18:09:00','admin','2024-07-22 09:38:14',null);
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1813153899775451137,1,'已入库','1','wms_receipt_status',null,'primary','N','1','admin','2024-07-16 18:09:11','admin','2024-07-22 09:38:22',null);
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1813397339171905537,3,'作废','-1','wms_receipt_status',null,'danger','N','1','admin','2024-07-17 10:16:32','admin','2024-07-22 09:38:29',null);
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1818850397680640002,2,'作废','-1','wms_shipment_status',null,'danger','N','1','admin','2024-08-01 11:25:02','admin','2024-08-01 14:25:24',null);
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1818850512650706945,0,'未出库','0','wms_shipment_status',null,'info','N','1','admin','2024-08-01 11:25:29','admin','2024-08-01 14:25:37',null);
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1818850565389885441,1,'已出库','1','wms_shipment_status',null,'primary','N','1','admin','2024-08-01 11:25:42','admin','2024-08-01 14:25:32',null);
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1821067084643434498,0,'入库','1','wms_inventory_history_type',null,'primary','N','1','admin','2024-08-07 14:13:21','admin','2024-08-07 14:57:41',null);
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1821067144441626625,1,'出库','2','wms_inventory_history_type',null,'primary','N','1','admin','2024-08-07 14:13:36','admin','2024-08-07 14:57:47',null);
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1821067181917732866,2,'移库','3','wms_inventory_history_type',null,'primary','N','1','admin','2024-08-07 14:13:45','admin','2024-08-07 14:57:54',null);
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1821067222455681026,3,'盘库','4','wms_inventory_history_type',null,'primary','N','1','admin','2024-08-07 14:13:54','admin','2024-08-07 14:58:06',null);
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1822820748966006786,0,'未移库','0','wms_movement_status',null,'info','N','1','admin','2024-08-12 10:21:48','admin','2024-08-12 10:21:48',null);
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1822820794864275457,1,'已移库','1','wms_movement_status',null,'primary','N','1','admin','2024-08-12 10:21:59','admin','2024-08-12 10:21:59',null);
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1822820855526494210,2,'作废','-1','wms_movement_status',null,'danger','N','1','admin','2024-08-12 10:22:13','admin','2024-08-12 10:22:13',null);
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1823182345731391489,0,'待盘库','0','wms_check_status',null,'info','N','1','admin','2024-08-13 10:18:39','admin','2024-08-13 10:18:39',null);
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1823182400756465666,1,'已盘库','1','wms_check_status',null,'primary','N','1','admin','2024-08-13 10:18:52','admin','2024-08-13 10:18:52',null);
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1823182471136886786,2,'作废','-1','wms_check_status',null,'danger','N','1','admin','2024-08-13 10:19:09','admin','2024-08-13 10:19:09',null);
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1900000000000002001,1,'启用','enabled','wms_rack_status','','primary','Y','1','admin','2026-04-30 14:04:34','admin','2026-04-30 14:04:34','货架启用');
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1900000000000002002,2,'停用','disabled','wms_rack_status','','danger','N','1','admin','2026-04-30 14:04:34','admin','2026-04-30 14:04:34','货架停用');
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1900000000000002006,1,'启用','enabled','wms_location_status','','primary','Y','1','admin','2026-04-30 14:04:34','admin','2026-04-30 14:04:34','货位启用');
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1900000000000002007,2,'停用','disabled','wms_location_status','','danger','N','1','admin','2026-04-30 14:04:34','admin','2026-04-30 14:04:34','货位停用');
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1900000000000002008,3,'占用','occupied','wms_location_status','','warning','N','1','admin','2026-04-30 14:04:34','admin','2026-04-30 14:04:34','货位占用');
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1900000000000004101,1,'待入库','待入库','wms_item_instance_status','','primary','Y','1','admin','2026-05-18 09:45:08','admin','2026-05-18 09:45:08','批量打印后待入库');
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1900000000000004102,2,'在库','在库','wms_item_instance_status','','success','N','1','admin','2026-05-18 09:45:08','admin','2026-05-18 09:45:08','已入库且可参与在库作业');
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1900000000000004103,5,'借出','借出','wms_item_instance_status','','warning','N','1','admin','2026-05-18 09:45:08','admin','2026-05-18 09:45:08','借用单借出');
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1900000000000004104,3,'出库','出库','wms_item_instance_status','','info','N','1','admin','2026-05-18 09:45:08','admin','2026-05-18 09:45:08','已完成出库');
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1900000000000004105,6,'报废','报废','wms_item_instance_status','','default','N','1','admin','2026-05-18 09:45:08','admin','2026-05-18 09:45:08','报废出库');
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1900000000000005101,1,'空箱','idle','wms_box_status','','default','Y','1','admin','2026-04-30 17:27:59','admin','2026-04-30 17:27:59','空箱状态');
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1900000000000005102,2,'已装箱','packed','wms_box_status','','primary','N','1','admin','2026-04-30 17:27:59','admin','2026-04-30 17:27:59','已装箱状态');
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1900000000000005103,3,'停用','disabled','wms_box_status','','danger','N','1','admin','2026-04-30 17:27:59','admin','2026-04-30 17:27:59','停用状态');
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1900000000000006101,1,'借出中','borrowed','wms_borrow_status','','warning','Y','1','admin','2026-04-30 17:44:30','admin','2026-04-30 17:44:30','当前已借出');
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1900000000000006102,2,'已归还','returned','wms_borrow_status','','success','N','1','admin','2026-04-30 17:44:30','admin','2026-04-30 17:44:30','当前已归还');
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1900000000000007101,4,'已出库','outbound','wms_box_status','','warning','N','1','admin','2026-05-02 23:02:59','admin','2026-05-02 23:02:59','箱体已整箱出库');
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1900000000000010101,1,'通装','通装','wms_equipment_type','','primary','Y','1','admin','2026-05-07 11:17:06','admin','2026-05-07 11:17:06','器材类型');
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1900000000000010102,2,'专装','专装','wms_equipment_type','','warning','N','1','admin','2026-05-07 11:17:06','admin','2026-05-07 11:17:06','器材类型');
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1900000000000010201,1,'启用','1','wms_item_status','','primary','Y','1','admin','2026-05-07 11:17:06','admin','2026-05-20 17:59:01','器材主档启用');
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1900000000000010202,2,'停用','0','wms_item_status','','danger','N','1','admin','2026-05-07 11:17:06','admin','2026-05-20 17:59:01','器材主档停用');
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1900000000000010301,1,'启用','1','wms_item_sku_status','','primary','Y','1','admin','2026-05-07 11:17:06','admin','2026-05-20 17:59:01','器材规格启用');
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1900000000000010302,2,'停用','0','wms_item_sku_status','','danger','N','1','admin','2026-05-07 11:17:06','admin','2026-05-20 17:59:01','器材规格停用');
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1900000000000010901,4,'异常','abnormal','wms_location_status','','danger','N','1','admin','2026-05-07 11:17:06','admin','2026-05-07 11:17:06','货位异常');
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1900000000000010902,4,'盘亏','盘亏','wms_item_instance_status','','danger','N','1','admin','2026-05-18 09:45:08','admin','2026-05-18 09:45:08','盘点盘亏');
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1900000000000010903,1,'采购入库','采购入库','wms_receipt_type','','primary','Y','1','admin','2026-05-18 11:44:06','admin','2026-05-18 11:44:06','采购入库');
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1900000000000010904,2,'归还入库','归还入库','wms_receipt_type','','success','N','1','admin','2026-05-18 11:44:06','admin','2026-05-18 11:44:06','归还入库');
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1900000000000010905,3,'调拨入库','调拨入库','wms_receipt_type','','warning','N','1','admin','2026-05-18 11:44:06','admin','2026-05-18 11:44:06','调拨入库');
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1900000000000010906,1,'借用出库','借用出库','wms_shipment_type','','primary','Y','1','admin','2026-05-18 11:44:06','admin','2026-05-18 11:44:06','借用出库');
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1900000000000010907,2,'调拨出库','调拨出库','wms_shipment_type','','warning','N','1','admin','2026-05-18 11:44:06','admin','2026-05-18 11:44:06','调拨出库');
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1900000000000010908,3,'报废出库','报废出库','wms_shipment_type','','danger','N','1','admin','2026-05-18 11:44:06','admin','2026-05-18 11:44:06','报废出库');
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1900000000000010909,1,'空运','air','wms_dispatch_mode','','primary','Y','1','admin','2026-05-20 16:42:54','admin','2026-05-20 17:59:01','调拨方式：空运');
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1900000000000010910,2,'公路','road','wms_dispatch_mode','','success','N','1','admin','2026-05-20 16:42:54','admin','2026-05-20 17:59:01','调拨方式：公路');
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1900000000000010911,3,'铁路','rail','wms_dispatch_mode','','warning','N','1','admin','2026-05-20 16:42:54','admin','2026-05-20 17:59:01','调拨方式：铁路');
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1900000000000010912,4,'水运','sea','wms_dispatch_mode','','info','N','1','admin','2026-05-20 16:42:54','admin','2026-05-20 17:59:01','调拨方式：水运');
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1900000000000010913,1,'通装','通装','wms_movement_type','','primary','Y','1','admin','2026-05-20 17:59:01','admin','2026-05-20 17:59:01','调拨类型：通装');
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1900000000000010914,2,'专装','专装','wms_movement_type','','warning','N','1','admin','2026-05-20 17:59:01','admin','2026-05-20 17:59:01','调拨类型：专装');
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1900000000000010915,1,'正常','0','wms_overdue_flag','','success','Y','1','admin','2026-05-20 17:59:01','admin','2026-05-20 17:59:01','超期状态：正常');
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1900000000000010916,2,'已超期','1','wms_overdue_flag','','danger','N','1','admin','2026-05-20 17:59:01','admin','2026-05-20 17:59:01','超期状态：已超期');
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1900000000000010917,1,'仓库','warehouse','wms_check_scope_type','','primary','Y','1','admin','2026-05-20 17:59:01','admin','2026-05-20 17:59:01','盘点范围：仓库');
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1900000000000010918,2,'库区','area','wms_check_scope_type','','success','N','1','admin','2026-05-20 17:59:01','admin','2026-05-20 17:59:01','盘点范围：库区');
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1900000000000010919,3,'货架','rack','wms_check_scope_type','','warning','N','1','admin','2026-05-20 17:59:01','admin','2026-05-20 17:59:01','盘点范围：货架');
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1900000000000010930,5,'借用','5','wms_inventory_history_type','','warning','N','1','admin','2026-05-20 23:15:58','admin','2026-05-20 23:15:58','库存流水操作类型：借用');
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1900000000000010931,6,'归还','6','wms_inventory_history_type','','success','N','1','admin','2026-05-20 23:15:58','admin','2026-05-20 23:15:58','库存流水操作类型：归还');
INSERT INTO wms_advence_master.sys_dict_data (dict_code,dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1900000000000010932,7,'调整','7','wms_inventory_history_type','','info','N','1','admin','2026-05-20 23:15:58','admin','2026-05-20 23:15:58','库存流水操作类型：调整');


-- Name: sys_dict_type; Type: Table; Schema: wms_advence_master;

SET search_path = wms_advence_master;
CREATE TABLE sys_dict_type (
    dict_id bigint AUTO_INCREMENT NOT NULL,
    dict_name varchar(100) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar,
    dict_type varchar(100) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar,
    status character(1) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT '0'::bpchar,
    create_by varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar,
    create_time timestamp(0) without time zone DEFAULT NULL::timestamp without time zone,
    update_by varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar,
    update_time timestamp(0) without time zone DEFAULT NULL::timestamp without time zone,
    remark varchar(500) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    CONSTRAINT sys_dict_type_pk PRIMARY KEY (dict_id)
) AUTO_INCREMENT = 1900000000000010013
CHARACTER SET = "UTF8" COLLATE = "utf8mb4_general_ci"
WITH (orientation=row, compression=no, fillfactor=80);
COMMENT ON TABLE sys_dict_type IS '字典类型表';
COMMENT ON COLUMN sys_dict_type.dict_id IS '字典主键';
COMMENT ON COLUMN sys_dict_type.dict_name IS '字典名称';
COMMENT ON COLUMN sys_dict_type.dict_type IS '字典类型';
COMMENT ON COLUMN sys_dict_type.status IS '状态（0正常 1停用）';
COMMENT ON COLUMN sys_dict_type.create_by IS '创建者';
COMMENT ON COLUMN sys_dict_type.create_time IS '创建时间';
COMMENT ON COLUMN sys_dict_type.update_by IS '更新者';
COMMENT ON COLUMN sys_dict_type.update_time IS '更新时间';
COMMENT ON COLUMN sys_dict_type.remark IS '备注';
ALTER TABLE sys_dict_type ADD CONSTRAINT sys_dict_type_uk1 UNIQUE USING btree (dict_type);

--Data for  Name: sys_dict_type; Type: Table; Schema: wms_advence_master;

INSERT INTO wms_advence_master.sys_dict_type (dict_id,dict_name,dict_type,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1,'用户性别','sys_user_sex','1','admin','2024-06-13 16:06:35','',null,'用户性别列表');
INSERT INTO wms_advence_master.sys_dict_type (dict_id,dict_name,dict_type,status,create_by,create_time,update_by,update_time,remark)
 VALUES (2,'菜单状态','sys_show_hide','1','admin','2024-06-13 16:06:35','',null,'菜单状态列表');
INSERT INTO wms_advence_master.sys_dict_type (dict_id,dict_name,dict_type,status,create_by,create_time,update_by,update_time,remark)
 VALUES (3,'系统开关','sys_normal_disable','1','admin','2024-06-13 16:06:35','',null,'系统开关列表');
INSERT INTO wms_advence_master.sys_dict_type (dict_id,dict_name,dict_type,status,create_by,create_time,update_by,update_time,remark)
 VALUES (6,'系统是否','sys_yes_no','1','admin','2024-06-13 16:06:35','',null,'系统是否列表');
INSERT INTO wms_advence_master.sys_dict_type (dict_id,dict_name,dict_type,status,create_by,create_time,update_by,update_time,remark)
 VALUES (7,'通知类型','sys_notice_type','1','admin','2024-06-13 16:06:35','',null,'通知类型列表');
INSERT INTO wms_advence_master.sys_dict_type (dict_id,dict_name,dict_type,status,create_by,create_time,update_by,update_time,remark)
 VALUES (8,'通知状态','sys_notice_status','1','admin','2024-06-13 16:06:35','',null,'通知状态列表');
INSERT INTO wms_advence_master.sys_dict_type (dict_id,dict_name,dict_type,status,create_by,create_time,update_by,update_time,remark)
 VALUES (9,'操作类型','sys_oper_type','1','admin','2024-06-13 16:06:35','',null,'操作类型列表');
INSERT INTO wms_advence_master.sys_dict_type (dict_id,dict_name,dict_type,status,create_by,create_time,update_by,update_time,remark)
 VALUES (10,'系统状态','sys_common_status','1','admin','2024-06-13 16:06:36','',null,'登录状态列表');
INSERT INTO wms_advence_master.sys_dict_type (dict_id,dict_name,dict_type,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1813152108564373505,'入库状态','wms_receipt_status','1','admin','2024-07-16 18:02:04','admin','2024-07-16 18:02:17','入库状态');
INSERT INTO wms_advence_master.sys_dict_type (dict_id,dict_name,dict_type,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1814219082624778242,'入库类型','wms_receipt_type','1','admin','2024-07-19 16:41:51','admin','2026-05-18 11:44:06','入库类型');
INSERT INTO wms_advence_master.sys_dict_type (dict_id,dict_name,dict_type,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1818848671749709825,'出库状态','wms_shipment_status','1','admin','2024-08-01 11:18:11','admin','2024-08-01 11:18:11',null);
INSERT INTO wms_advence_master.sys_dict_type (dict_id,dict_name,dict_type,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1818848738502057985,'出库类型','wms_shipment_type','1','admin','2024-08-01 11:18:26','admin','2026-05-18 11:44:06','出库类型');
INSERT INTO wms_advence_master.sys_dict_type (dict_id,dict_name,dict_type,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1821066855638630402,'库存记录操作类型','wms_inventory_history_type','1','admin','2024-08-07 14:12:27','admin','2024-08-07 14:12:27',null);
INSERT INTO wms_advence_master.sys_dict_type (dict_id,dict_name,dict_type,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1822820566366982146,'移库状态','wms_movement_status','1','admin','2024-08-12 10:21:04','admin','2024-08-12 10:21:04',null);
INSERT INTO wms_advence_master.sys_dict_type (dict_id,dict_name,dict_type,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1823182238898274306,'盘库状态','wms_check_status','1','admin','2024-08-13 10:18:14','admin','2024-08-13 10:18:14',null);
INSERT INTO wms_advence_master.sys_dict_type (dict_id,dict_name,dict_type,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1900000000000001001,'货架状态','wms_rack_status','1','admin','2026-04-30 14:04:34','admin','2026-04-30 14:04:34','货架状态字典');
INSERT INTO wms_advence_master.sys_dict_type (dict_id,dict_name,dict_type,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1900000000000001003,'货位状态','wms_location_status','1','admin','2026-04-30 14:04:34','admin','2026-04-30 14:04:34','货位状态字典');
INSERT INTO wms_advence_master.sys_dict_type (dict_id,dict_name,dict_type,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1900000000000004001,'单品实例状态','wms_item_instance_status','1','admin','2026-04-30 16:29:04','admin','2026-04-30 16:29:04','单品实例状态字典');
INSERT INTO wms_advence_master.sys_dict_type (dict_id,dict_name,dict_type,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1900000000000005001,'箱体状态','wms_box_status','1','admin','2026-04-30 17:27:59','admin','2026-04-30 17:27:59','箱体状态字典');
INSERT INTO wms_advence_master.sys_dict_type (dict_id,dict_name,dict_type,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1900000000000006001,'借还状态','wms_borrow_status','1','admin','2026-04-30 17:44:30','admin','2026-04-30 17:44:30','借还状态字典');
INSERT INTO wms_advence_master.sys_dict_type (dict_id,dict_name,dict_type,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1900000000000010001,'器材类型','wms_equipment_type','1','admin','2026-05-07 11:17:06','admin','2026-05-07 11:17:06','第一阶段器材类型字典');
INSERT INTO wms_advence_master.sys_dict_type (dict_id,dict_name,dict_type,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1900000000000010002,'器材主档状态','wms_item_status','1','admin','2026-05-07 11:17:06','admin','2026-05-07 11:17:06','第一阶段器材主档状态字典');
INSERT INTO wms_advence_master.sys_dict_type (dict_id,dict_name,dict_type,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1900000000000010003,'器材规格状态','wms_item_sku_status','1','admin','2026-05-07 11:17:06','admin','2026-05-07 11:17:06','第一阶段器材规格状态字典');
INSERT INTO wms_advence_master.sys_dict_type (dict_id,dict_name,dict_type,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1900000000000010009,'调拨方式','wms_dispatch_mode','1','admin','2026-05-20 16:42:54','admin','2026-05-20 16:42:54','WMS调拨方式字典，供入库/出库/调拨单共用');
INSERT INTO wms_advence_master.sys_dict_type (dict_id,dict_name,dict_type,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1900000000000010010,'调拨类型','wms_movement_type','1','admin','2026-05-20 17:59:01','admin','2026-05-20 17:59:01','WMS调拨类型字典');
INSERT INTO wms_advence_master.sys_dict_type (dict_id,dict_name,dict_type,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1900000000000010011,'超期状态','wms_overdue_flag','1','admin','2026-05-20 17:59:01','admin','2026-05-20 17:59:01','WMS借用超期状态字典');
INSERT INTO wms_advence_master.sys_dict_type (dict_id,dict_name,dict_type,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1900000000000010012,'盘点范围类型','wms_check_scope_type','1','admin','2026-05-20 17:59:01','admin','2026-05-20 17:59:01','WMS盘点范围类型字典');


-- Name: sys_logininfor; Type: Table; Schema: wms_advence_master;

SET search_path = wms_advence_master;
CREATE TABLE sys_logininfor (
    info_id bigint NOT NULL,
    user_name varchar(50) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar,
    ipaddr varchar(128) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar,
    login_location varchar(255) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar,
    browser varchar(50) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar,
    os varchar(50) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar,
    status character(1) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT '0'::bpchar,
    msg varchar(255) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar,
    login_time timestamp(0) without time zone DEFAULT NULL::timestamp without time zone
)
CHARACTER SET = "UTF8" COLLATE = "utf8mb4_general_ci"
WITH (orientation=row, compression=no, fillfactor=80);
COMMENT ON TABLE sys_logininfor IS '系统访问记录';
COMMENT ON COLUMN sys_logininfor.info_id IS '访问ID';
COMMENT ON COLUMN sys_logininfor.user_name IS '用户账号';
COMMENT ON COLUMN sys_logininfor.ipaddr IS '登录IP地址';
COMMENT ON COLUMN sys_logininfor.login_location IS '登录地点';
COMMENT ON COLUMN sys_logininfor.browser IS '浏览器类型';
COMMENT ON COLUMN sys_logininfor.os IS '操作系统';
COMMENT ON COLUMN sys_logininfor.status IS '登录状态（0成功 1失败）';
COMMENT ON COLUMN sys_logininfor.msg IS '提示消息';
COMMENT ON COLUMN sys_logininfor.login_time IS '访问时间';
CREATE INDEX tb0_sys_logininfor_idx_sys_logininfor_lt ON wms_advence_master.sys_logininfor USING btree (login_time) TABLESPACE pg_default;
CREATE INDEX tb0_sys_logininfor_idx_sys_logininfor_s ON wms_advence_master.sys_logininfor USING btree (status) TABLESPACE pg_default;
ALTER TABLE sys_logininfor ADD CONSTRAINT sys_logininfor_pk PRIMARY KEY USING btree  (info_id);

--Data for  Name: sys_logininfor; Type: Table; Schema: wms_advence_master;

INSERT INTO wms_advence_master.sys_logininfor (info_id,user_name,ipaddr,login_location,browser,os,status,msg,login_time)
 VALUES (2049758328805208066,'admin','0:0:0:0:0:0:0:1','内网IP','Chrome','Windows 10 or Windows Server 2016','1','登录成功','2026-04-30 15:50:43');
INSERT INTO wms_advence_master.sys_logininfor (info_id,user_name,ipaddr,login_location,browser,os,status,msg,login_time)
 VALUES (2051911181686620161,'admin','0:0:0:0:0:0:0:1','内网IP','Chrome','Windows 10 or Windows Server 2016','1','登录成功','2026-05-06 14:25:23');
INSERT INTO wms_advence_master.sys_logininfor (info_id,user_name,ipaddr,login_location,browser,os,status,msg,login_time)
 VALUES (2052294143653543938,'admin','0:0:0:0:0:0:0:1','内网IP','Chrome','Windows 10 or Windows Server 2016','1','登录成功','2026-05-07 15:47:08');
INSERT INTO wms_advence_master.sys_logininfor (info_id,user_name,ipaddr,login_location,browser,os,status,msg,login_time)
 VALUES (2052668409146343425,'admin','0:0:0:0:0:0:0:1','内网IP','Chrome','Windows 10 or Windows Server 2016','1','登录成功','2026-05-08 16:34:20');
INSERT INTO wms_advence_master.sys_logininfor (info_id,user_name,ipaddr,login_location,browser,os,status,msg,login_time)
 VALUES (2052690942419636226,'admin','0:0:0:0:0:0:0:1','内网IP','Chrome','Windows 10 or Windows Server 2016','1','退出成功','2026-05-08 18:03:52');
INSERT INTO wms_advence_master.sys_logininfor (info_id,user_name,ipaddr,login_location,browser,os,status,msg,login_time)
 VALUES (2052691075416821762,'admin','0:0:0:0:0:0:0:1','内网IP','Chrome','Windows 10 or Windows Server 2016','1','登录成功','2026-05-08 18:04:24');
INSERT INTO wms_advence_master.sys_logininfor (info_id,user_name,ipaddr,login_location,browser,os,status,msg,login_time)
 VALUES (2053038638946971649,'admin','0:0:0:0:0:0:0:1','内网IP','Chrome','Windows 10 or Windows Server 2016','1','退出成功','2026-05-09 17:05:30');
INSERT INTO wms_advence_master.sys_logininfor (info_id,user_name,ipaddr,login_location,browser,os,status,msg,login_time)
 VALUES (2053040509807882242,'admin','0:0:0:0:0:0:0:1','内网IP','Chrome','Windows 10 or Windows Server 2016','1','登录成功','2026-05-09 17:12:56');
INSERT INTO wms_advence_master.sys_logininfor (info_id,user_name,ipaddr,login_location,browser,os,status,msg,login_time)
 VALUES (2053645979656462338,'admin','0:0:0:0:0:0:0:1','内网IP','Chrome','Windows 10 or Windows Server 2016','0','验证码已失效','2026-05-11 09:18:51');
INSERT INTO wms_advence_master.sys_logininfor (info_id,user_name,ipaddr,login_location,browser,os,status,msg,login_time)
 VALUES (2053646000707674114,'admin','0:0:0:0:0:0:0:1','内网IP','Chrome','Windows 10 or Windows Server 2016','1','登录成功','2026-05-11 09:18:56');
INSERT INTO wms_advence_master.sys_logininfor (info_id,user_name,ipaddr,login_location,browser,os,status,msg,login_time)
 VALUES (2054017270859173890,'admin','0:0:0:0:0:0:0:1','内网IP','Chrome','Windows 10 or Windows Server 2016','1','登录成功','2026-05-12 09:54:14');
INSERT INTO wms_advence_master.sys_logininfor (info_id,user_name,ipaddr,login_location,browser,os,status,msg,login_time)
 VALUES (2054390365717635074,'admin','0:0:0:0:0:0:0:1','内网IP','Chrome','Windows 10 or Windows Server 2016','1','登录成功','2026-05-13 10:36:47');
INSERT INTO wms_advence_master.sys_logininfor (info_id,user_name,ipaddr,login_location,browser,os,status,msg,login_time)
 VALUES (2054804881378971649,'admin','0:0:0:0:0:0:0:1','内网IP','Chrome','Windows 10 or Windows Server 2016','1','登录成功','2026-05-14 14:03:55');
INSERT INTO wms_advence_master.sys_logininfor (info_id,user_name,ipaddr,login_location,browser,os,status,msg,login_time)
 VALUES (2055090854125989890,'admin','0:0:0:0:0:0:0:1','内网IP','Chrome','Windows 10 or Windows Server 2016','1','退出成功','2026-05-15 09:00:16');
INSERT INTO wms_advence_master.sys_logininfor (info_id,user_name,ipaddr,login_location,browser,os,status,msg,login_time)
 VALUES (2055092984673038337,'admin','0:0:0:0:0:0:0:1','内网IP','Chrome','Windows 10 or Windows Server 2016','0','验证码已失效','2026-05-15 09:08:44');
INSERT INTO wms_advence_master.sys_logininfor (info_id,user_name,ipaddr,login_location,browser,os,status,msg,login_time)
 VALUES (2055092994273800193,'admin','0:0:0:0:0:0:0:1','内网IP','Chrome','Windows 10 or Windows Server 2016','0','验证码错误','2026-05-15 09:08:46');
INSERT INTO wms_advence_master.sys_logininfor (info_id,user_name,ipaddr,login_location,browser,os,status,msg,login_time)
 VALUES (2055093005623586818,'admin','0:0:0:0:0:0:0:1','内网IP','Chrome','Windows 10 or Windows Server 2016','1','登录成功','2026-05-15 09:08:49');
INSERT INTO wms_advence_master.sys_logininfor (info_id,user_name,ipaddr,login_location,browser,os,status,msg,login_time)
 VALUES (2055527840410316801,'admin','0:0:0:0:0:0:0:1','内网IP','Chrome','Windows 10 or Windows Server 2016','1','登录成功','2026-05-16 13:56:42');
INSERT INTO wms_advence_master.sys_logininfor (info_id,user_name,ipaddr,login_location,browser,os,status,msg,login_time)
 VALUES (2055973708791914497,'admin','0:0:0:0:0:0:0:1','内网IP','Chrome','Windows 10 or Windows Server 2016','1','登录成功','2026-05-17 19:28:25');
INSERT INTO wms_advence_master.sys_logininfor (info_id,user_name,ipaddr,login_location,browser,os,status,msg,login_time)
 VALUES (2056372873405493250,'admin','0:0:0:0:0:0:0:1','内网IP','Chrome','Windows 10 or Windows Server 2016','0','验证码已失效','2026-05-18 21:54:33');
INSERT INTO wms_advence_master.sys_logininfor (info_id,user_name,ipaddr,login_location,browser,os,status,msg,login_time)
 VALUES (2056372896625160193,'admin','0:0:0:0:0:0:0:1','内网IP','Chrome','Windows 10 or Windows Server 2016','1','登录成功','2026-05-18 21:54:39');
INSERT INTO wms_advence_master.sys_logininfor (info_id,user_name,ipaddr,login_location,browser,os,status,msg,login_time)
 VALUES (2056752436123877377,'admin','0:0:0:0:0:0:0:1','内网IP','Chrome','Windows 10 or Windows Server 2016','1','登录成功','2026-05-19 23:02:48');
INSERT INTO wms_advence_master.sys_logininfor (info_id,user_name,ipaddr,login_location,browser,os,status,msg,login_time)
 VALUES (2057097095211753474,'admin','0:0:0:0:0:0:0:1','内网IP','Chrome','Windows 10 or Windows Server 2016','1','登录成功','2026-05-20 21:52:21');
INSERT INTO wms_advence_master.sys_logininfor (info_id,user_name,ipaddr,login_location,browser,os,status,msg,login_time)
 VALUES (2057459654594965506,'admin','0:0:0:0:0:0:0:1','内网IP','Chrome','Windows 10 or Windows Server 2016','1','登录成功','2026-05-21 21:53:02');
INSERT INTO wms_advence_master.sys_logininfor (info_id,user_name,ipaddr,login_location,browser,os,status,msg,login_time)
 VALUES (2058915229472206850,'admin','0:0:0:0:0:0:0:1','内网IP','Chrome','Windows 10 or Windows Server 2016','1','登录成功','2026-05-25 22:16:58');


-- Name: sys_menu; Type: Table; Schema: wms_advence_master;

SET search_path = wms_advence_master;
CREATE TABLE sys_menu (
    menu_id bigint NOT NULL,
    menu_name varchar(50) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci NOT NULL,
    parent_id bigint DEFAULT 0::bigint,
    order_num integer DEFAULT 0,
    path varchar(200) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar,
    component varchar(255) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    query_param varchar(255) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    is_frame integer DEFAULT 1,
    is_cache integer DEFAULT 0,
    menu_type character(1) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::bpchar,
    visible character(1) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT '0'::bpchar,
    status character(1) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT '0'::bpchar,
    perms varchar(100) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    icon varchar(100) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT '#'::varchar,
    create_by varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar,
    create_time timestamp(0) without time zone DEFAULT NULL::timestamp without time zone,
    update_by varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar,
    update_time timestamp(0) without time zone DEFAULT NULL::timestamp without time zone,
    remark varchar(500) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar
)
CHARACTER SET = "UTF8" COLLATE = "utf8mb4_general_ci"
WITH (orientation=row, compression=no, fillfactor=80);
COMMENT ON TABLE sys_menu IS '菜单权限表';
COMMENT ON COLUMN sys_menu.menu_id IS '菜单ID';
COMMENT ON COLUMN sys_menu.menu_name IS '菜单名称';
COMMENT ON COLUMN sys_menu.parent_id IS '父菜单ID';
COMMENT ON COLUMN sys_menu.order_num IS '显示顺序';
COMMENT ON COLUMN sys_menu.path IS '路由地址';
COMMENT ON COLUMN sys_menu.component IS '组件路径';
COMMENT ON COLUMN sys_menu.query_param IS '路由参数';
COMMENT ON COLUMN sys_menu.is_frame IS '是否为外链（0是 1否）';
COMMENT ON COLUMN sys_menu.is_cache IS '是否缓存（0缓存 1不缓存）';
COMMENT ON COLUMN sys_menu.menu_type IS '菜单类型（M目录 C菜单 F按钮）';
COMMENT ON COLUMN sys_menu.visible IS '显示状态（0显示 1隐藏）';
COMMENT ON COLUMN sys_menu.status IS '菜单状态（0正常 1停用）';
COMMENT ON COLUMN sys_menu.perms IS '权限标识';
COMMENT ON COLUMN sys_menu.icon IS '菜单图标';
COMMENT ON COLUMN sys_menu.create_by IS '创建者';
COMMENT ON COLUMN sys_menu.create_time IS '创建时间';
COMMENT ON COLUMN sys_menu.update_by IS '更新者';
COMMENT ON COLUMN sys_menu.update_time IS '更新时间';
COMMENT ON COLUMN sys_menu.remark IS '备注';
ALTER TABLE sys_menu ADD CONSTRAINT sys_menu_pk PRIMARY KEY USING btree  (menu_id);

--Data for  Name: sys_menu; Type: Table; Schema: wms_advence_master;

INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1,'系统管理',0,110,'system',null,'',0,0,'M','1','1','','system','admin','2024-06-13 16:06:26','admin','2024-08-20 13:45:48','系统管理目录');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (2,'系统监控',0,120,'monitor',null,'',0,0,'M','1','1','','monitor','admin','2024-06-13 16:06:26','admin','2024-08-20 13:45:57','系统监控目录');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (100,'用户管理',1,1,'user','system/user/index','',0,0,'C','1','1','system:user:list','user','admin','2024-06-13 16:06:26','',null,'用户管理菜单');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (101,'角色管理',1,2,'role','system/role/index','',0,0,'C','1','1','system:role:list','peoples','admin','2024-06-13 16:06:26','',null,'角色管理菜单');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (102,'菜单管理',1,3,'menu','system/menu/index','',0,0,'C','1','1','system:menu:list','tree-table','admin','2024-06-13 16:06:26','',null,'菜单管理菜单');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (103,'部门管理',1,4,'dept','system/dept/index','',0,0,'C','1','1','system:dept:list','tree','admin','2024-06-13 16:06:26','',null,'部门管理菜单');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (104,'岗位管理',1,5,'post','system/post/index','',0,0,'C','1','1','system:post:list','post','admin','2024-06-13 16:06:26','',null,'岗位管理菜单');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (105,'字典管理',1,6,'dict','system/dict/index','',0,0,'C','1','1','system:dict:list','dict','admin','2024-06-13 16:06:26','',null,'字典管理菜单');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (106,'参数设置',1,7,'config','system/config/index','',0,0,'C','1','1','system:config:list','edit','admin','2024-06-13 16:06:26','',null,'参数设置菜单');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (107,'通知公告',1,8,'notice','system/notice/index','',0,0,'C','1','1','system:notice:list','message','admin','2024-06-13 16:06:26','',null,'通知公告菜单');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (108,'日志管理',0,140,'log','','',0,0,'M','1','1','','log','admin','2024-06-13 16:06:27','admin','2024-08-20 13:46:16','日志管理菜单');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (109,'在线用户',2,1,'online','monitor/online/index','',0,0,'C','1','1','monitor:online:list','online','admin','2024-06-13 16:06:27','',null,'在线用户菜单');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (112,'缓存列表',2,6,'cacheList','monitor/cache/list','',0,0,'C','1','1','monitor:cache:list','redis-list','admin','2024-06-13 16:06:27','',null,'缓存列表菜单');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (113,'缓存监控',2,5,'cache','monitor/cache/index','',0,0,'C','1','1','monitor:cache:list','redis','admin','2024-06-13 16:06:27','',null,'缓存监控菜单');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (115,'代码生成',0,130,'gen','tool/gen/index','',0,0,'C','1','1','tool:gen:list','code','admin','2024-06-13 16:06:27','admin','2024-08-20 13:46:06','代码生成菜单');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (118,'文件管理',1,10,'oss','system/oss/index','',0,0,'C','1','1','system:oss:list','upload','admin','2024-06-13 16:06:27','',null,'文件管理菜单');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (500,'操作日志',108,1,'operlog','monitor/operlog/index','',0,0,'C','1','1','monitor:operlog:list','form','admin','2024-06-13 16:06:27','',null,'操作日志菜单');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (501,'登录日志',108,2,'logininfor','monitor/logininfor/index','',0,0,'C','1','1','monitor:logininfor:list','logininfor','admin','2024-06-13 16:06:27','',null,'登录日志菜单');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1001,'用户查询',100,1,'','','',0,0,'F','1','1','system:user:query','#','admin','2024-06-13 16:06:27','',null,'');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1002,'用户新增',100,2,'','','',0,0,'F','1','1','system:user:add','#','admin','2024-06-13 16:06:27','',null,'');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1003,'用户修改',100,3,'','','',0,0,'F','1','1','system:user:edit','#','admin','2024-06-13 16:06:27','',null,'');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1004,'用户删除',100,4,'','','',0,0,'F','1','1','system:user:remove','#','admin','2024-06-13 16:06:27','',null,'');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1005,'用户导出',100,5,'','','',0,0,'F','1','1','system:user:export','#','admin','2024-06-13 16:06:27','',null,'');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1006,'用户导入',100,6,'','','',0,0,'F','1','1','system:user:import','#','admin','2024-06-13 16:06:27','',null,'');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1007,'重置密码',100,7,'','','',0,0,'F','1','1','system:user:resetPwd','#','admin','2024-06-13 16:06:27','',null,'');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1008,'角色查询',101,1,'','','',0,0,'F','1','1','system:role:query','#','admin','2024-06-13 16:06:27','',null,'');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1009,'角色新增',101,2,'','','',0,0,'F','1','1','system:role:add','#','admin','2024-06-13 16:06:27','',null,'');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1010,'角色修改',101,3,'','','',0,0,'F','1','1','system:role:edit','#','admin','2024-06-13 16:06:28','',null,'');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1011,'角色删除',101,4,'','','',0,0,'F','1','1','system:role:remove','#','admin','2024-06-13 16:06:28','',null,'');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1012,'角色导出',101,5,'','','',0,0,'F','1','1','system:role:export','#','admin','2024-06-13 16:06:28','',null,'');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1013,'菜单查询',102,1,'','','',0,0,'F','1','1','system:menu:query','#','admin','2024-06-13 16:06:28','',null,'');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1014,'菜单新增',102,2,'','','',0,0,'F','1','1','system:menu:add','#','admin','2024-06-13 16:06:28','',null,'');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1015,'菜单修改',102,3,'','','',0,0,'F','1','1','system:menu:edit','#','admin','2024-06-13 16:06:28','',null,'');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1016,'菜单删除',102,4,'','','',0,0,'F','1','1','system:menu:remove','#','admin','2024-06-13 16:06:28','',null,'');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1017,'部门查询',103,1,'','','',0,0,'F','1','1','system:dept:query','#','admin','2024-06-13 16:06:28','',null,'');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1018,'部门新增',103,2,'','','',0,0,'F','1','1','system:dept:add','#','admin','2024-06-13 16:06:28','',null,'');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1019,'部门修改',103,3,'','','',0,0,'F','1','1','system:dept:edit','#','admin','2024-06-13 16:06:28','',null,'');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1020,'部门删除',103,4,'','','',0,0,'F','1','1','system:dept:remove','#','admin','2024-06-13 16:06:28','',null,'');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1021,'岗位查询',104,1,'','','',0,0,'F','1','1','system:post:query','#','admin','2024-06-13 16:06:28','',null,'');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1022,'岗位新增',104,2,'','','',0,0,'F','1','1','system:post:add','#','admin','2024-06-13 16:06:28','',null,'');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1023,'岗位修改',104,3,'','','',0,0,'F','1','1','system:post:edit','#','admin','2024-06-13 16:06:28','',null,'');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1024,'岗位删除',104,4,'','','',0,0,'F','1','1','system:post:remove','#','admin','2024-06-13 16:06:28','',null,'');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1025,'岗位导出',104,5,'','','',0,0,'F','1','1','system:post:export','#','admin','2024-06-13 16:06:28','',null,'');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1026,'字典查询',105,1,'#','','',0,0,'F','1','1','system:dict:query','#','admin','2024-06-13 16:06:28','',null,'');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1027,'字典新增',105,2,'#','','',0,0,'F','1','1','system:dict:add','#','admin','2024-06-13 16:06:28','',null,'');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1028,'字典修改',105,3,'#','','',0,0,'F','1','1','system:dict:edit','#','admin','2024-06-13 16:06:28','',null,'');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1029,'字典删除',105,4,'#','','',0,0,'F','1','1','system:dict:remove','#','admin','2024-06-13 16:06:28','',null,'');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1030,'字典导出',105,5,'#','','',0,0,'F','1','1','system:dict:export','#','admin','2024-06-13 16:06:29','',null,'');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1031,'参数查询',106,1,'#','','',0,0,'F','1','1','system:config:query','#','admin','2024-06-13 16:06:29','',null,'');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1032,'参数新增',106,2,'#','','',0,0,'F','1','1','system:config:add','#','admin','2024-06-13 16:06:29','',null,'');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1033,'参数修改',106,3,'#','','',0,0,'F','1','1','system:config:edit','#','admin','2024-06-13 16:06:29','',null,'');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1034,'参数删除',106,4,'#','','',0,0,'F','1','1','system:config:remove','#','admin','2024-06-13 16:06:29','',null,'');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1035,'参数导出',106,5,'#','','',0,0,'F','1','1','system:config:export','#','admin','2024-06-13 16:06:29','',null,'');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1036,'公告查询',107,1,'#','','',0,0,'F','1','1','system:notice:query','#','admin','2024-06-13 16:06:29','',null,'');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1037,'公告新增',107,2,'#','','',0,0,'F','1','1','system:notice:add','#','admin','2024-06-13 16:06:29','',null,'');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1038,'公告修改',107,3,'#','','',0,0,'F','1','1','system:notice:edit','#','admin','2024-06-13 16:06:29','',null,'');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1039,'公告删除',107,4,'#','','',0,0,'F','1','1','system:notice:remove','#','admin','2024-06-13 16:06:29','',null,'');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1040,'操作查询',500,1,'#','','',0,0,'F','1','1','monitor:operlog:query','#','admin','2024-06-13 16:06:29','',null,'');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1041,'操作删除',500,2,'#','','',0,0,'F','1','1','monitor:operlog:remove','#','admin','2024-06-13 16:06:29','',null,'');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1042,'日志导出',500,4,'#','','',0,0,'F','1','1','monitor:operlog:export','#','admin','2024-06-13 16:06:29','',null,'');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1043,'登录查询',501,1,'#','','',0,0,'F','1','1','monitor:logininfor:query','#','admin','2024-06-13 16:06:29','',null,'');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1044,'登录删除',501,2,'#','','',0,0,'F','1','1','monitor:logininfor:remove','#','admin','2024-06-13 16:06:29','',null,'');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1045,'日志导出',501,3,'#','','',0,0,'F','1','1','monitor:logininfor:export','#','admin','2024-06-13 16:06:29','',null,'');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1046,'在线查询',109,1,'#','','',0,0,'F','1','1','monitor:online:query','#','admin','2024-06-13 16:06:29','',null,'');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1047,'批量强退',109,2,'#','','',0,0,'F','1','1','monitor:online:batchLogout','#','admin','2024-06-13 16:06:29','',null,'');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1048,'单条强退',109,3,'#','','',0,0,'F','1','1','monitor:online:forceLogout','#','admin','2024-06-13 16:06:30','',null,'');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1050,'账户解锁',501,4,'#','','',0,0,'F','1','1','monitor:logininfor:unlock','#','admin','2024-06-13 16:06:29','',null,'');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1055,'生成查询',115,1,'#','','',0,0,'F','1','1','tool:gen:query','#','admin','2024-06-13 16:06:30','',null,'');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1056,'生成修改',115,2,'#','','',0,0,'F','1','1','tool:gen:edit','#','admin','2024-06-13 16:06:30','',null,'');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1057,'生成删除',115,3,'#','','',0,0,'F','1','1','tool:gen:remove','#','admin','2024-06-13 16:06:30','',null,'');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1058,'导入代码',115,2,'#','','',0,0,'F','1','1','tool:gen:import','#','admin','2024-06-13 16:06:30','',null,'');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1059,'预览代码',115,4,'#','','',0,0,'F','1','1','tool:gen:preview','#','admin','2024-06-13 16:06:30','',null,'');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1060,'生成代码',115,5,'#','','',0,0,'F','1','1','tool:gen:code','#','admin','2024-06-13 16:06:30','',null,'');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1600,'文件查询',118,1,'#','','',0,0,'F','1','1','system:oss:query','#','admin','2024-06-13 16:06:30','',null,'');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1601,'文件上传',118,2,'#','','',0,0,'F','1','1','system:oss:upload','#','admin','2024-06-13 16:06:30','',null,'');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1602,'文件下载',118,3,'#','','',0,0,'F','1','1','system:oss:download','#','admin','2024-06-13 16:06:30','',null,'');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1603,'文件删除',118,4,'#','','',0,0,'F','1','1','system:oss:remove','#','admin','2024-06-13 16:06:30','',null,'');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1604,'配置添加',118,5,'#','','',0,0,'F','1','1','system:oss:add','#','admin','2024-06-13 16:06:30','',null,'');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1605,'配置编辑',118,6,'#','','',0,0,'F','1','1','system:oss:edit','#','admin','2024-06-13 16:06:30','',null,'');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1808758090157985794,'基础资料',0,100,'basic',null,null,0,0,'M','1','1',null,'excel','admin','2024-07-04 15:01:48','admin','2024-08-20 13:45:39','');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1809059968309743618,'往来单位',1808758090157985794,6,'merchant','wms/basic/merchant/index',null,0,0,'C','1','1','wms:merchant:list','documentation','admin','2024-07-05 11:58:12','admin','2026-05-08 16:37:55','第二阶段往来单位入口');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1809059968309743619,'往来单位查询',1809059968309743618,1,'#','',null,0,0,'F','1','1','wms:merchant:list','#','admin','2024-07-05 11:58:12','admin','2024-08-30 10:43:54','');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1809059968309743621,'往来单位修改',1809059968309743618,3,'#','',null,0,0,'F','1','1','wms:merchant:edit','#','admin','2024-07-05 11:58:12','',null,'');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1813458070128599041,'仓储布局',0,3,'layout','wms/basic/layout/index',null,0,0,'C','0','1','wms:warehouse:list','documentation','admin','2024-07-17 14:17:51','admin','2026-05-08 16:49:17','布局浏览，不替代仓库/库区维护');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1813820131794837506,'器材管理',1808758090157985794,0,'item','wms/basic/item/index',null,0,0,'C','1','1','wms:item:list','documentation','admin','2024-07-18 14:16:33','admin','2026-05-08 16:37:00','第二阶段器材主档入口');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1815207165755183105,'编辑入库单',0,1000,'receiptOrderEdit','wms/order/receipt/edit',null,0,0,'C','0','1','wms:receipt:edit','#','admin','2024-07-22 10:08:08','admin','2024-08-27 16:43:28','');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1818466281474822145,'入库作业',2060000000000001001,1,'receiptOrder','wms/order/receipt/index',null,0,0,'C','1','1','wms:receipt:all','exit-fullscreen','admin','2024-07-31 09:58:42','admin','2024-08-30 08:58:25','第二阶段作业中心入口');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1818854933803638785,'出库作业',2060000000000001001,2,'shipmentOrder','wms/order/shipment/index',null,0,0,'C','1','1','wms:shipment:all','fullscreen','admin','2024-08-01 11:43:04','admin','2024-08-30 08:58:35','第二阶段作业中心入口');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1818855673632727042,'编辑出库单',0,1000,'shipmentOrderEdit','wms/order/shipment/edit',null,0,0,'C','0','1','wms:shipment:edit','#','admin','2024-08-01 11:46:00','admin','2024-08-27 16:43:37','');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1821075355068559361,'库存流水',2060000000000001003,3,'inventoryHistory','wms/inventory/history',null,0,0,'C','1','1','wms:inventoryHistory:all','list','admin','2024-08-07 14:46:13','admin','2024-08-30 08:58:13','第二阶段库存中心入口');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1822862323595145218,'编辑调拨单',0,1000,'movementOrderEdit','wms/order/movement/edit',null,0,0,'C','0','1','wms:movement:edit','#','admin','2024-08-12 13:07:00','admin','2024-08-27 16:43:50','调拨作业隐藏编辑页');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1823187248797270018,'库存盘点',2060000000000001002,3,'checkOrder','wms/order/check/index',null,0,0,'C','1','1','wms:check:all','example','admin','2024-08-13 10:38:08','admin','2024-08-30 08:58:57','第二阶段库内管理入口');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1823190638784757762,'编辑盘库单',0,1000,'checkOrderEdit','wms/order/check/edit',null,0,0,'C','0','1','wms:check:edit','#','admin','2024-08-13 10:51:36','admin','2024-08-27 16:43:44','');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1825769009480142850,'库存明细',2060000000000001003,2,'inventoryDetail','wms/itemInstance/index',null,0,0,'C','1','1','wms:inventoryDetail:all','table','admin','2024-08-20 13:37:08','admin','2026-05-21 14:29:50','第二阶段库存中心入口；2026-05-21 统一到器材实例台账页');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1829349433573822466,'仓库库区查询',1813458070128599041,1,'',null,null,0,0,'F','1','1','wms:warehouse:list','#','admin','2024-08-30 10:44:27','admin','2024-08-30 10:44:27','');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1829350022131142658,'仓库库区编辑',1813458070128599041,2,'',null,null,0,0,'F','1','1','wms:warehouse:edit','#','admin','2024-08-30 10:46:48','admin','2024-08-30 10:46:48','');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1829351081448755202,'商品查询',1813820131794837506,1,'',null,null,0,0,'F','1','1','wms:item:list','#','admin','2024-08-30 10:51:00','admin','2024-08-30 10:51:00','');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1829351166857367553,'商品编辑',1813820131794837506,2,'',null,null,0,0,'F','1','1','wms:item:edit','#','admin','2024-08-30 10:51:21','admin','2024-08-30 10:51:21','');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1900000000000003001,'货架管理',1808758090157985794,3,'rack','wms/basic/rack/index',null,0,0,'C','1','1','wms:rack:list','documentation','admin','2026-04-30 14:04:34','admin','2026-05-08 16:36:30','兼容保留：第二阶段主入口已收口到仓储布局');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1900000000000003002,'货位管理',1808758090157985794,4,'location','wms/basic/location/index',null,0,0,'C','1','1','wms:location:list','documentation','admin','2026-04-30 14:04:34','admin','2026-05-08 16:36:40','兼容保留：第二阶段主入口已收口到仓储布局');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1900000000000003101,'货架查询',1900000000000003001,1,'#',null,null,0,0,'F','1','1','wms:rack:list','#','admin','2026-04-30 14:04:34','admin','2026-04-30 14:04:34','货架查询权限');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1900000000000003102,'货架编辑',1900000000000003001,2,'#',null,null,0,0,'F','1','1','wms:rack:edit','#','admin','2026-04-30 14:04:34','admin','2026-04-30 14:04:34','货架编辑权限');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1900000000000003201,'货位查询',1900000000000003002,1,'#',null,null,0,0,'F','1','1','wms:location:list','#','admin','2026-04-30 14:04:34','admin','2026-04-30 14:04:34','货位查询权限');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1900000000000003202,'货位编辑',1900000000000003002,2,'#',null,null,0,0,'F','1','1','wms:location:edit','#','admin','2026-04-30 14:04:34','admin','2026-04-30 14:04:34','货位编辑权限');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1900000000000005201,'箱体管理',1808758090157985794,5,'box','wms/basic/box/index',null,0,0,'C','1','1','wms:box:list','documentation','admin','2026-04-30 17:27:59','admin','2026-05-08 16:36:52','兼容保留：第二阶段主入口已收口到仓储布局');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1900000000000005202,'箱体查询',1900000000000005201,1,'#',null,null,0,0,'F','1','1','wms:box:list','#','admin','2026-04-30 17:27:59','admin','2026-04-30 17:27:59','箱体查询权限');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1900000000000005203,'箱体编辑',1900000000000005201,2,'#',null,null,0,0,'F','1','1','wms:box:edit','#','admin','2026-04-30 17:27:59','admin','2026-04-30 17:27:59','箱体编辑权限');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1900000000000006201,'器材借用',2060000000000001001,4,'borrow-record','wms/business/borrow-record/index',null,0,0,'C','1','1','wms:borrowRecord:list','documentation','admin','2026-04-30 17:44:30','admin','2026-04-30 17:44:30','第二阶段作业中心入口');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1900000000000006202,'借还查询',1900000000000006201,1,'#',null,null,0,0,'F','1','1','wms:borrowRecord:list','#','admin','2026-04-30 17:44:30','admin','2026-04-30 17:44:30','借还查询权限');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (1900000000000006203,'借还编辑',1900000000000006201,2,'#',null,null,0,0,'F','1','1','wms:borrowRecord:edit','#','admin','2026-04-30 17:44:30','admin','2026-04-30 17:44:30','借还编辑权限');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (2060000000000001001,'作业中心',0,101,'operation',null,null,0,0,'M','1','1',null,'guide','admin','2026-05-07 14:23:08','admin','2026-05-07 14:23:08','第二阶段作业中心目录');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (2060000000000001002,'库内管理',0,102,'internal',null,null,0,0,'M','1','1',null,'drag','admin','2026-05-07 14:23:08','admin','2026-05-07 14:23:08','第二阶段库内管理目录');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (2060000000000001003,'库存中心',0,103,'inventoryCenter',null,null,0,0,'M','1','1',null,'chart','admin','2026-05-07 14:23:08','admin','2026-05-07 14:23:08','第二阶段库存中心目录');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (2060000000000001101,'仓库管理',1808758090157985794,1,'warehouse','wms/basic/warehouse/index',null,0,0,'C','1','1','wms:warehouse:list','documentation','admin','2026-05-08 15:47:46','admin','2026-05-08 15:47:46','基础资料仓库管理入口');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (2060000000000001102,'库区管理',1808758090157985794,2,'area','wms/basic/area/index',null,0,0,'C','1','1','wms:area:list','documentation','admin','2026-05-08 15:47:46','admin','2026-05-08 15:47:46','基础资料库区管理入口');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (2060000000000001103,'库区查询',2060000000000001102,1,'#',null,null,0,0,'F','1','1','wms:area:list','#','admin','2026-05-08 15:47:46','admin','2026-05-08 15:47:46','库区查询权限');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (2060000000000001104,'库区编辑',2060000000000001102,2,'#',null,null,0,0,'F','1','1','wms:area:edit','#','admin','2026-05-08 15:47:46','admin','2026-05-08 15:47:46','库区编辑权限');
INSERT INTO wms_advence_master.sys_menu (menu_id,menu_name,parent_id,order_num,path,component,query_param,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark)
 VALUES (2060000000000002103,'调拨作业',2060000000000001001,3,'transferOrder','wms/order/movement/index',null,0,0,'C','1','1','wms:movement:all','switch','admin','2026-05-07 14:23:08','admin','2026-05-07 14:23:08','调拨作业入口，复用现有调拨单流程');


-- Name: sys_notice; Type: Table; Schema: wms_advence_master;

SET search_path = wms_advence_master;
CREATE TABLE sys_notice (
    notice_id bigint NOT NULL,
    notice_title varchar(50) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci NOT NULL,
    notice_type character(1) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci NOT NULL,
    notice_content longblob,
    status character(1) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT '0'::bpchar,
    create_by varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar,
    create_time timestamp(0) without time zone DEFAULT NULL::timestamp without time zone,
    update_by varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar,
    update_time timestamp(0) without time zone DEFAULT NULL::timestamp without time zone,
    remark varchar(255) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar
)
CHARACTER SET = "UTF8" COLLATE = "utf8mb4_general_ci"
WITH (orientation=row, compression=no, fillfactor=80);
COMMENT ON TABLE sys_notice IS '通知公告表';
COMMENT ON COLUMN sys_notice.notice_id IS '公告ID';
COMMENT ON COLUMN sys_notice.notice_title IS '公告标题';
COMMENT ON COLUMN sys_notice.notice_type IS '公告类型（1通知 2公告）';
COMMENT ON COLUMN sys_notice.notice_content IS '公告内容';
COMMENT ON COLUMN sys_notice.status IS '公告状态（0正常 1关闭）';
COMMENT ON COLUMN sys_notice.create_by IS '创建者';
COMMENT ON COLUMN sys_notice.create_time IS '创建时间';
COMMENT ON COLUMN sys_notice.update_by IS '更新者';
COMMENT ON COLUMN sys_notice.update_time IS '更新时间';
COMMENT ON COLUMN sys_notice.remark IS '备注';
ALTER TABLE sys_notice ADD CONSTRAINT sys_notice_pk PRIMARY KEY USING btree  (notice_id);

--Data for  Name: sys_notice; Type: Table; Schema: wms_advence_master;

INSERT INTO wms_advence_master.sys_notice (notice_id,notice_title,notice_type,notice_content,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1,'温馨提醒：2018-07-01 新版本发布啦','2',utl_raw.cast_to_raw('新版本内容'),'1','admin','2024-06-13 16:06:38','',null,'管理员');
INSERT INTO wms_advence_master.sys_notice (notice_id,notice_title,notice_type,notice_content,status,create_by,create_time,update_by,update_time,remark)
 VALUES (2,'维护通知：2018-07-01 系统凌晨维护','1',utl_raw.cast_to_raw('维护内容'),'1','admin','2024-06-13 16:06:38','',null,'管理员');


-- Name: sys_oper_log; Type: Table; Schema: wms_advence_master;

SET search_path = wms_advence_master;
CREATE TABLE sys_oper_log (
    oper_id bigint NOT NULL,
    title varchar(50) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar,
    business_type integer DEFAULT 0,
    method varchar(100) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar,
    request_method varchar(10) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar,
    operator_type integer DEFAULT 0,
    oper_name varchar(50) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar,
    dept_name varchar(50) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar,
    oper_url varchar(255) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar,
    oper_ip varchar(128) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar,
    oper_location varchar(255) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar,
    oper_param varchar(2000) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar,
    json_result varchar(2000) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar,
    status integer DEFAULT 0,
    error_msg varchar(2000) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar,
    oper_time timestamp(0) without time zone DEFAULT NULL::timestamp without time zone
)
CHARACTER SET = "UTF8" COLLATE = "utf8mb4_general_ci"
WITH (orientation=row, compression=no, fillfactor=80);
COMMENT ON TABLE sys_oper_log IS '操作日志记录';
COMMENT ON COLUMN sys_oper_log.oper_id IS '日志主键';
COMMENT ON COLUMN sys_oper_log.title IS '模块标题';
COMMENT ON COLUMN sys_oper_log.business_type IS '业务类型（0其它 1新增 2修改 3删除）';
COMMENT ON COLUMN sys_oper_log.method IS '方法名称';
COMMENT ON COLUMN sys_oper_log.request_method IS '请求方式';
COMMENT ON COLUMN sys_oper_log.operator_type IS '操作类别（0其它 1后台用户 2手机端用户）';
COMMENT ON COLUMN sys_oper_log.oper_name IS '操作人员';
COMMENT ON COLUMN sys_oper_log.dept_name IS '部门名称';
COMMENT ON COLUMN sys_oper_log.oper_url IS '请求URL';
COMMENT ON COLUMN sys_oper_log.oper_ip IS '主机地址';
COMMENT ON COLUMN sys_oper_log.oper_location IS '操作地点';
COMMENT ON COLUMN sys_oper_log.oper_param IS '请求参数';
COMMENT ON COLUMN sys_oper_log.json_result IS '返回参数';
COMMENT ON COLUMN sys_oper_log.status IS '操作状态（0异常 1正常）';
COMMENT ON COLUMN sys_oper_log.error_msg IS '错误消息';
COMMENT ON COLUMN sys_oper_log.oper_time IS '操作时间';
CREATE INDEX tb0_sys_oper_log_idx_sys_oper_log_ot ON wms_advence_master.sys_oper_log USING btree (oper_time) TABLESPACE pg_default;
CREATE INDEX tb0_sys_oper_log_idx_sys_oper_log_s ON wms_advence_master.sys_oper_log USING btree (status) TABLESPACE pg_default;
CREATE INDEX tb0_sys_oper_log_idx_sys_oper_log_bt ON wms_advence_master.sys_oper_log USING btree (business_type) TABLESPACE pg_default;
ALTER TABLE sys_oper_log ADD CONSTRAINT sys_oper_log_pk PRIMARY KEY USING btree  (oper_id);

--Data for  Name: sys_oper_log; Type: Table; Schema: wms_advence_master;

INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2049759030667456514,'入库单',2,'com.ruoyi.wms.controller.ReceiptOrderController.doWarehousing()','POST',1,'admin','研发部门','/wms/receiptOrder/warehousing','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2049759028125708290","receiptOrderNo":"RK04309955","receiptOrderType":2,"merchantId":null,"orderNo":null,"totalQuantity":"1","payableAmount":null,"receiptOrderStatus":1,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","remark":null,"details":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"receiptOrderId":null,"skuId":"1829399118304964609","quantity":"1","amount":null,"batchNo":null,"productionDate":null,"expirationDate":null,"remark":null,"warehouseId":"1828364459110469633","areaId":"1828364518342430721"}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-04-30 15:53:30');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2049770937386307585,'入库单',2,'com.ruoyi.wms.controller.ReceiptOrderController.doWarehousing()','POST',1,'admin','研发部门','/wms/receiptOrder/warehousing','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2049770935654060034","receiptOrderNo":"RK04309512","receiptOrderType":2,"merchantId":null,"orderNo":null,"totalQuantity":"6","payableAmount":null,"receiptOrderStatus":1,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","remark":null,"details":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"receiptOrderId":null,"skuId":"1829399118304964609","quantity":"6","amount":null,"batchNo":null,"productionDate":null,"expirationDate":null,"remark":null,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","generateItemInstance":1,"generatedInstanceQuantity":0}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-04-30 16:40:49');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2051911939979034625,'箱体',1,'com.ruoyi.wms.controller.BoxController.add()','POST',1,'admin','研发部门','/wms/box','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"boxCode":"BOX2051911939723182080","boxName":"一箱水果","boxStatus":"idle","warehouseId":null,"areaId":null,"rackId":null,"locationId":null,"remark":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-06 14:28:24');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2051912397502103553,'箱体',1,'com.ruoyi.wms.controller.BoxController.add()','POST',1,'admin','研发部门','/wms/box','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"boxCode":"BOX2051912397434994688","boxName":"一箱水果","boxStatus":"idle","warehouseId":"1828364740028174337","areaId":"1829397566185992193","rackId":"1900000000000000001","locationId":"1900000000000000101","remark":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-06 14:30:13');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2051915098029899777,'物料',1,'com.ruoyi.wms.controller.ItemController.add()','POST',1,'admin','研发部门','/wms/item','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"ids":null,"itemCode":null,"itemName":"深海大魔王","itemCategory":"1829398007993004034","unit":null,"itemBrand":null,"itemType":"normal","trackingMode":"instance","allowBox":1,"specLevel":"single","equipmentName":null,"defaultQualityGrade":"grade_a","productMarkRule":null,"modelText":null,"remark":null,"sku":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"skuName":"大","itemId":"2051915097832767489","barcode":"","skuCode":"","specModel":"","defaultUnitPrice":null,"length":"10","width":"20","height":"30","grossWeight":null,"netWeight":null,"costPrice":"5","sellingPrice":"10","itemName":null,"itemCode":null,"equipmentName":null,"itemCategory":null,"itemBrand":null}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-06 14:40:57');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2051915277218955266,'入库单',2,'com.ruoyi.wms.controller.ReceiptOrderController.doWarehousing()','POST',1,'admin','研发部门','/wms/receiptOrder/warehousing','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2051915276543672322","receiptOrderNo":"RK05068886","receiptOrderType":2,"merchantId":"1828354153193836545","orderNo":null,"basisNo":null,"dispatchMode":null,"noticeOrg":null,"receiveUnit":null,"purchaseDate":null,"receiptDate":null,"purchaserName":null,"acceptorName":null,"keeperName":null,"totalQuantity":"10","payableAmount":"0","receiptOrderStatus":1,"warehouseId":"1828364740028174337","areaId":"1829397566185992193","remark":null,"details":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"receiptOrderId":null,"skuId":"2051915097966985218","quantity":"10","amount":"0","equipmentCode":null,"specModel":"","productMark":null,"qualityGrade":"grade_a","unitPrice":null,"lineAmount":"0","batchNo":null,"productionDate":null,"expirationDate":null,"remark":null,"warehouseId":"1828364740028174337","areaId":"1829397566185992193","generateItemInstance":1,"generatedInstanceQuantity":0}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-06 14:41:39');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2051926764859789314,'菜单管理',1,'com.ruoyi.system.controller.system.SysMenuController.add()','POST',1,'admin','研发部门','/system/menu','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"menuId":null,"parentId":0,"menuName":"总账","orderNum":90,"path":"ledger","component":"wms/inventory/ledger","queryParam":null,"isFrame":"0","isCache":"0","menuType":"C","visible":"1","status":"1","icon":"excel","remark":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-06 15:27:18');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2052668955555102722,'菜单管理',2,'com.ruoyi.system.controller.system.SysMenuController.edit()','PUT',1,'admin','研发部门','/system/menu','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":"2026-04-30 14:04:34","updateBy":null,"updateTime":null,"menuId":"1900000000000003001","parentId":"1808758090157985794","menuName":"货架管理","orderNum":3,"path":"rack","component":"wms/basic/rack/index","queryParam":null,"isFrame":"0","isCache":"0","menuType":"C","visible":"1","status":"1","perms":"wms:rack:list","icon":"documentation","remark":"兼容保留：第二阶段主入口已收口到仓储布局"}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-08 16:36:30');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2051927929655758850,'入库单',2,'com.ruoyi.wms.controller.ReceiptOrderController.doWarehousing()','POST',1,'admin','研发部门','/wms/receiptOrder/warehousing','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"1829399579699376129","receiptOrderNo":"RK08302046","receiptOrderType":2,"merchantId":null,"orderNo":null,"basisNo":null,"dispatchMode":null,"noticeOrg":null,"receiveUnit":null,"purchaseDate":null,"receiptDate":null,"purchaserName":null,"acceptorName":null,"keeperName":null,"totalQuantity":"998","payableAmount":"0","receiptOrderStatus":1,"warehouseId":"1828364740028174337","areaId":null,"remark":null,"details":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"1829399580026531841","receiptOrderId":"1829399579699376129","skuId":"1829399118304964609","quantity":"100","amount":"0","equipmentCode":null,"specModel":null,"productMark":null,"qualityGrade":null,"unitPrice":null,"lineAmount":"0","batchNo":null,"productionDate":null,"expirationDate":null,"remark":null,"warehouseId":"1828364740028174337","areaId":"1829397566185992193","generateItemInstance":0,"generatedInstanceQuantity":0},{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"1829399580026531842","receiptOrderId":"1829399579699376129","skuId":"1829399118304964610","quantity":"100","amount":"0","equipmentCode":null,"specModel":null,"productMark":null,"qualityGrade":null,"unitPrice":null,"lineAmount":"0","batchNo":null,"productionDate":null,"expirationDate":null,"remark":null,"warehouseId":"1828364740028174337","areaId":"1829397566185992193","generateItemInstance":0,"generatedInstanceQuantity":0},{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"1829399580026531843","receiptOrderId":"1829399579699376129","skuId":"1829398702011904001","quantity":"100","amount":"0","equipmentCode":null,"specModel":null,"productMark":null,"qualityGrade":null,"unitPrice":null,"lineAmount":"0","batchNo":null,"productionDate":null,"expirationDate":null,"remark":null,"warehouseId":"1828364740028174337","areaId":"1829397661719654401","generateItemInstance":0,"generatedInstanceQuantity":0},{"cre','',0,'入库单已完成入库','2026-05-06 15:31:56');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2052571160655491074,'入库单',2,'com.ruoyi.wms.controller.ReceiptOrderController.doWarehousing()','POST',1,'admin','研发部门','/wms/receiptOrder/warehousing','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2052571159107792897","receiptOrderNo":"RK05080728","receiptOrderType":2,"merchantId":null,"orderNo":null,"basisNo":null,"dispatchMode":null,"noticeOrg":null,"receiveUnit":null,"purchaseDate":null,"receiptDate":null,"purchaserName":null,"acceptorName":null,"keeperName":null,"totalQuantity":"1","payableAmount":"0","receiptOrderStatus":1,"warehouseId":"1828364740028174337","areaId":"1829397566185992193","remark":null,"details":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"receiptOrderId":null,"skuId":"2051915097966985218","quantity":"1","amount":"0","equipmentCode":"IT051915097832767489","specModel":"大","productMark":null,"qualityGrade":"grade_a","unitPrice":null,"lineAmount":"0","batchNo":null,"productionDate":null,"expirationDate":null,"remark":null,"warehouseId":"1828364740028174337","areaId":"1829397566185992193","rackId":"1900000000000000001","locationId":"1900000000000000101","generateItemInstance":1,"generatedInstanceQuantity":0}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-08 10:07:54');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2052596719624216577,'装箱',2,'com.ruoyi.wms.controller.BoxController.pack()','POST',1,'admin','研发部门','/wms/box/pack','0:0:0:0:0:0:0:1','内网IP','{"boxId":"2051912397434994690","itemInstanceIds":["2052571160454164482"]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-08 11:49:28');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2052635492739276802,'物料类型',3,'com.ruoyi.wms.controller.ItemCategoryController.remove()','DELETE',1,'admin','研发部门','/wms/itemCategory/1828364988754595841','0:0:0:0:0:0:0:1','内网IP','{}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-08 14:23:32');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2052635506840526849,'物料类型',3,'com.ruoyi.wms.controller.ItemCategoryController.remove()','DELETE',1,'admin','研发部门','/wms/itemCategory/1828405743737016322','0:0:0:0:0:0:0:1','内网IP','{}','',0,'删除失败！请先删除该分类下的子分类！','2026-05-08 14:23:36');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2052635524322385921,'物料类型',3,'com.ruoyi.wms.controller.ItemCategoryController.remove()','DELETE',1,'admin','研发部门','/wms/itemCategory/1828405773474631681','0:0:0:0:0:0:0:1','内网IP','{}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-08 14:23:40');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2052635532165734401,'物料类型',3,'com.ruoyi.wms.controller.ItemCategoryController.remove()','DELETE',1,'admin','研发部门','/wms/itemCategory/1828405825714688001','0:0:0:0:0:0:0:1','内网IP','{}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-08 14:23:42');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2052635565422370818,'物料类型',2,'com.ruoyi.wms.controller.ItemCategoryController.edit()','PUT',1,'admin','研发部门','/wms/itemCategory','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"1828408600515219457","parentId":-1,"categoryName":"健身器材","orderNum":null,"status":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-08 14:23:50');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2052636524890382338,'物料',1,'com.ruoyi.wms.controller.ItemController.add()','POST',1,'admin','研发部门','/wms/item','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"ids":null,"itemCode":null,"itemName":"惠普","itemCategory":"1828365014901886978","unit":"台","itemBrand":"1828407291103842306","itemType":"normal","trackingMode":"batch","allowBox":0,"specLevel":null,"equipmentName":null,"equipmentType":null,"status":"1","defaultTrackingMode":"batch","defaultBelongUnit":null,"defaultQualityGrade":null,"productMarkRule":null,"modelText":null,"remark":null,"sku":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"skuName":"惠普6","itemId":"2052636524512894978","barcode":"","skuCode":"","specModel":"","defaultUnitPrice":null,"defaultQualityGrade":"grade_a","status":"1","volume":"0.125","length":"50","width":"50","height":"50","grossWeight":"5","netWeight":"5","costPrice":"50","sellingPrice":"100","itemName":null,"itemCode":null,"equipmentName":null,"itemCategory":null,"itemBrand":null}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-08 14:27:38');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2052636940990504961,'物料',2,'com.ruoyi.wms.controller.ItemController.edit()','PUT',1,'admin','研发部门','/wms/item','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2052636524512894978","ids":null,"itemCode":"","itemName":"惠普","itemCategory":"1828365014901886978","unit":"台","itemBrand":"1828407291103842306","itemType":"normal","trackingMode":"instance","allowBox":1,"specLevel":null,"equipmentName":null,"equipmentType":"","status":"1","defaultTrackingMode":"instance","defaultBelongUnit":"A部","defaultQualityGrade":"grade_a","productMarkRule":null,"modelText":null,"remark":null,"sku":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2052636524764553218","skuName":"惠普6","itemId":"2052636524512894978","barcode":"","skuCode":"","specModel":"","defaultUnitPrice":null,"defaultQualityGrade":"grade_a","status":"1","volume":"0.1250","length":"50.0","width":"50.0","height":"50.0","grossWeight":"5.000","netWeight":"5.000","costPrice":"50.00","sellingPrice":"100.00","itemName":null,"itemCode":null,"equipmentName":null,"itemCategory":null,"itemBrand":null}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-08 14:29:18');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2052668993853292545,'菜单管理',2,'com.ruoyi.system.controller.system.SysMenuController.edit()','PUT',1,'admin','研发部门','/system/menu','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":"2026-04-30 14:04:34","updateBy":null,"updateTime":null,"menuId":"1900000000000003002","parentId":"1808758090157985794","menuName":"货位管理","orderNum":4,"path":"location","component":"wms/basic/location/index","queryParam":null,"isFrame":"0","isCache":"0","menuType":"C","visible":"1","status":"1","perms":"wms:location:list","icon":"documentation","remark":"兼容保留：第二阶段主入口已收口到仓储布局"}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-08 16:36:40');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2052669044629536770,'菜单管理',2,'com.ruoyi.system.controller.system.SysMenuController.edit()','PUT',1,'admin','研发部门','/system/menu','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":"2026-04-30 17:27:59","updateBy":null,"updateTime":null,"menuId":"1900000000000005201","parentId":"1808758090157985794","menuName":"箱体管理","orderNum":5,"path":"box","component":"wms/basic/box/index","queryParam":null,"isFrame":"0","isCache":"0","menuType":"C","visible":"1","status":"1","perms":"wms:box:list","icon":"documentation","remark":"兼容保留：第二阶段主入口已收口到仓储布局"}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-08 16:36:52');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2052669081535217665,'菜单管理',2,'com.ruoyi.system.controller.system.SysMenuController.edit()','PUT',1,'admin','研发部门','/system/menu','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":"2024-07-18 14:16:33","updateBy":null,"updateTime":null,"menuId":"1813820131794837506","parentId":"1808758090157985794","menuName":"器材管理","orderNum":0,"path":"item","component":"wms/basic/item/index","queryParam":null,"isFrame":"0","isCache":"0","menuType":"C","visible":"1","status":"1","perms":"wms:item:list","icon":"documentation","remark":"第二阶段器材主档入口"}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-08 16:37:00');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2052669247684182017,'菜单管理',2,'com.ruoyi.system.controller.system.SysMenuController.edit()','PUT',1,'admin','研发部门','/system/menu','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":"2024-07-17 14:17:51","updateBy":null,"updateTime":null,"menuId":"1813458070128599041","parentId":0,"menuName":"仓储布局","orderNum":3,"path":"layout","component":"wms/basic/layout/index","queryParam":null,"isFrame":"0","isCache":"0","menuType":"C","visible":"1","status":"1","perms":"wms:warehouse:list","icon":"documentation","remark":"布局浏览，不替代仓库/库区维护"}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-08 16:37:40');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2052669311332745217,'菜单管理',2,'com.ruoyi.system.controller.system.SysMenuController.edit()','PUT',1,'admin','研发部门','/system/menu','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":"2024-07-05 11:58:12","updateBy":null,"updateTime":null,"menuId":"1809059968309743618","parentId":"1808758090157985794","menuName":"往来单位","orderNum":6,"path":"merchant","component":"wms/basic/merchant/index","queryParam":null,"isFrame":"0","isCache":"0","menuType":"C","visible":"1","status":"1","perms":"wms:merchant:list","icon":"documentation","remark":"第二阶段往来单位入口"}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-08 16:37:55');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2052669347097575425,'菜单管理',2,'com.ruoyi.system.controller.system.SysMenuController.edit()','PUT',1,'admin','研发部门','/system/menu','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":"2024-07-30 11:18:27","updateBy":null,"updateTime":null,"menuId":"1818123963605549057","parentId":"1808758090157985794","menuName":"品牌管理","orderNum":7,"path":"itemBrand","component":"wms/basic/itemBrand/index","queryParam":null,"isFrame":"0","isCache":"0","menuType":"C","visible":"1","status":"1","perms":"wms:itemBrand:list","icon":"documentation","remark":""}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-08 16:38:04');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2052671087318499330,'货架',1,'com.ruoyi.wms.controller.RackController.add()','POST',1,'admin','研发部门','/wms/rack','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"rackCode":null,"rackName":"A","warehouseId":"1828364459110469633","areaId":"1828364518342430721","rackStatus":"enabled","rackType":"standard","rowCount":5,"columnCount":6,"length":"200","width":"200","height":"200","orderNum":1,"remark":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-08 16:44:59');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2052672070442385410,'菜单管理',3,'com.ruoyi.system.controller.system.SysMenuController.remove()','DELETE',1,'admin','研发部门','/system/menu/1813458070128599041','0:0:0:0:0:0:0:1','内网IP','{}','{"code":601,"msg":"存在子菜单,不允许删除","data":null}',1,'','2026-05-08 16:48:53');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2052672172884066306,'菜单管理',2,'com.ruoyi.system.controller.system.SysMenuController.edit()','PUT',1,'admin','研发部门','/system/menu','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":"2024-07-17 14:17:51","updateBy":null,"updateTime":null,"menuId":"1813458070128599041","parentId":0,"menuName":"仓储布局","orderNum":3,"path":"layout","component":"wms/basic/layout/index","queryParam":null,"isFrame":"0","isCache":"0","menuType":"C","visible":"0","status":"1","perms":"wms:warehouse:list","icon":"documentation","remark":"布局浏览，不替代仓库/库区维护"}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-08 16:49:17');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2052918577095610370,'货架',1,'com.ruoyi.wms.controller.RackController.add()','POST',1,'admin','研发部门','/wms/rack','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"rackCode":null,"rackName":"A","warehouseId":"1828364459110469633","areaId":"1828364518342430721","rackStatus":"enabled","rackType":"standard","rowCount":2,"columnCount":3,"length":"200","width":"50","height":"200","orderNum":0,"remark":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-09 09:08:25');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2053029267710013442,'货架',3,'com.ruoyi.wms.controller.RackController.remove()','DELETE',1,'admin','研发部门','/wms/rack/2052919288873193473','0:0:0:0:0:0:0:1','内网IP','{}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-09 16:28:16');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2052921598265364481,'货架',1,'com.ruoyi.wms.controller.RackController.add()','POST',1,'admin','研发部门','/wms/rack','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"rackCode":null,"rackName":"B","warehouseId":"1828364459110469633","areaId":"1828364518342430721","rackStatus":"enabled","rackType":"standard","rowCount":2,"columnCount":3,"length":"200","width":"50","height":"200","orderNum":1,"remark":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-09 09:20:25');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2052921948800126977,'货架',1,'com.ruoyi.wms.controller.RackController.add()','POST',1,'admin','研发部门','/wms/rack','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"rackCode":null,"rackName":"B","warehouseId":"1828364459110469633","areaId":"1828364518342430721","rackStatus":"enabled","rackType":"standard","rowCount":2,"columnCount":3,"length":"200","width":"50","height":"200","orderNum":1,"remark":null}','',0,'同一库区下货架名称重复','2026-05-09 09:21:49');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2052922082527121409,'货架',1,'com.ruoyi.wms.controller.RackController.add()','POST',1,'admin','研发部门','/wms/rack','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"rackCode":null,"rackName":"C","warehouseId":"1828364459110469633","areaId":"1828364518342430721","rackStatus":"enabled","rackType":"standard","rowCount":3,"columnCount":3,"length":"900","width":"50","height":"900","orderNum":2,"remark":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-09 09:22:21');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2052922260910870529,'货架',2,'com.ruoyi.wms.controller.RackController.edit()','PUT',1,'admin','研发部门','/wms/rack','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2052922082288046081","rackCode":null,"rackName":"C","warehouseId":"1828364459110469633","areaId":"1828364518342430721","rackStatus":"enabled","rackType":"standard","rowCount":3,"columnCount":3,"length":"900","width":"50","height":"900","orderNum":2,"remark":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-09 09:23:03');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2052922646400962562,'货架',1,'com.ruoyi.wms.controller.RackController.add()','POST',1,'admin','研发部门','/wms/rack','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"rackCode":null,"rackName":"D","warehouseId":"1828364459110469633","areaId":"1828364518342430721","rackStatus":"enabled","rackType":"standard","rowCount":3,"columnCount":3,"length":"600","width":"50","height":"600","orderNum":0,"remark":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-09 09:24:35');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2052924212667305986,'货架',2,'com.ruoyi.wms.controller.RackController.edit()','PUT',1,'admin','研发部门','/wms/rack','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2052922082288046081","rackCode":null,"rackName":"C","warehouseId":"1828364459110469633","areaId":"1828364518342430721","rackStatus":"enabled","rackType":"standard","rowCount":3,"columnCount":3,"length":"900","width":"50","height":"900","orderNum":2,"remark":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-09 09:30:48');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2052924376794615809,'货架',1,'com.ruoyi.wms.controller.RackController.add()','POST',1,'admin','研发部门','/wms/rack','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"rackCode":null,"rackName":"E","warehouseId":"1828364459110469633","areaId":"1828364518342430721","rackStatus":"enabled","rackType":"standard","rowCount":2,"columnCount":3,"length":"300","width":"50","height":"300","orderNum":0,"remark":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-09 09:31:28');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2052925526797275137,'入库单',2,'com.ruoyi.wms.controller.ReceiptOrderController.doWarehousing()','POST',1,'admin','研发部门','/wms/receiptOrder/warehousing','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2052925524435881986","receiptOrderNo":"RK05094002","receiptOrderType":2,"merchantId":null,"orderNo":null,"basisNo":null,"dispatchMode":null,"noticeOrg":null,"receiveUnit":null,"purchaseDate":null,"receiptDate":null,"purchaserName":null,"acceptorName":null,"keeperName":null,"totalQuantity":"10","payableAmount":"0","receiptOrderStatus":1,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","remark":null,"details":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"receiptOrderId":null,"skuId":"2052636524764553218","quantity":"10","amount":"0","equipmentCode":"","specModel":"","productMark":null,"qualityGrade":"grade_a","unitPrice":null,"lineAmount":"0","batchNo":null,"productionDate":null,"expirationDate":null,"remark":null,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","rackId":"2052918576537767938","locationId":"2052918576604876801","generateItemInstance":1,"generatedInstanceQuantity":0}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-09 09:36:02');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2052936709579296769,'入库单',2,'com.ruoyi.wms.controller.ReceiptOrderController.doWarehousing()','POST',1,'admin','研发部门','/wms/receiptOrder/warehousing','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2052936709201809409","receiptOrderNo":"RK05092373","receiptOrderType":2,"merchantId":null,"orderNo":null,"basisNo":null,"dispatchMode":null,"noticeOrg":null,"receiveUnit":null,"purchaseDate":null,"receiptDate":null,"purchaserName":null,"acceptorName":null,"keeperName":null,"totalQuantity":"5","payableAmount":"0","receiptOrderStatus":1,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","remark":null,"details":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"receiptOrderId":null,"skuId":"2052636524764553218","quantity":"5","amount":"0","equipmentCode":"","specModel":"","productMark":null,"qualityGrade":"grade_a","unitPrice":null,"lineAmount":"0","batchNo":null,"productionDate":null,"expirationDate":null,"remark":null,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","rackId":null,"locationId":null,"generateItemInstance":1,"generatedInstanceQuantity":0}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-09 10:20:28');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2052938171923386370,'入库单',2,'com.ruoyi.wms.controller.ReceiptOrderController.doWarehousing()','POST',1,'admin','研发部门','/wms/receiptOrder/warehousing','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2052936709201809409","receiptOrderNo":"RK05092373","receiptOrderType":2,"merchantId":null,"orderNo":null,"basisNo":null,"dispatchMode":null,"noticeOrg":null,"receiveUnit":null,"purchaseDate":null,"receiptDate":null,"purchaserName":null,"acceptorName":null,"keeperName":null,"totalQuantity":"5","payableAmount":"0","receiptOrderStatus":1,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","remark":null,"details":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2052936709256335362","receiptOrderId":"2052936709201809409","skuId":"2052636524764553218","quantity":"5","amount":"0","equipmentCode":"","specModel":"","productMark":null,"qualityGrade":"grade_a","unitPrice":null,"lineAmount":"0","batchNo":null,"productionDate":null,"expirationDate":null,"remark":null,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","rackId":"2052922415722631170","locationId":"2052922645545324545","generateItemInstance":1,"generatedInstanceQuantity":5}]}','',0,'入库单已完成入库','2026-05-09 10:26:17');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2052938934653374465,'入库单',2,'com.ruoyi.wms.controller.ReceiptOrderController.doWarehousing()','POST',1,'admin','研发部门','/wms/receiptOrder/warehousing','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2052938934393327617","receiptOrderNo":"RK05099378","receiptOrderType":2,"merchantId":null,"orderNo":null,"basisNo":null,"dispatchMode":null,"noticeOrg":null,"receiveUnit":null,"purchaseDate":null,"receiptDate":null,"purchaserName":null,"acceptorName":null,"keeperName":null,"totalQuantity":"3","payableAmount":"0","receiptOrderStatus":1,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","remark":null,"details":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"receiptOrderId":null,"skuId":"2052636524764553218","quantity":"3","amount":"0","equipmentCode":"","specModel":"","productMark":null,"qualityGrade":"grade_a","unitPrice":null,"lineAmount":"0","batchNo":null,"productionDate":null,"expirationDate":null,"remark":null,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","rackId":"2052924376542957569","locationId":"2052924376614260738","generateItemInstance":1,"generatedInstanceQuantity":0}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-09 10:29:18');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2052956894704996353,'出库单',2,'com.ruoyi.wms.controller.ShipmentOrderController.shipment()','PUT',1,'admin','研发部门','/wms/shipmentOrder/shipment','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"shipmentOrderNo":"CK05097524","shipmentOrderType":2,"orderNo":null,"merchantId":null,"basisNo":null,"dispatchMode":null,"noticeOrg":null,"receiveUnit":null,"purchaseDate":null,"shipmentDate":null,"purchaserName":null,"acceptorName":null,"keeperName":null,"receivableAmount":"0","totalQuantity":"15","shipmentOrderStatus":1,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","remark":null,"details":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"shipmentOrderId":null,"skuId":"2052636524764553218","quantity":"10","amount":"0","equipmentCode":null,"specModel":null,"productMark":null,"qualityGrade":null,"unitPrice":null,"lineAmount":null,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","batchNo":null,"productionDate":null,"expirationDate":null,"inventoryDetailId":"2052925524633014273","itemInstanceId":null,"boxId":null,"remark":null,"key":"1828364459110469633_1828364518342430721_2052636524764553218"},{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"shipmentOrderId":null,"skuId":"2052636524764553218","quantity":"1","amount":"0","equipmentCode":null,"specModel":null,"productMark":null,"qualityGrade":null,"unitPrice":null,"lineAmount":null,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","batchNo":null,"productionDate":null,"expirationDate":null,"inventoryDetailId":"2052936709256335363","itemInstanceId":"2052936709411524615","boxId":null,"remark":null,"key":"1828364459110469633_1828364518342430721_2052636524764553218"},{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"shipmentOrderId":null,"skuId":"2052636524764553218","quantity":"1","amount":"0","equipmentCode":null,"specModel":null,"productMark":null,"qualityGrade":null,"unitPrice":null,"lineAmount":null,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","batchNo":null,"productionDate":','',0,'','2026-05-09 11:40:40');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2052957076985253890,'出库单',2,'com.ruoyi.wms.controller.ShipmentOrderController.shipment()','PUT',1,'admin','研发部门','/wms/shipmentOrder/shipment','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2052957076427411457","shipmentOrderNo":"CK05095352","shipmentOrderType":2,"orderNo":null,"merchantId":null,"basisNo":null,"dispatchMode":null,"noticeOrg":null,"receiveUnit":null,"purchaseDate":null,"shipmentDate":null,"purchaserName":null,"acceptorName":null,"keeperName":null,"receivableAmount":"0","totalQuantity":"5","shipmentOrderStatus":1,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","remark":null,"details":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"shipmentOrderId":null,"skuId":"2052636524764553218","quantity":"5","amount":"0","equipmentCode":null,"specModel":null,"productMark":null,"qualityGrade":null,"unitPrice":null,"lineAmount":null,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","batchNo":null,"productionDate":null,"expirationDate":null,"inventoryDetailId":"2052936709256335363","itemInstanceId":null,"boxId":null,"remark":null,"key":"1828364459110469633_1828364518342430721_2052636524764553218"}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-09 11:41:24');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2052958034972676097,'出库单',2,'com.ruoyi.wms.controller.ShipmentOrderController.shipment()','PUT',1,'admin','研发部门','/wms/shipmentOrder/shipment','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2052958032862941186","shipmentOrderNo":"CK05092243","shipmentOrderType":2,"orderNo":null,"merchantId":null,"basisNo":null,"dispatchMode":null,"noticeOrg":null,"receiveUnit":null,"purchaseDate":null,"shipmentDate":null,"purchaserName":null,"acceptorName":null,"keeperName":null,"receivableAmount":"0","totalQuantity":"3","shipmentOrderStatus":1,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","remark":null,"details":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"shipmentOrderId":null,"skuId":"2052636524764553218","quantity":"3.00","amount":"0","equipmentCode":null,"specModel":null,"productMark":null,"qualityGrade":null,"unitPrice":null,"lineAmount":null,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","batchNo":null,"productionDate":null,"expirationDate":null,"inventoryDetailId":"2052938934393327619","itemInstanceId":null,"boxId":null,"remark":null,"key":"1828364459110469633_1828364518342430721_2052636524764553218"}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-09 11:45:12');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2053029272541851649,'货架',3,'com.ruoyi.wms.controller.RackController.remove()','DELETE',1,'admin','研发部门','/wms/rack/2052922082288046081','0:0:0:0:0:0:0:1','内网IP','{}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-09 16:28:17');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2052961111117172738,'出库单',2,'com.ruoyi.wms.controller.ShipmentOrderController.shipment()','PUT',1,'admin','研发部门','/wms/shipmentOrder/shipment','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2052961110991343617","shipmentOrderNo":"CK05099456","shipmentOrderType":2,"orderNo":null,"merchantId":null,"basisNo":null,"dispatchMode":null,"noticeOrg":null,"receiveUnit":null,"purchaseDate":null,"shipmentDate":null,"purchaserName":null,"acceptorName":null,"keeperName":null,"receivableAmount":"0","totalQuantity":"2","shipmentOrderStatus":1,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","remark":null,"details":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"shipmentOrderId":null,"skuId":"2052636524764553218","quantity":"2","amount":"0","equipmentCode":null,"specModel":null,"productMark":null,"qualityGrade":null,"unitPrice":null,"lineAmount":null,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","batchNo":null,"productionDate":null,"expirationDate":null,"inventoryDetailId":"2052925524633014273","itemInstanceId":null,"boxId":null,"remark":null,"key":"1828364459110469633_1828364518342430721_2052636524764553218"}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-09 11:57:26');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2052964295529238530,'出库单',1,'com.ruoyi.wms.controller.ShipmentOrderController.add()','POST',1,'admin','研发部门','/wms/shipmentOrder','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2052964295403409409","shipmentOrderNo":"CK05095909","shipmentOrderType":2,"orderNo":null,"merchantId":null,"basisNo":null,"dispatchMode":null,"noticeOrg":null,"receiveUnit":null,"purchaseDate":null,"shipmentDate":null,"purchaserName":null,"acceptorName":null,"keeperName":null,"receivableAmount":"0","totalQuantity":"2","shipmentOrderStatus":0,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","remark":null,"details":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"shipmentOrderId":null,"skuId":"2052636524764553218","quantity":"2","amount":"0","equipmentCode":"","specModel":"","productMark":null,"qualityGrade":"grade_a","unitPrice":null,"lineAmount":"0","warehouseId":"1828364459110469633","areaId":"1828364518342430721","batchNo":null,"productionDate":null,"expirationDate":null,"inventoryDetailId":"2052925524633014273","itemInstanceId":null,"boxId":null,"remark":null,"key":"1828364459110469633_1828364518342430721_2052636524764553218"}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-09 12:10:05');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2052995188960579586,'出库单',2,'com.ruoyi.wms.controller.ShipmentOrderController.shipment()','PUT',1,'admin','研发部门','/wms/shipmentOrder/shipment','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2052964295403409409","shipmentOrderNo":"CK05095909","shipmentOrderType":2,"orderNo":null,"merchantId":null,"basisNo":null,"dispatchMode":null,"noticeOrg":null,"receiveUnit":null,"purchaseDate":null,"shipmentDate":null,"purchaserName":null,"acceptorName":null,"keeperName":null,"receivableAmount":"0","totalQuantity":"2","shipmentOrderStatus":1,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","remark":null,"details":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2052964295470518274","shipmentOrderId":"2052964295403409409","skuId":"2052636524764553218","quantity":"2","amount":"0.00","equipmentCode":null,"specModel":null,"productMark":null,"qualityGrade":null,"unitPrice":null,"lineAmount":null,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","batchNo":null,"productionDate":null,"expirationDate":null,"inventoryDetailId":"2052925524633014273","itemInstanceId":null,"boxId":null,"remark":null,"key":"1828364459110469633_1828364518342430721_2052636524764553218"}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-09 14:12:51');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2052998365462188034,'调拨单',2,'com.ruoyi.wms.controller.MovementOrderController.move()','POST',1,'admin','研发部门','/wms/movementOrder/move','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2052998364958871553","movementOrderNo":"DB05096553","movementType":"common","dispatchBasis":null,"dispatchPurpose":null,"supportNo":null,"dispatchMode":null,"fromUnit":null,"toUnit":null,"fromStation":null,"toStation":null,"fromAddress":null,"toAddress":null,"contactAddress":null,"dispatchDate":null,"effectiveDate":null,"issueDate":null,"fromHandler":null,"toHandler":null,"sourceWarehouseId":"1828364459110469633","sourceAreaId":"1828364518342430721","sourceRackId":"2052918576537767938","sourceLocationId":"2052918576604876801","targetWarehouseId":"1828364459110469633","targetAreaId":"1828364518342430721","targetRackId":null,"targetLocationId":null,"movementScope":null,"movementOrderStatus":1,"totalQuantity":"2","remark":null,"details":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"movementOrderId":null,"skuId":"2052636524764553218","quantity":"2","equipmentCode":null,"specModel":null,"productMark":null,"qualityGrade":null,"unitPrice":null,"lineAmount":null,"remark":null,"batchNo":null,"productionDate":null,"expirationDate":null,"sourceWarehouseId":"1828364459110469633","sourceAreaId":"1828364518342430721","sourceRackId":"2052918576537767938","sourceLocationId":"2052918576604876801","targetWarehouseId":"1828364459110469633","targetAreaId":"1828364518342430721","targetRackId":null,"targetLocationId":null,"inventoryDetailId":"2052925524633014273","itemInstanceId":null,"boxId":null,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","key":"1828364459110469633_1828364518342430721_2052636524764553218"}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-09 14:25:28');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2053033045213442050,'库区',1,'com.ruoyi.wms.controller.AreaController.add()','POST',1,'admin','研发部门','/wms/area','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"areaCode":null,"areaName":"B3","warehouseId":"1828364740028174337","status":null,"areaType":null,"orderNum":null,"remark":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-09 16:43:16');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2053022266028322818,'入库单',2,'com.ruoyi.wms.controller.ReceiptOrderController.doWarehousing()','POST',1,'admin','研发部门','/wms/receiptOrder/warehousing','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2053022264082165762","receiptOrderNo":"RK05091420","receiptOrderType":2,"merchantId":"1828354284882399233","orderNo":"","basisNo":"a","dispatchMode":"road","noticeOrg":"a","receiveUnit":"a","purchaseDate":"2026-05-08","receiptDate":"2026-05-09","purchaserName":"a","acceptorName":"a","keeperName":"a","totalQuantity":"10","payableAmount":"50","receiptOrderStatus":1,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","remark":null,"details":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"receiptOrderId":null,"skuId":"2052636524764553218","quantity":"10","amount":"50","equipmentCode":"","specModel":"","productMark":"qqq","qualityGrade":"grade_a","unitPrice":"5","lineAmount":"50","batchNo":null,"productionDate":"2026-05-08 00:00:00","expirationDate":"2026-05-20 00:00:00","remark":null,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","rackId":"2052919288873193473","locationId":"2052921598110175233","generateItemInstance":1,"generatedInstanceQuantity":0}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-09 16:00:26');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2053025133032239106,'箱体',1,'com.ruoyi.wms.controller.BoxController.add()','POST',1,'admin','研发部门','/wms/box','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"boxCode":"BOX2053025132923187200","boxName":"一箱东西","boxStatus":"idle","length":null,"width":null,"height":null,"volume":null,"maxWeight":null,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","rackId":"2052919288873193473","locationId":"2052921598072426498","itemCount":null,"remark":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-09 16:11:50');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2053027138857463809,'物料',2,'com.ruoyi.wms.controller.ItemController.edit()','PUT',1,'admin','研发部门','/wms/item','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2052636524512894978","ids":null,"itemCode":"DYJ","itemName":"惠普","itemCategory":"1828365014901886978","unit":"台","itemBrand":"1828407291103842306","itemType":"normal","trackingMode":"instance","allowBox":1,"specLevel":null,"equipmentName":"装备名称","equipmentType":"normal","status":"1","defaultTrackingMode":"instance","defaultBelongUnit":"A部","defaultQualityGrade":"grade_a","productMarkRule":null,"modelText":null,"remark":null,"sku":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2052636524764553218","skuName":"惠普6","itemId":"2052636524512894978","barcode":"","skuCode":"","specModel":"","defaultUnitPrice":null,"defaultQualityGrade":"grade_a","status":"1","volume":"0.1250","length":"50","width":"50","height":"50","grossWeight":"5","netWeight":"5","costPrice":"50","sellingPrice":"100","itemName":null,"itemCode":null,"equipmentName":null,"itemCategory":null,"itemBrand":null}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-09 16:19:48');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2053027205957939202,'物料',2,'com.ruoyi.wms.controller.ItemController.edit()','PUT',1,'admin','研发部门','/wms/item','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2052636524512894978","ids":null,"itemCode":"DYJ","itemName":"惠普","itemCategory":"1828365014901886978","unit":"台","itemBrand":"1828407291103842306","itemType":"normal","trackingMode":"instance","allowBox":1,"specLevel":null,"equipmentName":"装备名称","equipmentType":"normal","status":"1","defaultTrackingMode":"instance","defaultBelongUnit":"A部","defaultQualityGrade":"grade_a","productMarkRule":null,"modelText":null,"remark":null,"sku":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2052636524764553218","skuName":"惠普6","itemId":"2052636524512894978","barcode":"","skuCode":"","specModel":"","defaultUnitPrice":null,"defaultQualityGrade":"grade_a","status":"1","volume":"0.1250","length":"50.0","width":"50.0","height":"50.0","grossWeight":"5.000","netWeight":"5.000","costPrice":"50.00","sellingPrice":"100.00","itemName":null,"itemCode":null,"equipmentName":null,"itemCategory":null,"itemBrand":null}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-09 16:20:04');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2053027303773302786,'物料',2,'com.ruoyi.wms.controller.ItemController.edit()','PUT',1,'admin','研发部门','/wms/item','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2052636524512894978","ids":null,"itemCode":"DYJ","itemName":"惠普","itemCategory":"1828365014901886978","unit":"台","itemBrand":"1828407291103842306","itemType":"normal","trackingMode":"instance","allowBox":1,"specLevel":null,"equipmentName":"装备名称","equipmentType":"normal","status":"1","defaultTrackingMode":"instance","defaultBelongUnit":"A部","defaultQualityGrade":"grade_a","productMarkRule":null,"modelText":null,"remark":null,"sku":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2052636524764553218","skuName":"惠普6","itemId":"2052636524512894978","barcode":"","skuCode":"","specModel":"","defaultUnitPrice":null,"defaultQualityGrade":"grade_a","status":"1","volume":"0.1250","length":"50.0","width":"50.0","height":"50.0","grossWeight":"5.000","netWeight":"5.000","costPrice":"50.00","sellingPrice":"100.00","itemName":null,"itemCode":null,"equipmentName":null,"itemCategory":null,"itemBrand":null}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-09 16:20:27');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2053029251067015169,'货架',3,'com.ruoyi.wms.controller.RackController.remove()','DELETE',1,'admin','研发部门','/wms/rack/2052924376542957569','0:0:0:0:0:0:0:1','内网IP','{}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-09 16:28:12');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2053029258029559810,'货架',3,'com.ruoyi.wms.controller.RackController.remove()','DELETE',1,'admin','研发部门','/wms/rack/2052918576537767938','0:0:0:0:0:0:0:1','内网IP','{}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-09 16:28:13');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2053029262932701186,'货架',3,'com.ruoyi.wms.controller.RackController.remove()','DELETE',1,'admin','研发部门','/wms/rack/2052922415722631170','0:0:0:0:0:0:0:1','内网IP','{}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-09 16:28:14');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2053033109776363522,'库区',1,'com.ruoyi.wms.controller.AreaController.add()','POST',1,'admin','研发部门','/wms/area','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"areaCode":null,"areaName":"C1","warehouseId":"1828364740028174337","status":null,"areaType":null,"orderNum":null,"remark":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-09 16:43:32');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2053033134812164098,'库区',1,'com.ruoyi.wms.controller.AreaController.add()','POST',1,'admin','研发部门','/wms/area','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"areaCode":null,"areaName":"C2","warehouseId":"1828364740028174337","status":null,"areaType":null,"orderNum":null,"remark":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-09 16:43:38');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2053033153170632705,'库区',1,'com.ruoyi.wms.controller.AreaController.add()','POST',1,'admin','研发部门','/wms/area','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"areaCode":null,"areaName":"C3","warehouseId":"1828364740028174337","status":null,"areaType":null,"orderNum":null,"remark":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-09 16:43:42');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2053033253456441345,'库区',1,'com.ruoyi.wms.controller.AreaController.add()','POST',1,'admin','研发部门','/wms/area','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"areaCode":null,"areaName":"D1","warehouseId":"1828364740028174337","status":null,"areaType":null,"orderNum":null,"remark":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-09 16:44:06');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2053033327955668993,'仓库',1,'com.ruoyi.wms.controller.WarehouseController.add()','POST',1,'admin','研发部门','/wms/warehouse','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"warehouseCode":null,"warehouseName":"A","status":null,"warehouseType":null,"address":null,"managerName":null,"managerPhone":null,"remark":null,"orderNum":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-09 16:44:24');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2053041346957406210,'货架',1,'com.ruoyi.wms.controller.RackController.add()','POST',1,'admin','研发部门','/wms/rack','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"rackCode":null,"rackName":"A","warehouseId":"1828364459110469633","areaId":"1828364518342430721","rackStatus":"enabled","rackType":"standard","rowCount":2,"columnCount":3,"length":"600","width":"50","height":"600","orderNum":0,"remark":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-09 17:16:15');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2053041423599923202,'货架',1,'com.ruoyi.wms.controller.RackController.add()','POST',1,'admin','研发部门','/wms/rack','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"rackCode":null,"rackName":"B","warehouseId":"1828364609002311682","areaId":"1828364666585911297","rackStatus":"enabled","rackType":"standard","rowCount":3,"columnCount":3,"length":"900","width":"50","height":"900","orderNum":0,"remark":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-09 17:16:34');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2053042128108777474,'入库单',2,'com.ruoyi.wms.controller.ReceiptOrderController.doWarehousing()','POST',1,'admin','研发部门','/wms/receiptOrder/warehousing','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2053042127790010370","receiptOrderNo":"RK05098919","receiptOrderType":2,"merchantId":"1828354016258199554","orderNo":null,"basisNo":"a","dispatchMode":"road","noticeOrg":"a","receiveUnit":"a","purchaseDate":"2026-05-06","receiptDate":"2026-05-09","purchaserName":"a","acceptorName":"a","keeperName":"a","totalQuantity":"10","payableAmount":"500","receiptOrderStatus":1,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","remark":null,"details":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"receiptOrderId":null,"skuId":"2052636524764553218","quantity":"10","amount":"500","equipmentCode":"DYJ","specModel":"","productMark":null,"qualityGrade":"grade_a","unitPrice":"50","lineAmount":"500","batchNo":null,"productionDate":null,"expirationDate":null,"remark":null,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","rackId":"2053041345556508674","locationId":"2053041345556508675","generateItemInstance":1,"generatedInstanceQuantity":0}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-09 17:19:22');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2053045196950990849,'出库单',1,'com.ruoyi.wms.controller.ShipmentOrderController.add()','POST',1,'admin','研发部门','/wms/shipmentOrder','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2053045196825161730","shipmentOrderNo":"CK05098379","shipmentOrderType":2,"orderNo":null,"merchantId":"1828354284882399233","basisNo":"a","dispatchMode":"road","noticeOrg":"a","receiveUnit":"a","purchaseDate":"2026-05-09","shipmentDate":"2026-05-09","purchaserName":"a","acceptorName":"a","keeperName":"a","receivableAmount":"100","totalQuantity":"2","shipmentOrderStatus":0,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","remark":null,"details":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"shipmentOrderId":null,"skuId":"2052636524764553218","quantity":"2","amount":"100","equipmentCode":"DYJ","specModel":"","productMark":null,"qualityGrade":"grade_a","unitPrice":"50","lineAmount":"100","warehouseId":"1828364459110469633","areaId":"1828364518342430721","batchNo":null,"productionDate":null,"expirationDate":null,"inventoryDetailId":"2053042127790010372","itemInstanceId":null,"boxId":null,"remark":null,"key":"1828364459110469633_1828364518342430721_2052636524764553218"}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-09 17:31:33');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2054090870790295553,'sku信息',3,'com.ruoyi.wms.controller.ItemSkuController.remove()','DELETE',1,'admin','研发部门','/wms/itemSku/2053127932654985219','0:0:0:0:0:0:0:1','内网IP','{}','',0,'至少包含一个商品规格','2026-05-12 14:46:41');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2054090892172857346,'sku信息',3,'com.ruoyi.wms.controller.ItemSkuController.remove()','DELETE',1,'admin','研发部门','/wms/itemSku/2053127932654985219','0:0:0:0:0:0:0:1','内网IP','{}','',0,'至少包含一个商品规格','2026-05-12 14:46:47');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2053045241419001858,'出库单',2,'com.ruoyi.wms.controller.ShipmentOrderController.shipment()','PUT',1,'admin','研发部门','/wms/shipmentOrder/shipment','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2053045196825161730","shipmentOrderNo":"CK05098379","shipmentOrderType":2,"orderNo":null,"merchantId":"1828354284882399233","basisNo":null,"dispatchMode":null,"noticeOrg":null,"receiveUnit":null,"purchaseDate":null,"shipmentDate":null,"purchaserName":null,"acceptorName":null,"keeperName":null,"receivableAmount":"100","totalQuantity":"2","shipmentOrderStatus":1,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","remark":null,"details":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2053045196888076289","shipmentOrderId":"2053045196825161730","skuId":"2052636524764553218","quantity":"2","amount":"100.00","equipmentCode":null,"specModel":null,"productMark":null,"qualityGrade":null,"unitPrice":null,"lineAmount":null,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","batchNo":null,"productionDate":null,"expirationDate":null,"inventoryDetailId":"2053042127790010372","itemInstanceId":null,"boxId":null,"remark":null,"key":"1828364459110469633_1828364518342430721_2052636524764553218"}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-09 17:31:44');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2053049024794402818,'出库单',2,'com.ruoyi.wms.controller.ShipmentOrderController.shipment()','PUT',1,'admin','研发部门','/wms/shipmentOrder/shipment','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2053049023712272386","shipmentOrderNo":"CK05091941","shipmentOrderType":2,"orderNo":null,"merchantId":null,"basisNo":null,"dispatchMode":null,"noticeOrg":null,"receiveUnit":null,"purchaseDate":null,"shipmentDate":null,"purchaserName":null,"acceptorName":null,"keeperName":null,"receivableAmount":"100","totalQuantity":"2","shipmentOrderStatus":1,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","remark":null,"details":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"shipmentOrderId":null,"skuId":"2052636524764553218","quantity":"2","amount":"100","equipmentCode":null,"specModel":null,"productMark":null,"qualityGrade":null,"unitPrice":null,"lineAmount":null,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","batchNo":null,"productionDate":null,"expirationDate":null,"inventoryDetailId":"2053042127790010372","itemInstanceId":null,"boxId":null,"remark":null,"key":"1828364459110469633_1828364518342430721_2052636524764553218"}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-09 17:46:46');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2053114723961217026,'货架',2,'com.ruoyi.wms.controller.RackController.edit()','PUT',1,'admin','研发部门','/wms/rack','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2053041423335682050","rackCode":"HJ-B","rackName":"B","warehouseId":"1828364609002311682","areaId":"1828364666585911297","rackStatus":"enabled","rackType":"standard","rowCount":3,"columnCount":3,"length":"900","width":"50","height":"900","orderNum":0,"remark":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-09 22:07:50');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2053114758937518082,'货架',2,'com.ruoyi.wms.controller.RackController.edit()','PUT',1,'admin','研发部门','/wms/rack','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2053041345556508674","rackCode":"HJ-A","rackName":"A","warehouseId":"1828364459110469633","areaId":"1828364518342430721","rackStatus":"enabled","rackType":"standard","rowCount":2,"columnCount":3,"length":"600.00","width":"50.00","height":"600.00","orderNum":0,"remark":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-09 22:07:58');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2053114964802347009,'库区',3,'com.ruoyi.wms.controller.AreaController.remove()','DELETE',1,'admin','研发部门','/wms/area/2053033253095731201','0:0:0:0:0:0:0:1','内网IP','{}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-09 22:08:47');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2053114973786546178,'库区',3,'com.ruoyi.wms.controller.AreaController.remove()','DELETE',1,'admin','研发部门','/wms/area/2053033109776363521','0:0:0:0:0:0:0:1','内网IP','{}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-09 22:08:49');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2053114981457928194,'库区',3,'com.ruoyi.wms.controller.AreaController.remove()','DELETE',1,'admin','研发部门','/wms/area/2053033153111912450','0:0:0:0:0:0:0:1','内网IP','{}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-09 22:08:51');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2053114994619654146,'库区',3,'com.ruoyi.wms.controller.AreaController.remove()','DELETE',1,'admin','研发部门','/wms/area/2053033045150527490','0:0:0:0:0:0:0:1','内网IP','{}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-09 22:08:54');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2053115004321079297,'库区',3,'com.ruoyi.wms.controller.AreaController.remove()','DELETE',1,'admin','研发部门','/wms/area/2053033134749249537','0:0:0:0:0:0:0:1','内网IP','{}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-09 22:08:57');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2053119223287586817,'箱体',1,'com.ruoyi.wms.controller.BoxController.add()','POST',1,'admin','研发部门','/wms/box','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"boxCode":"aaaaa","boxName":"aa","boxStatus":"idle","length":null,"width":null,"height":null,"volume":null,"maxWeight":null,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","rackId":"2053041345556508674","locationId":"2053041345556508675","itemCount":null,"remark":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-09 22:25:43');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2054091460647849985,'sku信息',3,'com.ruoyi.wms.controller.ItemSkuController.remove()','DELETE',1,'admin','研发部门','/wms/itemSku/2053127932654985219','0:0:0:0:0:0:0:1','内网IP','{}','',0,'至少包含一个商品规格','2026-05-12 14:49:02');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2054092002220576770,'sku信息',3,'com.ruoyi.wms.controller.ItemSkuController.remove()','DELETE',1,'admin','研发部门','/wms/itemSku/2053127932654985219','0:0:0:0:0:0:0:1','内网IP','{}','',0,'至少包含一个商品规格','2026-05-12 14:51:11');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2054099228096303106,'sku信息',3,'com.ruoyi.wms.controller.ItemSkuController.remove()','DELETE',1,'admin','研发部门','/wms/itemSku/2053127932654985219','0:0:0:0:0:0:0:1','内网IP','{}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-12 15:19:54');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2053127253072879617,'物料',1,'com.ruoyi.wms.controller.ItemController.add()','POST',1,'admin','研发部门','/wms/item','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"ids":null,"itemCode":null,"itemName":"iphone12","itemCategory":"1828365043024695300","unit":null,"itemBrand":null,"itemType":"normal","trackingMode":"instance","allowBox":1,"specLevel":null,"equipmentName":null,"equipmentType":null,"status":"1","defaultTrackingMode":"instance","defaultBelongUnit":null,"defaultQualityGrade":null,"productMarkRule":null,"modelText":null,"remark":null,"sku":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"skuName":"128","itemId":"2053127250589851649","barcode":"","skuCode":"","specModel":"","defaultUnitPrice":null,"defaultQualityGrade":null,"status":"1","volume":null,"length":null,"width":null,"height":null,"grossWeight":null,"netWeight":null,"costPrice":null,"sellingPrice":null,"itemName":null,"itemCode":null,"equipmentName":null,"itemCategory":null,"itemBrand":null},{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"skuName":"256","itemId":"2053127250589851649","barcode":"","skuCode":"","specModel":"","defaultUnitPrice":null,"defaultQualityGrade":null,"status":"1","volume":null,"length":null,"width":null,"height":null,"grossWeight":null,"netWeight":null,"costPrice":null,"sellingPrice":null,"itemName":null,"itemCode":null,"equipmentName":null,"itemCategory":null,"itemBrand":null}]}','',0,'com.ruoyi.wms.mapper.ItemSkuMapper.insert (batch index #1) failed. Cause: java.sql.BatchUpdateException: Duplicate entry ''2053127250589851649-'' for key ''wms_item_sku.uk_wms_item_sku_item_code''
; Duplicate entry ''2053127250589851649-'' for key ''wms_item_sku.uk_wms_item_sku_item_code''','2026-05-09 22:57:37');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2053127354751197186,'物料',1,'com.ruoyi.wms.controller.ItemController.add()','POST',1,'admin','研发部门','/wms/item','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"ids":null,"itemCode":null,"itemName":"iphone12","itemCategory":"1828365043024695300","unit":null,"itemBrand":null,"itemType":"normal","trackingMode":"instance","allowBox":1,"specLevel":null,"equipmentName":null,"equipmentType":null,"status":"1","defaultTrackingMode":"instance","defaultBelongUnit":null,"defaultQualityGrade":null,"productMarkRule":null,"modelText":null,"remark":null,"sku":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"skuName":"1280","itemId":"2053127354688282625","barcode":"","skuCode":"","specModel":"","defaultUnitPrice":null,"defaultQualityGrade":null,"status":"1","volume":null,"length":null,"width":null,"height":null,"grossWeight":null,"netWeight":null,"costPrice":null,"sellingPrice":null,"itemName":null,"itemCode":null,"equipmentName":null,"itemCategory":null,"itemBrand":null},{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"skuName":"2560","itemId":"2053127354688282625","barcode":"","skuCode":"","specModel":"","defaultUnitPrice":null,"defaultQualityGrade":null,"status":"1","volume":null,"length":null,"width":null,"height":null,"grossWeight":null,"netWeight":null,"costPrice":null,"sellingPrice":null,"itemName":null,"itemCode":null,"equipmentName":null,"itemCategory":null,"itemBrand":null}]}','',0,'com.ruoyi.wms.mapper.ItemSkuMapper.insert (batch index #1) failed. Cause: java.sql.BatchUpdateException: Duplicate entry ''2053127354688282625-'' for key ''wms_item_sku.uk_wms_item_sku_item_code''
; Duplicate entry ''2053127354688282625-'' for key ''wms_item_sku.uk_wms_item_sku_item_code''','2026-05-09 22:58:01');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2053127861280514050,'物料',1,'com.ruoyi.wms.controller.ItemController.add()','POST',1,'admin','研发部门','/wms/item','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"ids":null,"itemCode":null,"itemName":"iphone12","itemCategory":"1828365043024695300","unit":null,"itemBrand":null,"itemType":"normal","trackingMode":"instance","allowBox":1,"specLevel":null,"equipmentName":null,"equipmentType":null,"status":"1","defaultTrackingMode":"instance","defaultBelongUnit":null,"defaultQualityGrade":null,"productMarkRule":null,"modelText":null,"remark":null,"sku":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"skuName":"1280","itemId":"2053127861184045057","barcode":"","skuCode":"","specModel":"128","defaultUnitPrice":null,"defaultQualityGrade":null,"status":"1","volume":null,"length":null,"width":null,"height":null,"grossWeight":null,"netWeight":null,"costPrice":null,"sellingPrice":null,"itemName":null,"itemCode":null,"equipmentName":null,"itemCategory":null,"itemBrand":null},{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"skuName":"2560","itemId":"2053127861184045057","barcode":"","skuCode":"","specModel":"256","defaultUnitPrice":null,"defaultQualityGrade":null,"status":"1","volume":null,"length":null,"width":null,"height":null,"grossWeight":null,"netWeight":null,"costPrice":null,"sellingPrice":null,"itemName":null,"itemCode":null,"equipmentName":null,"itemCategory":null,"itemBrand":null}]}','',0,'com.ruoyi.wms.mapper.ItemSkuMapper.insert (batch index #1) failed. Cause: java.sql.BatchUpdateException: Duplicate entry ''2053127861184045057-'' for key ''wms_item_sku.uk_wms_item_sku_item_code''
; Duplicate entry ''2053127861184045057-'' for key ''wms_item_sku.uk_wms_item_sku_item_code''','2026-05-09 23:00:02');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2054200473041575937,'物料',0,'com.ruoyi.wms.controller.ItemController.batchPrintQrCode()','POST',1,'admin','研发部门','/wms/item/batchPrintQrCode','0:0:0:0:0:0:0:1','内网IP','{"row":{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2053852496372543489","ids":null,"itemCode":"a","itemName":"a","itemCategory":"1828405743737016322","unit":"a","itemBrand":"1828407291103842306","specLevel":null,"equipmentName":"a","equipmentType":"通装","status":"1","productMark":"a","modelText":null,"remark":"a","sku":null},"qrCodeCount":5}','',0,'com.ruoyi.wms.mapper.ItemInstanceMapper.insert (batch index #1) failed. Cause: java.sql.BatchUpdateException: Duplicate entry ''1000000001'' for key ''wms_item_instance.uk_wms_item_instance_code''
; Duplicate entry ''1000000001'' for key ''wms_item_instance.uk_wms_item_instance_code''','2026-05-12 22:02:13');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2053127932717899777,'物料',1,'com.ruoyi.wms.controller.ItemController.add()','POST',1,'admin','研发部门','/wms/item','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"ids":null,"itemCode":null,"itemName":"iphone12","itemCategory":"1828365043024695300","unit":null,"itemBrand":null,"itemType":"normal","trackingMode":"instance","allowBox":1,"specLevel":null,"equipmentName":null,"equipmentType":null,"status":"1","defaultTrackingMode":"instance","defaultBelongUnit":null,"defaultQualityGrade":null,"productMarkRule":null,"modelText":null,"remark":null,"sku":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"skuName":"1280","itemId":"2053127932654985217","barcode":"","skuCode":"128","specModel":"128","defaultUnitPrice":null,"defaultQualityGrade":null,"status":"1","volume":null,"length":null,"width":null,"height":null,"grossWeight":null,"netWeight":null,"costPrice":null,"sellingPrice":null,"itemName":null,"itemCode":null,"equipmentName":null,"itemCategory":null,"itemBrand":null},{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"skuName":"2560","itemId":"2053127932654985217","barcode":"","skuCode":"256","specModel":"256","defaultUnitPrice":null,"defaultQualityGrade":null,"status":"1","volume":null,"length":null,"width":null,"height":null,"grossWeight":null,"netWeight":null,"costPrice":null,"sellingPrice":null,"itemName":null,"itemCode":null,"equipmentName":null,"itemCategory":null,"itemBrand":null}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-09 23:00:19');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2053130064548405250,'物料类型',3,'com.ruoyi.wms.controller.ItemCategoryController.remove()','DELETE',1,'admin','研发部门','/wms/itemCategory/1829397860466749441','0:0:0:0:0:0:0:1','内网IP','{}','',0,'删除失败！请先删除该分类下的子分类！','2026-05-09 23:08:47');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2053130076367953921,'物料类型',3,'com.ruoyi.wms.controller.ItemCategoryController.remove()','DELETE',1,'admin','研发部门','/wms/itemCategory/1829398007993004034','0:0:0:0:0:0:0:1','内网IP','{}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-09 23:08:50');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2053130081652776961,'物料类型',3,'com.ruoyi.wms.controller.ItemCategoryController.remove()','DELETE',1,'admin','研发部门','/wms/itemCategory/1829397958923841538','0:0:0:0:0:0:0:1','内网IP','{}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-09 23:08:51');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2053130087696769025,'物料类型',3,'com.ruoyi.wms.controller.ItemCategoryController.remove()','DELETE',1,'admin','研发部门','/wms/itemCategory/1829397860466749441','0:0:0:0:0:0:0:1','内网IP','{}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-09 23:08:53');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2053132230637985794,'库存盘点单据',2,'com.ruoyi.wms.controller.CheckOrderController.check()','POST',1,'admin','研发部门','/wms/checkOrder/check','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"checkOrderNo":"PK05092830","checkOrderStatus":1,"checkOrderTotal":"-4","warehouseId":"1828364459110469633","areaId":"1828364518342430721","rackId":"2053041345556508674","checkScopeType":"warehouse","checkDate":null,"checkerName":null,"reviewerName":null,"remark":null,"details":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"checkOrderId":null,"skuId":"2052636524764553218","quantity":"6","checkQuantity":"2","profitAndLoss":"-4","differenceQuantity":"-4","warehouseId":"1828364459110469633","areaId":"1828364518342430721","rackId":"2053041345556508674","locationId":"2053041345556508675","batchNo":null,"productionDate":null,"expirationDate":null,"receiptTime":"2026-05-09 17:19:21","inventoryDetailId":"2053042127790010372","equipmentCode":null,"specModel":null,"productMark":null,"qualityGrade":null,"itemInstanceId":null,"boxId":null,"remark":null,"haveProfitAndLoss":null}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-09 23:17:24');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2053132967078076418,'菜单管理',2,'com.ruoyi.system.controller.system.SysMenuController.edit()','PUT',1,'admin','研发部门','/system/menu','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":"2024-08-12 10:19:35","updateBy":null,"updateTime":null,"menuId":"1822820194307051521","parentId":"2060000000000001002","menuName":"库内移库","orderNum":1,"path":"internalMoveOrder","component":"wms/internal/move/index","queryParam":null,"isFrame":"0","isCache":"0","menuType":"C","visible":"0","status":"1","perms":"wms:internalMove:list","icon":"drag","remark":"独立库内移库入口"}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-09 23:20:19');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2053132980105584641,'菜单管理',2,'com.ruoyi.system.controller.system.SysMenuController.edit()','PUT',1,'admin','研发部门','/system/menu','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":"2026-05-07 14:23:08","updateBy":null,"updateTime":null,"menuId":"2060000000000003102","parentId":"2060000000000001002","menuName":"库存调整","orderNum":2,"path":"inventoryAdjust","component":"wms/internal/adjust/index","queryParam":null,"isFrame":"0","isCache":"0","menuType":"C","visible":"0","status":"1","perms":"wms:inventoryDetail:all","icon":"edit","remark":"第二阶段页面落位入口，执行逻辑留待第三阶段"}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-09 23:20:23');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2053766214623547393,'库区',1,'com.ruoyi.wms.controller.AreaController.add()','POST',1,'admin','研发部门','/wms/area','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"areaCode":null,"areaName":"a","warehouseId":"1828364459110469633","status":null,"areaType":null,"orderNum":null,"remark":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-11 17:16:37');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2054391358601666561,'货位',2,'com.ruoyi.wms.controller.LocationController.edit()','PUT',1,'admin','研发部门','/wms/location','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2053041423398596609","locationCode":"HJ-B-R1-C1","locationName":"B-1-1","warehouseId":"1828364609002311682","areaId":"1828364666585911297","rackId":"2053041423335682050","locationStatus":"enabled","locationType":null,"rowNo":1,"columnNo":1,"length":"300","width":"50","height":"299","maxWeight":null,"occupiedFlag":0,"sortNo":1001,"remark":null}','',0,'货位长度之和不能超过货架实际长度(900.00cm)','2026-05-13 10:40:43');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2053852496691310593,'物料',1,'com.ruoyi.wms.controller.ItemController.add()','POST',1,'admin','研发部门','/wms/item','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"ids":null,"itemCode":"a","itemName":"a","itemCategory":"1828365014901887000","unit":"a","itemBrand":"1828407291103842306","itemType":"normal","trackingMode":"instance","allowBox":1,"specLevel":null,"equipmentName":"a","equipmentType":"a","status":"1","productMarkRule":"a","modelText":null,"remark":"a","sku":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"skuName":"a","itemId":"2053852496372543489","specModel":"a","status":"1","volume":"0","length":"1","width":"2","height":"2","grossWeight":"1","netWeight":"1","costPrice":"1","sellingPrice":"1","itemName":null,"itemCode":null,"equipmentName":null,"itemCategory":null,"itemBrand":null}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-11 22:59:29');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2053858205621325826,'物料',1,'com.ruoyi.wms.controller.ItemController.add()','POST',1,'admin','研发部门','/wms/item','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"ids":null,"itemCode":null,"itemName":"b","itemCategory":"1828365043024695300","unit":"b","itemBrand":"1828407291103842306","itemType":"normal","trackingMode":"batch","allowBox":1,"specLevel":null,"equipmentName":"b","equipmentType":"b","status":"1","productMark":"b","modelText":null,"remark":"b","sku":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"skuName":"b","itemId":"2053858205260615682","specModel":"b","status":"1","volume":"0","length":"1","width":"1","height":"1","grossWeight":"1","netWeight":"1","costPrice":"1","sellingPrice":"1","itemName":null,"itemCode":null,"equipmentName":null,"itemCategory":null,"itemBrand":null},{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"skuName":"c","itemId":"2053858205260615682","specModel":"c","status":"1","volume":"0","length":"1","width":"1","height":"1","grossWeight":"1","netWeight":"1","costPrice":"1","sellingPrice":"1","itemName":null,"itemCode":null,"equipmentName":null,"itemCategory":null,"itemBrand":null}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-11 23:22:10');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2054089783102410754,'物料',2,'com.ruoyi.wms.controller.ItemController.edit()','PUT',1,'admin','研发部门','/wms/item','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2053852496372543489","ids":null,"itemCode":"a","itemName":"a","itemCategory":"1828405743737016322","unit":"a","itemBrand":"1828407291103842306","allowBox":1,"specLevel":null,"equipmentName":"a","equipmentType":"通装","status":"1","productMark":"a","modelText":null,"remark":"a","sku":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2053852496494178305","skuName":"a","itemId":"2053852496372543489","specModel":"a","status":"1","volume":"0.0000","length":"1","width":"2","height":"2","grossWeight":"1","netWeight":"1","costPrice":"1","sellingPrice":"1","itemName":null,"itemCode":null,"equipmentName":null,"itemCategory":null,"itemBrand":null}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-12 14:42:22');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2054089831961858050,'物料',2,'com.ruoyi.wms.controller.ItemController.edit()','PUT',1,'admin','研发部门','/wms/item','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2053858205260615682","ids":null,"itemCode":null,"itemName":"b","itemCategory":"1828365014901886978","unit":"b","itemBrand":"1828407291103842306","allowBox":1,"specLevel":null,"equipmentName":"b","equipmentType":"通装","status":"1","productMark":"b","modelText":null,"remark":"b","sku":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2053858205306753025","skuName":"b","itemId":"2053858205260615682","specModel":"b","status":"1","volume":"0.0000","length":"1.0","width":"1.0","height":"1.0","grossWeight":"1.000","netWeight":"1.000","costPrice":"1.00","sellingPrice":"1.00","itemName":null,"itemCode":null,"equipmentName":null,"itemCategory":null,"itemBrand":null},{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2053858205306753026","skuName":"c","itemId":"2053858205260615682","specModel":"c","status":"1","volume":"0.0000","length":"1","width":"1","height":"1","grossWeight":"1","netWeight":"1","costPrice":"1","sellingPrice":"1","itemName":null,"itemCode":null,"equipmentName":null,"itemCategory":null,"itemBrand":null}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-12 14:42:34');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2054089980733820929,'物料',1,'com.ruoyi.wms.controller.ItemController.add()','POST',1,'admin','研发部门','/wms/item','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"ids":null,"itemCode":null,"itemName":"d","itemCategory":"1828365043024695297","unit":"d","itemBrand":"1828407291103842306","allowBox":1,"specLevel":null,"equipmentName":"d","equipmentType":"专装","status":"1","productMark":"d","modelText":null,"remark":"d","sku":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"skuName":"d","itemId":"2054089980620574721","specModel":"d","status":"1","volume":"0","length":"1","width":"1","height":"1","grossWeight":"1","netWeight":"1","costPrice":"1","sellingPrice":"1","itemName":null,"itemCode":null,"equipmentName":null,"itemCategory":null,"itemBrand":null}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-12 14:43:09');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2054090129816162305,'sku信息',3,'com.ruoyi.wms.controller.ItemSkuController.remove()','DELETE',1,'admin','研发部门','/wms/itemSku/2053127932654985219','0:0:0:0:0:0:0:1','内网IP','{}','',0,'至少包含一个商品规格','2026-05-12 14:43:45');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2054090140914290689,'sku信息',3,'com.ruoyi.wms.controller.ItemSkuController.remove()','DELETE',1,'admin','研发部门','/wms/itemSku/2053127932654985218','0:0:0:0:0:0:0:1','内网IP','{}','',0,'至少包含一个商品规格','2026-05-12 14:43:47');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2054090176813338626,'sku信息',3,'com.ruoyi.wms.controller.ItemSkuController.remove()','DELETE',1,'admin','研发部门','/wms/itemSku/2053127932654985218','0:0:0:0:0:0:0:1','内网IP','{}','',0,'至少包含一个商品规格','2026-05-12 14:43:56');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2054099322778521601,'物料',2,'com.ruoyi.wms.controller.ItemController.edit()','PUT',1,'admin','研发部门','/wms/item','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2053127932654985217","ids":null,"itemCode":"IP","itemName":"iphone12","itemCategory":"1828365043024695297","unit":null,"itemBrand":null,"allowBox":1,"specLevel":null,"equipmentName":null,"equipmentType":"通装","status":"1","productMark":null,"modelText":null,"remark":null,"sku":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2053127932654985218","skuName":"1280","itemId":"2053127932654985217","specModel":"128","status":"1","volume":null,"length":null,"width":null,"height":null,"grossWeight":null,"netWeight":null,"costPrice":null,"sellingPrice":null,"itemName":null,"itemCode":null,"equipmentName":null,"itemCategory":null,"itemBrand":null}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-12 15:20:17');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2054100788436111362,'sku信息',3,'com.ruoyi.wms.controller.ItemSkuController.remove()','DELETE',1,'admin','研发部门','/wms/itemSku/2053858205306753026','0:0:0:0:0:0:0:1','内网IP','{}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-12 15:26:06');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2054100799756537857,'物料',2,'com.ruoyi.wms.controller.ItemController.edit()','PUT',1,'admin','研发部门','/wms/item','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2053858205260615682","ids":null,"itemCode":null,"itemName":"b","itemCategory":"1828365014901886978","unit":"b","itemBrand":"1828407291103842306","allowBox":1,"specLevel":null,"equipmentName":"b","equipmentType":"通装","status":"1","productMark":"b","modelText":null,"remark":"b","sku":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2053858205306753025","skuName":"b","itemId":"2053858205260615682","specModel":"b","status":"1","volume":"0.0000","length":"1.0","width":"1.0","height":"1.0","grossWeight":"1.000","netWeight":"1.000","costPrice":"1.00","sellingPrice":"1.00","itemName":null,"itemCode":null,"equipmentName":null,"itemCategory":null,"itemBrand":null}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-12 15:26:09');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2054101197615632385,'物料',1,'com.ruoyi.wms.controller.ItemController.add()','POST',1,'admin','研发部门','/wms/item','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"ids":null,"itemCode":null,"itemName":"xx","itemCategory":"1828365014901886978","unit":"xx","itemBrand":"1828407291103842306","allowBox":1,"specLevel":null,"equipmentName":"xx","equipmentType":"xx","status":"1","productMark":"xx","modelText":null,"remark":"xx","sku":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"skuName":"xx","itemId":"2054101197485608961","specModel":"","status":"1","volume":null,"length":null,"width":null,"height":null,"grossWeight":null,"netWeight":null,"costPrice":null,"sellingPrice":null,"itemName":null,"itemCode":null,"equipmentName":null,"itemCategory":null,"itemBrand":null}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-12 15:27:44');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2054199224246280194,'物料',1,'com.ruoyi.wms.controller.ItemController.add()','POST',1,'admin','研发部门','/wms/item','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"ids":null,"itemCode":null,"itemName":"n","itemCategory":"1828365014901886978","unit":"n","itemBrand":"1828407291103842306","specLevel":null,"equipmentName":"n","equipmentType":"通装","status":"1","productMark":"n","modelText":null,"remark":"n","sku":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"skuName":"n","itemId":"2054199223839432706","specModel":"n","status":"1","volume":"0","length":"1","width":"1","height":"1","grossWeight":"1","netWeight":"1","costPrice":"1","sellingPrice":"1","itemName":null,"itemCode":null,"equipmentName":null,"itemCategory":null,"itemBrand":null}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-12 21:57:15');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2054199255686782977,'物料',2,'com.ruoyi.wms.controller.ItemController.edit()','PUT',1,'admin','研发部门','/wms/item','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2054199223839432706","ids":null,"itemCode":null,"itemName":"n","itemCategory":"1828365014901886978","unit":"n","itemBrand":"1828407291103842306","specLevel":null,"equipmentName":"n","equipmentType":"专装","status":"1","productMark":"n","modelText":null,"remark":"n","sku":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2054199223948484610","skuName":"n","itemId":"2054199223839432706","specModel":"n","status":"1","volume":"0.0000","length":"1.0","width":"1.0","height":"1.0","grossWeight":"1.000","netWeight":"1.000","costPrice":"1.00","sellingPrice":"1.00","itemName":null,"itemCode":null,"equipmentName":null,"itemCategory":null,"itemBrand":null}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-12 21:57:22');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2054199361722982402,'物料',0,'com.ruoyi.wms.controller.ItemController.batchPrintQrCode()','POST',1,'admin','研发部门','/wms/item/batchPrintQrCode','0:0:0:0:0:0:0:1','内网IP','{"row":{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2054199223839432706","ids":null,"itemCode":null,"itemName":"n","itemCategory":"1828365014901886978","unit":"n","itemBrand":"1828407291103842306","specLevel":null,"equipmentName":"n","equipmentType":"专装","status":"1","productMark":"n","modelText":null,"remark":"n","sku":null},"qrCodeCount":20}','{"code":200,"msg":"操作成功","data":{"itemKey":"n||n","qrCodeCount":20,"row":{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2054199223839432706","ids":null,"itemCode":null,"itemName":"n","itemCategory":"1828365014901886978","unit":"n","itemBrand":"1828407291103842306","specLevel":null,"equipmentName":"n","equipmentType":"专装","status":"1","productMark":"n","modelText":null,"remark":"n","sku":null},"details":[{"serialValue":1000000001,"instanceCode":"1000000001","qrCodeValue":"n||n1000000001","qrContent":"n||n1000000001"},{"serialValue":1000000002,"instanceCode":"1000000002","qrCodeValue":"n||n1000000002","qrContent":"n||n1000000002"},{"serialValue":1000000003,"instanceCode":"1000000003","qrCodeValue":"n||n1000000003","qrContent":"n||n1000000003"},{"serialValue":1000000004,"instanceCode":"1000000004","qrCodeValue":"n||n1000000004","qrContent":"n||n1000000004"},{"serialValue":1000000005,"instanceCode":"1000000005","qrCodeValue":"n||n1000000005","qrContent":"n||n1000000005"},{"serialValue":1000000006,"instanceCode":"1000000006","qrCodeValue":"n||n1000000006","qrContent":"n||n1000000006"},{"serialValue":1000000007,"instanceCode":"1000000007","qrCodeValue":"n||n1000000007","qrContent":"n||n1000000007"},{"serialValue":1000000008,"instanceCode":"1000000008","qrCodeValue":"n||n1000000008","qrContent":"n||n1000000008"},{"serialValue":1000000009,"instanceCode":"1000000009","qrCodeValue":"n||n1000000009","qrContent":"n||n1000000009"},{"serialValue":1000000010,"instanceCode":"1000000010","qrCodeValue":"n||n1000000010","qrContent":"n||n1000000010"},{"serialValue":1000000011,"instanceCode":"1000000011","qrCodeValue":"n||n1000000011","qrContent":"n||n1000000011"},{"serialValue":1000000012,"instanceCode":"1000000012","qrCodeValue":"n||n1000000012","qrContent":"n||n1000000012"},{"serialValue":1000000013,"instanceCode":"1000000013","qrCodeValue":"n||n1000000013","qrContent":"n||n1000000013"},{"serialValue":1000000014,"instanceCode":"1000000014","qrCodeValue":"n',1,'','2026-05-12 21:57:48');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2054201007081332738,'物料',0,'com.ruoyi.wms.controller.ItemController.batchPrintQrCode()','POST',1,'admin','研发部门','/wms/item/batchPrintQrCode','0:0:0:0:0:0:0:1','内网IP','{"row":{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2052636524512894978","ids":null,"itemCode":"DYJ","itemName":"惠普","itemCategory":"1828365014901886978","unit":"台","itemBrand":"1828407291103842306","specLevel":null,"equipmentName":"装备名称","equipmentType":"通装","status":"1","productMark":null,"modelText":null,"remark":null,"sku":null},"qrCodeCount":1}','{"code":200,"msg":"操作成功","data":{"itemKey":"惠普||惠普6","qrCodeCount":1,"row":{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2052636524512894978","ids":null,"itemCode":"DYJ","itemName":"惠普","itemCategory":"1828365014901886978","unit":"台","itemBrand":"1828407291103842306","specLevel":null,"equipmentName":"装备名称","equipmentType":"通装","status":"1","productMark":null,"modelText":null,"remark":null,"sku":null},"details":[{"serialValue":1000000001,"instanceCode":"1000000001","qrCodeValue":"惠普||惠普61000000001","qrContent":"惠普||惠普61000000001"}]}}',1,'','2026-05-12 22:04:20');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2054222567930400769,'物料',3,'com.ruoyi.wms.controller.ItemController.remove()','DELETE',1,'admin','研发部门','/wms/item/2054101197485608961','0:0:0:0:0:0:0:1','内网IP','{}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-12 23:30:00');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2054237443369304066,'货位',2,'com.ruoyi.wms.controller.LocationController.edit()','PUT',1,'admin','研发部门','/wms/location','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2053041423398596609","locationCode":"HJ-B-R1-C1","locationName":"B-1-1","warehouseId":"1828364609002311682","areaId":"1828364666585911297","rackId":"2053041423335682050","locationStatus":"enabled","locationType":null,"rowNo":1,"columnNo":1,"length":"300","width":"50","height":"301","volume":"40500000","maxWeight":null,"occupiedFlag":0,"sortNo":1001,"remark":null}','',0,'货位长度之和不能超过货架实际长度(900.00cm)','2026-05-13 00:29:07');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2054390584119238657,'货位',2,'com.ruoyi.wms.controller.LocationController.edit()','PUT',1,'admin','研发部门','/wms/location','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2053041423474094083","locationCode":"HJ-B-R2-C3","locationName":"B-2-3","warehouseId":"1828364609002311682","areaId":"1828364666585911297","rackId":"2053041423335682050","locationStatus":"enabled","locationType":null,"rowNo":2,"columnNo":3,"length":"300","width":"50","height":"299","maxWeight":null,"occupiedFlag":0,"sortNo":2003,"remark":null}','',0,'货位长度之和不能超过货架实际长度(900.00cm)','2026-05-13 10:37:39');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2054390685638172674,'货位',2,'com.ruoyi.wms.controller.LocationController.edit()','PUT',1,'admin','研发部门','/wms/location','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2053041423474094084","locationCode":"HJ-B-R3-C1","locationName":"B-3-1","warehouseId":"1828364609002311682","areaId":"1828364666585911297","rackId":"2053041423335682050","locationStatus":"enabled","locationType":null,"rowNo":3,"columnNo":1,"length":"299","width":"50.00","height":"299","maxWeight":null,"occupiedFlag":0,"sortNo":3001,"remark":null}','',0,'货位长度之和不能超过货架实际长度(900.00cm)','2026-05-13 10:38:03');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2054390702251810817,'货位',2,'com.ruoyi.wms.controller.LocationController.edit()','PUT',1,'admin','研发部门','/wms/location','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2053041423474094084","locationCode":"HJ-B-R3-C1","locationName":"B-3-1","warehouseId":"1828364609002311682","areaId":"1828364666585911297","rackId":"2053041423335682050","locationStatus":"enabled","locationType":null,"rowNo":3,"columnNo":1,"length":"299","width":"50.00","height":"300","maxWeight":null,"occupiedFlag":0,"sortNo":3001,"remark":null}','',0,'货位长度之和不能超过货架实际长度(900.00cm)','2026-05-13 10:38:07');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2054390763895496706,'货位',2,'com.ruoyi.wms.controller.LocationController.edit()','PUT',1,'admin','研发部门','/wms/location','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2053041423398596609","locationCode":"HJ-B-R1-C1","locationName":"B-1-1","warehouseId":"1828364609002311682","areaId":"1828364666585911297","rackId":"2053041423335682050","locationStatus":"enabled","locationType":null,"rowNo":1,"columnNo":1,"length":"300","width":"50","height":"299","maxWeight":null,"occupiedFlag":0,"sortNo":1001,"remark":null}','',0,'货位长度之和不能超过货架实际长度(900.00cm)','2026-05-13 10:38:22');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2054390943747252225,'货位',2,'com.ruoyi.wms.controller.LocationController.edit()','PUT',1,'admin','研发部门','/wms/location','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2053041423398596609","locationCode":"HJ-B-R1-C1","locationName":"B-1-1","warehouseId":"1828364609002311682","areaId":"1828364666585911297","rackId":"2053041423335682050","locationStatus":"enabled","locationType":null,"rowNo":1,"columnNo":1,"length":"300","width":"50","height":"299","maxWeight":null,"occupiedFlag":0,"sortNo":1001,"remark":null}','',0,'货位长度之和不能超过货架实际长度(900.00cm)','2026-05-13 10:39:04');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2054391184571604993,'货位',2,'com.ruoyi.wms.controller.LocationController.edit()','PUT',1,'admin','研发部门','/wms/location','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2053041423398596609","locationCode":"HJ-B-R1-C1","locationName":"B-1-1","warehouseId":"1828364609002311682","areaId":"1828364666585911297","rackId":"2053041423335682050","locationStatus":"enabled","locationType":null,"rowNo":1,"columnNo":1,"length":"300","width":"50","height":"299","maxWeight":null,"occupiedFlag":0,"sortNo":1001,"remark":null}','',0,'货位长度之和不能超过货架实际长度(900.00cm)','2026-05-13 10:40:02');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2054445955898748929,'物料',0,'com.ruoyi.wms.controller.ItemController.batchPrintQrCode()','POST',1,'admin','研发部门','/wms/item/batchPrintQrCode','0:0:0:0:0:0:0:1','内网IP','{"row":{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2053852496372543489","ids":null,"itemCode":"a","itemName":"a","itemCategory":"1828405743737016322","unit":"a","itemBrand":"1828407291103842306","specLevel":null,"equipmentName":"a","equipmentType":"通装","status":"1","productMark":"a","modelText":null,"remark":"a","sku":null},"qrCodeCount":10}','{"code":200,"msg":"操作成功","data":{"itemKey":"a-a","qrCodeCount":10,"row":{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2053852496372543489","ids":null,"itemCode":"a","itemName":"a","itemCategory":"1828405743737016322","unit":"a","itemBrand":"1828407291103842306","specLevel":null,"equipmentName":"a","equipmentType":"通装","status":"1","productMark":"a","modelText":null,"remark":"a","sku":null},"details":[{"serialValue":1000000001,"instanceCode":"1000000001","qrCodeValue":"a-a1000000001","qrContent":"a-a1000000001"},{"serialValue":1000000002,"instanceCode":"1000000002","qrCodeValue":"a-a1000000002","qrContent":"a-a1000000002"},{"serialValue":1000000003,"instanceCode":"1000000003","qrCodeValue":"a-a1000000003","qrContent":"a-a1000000003"},{"serialValue":1000000004,"instanceCode":"1000000004","qrCodeValue":"a-a1000000004","qrContent":"a-a1000000004"},{"serialValue":1000000005,"instanceCode":"1000000005","qrCodeValue":"a-a1000000005","qrContent":"a-a1000000005"},{"serialValue":1000000006,"instanceCode":"1000000006","qrCodeValue":"a-a1000000006","qrContent":"a-a1000000006"},{"serialValue":1000000007,"instanceCode":"1000000007","qrCodeValue":"a-a1000000007","qrContent":"a-a1000000007"},{"serialValue":1000000008,"instanceCode":"1000000008","qrCodeValue":"a-a1000000008","qrContent":"a-a1000000008"},{"serialValue":1000000009,"instanceCode":"1000000009","qrCodeValue":"a-a1000000009","qrContent":"a-a1000000009"},{"serialValue":1000000010,"instanceCode":"1000000010","qrCodeValue":"a-a1000000010","qrContent":"a-a1000000010"}]}}',1,'','2026-05-13 14:17:40');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2054446271838892035,'物料',0,'com.ruoyi.wms.controller.ItemController.batchPrintQrCode()','POST',1,'admin','研发部门','/wms/item/batchPrintQrCode','0:0:0:0:0:0:0:1','内网IP','{"row":{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2052636524512894978","ids":null,"itemCode":"DYJ","itemName":"惠普","itemCategory":"1828365014901886978","unit":"台","itemBrand":"1828407291103842306","specLevel":null,"equipmentName":"装备名称","equipmentType":"通装","status":"1","productMark":null,"modelText":null,"remark":null,"sku":null},"qrCodeCount":1}','{"code":200,"msg":"操作成功","data":{"itemKey":"惠普-惠普6","qrCodeCount":1,"row":{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2052636524512894978","ids":null,"itemCode":"DYJ","itemName":"惠普","itemCategory":"1828365014901886978","unit":"台","itemBrand":"1828407291103842306","specLevel":null,"equipmentName":"装备名称","equipmentType":"通装","status":"1","productMark":null,"modelText":null,"remark":null,"sku":null},"details":[{"serialValue":1000000001,"instanceCode":"1000000001","qrCodeValue":"惠普-惠普61000000001","qrContent":"惠普-惠普61000000001"}]}}',1,'','2026-05-13 14:18:56');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2054492915343126529,'入库单',1,'com.ruoyi.wms.controller.ReceiptOrderController.add()','POST',1,'admin','研发部门','/wms/receiptOrder','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2054492913195642881","receiptOrderNo":"RK05138177","receiptOrderType":2,"merchantId":null,"orderNo":null,"basisNo":null,"dispatchMode":null,"noticeOrg":null,"receiveUnit":null,"purchaseDate":null,"receiptDate":null,"purchaserName":null,"acceptorName":null,"keeperName":null,"totalQuantity":"3","payableAmount":"0","receiptOrderStatus":0,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","remark":null,"details":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2054492913195642882","receiptOrderId":null,"skuId":"2053852496494178305","quantity":"1","amount":"0","equipmentCode":"a","specModel":"a","productMark":"a","qualityGrade":null,"unitPrice":null,"lineAmount":"0","productionDate":null,"expirationDate":null,"remark":null,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","rackId":"2053041345556508674","locationId":"2053041345556508675","generateItemInstance":1,"generatedInstanceQuantity":0,"receiptItemInstances":[{"id":"2054445955298963460","instanceCode":"a-a1000000005","boxCode":"","productMark":"a","qualityGrade":null,"remark":null}]},{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2054492913195642883","receiptOrderId":null,"skuId":"2053852496494178305","quantity":"1","amount":"0","equipmentCode":"a","specModel":"a","productMark":"a","qualityGrade":null,"unitPrice":null,"lineAmount":"0","productionDate":null,"expirationDate":null,"remark":null,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","rackId":"2053041345556508674","locationId":"2053041345619423233","generateItemInstance":1,"generatedInstanceQuantity":0,"receiptItemInstances":[{"id":"2054445955298963459","instanceCode":"a-a1000000004","boxCode":"","productMark":"a","qualityGrade":null,"remark":null}]},{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2054492913195642884","receiptOrderId":null,"skuId":"2053852','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-13 17:24:16');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2054493599459274753,'入库单',2,'com.ruoyi.wms.controller.ReceiptOrderController.doWarehousing()','POST',1,'admin','研发部门','/wms/receiptOrder/warehousing','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2054493598368755713","receiptOrderNo":"RK05136865","receiptOrderType":2,"merchantId":null,"orderNo":null,"basisNo":null,"dispatchMode":null,"noticeOrg":null,"receiveUnit":null,"purchaseDate":null,"receiptDate":null,"purchaserName":null,"acceptorName":null,"keeperName":null,"totalQuantity":"1","payableAmount":"0","receiptOrderStatus":1,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","remark":null,"details":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2054493598368755714","receiptOrderId":null,"skuId":"2053852496494178305","quantity":"1","amount":"0","equipmentCode":"a","specModel":"a","productMark":"a","qualityGrade":null,"unitPrice":null,"lineAmount":"0","productionDate":null,"expirationDate":null,"remark":null,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","rackId":"2053041345556508674","locationId":"2053041345619423237","generateItemInstance":1,"generatedInstanceQuantity":0,"receiptItemInstances":[{"id":"2054445955265409026","instanceCode":"a-a1000000001","boxCode":"99999999","productMark":"a","qualityGrade":null,"remark":null}]}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-13 17:26:59');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2054493806725001218,'入库单详情',3,'com.ruoyi.wms.controller.ReceiptOrderDetailController.remove()','DELETE',1,'admin','研发部门','/wms/receiptOrderDetail/2054492913195642884','0:0:0:0:0:0:0:1','内网IP','{}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-13 17:27:49');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2054493816199933954,'入库单详情',3,'com.ruoyi.wms.controller.ReceiptOrderDetailController.remove()','DELETE',1,'admin','研发部门','/wms/receiptOrderDetail/2054492913195642882','0:0:0:0:0:0:0:1','内网IP','{}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-13 17:27:51');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2054493829051281410,'入库单详情',3,'com.ruoyi.wms.controller.ReceiptOrderDetailController.remove()','DELETE',1,'admin','研发部门','/wms/receiptOrderDetail/2054492913195642883','0:0:0:0:0:0:0:1','内网IP','{}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-13 17:27:54');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2054731781996134402,'货位',2,'com.ruoyi.wms.controller.LocationController.edit()','PUT',1,'admin','研发部门','/wms/location','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2053041423474094081","locationCode":"HJ-B-R2-C1","locationName":"B-2-1","warehouseId":"1828364609002311682","areaId":"1828364666585911297","rackId":"2053041423335682050","locationStatus":"enabled","locationType":null,"rowNo":2,"columnNo":1,"length":"300.00","width":"50.00","height":"300","maxWeight":null,"occupiedFlag":0,"sortNo":2001,"remark":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-14 09:13:27');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2054494021225902082,'入库单',2,'com.ruoyi.wms.controller.ReceiptOrderController.edit()','PUT',1,'admin','研发部门','/wms/receiptOrder','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2054492913195642881","receiptOrderNo":"RK05138177","receiptOrderType":2,"merchantId":null,"orderNo":null,"basisNo":null,"dispatchMode":null,"noticeOrg":null,"receiveUnit":null,"purchaseDate":null,"receiptDate":null,"purchaserName":null,"acceptorName":null,"keeperName":null,"totalQuantity":"3","payableAmount":"0","receiptOrderStatus":0,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","remark":null,"details":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2054494021095878658","receiptOrderId":"2054492913195642881","skuId":"2053852496494178305","quantity":"1","amount":"0","equipmentCode":"a","specModel":"a","productMark":"a","qualityGrade":null,"unitPrice":null,"lineAmount":"0","productionDate":null,"expirationDate":null,"remark":null,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","rackId":"2053041345556508674","locationId":"2053041345556508675","generateItemInstance":1,"generatedInstanceQuantity":0,"receiptItemInstances":[{"id":"2054445955298963460","instanceCode":"a-a1000000005","boxCode":"","productMark":"a","qualityGrade":null,"remark":null}]},{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2054494021095878659","receiptOrderId":"2054492913195642881","skuId":"2053852496494178305","quantity":"1","amount":"0","equipmentCode":"a","specModel":"a","productMark":"a","qualityGrade":null,"unitPrice":null,"lineAmount":"0","productionDate":null,"expirationDate":null,"remark":null,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","rackId":"2053041345556508674","locationId":"2053041345619423233","generateItemInstance":1,"generatedInstanceQuantity":0,"receiptItemInstances":[{"id":"2054445955298963461","instanceCode":"a-a1000000006","boxCode":"","productMark":"a","qualityGrade":null,"remark":null}]},{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2054494021095878660","rec','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-13 17:28:40');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2054499863459360769,'入库单详情',3,'com.ruoyi.wms.controller.ReceiptOrderDetailController.remove()','DELETE',1,'admin','研发部门','/wms/receiptOrderDetail/2054494021095878660','0:0:0:0:0:0:0:1','内网IP','{}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-13 17:51:53');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2054499870736478209,'入库单详情',3,'com.ruoyi.wms.controller.ReceiptOrderDetailController.remove()','DELETE',1,'admin','研发部门','/wms/receiptOrderDetail/2054494021095878658','0:0:0:0:0:0:0:1','内网IP','{}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-13 17:51:55');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2054499885726920706,'入库单详情',3,'com.ruoyi.wms.controller.ReceiptOrderDetailController.remove()','DELETE',1,'admin','研发部门','/wms/receiptOrderDetail/2054494021095878659','0:0:0:0:0:0:0:1','内网IP','{}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-13 17:51:58');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2054500093005230082,'入库单',2,'com.ruoyi.wms.controller.ReceiptOrderController.edit()','PUT',1,'admin','研发部门','/wms/receiptOrder','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2054492913195642881","receiptOrderNo":"RK05138177","receiptOrderType":2,"merchantId":null,"orderNo":null,"basisNo":null,"dispatchMode":null,"noticeOrg":null,"receiveUnit":null,"purchaseDate":null,"receiptDate":null,"purchaserName":null,"acceptorName":null,"keeperName":null,"totalQuantity":"3","payableAmount":"0","receiptOrderStatus":0,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","remark":null,"details":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2054500092657102849","receiptOrderId":"2054492913195642881","skuId":"2053852496494178305","quantity":"1","amount":"0","equipmentCode":"a","specModel":"a","productMark":"a","qualityGrade":null,"unitPrice":null,"lineAmount":"0","productionDate":null,"expirationDate":null,"remark":null,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","rackId":"2053041345556508674","locationId":"2053041345556508675","generateItemInstance":1,"generatedInstanceQuantity":0,"receiptItemInstances":[{"id":"2054445955298963460","instanceCode":"a-a1000000005","boxCode":"","productMark":"a","qualityGrade":null,"remark":null}]},{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2054500092657102850","receiptOrderId":"2054492913195642881","skuId":"2053852496494178305","quantity":"1","amount":"0","equipmentCode":"a","specModel":"a","productMark":"a","qualityGrade":null,"unitPrice":null,"lineAmount":"0","productionDate":null,"expirationDate":null,"remark":null,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","rackId":"2053041345556508674","locationId":"2053041345619423233","generateItemInstance":1,"generatedInstanceQuantity":0,"receiptItemInstances":[{"id":"2054445955298963461","instanceCode":"a-a1000000006","boxCode":"","productMark":"a","qualityGrade":null,"remark":null}]},{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2054500092657102851","rec','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-13 17:52:48');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2054500261146488833,'入库单',3,'com.ruoyi.wms.controller.ReceiptOrderController.remove()','DELETE',1,'admin','研发部门','/wms/receiptOrder/2054492913195642881','0:0:0:0:0:0:0:1','内网IP','{}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-13 17:53:28');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2054500600377602049,'入库单',1,'com.ruoyi.wms.controller.ReceiptOrderController.add()','POST',1,'admin','研发部门','/wms/receiptOrder','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2054500600260161538","receiptOrderNo":"RK05131358","receiptOrderType":2,"merchantId":null,"orderNo":null,"basisNo":null,"dispatchMode":null,"noticeOrg":null,"receiveUnit":null,"purchaseDate":null,"receiptDate":null,"purchaserName":null,"acceptorName":null,"keeperName":null,"totalQuantity":"3","payableAmount":"0","receiptOrderStatus":0,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","remark":null,"details":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2054500600327270402","receiptOrderId":null,"skuId":"2053852496494178305","quantity":"1","amount":"0","equipmentCode":"a","specModel":"a","productMark":"a","qualityGrade":null,"unitPrice":null,"lineAmount":"0","productionDate":null,"expirationDate":null,"remark":null,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","rackId":"2053041345556508674","locationId":"2053041345556508675","generateItemInstance":1,"generatedInstanceQuantity":0,"receiptItemInstances":[{"id":"2054445955298963461","instanceCode":"a-a1000000006","boxCode":"","productMark":"a","qualityGrade":null,"remark":null}]},{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2054500600327270403","receiptOrderId":null,"skuId":"2053852496494178305","quantity":"1","amount":"0","equipmentCode":"a","specModel":"a","productMark":"a","qualityGrade":null,"unitPrice":null,"lineAmount":"0","productionDate":null,"expirationDate":null,"remark":null,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","rackId":"2053041345556508674","locationId":"2053041345619423233","generateItemInstance":1,"generatedInstanceQuantity":0,"receiptItemInstances":[{"id":"2054445955298963460","instanceCode":"a-a1000000005","boxCode":"","productMark":"a","qualityGrade":null,"remark":null}]},{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2054500600327270404","receiptOrderId":null,"skuId":"2053852','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-13 17:54:49');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2054500717935554561,'入库单',3,'com.ruoyi.wms.controller.ReceiptOrderController.remove()','DELETE',1,'admin','研发部门','/wms/receiptOrder/2054500600260161538','0:0:0:0:0:0:0:1','内网IP','{}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-13 17:55:17');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2054510260040036354,'入库单',1,'com.ruoyi.wms.controller.ReceiptOrderController.add()','POST',1,'admin','研发部门','/wms/receiptOrder','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2054510259423473665","receiptOrderNo":"RK05137553","receiptOrderType":2,"merchantId":null,"orderNo":null,"basisNo":null,"dispatchMode":null,"noticeOrg":null,"receiveUnit":null,"purchaseDate":null,"receiptDate":null,"purchaserName":null,"acceptorName":null,"keeperName":null,"totalQuantity":"3","payableAmount":"0","receiptOrderStatus":0,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","remark":null,"details":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2054510259503165442","receiptOrderId":null,"skuId":"2053852496494178305","quantity":"1","amount":"0","equipmentCode":"a","specModel":"a","productMark":"a","qualityGrade":null,"unitPrice":null,"lineAmount":"0","productionDate":null,"expirationDate":null,"remark":null,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","rackId":"2053041345556508674","locationId":"2053041345556508675","generateItemInstance":1,"generatedInstanceQuantity":0,"receiptItemInstances":[{"id":"2054445955298963461","instanceCode":"a-a1000000006","boxCode":"","productMark":"a","qualityGrade":null,"remark":null}]},{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2054510259503165443","receiptOrderId":null,"skuId":"2053852496494178305","quantity":"1","amount":"0","equipmentCode":"a","specModel":"a","productMark":"a","qualityGrade":null,"unitPrice":null,"lineAmount":"0","productionDate":null,"expirationDate":null,"remark":null,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","rackId":"2053041345556508674","locationId":"2053041345619423233","generateItemInstance":1,"generatedInstanceQuantity":0,"receiptItemInstances":[{"id":"2054445955298963462","instanceCode":"a-a1000000007","boxCode":"","productMark":"a","qualityGrade":null,"remark":null}]},{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2054510259503165444","receiptOrderId":null,"skuId":"2053852','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-13 18:33:12');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2054510528634875905,'入库单',1,'com.ruoyi.wms.controller.ReceiptOrderController.add()','POST',1,'admin','研发部门','/wms/receiptOrder','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2054510528441937921","receiptOrderNo":"RK05131657","receiptOrderType":2,"merchantId":null,"orderNo":null,"basisNo":null,"dispatchMode":null,"noticeOrg":null,"receiveUnit":null,"purchaseDate":null,"receiptDate":null,"purchaserName":null,"acceptorName":null,"keeperName":null,"totalQuantity":"1","payableAmount":"0","receiptOrderStatus":0,"warehouseId":"1828364609002311682","areaId":"1828364666585911297","remark":null,"details":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2054510528534212610","receiptOrderId":null,"skuId":"2053852496494178305","quantity":"1","amount":"0","equipmentCode":"a","specModel":"a","productMark":"a","qualityGrade":null,"unitPrice":null,"lineAmount":"0","productionDate":null,"expirationDate":null,"remark":null,"warehouseId":"1828364609002311682","areaId":"1828364666585911297","rackId":"2053041423335682050","locationId":"2053041423398596609","generateItemInstance":1,"generatedInstanceQuantity":0,"receiptItemInstances":[{"id":"2054445955298963457","instanceCode":"a-a1000000002","boxCode":"","productMark":"a","qualityGrade":null,"remark":null}]}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-13 18:34:16');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2054510631814754306,'入库单',2,'com.ruoyi.wms.controller.ReceiptOrderController.doWarehousing()','POST',1,'admin','研发部门','/wms/receiptOrder/warehousing','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2054510528441937921","receiptOrderNo":"RK05131657","receiptOrderType":2,"merchantId":null,"orderNo":null,"basisNo":null,"dispatchMode":null,"noticeOrg":null,"receiveUnit":null,"purchaseDate":null,"receiptDate":null,"purchaserName":null,"acceptorName":null,"keeperName":null,"totalQuantity":"1","payableAmount":"0","receiptOrderStatus":1,"warehouseId":"1828364609002311682","areaId":"1828364666585911297","remark":null,"details":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2054510528534212610","receiptOrderId":"2054510528441937921","skuId":"2053852496494178305","quantity":"1","amount":"0","equipmentCode":"a","specModel":"a","productMark":"a","qualityGrade":null,"unitPrice":null,"lineAmount":"0","productionDate":null,"expirationDate":null,"remark":null,"warehouseId":"1828364609002311682","areaId":"1828364666585911297","rackId":"2053041423335682050","locationId":"2053041423398596609","generateItemInstance":1,"generatedInstanceQuantity":0,"receiptItemInstances":[{"id":"2054445955298963457","instanceCode":"a-a1000000002","boxCode":"","productMark":"a","qualityGrade":null,"remark":null}]}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-13 18:34:40');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2054513015580323842,'菜单管理',2,'com.ruoyi.system.controller.system.SysMenuController.edit()','PUT',1,'admin','研发部门','/system/menu','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":"2024-08-06 15:50:30","updateBy":null,"updateTime":null,"menuId":"1820729144067321858","parentId":"2060000000000001003","menuName":"器材总账","orderNum":0,"path":"inventory","component":"wms/inventory/statistic","queryParam":null,"isFrame":"0","isCache":"0","menuType":"C","visible":"1","status":"1","perms":"wms:inventory:all","icon":"chart","remark":"兼容保留：第二阶段主入口已收口到器材总账"}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-13 18:44:09');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2054513184275230721,'菜单管理',2,'com.ruoyi.system.controller.system.SysMenuController.edit()','PUT',1,'admin','研发部门','/system/menu','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":"2026-05-06 15:27:18","updateBy":null,"updateTime":null,"menuId":"2051926764629102594","parentId":"2060000000000001003","menuName":"库存总账","orderNum":1,"path":"ledger","component":"wms/inventory/ledger","queryParam":null,"isFrame":"0","isCache":"0","menuType":"C","visible":"0","status":"1","perms":"wms:ledger:list","icon":"excel","remark":"第二阶段库存中心主入口"}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-13 18:44:49');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2054522541360607234,'箱体',3,'com.ruoyi.wms.controller.BoxController.remove()','DELETE',1,'admin','研发部门','/wms/box/2053119223216283650','0:0:0:0:0:0:0:1','内网IP','{}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-13 19:22:00');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2054730992300965890,'货架',1,'com.ruoyi.wms.controller.RackController.add()','POST',1,'admin','研发部门','/wms/rack','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"rackCode":null,"rackName":"高2","warehouseId":"1828364740028174337","areaId":"1829397621378838530","rackStatus":"enabled","rackType":"standard","rowCount":1,"columnCount":1,"length":"50","width":"50","height":"50","orderNum":0,"remark":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-14 09:10:18');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2054731372506234882,'货位',2,'com.ruoyi.wms.controller.LocationController.edit()','PUT',1,'admin','研发部门','/wms/location','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2053041423398596609","locationCode":"HJ-B-R1-C1","locationName":"B-1-1","warehouseId":"1828364609002311682","areaId":"1828364666585911297","rackId":"2053041423335682050","locationStatus":"enabled","locationType":null,"rowNo":1,"columnNo":1,"length":"299","width":"50","height":"300","maxWeight":null,"occupiedFlag":0,"sortNo":1001,"remark":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-14 09:11:49');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2054731414612852737,'货位',2,'com.ruoyi.wms.controller.LocationController.edit()','PUT',1,'admin','研发部门','/wms/location','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2053041423398596609","locationCode":"HJ-B-R1-C1","locationName":"B-1-1","warehouseId":"1828364609002311682","areaId":"1828364666585911297","rackId":"2053041423335682050","locationStatus":"enabled","locationType":null,"rowNo":1,"columnNo":1,"length":"300","width":"50.00","height":"300","maxWeight":null,"occupiedFlag":0,"sortNo":1001,"remark":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-14 09:11:59');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2054731449983418369,'货位',2,'com.ruoyi.wms.controller.LocationController.edit()','PUT',1,'admin','研发部门','/wms/location','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2053041423398596609","locationCode":"HJ-B-R1-C1","locationName":"B-1-1","warehouseId":"1828364609002311682","areaId":"1828364666585911297","rackId":"2053041423335682050","locationStatus":"enabled","locationType":null,"rowNo":1,"columnNo":1,"length":"300.00","width":"50.00","height":"299","maxWeight":null,"occupiedFlag":0,"sortNo":1001,"remark":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-14 09:12:07');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2054731758495449089,'货位',2,'com.ruoyi.wms.controller.LocationController.edit()','PUT',1,'admin','研发部门','/wms/location','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2053041423474094081","locationCode":"HJ-B-R2-C1","locationName":"B-2-1","warehouseId":"1828364609002311682","areaId":"1828364666585911297","rackId":"2053041423335682050","locationStatus":"enabled","locationType":null,"rowNo":2,"columnNo":1,"length":"300.00","width":"50.00","height":"301","maxWeight":null,"occupiedFlag":0,"sortNo":2001,"remark":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-14 09:13:21');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2054731800174247937,'货位',2,'com.ruoyi.wms.controller.LocationController.edit()','PUT',1,'admin','研发部门','/wms/location','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2053041423398596609","locationCode":"HJ-B-R1-C1","locationName":"B-1-1","warehouseId":"1828364609002311682","areaId":"1828364666585911297","rackId":"2053041423335682050","locationStatus":"enabled","locationType":null,"rowNo":1,"columnNo":1,"length":"300.00","width":"50.00","height":"299","maxWeight":null,"occupiedFlag":0,"sortNo":1001,"remark":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-14 09:13:31');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2054731815370211330,'货位',2,'com.ruoyi.wms.controller.LocationController.edit()','PUT',1,'admin','研发部门','/wms/location','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2053041423474094081","locationCode":"HJ-B-R2-C1","locationName":"B-2-1","warehouseId":"1828364609002311682","areaId":"1828364666585911297","rackId":"2053041423335682050","locationStatus":"enabled","locationType":null,"rowNo":2,"columnNo":1,"length":"300.00","width":"50.00","height":"301","maxWeight":null,"occupiedFlag":0,"sortNo":2001,"remark":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-14 09:13:35');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2054731870080712706,'货位',2,'com.ruoyi.wms.controller.LocationController.edit()','PUT',1,'admin','研发部门','/wms/location','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2053041423402790913","locationCode":"HJ-B-R1-C2","locationName":"B-1-2","warehouseId":"1828364609002311682","areaId":"1828364666585911297","rackId":"2053041423335682050","locationStatus":"enabled","locationType":null,"rowNo":1,"columnNo":2,"length":"100","width":"50.00","height":"300.00","maxWeight":null,"occupiedFlag":0,"sortNo":1002,"remark":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-14 09:13:48');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2054732104273870850,'货位',2,'com.ruoyi.wms.controller.LocationController.edit()','PUT',1,'admin','研发部门','/wms/location','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2053041423402790914","locationCode":"HJ-B-R1-C3","locationName":"B-1-3","warehouseId":"1828364609002311682","areaId":"1828364666585911297","rackId":"2053041423335682050","locationStatus":"enabled","locationType":null,"rowNo":1,"columnNo":3,"length":"500","width":"50.00","height":"300.00","maxWeight":null,"occupiedFlag":0,"sortNo":1003,"remark":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-14 09:14:43');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2054732172653608962,'货位',2,'com.ruoyi.wms.controller.LocationController.edit()','PUT',1,'admin','研发部门','/wms/location','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2053041423402790913","locationCode":"HJ-B-R1-C2","locationName":"B-1-2","warehouseId":"1828364609002311682","areaId":"1828364666585911297","rackId":"2053041423335682050","locationStatus":"enabled","locationType":null,"rowNo":1,"columnNo":2,"length":"0","width":"50.00","height":"300.00","maxWeight":null,"occupiedFlag":0,"sortNo":1002,"remark":null}','',0,'货位长度必须大于0','2026-05-14 09:15:00');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2054732187987984386,'货位',2,'com.ruoyi.wms.controller.LocationController.edit()','PUT',1,'admin','研发部门','/wms/location','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2053041423402790913","locationCode":"HJ-B-R1-C2","locationName":"B-1-2","warehouseId":"1828364609002311682","areaId":"1828364666585911297","rackId":"2053041423335682050","locationStatus":"enabled","locationType":null,"rowNo":1,"columnNo":2,"length":"1","width":"50.00","height":"300.00","maxWeight":null,"occupiedFlag":0,"sortNo":1002,"remark":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-14 09:15:03');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2054732210595282946,'货位',2,'com.ruoyi.wms.controller.LocationController.edit()','PUT',1,'admin','研发部门','/wms/location','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2053041423402790914","locationCode":"HJ-B-R1-C3","locationName":"B-1-3","warehouseId":"1828364609002311682","areaId":"1828364666585911297","rackId":"2053041423335682050","locationStatus":"enabled","locationType":null,"rowNo":1,"columnNo":3,"length":"599","width":"50.00","height":"300.00","maxWeight":null,"occupiedFlag":0,"sortNo":1003,"remark":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-14 09:15:09');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2054732521187688449,'物料类型',1,'com.ruoyi.wms.controller.ItemCategoryController.add()','POST',1,'admin','研发部门','/wms/itemCategory','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"parentId":null,"categoryName":"器材箱","orderNum":null,"status":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-14 09:16:23');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2054733101314457601,'物料',1,'com.ruoyi.wms.controller.ItemController.add()','POST',1,'admin','研发部门','/wms/item','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"ids":null,"itemCode":null,"itemName":"器材箱","itemCategory":"2054732521107996674","unit":null,"itemBrand":null,"specLevel":null,"equipmentName":null,"equipmentType":null,"status":"1","productMark":null,"modelText":null,"remark":null,"sku":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"skuName":"小箱子","itemId":"2054733100475596802","specModel":"","status":"1","length":null,"width":null,"height":null,"grossWeight":null,"netWeight":null,"costPrice":null,"sellingPrice":null,"itemName":null,"itemCode":null,"equipmentName":null,"itemCategory":null,"itemBrand":null}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-14 09:18:41');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2056770294870519810,'库区',2,'com.ruoyi.wms.controller.AreaController.edit()','PUT',1,'admin','研发部门','/wms/area','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2056770261064429569","areaCode":null,"areaName":"北B区","warehouseId":"1828364459110469633","status":null,"orderNum":null,"remark":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-20 00:13:46');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2054735451844362241,'出库单',1,'com.ruoyi.wms.controller.ShipmentOrderController.add()','POST',1,'admin','研发部门','/wms/shipmentOrder','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2054735451613675522","shipmentOrderNo":"CK05143470","shipmentOrderType":2,"orderNo":null,"merchantId":null,"basisNo":null,"dispatchMode":null,"noticeOrg":null,"receiveUnit":null,"purchaseDate":null,"shipmentDate":null,"purchaserName":null,"acceptorName":null,"keeperName":null,"receivableAmount":"0","totalQuantity":"1","shipmentOrderStatus":0,"warehouseId":"1828364459110469633","areaId":null,"remark":null,"details":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"shipmentOrderId":null,"skuId":"2053852496494178305","quantity":"1","amount":"0","equipmentCode":"a-a1000000001","specModel":"a","productMark":"a","qualityGrade":null,"unitPrice":null,"lineAmount":"0","warehouseId":"1828364459110469633","areaId":"1828364518342430721","productionDate":null,"expirationDate":null,"inventoryDetailId":"2054493598884655106","itemInstanceId":"2054445955265409026","boxId":"2054493598427475969","remark":null,"key":"1828364459110469633_1828364518342430721_2053852496494178305"}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-14 09:28:02');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2054930695135289345,'入库单',2,'com.ruoyi.wms.controller.ReceiptOrderController.doWarehousing()','POST',1,'admin','研发部门','/wms/receiptOrder/warehousing','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2054930694493560834","receiptOrderNo":"RK05142665","receiptOrderType":2,"merchantId":null,"orderNo":null,"basisNo":null,"dispatchMode":null,"noticeOrg":null,"receiveUnit":null,"purchaseDate":null,"receiptDate":null,"purchaserName":null,"acceptorName":null,"keeperName":null,"totalQuantity":"2","payableAmount":"0","receiptOrderStatus":1,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","remark":null,"details":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2054930694673915905","receiptOrderId":null,"skuId":101,"quantity":"1","amount":"0","equipmentCode":"GL","specModel":"20KG标准型","productMark":"P2026","qualityGrade":"1","unitPrice":null,"lineAmount":"0","productionDate":"2026-01-01 00:00:00","expirationDate":"2030-01-01 00:00:00","remark":null,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","rackId":"2053041345556508674","locationId":"2053041345556508675","generateItemInstance":1,"generatedInstanceQuantity":0,"receiptItemInstances":[{"id":1002,"instanceCode":"INSTANCE20260004","boxCode":"BOX20260001","productMark":"P2026","qualityGrade":"1","remark":null}]},{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2054930694736830466","receiptOrderId":null,"skuId":101,"quantity":"1","amount":"0","equipmentCode":"GL","specModel":"20KG标准型","productMark":"P2027","qualityGrade":"1","unitPrice":null,"lineAmount":"0","productionDate":"2026-02-01 00:00:00","expirationDate":"2030-02-01 00:00:00","remark":null,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","rackId":"2053041345556508674","locationId":"2053041345556508675","generateItemInstance":1,"generatedInstanceQuantity":0,"receiptItemInstances":[{"id":1003,"instanceCode":"INSTANCE20260005","boxCode":"BOX20260001","productMark":"P2027","qualityGrade":"1","remark":null}]}]}','',0,'器材实例不存在','2026-05-14 22:23:51');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2054930922126827521,'入库单',2,'com.ruoyi.wms.controller.ReceiptOrderController.doWarehousing()','POST',1,'admin','研发部门','/wms/receiptOrder/warehousing','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2054930920805621761","receiptOrderNo":"RK05142665","receiptOrderType":2,"merchantId":null,"orderNo":null,"basisNo":null,"dispatchMode":null,"noticeOrg":null,"receiveUnit":null,"purchaseDate":null,"receiptDate":null,"purchaserName":null,"acceptorName":null,"keeperName":null,"totalQuantity":"2","payableAmount":"0","receiptOrderStatus":1,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","remark":null,"details":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2054930920805621762","receiptOrderId":null,"skuId":"2053852496494178305","quantity":"1","amount":"0","equipmentCode":"a","specModel":"a","productMark":"a","qualityGrade":null,"unitPrice":null,"lineAmount":"0","productionDate":null,"expirationDate":null,"remark":null,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","rackId":"2053041345556508674","locationId":"2053041345556508675","generateItemInstance":1,"generatedInstanceQuantity":0,"receiptItemInstances":[{"id":"2054445955298963458","instanceCode":"a-a1000000003","boxCode":"BOX20260001","productMark":"a","qualityGrade":null,"remark":null}]},{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2054930920876924930","receiptOrderId":null,"skuId":"2053852496494178305","quantity":"1","amount":"0","equipmentCode":"a","specModel":"a","productMark":"a","qualityGrade":null,"unitPrice":null,"lineAmount":"0","productionDate":null,"expirationDate":null,"remark":null,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","rackId":"2053041345556508674","locationId":"2053041345556508675","generateItemInstance":1,"generatedInstanceQuantity":0,"receiptItemInstances":[{"id":"2054445955298963459","instanceCode":"a-a1000000004","boxCode":"BOX20260001","productMark":"a","qualityGrade":null,"remark":null}]}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-14 22:24:45');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2054933008931479554,'入库单',2,'com.ruoyi.wms.controller.ReceiptOrderController.doWarehousing()','POST',1,'admin','研发部门','/wms/receiptOrder/warehousing','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2054510259423473665","receiptOrderNo":"RK05137553","receiptOrderType":2,"merchantId":"1828354284882399233","orderNo":"a","basisNo":"a","dispatchMode":"road","noticeOrg":"a","receiveUnit":"a","purchaseDate":"2026-05-13","receiptDate":"2026-05-15","purchaserName":"a","acceptorName":"a","keeperName":"a","totalQuantity":"3","payableAmount":"0","receiptOrderStatus":1,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","remark":"a","details":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2054510259503165442","receiptOrderId":"2054510259423473665","skuId":"2053852496494178305","quantity":"1","amount":"0","equipmentCode":"a","specModel":"a","productMark":"a","qualityGrade":null,"unitPrice":null,"lineAmount":"0","productionDate":null,"expirationDate":null,"remark":null,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","rackId":"2053041345556508674","locationId":"2053041345619423233","generateItemInstance":1,"generatedInstanceQuantity":0,"receiptItemInstances":[{"id":"2054445955298963461","instanceCode":"a-a1000000006","boxCode":"","productMark":"a","qualityGrade":null,"remark":null}]},{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2054510259503165443","receiptOrderId":"2054510259423473665","skuId":"2053852496494178305","quantity":"1","amount":"0","equipmentCode":"a","specModel":"a","productMark":"a","qualityGrade":null,"unitPrice":null,"lineAmount":"0","productionDate":null,"expirationDate":null,"remark":null,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","rackId":"2053041345556508674","locationId":"2053041345619423233","generateItemInstance":1,"generatedInstanceQuantity":0,"receiptItemInstances":[{"id":"2054445955298963462","instanceCode":"a-a1000000007","boxCode":"","productMark":"a","qualityGrade":null,"remark":null}]},{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id"','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-14 22:33:03');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2054957254437072898,'出库单',1,'com.ruoyi.wms.controller.ShipmentOrderController.add()','POST',1,'admin','研发部门','/wms/shipmentOrder','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2054957253988282370","shipmentOrderNo":"CK05150115","shipmentOrderType":2,"orderNo":null,"merchantId":null,"basisNo":null,"dispatchMode":null,"noticeOrg":null,"receiveUnit":null,"purchaseDate":null,"shipmentDate":null,"purchaserName":null,"acceptorName":null,"keeperName":null,"receivableAmount":"0","totalQuantity":"1","shipmentOrderStatus":0,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","remark":null,"details":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"shipmentOrderId":null,"skuId":"2053852496494178305","quantity":"1","amount":"0","equipmentCode":"a-a1000000001","specModel":"a","productMark":"a","qualityGrade":null,"unitPrice":null,"lineAmount":"0","warehouseId":"1828364459110469633","areaId":"1828364518342430721","productionDate":null,"expirationDate":null,"inventoryDetailId":"2054493598884655106","itemInstanceId":"2054445955298963462","boxId":null,"remark":null,"key":"1828364459110469633_1828364518342430721_2053852496494178305"}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-15 00:09:23');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2055551078075019265,'库存盘点单据',2,'com.ruoyi.wms.controller.CheckOrderController.check()','POST',1,'admin','研发部门','/wms/checkOrder/check','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"checkOrderNo":"PK05162985","checkOrderStatus":1,"checkOrderTotal":"-1","warehouseId":"1828364459110469633","areaId":"1828364518342430721","rackId":null,"checkScopeType":"warehouse","checkDate":null,"checkerName":null,"reviewerName":null,"remark":null,"details":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"checkOrderId":null,"skuId":"2053852496494178305","quantity":"1","checkQuantity":"0","profitAndLoss":"-1","differenceQuantity":"-1","warehouseId":"1828364459110469633","areaId":"1828364518342430721","rackId":"2053041345556508674","locationId":"2053041345619423237","productionDate":null,"expirationDate":null,"receiptTime":"2026-05-13 17:26:59","inventoryDetailId":"2054493598884655106","equipmentCode":null,"specModel":null,"productMark":null,"qualityGrade":null,"itemInstanceId":null,"boxId":null,"remark":null,"haveProfitAndLoss":null},{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"checkOrderId":null,"skuId":"2053852496494178305","quantity":"1","checkQuantity":"1","profitAndLoss":"0","differenceQuantity":"0","warehouseId":"1828364459110469633","areaId":"1828364518342430721","rackId":"2053041345556508674","locationId":"2053041345556508675","productionDate":null,"expirationDate":null,"receiptTime":"2026-05-14 22:24:45","inventoryDetailId":"2054930921522847745","equipmentCode":null,"specModel":null,"productMark":null,"qualityGrade":null,"itemInstanceId":null,"boxId":null,"remark":null,"haveProfitAndLoss":null},{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"checkOrderId":null,"skuId":"2053852496494178305","quantity":"1","checkQuantity":"1","profitAndLoss":"0","differenceQuantity":"0","warehouseId":"1828364459110469633","areaId":"1828364518342430721","rackId":"2053041345556508674","locationId":"2053041345556508675","productionDate":null,"expirationDate":null,"receiptTime":"2026-05-14 22:24:45","i','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-16 15:29:02');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2054957324079296513,'出库单',1,'com.ruoyi.wms.controller.ShipmentOrderController.add()','POST',1,'admin','研发部门','/wms/shipmentOrder','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2054957323764723714","shipmentOrderNo":"CK05157373","shipmentOrderType":2,"orderNo":null,"merchantId":null,"basisNo":null,"dispatchMode":null,"noticeOrg":null,"receiveUnit":null,"purchaseDate":null,"shipmentDate":null,"purchaserName":null,"acceptorName":null,"keeperName":null,"receivableAmount":"0","totalQuantity":"2","shipmentOrderStatus":0,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","remark":null,"details":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"shipmentOrderId":null,"skuId":"2053852496494178305","quantity":"1","amount":"0","equipmentCode":"a-a1000000001","specModel":"a","productMark":"a","qualityGrade":null,"unitPrice":null,"lineAmount":"0","warehouseId":"1828364459110469633","areaId":"1828364518342430721","productionDate":null,"expirationDate":null,"inventoryDetailId":"2054493598884655106","itemInstanceId":"2054445955298963462","boxId":null,"remark":null,"key":"1828364459110469633_1828364518342430721_2053852496494178305"},{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"shipmentOrderId":null,"skuId":"2053852496494178305","quantity":"1","amount":"0","equipmentCode":"a-a1000000003","specModel":"a","productMark":"a","qualityGrade":null,"unitPrice":null,"lineAmount":"0","warehouseId":"1828364459110469633","areaId":"1828364518342430721","productionDate":null,"expirationDate":null,"inventoryDetailId":"2054930921522847745","itemInstanceId":"2054445955298963461","boxId":null,"remark":null,"key":"1828364459110469633_1828364518342430721_2053852496494178305"}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-15 00:09:40');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2055184097836609538,'出库单',2,'com.ruoyi.wms.controller.ShipmentOrderController.edit()','PUT',1,'admin','研发部门','/wms/shipmentOrder','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2054735451613675522","shipmentOrderNo":"CK05143470","shipmentOrderType":2,"orderNo":null,"merchantId":null,"basisNo":null,"dispatchMode":null,"noticeOrg":null,"receiveUnit":null,"purchaseDate":null,"shipmentDate":null,"purchaserName":null,"acceptorName":null,"keeperName":null,"receivableAmount":"0","totalQuantity":"1","shipmentOrderStatus":-1,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","remark":null,"details":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2054735451726921729","shipmentOrderId":"2054735451613675522","skuId":"2053852496494178305","quantity":"1","amount":"0","equipmentCode":"a","specModel":"a","productMark":null,"qualityGrade":null,"unitPrice":null,"lineAmount":"0","warehouseId":"1828364459110469633","areaId":"1828364518342430721","productionDate":null,"expirationDate":null,"inventoryDetailId":"2054493598884655106","itemInstanceId":"2054445955265409026","boxId":"2054493598427475969","remark":null,"key":"1828364459110469633_1828364518342430721_2053852496494178305"}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-15 15:10:47');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2055184121710587905,'出库单',3,'com.ruoyi.wms.controller.ShipmentOrderController.remove()','DELETE',1,'admin','研发部门','/wms/shipmentOrder/2054735451613675522','0:0:0:0:0:0:0:1','内网IP','{}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-15 15:10:53');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2055184131537842177,'出库单',3,'com.ruoyi.wms.controller.ShipmentOrderController.remove()','DELETE',1,'admin','研发部门','/wms/shipmentOrder/2054957253988282370','0:0:0:0:0:0:0:1','内网IP','{}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-15 15:10:55');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2055184140706590722,'出库单',3,'com.ruoyi.wms.controller.ShipmentOrderController.remove()','DELETE',1,'admin','研发部门','/wms/shipmentOrder/2054957323764723714','0:0:0:0:0:0:0:1','内网IP','{}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-15 15:10:57');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2055528939015974913,'出库单',1,'com.ruoyi.wms.controller.ShipmentOrderController.add()','POST',1,'admin','研发部门','/wms/shipmentOrder','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2055528938051284994","shipmentOrderNo":"CK05165124","shipmentOrderType":2,"orderNo":null,"merchantId":null,"basisNo":null,"dispatchMode":null,"noticeOrg":null,"receiveUnit":null,"purchaseDate":null,"shipmentDate":null,"purchaserName":null,"acceptorName":null,"keeperName":null,"receivableAmount":"0","totalQuantity":"2","shipmentOrderStatus":0,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","remark":null,"details":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2055528938118393858","shipmentOrderId":null,"skuId":"2053852496494178305","quantity":"1","amount":"0","equipmentCode":"a-a1000000001","specModel":"a","productMark":"a","qualityGrade":null,"unitPrice":null,"lineAmount":"0","warehouseId":"1828364459110469633","areaId":"1828364518342430721","productionDate":null,"expirationDate":null,"inventoryDetailId":"2054493598884655106","itemInstanceId":"2054445955298963459","boxId":null,"remark":null,"key":"1828364459110469633_1828364518342430721_2053852496494178305"},{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2055528938437160962","shipmentOrderId":null,"skuId":"2053852496494178305","quantity":"1","amount":"0","equipmentCode":"a-a1000000003","specModel":"a","productMark":"a","qualityGrade":null,"unitPrice":null,"lineAmount":"0","warehouseId":"1828364459110469633","areaId":"1828364518342430721","productionDate":null,"expirationDate":null,"inventoryDetailId":"2054930921522847745","itemInstanceId":"2054445955298963458","boxId":"2054930921195692034","remark":null,"key":"1828364459110469633_1828364518342430721_2053852496494178305"}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-16 14:01:04');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2056770316508934146,'库区',2,'com.ruoyi.wms.controller.AreaController.edit()','PUT',1,'admin','研发部门','/wms/area','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2056770222548135937","areaCode":null,"areaName":"北A区","warehouseId":"1828364459110469633","status":null,"orderNum":null,"remark":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-20 00:13:51');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2055552085139992578,'出库单',2,'com.ruoyi.wms.controller.ShipmentOrderController.shipment()','PUT',1,'admin','研发部门','/wms/shipmentOrder/shipment','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2055528938051284994","shipmentOrderNo":"CK05165124","shipmentOrderType":2,"orderNo":null,"merchantId":null,"basisNo":null,"dispatchMode":null,"noticeOrg":null,"receiveUnit":null,"purchaseDate":null,"shipmentDate":null,"purchaserName":null,"acceptorName":null,"keeperName":null,"receivableAmount":"0","totalQuantity":"2","shipmentOrderStatus":1,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","remark":null,"details":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2055528938118393858","shipmentOrderId":"2055528938051284994","skuId":"2053852496494178305","quantity":"0","amount":"0.00","equipmentCode":null,"specModel":null,"productMark":null,"qualityGrade":null,"unitPrice":null,"lineAmount":null,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","productionDate":null,"expirationDate":null,"inventoryDetailId":"2054493598884655106","itemInstanceId":"2054445955298963459","boxId":null,"remark":null,"key":"1828364459110469633_1828364518342430721_2053852496494178305"},{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2055528938437160962","shipmentOrderId":"2055528938051284994","skuId":"2053852496494178305","quantity":"1","amount":"0.00","equipmentCode":null,"specModel":null,"productMark":null,"qualityGrade":null,"unitPrice":null,"lineAmount":null,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","productionDate":null,"expirationDate":null,"inventoryDetailId":"2054930921522847745","itemInstanceId":"2054445955298963458","boxId":"2054930921195692034","remark":null,"key":"1828364459110469633_1828364518342430721_2053852496494178305"}]}','',0,'按单品实例出库时，数量必须为1','2026-05-16 15:33:02');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2055552112654626817,'出库单详情',3,'com.ruoyi.wms.controller.ShipmentOrderDetailController.remove()','DELETE',1,'admin','研发部门','/wms/shipmentOrderDetail/2055528938118393858','0:0:0:0:0:0:0:1','内网IP','{}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-16 15:33:09');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2055552126143508481,'出库单详情',3,'com.ruoyi.wms.controller.ShipmentOrderDetailController.remove()','DELETE',1,'admin','研发部门','/wms/shipmentOrderDetail/2055528938437160962','0:0:0:0:0:0:0:1','内网IP','{}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-16 15:33:12');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2055552288500822017,'出库单',2,'com.ruoyi.wms.controller.ShipmentOrderController.shipment()','PUT',1,'admin','研发部门','/wms/shipmentOrder/shipment','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2055528938051284994","shipmentOrderNo":"CK05165124","shipmentOrderType":2,"orderNo":null,"merchantId":null,"basisNo":null,"dispatchMode":null,"noticeOrg":null,"receiveUnit":null,"purchaseDate":null,"shipmentDate":null,"purchaserName":null,"acceptorName":null,"keeperName":null,"receivableAmount":"0","totalQuantity":"1","shipmentOrderStatus":1,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","remark":null,"details":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2055552287376748546","shipmentOrderId":"2055528938051284994","skuId":"2053852496494178305","quantity":"1","amount":"0","equipmentCode":null,"specModel":null,"productMark":null,"qualityGrade":null,"unitPrice":null,"lineAmount":null,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","productionDate":null,"expirationDate":null,"inventoryDetailId":"2054930921522847745","itemInstanceId":"2054445955298963460","boxId":null,"remark":null,"key":"1828364459110469633_1828364518342430721_2053852496494178305"}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-16 15:33:51');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2055979507337502722,'物料',0,'com.ruoyi.wms.controller.ItemController.batchPrintQrCode()','POST',1,'admin','研发部门','/wms/item/batchPrintQrCode','0:0:0:0:0:0:0:1','内网IP','{"row":{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2052636524512894978","ids":null,"itemCode":"DYJ","itemName":"惠普","itemCategory":"1828365014901886978","unit":"台","level":null,"equipmentName":"装备名称","equipmentType":"通装","status":"1","productMark":null,"modelText":null,"remark":null,"sku":null},"qrCodeCount":1}','{"code":200,"msg":"操作成功","data":{"itemKey":"WH1ITEM","qrCodeCount":1,"row":{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2052636524512894978","ids":null,"itemCode":"DYJ","itemName":"惠普","itemCategory":"1828365014901886978","unit":"台","level":null,"equipmentName":"装备名称","equipmentType":"通装","status":"1","productMark":null,"modelText":null,"remark":null,"sku":null},"details":[{"serialValue":1000000001,"instanceCode":"WH1ITEM1000000001","qrCodeValue":"WH1ITEM1000000001","qrContent":"WH1ITEM1000000001"}]}}',1,'','2026-05-17 19:51:27');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2055998963526770690,'箱体',1,'com.ruoyi.wms.controller.BoxController.add()','POST',1,'admin','研发部门','/wms/box','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"boxCode":"BOX2055998963338027008","boxName":"aaa","boxStatus":"idle","warehouseId":null,"areaId":null,"rackId":null,"locationId":null,"itemCount":null,"remark":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-17 21:08:46');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2056752728802410497,'物料',1,'com.ruoyi.wms.controller.ItemController.add()','POST',1,'admin','研发部门','/wms/item','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"ids":null,"itemCode":"box","itemName":"器材箱","itemCategory":"2054732521107996674","unit":"个","equipmentName":null,"equipmentType":null,"status":"1","remark":null,"sku":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"skuName":"大","productIdentifier":"","qualityGrade":"","itemId":"2056752728542363650","status":"1","itemName":null,"itemCode":null,"equipmentName":null,"itemCategory":null}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-19 23:03:58');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2056203328992686082,'物料',0,'com.ruoyi.wms.controller.ItemController.batchPrintQrCode()','POST',1,'admin','研发部门','/wms/item/batchPrintQrCode','0:0:0:0:0:0:0:1','内网IP','{"row":{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2052636524512894978","ids":null,"itemCode":"DYJ","itemName":"惠普","itemCategory":"1828365014901886978","unit":"台","level":null,"equipmentName":"装备名称","equipmentType":"通装","status":"1","modelText":null,"remark":null,"sku":null},"qrCodeCount":5}','{"code":200,"msg":"操作成功","data":{"itemKey":"WH1ITEM","qrCodeCount":5,"row":{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2052636524512894978","ids":null,"itemCode":"DYJ","itemName":"惠普","itemCategory":"1828365014901886978","unit":"台","level":null,"equipmentName":"装备名称","equipmentType":"通装","status":"1","modelText":null,"remark":null,"sku":null},"details":[{"serialValue":1000000002,"instanceCode":"WH1ITEM1000000002","qrCodeValue":"WH1ITEM1000000002","qrContent":"WH1ITEM1000000002"},{"serialValue":1000000003,"instanceCode":"WH1ITEM1000000003","qrCodeValue":"WH1ITEM1000000003","qrContent":"WH1ITEM1000000003"},{"serialValue":1000000004,"instanceCode":"WH1ITEM1000000004","qrCodeValue":"WH1ITEM1000000004","qrContent":"WH1ITEM1000000004"},{"serialValue":1000000005,"instanceCode":"WH1ITEM1000000005","qrCodeValue":"WH1ITEM1000000005","qrContent":"WH1ITEM1000000005"},{"serialValue":1000000006,"instanceCode":"WH1ITEM1000000006","qrCodeValue":"WH1ITEM1000000006","qrContent":"WH1ITEM1000000006"}]}}',1,'','2026-05-18 10:40:51');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2056286419052486658,'单品实例',5,'com.ruoyi.wms.controller.ItemInstanceController.export()','POST',1,'admin','研发部门','/wms/itemInstance/export','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"instanceCode":null,"itemId":null,"skuId":null,"instanceStatus":null,"inBox":null,"borrowed":null,"warehouseId":null,"areaId":null,"rackId":null,"locationId":null,"sourceType":null,"sourceOrderType":null,"boxId":null,"boxCode":null,"sourceOrderId":null,"sourceOrderNo":null,"receiptOrderDetailId":null,"shipmentOrderDetailId":null,"belongUnit":null,"currentOwnerUnit":null,"lastOperationType":null,"lastOperationTime":null,"productionDate":null,"expirationDate":null,"remark":null,"unreceivedOnly":null,"unshippedOnly":null,"targetStatus":null}','',1,'','2026-05-18 16:11:01');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2056399868721987585,'出库单',2,'com.ruoyi.wms.controller.ShipmentOrderController.shipment()','PUT',1,'admin','研发部门','/wms/shipmentOrder/shipment','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2056399867396587521","shipmentOrderNo":"CK05187208","shipmentOrderType":"借用出库","basisNo":null,"dispatchMode":null,"noticeOrg":null,"receiveUnit":null,"purchaseDate":null,"shipmentDate":null,"receivableAmount":"1","totalQuantity":"1","shipmentOrderStatus":1,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","remark":null,"details":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2056399867451113474","shipmentOrderId":null,"skuId":"2053852496494178305","quantity":"1","amount":"1","warehouseId":"1828364459110469633","areaId":"1828364518342430721","inventoryDetailId":"2054933008474300417","itemInstanceId":"2054445955265409026","boxId":"2054493598427475969","remark":null,"key":"1828364459110469633_1828364518342430721_2053852496494178305"}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-18 23:41:49');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2056577523224182785,'入库单',2,'com.ruoyi.wms.controller.ReceiptOrderController.doWarehousing()','POST',1,'admin','研发部门','/wms/receiptOrder/warehousing','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2056577521617764354","receiptOrderNo":"RK05196065","receiptOrderType":"采购入库","basisNo":null,"dispatchMode":null,"noticeOrg":null,"receiveUnit":null,"purchaseDate":null,"receiptDate":null,"totalQuantity":"1","payableAmount":"1","receiptOrderStatus":1,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","remark":null,"details":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2056577521710039041","receiptOrderId":null,"skuId":"2053852496494178305","quantity":"1","itemCode":"a","itemName":"a","skuName":"a","unit":"a","productIdentifier":null,"qualityGrade":null,"unitPrice":"1","lineAmount":"1.00","amount":"1.00","remark":"1","warehouseId":"1828364459110469633","areaId":"1828364518342430721","rackId":"2053041345556508674","locationId":"2053041345619423237","receiptItemInstances":[{"id":"2054445955361878019","instanceCode":"a-a1000000010","boxCode":"2121211","remark":"1"}]}]}','',0,'com.ruoyi.wms.mapper.InventoryDetailMapper.insert (batch index #1) failed. Cause: java.sql.BatchUpdateException: Unknown column ''equipment_code'' in ''field list''
; bad SQL grammar []','2026-05-19 11:27:46');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2056582073318449153,'入库单',2,'com.ruoyi.wms.controller.ReceiptOrderController.doWarehousing()','POST',1,'admin','研发部门','/wms/receiptOrder/warehousing','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2056582069300305922","receiptOrderNo":"RK05196065","receiptOrderType":"采购入库","basisNo":null,"dispatchMode":null,"noticeOrg":null,"receiveUnit":null,"purchaseDate":null,"receiptDate":null,"totalQuantity":"1","payableAmount":"1","receiptOrderStatus":1,"warehouseId":"1828364459110469633","areaId":"1828364518342430721","remark":null,"details":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2056582069354831873","receiptOrderId":null,"skuId":"2053852496494178305","quantity":"1","itemCode":"a","itemName":"a","skuName":"a","unit":"a","productIdentifier":null,"qualityGrade":null,"unitPrice":"1","lineAmount":"1.00","amount":"1.00","remark":"1","warehouseId":"1828364459110469633","areaId":"1828364518342430721","rackId":"2053041345556508674","locationId":"2053041345619423237","receiptItemInstances":[{"id":"2054445955361878019","instanceCode":"a-a1000000010","boxCode":"2121211","remark":"1"}]}]}','',0,'com.ruoyi.wms.mapper.InventoryDetailMapper.insert (batch index #1) failed. Cause: java.sql.BatchUpdateException: Unknown column ''unit_price'' in ''field list''
; bad SQL grammar []','2026-05-19 11:45:50');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2056768878512779265,'物料',1,'com.ruoyi.wms.controller.ItemController.add()','POST',1,'admin','研发部门','/wms/item','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"ids":null,"itemCode":"box","itemName":"器材箱","itemCategory":"2054732521107996674","unit":"个","equipmentName":null,"equipmentType":null,"status":"1","remark":null,"sku":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"skuName":"大","productIdentifier":"","qualityGrade":"","itemId":"2056768878227566594","status":"1","itemName":null,"itemCode":null,"equipmentName":null,"itemCategory":null},{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"skuName":"小","productIdentifier":"","qualityGrade":"","itemId":"2056768878227566594","status":"1","itemName":null,"itemCode":null,"equipmentName":null,"itemCategory":null}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-20 00:08:08');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2056769240380551169,'物料',0,'com.ruoyi.wms.controller.ItemController.batchPrintQrCode()','POST',1,'admin','研发部门','/wms/item/batchPrintQrCode','0:0:0:0:0:0:0:1','内网IP','{"row":{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2056768878227566594","ids":null,"itemCode":"box","itemName":"器材箱","itemCategory":null,"unit":null,"equipmentName":null,"equipmentType":null,"status":null,"remark":null,"sku":null},"skuId":"2056768878252732418","qrCodeCount":10}','{"code":200,"msg":"操作成功","data":{"itemKey":"WH1ITEM","qrCodeCount":10,"row":{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2056768878227566594","ids":null,"itemCode":"box","itemName":"器材箱","itemCategory":null,"unit":null,"equipmentName":null,"equipmentType":null,"status":null,"remark":null,"sku":null},"details":[{"serialValue":1000000001,"instanceCode":"WH1ITEM1000000001","qrCodeValue":"WH1ITEM1000000001","qrContent":"WH1ITEM1000000001"},{"serialValue":1000000002,"instanceCode":"WH1ITEM1000000002","qrCodeValue":"WH1ITEM1000000002","qrContent":"WH1ITEM1000000002"},{"serialValue":1000000003,"instanceCode":"WH1ITEM1000000003","qrCodeValue":"WH1ITEM1000000003","qrContent":"WH1ITEM1000000003"},{"serialValue":1000000004,"instanceCode":"WH1ITEM1000000004","qrCodeValue":"WH1ITEM1000000004","qrContent":"WH1ITEM1000000004"},{"serialValue":1000000005,"instanceCode":"WH1ITEM1000000005","qrCodeValue":"WH1ITEM1000000005","qrContent":"WH1ITEM1000000005"},{"serialValue":1000000006,"instanceCode":"WH1ITEM1000000006","qrCodeValue":"WH1ITEM1000000006","qrContent":"WH1ITEM1000000006"},{"serialValue":1000000007,"instanceCode":"WH1ITEM1000000007","qrCodeValue":"WH1ITEM1000000007","qrContent":"WH1ITEM1000000007"},{"serialValue":1000000008,"instanceCode":"WH1ITEM1000000008","qrCodeValue":"WH1ITEM1000000008","qrContent":"WH1ITEM1000000008"},{"serialValue":1000000009,"instanceCode":"WH1ITEM1000000009","qrCodeValue":"WH1ITEM1000000009","qrContent":"WH1ITEM1000000009"},{"serialValue":1000000010,"instanceCode":"WH1ITEM1000000010","qrCodeValue":"WH1ITEM1000000010","qrContent":"WH1ITEM1000000010"}]}}',1,'','2026-05-20 00:09:35');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2056769319761948674,'仓库',2,'com.ruoyi.wms.controller.WarehouseController.edit()','PUT',1,'admin','研发部门','/wms/warehouse','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"1828364459110469633","warehouseCode":"WH1","warehouseName":"北京仓","status":"enabled","address":null,"managerName":null,"managerPhone":null,"remark":null,"orderNum":0}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-20 00:09:53');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2056769387365740546,'仓库',1,'com.ruoyi.wms.controller.WarehouseController.add()','POST',1,'admin','研发部门','/wms/warehouse','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"warehouseCode":null,"warehouseName":"海淀仓","status":null,"address":null,"managerName":null,"managerPhone":null,"remark":null,"orderNum":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-20 00:10:10');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2056769438028738561,'仓库',1,'com.ruoyi.wms.controller.WarehouseController.add()','POST',1,'admin','研发部门','/wms/warehouse','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"warehouseCode":null,"warehouseName":"朝阳仓","status":null,"address":null,"managerName":null,"managerPhone":null,"remark":null,"orderNum":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-20 00:10:22');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2056769466898132993,'仓库',1,'com.ruoyi.wms.controller.WarehouseController.add()','POST',1,'admin','研发部门','/wms/warehouse','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"warehouseCode":null,"warehouseName":"东城仓","status":null,"address":null,"managerName":null,"managerPhone":null,"remark":null,"orderNum":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-20 00:10:29');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2056769490910523393,'仓库',1,'com.ruoyi.wms.controller.WarehouseController.add()','POST',1,'admin','研发部门','/wms/warehouse','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"warehouseCode":null,"warehouseName":"西城仓","status":null,"address":null,"managerName":null,"managerPhone":null,"remark":null,"orderNum":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-20 00:10:34');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2056769502834929666,'仓库',3,'com.ruoyi.wms.controller.WarehouseController.remove()','DELETE',1,'admin','研发部门','/wms/warehouse/2053033327892754434','0:0:0:0:0:0:0:1','内网IP','{}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-20 00:10:37');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2056769510850244609,'仓库',3,'com.ruoyi.wms.controller.WarehouseController.remove()','DELETE',1,'admin','研发部门','/wms/warehouse/1828364740028174337','0:0:0:0:0:0:0:1','内网IP','{}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-20 00:10:39');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2056769517800206337,'仓库',3,'com.ruoyi.wms.controller.WarehouseController.remove()','DELETE',1,'admin','研发部门','/wms/warehouse/1828364609002311682','0:0:0:0:0:0:0:1','内网IP','{}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-20 00:10:41');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2056769648293392385,'仓库',1,'com.ruoyi.wms.controller.WarehouseController.add()','POST',1,'admin','研发部门','/wms/warehouse','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"warehouseCode":null,"warehouseName":"大兴仓","status":null,"address":null,"managerName":null,"managerPhone":null,"remark":null,"orderNum":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-20 00:11:12');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2056770222615244801,'库区',1,'com.ruoyi.wms.controller.AreaController.add()','POST',1,'admin','研发部门','/wms/area','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"areaCode":null,"areaName":"A区","warehouseId":"1828364459110469633","status":null,"orderNum":null,"remark":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-20 00:13:29');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2056770261127344129,'库区',1,'com.ruoyi.wms.controller.AreaController.add()','POST',1,'admin','研发部门','/wms/area','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"areaCode":null,"areaName":"B区","warehouseId":"1828364459110469633","status":null,"orderNum":null,"remark":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-20 00:13:38');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2056770367792689153,'库区',1,'com.ruoyi.wms.controller.AreaController.add()','POST',1,'admin','研发部门','/wms/area','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"areaCode":null,"areaName":"海A区","warehouseId":"2056769387227328513","status":null,"orderNum":null,"remark":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-20 00:14:03');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2056770392295813122,'库区',1,'com.ruoyi.wms.controller.AreaController.add()','POST',1,'admin','研发部门','/wms/area','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"areaCode":null,"areaName":"海B区","warehouseId":"2056769387227328513","status":null,"orderNum":null,"remark":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-20 00:14:09');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2056770428949835778,'库区',1,'com.ruoyi.wms.controller.AreaController.add()','POST',1,'admin','研发部门','/wms/area','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"areaCode":null,"areaName":"朝A区","warehouseId":"2056769437957435394","status":null,"orderNum":null,"remark":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-20 00:14:18');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2056770456254754817,'库区',1,'com.ruoyi.wms.controller.AreaController.add()','POST',1,'admin','研发部门','/wms/area','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"areaCode":null,"areaName":"朝B区","warehouseId":"2056769437957435394","status":null,"orderNum":null,"remark":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-20 00:14:24');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2056770508129906690,'库区',1,'com.ruoyi.wms.controller.AreaController.add()','POST',1,'admin','研发部门','/wms/area','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"areaCode":null,"areaName":"东A区","warehouseId":"2056769466831024130","status":null,"orderNum":null,"remark":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-20 00:14:37');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2056770538815434753,'库区',1,'com.ruoyi.wms.controller.AreaController.add()','POST',1,'admin','研发部门','/wms/area','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"areaCode":null,"areaName":"东B区","warehouseId":"2056769466831024130","status":null,"orderNum":null,"remark":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-20 00:14:44');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2056770584361381889,'库区',1,'com.ruoyi.wms.controller.AreaController.add()','POST',1,'admin','研发部门','/wms/area','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"areaCode":null,"areaName":"西A区","warehouseId":"2056769490864386049","status":null,"orderNum":null,"remark":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-20 00:14:55');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2056770610353483778,'库区',1,'com.ruoyi.wms.controller.AreaController.add()','POST',1,'admin','研发部门','/wms/area','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"areaCode":null,"areaName":"西B区","warehouseId":"2056769490864386049","status":null,"orderNum":null,"remark":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-20 00:15:01');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2056770643463319553,'库区',1,'com.ruoyi.wms.controller.AreaController.add()','POST',1,'admin','研发部门','/wms/area','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"areaCode":null,"areaName":"大A区","warehouseId":"2056769648226283522","status":null,"orderNum":null,"remark":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-20 00:15:09');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2056770672877973506,'库区',1,'com.ruoyi.wms.controller.AreaController.add()','POST',1,'admin','研发部门','/wms/area','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"areaCode":null,"areaName":"大B区","warehouseId":"2056769648226283522","status":null,"orderNum":null,"remark":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-20 00:15:16');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2056770719334084609,'库区',2,'com.ruoyi.wms.controller.AreaController.edit()','PUT',1,'admin','研发部门','/wms/area','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2056770261064429569","areaCode":null,"areaName":"京B区","warehouseId":"1828364459110469633","status":null,"orderNum":null,"remark":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-20 00:15:27');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2056770739433189378,'库区',2,'com.ruoyi.wms.controller.AreaController.edit()','PUT',1,'admin','研发部门','/wms/area','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2056770222548135937","areaCode":null,"areaName":"京A区","warehouseId":"1828364459110469633","status":null,"orderNum":null,"remark":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-20 00:15:32');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2056771039967653889,'货架',1,'com.ruoyi.wms.controller.RackController.add()','POST',1,'admin','研发部门','/wms/rack','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"rackCode":null,"rackName":"京A货1","warehouseId":"1828364459110469633","areaId":"2056770222548135937","rackStatus":"enabled","rowCount":2,"columnCount":3,"length":"600","width":"600","height":"600","orderNum":0,"remark":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-20 00:16:44');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2056771137321644034,'货架',1,'com.ruoyi.wms.controller.RackController.add()','POST',1,'admin','研发部门','/wms/rack','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"rackCode":null,"rackName":"京A货2","warehouseId":"1828364459110469633","areaId":"2056770222548135937","rackStatus":"enabled","rowCount":1,"columnCount":1,"length":"200","width":"200","height":"200","orderNum":0,"remark":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-20 00:17:07');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2056771222071750658,'货架',1,'com.ruoyi.wms.controller.RackController.add()','POST',1,'admin','研发部门','/wms/rack','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"rackCode":null,"rackName":"京B货1","warehouseId":"1828364459110469633","areaId":"2056770261064429569","rackStatus":"enabled","rowCount":3,"columnCount":3,"length":"900","width":"900","height":"900","orderNum":0,"remark":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-20 00:17:27');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2056771350128046082,'货架',1,'com.ruoyi.wms.controller.RackController.add()','POST',1,'admin','研发部门','/wms/rack','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"rackCode":null,"rackName":"海A货1","warehouseId":"2056769387227328513","areaId":"2056770367725580289","rackStatus":"enabled","rowCount":1,"columnCount":1,"length":"50","width":"50","height":"50","orderNum":0,"remark":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-20 00:17:57');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2056772480916598785,'货架',1,'com.ruoyi.wms.controller.RackController.add()','POST',1,'admin','研发部门','/wms/rack','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"rackCode":null,"rackName":"海B货1","warehouseId":"2056769387227328513","areaId":"2056770392228704258","rackStatus":"enabled","rowCount":2,"columnCount":3,"length":"600","width":"600","height":"600","orderNum":0,"remark":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-20 00:22:27');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2056772973021708289,'货架',1,'com.ruoyi.wms.controller.RackController.add()','POST',1,'admin','研发部门','/wms/rack','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"rackCode":null,"rackName":"海B货2","warehouseId":"2056769387227328513","areaId":"2056770392228704258","rackStatus":"enabled","rowCount":2,"columnCount":3,"length":"600","width":"600","height":"600","orderNum":0,"remark":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-20 00:24:24');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2056774315412897794,'物料',1,'com.ruoyi.wms.controller.ItemController.add()','POST',1,'admin','研发部门','/wms/item','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"ids":null,"itemCode":"HUI-DYJ","itemName":"惠普打印机","itemCategory":"1828365014901886978","unit":"台","equipmentName":null,"equipmentType":null,"status":"1","remark":null,"sku":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"skuName":"2023","productIdentifier":"xx","qualityGrade":"高","itemId":"2056774315211571201","status":"1","itemName":null,"itemCode":null,"equipmentName":null,"itemCategory":null}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-20 00:29:44');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2056774828229476353,'物料',0,'com.ruoyi.wms.controller.ItemController.batchPrintQrCode()','POST',1,'admin','研发部门','/wms/item/batchPrintQrCode','0:0:0:0:0:0:0:1','内网IP','{"row":{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2056774315211571201","ids":null,"itemCode":"HUI-DYJ","itemName":"惠普打印机","itemCategory":null,"unit":null,"equipmentName":null,"equipmentType":null,"status":null,"remark":null,"sku":null},"skuId":"2056774315278680066","qrCodeCount":10}','{"code":200,"msg":"操作成功","data":{"itemKey":"WH1ITEM","qrCodeCount":10,"row":{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2056774315211571201","ids":null,"itemCode":"HUI-DYJ","itemName":"惠普打印机","itemCategory":null,"unit":null,"equipmentName":null,"equipmentType":null,"status":null,"remark":null,"sku":null},"details":[{"serialValue":1000000001,"instanceCode":"WH1ITEM1000000001","qrCodeValue":"WH1ITEM1000000001","qrContent":"WH1ITEM1000000001"},{"serialValue":1000000002,"instanceCode":"WH1ITEM1000000002","qrCodeValue":"WH1ITEM1000000002","qrContent":"WH1ITEM1000000002"},{"serialValue":1000000003,"instanceCode":"WH1ITEM1000000003","qrCodeValue":"WH1ITEM1000000003","qrContent":"WH1ITEM1000000003"},{"serialValue":1000000004,"instanceCode":"WH1ITEM1000000004","qrCodeValue":"WH1ITEM1000000004","qrContent":"WH1ITEM1000000004"},{"serialValue":1000000005,"instanceCode":"WH1ITEM1000000005","qrCodeValue":"WH1ITEM1000000005","qrContent":"WH1ITEM1000000005"},{"serialValue":1000000006,"instanceCode":"WH1ITEM1000000006","qrCodeValue":"WH1ITEM1000000006","qrContent":"WH1ITEM1000000006"},{"serialValue":1000000007,"instanceCode":"WH1ITEM1000000007","qrCodeValue":"WH1ITEM1000000007","qrContent":"WH1ITEM1000000007"},{"serialValue":1000000008,"instanceCode":"WH1ITEM1000000008","qrCodeValue":"WH1ITEM1000000008","qrContent":"WH1ITEM1000000008"},{"serialValue":1000000009,"instanceCode":"WH1ITEM1000000009","qrCodeValue":"WH1ITEM1000000009","qrContent":"WH1ITEM1000000009"},{"serialValue":1000000010,"instanceCode":"WH1ITEM1000000010","qrCodeValue":"WH1ITEM1000000010","qrContent":"WH1ITEM1000000010"}]}}',1,'','2026-05-20 00:31:47');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2056999507997298690,'入库单',1,'com.ruoyi.wms.controller.ReceiptOrderController.add()','POST',1,'admin','研发部门','/wms/receiptOrder','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2056999507242323970","receiptOrderNo":"RK05201617","receiptOrderType":"采购入库","basisNo":"纸质","dispatchMode":"air","noticeOrg":"北大","receiveUnit":"清华","purchaseDate":"2026-05-17","receiptDate":"2026-05-20","totalQuantity":"2","payableAmount":"100","receiptOrderStatus":0,"warehouseId":"1828364459110469633","areaId":"2056770222548135937","remark":"aaaaa","details":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2056999507401707521","receiptOrderId":null,"skuId":"2056774315278680066","quantity":"1","itemCode":"HUI-DYJ","itemName":"惠普打印机","skuName":"2023","unit":"台","productIdentifier":"xx","qualityGrade":"高","unitPrice":"50","lineAmount":"50.00","remark":null,"warehouseId":"1828364459110469633","areaId":"2056770222548135937","rackId":"2056771137254535169","locationId":"2056771137254535170","receiptItemInstances":[{"id":"2056774828107841539","instanceCode":"WH1ITEM1000000002","boxCode":"BOX1122334455","remark":null}]},{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2056999507401707522","receiptOrderId":null,"skuId":"2056774315278680066","quantity":"1","itemCode":"HUI-DYJ","itemName":"惠普打印机","skuName":"2023","unit":"台","productIdentifier":"xx","qualityGrade":"高","unitPrice":"50","lineAmount":"50.00","remark":null,"warehouseId":"1828364459110469633","areaId":"2056770222548135937","rackId":"2056771137254535169","locationId":"2056771137254535170","receiptItemInstances":[{"id":"2056774828107841538","instanceCode":"WH1ITEM1000000001","boxCode":"BOX1122334466","remark":null}]}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-20 15:24:35');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2057002889411629058,'入库单',2,'com.ruoyi.wms.controller.ReceiptOrderController.doWarehousing()','POST',1,'admin','研发部门','/wms/receiptOrder/warehousing','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2057002888316915714","receiptOrderNo":"RK05207589","receiptOrderType":"采购入库","basisNo":"zzz","dispatchMode":"road","noticeOrg":"a","receiveUnit":"a","purchaseDate":"2026-05-11","receiptDate":"2026-05-20","totalQuantity":"1","payableAmount":"6","receiptOrderStatus":1,"warehouseId":"1828364459110469633","areaId":"2056770261064429569","remark":"a","details":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2057002888392413186","receiptOrderId":null,"skuId":"2056774315278680066","quantity":"1","itemCode":"HUI-DYJ","itemName":"惠普打印机","skuName":"2023","unit":"台","productIdentifier":"xx","qualityGrade":"高","unitPrice":"6","lineAmount":"6.00","remark":null,"warehouseId":"1828364459110469633","areaId":"2056770261064429569","rackId":"2056771221887201282","locationId":"2056771221887201283","receiptItemInstances":[{"id":"2056774828107841540","instanceCode":"WH1ITEM1000000003","boxCode":"aa","remark":null}]}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-20 15:38:01');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2057019388994064386,'字典类型',9,'com.ruoyi.system.controller.system.SysDictTypeController.refreshCache()','DELETE',1,'admin','研发部门','/system/dict/type/refreshCache','0:0:0:0:0:0:0:1','内网IP','{}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-20 16:43:35');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2057019423626432513,'字典类型',9,'com.ruoyi.system.controller.system.SysDictTypeController.refreshCache()','DELETE',1,'admin','研发部门','/system/dict/type/refreshCache','0:0:0:0:0:0:0:1','内网IP','{}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-20 16:43:43');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2057020074561441794,'出库单',1,'com.ruoyi.wms.controller.ShipmentOrderController.add()','POST',1,'admin','研发部门','/wms/shipmentOrder','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2057020074368503809","shipmentOrderNo":"CK05202806","shipmentOrderType":"借用出库","basisNo":null,"dispatchMode":null,"noticeOrg":null,"receiveUnit":null,"purchaseDate":null,"shipmentDate":null,"receivableAmount":"0","totalQuantity":"1","shipmentOrderStatus":0,"warehouseId":"1828364459110469633","areaId":"2056770261064429569","remark":null,"details":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2057020074368503810","shipmentOrderId":null,"skuId":"2056774315278680066","quantity":"1","itemCode":"HUI-DYJ","itemName":"惠普打印机","skuName":"2023","unit":"台","productIdentifier":"xx","qualityGrade":"高","unitPrice":null,"lineAmount":"0","warehouseId":"1828364459110469633","areaId":"2056770261064429569","inventoryDetailId":"2057002889092861954","itemInstanceId":"2056774828107841540","boxId":null,"remark":null,"key":"1828364459110469633_2056770261064429569_2056774315278680066"}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-20 16:46:18');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2057020276588482562,'入库单',2,'com.ruoyi.wms.controller.ReceiptOrderController.doWarehousing()','POST',1,'admin','研发部门','/wms/receiptOrder/warehousing','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2057020275804147713","receiptOrderNo":"RK05200069","receiptOrderType":"采购入库","basisNo":null,"dispatchMode":null,"noticeOrg":null,"receiveUnit":null,"purchaseDate":null,"receiptDate":null,"totalQuantity":"3","payableAmount":"750","receiptOrderStatus":1,"warehouseId":"2056769387227328513","areaId":"2056770392228704258","remark":null,"details":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2057020275804147714","receiptOrderId":null,"skuId":"2056774315278680066","quantity":"1","itemCode":"HUI-DYJ","itemName":"惠普打印机","skuName":"2023","unit":"台","productIdentifier":"xx","qualityGrade":"高","unitPrice":"50","lineAmount":"50.00","remark":null,"warehouseId":"2056769387227328513","areaId":"2056770392228704258","rackId":"2056772972287705090","locationId":"2056772972304482306","receiptItemInstances":[{"id":"2056774828107841541","instanceCode":"WH1ITEM1000000004","boxCode":"hadfad","remark":null}]},{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2057020275804147715","receiptOrderId":null,"skuId":"2056774315278680066","quantity":"1","itemCode":"HUI-DYJ","itemName":"惠普打印机","skuName":"2023","unit":"台","productIdentifier":"xx","qualityGrade":"高","unitPrice":"400","lineAmount":"400.00","remark":null,"warehouseId":"2056769387227328513","areaId":"2056770392228704258","rackId":"2056772480702689282","locationId":"2056772480702689288","receiptItemInstances":[{"id":"2056774828107841542","instanceCode":"WH1ITEM1000000005","boxCode":"fadsfdas","remark":null}]},{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2057020275892228098","receiptOrderId":null,"skuId":"2056774315278680066","quantity":"1","itemCode":"HUI-DYJ","itemName":"惠普打印机","skuName":"2023","unit":"台","productIdentifier":"xx","qualityGrade":"高","unitPrice":"300","lineAmount":"300.00","remark":null,"warehouseId":"2056769387227328513","areaId":"2056770392228704258","rackId":"20567','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-20 16:47:06');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2057020612040527874,'出库单',2,'com.ruoyi.wms.controller.ShipmentOrderController.shipment()','PUT',1,'admin','研发部门','/wms/shipmentOrder/shipment','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"shipmentOrderNo":"CK05203134","shipmentOrderType":"借用出库","basisNo":"aa","dispatchMode":"air","noticeOrg":"aa","receiveUnit":"aa","purchaseDate":"2026-05-05","shipmentDate":"2026-05-20","receivableAmount":"900","totalQuantity":"1","shipmentOrderStatus":1,"warehouseId":"2056769387227328513","areaId":"2056770392228704258","remark":"aa","details":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"shipmentOrderId":null,"skuId":"2056774315278680066","quantity":"1","itemCode":"HUI-DYJ","itemName":"惠普打印机","skuName":"2023","unit":"台","productIdentifier":"xx","qualityGrade":"高","unitPrice":"900","lineAmount":"900","warehouseId":"2056769387227328513","areaId":"2056770392228704258","inventoryDetailId":"2057020276391350273","itemInstanceId":"2056774828107841541","boxId":"2057020275938365442","remark":null,"key":"2056769387227328513_2056770392228704258_2056774315278680066"}]}','',0,'
### Error querying database.  Cause: java.sql.SQLSyntaxErrorException: Unknown column ''production_date'' in ''field list''
### The error may exist in com/ruoyi/wms/mapper/InventoryDetailMapper.java (best guess)
### The error may involve defaultParameterMap
### The error occurred while setting parameters
### SQL: SELECT id,receipt_order_id,receipt_order_type,order_no,type,sku_id,warehouse_id,area_id,rack_id,location_id,item_instance_id,box_id,source_order_type,source_order_id,line_no,quantity,production_date,expiration_date,unit_price,line_amount,remark,remain_quantity,create_by,create_time,update_by,update_time FROM wms_inventory_detail WHERE id IN (   ?  )
### Cause: java.sql.SQLSyntaxErrorException: Unknown column ''production_date'' in ''field list''
; bad SQL grammar []','2026-05-20 16:48:26');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2057024371240026114,'出库单',2,'com.ruoyi.wms.controller.ShipmentOrderController.shipment()','PUT',1,'admin','研发部门','/wms/shipmentOrder/shipment','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2057024369574887426","shipmentOrderNo":"CK05203134","shipmentOrderType":"借用出库","basisNo":"aa","dispatchMode":"air","noticeOrg":"aa","receiveUnit":"aa","purchaseDate":"2026-05-05","shipmentDate":"2026-05-20","receivableAmount":"900","totalQuantity":"1","shipmentOrderStatus":1,"warehouseId":"2056769387227328513","areaId":"2056770392228704258","remark":"aa","details":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2057024369574887427","shipmentOrderId":null,"skuId":"2056774315278680066","quantity":"1","itemCode":"HUI-DYJ","itemName":"惠普打印机","skuName":"2023","unit":"台","productIdentifier":"xx","qualityGrade":"高","unitPrice":"900","lineAmount":"900.00","warehouseId":"2056769387227328513","areaId":"2056770392228704258","inventoryDetailId":"2057020276391350273","itemInstanceId":"2056774828107841541","boxId":"2057020275938365442","remark":null,"key":"2056769387227328513_2056770392228704258_2056774315278680066"}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-20 17:03:22');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2057028354226606081,'出库单',2,'com.ruoyi.wms.controller.ShipmentOrderController.shipment()','PUT',1,'admin','研发部门','/wms/shipmentOrder/shipment','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2057028353064783874","shipmentOrderNo":"CK05203882","shipmentOrderType":"借用出库","basisNo":"s","dispatchMode":"rail","noticeOrg":"s","receiveUnit":"s","purchaseDate":"2026-05-11","shipmentDate":"2026-05-20","receivableAmount":"500","totalQuantity":"1","shipmentOrderStatus":1,"warehouseId":"2056769387227328513","areaId":"2056770392228704258","remark":"ss","details":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2057028353144475649","shipmentOrderId":null,"skuId":"2056774315278680066","quantity":"1","itemCode":"HUI-DYJ","itemName":"惠普打印机","skuName":"2023","unit":"台","productIdentifier":"xx","qualityGrade":"高","unitPrice":"500","lineAmount":"500.00","warehouseId":"2056769387227328513","areaId":"2056770392228704258","inventoryDetailId":"2057020276391350275","itemInstanceId":"2056774828107841543","boxId":"2057020276013862913","remark":null,"key":"2056769387227328513_2056770392228704258_2056774315278680066"}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-20 17:19:12');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2057100083145842690,'入库单',2,'com.ruoyi.wms.controller.ReceiptOrderController.doWarehousing()','POST',1,'admin','研发部门','/wms/receiptOrder/warehousing','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2056999507242323970","receiptOrderNo":"RK05201617","receiptOrderType":"采购入库","basisNo":"纸质","dispatchMode":"air","noticeOrg":"北大","receiveUnit":"清华","purchaseDate":"2026-05-17","receiptDate":"2026-05-20","totalQuantity":"2","payableAmount":"100","receiptOrderStatus":1,"warehouseId":"1828364459110469633","areaId":"2056770222548135937","remark":"aaaaa","details":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2056999507401707521","receiptOrderId":"2056999507242323970","skuId":"2056774315278680066","quantity":"1","itemCode":"HUI-DYJ","itemName":"惠普打印机","skuName":"2023","unit":"台","productIdentifier":"xx","qualityGrade":"高","unitPrice":"50","lineAmount":"50.00","remark":null,"warehouseId":"1828364459110469633","areaId":"2056770222548135937","rackId":"2056771137254535169","locationId":"2056771137254535170","receiptItemInstances":[{"id":"2056774828107841539","instanceCode":"WH1ITEM1000000002","boxCode":"","remark":null}]},{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2056999507401707522","receiptOrderId":"2056999507242323970","skuId":"2056774315278680066","quantity":"1","itemCode":"HUI-DYJ","itemName":"惠普打印机","skuName":"2023","unit":"台","productIdentifier":"xx","qualityGrade":"高","unitPrice":"50","lineAmount":"50.00","remark":null,"warehouseId":"1828364459110469633","areaId":"2056770222548135937","rackId":"2056771137254535169","locationId":"2056771137254535170","receiptItemInstances":[{"id":"2056774828107841538","instanceCode":"WH1ITEM1000000001","boxCode":"","remark":null}]}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-20 22:04:14');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2057102397088833537,'借出登记',1,'com.ruoyi.wms.controller.BorrowRecordController.borrow()','POST',1,'admin','研发部门','/wms/borrowRecord/borrow','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"itemInstanceId":"2056774828107841539","borrowStatus":null,"borrower":"张哥","fromUnit":"A","toUnit":"B","fromPerson":"小a","toPerson":"小b","docDate":"2026-05-20","borrowNo":null,"planReturnDate":"2026-05-30","overdueFlag":null,"overdueDays":null,"instanceCode":"WH1ITEM1000000002","borrowTime":"2026-05-20 22:12:33","returnTime":null,"borrowRemark":"zzz","returnRemark":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-20 22:13:25');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2057102620561350657,'归还登记',2,'com.ruoyi.wms.controller.BorrowRecordController.returnItem()','POST',1,'admin','研发部门','/wms/borrowRecord/return','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"itemInstanceId":"2056774828107841500","borrowStatus":null,"borrower":null,"fromUnit":null,"toUnit":null,"fromPerson":null,"toPerson":null,"docDate":null,"borrowNo":null,"planReturnDate":null,"overdueFlag":null,"overdueDays":null,"instanceCode":null,"borrowTime":null,"returnTime":"2026-05-21 00:00:00","borrowRemark":null,"returnRemark":"已还"}','',0,'当前单品不存在未归还借用记录','2026-05-20 22:14:19');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2057102684860030977,'归还登记',2,'com.ruoyi.wms.controller.BorrowRecordController.returnItem()','POST',1,'admin','研发部门','/wms/borrowRecord/return','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"itemInstanceId":"2056774828107841500","borrowStatus":null,"borrower":null,"fromUnit":null,"toUnit":null,"fromPerson":null,"toPerson":null,"docDate":null,"borrowNo":null,"planReturnDate":null,"overdueFlag":null,"overdueDays":null,"instanceCode":null,"borrowTime":null,"returnTime":"2026-05-21 00:00:00","borrowRemark":null,"returnRemark":"已还"}','',0,'当前单品不存在未归还借用记录','2026-05-20 22:14:34');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2057102760772739073,'归还登记',2,'com.ruoyi.wms.controller.BorrowRecordController.returnItem()','POST',1,'admin','研发部门','/wms/borrowRecord/return','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"itemInstanceId":"2056774828107841500","borrowStatus":null,"borrower":null,"fromUnit":null,"toUnit":null,"fromPerson":null,"toPerson":null,"docDate":null,"borrowNo":null,"planReturnDate":null,"overdueFlag":null,"overdueDays":null,"instanceCode":null,"borrowTime":null,"returnTime":"2026-05-21 00:00:00","borrowRemark":null,"returnRemark":"已还"}','',0,'当前单品不存在未归还借用记录','2026-05-20 22:14:52');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2057102866418868225,'归还登记',2,'com.ruoyi.wms.controller.BorrowRecordController.returnItem()','POST',1,'admin','研发部门','/wms/borrowRecord/return','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"itemInstanceId":"2056774828107841500","borrowStatus":null,"borrower":null,"fromUnit":null,"toUnit":null,"fromPerson":null,"toPerson":null,"docDate":null,"borrowNo":null,"planReturnDate":null,"overdueFlag":null,"overdueDays":null,"instanceCode":null,"borrowTime":null,"returnTime":"2026-05-21 00:00:00","borrowRemark":null,"returnRemark":"已还"}','',0,'当前单品不存在未归还借用记录','2026-05-20 22:15:17');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2057103085663526914,'归还登记',2,'com.ruoyi.wms.controller.BorrowRecordController.returnItem()','POST',1,'admin','研发部门','/wms/borrowRecord/return','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"itemInstanceId":"2056774828107841500","borrowStatus":null,"borrower":null,"fromUnit":null,"toUnit":null,"fromPerson":null,"toPerson":null,"docDate":null,"borrowNo":null,"planReturnDate":null,"overdueFlag":null,"overdueDays":null,"instanceCode":null,"borrowTime":null,"returnTime":"2026-05-21 00:00:00","borrowRemark":null,"returnRemark":"已还"}','',0,'当前单品不存在未归还借用记录','2026-05-20 22:16:09');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2057105562932715522,'货位',2,'com.ruoyi.wms.controller.LocationController.rebuildByRack()','POST',1,'admin','研发部门','/wms/location/rebuildByRack/2056772972287705090','0:0:0:0:0:0:0:1','内网IP','"2056772972287705090"','{"code":200,"msg":"操作成功","data":{"rackId":"2056772972287705090","rackCode":"WH1RACK1000000006","rackName":"海B货2","expectedLocationCount":6,"existingLocationCount":6,"createdLocationCount":0,"blockedLocationCount":0,"messages":[]}}',1,'','2026-05-20 22:26:00');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2057105659552702466,'货位',2,'com.ruoyi.wms.controller.LocationController.rebuildByRack()','POST',1,'admin','研发部门','/wms/location/rebuildByRack/2056771137254535169','0:0:0:0:0:0:0:1','内网IP','"2056771137254535169"','{"code":200,"msg":"操作成功","data":{"rackId":"2056771137254535169","rackCode":"WH1RACK1000000002","rackName":"京A货2","expectedLocationCount":1,"existingLocationCount":1,"createdLocationCount":0,"blockedLocationCount":0,"messages":[]}}',1,'','2026-05-20 22:26:23');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2057110762208731138,'单品实例',2,'com.ruoyi.wms.controller.ItemInstanceController.edit()','PUT',1,'admin','研发部门','/wms/itemInstance','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2056774828107841538","instanceCode":"WH1ITEM1000000001","itemId":"2056774315211571201","skuId":"2056774315278680066","instanceStatus":"在库","warehouseId":"1828364459110469633","areaId":"2056770222548135937","rackId":"2056771137254535169","locationId":"2056771137254535170","sourceType":"入库单","sourceOrderType":"入库单","boxId":null,"boxCode":"bolkjfladdffa","sourceOrderId":"2056999507242323970","sourceOrderNo":"RK05201617","receiptOrderDetailId":"2056999507401707522","shipmentOrderDetailId":null,"lastOperationType":"batch_print","lastOperationTime":"2026-05-20 00:31:46","remark":null,"unreceivedOnly":null,"unshippedOnly":null,"targetStatus":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-20 22:46:40');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2057111300065304578,'箱体',3,'com.ruoyi.wms.controller.BoxController.remove()','DELETE',1,'admin','研发部门','/wms/box/2057110762082902017','0:0:0:0:0:0:0:1','内网IP','{}','',0,'箱体内仍有单品，无法删除','2026-05-20 22:48:48');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2057114735401607169,'归还登记',2,'com.ruoyi.wms.controller.BorrowRecordController.returnItem()','POST',1,'admin','研发部门','/wms/borrowRecord/return','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2057102396967198721","itemInstanceId":null,"borrowStatus":null,"borrower":null,"fromUnit":null,"toUnit":null,"fromPerson":null,"toPerson":null,"docDate":null,"borrowNo":null,"planReturnDate":null,"overdueFlag":null,"overdueDays":null,"instanceCode":null,"borrowTime":null,"returnTime":"2026-05-20 23:02:11","borrowRemark":null,"returnRemark":"已还"}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-20 23:02:27');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2057116912979386370,'借出登记',1,'com.ruoyi.wms.controller.BorrowRecordController.borrow()','POST',1,'admin','研发部门','/wms/borrowRecord/borrow','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"itemInstanceId":"2056774828107841539","borrowStatus":null,"borrower":"aa","fromUnit":"aa","toUnit":"aa","fromPerson":"a","toPerson":"a","docDate":"2026-05-20","borrowNo":null,"planReturnDate":"2026-05-21","overdueFlag":null,"overdueDays":null,"instanceCode":"WH1ITEM1000000002","borrowTime":"2026-05-20 23:10:06","returnTime":null,"borrowRemark":"借用","returnRemark":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-20 23:11:06');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2057122662745198593,'单品实例',5,'com.ruoyi.wms.controller.ItemInstanceController.export()','POST',1,'admin','研发部门','/wms/itemInstance/export','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"instanceCode":null,"itemId":null,"skuId":null,"instanceStatus":null,"warehouseId":null,"areaId":null,"rackId":null,"locationId":null,"sourceType":null,"sourceOrderType":null,"boxId":null,"boxCode":null,"sourceOrderId":null,"sourceOrderNo":null,"receiptOrderDetailId":null,"shipmentOrderDetailId":null,"lastOperationType":null,"lastOperationTime":null,"remark":null,"unreceivedOnly":null,"unshippedOnly":null,"targetStatus":null}','',1,'','2026-05-20 23:33:57');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2057144243794034689,'库存盘点单据',2,'com.ruoyi.wms.controller.CheckOrderController.check()','POST',1,'admin','研发部门','/wms/checkOrder/check','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"checkOrderNo":"PK05212439","checkOrderStatus":1,"checkOrderTotal":"0","warehouseId":"1828364459110469633","areaId":null,"rackId":null,"checkScopeType":"warehouse","checkDate":"2026-05-21 00:59:37","checkerName":"a","reviewerName":"a","remark":null,"details":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"checkOrderId":null,"skuId":"2056774315278680066","quantity":"1","checkQuantity":"1","profitAndLoss":"0","differenceQuantity":"0","warehouseId":"1828364459110469633","areaId":"2056770222548135937","rackId":"2056771137254535169","locationId":"2056771137254535170","receiptTime":"2026-05-20 22:04:13","inventoryDetailId":"2057100082160181250","itemInstanceId":null,"boxId":null,"remark":null,"haveProfitAndLoss":null},{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"checkOrderId":null,"skuId":"2056774315278680066","quantity":"1","checkQuantity":"1","profitAndLoss":"0","differenceQuantity":"0","warehouseId":"1828364459110469633","areaId":"2056770222548135937","rackId":"2056771137254535169","locationId":"2056771137254535170","receiptTime":"2026-05-20 22:04:13","inventoryDetailId":"2057100082160181251","itemInstanceId":null,"boxId":null,"remark":null,"haveProfitAndLoss":null},{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"checkOrderId":null,"skuId":"2056774315278680066","quantity":"1","checkQuantity":"1","profitAndLoss":"0","differenceQuantity":"0","warehouseId":"1828364459110469633","areaId":"2056770261064429569","rackId":"2056771221887201282","locationId":"2056771221887201283","receiptTime":"2026-05-20 15:38:00","inventoryDetailId":"2057002889092861954","itemInstanceId":null,"boxId":null,"remark":null,"haveProfitAndLoss":null}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-21 00:59:42');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2057301176236675073,'库存盘点单据',2,'com.ruoyi.wms.controller.CheckOrderController.check()','POST',1,'admin','研发部门','/wms/checkOrder/check','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"checkOrderNo":"PK05213385","checkOrderStatus":1,"checkOrderTotal":"0","warehouseId":"1828364459110469633","areaId":null,"rackId":null,"checkScopeType":"warehouse","checkDate":null,"checkerName":null,"reviewerName":null,"remark":null,"details":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"checkOrderId":null,"skuId":"2056774315278680066","quantity":"1","checkQuantity":"1","profitAndLoss":"0","differenceQuantity":"0","warehouseId":"1828364459110469633","areaId":"2056770222548135937","rackId":"2056771137254535169","locationId":"2056771137254535170","receiptTime":"2026-05-20 22:04:13","inventoryDetailId":"2057100082160181250","itemInstanceId":null,"boxId":null,"remark":null,"haveProfitAndLoss":null},{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"checkOrderId":null,"skuId":"2056774315278680066","quantity":"1","checkQuantity":"1","profitAndLoss":"0","differenceQuantity":"0","warehouseId":"1828364459110469633","areaId":"2056770222548135937","rackId":"2056771137254535169","locationId":"2056771137254535170","receiptTime":"2026-05-20 22:04:13","inventoryDetailId":"2057100082160181251","itemInstanceId":null,"boxId":null,"remark":null,"haveProfitAndLoss":null},{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"checkOrderId":null,"skuId":"2056774315278680066","quantity":"1","checkQuantity":"1","profitAndLoss":"0","differenceQuantity":"0","warehouseId":"1828364459110469633","areaId":"2056770261064429569","rackId":"2056771221887201282","locationId":"2056771221887201283","receiptTime":"2026-05-20 15:38:00","inventoryDetailId":"2057002889092861954","itemInstanceId":null,"boxId":null,"remark":null,"haveProfitAndLoss":null}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-21 11:23:18');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2057353035051753473,'调拨单',1,'com.ruoyi.wms.controller.MovementOrderController.add()','POST',1,'admin','研发部门','/wms/movementOrder','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2057353034334527489","movementOrderNo":"DB05215667","movementType":"通装","dispatchBasis":null,"dispatchPurpose":null,"dispatchMode":null,"fromUnit":null,"toUnit":null,"fromStation":null,"toStation":null,"fromAddress":null,"toAddress":null,"contactAddress":null,"dispatchDate":null,"effectiveDate":null,"issueDate":null,"sourceWarehouseId":"1828364459110469633","sourceAreaId":"2056770261064429569","targetWarehouseId":"1828364459110469633","targetAreaId":"2056770222548135937","movementOrderStatus":0,"totalQuantity":"1","remark":null,"details":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"movementOrderId":null,"skuId":"2056774315278680066","quantity":"1","itemCode":"HUI-DYJ","itemName":"惠普打印机","skuName":"2023","unit":"台","productIdentifier":"xx","qualityGrade":"高","unitPrice":"6","lineAmount":"6.00","remark":null,"sourceWarehouseId":"1828364459110469633","sourceAreaId":"2056770261064429569","targetWarehouseId":"1828364459110469633","targetAreaId":"2056770222548135937","inventoryDetailId":"2057002889092861954","warehouseId":"1828364459110469633","areaId":"2056770261064429569","key":"1828364459110469633_2056770261064429569_2056774315278680066"}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-21 14:49:22');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2057353886222835713,'调拨单',2,'com.ruoyi.wms.controller.MovementOrderController.move()','POST',1,'admin','研发部门','/wms/movementOrder/move','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2057353034334527489","movementOrderNo":"DB05215667","movementType":"通装","dispatchBasis":null,"dispatchPurpose":null,"dispatchMode":null,"fromUnit":null,"toUnit":null,"fromStation":null,"toStation":null,"fromAddress":null,"toAddress":null,"contactAddress":null,"dispatchDate":null,"effectiveDate":null,"issueDate":null,"sourceWarehouseId":"1828364459110469633","sourceAreaId":"2056770261064429569","targetWarehouseId":"1828364459110469633","targetAreaId":"2056770222548135937","movementOrderStatus":1,"totalQuantity":"1.00","remark":null,"details":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2057353034393247746","movementOrderId":"2057353034334527489","skuId":"2056774315278680066","quantity":"1.00","itemCode":"HUI-DYJ","itemName":"惠普打印机","skuName":"2023","unit":"台","productIdentifier":"xx","qualityGrade":"高","unitPrice":"6","lineAmount":"6.00","remark":null,"sourceWarehouseId":"1828364459110469633","sourceAreaId":"2056770261064429569","targetWarehouseId":"1828364459110469633","targetAreaId":"2056770222548135937","inventoryDetailId":"2057002889092861954","warehouseId":"1828364459110469633","areaId":"2056770261064429569","key":"1828364459110469633_2056770261064429569_2056774315278680066"}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-21 14:52:45');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2058920332694421506,'物料',0,'com.ruoyi.wms.controller.ItemController.batchPrintQrCode()','POST',1,'admin','研发部门','/wms/item/batchPrintQrCode','0:0:0:0:0:0:0:1','内网IP','{"row":{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2058919797824192514","ids":null,"itemCode":"DN","itemName":"苹果电脑","itemCategory":null,"unit":null,"equipmentName":null,"equipmentType":null,"status":null,"remark":"z","sku":null},"skuId":"2058919798067462146","qrCodeCount":1}','{"code":200,"msg":"操作成功","data":{"itemKey":"ITEMWH1","qrCodeCount":1,"row":{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2058919797824192514","ids":null,"itemCode":"DN","itemName":"苹果电脑","itemCategory":null,"unit":null,"equipmentName":null,"equipmentType":null,"status":null,"remark":"z","sku":null},"details":[{"serialValue":1000000001,"instanceCode":"ITEMWH11000000001","qrCodeValue":"ITEMWH11000000001","qrContent":"ITEMWH11000000001"}]}}',1,'','2026-05-25 22:37:15');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2057356266884620290,'出库单',2,'com.ruoyi.wms.controller.ShipmentOrderController.shipment()','PUT',1,'admin','研发部门','/wms/shipmentOrder/shipment','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2057020074368503809","shipmentOrderNo":"CK05202806","shipmentOrderType":"借用出库","basisNo":null,"dispatchMode":null,"noticeOrg":null,"receiveUnit":null,"purchaseDate":null,"shipmentDate":null,"receivableAmount":"0","totalQuantity":"1","shipmentOrderStatus":1,"warehouseId":"1828364459110469633","areaId":"2056770261064429569","remark":null,"details":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2057020074368503810","shipmentOrderId":"2057020074368503809","skuId":"2056774315278680066","quantity":"0","itemCode":"HUI-DYJ","itemName":"惠普打印机","skuName":"2023","unit":"台","productIdentifier":"xx","qualityGrade":"高","unitPrice":null,"lineAmount":"0","warehouseId":"1828364459110469633","areaId":"2056770261064429569","inventoryDetailId":"2057002889092861954","itemInstanceId":"2056774828107841540","boxId":null,"remark":null,"key":"1828364459110469633_2056770261064429569_2056774315278680066"}]}','',0,'按单品实例出库时，数量必须为1','2026-05-21 15:02:13');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2057356315366580226,'出库单详情',3,'com.ruoyi.wms.controller.ShipmentOrderDetailController.remove()','DELETE',1,'admin','研发部门','/wms/shipmentOrderDetail/2057020074368503810','0:0:0:0:0:0:0:1','内网IP','{}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-21 15:02:24');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2057356420693942274,'出库单',2,'com.ruoyi.wms.controller.ShipmentOrderController.shipment()','PUT',1,'admin','研发部门','/wms/shipmentOrder/shipment','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2057020074368503809","shipmentOrderNo":"CK05202806","shipmentOrderType":"借用出库","basisNo":null,"dispatchMode":null,"noticeOrg":null,"receiveUnit":null,"purchaseDate":null,"shipmentDate":null,"receivableAmount":"50","totalQuantity":"1","shipmentOrderStatus":1,"warehouseId":"1828364459110469633","areaId":"2056770222548135937","remark":null,"details":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2057356420182237185","shipmentOrderId":"2057020074368503809","skuId":"2056774315278680066","quantity":"1","itemCode":"HUI-DYJ","itemName":"惠普打印机","skuName":"2023","unit":"台","productIdentifier":"xx","qualityGrade":"高","unitPrice":"50","lineAmount":"50.00","warehouseId":"1828364459110469633","areaId":"2056770222548135937","inventoryDetailId":"2057100082160181250","itemInstanceId":"2056774828107841538","boxId":"2057110762082902017","remark":null,"key":"1828364459110469633_2056770222548135937_2056774315278680066"}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-21 15:02:49');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2057384631653969922,'入库单',2,'com.ruoyi.wms.controller.ReceiptOrderController.doWarehousing()','POST',1,'admin','研发部门','/wms/receiptOrder/warehousing','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2057384612725075970","receiptOrderNo":"RK05217410","receiptOrderType":"采购入库","basisNo":null,"dispatchMode":null,"noticeOrg":null,"receiveUnit":null,"purchaseDate":null,"receiptDate":null,"totalQuantity":"1","payableAmount":"4444","receiptOrderStatus":1,"warehouseId":"1828364459110469633","areaId":"2056770261064429569","remark":null,"details":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2057384612792184833","receiptOrderId":null,"skuId":"2056774315278680066","quantity":"1","itemCode":"HUI-DYJ","itemName":"惠普打印机","skuName":"2023","unit":"台","productIdentifier":"xx","qualityGrade":"高","unitPrice":"4444","lineAmount":"4444.00","remark":null,"warehouseId":"1828364459110469633","areaId":"2056770261064429569","rackId":"2056771221887201282","locationId":"2056771221937532933","receiptItemInstances":[{"id":"2056774828187533314","instanceCode":"WH1ITEM1000000007","boxCode":"dfadfad","remark":null}]}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-21 16:54:55');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2057385076975808514,'调拨单',2,'com.ruoyi.wms.controller.MovementOrderController.move()','POST',1,'admin','研发部门','/wms/movementOrder/move','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2057385076648652802","movementOrderNo":"DB05217966","movementType":"通装","dispatchBasis":null,"dispatchPurpose":null,"dispatchMode":null,"fromUnit":null,"toUnit":null,"fromStation":null,"toStation":null,"fromAddress":null,"toAddress":null,"contactAddress":null,"dispatchDate":null,"effectiveDate":null,"issueDate":null,"sourceWarehouseId":"1828364459110469633","sourceAreaId":"2056770261064429569","targetWarehouseId":"2056769648226283522","targetAreaId":"2056770672827641857","movementOrderStatus":1,"totalQuantity":"1","remark":null,"details":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"movementOrderId":null,"skuId":"2056774315278680066","quantity":"1","itemCode":"HUI-DYJ","itemName":"惠普打印机","skuName":"2023","unit":"台","productIdentifier":"xx","qualityGrade":"高","unitPrice":"4444","lineAmount":"4444.00","remark":null,"sourceWarehouseId":"1828364459110469633","sourceAreaId":"2056770261064429569","targetWarehouseId":"2056769648226283522","targetAreaId":"2056770672827641857","inventoryDetailId":"2057384626956349442","warehouseId":"1828364459110469633","areaId":"2056770261064429569","key":"1828364459110469633_2056770261064429569_2056774315278680066"}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-21 16:56:41');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2057404460960133122,'入库单',2,'com.ruoyi.wms.controller.ReceiptOrderController.doWarehousing()','POST',1,'admin','研发部门','/wms/receiptOrder/warehousing','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2057404460297433090","receiptOrderNo":"RK05210078","receiptOrderType":"采购入库","basisNo":null,"dispatchMode":null,"noticeOrg":null,"receiveUnit":null,"purchaseDate":null,"receiptDate":null,"totalQuantity":"2","payableAmount":"2","receiptOrderStatus":1,"warehouseId":"1828364459110469633","areaId":"2056770222548135937","remark":null,"details":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2057404460297433091","receiptOrderId":null,"skuId":"2056774315278680066","quantity":"1","itemCode":"HUI-DYJ","itemName":"惠普打印机","skuName":"2023","unit":"台","productIdentifier":"xx","qualityGrade":"高","unitPrice":"1","lineAmount":"1.00","remark":null,"warehouseId":"1828364459110469633","areaId":"2056770222548135937","rackId":"2056771137254535169","locationId":"2056771137254535170","receiptItemInstances":[{"id":"2056774828187533315","instanceCode":"WH1ITEM1000000008","boxCode":"zzzzz","remark":null}]},{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2057404460297433092","receiptOrderId":null,"skuId":"2056774315278680066","quantity":"1","itemCode":"HUI-DYJ","itemName":"惠普打印机","skuName":"2023","unit":"台","productIdentifier":"xx","qualityGrade":"高","unitPrice":"1","lineAmount":"1.00","remark":null,"warehouseId":"1828364459110469633","areaId":"2056770222548135937","rackId":"2056771137254535169","locationId":"2056771137254535170","receiptItemInstances":[{"id":"2056774828187533316","instanceCode":"WH1ITEM1000000009","boxCode":"zzz","remark":null}]}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-21 18:13:43');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2057405642420072449,'调拨单',2,'com.ruoyi.wms.controller.MovementOrderController.move()','POST',1,'admin','研发部门','/wms/movementOrder/move','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2057405642227134465","movementOrderNo":"DB05217208","movementType":"通装","dispatchBasis":"z","dispatchPurpose":"z","dispatchMode":"air","fromUnit":"z","toUnit":"z","fromStation":null,"toStation":"z","fromAddress":null,"toAddress":null,"contactAddress":"z","dispatchDate":null,"effectiveDate":"2026-05-20","issueDate":"2026-05-21","sourceWarehouseId":"1828364459110469633","sourceAreaId":"2056770222548135937","targetWarehouseId":"2056769466831024130","targetAreaId":"2056770538748325889","movementOrderStatus":1,"totalQuantity":"1","remark":"zz","details":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"movementOrderId":null,"skuId":"2056774315278680066","quantity":"1","itemCode":"HUI-DYJ","itemName":"惠普打印机","skuName":"2023","unit":"台","productIdentifier":"xx","qualityGrade":"高","unitPrice":"1","lineAmount":"1.00","remark":null,"sourceWarehouseId":"1828364459110469633","sourceAreaId":"2056770222548135937","targetWarehouseId":"2056769466831024130","targetAreaId":"2056770538748325889","inventoryDetailId":"2057404460763000833","warehouseId":"1828364459110469633","areaId":"2056770222548135937","key":"1828364459110469633_2056770222548135937_2056774315278680066"}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-21 18:18:25');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2057412630440890370,'调拨单',2,'com.ruoyi.wms.controller.MovementOrderController.move()','POST',1,'admin','研发部门','/wms/movementOrder/move','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2057412629375537153","movementOrderNo":"DB05211481","movementType":"通装","dispatchBasis":null,"dispatchPurpose":null,"dispatchMode":null,"fromUnit":null,"toUnit":null,"fromStation":null,"toStation":null,"fromAddress":null,"toAddress":null,"contactAddress":null,"dispatchDate":null,"effectiveDate":null,"issueDate":null,"sourceWarehouseId":"1828364459110469633","sourceAreaId":"2056770222548135937","targetWarehouseId":"2056769490864386049","targetAreaId":"2056770610286374914","movementOrderStatus":1,"totalQuantity":"1","remark":null,"details":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"movementOrderId":null,"skuId":"2056774315278680066","quantity":"1","itemCode":"HUI-DYJ","itemName":"惠普打印机","skuName":"2023","unit":"台","productIdentifier":"xx","qualityGrade":"高","unitPrice":"1","lineAmount":"1.00","remark":null,"sourceWarehouseId":"1828364459110469633","sourceAreaId":"2056770222548135937","sourceRackId":"2056771137254535169","sourceLocationId":"2056771137254535170","targetWarehouseId":"2056769490864386049","targetAreaId":"2056770610286374914","targetRackId":null,"targetLocationId":null,"inventoryDetailId":"2057404460763000834","itemInstanceId":"2056774828187533316","warehouseId":"1828364459110469633","areaId":"2056770222548135937","key":"1828364459110469633_2056770222548135937_2056774315278680066"}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-21 18:46:11');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2057460273196085250,'入库单',2,'com.ruoyi.wms.controller.ReceiptOrderController.doWarehousing()','POST',1,'admin','研发部门','/wms/receiptOrder/warehousing','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2057460272143314946","receiptOrderNo":"RK05218626","receiptOrderType":"采购入库","basisNo":null,"dispatchMode":null,"noticeOrg":null,"receiveUnit":null,"purchaseDate":null,"receiptDate":null,"totalQuantity":"1","payableAmount":"901","receiptOrderStatus":1,"warehouseId":"1828364459110469633","areaId":"2056770261064429569","remark":null,"details":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2057460272206229506","receiptOrderId":null,"skuId":"2056774315278680066","quantity":"1","itemCode":"HUI-DYJ","itemName":"惠普打印机","skuName":"2023","unit":"台","productIdentifier":"xx","qualityGrade":"高","unitPrice":"901","lineAmount":"901.00","remark":null,"warehouseId":"1828364459110469633","areaId":"2056770261064429569","rackId":"2056771221887201282","locationId":"2056771221937532934","receiptItemInstances":[{"id":"2056774828187533317","instanceCode":"WH1ITEM1000000010","boxCode":"xxzxcx","remark":null}]}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-21 21:55:30');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2057463795098628098,'调拨单',2,'com.ruoyi.wms.controller.MovementOrderController.move()','POST',1,'admin','研发部门','/wms/movementOrder/move','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2057463794524008450","movementOrderNo":"DB05215912","movementType":"通装","dispatchBasis":"z","dispatchPurpose":"z","dispatchMode":"rail","fromUnit":"z","toUnit":"zz","fromStation":null,"toStation":"z","fromAddress":null,"toAddress":null,"contactAddress":"z","dispatchDate":null,"effectiveDate":"2026-05-22","issueDate":"2026-05-29","sourceWarehouseId":"1828364459110469633","sourceAreaId":"2056770261064429569","targetWarehouseId":"1828364459110469633","targetAreaId":"2056770222548135937","movementOrderStatus":1,"totalQuantity":"1","remark":"zzz","details":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"movementOrderId":null,"skuId":"2056774315278680066","quantity":"1","itemCode":"HUI-DYJ","itemName":"惠普打印机","skuName":"2023","unit":"台","productIdentifier":"xx","qualityGrade":"高","unitPrice":"901","lineAmount":"901.00","remark":null,"sourceWarehouseId":"1828364459110469633","sourceAreaId":"2056770261064429569","sourceRackId":"2056771221887201282","sourceLocationId":"2056771221937532934","targetWarehouseId":"1828364459110469633","targetAreaId":"2056770222548135937","targetRackId":null,"targetLocationId":null,"inventoryDetailId":"2057460272671797250","itemInstanceId":"2056774828187533317","warehouseId":"1828364459110469633","areaId":"2056770261064429569","key":"1828364459110469633_2056770261064429569_2056774315278680066"}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-21 22:09:29');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2057493158808633345,'物料',2,'com.ruoyi.wms.controller.ItemController.edit()','PUT',1,'admin','研发部门','/wms/item','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2056774315211571201","ids":null,"itemCode":"HUI-DYJ","itemName":"惠普打印机","itemCategory":"1828365014901886978","unit":"台","equipmentName":null,"equipmentType":null,"status":"1","remark":null,"sku":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2056774315278680066","skuName":"2023","productIdentifier":"xx","qualityGrade":"高","itemId":"2056774315211571201","status":"1","itemName":null,"itemCode":null,"equipmentName":null,"itemCategory":null},{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"skuName":"2024","productIdentifier":"yy","qualityGrade":"低","itemId":"2056774315211571201","status":"1","itemName":null,"itemCode":null,"equipmentName":null,"itemCategory":null}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-22 00:06:10');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2057493186092580866,'物料',0,'com.ruoyi.wms.controller.ItemController.batchPrintQrCode()','POST',1,'admin','研发部门','/wms/item/batchPrintQrCode','0:0:0:0:0:0:0:1','内网IP','{"row":{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2056774315211571201","ids":null,"itemCode":"HUI-DYJ","itemName":"惠普打印机","itemCategory":null,"unit":null,"equipmentName":null,"equipmentType":null,"status":null,"remark":null,"sku":null},"skuId":"2057493158586335233","qrCodeCount":5}','{"code":200,"msg":"操作成功","data":{"itemKey":"WH1ITEM","qrCodeCount":5,"row":{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2056774315211571201","ids":null,"itemCode":"HUI-DYJ","itemName":"惠普打印机","itemCategory":null,"unit":null,"equipmentName":null,"equipmentType":null,"status":null,"remark":null,"sku":null},"details":[{"serialValue":1000000011,"instanceCode":"WH1ITEM1000000011","qrCodeValue":"WH1ITEM1000000011","qrContent":"WH1ITEM1000000011"},{"serialValue":1000000012,"instanceCode":"WH1ITEM1000000012","qrCodeValue":"WH1ITEM1000000012","qrContent":"WH1ITEM1000000012"},{"serialValue":1000000013,"instanceCode":"WH1ITEM1000000013","qrCodeValue":"WH1ITEM1000000013","qrContent":"WH1ITEM1000000013"},{"serialValue":1000000014,"instanceCode":"WH1ITEM1000000014","qrCodeValue":"WH1ITEM1000000014","qrContent":"WH1ITEM1000000014"},{"serialValue":1000000015,"instanceCode":"WH1ITEM1000000015","qrCodeValue":"WH1ITEM1000000015","qrContent":"WH1ITEM1000000015"}]}}',1,'','2026-05-22 00:06:17');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2057493372961406978,'物料',0,'com.ruoyi.wms.controller.ItemController.batchPrintQrCode()','POST',1,'admin','研发部门','/wms/item/batchPrintQrCode','0:0:0:0:0:0:0:1','内网IP','{"row":{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2056774315211571201","ids":null,"itemCode":"HUI-DYJ","itemName":"惠普打印机","itemCategory":null,"unit":null,"equipmentName":null,"equipmentType":null,"status":null,"remark":null,"sku":null},"skuId":"2056774315278680066","qrCodeCount":10}','{"code":200,"msg":"操作成功","data":{"itemKey":"WH1ITEM","qrCodeCount":10,"row":{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2056774315211571201","ids":null,"itemCode":"HUI-DYJ","itemName":"惠普打印机","itemCategory":null,"unit":null,"equipmentName":null,"equipmentType":null,"status":null,"remark":null,"sku":null},"details":[{"serialValue":1000000016,"instanceCode":"WH1ITEM1000000016","qrCodeValue":"WH1ITEM1000000016","qrContent":"WH1ITEM1000000016"},{"serialValue":1000000017,"instanceCode":"WH1ITEM1000000017","qrCodeValue":"WH1ITEM1000000017","qrContent":"WH1ITEM1000000017"},{"serialValue":1000000018,"instanceCode":"WH1ITEM1000000018","qrCodeValue":"WH1ITEM1000000018","qrContent":"WH1ITEM1000000018"},{"serialValue":1000000019,"instanceCode":"WH1ITEM1000000019","qrCodeValue":"WH1ITEM1000000019","qrContent":"WH1ITEM1000000019"},{"serialValue":1000000020,"instanceCode":"WH1ITEM1000000020","qrCodeValue":"WH1ITEM1000000020","qrContent":"WH1ITEM1000000020"},{"serialValue":1000000021,"instanceCode":"WH1ITEM1000000021","qrCodeValue":"WH1ITEM1000000021","qrContent":"WH1ITEM1000000021"},{"serialValue":1000000022,"instanceCode":"WH1ITEM1000000022","qrCodeValue":"WH1ITEM1000000022","qrContent":"WH1ITEM1000000022"},{"serialValue":1000000023,"instanceCode":"WH1ITEM1000000023","qrCodeValue":"WH1ITEM1000000023","qrContent":"WH1ITEM1000000023"},{"serialValue":1000000024,"instanceCode":"WH1ITEM1000000024","qrCodeValue":"WH1ITEM1000000024","qrContent":"WH1ITEM1000000024"},{"serialValue":1000000025,"instanceCode":"WH1ITEM1000000025","qrCodeValue":"WH1ITEM1000000025","qrContent":"WH1ITEM1000000025"}]}}',1,'','2026-05-22 00:07:01');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2058919798960848898,'物料',1,'com.ruoyi.wms.controller.ItemController.add()','POST',1,'admin','研发部门','/wms/item','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"ids":null,"itemCode":"DN","itemName":"苹果电脑","itemCategory":"1828365043024695297","unit":"台","equipmentName":null,"equipmentType":null,"status":"1","remark":"z","sku":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"skuName":"1T","productIdentifier":"高标识","qualityGrade":"高","itemId":"2058919797824192514","status":"1","itemName":null,"itemCode":null,"equipmentName":null,"itemCategory":null},{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"skuName":"2T","productIdentifier":"低标识","qualityGrade":"低","itemId":"2058919797824192514","status":"1","itemName":null,"itemCode":null,"equipmentName":null,"itemCategory":null}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-25 22:35:08');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2058920375136583682,'物料',0,'com.ruoyi.wms.controller.ItemController.batchPrintQrCode()','POST',1,'admin','研发部门','/wms/item/batchPrintQrCode','0:0:0:0:0:0:0:1','内网IP','{"row":{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2058919797824192514","ids":null,"itemCode":"DN","itemName":"苹果电脑","itemCategory":null,"unit":null,"equipmentName":null,"equipmentType":null,"status":null,"remark":"z","sku":null},"skuId":"2058919798067462147","qrCodeCount":10}','{"code":200,"msg":"操作成功","data":{"itemKey":"ITEMWH1","qrCodeCount":10,"row":{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2058919797824192514","ids":null,"itemCode":"DN","itemName":"苹果电脑","itemCategory":null,"unit":null,"equipmentName":null,"equipmentType":null,"status":null,"remark":"z","sku":null},"details":[{"serialValue":1000000002,"instanceCode":"ITEMWH11000000002","qrCodeValue":"ITEMWH11000000002","qrContent":"ITEMWH11000000002"},{"serialValue":1000000003,"instanceCode":"ITEMWH11000000003","qrCodeValue":"ITEMWH11000000003","qrContent":"ITEMWH11000000003"},{"serialValue":1000000004,"instanceCode":"ITEMWH11000000004","qrCodeValue":"ITEMWH11000000004","qrContent":"ITEMWH11000000004"},{"serialValue":1000000005,"instanceCode":"ITEMWH11000000005","qrCodeValue":"ITEMWH11000000005","qrContent":"ITEMWH11000000005"},{"serialValue":1000000006,"instanceCode":"ITEMWH11000000006","qrCodeValue":"ITEMWH11000000006","qrContent":"ITEMWH11000000006"},{"serialValue":1000000007,"instanceCode":"ITEMWH11000000007","qrCodeValue":"ITEMWH11000000007","qrContent":"ITEMWH11000000007"},{"serialValue":1000000008,"instanceCode":"ITEMWH11000000008","qrCodeValue":"ITEMWH11000000008","qrContent":"ITEMWH11000000008"},{"serialValue":1000000009,"instanceCode":"ITEMWH11000000009","qrCodeValue":"ITEMWH11000000009","qrContent":"ITEMWH11000000009"},{"serialValue":1000000010,"instanceCode":"ITEMWH11000000010","qrCodeValue":"ITEMWH11000000010","qrContent":"ITEMWH11000000010"},{"serialValue":1000000011,"instanceCode":"ITEMWH11000000011","qrCodeValue":"ITEMWH11000000011","qrContent":"ITEMWH11000000011"}]}}',1,'','2026-05-25 22:37:25');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2058920404186333186,'物料',0,'com.ruoyi.wms.controller.ItemController.batchPrintQrCode()','POST',1,'admin','研发部门','/wms/item/batchPrintQrCode','0:0:0:0:0:0:0:1','内网IP','{"row":{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2058919797824192514","ids":null,"itemCode":"DN","itemName":"苹果电脑","itemCategory":null,"unit":null,"equipmentName":null,"equipmentType":null,"status":null,"remark":"z","sku":null},"skuId":"2058919798067462146","qrCodeCount":4}','{"code":200,"msg":"操作成功","data":{"itemKey":"ITEMWH1","qrCodeCount":4,"row":{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2058919797824192514","ids":null,"itemCode":"DN","itemName":"苹果电脑","itemCategory":null,"unit":null,"equipmentName":null,"equipmentType":null,"status":null,"remark":"z","sku":null},"details":[{"serialValue":1000000012,"instanceCode":"ITEMWH11000000012","qrCodeValue":"ITEMWH11000000012","qrContent":"ITEMWH11000000012"},{"serialValue":1000000013,"instanceCode":"ITEMWH11000000013","qrCodeValue":"ITEMWH11000000013","qrContent":"ITEMWH11000000013"},{"serialValue":1000000014,"instanceCode":"ITEMWH11000000014","qrCodeValue":"ITEMWH11000000014","qrContent":"ITEMWH11000000014"},{"serialValue":1000000015,"instanceCode":"ITEMWH11000000015","qrCodeValue":"ITEMWH11000000015","qrContent":"ITEMWH11000000015"}]}}',1,'','2026-05-25 22:37:32');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2058920568519163906,'库区',1,'com.ruoyi.wms.controller.AreaController.add()','POST',1,'admin','研发部门','/wms/area','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"areaCode":null,"areaName":"朝C区","warehouseId":"2056769437957435394","status":null,"orderNum":null,"remark":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-25 22:38:11');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2058927018800873474,'货架',1,'com.ruoyi.wms.controller.RackController.add()','POST',1,'admin','研发部门','/wms/rack','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"rackCode":null,"rackName":"朝C货1","warehouseId":"2056769437957435394","areaId":"2058920568263311362","rackStatus":"enabled","rowCount":2,"columnCount":3,"length":"600","width":"600","height":"600","orderNum":0,"remark":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-25 23:03:49');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2058927107212607490,'箱体',1,'com.ruoyi.wms.controller.BoxController.add()','POST',1,'admin','研发部门','/wms/box','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"boxCode":"BOX2058927106524741632","boxName":"ad","boxStatus":"idle","warehouseId":null,"areaId":null,"rackId":null,"locationId":null,"itemCount":null,"remark":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-25 23:04:10');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2058929556761640962,'入库单',1,'com.ruoyi.wms.controller.ReceiptOrderController.add()','POST',1,'admin','研发部门','/wms/receiptOrder','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2058929554794512385","receiptOrderNo":"RKWH12058929554542854144","receiptOrderType":"采购入库","basisNo":"a","dispatchMode":"air","noticeOrg":"a","receiveUnit":"a","purchaseDate":"2026-05-19","receiptDate":"2026-05-25","totalQuantity":"6","payableAmount":"25","receiptOrderStatus":0,"warehouseId":"1828364459110469633","areaId":"2056770261064429569","remark":"aa","details":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2058929555029393409","receiptOrderId":null,"skuId":"2058919798067462146","quantity":"1","itemCode":"DN","itemName":"苹果电脑","skuName":"1T","unit":"台","productIdentifier":"高标识","qualityGrade":"高","unitPrice":"4","lineAmount":"4.00","remark":"a","warehouseId":"1828364459110469633","areaId":"2056770261064429569","rackId":"2056771221887201282","locationId":"2056771221887201283","receiptItemInstances":[{"id":"2058920403737542661","instanceCode":"ITEMWH11000000015","boxCode":"dsfdas","remark":"a"}]},{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2058929555029393410","receiptOrderId":null,"skuId":"2058919798067462146","quantity":"1","itemCode":"DN","itemName":"苹果电脑","skuName":"1T","unit":"台","productIdentifier":"高标识","qualityGrade":"高","unitPrice":"7","lineAmount":"7.00","remark":"a","warehouseId":"1828364459110469633","areaId":"2056770261064429569","rackId":"2056771221887201282","locationId":"2056771221887201283","receiptItemInstances":[{"id":"2058920403737542660","instanceCode":"ITEMWH11000000014","boxCode":"dsfdas","remark":"a"}]},{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2058929555029393411","receiptOrderId":null,"skuId":"2058919798067462146","quantity":"1","itemCode":"DN","itemName":"苹果电脑","skuName":"1T","unit":"台","productIdentifier":"高标识","qualityGrade":"高","unitPrice":"5","lineAmount":"5.00","remark":"a","warehouseId":"1828364459110469633","areaId":"2056770261064429569","rackId":"2056771221887201','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-25 23:13:54');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2058930936721211393,'入库单',2,'com.ruoyi.wms.controller.ReceiptOrderController.doWarehousing()','POST',1,'admin','研发部门','/wms/receiptOrder/warehousing','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2058929554794512385","receiptOrderNo":"RKWH12058929554542854144","receiptOrderType":"采购入库","basisNo":"a","dispatchMode":"air","noticeOrg":"a","receiveUnit":"a","purchaseDate":"2026-05-19","receiptDate":"2026-05-25","totalQuantity":"6","payableAmount":"25","receiptOrderStatus":1,"warehouseId":"1828364459110469633","areaId":"2056770261064429569","remark":"aa","details":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2058929555029393409","receiptOrderId":"2058929554794512385","skuId":"2058919798067462146","quantity":"1","itemCode":"DN","itemName":"苹果电脑","skuName":"1T","unit":"台","productIdentifier":"高标识","qualityGrade":"高","unitPrice":"4","lineAmount":"4.00","remark":"a","warehouseId":"1828364459110469633","areaId":"2056770261064429569","rackId":"2056771221887201282","locationId":"2056771221887201283","receiptItemInstances":[{"id":"2058920403737542661","instanceCode":"ITEMWH11000000015","boxCode":"asdfsd","remark":"a"}]},{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2058929555029393410","receiptOrderId":"2058929554794512385","skuId":"2058919798067462146","quantity":"1","itemCode":"DN","itemName":"苹果电脑","skuName":"1T","unit":"台","productIdentifier":"高标识","qualityGrade":"高","unitPrice":"7","lineAmount":"7.00","remark":"a","warehouseId":"1828364459110469633","areaId":"2056770261064429569","rackId":"2056771221887201282","locationId":"2056771221887201283","receiptItemInstances":[{"id":"2058920403737542660","instanceCode":"ITEMWH11000000014","boxCode":"asdfsd","remark":"a"}]},{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2058929555029393411","receiptOrderId":"2058929554794512385","skuId":"2058919798067462146","quantity":"1","itemCode":"DN","itemName":"苹果电脑","skuName":"1T","unit":"台","productIdentifier":"高标识","qualityGrade":"高","unitPrice":"5","lineAmount":"5.00","remark":"a","warehouseId":"1828364459110469633","areaI','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-25 23:19:23');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2058931143190020098,'出库单',1,'com.ruoyi.wms.controller.ShipmentOrderController.add()','POST',1,'admin','研发部门','/wms/shipmentOrder','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"shipmentOrderNo":"CKWH12058931140954456064","shipmentOrderType":"借用出库","basisNo":"dfas","dispatchMode":"road","noticeOrg":"dsfsdaf","receiveUnit":"sdaf","purchaseDate":"2026-05-24","shipmentDate":"2026-05-25","receivableAmount":"3","totalQuantity":"1","shipmentOrderStatus":0,"warehouseId":"1828364459110469633","areaId":"2056770261064429569","remark":"sdafa","details":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"shipmentOrderId":null,"skuId":"2058919798067462146","quantity":"1","itemCode":"DN","itemName":"苹果电脑","skuName":"1T","unit":"台","productIdentifier":"高标识","qualityGrade":"高","unitPrice":"3","lineAmount":"3.00","warehouseId":"1828364459110469633","areaId":"2056770261064429569","inventoryDetailId":"2058930934737305601","itemInstanceId":"2058920403737542661","boxId":null,"remark":"adfa","key":"1828364459110469633_2056770261064429569_2058919798067462146"}]}','',0,'
### Error updating database.  Cause: cn.com.vastbase.util.PSQLException: ERROR: value too long for type character varying(22)
  在位置：referenced column: shipment_order_no
### The error may exist in com/ruoyi/wms/mapper/ShipmentOrderMapper.java (best guess)
### The error may involve com.ruoyi.wms.mapper.ShipmentOrderMapper.insert-Inline
### The error occurred while setting parameters
### SQL: INSERT INTO wms_shipment_order  ( id, shipment_order_no, shipment_order_type, basis_no, dispatch_mode, notice_org, receive_unit, purchase_date, shipment_date, receivable_amount, total_quantity, shipment_order_status, warehouse_id, area_id, remark, create_by, create_time, update_by, update_time )  VALUES (  ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?  )
### Cause: cn.com.vastbase.util.PSQLException: ERROR: value too long for type character varying(22)
  在位置：referenced column: shipment_order_no
; ERROR: value too long for type character varying(22)
  在位置：referenced column: shipment_order_no','2026-05-25 23:20:12');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2058933304363876354,'出库单',2,'com.ruoyi.wms.controller.ShipmentOrderController.shipment()','PUT',1,'admin','研发部门','/wms/shipmentOrder/shipment','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2058933291508334593","shipmentOrderNo":"CKWH12058933269035253760","shipmentOrderType":"借用出库","basisNo":"dfas","dispatchMode":"road","noticeOrg":"dsfsdaf","receiveUnit":"sdaf","purchaseDate":"2026-05-24","shipmentDate":"2026-05-25","receivableAmount":"3","totalQuantity":"1","shipmentOrderStatus":1,"warehouseId":"1828364459110469633","areaId":"2056770261064429569","remark":"sdafa","details":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2058933291709661185","shipmentOrderId":null,"skuId":"2058919798067462146","quantity":"1","itemCode":"DN","itemName":"苹果电脑","skuName":"1T","unit":"台","productIdentifier":"高标识","qualityGrade":"高","unitPrice":"3","lineAmount":"3.00","warehouseId":"1828364459110469633","areaId":"2056770261064429569","inventoryDetailId":"2058930934737305601","itemInstanceId":"2058920403737542661","boxId":null,"remark":null,"key":"1828364459110469633_2056770261064429569_2058919798067462146"}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-25 23:28:48');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2058933463449632769,'借出登记',1,'com.ruoyi.wms.controller.BorrowRecordController.borrow()','POST',1,'admin','研发部门','/wms/borrowRecord/borrow','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"itemInstanceId":"2058920374624878599","borrowStatus":null,"borrower":"ad","fromUnit":"dfad","toUnit":"fads","fromPerson":"f","toPerson":"dsfdas","docDate":"2026-05-25","borrowNo":null,"planReturnDate":"2026-05-27","overdueFlag":null,"overdueDays":null,"instanceCode":"ITEMWH11000000010","borrowTime":"2026-05-25 23:29:19","returnTime":null,"borrowRemark":"fdaf","returnRemark":null,"originalBoxId":null,"returnedBoxId":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-25 23:29:26');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2058933544445837313,'归还登记',2,'com.ruoyi.wms.controller.BorrowRecordController.returnItem()','POST',1,'admin','研发部门','/wms/borrowRecord/return','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2058933462380085250","itemInstanceId":null,"borrowStatus":null,"borrower":null,"fromUnit":null,"toUnit":null,"fromPerson":null,"toPerson":null,"docDate":null,"borrowNo":null,"planReturnDate":null,"overdueFlag":null,"overdueDays":null,"instanceCode":null,"borrowTime":null,"returnTime":"2026-05-29 00:00:00","borrowRemark":null,"returnRemark":"fads","originalBoxId":null,"returnedBoxId":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-25 23:29:45');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2058933864563507202,'调拨单',1,'com.ruoyi.wms.controller.MovementOrderController.add()','POST',1,'admin','研发部门','/wms/movementOrder','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2058933863678509057","movementOrderNo":"DBWH12058933863439433728","movementType":"通装","dispatchBasis":"fa","dispatchPurpose":"af","dispatchMode":"road","fromUnit":"fadsf","toUnit":"f","fromStation":null,"toStation":"dsfsd","fromAddress":null,"toAddress":null,"contactAddress":"dasfds","dispatchDate":null,"effectiveDate":"2026-06-04","issueDate":"2026-05-28","sourceWarehouseId":"1828364459110469633","sourceAreaId":"2056770261064429569","targetWarehouseId":"2056769387227328513","targetAreaId":"2056770367725580289","movementOrderStatus":0,"totalQuantity":"1","remark":"dsfa","details":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"movementOrderId":null,"skuId":"2058919798067462146","quantity":"1","itemCode":"DN","itemName":"苹果电脑","skuName":"1T","unit":"台","productIdentifier":"高标识","qualityGrade":"高","unitPrice":"7","lineAmount":"7.00","remark":null,"sourceWarehouseId":"1828364459110469633","sourceAreaId":"2056770261064429569","sourceRackId":"2056771221887201282","sourceLocationId":"2056771221887201283","targetWarehouseId":"2056769387227328513","targetAreaId":"2056770367725580289","targetRackId":"2056771350060937218","targetLocationId":"2056771350060937219","inventoryDetailId":"2058930934737305602","itemInstanceId":"2058920403737542660","warehouseId":"1828364459110469633","areaId":"2056770261064429569","key":"1828364459110469633_2056770261064429569_2058919798067462146"}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-25 23:31:01');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2058933939415056386,'调拨单',2,'com.ruoyi.wms.controller.MovementOrderController.move()','POST',1,'admin','研发部门','/wms/movementOrder/move','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2058933863678509057","movementOrderNo":"DBWH12058933863439433728","movementType":"通装","dispatchBasis":"fa","dispatchPurpose":"af","dispatchMode":"road","fromUnit":"fadsf","toUnit":"f","fromStation":null,"toStation":"dsfsd","fromAddress":null,"toAddress":null,"contactAddress":"dasfds","dispatchDate":null,"effectiveDate":"2026-06-04","issueDate":"2026-05-28","sourceWarehouseId":"1828364459110469633","sourceAreaId":"2056770261064429569","targetWarehouseId":"2056769387227328513","targetAreaId":"2056770367725580289","movementOrderStatus":1,"totalQuantity":"1","remark":"dsfa","details":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2058933863925972993","movementOrderId":"2058933863678509057","skuId":"2058919798067462146","quantity":"1","itemCode":"DN","itemName":"苹果电脑","skuName":"1T","unit":"台","productIdentifier":"高标识","qualityGrade":"高","unitPrice":"7","lineAmount":"7.00","remark":null,"sourceWarehouseId":"1828364459110469633","sourceAreaId":"2056770261064429569","sourceRackId":"2056771221887201282","sourceLocationId":"2056771221887201283","targetWarehouseId":"2056769387227328513","targetAreaId":"2056770367725580289","targetRackId":null,"targetLocationId":null,"inventoryDetailId":"2058930934737305602","itemInstanceId":"2058920403737542660","warehouseId":"1828364459110469633","areaId":"2056770261064429569","key":"1828364459110469633_2056770261064429569_2058919798067462146"}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-25 23:31:19');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2058935452858351618,'调拨单',2,'com.ruoyi.wms.controller.MovementOrderController.move()','POST',1,'admin','研发部门','/wms/movementOrder/move','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":"2058935449213501441","movementOrderNo":"DBWH12058935449062506496","movementType":"通装","dispatchBasis":"fasd","dispatchPurpose":"fasd","dispatchMode":"road","fromUnit":"sfasd","toUnit":"fads","fromStation":null,"toStation":"sad","fromAddress":null,"toAddress":null,"contactAddress":"fad","dispatchDate":null,"effectiveDate":"2026-05-29","issueDate":"2026-06-05","sourceWarehouseId":"1828364459110469633","sourceAreaId":"2056770261064429569","targetWarehouseId":"2056769387227328513","targetAreaId":"2056770392228704258","movementOrderStatus":1,"totalQuantity":"1","remark":"fads","details":[{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"movementOrderId":null,"skuId":"2058919798067462147","quantity":"1","itemCode":"DN","itemName":"苹果电脑","skuName":"2T","unit":"台","productIdentifier":"低标识","qualityGrade":"低","unitPrice":"2","lineAmount":"2.00","remark":null,"sourceWarehouseId":"1828364459110469633","sourceAreaId":"2056770261064429569","sourceRackId":"2056771221887201282","sourceLocationId":"2056771221887201284","targetWarehouseId":"2056769387227328513","targetAreaId":"2056770392228704258","targetRackId":null,"targetLocationId":null,"inventoryDetailId":"2058930934737305606","itemInstanceId":"2058920374624878597","warehouseId":"1828364459110469633","areaId":"2056770261064429569","key":"1828364459110469633_2056770261064429569_2058919798067462147"}]}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-25 23:37:20');
INSERT INTO wms_advence_master.sys_oper_log (oper_id,title,business_type,method,request_method,operator_type,oper_name,dept_name,oper_url,oper_ip,oper_location,oper_param,json_result,status,error_msg,oper_time)
 VALUES (2058937017069502466,'借出登记',1,'com.ruoyi.wms.controller.BorrowRecordController.borrow()','POST',1,'admin','研发部门','/wms/borrowRecord/borrow','0:0:0:0:0:0:0:1','内网IP','{"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"id":null,"itemInstanceId":"2058920374624878598","borrowStatus":null,"borrower":"a","fromUnit":"f","toUnit":"dfa","fromPerson":"ad","toPerson":"dfd","docDate":"2026-05-29","borrowNo":null,"planReturnDate":"2026-05-28","overdueFlag":null,"overdueDays":null,"instanceCode":"ITEMWH11000000009","borrowTime":"2026-05-25 23:43:21","returnTime":null,"borrowRemark":"fdf","returnRemark":null,"originalBoxId":null,"returnedBoxId":null}','{"code":200,"msg":"操作成功","data":null}',1,'','2026-05-25 23:43:33');


-- Name: sys_oss; Type: Table; Schema: wms_advence_master;

SET search_path = wms_advence_master;
CREATE TABLE sys_oss (
    oss_id bigint NOT NULL,
    file_name varchar(255) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar NOT NULL,
    original_name varchar(255) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar NOT NULL,
    file_suffix varchar(10) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar NOT NULL,
    url varchar(500) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci NOT NULL,
    create_time timestamp(0) without time zone DEFAULT NULL::timestamp without time zone,
    create_by varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar,
    update_time timestamp(0) without time zone DEFAULT NULL::timestamp without time zone,
    update_by varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar,
    service varchar(20) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT 'minio'::varchar NOT NULL
)
CHARACTER SET = "UTF8" COLLATE = "utf8mb4_general_ci"
WITH (orientation=row, compression=no, fillfactor=80);
COMMENT ON TABLE sys_oss IS 'OSS对象存储表';
COMMENT ON COLUMN sys_oss.oss_id IS '对象存储主键';
COMMENT ON COLUMN sys_oss.file_name IS '文件名';
COMMENT ON COLUMN sys_oss.original_name IS '原名';
COMMENT ON COLUMN sys_oss.file_suffix IS '文件后缀名';
COMMENT ON COLUMN sys_oss.url IS 'URL地址';
COMMENT ON COLUMN sys_oss.create_time IS '创建时间';
COMMENT ON COLUMN sys_oss.create_by IS '上传人';
COMMENT ON COLUMN sys_oss.update_time IS '更新时间';
COMMENT ON COLUMN sys_oss.update_by IS '更新人';
COMMENT ON COLUMN sys_oss.service IS '服务商';
ALTER TABLE sys_oss ADD CONSTRAINT sys_oss_pk PRIMARY KEY USING btree  (oss_id);

--Data for  Name: sys_oss; Type: Table; Schema: wms_advence_master;



-- Name: sys_oss_config; Type: Table; Schema: wms_advence_master;

SET search_path = wms_advence_master;
CREATE TABLE sys_oss_config (
    oss_config_id bigint NOT NULL,
    config_key varchar(20) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar NOT NULL,
    access_key varchar(255) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar,
    secret_key varchar(255) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar,
    bucket_name varchar(255) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar,
    prefix varchar(255) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar,
    endpoint varchar(255) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar,
    domain varchar(255) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar,
    is_https character(1) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT 'N'::bpchar,
    region varchar(255) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar,
    access_policy character(1) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT '1'::bpchar NOT NULL,
    status character(1) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT '1'::bpchar,
    ext1 varchar(255) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar,
    create_by varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar,
    create_time timestamp(0) without time zone DEFAULT NULL::timestamp without time zone,
    update_by varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar,
    update_time timestamp(0) without time zone DEFAULT NULL::timestamp without time zone,
    remark varchar(500) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar
)
CHARACTER SET = "UTF8" COLLATE = "utf8mb4_general_ci"
WITH (orientation=row, compression=no, fillfactor=80);
COMMENT ON TABLE sys_oss_config IS '对象存储配置表';
COMMENT ON COLUMN sys_oss_config.oss_config_id IS '主建';
COMMENT ON COLUMN sys_oss_config.config_key IS '配置key';
COMMENT ON COLUMN sys_oss_config.access_key IS 'accessKey';
COMMENT ON COLUMN sys_oss_config.secret_key IS '秘钥';
COMMENT ON COLUMN sys_oss_config.bucket_name IS '桶名称';
COMMENT ON COLUMN sys_oss_config.prefix IS '前缀';
COMMENT ON COLUMN sys_oss_config.endpoint IS '访问站点';
COMMENT ON COLUMN sys_oss_config.domain IS '自定义域名';
COMMENT ON COLUMN sys_oss_config.is_https IS '是否https（Y=是,N=否）';
COMMENT ON COLUMN sys_oss_config.region IS '域';
COMMENT ON COLUMN sys_oss_config.access_policy IS '桶权限类型(0=private 1=public 2=custom)';
COMMENT ON COLUMN sys_oss_config.status IS '是否默认（0=是,1=否）';
COMMENT ON COLUMN sys_oss_config.ext1 IS '扩展字段';
COMMENT ON COLUMN sys_oss_config.create_by IS '创建者';
COMMENT ON COLUMN sys_oss_config.create_time IS '创建时间';
COMMENT ON COLUMN sys_oss_config.update_by IS '更新者';
COMMENT ON COLUMN sys_oss_config.update_time IS '更新时间';
COMMENT ON COLUMN sys_oss_config.remark IS '备注';
ALTER TABLE sys_oss_config ADD CONSTRAINT sys_oss_config_pk PRIMARY KEY USING btree  (oss_config_id);

--Data for  Name: sys_oss_config; Type: Table; Schema: wms_advence_master;

INSERT INTO wms_advence_master.sys_oss_config (oss_config_id,config_key,access_key,secret_key,bucket_name,prefix,endpoint,domain,is_https,region,access_policy,status,ext1,create_by,create_time,update_by,update_time,remark)
 VALUES (1,'minio','ruoyi','ruoyi123','ruoyi','','127.0.0.1:9000','','N','','1','0','','admin','2024-06-13 16:06:38','admin','2024-08-16 16:48:05',null);
INSERT INTO wms_advence_master.sys_oss_config (oss_config_id,config_key,access_key,secret_key,bucket_name,prefix,endpoint,domain,is_https,region,access_policy,status,ext1,create_by,create_time,update_by,update_time,remark)
 VALUES (2,'qiniu','XXXXXXXXXXXXXXX','XXXXXXXXXXXXXXX','ruoyi','','s3-cn-north-1.qiniucs.com','','N','','1','0','','admin','2024-06-13 16:06:38','admin','2024-06-13 16:06:38',null);
INSERT INTO wms_advence_master.sys_oss_config (oss_config_id,config_key,access_key,secret_key,bucket_name,prefix,endpoint,domain,is_https,region,access_policy,status,ext1,create_by,create_time,update_by,update_time,remark)
 VALUES (3,'aliyun','XXXXXXXXXXXXXXX','XXXXXXXXXXXXXXX','ruoyi','','oss-cn-beijing.aliyuncs.com','','N','','1','0','','admin','2024-06-13 16:06:38','admin','2024-07-10 17:50:41',null);
INSERT INTO wms_advence_master.sys_oss_config (oss_config_id,config_key,access_key,secret_key,bucket_name,prefix,endpoint,domain,is_https,region,access_policy,status,ext1,create_by,create_time,update_by,update_time,remark)
 VALUES (4,'qcloud','XXXXXXXXXXXXXXX','XXXXXXXXXXXXXXX','ruoyi-1250000000','','cos.ap-beijing.myqcloud.com','','N','ap-beijing','1','0','','admin','2024-06-13 16:06:38','admin','2024-06-13 16:06:38',null);
INSERT INTO wms_advence_master.sys_oss_config (oss_config_id,config_key,access_key,secret_key,bucket_name,prefix,endpoint,domain,is_https,region,access_policy,status,ext1,create_by,create_time,update_by,update_time,remark)
 VALUES (5,'image','ruoyi','ruoyi123','ruoyi','image','127.0.0.1:9000','','N','','1','0','','admin','2024-06-13 16:06:38','admin','2024-06-13 16:06:38',null);


-- Name: sys_post; Type: Table; Schema: wms_advence_master;

SET search_path = wms_advence_master;
CREATE TABLE sys_post (
    post_id bigint NOT NULL,
    post_code varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci NOT NULL,
    post_name varchar(50) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci NOT NULL,
    post_sort integer NOT NULL,
    status character(1) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci NOT NULL,
    create_by varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar,
    create_time timestamp(0) without time zone DEFAULT NULL::timestamp without time zone,
    update_by varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar,
    update_time timestamp(0) without time zone DEFAULT NULL::timestamp without time zone,
    remark varchar(500) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar
)
CHARACTER SET = "UTF8" COLLATE = "utf8mb4_general_ci"
WITH (orientation=row, compression=no, fillfactor=80);
COMMENT ON TABLE sys_post IS '岗位信息表';
COMMENT ON COLUMN sys_post.post_id IS '岗位ID';
COMMENT ON COLUMN sys_post.post_code IS '岗位编码';
COMMENT ON COLUMN sys_post.post_name IS '岗位名称';
COMMENT ON COLUMN sys_post.post_sort IS '显示顺序';
COMMENT ON COLUMN sys_post.status IS '状态（0正常 1停用）';
COMMENT ON COLUMN sys_post.create_by IS '创建者';
COMMENT ON COLUMN sys_post.create_time IS '创建时间';
COMMENT ON COLUMN sys_post.update_by IS '更新者';
COMMENT ON COLUMN sys_post.update_time IS '更新时间';
COMMENT ON COLUMN sys_post.remark IS '备注';
ALTER TABLE sys_post ADD CONSTRAINT sys_post_pk PRIMARY KEY USING btree  (post_id);

--Data for  Name: sys_post; Type: Table; Schema: wms_advence_master;

INSERT INTO wms_advence_master.sys_post (post_id,post_code,post_name,post_sort,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1,'ceo','董事长',1,'1','admin','2024-06-13 16:06:25','',null,'');
INSERT INTO wms_advence_master.sys_post (post_id,post_code,post_name,post_sort,status,create_by,create_time,update_by,update_time,remark)
 VALUES (2,'se','项目经理',2,'1','admin','2024-06-13 16:06:25','',null,'');
INSERT INTO wms_advence_master.sys_post (post_id,post_code,post_name,post_sort,status,create_by,create_time,update_by,update_time,remark)
 VALUES (3,'hr','人力资源',3,'1','admin','2024-06-13 16:06:25','',null,'');
INSERT INTO wms_advence_master.sys_post (post_id,post_code,post_name,post_sort,status,create_by,create_time,update_by,update_time,remark)
 VALUES (4,'user','普通员工',4,'1','admin','2024-06-13 16:06:25','',null,'');
INSERT INTO wms_advence_master.sys_post (post_id,post_code,post_name,post_sort,status,create_by,create_time,update_by,update_time,remark)
 VALUES (1811656351757385729,'caiwu8989','财务',5,'1','admin','2024-07-12 22:58:28','admin','2024-07-12 14:58:38',null);


-- Name: sys_role; Type: Table; Schema: wms_advence_master;

SET search_path = wms_advence_master;
CREATE TABLE sys_role (
    role_id bigint NOT NULL,
    role_name varchar(30) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci NOT NULL,
    role_key varchar(100) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci NOT NULL,
    role_sort integer NOT NULL,
    data_scope character(1) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT '1'::bpchar,
    menu_check_strictly smallint DEFAULT 1::smallint,
    dept_check_strictly smallint DEFAULT 1::smallint,
    status character(1) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci NOT NULL,
    del_flag character(1) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT '0'::bpchar,
    create_by varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar,
    create_time timestamp(0) without time zone DEFAULT NULL::timestamp without time zone,
    update_by varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar,
    update_time timestamp(0) without time zone DEFAULT NULL::timestamp without time zone,
    remark varchar(500) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar
)
CHARACTER SET = "UTF8" COLLATE = "utf8mb4_general_ci"
WITH (orientation=row, compression=no, fillfactor=80);
COMMENT ON TABLE sys_role IS '角色信息表';
COMMENT ON COLUMN sys_role.role_id IS '角色ID';
COMMENT ON COLUMN sys_role.role_name IS '角色名称';
COMMENT ON COLUMN sys_role.role_key IS '角色权限字符串';
COMMENT ON COLUMN sys_role.role_sort IS '显示顺序';
COMMENT ON COLUMN sys_role.data_scope IS '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）';
COMMENT ON COLUMN sys_role.menu_check_strictly IS '菜单树选择项是否关联显示';
COMMENT ON COLUMN sys_role.dept_check_strictly IS '部门树选择项是否关联显示';
COMMENT ON COLUMN sys_role.status IS '角色状态（0正常 1停用）';
COMMENT ON COLUMN sys_role.del_flag IS '删除标志（0代表存在 2代表删除）';
COMMENT ON COLUMN sys_role.create_by IS '创建者';
COMMENT ON COLUMN sys_role.create_time IS '创建时间';
COMMENT ON COLUMN sys_role.update_by IS '更新者';
COMMENT ON COLUMN sys_role.update_time IS '更新时间';
COMMENT ON COLUMN sys_role.remark IS '备注';
ALTER TABLE sys_role ADD CONSTRAINT sys_role_pk PRIMARY KEY USING btree  (role_id);

--Data for  Name: sys_role; Type: Table; Schema: wms_advence_master;

INSERT INTO wms_advence_master.sys_role (role_id,role_name,role_key,role_sort,data_scope,menu_check_strictly,dept_check_strictly,status,del_flag,create_by,create_time,update_by,update_time,remark)
 VALUES (1,'超级管理员','admin',1,'1',1,1,'1','0','admin','2024-06-13 16:06:26','',null,'超级管理员');
INSERT INTO wms_advence_master.sys_role (role_id,role_name,role_key,role_sort,data_scope,menu_check_strictly,dept_check_strictly,status,del_flag,create_by,create_time,update_by,update_time,remark)
 VALUES (2,'普通角色','common',2,'2',1,1,'1','1','admin','2024-06-13 16:06:26','admin','2024-07-10 17:13:05','普通角色');
INSERT INTO wms_advence_master.sys_role (role_id,role_name,role_key,role_sort,data_scope,menu_check_strictly,dept_check_strictly,status,del_flag,create_by,create_time,update_by,update_time,remark)
 VALUES (1811607750859661314,'测试角色1','test1',2,'1',1,1,'1','1','admin','2024-07-12 11:45:21','admin','2024-07-12 11:45:21',null);
INSERT INTO wms_advence_master.sys_role (role_id,role_name,role_key,role_sort,data_scope,menu_check_strictly,dept_check_strictly,status,del_flag,create_by,create_time,update_by,update_time,remark)
 VALUES (1811629311809396737,'测试角色2','test2',3,'1',1,1,'1','1','admin','2024-07-12 13:11:01','admin','2024-07-12 13:11:01',null);
INSERT INTO wms_advence_master.sys_role (role_id,role_name,role_key,role_sort,data_scope,menu_check_strictly,dept_check_strictly,status,del_flag,create_by,create_time,update_by,update_time,remark)
 VALUES (1829105952432427010,'试用','trier',0,'1',1,1,'1','0','admin','2024-08-29 18:36:57','admin','2024-08-30 10:51:57',null);


-- Name: sys_role_dept; Type: Table; Schema: wms_advence_master;

SET search_path = wms_advence_master;
CREATE TABLE sys_role_dept (
    role_id bigint NOT NULL,
    dept_id bigint NOT NULL
)
CHARACTER SET = "UTF8" COLLATE = "utf8mb4_general_ci"
WITH (orientation=row, compression=no, fillfactor=80);
COMMENT ON TABLE sys_role_dept IS '角色和部门关联表';
COMMENT ON COLUMN sys_role_dept.role_id IS '角色ID';
COMMENT ON COLUMN sys_role_dept.dept_id IS '部门ID';
ALTER TABLE sys_role_dept ADD CONSTRAINT sys_role_dept_pk PRIMARY KEY USING btree  (role_id, dept_id);

--Data for  Name: sys_role_dept; Type: Table; Schema: wms_advence_master;



-- Name: sys_role_menu; Type: Table; Schema: wms_advence_master;

SET search_path = wms_advence_master;
CREATE TABLE sys_role_menu (
    role_id bigint NOT NULL,
    menu_id bigint NOT NULL
)
CHARACTER SET = "UTF8" COLLATE = "utf8mb4_general_ci"
WITH (orientation=row, compression=no, fillfactor=80);
COMMENT ON TABLE sys_role_menu IS '角色和菜单关联表';
COMMENT ON COLUMN sys_role_menu.role_id IS '角色ID';
COMMENT ON COLUMN sys_role_menu.menu_id IS '菜单ID';
ALTER TABLE sys_role_menu ADD CONSTRAINT sys_role_menu_pk PRIMARY KEY USING btree  (role_id, menu_id);

--Data for  Name: sys_role_menu; Type: Table; Schema: wms_advence_master;

INSERT INTO wms_advence_master.sys_role_menu (role_id,menu_id)
 VALUES (1829105952432427010,1808758090157985794);
INSERT INTO wms_advence_master.sys_role_menu (role_id,menu_id)
 VALUES (1829105952432427010,1809059968309743618);
INSERT INTO wms_advence_master.sys_role_menu (role_id,menu_id)
 VALUES (1829105952432427010,1809059968309743619);
INSERT INTO wms_advence_master.sys_role_menu (role_id,menu_id)
 VALUES (1829105952432427010,1813458070128599041);
INSERT INTO wms_advence_master.sys_role_menu (role_id,menu_id)
 VALUES (1829105952432427010,1813820131794837506);
INSERT INTO wms_advence_master.sys_role_menu (role_id,menu_id)
 VALUES (1829105952432427010,1815207165755183105);
INSERT INTO wms_advence_master.sys_role_menu (role_id,menu_id)
 VALUES (1829105952432427010,1818466281474822145);
INSERT INTO wms_advence_master.sys_role_menu (role_id,menu_id)
 VALUES (1829105952432427010,1818854933803638785);
INSERT INTO wms_advence_master.sys_role_menu (role_id,menu_id)
 VALUES (1829105952432427010,1818855673632727042);
INSERT INTO wms_advence_master.sys_role_menu (role_id,menu_id)
 VALUES (1829105952432427010,1820729144067321858);
INSERT INTO wms_advence_master.sys_role_menu (role_id,menu_id)
 VALUES (1829105952432427010,1821075355068559361);
INSERT INTO wms_advence_master.sys_role_menu (role_id,menu_id)
 VALUES (1829105952432427010,1822862323595145218);
INSERT INTO wms_advence_master.sys_role_menu (role_id,menu_id)
 VALUES (1829105952432427010,1823187248797270018);
INSERT INTO wms_advence_master.sys_role_menu (role_id,menu_id)
 VALUES (1829105952432427010,1823190638784757762);
INSERT INTO wms_advence_master.sys_role_menu (role_id,menu_id)
 VALUES (1829105952432427010,1825769009480142850);
INSERT INTO wms_advence_master.sys_role_menu (role_id,menu_id)
 VALUES (1829105952432427010,1829349433573822466);
INSERT INTO wms_advence_master.sys_role_menu (role_id,menu_id)
 VALUES (1829105952432427010,1829351081448755202);


-- Name: sys_user; Type: Table; Schema: wms_advence_master;

SET search_path = wms_advence_master;
CREATE TABLE sys_user (
    user_id bigint NOT NULL,
    dept_id bigint,
    user_name varchar(30) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci NOT NULL,
    nick_name varchar(30) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci NOT NULL,
    user_type varchar(10) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT 'sys_user'::varchar,
    email varchar(50) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar,
    phonenumber varchar(11) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar,
    sex character(1) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT '0'::bpchar,
    avatar varchar(100) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar,
    password varchar(100) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar,
    status character(1) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT '0'::bpchar,
    del_flag character(1) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT '0'::bpchar,
    login_ip varchar(128) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar,
    login_date timestamp(0) without time zone DEFAULT NULL::timestamp without time zone,
    create_by varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar,
    create_time timestamp(0) without time zone DEFAULT NULL::timestamp without time zone,
    update_by varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar,
    update_time timestamp(0) without time zone DEFAULT NULL::timestamp without time zone,
    remark varchar(500) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar
)
CHARACTER SET = "UTF8" COLLATE = "utf8mb4_general_ci"
WITH (orientation=row, compression=no, fillfactor=80);
COMMENT ON TABLE sys_user IS '用户信息表';
COMMENT ON COLUMN sys_user.user_id IS '用户ID';
COMMENT ON COLUMN sys_user.dept_id IS '部门ID';
COMMENT ON COLUMN sys_user.user_name IS '用户账号';
COMMENT ON COLUMN sys_user.nick_name IS '用户昵称';
COMMENT ON COLUMN sys_user.user_type IS '用户类型（sys_user系统用户）';
COMMENT ON COLUMN sys_user.email IS '用户邮箱';
COMMENT ON COLUMN sys_user.phonenumber IS '手机号码';
COMMENT ON COLUMN sys_user.sex IS '用户性别（0男 1女 2未知）';
COMMENT ON COLUMN sys_user.avatar IS '头像地址';
COMMENT ON COLUMN sys_user.password IS '密码';
COMMENT ON COLUMN sys_user.status IS '帐号状态（0正常 1停用）';
COMMENT ON COLUMN sys_user.del_flag IS '删除标志（0代表存在 2代表删除）';
COMMENT ON COLUMN sys_user.login_ip IS '最后登录IP';
COMMENT ON COLUMN sys_user.login_date IS '最后登录时间';
COMMENT ON COLUMN sys_user.create_by IS '创建者';
COMMENT ON COLUMN sys_user.create_time IS '创建时间';
COMMENT ON COLUMN sys_user.update_by IS '更新者';
COMMENT ON COLUMN sys_user.update_time IS '更新时间';
COMMENT ON COLUMN sys_user.remark IS '备注';
ALTER TABLE sys_user ADD CONSTRAINT sys_user_pk PRIMARY KEY USING btree  (user_id);

--Data for  Name: sys_user; Type: Table; Schema: wms_advence_master;

INSERT INTO wms_advence_master.sys_user (user_id,dept_id,user_name,nick_name,user_type,email,phonenumber,sex,avatar,password,status,del_flag,login_ip,login_date,create_by,create_time,update_by,update_time,remark)
 VALUES (1829105396288688129,105,'kucun','kucun','sys_user','','','0','','$2a$10$jpbgHXxmB9nszkvhixjaQuwQtXcq7XJrqFaFpev/93WvaWa/oEpGy','1','0','127.0.0.1','2024-08-30 13:54:01','admin','2024-08-29 18:34:44','kucun','2024-08-30 13:54:01',null);
INSERT INTO wms_advence_master.sys_user (user_id,dept_id,user_name,nick_name,user_type,email,phonenumber,sex,avatar,password,status,del_flag,login_ip,login_date,create_by,create_time,update_by,update_time,remark)
 VALUES (1,103,'admin','系统管理员','sys_user','zccbbg@qq.com','18888888888','0','','$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2','1','0','0:0:0:0:0:0:0:1','2026-05-25 22:16:58','admin','2024-06-13 16:06:25','admin','2026-05-25 22:16:58','管理员');


-- Name: sys_user_post; Type: Table; Schema: wms_advence_master;

SET search_path = wms_advence_master;
CREATE TABLE sys_user_post (
    user_id bigint NOT NULL,
    post_id bigint NOT NULL
)
CHARACTER SET = "UTF8" COLLATE = "utf8mb4_general_ci"
WITH (orientation=row, compression=no, fillfactor=80);
COMMENT ON TABLE sys_user_post IS '用户与岗位关联表';
COMMENT ON COLUMN sys_user_post.user_id IS '用户ID';
COMMENT ON COLUMN sys_user_post.post_id IS '岗位ID';
ALTER TABLE sys_user_post ADD CONSTRAINT sys_user_post_pk PRIMARY KEY USING btree  (user_id, post_id);

--Data for  Name: sys_user_post; Type: Table; Schema: wms_advence_master;

INSERT INTO wms_advence_master.sys_user_post (user_id,post_id)
 VALUES (1,1);


-- Name: sys_user_role; Type: Table; Schema: wms_advence_master;

SET search_path = wms_advence_master;
CREATE TABLE sys_user_role (
    user_id bigint NOT NULL,
    role_id bigint NOT NULL
)
CHARACTER SET = "UTF8" COLLATE = "utf8mb4_general_ci"
WITH (orientation=row, compression=no, fillfactor=80);
COMMENT ON TABLE sys_user_role IS '用户和角色关联表';
COMMENT ON COLUMN sys_user_role.user_id IS '用户ID';
COMMENT ON COLUMN sys_user_role.role_id IS '角色ID';
ALTER TABLE sys_user_role ADD CONSTRAINT sys_user_role_pk PRIMARY KEY USING btree  (user_id, role_id);

--Data for  Name: sys_user_role; Type: Table; Schema: wms_advence_master;

INSERT INTO wms_advence_master.sys_user_role (user_id,role_id)
 VALUES (1,1);
INSERT INTO wms_advence_master.sys_user_role (user_id,role_id)
 VALUES (1829105396288688129,1829105952432427010);


-- Name: wms_area; Type: Table; Schema: wms_advence_master;

SET search_path = wms_advence_master;
CREATE TABLE wms_area (
    id bigint AUTO_INCREMENT NOT NULL,
    area_code varchar(20) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    area_name varchar(60) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci NOT NULL,
    warehouse_id bigint NOT NULL,
    status varchar(32) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    order_num bigint,
    remark varchar(255) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    create_by varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    create_time timestamp(3) without time zone DEFAULT NULL::timestamp without time zone,
    update_by varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    update_time timestamp(3) without time zone DEFAULT NULL::timestamp without time zone,
    CONSTRAINT wms_area_pk PRIMARY KEY (id)
) AUTO_INCREMENT = 2058920568263311363
CHARACTER SET = "UTF8" COLLATE = "utf8mb4_general_ci"
WITH (orientation=row, compression=no, fillfactor=80);
COMMENT ON TABLE wms_area IS '库区';
COMMENT ON COLUMN wms_area.area_code IS '库区编码';
COMMENT ON COLUMN wms_area.area_name IS '库区名称';
COMMENT ON COLUMN wms_area.warehouse_id IS '所属仓库ID';
COMMENT ON COLUMN wms_area.status IS '启用状态';
COMMENT ON COLUMN wms_area.order_num IS '排序';
COMMENT ON COLUMN wms_area.remark IS '备注';
COMMENT ON COLUMN wms_area.create_by IS '创建人';
COMMENT ON COLUMN wms_area.create_time IS '创建时间';
COMMENT ON COLUMN wms_area.update_by IS '修改人';
COMMENT ON COLUMN wms_area.update_time IS '修改时间';
CREATE INDEX tb0_wms_area_idx_wms_area_status ON wms_advence_master.wms_area USING btree (status) TABLESPACE pg_default;
CREATE INDEX tb0_wms_area_idx_wms_area_warehouse_id ON wms_advence_master.wms_area USING btree (warehouse_id) TABLESPACE pg_default;
ALTER TABLE wms_area ADD CONSTRAINT wms_area_uk1 UNIQUE USING btree (warehouse_id, area_code);

--Data for  Name: wms_area; Type: Table; Schema: wms_advence_master;

INSERT INTO wms_advence_master.wms_area (id,area_code,area_name,warehouse_id,status,order_num,remark,create_by,create_time,update_by,update_time)
 VALUES (2056770222548135937,null,'京A区',1828364459110469633,null,null,null,'admin','2026-05-20 00:13:28.665','admin','2026-05-20 00:15:31.883');
INSERT INTO wms_advence_master.wms_area (id,area_code,area_name,warehouse_id,status,order_num,remark,create_by,create_time,update_by,update_time)
 VALUES (2056770261064429569,null,'京B区',1828364459110469633,null,null,null,'admin','2026-05-20 00:13:37.849','admin','2026-05-20 00:15:27.078');
INSERT INTO wms_advence_master.wms_area (id,area_code,area_name,warehouse_id,status,order_num,remark,create_by,create_time,update_by,update_time)
 VALUES (2056770367725580289,null,'海A区',2056769387227328513,null,null,null,'admin','2026-05-20 00:14:03.277','admin','2026-05-20 00:14:03.277');
INSERT INTO wms_advence_master.wms_area (id,area_code,area_name,warehouse_id,status,order_num,remark,create_by,create_time,update_by,update_time)
 VALUES (2056770392228704258,null,'海B区',2056769387227328513,null,null,null,'admin','2026-05-20 00:14:09.122','admin','2026-05-20 00:14:09.122');
INSERT INTO wms_advence_master.wms_area (id,area_code,area_name,warehouse_id,status,order_num,remark,create_by,create_time,update_by,update_time)
 VALUES (2056770428895309825,null,'朝A区',2056769437957435394,null,null,null,'admin','2026-05-20 00:14:17.861','admin','2026-05-20 00:14:17.861');
INSERT INTO wms_advence_master.wms_area (id,area_code,area_name,warehouse_id,status,order_num,remark,create_by,create_time,update_by,update_time)
 VALUES (2056770456179257345,null,'朝B区',2056769437957435394,null,null,null,'admin','2026-05-20 00:14:24.373','admin','2026-05-20 00:14:24.373');
INSERT INTO wms_advence_master.wms_area (id,area_code,area_name,warehouse_id,status,order_num,remark,create_by,create_time,update_by,update_time)
 VALUES (2056770508046020609,null,'东A区',2056769466831024130,null,null,null,'admin','2026-05-20 00:14:36.736','admin','2026-05-20 00:14:36.736');
INSERT INTO wms_advence_master.wms_area (id,area_code,area_name,warehouse_id,status,order_num,remark,create_by,create_time,update_by,update_time)
 VALUES (2056770538748325889,null,'东B区',2056769466831024130,null,null,null,'admin','2026-05-20 00:14:44.052','admin','2026-05-20 00:14:44.052');
INSERT INTO wms_advence_master.wms_area (id,area_code,area_name,warehouse_id,status,order_num,remark,create_by,create_time,update_by,update_time)
 VALUES (2056770584306855938,null,'西A区',2056769490864386049,null,null,null,'admin','2026-05-20 00:14:54.914','admin','2026-05-20 00:14:54.914');
INSERT INTO wms_advence_master.wms_area (id,area_code,area_name,warehouse_id,status,order_num,remark,create_by,create_time,update_by,update_time)
 VALUES (2056770610286374914,null,'西B区',2056769490864386049,null,null,null,'admin','2026-05-20 00:15:01.110','admin','2026-05-20 00:15:01.110');
INSERT INTO wms_advence_master.wms_area (id,area_code,area_name,warehouse_id,status,order_num,remark,create_by,create_time,update_by,update_time)
 VALUES (2056770643404599297,null,'大A区',2056769648226283522,null,null,null,'admin','2026-05-20 00:15:09.002','admin','2026-05-20 00:15:09.002');
INSERT INTO wms_advence_master.wms_area (id,area_code,area_name,warehouse_id,status,order_num,remark,create_by,create_time,update_by,update_time)
 VALUES (2056770672827641857,null,'大B区',2056769648226283522,null,null,null,'admin','2026-05-20 00:15:16.011','admin','2026-05-20 00:15:16.011');
INSERT INTO wms_advence_master.wms_area (id,area_code,area_name,warehouse_id,status,order_num,remark,create_by,create_time,update_by,update_time)
 VALUES (2058920568263311362,null,'朝C区',2056769437957435394,null,null,null,'admin','2026-05-25 22:38:11.035','admin','2026-05-25 22:38:11.035');


-- Name: wms_borrow_record; Type: Table; Schema: wms_advence_master;

SET search_path = wms_advence_master;
CREATE TABLE wms_borrow_record (
    id bigint AUTO_INCREMENT NOT NULL,
    item_instance_id bigint NOT NULL,
    borrow_status varchar(32) CHARACTER SET "UTF8" COLLATE utf8mb4_0900_ai_ci NOT NULL,
    borrower varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_0900_ai_ci NOT NULL,
    from_unit varchar(128) CHARACTER SET "UTF8" COLLATE utf8mb4_0900_ai_ci DEFAULT NULL::varchar,
    to_unit varchar(128) CHARACTER SET "UTF8" COLLATE utf8mb4_0900_ai_ci DEFAULT NULL::varchar,
    from_person varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_0900_ai_ci DEFAULT NULL::varchar,
    to_person varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_0900_ai_ci DEFAULT NULL::varchar,
    doc_date date,
    borrow_no varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_0900_ai_ci DEFAULT NULL::varchar,
    plan_return_date date,
    overdue_flag smallint DEFAULT 0::smallint,
    overdue_days integer DEFAULT 0,
    instance_code varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_0900_ai_ci DEFAULT NULL::varchar,
    borrow_time timestamp(3) without time zone NOT NULL,
    return_time timestamp(3) without time zone DEFAULT NULL::timestamp without time zone,
    borrow_remark varchar(255) CHARACTER SET "UTF8" COLLATE utf8mb4_0900_ai_ci DEFAULT NULL::varchar,
    return_remark varchar(255) CHARACTER SET "UTF8" COLLATE utf8mb4_0900_ai_ci DEFAULT NULL::varchar,
    original_warehouse_id bigint,
    original_area_id bigint,
    original_rack_id bigint,
    original_location_id bigint,
    original_box_id bigint,
    returned_warehouse_id bigint,
    returned_area_id bigint,
    returned_rack_id bigint,
    returned_location_id bigint,
    returned_box_id bigint,
    create_by varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_0900_ai_ci DEFAULT NULL::varchar,
    create_time timestamp(3) without time zone DEFAULT NULL::timestamp without time zone,
    update_by varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_0900_ai_ci DEFAULT NULL::varchar,
    update_time timestamp(3) without time zone DEFAULT NULL::timestamp without time zone,
    CONSTRAINT wms_borrow_record_pk PRIMARY KEY (id)
) AUTO_INCREMENT = 2058937016222253059
CHARACTER SET = "UTF8" COLLATE = "utf8mb4_0900_ai_ci"
WITH (orientation=row, compression=no, fillfactor=80);
COMMENT ON TABLE wms_borrow_record IS '借还记录';
COMMENT ON COLUMN wms_borrow_record.item_instance_id IS '器材实例ID';
COMMENT ON COLUMN wms_borrow_record.borrow_status IS '借还状态';
COMMENT ON COLUMN wms_borrow_record.borrower IS '借用人';
COMMENT ON COLUMN wms_borrow_record.from_unit IS '发货单位';
COMMENT ON COLUMN wms_borrow_record.to_unit IS '收货单位';
COMMENT ON COLUMN wms_borrow_record.from_person IS '发货人';
COMMENT ON COLUMN wms_borrow_record.to_person IS '收货人';
COMMENT ON COLUMN wms_borrow_record.doc_date IS '单据日期';
COMMENT ON COLUMN wms_borrow_record.borrow_no IS '借用单号';
COMMENT ON COLUMN wms_borrow_record.plan_return_date IS '计划归还日期';
COMMENT ON COLUMN wms_borrow_record.overdue_flag IS '是否逾期';
COMMENT ON COLUMN wms_borrow_record.overdue_days IS '逾期天数';
COMMENT ON COLUMN wms_borrow_record.instance_code IS '器材编码';
COMMENT ON COLUMN wms_borrow_record.borrow_time IS '借用时间';
COMMENT ON COLUMN wms_borrow_record.return_time IS '归还时间';
COMMENT ON COLUMN wms_borrow_record.borrow_remark IS '借用备注';
COMMENT ON COLUMN wms_borrow_record.return_remark IS '归还备注';
COMMENT ON COLUMN wms_borrow_record.original_warehouse_id IS '借出前仓库';
COMMENT ON COLUMN wms_borrow_record.original_area_id IS '借出前库区';
COMMENT ON COLUMN wms_borrow_record.original_rack_id IS '借出前货架';
COMMENT ON COLUMN wms_borrow_record.original_location_id IS '借出前货位';
COMMENT ON COLUMN wms_borrow_record.original_box_id IS '借出前箱体ID';
COMMENT ON COLUMN wms_borrow_record.returned_warehouse_id IS '归还后仓库';
COMMENT ON COLUMN wms_borrow_record.returned_area_id IS '归还后库区';
COMMENT ON COLUMN wms_borrow_record.returned_rack_id IS '归还后货架';
COMMENT ON COLUMN wms_borrow_record.returned_location_id IS '归还后货位';
COMMENT ON COLUMN wms_borrow_record.returned_box_id IS '归还后箱体ID';
COMMENT ON COLUMN wms_borrow_record.create_by IS '创建人';
COMMENT ON COLUMN wms_borrow_record.create_time IS '创建时间';
COMMENT ON COLUMN wms_borrow_record.update_by IS '修改人';
COMMENT ON COLUMN wms_borrow_record.update_time IS '修改时间';
CREATE INDEX tb0_wms_borrow_record_idx_wms_borrow_record_status ON wms_advence_master.wms_borrow_record USING btree (borrow_status) TABLESPACE pg_default;
CREATE INDEX tb0_wms_borrow_record_idx_wms_borrow_record_returned_box_id ON wms_advence_master.wms_borrow_record USING btree (returned_box_id) TABLESPACE pg_default;
CREATE INDEX tb0_wms_borrow_record_idx_wms_borrow_record_overdue ON wms_advence_master.wms_borrow_record USING btree (overdue_flag, borrow_status) TABLESPACE pg_default;
CREATE INDEX tb0_wms_borrow_record_idx_wms_borrow_record_item_status ON wms_advence_master.wms_borrow_record USING btree (item_instance_id, borrow_status, borrow_time) TABLESPACE pg_default;
CREATE INDEX tb0_wms_borrow_record_idx_wms_borrow_record_original_box_id ON wms_advence_master.wms_borrow_record USING btree (original_box_id) TABLESPACE pg_default;
CREATE INDEX tb0_wms_borrow_record_idx_wms_borrow_record_item_instance_id ON wms_advence_master.wms_borrow_record USING btree (item_instance_id) TABLESPACE pg_default;
CREATE INDEX tb0_wms_borrow_record_idx_wms_borrow_record_borrow_time ON wms_advence_master.wms_borrow_record USING btree (borrow_time) TABLESPACE pg_default;
ALTER TABLE wms_borrow_record ADD CONSTRAINT wms_borrow_record_uk1 UNIQUE USING btree (borrow_no);

--Data for  Name: wms_borrow_record; Type: Table; Schema: wms_advence_master;

INSERT INTO wms_advence_master.wms_borrow_record (id,item_instance_id,borrow_status,borrower,from_unit,to_unit,from_person,to_person,doc_date,borrow_no,plan_return_date,overdue_flag,overdue_days,instance_code,borrow_time,return_time,borrow_remark,return_remark,original_warehouse_id,original_area_id,original_rack_id,original_location_id,original_box_id,returned_warehouse_id,returned_area_id,returned_rack_id,returned_location_id,returned_box_id,create_by,create_time,update_by,update_time)
 VALUES (2058933462380085250,2058920374624878599,'returned','ad','dfad','fads','f','dsfdas','2026-05-25','BRWH12058933462598189056','2026-05-27',0,0,'ITEMWH11000000010','2026-05-25 23:29:19.000','2026-05-29 00:00:00.000','fdaf','fads',1828364459110469633,2056770261064429569,2056771221887201282,2056771221887201284,2058930926625521665,1828364459110469633,2056770261064429569,2056771221887201282,2056771221887201284,2058930926625521665,'admin','2026-05-25 23:29:25.283','admin','2026-05-25 23:29:44.600');
INSERT INTO wms_advence_master.wms_borrow_record (id,item_instance_id,borrow_status,borrower,from_unit,to_unit,from_person,to_person,doc_date,borrow_no,plan_return_date,overdue_flag,overdue_days,instance_code,borrow_time,return_time,borrow_remark,return_remark,original_warehouse_id,original_area_id,original_rack_id,original_location_id,original_box_id,returned_warehouse_id,returned_area_id,returned_rack_id,returned_location_id,returned_box_id,create_by,create_time,update_by,update_time)
 VALUES (2058937016222253058,2058920374624878598,'borrowed','a','f','dfa','ad','dfd','2026-05-29','BRWH12058937016234835968','2026-05-28',0,0,'ITEMWH11000000009','2026-05-25 23:43:21.000',null,'fdf',null,1828364459110469633,2056770261064429569,2056771221887201282,2056771221887201284,2058930926625521665,null,null,null,null,null,'admin','2026-05-25 23:43:32.527','admin','2026-05-25 23:43:32.527');


-- Name: wms_box; Type: Table; Schema: wms_advence_master;

SET search_path = wms_advence_master;
CREATE TABLE wms_box (
    id bigint AUTO_INCREMENT NOT NULL,
    box_code varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_0900_ai_ci NOT NULL,
    box_name varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_0900_ai_ci DEFAULT NULL::varchar,
    box_status varchar(32) CHARACTER SET "UTF8" COLLATE utf8mb4_0900_ai_ci DEFAULT 'idle'::varchar,
    item_count integer,
    warehouse_id bigint,
    area_id bigint,
    rack_id bigint,
    location_id bigint,
    remark varchar(255) CHARACTER SET "UTF8" COLLATE utf8mb4_0900_ai_ci DEFAULT NULL::varchar,
    create_by varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_0900_ai_ci DEFAULT NULL::varchar,
    create_time timestamp(3) without time zone DEFAULT NULL::timestamp without time zone,
    update_by varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_0900_ai_ci DEFAULT NULL::varchar,
    update_time timestamp(3) without time zone DEFAULT NULL::timestamp without time zone,
    CONSTRAINT wms_box_pk PRIMARY KEY (id)
) AUTO_INCREMENT = 2058930926625521666
CHARACTER SET = "UTF8" COLLATE = "utf8mb4_0900_ai_ci"
WITH (orientation=row, compression=no, fillfactor=80);
COMMENT ON TABLE wms_box IS '箱体';
COMMENT ON COLUMN wms_box.box_code IS '箱码';
COMMENT ON COLUMN wms_box.box_name IS '箱体名称';
COMMENT ON COLUMN wms_box.box_status IS '箱体状态';
COMMENT ON COLUMN wms_box.item_count IS '箱内器材数量';
COMMENT ON COLUMN wms_box.warehouse_id IS '所属仓库';
COMMENT ON COLUMN wms_box.area_id IS '所属库区';
COMMENT ON COLUMN wms_box.rack_id IS '所属货架';
COMMENT ON COLUMN wms_box.location_id IS '所属货位';
COMMENT ON COLUMN wms_box.remark IS '备注';
COMMENT ON COLUMN wms_box.create_by IS '创建人';
COMMENT ON COLUMN wms_box.create_time IS '创建时间';
COMMENT ON COLUMN wms_box.update_by IS '修改人';
COMMENT ON COLUMN wms_box.update_time IS '修改时间';
CREATE INDEX tb0_wms_box_idx_wms_box_location_id ON wms_advence_master.wms_box USING btree (location_id) TABLESPACE pg_default;
CREATE INDEX tb0_wms_box_idx_wms_box_position ON wms_advence_master.wms_box USING btree (warehouse_id, area_id, rack_id, location_id) TABLESPACE pg_default;
CREATE INDEX tb0_wms_box_idx_wms_box_status ON wms_advence_master.wms_box USING btree (box_status) TABLESPACE pg_default;
ALTER TABLE wms_box ADD CONSTRAINT wms_box_uk1 UNIQUE USING btree (box_code);

--Data for  Name: wms_box; Type: Table; Schema: wms_advence_master;

INSERT INTO wms_advence_master.wms_box (id,box_code,box_name,box_status,item_count,warehouse_id,area_id,rack_id,location_id,remark,create_by,create_time,update_by,update_time)
 VALUES (2058927106696708098,'BOX2058927106524741632','ad','idle',null,null,null,null,null,null,'admin','2026-05-25 23:04:09.931','admin','2026-05-25 23:04:09.931');
INSERT INTO wms_advence_master.wms_box (id,box_code,box_name,box_status,item_count,warehouse_id,area_id,rack_id,location_id,remark,create_by,create_time,update_by,update_time)
 VALUES (2058930926625521665,'dfdasf','dfdasf','packed',3,1828364459110469633,2056770261064429569,2056771221887201282,2056771221887201284,null,'admin','2026-05-25 23:19:20.667','admin','2026-05-25 23:19:21.803');
INSERT INTO wms_advence_master.wms_box (id,box_code,box_name,box_status,item_count,warehouse_id,area_id,rack_id,location_id,remark,create_by,create_time,update_by,update_time)
 VALUES (2058930926243840002,'asdfsd','asdfsd','packed',3,1828364459110469633,2056770261064429569,2056771221887201282,2056771221887201283,null,'admin','2026-05-25 23:19:20.573','admin','2026-05-25 23:19:22.381');


-- Name: wms_check_order; Type: Table; Schema: wms_advence_master;

SET search_path = wms_advence_master;
CREATE TABLE wms_check_order (
    id bigint AUTO_INCREMENT NOT NULL,
    check_order_no varchar(32) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    check_order_status smallint DEFAULT 11::smallint,
    check_order_total numeric(20,2) DEFAULT NULL::numeric,
    warehouse_id bigint,
    area_id bigint,
    rack_id bigint,
    check_scope_type varchar(32) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    check_date timestamp(3) without time zone DEFAULT NULL::timestamp without time zone,
    checker_name varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    reviewer_name varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    remark varchar(255) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    create_by varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    create_time timestamp(3) without time zone DEFAULT NULL::timestamp without time zone,
    update_by varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    update_time timestamp(3) without time zone DEFAULT NULL::timestamp without time zone,
    CONSTRAINT wms_check_order_pk PRIMARY KEY (id)
) AUTO_INCREMENT = 1
CHARACTER SET = "UTF8" COLLATE = "utf8mb4_general_ci"
WITH (orientation=row, compression=no, fillfactor=80);
COMMENT ON TABLE wms_check_order IS '库存盘点单据';
COMMENT ON COLUMN wms_check_order.check_order_no IS '盘点单号';
COMMENT ON COLUMN wms_check_order.check_order_status IS '库存盘点单状态 -1：作废 0：未盘库 1：已盘库';
COMMENT ON COLUMN wms_check_order.check_order_total IS '盈亏数';
COMMENT ON COLUMN wms_check_order.warehouse_id IS '所属仓库';
COMMENT ON COLUMN wms_check_order.area_id IS '所属库区';
COMMENT ON COLUMN wms_check_order.rack_id IS '货架';
COMMENT ON COLUMN wms_check_order.check_scope_type IS '盘点范围类型';
COMMENT ON COLUMN wms_check_order.check_date IS '盘点日期';
COMMENT ON COLUMN wms_check_order.checker_name IS '盘点人';
COMMENT ON COLUMN wms_check_order.reviewer_name IS '复核人';
COMMENT ON COLUMN wms_check_order.remark IS '备注';
COMMENT ON COLUMN wms_check_order.create_by IS '创建人';
COMMENT ON COLUMN wms_check_order.create_time IS '创建时间';
COMMENT ON COLUMN wms_check_order.update_by IS '修改人';
COMMENT ON COLUMN wms_check_order.update_time IS '修改时间';

--Data for  Name: wms_check_order; Type: Table; Schema: wms_advence_master;



-- Name: wms_check_order_detail; Type: Table; Schema: wms_advence_master;

SET search_path = wms_advence_master;
CREATE TABLE wms_check_order_detail (
    id bigint AUTO_INCREMENT NOT NULL,
    check_order_id bigint,
    sku_id bigint NOT NULL,
    quantity numeric(20,2) DEFAULT NULL::numeric,
    check_quantity numeric(20,2) DEFAULT NULL::numeric,
    difference_quantity numeric(20,2) DEFAULT NULL::numeric,
    warehouse_id bigint,
    area_id bigint,
    rack_id bigint,
    location_id bigint,
    item_instance_id bigint,
    box_id bigint,
    receipt_time timestamp(3) without time zone DEFAULT NULL::timestamp without time zone,
    inventory_detail_id bigint,
    remark varchar(255) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    create_by varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    create_time timestamp(3) without time zone DEFAULT NULL::timestamp without time zone,
    update_by varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    update_time timestamp(3) without time zone DEFAULT NULL::timestamp without time zone,
    CONSTRAINT wms_check_order_detail_pk PRIMARY KEY (id)
) AUTO_INCREMENT = 1
CHARACTER SET = "UTF8" COLLATE = "utf8mb4_general_ci"
WITH (orientation=row, compression=no, fillfactor=80);
COMMENT ON TABLE wms_check_order_detail IS '库存盘点单据详情';
COMMENT ON COLUMN wms_check_order_detail.check_order_id IS '盘点单id';
COMMENT ON COLUMN wms_check_order_detail.sku_id IS '规格id';
COMMENT ON COLUMN wms_check_order_detail.quantity IS '库存数量';
COMMENT ON COLUMN wms_check_order_detail.check_quantity IS '盘点数量';
COMMENT ON COLUMN wms_check_order_detail.difference_quantity IS '差异数量';
COMMENT ON COLUMN wms_check_order_detail.warehouse_id IS '所属仓库';
COMMENT ON COLUMN wms_check_order_detail.area_id IS '所属库区';
COMMENT ON COLUMN wms_check_order_detail.rack_id IS '所属货架';
COMMENT ON COLUMN wms_check_order_detail.location_id IS '所属货位';
COMMENT ON COLUMN wms_check_order_detail.item_instance_id IS '器材实例ID';
COMMENT ON COLUMN wms_check_order_detail.box_id IS '箱体ID';
COMMENT ON COLUMN wms_check_order_detail.receipt_time IS '入库时间';
COMMENT ON COLUMN wms_check_order_detail.inventory_detail_id IS '入库记录id';
COMMENT ON COLUMN wms_check_order_detail.remark IS '备注';
COMMENT ON COLUMN wms_check_order_detail.create_by IS '创建人';
COMMENT ON COLUMN wms_check_order_detail.create_time IS '创建时间';
COMMENT ON COLUMN wms_check_order_detail.update_by IS '修改人';
COMMENT ON COLUMN wms_check_order_detail.update_time IS '修改时间';

--Data for  Name: wms_check_order_detail; Type: Table; Schema: wms_advence_master;



-- Name: wms_inventory; Type: Table; Schema: wms_advence_master;

SET search_path = wms_advence_master;
CREATE TABLE wms_inventory (
    id bigint AUTO_INCREMENT NOT NULL,
    sku_id bigint,
    warehouse_id bigint,
    area_id bigint,
    rack_id bigint,
    location_id bigint,
    quantity numeric(20,2) DEFAULT NULL::numeric,
    remark varchar(255) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    create_by varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    create_time timestamp(3) without time zone DEFAULT NULL::timestamp without time zone,
    update_by varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    update_time timestamp(3) without time zone DEFAULT NULL::timestamp without time zone,
    CONSTRAINT wms_inventory_pk PRIMARY KEY (id)
) AUTO_INCREMENT = 2058935450689896450
CHARACTER SET = "UTF8" COLLATE = "utf8mb4_general_ci"
WITH (orientation=row, compression=no, fillfactor=80);
COMMENT ON TABLE wms_inventory IS '库存表';
COMMENT ON COLUMN wms_inventory.sku_id IS '规格ID';
COMMENT ON COLUMN wms_inventory.warehouse_id IS '所属仓库';
COMMENT ON COLUMN wms_inventory.area_id IS '所属库区';
COMMENT ON COLUMN wms_inventory.rack_id IS '所属货架';
COMMENT ON COLUMN wms_inventory.location_id IS '所属货位';
COMMENT ON COLUMN wms_inventory.quantity IS '库存';
COMMENT ON COLUMN wms_inventory.remark IS '备注';
COMMENT ON COLUMN wms_inventory.create_by IS '创建人';
COMMENT ON COLUMN wms_inventory.create_time IS '创建时间';
COMMENT ON COLUMN wms_inventory.update_by IS '修改人';
COMMENT ON COLUMN wms_inventory.update_time IS '修改时间';
CREATE INDEX tb0_wms_inventory_idx_wms_inventory_position ON wms_advence_master.wms_inventory USING btree (warehouse_id, area_id, rack_id, location_id) TABLESPACE pg_default;
ALTER TABLE wms_inventory ADD CONSTRAINT wms_inventory_uk1 UNIQUE USING btree (warehouse_id, area_id, rack_id, location_id, sku_id);

--Data for  Name: wms_inventory; Type: Table; Schema: wms_advence_master;

INSERT INTO wms_advence_master.wms_inventory (id,sku_id,warehouse_id,area_id,rack_id,location_id,quantity,remark,create_by,create_time,update_by,update_time)
 VALUES (2058930935697801218,2058919798067462147,1828364459110469633,2056770261064429569,2056771221887201282,2056771221887201284,3,null,'admin','2026-05-25 23:19:22.819','admin','2026-05-25 23:19:22.819');
INSERT INTO wms_advence_master.wms_inventory (id,sku_id,warehouse_id,area_id,rack_id,location_id,quantity,remark,create_by,create_time,update_by,update_time)
 VALUES (2058930935697801219,2058919798067462146,1828364459110469633,2056770261064429569,2056771221887201282,2056771221887201283,2,null,'admin','2026-05-25 23:19:22.820','admin','2026-05-25 23:28:45.073');
INSERT INTO wms_advence_master.wms_inventory (id,sku_id,warehouse_id,area_id,rack_id,location_id,quantity,remark,create_by,create_time,update_by,update_time)
 VALUES (2058933936357408769,2058919798067462146,1828364459110469633,2056770261064429569,null,null,-1,null,'admin','2026-05-25 23:31:18.242','admin','2026-05-25 23:31:18.242');
INSERT INTO wms_advence_master.wms_inventory (id,sku_id,warehouse_id,area_id,rack_id,location_id,quantity,remark,create_by,create_time,update_by,update_time)
 VALUES (2058933936990748674,2058919798067462146,2056769387227328513,2056770367725580289,null,null,1,null,'admin','2026-05-25 23:31:18.392','admin','2026-05-25 23:31:18.392');
INSERT INTO wms_advence_master.wms_inventory (id,sku_id,warehouse_id,area_id,rack_id,location_id,quantity,remark,create_by,create_time,update_by,update_time)
 VALUES (2058935450048167938,2058919798067462147,1828364459110469633,2056770261064429569,null,null,-1,null,'admin','2026-05-25 23:37:19.123','admin','2026-05-25 23:37:19.123');
INSERT INTO wms_advence_master.wms_inventory (id,sku_id,warehouse_id,area_id,rack_id,location_id,quantity,remark,create_by,create_time,update_by,update_time)
 VALUES (2058935450689896449,2058919798067462147,2056769387227328513,2056770392228704258,null,null,1,null,'admin','2026-05-25 23:37:19.274','admin','2026-05-25 23:37:19.274');


-- Name: wms_inventory_detail; Type: Table; Schema: wms_advence_master;

SET search_path = wms_advence_master;
CREATE TABLE wms_inventory_detail (
    id bigint AUTO_INCREMENT NOT NULL,
    receipt_order_id bigint,
    receipt_order_type varchar(20) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    order_no varchar(255) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    type integer,
    sku_id bigint,
    warehouse_id bigint,
    area_id bigint,
    rack_id bigint,
    location_id bigint,
    item_instance_id bigint,
    box_id bigint,
    source_order_type varchar(32) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    source_order_id bigint,
    line_no integer,
    quantity numeric(20,2) DEFAULT NULL::numeric,
    unit_price numeric(18,2) DEFAULT NULL::numeric,
    line_amount numeric(18,2) DEFAULT NULL::numeric,
    remark varchar(255) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    remain_quantity numeric(10,2) DEFAULT NULL::numeric,
    create_by varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    create_time timestamp(3) without time zone DEFAULT NULL::timestamp without time zone,
    update_by varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    update_time timestamp(3) without time zone DEFAULT NULL::timestamp without time zone,
    CONSTRAINT wms_inventory_detail_pk PRIMARY KEY (id)
) AUTO_INCREMENT = 2058935451075772419
CHARACTER SET = "UTF8" COLLATE = "utf8mb4_0900_ai_ci"
WITH (orientation=row, compression=no, fillfactor=80);
COMMENT ON TABLE wms_inventory_detail IS '库存详情';
COMMENT ON COLUMN wms_inventory_detail.receipt_order_id IS '入库单id';
COMMENT ON COLUMN wms_inventory_detail.receipt_order_type IS '入库单类型';
COMMENT ON COLUMN wms_inventory_detail.order_no IS '单号';
COMMENT ON COLUMN wms_inventory_detail.type IS '类型 1：入库 2：移库 3：盘库';
COMMENT ON COLUMN wms_inventory_detail.sku_id IS '规格ID';
COMMENT ON COLUMN wms_inventory_detail.warehouse_id IS '所属仓库';
COMMENT ON COLUMN wms_inventory_detail.area_id IS '所属库区';
COMMENT ON COLUMN wms_inventory_detail.rack_id IS '所属货架';
COMMENT ON COLUMN wms_inventory_detail.location_id IS '所属货位';
COMMENT ON COLUMN wms_inventory_detail.item_instance_id IS '器材明细ID';
COMMENT ON COLUMN wms_inventory_detail.box_id IS '箱体ID';
COMMENT ON COLUMN wms_inventory_detail.source_order_type IS '来源单据类型';
COMMENT ON COLUMN wms_inventory_detail.source_order_id IS '来源单据ID';
COMMENT ON COLUMN wms_inventory_detail.line_no IS '来源行号';
COMMENT ON COLUMN wms_inventory_detail.quantity IS '入库数量';
COMMENT ON COLUMN wms_inventory_detail.unit_price IS '单价';
COMMENT ON COLUMN wms_inventory_detail.line_amount IS '总价';
COMMENT ON COLUMN wms_inventory_detail.remark IS '备注';
COMMENT ON COLUMN wms_inventory_detail.remain_quantity IS '剩余数量';
COMMENT ON COLUMN wms_inventory_detail.create_by IS '创建人';
COMMENT ON COLUMN wms_inventory_detail.create_time IS '创建时间';
COMMENT ON COLUMN wms_inventory_detail.update_by IS '修改人';
COMMENT ON COLUMN wms_inventory_detail.update_time IS '修改时间';
CREATE INDEX tb0_wms_inventory_detail_idx_wms_inventory_detail_box_id ON wms_advence_master.wms_inventory_detail USING btree (box_id) TABLESPACE pg_default;
CREATE INDEX tb0_wms_inventory_detail_idx_wms_inventory_detail_spec_time ON wms_advence_master.wms_inventory_detail USING btree (create_time) TABLESPACE pg_default;
CREATE INDEX tb0_wms_inventory_detail_idx_wms_inventory_detail_position ON wms_advence_master.wms_inventory_detail USING btree (warehouse_id, area_id, rack_id, location_id) TABLESPACE pg_default;
CREATE INDEX tb0_wms_inventory_detail_idx_wms_inventory_detail_order_line ON wms_advence_master.wms_inventory_detail USING btree (order_no, line_no) TABLESPACE pg_default;
CREATE INDEX tb0_wms_inventory_detail_idx_wms_inventory_detail_instance_id ON wms_advence_master.wms_inventory_detail USING btree (item_instance_id) TABLESPACE pg_default;

--Data for  Name: wms_inventory_detail; Type: Table; Schema: wms_advence_master;

INSERT INTO wms_advence_master.wms_inventory_detail (id,receipt_order_id,receipt_order_type,order_no,type,sku_id,warehouse_id,area_id,rack_id,location_id,item_instance_id,box_id,source_order_type,source_order_id,line_no,quantity,unit_price,line_amount,remark,remain_quantity,create_by,create_time,update_by,update_time)
 VALUES (2058930934737305603,2058929554794512385,null,null,1,2058919798067462146,1828364459110469633,2056770261064429569,2056771221887201282,2056771221887201283,2058920403737542659,2058930926243840002,null,null,null,1,5,5,'a',1,'admin','2026-05-25 23:19:22.590','admin','2026-05-25 23:19:22.590');
INSERT INTO wms_advence_master.wms_inventory_detail (id,receipt_order_id,receipt_order_type,order_no,type,sku_id,warehouse_id,area_id,rack_id,location_id,item_instance_id,box_id,source_order_type,source_order_id,line_no,quantity,unit_price,line_amount,remark,remain_quantity,create_by,create_time,update_by,update_time)
 VALUES (2058930934737305604,2058929554794512385,null,null,1,2058919798067462147,1828364459110469633,2056770261064429569,2056771221887201282,2056771221887201284,2058920374624878598,2058930926625521665,null,null,null,1,4,4,'a',1,'admin','2026-05-25 23:19:22.591','admin','2026-05-25 23:19:22.591');
INSERT INTO wms_advence_master.wms_inventory_detail (id,receipt_order_id,receipt_order_type,order_no,type,sku_id,warehouse_id,area_id,rack_id,location_id,item_instance_id,box_id,source_order_type,source_order_id,line_no,quantity,unit_price,line_amount,remark,remain_quantity,create_by,create_time,update_by,update_time)
 VALUES (2058930934737305605,2058929554794512385,null,null,1,2058919798067462147,1828364459110469633,2056770261064429569,2056771221887201282,2056771221887201284,2058920374624878599,2058930926625521665,null,null,null,1,3,3,'a',1,'admin','2026-05-25 23:19:22.592','admin','2026-05-25 23:19:22.592');
INSERT INTO wms_advence_master.wms_inventory_detail (id,receipt_order_id,receipt_order_type,order_no,type,sku_id,warehouse_id,area_id,rack_id,location_id,item_instance_id,box_id,source_order_type,source_order_id,line_no,quantity,unit_price,line_amount,remark,remain_quantity,create_by,create_time,update_by,update_time)
 VALUES (2058933937439539202,2058933863678509057,null,'DBWH12058933863439433728',2,2058919798067462146,2056769387227328513,2056770367725580289,null,null,2058920403737542660,null,'调拨单',2058933863678509057,null,1,7,7,null,1,'admin','2026-05-25 23:31:18.491','admin','2026-05-25 23:31:18.491');
INSERT INTO wms_advence_master.wms_inventory_detail (id,receipt_order_id,receipt_order_type,order_no,type,sku_id,warehouse_id,area_id,rack_id,location_id,item_instance_id,box_id,source_order_type,source_order_id,line_no,quantity,unit_price,line_amount,remark,remain_quantity,create_by,create_time,update_by,update_time)
 VALUES (2058935451075772418,2058935449213501441,null,'DBWH12058935449062506496',2,2058919798067462147,2056769387227328513,2056770392228704258,null,null,2058920374624878597,null,'调拨单',2058935449213501441,null,1,2,2,null,1,'admin','2026-05-25 23:37:19.369','admin','2026-05-25 23:37:19.369');


-- Name: wms_inventory_history; Type: Table; Schema: wms_advence_master;

SET search_path = wms_advence_master;
CREATE TABLE wms_inventory_history (
    id bigint AUTO_INCREMENT NOT NULL,
    warehouse_id bigint,
    area_id bigint,
    rack_id bigint,
    location_id bigint,
    item_instance_id bigint,
    box_id bigint,
    before_quantity numeric(18,2) DEFAULT NULL::numeric,
    after_quantity numeric(18,2) DEFAULT NULL::numeric,
    operation_type varchar(32) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    operator_name varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    sku_id bigint,
    quantity numeric(20,2) DEFAULT NULL::numeric,
    unit_price numeric(18,2) DEFAULT NULL::numeric,
    line_amount numeric(18,2) DEFAULT NULL::numeric,
    remark varchar(255) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    order_id bigint,
    order_no varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    order_type integer,
    create_time timestamp(3) without time zone DEFAULT NULL::timestamp without time zone,
    CONSTRAINT wms_inventory_history_pk PRIMARY KEY (id)
) AUTO_INCREMENT = 2058937016595546114
CHARACTER SET = "UTF8" COLLATE = "utf8mb4_general_ci"
WITH (orientation=row, compression=no, fillfactor=80);
COMMENT ON TABLE wms_inventory_history IS '库存记录';
COMMENT ON COLUMN wms_inventory_history.warehouse_id IS '所属仓库';
COMMENT ON COLUMN wms_inventory_history.area_id IS '所属库区';
COMMENT ON COLUMN wms_inventory_history.rack_id IS '所属货架';
COMMENT ON COLUMN wms_inventory_history.location_id IS '所属货位';
COMMENT ON COLUMN wms_inventory_history.item_instance_id IS '器材明细ID';
COMMENT ON COLUMN wms_inventory_history.box_id IS '箱体ID';
COMMENT ON COLUMN wms_inventory_history.before_quantity IS '变化前数量';
COMMENT ON COLUMN wms_inventory_history.after_quantity IS '变化后数量';
COMMENT ON COLUMN wms_inventory_history.operation_type IS '业务动作类型';
COMMENT ON COLUMN wms_inventory_history.operator_name IS '操作人';
COMMENT ON COLUMN wms_inventory_history.sku_id IS '规格ID';
COMMENT ON COLUMN wms_inventory_history.quantity IS '库存变化';
COMMENT ON COLUMN wms_inventory_history.unit_price IS '单价';
COMMENT ON COLUMN wms_inventory_history.line_amount IS '总价';
COMMENT ON COLUMN wms_inventory_history.remark IS '备注';
COMMENT ON COLUMN wms_inventory_history.order_id IS '操作id（出库、入库、库存移动表单id）';
COMMENT ON COLUMN wms_inventory_history.order_no IS '操作单号（入库、出库、移库、盘库单号）';
COMMENT ON COLUMN wms_inventory_history.order_type IS '操作类型';
COMMENT ON COLUMN wms_inventory_history.create_time IS '创建时间';
CREATE INDEX tb0_wms_inventory_history_idx_wms_inventory_history_order_no ON wms_advence_master.wms_inventory_history USING btree (order_no) TABLESPACE pg_default;
CREATE INDEX tb0_wms_inventory_history_idx_wms_inventory_history_time ON wms_advence_master.wms_inventory_history USING btree (create_time, order_type) TABLESPACE pg_default;
CREATE INDEX tb0_wms_inventory_history_idx_wms_inventory_history_position ON wms_advence_master.wms_inventory_history USING btree (warehouse_id, area_id, rack_id, location_id) TABLESPACE pg_default;
CREATE INDEX tb0_wms_inventory_history_idx_wms_inventory_history_box_id ON wms_advence_master.wms_inventory_history USING btree (box_id) TABLESPACE pg_default;
CREATE INDEX tb0_wms_inventory_history_idx_wms_inventory_history_instance_id ON wms_advence_master.wms_inventory_history USING btree (item_instance_id) TABLESPACE pg_default;

--Data for  Name: wms_inventory_history; Type: Table; Schema: wms_advence_master;

INSERT INTO wms_advence_master.wms_inventory_history (id,warehouse_id,area_id,rack_id,location_id,item_instance_id,box_id,before_quantity,after_quantity,operation_type,operator_name,sku_id,quantity,unit_price,line_amount,remark,order_id,order_no,order_type,create_time)
 VALUES (2058930936146591745,1828364459110469633,2056770261064429569,2056771221887201282,2056771221887201283,2058920403737542661,2058930926243840002,null,null,null,null,2058919798067462146,1,4,4,'a',2058929554794512385,'RKWH12058929554542854144',1,'2026-05-25 23:19:22.922');
INSERT INTO wms_advence_master.wms_inventory_history (id,warehouse_id,area_id,rack_id,location_id,item_instance_id,box_id,before_quantity,after_quantity,operation_type,operator_name,sku_id,quantity,unit_price,line_amount,remark,order_id,order_no,order_type,create_time)
 VALUES (2058930936154980353,1828364459110469633,2056770261064429569,2056771221887201282,2056771221887201283,2058920403737542660,2058930926243840002,null,null,null,null,2058919798067462146,1,7,7,'a',2058929554794512385,'RKWH12058929554542854144',1,'2026-05-25 23:19:22.923');
INSERT INTO wms_advence_master.wms_inventory_history (id,warehouse_id,area_id,rack_id,location_id,item_instance_id,box_id,before_quantity,after_quantity,operation_type,operator_name,sku_id,quantity,unit_price,line_amount,remark,order_id,order_no,order_type,create_time)
 VALUES (2058930936154980354,1828364459110469633,2056770261064429569,2056771221887201282,2056771221887201283,2058920403737542659,2058930926243840002,null,null,null,null,2058919798067462146,1,5,5,'a',2058929554794512385,'RKWH12058929554542854144',1,'2026-05-25 23:19:22.924');
INSERT INTO wms_advence_master.wms_inventory_history (id,warehouse_id,area_id,rack_id,location_id,item_instance_id,box_id,before_quantity,after_quantity,operation_type,operator_name,sku_id,quantity,unit_price,line_amount,remark,order_id,order_no,order_type,create_time)
 VALUES (2058930936154980355,1828364459110469633,2056770261064429569,2056771221887201282,2056771221887201284,2058920374624878598,2058930926625521665,null,null,null,null,2058919798067462147,1,4,4,'a',2058929554794512385,'RKWH12058929554542854144',1,'2026-05-25 23:19:22.924');
INSERT INTO wms_advence_master.wms_inventory_history (id,warehouse_id,area_id,rack_id,location_id,item_instance_id,box_id,before_quantity,after_quantity,operation_type,operator_name,sku_id,quantity,unit_price,line_amount,remark,order_id,order_no,order_type,create_time)
 VALUES (2058930936154980356,1828364459110469633,2056770261064429569,2056771221887201282,2056771221887201284,2058920374624878599,2058930926625521665,null,null,null,null,2058919798067462147,1,3,3,'a',2058929554794512385,'RKWH12058929554542854144',1,'2026-05-25 23:19:22.925');
INSERT INTO wms_advence_master.wms_inventory_history (id,warehouse_id,area_id,rack_id,location_id,item_instance_id,box_id,before_quantity,after_quantity,operation_type,operator_name,sku_id,quantity,unit_price,line_amount,remark,order_id,order_no,order_type,create_time)
 VALUES (2058930936163368961,1828364459110469633,2056770261064429569,2056771221887201282,2056771221887201284,2058920374624878597,2058930926625521665,null,null,null,null,2058919798067462147,1,2,2,'z',2058929554794512385,'RKWH12058929554542854144',1,'2026-05-25 23:19:22.927');
INSERT INTO wms_advence_master.wms_inventory_history (id,warehouse_id,area_id,rack_id,location_id,item_instance_id,box_id,before_quantity,after_quantity,operation_type,operator_name,sku_id,quantity,unit_price,line_amount,remark,order_id,order_no,order_type,create_time)
 VALUES (2058933295316762625,1828364459110469633,2056770261064429569,2056771221887201282,2056771221887201283,2058920403737542661,2058930926243840002,null,null,null,null,2058919798067462146,-1,3,3,null,2058933291508334593,'CKWH12058933269035253760',2,'2026-05-25 23:28:45.627');
INSERT INTO wms_advence_master.wms_inventory_history (id,warehouse_id,area_id,rack_id,location_id,item_instance_id,box_id,before_quantity,after_quantity,operation_type,operator_name,sku_id,quantity,unit_price,line_amount,remark,order_id,order_no,order_type,create_time)
 VALUES (2058933462380085251,1828364459110469633,2056770261064429569,2056771221887201282,2056771221887201284,2058920374624878599,2058930926625521665,null,null,'borrow','ad',2058919798067462147,-1,null,null,'fdaf',2058933462380085250,'BRWH12058933462598189056',5,'2026-05-25 23:29:19.000');
INSERT INTO wms_advence_master.wms_inventory_history (id,warehouse_id,area_id,rack_id,location_id,item_instance_id,box_id,before_quantity,after_quantity,operation_type,operator_name,sku_id,quantity,unit_price,line_amount,remark,order_id,order_no,order_type,create_time)
 VALUES (2058933544068349954,1828364459110469633,2056770261064429569,2056771221887201282,2056771221887201284,2058920374624878599,2058930926625521665,null,null,'return','ad',2058919798067462147,1,null,null,'fads',2058933462380085250,'BRWH12058933462598189056',6,'2026-05-29 00:00:00.000');
INSERT INTO wms_advence_master.wms_inventory_history (id,warehouse_id,area_id,rack_id,location_id,item_instance_id,box_id,before_quantity,after_quantity,operation_type,operator_name,sku_id,quantity,unit_price,line_amount,remark,order_id,order_no,order_type,create_time)
 VALUES (2058933937821220866,1828364459110469633,2056770261064429569,2056771221887201282,2056771221887201283,2058920403737542660,null,null,null,null,null,2058919798067462146,-1,7,7,null,2058933863678509057,'DBWH12058933863439433728',3,'2026-05-25 23:31:18.588');
INSERT INTO wms_advence_master.wms_inventory_history (id,warehouse_id,area_id,rack_id,location_id,item_instance_id,box_id,before_quantity,after_quantity,operation_type,operator_name,sku_id,quantity,unit_price,line_amount,remark,order_id,order_no,order_type,create_time)
 VALUES (2058933937821220867,2056769387227328513,2056770367725580289,null,null,2058920403737542660,null,null,null,null,null,2058919798067462146,1,7,7,null,2058933863678509057,'DBWH12058933863439433728',3,'2026-05-25 23:31:18.589');
INSERT INTO wms_advence_master.wms_inventory_history (id,warehouse_id,area_id,rack_id,location_id,item_instance_id,box_id,before_quantity,after_quantity,operation_type,operator_name,sku_id,quantity,unit_price,line_amount,remark,order_id,order_no,order_type,create_time)
 VALUES (2058935451461648386,1828364459110469633,2056770261064429569,2056771221887201282,2056771221887201284,2058920374624878597,null,null,null,null,null,2058919798067462147,-1,2,2,null,2058935449213501441,'DBWH12058935449062506496',3,'2026-05-25 23:37:19.464');
INSERT INTO wms_advence_master.wms_inventory_history (id,warehouse_id,area_id,rack_id,location_id,item_instance_id,box_id,before_quantity,after_quantity,operation_type,operator_name,sku_id,quantity,unit_price,line_amount,remark,order_id,order_no,order_type,create_time)
 VALUES (2058935451461648387,2056769387227328513,2056770392228704258,null,null,2058920374624878597,null,null,null,null,null,2058919798067462147,1,2,2,null,2058935449213501441,'DBWH12058935449062506496',3,'2026-05-25 23:37:19.465');
INSERT INTO wms_advence_master.wms_inventory_history (id,warehouse_id,area_id,rack_id,location_id,item_instance_id,box_id,before_quantity,after_quantity,operation_type,operator_name,sku_id,quantity,unit_price,line_amount,remark,order_id,order_no,order_type,create_time)
 VALUES (2058937016595546113,1828364459110469633,2056770261064429569,2056771221887201282,2056771221887201284,2058920374624878598,2058930926625521665,null,null,'borrow','a',2058919798067462147,-1,null,null,'fdf',2058937016222253058,'BRWH12058937016234835968',5,'2026-05-25 23:43:21.000');


-- Name: wms_item; Type: Table; Schema: wms_advence_master;

SET search_path = wms_advence_master;
CREATE TABLE wms_item (
    id bigint AUTO_INCREMENT NOT NULL,
    item_code varchar(20) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci NOT NULL,
    item_name varchar(60) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci NOT NULL,
    item_category varchar(20) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    unit varchar(20) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    equipment_name varchar(128) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    equipment_type varchar(32) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    status varchar(32) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    remark varchar(255) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    create_by varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    create_time timestamp(3) without time zone DEFAULT NULL::timestamp without time zone,
    update_by varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    update_time timestamp(3) without time zone DEFAULT NULL::timestamp without time zone,
    CONSTRAINT wms_item_pk PRIMARY KEY (id)
) AUTO_INCREMENT = 2058919797824192515
CHARACTER SET = "UTF8" COLLATE = "utf8mb4_general_ci"
WITH (orientation=row, compression=no, fillfactor=80);
COMMENT ON TABLE wms_item IS '器材';
COMMENT ON COLUMN wms_item.item_code IS '器材编码';
COMMENT ON COLUMN wms_item.item_name IS '器材名称';
COMMENT ON COLUMN wms_item.item_category IS '分类';
COMMENT ON COLUMN wms_item.unit IS '计量单位';
COMMENT ON COLUMN wms_item.equipment_name IS '装备名称';
COMMENT ON COLUMN wms_item.equipment_type IS '器材类型：通装/专装';
COMMENT ON COLUMN wms_item.status IS '启用状态';
COMMENT ON COLUMN wms_item.remark IS '备注';
COMMENT ON COLUMN wms_item.create_by IS '创建人';
COMMENT ON COLUMN wms_item.create_time IS '创建时间';
COMMENT ON COLUMN wms_item.update_by IS '修改人';
COMMENT ON COLUMN wms_item.update_time IS '修改时间';
CREATE INDEX tb0_wms_item_idx_wms_item_status ON wms_advence_master.wms_item USING btree (status) TABLESPACE pg_default;
CREATE INDEX tb0_wms_item_idx_wms_item_equipment_type ON wms_advence_master.wms_item USING btree (equipment_type) TABLESPACE pg_default;
ALTER TABLE wms_item ADD CONSTRAINT wms_item_uk1 UNIQUE USING btree (item_code);

--Data for  Name: wms_item; Type: Table; Schema: wms_advence_master;

INSERT INTO wms_advence_master.wms_item (id,item_code,item_name,item_category,unit,equipment_name,equipment_type,status,remark,create_by,create_time,update_by,update_time)
 VALUES (2056768878227566594,'box','器材箱','2054732521107996674','个',null,null,'1',null,'admin','2026-05-20 00:08:08.145','admin','2026-05-20 00:08:08.145');
INSERT INTO wms_advence_master.wms_item (id,item_code,item_name,item_category,unit,equipment_name,equipment_type,status,remark,create_by,create_time,update_by,update_time)
 VALUES (2056774315211571201,'HUI-DYJ','惠普打印机','1828365014901886978','台',null,null,'1',null,'admin','2026-05-20 00:29:44.432','admin','2026-05-22 00:06:10.003');
INSERT INTO wms_advence_master.wms_item (id,item_code,item_name,item_category,unit,equipment_name,equipment_type,status,remark,create_by,create_time,update_by,update_time)
 VALUES (2058919797824192514,'DN','苹果电脑','1828365043024695297','台',null,null,'1','z','admin','2026-05-25 22:35:07.339','admin','2026-05-25 22:35:07.339');


-- Name: wms_item_category; Type: Table; Schema: wms_advence_master;

SET search_path = wms_advence_master;
CREATE TABLE wms_item_category (
    id bigint AUTO_INCREMENT NOT NULL,
    parent_id bigint DEFAULT 0::bigint,
    category_name varchar(30) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT ''::varchar,
    order_num integer DEFAULT 0,
    status character(1) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT '1'::bpchar,
    create_by varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    create_time timestamp(3) without time zone DEFAULT NULL::timestamp without time zone,
    update_by varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    update_time timestamp(3) without time zone DEFAULT NULL::timestamp without time zone,
    CONSTRAINT wms_item_category_pk PRIMARY KEY (id)
) AUTO_INCREMENT = 2054732521107996675
CHARACTER SET = "UTF8" COLLATE = "utf8mb4_general_ci"
WITH (orientation=row, compression=no, fillfactor=80);
COMMENT ON TABLE wms_item_category IS '器材类型表';
COMMENT ON COLUMN wms_item_category.id IS '器材类型id';
COMMENT ON COLUMN wms_item_category.parent_id IS '父器材类型id';
COMMENT ON COLUMN wms_item_category.category_name IS '器材类型名称';
COMMENT ON COLUMN wms_item_category.order_num IS '显示顺序';
COMMENT ON COLUMN wms_item_category.status IS '器材类型状态（0停用 1正常）';
COMMENT ON COLUMN wms_item_category.create_by IS '创建者';
COMMENT ON COLUMN wms_item_category.create_time IS '创建时间';
COMMENT ON COLUMN wms_item_category.update_by IS '更新者';
COMMENT ON COLUMN wms_item_category.update_time IS '更新时间';

--Data for  Name: wms_item_category; Type: Table; Schema: wms_advence_master;

INSERT INTO wms_advence_master.wms_item_category (id,parent_id,category_name,order_num,status,create_by,create_time,update_by,update_time)
 VALUES (1828365014901886978,0,'打印机',1,'1','admin','2024-08-27 17:32:43.598','admin','2024-08-27 20:14:12.447');
INSERT INTO wms_advence_master.wms_item_category (id,parent_id,category_name,order_num,status,create_by,create_time,update_by,update_time)
 VALUES (1828365043024695297,0,'电脑',3,'1','admin','2024-08-27 17:32:50.301','admin','2024-08-27 20:14:12.704');
INSERT INTO wms_advence_master.wms_item_category (id,parent_id,category_name,order_num,status,create_by,create_time,update_by,update_time)
 VALUES (1828405743737016322,0,'家电',4,'1','admin','2024-08-27 20:14:34.104','admin','2024-08-27 20:14:34.104');
INSERT INTO wms_advence_master.wms_item_category (id,parent_id,category_name,order_num,status,create_by,create_time,update_by,update_time)
 VALUES (1828408600515219457,0,'健身器材',5,'1','admin','2024-08-27 20:25:55.213','admin','2026-05-08 14:23:49.560');
INSERT INTO wms_advence_master.wms_item_category (id,parent_id,category_name,order_num,status,create_by,create_time,update_by,update_time)
 VALUES (2054732521107996674,0,'器材箱',6,'1','admin','2026-05-14 09:16:22.782','admin','2026-05-14 09:16:22.782');


-- Name: wms_item_instance; Type: Table; Schema: wms_advence_master;

SET search_path = wms_advence_master;
CREATE TABLE wms_item_instance (
    id bigint AUTO_INCREMENT NOT NULL,
    instance_code varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_0900_ai_ci NOT NULL,
    item_id bigint NOT NULL,
    sku_id bigint NOT NULL,
    instance_status varchar(32) CHARACTER SET "UTF8" COLLATE utf8mb4_0900_ai_ci DEFAULT '待入库'::varchar,
    warehouse_id bigint,
    area_id bigint,
    rack_id bigint,
    location_id bigint,
    source_type varchar(32) CHARACTER SET "UTF8" COLLATE utf8mb4_0900_ai_ci DEFAULT NULL::varchar,
    source_order_id bigint,
    source_order_no varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_0900_ai_ci DEFAULT NULL::varchar,
    receipt_order_detail_id bigint,
    shipment_order_detail_id bigint,
    source_order_type varchar(32) CHARACTER SET "UTF8" COLLATE utf8mb4_0900_ai_ci DEFAULT NULL::varchar,
    box_id bigint,
    remark varchar(255) CHARACTER SET "UTF8" COLLATE utf8mb4_0900_ai_ci DEFAULT NULL::varchar,
    create_by varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_0900_ai_ci DEFAULT NULL::varchar,
    create_time timestamp(3) without time zone DEFAULT NULL::timestamp without time zone,
    update_by varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_0900_ai_ci DEFAULT NULL::varchar,
    update_time timestamp(3) without time zone DEFAULT NULL::timestamp without time zone,
    CONSTRAINT wms_item_instance_pk PRIMARY KEY (id)
) AUTO_INCREMENT = 2058920403737542662
CHARACTER SET = "UTF8" COLLATE = "utf8mb4_0900_ai_ci"
WITH (orientation=row, compression=no, fillfactor=80);
COMMENT ON TABLE wms_item_instance IS '器材实例';
COMMENT ON COLUMN wms_item_instance.instance_code IS '器材实例编码';
COMMENT ON COLUMN wms_item_instance.item_id IS '器材ID';
COMMENT ON COLUMN wms_item_instance.sku_id IS '规格ID';
COMMENT ON COLUMN wms_item_instance.instance_status IS '器材实例状态';
COMMENT ON COLUMN wms_item_instance.warehouse_id IS '所属仓库';
COMMENT ON COLUMN wms_item_instance.area_id IS '所属库区';
COMMENT ON COLUMN wms_item_instance.rack_id IS '所属货架';
COMMENT ON COLUMN wms_item_instance.location_id IS '所属货位';
COMMENT ON COLUMN wms_item_instance.source_type IS '来源类型';
COMMENT ON COLUMN wms_item_instance.source_order_id IS '来源单据ID';
COMMENT ON COLUMN wms_item_instance.source_order_no IS '来源单据号';
COMMENT ON COLUMN wms_item_instance.receipt_order_detail_id IS '来源入库单明细ID';
COMMENT ON COLUMN wms_item_instance.shipment_order_detail_id IS '来源出库单明细ID';
COMMENT ON COLUMN wms_item_instance.source_order_type IS '来源单据类型';
COMMENT ON COLUMN wms_item_instance.box_id IS '当前所在箱体ID';
COMMENT ON COLUMN wms_item_instance.remark IS '备注';
COMMENT ON COLUMN wms_item_instance.create_by IS '创建人';
COMMENT ON COLUMN wms_item_instance.create_time IS '创建时间';
COMMENT ON COLUMN wms_item_instance.update_by IS '修改人';
COMMENT ON COLUMN wms_item_instance.update_time IS '修改时间';
CREATE INDEX tb0_wms_item_instance_idx_wms_item_instance_source_order_id ON wms_advence_master.wms_item_instance USING btree (source_order_id) TABLESPACE pg_default;
CREATE INDEX tb0_wms_item_instance_idx_wms_item_instance_source_order ON wms_advence_master.wms_item_instance USING btree (source_order_type, source_order_id) TABLESPACE pg_default;
CREATE INDEX tb0_wms_item_instance_idx_wms_item_instance_item_sku ON wms_advence_master.wms_item_instance USING btree (item_id, sku_id) TABLESPACE pg_default;
CREATE INDEX tb0_wms_item_instance_idx_wms_item_instance_sku_id ON wms_advence_master.wms_item_instance USING btree (sku_id) TABLESPACE pg_default;
CREATE INDEX tb0_wms_item_instance_idx_wms_item_instance_location_id ON wms_advence_master.wms_item_instance USING btree (location_id) TABLESPACE pg_default;
CREATE INDEX tb0_wms_item_instance_idx_wms_item_instance_box_id ON wms_advence_master.wms_item_instance USING btree (box_id) TABLESPACE pg_default;
CREATE INDEX tb0_wms_item_instance_idx_wms_item_instance_location ON wms_advence_master.wms_item_instance USING btree (warehouse_id, area_id, rack_id, location_id) TABLESPACE pg_default;
CREATE INDEX tb0_wms_item_instance_idx_wms_item_instance_receipt_detail_id ON wms_advence_master.wms_item_instance USING btree (receipt_order_detail_id) TABLESPACE pg_default;
CREATE INDEX tb0_wms_item_instance_idx_wms_item_instance_item_id ON wms_advence_master.wms_item_instance USING btree (item_id) TABLESPACE pg_default;
CREATE INDEX tb0_wms_item_instance_idx_wms_item_instance_shipment_detail ON wms_advence_master.wms_item_instance USING btree (shipment_order_detail_id) TABLESPACE pg_default;
ALTER TABLE wms_item_instance ADD CONSTRAINT wms_item_instance_uk1 UNIQUE USING btree (instance_code);

--Data for  Name: wms_item_instance; Type: Table; Schema: wms_advence_master;

INSERT INTO wms_advence_master.wms_item_instance (id,instance_code,item_id,sku_id,instance_status,warehouse_id,area_id,rack_id,location_id,source_type,source_order_id,source_order_no,receipt_order_detail_id,shipment_order_detail_id,source_order_type,box_id,remark,create_by,create_time,update_by,update_time)
 VALUES (2058920331998167041,'ITEMWH11000000001',2058919797824192514,2058919798067462146,'待入库',null,null,null,null,null,null,null,null,null,null,null,'z','admin','2026-05-25 22:37:14.709','admin','2026-05-25 22:37:14.709');
INSERT INTO wms_advence_master.wms_item_instance (id,instance_code,item_id,sku_id,instance_status,warehouse_id,area_id,rack_id,location_id,source_type,source_order_id,source_order_no,receipt_order_detail_id,shipment_order_detail_id,source_order_type,box_id,remark,create_by,create_time,update_by,update_time)
 VALUES (2058920374561964033,'ITEMWH11000000002',2058919797824192514,2058919798067462147,'待入库',null,null,null,null,null,null,null,null,null,null,null,'z','admin','2026-05-25 22:37:24.851','admin','2026-05-25 22:37:24.851');
INSERT INTO wms_advence_master.wms_item_instance (id,instance_code,item_id,sku_id,instance_status,warehouse_id,area_id,rack_id,location_id,source_type,source_order_id,source_order_no,receipt_order_detail_id,shipment_order_detail_id,source_order_type,box_id,remark,create_by,create_time,update_by,update_time)
 VALUES (2058920374561964034,'ITEMWH11000000003',2058919797824192514,2058919798067462147,'待入库',null,null,null,null,null,null,null,null,null,null,null,'z','admin','2026-05-25 22:37:24.853','admin','2026-05-25 22:37:24.853');
INSERT INTO wms_advence_master.wms_item_instance (id,instance_code,item_id,sku_id,instance_status,warehouse_id,area_id,rack_id,location_id,source_type,source_order_id,source_order_no,receipt_order_detail_id,shipment_order_detail_id,source_order_type,box_id,remark,create_by,create_time,update_by,update_time)
 VALUES (2058920374561964035,'ITEMWH11000000004',2058919797824192514,2058919798067462147,'待入库',null,null,null,null,null,null,null,null,null,null,null,'z','admin','2026-05-25 22:37:24.855','admin','2026-05-25 22:37:24.855');
INSERT INTO wms_advence_master.wms_item_instance (id,instance_code,item_id,sku_id,instance_status,warehouse_id,area_id,rack_id,location_id,source_type,source_order_id,source_order_no,receipt_order_detail_id,shipment_order_detail_id,source_order_type,box_id,remark,create_by,create_time,update_by,update_time)
 VALUES (2058920374624878594,'ITEMWH11000000005',2058919797824192514,2058919798067462147,'待入库',null,null,null,null,null,null,null,null,null,null,null,'z','admin','2026-05-25 22:37:24.858','admin','2026-05-25 22:37:24.858');
INSERT INTO wms_advence_master.wms_item_instance (id,instance_code,item_id,sku_id,instance_status,warehouse_id,area_id,rack_id,location_id,source_type,source_order_id,source_order_no,receipt_order_detail_id,shipment_order_detail_id,source_order_type,box_id,remark,create_by,create_time,update_by,update_time)
 VALUES (2058920374624878595,'ITEMWH11000000006',2058919797824192514,2058919798067462147,'待入库',null,null,null,null,null,null,null,null,null,null,null,'z','admin','2026-05-25 22:37:24.862','admin','2026-05-25 22:37:24.862');
INSERT INTO wms_advence_master.wms_item_instance (id,instance_code,item_id,sku_id,instance_status,warehouse_id,area_id,rack_id,location_id,source_type,source_order_id,source_order_no,receipt_order_detail_id,shipment_order_detail_id,source_order_type,box_id,remark,create_by,create_time,update_by,update_time)
 VALUES (2058920374624878596,'ITEMWH11000000007',2058919797824192514,2058919798067462147,'待入库',null,null,null,null,null,null,null,null,null,null,null,'z','admin','2026-05-25 22:37:24.865','admin','2026-05-25 22:37:24.865');
INSERT INTO wms_advence_master.wms_item_instance (id,instance_code,item_id,sku_id,instance_status,warehouse_id,area_id,rack_id,location_id,source_type,source_order_id,source_order_no,receipt_order_detail_id,shipment_order_detail_id,source_order_type,box_id,remark,create_by,create_time,update_by,update_time)
 VALUES (2058920374624878600,'ITEMWH11000000011',2058919797824192514,2058919798067462147,'待入库',null,null,null,null,null,null,null,null,null,null,null,'z','admin','2026-05-25 22:37:24.872','admin','2026-05-25 22:37:24.872');
INSERT INTO wms_advence_master.wms_item_instance (id,instance_code,item_id,sku_id,instance_status,warehouse_id,area_id,rack_id,location_id,source_type,source_order_id,source_order_no,receipt_order_detail_id,shipment_order_detail_id,source_order_type,box_id,remark,create_by,create_time,update_by,update_time)
 VALUES (2058920403737542658,'ITEMWH11000000012',2058919797824192514,2058919798067462146,'待入库',null,null,null,null,null,null,null,null,null,null,null,'z','admin','2026-05-25 22:37:31.806','admin','2026-05-25 22:37:31.806');
INSERT INTO wms_advence_master.wms_item_instance (id,instance_code,item_id,sku_id,instance_status,warehouse_id,area_id,rack_id,location_id,source_type,source_order_id,source_order_no,receipt_order_detail_id,shipment_order_detail_id,source_order_type,box_id,remark,create_by,create_time,update_by,update_time)
 VALUES (2058920403737542659,'ITEMWH11000000013',2058919797824192514,2058919798067462146,'在库',1828364459110469633,2056770261064429569,2056771221887201282,2056771221887201283,'入库单',2058929554794512385,'RKWH12058929554542854144',2058929555029393411,null,'入库单',2058930926243840002,'a','admin','2026-05-25 22:37:31.807','admin','2026-05-25 23:19:21.170');
INSERT INTO wms_advence_master.wms_item_instance (id,instance_code,item_id,sku_id,instance_status,warehouse_id,area_id,rack_id,location_id,source_type,source_order_id,source_order_no,receipt_order_detail_id,shipment_order_detail_id,source_order_type,box_id,remark,create_by,create_time,update_by,update_time)
 VALUES (2058920403737542661,'ITEMWH11000000015',2058919797824192514,2058919798067462146,'出库',null,null,null,null,'入库单',2058929554794512385,'RKWH12058929554542854144',2058929555029393409,2058933291709661185,'入库单',null,'a','admin','2026-05-25 22:37:31.809','admin','2026-05-25 23:28:44.731');
INSERT INTO wms_advence_master.wms_item_instance (id,instance_code,item_id,sku_id,instance_status,warehouse_id,area_id,rack_id,location_id,source_type,source_order_id,source_order_no,receipt_order_detail_id,shipment_order_detail_id,source_order_type,box_id,remark,create_by,create_time,update_by,update_time)
 VALUES (2058920374624878599,'ITEMWH11000000010',2058919797824192514,2058919798067462147,'在库',1828364459110469633,2056770261064429569,2056771221887201282,2056771221887201284,'入库单',2058929554794512385,'RKWH12058929554542854144',2058929555029393413,null,'入库单',2058930926625521665,'a','admin','2026-05-25 22:37:24.871','admin','2026-05-25 23:19:21.171');
INSERT INTO wms_advence_master.wms_item_instance (id,instance_code,item_id,sku_id,instance_status,warehouse_id,area_id,rack_id,location_id,source_type,source_order_id,source_order_no,receipt_order_detail_id,shipment_order_detail_id,source_order_type,box_id,remark,create_by,create_time,update_by,update_time)
 VALUES (2058920403737542660,'ITEMWH11000000014',2058919797824192514,2058919798067462146,'在库',2056769387227328513,2056770367725580289,null,null,'调拨单',2058933863678509057,'DBWH12058933863439433728',null,null,'调拨单',null,'a','admin','2026-05-25 22:37:31.807','admin','2026-05-25 23:19:21.169');
INSERT INTO wms_advence_master.wms_item_instance (id,instance_code,item_id,sku_id,instance_status,warehouse_id,area_id,rack_id,location_id,source_type,source_order_id,source_order_no,receipt_order_detail_id,shipment_order_detail_id,source_order_type,box_id,remark,create_by,create_time,update_by,update_time)
 VALUES (2058920374624878597,'ITEMWH11000000008',2058919797824192514,2058919798067462147,'在库',2056769387227328513,2056770392228704258,null,null,'调拨单',2058935449213501441,'DBWH12058935449062506496',null,null,'调拨单',null,'z','admin','2026-05-25 22:37:24.870','admin','2026-05-25 23:19:21.172');
INSERT INTO wms_advence_master.wms_item_instance (id,instance_code,item_id,sku_id,instance_status,warehouse_id,area_id,rack_id,location_id,source_type,source_order_id,source_order_no,receipt_order_detail_id,shipment_order_detail_id,source_order_type,box_id,remark,create_by,create_time,update_by,update_time)
 VALUES (2058920374624878598,'ITEMWH11000000009',2058919797824192514,2058919798067462147,'借出',null,null,null,null,'入库单',2058929554794512385,'RKWH12058929554542854144',2058929555029393412,null,'入库单',null,'a','admin','2026-05-25 22:37:24.871','admin','2026-05-25 23:19:21.170');


-- Name: wms_item_qr_code_serial; Type: Table; Schema: wms_advence_master;

SET search_path = wms_advence_master;
CREATE TABLE wms_item_qr_code_serial (
    item_key varchar(255) CHARACTER SET "UTF8" COLLATE utf8mb4_0900_ai_ci NOT NULL,
    current_value bigint DEFAULT 1000000001::bigint NOT NULL
)
CHARACTER SET = "UTF8" COLLATE = "utf8mb4_0900_ai_ci"
WITH (orientation=row, compression=no, fillfactor=80);
COMMENT ON TABLE wms_item_qr_code_serial IS '器材二维码序列表';
COMMENT ON COLUMN wms_item_qr_code_serial.item_key IS '器材名称+规格名称唯一键';
COMMENT ON COLUMN wms_item_qr_code_serial.current_value IS '当前可分配的唯一值，默认从1000000001开始';
ALTER TABLE wms_item_qr_code_serial ADD CONSTRAINT wms_item_qr_code_serial_pk PRIMARY KEY USING btree  (item_key);

--Data for  Name: wms_item_qr_code_serial; Type: Table; Schema: wms_advence_master;

INSERT INTO wms_advence_master.wms_item_qr_code_serial (item_key,current_value)
 VALUES ('ITEMWH1',1000000016);
INSERT INTO wms_advence_master.wms_item_qr_code_serial (item_key,current_value)
 VALUES ('RACKWH1',1000000002);


-- Name: wms_item_sku; Type: Table; Schema: wms_advence_master;

SET search_path = wms_advence_master;
CREATE TABLE wms_item_sku (
    id bigint AUTO_INCREMENT NOT NULL,
    sku_name varchar(255) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    product_identifier varchar(128) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    quality_grade varchar(32) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    item_id bigint,
    status varchar(32) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    create_by varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    create_time timestamp(3) without time zone DEFAULT NULL::timestamp without time zone,
    update_by varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    update_time timestamp(3) without time zone DEFAULT NULL::timestamp without time zone,
    CONSTRAINT wms_item_sku_pk PRIMARY KEY (id)
) AUTO_INCREMENT = 2058919798067462148
CHARACTER SET = "UTF8" COLLATE = "utf8mb4_general_ci"
WITH (orientation=row, compression=no, fillfactor=80);
COMMENT ON TABLE wms_item_sku IS '器材规格信息';
COMMENT ON COLUMN wms_item_sku.sku_name IS '规格名称';
COMMENT ON COLUMN wms_item_sku.product_identifier IS '产品标识';
COMMENT ON COLUMN wms_item_sku.quality_grade IS '质量等级';
COMMENT ON COLUMN wms_item_sku.item_id IS '器材id';
COMMENT ON COLUMN wms_item_sku.status IS '启用状态';
COMMENT ON COLUMN wms_item_sku.create_by IS '创建人';
COMMENT ON COLUMN wms_item_sku.create_time IS '创建时间';
COMMENT ON COLUMN wms_item_sku.update_by IS '修改人';
COMMENT ON COLUMN wms_item_sku.update_time IS '修改时间';
CREATE INDEX tb0_wms_item_sku_idx_wms_item_sku_item_id ON wms_advence_master.wms_item_sku USING btree (item_id) TABLESPACE pg_default;
CREATE INDEX tb0_wms_item_sku_idx_wms_item_sku_status ON wms_advence_master.wms_item_sku USING btree (status) TABLESPACE pg_default;

--Data for  Name: wms_item_sku; Type: Table; Schema: wms_advence_master;

INSERT INTO wms_advence_master.wms_item_sku (id,sku_name,product_identifier,quality_grade,item_id,status,create_by,create_time,update_by,update_time)
 VALUES (2056768878252732418,'大','','',2056768878227566594,'1','admin','2026-05-20 00:08:08.164','admin','2026-05-20 00:08:08.164');
INSERT INTO wms_advence_master.wms_item_sku (id,sku_name,product_identifier,quality_grade,item_id,status,create_by,create_time,update_by,update_time)
 VALUES (2056768878315646977,'小','','',2056768878227566594,'1','admin','2026-05-20 00:08:08.166','admin','2026-05-20 00:08:08.166');
INSERT INTO wms_advence_master.wms_item_sku (id,sku_name,product_identifier,quality_grade,item_id,status,create_by,create_time,update_by,update_time)
 VALUES (2056774315278680066,'2023','xx','高',2056774315211571201,'1','admin','2026-05-20 00:29:44.447','admin','2026-05-22 00:06:10.031');
INSERT INTO wms_advence_master.wms_item_sku (id,sku_name,product_identifier,quality_grade,item_id,status,create_by,create_time,update_by,update_time)
 VALUES (2057493158586335233,'2024','yy','低',2056774315211571201,'1','admin','2026-05-22 00:06:10.039','admin','2026-05-22 00:06:10.039');
INSERT INTO wms_advence_master.wms_item_sku (id,sku_name,product_identifier,quality_grade,item_id,status,create_by,create_time,update_by,update_time)
 VALUES (2058919798067462146,'1T','高标识','高',2058919797824192514,'1','admin','2026-05-25 22:35:07.406','admin','2026-05-25 22:35:07.406');
INSERT INTO wms_advence_master.wms_item_sku (id,sku_name,product_identifier,quality_grade,item_id,status,create_by,create_time,update_by,update_time)
 VALUES (2058919798067462147,'2T','低标识','低',2058919797824192514,'1','admin','2026-05-25 22:35:07.410','admin','2026-05-25 22:35:07.410');


-- Name: wms_location; Type: Table; Schema: wms_advence_master;

SET search_path = wms_advence_master;
CREATE TABLE wms_location (
    id bigint AUTO_INCREMENT NOT NULL,
    location_code varchar(32) CHARACTER SET "UTF8" COLLATE utf8mb4_0900_ai_ci DEFAULT NULL::varchar,
    location_name varchar(60) CHARACTER SET "UTF8" COLLATE utf8mb4_0900_ai_ci NOT NULL,
    warehouse_id bigint NOT NULL,
    area_id bigint NOT NULL,
    rack_id bigint NOT NULL,
    location_status varchar(32) CHARACTER SET "UTF8" COLLATE utf8mb4_0900_ai_ci DEFAULT 'enabled'::varchar,
    row_no integer,
    column_no integer,
    length numeric(18,2) DEFAULT NULL::numeric,
    width numeric(18,2) DEFAULT NULL::numeric,
    height numeric(18,2) DEFAULT NULL::numeric,
    occupied_flag smallint DEFAULT 0::smallint,
    sort_no bigint,
    remark varchar(255) CHARACTER SET "UTF8" COLLATE utf8mb4_0900_ai_ci DEFAULT NULL::varchar,
    create_by varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_0900_ai_ci DEFAULT NULL::varchar,
    create_time timestamp(3) without time zone DEFAULT NULL::timestamp without time zone,
    update_by varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_0900_ai_ci DEFAULT NULL::varchar,
    update_time timestamp(3) without time zone DEFAULT NULL::timestamp without time zone,
    CONSTRAINT wms_location_pk PRIMARY KEY (id)
) AUTO_INCREMENT = 2058927018364665859
CHARACTER SET = "UTF8" COLLATE = "utf8mb4_0900_ai_ci"
WITH (orientation=row, compression=no, fillfactor=80);
COMMENT ON TABLE wms_location IS '货位';
COMMENT ON COLUMN wms_location.location_code IS '货位编码';
COMMENT ON COLUMN wms_location.location_name IS '货位名称';
COMMENT ON COLUMN wms_location.warehouse_id IS '所属仓库';
COMMENT ON COLUMN wms_location.area_id IS '所属库区';
COMMENT ON COLUMN wms_location.rack_id IS '所属货架';
COMMENT ON COLUMN wms_location.location_status IS '货位状态';
COMMENT ON COLUMN wms_location.row_no IS '行号';
COMMENT ON COLUMN wms_location.column_no IS '列号';
COMMENT ON COLUMN wms_location.length IS '长';
COMMENT ON COLUMN wms_location.width IS '宽';
COMMENT ON COLUMN wms_location.height IS '高';
COMMENT ON COLUMN wms_location.occupied_flag IS '是否占用';
COMMENT ON COLUMN wms_location.sort_no IS '排序';
COMMENT ON COLUMN wms_location.remark IS '备注';
COMMENT ON COLUMN wms_location.create_by IS '创建人';
COMMENT ON COLUMN wms_location.create_time IS '创建时间';
COMMENT ON COLUMN wms_location.update_by IS '修改人';
COMMENT ON COLUMN wms_location.update_time IS '修改时间';
CREATE INDEX tb0_wms_location_idx_wms_location_warehouse_id ON wms_advence_master.wms_location USING btree (warehouse_id) TABLESPACE pg_default;
CREATE INDEX tb0_wms_location_idx_wms_location_rack_id ON wms_advence_master.wms_location USING btree (rack_id) TABLESPACE pg_default;
CREATE INDEX tb0_wms_location_idx_wms_location_status ON wms_advence_master.wms_location USING btree (location_status) TABLESPACE pg_default;
CREATE INDEX tb0_wms_location_idx_wms_location_occupied ON wms_advence_master.wms_location USING btree (occupied_flag) TABLESPACE pg_default;
CREATE INDEX tb0_wms_location_idx_wms_location_area_id ON wms_advence_master.wms_location USING btree (area_id) TABLESPACE pg_default;
ALTER TABLE wms_location ADD CONSTRAINT wms_location_uk1 UNIQUE USING btree (warehouse_id, area_id, rack_id, location_code);
ALTER TABLE wms_location ADD CONSTRAINT wms_location_uk2 UNIQUE USING btree (rack_id, row_no, column_no);

--Data for  Name: wms_location; Type: Table; Schema: wms_advence_master;

INSERT INTO wms_advence_master.wms_location (id,location_code,location_name,warehouse_id,area_id,rack_id,location_status,row_no,column_no,length,width,height,occupied_flag,sort_no,remark,create_by,create_time,update_by,update_time)
 VALUES (2056771039846019074,'WH1RACK1000000001-R1-C1','京A货1-1-1',1828364459110469633,2056770222548135937,2056771039778910209,'enabled',1,1,200,600,300,0,1001,null,'admin','2026-05-20 00:16:43.518','admin','2026-05-20 00:16:43.518');
INSERT INTO wms_advence_master.wms_location (id,location_code,location_name,warehouse_id,area_id,rack_id,location_status,row_no,column_no,length,width,height,occupied_flag,sort_no,remark,create_by,create_time,update_by,update_time)
 VALUES (2056771039846019075,'WH1RACK1000000001-R1-C2','京A货1-1-2',1828364459110469633,2056770222548135937,2056771039778910209,'enabled',1,2,200,600,300,0,1002,null,'admin','2026-05-20 00:16:43.522','admin','2026-05-20 00:16:43.522');
INSERT INTO wms_advence_master.wms_location (id,location_code,location_name,warehouse_id,area_id,rack_id,location_status,row_no,column_no,length,width,height,occupied_flag,sort_no,remark,create_by,create_time,update_by,update_time)
 VALUES (2056771039846019076,'WH1RACK1000000001-R1-C3','京A货1-1-3',1828364459110469633,2056770222548135937,2056771039778910209,'enabled',1,3,200,600,300,0,1003,null,'admin','2026-05-20 00:16:43.525','admin','2026-05-20 00:16:43.525');
INSERT INTO wms_advence_master.wms_location (id,location_code,location_name,warehouse_id,area_id,rack_id,location_status,row_no,column_no,length,width,height,occupied_flag,sort_no,remark,create_by,create_time,update_by,update_time)
 VALUES (2056771039846019077,'WH1RACK1000000001-R2-C1','京A货1-2-1',1828364459110469633,2056770222548135937,2056771039778910209,'enabled',2,1,200,600,300,0,2001,null,'admin','2026-05-20 00:16:43.527','admin','2026-05-20 00:16:43.527');
INSERT INTO wms_advence_master.wms_location (id,location_code,location_name,warehouse_id,area_id,rack_id,location_status,row_no,column_no,length,width,height,occupied_flag,sort_no,remark,create_by,create_time,update_by,update_time)
 VALUES (2056771039846019078,'WH1RACK1000000001-R2-C2','京A货1-2-2',1828364459110469633,2056770222548135937,2056771039778910209,'enabled',2,2,200,600,300,0,2002,null,'admin','2026-05-20 00:16:43.531','admin','2026-05-20 00:16:43.531');
INSERT INTO wms_advence_master.wms_location (id,location_code,location_name,warehouse_id,area_id,rack_id,location_status,row_no,column_no,length,width,height,occupied_flag,sort_no,remark,create_by,create_time,update_by,update_time)
 VALUES (2056771039913127937,'WH1RACK1000000001-R2-C3','京A货1-2-3',1828364459110469633,2056770222548135937,2056771039778910209,'enabled',2,3,200,600,300,0,2003,null,'admin','2026-05-20 00:16:43.538','admin','2026-05-20 00:16:43.538');
INSERT INTO wms_advence_master.wms_location (id,location_code,location_name,warehouse_id,area_id,rack_id,location_status,row_no,column_no,length,width,height,occupied_flag,sort_no,remark,create_by,create_time,update_by,update_time)
 VALUES (2056771137254535170,'WH1RACK1000000002-R1-C1','京A货2-1-1',1828364459110469633,2056770222548135937,2056771137254535169,'enabled',1,1,200,200,200,0,1001,null,'admin','2026-05-20 00:17:06.742','admin','2026-05-20 00:17:06.742');
INSERT INTO wms_advence_master.wms_location (id,location_code,location_name,warehouse_id,area_id,rack_id,location_status,row_no,column_no,length,width,height,occupied_flag,sort_no,remark,create_by,create_time,update_by,update_time)
 VALUES (2056771221887201283,'WH1RACK1000000003-R1-C1','京B货1-1-1',1828364459110469633,2056770261064429569,2056771221887201282,'enabled',1,1,300,900,300,1,1001,null,'admin','2026-05-20 00:17:26.922','admin','2026-05-20 00:17:26.922');
INSERT INTO wms_advence_master.wms_location (id,location_code,location_name,warehouse_id,area_id,rack_id,location_status,row_no,column_no,length,width,height,occupied_flag,sort_no,remark,create_by,create_time,update_by,update_time)
 VALUES (2056771221887201285,'WH1RACK1000000003-R1-C3','京B货1-1-3',1828364459110469633,2056770261064429569,2056771221887201282,'enabled',1,3,300,900,300,0,1003,null,'admin','2026-05-20 00:17:26.928','admin','2026-05-20 00:17:26.928');
INSERT INTO wms_advence_master.wms_location (id,location_code,location_name,warehouse_id,area_id,rack_id,location_status,row_no,column_no,length,width,height,occupied_flag,sort_no,remark,create_by,create_time,update_by,update_time)
 VALUES (2056771221937532930,'WH1RACK1000000003-R2-C1','京B货1-2-1',1828364459110469633,2056770261064429569,2056771221887201282,'enabled',2,1,300,900,300,0,2001,null,'admin','2026-05-20 00:17:26.931','admin','2026-05-20 00:17:26.931');
INSERT INTO wms_advence_master.wms_location (id,location_code,location_name,warehouse_id,area_id,rack_id,location_status,row_no,column_no,length,width,height,occupied_flag,sort_no,remark,create_by,create_time,update_by,update_time)
 VALUES (2056771221937532931,'WH1RACK1000000003-R2-C2','京B货1-2-2',1828364459110469633,2056770261064429569,2056771221887201282,'enabled',2,2,300,900,300,0,2002,null,'admin','2026-05-20 00:17:26.936','admin','2026-05-20 00:17:26.936');
INSERT INTO wms_advence_master.wms_location (id,location_code,location_name,warehouse_id,area_id,rack_id,location_status,row_no,column_no,length,width,height,occupied_flag,sort_no,remark,create_by,create_time,update_by,update_time)
 VALUES (2056771221937532932,'WH1RACK1000000003-R2-C3','京B货1-2-3',1828364459110469633,2056770261064429569,2056771221887201282,'enabled',2,3,300,900,300,0,2003,null,'admin','2026-05-20 00:17:26.939','admin','2026-05-20 00:17:26.939');
INSERT INTO wms_advence_master.wms_location (id,location_code,location_name,warehouse_id,area_id,rack_id,location_status,row_no,column_no,length,width,height,occupied_flag,sort_no,remark,create_by,create_time,update_by,update_time)
 VALUES (2056771221937532933,'WH1RACK1000000003-R3-C1','京B货1-3-1',1828364459110469633,2056770261064429569,2056771221887201282,'enabled',3,1,300,900,300,0,3001,null,'admin','2026-05-20 00:17:26.942','admin','2026-05-20 00:17:26.942');
INSERT INTO wms_advence_master.wms_location (id,location_code,location_name,warehouse_id,area_id,rack_id,location_status,row_no,column_no,length,width,height,occupied_flag,sort_no,remark,create_by,create_time,update_by,update_time)
 VALUES (2056771221937532934,'WH1RACK1000000003-R3-C2','京B货1-3-2',1828364459110469633,2056770261064429569,2056771221887201282,'enabled',3,2,300,900,300,0,3002,null,'admin','2026-05-20 00:17:26.945','admin','2026-05-20 00:17:26.945');
INSERT INTO wms_advence_master.wms_location (id,location_code,location_name,warehouse_id,area_id,rack_id,location_status,row_no,column_no,length,width,height,occupied_flag,sort_no,remark,create_by,create_time,update_by,update_time)
 VALUES (2056771222004641794,'WH1RACK1000000003-R3-C3','京B货1-3-3',1828364459110469633,2056770261064429569,2056771221887201282,'enabled',3,3,300,900,300,0,3003,null,'admin','2026-05-20 00:17:26.947','admin','2026-05-20 00:17:26.947');
INSERT INTO wms_advence_master.wms_location (id,location_code,location_name,warehouse_id,area_id,rack_id,location_status,row_no,column_no,length,width,height,occupied_flag,sort_no,remark,create_by,create_time,update_by,update_time)
 VALUES (2056771350060937219,'WH1RACK1000000004-R1-C1','海A货1-1-1',2056769387227328513,2056770367725580289,2056771350060937218,'enabled',1,1,50,50,50,0,1001,null,'admin','2026-05-20 00:17:57.482','admin','2026-05-20 00:17:57.482');
INSERT INTO wms_advence_master.wms_location (id,location_code,location_name,warehouse_id,area_id,rack_id,location_status,row_no,column_no,length,width,height,occupied_flag,sort_no,remark,create_by,create_time,update_by,update_time)
 VALUES (2056772480702689283,'WH1RACK1000000005-R1-C1','海B货1-1-1',2056769387227328513,2056770392228704258,2056772480702689282,'enabled',1,1,200,600,300,0,1001,null,'admin','2026-05-20 00:22:27.059','admin','2026-05-20 00:22:27.059');
INSERT INTO wms_advence_master.wms_location (id,location_code,location_name,warehouse_id,area_id,rack_id,location_status,row_no,column_no,length,width,height,occupied_flag,sort_no,remark,create_by,create_time,update_by,update_time)
 VALUES (2056772480702689284,'WH1RACK1000000005-R1-C2','海B货1-1-2',2056769387227328513,2056770392228704258,2056772480702689282,'enabled',1,2,200,600,300,0,1002,null,'admin','2026-05-20 00:22:27.063','admin','2026-05-20 00:22:27.063');
INSERT INTO wms_advence_master.wms_location (id,location_code,location_name,warehouse_id,area_id,rack_id,location_status,row_no,column_no,length,width,height,occupied_flag,sort_no,remark,create_by,create_time,update_by,update_time)
 VALUES (2056772480702689285,'WH1RACK1000000005-R1-C3','海B货1-1-3',2056769387227328513,2056770392228704258,2056772480702689282,'enabled',1,3,200,600,300,0,1003,null,'admin','2026-05-20 00:22:27.066','admin','2026-05-20 00:22:27.066');
INSERT INTO wms_advence_master.wms_location (id,location_code,location_name,warehouse_id,area_id,rack_id,location_status,row_no,column_no,length,width,height,occupied_flag,sort_no,remark,create_by,create_time,update_by,update_time)
 VALUES (2056772480702689286,'WH1RACK1000000005-R2-C1','海B货1-2-1',2056769387227328513,2056770392228704258,2056772480702689282,'enabled',2,1,200,600,300,0,2001,null,'admin','2026-05-20 00:22:27.068','admin','2026-05-20 00:22:27.068');
INSERT INTO wms_advence_master.wms_location (id,location_code,location_name,warehouse_id,area_id,rack_id,location_status,row_no,column_no,length,width,height,occupied_flag,sort_no,remark,create_by,create_time,update_by,update_time)
 VALUES (2056772480702689287,'WH1RACK1000000005-R2-C2','海B货1-2-2',2056769387227328513,2056770392228704258,2056772480702689282,'enabled',2,2,200,600,300,0,2002,null,'admin','2026-05-20 00:22:27.072','admin','2026-05-20 00:22:27.072');
INSERT INTO wms_advence_master.wms_location (id,location_code,location_name,warehouse_id,area_id,rack_id,location_status,row_no,column_no,length,width,height,occupied_flag,sort_no,remark,create_by,create_time,update_by,update_time)
 VALUES (2056772480702689288,'WH1RACK1000000005-R2-C3','海B货1-2-3',2056769387227328513,2056770392228704258,2056772480702689282,'enabled',2,3,200,600,300,0,2003,null,'admin','2026-05-20 00:22:27.074','admin','2026-05-20 00:22:27.074');
INSERT INTO wms_advence_master.wms_location (id,location_code,location_name,warehouse_id,area_id,rack_id,location_status,row_no,column_no,length,width,height,occupied_flag,sort_no,remark,create_by,create_time,update_by,update_time)
 VALUES (2056772972304482306,'WH1RACK1000000006-R1-C1','海B货2-1-1',2056769387227328513,2056770392228704258,2056772972287705090,'enabled',1,1,200,600,300,0,1001,null,'admin','2026-05-20 00:24:24.259','admin','2026-05-20 00:24:24.259');
INSERT INTO wms_advence_master.wms_location (id,location_code,location_name,warehouse_id,area_id,rack_id,location_status,row_no,column_no,length,width,height,occupied_flag,sort_no,remark,create_by,create_time,update_by,update_time)
 VALUES (2056772972304482307,'WH1RACK1000000006-R1-C2','海B货2-1-2',2056769387227328513,2056770392228704258,2056772972287705090,'enabled',1,2,200,600,300,0,1002,null,'admin','2026-05-20 00:24:24.263','admin','2026-05-20 00:24:24.263');
INSERT INTO wms_advence_master.wms_location (id,location_code,location_name,warehouse_id,area_id,rack_id,location_status,row_no,column_no,length,width,height,occupied_flag,sort_no,remark,create_by,create_time,update_by,update_time)
 VALUES (2056772972367396865,'WH1RACK1000000006-R1-C3','海B货2-1-3',2056769387227328513,2056770392228704258,2056772972287705090,'enabled',1,3,200,600,300,0,1003,null,'admin','2026-05-20 00:24:24.266','admin','2026-05-20 00:24:24.266');
INSERT INTO wms_advence_master.wms_location (id,location_code,location_name,warehouse_id,area_id,rack_id,location_status,row_no,column_no,length,width,height,occupied_flag,sort_no,remark,create_by,create_time,update_by,update_time)
 VALUES (2056772972367396866,'WH1RACK1000000006-R2-C1','海B货2-2-1',2056769387227328513,2056770392228704258,2056772972287705090,'enabled',2,1,200,600,300,0,2001,null,'admin','2026-05-20 00:24:24.269','admin','2026-05-20 00:24:24.269');
INSERT INTO wms_advence_master.wms_location (id,location_code,location_name,warehouse_id,area_id,rack_id,location_status,row_no,column_no,length,width,height,occupied_flag,sort_no,remark,create_by,create_time,update_by,update_time)
 VALUES (2056772972367396867,'WH1RACK1000000006-R2-C2','海B货2-2-2',2056769387227328513,2056770392228704258,2056772972287705090,'enabled',2,2,200,600,300,0,2002,null,'admin','2026-05-20 00:24:24.274','admin','2026-05-20 00:24:24.274');
INSERT INTO wms_advence_master.wms_location (id,location_code,location_name,warehouse_id,area_id,rack_id,location_status,row_no,column_no,length,width,height,occupied_flag,sort_no,remark,create_by,create_time,update_by,update_time)
 VALUES (2056772972438700034,'WH1RACK1000000006-R2-C3','海B货2-2-3',2056769387227328513,2056770392228704258,2056772972287705090,'enabled',2,3,200,600,300,0,2003,null,'admin','2026-05-20 00:24:24.281','admin','2026-05-20 00:24:24.281');
INSERT INTO wms_advence_master.wms_location (id,location_code,location_name,warehouse_id,area_id,rack_id,location_status,row_no,column_no,length,width,height,occupied_flag,sort_no,remark,create_by,create_time,update_by,update_time)
 VALUES (2058927017274146817,'RACKWH11000000001-R1-C1','朝C货1-1-1',2056769437957435394,2058920568263311362,2058927017085403138,'enabled',1,1,200,600,300,0,1001,null,'admin','2026-05-25 23:03:48.602','admin','2026-05-25 23:03:48.602');
INSERT INTO wms_advence_master.wms_location (id,location_code,location_name,warehouse_id,area_id,rack_id,location_status,row_no,column_no,length,width,height,occupied_flag,sort_no,remark,create_by,create_time,update_by,update_time)
 VALUES (2058927017529999361,'RACKWH11000000001-R1-C2','朝C货1-1-2',2056769437957435394,2058920568263311362,2058927017085403138,'enabled',1,2,200,600,300,0,1002,null,'admin','2026-05-25 23:03:48.655','admin','2026-05-25 23:03:48.655');
INSERT INTO wms_advence_master.wms_location (id,location_code,location_name,warehouse_id,area_id,rack_id,location_status,row_no,column_no,length,width,height,occupied_flag,sort_no,remark,create_by,create_time,update_by,update_time)
 VALUES (2058927017722937345,'RACKWH11000000001-R1-C3','朝C货1-1-3',2056769437957435394,2058920568263311362,2058927017085403138,'enabled',1,3,200,600,300,0,1003,null,'admin','2026-05-25 23:03:48.704','admin','2026-05-25 23:03:48.704');
INSERT INTO wms_advence_master.wms_location (id,location_code,location_name,warehouse_id,area_id,rack_id,location_status,row_no,column_no,length,width,height,occupied_flag,sort_no,remark,create_by,create_time,update_by,update_time)
 VALUES (2058927017915875330,'RACKWH11000000001-R2-C1','朝C货1-2-1',2056769437957435394,2058920568263311362,2058927017085403138,'enabled',2,1,200,600,300,0,2001,null,'admin','2026-05-25 23:03:48.755','admin','2026-05-25 23:03:48.755');
INSERT INTO wms_advence_master.wms_location (id,location_code,location_name,warehouse_id,area_id,rack_id,location_status,row_no,column_no,length,width,height,occupied_flag,sort_no,remark,create_by,create_time,update_by,update_time)
 VALUES (2058927018175922178,'RACKWH11000000001-R2-C2','朝C货1-2-2',2056769437957435394,2058920568263311362,2058927017085403138,'enabled',2,2,200,600,300,0,2002,null,'admin','2026-05-25 23:03:48.805','admin','2026-05-25 23:03:48.805');
INSERT INTO wms_advence_master.wms_location (id,location_code,location_name,warehouse_id,area_id,rack_id,location_status,row_no,column_no,length,width,height,occupied_flag,sort_no,remark,create_by,create_time,update_by,update_time)
 VALUES (2056771221887201284,'WH1RACK1000000003-R1-C2','京B货1-1-2',1828364459110469633,2056770261064429569,2056771221887201282,'enabled',1,2,300,900,300,1,1002,null,'admin','2026-05-20 00:17:26.925','admin','2026-05-20 00:17:26.925');
INSERT INTO wms_advence_master.wms_location (id,location_code,location_name,warehouse_id,area_id,rack_id,location_status,row_no,column_no,length,width,height,occupied_flag,sort_no,remark,create_by,create_time,update_by,update_time)
 VALUES (2058927018364665858,'RACKWH11000000001-R2-C3','朝C货1-2-3',2056769437957435394,2058920568263311362,2058927017085403138,'enabled',2,3,200,600,300,0,2003,null,'admin','2026-05-25 23:03:48.856','admin','2026-05-25 23:03:48.856');


-- Name: wms_merchant; Type: Table; Schema: wms_advence_master;

SET search_path = wms_advence_master;
CREATE TABLE wms_merchant (
    id bigint AUTO_INCREMENT NOT NULL,
    merchant_code varchar(20) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci NOT NULL,
    merchant_name varchar(60) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci NOT NULL,
    address varchar(200) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    mobile varchar(13) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    tel varchar(13) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    contact_person varchar(30) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    email varchar(50) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    remark varchar(255) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    create_by varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    create_time timestamp(3) without time zone DEFAULT NULL::timestamp without time zone,
    update_by varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    update_time timestamp(3) without time zone DEFAULT NULL::timestamp without time zone,
    CONSTRAINT wms_merchant_pk PRIMARY KEY (id)
) AUTO_INCREMENT = 1
CHARACTER SET = "UTF8" COLLATE = "utf8mb4_general_ci"
WITH (orientation=row, compression=no, fillfactor=80);
COMMENT ON TABLE wms_merchant IS '往来单位';
COMMENT ON COLUMN wms_merchant.merchant_code IS '单位编号';
COMMENT ON COLUMN wms_merchant.merchant_name IS '单位名称';
COMMENT ON COLUMN wms_merchant.address IS '地址';
COMMENT ON COLUMN wms_merchant.mobile IS '手机号';
COMMENT ON COLUMN wms_merchant.tel IS '座机号';
COMMENT ON COLUMN wms_merchant.contact_person IS '联系人';
COMMENT ON COLUMN wms_merchant.email IS 'Email';
COMMENT ON COLUMN wms_merchant.remark IS '备注';
COMMENT ON COLUMN wms_merchant.create_by IS '创建人';
COMMENT ON COLUMN wms_merchant.create_time IS '创建时间';
COMMENT ON COLUMN wms_merchant.update_by IS '修改人';
COMMENT ON COLUMN wms_merchant.update_time IS '修改时间';

--Data for  Name: wms_merchant; Type: Table; Schema: wms_advence_master;



-- Name: wms_movement_order; Type: Table; Schema: wms_advence_master;

SET search_path = wms_advence_master;
CREATE TABLE wms_movement_order (
    id bigint AUTO_INCREMENT NOT NULL,
    movement_order_no varchar(32) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    movement_type varchar(32) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    dispatch_basis varchar(255) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    dispatch_purpose varchar(255) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    dispatch_mode varchar(32) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    from_unit varchar(128) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    to_unit varchar(128) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    from_station varchar(128) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    to_station varchar(128) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    from_address varchar(255) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    to_address varchar(255) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    contact_address varchar(255) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    dispatch_date date,
    effective_date date,
    issue_date date,
    source_warehouse_id bigint,
    source_area_id bigint,
    target_warehouse_id bigint,
    target_area_id bigint,
    movement_order_status smallint,
    total_quantity numeric(10,2) DEFAULT NULL::numeric,
    remark varchar(255) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    create_by varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    create_time timestamp(3) without time zone DEFAULT NULL::timestamp without time zone,
    update_by varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    update_time timestamp(3) without time zone DEFAULT NULL::timestamp without time zone,
    CONSTRAINT wms_movement_order_pk PRIMARY KEY (id)
) AUTO_INCREMENT = 2058935449213501442
CHARACTER SET = "UTF8" COLLATE = "utf8mb4_general_ci"
WITH (orientation=row, compression=no, fillfactor=80);
COMMENT ON TABLE wms_movement_order IS '调拨单';
COMMENT ON COLUMN wms_movement_order.movement_order_no IS '编号';
COMMENT ON COLUMN wms_movement_order.movement_type IS '调拨类型';
COMMENT ON COLUMN wms_movement_order.dispatch_basis IS '调拨依据';
COMMENT ON COLUMN wms_movement_order.dispatch_purpose IS '调拨目的';
COMMENT ON COLUMN wms_movement_order.dispatch_mode IS '调拨方式';
COMMENT ON COLUMN wms_movement_order.from_unit IS '发货单位';
COMMENT ON COLUMN wms_movement_order.to_unit IS '收货单位';
COMMENT ON COLUMN wms_movement_order.from_station IS '发站';
COMMENT ON COLUMN wms_movement_order.to_station IS '到站';
COMMENT ON COLUMN wms_movement_order.from_address IS '发货地址';
COMMENT ON COLUMN wms_movement_order.to_address IS '收货地址';
COMMENT ON COLUMN wms_movement_order.contact_address IS '通信地址';
COMMENT ON COLUMN wms_movement_order.dispatch_date IS '调拨日期';
COMMENT ON COLUMN wms_movement_order.effective_date IS '有效日期';
COMMENT ON COLUMN wms_movement_order.issue_date IS '发出日期';
COMMENT ON COLUMN wms_movement_order.source_warehouse_id IS '源仓库';
COMMENT ON COLUMN wms_movement_order.source_area_id IS '源库区';
COMMENT ON COLUMN wms_movement_order.target_warehouse_id IS '目标仓库';
COMMENT ON COLUMN wms_movement_order.target_area_id IS '目标库区';
COMMENT ON COLUMN wms_movement_order.movement_order_status IS '状态';
COMMENT ON COLUMN wms_movement_order.total_quantity IS '总数量';
COMMENT ON COLUMN wms_movement_order.remark IS '备注';
COMMENT ON COLUMN wms_movement_order.create_by IS '创建人';
COMMENT ON COLUMN wms_movement_order.create_time IS '创建时间';
COMMENT ON COLUMN wms_movement_order.update_by IS '修改人';
COMMENT ON COLUMN wms_movement_order.update_time IS '修改时间';

--Data for  Name: wms_movement_order; Type: Table; Schema: wms_advence_master;

INSERT INTO wms_advence_master.wms_movement_order (id,movement_order_no,movement_type,dispatch_basis,dispatch_purpose,dispatch_mode,from_unit,to_unit,from_station,to_station,from_address,to_address,contact_address,dispatch_date,effective_date,issue_date,source_warehouse_id,source_area_id,target_warehouse_id,target_area_id,movement_order_status,total_quantity,remark,create_by,create_time,update_by,update_time)
 VALUES (2058933863678509057,'DBWH12058933863439433728','通装','fa','af','road','fadsf','f',null,'dsfsd',null,null,'dasfds',null,'2026-06-04','2026-05-28',1828364459110469633,2056770261064429569,2056769387227328513,2056770367725580289,1,1,'dsfa','admin','2026-05-25 23:31:00.905','admin','2026-05-25 23:31:17.995');
INSERT INTO wms_advence_master.wms_movement_order (id,movement_order_no,movement_type,dispatch_basis,dispatch_purpose,dispatch_mode,from_unit,to_unit,from_station,to_station,from_address,to_address,contact_address,dispatch_date,effective_date,issue_date,source_warehouse_id,source_area_id,target_warehouse_id,target_area_id,movement_order_status,total_quantity,remark,create_by,create_time,update_by,update_time)
 VALUES (2058935449213501441,'DBWH12058935449062506496','通装','fasd','fasd','road','sfasd','fads',null,'sad',null,null,'fad',null,'2026-05-29','2026-06-05',1828364459110469633,2056770261064429569,2056769387227328513,2056770392228704258,1,1,'fads','admin','2026-05-25 23:37:18.935','admin','2026-05-25 23:37:18.935');


-- Name: wms_movement_order_detail; Type: Table; Schema: wms_advence_master;

SET search_path = wms_advence_master;
CREATE TABLE wms_movement_order_detail (
    id bigint AUTO_INCREMENT NOT NULL,
    movement_order_id bigint,
    sku_id bigint,
    quantity numeric(20,2) DEFAULT NULL::numeric,
    item_code varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    item_name varchar(128) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    sku_name varchar(255) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    unit varchar(32) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    product_identifier varchar(128) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    quality_grade varchar(32) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    unit_price numeric(18,2) DEFAULT NULL::numeric,
    line_amount numeric(18,2) DEFAULT NULL::numeric,
    remark varchar(255) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    source_warehouse_id bigint,
    source_area_id bigint,
    source_rack_id bigint,
    source_location_id bigint,
    target_warehouse_id bigint,
    target_area_id bigint,
    target_rack_id bigint,
    target_location_id bigint,
    inventory_detail_id bigint,
    item_instance_id bigint,
    create_by varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    create_time timestamp(3) without time zone DEFAULT NULL::timestamp without time zone,
    update_by varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    update_time timestamp(3) without time zone DEFAULT NULL::timestamp without time zone,
    CONSTRAINT wms_movement_order_detail_pk PRIMARY KEY (id)
) AUTO_INCREMENT = 2058935449473548291
CHARACTER SET = "UTF8" COLLATE = "utf8mb4_general_ci"
WITH (orientation=row, compression=no, fillfactor=80);
COMMENT ON TABLE wms_movement_order_detail IS '调拨单详情';
COMMENT ON COLUMN wms_movement_order_detail.movement_order_id IS '调拨单Id';
COMMENT ON COLUMN wms_movement_order_detail.sku_id IS '规格id';
COMMENT ON COLUMN wms_movement_order_detail.quantity IS '数量';
COMMENT ON COLUMN wms_movement_order_detail.item_code IS '器材编码';
COMMENT ON COLUMN wms_movement_order_detail.item_name IS '器材名称';
COMMENT ON COLUMN wms_movement_order_detail.sku_name IS '规格型号';
COMMENT ON COLUMN wms_movement_order_detail.unit IS '计量单位';
COMMENT ON COLUMN wms_movement_order_detail.product_identifier IS '产品标识';
COMMENT ON COLUMN wms_movement_order_detail.quality_grade IS '质量等级';
COMMENT ON COLUMN wms_movement_order_detail.unit_price IS '单价';
COMMENT ON COLUMN wms_movement_order_detail.line_amount IS '行金额';
COMMENT ON COLUMN wms_movement_order_detail.remark IS '备注';
COMMENT ON COLUMN wms_movement_order_detail.source_warehouse_id IS '源仓库';
COMMENT ON COLUMN wms_movement_order_detail.source_area_id IS '源库区';
COMMENT ON COLUMN wms_movement_order_detail.source_rack_id IS '源货架ID';
COMMENT ON COLUMN wms_movement_order_detail.source_location_id IS '源货位ID';
COMMENT ON COLUMN wms_movement_order_detail.target_warehouse_id IS '目标仓库';
COMMENT ON COLUMN wms_movement_order_detail.target_area_id IS '目标库区';
COMMENT ON COLUMN wms_movement_order_detail.target_rack_id IS '目标货架ID';
COMMENT ON COLUMN wms_movement_order_detail.target_location_id IS '目标货位ID';
COMMENT ON COLUMN wms_movement_order_detail.inventory_detail_id IS '入库记录id';
COMMENT ON COLUMN wms_movement_order_detail.item_instance_id IS '器材实例ID';
COMMENT ON COLUMN wms_movement_order_detail.create_by IS '创建人';
COMMENT ON COLUMN wms_movement_order_detail.create_time IS '创建时间';
COMMENT ON COLUMN wms_movement_order_detail.update_by IS '修改人';
COMMENT ON COLUMN wms_movement_order_detail.update_time IS '修改时间';
CREATE INDEX tb0_wms_movement_order_detail_idx_wms_movement_order_detail_item_instance_id ON wms_advence_master.wms_movement_order_detail USING btree (item_instance_id) TABLESPACE pg_default;

--Data for  Name: wms_movement_order_detail; Type: Table; Schema: wms_advence_master;

INSERT INTO wms_advence_master.wms_movement_order_detail (id,movement_order_id,sku_id,quantity,item_code,item_name,sku_name,unit,product_identifier,quality_grade,unit_price,line_amount,remark,source_warehouse_id,source_area_id,source_rack_id,source_location_id,target_warehouse_id,target_area_id,target_rack_id,target_location_id,inventory_detail_id,item_instance_id,create_by,create_time,update_by,update_time)
 VALUES (2058933863925972993,2058933863678509057,2058919798067462146,1,'DN','苹果电脑','1T','台','高标识','高',7,7,null,1828364459110469633,2056770261064429569,2056771221887201282,2056771221887201283,2056769387227328513,2056770367725580289,2056771350060937218,2056771350060937219,2058930934737305602,2058920403737542660,'admin','2026-05-25 23:31:00.968','admin','2026-05-25 23:31:18.096');
INSERT INTO wms_advence_master.wms_movement_order_detail (id,movement_order_id,sku_id,quantity,item_code,item_name,sku_name,unit,product_identifier,quality_grade,unit_price,line_amount,remark,source_warehouse_id,source_area_id,source_rack_id,source_location_id,target_warehouse_id,target_area_id,target_rack_id,target_location_id,inventory_detail_id,item_instance_id,create_by,create_time,update_by,update_time)
 VALUES (2058935449473548290,2058935449213501441,2058919798067462147,1,'DN','苹果电脑','2T','台','低标识','低',2,2,null,1828364459110469633,2056770261064429569,2056771221887201282,2056771221887201284,2056769387227328513,2056770392228704258,null,null,2058930934737305606,2058920374624878597,'admin','2026-05-25 23:37:18.983','admin','2026-05-25 23:37:18.983');


-- Name: wms_rack; Type: Table; Schema: wms_advence_master;

SET search_path = wms_advence_master;
CREATE TABLE wms_rack (
    id bigint AUTO_INCREMENT NOT NULL,
    rack_code varchar(32) CHARACTER SET "UTF8" COLLATE utf8mb4_0900_ai_ci DEFAULT NULL::varchar,
    rack_name varchar(60) CHARACTER SET "UTF8" COLLATE utf8mb4_0900_ai_ci NOT NULL,
    warehouse_id bigint NOT NULL,
    area_id bigint NOT NULL,
    rack_status varchar(32) CHARACTER SET "UTF8" COLLATE utf8mb4_0900_ai_ci DEFAULT 'enabled'::varchar,
    row_count integer,
    column_count integer,
    length numeric(18,2) DEFAULT NULL::numeric,
    width numeric(18,2) DEFAULT NULL::numeric,
    height numeric(18,2) DEFAULT NULL::numeric,
    order_num bigint,
    remark varchar(255) CHARACTER SET "UTF8" COLLATE utf8mb4_0900_ai_ci DEFAULT NULL::varchar,
    create_by varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_0900_ai_ci DEFAULT NULL::varchar,
    create_time timestamp(3) without time zone DEFAULT NULL::timestamp without time zone,
    update_by varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_0900_ai_ci DEFAULT NULL::varchar,
    update_time timestamp(3) without time zone DEFAULT NULL::timestamp without time zone,
    CONSTRAINT wms_rack_pk PRIMARY KEY (id)
) AUTO_INCREMENT = 2058927017085403139
CHARACTER SET = "UTF8" COLLATE = "utf8mb4_0900_ai_ci"
WITH (orientation=row, compression=no, fillfactor=80);
COMMENT ON TABLE wms_rack IS '货架';
COMMENT ON COLUMN wms_rack.rack_code IS '货架编码';
COMMENT ON COLUMN wms_rack.rack_name IS '货架名称';
COMMENT ON COLUMN wms_rack.warehouse_id IS '所属仓库';
COMMENT ON COLUMN wms_rack.area_id IS '所属库区';
COMMENT ON COLUMN wms_rack.rack_status IS '货架状态';
COMMENT ON COLUMN wms_rack.row_count IS '行数';
COMMENT ON COLUMN wms_rack.column_count IS '列数';
COMMENT ON COLUMN wms_rack.length IS '长';
COMMENT ON COLUMN wms_rack.width IS '宽';
COMMENT ON COLUMN wms_rack.height IS '高';
COMMENT ON COLUMN wms_rack.order_num IS '排序';
COMMENT ON COLUMN wms_rack.remark IS '备注';
COMMENT ON COLUMN wms_rack.create_by IS '创建人';
COMMENT ON COLUMN wms_rack.create_time IS '创建时间';
COMMENT ON COLUMN wms_rack.update_by IS '修改人';
COMMENT ON COLUMN wms_rack.update_time IS '修改时间';
CREATE INDEX tb0_wms_rack_idx_wms_rack_warehouse_id ON wms_advence_master.wms_rack USING btree (warehouse_id) TABLESPACE pg_default;
CREATE INDEX tb0_wms_rack_idx_wms_rack_area_id ON wms_advence_master.wms_rack USING btree (area_id) TABLESPACE pg_default;
CREATE INDEX tb0_wms_rack_idx_wms_rack_position ON wms_advence_master.wms_rack USING btree (warehouse_id, area_id) TABLESPACE pg_default;
ALTER TABLE wms_rack ADD CONSTRAINT wms_rack_uk1 UNIQUE USING btree (warehouse_id, area_id, rack_code);

--Data for  Name: wms_rack; Type: Table; Schema: wms_advence_master;

INSERT INTO wms_advence_master.wms_rack (id,rack_code,rack_name,warehouse_id,area_id,rack_status,row_count,column_count,length,width,height,order_num,remark,create_by,create_time,update_by,update_time)
 VALUES (2056771039778910209,'WH1RACK1000000001','京A货1',1828364459110469633,2056770222548135937,'enabled',2,3,600,600,600,0,null,'admin','2026-05-20 00:16:43.507','admin','2026-05-20 00:16:43.507');
INSERT INTO wms_advence_master.wms_rack (id,rack_code,rack_name,warehouse_id,area_id,rack_status,row_count,column_count,length,width,height,order_num,remark,create_by,create_time,update_by,update_time)
 VALUES (2056771137254535169,'WH1RACK1000000002','京A货2',1828364459110469633,2056770222548135937,'enabled',1,1,200,200,200,0,null,'admin','2026-05-20 00:17:06.738','admin','2026-05-20 00:17:06.738');
INSERT INTO wms_advence_master.wms_rack (id,rack_code,rack_name,warehouse_id,area_id,rack_status,row_count,column_count,length,width,height,order_num,remark,create_by,create_time,update_by,update_time)
 VALUES (2056771221887201282,'WH1RACK1000000003','京B货1',1828364459110469633,2056770261064429569,'enabled',3,3,900,900,900,0,null,'admin','2026-05-20 00:17:26.919','admin','2026-05-20 00:17:26.919');
INSERT INTO wms_advence_master.wms_rack (id,rack_code,rack_name,warehouse_id,area_id,rack_status,row_count,column_count,length,width,height,order_num,remark,create_by,create_time,update_by,update_time)
 VALUES (2056771350060937218,'WH1RACK1000000004','海A货1',2056769387227328513,2056770367725580289,'enabled',1,1,50,50,50,0,null,'admin','2026-05-20 00:17:57.480','admin','2026-05-20 00:17:57.480');
INSERT INTO wms_advence_master.wms_rack (id,rack_code,rack_name,warehouse_id,area_id,rack_status,row_count,column_count,length,width,height,order_num,remark,create_by,create_time,update_by,update_time)
 VALUES (2056772480702689282,'WH1RACK1000000005','海B货1',2056769387227328513,2056770392228704258,'enabled',2,3,600,600,600,0,null,'admin','2026-05-20 00:22:27.057','admin','2026-05-20 00:22:27.057');
INSERT INTO wms_advence_master.wms_rack (id,rack_code,rack_name,warehouse_id,area_id,rack_status,row_count,column_count,length,width,height,order_num,remark,create_by,create_time,update_by,update_time)
 VALUES (2056772972287705090,'WH1RACK1000000006','海B货2',2056769387227328513,2056770392228704258,'enabled',2,3,600,600,600,0,null,'admin','2026-05-20 00:24:24.246','admin','2026-05-20 00:24:24.246');
INSERT INTO wms_advence_master.wms_rack (id,rack_code,rack_name,warehouse_id,area_id,rack_status,row_count,column_count,length,width,height,order_num,remark,create_by,create_time,update_by,update_time)
 VALUES (2058927017085403138,'RACKWH11000000001','朝C货1',2056769437957435394,2058920568263311362,'enabled',2,3,600,600,600,0,null,'admin','2026-05-25 23:03:48.546','admin','2026-05-25 23:03:48.546');


-- Name: wms_receipt_order; Type: Table; Schema: wms_advence_master;

SET search_path = wms_advence_master;
CREATE TABLE wms_receipt_order (
    id bigint AUTO_INCREMENT NOT NULL,
    receipt_order_no varchar(32) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    receipt_order_type varchar(20) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    basis_no varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    dispatch_mode varchar(32) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    notice_org varchar(128) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    receive_unit varchar(128) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    purchase_date date,
    receipt_date date,
    total_quantity numeric(10,2) DEFAULT NULL::numeric,
    payable_amount numeric(10,2) DEFAULT NULL::numeric,
    receipt_order_status smallint,
    warehouse_id bigint,
    area_id bigint,
    remark varchar(255) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    create_by varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    create_time timestamp(3) without time zone DEFAULT NULL::timestamp without time zone,
    update_by varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    update_time timestamp(3) without time zone DEFAULT NULL::timestamp without time zone,
    CONSTRAINT wms_receipt_order_pk PRIMARY KEY (id)
) AUTO_INCREMENT = 2058929554794512386
CHARACTER SET = "UTF8" COLLATE = "utf8mb4_general_ci"
WITH (orientation=row, compression=no, fillfactor=80);
COMMENT ON TABLE wms_receipt_order IS '入库单';
COMMENT ON COLUMN wms_receipt_order.receipt_order_no IS '入库单号';
COMMENT ON COLUMN wms_receipt_order.receipt_order_type IS '入库类型';
COMMENT ON COLUMN wms_receipt_order.basis_no IS '调拨根据';
COMMENT ON COLUMN wms_receipt_order.dispatch_mode IS '调拨方式';
COMMENT ON COLUMN wms_receipt_order.notice_org IS '通知机关';
COMMENT ON COLUMN wms_receipt_order.receive_unit IS '收物单位';
COMMENT ON COLUMN wms_receipt_order.purchase_date IS '采购日期';
COMMENT ON COLUMN wms_receipt_order.receipt_date IS '入库日期';
COMMENT ON COLUMN wms_receipt_order.total_quantity IS '器材总数';
COMMENT ON COLUMN wms_receipt_order.payable_amount IS '订单金额';
COMMENT ON COLUMN wms_receipt_order.receipt_order_status IS '入库状态';
COMMENT ON COLUMN wms_receipt_order.warehouse_id IS '仓库id';
COMMENT ON COLUMN wms_receipt_order.area_id IS '库区id';
COMMENT ON COLUMN wms_receipt_order.remark IS '备注';
COMMENT ON COLUMN wms_receipt_order.create_by IS '创建人';
COMMENT ON COLUMN wms_receipt_order.create_time IS '创建时间';
COMMENT ON COLUMN wms_receipt_order.update_by IS '修改人';
COMMENT ON COLUMN wms_receipt_order.update_time IS '修改时间';

--Data for  Name: wms_receipt_order; Type: Table; Schema: wms_advence_master;

INSERT INTO wms_advence_master.wms_receipt_order (id,receipt_order_no,receipt_order_type,basis_no,dispatch_mode,notice_org,receive_unit,purchase_date,receipt_date,total_quantity,payable_amount,receipt_order_status,warehouse_id,area_id,remark,create_by,create_time,update_by,update_time)
 VALUES (2058929554794512385,'RKWH12058929554542854144','采购入库','a','air','a','a','2026-05-19','2026-05-25',6,25,1,1828364459110469633,2056770261064429569,'aa','admin','2026-05-25 23:13:53.591','admin','2026-05-25 23:19:19.431');


-- Name: wms_receipt_order_detail; Type: Table; Schema: wms_advence_master;

SET search_path = wms_advence_master;
CREATE TABLE wms_receipt_order_detail (
    id bigint AUTO_INCREMENT NOT NULL,
    receipt_order_id bigint,
    sku_id bigint,
    quantity numeric(20,2) DEFAULT NULL::numeric,
    item_code varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    item_name varchar(128) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    sku_name varchar(255) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    unit varchar(32) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    product_identifier varchar(128) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    quality_grade varchar(32) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    unit_price numeric(18,2) DEFAULT NULL::numeric,
    line_amount numeric(18,2) DEFAULT NULL::numeric,
    remark varchar(255) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    create_by varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    create_time timestamp(3) without time zone DEFAULT NULL::timestamp without time zone,
    update_by varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    update_time timestamp(3) without time zone DEFAULT NULL::timestamp without time zone,
    warehouse_id bigint,
    area_id bigint,
    rack_id bigint,
    location_id bigint,
    box_code varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci,
    CONSTRAINT wms_receipt_order_detail_pk PRIMARY KEY (id)
) AUTO_INCREMENT = 2058929555096502275
CHARACTER SET = "UTF8" COLLATE = "utf8mb4_general_ci"
WITH (orientation=row, compression=no, fillfactor=80);
COMMENT ON TABLE wms_receipt_order_detail IS '入库单详情';
COMMENT ON COLUMN wms_receipt_order_detail.receipt_order_id IS '入库单号';
COMMENT ON COLUMN wms_receipt_order_detail.sku_id IS '规格id';
COMMENT ON COLUMN wms_receipt_order_detail.quantity IS '入库数量';
COMMENT ON COLUMN wms_receipt_order_detail.item_code IS '器材编码';
COMMENT ON COLUMN wms_receipt_order_detail.item_name IS '器材名称';
COMMENT ON COLUMN wms_receipt_order_detail.sku_name IS '规格型号';
COMMENT ON COLUMN wms_receipt_order_detail.unit IS '计量单位';
COMMENT ON COLUMN wms_receipt_order_detail.product_identifier IS '产品标识';
COMMENT ON COLUMN wms_receipt_order_detail.quality_grade IS '质量等级';
COMMENT ON COLUMN wms_receipt_order_detail.unit_price IS '单价';
COMMENT ON COLUMN wms_receipt_order_detail.line_amount IS '总价';
COMMENT ON COLUMN wms_receipt_order_detail.remark IS '备注';
COMMENT ON COLUMN wms_receipt_order_detail.create_by IS '创建人';
COMMENT ON COLUMN wms_receipt_order_detail.create_time IS '创建时间';
COMMENT ON COLUMN wms_receipt_order_detail.update_by IS '修改人';
COMMENT ON COLUMN wms_receipt_order_detail.update_time IS '修改时间';
COMMENT ON COLUMN wms_receipt_order_detail.warehouse_id IS '所属仓库';
COMMENT ON COLUMN wms_receipt_order_detail.area_id IS '所属库区';
COMMENT ON COLUMN wms_receipt_order_detail.rack_id IS '所属货架';
COMMENT ON COLUMN wms_receipt_order_detail.location_id IS '所属货位';

--Data for  Name: wms_receipt_order_detail; Type: Table; Schema: wms_advence_master;

INSERT INTO wms_advence_master.wms_receipt_order_detail (id,receipt_order_id,sku_id,quantity,item_code,item_name,sku_name,unit,product_identifier,quality_grade,unit_price,line_amount,remark,create_by,create_time,update_by,update_time,warehouse_id,area_id,rack_id,location_id,box_code)
 VALUES (2058929555029393409,2058929554794512385,2058919798067462146,1,'DN','苹果电脑','1T','台','高标识','高',4,4,'a','admin','2026-05-25 23:13:53.650','admin','2026-05-25 23:19:20.862',1828364459110469633,2056770261064429569,2056771221887201282,2056771221887201283,null);
INSERT INTO wms_advence_master.wms_receipt_order_detail (id,receipt_order_id,sku_id,quantity,item_code,item_name,sku_name,unit,product_identifier,quality_grade,unit_price,line_amount,remark,create_by,create_time,update_by,update_time,warehouse_id,area_id,rack_id,location_id,box_code)
 VALUES (2058929555029393410,2058929554794512385,2058919798067462146,1,'DN','苹果电脑','1T','台','高标识','高',7,7,'a','admin','2026-05-25 23:13:53.652','admin','2026-05-25 23:19:20.909',1828364459110469633,2056770261064429569,2056771221887201282,2056771221887201283,null);
INSERT INTO wms_advence_master.wms_receipt_order_detail (id,receipt_order_id,sku_id,quantity,item_code,item_name,sku_name,unit,product_identifier,quality_grade,unit_price,line_amount,remark,create_by,create_time,update_by,update_time,warehouse_id,area_id,rack_id,location_id,box_code)
 VALUES (2058929555029393411,2058929554794512385,2058919798067462146,1,'DN','苹果电脑','1T','台','高标识','高',5,5,'a','admin','2026-05-25 23:13:53.652','admin','2026-05-25 23:19:20.957',1828364459110469633,2056770261064429569,2056771221887201282,2056771221887201283,null);
INSERT INTO wms_advence_master.wms_receipt_order_detail (id,receipt_order_id,sku_id,quantity,item_code,item_name,sku_name,unit,product_identifier,quality_grade,unit_price,line_amount,remark,create_by,create_time,update_by,update_time,warehouse_id,area_id,rack_id,location_id,box_code)
 VALUES (2058929555029393412,2058929554794512385,2058919798067462147,1,'DN','苹果电脑','2T','台','低标识','低',4,4,'a','admin','2026-05-25 23:13:53.653','admin','2026-05-25 23:19:21.017',1828364459110469633,2056770261064429569,2056771221887201282,2056771221887201284,null);
INSERT INTO wms_advence_master.wms_receipt_order_detail (id,receipt_order_id,sku_id,quantity,item_code,item_name,sku_name,unit,product_identifier,quality_grade,unit_price,line_amount,remark,create_by,create_time,update_by,update_time,warehouse_id,area_id,rack_id,location_id,box_code)
 VALUES (2058929555029393413,2058929554794512385,2058919798067462147,1,'DN','苹果电脑','2T','台','低标识','低',3,3,'a','admin','2026-05-25 23:13:53.653','admin','2026-05-25 23:19:21.066',1828364459110469633,2056770261064429569,2056771221887201282,2056771221887201284,null);
INSERT INTO wms_advence_master.wms_receipt_order_detail (id,receipt_order_id,sku_id,quantity,item_code,item_name,sku_name,unit,product_identifier,quality_grade,unit_price,line_amount,remark,create_by,create_time,update_by,update_time,warehouse_id,area_id,rack_id,location_id,box_code)
 VALUES (2058929555096502274,2058929554794512385,2058919798067462147,1,'DN','苹果电脑','2T','台','低标识','低',2,2,'z','admin','2026-05-25 23:13:53.657','admin','2026-05-25 23:19:21.115',1828364459110469633,2056770261064429569,2056771221887201282,2056771221887201284,null);


-- Name: wms_shipment_order; Type: Table; Schema: wms_advence_master;

SET search_path = wms_advence_master;
CREATE TABLE wms_shipment_order (
    id bigint AUTO_INCREMENT NOT NULL,
    shipment_order_no varchar(32) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    shipment_order_type varchar(20) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    basis_no varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    dispatch_mode varchar(32) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    notice_org varchar(128) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    receive_unit varchar(128) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    purchase_date date,
    shipment_date date,
    receivable_amount numeric(10,2) DEFAULT NULL::numeric,
    total_quantity numeric(10,2) DEFAULT NULL::numeric,
    shipment_order_status smallint,
    warehouse_id bigint,
    area_id bigint,
    remark varchar(255) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    create_by varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    create_time timestamp(3) without time zone DEFAULT NULL::timestamp without time zone,
    update_by varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    update_time timestamp(3) without time zone DEFAULT NULL::timestamp without time zone,
    CONSTRAINT wms_shipment_order_pk PRIMARY KEY (id)
) AUTO_INCREMENT = 2058933291508334594
CHARACTER SET = "UTF8" COLLATE = "utf8mb4_general_ci"
WITH (orientation=row, compression=no, fillfactor=80);
COMMENT ON TABLE wms_shipment_order IS '出库单';
COMMENT ON COLUMN wms_shipment_order.shipment_order_no IS '出库单号，系统自动生成';
COMMENT ON COLUMN wms_shipment_order.shipment_order_type IS '出库类型';
COMMENT ON COLUMN wms_shipment_order.basis_no IS '调拨根据';
COMMENT ON COLUMN wms_shipment_order.dispatch_mode IS '调拨方式';
COMMENT ON COLUMN wms_shipment_order.notice_org IS '通知机关';
COMMENT ON COLUMN wms_shipment_order.receive_unit IS '收物单位';
COMMENT ON COLUMN wms_shipment_order.purchase_date IS '采购日期';
COMMENT ON COLUMN wms_shipment_order.shipment_date IS '出库日期';
COMMENT ON COLUMN wms_shipment_order.receivable_amount IS '订单金额';
COMMENT ON COLUMN wms_shipment_order.total_quantity IS '出库数量';
COMMENT ON COLUMN wms_shipment_order.shipment_order_status IS '出库单状态';
COMMENT ON COLUMN wms_shipment_order.warehouse_id IS '仓库id';
COMMENT ON COLUMN wms_shipment_order.area_id IS '库区id';
COMMENT ON COLUMN wms_shipment_order.remark IS '备注';
COMMENT ON COLUMN wms_shipment_order.create_by IS '创建人';
COMMENT ON COLUMN wms_shipment_order.create_time IS '创建时间';
COMMENT ON COLUMN wms_shipment_order.update_by IS '修改人';
COMMENT ON COLUMN wms_shipment_order.update_time IS '修改时间';

--Data for  Name: wms_shipment_order; Type: Table; Schema: wms_advence_master;

INSERT INTO wms_advence_master.wms_shipment_order (id,shipment_order_no,shipment_order_type,basis_no,dispatch_mode,notice_org,receive_unit,purchase_date,shipment_date,receivable_amount,total_quantity,shipment_order_status,warehouse_id,area_id,remark,create_by,create_time,update_by,update_time)
 VALUES (2058933291508334593,'CKWH12058933269035253760','借用出库','dfas','road','dsfsdaf','sdaf','2026-05-24','2026-05-25',3,1,1,1828364459110469633,2056770261064429569,'sdafa','admin','2026-05-25 23:28:44.483','admin','2026-05-25 23:28:44.483');


-- Name: wms_shipment_order_detail; Type: Table; Schema: wms_advence_master;

SET search_path = wms_advence_master;
CREATE TABLE wms_shipment_order_detail (
    id bigint AUTO_INCREMENT NOT NULL,
    shipment_order_id bigint,
    sku_id bigint,
    quantity numeric(10,2) DEFAULT NULL::numeric,
    item_code varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    item_name varchar(128) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    sku_name varchar(255) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    unit varchar(32) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    product_identifier varchar(128) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    quality_grade varchar(32) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    unit_price numeric(18,2) DEFAULT NULL::numeric,
    line_amount numeric(18,2) DEFAULT NULL::numeric,
    warehouse_id bigint,
    area_id bigint,
    inventory_detail_id bigint,
    item_instance_id bigint,
    box_id bigint,
    remark varchar(255) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    create_by varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    create_time timestamp(3) without time zone DEFAULT NULL::timestamp without time zone,
    update_by varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    update_time timestamp(3) without time zone DEFAULT NULL::timestamp without time zone,
    CONSTRAINT wms_shipment_order_detail_pk PRIMARY KEY (id)
) AUTO_INCREMENT = 2058933291709661186
CHARACTER SET = "UTF8" COLLATE = "utf8mb4_general_ci"
WITH (orientation=row, compression=no, fillfactor=80);
COMMENT ON TABLE wms_shipment_order_detail IS '出库单详情';
COMMENT ON COLUMN wms_shipment_order_detail.shipment_order_id IS '出库单';
COMMENT ON COLUMN wms_shipment_order_detail.sku_id IS '规格id';
COMMENT ON COLUMN wms_shipment_order_detail.quantity IS '数量';
COMMENT ON COLUMN wms_shipment_order_detail.item_code IS '器材编码';
COMMENT ON COLUMN wms_shipment_order_detail.item_name IS '器材名称';
COMMENT ON COLUMN wms_shipment_order_detail.sku_name IS '规格型号';
COMMENT ON COLUMN wms_shipment_order_detail.unit IS '计量单位';
COMMENT ON COLUMN wms_shipment_order_detail.product_identifier IS '产品标识';
COMMENT ON COLUMN wms_shipment_order_detail.quality_grade IS '质量等级';
COMMENT ON COLUMN wms_shipment_order_detail.unit_price IS '单价';
COMMENT ON COLUMN wms_shipment_order_detail.line_amount IS '总价';
COMMENT ON COLUMN wms_shipment_order_detail.warehouse_id IS '所属仓库';
COMMENT ON COLUMN wms_shipment_order_detail.area_id IS '所属库区';
COMMENT ON COLUMN wms_shipment_order_detail.inventory_detail_id IS '入库记录id';
COMMENT ON COLUMN wms_shipment_order_detail.item_instance_id IS '器材实例ID';
COMMENT ON COLUMN wms_shipment_order_detail.box_id IS '箱体ID';
COMMENT ON COLUMN wms_shipment_order_detail.remark IS '备注';
COMMENT ON COLUMN wms_shipment_order_detail.create_by IS '创建人';
COMMENT ON COLUMN wms_shipment_order_detail.create_time IS '创建时间';
COMMENT ON COLUMN wms_shipment_order_detail.update_by IS '修改人';
COMMENT ON COLUMN wms_shipment_order_detail.update_time IS '修改时间';
CREATE INDEX tb0_wms_shipment_order_detail_idx_wms_shipment_detail_item_instance_id ON wms_advence_master.wms_shipment_order_detail USING btree (item_instance_id) TABLESPACE pg_default;
CREATE INDEX tb0_wms_shipment_order_detail_idx_wms_shipment_detail_box_id ON wms_advence_master.wms_shipment_order_detail USING btree (box_id) TABLESPACE pg_default;

--Data for  Name: wms_shipment_order_detail; Type: Table; Schema: wms_advence_master;

INSERT INTO wms_advence_master.wms_shipment_order_detail (id,shipment_order_id,sku_id,quantity,item_code,item_name,sku_name,unit,product_identifier,quality_grade,unit_price,line_amount,warehouse_id,area_id,inventory_detail_id,item_instance_id,box_id,remark,create_by,create_time,update_by,update_time)
 VALUES (2058933291709661185,2058933291508334593,2058919798067462146,1,'DN','苹果电脑','1T','台','高标识','高',3,3,1828364459110469633,2056770261064429569,2058930934737305601,2058920403737542661,null,null,'admin','2026-05-25 23:28:44.539','admin','2026-05-25 23:28:44.539');


-- Name: wms_warehouse; Type: Table; Schema: wms_advence_master;

SET search_path = wms_advence_master;
CREATE TABLE wms_warehouse (
    id bigint AUTO_INCREMENT NOT NULL,
    warehouse_code varchar(20) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    warehouse_name varchar(50) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci NOT NULL,
    remark varchar(255) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    order_num bigint DEFAULT 0::bigint,
    status varchar(32) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    address varchar(255) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    manager_name varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    manager_phone varchar(32) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    create_by varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    create_time timestamp(3) without time zone DEFAULT NULL::timestamp without time zone,
    update_by varchar(64) CHARACTER SET "UTF8" COLLATE utf8mb4_general_ci DEFAULT NULL::varchar,
    update_time timestamp(3) without time zone DEFAULT NULL::timestamp without time zone,
    CONSTRAINT wms_warehouse_pk PRIMARY KEY (id)
) AUTO_INCREMENT = 2056769648226283523
CHARACTER SET = "UTF8" COLLATE = "utf8mb4_general_ci"
WITH (orientation=row, compression=no, fillfactor=80);
COMMENT ON TABLE wms_warehouse IS '仓库';
COMMENT ON COLUMN wms_warehouse.warehouse_code IS '仓库编码';
COMMENT ON COLUMN wms_warehouse.warehouse_name IS '仓库名称';
COMMENT ON COLUMN wms_warehouse.remark IS '备注';
COMMENT ON COLUMN wms_warehouse.order_num IS '排序';
COMMENT ON COLUMN wms_warehouse.status IS '启用状态';
COMMENT ON COLUMN wms_warehouse.address IS '地址';
COMMENT ON COLUMN wms_warehouse.manager_name IS '负责人';
COMMENT ON COLUMN wms_warehouse.manager_phone IS '负责人电话';
COMMENT ON COLUMN wms_warehouse.create_by IS '创建人';
COMMENT ON COLUMN wms_warehouse.create_time IS '创建时间';
COMMENT ON COLUMN wms_warehouse.update_by IS '修改人';
COMMENT ON COLUMN wms_warehouse.update_time IS '修改时间';
CREATE INDEX tb0_wms_warehouse_idx_wms_warehouse_status ON wms_advence_master.wms_warehouse USING btree (status) TABLESPACE pg_default;
ALTER TABLE wms_warehouse ADD CONSTRAINT wms_warehouse_uk1 UNIQUE USING btree (warehouse_code);

--Data for  Name: wms_warehouse; Type: Table; Schema: wms_advence_master;

INSERT INTO wms_advence_master.wms_warehouse (id,warehouse_code,warehouse_name,remark,order_num,status,address,manager_name,manager_phone,create_by,create_time,update_by,update_time)
 VALUES (1828364459110469633,'WH1','北京仓',null,0,null,null,null,null,'admin','2024-08-27 17:30:31.084','admin','2026-05-20 00:09:53.409');
INSERT INTO wms_advence_master.wms_warehouse (id,warehouse_code,warehouse_name,remark,order_num,status,address,manager_name,manager_phone,create_by,create_time,update_by,update_time)
 VALUES (2056769387227328513,'WH2','海淀仓',null,4,null,null,null,null,'admin','2026-05-20 00:10:09.514','admin','2026-05-20 00:10:09.514');
INSERT INTO wms_advence_master.wms_warehouse (id,warehouse_code,warehouse_name,remark,order_num,status,address,manager_name,manager_phone,create_by,create_time,update_by,update_time)
 VALUES (2056769437957435394,'WH3','朝阳仓',null,5,null,null,null,null,'admin','2026-05-20 00:10:21.609','admin','2026-05-20 00:10:21.609');
INSERT INTO wms_advence_master.wms_warehouse (id,warehouse_code,warehouse_name,remark,order_num,status,address,manager_name,manager_phone,create_by,create_time,update_by,update_time)
 VALUES (2056769466831024130,'WH4','东城仓',null,6,null,null,null,null,'admin','2026-05-20 00:10:28.487','admin','2026-05-20 00:10:28.487');
INSERT INTO wms_advence_master.wms_warehouse (id,warehouse_code,warehouse_name,remark,order_num,status,address,manager_name,manager_phone,create_by,create_time,update_by,update_time)
 VALUES (2056769490864386049,'WH5','西城仓',null,7,null,null,null,null,'admin','2026-05-20 00:10:34.217','admin','2026-05-20 00:10:34.217');
INSERT INTO wms_advence_master.wms_warehouse (id,warehouse_code,warehouse_name,remark,order_num,status,address,manager_name,manager_phone,create_by,create_time,update_by,update_time)
 VALUES (2056769648226283522,'WH6','大兴仓',null,8,null,null,null,null,'admin','2026-05-20 00:11:11.735','admin','2026-05-20 00:11:11.735');
