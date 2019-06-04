/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : alipay_apy

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2019-06-04 12:18:49
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `pay`
-- ----------------------------
DROP TABLE IF EXISTS `pay`;
CREATE TABLE `pay` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `outTradeNo` varchar(125) NOT NULL,
  `totalAmount` decimal(5,2) NOT NULL,
  `subject` varchar(520) NOT NULL,
  `body` varchar(520) DEFAULT NULL,
  `tradeNo` varchar(520) DEFAULT NULL,
  `createTime` datetime NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `version` int(3) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`,`outTradeNo`)
) ENGINE=InnoDB AUTO_INCREMENT=152 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pay
-- ----------------------------
INSERT INTO `pay` VALUES ('121', 'ORDER-2019-06-02-17-42-43-1559468563951-8', '100.00', '购买金钥匙', '购买十个金钥匙', null, '2019-06-02 05:42:45', null, '0');
INSERT INTO `pay` VALUES ('122', 'ORDER-2019-06-02-17-42-59-1559468579308-9', '100.00', '购买金钥匙', '购买十个金钥匙', null, '2019-06-02 05:42:59', null, '0');
INSERT INTO `pay` VALUES ('123', 'ORDER-2019-06-02-17-43-52-1559468632579-10', '100.00', '购买金钥匙', '购买十个金钥匙', null, '2019-06-02 05:43:52', null, '0');
INSERT INTO `pay` VALUES ('124', 'ORDER-2019-06-02-17-46-25-1559468785417-11', '100.00', '购买金钥匙', '购买十个金钥匙', null, '2019-06-02 05:46:26', null, '0');
INSERT INTO `pay` VALUES ('125', 'ORDER-2019-06-02-17-46-30-1559468790018-12', '100.00', '购买金钥匙', '购买十个金钥匙', null, '2019-06-02 05:46:30', null, '0');
INSERT INTO `pay` VALUES ('126', 'ORDER-2019-06-02-17-50-44-1559469044590-13', '100.00', '购买金钥匙', '购买十个金钥匙', null, '2019-06-02 05:50:45', null, '0');
INSERT INTO `pay` VALUES ('127', 'ORDER-2019-06-02-17-50-47-1559469047763-14', '100.00', '购买金钥匙', '购买十个金钥匙', null, '2019-06-02 05:50:47', null, '0');
INSERT INTO `pay` VALUES ('128', 'ORDER-2019-06-02-17-53-23-1559469203872-15', '100.00', '购买金钥匙', '购买十个金钥匙', null, '2019-06-02 05:53:25', null, '0');
INSERT INTO `pay` VALUES ('129', 'ORDER-2019-06-02-17-53-28-1559469208834-16', '100.00', '购买金钥匙', '购买十个金钥匙', null, '2019-06-02 05:53:28', null, '0');
INSERT INTO `pay` VALUES ('130', 'ORDER-2019-06-02-17-57-59-1559469479904-17', '100.00', '购买金钥匙', '购买十个金钥匙', null, '2019-06-02 05:58:01', null, '0');
INSERT INTO `pay` VALUES ('131', 'ORDER-2019-06-02-17-58-04-1559469484179-18', '100.00', '购买金钥匙', '购买十个金钥匙', null, '2019-06-02 05:58:04', null, '0');
INSERT INTO `pay` VALUES ('132', 'ORDER-2019-06-02-18-10-52-1559470252822-19', '100.00', '购买金钥匙', '购买十个金钥匙', null, '2019-06-02 06:10:54', null, '0');
INSERT INTO `pay` VALUES ('133', 'ORDER-2019-06-02-18-10-56-1559470256037-20', '100.00', '购买金钥匙', '购买十个金钥匙', null, '2019-06-02 06:10:56', null, '0');
INSERT INTO `pay` VALUES ('134', 'ORDER-2019-06-02-18-15-37-1559470537640-21', '100.00', '购买金钥匙', '购买十个金钥匙', null, '2019-06-02 06:15:39', null, '0');
INSERT INTO `pay` VALUES ('135', 'ORDER-2019-06-02-18-15-48-1559470548263-22', '100.00', '购买金钥匙', '购买十个金钥匙', null, '2019-06-02 06:15:48', null, '0');
INSERT INTO `pay` VALUES ('136', 'ORDER-2019-06-02-18-34-12-1559471652170-2', '100.00', '购买金钥匙', '购买十个金钥匙', null, '2019-06-02 06:34:39', null, '0');
INSERT INTO `pay` VALUES ('137', 'ORDER-2019-06-02-18-38-48-1559471928558-1', '100.00', '购买金钥匙', '购买十个金钥匙', null, '2019-06-02 06:38:50', null, '0');
INSERT INTO `pay` VALUES ('138', 'ORDER-2019-06-02-18-38-48-1559471928558-1', '100.00', '购买金钥匙', '购买十个金钥匙', null, '2019-06-02 06:39:07', null, '0');
INSERT INTO `pay` VALUES ('139', 'ORDER-2019-06-02-18-38-48-1559471928558-1', '100.00', '购买金钥匙', '购买十个金钥匙', null, '2019-06-02 06:39:39', null, '0');
INSERT INTO `pay` VALUES ('140', 'ORDER-2019-06-02-18-38-48-1559471928558-1', '100.00', '购买金钥匙', '购买十个金钥匙', null, '2019-06-02 06:42:12', null, '0');
INSERT INTO `pay` VALUES ('141', 'ORDER-2019-06-02-18-38-48-1559471928558-1', '100.00', '购买金钥匙', '购买十个金钥匙', null, '2019-06-02 06:42:23', null, '0');
INSERT INTO `pay` VALUES ('142', 'ORDER-2019-06-02-18-38-48-1559471928558-1', '100.00', '购买金钥匙', '购买十个金钥匙', null, '2019-06-02 06:42:36', null, '0');
INSERT INTO `pay` VALUES ('143', 'ORDER-2019-06-02-18-42-49-1559472169613-2', '100.00', '购买金钥匙', '购买十个金钥匙', null, '2019-06-02 06:42:49', null, '0');
INSERT INTO `pay` VALUES ('144', 'ORDER-2019-06-02-18-51-19-1559472679139-1', '100.00', '购买金钥匙', '购买十个金钥匙', null, '2019-06-02 06:51:20', null, '0');
INSERT INTO `pay` VALUES ('145', 'ORDER-2019-06-02-18-51-19-1559472679139-1', '100.00', '购买金钥匙', '购买十个金钥匙', null, '2019-06-02 06:51:25', null, '0');
INSERT INTO `pay` VALUES ('146', 'ORDER-2019-06-02-18-51-19-1559472679139-1', '100.00', '购买金钥匙', '购买十个金钥匙', null, '2019-06-02 06:55:33', null, '0');
INSERT INTO `pay` VALUES ('147', 'ORDER-2019-06-02-18-51-19-1559472679139-1', '100.00', '购买金钥匙', '购买十个金钥匙', null, '2019-06-02 06:55:34', null, '0');
INSERT INTO `pay` VALUES ('148', 'ORDER-2019-06-02-18-55-54-1559472954536-2', '100.00', '购买金钥匙', '购买十个金钥匙', null, '2019-06-02 06:55:54', null, '0');
INSERT INTO `pay` VALUES ('149', 'ORDER-2019-06-02-18-55-54-1559472954536-2', '100.00', '购买金钥匙', '购买十个金钥匙', null, '2019-06-02 06:57:05', null, '0');
INSERT INTO `pay` VALUES ('150', 'ORDER-2019-06-02-18-59-17-1559473157365-3', '100.00', '购买金钥匙', '购买十个金钥匙', null, '2019-06-02 06:59:18', null, '0');
INSERT INTO `pay` VALUES ('151', 'ORDER-2019-06-02-18-59-17-1559473157365-3', '100.00', '购买金钥匙', '购买十个金钥匙', null, '2019-06-02 06:59:21', null, '0');
