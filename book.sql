/*
SQLyog Ultimate v12.08 (64 bit)
MySQL - 8.0.15 : Database - bookshop
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE `bookshop` ;

USE `bookshop`;

/*Table structure for table `book` */

DROP TABLE IF EXISTS `book`;

CREATE TABLE `book` (
  `book_id` int(11) NOT NULL AUTO_INCREMENT,
  `book_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `book_author` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `book_price` double NOT NULL,
  `book_image` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `book_description` varchar(200) DEFAULT NULL,
  `book_sales` int(11) DEFAULT '0',
  `book_category` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `book_press` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`book_id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8;

/*Data for the table `book` */

insert  into `book`(`book_id`,`book_name`,`book_author`,`book_price`,`book_image`,`book_description`,`book_sales`,`book_category`,`book_press`) values (3,'小王子','圣埃克苏佩里',49,'http://47.100.121.231:8080//imgupload/小王子.jpg','书中以一位飞行员作为故事叙述者，讲述了小王子从自己星球出发前往地球的过程中，所经历的各种历险。',56,'文学',NULL),(4,'平凡的世界','路遥',74.5,'http://47.100.121.231:8080//imgupload/平凡的世界.jpg','这部小说以恢宏的气势和史诗般的品格，全景式地表现了改革时代中国城乡的社会生活和人们思想情感的巨大变迁',342,'文学',NULL),(5,'朝花夕拾','鲁迅',70.5,'http://47.100.121.231:8080//imgupload/朝花夕拾.jpg','《白夜行》将无望却坚守的凄凉爱情和执着而缜密的冷静推理完 美结合，被众多书迷视作东野圭吾作品中的无冕之王。',234,'文学',NULL),(6,'朗读者','董卿',46,'http://47.100.121.231:8080//imgupload/朗读者.jpg','改变生活习惯，提升幸福指数，开启精彩人生让你优雅从容，拥有快乐如意的生活。',432,'文学',NULL),(7,'默读','Priest',70.5,'http://47.100.121.231:8080//imgupload/默读.jpg','作者构思极其巧妙，单元案件互相串联，组成牵连三代人的惊天大案；探讨深刻的人性与社会问题',432,'文学',NULL),(8,'万水千山','三毛',87,'http://47.100.121.231:8080//imgupload/万水千山.jpg','三毛游历中南美洲后写就的一部游记，她带领读者游历野外。',345,'文学',NULL),(9,'倚天屠龙记','金庸',45,'http://47.100.121.231:8080//imgupload/倚天屠龙记.jpg','张翠山和殷素素为爱情及气节自尽，张无忌与赵敏及周芷若纠缠不清的情债',212,'武侠',NULL),(10,'笑傲江湖','金庸',66.8,'http://47.100.121.231:8080//imgupload/笑傲江湖.jpg','处处渗透着追求个性解放与人格独立的精神，对人性的刻画殊为深刻。',234,'武侠',NULL),(11,'天龙八部','金庸',85,'http://47.100.121.231:8080//imgupload/天龙八部.jpg','北宋年间，外族纷纷觊觎大宋国土，形成汉、胡对立的局面。',3245,'武侠',NULL),(12,'昆仑','凤歌',84,'http://47.100.121.231:8080//imgupload/昆仑.jpg','人物形形色色，情节高潮迭起，有平江秋月之美，也有风雷激荡之烈',321,'武侠',NULL),(13,'资治通鉴','司马光',298,'http://47.100.121.231:8080//imgupload/资治通鉴.jpg','资治通鉴正版全套全集原著无删减白话版历史类书籍畅销故事书中国古代史中华书局儿童柏杨胡三省资质通鉴青少年史记',21,'历史',NULL),(14,'中国抗日战争全史','李继峰',58,'http://47.100.121.231:8080//imgupload/中国抗日战争全史.jpg','历史书籍 中国史 抗战纪实 中国通史 南京大屠杀 拉贝日记 正版历史类书籍',2321,'历史',NULL),(15,'左传','无',29,'http://47.100.121.231:8080//imgupload/左传.jpg','春秋战国古代史文言文全本诠注全译左转原版书成人版 关于历史类畅销书排行榜 非杨伯峻 中华书局',31,'历史',NULL),(16,'二十四史','编委会编',689,'http://47.100.121.231:8080//imgupload/二十四史.jpg','精选精译精编版 全套装共12册 中华国学书局 史记 三国志 中国通史 历史类书籍',312,'历史',NULL),(17,'清朝大历史','孟森',29.8,'http://47.100.121.231:8080//imgupload/清朝大历史.jpg','清朝书籍 中国近代史书籍 历史书籍 中国通史 历史类书籍 明史讲义 新华先锋',3123,'历史',NULL),(18,'中华上下五千年','李金龙',29.8,'http://47.100.121.231:8080//imgupload/中华上下五千年.jpg','李金龙著历史书籍中国古代史中国上下五千年史记资治通鉴通史5000年历史故事单本册历史类书籍',4234,'历史',NULL),(19,'史记','司马迁',560,'http://47.100.121.231:8080//imgupload/史记.jpg','中国历史书籍青少年读史记故事中华上下五千年中国通史全套历史书籍畅销书',442,'历史',NULL),(20,'宇宙病毒','饶中华',35,'http://47.100.121.231:8080//imgupload/宇宙病毒.jpg','全新正版宇宙病毒/中国科幻精品屋书籍类关于有关方面的地和与跟学习了解知识',12,'科幻',NULL),(21,'给孩子的科幻','刘慈欣',52,'http://47.100.121.231:8080//imgupload/给孩子的科幻.jpg','给孩子的科幻 刘慈欣、韩松选编 著 刘慈欣,韩松 编 低幼启蒙 少儿 中信出版社 全新正书籍类关于有关方面的地和与跟学习了解知识',432,'科幻',NULL),(22,'海底恐龙','饶中华',35,'http://47.100.121.231:8080//imgupload/海底恐龙.jpg','全新正版海底恐龙/中国科幻精品屋书籍类关于有关方面的地和与跟学习了解知识',546,'科幻',NULL);

/*Table structure for table `cart` */

DROP TABLE IF EXISTS `cart`;

CREATE TABLE `cart` (
  `cart_id` int(11) NOT NULL AUTO_INCREMENT,
  `cart_user_id` int(11) NOT NULL,
  PRIMARY KEY (`cart_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

/*Data for the table `cart` */

/*Table structure for table `cartitem` */

DROP TABLE IF EXISTS `cartitem`;

CREATE TABLE `cartitem` (
  `cartitem_cart_id` int(11) NOT NULL,
  `cartitem_book_id` int(11) NOT NULL,
  `cartitem_id` int(11) NOT NULL AUTO_INCREMENT,
  `cartitem_book_number` int(11) DEFAULT NULL,
  PRIMARY KEY (`cartitem_id`),
  KEY `cartitem_cart_id` (`cartitem_cart_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `cartitem` */

/*Table structure for table `orderitem` */

DROP TABLE IF EXISTS `orderitem`;

CREATE TABLE `orderitem` (
  `orderitem_id` int(11) NOT NULL AUTO_INCREMENT,
  `orderitem_order_id` varchar(16) DEFAULT NULL,
  `orderitem_book_id` int(11) DEFAULT NULL,
  `orderitem_book_number` int(11) DEFAULT NULL,
  PRIMARY KEY (`orderitem_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Data for the table `orderitem` */

/*Table structure for table `orders` */

DROP TABLE IF EXISTS `orders`;

CREATE TABLE `orders` (
  `order_id` varchar(16) NOT NULL,
  `order_user_id` int(11) NOT NULL,
  `order_phone` varchar(11) NOT NULL,
  `order_name` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `order_address` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `order_type` varchar(20) NOT NULL DEFAULT 'noreceive',
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `orders` */

/*Table structure for table `shopuser` */

DROP TABLE IF EXISTS `shopuser`;

CREATE TABLE `shopuser` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `user_password` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `user_createTime` datetime NOT NULL,
  `user_type` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'user',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

/*Data for the table `shopuser` */

insert  into `shopuser`(`user_id`,`user_name`,`user_password`,`user_createTime`,`user_type`) values (2,'admin','admin','2019-06-09 21:33:42','manager');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
