/*
Navicat MySQL Data Transfer

Source Server         : 本机
Source Server Version : 50732
Source Host           : localhost:3306
Source Database       : myblog2

Target Server Type    : MYSQL
Target Server Version : 50732
File Encoding         : 65001

Date: 2021-04-27 12:58:47
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for article
-- ----------------------------
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article` (
  `id` int(16) NOT NULL AUTO_INCREMENT,
  `del_flag` tinyint(1) NOT NULL COMMENT '删除状态：1删除，0未删除',
  `create_by` int(16) NOT NULL COMMENT '创建人用户名',
  `update_by` int(16) NOT NULL COMMENT '更新人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `title` varchar(60) DEFAULT NULL,
  `context` text,
  `subtitle` varchar(60) DEFAULT NULL,
  `status` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for attachment
-- ----------------------------
DROP TABLE IF EXISTS `attachment`;
CREATE TABLE `attachment` (
  `id` int(16) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `del_flag` tinyint(1) NOT NULL COMMENT '删除状态：1删除，0未删除',
  `create_by` int(16) NOT NULL COMMENT '创建人用户名',
  `update_by` int(16) NOT NULL COMMENT '更新人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `attach_name` varchar(100) DEFAULT NULL COMMENT '附件名',
  `attach_path` varchar(300) DEFAULT NULL COMMENT '附件路径',
  `attach_small_path` varchar(300) DEFAULT NULL COMMENT '附件缩略图路径',
  `attach_type` varchar(10) DEFAULT NULL COMMENT '附件类型',
  `attach_suffix` varchar(10) DEFAULT NULL COMMENT '附件后缀',
  `attach_size` varchar(10) DEFAULT NULL COMMENT '附件大小',
  `attach_wh` varchar(10) DEFAULT NULL COMMENT '附件长宽',
  `attach_location` varchar(10) DEFAULT NULL COMMENT '附件存储地址',
  `attach_origin` varchar(10) DEFAULT NULL COMMENT '附件来源，0：上传，1：外部链接',
  `user_id` int(16) NOT NULL COMMENT '所属用户',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
  `id` int(100) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `del_flag` tinyint(1) NOT NULL COMMENT '删除状态：1删除，0未删除',
  `create_by` varchar(16) DEFAULT NULL COMMENT '创建人用户名',
  `update_by` varchar(16) DEFAULT NULL COMMENT '更新人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `cate_name` varchar(100) DEFAULT NULL COMMENT '分类名称不能为空',
  `cate_pid` int(16) DEFAULT NULL COMMENT '分类父节点',
  `cate_sort` int(10) DEFAULT NULL COMMENT '分类排序号',
  `cate_level` tinyint(2) DEFAULT NULL COMMENT '分类层级',
  `path_trace` varchar(255) DEFAULT NULL COMMENT '关系路径',
  `cate_desc` varchar(255) DEFAULT NULL COMMENT '关系路径',
  `user_id` int(16) DEFAULT NULL COMMENT '所属用户ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
  `id` int(16) NOT NULL AUTO_INCREMENT,
  `del_flag` tinyint(1) NOT NULL COMMENT '删除状态：1删除，0未删除',
  `create_by` int(16) NOT NULL COMMENT '创建人用户名',
  `update_by` int(16) NOT NULL COMMENT '更新人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `post_id` int(16) DEFAULT NULL COMMENT '文章ID',
  `comment_author` varchar(16) DEFAULT NULL COMMENT '评论人',
  `comment_author_email` varchar(16) DEFAULT NULL COMMENT '评论人的邮箱',
  `comment_author_url` varchar(16) DEFAULT NULL COMMENT '评论人的主页',
  `comment_author_ip` varchar(16) DEFAULT NULL COMMENT '评论人的ip',
  `comment_author_email_Md5` varchar(16) DEFAULT NULL COMMENT 'Email的md5，用于gavatar',
  `comment_author_avatar` varchar(16) DEFAULT NULL COMMENT '评论人头像',
  `comment_content` varchar(100) DEFAULT NULL COMMENT '评论内容',
  `comment_agent` varchar(100) DEFAULT NULL COMMENT '评论者ua信息',
  `commentParent` int(16) DEFAULT NULL COMMENT '上一级',
  `comment_status` tinyint(1) DEFAULT '1' COMMENT '论状态，0：正常，1：待审核，2：回收站',
  `is_admin` tinyint(1) DEFAULT NULL COMMENT '是否是博主的评论 0:不是 1:是',
  `user_id` int(16) DEFAULT NULL COMMENT '所属用户ID',
  `path_trace` varchar(100) DEFAULT NULL COMMENT '评论内容',
  `accept_user_id` varchar(100) DEFAULT NULL COMMENT '接受者用户Id',
  `comment_type` tinyint(1) DEFAULT NULL COMMENT '评论类型(匿名评论0，登录评论1)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for link
-- ----------------------------
DROP TABLE IF EXISTS `link`;
CREATE TABLE `link` (
  `id` int(16) NOT NULL AUTO_INCREMENT,
  `del_flag` tinyint(1) NOT NULL COMMENT '删除状态：1删除，0未删除',
  `create_by` int(16) NOT NULL COMMENT '创建人用户名',
  `update_by` int(16) NOT NULL COMMENT '更新人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `link_name` varchar(255) DEFAULT NULL COMMENT '友情链接名称',
  `link_url` varchar(255) DEFAULT NULL COMMENT '友情链接地址',
  `link_pic` varchar(255) DEFAULT NULL COMMENT '友情链接头像',
  `link_desc` varchar(255) DEFAULT NULL COMMENT '友情链接描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for log
-- ----------------------------
DROP TABLE IF EXISTS `log`;
CREATE TABLE `log` (
  `id` int(16) NOT NULL AUTO_INCREMENT,
  `del_flag` tinyint(1) NOT NULL COMMENT '删除状态：1删除，0未删除',
  `create_by` int(16) DEFAULT NULL COMMENT '创建人用户名',
  `update_by` int(16) DEFAULT NULL COMMENT '更新人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `name` varchar(255) DEFAULT NULL COMMENT '友情链接名称',
  `log_type` varchar(255) DEFAULT NULL COMMENT '日志类型',
  `request_url` varchar(255) DEFAULT NULL COMMENT '请求路径',
  `request_type` varchar(255) DEFAULT NULL COMMENT '请求类型',
  `request_param` varchar(255) DEFAULT NULL COMMENT '请求参数',
  `username` varchar(255) DEFAULT NULL COMMENT '请求用户',
  `ip` varchar(255) DEFAULT NULL COMMENT 'ip',
  `ip_info` varchar(255) DEFAULT NULL COMMENT 'ip信息',
  `cost_time` varchar(255) DEFAULT NULL COMMENT '花费时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for log_info
-- ----------------------------
DROP TABLE IF EXISTS `log_info`;
CREATE TABLE `log_info` (
  `id` int(16) NOT NULL AUTO_INCREMENT,
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
-- Table structure for login_info
-- ----------------------------
DROP TABLE IF EXISTS `login_info`;
CREATE TABLE `login_info` (
  `id` int(16) NOT NULL AUTO_INCREMENT,
  `del_flag` tinyint(1) NOT NULL COMMENT '删除状态：1删除，0未删除',
  `create_by` int(16) NOT NULL COMMENT '创建人用户名',
  `update_by` int(16) NOT NULL COMMENT '更新人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `user_id` int(16) DEFAULT NULL,
  `last_login_time` datetime DEFAULT NULL,
  `last_login_region` varchar(16) DEFAULT NULL,
  `ipaddress` varchar(16) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for mail_retrieve
-- ----------------------------
DROP TABLE IF EXISTS `mail_retrieve`;
CREATE TABLE `mail_retrieve` (
  `id` int(16) NOT NULL AUTO_INCREMENT,
  `del_flag` tinyint(1) NOT NULL COMMENT '删除状态：1删除，0未删除',
  `create_by` int(16) NOT NULL COMMENT '创建人用户名',
  `update_by` int(16) NOT NULL COMMENT '更新人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `user_id` int(16) NOT NULL COMMENT '用户Id',
  `email` varchar(200) NOT NULL COMMENT 'Email',
  `code` varchar(200) NOT NULL COMMENT '验证码',
  `outTime` datetime NOT NULL COMMENT '过期时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu` (
  `id` int(16) NOT NULL AUTO_INCREMENT,
  `del_flag` tinyint(1) NOT NULL COMMENT '删除状态：1删除，0未删除',
  `create_by` int(16) DEFAULT NULL COMMENT '创建人用户名',
  `update_by` int(16) DEFAULT NULL COMMENT '更新人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `menu_name` varchar(20) DEFAULT NULL COMMENT '菜单名称',
  `menu_url` varchar(20) DEFAULT NULL COMMENT '菜单路径',
  `menu_icon` varchar(20) DEFAULT NULL COMMENT '图标，可选，部分主题可显示',
  `menu_target` varchar(20) DEFAULT NULL COMMENT '打开方式',
  `menu_sort` int(10) DEFAULT NULL COMMENT '排序编号',
  `menu_pid` int(16) DEFAULT NULL COMMENT '菜单Pid',
  `menu_type` tinyint(1) DEFAULT NULL COMMENT '菜单类型(0前台主要菜单，1前台顶部菜单)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for options
-- ----------------------------
DROP TABLE IF EXISTS `options`;
CREATE TABLE `options` (
  `id` int(16) NOT NULL AUTO_INCREMENT,
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
-- Table structure for permission
-- ----------------------------
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission` (
  `id` int(16) NOT NULL AUTO_INCREMENT,
  `del_flag` tinyint(1) NOT NULL COMMENT '删除状态：1删除，0未删除',
  `create_by` int(16) NOT NULL COMMENT '创建人用户名',
  `update_by` int(16) NOT NULL COMMENT '更新人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `name` varchar(20) DEFAULT NULL COMMENT '权限名称',
  `url` varchar(100) DEFAULT NULL COMMENT '请求URL',
  `icon` varchar(64) DEFAULT NULL COMMENT '图标',
  `target` varchar(64) DEFAULT NULL COMMENT '打开方式(_self或target)',
  `sort` int(10) DEFAULT NULL COMMENT '序号(越小越靠前)',
  `resource_type` char(4) DEFAULT NULL COMMENT '资源类型',
  `pid` int(16) DEFAULT NULL COMMENT 'pid',
  `level` int(10) DEFAULT NULL COMMENT '级别',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for post
-- ----------------------------
DROP TABLE IF EXISTS `post`;
CREATE TABLE `post` (
  `id` int(16) NOT NULL AUTO_INCREMENT,
  `del_flag` tinyint(1) NOT NULL COMMENT '删除状态：1删除，0未删除',
  `create_by` int(16) NOT NULL COMMENT '创建人用户名',
  `update_by` int(16) NOT NULL COMMENT '更新人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `user_id` int(16) DEFAULT NULL COMMENT '用户ID',
  `post_title` varchar(20) DEFAULT NULL COMMENT '文章标题',
  `post_type` varchar(20) DEFAULT NULL COMMENT '文章类型',
  `post_content` varchar(20) DEFAULT NULL COMMENT '文章内容 html格式',
  `post_url` varchar(20) DEFAULT NULL COMMENT '文章路径',
  `post_summary` varchar(20) DEFAULT NULL COMMENT '文章摘要',
  `post_thumbnail` varchar(20) DEFAULT NULL COMMENT '缩略图',
  `post_status` varchar(20) DEFAULT NULL COMMENT '0 已发布,1 草稿,2 回收站',
  `post_views` varchar(20) DEFAULT NULL COMMENT '文章访问量',
  `post_likes` varchar(20) DEFAULT NULL COMMENT '点赞访问量',
  `comment_size` varchar(20) DEFAULT NULL COMMENT '评论数量(冗余字段，加快查询速度)',
  `post_source` varchar(20) DEFAULT NULL COMMENT '文章来源（原创1，转载2，翻译3）',
  `allow_comment` varchar(20) DEFAULT NULL COMMENT '是否允许评论（允许1，禁止0）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for post_category_ref
-- ----------------------------
DROP TABLE IF EXISTS `post_category_ref`;
CREATE TABLE `post_category_ref` (
  `id` int(16) NOT NULL AUTO_INCREMENT,
  `del_flag` tinyint(1) NOT NULL COMMENT '删除状态：1删除，0未删除',
  `create_by` int(16) NOT NULL COMMENT '创建人用户名',
  `update_by` int(16) NOT NULL COMMENT '更新人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `postId` int(16) DEFAULT NULL COMMENT '文章Id',
  `cateId` int(16) DEFAULT NULL COMMENT '分类Id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for post_tag_ref
-- ----------------------------
DROP TABLE IF EXISTS `post_tag_ref`;
CREATE TABLE `post_tag_ref` (
  `id` int(16) NOT NULL AUTO_INCREMENT,
  `del_flag` tinyint(1) NOT NULL COMMENT '删除状态：1删除，0未删除',
  `create_by` int(16) NOT NULL COMMENT '创建人用户名',
  `update_by` int(16) NOT NULL COMMENT '更新人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `postId` int(16) DEFAULT NULL COMMENT '文章Id',
  `tag_id` int(16) DEFAULT NULL COMMENT '标签Id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` int(16) NOT NULL AUTO_INCREMENT,
  `del_flag` tinyint(1) NOT NULL COMMENT '删除状态：1删除，0未删除',
  `create_by` int(16) NOT NULL COMMENT '创建人用户名',
  `update_by` int(16) NOT NULL COMMENT '更新人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `role` varchar(20) DEFAULT NULL COMMENT '角色名称admin，author，subscriber',
  `description` varchar(20) DEFAULT NULL COMMENT '描述：管理员，作者，订阅者',
  `level` int(10) DEFAULT NULL COMMENT '级别',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for role_permission_ref
-- ----------------------------
DROP TABLE IF EXISTS `role_permission_ref`;
CREATE TABLE `role_permission_ref` (
  `id` int(16) NOT NULL AUTO_INCREMENT,
  `del_flag` tinyint(1) NOT NULL COMMENT '删除状态：1删除，0未删除',
  `create_by` int(16) NOT NULL COMMENT '创建人用户名',
  `update_by` int(16) NOT NULL COMMENT '更新人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `role_id` int(16) DEFAULT NULL COMMENT '角色Id',
  `permission_id` int(16) DEFAULT NULL COMMENT '权限Id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tag
-- ----------------------------
DROP TABLE IF EXISTS `tag`;
CREATE TABLE `tag` (
  `id` int(16) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `del_flag` tinyint(1) NOT NULL COMMENT '删除状态：1删除，0未删除',
  `create_by` int(16) NOT NULL COMMENT '创建人用户名',
  `update_by` int(16) NOT NULL COMMENT '更新人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `tag_name` varchar(100) DEFAULT NULL COMMENT '附件名',
  `user_id` int(16) DEFAULT NULL,
  `count` int(16) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for topic
-- ----------------------------
DROP TABLE IF EXISTS `topic`;
CREATE TABLE `topic` (
  `id` int(16) NOT NULL AUTO_INCREMENT,
  `del_flag` tinyint(1) NOT NULL COMMENT '删除状态：1删除，0未删除',
  `create_by` int(16) NOT NULL COMMENT '创建人用户名',
  `update_by` int(16) NOT NULL COMMENT '更新人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `name` varchar(60) NOT NULL,
  `description` varchar(100) DEFAULT NULL,
  `parent_id` int(16) DEFAULT NULL,
  `is_leaf` tinyint(1) DEFAULT '1' COMMENT '0:否，1是',
  `status` tinyint(1) DEFAULT NULL,
  `path_trace` varchar(100) DEFAULT NULL,
  `cate_sort` int(10) DEFAULT NULL,
  `user_id` int(16) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(16) NOT NULL AUTO_INCREMENT,
  `del_flag` tinyint(1) NOT NULL COMMENT '删除状态：1删除，0未删除',
  `create_by` int(16) DEFAULT NULL COMMENT '创建人用户名',
  `update_by` int(16) DEFAULT NULL COMMENT '更新人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `user_name` varchar(20) NOT NULL,
  `user_display_name` varchar(30) DEFAULT NULL,
  `user_pass` varchar(100)  DEFAULT NULL,
  `user_email` varchar(32)  DEFAULT NULL,
  `gender` char(10) DEFAULT '2' COMMENT '0女,1男,2不愿透露',
  `email_enable` varchar(10) DEFAULT NULL,
  `is_admin` char(8) DEFAULT NULL,
  `user_avatar` varchar(20) DEFAULT NULL COMMENT '头像',
  `status` tinyint(1) DEFAULT '0' COMMENT '0 正常,1 禁用,2 已删除',
  `user_desc` varchar(100) DEFAULT NULL,
  `salt` varchar(100) DEFAULT NULL,
  `user_site` varchar(100) DEFAULT NULL,
  `login_enable` char(10) DEFAULT NULL,
  `login_last` datetime DEFAULT NULL,
  `login_error` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user_role_ref
-- ----------------------------
DROP TABLE IF EXISTS `user_role_ref`;
CREATE TABLE `user_role_ref` (
  `id` int(16) NOT NULL AUTO_INCREMENT,
  `del_flag` tinyint(1) NOT NULL COMMENT '删除状态：1删除，0未删除',
  `create_by` int(16) NOT NULL COMMENT '创建人用户名',
  `update_by` int(16) NOT NULL COMMENT '更新人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `user_id` int(16) DEFAULT NULL,
  `role_id` int(16) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for widget
-- ----------------------------
DROP TABLE IF EXISTS `widget`;
CREATE TABLE `widget` (
  `id` int(16) NOT NULL AUTO_INCREMENT,
  `del_flag` tinyint(1) NOT NULL COMMENT '删除状态：1删除，0未删除',
  `create_by` int(16) NOT NULL COMMENT '创建人用户名',
  `update_by` int(16) NOT NULL COMMENT '更新人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
