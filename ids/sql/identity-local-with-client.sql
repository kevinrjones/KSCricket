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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ApiResourceClaims`
--

LOCK TABLES `ApiResourceClaims` WRITE;
/*!40000 ALTER TABLE `ApiResourceClaims` DISABLE KEYS */;
INSERT INTO `ApiResourceClaims` VALUES
(3,'role',3);
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
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
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
(4,'admin_ui_webhooks',2),
(8,'acs.api',3),
(9,'acs.api.read',3),
(10,'acs.api.write',3);
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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ApiResources`
--

LOCK TABLES `ApiResources` WRITE;
/*!40000 ALTER TABLE `ApiResources` DISABLE KEYS */;
INSERT INTO `ApiResources` VALUES
(1,'API backing the Admin UI Product','Admin API',0x01,'admin_api','2025-07-17 16:23:37.666383',NULL,1,NULL,'',0x01,0),
(2,'Access to the Webhooks for admin UI','Admin UI Webhooks',0x01,'admin_ui_webhooks','2025-07-17 16:23:37.685729',NULL,0,NULL,'',0x01,0),
(3,'','ACS API Access',0x01,'acs.api','2025-07-17 16:34:58.960558',NULL,0,'2025-07-17 16:35:47.135797','',0x01,0);
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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ApiScopes`
--

LOCK TABLES `ApiScopes` WRITE;
/*!40000 ALTER TABLE `ApiScopes` DISABLE KEYS */;
INSERT INTO `ApiScopes` VALUES
(1,'Full access to API backing the Admin UI Product','Admin API',0x00,'admin_api',0x01,0x00,0x01,'2025-07-17 16:23:37.655470',NULL,0,NULL),
(2,'Read only access to API backing the Admin UI Product','Admin API Read Only',0x00,'admin_api_readonly',0x01,0x00,0x01,'2025-07-17 16:23:37.666026',NULL,0,NULL),
(3,'Access to the Webhooks for admin UI','Admin UI Webhooks',0x00,'admin_ui_webhooks',0x00,0x00,0x01,'2025-07-17 16:23:37.685603',NULL,0,NULL),
(4,'ACS API Access - Full Access','ACS API Access',0x00,'acs.api',0x00,0x01,0x01,'2025-07-17 16:34:58.959608',NULL,0,NULL),
(5,'ACS API Read Scope','ACS API Read Scope',0x00,'acs.api.read',0x00,0x01,0x01,'2025-07-17 16:35:31.042520',NULL,0,NULL),
(6,'ACS API Write Scope','ACS API Write Scope',0x00,'acs.api.write',0x00,0x01,0x01,'2025-07-17 16:35:47.133449',NULL,0,NULL);
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
('0baa605b-a0d0-49e8-8e18-7589d5707e80',NULL,NULL,'email_verified','EMAIL_VERIFIED',0x00,0x01,NULL,NULL,0x00,3,'Email Verified'),
('2bab74e4-e90c-456a-8cdc-3e420d807bd0',NULL,NULL,'gender','GENDER',0x00,0x01,NULL,NULL,0x00,0,'Gender'),
('3db9f30a-bd8f-427b-add1-66dce526fa49',NULL,NULL,'middle_name','MIDDLE_NAME',0x00,0x01,NULL,NULL,0x00,0,'Middle Name'),
('5160bbf2-62e2-4c65-86b1-3a2a473197f0',NULL,NULL,'sub','SUB',0x00,0x01,NULL,NULL,0x00,0,'Subject'),
('5d0453e1-52e1-4adf-8738-b3d3e8e2912a',NULL,NULL,'given_name','GIVEN_NAME',0x00,0x01,NULL,NULL,0x00,0,'Given Name'),
('61fe7541-b9c3-446c-81ec-0a340184f239',NULL,NULL,'email','EMAIL',0x00,0x01,NULL,NULL,0x00,0,'Email'),
('655ef203-3a27-495f-a7f3-ec1a49c28a3b',NULL,NULL,'phone_number_verified','PHONE_NUMBER_VERIFIED',0x00,0x01,NULL,NULL,0x00,3,'Phone Number Verified'),
('66d63387-c6b0-45a4-95cb-2532511dcf1a',NULL,NULL,'nickname','NICKNAME',0x00,0x01,NULL,NULL,0x00,0,'Nickname'),
('6e1a1b2c-5d47-41ab-a8d8-b883fb9e1acb',NULL,NULL,'phone_number','PHONE_NUMBER',0x00,0x01,NULL,NULL,0x00,0,'Phone Number'),
('8627735e-dbde-41ec-89c3-ec63491220be',NULL,NULL,'birthdate','BIRTHDATE',0x00,0x01,NULL,NULL,0x00,2,'Birthdate'),
('868a91d2-6702-4e2b-87c6-8575367033ea',NULL,NULL,'family_name','FAMILY_NAME',0x00,0x01,NULL,NULL,0x00,0,'Family Name'),
('96ee4045-2293-4bb5-b159-769ea9459d9b',NULL,NULL,'locale','LOCALE',0x00,0x01,NULL,NULL,0x00,0,'Locale'),
('9c9f0d61-53a1-429b-8827-d978bd975b6e',NULL,NULL,'profile','PROFILE',0x00,0x01,NULL,NULL,0x00,0,'Profile'),
('b5fab911-cf58-416d-a8aa-16bebdbc6e6a',NULL,NULL,'website','WEBSITE',0x00,0x01,NULL,NULL,0x00,0,'Website'),
('be39c719-2974-449b-b99d-83deea1168d2',NULL,NULL,'role','ROLE',0x00,0x01,NULL,NULL,0x00,0,'Role'),
('c7ccd1f5-4204-47d9-a746-e4d138d99059',NULL,NULL,'name','NAME',0x00,0x01,NULL,NULL,0x00,0,'Name'),
('d219caa1-c977-4215-a7fa-731c126ecfc2',NULL,NULL,'zoneinfo','ZONEINFO',0x00,0x01,NULL,NULL,0x00,0,'Zone Info'),
('ee412330-45a4-493f-a0a9-3c5076b6bc86',NULL,NULL,'picture','PICTURE',0x00,0x01,NULL,NULL,0x00,0,'Picture'),
('fbb074fc-4254-4a45-b035-110f6dd43a62',NULL,NULL,'preferred_username','PREFERRED_USERNAME',0x00,0x01,NULL,NULL,0x00,0,'Preferred Username');
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
('bc293cb4-cedb-436c-a295-d7233d8e487a','d9d1b16d-b7b6-4a8f-8c4f-aad0e1aea1e7','Administrator for AdminUI','AdminUI Administrator','ADMINUI ADMINISTRATOR',0x01);
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
('9e7a274a-487d-406b-9f00-2e462779db40','bc293cb4-cedb-436c-a295-d7233d8e487a');
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
('9e7a274a-487d-406b-9f00-2e462779db40',0,'42442f90-f067-4cca-b6ec-f45c0d17b80b','kevin@knowledgespike.com',0x01,'Kevin',0x00,0x00,'Jones',0x01,NULL,'KEVIN@KNOWLEDGESPIKE.COM','KEVIN@KNOWLEDGESPIKE.COM','AQAAAAIAAYagAAAAEMtCy7e5F2blsku8tDS3qTWOB5WmhV722J0JmkqF6COYzIxwB8I7T6a8roT3UZj7KA==',NULL,0x00,'TTETDS2KCCKRZ7BY37ZXIVMOB4RQF52Y',0x00,'kevin@knowledgespike.com','KEVIN','JONES');
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
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `AuditEntries`
--

LOCK TABLES `AuditEntries` WRITE;
/*!40000 ALTER TABLE `AuditEntries` DISABLE KEYS */;
INSERT INTO `AuditEntries` VALUES
(1,'2025-07-17 16:29:34.942810','AdminUI','User','14833382-84f9-4e8a-89bc-40b2f4628a4e','info@rocksolidknowledge.com','Get ClaimType(s)','ClaimType','*','*',1,'info@rocksolidknowledge.com viewed ClaimType(s)','INFO@ROCKSOLIDKNOWLEDGE.COM','GET CLAIMTYPE(S)','*','ADMINUI'),
(2,'2025-07-17 16:29:34.942781','AdminUI','User','14833382-84f9-4e8a-89bc-40b2f4628a4e','info@rocksolidknowledge.com','Get User(s)','User','*','*',1,'info@rocksolidknowledge.com viewed User(s)','INFO@ROCKSOLIDKNOWLEDGE.COM','GET USER(S)','*','ADMINUI'),
(3,'2025-07-17 16:29:37.148863','AdminUI','User','14833382-84f9-4e8a-89bc-40b2f4628a4e','info@rocksolidknowledge.com','Get Role','Role','*','*',1,'info@rocksolidknowledge.com viewed Role(s)','INFO@ROCKSOLIDKNOWLEDGE.COM','GET ROLE','*','ADMINUI'),
(4,'2025-07-17 16:29:39.248903','AdminUI','User','14833382-84f9-4e8a-89bc-40b2f4628a4e','info@rocksolidknowledge.com','Get ClaimType(s)','ClaimType','*','*',1,'info@rocksolidknowledge.com viewed ClaimType(s)','INFO@ROCKSOLIDKNOWLEDGE.COM','GET CLAIMTYPE(S)','*','ADMINUI'),
(5,'2025-07-17 16:29:40.612386','AdminUI','User','14833382-84f9-4e8a-89bc-40b2f4628a4e','info@rocksolidknowledge.com','Get Clients','Client','*','*',1,'info@rocksolidknowledge.com viewed Client(s)','INFO@ROCKSOLIDKNOWLEDGE.COM','GET CLIENTS','*','ADMINUI'),
(6,'2025-07-17 16:29:42.771136','AdminUI','User','14833382-84f9-4e8a-89bc-40b2f4628a4e','info@rocksolidknowledge.com','Get ClaimType(s)','ClaimType','*','*',1,'info@rocksolidknowledge.com viewed ClaimType(s)','INFO@ROCKSOLIDKNOWLEDGE.COM','GET CLAIMTYPE(S)','*','ADMINUI'),
(7,'2025-07-17 16:29:42.773314','AdminUI','User','14833382-84f9-4e8a-89bc-40b2f4628a4e','info@rocksolidknowledge.com','Get User(s)','User','*','*',1,'info@rocksolidknowledge.com viewed User(s)','INFO@ROCKSOLIDKNOWLEDGE.COM','GET USER(S)','*','ADMINUI'),
(8,'2025-07-17 16:29:58.442992','AdminUI','User','14833382-84f9-4e8a-89bc-40b2f4628a4e','info@rocksolidknowledge.com','Create User','User','kevin@knowledgespike.com','9e7a274a-487d-406b-9f00-2e462779db40',1,'info@rocksolidknowledge.com created User (kevin@knowledgespike.com) (Username: kevin@knowledgespike.com, Email Address: kevin@knowledgespike.com, First Name: Kevin, Last Name: Jones)','INFO@ROCKSOLIDKNOWLEDGE.COM','CREATE USER','KEVIN@KNOWLEDGESPIKE.COM','ADMINUI'),
(9,'2025-07-17 16:29:58.466057','AdminUI','User','14833382-84f9-4e8a-89bc-40b2f4628a4e','info@rocksolidknowledge.com','Get User(s)','User','*','*',1,'info@rocksolidknowledge.com viewed User(s)','INFO@ROCKSOLIDKNOWLEDGE.COM','GET USER(S)','*','ADMINUI'),
(10,'2025-07-17 16:29:58.466015','AdminUI','User','14833382-84f9-4e8a-89bc-40b2f4628a4e','info@rocksolidknowledge.com','Get User(s)','User','*','*',1,'info@rocksolidknowledge.com viewed User(s)','INFO@ROCKSOLIDKNOWLEDGE.COM','GET USER(S)','*','ADMINUI'),
(11,'2025-07-17 16:30:00.813505','AdminUI','User','14833382-84f9-4e8a-89bc-40b2f4628a4e','info@rocksolidknowledge.com','Get User By Subject','User','kevin@knowledgespike.com','9e7a274a-487d-406b-9f00-2e462779db40',1,'info@rocksolidknowledge.com viewed User (kevin@knowledgespike.com)','INFO@ROCKSOLIDKNOWLEDGE.COM','GET USER BY SUBJECT','KEVIN@KNOWLEDGESPIKE.COM','ADMINUI'),
(12,'2025-07-17 16:30:00.850552','AdminUI','User','14833382-84f9-4e8a-89bc-40b2f4628a4e','info@rocksolidknowledge.com','Get ClaimType(s)','ClaimType','*','*',1,'info@rocksolidknowledge.com viewed ClaimType(s)','INFO@ROCKSOLIDKNOWLEDGE.COM','GET CLAIMTYPE(S)','*','ADMINUI'),
(13,'2025-07-17 16:30:03.849135','AdminUI','User','14833382-84f9-4e8a-89bc-40b2f4628a4e','info@rocksolidknowledge.com','Get ClaimType(s)','ClaimType','*','*',1,'info@rocksolidknowledge.com viewed ClaimType(s)','INFO@ROCKSOLIDKNOWLEDGE.COM','GET CLAIMTYPE(S)','*','ADMINUI'),
(14,'2025-07-17 16:30:10.765308','AdminUI','User','14833382-84f9-4e8a-89bc-40b2f4628a4e','info@rocksolidknowledge.com','Update User Claim','User','kevin@knowledgespike.com','9e7a274a-487d-406b-9f00-2e462779db40',1,'info@rocksolidknowledge.com edited claim for User (kevin@knowledgespike.com), from type \'email_verified\' value \'false\' to type \'email_verified\' value \'true\' ','INFO@ROCKSOLIDKNOWLEDGE.COM','UPDATE USER CLAIM','KEVIN@KNOWLEDGESPIKE.COM','ADMINUI'),
(15,'2025-07-17 16:31:45.116079','AdminUI','User','14833382-84f9-4e8a-89bc-40b2f4628a4e','info@rocksolidknowledge.com','Get User(s)','User','*','*',1,'info@rocksolidknowledge.com viewed User(s)','INFO@ROCKSOLIDKNOWLEDGE.COM','GET USER(S)','*','ADMINUI'),
(16,'2025-07-17 16:31:45.117382','AdminUI','User','14833382-84f9-4e8a-89bc-40b2f4628a4e','info@rocksolidknowledge.com','Get ClaimType(s)','ClaimType','*','*',1,'info@rocksolidknowledge.com viewed ClaimType(s)','INFO@ROCKSOLIDKNOWLEDGE.COM','GET CLAIMTYPE(S)','*','ADMINUI'),
(17,'2025-07-17 16:31:46.703468','AdminUI','User','14833382-84f9-4e8a-89bc-40b2f4628a4e','info@rocksolidknowledge.com','Get User By Subject','User','kevin@knowledgespike.com','9e7a274a-487d-406b-9f00-2e462779db40',1,'info@rocksolidknowledge.com viewed User (kevin@knowledgespike.com)','INFO@ROCKSOLIDKNOWLEDGE.COM','GET USER BY SUBJECT','KEVIN@KNOWLEDGESPIKE.COM','ADMINUI'),
(18,'2025-07-17 16:31:46.760443','AdminUI','User','14833382-84f9-4e8a-89bc-40b2f4628a4e','info@rocksolidknowledge.com','Get ClaimType(s)','ClaimType','*','*',1,'info@rocksolidknowledge.com viewed ClaimType(s)','INFO@ROCKSOLIDKNOWLEDGE.COM','GET CLAIMTYPE(S)','*','ADMINUI'),
(19,'2025-07-17 16:31:48.199495','AdminUI','User','14833382-84f9-4e8a-89bc-40b2f4628a4e','info@rocksolidknowledge.com','Get Role','Role','*','*',1,'info@rocksolidknowledge.com viewed Role(s)','INFO@ROCKSOLIDKNOWLEDGE.COM','GET ROLE','*','ADMINUI'),
(20,'2025-07-17 16:31:50.804199','AdminUI','User','14833382-84f9-4e8a-89bc-40b2f4628a4e','info@rocksolidknowledge.com','Assign Role(s) to User','User','kevin@knowledgespike.com','9e7a274a-487d-406b-9f00-2e462779db40',1,'info@rocksolidknowledge.com assigned role(s) (AdminUI Administrator) to User (kevin@knowledgespike.com)','INFO@ROCKSOLIDKNOWLEDGE.COM','ASSIGN ROLE(S) TO USER','KEVIN@KNOWLEDGESPIKE.COM','ADMINUI'),
(21,'2025-07-17 16:32:34.837266','AdminUI','User','9e7a274a-487d-406b-9f00-2e462779db40','kevin@knowledgespike.com','Delete User','User','info@rocksolidknowledge.com','info@rocksolidknowledge.com',1,'kevin@knowledgespike.com deleted User (info@rocksolidknowledge.com)','KEVIN@KNOWLEDGESPIKE.COM','DELETE USER','INFO@ROCKSOLIDKNOWLEDGE.COM','ADMINUI'),
(22,'2025-07-17 16:33:36.938120','AdminUI','User','9e7a274a-487d-406b-9f00-2e462779db40','kevin@knowledgespike.com','Get ClaimType(s)','ClaimType','*','*',1,'kevin@knowledgespike.com viewed ClaimType(s)','KEVIN@KNOWLEDGESPIKE.COM','GET CLAIMTYPE(S)','*','ADMINUI'),
(23,'2025-07-17 16:33:36.963323','AdminUI','User','9e7a274a-487d-406b-9f00-2e462779db40','kevin@knowledgespike.com','Get Identity Resource(s)','IdentityResource','*','*',1,'kevin@knowledgespike.com viewed all Identity Resource(s)','KEVIN@KNOWLEDGESPIKE.COM','GET IDENTITY RESOURCE(S)','*','ADMINUI'),
(24,'2025-07-17 16:34:18.349471','AdminUI','User','9e7a274a-487d-406b-9f00-2e462779db40','kevin@knowledgespike.com','Create Identity Resource','IdentityResource','ACS Identity Profile','d1dd6f61-bd1a-46a5-a8ca-2afd0b0234d5',1,'kevin@knowledgespike.com created Identity Resource (ACS Identity Profile) (Name: acs.identity, Display Name: ACS Identity Profile, AllowedClaims: (email, given_name, name, role))','KEVIN@KNOWLEDGESPIKE.COM','CREATE IDENTITY RESOURCE','ACS IDENTITY PROFILE','ADMINUI'),
(25,'2025-07-17 16:34:18.361773','AdminUI','User','9e7a274a-487d-406b-9f00-2e462779db40','kevin@knowledgespike.com','Get Identity Resource(s)','IdentityResource','*','*',1,'kevin@knowledgespike.com viewed all Identity Resource(s)','KEVIN@KNOWLEDGESPIKE.COM','GET IDENTITY RESOURCE(S)','*','ADMINUI'),
(26,'2025-07-17 16:34:28.470289','AdminUI','User','9e7a274a-487d-406b-9f00-2e462779db40','kevin@knowledgespike.com','Get ClaimType(s)','ClaimType','*','*',1,'kevin@knowledgespike.com viewed ClaimType(s)','KEVIN@KNOWLEDGESPIKE.COM','GET CLAIMTYPE(S)','*','ADMINUI'),
(27,'2025-07-17 16:34:28.529495','AdminUI','User','9e7a274a-487d-406b-9f00-2e462779db40','kevin@knowledgespike.com','Get Protected Resource(s)','ProtectedResource','*','*',1,'kevin@knowledgespike.com viewed all Protected Resource(s)','KEVIN@KNOWLEDGESPIKE.COM','GET PROTECTED RESOURCE(S)','*','ADMINUI'),
(28,'2025-07-17 16:34:59.007983','AdminUI','User','9e7a274a-487d-406b-9f00-2e462779db40','kevin@knowledgespike.com','Create Protected Resource','ProtectedResource','ACS API Access','927e6f58-57d3-4fe2-a6c8-cad7a3f5370a',1,'kevin@knowledgespike.com created Protected Resource (ACS API Access) (Name: acs.api, Display Name: ACS API Access, AllowedClaims: (role))','KEVIN@KNOWLEDGESPIKE.COM','CREATE PROTECTED RESOURCE','ACS API ACCESS','ADMINUI'),
(29,'2025-07-17 16:34:59.024803','AdminUI','User','9e7a274a-487d-406b-9f00-2e462779db40','kevin@knowledgespike.com','Get Protected Resource(s)','ProtectedResource','*','*',1,'kevin@knowledgespike.com viewed all Protected Resource(s)','KEVIN@KNOWLEDGESPIKE.COM','GET PROTECTED RESOURCE(S)','*','ADMINUI'),
(30,'2025-07-17 16:35:00.588890','AdminUI','User','9e7a274a-487d-406b-9f00-2e462779db40','kevin@knowledgespike.com','Get Protected Resource By Id','ProtectedResource','ACS API Access','927e6f58-57d3-4fe2-a6c8-cad7a3f5370a',1,'kevin@knowledgespike.com viewed Protected Resource (ACS API Access)','KEVIN@KNOWLEDGESPIKE.COM','GET PROTECTED RESOURCE BY ID','ACS API ACCESS','ADMINUI'),
(31,'2025-07-17 16:35:00.618195','AdminUI','User','9e7a274a-487d-406b-9f00-2e462779db40','kevin@knowledgespike.com','Get ClaimType(s)','ClaimType','*','*',1,'kevin@knowledgespike.com viewed ClaimType(s)','KEVIN@KNOWLEDGESPIKE.COM','GET CLAIMTYPE(S)','*','ADMINUI'),
(32,'2025-07-17 16:35:02.407340','AdminUI','User','9e7a274a-487d-406b-9f00-2e462779db40','kevin@knowledgespike.com','Get All Scopes','Scope','*','*',1,'kevin@knowledgespike.com viewed all Protected Resource(s)','KEVIN@KNOWLEDGESPIKE.COM','GET ALL SCOPES','*','ADMINUI'),
(33,'2025-07-17 16:35:31.056084','AdminUI','User','9e7a274a-487d-406b-9f00-2e462779db40','kevin@knowledgespike.com','Add Scope to Protected Resource','ProtectedResource','ACS API Access','927e6f58-57d3-4fe2-a6c8-cad7a3f5370a',1,'kevin@knowledgespike.com added Scope to Protected Resource (acs.api) (Name: acs.api.read, Display Name: ACS API Read Scope, Description: ACS API Read Scope, Required: False, Emphasize: False, Show In Discovery Document: True, IsFinalInstanceOnResource: False)','KEVIN@KNOWLEDGESPIKE.COM','ADD SCOPE TO PROTECTED RESOURCE','ACS API ACCESS','ADMINUI'),
(34,'2025-07-17 16:35:31.071457','AdminUI','User','9e7a274a-487d-406b-9f00-2e462779db40','kevin@knowledgespike.com','Get Protected Resource By Id','ProtectedResource','ACS API Access','927e6f58-57d3-4fe2-a6c8-cad7a3f5370a',1,'kevin@knowledgespike.com viewed Protected Resource (ACS API Access)','KEVIN@KNOWLEDGESPIKE.COM','GET PROTECTED RESOURCE BY ID','ACS API ACCESS','ADMINUI'),
(35,'2025-07-17 16:35:31.079278','AdminUI','User','9e7a274a-487d-406b-9f00-2e462779db40','kevin@knowledgespike.com','Get All Scopes','Scope','*','*',1,'kevin@knowledgespike.com viewed all Protected Resource(s)','KEVIN@KNOWLEDGESPIKE.COM','GET ALL SCOPES','*','ADMINUI'),
(36,'2025-07-17 16:35:31.079347','AdminUI','User','9e7a274a-487d-406b-9f00-2e462779db40','kevin@knowledgespike.com','Get All Scopes','Scope','*','*',1,'kevin@knowledgespike.com viewed all Protected Resource(s)','KEVIN@KNOWLEDGESPIKE.COM','GET ALL SCOPES','*','ADMINUI'),
(37,'2025-07-17 16:35:47.140750','AdminUI','User','9e7a274a-487d-406b-9f00-2e462779db40','kevin@knowledgespike.com','Add Scope to Protected Resource','ProtectedResource','ACS API Access','927e6f58-57d3-4fe2-a6c8-cad7a3f5370a',1,'kevin@knowledgespike.com added Scope to Protected Resource (acs.api) (Name: acs.api.write, Display Name: ACS API Write Scope, Description: ACS API Write Scope, Required: False, Emphasize: False, Show In Discovery Document: True, IsFinalInstanceOnResource: False)','KEVIN@KNOWLEDGESPIKE.COM','ADD SCOPE TO PROTECTED RESOURCE','ACS API ACCESS','ADMINUI'),
(38,'2025-07-17 16:35:47.158322','AdminUI','User','9e7a274a-487d-406b-9f00-2e462779db40','kevin@knowledgespike.com','Get Protected Resource By Id','ProtectedResource','ACS API Access','927e6f58-57d3-4fe2-a6c8-cad7a3f5370a',1,'kevin@knowledgespike.com viewed Protected Resource (ACS API Access)','KEVIN@KNOWLEDGESPIKE.COM','GET PROTECTED RESOURCE BY ID','ACS API ACCESS','ADMINUI'),
(39,'2025-07-17 16:35:47.165198','AdminUI','User','9e7a274a-487d-406b-9f00-2e462779db40','kevin@knowledgespike.com','Get All Scopes','Scope','*','*',1,'kevin@knowledgespike.com viewed all Protected Resource(s)','KEVIN@KNOWLEDGESPIKE.COM','GET ALL SCOPES','*','ADMINUI'),
(40,'2025-07-17 16:35:47.165348','AdminUI','User','9e7a274a-487d-406b-9f00-2e462779db40','kevin@knowledgespike.com','Get All Scopes','Scope','*','*',1,'kevin@knowledgespike.com viewed all Protected Resource(s)','KEVIN@KNOWLEDGESPIKE.COM','GET ALL SCOPES','*','ADMINUI'),
(41,'2025-07-17 16:35:50.212618','AdminUI','User','9e7a274a-487d-406b-9f00-2e462779db40','kevin@knowledgespike.com','Get Clients','Client','*','*',1,'kevin@knowledgespike.com viewed Client(s)','KEVIN@KNOWLEDGESPIKE.COM','GET CLIENTS','*','ADMINUI'),
(42,'2025-07-17 16:38:55.897207','AdminUI','User','9e7a274a-487d-406b-9f00-2e462779db40','kevin@knowledgespike.com','Get Identity Resource(s)','IdentityResource','*','*',1,'kevin@knowledgespike.com viewed all Identity Resource(s)','KEVIN@KNOWLEDGESPIKE.COM','GET IDENTITY RESOURCE(S)','*','ADMINUI'),
(43,'2025-07-17 16:39:10.720861','AdminUI','User','9e7a274a-487d-406b-9f00-2e462779db40','kevin@knowledgespike.com','Get Protected Resource(s)','ProtectedResource','*','*',1,'kevin@knowledgespike.com viewed all Protected Resource(s)','KEVIN@KNOWLEDGESPIKE.COM','GET PROTECTED RESOURCE(S)','*','ADMINUI'),
(44,'2025-07-17 16:39:19.873828','AdminUI','User','9e7a274a-487d-406b-9f00-2e462779db40','kevin@knowledgespike.com','Create Client','Client','ACS Statistics Application','2de03cde-5f94-4dd5-b138-258c08093317',1,'kevin@knowledgespike.com created Client (ACS Statistics Application) (Client Type: ServerSidePKCE, Client ID: acsstats, Display Name: ACS Statistics Application, Display URL: https://cricket.knowledgespike.local, Logo URL: https://cricket.knowledgespike.local/KnowledgeSpikeLogo.png, Require Consent: True, Redirect Uris: (https://cricket.knowledgespike.local/signin-oicd), Post Logout Redirect Uris: (https://cricket.knowledgespike.local/signout-callback-oidc), Allowed Scopes: (acs.identity, openid, profile, acs.api, acs.api.read, acs.api.write))','KEVIN@KNOWLEDGESPIKE.COM','CREATE CLIENT','ACS STATISTICS APPLICATION','ADMINUI'),
(45,'2025-07-17 16:39:19.893181','AdminUI','User','9e7a274a-487d-406b-9f00-2e462779db40','kevin@knowledgespike.com','Get Clients','Client','*','*',1,'kevin@knowledgespike.com viewed Client(s)','KEVIN@KNOWLEDGESPIKE.COM','GET CLIENTS','*','ADMINUI'),
(46,'2025-07-17 16:39:19.893367','AdminUI','User','9e7a274a-487d-406b-9f00-2e462779db40','kevin@knowledgespike.com','Get Clients','Client','*','*',1,'kevin@knowledgespike.com viewed Client(s)','KEVIN@KNOWLEDGESPIKE.COM','GET CLIENTS','*','ADMINUI');
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ClientGrantTypes`
--

