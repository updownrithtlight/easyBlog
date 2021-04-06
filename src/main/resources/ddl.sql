/*
Navicat MySQL Data Transfer

Source Server         : 本机
Source Server Version : 50732
Source Host           : localhost:3306
Source Database       : myblog

Target Server Type    : MYSQL
Target Server Version : 50732
File Encoding         : 65001

Date: 2021-04-06 15:16:21
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for article
-- ----------------------------
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article` (
                           `id` int(16) NOT NULL,
                           `del_flag` tinyint(1) NOT NULL COMMENT '删除状态：1删除，0未删除',
                           `create_by` int(16) NOT NULL COMMENT '创建人用户名',
                           `update_by` int(16) NOT NULL COMMENT '更新人',
                           `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                           `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                           `title` varchar(60) CHARACTER SET utf8 DEFAULT NULL,
                           `context` text CHARACTER SET utf8,
                           `subtitle` varchar(60) CHARACTER SET utf8 DEFAULT NULL,
                           `status` tinyint(1) DEFAULT NULL,
                           PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of article
-- ----------------------------

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
                           `id` int(16) NOT NULL,
                           `del_flag` tinyint(1) NOT NULL COMMENT '删除状态：1删除，0未删除',
                           `create_by` int(16) NOT NULL COMMENT '创建人用户名',
                           `update_by` int(16) NOT NULL COMMENT '更新人',
                           `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                           `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                           `post_id` int(16) DEFAULT NULL COMMENT '文章ID',
                           `comment_author` varchar(16) DEFAULT NULL,
                           `comment_author_ip` varchar(16) DEFAULT NULL,
                           `comment_content` varchar(100) DEFAULT NULL COMMENT '评论内容',
                           `parent_id` int(16) DEFAULT NULL,
                           `comment_status` tinyint(1) DEFAULT '1' COMMENT '论状态，0：正常，1：待审核，2：回收站',
                           `path_trace` varchar(100) DEFAULT NULL,
                           `comment_type` tinyint(1) DEFAULT NULL COMMENT '评论类型(匿名评论0，登录评论1)',
                           `is_admin` tinyint(1) DEFAULT NULL,
                           `comment_author_avatar` varchar(16) DEFAULT NULL COMMENT '评论人头像',
                           PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of comment
-- ----------------------------

-- ----------------------------
-- Table structure for link
-- ----------------------------
DROP TABLE IF EXISTS `link`;
CREATE TABLE `link` (
                        `id` int(16) NOT NULL,
                        `del_flag` tinyint(1) NOT NULL COMMENT '删除状态：1删除，0未删除',
                        `create_by` int(16) NOT NULL COMMENT '创建人用户名',
                        `update_by` int(16) NOT NULL COMMENT '更新人',
                        `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                        `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                        `link_name` varchar(255) DEFAULT NULL,
                        `link_url` varchar(255) DEFAULT NULL,
                        `link_pic` varchar(255) DEFAULT NULL,
                        `link_desc` varchar(255) DEFAULT NULL,
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of link
-- ----------------------------

-- ----------------------------
-- Table structure for log_info
-- ----------------------------
DROP TABLE IF EXISTS `log_info`;
CREATE TABLE `log_info` (
                            `id` int(16) NOT NULL,
                            `del_flag` tinyint(1) NOT NULL COMMENT '删除状态：1删除，0未删除',
                            `create_by` int(16) NOT NULL COMMENT '创建人用户名',
                            `update_by` int(16) NOT NULL COMMENT '更新人',
                            `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                            `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                            `name` varchar(32) DEFAULT NULL,
                            `log_type` tinyint(1) DEFAULT NULL,
                            `request_url` varchar(255) DEFAULT NULL,
                            `request_type` char(6) DEFAULT NULL,
                            `request_param` varchar(255) DEFAULT NULL,
                            `username` varchar(16) DEFAULT NULL,
                            `ip` varchar(32) DEFAULT NULL,
                            `ipInfo` varchar(32) DEFAULT NULL,
                            `cost_time` int(10) DEFAULT NULL,
                            PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of log_info
-- ----------------------------

-- ----------------------------
-- Table structure for login_info
-- ----------------------------
DROP TABLE IF EXISTS `login_info`;
CREATE TABLE `login_info` (
                              `id` int(16) NOT NULL,
                              `del_flag` tinyint(1) NOT NULL COMMENT '删除状态：1删除，0未删除',
                              `create_by` int(16) NOT NULL COMMENT '创建人用户名',
                              `update_by` int(16) NOT NULL COMMENT '更新人',
                              `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                              `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                              `user_id` int(16) DEFAULT NULL,
                              `last_login_time` datetime DEFAULT NULL,
                              `last_login_region` varchar(16) CHARACTER SET utf8 DEFAULT NULL,
                              `ipaddress` varchar(16) CHARACTER SET utf8 DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of login_info
-- ----------------------------

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu` (
                        `id` int(16) NOT NULL,
                        `del_flag` tinyint(1) NOT NULL COMMENT '删除状态：1删除，0未删除',
                        `create_by` int(16) NOT NULL COMMENT '创建人用户名',
                        `update_by` int(16) NOT NULL COMMENT '更新人',
                        `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                        `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                        `name` varchar(20) DEFAULT NULL,
                        `menu_url` varchar(20) DEFAULT NULL,
                        `menuIcon` varchar(20) DEFAULT NULL,
                        `cate_sort` int(10) DEFAULT NULL,
                        `parent_id` int(16) DEFAULT NULL,
                        `menu_type` tinyint(1) DEFAULT NULL COMMENT '菜单类型(0前台主要菜单，1前台顶部菜单)',
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of menu
-- ----------------------------

-- ----------------------------
-- Table structure for options
-- ----------------------------
DROP TABLE IF EXISTS `options`;
CREATE TABLE `options` (
                           `id` int(16) NOT NULL,
                           `del_flag` tinyint(1) NOT NULL COMMENT '删除状态：1删除，0未删除',
                           `create_by` int(16) NOT NULL COMMENT '创建人用户名',
                           `update_by` int(16) NOT NULL COMMENT '更新人',
                           `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                           `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                           `option_name` varchar(32) DEFAULT NULL,
                           `option_value` varchar(32) DEFAULT NULL,
                           `cate_sort` int(10) DEFAULT NULL,
                           PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of options
-- ----------------------------

-- ----------------------------
-- Table structure for permission
-- ----------------------------
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission` (
                              `id` int(16) NOT NULL,
                              `del_flag` tinyint(1) NOT NULL COMMENT '删除状态：1删除，0未删除',
                              `create_by` int(16) NOT NULL COMMENT '创建人用户名',
                              `update_by` int(16) NOT NULL COMMENT '更新人',
                              `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                              `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                              `name` varchar(20) DEFAULT NULL,
                              `url` varchar(100) DEFAULT NULL,
                              `icon` varchar(64) DEFAULT NULL,
                              `sort` int(10) DEFAULT NULL,
                              `resource_type` char(4) DEFAULT NULL,
                              `pid` int(16) DEFAULT NULL,
                              `level` int(10) DEFAULT NULL,
                              PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of permission
-- ----------------------------

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
                        `id` int(16) NOT NULL,
                        `del_flag` tinyint(1) NOT NULL COMMENT '删除状态：1删除，0未删除',
                        `create_by` int(16) NOT NULL COMMENT '创建人用户名',
                        `update_by` int(16) NOT NULL COMMENT '更新人',
                        `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                        `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                        `role` varchar(20) DEFAULT NULL COMMENT '角色名称admin，author，subscriber',
                        `description` varchar(20) DEFAULT NULL COMMENT '描述：管理员，作者，订阅者',
                        `level` int(10) DEFAULT NULL,
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role
-- ----------------------------

-- ----------------------------
-- Table structure for topic
-- ----------------------------
DROP TABLE IF EXISTS `topic`;
CREATE TABLE `topic` (
                         `id` int(16) NOT NULL,
                         `del_flag` tinyint(1) NOT NULL COMMENT '删除状态：1删除，0未删除',
                         `create_by` int(16) NOT NULL COMMENT '创建人用户名',
                         `update_by` int(16) NOT NULL COMMENT '更新人',
                         `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                         `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                         `name` varchar(60) CHARACTER SET utf8 NOT NULL,
                         `description` varchar(100) CHARACTER SET utf8 DEFAULT NULL,
                         `parent_id` int(16) DEFAULT NULL,
                         `is_leaf` tinyint(1) DEFAULT '1' COMMENT '0:否，1是',
                         `status` tinyint(1) DEFAULT NULL,
                         `path_trace` varchar(100) CHARACTER SET utf8 DEFAULT NULL,
                         `cate_sort` int(10) DEFAULT NULL,
                         `user_id` int(16) DEFAULT NULL,
                         PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of topic
-- ----------------------------

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
                        `id` int(16) NOT NULL,
                        `del_flag` tinyint(1) NOT NULL COMMENT '删除状态：1删除，0未删除',
                        `create_by` int(16) NOT NULL COMMENT '创建人用户名',
                        `update_by` int(16) NOT NULL COMMENT '更新人',
                        `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                        `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                        `name` varchar(20) CHARACTER SET utf8 DEFAULT NULL,
                        `nick_name` varchar(30) CHARACTER SET utf8 DEFAULT NULL,
                        `password` char(8) CHARACTER SET utf8 DEFAULT NULL,
                        `email` varchar(16) CHARACTER SET utf8 DEFAULT NULL,
                        `gender` tinyint(1) DEFAULT '2' COMMENT '0女,1男,2不愿透露',
                        `vip_level` tinyint(1) DEFAULT '0' COMMENT '0，1，2，3，4，5，6，7',
                        `register_time` datetime DEFAULT NULL COMMENT '注册时间',
                        `is_admin` char(8) CHARACTER SET utf8 DEFAULT NULL,
                        `email_enable` tinyint(1) DEFAULT NULL,
                        `status` tinyint(1) DEFAULT '0' COMMENT '0 正常,1 禁用,2 已删除',
                        `user_desc` varchar(100) CHARACTER SET utf8 DEFAULT NULL,
                        `user_avatar` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '头像',
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
