DROP TABLE IF EXISTS `Test`;
CREATE TABLE `Test` (
  `id` bigint(100) NOT NULL  AUTO_INCREMENT COMMENT 'sad',
  `birthday` date DEFAULT NULL  COMMENT 'sad',
  `create_time` datetime NOT NULL,
  `create_by` varchar(36) NOT NULL,
  `modify_time` datetime NOT NULL,
  `modify_by` varchar(36) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4;