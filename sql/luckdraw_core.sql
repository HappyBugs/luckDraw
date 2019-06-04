/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : luckdraw_core

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2019-06-04 12:19:08
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `commodity`
-- ----------------------------
DROP TABLE IF EXISTS `commodity`;
CREATE TABLE `commodity` (
  `cId` int(3) unsigned NOT NULL AUTO_INCREMENT,
  `probability` varchar(12) NOT NULL,
  `commodityNumber` varchar(225) NOT NULL,
  `commodityDescribe` varchar(225) DEFAULT NULL,
  `commodityName` varchar(225) NOT NULL,
  `exchange` int(4) NOT NULL,
  `decompose` int(3) NOT NULL,
  `logAddress` varchar(522) DEFAULT NULL,
  `version` int(3) NOT NULL DEFAULT '0',
  PRIMARY KEY (`cId`,`commodityNumber`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of commodity
-- ----------------------------
INSERT INTO `commodity` VALUES ('2', '1~2', 'cNumber-5890568989985', null, '苹果XMAX', '9999', '4500', null, '0');
INSERT INTO `commodity` VALUES ('3', '3~5', 'cNumber-5308247877979', null, 'IQOO(8+128)', '3200', '1600', null, '0');
INSERT INTO `commodity` VALUES ('4', '16~11', 'cNumber-4587956857746', null, 'Airpods(搭配无线充电盒)', '1600', '800', null, '0');
INSERT INTO `commodity` VALUES ('5', '12~21', 'cNumber-6819535703249', null, '现金1000元', '900', '450', null, '0');
INSERT INTO `commodity` VALUES ('6', '22~51', 'cNumber-8239604343678', null, '现金200元', '150', '45', null, '0');
INSERT INTO `commodity` VALUES ('7', '52~201', 'cNumber-3464936715941', null, '纪念抱枕一个', '50', '25', null, '0');
INSERT INTO `commodity` VALUES ('8', '202~301', 'cNumber-9162315003068', null, '话费优惠卷50元', '30', '15', null, '0');
INSERT INTO `commodity` VALUES ('9', '302~501', 'cNumber-3051889032680', null, '话费优惠卷30元', '20', '10', null, '0');
INSERT INTO `commodity` VALUES ('10', '202~1000', 'cNumber-9516972173778', null, '话费优惠卷10元', '5', '3', null, '0');

-- ----------------------------
-- Table structure for `goldkey`
-- ----------------------------
DROP TABLE IF EXISTS `goldkey`;
CREATE TABLE `goldkey` (
  `gId` int(3) unsigned NOT NULL AUTO_INCREMENT,
  `accountNumber` varchar(15) NOT NULL,
  `keyNumber` int(3) NOT NULL,
  `integral` int(3) NOT NULL DEFAULT '0',
  `luckNumber` int(3) NOT NULL DEFAULT '0',
  `version` int(3) NOT NULL DEFAULT '0',
  PRIMARY KEY (`gId`,`accountNumber`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of goldkey
-- ----------------------------
INSERT INTO `goldkey` VALUES ('10', '485796548745', '990', '0', '10', '9');

-- ----------------------------
-- Table structure for `luckdrawlog`
-- ----------------------------
DROP TABLE IF EXISTS `luckdrawlog`;
CREATE TABLE `luckdrawlog` (
  `lId` int(4) unsigned NOT NULL AUTO_INCREMENT,
  `createTime` datetime NOT NULL,
  `accountNumber` varchar(15) NOT NULL,
  `commodityNumber` varchar(225) NOT NULL,
  `version` int(3) NOT NULL DEFAULT '0',
  PRIMARY KEY (`lId`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of luckdrawlog
-- ----------------------------
INSERT INTO `luckdrawlog` VALUES ('8', '2019-06-03 07:31:06', '485796548745', 'cNumber-3464936715941', '0');
INSERT INTO `luckdrawlog` VALUES ('9', '2019-06-03 07:31:10', '485796548745', 'cNumber-3464936715941', '0');
INSERT INTO `luckdrawlog` VALUES ('10', '2019-06-03 07:31:52', '485796548745', 'cNumber-9162315003068', '0');
INSERT INTO `luckdrawlog` VALUES ('11', '2019-06-03 07:31:55', '485796548745', 'cNumber-9516972173778', '0');
INSERT INTO `luckdrawlog` VALUES ('12', '2019-06-03 07:31:58', '485796548745', 'cNumber-9516972173778', '0');
INSERT INTO `luckdrawlog` VALUES ('13', '2019-06-03 07:32:01', '485796548745', 'cNumber-9516972173778', '0');
