SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

CREATE SCHEMA IF NOT EXISTS `game_server` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `game_server` ;

-- -----------------------------------------------------
-- Table `game_server`.`player`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `game_server`.`player` (
  `id_player` INT NOT NULL AUTO_INCREMENT,
  `nome_player` VARCHAR(45) NOT NULL,
  `senha` VARCHAR(45) NOT NULL DEFAULT '123',
  `life` DOUBLE NOT NULL DEFAULT 3,
  `id_level_atual` INT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id_player`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `game_server`.`game`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `game_server`.`game` (
  `id_game` INT NOT NULL AUTO_INCREMENT,
  `player_id_player` INT NOT NULL,
  PRIMARY KEY (`id_game`),
  INDEX `fk_game_player_idx` (`player_id_player` ASC),
  CONSTRAINT `fk_game_player`
    FOREIGN KEY (`player_id_player`)
    REFERENCES `game_server`.`player` (`id_player`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `game_server`.`trophy`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `game_server`.`trophy` (
  `id_trophy` INT NOT NULL AUTO_INCREMENT,
  `name_trophy` VARCHAR(45) NOT NULL,
  `xp_trophy` DOUBLE NOT NULL,
  `title_trophy` VARCHAR(45) NOT NULL,
  `description_trophy` VARCHAR(140) NOT NULL,
  PRIMARY KEY (`id_trophy`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `game_server`.`player_has_trophy`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `game_server`.`player_has_trophy` (
  `player_id_player` INT NOT NULL,
  `trophy_id_trophy` INT NOT NULL,
  PRIMARY KEY (`player_id_player`, `trophy_id_trophy`),
  INDEX `fk_player_has_trophy_trophy1_idx` (`trophy_id_trophy` ASC),
  INDEX `fk_player_has_trophy_player1_idx` (`player_id_player` ASC),
  CONSTRAINT `fk_player_has_trophy_player1`
    FOREIGN KEY (`player_id_player`)
    REFERENCES `game_server`.`player` (`id_player`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_player_has_trophy_trophy1`
    FOREIGN KEY (`trophy_id_trophy`)
    REFERENCES `game_server`.`trophy` (`id_trophy`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `game_server`.`level`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `game_server`.`level` (
  `id_level` INT NOT NULL,
  `player_id_player` INT NOT NULL,
  `coins` DOUBLE NOT NULL,
  `save_point_x` DOUBLE NOT NULL,
  `save_pont_y` DOUBLE NOT NULL,
  `save_point_id` DOUBLE NOT NULL,
  `life` INT NOT NULL,
  `xp` INT NOT NULL,
  PRIMARY KEY (`id_level`),
  INDEX `fk_level_player1_idx` (`player_id_player` ASC),
  CONSTRAINT `fk_level_player1`
    FOREIGN KEY (`player_id_player`)
    REFERENCES `game_server`.`player` (`id_player`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
