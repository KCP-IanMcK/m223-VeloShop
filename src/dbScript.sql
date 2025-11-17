create database VeloShop;

CREATE TABLE IF NOT EXISTS `VeloShop`.`StorageItems` (
  `itemId` INT NOT NULL AUTO_INCREMENT,
  `type` VARCHAR(45) NOT NULL,
  `amount` INT NULL,
  `price` DOUBLE NULL,
  PRIMARY KEY (`itemId`),
  UNIQUE INDEX `itemId_UNIQUE` (`itemId` ASC) VISIBLE,
  UNIQUE INDEX `type_UNIQUE` (`type` ASC) VISIBLE);