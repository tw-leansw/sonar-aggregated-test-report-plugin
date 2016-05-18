CREATE TABLE my_data (
  name varchar(50)  DEFAULT NULL,
  age int(11) DEFAULT NULL
);

CREATE TABLE `lean_test_step` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `duration` double DEFAULT NULL,
  `result` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

