-- MySQL dump 10.13  Distrib 5.7.12, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: lego
-- ------------------------------------------------------
-- Server version	5.7.17-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `demo_human-info`
--

DROP TABLE IF EXISTS `demo_human-info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `demo_human-info` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `name` varchar(32) DEFAULT NULL COMMENT '姓名',
  `password` varchar(64) DEFAULT NULL COMMENT '密码（只做加密展示）',
  `age` int(11) DEFAULT NULL COMMENT '年龄',
  `sex` int(11) DEFAULT NULL COMMENT '性别\n男：1\n女：0',
  `code` varchar(32) DEFAULT NULL COMMENT '身份证号码',
  `address` varchar(256) DEFAULT NULL COMMENT '家庭住址',
  `remark` varchar(256) DEFAULT NULL COMMENT '备注',
  `create_datetime` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COMMENT='示例_人员信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `demo_human-info`
--

LOCK TABLES `demo_human-info` WRITE;
/*!40000 ALTER TABLE `demo_human-info` DISABLE KEYS */;
INSERT INTO `demo_human-info` VALUES (1,'周一',NULL,12,1,NULL,'山东省济南市',NULL,'2017-02-23 08:56:36'),(2,'沈伟沈伟',NULL,21,0,NULL,'中国北京',NULL,'2017-02-23 08:56:36'),(3,'孙婷',NULL,14,0,NULL,'山东省青岛市',NULL,'2017-02-23 08:56:50'),(4,'袁航',NULL,33,0,NULL,'山东省济南市',NULL,'2017-03-03 10:15:24'),(5,'张三',NULL,24,0,'370103198410195010','山东省东营市','济南的身份证，东营的地址。','2017-05-08 19:53:03'),(6,'张三',NULL,24,0,'370103198410195010','山东省东营市','济南的身份证，东营的地址。','2017-05-08 19:53:25'),(7,'张三',NULL,24,0,'370103198410195010',NULL,NULL,'2017-05-08 19:53:54'),(8,'张三',NULL,24,0,'370103198410195010',NULL,'no address','2017-05-08 19:54:10'),(10,'王五','202cb962ac59075b964b07152d234b70',33,1,'370103198410195010','美国',NULL,'2017-05-09 10:17:33');
/*!40000 ALTER TABLE `demo_human-info` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-05-10 16:34:32
