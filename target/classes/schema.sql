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
