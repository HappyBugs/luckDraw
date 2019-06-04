/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : lucadraw_sso

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2019-06-04 12:19:00
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(8) unsigned NOT NULL AUTO_INCREMENT,
  `accountNumber` varchar(15) NOT NULL,
  `passWord` varchar(255) NOT NULL,
  `nickName` varchar(25) NOT NULL,
  `registerTime` datetime NOT NULL,
  `version` int(3) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`,`accountNumber`)
) ENGINE=InnoDB AUTO_INCREMENT=905 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('903', '807117348630', 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjM0NTYifQ.-9tRS9x1wt6wwEQldXPEdTvra6wl8s1bWayzAPMyZIo', '807117348630', '2019-05-31 10:21:32', '0');
INSERT INTO `user` VALUES ('904', '218607021166', 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjM0NTYifQ.-9tRS9x1wt6wwEQldXPEdTvra6wl8s1bWayzAPMyZIo', '218607021166', '2019-06-02 10:42:01', '0');
