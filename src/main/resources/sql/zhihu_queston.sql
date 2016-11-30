/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50713
Source Host           : localhost:3306
Source Database       : sys

Target Server Type    : MYSQL
Target Server Version : 50713
File Encoding         : 65001

Date: 2016-11-30 18:01:02
*/
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `zhihu_a`
-- ----------------------------
DROP TABLE IF EXISTS `zhihu_a`;
CREATE TABLE `zhihu_a` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `answer` varchar(20000) DEFAULT NULL,
  `q_id` varchar(25) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of zhihu_a
-- ----------------------------

-- ----------------------------
-- Table structure for `zhihu_q`
-- ----------------------------
DROP TABLE IF EXISTS `zhihu_q`;
CREATE TABLE `zhihu_q` (
  `id` varchar(25) NOT NULL,
  `question` varchar(500) DEFAULT NULL,
  `description` varchar(1000) DEFAULT NULL,
  `answer_num` int(25) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of zhihu_q
-- ----------------------------
