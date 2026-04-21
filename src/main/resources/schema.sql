-- 创建数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS tlias DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE tlias;

-- 创建部门表
DROP TABLE IF EXISTS dept;
CREATE TABLE dept (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    name VARCHAR(100) NOT NULL COMMENT '部门名称',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间'
) COMMENT '部门表';

-- 插入测试数据
INSERT INTO dept (name, create_time, update_time) VALUES 
('研发部', NOW(), NOW()),
('市场部', NOW(), NOW()),
('人力资源部', NOW(), NOW());

-- 创建员工表
DROP TABLE IF EXISTS emp;
CREATE TABLE emp (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    password VARCHAR(50) NOT NULL COMMENT '密码',
    name VARCHAR(50) NOT NULL COMMENT '姓名',
    gender TINYINT NOT NULL COMMENT '性别: 1男, 2女',
    phone VARCHAR(20) COMMENT '手机号',
    dept_id INT COMMENT '部门ID',
    entry_date DATE COMMENT '入职日期',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间'
) COMMENT '员工表';

-- 插入员工测试数据
INSERT INTO emp (username, password, name, gender, phone, dept_id, entry_date, create_time, update_time) VALUES
('zhangsan', '123456', '张三', 1, '13800138001', 1, '2024-01-15', NOW(), NOW()),
('lisi', '123456', '李四', 2, '13800138002', 1, '2024-02-20', NOW(), NOW()),
('wangwu', '123456', '王五', 1, '13800138003', 2, '2024-03-10', NOW(), NOW()),
('zhaoliu', '123456', '赵六', 2, '13800138004', 3, '2024-04-05', NOW(), NOW()),
('sunqi', '123456', '孙七', 1, '13800138005', 2, '2024-05-12', NOW(), NOW());

-- 创建员工工作经历表
DROP TABLE IF EXISTS emp_expr;
CREATE TABLE emp_expr (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    emp_id INT NOT NULL COMMENT '员工ID',
    company VARCHAR(100) COMMENT '公司名称',
    profession VARCHAR(100) COMMENT '职位',
    begin DATE COMMENT '开始时间',
    end DATE COMMENT '结束时间'
) COMMENT '员工工作经历表';

-- 创建操作日志表
DROP TABLE IF EXISTS operate_log;
CREATE TABLE operate_log (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    operate_user INT COMMENT '操作人ID',
    operate_time DATETIME NOT NULL COMMENT '操作时间',
    class_name VARCHAR(100) NOT NULL COMMENT '操作的类名',
    method_name VARCHAR(100) NOT NULL COMMENT '操作的方法名',
    method_params VARCHAR(500) COMMENT '方法参数',
    return_value VARCHAR(2000) COMMENT '返回值',
    cost_time BIGINT COMMENT '方法执行耗时(单位:ms)',
    state INT NOT NULL COMMENT '操作状态: 1成功, 0失败'
) COMMENT '操作日志表';
