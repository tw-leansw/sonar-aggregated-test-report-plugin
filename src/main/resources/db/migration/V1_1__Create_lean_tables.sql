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

CREATE TABLE `lean_test_feature` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `report_id` INT NULL,
  `framework_type` VARCHAR(45) NULL,
  `test_type` VARCHAR(45) NULL,
  `name` VARCHAR(45) NULL,
  `description` VARCHAR(255) NULL,
  `duration` INT NULL,
  `passed_scenarios` INT NULL,
  `failed_scenarios` INT NULL,
  `skipped_scenarios` INT NULL,
  `build_label` VARCHAR(45) NULL,
  `create_time` DATETIME NULL,
  `execution_time` DATETIME NULL,
  PRIMARY KEY (`id`));

CREATE TABLE `lean_test_scenario` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `feature_id` INT NULL,
  `name` VARCHAR(45) NULL,
  `result_type` VARCHAR(45) NULL,
  `duration` INT NULL,
  PRIMARY KEY (`id`));


