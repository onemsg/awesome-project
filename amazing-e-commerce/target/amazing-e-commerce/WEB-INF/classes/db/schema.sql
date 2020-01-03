create database amazing;

create table `users`(
	`account` varchar(20) primary key,
  `password` varchar(50) not null,
  `name` varchar(50) null default null,
  `email` varchar(50) null default null,
  `default_address_id` int null default null,
  `isvip` tinyint null default 0
);

  
CREATE TABLE `address` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `account` varchar(20) NOT NULL,
  `city` varchar(20) NOT NULL,
  `street` varchar(50) NOT NULL,
  `name` varchar(20) NOT NULL,
  `phone` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
)

CREATE TABLE `orders` (
  `id` varchar(20) not null,
  `account` VARCHAR(20) not NULL,
  `item_id` varchar(20) null,
  `count` SMALLINT NULL DEFAULT NULL,
  `date` DATETIME NULL DEFAULT NULL,
  `address_id` int(11) DEFAULT NULL,
  `ispayed` tinyint null default 0
	check (`ispayed`=0 or `ispayed`=1),
   PRIMARY KEY (`id`, `account`)
);

create table `items`(
	`id` varchar(20) primary key,
    `name` varchar(50) null,
    `price` float null check( `price`>0),
    `stock` int null check(`stock` >0),		/*库存数量*/
    `info` text null,	/* 可存json数据 */
    `type_id` tinyint null
);

create table `item_imgs`(
	`id` int primary key AUTO_INCREMENT,
    `name` varchar(20) null,
    `url` varchar(50) null,
    `item_id` varchar(20) null
);

create table `item_types`(
	`id` tinyint primary key,
    `name` varchar(10) null,
    `info` text null
)



