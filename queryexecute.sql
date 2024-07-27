-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema supportdb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema supportdb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `supportdb` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `supportdb` ;

-- -----------------------------------------------------
-- Table `supportdb`.`users_type`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `supportdb`.`users_type` (
  `user_type_id` INT NOT NULL AUTO_INCREMENT,
  `user_type_name` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`user_type_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 3
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `supportdb`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `supportdb`.`users` (
  `user_id` INT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(255) NOT NULL,
  `password` VARCHAR(255) NULL DEFAULT NULL,
  `user_type_id` INT NOT NULL,
  PRIMARY KEY (`user_id`, `email`),
  INDEX `fk_users_users_type_idx` (`user_type_id` ASC),
  CONSTRAINT `fk_users_users_type`
    FOREIGN KEY (`user_type_id`)
    REFERENCES `supportdb`.`users_type` (`user_type_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 8
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `supportdb`.`users_type_seq`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `supportdb`.`users_type_seq` (
  `next_val` BIGINT NULL DEFAULT NULL)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;


CREATE TABLE `admin_profile` (
  `user_account_id` int NOT NULL,
  `city` varchar(255) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`user_account_id`),
  CONSTRAINT `fk_admin_profile_users_idx` FOREIGN KEY (`user_account_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `staff_profile` (
  `user_account_id` int NOT NULL,
  `city` varchar(255) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`user_account_id`),
  CONSTRAINT `fk_staff_profile_users_idx` FOREIGN KEY (`user_account_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;