LOCK TABLES `ClientGrantTypes` WRITE;
/*!40000 ALTER TABLE `ClientGrantTypes` DISABLE KEYS */;
INSERT INTO `ClientGrantTypes` VALUES
(1,1,'authorization_code'),
(2,2,'authorization_code');
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ClientPostLogoutRedirectUris`
--

LOCK TABLES `ClientPostLogoutRedirectUris` WRITE;
/*!40000 ALTER TABLE `ClientPostLogoutRedirectUris` DISABLE KEYS */;
INSERT INTO `ClientPostLogoutRedirectUris` VALUES
(1,1,'https://adminui.knowledgespike.local'),
(2,2,'https://cricket.knowledgespike.local/signout-callback-oidc');
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ClientRedirectUris`
--

LOCK TABLES `ClientRedirectUris` WRITE;
/*!40000 ALTER TABLE `ClientRedirectUris` DISABLE KEYS */;
INSERT INTO `ClientRedirectUris` VALUES
(1,1,'https://adminui.knowledgespike.local/signin-oidc'),
(2,2,'https://cricket.knowledgespike.local/signin-oicd');
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
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ClientScopes`
--

LOCK TABLES `ClientScopes` WRITE;
/*!40000 ALTER TABLE `ClientScopes` DISABLE KEYS */;
INSERT INTO `ClientScopes` VALUES
(2,1,'admin_api'),
(3,1,'admin_ui_profile'),
(1,1,'openid'),
(7,2,'acs.api'),
(8,2,'acs.api.read'),
(9,2,'acs.api.write'),
(4,2,'acs.identity'),
(5,2,'openid'),
(6,2,'profile');
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ClientSecrets`
--

