DROP TABLE IF EXISTS `User`;
CREATE TABLE `User` (
  `id` bigint(100) NOT NULL  AUTO_INCREMENT COMMENT 'id',
  `account` varchar(36) NOT NULL  COMMENT 'account',
  `user_name` varchar(36) NOT NULL  COMMENT 'user_name',
  `pass_word` varchar(36) NOT NULL  COMMENT 'pass_word',
  `create_time` datetime NOT NULL,
  `create_by` varchar(36) NOT NULL,
  `modify_time` datetime NOT NULL,
  `modify_by` varchar(36) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4;