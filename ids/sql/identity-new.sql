/*M!999999\- enable the sandbox mode */ 
-- MariaDB dump 10.19-11.7.2-MariaDB, for osx10.20 (arm64)
--
-- Host: localhost    Database: identity-local
-- ------------------------------------------------------
-- Server version	11.7.2-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*M!100616 SET @OLD_NOTE_VERBOSITY=@@NOTE_VERBOSITY, NOTE_VERBOSITY=0 */;

--
-- Table structure for table `ApiResourceClaims`
--

DROP TABLE IF EXISTS `ApiResourceClaims`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `ApiResourceClaims` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Type` varchar(200) NOT NULL,
  `ApiResourceId` int(11) NOT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `IX_ApiResourceClaims_ApiResourceId_Type` (`ApiResourceId`,`Type`),
  CONSTRAINT `FK_ApiResourceClaims_ApiResources_ApiResourceId` FOREIGN KEY (`ApiResourceId`) REFERENCES `ApiResources` (`Id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ApiResourceClaims`
--

LOCK TABLES `ApiResourceClaims` WRITE;
/*!40000 ALTER TABLE `ApiResourceClaims` DISABLE KEYS */;
/*!40000 ALTER TABLE `ApiResourceClaims` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ApiResourceProperties`
--

DROP TABLE IF EXISTS `ApiResourceProperties`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `ApiResourceProperties` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Key` varchar(250) NOT NULL,
  `Value` varchar(2000) NOT NULL,
  `ApiResourceId` int(11) NOT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `IX_ApiResourceProperties_ApiResourceId_Key` (`ApiResourceId`,`Key`),
  CONSTRAINT `FK_ApiResourceProperties_ApiResources_ApiResourceId` FOREIGN KEY (`ApiResourceId`) REFERENCES `ApiResources` (`Id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ApiResourceProperties`
--

LOCK TABLES `ApiResourceProperties` WRITE;
/*!40000 ALTER TABLE `ApiResourceProperties` DISABLE KEYS */;
/*!40000 ALTER TABLE `ApiResourceProperties` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ApiResourceScopes`
--

