-- MySQL dump 10.13  Distrib 8.0.21, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: ampify
-- ------------------------------------------------------
-- Server version	8.0.21

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `albums`
--

DROP TABLE IF EXISTS `albums`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `albums` (
  `ALBUM_ID` varchar(15) NOT NULL,
  `ARTIST_ID` varchar(15) DEFAULT NULL,
  `Album_Name` varchar(30) NOT NULL,
  PRIMARY KEY (`ALBUM_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `albums`
--

LOCK TABLES `albums` WRITE;
/*!40000 ALTER TABLE `albums` DISABLE KEYS */;
INSERT INTO `albums` VALUES ('Alb#001','A#001','Narrated for You'),('Alb#002','A#002','No.6 Collaborations Project'),('Alb#003','A#017','Blinding Lights'),('Alb#004','A#015','Dance Monkey'),('Alb#005','A#003','Memories'),('Alb#006','A#003','Red Pill Blues'),('Alb#007','A#008','Youngblood'),('Alb#008','A#014','News of the World'),('Alb#009','A#016','Hollywoods Bleeding'),('Alb#011','A#004','Lovely'),('Alb#012','A#013','Pray for the Wicked'),('Alb#013','A#009','Just Lose it'),('Alb#014','A#012','Shawn Mendes Original'),('Alb#015','A#011','Happier'),('Alb#016','A#007','7EP'),('Alb#017','A#005','World War Joy'),('Alb#018','A#006','Fine Line'),('Alb#019','A#002','x'),('Alb#021','A#005','Memories...Do not Open.'),('Alb#022','A#004','No Time to Die');
/*!40000 ALTER TABLE `albums` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `artists`
--

DROP TABLE IF EXISTS `artists`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `artists` (
  `ARTIST_ID` varchar(15) NOT NULL,
  `Artist_Name` varchar(30) NOT NULL,
  PRIMARY KEY (`ARTIST_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `artists`
--

LOCK TABLES `artists` WRITE;
/*!40000 ALTER TABLE `artists` DISABLE KEYS */;
INSERT INTO `artists` VALUES ('A#001','Alec Benjamin'),('A#002','Ed Sheeran'),('A#003','Maroon 5'),('A#004','Billie Ellish'),('A#005','The Chainsmokers'),('A#006','Harry Styles'),('A#007','Lil Nas X'),('A#008','5 Seconds of Summer'),('A#009','Eminem'),('A#011','Marshmello'),('A#012','Shawn Mendes'),('A#013','Panic! At the Disco'),('A#014','Queen'),('A#015','Tones and I'),('A#016','Post Malone'),('A#017','The Weeknd');
/*!40000 ALTER TABLE `artists` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `playlists`
--

DROP TABLE IF EXISTS `playlists`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `playlists` (
  `Indexing` int NOT NULL,
  `PLAYLIST_ID` varchar(15) DEFAULT NULL,
  `Playlist_Name` varchar(50) DEFAULT NULL,
  `Owner` varchar(30) DEFAULT NULL,
  `Songs_incl` varchar(250) DEFAULT NULL,
  `Viewer` varchar(30) DEFAULT NULL,
  `Visibility` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`Indexing`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `playlists`
--

LOCK TABLES `playlists` WRITE;
/*!40000 ALTER TABLE `playlists` DISABLE KEYS */;
INSERT INTO `playlists` VALUES (1,'P#001','First Vibes','U#001','S#009-S#004-S#027-S#029-','U#001',1),(2,'P#002','Ed\'s Voice','U#001','S#029-S#027-S#018-','U#001',0),(3,'P#003','My Vibe','U#002','S#011-S#019-S#013-S#016-','U#002',1);
/*!40000 ALTER TABLE `playlists` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `songs`
--

DROP TABLE IF EXISTS `songs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `songs` (
  `SONG_ID` varchar(15) NOT NULL,
  `ARTIST_ID` varchar(15) NOT NULL,
  `ALBUM_ID` varchar(15) DEFAULT NULL,
  `Name` varchar(80) DEFAULT NULL,
  `Location` varchar(260) DEFAULT NULL,
  `Genre` varchar(30) DEFAULT NULL,
  `Language` varchar(30) DEFAULT NULL,
  `Total_Views` int DEFAULT '0',
  `Upload_time` varchar(20) DEFAULT NULL,
  `Lyric` varchar(260) DEFAULT NULL,
  PRIMARY KEY (`SONG_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `songs`
--

LOCK TABLES `songs` WRITE;
/*!40000 ALTER TABLE `songs` DISABLE KEYS */;
INSERT INTO `songs` VALUES ('S#001','A#002','Alb#002','I don\'t Care (feat. Justin Bieber)',NULL,'Pop','English',0,'2019-05-10',NULL),('S#002','A#016','Alb#009','Sunflower (feat. Swae Lee)',NULL,'Pop','English',2,'2018-12-13',NULL),('S#003','A#017','Alb#003','Blinding Lights',NULL,'R_&_B','English',0,'2019-11-28',NULL),('S#004','A#004','Alb#011','lovely (feat. Khalid)',NULL,'Indie','English',6,'2018-04-19',NULL),('S#005','A#016','Alb#009','Circles',NULL,'Pop','English',2,'2019-08-30',NULL),('S#006','A#014','Alb#008','We are the Champions',NULL,'Rock','English',0,'1977-10-07',NULL),('S#007','A#015','Alb#004','Dance Monkey',NULL,'Electro_Pop','English',14,'2019-05-10',NULL),('S#008','A#013','Alb#012','High Hopes',NULL,'Electro_Pop','English',0,'2018-05-23',NULL),('S#009','A#009','Alb#013','Lose Yourself (8 Mile version)',NULL,'Hip_Hop','English',1,'2015-08-06',NULL),('S#011','A#012','Alb#014','If I cant Have You',NULL,'Pop','English',0,'2018-12-06',NULL),('S#012','A#011','Alb#015','Happier',NULL,'Electronic','English',0,'2018-08-17',NULL),('S#013','A#008','Alb#007','Youngblood',NULL,'Rock','English',0,'2018-08-09',NULL),('S#014','A#001','Alb#001','Let Me Down Slowly',NULL,'Pop','English',0,'2018-11-16',NULL),('S#015','A#007','Alb#016','Old Town Road (feat. Billy Ray Cyrus)',NULL,'Hip_Hop','English',0,'2019-04-05',NULL),('S#016','A#005','Alb#017','Takeaway',NULL,'Electronic','English',0,'2019-07-24',NULL),('S#017','A#006','Alb#018','Adore You',NULL,'Pop','English',0,'2019-12-06',NULL),('S#018','A#002','Alb#019','Photograph',NULL,'Pop','English',0,'2014-06-16',NULL),('S#019','A#005','Alb#021','Something Just Like This',NULL,'Electronic','English',0,'2017-02-22',NULL),('S#021','A#003','Alb#006','Girls Like You (feat. Cardi B)',NULL,'R_&_B','English',0,'2018-05-03',NULL),('S#022','A#004','Alb#022','No Time to Die',NULL,'Indie','English',0,'2020-02-13',NULL),('S#023','A#001','Alb#001','If we have Each Other',NULL,'Pop','English',0,'2018-11-06',NULL),('S#024','A#001','Alb#001','The Water Fountain',NULL,'Pop','English',0,'2018-11-25',NULL),('S#025','A#017','Alb#003','Scared to Live',NULL,'R_&_B','English',0,'2020-03-08',NULL),('S#026','A#015','Alb#004','Bad Child',NULL,'Electro_Pop','English',0,'2019-05-17',NULL),('S#027','A#002','Alb#002','Best Part of Me (feat. YEBBA)',NULL,'Pop','English',0,'2019-07-05',NULL),('S#028','A#002','Alb#002','BLOW (feat. Chris Stapelton & Bruno Mars)',NULL,'Rock','English',0,'2019-07-04',NULL),('S#029','A#002','Alb#002','Antisocial (feat. Travis Scott)',NULL,'Pop','English',0,'2019-07-12',NULL),('S#031','A#003','Alb#005','Memories',NULL,'Pop','English',0,'2019-09-20',NULL),('S#032','A#008','Alb#007','Old Me',NULL,'Pop','English',4,'2019-02-21',NULL),('S#033','A#014','Alb#008','We will Rock You',NULL,'Rock','English',11,'1977-09-17',NULL);
/*!40000 ALTER TABLE `songs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_groups`
--

DROP TABLE IF EXISTS `user_groups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_groups` (
  `GROUP_ID` varchar(15) NOT NULL,
  `Grp_Name` varchar(50) NOT NULL,
  `Users_incl` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`GROUP_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_groups`
--

LOCK TABLES `user_groups` WRITE;
/*!40000 ALTER TABLE `user_groups` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_groups` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_interact`
--

DROP TABLE IF EXISTS `user_interact`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_interact` (
  `USERID` varchar(30) NOT NULL,
  `History` varchar(400) DEFAULT NULL,
  `Liked` varchar(200) DEFAULT NULL,
  `Disliked` varchar(200) DEFAULT NULL,
  `Playlists` varchar(200) DEFAULT NULL,
  `Pref_lang` varchar(40) DEFAULT NULL,
  `Pref_genre` varchar(60) DEFAULT NULL,
  `Pref_artist` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`USERID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_interact`
--

LOCK TABLES `user_interact` WRITE;
/*!40000 ALTER TABLE `user_interact` DISABLE KEYS */;
INSERT INTO `user_interact` VALUES ('U#001','[01:26]S#014-[19:24]S#001-[11:02]S#002-[13:21]S#011-','S#001-S#027-S#007-',NULL,NULL,'English-','Hip_Hop-Rock-','A#001-'),('U#002',NULL,'S#015-S#033-',NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `user_interact` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `USERID` varchar(30) NOT NULL,
  `Uname` varchar(30) NOT NULL,
  `Password` varchar(60) NOT NULL,
  PRIMARY KEY (`USERID`),
  UNIQUE KEY `Uname` (`Uname`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES ('U#001','Admin','password'),('U#002','Spirit','joy');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-10-30 23:11:52