LOCK TABLES `ClientSecrets` WRITE;
/*!40000 ALTER TABLE `ClientSecrets` DISABLE KEYS */;
INSERT INTO `ClientSecrets` VALUES
(1,1,NULL,NULL,'SharedSecret','FOWZHtghkS3FQW/dl8rsqBv4+DqQh2K3IJtR22ELWkexzwv0bmySsH5DGkL5lfpVh4YyBac7WJ70/nl6pk4luw==','2025-07-17 16:23:37.730688'),
(2,2,'',NULL,'SharedSecret','rEupmALgn9v+ysqJNq9yFdAnsRle9GL97vrPVe0epR2x4UKueah2Rex3falXHCr8UyaWMUA9RLJU7S1P0+XscA==','2025-07-17 16:39:19.839862');
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Clients`
--

LOCK TABLES `Clients` WRITE;
/*!40000 ALTER TABLE `Clients` DISABLE KEYS */;
INSERT INTO `Clients` VALUES
(1,86400,3600,0,0x00,0x01,0x00,0x01,0x00,0x00,300,0x01,NULL,NULL,'admin_ui','Admin UI',NULL,NULL,'IdentityExpress Admin UI',0x01,0x01,0x01,NULL,300,0x01,NULL,NULL,'oidc',1,0,0x01,0x00,0x01,1296000,0x00,'2025-07-17 16:23:37.730079',300,'0001-01-01 00:00:00.000000',1,'0001-01-01 00:00:00.000000',NULL,NULL,'',0x00,NULL,NULL,NULL,'00:05:00.000000',1,NULL,0,NULL,0),
(2,2592000,3600,0,0x00,0x00,0x00,0x01,0x00,0x00,300,0x01,NULL,NULL,'acsstats','ACS Statistics Application','https://cricket.knowledgespike.local',NULL,'',0x01,0x01,0x01,NULL,300,0x01,'https://cricket.knowledgespike.local/KnowledgeSpikeLogo.png',NULL,'oidc',1,0,0x01,0x01,0x01,1296000,0x00,'2025-07-17 16:39:19.839821',300,'0001-01-01 00:00:00.000000',0,'0001-01-01 00:00:00.000000',NULL,NULL,'',0x00,NULL,NULL,NULL,'00:05:00.000000',1,NULL,0,NULL,0);
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
('bootstrapconfig','{\"PreventDefaultUserBootstrap\":true}'),
('policy','{\"PolicyClaims\":[],\"Version\":\"07/17/2025 16:23:37\",\"ResourceType\":\"accessPolicy\",\"ResourceIdentifier\":\"07/17/2025 16:23:37\"}'),
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
('3386717f-7025-4e5b-b3b0-bd834a90c326','admin_ui_webhooks','ADMIN_UI_WEBHOOKS',0x00),
('628e4e8c-55f8-4681-a672-e82af466ec65','admin_api','ADMIN_API',0x01),
('927e6f58-57d3-4fe2-a6c8-cad7a3f5370a','acs.api','ACS.API',0x00);
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
('2920d5dd-6c0c-4d6a-ab21-8d436140f938','admin_ui','IdentityExpress Admin UI','ADMIN_UI','ADMIN UI',0x01,0),
('2de03cde-5f94-4dd5-b138-258c08093317','acsstats','','ACSSTATS','ACS STATISTICS APPLICATION',0x00,9);
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
('340ccf33-03e8-4df4-a2f5-8a2d04cc8548','openid','OPENID',0x00),
('3c664eb1-abe6-4e17-8ae3-9dadb8c2befb','profile','PROFILE',0x01),
('d1dd6f61-bd1a-46a5-a8ca-2afd0b0234d5','acs.identity','ACS.IDENTITY',0x00),
('ecaeffa3-8b9d-4fee-a980-3d300679e19f','admin_ui_profile','ADMIN_UI_PROFILE',0x00);
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
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
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
(18,'role',3),
(21,'email',4),
(22,'given_name',4),
(23,'name',4),
(24,'role',4);
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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `IdentityResources`
--

LOCK TABLES `IdentityResources` WRITE;
/*!40000 ALTER TABLE `IdentityResources` DISABLE KEYS */;
INSERT INTO `IdentityResources` VALUES
(1,NULL,'Your user identifier',0x00,0x01,'openid',0x01,0x01,'2025-07-17 16:23:37.611093',1,NULL),
(2,'Your user profile information (first name, last name, etc.)','User profile',0x01,0x01,'profile',0x00,0x01,'2025-07-17 16:23:37.639749',1,NULL),
(3,'Identity data required to access AdminUI','Admin UI Profile',0x00,0x01,'admin_ui_profile',0x01,0x00,'2025-07-17 16:23:37.682887',0,NULL),
(4,'','ACS Identity Profile',0x00,0x01,'acs.identity',0x00,0x01,'2025-07-17 16:34:18.335828',0,NULL);
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
INSERT INTO `Keys` VALUES
('7BB056D6DDF113E8E06F40374EF70056',1,'2025-07-17 16:28:46.293410','signing','RS256',0,1,'CfDJ8LwnNV2MojdDtqdrfmq7UrMu-wbee7ZkM3HpZQTW9gGMGtnCU7IhlweI59CEJFgVbXE5XEeIqZuVs-UpX47L1GPkdLD-I5BhAACukEQR6E0ggDMnHcrmmHWH4R8ZRyWp7JpkJSx9sPancwv2gNvYbkCNSvEHBqZmj6H-hOzKUXfwqQH6ExPuU7wRWLVplEbv0j3-Gi8fOzJ57hUa7u6q5NLgqMcGcDNgLbQsOz6xM5NRWL389Vzc_d-48LDy15Ol0m9pGOZdt8-4mUYxjcCsuglye5kqfUkhRGIiTFkK1mZgTMKh-DzYya5th2qnGxYwTw7g7sSTAHRSqvEDwSnC1OjXG4UMkFhztQD2-zm1AjlMpHXaElLoWUmv99ZfLixKnImZ_T9AOoUYgs9QJGgEa6p8ypPobbXNlr7vLafU36SFbn1utbizNGOo6pOzWRRwJIrdrj733X8Wj1pVpbWzhpjQyQcg0x_o5SBZQyEQ6t_G2d1WnbYn4I_JPauh4OTeKYoro630vdOULKfgqVIj9ObzJeQzzxdI2AsBA02pHaolPtNEbxOTd0lsjEjI8R80U3yaGBW04BvFFrmBjG19WLFAYM8eliXZDzFg5m5wJKj8KTdo-qXa13fJUCJTvhfO2iCK3ydZMnc1RO9n6kiRXZ-9CKH2DqcXWAFrbomHrofnzEnZAN7hAEvFNn-0TY6VgJqZjwwWz4NMCYoN4iZNHrQ4-L1XQ5k-rM2JhfnH1suMc1h2L7bzlGmJcan-O2hSjdczpsPeihQurCqg7GAMMukNwVtiMLFr5CzrAjkZP_QJjtiKwohim8mteQECXiRryThmnZQY9iD1E-nmEuYwBe4IxPiF6YWfXSumBQ1Ptk8ZbBoQD1brDONQyZ__VwHaYhKHtZaHEnuuV5NVPdrI9VJg_UGzp_Wchtjgk_XFWEHaUyztAUg4yl00ByWEOGAQNb_H-5j2pDXJgVVdBLiJ5kwRyocz0S91N-svrcKSJbskp8UDTxpHItG67ElYk6ZFwKa_IKr0a97nX359PJQ3OgT-Z5WwO0YLwP6UWquzX0Xy2iUBrmj6fZqPuwuytP-dJXnoQ0RbD7BgEbXPfVLZZlFNWFtaqMYBDg-deGG3jiIDxAUZprmPMzoMIhSWGZLFnMDjPpvC3U6zMFGXWLOlIoSRyiV5olBpmIyaZYOFscLrBt_9MBqBI0fW6Q4Msb2TnaN29Gg7abA-aDV2Djln_Q1fVeLlCarTu_QhQ3GCz7PfZq2jEbycr64Ai4SHzw-27qNrIwWDDyH6b14qSaz7mDcWHJg7st_8uXDH68XydUyeWQGDiygiHwfsbXcJecDPhNmB1hMRJIymD0TsbtMTKuixvbnG2GZFz9UvnpEKavOwU64ptN1ujaJCw1aXECwfBGA2x9oPhoTVTCWiYlOZrh0dCddKfLClaeIyZVmB_I3kFzYmKvyee0zOc06jWaBCExHT1OLG3a0JtWpAwfIB9eH02zw455UfVqhQYqch67vo5H9QcWalDtvqX_I6ExdhbkAZ4bnAJqyqefMamxrQW_7v6pdppemTe0oV4AEsRfUJR8N7Q74vvXQ7rukKdz6osmnOo0VjpgHMNmVIfApteAsdL3xVlUwHYUKXFSvZp83zEbbaqsGNMSmtKMlmVsGt06rXWY1T5Z2cBMU9FAHWZMUnS1SJWrw50cwtUW6zk0HWFjTbuSUoSA_P3rcZ4qp4XefFY2kPa6AwmZuJ1TG3qGtlA-0E-VOOMXBKBtKrSN0Fgu7RRxWyUeUkEg-J8wfmx9YtQqtkb-zBRz7rXStd1caOTtlV1Q4kaslFUHa6ps-irfthMktFz0hPBRzxYxogGPOhnv-I0mDMSRQWLAWeXYZb1iNHgsnOMe8PjCYQHMe7knnXnEavukLqA3WNrCX7fmN1GObnRHWNLjRgFhl8uefT9hJkpGNMPQeOHeGY9VkmoUs1B_DJUpm4SeHQQJ_pohYwslZJA1KSBZBKPrkqeR3R3Pl3JDfQlHxclwAavZrHDYQOdAhUAobLV-ivuF8WmwOmnPwZdEgWtDrV3V9Sl5d2D7RwjkfJd1B30Z4xW9QEpcYsadk2yYMqjAokiijvJ3yDc-427zlBVO7Plp33SOj3FmxoGLWrK-8RBB6JX7iW1px2vPAv_XAkfe3bZIbT0MiQsr-hYgrLZWv1QqnBkLUc9XN_RppE-6GwQw1Cdkf4dQ2MuBunm3hjJfheuixa4DFdYaZ3M7Czp0SDkKGGtnnxQKSt2HdiCwqsEvQXXYkM_T1OafAdhVHP4UQ8tCxvPbE6RyRMKK83of7XOJf685EIJiwGXw1IBE5HqdP2AMWX-dDjjy7FjugU-NdgelDhTYITh66v1s_PnXg1MhyHoq258gSqdRtnx3DYJYExXYRG38f7T8wneQvIAYyhJfwjwyf86YXCSwdijBJgXSW9a0WlTgI0YWqQigaT9lKThRiu');
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
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PersistedGrants`
--

