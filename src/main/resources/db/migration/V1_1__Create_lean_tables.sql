CREATE TABLE `lean_test_step` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `scenario_id` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `duration` double DEFAULT NULL,
  `result` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `lean_test_feature` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `report_id` INT NULL,
  `framework_type` VARCHAR(45) NULL,
  `test_type` VARCHAR(45) NULL,
  `name` VARCHAR(255) NULL,
  `description` VARCHAR(255) NULL,
  `duration` INT NULL,
  `passed_scenarios` INT NULL,
  `failed_scenarios` INT NULL,
  `skipped_scenarios` INT NULL,
  `create_time` DATETIME NULL,
  `execution_time` DATETIME NULL,
  PRIMARY KEY (`id`));

CREATE TABLE `lean_test_scenario` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `feature_id` INT NULL,
  `name` VARCHAR(255) NULL,
  `result_type` VARCHAR(45) NULL,
  `duration` INT NULL,
  PRIMARY KEY (`id`));

CREATE TABLE `lean_test_report` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_id` varchar(45) DEFAULT NULL,
  `build_label` varchar(45) DEFAULT NULL,
  `duration` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `execution_time` datetime DEFAULT NULL,
  `feature_num` int(11) DEFAULT '0',
  `scenario_num` int(11) DEFAULT '0',
  `step_num` int(11) DEFAULT '0',
  PRIMARY KEY (`id`));


