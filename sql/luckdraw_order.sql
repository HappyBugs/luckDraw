/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : luckdraw_order

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2019-06-04 12:19:19
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `order`
-- ----------------------------
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order` (
  `oId` int(5) NOT NULL AUTO_INCREMENT,
  `orderNumber` varchar(125) NOT NULL,
  `accountNumber` varchar(25) NOT NULL,
  `orderDescribe` varchar(255) DEFAULT NULL,
  `createTime` datetime NOT NULL,
  `payState` int(1) NOT NULL DEFAULT '0' COMMENT '0 未支付 1 已支付 2异常金额',
  `money` decimal(5,2) NOT NULL,
  `type` int(1) NOT NULL COMMENT '订单购买的金钥匙的类型 0 一个钥匙 2 十个钥匙',
  `payToken` varchar(225) DEFAULT NULL,
  `updateTime` datetime DEFAULT NULL,
  `version` int(5) NOT NULL DEFAULT '0',
  PRIMARY KEY (`oId`,`orderNumber`)
) ENGINE=InnoDB AUTO_INCREMENT=119 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of order
-- ----------------------------
INSERT INTO `order` VALUES ('114', 'ORDER-2019-06-02-18-38-48-1559471928558-1', '218607021166', '购买十个金钥匙', '2019-06-02 06:38:48', '1', '100.00', '1', 'PAYTOKEN-2019-06-02-18-38-48-1559471928479KZDISQPRVI', null, '0');
INSERT INTO `order` VALUES ('115', 'ORDER-2019-06-02-18-42-49-1559472169613-2', '218607021166', '购买十个金钥匙', '2019-06-02 06:42:49', '1', '100.00', '1', 'PAYTOKEN-2019-06-02-18-42-49-1559472169607HCCIQHIQMH', null, '1');
INSERT INTO `order` VALUES ('116', 'ORDER-2019-06-02-18-51-19-1559472679139-1', '218607021166', '购买十个金钥匙', '2019-06-02 06:51:19', '1', '100.00', '1', 'PAYTOKEN-2019-06-02-18-51-19-1559472679098QNNDOWWSGZ', null, '0');
INSERT INTO `order` VALUES ('117', 'ORDER-2019-06-02-18-55-54-1559472954536-2', '218607021166', '购买十个金钥匙', '2019-06-02 06:55:54', '1', '100.00', '1', 'PAYTOKEN-2019-06-02-18-55-54-1559472954525SVGQCPBRRF', null, '0');
INSERT INTO `order` VALUES ('118', 'ORDER-2019-06-02-18-59-17-1559473157365-3', '218607021166', '购买十个金钥匙', '2019-06-02 06:59:17', '0', '100.00', '1', 'PAYTOKEN-2019-06-02-18-59-17-1559473157358ICOSLRUCLT', null, '0');