LOCK TABLES `PersistedGrants` WRITE;
/*!40000 ALTER TABLE `PersistedGrants` DISABLE KEYS */;
INSERT INTO `PersistedGrants` VALUES
('79AFCA1328FA63297359392324EB7067B59384C7BF46E246B2894D2CB88A3F54','admin_ui','2025-07-17 16:29:18.861978','{\"PersistentGrantDataContainerVersion\":1,\"DataProtected\":true,\"Payload\":\"CfDJ8LwnNV2MojdDtqdrfmq7UrMclOxZ0KtTuMjzoHI9LdESSSkBpBrOLWQOVvmndgru2KjOwpRXVWBRBzb7ktZbbjujZuHTzbhFN6I1VoU7pbZkwIhpvUAa4EjWsLbVCCxZpV7R38cBqMewMeP16HTlVriHYJ6M79Q4Xf-2Zc1WtO4am1KyHGpVaZJ7na34qW3bUQs_B6oNJ-gsU2qbwVCLmPytby7mhmYygHO6zb32dAgrpNbDyOx5UNTCWRna3-Q1Nu9qgKPG7h7o6XJuR5CEa86tFgMBclglI0pq0fxVX8mexP3xZCa4dFKX--WjWo-trUF_VQgtYQW7cIiao7TkySx19wm0bEzAeqaNt8jv5OWKvTu8varhDs-m6g3OFBs_erg8y1N_fLOCGW1qH2bAf5IzOT7PrF07jd8enIpF_sEGhcdfDNgsPkAHm6NFZVuMXjGiacUVk_nj8IhdHJCLjWXo-O4pKL7oNU27AIGopJwoT954otMKC7AvtnH1ST3DsmpeO4KwHgD400NRQSQ4FukngAKkBeq92xLZdn5cHl4APV4xU5ToL884hBrNyWIY9ZQME2_XsM5njKVF1s3XGSzWJz7IxyF86ugnG4qhVH_nVG4IEpurtoZdGYqTjywtP3TMkQTJFoLqlMARbVI54V1DYfmT3Meq-BEB47SVYN4Ey2UOqo9_g5Yrh0p-vQV0DN0QxaM511yhKEETGBrWbuXyU0u26fDPSQUWBNaXx5LHbF-G0xBCq_gZPwVZqhMGJZuTt0bEKYXoYBYwOESv2yG3OxEKkWTpgg9q2SeDjN9loEzMmvaiT2MNMsg1jIA7LE3wBqCAb0YKeKdaXbWK_nSptdPsxXebsq-E4UXE8juR-SCHPCho-8s9D_Vt-d9l8wqH6naVeexyLyRI9Je2dh6GVROFeR1Lj0DuQ8iDmEcOxa5GBIxwNo8sBwK3xCzp3s7x6sgsTZZUnl84ACxCtvcOn1Tvu8exu3sBOcaxBwQXwJtAwoVk-QoIHl0qH9qCigyETVuEclgFytOknxbD3soDxiEna0bUamfuHjbDlwno4npKg48Iuv5ZGsPjiMofHpaUGiT31YiUzHpufw_wyoCjq7JPfzYxWibcp8V1Ds9gx21wuAw8m7N4vaEH7FDwe2B0CCyYqLxNwrMyiKbgJoZf_0N1mKqAuNJyKKLp1iUZ56g72oB_sPeMeNHKKo8zgI9Rfg8lJhQpd9thETeFnZuyT7cnhCKtDFdo7p2XACcdK0HnnWWEwciEyW_7srtBdRyJMI9FWi878uh36kVcClYdHNe9gvVogIqBafY37o3v1h1lj8PomCbMVT7z-1xecz0H2qm_Tjg_cSXWlXZb0OQd6HE-W2HOkb2b4S_ZaKXJ-8vzsqqWZfP_KKcG-ev-yu-d97TIWjPiC0bwDzh_bIfblr-bKnJgWXNB5BQvW_t_VGZzbphVajo5w_fL-XHoQFgjdzURAZxe5dS5edYA3zeSEaCzYeWd8zDVdasPMughKD-uASS-DUfPnrylzK7vHfw1_upIJZHAr7UvfVIH4WXmQb50pQ6QlJrqiqlur11bMFRw4Lz0o13kkTnDCGy-Lj_e7TXCRRdzikmz6f38MYw3ifKAZ-K068sNIu5md5hubKXTTaROl_XBgCsSUvd-Gkmqzwhlu8aDRLELjdXUgLgLDsdbFZlaPDT67vYIxZLvecMYym_RhgiauYcPCfnVpFx39DVbcp4VtxBMtH1_kFZqTwDVboKM5hRXBEwhZbdi8f6ftTeFwSlfg6AcvsJMY4O94mHsIpnjsyUBEMn5V9E7_O6IQzTSmsO1ld3O7Pz9SRc0FkbPVeG5Rxs8Qg4jcrVoqJT-FVxGJoro7he--qU2hFfyCSwxpniIlqOOamcMgAV3Pjf3GvVnJ30f4kXq7cNKUNU7NHpdKsGcAqIDXqnhpVBs_wyK-fHCkpcsFSi8SYjjVvmITSFzVuav4-fTHrHVnycb4PAde0GWcP2aKOJ9wz489Q0gheEXEMAoqC9UKwJn67zO_icsxxK4tZir5uiwQ1u-SfEfWWYLjrJUUyaDYJbZESLrYdHmnLsOvBFpS43zWbOPLcd_RCbQbjC_ePSfFiPkKgJaM-Jmo7SuzJPIQe8zY43amKoN-DDiMo6J7uPCHbfn38Sz4JgnRyZ4qu4nODVUNfOep1ZNDjrgdj18gynAxI2ZLuPt0eEcEh-1wnVyjpBtdksqLQJl1b4Cg8Kq4SWhdcBhACG0ePfomkIjyIfxXaAQDQTunAEYzDWNwqd1gVk-tBU9yGsjBXTRcBAeqf7ftYICIQ7LzT6083A\"}','2025-07-18 16:29:18.861978','14833382-84f9-4e8a-89bc-40b2f4628a4e','refresh_token','AEC5DE0E97C863E599986FA8E31D8C4A',NULL,NULL,2),
('32356A73067255B0576E278F25FA5198E7D5D7984B677AD8BF6E2EB2FF71A851','admin_ui','2025-07-17 16:29:19.143967','{\"PersistentGrantDataContainerVersion\":1,\"DataProtected\":true,\"Payload\":\"CfDJ8LwnNV2MojdDtqdrfmq7UrNekYPDC_HoHSsvo6J6_UCZkHBQL2vWvtczfPyg6e2neluf2XbX5jLuJ0eE3QmUqXBRUMLzOtpeOI3f3gBTGSXpDBfx6h4_tSCBY9RMTe0oA4KmEIK4PqKZphfDGXkhsjOQrvAjakThXsuCliQYAtpPOwDnGsCPHUVizBv5XlN-DrIzVPuh61Rya5vS9qDmDJc88RsQlT1jji6uRzoyjydDkcSgGACUIdflbSvB51qO3jv6yAAC19bXpjsRhWr3uWoOvWjtYPAmv8wjC2IPm6UUYGF2uWXnyu2rhHi6t7gphF2JC6tFOwLRo8diyFoiX9lK-pZ24pUXFrOb0IQ3r9x80IEtfvNSpuVtabdu586lZiTfa9KaRjRtVybU1LPX_jcZreHyhM0BbDtWnLuYfZGwiXaookxVTi0nW3drpFFY0QcUS69EVuKqoGcoo11-it-RwjOBe25DkHokfm78ymlrWhOnbAtnOn7LAw1_ym5Yt6WawuaPbnUJ1GPiFMRGkCD2ePWIDb25mx97PbxWuk9muyNz4pMYdWs9LAQ9v4XHL_t20fCSfMaf1Eh1VaNLtRRlSq4vHLtaSKG8KrUk3FAyWaSvqcnqVnUeKw5BvJ0geDpvJLwZTx_dZFzaPrgPImyJJ2fv2pDj5GH4riuYQgMduE3wQVZCEUkCHlOceEDc4bMbV8WC0LBZt9uo2TV_-AwBATVPoLEsgi2KxmVhjhzYwnrDi-xuPrJiWHvc6nz0sNgC1xhPI2bWy_OafxQJ8ZTiFJKG9NlWZXprzq6CqIPC2n1HBkc6XTX0EpuBpHz36WY4M5PCGc-adCBmpUQmF-y2HnI6a2325ozvFOQm41jB6xYl0TXb_V8vi454_s5OEDjXFZCpH4RUidj2pKy3s8M32Fm10joknkCRmE2itIM5eJ2Xu5s42d-gRjb93hsmmUA_B9yZoA1M5ib0Qxw0S_SWpScS9XJXNtyKFpo8EVG3Z_85urCVNpnm5SedgFenTWPaZiw1QVaI-xW-3gCJ-xpyP2nhZz4BtV03j7Ex2yllz72IIMMNTNF1XXRX5Tiv_zreSwdGWiBDF0nL0YXZB0a89FK2EZdIXJS_A1Idshy0YSubnV7e72ftK6v9kyI5cOvuUDY7BHHLQF2za6WU-KmBdhofk9ZM9_UhBD_oCzcVkAfoTotUQzoZ9Amd9NU5ZARxxUhWPOcjSW3gbc1eE31eJfWUw6WorgjhjDir0XccDIFfjTiin7QL38sYFc_W49ZqVFZ275EiptBBgp0Rr05HJqvR93a1yDs5mMnN5Hv1RCEAVOYtsKfoh4CHUnQ1SShuwFh26vUKq9kaS5aw4MrNpzNa91JFs03t5T0mLV9vc9npt7xvGUubUD6RojU9LHbsEzQVyu8D4zDh6HSHD_HwjIGC7jNekWnF05Y3qTzbpic6w1SWoG_T7lYfSbYgii7X_MauwN_yNSQ3Ot0izRe9LRW4DC-SITZQjNhGQjwtFO-kIewez4UILEuJMJxjojLWLJ62G3_aY5rrVvAMz1MaVcZAaQodDqDKKVrJ6CVr2tW4XwACX1vgo-TocYwNdAlGimyydDoL61uXdOMzQIzqr8tV1fve2egoX-x_scQ-v8Ff07nTlBgMb4GOX3WjRGRspEK2klJtysmlWD2oocAs0ZWD5c2MhVTc3VOx0AecDBFnMivjI7nXws_FBd4MQVX6rkR3bG6q-e-9wtLysq6btZ0WAqAFpbuX_dUVrtlFaQJgjlEjNmBucGz1yS2672fbYY7UQ-usHkXh2uecFBUgLLSMUjfonQ9-sIBK11ilgMfcdjbqUZQ5jT9LIxAgC0qf69xuz-tw1c4x3q0OkGkiEER6F46dkoDbJlOAZOx0sSteDgGotwNYliG6a-YSSP6pVJ2T6IkfjJ-l6g9V-B7xvUcEuVDsQWSjgoO0Aiq__UMK-xfeHDEspaY_yiRwrMekmlVIAwc1Vd5mq2-lbt-LQENyAV0UnOGZgWRocbFQ_DVK0DJj0DqbQ7zkn1WhvtK5QIaqupktDAKlIYjsDNY9YZ0KBYFEB1A2AvwvZcvFDlZjFD9PNfgOoMUVbNF9qrBJFamSN0--dNAbsOMW6ae206F_IhNHevd-Rff2aMhparlXk6cUYBXsxVyezU9Y_8k8ZJhqtuLaFq6gcTTLaNLziC1XOuzXXKyhyCG2fgFQ9ffYa7cU_e1vMHIHZOSGmQnXIau54LiDqKy9x02W-IFDGTu5W4PSdGu9-iBqzUYfxwEzkK-1LH-pHj8lEdA6LqdYqRnefy1YHl9vSPG7BYM\"}','2025-07-18 16:29:19.143967','14833382-84f9-4e8a-89bc-40b2f4628a4e','refresh_token','AEC5DE0E97C863E599986FA8E31D8C4A',NULL,NULL,4),
('D9837146B56A77C64406063522524D88B2470F65149D2D3013926D411FA87ADE','admin_ui','2025-07-17 16:30:21.610437','{\"PersistentGrantDataContainerVersion\":1,\"DataProtected\":true,\"Payload\":\"CfDJ8LwnNV2MojdDtqdrfmq7UrPlAqo_LEkt1OCQ_SAYJ_b2LEbNHZ5jBVxa7On-4LMFAlmLhZmPawoq-nBQMeukap6EHJFhPYNis0TT7QRXxYKX2bhMu-E0ZUxuPLElJyUeXUy_5A5UOL50QDMZIwu57_Ru5AmIfHY2wiNgOQq2X2CQvCzUJDGUrld1CE0PHfrbamapUsQ68zG6i-c_br86Bwp81Q4z-2OzLxdXzm0Iumqh3zSF65Ley5Femz_jt0wSgwj1RiMR6IGLWLmJrZEJamgQ9M3jASrkkreG6ygP5sMuLLK9RMUZ5Y-UwGGQfnHU70XeLflH9sFp8LvSp0oRFdrSE3uZTVjeuYozrgmnRo_iJ6wKJB3bUpmeAABK7I6n5mdk28-5ZMSC7yAp5sMCkPBLfJ2DKTvUMfaABoBIL1XnVvopMB2WkyYgqNXobC02O1EPYUfWinvULkcpoYExwurf4iuJx7jTxs7BdKlZPkZbya7HTDabQEd7iRwBZX6mxR_pNWQBFC2Bi6ZE2du2lwClG9im3xYKnA57xmfDnAq-7Xi6hR6WXG48zHKwbYU_ZbtV6p2_8NZ2y_C7zjFg-Gqjk0iRkX9cznYxvVPdNtk8AdVL-IlyUXNtCBWEHcEciBGPS264DA7uojxnYPV05dZDb4jt_bwv2sxyPcKFm-CSBW4a_QCPvxYX8rTkYPLsZtt-RIUWuXz1teIhrDkxJd68x9e0FR7O7V7xSicZxklVJ2Ppr__m86opxN4MyZFchfmA_YGefrG2nGoVVN78Hib3wLQlm4aSe2kdbpfOVEnC7yOF1F9apbenrAevGQfupc0F0OBgm1JGN3q_GJ2iZR7b9nc5evHMWujIcGxQOyJ7cn01eQrjAB9rrlnfSZlbvWqOF6wh4UoRNTd6uGsxfo87Su80AkT7CxcySGEG8KGxANelvzMbEqLKCKtkPW1sDDHQbGCrKE24nbmWi8ROhdDFrLVXB2hEK3CLOpfukaF5weSsOKNQOt5SAp37Sg7fZ8VXv0t8A5I6exYwpsmMFp3bTkne1AWU8WdF_mCEAos61Xd3t0vlQAHN5fBM_gAPhVznYWrYlanFh19qL5413Ml9JdFlwZ_Wk83nSxktREFxNe45aF9gWswiSnIrtsYngSivFJP89MEc90oyaPoQdBRmFc_tn3E2ry6QT7bZh-oLCS68j65UqBLmLHMxUS2LEowRDx8eEMf_QttA_nyUoqdOCFOuCviHLZFDSu53n5Gk8r0Tx_1RrIYuUbWKR64WbOxmgp_YokCv72nFFcHrgpjMTgwUu6zeSXwIgeLXgtuhLCAFO5eCgXw9Lrk41Q0wL9FDIfC0__HPuGOdpypZ_dLr-wipqG4b-3XhxwWpUO_kQnui3iiYKDQ4IGI1rf34Iy4fNU4tCFnYVmTg_YltLKQky9074UzHPiE7nNA6Q6zmmBctXT-6WLL6UB82QHACgQybHNTFmdaOWn_wG94uO6pm_Ij2YDu07LMGb4iI46BSngVwcMNN6WGxmEqLvFJyEwaP41_0kmVy-IDM9D-K0AUQEMBP-OS-KpPUVCWXnQeDqIp4zBirDtzNAtihrUUSRXZMPFfU1Z97LPuoidV0_3SR-6hoLO-dy3MPc728MsaU7FZYXF9hzg0HyBbacpNAC6r_hQ8N37_ZnkEAcZ8Us26bS4yPLmw6U3OnEBf2qH2fDCUzjht3dRqCAH3q1XqtHEsvuL1pZ4C0_o6SyHcOtaJYklD5S41glzY39NWEuTd4iFAcnRY_c1BCLDAohNpO2Z74FekFwJZTXq_0Xie4xOLQgk8e__3A_lMOyFDdu5k79NpaSiEZ4sozhIuH9x_2FNPb1Ie-7eGWpkN8sM8HRNQkbFcxukQ1feMfxPeKQpilCyMSCIXJeRBh9QZ9Wtr6zo6YCgfkFkkq76Imhhcl46K7tYO5w51rg8BWdfoNUWZByrqNGwwPVdFQRfiMILSs17h0Ns2BL4a_i5wlBKEIn3k8UpqGafOYbn0bhhANuaIM_PXn9JaVcjFqy6ZKtzwQ3HnfVqBZpnHpFXMBSFLR2Ew9kufqE7S_-mkyUP99zsxiQDLQPVQu_uIrba8UPc0G5I_mLOLXm8hUrcQuKvN3_mPj1kTX5ttqwjOstioQTcSxZr2PHQ92jXPHJFxYFgkIsobAYPibz5Ij1fklVye-h9symjGZumhBbsTc5ps1_cgAkNlazoSttLSxS6LuLvkuL3kb8xPGr8kKN906DfI2tOLDCOR88x1orr2X0zpcHI8dWwkabOeStsDy70perYK-XX_bVVzgqC7x6Ajl26VX2kA\"}','2025-07-18 16:30:21.610437','14833382-84f9-4e8a-89bc-40b2f4628a4e','refresh_token','AEC5DE0E97C863E599986FA8E31D8C4A',NULL,NULL,6),
('C7D59B3B983629B9B404927E3CE73592171AF9AE9EFF88F1107F6B21CC68C2C1','admin_ui','2025-07-17 16:30:41.480858','{\"PersistentGrantDataContainerVersion\":1,\"DataProtected\":true,\"Payload\":\"CfDJ8LwnNV2MojdDtqdrfmq7UrPrQKNW60PmklkCIIIlMUnlI4eF7i-k6YfAxVt9WMC70W2tYUGfNF6UVZ-2XgqBg0GBtCXN4GJ2VlRnoeL0hgt1jv-NmP80QP35X2X-0G6eGuvWCH4bDjnxrSpu8HPQpKUyNhTKIaPD7i4fJq2anJg7t-lgk-CPzwOhOyzwewd8-iBo3RMk5YAy1R6s1jnv7dM7GUye3Fu-FIQdgEEtgyrzrOutUIm43dZOX14WpCAlvA0Z3Obl2UsvajLq14PsTR7adrDuQkxROYsqedJ9vzMSvxaFA2jRCAPp4KQz4DcnvIVXTKjnmxe6CE7q-lS4Disr6KZ-X1ckguFD2D55QbWSbs8jMLXD1CISwD4fEpBAvC3pa9-R3gHyTYwURTfADXUbsUqK1ZUNGwIImd_1LTQumHII0oy8KIwt1EWTmP5CoOOnMcHbegeIyyXyhYJnFDS1QIq4tMOQvb1z-lMBPaNWKW83k0u80tI-_-ii0DNQst8LPBAwfDVpvGDFDDILTWyEz8vJ0nCirKX4MfqUZmdLGWqYV7S6sh5Ye6Br3yWipTUEtH9_oTubuTSs44ka_v7zf9cKAVUuhjvLL5DqZLdUPfkPt1zZ2ychUOWD3R6U8Ot8ChtTCOvHfD1P3BpXV3lJ6POq2frzVo0ywiGNUBA_lzghD_B9eTdiYewZK0pOiTxFSUYuuAZH9mdu7tDrHIBl2gRLV82lUbv7rP0aakoWqiqdP4VB2Q-GHAWdX6WB6DkQqZQ6JbwVzZ55dPmRn_fPq-51fMfzxKISADioH8O9ryhXKLI8GG7XithpZ9anUMn1YkZue4M3zLF3hIr5TzqLJVbK6GPh2BzTWrmb3BCeiH-dHuDTZH_LhL5QijZGOU157nayn74BxAp6DVhhF8HwbcfTQS-Fowp2STZLBpAjh5UZI8RStS6QRfSxrrndOnSthpAx2io81qkxOsBMYM9YLEXnFBaBH2QVZQapAh5W8GhTYKrquFoY6GMZ9MiYHilo8ltCIxJrgq1qdcKyhO6Z47tbKtCUuU54x19q4teD4_D_azcSLYrtKFvtZttDRc3LuTMJYUYQ_y2_H0ayKufjPWtNzokMjVsbwSDnW_cedf10nRNYA4FGUxbKlP3yMJWcIsVR1Iq7o1yV_vvMBx45tr2fLtnvmFLhfpiK6rhrSA-Usc_cLwhsfgr1lZ8u25LHph041QgsBkv-cG5apKk06hrX67ZgTDMBwVEhgXDC-vv9G9bPhzs7l6GZ0TVczaUxeITvm7KoltBJcm8y3KGr6HEkuo2ZXXdoqZDbfcaDWc14u0ZOFfylvcD8P3nzwrXzaK7J8jheA1_YW-dFCY52SnxhrDhVySvHCNmCoAzj4Gv9sB_CWgzPX1n0W1iSEQVcQpcvP19M4NU41PnldYuamnrYVkTcJa2od9yhLKW_cTq77fQIKSHcTdsmceE31jyENtsKR46rxIT6vUIG01ISUGO0vPVTzqDS8pLkSJuxy2CVWoyY_5fVTLkK3Fjb0nux2nVK9w7Hkhqt3IBN1iFWm08xN3kvqJio5LlkZ9hl623Dvr75fvcGXs-8H5d851oVbtLIi6ASS_zWBmDo10RL-yQxdGWJywoX_hfVlYYoc6IxKMxV9V4bdQv6N1FDJLgT6vF7Jb7fooC_FzmwyxkJ6eqPBNHJTm3SgQdxpHuLXVL9FZLQI8HIVF-X6Pug677SSN-ACCuTRxN4EgveZ2XMlUtnLisdsFwlLA9Pc_-x8NMutC0ak_ll5abeos8BYvoemSqVSkclG52r_fZQtSqvMV9gi-btXG9GkP-ypzUYpoVGMuu2wgghSDkbRL8CXSu0AqGlHz2A56fTNstms5HnhCRoxmCh3-T1nNQxl07VQ1eF4GEkRpSOdodkuwBiJJffW7v0GqEbcdQ8ysZvuWshZYe0S7jAyoDQ4zjwNODM9bZ_F14zyd3kvuoA3OfHLKebvAVHUoJmPtH1GKrEtyvGdA9JeXMQiqJ5XMChUmjx67yELQ690gK8UxrkLWof7TxEUGp9Pxh-vAu-dQABuf126OtqN59V3r0-VolCGifxD1UMoR4Uvq0Kq4sex_9shLbwAuoTPvHslnhI_AEz_nzP8VqGQIafLMzRzRMWmHauQrhL1X8-BI272Lh-WgiBeY8VwUfAOVriplvSEwx3jdJLNRAK5ynaOG7AMQkZsEFS8p4HInTQknSg_QorfQDegwZBbVmCKe4l55NYHsDnXfcenNVwpQ_Da4s5Us-zgs3PMxdt1Sh8EeRqygkbMCf8TRc6lmTlFw2FDg9rcZiz76E\"}','2025-07-18 16:30:41.480858','14833382-84f9-4e8a-89bc-40b2f4628a4e','refresh_token','AEC5DE0E97C863E599986FA8E31D8C4A',NULL,NULL,8),
('C27EFC7F7631BC47D6BC67AF19C0C1ABF7493D6333DA364C31C3D30B8B739A34','admin_ui','2025-07-17 16:31:05.830098','{\"PersistentGrantDataContainerVersion\":1,\"DataProtected\":true,\"Payload\":\"CfDJ8LwnNV2MojdDtqdrfmq7UrNyVvgdoqGlT9rX-ufcSSHv9TulnprWUcPfmGA5NtblGJnQkqbpxCSfPaKbyCz6eEWZZZPD2rUg9NpVM6VqSaToY44rTTcnxJBNova7I9CTYi_g-LKscqEj1HKHCeeYHpjOjckoY0aapYT-ehCQY3v7wQp4eL6tZnzzVrqLQcdjnHZXXHH27NuvadyitdzEi-3wKT5s6TAhrApjLfWYXv1iCApaYnRvywm8rUQfdD2U0LZhNLUb0t9_QNZCD7pq9IcZpgDgb2cN2LPgw8XVCfFIWltQCb1d4ZGaaEiWUsRIez4Tu_ThygcGdrU_eHIkBUz_5ZJvsNq8LgKR13LxjO29tY6TnqgzOA2qDbiZUGMrI_geICOqQVpE7nFapy3csNmaKDLHTnXXRPomUQy0y8kKlhScx2pwSyoteyvp6NM6KS48jiow2T7ESVAIqbyiRvuciBK4XqpL5uPi3tr96-W-1CFRhN7wcFCsKzrtMBUVDCE3mjrqTGWy7jJZ_bL0rm0KpyZvO52VNMbpXzPWf-kkDdpyGaBw9sadjHGB-VV3iYL1dHc1iDanZz1Y4pSDP0VxT0ghCdSFYOXWVWVAdz5wIHk6517hwdXkzw__c0-NUI8zgR4p1Vng-TjVh_uNvj9uihVUJD-V4DYJ1Wl6JarjtFzdNzn8Jx8Q8wzLr5nYP9eUm9-u1qXO32h8sKSqumdMXpGfNyU0rCdd4Vs47aWDzQWMsyLzy7eaFkc_ZC-h_N_LFE-EJEewpNFUwarJCSwkIysrblUzKdxKks5kDy4uHnNrjVhRdifJElaxeUQii0Qi1DcwqY2K6oB_jF0CiaXC7VZgG8YQnkNgBos3gXTKbrFx42PR7VCEsxUvP6FGK1Y50v5rSozC5Y4_i5_gUaX5v4H7m53D-EDf7H483HLRu7ZtLhk2zQkolCrFbQeRiKXoVS0M7rBcdlYM5jrvPfbplRHDV_T4AhmI41DtKHTsgk9c_5MyWSTUKNxXp9FPa_4gAs3nrhXFuB9JDR5lPWMz3MhzFhJXKqnYYApZ98iKJi0bCuy2cUqXu3G8niyFvW_Gt4GbmKyRLuF9i4vm2PHXzHTrqo0t2dNbqoAYV_DibtyiCp8umqMFGEjbUqutcsju7UJkAxQeh0SRpAUaTtsf-pLZNYbiXkP-34oPAF64BibVrhjp70AVsT0VMVKx0S1eeCOt9MXox1CzykevFAzhlT4YvHOE8oS4yf-x3icUfGlGd4c-ahgA5EhsZCAb89HxSs1CkV8lHNJtbFsBCsuC9sNOiLcdhGZad162PS3t9fieuVIydW-5ZmH0e1Mkqo5UUBP_74y6JvVEypOjzIkMwDNN3b7T5drtxZG4xqFYR3T7DgUyvjEN-bO7KrcJtwoPb3i6lOYtZxHICehJY9n9akd85veJ4ynYYLwMeELbd0HmYBMnAaONhbquSfWVhOmUriBpdgDW8jAtvzNVWxDaGEHfh-mG-ITN8kdfnTaqB_KZJgATinhycrJYYhNbDM1hRRESPPyODrrAQVEv0dLPLdv_uDsuotAy6OT-9fZrv1ERJr-mgaS_UWGmiB6-_PUNt2gXqmYXR29q115bWwUDeRPO6CCswlb-nBtXiDAujx9YnHqRKfy_VWBJpfnTjcAB85LkPp1vywzZ-xa0v4l8PM4DHsikcPuxvl1J7BFSWWDL4ml22g1j4Msf2G38pN00xvoNpts7lm10e6rpdT80pwQu322aajD_dKspUv8htqfkW6V7KtlVm8HYvBx1dCO7ck-mRwgi1Gynorb1S9jDkK_znHnt152GAxAGA5UyhTt0lEikEMXGJpiYwe-7A0l07rqUCH1XouPG2B-pnvzZqVtloWPkpMxxQy6p98QAq6kwVT2jEMqrdoewZgNVaSn9yjzg-4Tm46l8GEh244CDtQb4cwrnMRif_4az4mDgXdykWBUkFKLxYs4w7r6HGibg4CxjPDj_KJru3AGc-k1Rg6xh2vHGQT7ZHPaJ8isDePQzjbiS3Al2cNBhuCnYMyBstfiByWq8_QCEBNGl4LuoRamr4N1Xxkc-OBtrGwwzPLneATjZmsojcL6t6GhTsVVnrsoUsiZU67qXMOBE6WzJCYS6y3g_FbW4wMGzyT5JNulXhi2x4lUjcclQAnq3oZCuoYwuF8ga7QVRpXeHQ9fBS_doZkS5bVxrVI0pm5G_KwvCmzCFh9LQFzQhTSdX4Q\"}','2025-07-18 16:31:05.830098','9e7a274a-487d-406b-9f00-2e462779db40','refresh_token','04B5B4AA294DF08A2E6DFE251F6C6EEB',NULL,NULL,10),
('F30272813B504D2639CE9516DE2FB0CD400C2E657DB6BEF18A7B33D23E88295F','admin_ui','2025-07-17 16:31:39.498002','{\"PersistentGrantDataContainerVersion\":1,\"DataProtected\":true,\"Payload\":\"CfDJ8LwnNV2MojdDtqdrfmq7UrNiY1Q4D08JL8y1k299LxJI06xpq7FKAeouEypqCBVkByn5UujtdBetkhodLaJO_gkPB40xEkrdOTwbuUocDMhvfh_5W2nuqUGgDA99weiuAhyLrZM4sVzqslltD5F4FFzqRJ38gNyWngPgyPGjJ-cCnwiO7LTv2SU-kagVNrYTn2HMBU2_NKCHka6jrv_uJlYqRGv4-8xbjRkKpzEK8KcOXp3oVrzdapNr5ZoVeLYu3BCiLYQrR1P4C77g8WEnzzev5W5buhw-tyiOL0CBOQ8btRAMM8xbSv1qfHk8PDb0tCWZgxJT804NU5y5qxwtcN5ewQZ1t9VGFBCF6b1ZclZlPT9tKs9AufW9FE11Lp72OfY93GbMviWr55iyO6LrTne3O8sgF2qk5HDtLFHDdC-z59vvt-xVQ4RU3b1UiALpSpCDe_bXzcKu6M1NFdc_6-1JivVHxvtvZSak3hWeNlTJItfc60W0a2ukXI3aObW9qMjYQcKXlu9c459Q8ySn5cYz9ZQy4wS79qQNaaASlPm1ti1jEMhu1sO46ITo0q3ZlJhKL19ENkA2II6U3jDp4P0YWiTIJcC-qQH0JI0zo2ZpisgSpI-nSGuGv6t6ln07vscmWXkw7bg6GvbYeWojL-AyvmhshCn-BkA5Jnh9Dyt98ZZjrqna3pkGgcFN-iITYczy23bbICm01MAdvLuwwbEuUwipORslun50MHatsX7Znlq7rwbyYIgHd1P3q91A-ZS95bv4-yx4WSm5I_wyLh-GSOTaCHlXJZRa6KKq8zRf-KNCtB2xMDDvMualM-hajn3POnZFVb3jrtO56Ssfzl4WQN4Q2yDEyFqEBrseIQE1hgCuuq8D23hqFDJCh5DrfWa5YQS8sE1cZZbocrlFCMcs6KqC6XPyv5izTAXLEFHVao-czidcMjOMoy5_YPm7HvCM41Wfkl47EPXtakurVQKcZQcmgxbRcB66qbzyFTh9n8e0sVHDRwRg3l-BEPUcxjEJlSw7pliiBCAoE7jvvTfln7dae3PCzx6c5QeM9BjyQkvqbyGCRHKFDbZ7HPxnmATnz6SuExmr8iW7EeQ9XfALUv7fF42vxrNs9gtEPEKCQP_rnctKGpe-vP_X0eoYvoVLvhAkJcVGj0oYxM28hol-8HKvV9qo5qyYaqrbp_4nBeyGpLDPbKde3FYH6Y45QJiQYSVdGXFbPmrOb9iPA0exFtTwhMgCAj2gVkW51FQzjaSrI6M-z0zsY-xpasQmxSnL79AmvxjbOPkKYaliNETM1YQ7bi8IJKTiU2MP6pYxWr_AL-7jHvQj4f3yAJ9aQFjvZYiqL3DLThReIIHmv_-yAGX3bS_CEhc2ltIk0fld29YcBL2cKnkXWtXwaH0NwIdIlGsXNv_MTTwdoc6cbBmEP3LbjWLn7wljUBPSjsETJlu3RLgLo0gk_7nv7Aqd24TipWYmLOuKHAhFpyWJPbWSfvde1I8X34iR2Vsv3qbug7AZSvjD6q_KUtc4wyC5faOAbnqOqOPB3HbF82wgfcti6-lJ2mAME5uuLk1qyw58a7W_ilj-CD2IZs9qS9kjM2-kc_n8VA4ie2Y1WiGCtU8thzFtTQuyEe9tahUND5X5Y-45NeY1tl_DEIZqCiOOszdodHtYtCGZdrHEIPco2agmDWqoc4_8j3lnGvnC2O5fP9xn4Nl4InYpoPIo6d63IRSj136l7sBBfViZ9nS9Zc-eVOd2MAtJg7p6ctuinUCP06cDWjvFKJS_DczbIymtg1jGw1JbXEDVVIEqMXz1Dr53o0OlhMsTMb55S0D6JgRiB8iSuam80BB1aMMRUF-rsuLEB0iBn7oQgzZuL36kS7ce4zl5wT1lZ0UT2COQRWpW24HNiK3qyOcR5C0GUb1-P5ZoTGnSrQ-SECemDFyTiUDJrpbVbtbyVuBsoW8GwmjTdHDda-sEcKha_Ln_USfLO-i06zRhMz0go_yDvw5T73ZJaxZDMWbgWX-xAqqhnJvphVVdihFLi9AkoQwR6XD-NfXmrN7P4ixEwWJY34LokLXZWD_uQDLt93CPMk_zegXf6w3iVAwepSKYlOklKOESABovwfC2i8HylhSlKpxbdcgB76ZLuP4Q_5sWlCnqGdwP_x4ymy5I0Wel0X8HBf1IVGVUmKrT1WdFyknNtBcYMsMqP4cHltN3dlICSKrgPD7mXpL9168RAbmbdYtY91Chn7DFL-JAx2tu4a1vJ2f_rl_O08k8S1oe4qNzCBwOO8OvL-ekaqMnn8fRgdJHOBEATdTfuD1Q6kBLdM_DBAJWnFM\"}','2025-07-18 16:31:39.498002','14833382-84f9-4e8a-89bc-40b2f4628a4e','refresh_token','B6013EFA4101D04A04A87A36CAD79744',NULL,NULL,12),
('9CAE45F0BBCC7D381308F511EF5E560223152682053173625AEF45DB89C2DC85','admin_ui','2025-07-17 16:32:04.124975','{\"PersistentGrantDataContainerVersion\":1,\"DataProtected\":true,\"Payload\":\"CfDJ8LwnNV2MojdDtqdrfmq7UrOt1XDNRSsBEP5rbp-z2_dGqNZKCU-5QrVHHukwSWlssc8noTqDCiDsNz5Ji7BXeaMMwjq9n0YsPwpVz_ZR9Jsi5UddPX7gtNR1b8-gumuHMol3F3u6P5yUkRfByXrj4wr0j3I7-vW7VucgQqv9DreRcwxvnWkySINsH7jM61xEWjSrRCD251NPYpwBakYoxg6hBzPhd0mSeGq_vPTH-onnHEfJeDMsmpfda9bBZ1TtO3XCOuYSqq705CQxy7-uNbU5Wo3CxkrTFgzYHSRjOVrUUKhvzmLkpR9i3un87r4rGoCTdY2JuR-9GggDKBLDb3HcTgPzIojuTTU7LH8rh05tblF44w30m6glcogTmUnWb9tVPQE45Jtjv19of8CKf7QB-uAeliHjbyQhwIpMkKpiWGs_27d2g4MTCM-S_3NECiXXUzHHKPxEmvKiruURh4PVLQVHVFgqAdXMcpK4kovzjBab72eI5PNXbz40GzqQTSIr1hLWkC9XFItNnauEMfu8k3Q_RGLQBVNhcxUksXK7_YqFUwKWdD23lw_LlvAmHJCqe66dqD2vMHqGzmeDsfqdxBtxNOcRRsACFpXsQR3TUwox1tDIX6UbArZC-osRWJ2CnvRN5fCaUj2iiK7M5dcYfBMPH4Y6DWKuyOH3R_igdxcENimLEqJmtpDDXY8NR6-dhnXa_UxxmSdSbg6QSu6uZM5DArZHWFG5AZvdxpSjbkfjVdv4o-Lgaptki_8WetTda1xSaDmj62ssofa8jaDV6M6LEmnnShcqK6c37C67C-x7noJCGm_xRPsa-JKCA0cT8RTBETXfCIjeOOyzi04JMdobjXxNFCO3wDxV1eHQq1_KwHydHIp1bdlP5YhSBQtmh9kgkote1XFIcccaQM0zVrdCwQQKXK8FdGknm5SklRonwBazSb8wXtGGjICq25DSFAu5Ss7Gql811x9g35wKQWIMXmuha8IrJHzEDQ2kBsM-PxQ235DpHzNDFh6g1sb-1e150uU5Z4RAdCQ3ig2cz4TPBQYMlJ7uF-DckQ_Jd0v4y9RHaXnvNxMoKuHanzxdi1m43Vk7SzdzRjWha1q7pLEHfgTVrbps8v6yEVgI9kyLglhZVJMNksYj9CMargn7DaB19cQRcL8mal7VSS4yCX5whtCtg3ibE5ukghXOPecoAh8x_sYdww8PiMjX6DXQ2afgcZZt46rW97FMn2fF-Gc2m3WOmM1YADWn7vaAAaPhQPsph26qVuXIBzhBO3CgSvskHPtT1hzkNED0mc-I345zW_UorvEU0cNwrf21A-bKf0QenqgCdDBbAb4YeKt23KhewAclrWB8OSxiUGKkQswD_DSUdAWAtWA_h4TsSXjenKykGV-J-gQG_XE_YRSLuuN5ZCIj6tmCtdIMQDvYX18cIuwNht47ndWL4D_5vinD74bzYnJGRdB-G5Y2u6pNeXnT4yKuonolSiyADxuij-m0IaqNG9Mmp5Yvv2wjg-P_jctnxc4Hp45aXcK648ryWMTUT8wN6H08WTkhvc4Jszg3tbTr5ULr0_DlLjbrBEMTnOe846neM9p9on5K-miakcck0wcvNh7giF5mGgt2699Oit3hgQJI8qtmncwNtWlUn2RqzEtPfdl2Ylr72wGiW2pO8qKuMmUyprwfGRQDdF9OTtOhhyUNknwRdSWu8yppVVQC4vPxQ1m9FqNLeIRrUz-e90v814rDL7r6qsjZPjtGiM9xQiDkN4ahAHqzqyvd-R6eWBNNo-wNFTZNSDGfJKQa9Eb8X_aUHhDznIN1xjPpPDYYBMJ88CUGMsREEjb_s7muakrqka287cgGo4U8rzolcITlLFjvHsTkUAG7hkmV4QMwsluX362vnKDaGJTq3BAAXa4HxnW94v1C2PQXC5bayn9FGbaMtfkBgOss1Dp1vCQXK5NguSVxJlmQAQcZGaovlfoTlZCUTvjfHFoXj1AaXlrI5VUqrGATOvFX-2eTbs_sPlLY7sqbcvTymGFfrrxG6dedxkDikGt4zv67WhM61__cgv2rAo_aC8BivRFOfJww4zaUNsfIlDOq8q_Shj4Z8KeUsmJGmUU60Qrvn1BsmBLJ3SIJ6BrMbUVpovqam2LkjB4nS5j-lMQQc8_pPsK2_7n0uH4iXh5YcQZuEQkYi59XqBbR-KN72_HEsG1fmZiitYEOAkxVBD23nSXQvVE0Rzz2pCAi7O4vkF_bzwC7G7EfsIEo0klnSp4Cqo2UuDk0l6bxaH7Efg38BKAkPYQnu0hw2LCZdHl4Cw-hZepH8NOrrqcOrKE6AmY\"}','2025-07-18 16:32:04.124975','14833382-84f9-4e8a-89bc-40b2f4628a4e','refresh_token','B6013EFA4101D04A04A87A36CAD79744',NULL,NULL,14),
('508D6ABC1BCCFEE4D81633C1F1F09B22D47452855B9DEAD861DD45419D9DD804','admin_ui','2025-07-17 16:32:28.806824','{\"PersistentGrantDataContainerVersion\":1,\"DataProtected\":true,\"Payload\":\"CfDJ8LwnNV2MojdDtqdrfmq7UrNnDIg5oriHDW7vFPKbZRzf7V0MV5WhE8xvbNRaC2vAhIadjHymMz460jkZOXebGIlXM8NsMbIKfpKUmmbmuYBQQ92gqEQuAu_uHYeO-YyedBPVvxS172cpPIPoTeqJAp6tNFYrHuQb2dWwABz4r2HGKiEogULSpDiyPq_vSvGluJEHGJdOW2vIMjMowsZQ5ltwIuE1ygSoPc_Ia8GXzZnq6SwnSpm5T7N_ownA-3X6FSOd8swgNm8XIUdHT5H93o8qTGXlQ0-2dc0hP2hhjWUgjvEltaP9uZrXrTsLXOmBUhK0oqp9u1Ig7YgdSbYLVvrwRPJBy8D7R37_iSUuJjryNYVLt2yAQwOuI0bo9d6zWcGzsDwS0ScxPgCRe9s5YVnGPfn4bhEGM79TTw4oq5e6N2THW3z9qPGo5Wd6VsLzd2QLcmn1jO20j1J6gbJlFZ_NBOOd3aghufiDvtPcuYArFS7V081fseXQ71hamPYRvtc01ZPAcWS074guy6Udgvj8vJgu_JM8TdgLaJhxeM6Cnr9pH2UcFuO_BFW_rq0hzW6eTVArW4dzhLXEIAqoo-tL_1v-GsyeL2IS6tUCBXy55_pH3cd2QCpV8F0XhIwYcPygtYV3lMT561YUlX3kplHNaBfsXd97m0FHswvbz5dD9woGQWpv9Fjuw2Em6ejUi-zEEQfyzmRHtbq-ocB4JMb2aUHtwvyx2ynJqnLgPZjr75c_5F8eff-1Jhp_Sk4qAZW7QKyXWa00Qz31FnciC5LF_Mw-5lHTAMa3K0KKow7viivZTgeDnGnm4Poqm95aidizlHSNwVbuV8Es3xwH9TmkyibzuxmTa7NJEKmRVfpLYzbskPZ8PjG-U7PJjjXwA0hvPM1sOWyZ4ccqXp4BLG0cRUWtRqPuUIUawjXdS4Ra9EB7uY-XqDh6DyhPVyJsKtwQII7EIWh4BK5ewLUpT2O7DrpsAUUeHVANYFD5BBxJtmHFjzKhADIaM5-BBKKtrYzWW5vnKiowrF6KHDf3mBsHX7GPfbmKNHsbzNSDGNQLZLiNf7Cl2OwCjb0uiWJ5QOAPcYCv8jMwj4kE9-G-axRfL43cC2yEWUfhbe919oj4EcLcMH7Y2USwvbgTHpLs1CcVz6rdPcw-g8bJ_3lus2fHopeJ-8OSRIq79Z8UFM9GB24_GngI8jWsajIDWusStob7vOOsdNwNRpTZjn8Rb6wCq8EcLneHpjrYRzlu8oCuSB_u7PGk8LiTUFqoA0nG4lkAHivwHQZcbphM3i5r5AeaXDOv-11olViJusqY1gzO55yzsVNauvObPdoNzkA6juvGSrmuwlp9h7jArLnq9tcYsc0gmmzB2r9E2toG21YipRJK9-QzZsk9RYzQkdUdlIz9B4e_rfU0w8E_mxbYagVcULf2jUezptaz6PZtOvS-xVmu1tjmU-NIkDbyZyTx9t47iq85zPmeGnMQ8nX6MSDYeV-G4H0xMMVDTNoWxoIi5YFjX8ftEHfMbATKn3-Ni0AImHT3RXY4xwbSUWWs3RqHJL-sOcHvG8e0zOvmK6UO5m9thVZAU1u3EE0_pEBnW-b-i7cqNXsVBwCckMgPvW7cUW_vlWM09ZsGJOEDzZWPkpjuXHqy_AZUJXs6GmXLVSAVIc_p0sqo7f1qE4uQlc6RwQpfagxdOoMNn8uM3WrTFh_kar8EqW1T-qzvypZtlnFbLpGAj7SsXrxE3pJ-AZE55hCMQdZo-4pd9HneXZ4JzdXOh1uuG5JoNAXa-kexmFmpLX4h-7cNyDSZUCcw_AwZWhqyRJ8dAMN3TO4Nyv0r_GyTdk8bPZ23RjkjXrS4NE_XSZZdlGRF-9sZ8pM3jkjPAacr3hh7f5svbPztN7aZIsWthh-kyTzAWSAUUUlru7IFb0ASiORQ2-o5PG25zLXvh5d4vpApX7nGlp2lDSVFQHwi241BvnDwofw6oTfwjLchhEsCJPp-0n8n49kG4QYZjYWq7btNqqIsvHIHpVd1qYx6Rql7HU0loy3Sh4JgQU64ILBW9k-8M_grHX34Otu77jkjMPInYZavnVgE1Wl5YPTsTCRSkAUyUkMl5-C10ZfX_cGNyCtcFzWkQl-ShoCSekO92x_M4cUtfl9DfM25e10uROFQw4og2RkIroL_tpvX-sQB-B_WnQVAPIBp0u7G1nJpOuQ1-Kw8yahm561NnwnlpfTJhVW8D2CigH_CandvulbL60tKEop4LKWV-glVLehz4WWoct-HFvuRfZ5X5hQyHwd-t4rCYQZl59dQ1g\"}','2025-07-18 16:32:28.806824','9e7a274a-487d-406b-9f00-2e462779db40','refresh_token','8CCA004A2AD629DB0DE6B671FB5E1DD8',NULL,NULL,16);
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
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
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

-- Dump completed on 2025-07-17 17:41:17