DROP TABLE IF EXISTS `ApiResourceScopes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `ApiResourceScopes` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Scope` varchar(200) NOT NULL,
  `ApiResourceId` int(11) NOT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `IX_ApiResourceScopes_ApiResourceId_Scope` (`ApiResourceId`,`Scope`),
  CONSTRAINT `FK_ApiResourceScopes_ApiResources_ApiResourceId` FOREIGN KEY (`ApiResourceId`) REFERENCES `ApiResources` (`Id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ApiResourceScopes`
--

LOCK TABLES `ApiResourceScopes` WRITE;
/*!40000 ALTER TABLE `ApiResourceScopes` DISABLE KEYS */;
INSERT INTO `ApiResourceScopes` VALUES
(1,'admin_api',1),
(2,'admin_api_readonly',1),
(3,'admin_ui_public',1),
(4,'admin_ui_webhooks',2);
/*!40000 ALTER TABLE `ApiResourceScopes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ApiResourceSecrets`
--

DROP TABLE IF EXISTS `ApiResourceSecrets`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `ApiResourceSecrets` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Description` varchar(1000) DEFAULT NULL,
  `Value` varchar(4000) CHARACTER SET utf8mb4 COLLATE utf8mb4_uca1400_ai_ci NOT NULL,
  `Expiration` date DEFAULT NULL,
  `Type` varchar(250) NOT NULL,
  `Created` date NOT NULL,
  `ApiResourceId` int(11) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `IX_ApiResourceSecrets_ApiResourceId` (`ApiResourceId`),
  CONSTRAINT `FK_ApiResourceSecrets_ApiResources_ApiResourceId` FOREIGN KEY (`ApiResourceId`) REFERENCES `ApiResources` (`Id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ApiResourceSecrets`
--

LOCK TABLES `ApiResourceSecrets` WRITE;
/*!40000 ALTER TABLE `ApiResourceSecrets` DISABLE KEYS */;
/*!40000 ALTER TABLE `ApiResourceSecrets` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ApiResources`
--

DROP TABLE IF EXISTS `ApiResources`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `ApiResources` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Description` varchar(1000) DEFAULT NULL,
  `DisplayName` varchar(200) DEFAULT NULL,
  `Enabled` bit(1) NOT NULL,
  `Name` varchar(200) NOT NULL,
  `Created` datetime(6) NOT NULL DEFAULT '0001-01-01 00:00:00.000000',
  `LastAccessed` datetime(6) DEFAULT NULL,
  `NonEditable` tinyint(1) NOT NULL DEFAULT 0,
  `Updated` datetime(6) DEFAULT NULL,
  `AllowedAccessTokenSigningAlgorithms` varchar(100) DEFAULT NULL,
  `ShowInDiscoveryDocument` bit(1) NOT NULL,
  `RequireResourceIndicator` tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `IX_ApiResources_Name` (`Name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ApiResources`
--

LOCK TABLES `ApiResources` WRITE;
/*!40000 ALTER TABLE `ApiResources` DISABLE KEYS */;
INSERT INTO `ApiResources` VALUES
(1,'API backing the Admin UI Product','Admin API',0x01,'admin_api','2025-07-17 16:42:20.270299',NULL,1,NULL,'',0x01,0),
(2,'Access to the Webhooks for admin UI','Admin UI Webhooks',0x01,'admin_ui_webhooks','2025-07-17 16:42:20.287608',NULL,0,NULL,'',0x01,0);
/*!40000 ALTER TABLE `ApiResources` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ApiScopeClaims`
--

DROP TABLE IF EXISTS `ApiScopeClaims`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `ApiScopeClaims` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `ScopeId` int(11) DEFAULT NULL,
  `Type` varchar(200) NOT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `IX_ApiScopeClaims_ScopeId_Type` (`ScopeId`,`Type`),
  CONSTRAINT `FK_ApiScopeClaims_ApiScopes_ScopeId` FOREIGN KEY (`ScopeId`) REFERENCES `ApiScopes` (`Id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ApiScopeClaims`
--

LOCK TABLES `ApiScopeClaims` WRITE;
/*!40000 ALTER TABLE `ApiScopeClaims` DISABLE KEYS */;
INSERT INTO `ApiScopeClaims` VALUES
(2,1,'name'),
(1,1,'role'),
(4,2,'name'),
(3,2,'role');
/*!40000 ALTER TABLE `ApiScopeClaims` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ApiScopeProperties`
--

DROP TABLE IF EXISTS `ApiScopeProperties`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `ApiScopeProperties` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Key` varchar(250) NOT NULL,
  `Value` varchar(2000) NOT NULL,
  `ScopeId` int(11) NOT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `IX_ApiScopeProperties_ScopeId_Key` (`ScopeId`,`Key`),
  CONSTRAINT `FK_ApiScopeProperties_ApiScopes_ScopeId` FOREIGN KEY (`ScopeId`) REFERENCES `ApiScopes` (`Id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ApiScopeProperties`
--

LOCK TABLES `ApiScopeProperties` WRITE;
/*!40000 ALTER TABLE `ApiScopeProperties` DISABLE KEYS */;
/*!40000 ALTER TABLE `ApiScopeProperties` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ApiScopes`
--

DROP TABLE IF EXISTS `ApiScopes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `ApiScopes` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Description` varchar(1000) DEFAULT NULL,
  `DisplayName` varchar(200) DEFAULT NULL,
  `Emphasize` bit(1) NOT NULL,
  `Name` varchar(200) NOT NULL,
  `Required` bit(1) NOT NULL,
  `ShowInDiscoveryDocument` bit(1) NOT NULL,
  `Enabled` bit(1) NOT NULL,
  `Created` datetime(6) NOT NULL DEFAULT '0001-01-01 00:00:00.000000',
  `LastAccessed` datetime(6) DEFAULT NULL,
  `NonEditable` tinyint(1) NOT NULL DEFAULT 0,
  `Updated` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `IX_ApiScopes_Name` (`Name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ApiScopes`
--

LOCK TABLES `ApiScopes` WRITE;
/*!40000 ALTER TABLE `ApiScopes` DISABLE KEYS */;
INSERT INTO `ApiScopes` VALUES
(1,'Full access to API backing the Admin UI Product','Admin API',0x00,'admin_api',0x01,0x00,0x01,'2025-07-17 16:42:20.258733',NULL,0,NULL),
(2,'Read only access to API backing the Admin UI Product','Admin API Read Only',0x00,'admin_api_readonly',0x01,0x00,0x01,'2025-07-17 16:42:20.269945',NULL,0,NULL),
(3,'Access to the Webhooks for admin UI','Admin UI Webhooks',0x00,'admin_ui_webhooks',0x00,0x00,0x01,'2025-07-17 16:42:20.287499',NULL,0,NULL);
/*!40000 ALTER TABLE `ApiScopes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `AspNetClaimTypes`
--

DROP TABLE IF EXISTS `AspNetClaimTypes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `AspNetClaimTypes` (
  `Id` varchar(127) NOT NULL,
  `ConcurrencyStamp` longtext DEFAULT NULL,
  `Description` longtext DEFAULT NULL,
  `Name` varchar(256) NOT NULL,
  `NormalizedName` varchar(256) DEFAULT NULL,
  `Required` bit(1) NOT NULL,
  `Reserved` bit(1) NOT NULL,
  `Rule` longtext DEFAULT NULL,
  `RuleValidationFailureDescription` longtext DEFAULT NULL,
  `UserEditable` bit(1) NOT NULL DEFAULT b'0',
  `ValueType` int(11) NOT NULL,
  `DisplayName` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_uca1400_ai_ci DEFAULT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `AK_AspNetClaimTypes_Name` (`Name`),
  UNIQUE KEY `ClaimTypeNameIndex` (`NormalizedName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `AspNetClaimTypes`
--

LOCK TABLES `AspNetClaimTypes` WRITE;
/*!40000 ALTER TABLE `AspNetClaimTypes` DISABLE KEYS */;
INSERT INTO `AspNetClaimTypes` VALUES
('0ea372e4-79e3-47d9-add9-5c557c52ab44',NULL,NULL,'profile','PROFILE',0x00,0x01,NULL,NULL,0x00,0,'Profile'),
('0ea46bbf-7ec2-47f0-a8f1-6a0d83f34f82',NULL,NULL,'name','NAME',0x00,0x01,NULL,NULL,0x00,0,'Name'),
('225c21ce-f599-4d3b-a8ad-533a7690bcc2',NULL,NULL,'gender','GENDER',0x00,0x01,NULL,NULL,0x00,0,'Gender'),
('23bd0994-3cf6-4310-9eab-fa4244286dfa',NULL,NULL,'nickname','NICKNAME',0x00,0x01,NULL,NULL,0x00,0,'Nickname'),
('24a7cdb5-e2f2-44dc-b1f2-8c78042659d6',NULL,NULL,'locale','LOCALE',0x00,0x01,NULL,NULL,0x00,0,'Locale'),
('289b9a02-1ecf-49be-8a78-9a95ed6aedb8',NULL,NULL,'middle_name','MIDDLE_NAME',0x00,0x01,NULL,NULL,0x00,0,'Middle Name'),
('34a8f44a-4992-467b-a3ae-a694cb02d3e0',NULL,NULL,'website','WEBSITE',0x00,0x01,NULL,NULL,0x00,0,'Website'),
('44745605-c3f4-46cb-a595-8360a539f2c8',NULL,NULL,'role','ROLE',0x00,0x01,NULL,NULL,0x00,0,'Role'),
('6abac86f-efbc-45fa-90ec-adb5048873dc',NULL,NULL,'phone_number','PHONE_NUMBER',0x00,0x01,NULL,NULL,0x00,0,'Phone Number'),
('721fca91-5603-4956-9e99-d71cccd68366',NULL,NULL,'email_verified','EMAIL_VERIFIED',0x00,0x01,NULL,NULL,0x00,3,'Email Verified'),
('754cf99d-2276-4cb7-a094-223ce77cc5f7',NULL,NULL,'picture','PICTURE',0x00,0x01,NULL,NULL,0x00,0,'Picture'),
('7633e74b-c682-405f-8185-84f56228fbf2',NULL,NULL,'birthdate','BIRTHDATE',0x00,0x01,NULL,NULL,0x00,2,'Birthdate'),
('9d6518e2-e9f8-44df-b778-37271379eaa8',NULL,NULL,'sub','SUB',0x00,0x01,NULL,NULL,0x00,0,'Subject'),
('b7f2ab16-ac51-401b-8f52-6932ffc1dc5b',NULL,NULL,'given_name','GIVEN_NAME',0x00,0x01,NULL,NULL,0x00,0,'Given Name'),
('d76d8d48-c532-40b2-bbed-f6e90712ca2e',NULL,NULL,'family_name','FAMILY_NAME',0x00,0x01,NULL,NULL,0x00,0,'Family Name'),
('e88f3afd-5bf0-4b88-886b-f2a313d8c53d',NULL,NULL,'zoneinfo','ZONEINFO',0x00,0x01,NULL,NULL,0x00,0,'Zone Info'),
('ed196372-3b03-4878-9665-fade4ec70ab6',NULL,NULL,'phone_number_verified','PHONE_NUMBER_VERIFIED',0x00,0x01,NULL,NULL,0x00,3,'Phone Number Verified'),
('eec5a2b3-3d9c-408a-8c0b-2553733684b2',NULL,NULL,'preferred_username','PREFERRED_USERNAME',0x00,0x01,NULL,NULL,0x00,0,'Preferred Username'),
('f2212384-c95f-403c-b501-97474b9150ea',NULL,NULL,'email','EMAIL',0x00,0x01,NULL,NULL,0x00,0,'Email');
/*!40000 ALTER TABLE `AspNetClaimTypes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `AspNetRoleClaims`
--

DROP TABLE IF EXISTS `AspNetRoleClaims`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `AspNetRoleClaims` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `ClaimType` longtext DEFAULT NULL,
  `ClaimValue` longtext DEFAULT NULL,
  `RoleId` varchar(127) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `IX_AspNetRoleClaims_RoleId` (`RoleId`),
  CONSTRAINT `FK_AspNetRoleClaims_AspNetRoles_RoleId` FOREIGN KEY (`RoleId`) REFERENCES `AspNetRoles` (`Id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `AspNetRoleClaims`
--

LOCK TABLES `AspNetRoleClaims` WRITE;
/*!40000 ALTER TABLE `AspNetRoleClaims` DISABLE KEYS */;
/*!40000 ALTER TABLE `AspNetRoleClaims` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `AspNetRoles`
--

DROP TABLE IF EXISTS `AspNetRoles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `AspNetRoles` (
  `Id` varchar(127) NOT NULL,
  `ConcurrencyStamp` longtext DEFAULT NULL,
  `Description` longtext DEFAULT NULL,
  `Name` varchar(256) DEFAULT NULL,
  `NormalizedName` varchar(256) DEFAULT NULL,
  `Reserved` bit(1) NOT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `RoleNameIndex` (`NormalizedName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `AspNetRoles`
--

LOCK TABLES `AspNetRoles` WRITE;
/*!40000 ALTER TABLE `AspNetRoles` DISABLE KEYS */;
INSERT INTO `AspNetRoles` VALUES
('38845e5d-1bca-42a3-a9d9-6fcdaaa460e6','1bada5d2-09ac-4e1f-812c-e811a26ce4b6','Administrator for AdminUI','AdminUI Administrator','ADMINUI ADMINISTRATOR',0x01);
/*!40000 ALTER TABLE `AspNetRoles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `AspNetUserClaims`
--

DROP TABLE IF EXISTS `AspNetUserClaims`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `AspNetUserClaims` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `ClaimType` varchar(256) NOT NULL,
  `ClaimValue` longtext DEFAULT NULL,
  `UserId` varchar(127) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `IX_AspNetUserClaims_ClaimType` (`ClaimType`),
  KEY `IX_AspNetUserClaims_UserId` (`UserId`),
  CONSTRAINT `FK_AspNetUserClaims_AspNetClaimTypes_ClaimType` FOREIGN KEY (`ClaimType`) REFERENCES `AspNetClaimTypes` (`Name`) ON DELETE CASCADE,
  CONSTRAINT `FK_AspNetUserClaims_AspNetUsers_UserId` FOREIGN KEY (`UserId`) REFERENCES `AspNetUsers` (`Id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `AspNetUserClaims`
--

LOCK TABLES `AspNetUserClaims` WRITE;
/*!40000 ALTER TABLE `AspNetUserClaims` DISABLE KEYS */;
/*!40000 ALTER TABLE `AspNetUserClaims` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `AspNetUserLogins`
--

DROP TABLE IF EXISTS `AspNetUserLogins`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `AspNetUserLogins` (
  `LoginProvider` varchar(127) NOT NULL,
  `ProviderKey` varchar(127) NOT NULL,
  `ProviderDisplayName` longtext DEFAULT NULL,
  `UserId` varchar(127) NOT NULL,
  PRIMARY KEY (`LoginProvider`,`ProviderKey`),
  KEY `IX_AspNetUserLogins_UserId` (`UserId`),
  CONSTRAINT `FK_AspNetUserLogins_AspNetUsers_UserId` FOREIGN KEY (`UserId`) REFERENCES `AspNetUsers` (`Id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `AspNetUserLogins`
--

LOCK TABLES `AspNetUserLogins` WRITE;
/*!40000 ALTER TABLE `AspNetUserLogins` DISABLE KEYS */;
/*!40000 ALTER TABLE `AspNetUserLogins` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `AspNetUserRoles`
--

DROP TABLE IF EXISTS `AspNetUserRoles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `AspNetUserRoles` (
  `UserId` varchar(127) NOT NULL,
  `RoleId` varchar(127) NOT NULL,
  PRIMARY KEY (`UserId`,`RoleId`),
  KEY `IX_AspNetUserRoles_RoleId` (`RoleId`),
  CONSTRAINT `FK_AspNetUserRoles_AspNetRoles_RoleId` FOREIGN KEY (`RoleId`) REFERENCES `AspNetRoles` (`Id`) ON DELETE CASCADE,
  CONSTRAINT `FK_AspNetUserRoles_AspNetUsers_UserId` FOREIGN KEY (`UserId`) REFERENCES `AspNetUsers` (`Id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `AspNetUserRoles`
--

LOCK TABLES `AspNetUserRoles` WRITE;
/*!40000 ALTER TABLE `AspNetUserRoles` DISABLE KEYS */;
INSERT INTO `AspNetUserRoles` VALUES
('1e9525af-fd26-4109-b842-66bbb9895fba','38845e5d-1bca-42a3-a9d9-6fcdaaa460e6');
/*!40000 ALTER TABLE `AspNetUserRoles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `AspNetUserTokens`
--

DROP TABLE IF EXISTS `AspNetUserTokens`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `AspNetUserTokens` (
  `UserId` varchar(127) NOT NULL,
  `LoginProvider` varchar(127) NOT NULL,
  `Name` varchar(127) NOT NULL,
  `Value` longtext DEFAULT NULL,
  PRIMARY KEY (`UserId`,`LoginProvider`,`Name`),
  CONSTRAINT `FK_AspNetUserTokens_AspNetUsers_UserId` FOREIGN KEY (`UserId`) REFERENCES `AspNetUsers` (`Id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `AspNetUserTokens`
--

LOCK TABLES `AspNetUserTokens` WRITE;
/*!40000 ALTER TABLE `AspNetUserTokens` DISABLE KEYS */;
/*!40000 ALTER TABLE `AspNetUserTokens` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `AspNetUsers`
--

DROP TABLE IF EXISTS `AspNetUsers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `AspNetUsers` (
  `Id` varchar(127) NOT NULL,
  `AccessFailedCount` int(11) NOT NULL,
  `ConcurrencyStamp` longtext DEFAULT NULL,
  `Email` varchar(256) DEFAULT NULL,
  `EmailConfirmed` bit(1) NOT NULL,
  `FirstName` longtext DEFAULT NULL,
  `IsBlocked` bit(1) NOT NULL,
  `IsDeleted` bit(1) NOT NULL,
  `LastName` longtext DEFAULT NULL,
  `LockoutEnabled` bit(1) NOT NULL,
  `LockoutEnd` datetime(6) DEFAULT NULL,
  `NormalizedEmail` varchar(256) DEFAULT NULL,
  `NormalizedUserName` varchar(256) DEFAULT NULL,
  `PasswordHash` longtext DEFAULT NULL,
  `PhoneNumber` longtext DEFAULT NULL,
  `PhoneNumberConfirmed` bit(1) NOT NULL,
  `SecurityStamp` longtext DEFAULT NULL,
  `TwoFactorEnabled` bit(1) NOT NULL,
  `UserName` varchar(256) DEFAULT NULL,
  `NormalizedFirstName` varchar(256) DEFAULT NULL,
  `NormalizedLastName` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `UserNameIndex` (`NormalizedUserName`),
  KEY `EmailIndex` (`NormalizedEmail`),
  KEY `FirstNameIndex` (`NormalizedFirstName`),
  KEY `LastNameIndex` (`NormalizedLastName`),
  KEY `CountIndex` (`IsBlocked`,`IsDeleted`),
  KEY `CountIndexReversed` (`IsDeleted`,`IsBlocked`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `AspNetUsers`
--

LOCK TABLES `AspNetUsers` WRITE;
/*!40000 ALTER TABLE `AspNetUsers` DISABLE KEYS */;
INSERT INTO `AspNetUsers` VALUES
('1e9525af-fd26-4109-b842-66bbb9895fba',0,NULL,'info@rocksolidknowledge.com',0x01,'Bootstrap',0x00,0x00,'User',0x01,NULL,'INFO@ROCKSOLIDKNOWLEDGE.COM','INFO@ROCKSOLIDKNOWLEDGE.COM','AQAAAAIAAYagAAAAEDsyr3YdVnJuE8r0b8A5A6KkPv5U2K+bZXdyGAwHzw4JLIpQOhyRIU1sL01zmUanEA==',NULL,0x00,'DHAND2UWGPJHGZ2KUW7TDAMJEBWNOLKE',0x00,'info@rocksolidknowledge.com','BOOTSTRAP','USER');
/*!40000 ALTER TABLE `AspNetUsers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `AuditEntries`
--

DROP TABLE IF EXISTS `AuditEntries`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `AuditEntries` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `When` datetime(6) NOT NULL,
  `Source` longtext DEFAULT NULL,
  `SubjectType` longtext DEFAULT NULL,
  `SubjectIdentifier` longtext DEFAULT NULL,
  `Subject` longtext DEFAULT NULL,
  `Action` longtext DEFAULT NULL,
  `ResourceType` longtext DEFAULT NULL,
  `Resource` longtext DEFAULT NULL,
  `ResourceIdentifier` longtext DEFAULT NULL,
  `Succeeded` tinyint(1) NOT NULL,
  `Description` longtext DEFAULT NULL,
  `NormalisedSubject` longtext DEFAULT NULL,
  `NormalisedAction` longtext DEFAULT NULL,
  `NormalisedResource` longtext DEFAULT NULL,
  `NormalisedSource` longtext DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `IX_AuditEntries_When` (`When`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `AuditEntries`
--

LOCK TABLES `AuditEntries` WRITE;
/*!40000 ALTER TABLE `AuditEntries` DISABLE KEYS */;
/*!40000 ALTER TABLE `AuditEntries` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ClientClaims`
--

DROP TABLE IF EXISTS `ClientClaims`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `ClientClaims` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `ClientId` int(11) NOT NULL,
  `Type` varchar(250) NOT NULL,
  `Value` varchar(250) NOT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `IX_ClientClaims_ClientId_Type_Value` (`ClientId`,`Type`,`Value`),
  CONSTRAINT `FK_ClientClaims_Clients_ClientId` FOREIGN KEY (`ClientId`) REFERENCES `Clients` (`Id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ClientClaims`
--

LOCK TABLES `ClientClaims` WRITE;
/*!40000 ALTER TABLE `ClientClaims` DISABLE KEYS */;
/*!40000 ALTER TABLE `ClientClaims` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ClientCorsOrigins`
--

DROP TABLE IF EXISTS `ClientCorsOrigins`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `ClientCorsOrigins` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `ClientId` int(11) NOT NULL,
  `Origin` varchar(150) NOT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `IX_ClientCorsOrigins_ClientId_Origin` (`ClientId`,`Origin`),
  CONSTRAINT `FK_ClientCorsOrigins_Clients_ClientId` FOREIGN KEY (`ClientId`) REFERENCES `Clients` (`Id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ClientCorsOrigins`
--

LOCK TABLES `ClientCorsOrigins` WRITE;
/*!40000 ALTER TABLE `ClientCorsOrigins` DISABLE KEYS */;
/*!40000 ALTER TABLE `ClientCorsOrigins` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ClientGrantTypes`
--

DROP TABLE IF EXISTS `ClientGrantTypes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `ClientGrantTypes` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `ClientId` int(11) NOT NULL,
  `GrantType` varchar(250) NOT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `IX_ClientGrantTypes_ClientId_GrantType` (`ClientId`,`GrantType`),
  CONSTRAINT `FK_ClientGrantTypes_Clients_ClientId` FOREIGN KEY (`ClientId`) REFERENCES `Clients` (`Id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ClientGrantTypes`
--

LOCK TABLES `ClientGrantTypes` WRITE;
/*!40000 ALTER TABLE `ClientGrantTypes` DISABLE KEYS */;
INSERT INTO `ClientGrantTypes` VALUES
(1,1,'authorization_code');
/*!40000 ALTER TABLE `ClientGrantTypes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ClientIdPRestrictions`
--

DROP TABLE IF EXISTS `ClientIdPRestrictions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `ClientIdPRestrictions` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `ClientId` int(11) NOT NULL,
  `Provider` varchar(200) NOT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `IX_ClientIdPRestrictions_ClientId_Provider` (`ClientId`,`Provider`),
  CONSTRAINT `FK_ClientIdPRestrictions_Clients_ClientId` FOREIGN KEY (`ClientId`) REFERENCES `Clients` (`Id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ClientIdPRestrictions`
--

LOCK TABLES `ClientIdPRestrictions` WRITE;
/*!40000 ALTER TABLE `ClientIdPRestrictions` DISABLE KEYS */;
/*!40000 ALTER TABLE `ClientIdPRestrictions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ClientPostLogoutRedirectUris`
--

DROP TABLE IF EXISTS `ClientPostLogoutRedirectUris`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `ClientPostLogoutRedirectUris` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `ClientId` int(11) NOT NULL,
  `PostLogoutRedirectUri` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_uca1400_ai_ci NOT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `IX_ClientPostLogoutRedirectUris_ClientId_PostLogoutRedirectUri` (`ClientId`,`PostLogoutRedirectUri`),
  CONSTRAINT `FK_ClientPostLogoutRedirectUris_Clients_ClientId` FOREIGN KEY (`ClientId`) REFERENCES `Clients` (`Id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ClientPostLogoutRedirectUris`
--

LOCK TABLES `ClientPostLogoutRedirectUris` WRITE;
/*!40000 ALTER TABLE `ClientPostLogoutRedirectUris` DISABLE KEYS */;
INSERT INTO `ClientPostLogoutRedirectUris` VALUES
(1,1,'https://adminui.knowledgespike.local');
/*!40000 ALTER TABLE `ClientPostLogoutRedirectUris` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ClientProperties`
--

DROP TABLE IF EXISTS `ClientProperties`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `ClientProperties` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `ClientId` int(11) NOT NULL,
  `Key` varchar(250) NOT NULL,
  `Value` varchar(2000) NOT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `IX_ClientProperties_ClientId_Key` (`ClientId`,`Key`),
  CONSTRAINT `FK_ClientProperties_Clients_ClientId` FOREIGN KEY (`ClientId`) REFERENCES `Clients` (`Id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ClientProperties`
--

LOCK TABLES `ClientProperties` WRITE;
/*!40000 ALTER TABLE `ClientProperties` DISABLE KEYS */;
/*!40000 ALTER TABLE `ClientProperties` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ClientRedirectUris`
--

DROP TABLE IF EXISTS `ClientRedirectUris`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `ClientRedirectUris` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `ClientId` int(11) NOT NULL,
  `RedirectUri` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_uca1400_ai_ci NOT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `IX_ClientRedirectUris_ClientId_RedirectUri` (`ClientId`,`RedirectUri`),
  CONSTRAINT `FK_ClientRedirectUris_Clients_ClientId` FOREIGN KEY (`ClientId`) REFERENCES `Clients` (`Id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ClientRedirectUris`
--

LOCK TABLES `ClientRedirectUris` WRITE;
/*!40000 ALTER TABLE `ClientRedirectUris` DISABLE KEYS */;
INSERT INTO `ClientRedirectUris` VALUES
(1,1,'https://adminui.knowledgespike.local/signin-oidc');
/*!40000 ALTER TABLE `ClientRedirectUris` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ClientScopes`
--

DROP TABLE IF EXISTS `ClientScopes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `ClientScopes` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `ClientId` int(11) NOT NULL,
  `Scope` varchar(200) NOT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `IX_ClientScopes_ClientId_Scope` (`ClientId`,`Scope`),
  CONSTRAINT `FK_ClientScopes_Clients_ClientId` FOREIGN KEY (`ClientId`) REFERENCES `Clients` (`Id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ClientScopes`
--

LOCK TABLES `ClientScopes` WRITE;
/*!40000 ALTER TABLE `ClientScopes` DISABLE KEYS */;
INSERT INTO `ClientScopes` VALUES
(2,1,'admin_api'),
(3,1,'admin_ui_profile'),
(1,1,'openid');
/*!40000 ALTER TABLE `ClientScopes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ClientSecrets`
--

DROP TABLE IF EXISTS `ClientSecrets`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `ClientSecrets` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `ClientId` int(11) NOT NULL,
  `Description` varchar(2000) DEFAULT NULL,
  `Expiration` datetime(6) DEFAULT NULL,
  `Type` varchar(250) NOT NULL,
  `Value` varchar(4000) CHARACTER SET utf8mb4 COLLATE utf8mb4_uca1400_ai_ci NOT NULL,
  `Created` datetime(6) NOT NULL DEFAULT '0001-01-01 00:00:00.000000',
  PRIMARY KEY (`Id`),
  KEY `IX_ClientSecrets_ClientId` (`ClientId`),
  CONSTRAINT `FK_ClientSecrets_Clients_ClientId` FOREIGN KEY (`ClientId`) REFERENCES `Clients` (`Id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ClientSecrets`
--

LOCK TABLES `ClientSecrets` WRITE;
/*!40000 ALTER TABLE `ClientSecrets` DISABLE KEYS */;
INSERT INTO `ClientSecrets` VALUES
(1,1,NULL,NULL,'SharedSecret','FOWZHtghkS3FQW/dl8rsqBv4+DqQh2K3IJtR22ELWkexzwv0bmySsH5DGkL5lfpVh4YyBac7WJ70/nl6pk4luw==','2025-07-17 16:42:20.328756');
/*!40000 ALTER TABLE `ClientSecrets` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Clients`
--

DROP TABLE IF EXISTS `Clients`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `Clients` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `AbsoluteRefreshTokenLifetime` int(11) NOT NULL,
  `AccessTokenLifetime` int(11) NOT NULL,
  `AccessTokenType` int(11) NOT NULL,
  `AllowAccessTokensViaBrowser` bit(1) NOT NULL,
  `AllowOfflineAccess` bit(1) NOT NULL,
  `AllowPlainTextPkce` bit(1) NOT NULL,
  `AllowRememberConsent` bit(1) NOT NULL,
  `AlwaysIncludeUserClaimsInIdToken` bit(1) NOT NULL,
  `AlwaysSendClientClaims` bit(1) NOT NULL,
  `AuthorizationCodeLifetime` int(11) NOT NULL,
  `BackChannelLogoutSessionRequired` bit(1) NOT NULL,
  `BackChannelLogoutUri` varchar(2000) DEFAULT NULL,
  `ClientClaimsPrefix` varchar(200) DEFAULT NULL,
  `ClientId` varchar(200) NOT NULL,
  `ClientName` varchar(200) DEFAULT NULL,
  `ClientUri` varchar(2000) DEFAULT NULL,
  `ConsentLifetime` int(11) DEFAULT NULL,
  `Description` varchar(1000) DEFAULT NULL,
  `EnableLocalLogin` bit(1) NOT NULL,
  `Enabled` bit(1) NOT NULL,
  `FrontChannelLogoutSessionRequired` bit(1) NOT NULL,
  `FrontChannelLogoutUri` varchar(2000) DEFAULT NULL,
  `IdentityTokenLifetime` int(11) NOT NULL,
  `IncludeJwtId` bit(1) NOT NULL,
  `LogoUri` varchar(2000) DEFAULT NULL,
  `PairWiseSubjectSalt` varchar(200) DEFAULT NULL,
  `ProtocolType` varchar(200) NOT NULL,
  `RefreshTokenExpiration` int(11) NOT NULL,
  `RefreshTokenUsage` int(11) NOT NULL,
  `RequireClientSecret` bit(1) NOT NULL,
  `RequireConsent` bit(1) NOT NULL,
  `RequirePkce` bit(1) NOT NULL,
  `SlidingRefreshTokenLifetime` int(11) NOT NULL,
  `UpdateAccessTokenClaimsOnRefresh` bit(1) NOT NULL,
  `Created` datetime(6) NOT NULL DEFAULT '0001-01-01 00:00:00.000000',
  `DeviceCodeLifetime` int(11) NOT NULL DEFAULT 0,
  `LastAccessed` datetime(6) DEFAULT NULL,
  `NonEditable` tinyint(1) NOT NULL DEFAULT 0,
  `Updated` datetime(6) DEFAULT NULL,
  `UserCodeType` varchar(100) DEFAULT NULL,
  `UserSsoLifetime` int(11) DEFAULT NULL,
  `AllowedIdentityTokenSigningAlgorithms` varchar(100) DEFAULT NULL,
  `RequireRequestObject` bit(1) NOT NULL,
  `CibaLifetime` int(11) DEFAULT NULL,
  `PollingInterval` int(11) DEFAULT NULL,
  `CoordinateLifetimeWithUserSession` tinyint(1) DEFAULT NULL,
  `DPoPClockSkew` time(6) NOT NULL DEFAULT '00:00:00.000000',
  `DPoPValidationMode` int(11) NOT NULL DEFAULT 0,
  `InitiateLoginUri` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_uca1400_ai_ci DEFAULT NULL,
  `RequireDPoP` tinyint(1) NOT NULL DEFAULT 0,
  `PushedAuthorizationLifetime` int(11) DEFAULT NULL,
  `RequirePushedAuthorization` tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `IX_Clients_ClientId` (`ClientId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Clients`
--

LOCK TABLES `Clients` WRITE;
/*!40000 ALTER TABLE `Clients` DISABLE KEYS */;
INSERT INTO `Clients` VALUES
(1,86400,3600,0,0x00,0x01,0x00,0x01,0x00,0x00,300,0x01,NULL,NULL,'admin_ui','Admin UI',NULL,NULL,'IdentityExpress Admin UI',0x01,0x01,0x01,NULL,300,0x01,NULL,NULL,'oidc',1,0,0x01,0x00,0x01,1296000,0x00,'2025-07-17 16:42:20.328166',300,'0001-01-01 00:00:00.000000',1,'0001-01-01 00:00:00.000000',NULL,NULL,'',0x00,NULL,NULL,NULL,'00:05:00.000000',1,NULL,0,NULL,0);
/*!40000 ALTER TABLE `Clients` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ConfigurationEntries`
--

DROP TABLE IF EXISTS `ConfigurationEntries`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `ConfigurationEntries` (
  `Key` varchar(95) NOT NULL,
  `Value` longtext DEFAULT NULL,
  PRIMARY KEY (`Key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ConfigurationEntries`
--

LOCK TABLES `ConfigurationEntries` WRITE;
/*!40000 ALTER TABLE `ConfigurationEntries` DISABLE KEYS */;
INSERT INTO `ConfigurationEntries` VALUES
('policy','{\"PolicyClaims\":[],\"Version\":\"07/17/2025 16:42:19\",\"ResourceType\":\"accessPolicy\",\"ResourceIdentifier\":\"07/17/2025 16:42:19\"}'),
('webhooks','{\"ClientId\":null,\"ClientSecret\":null,\"Webhooks\":{\"MfaReset\":{\"Url\":\"\",\"ScopeName\":\"\",\"Enabled\":false},\"PasswordReset\":{\"Url\":\"\",\"ScopeName\":\"\",\"Enabled\":false},\"UserRegistration\":{\"Url\":\"\",\"ScopeName\":\"\",\"Enabled\":false},\"ServerSideSessionDelete\":{\"Url\":\"\",\"ScopeName\":\"\",\"Enabled\":false}},\"ResourceType\":\"webhookConfiguration\",\"ResourceIdentifier\":null}');
/*!40000 ALTER TABLE `ConfigurationEntries` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `DataProtectionKeys`
--

DROP TABLE IF EXISTS `DataProtectionKeys`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `DataProtectionKeys` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `FriendlyName` longtext DEFAULT NULL,
  `Xml` longtext DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `DataProtectionKeys`
--

LOCK TABLES `DataProtectionKeys` WRITE;
/*!40000 ALTER TABLE `DataProtectionKeys` DISABLE KEYS */;
/*!40000 ALTER TABLE `DataProtectionKeys` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `DeviceCodes`
--

DROP TABLE IF EXISTS `DeviceCodes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `DeviceCodes` (
  `DeviceCode` varchar(200) NOT NULL,
  `UserCode` varchar(200) NOT NULL,
  `SubjectId` varchar(200) DEFAULT NULL,
  `ClientId` varchar(200) NOT NULL,
  `CreationTime` datetime(6) NOT NULL,
  `Expiration` datetime(6) NOT NULL,
  `Data` longtext NOT NULL,
  `SessionId` varchar(100) DEFAULT NULL,
  `Description` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`UserCode`),
  UNIQUE KEY `IX_DeviceCodes_DeviceCode` (`DeviceCode`),
  UNIQUE KEY `IX_DeviceCodes_UserCode` (`UserCode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `DeviceCodes`
--

LOCK TABLES `DeviceCodes` WRITE;
/*!40000 ALTER TABLE `DeviceCodes` DISABLE KEYS */;
/*!40000 ALTER TABLE `DeviceCodes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `EnumClaimTypeAllowedValues`
--

DROP TABLE IF EXISTS `EnumClaimTypeAllowedValues`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `EnumClaimTypeAllowedValues` (
  `ClaimTypeId` varchar(95) NOT NULL,
  `Value` varchar(95) NOT NULL,
  PRIMARY KEY (`ClaimTypeId`,`Value`),
  CONSTRAINT `FK_EnumClaimTypeAllowedValues_AspNetClaimTypes_ClaimTypeId` FOREIGN KEY (`ClaimTypeId`) REFERENCES `AspNetClaimTypes` (`Id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `EnumClaimTypeAllowedValues`
--

LOCK TABLES `EnumClaimTypeAllowedValues` WRITE;
/*!40000 ALTER TABLE `EnumClaimTypeAllowedValues` DISABLE KEYS */;
/*!40000 ALTER TABLE `EnumClaimTypeAllowedValues` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ExtendedApiResources`
--

DROP TABLE IF EXISTS `ExtendedApiResources`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `ExtendedApiResources` (
  `Id` varchar(127) NOT NULL,
  `ApiResourceName` varchar(200) NOT NULL,
  `NormalizedName` varchar(200) NOT NULL,
  `Reserved` bit(1) NOT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `ApiNameIndex` (`ApiResourceName`),
  UNIQUE KEY `ApiResourceNameIndex` (`NormalizedName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ExtendedApiResources`
--

LOCK TABLES `ExtendedApiResources` WRITE;
/*!40000 ALTER TABLE `ExtendedApiResources` DISABLE KEYS */;
INSERT INTO `ExtendedApiResources` VALUES
('0d586464-05eb-4069-b65e-6b8fb3d3e83a','admin_ui_webhooks','ADMIN_UI_WEBHOOKS',0x00),
('6f6edf4f-bee1-4d98-b25f-ed9f08a822b7','admin_api','ADMIN_API',0x01);
/*!40000 ALTER TABLE `ExtendedApiResources` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ExtendedClients`
--

DROP TABLE IF EXISTS `ExtendedClients`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `ExtendedClients` (
  `Id` varchar(127) NOT NULL,
  `ClientId` varchar(200) NOT NULL,
  `Description` longtext DEFAULT NULL,
  `NormalizedClientId` varchar(200) NOT NULL,
  `NormalizedClientName` varchar(200) DEFAULT NULL,
  `Reserved` bit(1) NOT NULL,
  `ClientType` int(11) DEFAULT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `IdIndex` (`ClientId`),
  UNIQUE KEY `ClientIdIndex` (`NormalizedClientId`),
  UNIQUE KEY `ClientNameIndex` (`NormalizedClientName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ExtendedClients`
--

LOCK TABLES `ExtendedClients` WRITE;
/*!40000 ALTER TABLE `ExtendedClients` DISABLE KEYS */;
INSERT INTO `ExtendedClients` VALUES
('3c2ec748-5276-4a38-8e78-e82e8e575af8','admin_ui','IdentityExpress Admin UI','ADMIN_UI','ADMIN UI',0x01,0);
/*!40000 ALTER TABLE `ExtendedClients` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ExtendedIdentityResources`
--

DROP TABLE IF EXISTS `ExtendedIdentityResources`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `ExtendedIdentityResources` (
  `Id` varchar(127) NOT NULL,
  `IdentityResourceName` varchar(200) NOT NULL,
  `NormalizedName` varchar(200) NOT NULL,
  `Reserved` bit(1) NOT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `IdentityNameIndex` (`IdentityResourceName`),
  UNIQUE KEY `IdentityResourceNameIndex` (`NormalizedName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ExtendedIdentityResources`
--

LOCK TABLES `ExtendedIdentityResources` WRITE;
/*!40000 ALTER TABLE `ExtendedIdentityResources` DISABLE KEYS */;
INSERT INTO `ExtendedIdentityResources` VALUES
('0298b85f-3688-4497-b2c3-90d8787a2598','profile','PROFILE',0x01),
('921fe639-4997-45a8-bef4-623d872cc3b5','admin_ui_profile','ADMIN_UI_PROFILE',0x00),
('b465d7d3-8bab-45d6-ab68-a9636d5d0383','openid','OPENID',0x00);
/*!40000 ALTER TABLE `ExtendedIdentityResources` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `IdentityProviders`
--

DROP TABLE IF EXISTS `IdentityProviders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `IdentityProviders` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Scheme` varchar(200) NOT NULL,
  `DisplayName` varchar(200) DEFAULT NULL,
  `Enabled` tinyint(1) NOT NULL,
  `Type` varchar(20) NOT NULL,
  `Properties` longtext DEFAULT NULL,
  `Created` datetime(6) NOT NULL DEFAULT '0001-01-01 00:00:00.000000',
  `LastAccessed` datetime(6) DEFAULT NULL,
  `NonEditable` tinyint(1) NOT NULL DEFAULT 0,
  `Updated` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `IX_IdentityProviders_Scheme` (`Scheme`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `IdentityProviders`
--

LOCK TABLES `IdentityProviders` WRITE;
/*!40000 ALTER TABLE `IdentityProviders` DISABLE KEYS */;
/*!40000 ALTER TABLE `IdentityProviders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `IdentityResourceClaims`
--

DROP TABLE IF EXISTS `IdentityResourceClaims`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `IdentityResourceClaims` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Type` varchar(200) NOT NULL,
  `IdentityResourceId` int(11) NOT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `IX_IdentityResourceClaims_IdentityResourceId_Type` (`IdentityResourceId`,`Type`),
  CONSTRAINT `FK_IdentityResourceClaims_IdentityResources_IdentityResourceId` FOREIGN KEY (`IdentityResourceId`) REFERENCES `IdentityResources` (`Id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `IdentityResourceClaims`
--

LOCK TABLES `IdentityResourceClaims` WRITE;
/*!40000 ALTER TABLE `IdentityResourceClaims` DISABLE KEYS */;
INSERT INTO `IdentityResourceClaims` VALUES
(1,'sub',1),
(12,'birthdate',2),
(3,'family_name',2),
(11,'gender',2),
(4,'given_name',2),
(14,'locale',2),
(5,'middle_name',2),
(2,'name',2),
(6,'nickname',2),
(9,'picture',2),
(7,'preferred_username',2),
(8,'profile',2),
(15,'updated_at',2),
(10,'website',2),
(13,'zoneinfo',2),
(17,'email',3),
(20,'given_name',3),
(19,'name',3),
(16,'openid',3),
(18,'role',3);
/*!40000 ALTER TABLE `IdentityResourceClaims` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `IdentityResourceProperties`
--

DROP TABLE IF EXISTS `IdentityResourceProperties`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `IdentityResourceProperties` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Key` varchar(250) NOT NULL,
  `Value` varchar(2000) NOT NULL,
  `IdentityResourceId` int(11) NOT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `IX_IdentityResourceProperties_IdentityResourceId_Key` (`IdentityResourceId`,`Key`),
  CONSTRAINT `FK_IdentityResourceProperties_IdentityResources_IdentityResource` FOREIGN KEY (`IdentityResourceId`) REFERENCES `IdentityResources` (`Id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `IdentityResourceProperties`
--

LOCK TABLES `IdentityResourceProperties` WRITE;
/*!40000 ALTER TABLE `IdentityResourceProperties` DISABLE KEYS */;
/*!40000 ALTER TABLE `IdentityResourceProperties` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `IdentityResources`
--

DROP TABLE IF EXISTS `IdentityResources`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `IdentityResources` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Description` varchar(1000) DEFAULT NULL,
  `DisplayName` varchar(200) DEFAULT NULL,
  `Emphasize` bit(1) NOT NULL,
  `Enabled` bit(1) NOT NULL,
  `Name` varchar(200) NOT NULL,
  `Required` bit(1) NOT NULL,
  `ShowInDiscoveryDocument` bit(1) NOT NULL,
  `Created` datetime(6) NOT NULL DEFAULT '0001-01-01 00:00:00.000000',
  `NonEditable` tinyint(1) NOT NULL DEFAULT 0,
  `Updated` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `IX_IdentityResources_Name` (`Name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `IdentityResources`
--

LOCK TABLES `IdentityResources` WRITE;
/*!40000 ALTER TABLE `IdentityResources` DISABLE KEYS */;
INSERT INTO `IdentityResources` VALUES
(1,NULL,'Your user identifier',0x00,0x01,'openid',0x01,0x01,'2025-07-17 16:42:20.219605',1,NULL),
(2,'Your user profile information (first name, last name, etc.)','User profile',0x01,0x01,'profile',0x00,0x01,'2025-07-17 16:42:20.245042',1,NULL),
(3,'Identity data required to access AdminUI','Admin UI Profile',0x00,0x01,'admin_ui_profile',0x01,0x00,'2025-07-17 16:42:20.285503',0,NULL);
/*!40000 ALTER TABLE `IdentityResources` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Keys`
--

DROP TABLE IF EXISTS `Keys`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `Keys` (
  `Id` varchar(95) NOT NULL,
  `Version` int(11) NOT NULL,
  `Created` datetime(6) NOT NULL,
  `Use` varchar(95) DEFAULT NULL,
  `Algorithm` varchar(100) NOT NULL,
  `IsX509Certificate` tinyint(1) NOT NULL,
  `DataProtected` tinyint(1) NOT NULL,
  `Data` longtext NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `IX_Keys_Use` (`Use`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Keys`
--

LOCK TABLES `Keys` WRITE;
/*!40000 ALTER TABLE `Keys` DISABLE KEYS */;
/*!40000 ALTER TABLE `Keys` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PersistedGrants`
--

DROP TABLE IF EXISTS `PersistedGrants`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `PersistedGrants` (
  `Key` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_uca1400_ai_ci DEFAULT NULL,
  `ClientId` varchar(200) NOT NULL,
  `CreationTime` datetime(6) NOT NULL,
  `Data` longtext NOT NULL,
  `Expiration` datetime(6) DEFAULT NULL,
  `SubjectId` varchar(200) DEFAULT NULL,
  `Type` varchar(50) NOT NULL,
  `SessionId` varchar(100) DEFAULT NULL,
  `Description` varchar(200) DEFAULT NULL,
  `ConsumedTime` date DEFAULT NULL,
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `IX_PersistedGrants_Key` (`Key`),
  KEY `IX_PersistedGrants_SubjectId_ClientId_Type` (`SubjectId`,`ClientId`,`Type`),
  KEY `IX_PersistedGrants_SubjectId_SessionId_Type` (`SubjectId`,`SessionId`,`Type`),
  KEY `IX_PersistedGrants_ConsumedTime` (`ConsumedTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PersistedGrants`
--

LOCK TABLES `PersistedGrants` WRITE;
/*!40000 ALTER TABLE `PersistedGrants` DISABLE KEYS */;
/*!40000 ALTER TABLE `PersistedGrants` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PushedAuthorizationRequests`
--

DROP TABLE IF EXISTS `PushedAuthorizationRequests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `PushedAuthorizationRequests` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ReferenceValueHash` varchar(64) NOT NULL,
  `ExpiresAtUtc` datetime(6) NOT NULL,
  `Parameters` longtext NOT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `IX_PushedAuthorizationRequests_ReferenceValueHash` (`ReferenceValueHash`),
  KEY `IX_PushedAuthorizationRequests_ExpiresAtUtc` (`ExpiresAtUtc`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PushedAuthorizationRequests`
--

LOCK TABLES `PushedAuthorizationRequests` WRITE;
/*!40000 ALTER TABLE `PushedAuthorizationRequests` DISABLE KEYS */;
/*!40000 ALTER TABLE `PushedAuthorizationRequests` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `RelyingParties`
--

DROP TABLE IF EXISTS `RelyingParties`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `RelyingParties` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Realm` varchar(200) NOT NULL,
  `TokenType` longtext DEFAULT NULL,
  `SignatureAlgorithm` longtext DEFAULT NULL,
  `DigestAlgorithm` longtext DEFAULT NULL,
  `SamlNameIdentifierFormat` longtext DEFAULT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `IX_RelyingParties_Realm` (`Realm`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `RelyingParties`
--

LOCK TABLES `RelyingParties` WRITE;
/*!40000 ALTER TABLE `RelyingParties` DISABLE KEYS */;
/*!40000 ALTER TABLE `RelyingParties` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `RelyingPartyClaimMappings`
--

DROP TABLE IF EXISTS `RelyingPartyClaimMappings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `RelyingPartyClaimMappings` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `OriginalClaimType` varchar(250) NOT NULL,
  `NewClaimType` varchar(250) NOT NULL,
  `RelyingPartyId` int(11) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `IX_RelyingPartyClaimMappings_RelyingPartyId` (`RelyingPartyId`),
  CONSTRAINT `FK_RelyingPartyClaimMappings_RelyingParties_RelyingPartyId` FOREIGN KEY (`RelyingPartyId`) REFERENCES `RelyingParties` (`Id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `RelyingPartyClaimMappings`
--

LOCK TABLES `RelyingPartyClaimMappings` WRITE;
/*!40000 ALTER TABLE `RelyingPartyClaimMappings` DISABLE KEYS */;
/*!40000 ALTER TABLE `RelyingPartyClaimMappings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SamlArtifacts`
--

DROP TABLE IF EXISTS `SamlArtifacts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `SamlArtifacts` (
  `Key` varchar(200) NOT NULL,
  `EntityId` varchar(200) NOT NULL,
  `MessageType` varchar(50) NOT NULL,
  `Message` longtext NOT NULL,
  `CreationTime` datetime(6) NOT NULL,
  `Expiration` datetime(6) NOT NULL,
  PRIMARY KEY (`Key`),
  KEY `IX_SamlArtifacts_Expiration` (`Expiration`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SamlArtifacts`
--

LOCK TABLES `SamlArtifacts` WRITE;
/*!40000 ALTER TABLE `SamlArtifacts` DISABLE KEYS */;
/*!40000 ALTER TABLE `SamlArtifacts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ServerSideSessions`
--

DROP TABLE IF EXISTS `ServerSideSessions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `ServerSideSessions` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `Key` varchar(100) NOT NULL,
  `Scheme` varchar(100) NOT NULL,
  `SubjectId` varchar(100) NOT NULL,
  `SessionId` varchar(100) DEFAULT NULL,
  `DisplayName` varchar(100) DEFAULT NULL,
  `Created` datetime(6) NOT NULL,
  `Renewed` datetime(6) NOT NULL,
  `Expires` datetime(6) DEFAULT NULL,
  `Data` longtext NOT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `IX_ServerSideSessions_Key` (`Key`),
  KEY `IX_ServerSideSessions_DisplayName` (`DisplayName`),
  KEY `IX_ServerSideSessions_Expires` (`Expires`),
  KEY `IX_ServerSideSessions_SessionId` (`SessionId`),
  KEY `IX_ServerSideSessions_SubjectId` (`SubjectId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ServerSideSessions`
--

LOCK TABLES `ServerSideSessions` WRITE;
/*!40000 ALTER TABLE `ServerSideSessions` DISABLE KEYS */;
/*!40000 ALTER TABLE `ServerSideSessions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ServiceProviderArtifactResolutionServices`
--

DROP TABLE IF EXISTS `ServiceProviderArtifactResolutionServices`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `ServiceProviderArtifactResolutionServices` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Binding` varchar(2000) NOT NULL,
  `Location` varchar(2000) NOT NULL,
  `Index` int(11) NOT NULL,
  `IsDefault` tinyint(1) NOT NULL,
  `ServiceProviderId` int(11) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `IX_ServiceProviderArtifactResolutionServices_ServiceProviderId` (`ServiceProviderId`),
  CONSTRAINT `FK_ServiceProviderArtifactResolutionServices_ServiceProviders_S~` FOREIGN KEY (`ServiceProviderId`) REFERENCES `ServiceProviders` (`Id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ServiceProviderArtifactResolutionServices`
--

LOCK TABLES `ServiceProviderArtifactResolutionServices` WRITE;
/*!40000 ALTER TABLE `ServiceProviderArtifactResolutionServices` DISABLE KEYS */;
/*!40000 ALTER TABLE `ServiceProviderArtifactResolutionServices` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ServiceProviderAssertionConsumerServices`
--

DROP TABLE IF EXISTS `ServiceProviderAssertionConsumerServices`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `ServiceProviderAssertionConsumerServices` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Binding` varchar(2000) NOT NULL,
  `Location` varchar(2000) NOT NULL,
  `Index` int(11) NOT NULL,
  `IsDefault` tinyint(1) NOT NULL,
  `ServiceProviderId` int(11) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `IX_ServiceProviderAssertionConsumerServices_ServiceProviderId` (`ServiceProviderId`),
  CONSTRAINT `FK_ServiceProviderAssertionConsumerServices_ServiceProviders_Se~` FOREIGN KEY (`ServiceProviderId`) REFERENCES `ServiceProviders` (`Id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ServiceProviderAssertionConsumerServices`
--

LOCK TABLES `ServiceProviderAssertionConsumerServices` WRITE;
/*!40000 ALTER TABLE `ServiceProviderAssertionConsumerServices` DISABLE KEYS */;
/*!40000 ALTER TABLE `ServiceProviderAssertionConsumerServices` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ServiceProviderClaimMappings`
--

DROP TABLE IF EXISTS `ServiceProviderClaimMappings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `ServiceProviderClaimMappings` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `OriginalClaimType` varchar(250) NOT NULL,
  `NewClaimType` varchar(250) NOT NULL,
  `ServiceProviderId` int(11) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `IX_ServiceProviderClaimMappings_ServiceProviderId` (`ServiceProviderId`),
  CONSTRAINT `FK_ServiceProviderClaimMappings_ServiceProviders_ServiceProvide~` FOREIGN KEY (`ServiceProviderId`) REFERENCES `ServiceProviders` (`Id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ServiceProviderClaimMappings`
--

LOCK TABLES `ServiceProviderClaimMappings` WRITE;
/*!40000 ALTER TABLE `ServiceProviderClaimMappings` DISABLE KEYS */;
/*!40000 ALTER TABLE `ServiceProviderClaimMappings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ServiceProviderSignCertificates`
--

DROP TABLE IF EXISTS `ServiceProviderSignCertificates`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `ServiceProviderSignCertificates` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Certificate` longblob NOT NULL,
  `ServiceProviderId` int(11) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `IX_ServiceProviderSignCertificates_ServiceProviderId` (`ServiceProviderId`),
  CONSTRAINT `FK_ServiceProviderSignCertificates_ServiceProviders_ServiceProv~` FOREIGN KEY (`ServiceProviderId`) REFERENCES `ServiceProviders` (`Id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ServiceProviderSignCertificates`
--

LOCK TABLES `ServiceProviderSignCertificates` WRITE;
/*!40000 ALTER TABLE `ServiceProviderSignCertificates` DISABLE KEYS */;
/*!40000 ALTER TABLE `ServiceProviderSignCertificates` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ServiceProviderSingleLogoutServices`
--

DROP TABLE IF EXISTS `ServiceProviderSingleLogoutServices`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `ServiceProviderSingleLogoutServices` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Binding` varchar(2000) NOT NULL,
  `Location` varchar(2000) NOT NULL,
  `Index` int(11) NOT NULL,
  `IsDefault` tinyint(1) NOT NULL,
  `ServiceProviderId` int(11) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `IX_ServiceProviderSingleLogoutServices_ServiceProviderId` (`ServiceProviderId`),
  CONSTRAINT `FK_ServiceProviderSingleLogoutServices_ServiceProviders_Service~` FOREIGN KEY (`ServiceProviderId`) REFERENCES `ServiceProviders` (`Id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ServiceProviderSingleLogoutServices`
--

LOCK TABLES `ServiceProviderSingleLogoutServices` WRITE;
/*!40000 ALTER TABLE `ServiceProviderSingleLogoutServices` DISABLE KEYS */;
/*!40000 ALTER TABLE `ServiceProviderSingleLogoutServices` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ServiceProviders`
--

DROP TABLE IF EXISTS `ServiceProviders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `ServiceProviders` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `EntityId` varchar(200) NOT NULL,
  `EncryptionCertificate` longblob DEFAULT NULL,
  `SignAssertions` tinyint(1) NOT NULL,
  `EncryptAssertions` tinyint(1) NOT NULL,
  `RequireSamlMessageDestination` tinyint(1) NOT NULL,
  `AllowIdpInitiatedSso` tinyint(1) NOT NULL DEFAULT 0,
  `RequireAuthenticationRequestsSigned` tinyint(1) DEFAULT NULL,
  `ArtifactDeliveryBindingType` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_uca1400_ai_ci DEFAULT NULL,
  `NameIdentifierFormat` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_uca1400_ai_ci DEFAULT NULL,
  `RequireSignedArtifactResolveRequests` tinyint(1) DEFAULT NULL,
  `RequireSignedArtifactResponses` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `IX_ServiceProviders_EntityId` (`EntityId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ServiceProviders`
--

LOCK TABLES `ServiceProviders` WRITE;
/*!40000 ALTER TABLE `ServiceProviders` DISABLE KEYS */;
/*!40000 ALTER TABLE `ServiceProviders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `__EFMigrationsHistory`
--

DROP TABLE IF EXISTS `__EFMigrationsHistory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `__EFMigrationsHistory` (
  `MigrationId` varchar(150) NOT NULL,
  `ProductVersion` varchar(32) NOT NULL,
  PRIMARY KEY (`MigrationId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `__EFMigrationsHistory`
--

LOCK TABLES `__EFMigrationsHistory` WRITE;
/*!40000 ALTER TABLE `__EFMigrationsHistory` DISABLE KEYS */;
INSERT INTO `__EFMigrationsHistory` VALUES
('20171026082026_InitialMySqlIdentityDbMigration','9.0.6'),
('20171026082423_InitialMySqlOperationalDbMigration','9.0.6'),
('20171026082716_InitialMySqlConfigurationDbMigration','9.0.6'),
('20171026082841_InitialMySqlExtendedConfigurationDbMigration','9.0.6'),
('20171122164343_UserSearchOptimizationMigration','9.0.6'),
('20180725083018_ConfigurationEntries','9.0.6'),
('20181112152920_IdentityServer2.3SMySqlConfigurationDbMigration','9.0.6'),
('20181112153007_IdentityServer2.3MySqlOperationalDbMigration','9.0.6'),
('20181205164929_ExtendedDataMigration2.3','9.0.6'),
('20181220155839_InitalMySqlAuditDbMigration','9.0.6'),
('20190305133042_MySqlSaml2PInitial','9.0.6'),
('20190305133541_MySqlWsfederationInitial','9.0.6'),
('20190321122253_ClientType','9.0.6'),
('20200225105251_Added AllowIdpInitiated','9.0.6'),
('20200626132027_InitialMySqlDataProtectionKeyMigration','9.0.6'),
('20200702075924_V3toV4MySqlConfigurationDbMigration','9.0.6'),
('20200702080404_V3toV4MySqlOperationalDbMigration','9.0.6'),
('20200914074711_RskSamlV3','9.0.6'),
('20210106150621_DuendeMySqlOperationalMigration','9.0.6'),
('20210106162829_DuendeMySqlConfigurationMigration','9.0.6'),
('20210504100649_EnumeratedClaimTypeMigration','9.0.6'),
('20210602105143_IdentityProvidersMySqlConfigurationMigration','9.0.6'),
('20210602110210_PersistedGrantConsumeTimeMySqlOperationalMigration','9.0.6'),
('20220104115922_DuendeV6MySqlConfigurationMigration','9.0.6'),
('20220112114906_ClaimTypeDisplayNameMigration','9.0.6'),
('20220112145727_RskSamlPackageUpdate','9.0.6'),
('20220114164323_MySqlSamlArtifactInitialMigration','9.0.6'),
('20220608092034_Duende61Update','9.0.6'),
('20220608093234_Duende61ConfigurationUpdate','9.0.6'),
('20230620154609_Duende63ConfigurationUpdate','9.0.6'),
('20240105120950_DuendeV7MySqlConfigurationMigration','9.0.6'),
('20240108113305_DuendeV7MySqlOperationalMigration','9.0.6'),
('20250121145920_DuendeV7.1MySqlOperationalMigration','9.0.6');
/*!40000 ALTER TABLE `__EFMigrationsHistory` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*M!100616 SET NOTE_VERBOSITY=@OLD_NOTE_VERBOSITY */;

-- Dump completed on 2025-07-17 17:42:29
