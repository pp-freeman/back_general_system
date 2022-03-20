DROP TABLE IF EXISTS `BookInfo`;
CREATE TABLE `BookInfo` (
  `id` bigint(100) NOT NULL  AUTO_INCREMENT COMMENT '自增id',
  `name` varchar(64) NOT NULL  COMMENT '书名',
  `create_time` datetime NOT NULL,
  `create_by` varchar(36) NOT NULL,
  `modify_time` datetime NOT NULL,
  `modify_by` varchar(36) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4;