/*M!999999\- enable the sandbox mode */ 
-- MariaDB dump 10.19-11.7.2-MariaDB, for osx10.20 (arm64)
--
-- Host: localhost    Database: identity-dev-new
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
(1,'API backing the Admin UI Product','Admin API',0x01,'admin_api','2025-07-16 10:04:27.013370',NULL,1,NULL,'',0x01,0),
(2,'Access to the Webhooks for admin UI','Admin UI Webhooks',0x01,'admin_ui_webhooks','2025-07-16 10:04:27.037963',NULL,0,NULL,'',0x01,0),
(3,'','ACS API Access',0x01,'acs.api','2025-07-16 10:15:03.552070',NULL,0,'2025-07-16 10:16:02.109147','',0x01,0);
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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
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
(1,'Full access to API backing the Admin UI Product','Admin API',0x00,'admin_api',0x01,0x00,0x01,'2025-07-16 10:04:26.995585',NULL,0,NULL),
(2,'Read only access to API backing the Admin UI Product','Admin API Read Only',0x00,'admin_api_readonly',0x01,0x00,0x01,'2025-07-16 10:04:27.012901',NULL,0,NULL),
(3,'Access to the Webhooks for admin UI','Admin UI Webhooks',0x00,'admin_ui_webhooks',0x00,0x00,0x01,'2025-07-16 10:04:27.037783',NULL,0,NULL),
(4,'ACS API Access - Full Access','ACS API Access',0x00,'acs.api',0x00,0x01,0x01,'2025-07-16 10:15:03.551123',NULL,0,NULL),
(5,'ACS API Read Scope','ACS API Read Scope',0x00,'acs.api.read',0x00,0x01,0x01,'2025-07-16 10:15:43.373199',NULL,0,NULL),
(6,'ACS API Write Scope','ACS API Write Scope',0x00,'acs.api.write',0x00,0x01,0x01,'2025-07-16 10:16:02.106004',NULL,0,NULL);
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
('351eadcb-e785-4198-93db-790caca0485f',NULL,NULL,'locale','LOCALE',0x00,0x01,NULL,NULL,0x00,0,'Locale'),
('3c184096-efd9-46c5-a65e-2984c0d05dae',NULL,NULL,'name','NAME',0x00,0x01,NULL,NULL,0x00,0,'Name'),
('409dacb0-98d6-44bc-b2cf-eda2c6b4c554',NULL,NULL,'preferred_username','PREFERRED_USERNAME',0x00,0x01,NULL,NULL,0x00,0,'Preferred Username'),
('4c8b6d77-521e-41a4-88f0-8584370e7b60',NULL,NULL,'middle_name','MIDDLE_NAME',0x00,0x01,NULL,NULL,0x00,0,'Middle Name'),
('50782d57-a631-4f3d-b858-d53ca86d6065',NULL,NULL,'zoneinfo','ZONEINFO',0x00,0x01,NULL,NULL,0x00,0,'Zone Info'),
('700cabf4-677e-45ba-94ed-a8c84d9be28c',NULL,NULL,'website','WEBSITE',0x00,0x01,NULL,NULL,0x00,0,'Website'),
('78b1baab-b536-40bc-b511-1426cf4ec90b',NULL,NULL,'phone_number','PHONE_NUMBER',0x00,0x01,NULL,NULL,0x00,0,'Phone Number'),
('9ca37a6b-92f2-42a0-bc7f-9a555a5baa2e',NULL,NULL,'picture','PICTURE',0x00,0x01,NULL,NULL,0x00,0,'Picture'),
('9de8056d-507e-4053-935c-780884e45b9b',NULL,NULL,'email','EMAIL',0x00,0x01,NULL,NULL,0x00,0,'Email'),
('a7b74150-f355-412e-9076-0e839574277f',NULL,NULL,'phone_number_verified','PHONE_NUMBER_VERIFIED',0x00,0x01,NULL,NULL,0x00,3,'Phone Number Verified'),
('af1210cc-f72c-4ce0-9a82-14de0c2a4139',NULL,NULL,'profile','PROFILE',0x00,0x01,NULL,NULL,0x00,0,'Profile'),
('c94531bc-7d60-4b14-9d35-d25bb2992c79',NULL,NULL,'gender','GENDER',0x00,0x01,NULL,NULL,0x00,0,'Gender'),
('c9849579-eef8-436e-9ac2-004087b472c9',NULL,NULL,'nickname','NICKNAME',0x00,0x01,NULL,NULL,0x00,0,'Nickname'),
('d24a6ba9-8d5c-4fd6-b6da-10bb388236a1',NULL,NULL,'email_verified','EMAIL_VERIFIED',0x00,0x01,NULL,NULL,0x00,3,'Email Verified'),
('d7cee55a-0b30-4d2d-a078-acefebf39e3e',NULL,NULL,'birthdate','BIRTHDATE',0x00,0x01,NULL,NULL,0x00,2,'Birthdate'),
('dd53d8fb-5c3a-432f-8d74-65ad8401ab87',NULL,NULL,'given_name','GIVEN_NAME',0x00,0x01,NULL,NULL,0x00,0,'Given Name'),
('e17617c8-977f-46a0-a1d7-85be6dc4881c',NULL,NULL,'role','ROLE',0x00,0x01,NULL,NULL,0x00,0,'Role'),
('e6ee338f-b2ef-4272-bfc2-557690159b8f',NULL,NULL,'sub','SUB',0x00,0x01,NULL,NULL,0x00,0,'Subject'),
('fc1b4759-62ec-4c02-80de-315c9e9ea679',NULL,NULL,'family_name','FAMILY_NAME',0x00,0x01,NULL,NULL,0x00,0,'Family Name');
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
('7e071583-f7bd-44e8-b3e2-4fbcb2b8bb84','5d2c1b5d-997b-4aa2-8562-d318e03360eb','ACS Stats Writer','ACS.Stats.Writer','ACS.STATS.WRITER',0x00),
('ca6dc324-71a3-488e-af56-d54d10d860a5','908f7735-f38a-4bf8-891b-f2aafd82fc33','ACS Stats Administrator','ACS.Stats.Admin','ACS.STATS.ADMIN',0x00),
('df0bf8c2-6406-48a2-b3ec-ace315657563','9573b69d-c2b3-47dc-adec-3cfce5ef637b','Administrator for AdminUI','AdminUI Administrator','ADMINUI ADMINISTRATOR',0x01),
('ee8db228-191d-4814-aa94-65769fe5cba3','3ee570be-e0a2-4c02-8839-4db7b84efc6b','ACS Stats Reader','ACS.Stats.Reader','ACS.STATS.READER',0x00);
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
('1b033686-202b-4970-9002-70cb3f1593af','7e071583-f7bd-44e8-b3e2-4fbcb2b8bb84'),
('1b033686-202b-4970-9002-70cb3f1593af','ca6dc324-71a3-488e-af56-d54d10d860a5'),
('1b033686-202b-4970-9002-70cb3f1593af','df0bf8c2-6406-48a2-b3ec-ace315657563'),
('1b033686-202b-4970-9002-70cb3f1593af','ee8db228-191d-4814-aa94-65769fe5cba3');
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
('1b033686-202b-4970-9002-70cb3f1593af',0,'a06ae2fb-c471-4afb-9975-4ea4cee34031','kevin@knowledgespike.com',0x01,'Kevin',0x00,0x00,'Jones',0x01,NULL,'KEVIN@KNOWLEDGESPIKE.COM','KEVIN@KNOWLEDGESPIKE.COM','AQAAAAIAAYagAAAAECQ/+30OH2IXHcsc3uOl53gD8dDak6CUb2spkBiAK1cOfMAhPnuU8OcvQb+U7DUZ1A==',NULL,0x00,'AHARIH5VK2HLSBXARVW2IWD4W2MRTDSN',0x00,'kevin@knowledgespike.com','KEVIN','JONES');
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
) ENGINE=InnoDB AUTO_INCREMENT=74 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `AuditEntries`
--

LOCK TABLES `AuditEntries` WRITE;
/*!40000 ALTER TABLE `AuditEntries` DISABLE KEYS */;
INSERT INTO `AuditEntries` VALUES
(1,'2025-07-16 10:05:06.689956','AdminUI','User','166ae405-1a94-4849-ad2a-55af06e62335','info@rocksolidknowledge.com','Get ClaimType(s)','ClaimType','*','*',1,'info@rocksolidknowledge.com viewed ClaimType(s)','INFO@ROCKSOLIDKNOWLEDGE.COM','GET CLAIMTYPE(S)','*','ADMINUI'),
(2,'2025-07-16 10:05:06.689960','AdminUI','User','166ae405-1a94-4849-ad2a-55af06e62335','info@rocksolidknowledge.com','Get User(s)','User','*','*',1,'info@rocksolidknowledge.com viewed User(s)','INFO@ROCKSOLIDKNOWLEDGE.COM','GET USER(S)','*','ADMINUI'),
(3,'2025-07-16 10:05:27.719136','AdminUI','User','166ae405-1a94-4849-ad2a-55af06e62335','info@rocksolidknowledge.com','Create User','User','kevin@knowledgespike.com','1b033686-202b-4970-9002-70cb3f1593af',1,'info@rocksolidknowledge.com created User (kevin@knowledgespike.com) (Username: kevin@knowledgespike.com, Email Address: kevin@knowledgespike.com, First Name: Kevin, Last Name: Jones)','INFO@ROCKSOLIDKNOWLEDGE.COM','CREATE USER','KEVIN@KNOWLEDGESPIKE.COM','ADMINUI'),
(4,'2025-07-16 10:05:27.759024','AdminUI','User','166ae405-1a94-4849-ad2a-55af06e62335','info@rocksolidknowledge.com','Get User(s)','User','*','*',1,'info@rocksolidknowledge.com viewed User(s)','INFO@ROCKSOLIDKNOWLEDGE.COM','GET USER(S)','*','ADMINUI'),
(5,'2025-07-16 10:05:27.759219','AdminUI','User','166ae405-1a94-4849-ad2a-55af06e62335','info@rocksolidknowledge.com','Get User(s)','User','*','*',1,'info@rocksolidknowledge.com viewed User(s)','INFO@ROCKSOLIDKNOWLEDGE.COM','GET USER(S)','*','ADMINUI'),
(6,'2025-07-16 10:05:36.403989','AdminUI','User','166ae405-1a94-4849-ad2a-55af06e62335','info@rocksolidknowledge.com','Get User By Subject','User','kevin@knowledgespike.com','1b033686-202b-4970-9002-70cb3f1593af',1,'info@rocksolidknowledge.com viewed User (kevin@knowledgespike.com)','INFO@ROCKSOLIDKNOWLEDGE.COM','GET USER BY SUBJECT','KEVIN@KNOWLEDGESPIKE.COM','ADMINUI'),
(7,'2025-07-16 10:05:36.452109','AdminUI','User','166ae405-1a94-4849-ad2a-55af06e62335','info@rocksolidknowledge.com','Get ClaimType(s)','ClaimType','*','*',1,'info@rocksolidknowledge.com viewed ClaimType(s)','INFO@ROCKSOLIDKNOWLEDGE.COM','GET CLAIMTYPE(S)','*','ADMINUI'),
(8,'2025-07-16 10:05:38.126754','AdminUI','User','166ae405-1a94-4849-ad2a-55af06e62335','info@rocksolidknowledge.com','Get Role','Role','*','*',1,'info@rocksolidknowledge.com viewed Role(s)','INFO@ROCKSOLIDKNOWLEDGE.COM','GET ROLE','*','ADMINUI'),
(9,'2025-07-16 10:05:40.796053','AdminUI','User','166ae405-1a94-4849-ad2a-55af06e62335','info@rocksolidknowledge.com','Assign Role(s) to User','User','kevin@knowledgespike.com','1b033686-202b-4970-9002-70cb3f1593af',1,'info@rocksolidknowledge.com assigned role(s) (AdminUI Administrator) to User (kevin@knowledgespike.com)','INFO@ROCKSOLIDKNOWLEDGE.COM','ASSIGN ROLE(S) TO USER','KEVIN@KNOWLEDGESPIKE.COM','ADMINUI'),
(10,'2025-07-16 10:09:22.997258','AdminUI','User','166ae405-1a94-4849-ad2a-55af06e62335','info@rocksolidknowledge.com','Get User By Subject','User','kevin@knowledgespike.com','1b033686-202b-4970-9002-70cb3f1593af',1,'info@rocksolidknowledge.com viewed User (kevin@knowledgespike.com)','INFO@ROCKSOLIDKNOWLEDGE.COM','GET USER BY SUBJECT','KEVIN@KNOWLEDGESPIKE.COM','ADMINUI'),
(11,'2025-07-16 10:09:23.012822','AdminUI','User','166ae405-1a94-4849-ad2a-55af06e62335','info@rocksolidknowledge.com','Get Role','Role','*','*',1,'info@rocksolidknowledge.com viewed Role(s)','INFO@ROCKSOLIDKNOWLEDGE.COM','GET ROLE','*','ADMINUI'),
(12,'2025-07-16 10:09:29.943937','AdminUI','User','166ae405-1a94-4849-ad2a-55af06e62335','info@rocksolidknowledge.com','Get ClaimType(s)','ClaimType','*','*',1,'info@rocksolidknowledge.com viewed ClaimType(s)','INFO@ROCKSOLIDKNOWLEDGE.COM','GET CLAIMTYPE(S)','*','ADMINUI'),
(13,'2025-07-16 10:12:29.317418','AdminUI','User','166ae405-1a94-4849-ad2a-55af06e62335','info@rocksolidknowledge.com','Get User(s)','User','*','*',1,'info@rocksolidknowledge.com viewed User(s)','INFO@ROCKSOLIDKNOWLEDGE.COM','GET USER(S)','*','ADMINUI'),
(14,'2025-07-16 10:12:29.321270','AdminUI','User','166ae405-1a94-4849-ad2a-55af06e62335','info@rocksolidknowledge.com','Get ClaimType(s)','ClaimType','*','*',1,'info@rocksolidknowledge.com viewed ClaimType(s)','INFO@ROCKSOLIDKNOWLEDGE.COM','GET CLAIMTYPE(S)','*','ADMINUI'),
(15,'2025-07-16 10:12:31.235227','AdminUI','User','166ae405-1a94-4849-ad2a-55af06e62335','info@rocksolidknowledge.com','Get User By Subject','User','kevin@knowledgespike.com','1b033686-202b-4970-9002-70cb3f1593af',1,'info@rocksolidknowledge.com viewed User (kevin@knowledgespike.com)','INFO@ROCKSOLIDKNOWLEDGE.COM','GET USER BY SUBJECT','KEVIN@KNOWLEDGESPIKE.COM','ADMINUI'),
(16,'2025-07-16 10:12:31.279849','AdminUI','User','166ae405-1a94-4849-ad2a-55af06e62335','info@rocksolidknowledge.com','Get ClaimType(s)','ClaimType','*','*',1,'info@rocksolidknowledge.com viewed ClaimType(s)','INFO@ROCKSOLIDKNOWLEDGE.COM','GET CLAIMTYPE(S)','*','ADMINUI'),
(17,'2025-07-16 10:12:35.395081','AdminUI','User','166ae405-1a94-4849-ad2a-55af06e62335','info@rocksolidknowledge.com','Get ClaimType(s)','ClaimType','*','*',1,'info@rocksolidknowledge.com viewed ClaimType(s)','INFO@ROCKSOLIDKNOWLEDGE.COM','GET CLAIMTYPE(S)','*','ADMINUI'),
(18,'2025-07-16 10:12:40.127952','AdminUI','User','166ae405-1a94-4849-ad2a-55af06e62335','info@rocksolidknowledge.com','Update User Claim','User','kevin@knowledgespike.com','1b033686-202b-4970-9002-70cb3f1593af',1,'info@rocksolidknowledge.com edited claim for User (kevin@knowledgespike.com), from type \'email_verified\' value \'false\' to type \'email_verified\' value \'true\' ','INFO@ROCKSOLIDKNOWLEDGE.COM','UPDATE USER CLAIM','KEVIN@KNOWLEDGESPIKE.COM','ADMINUI'),
(19,'2025-07-16 10:13:16.131588','AdminUI','User','1b033686-202b-4970-9002-70cb3f1593af','kevin@knowledgespike.com','Delete User','User','info@rocksolidknowledge.com','info@rocksolidknowledge.com',1,'kevin@knowledgespike.com deleted User (info@rocksolidknowledge.com)','KEVIN@KNOWLEDGESPIKE.COM','DELETE USER','INFO@ROCKSOLIDKNOWLEDGE.COM','ADMINUI'),
(20,'2025-07-16 10:13:20.190682','AdminUI','User','1b033686-202b-4970-9002-70cb3f1593af','kevin@knowledgespike.com','Get ClaimType(s)','ClaimType','*','*',1,'kevin@knowledgespike.com viewed ClaimType(s)','KEVIN@KNOWLEDGESPIKE.COM','GET CLAIMTYPE(S)','*','ADMINUI'),
(21,'2025-07-16 10:13:20.243920','AdminUI','User','1b033686-202b-4970-9002-70cb3f1593af','kevin@knowledgespike.com','Get Identity Resource(s)','IdentityResource','*','*',1,'kevin@knowledgespike.com viewed all Identity Resource(s)','KEVIN@KNOWLEDGESPIKE.COM','GET IDENTITY RESOURCE(S)','*','ADMINUI'),
(22,'2025-07-16 10:14:04.730396','AdminUI','User','1b033686-202b-4970-9002-70cb3f1593af','kevin@knowledgespike.com','Create Identity Resource','IdentityResource','ACS Identity Profile','7f63c20b-b1f0-4393-b3da-93377ee22f78',1,'kevin@knowledgespike.com created Identity Resource (ACS Identity Profile) (Name: acs.identity, Display Name: ACS Identity Profile, AllowedClaims: (email, given_name, name, role))','KEVIN@KNOWLEDGESPIKE.COM','CREATE IDENTITY RESOURCE','ACS IDENTITY PROFILE','ADMINUI'),
(23,'2025-07-16 10:14:04.742774','AdminUI','User','1b033686-202b-4970-9002-70cb3f1593af','kevin@knowledgespike.com','Get Identity Resource(s)','IdentityResource','*','*',1,'kevin@knowledgespike.com viewed all Identity Resource(s)','KEVIN@KNOWLEDGESPIKE.COM','GET IDENTITY RESOURCE(S)','*','ADMINUI'),
(24,'2025-07-16 10:14:16.628093','AdminUI','User','1b033686-202b-4970-9002-70cb3f1593af','kevin@knowledgespike.com','Get Identity Resource By Id','IdentityResource','ACS Identity Profile','7f63c20b-b1f0-4393-b3da-93377ee22f78',1,'kevin@knowledgespike.com viewed Identity Resource (ACS Identity Profile)','KEVIN@KNOWLEDGESPIKE.COM','GET IDENTITY RESOURCE BY ID','ACS IDENTITY PROFILE','ADMINUI'),
(25,'2025-07-16 10:14:16.648718','AdminUI','User','1b033686-202b-4970-9002-70cb3f1593af','kevin@knowledgespike.com','Get ClaimType(s)','ClaimType','*','*',1,'kevin@knowledgespike.com viewed ClaimType(s)','KEVIN@KNOWLEDGESPIKE.COM','GET CLAIMTYPE(S)','*','ADMINUI'),
(26,'2025-07-16 10:14:21.197742','AdminUI','User','1b033686-202b-4970-9002-70cb3f1593af','kevin@knowledgespike.com','Get User(s)','User','*','*',1,'kevin@knowledgespike.com viewed User(s)','KEVIN@KNOWLEDGESPIKE.COM','GET USER(S)','*','ADMINUI'),
(27,'2025-07-16 10:14:21.202441','AdminUI','User','1b033686-202b-4970-9002-70cb3f1593af','kevin@knowledgespike.com','Get ClaimType(s)','ClaimType','*','*',1,'kevin@knowledgespike.com viewed ClaimType(s)','KEVIN@KNOWLEDGESPIKE.COM','GET CLAIMTYPE(S)','*','ADMINUI'),
(28,'2025-07-16 10:14:22.965395','AdminUI','User','1b033686-202b-4970-9002-70cb3f1593af','kevin@knowledgespike.com','Get User By Subject','User','kevin@knowledgespike.com','1b033686-202b-4970-9002-70cb3f1593af',1,'kevin@knowledgespike.com viewed User (kevin@knowledgespike.com)','KEVIN@KNOWLEDGESPIKE.COM','GET USER BY SUBJECT','KEVIN@KNOWLEDGESPIKE.COM','ADMINUI'),
(29,'2025-07-16 10:14:23.005156','AdminUI','User','1b033686-202b-4970-9002-70cb3f1593af','kevin@knowledgespike.com','Get ClaimType(s)','ClaimType','*','*',1,'kevin@knowledgespike.com viewed ClaimType(s)','KEVIN@KNOWLEDGESPIKE.COM','GET CLAIMTYPE(S)','*','ADMINUI'),
(30,'2025-07-16 10:14:29.005418','AdminUI','User','1b033686-202b-4970-9002-70cb3f1593af','kevin@knowledgespike.com','Get ClaimType(s)','ClaimType','*','*',1,'kevin@knowledgespike.com viewed ClaimType(s)','KEVIN@KNOWLEDGESPIKE.COM','GET CLAIMTYPE(S)','*','ADMINUI'),
(31,'2025-07-16 10:14:29.085193','AdminUI','User','1b033686-202b-4970-9002-70cb3f1593af','kevin@knowledgespike.com','Get Protected Resource(s)','ProtectedResource','*','*',1,'kevin@knowledgespike.com viewed all Protected Resource(s)','KEVIN@KNOWLEDGESPIKE.COM','GET PROTECTED RESOURCE(S)','*','ADMINUI'),
(32,'2025-07-16 10:15:03.591433','AdminUI','User','1b033686-202b-4970-9002-70cb3f1593af','kevin@knowledgespike.com','Create Protected Resource','ProtectedResource','ACS API Access','dd5b05bd-4d6b-4345-92fd-020687bc9b0d',1,'kevin@knowledgespike.com created Protected Resource (ACS API Access) (Name: acs.api, Display Name: ACS API Access, AllowedClaims: (role))','KEVIN@KNOWLEDGESPIKE.COM','CREATE PROTECTED RESOURCE','ACS API ACCESS','ADMINUI'),
(33,'2025-07-16 10:15:03.601849','AdminUI','User','1b033686-202b-4970-9002-70cb3f1593af','kevin@knowledgespike.com','Get Protected Resource(s)','ProtectedResource','*','*',1,'kevin@knowledgespike.com viewed all Protected Resource(s)','KEVIN@KNOWLEDGESPIKE.COM','GET PROTECTED RESOURCE(S)','*','ADMINUI'),
(34,'2025-07-16 10:15:05.599071','AdminUI','User','1b033686-202b-4970-9002-70cb3f1593af','kevin@knowledgespike.com','Get Protected Resource By Id','ProtectedResource','ACS API Access','dd5b05bd-4d6b-4345-92fd-020687bc9b0d',1,'kevin@knowledgespike.com viewed Protected Resource (ACS API Access)','KEVIN@KNOWLEDGESPIKE.COM','GET PROTECTED RESOURCE BY ID','ACS API ACCESS','ADMINUI'),
(35,'2025-07-16 10:15:05.613131','AdminUI','User','1b033686-202b-4970-9002-70cb3f1593af','kevin@knowledgespike.com','Get ClaimType(s)','ClaimType','*','*',1,'kevin@knowledgespike.com viewed ClaimType(s)','KEVIN@KNOWLEDGESPIKE.COM','GET CLAIMTYPE(S)','*','ADMINUI'),
(36,'2025-07-16 10:15:08.778165','AdminUI','User','1b033686-202b-4970-9002-70cb3f1593af','kevin@knowledgespike.com','Get All Scopes','Scope','*','*',1,'kevin@knowledgespike.com viewed all Protected Resource(s)','KEVIN@KNOWLEDGESPIKE.COM','GET ALL SCOPES','*','ADMINUI'),
(37,'2025-07-16 10:15:43.391206','AdminUI','User','1b033686-202b-4970-9002-70cb3f1593af','kevin@knowledgespike.com','Add Scope to Protected Resource','ProtectedResource','ACS API Access','dd5b05bd-4d6b-4345-92fd-020687bc9b0d',1,'kevin@knowledgespike.com added Scope to Protected Resource (acs.api) (Name: acs.api.read, Display Name: ACS API Read Scope, Description: ACS API Read Scope, Required: False, Emphasize: False, Show In Discovery Document: True, IsFinalInstanceOnResource: False, UserClaims: (role))','KEVIN@KNOWLEDGESPIKE.COM','ADD SCOPE TO PROTECTED RESOURCE','ACS API ACCESS','ADMINUI'),
(38,'2025-07-16 10:15:43.401384','AdminUI','User','1b033686-202b-4970-9002-70cb3f1593af','kevin@knowledgespike.com','Get Protected Resource By Id','ProtectedResource','ACS API Access','dd5b05bd-4d6b-4345-92fd-020687bc9b0d',1,'kevin@knowledgespike.com viewed Protected Resource (ACS API Access)','KEVIN@KNOWLEDGESPIKE.COM','GET PROTECTED RESOURCE BY ID','ACS API ACCESS','ADMINUI'),
(39,'2025-07-16 10:15:43.417366','AdminUI','User','1b033686-202b-4970-9002-70cb3f1593af','kevin@knowledgespike.com','Get All Scopes','Scope','*','*',1,'kevin@knowledgespike.com viewed all Protected Resource(s)','KEVIN@KNOWLEDGESPIKE.COM','GET ALL SCOPES','*','ADMINUI'),
(40,'2025-07-16 10:16:02.116233','AdminUI','User','1b033686-202b-4970-9002-70cb3f1593af','kevin@knowledgespike.com','Add Scope to Protected Resource','ProtectedResource','ACS API Access','dd5b05bd-4d6b-4345-92fd-020687bc9b0d',1,'kevin@knowledgespike.com added Scope to Protected Resource (acs.api) (Name: acs.api.write, Display Name: ACS API Write Scope, Description: ACS API Write Scope, Required: False, Emphasize: False, Show In Discovery Document: True, IsFinalInstanceOnResource: False, UserClaims: (role))','KEVIN@KNOWLEDGESPIKE.COM','ADD SCOPE TO PROTECTED RESOURCE','ACS API ACCESS','ADMINUI'),
(41,'2025-07-16 10:16:02.129102','AdminUI','User','1b033686-202b-4970-9002-70cb3f1593af','kevin@knowledgespike.com','Get Protected Resource By Id','ProtectedResource','ACS API Access','dd5b05bd-4d6b-4345-92fd-020687bc9b0d',1,'kevin@knowledgespike.com viewed Protected Resource (ACS API Access)','KEVIN@KNOWLEDGESPIKE.COM','GET PROTECTED RESOURCE BY ID','ACS API ACCESS','ADMINUI'),
(42,'2025-07-16 10:16:02.146660','AdminUI','User','1b033686-202b-4970-9002-70cb3f1593af','kevin@knowledgespike.com','Get All Scopes','Scope','*','*',1,'kevin@knowledgespike.com viewed all Protected Resource(s)','KEVIN@KNOWLEDGESPIKE.COM','GET ALL SCOPES','*','ADMINUI'),
(43,'2025-07-16 10:16:31.806575','AdminUI','User','1b033686-202b-4970-9002-70cb3f1593af','kevin@knowledgespike.com','Update Scope for Protected Resource','ProtectedResource','ACS API Access','dd5b05bd-4d6b-4345-92fd-020687bc9b0d',1,'kevin@knowledgespike.com updated Scope for Protected Resource (ACS API Access) (IsFinalInstanceOnResource: True => False, UserClaims: (role) => ())','KEVIN@KNOWLEDGESPIKE.COM','UPDATE SCOPE FOR PROTECTED RESOURCE','ACS API ACCESS','ADMINUI'),
(44,'2025-07-16 10:16:31.817725','AdminUI','User','1b033686-202b-4970-9002-70cb3f1593af','kevin@knowledgespike.com','Get Protected Resource By Id','ProtectedResource','ACS API Access','dd5b05bd-4d6b-4345-92fd-020687bc9b0d',1,'kevin@knowledgespike.com viewed Protected Resource (ACS API Access)','KEVIN@KNOWLEDGESPIKE.COM','GET PROTECTED RESOURCE BY ID','ACS API ACCESS','ADMINUI'),
(45,'2025-07-16 10:16:31.822703','AdminUI','User','1b033686-202b-4970-9002-70cb3f1593af','kevin@knowledgespike.com','Get All Scopes','Scope','*','*',1,'kevin@knowledgespike.com viewed all Protected Resource(s)','KEVIN@KNOWLEDGESPIKE.COM','GET ALL SCOPES','*','ADMINUI'),
(46,'2025-07-16 10:16:37.485891','AdminUI','User','1b033686-202b-4970-9002-70cb3f1593af','kevin@knowledgespike.com','Update Scope for Protected Resource','ProtectedResource','ACS API Access','dd5b05bd-4d6b-4345-92fd-020687bc9b0d',1,'kevin@knowledgespike.com updated Scope for Protected Resource (ACS API Access) (IsFinalInstanceOnResource: True => False, UserClaims: (role) => ())','KEVIN@KNOWLEDGESPIKE.COM','UPDATE SCOPE FOR PROTECTED RESOURCE','ACS API ACCESS','ADMINUI'),
(47,'2025-07-16 10:16:37.497129','AdminUI','User','1b033686-202b-4970-9002-70cb3f1593af','kevin@knowledgespike.com','Get Protected Resource By Id','ProtectedResource','ACS API Access','dd5b05bd-4d6b-4345-92fd-020687bc9b0d',1,'kevin@knowledgespike.com viewed Protected Resource (ACS API Access)','KEVIN@KNOWLEDGESPIKE.COM','GET PROTECTED RESOURCE BY ID','ACS API ACCESS','ADMINUI'),
(48,'2025-07-16 10:16:37.502892','AdminUI','User','1b033686-202b-4970-9002-70cb3f1593af','kevin@knowledgespike.com','Get All Scopes','Scope','*','*',1,'kevin@knowledgespike.com viewed all Protected Resource(s)','KEVIN@KNOWLEDGESPIKE.COM','GET ALL SCOPES','*','ADMINUI'),
(49,'2025-07-16 10:16:41.805115','AdminUI','User','1b033686-202b-4970-9002-70cb3f1593af','kevin@knowledgespike.com','Get User(s)','User','*','*',1,'kevin@knowledgespike.com viewed User(s)','KEVIN@KNOWLEDGESPIKE.COM','GET USER(S)','*','ADMINUI'),
(50,'2025-07-16 10:16:41.806234','AdminUI','User','1b033686-202b-4970-9002-70cb3f1593af','kevin@knowledgespike.com','Get ClaimType(s)','ClaimType','*','*',1,'kevin@knowledgespike.com viewed ClaimType(s)','KEVIN@KNOWLEDGESPIKE.COM','GET CLAIMTYPE(S)','*','ADMINUI'),
(51,'2025-07-16 10:16:43.629777','AdminUI','User','1b033686-202b-4970-9002-70cb3f1593af','kevin@knowledgespike.com','Get User By Subject','User','kevin@knowledgespike.com','1b033686-202b-4970-9002-70cb3f1593af',1,'kevin@knowledgespike.com viewed User (kevin@knowledgespike.com)','KEVIN@KNOWLEDGESPIKE.COM','GET USER BY SUBJECT','KEVIN@KNOWLEDGESPIKE.COM','ADMINUI'),
(52,'2025-07-16 10:16:43.663078','AdminUI','User','1b033686-202b-4970-9002-70cb3f1593af','kevin@knowledgespike.com','Get ClaimType(s)','ClaimType','*','*',1,'kevin@knowledgespike.com viewed ClaimType(s)','KEVIN@KNOWLEDGESPIKE.COM','GET CLAIMTYPE(S)','*','ADMINUI'),
(53,'2025-07-16 10:16:45.080065','AdminUI','User','1b033686-202b-4970-9002-70cb3f1593af','kevin@knowledgespike.com','Get Role','Role','*','*',1,'kevin@knowledgespike.com viewed Role(s)','KEVIN@KNOWLEDGESPIKE.COM','GET ROLE','*','ADMINUI'),
(54,'2025-07-16 10:16:59.929020','AdminUI','User','1b033686-202b-4970-9002-70cb3f1593af','kevin@knowledgespike.com','Get Role','Role','*','*',1,'kevin@knowledgespike.com viewed Role(s)','KEVIN@KNOWLEDGESPIKE.COM','GET ROLE','*','ADMINUI'),
(55,'2025-07-16 10:17:30.165758','AdminUI','User','1b033686-202b-4970-9002-70cb3f1593af','kevin@knowledgespike.com','Create Role','Role','ACS.Stats.Admin','ca6dc324-71a3-488e-af56-d54d10d860a5',1,'kevin@knowledgespike.com Successfully Created Role (ACS.Stats.Admin)','KEVIN@KNOWLEDGESPIKE.COM','CREATE ROLE','ACS.STATS.ADMIN','ADMINUI'),
(56,'2025-07-16 10:17:30.175935','AdminUI','User','1b033686-202b-4970-9002-70cb3f1593af','kevin@knowledgespike.com','Get Role','Role','*','*',1,'kevin@knowledgespike.com viewed Role(s)','KEVIN@KNOWLEDGESPIKE.COM','GET ROLE','*','ADMINUI'),
(57,'2025-07-16 10:17:56.033519','AdminUI','User','1b033686-202b-4970-9002-70cb3f1593af','kevin@knowledgespike.com','Create Role','Role','ACS.Stats.Reader','ee8db228-191d-4814-aa94-65769fe5cba3',1,'kevin@knowledgespike.com Successfully Created Role (ACS.Stats.Reader)','KEVIN@KNOWLEDGESPIKE.COM','CREATE ROLE','ACS.STATS.READER','ADMINUI'),
(58,'2025-07-16 10:17:56.047185','AdminUI','User','1b033686-202b-4970-9002-70cb3f1593af','kevin@knowledgespike.com','Get Role','Role','*','*',1,'kevin@knowledgespike.com viewed Role(s)','KEVIN@KNOWLEDGESPIKE.COM','GET ROLE','*','ADMINUI'),
(59,'2025-07-16 10:18:08.835317','AdminUI','User','1b033686-202b-4970-9002-70cb3f1593af','kevin@knowledgespike.com','Create Role','Role','ACS.Stats.Writer','7e071583-f7bd-44e8-b3e2-4fbcb2b8bb84',1,'kevin@knowledgespike.com Successfully Created Role (ACS.Stats.Writer)','KEVIN@KNOWLEDGESPIKE.COM','CREATE ROLE','ACS.STATS.WRITER','ADMINUI'),
(60,'2025-07-16 10:18:08.840549','AdminUI','User','1b033686-202b-4970-9002-70cb3f1593af','kevin@knowledgespike.com','Get Role','Role','*','*',1,'kevin@knowledgespike.com viewed Role(s)','KEVIN@KNOWLEDGESPIKE.COM','GET ROLE','*','ADMINUI'),
(61,'2025-07-16 10:18:13.287576','AdminUI','User','1b033686-202b-4970-9002-70cb3f1593af','kevin@knowledgespike.com','Get User(s)','User','*','*',1,'kevin@knowledgespike.com viewed User(s)','KEVIN@KNOWLEDGESPIKE.COM','GET USER(S)','*','ADMINUI'),
(62,'2025-07-16 10:18:13.294377','AdminUI','User','1b033686-202b-4970-9002-70cb3f1593af','kevin@knowledgespike.com','Get ClaimType(s)','ClaimType','*','*',1,'kevin@knowledgespike.com viewed ClaimType(s)','KEVIN@KNOWLEDGESPIKE.COM','GET CLAIMTYPE(S)','*','ADMINUI'),
(63,'2025-07-16 10:18:15.209849','AdminUI','User','1b033686-202b-4970-9002-70cb3f1593af','kevin@knowledgespike.com','Get User By Subject','User','kevin@knowledgespike.com','1b033686-202b-4970-9002-70cb3f1593af',1,'kevin@knowledgespike.com viewed User (kevin@knowledgespike.com)','KEVIN@KNOWLEDGESPIKE.COM','GET USER BY SUBJECT','KEVIN@KNOWLEDGESPIKE.COM','ADMINUI'),
(64,'2025-07-16 10:18:15.242968','AdminUI','User','1b033686-202b-4970-9002-70cb3f1593af','kevin@knowledgespike.com','Get ClaimType(s)','ClaimType','*','*',1,'kevin@knowledgespike.com viewed ClaimType(s)','KEVIN@KNOWLEDGESPIKE.COM','GET CLAIMTYPE(S)','*','ADMINUI'),
(65,'2025-07-16 10:18:16.637202','AdminUI','User','1b033686-202b-4970-9002-70cb3f1593af','kevin@knowledgespike.com','Get Role','Role','*','*',1,'kevin@knowledgespike.com viewed Role(s)','KEVIN@KNOWLEDGESPIKE.COM','GET ROLE','*','ADMINUI'),
(66,'2025-07-16 10:18:21.463061','AdminUI','User','1b033686-202b-4970-9002-70cb3f1593af','kevin@knowledgespike.com','Assign Role(s) to User','User','kevin@knowledgespike.com','1b033686-202b-4970-9002-70cb3f1593af',1,'kevin@knowledgespike.com assigned role(s) (ACS.Stats.Admin, ACS.Stats.Reader, ACS.Stats.Writer) to User (kevin@knowledgespike.com)','KEVIN@KNOWLEDGESPIKE.COM','ASSIGN ROLE(S) TO USER','KEVIN@KNOWLEDGESPIKE.COM','ADMINUI'),
(67,'2025-07-16 10:19:32.347878','AdminUI','User','1b033686-202b-4970-9002-70cb3f1593af','kevin@knowledgespike.com','Get Clients','Client','*','*',1,'kevin@knowledgespike.com viewed Client(s)','KEVIN@KNOWLEDGESPIKE.COM','GET CLIENTS','*','ADMINUI'),
(68,'2025-07-16 10:21:19.749220','AdminUI','User','1b033686-202b-4970-9002-70cb3f1593af','kevin@knowledgespike.com','Get Identity Resource(s)','IdentityResource','*','*',1,'kevin@knowledgespike.com viewed all Identity Resource(s)','KEVIN@KNOWLEDGESPIKE.COM','GET IDENTITY RESOURCE(S)','*','ADMINUI'),
(69,'2025-07-16 10:21:40.702586','AdminUI','User','1b033686-202b-4970-9002-70cb3f1593af','kevin@knowledgespike.com','Get Protected Resource(s)','ProtectedResource','*','*',1,'kevin@knowledgespike.com viewed all Protected Resource(s)','KEVIN@KNOWLEDGESPIKE.COM','GET PROTECTED RESOURCE(S)','*','ADMINUI'),
(70,'2025-07-16 10:22:01.947817','AdminUI','User','1b033686-202b-4970-9002-70cb3f1593af','kevin@knowledgespike.com','Create Client','Client','ACS Statistics Application','d611b0a2-cc82-4d29-9ba0-dce28b1be255',1,'kevin@knowledgespike.com created Client (ACS Statistics Application) (Client Type: ServerSidePKCE, Client ID: acsstats, Display Name: ACS Statistics Application, Require Consent: True, Redirect Uris: (https://cricket.knowledgespike.local/signin-oidc), Post Logout Redirect Uris: (https://cricket.knowledgespike.local/signout-callback-oidc), Allowed Scopes: (acs.identity, openid, profile, acs.api, acs.api.read, acs.api.write))','KEVIN@KNOWLEDGESPIKE.COM','CREATE CLIENT','ACS STATISTICS APPLICATION','ADMINUI'),
(71,'2025-07-16 10:22:01.961699','AdminUI','User','1b033686-202b-4970-9002-70cb3f1593af','kevin@knowledgespike.com','Get Clients','Client','*','*',1,'kevin@knowledgespike.com viewed Client(s)','KEVIN@KNOWLEDGESPIKE.COM','GET CLIENTS','*','ADMINUI'),
(72,'2025-07-16 10:23:27.930908','AdminUI','User','1b033686-202b-4970-9002-70cb3f1593af','kevin@knowledgespike.com','Get Client by Id','Client','ACS Statistics Application','d611b0a2-cc82-4d29-9ba0-dce28b1be255',1,'kevin@knowledgespike.com viewed Client (ACS Statistics Application)','KEVIN@KNOWLEDGESPIKE.COM','GET CLIENT BY ID','ACS STATISTICS APPLICATION','ADMINUI'),
(73,'2025-07-16 10:25:01.355407','AdminUI','User','1b033686-202b-4970-9002-70cb3f1593af','kevin@knowledgespike.com','Update Client','Client','ACS Statistics Application','d611b0a2-cc82-4d29-9ba0-dce28b1be255',1,'kevin@knowledgespike.com updated Client (ACS Statistics Application) (Display URL:  => https://cricket.knowledgespike.local, Logo URL:  => https://cricket.knowledgespike.local/KnowledgeSpikeLogo.png)','KEVIN@KNOWLEDGESPIKE.COM','UPDATE CLIENT','ACS STATISTICS APPLICATION','ADMINUI');
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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ClientGrantTypes`
--

LOCK TABLES `ClientGrantTypes` WRITE;
/*!40000 ALTER TABLE `ClientGrantTypes` DISABLE KEYS */;
INSERT INTO `ClientGrantTypes` VALUES
(1,1,'authorization_code'),
(3,2,'authorization_code');
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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ClientPostLogoutRedirectUris`
--

LOCK TABLES `ClientPostLogoutRedirectUris` WRITE;
/*!40000 ALTER TABLE `ClientPostLogoutRedirectUris` DISABLE KEYS */;
INSERT INTO `ClientPostLogoutRedirectUris` VALUES
(1,1,'https://ids.knowledgespike.local'),
(3,2,'https://cricket.knowledgespike.local/signout-callback-oidc');
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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ClientRedirectUris`
--

LOCK TABLES `ClientRedirectUris` WRITE;
/*!40000 ALTER TABLE `ClientRedirectUris` DISABLE KEYS */;
INSERT INTO `ClientRedirectUris` VALUES
(1,1,'https://ids.knowledgespike.local/signin-oidc'),
(3,2,'https://cricket.knowledgespike.local/signin-oidc');
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
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
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
(10,2,'acs.api'),
(11,2,'acs.api.read'),
(12,2,'acs.api.write'),
(13,2,'acs.identity'),
(14,2,'openid'),
(15,2,'profile');
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
(1,1,NULL,NULL,'SharedSecret','FOWZHtghkS3FQW/dl8rsqBv4+DqQh2K3IJtR22ELWkexzwv0bmySsH5DGkL5lfpVh4YyBac7WJ70/nl6pk4luw==','2025-07-16 10:04:27.103959'),
(2,2,'',NULL,'SharedSecret','rEupmALgn9v+ysqJNq9yFdAnsRle9GL97vrPVe0epR2x4UKueah2Rex3falXHCr8UyaWMUA9RLJU7S1P0+XscA==','2025-07-16 09:22:01.920362');
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
(1,86400,3600,0,0x00,0x01,0x00,0x01,0x00,0x00,300,0x01,NULL,NULL,'admin_ui','Admin UI',NULL,NULL,'IdentityExpress Admin UI',0x01,0x01,0x01,NULL,300,0x01,NULL,NULL,'oidc',1,0,0x01,0x00,0x01,1296000,0x00,'2025-07-16 10:04:27.099790',300,'0001-01-01 00:00:00.000000',1,'0001-01-01 00:00:00.000000',NULL,NULL,'',0x00,NULL,NULL,NULL,'00:05:00.000000',1,NULL,0,NULL,0),
(2,2592000,3600,0,0x00,0x00,0x00,0x01,0x00,0x00,300,0x01,NULL,NULL,'acsstats','ACS Statistics Application','https://cricket.knowledgespike.local',NULL,'',0x01,0x01,0x01,NULL,300,0x01,'https://cricket.knowledgespike.local/KnowledgeSpikeLogo.png',NULL,'oidc',1,0,0x01,0x01,0x01,1296000,0x00,'2025-07-16 10:22:01.920316',300,'0001-01-01 00:00:00.000000',0,'2025-07-16 10:25:01.344636',NULL,NULL,'',0x00,NULL,NULL,NULL,'00:05:00.000000',1,NULL,0,NULL,0);
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
('policy','{\"PolicyClaims\":[],\"Version\":\"07/16/2025 10:04:26\",\"ResourceType\":\"accessPolicy\",\"ResourceIdentifier\":\"07/16/2025 10:04:26\"}'),
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
('1898b341-ea75-4c0d-a233-b4ae2f1c18a1','admin_api','ADMIN_API',0x01),
('b1031679-f2bd-4107-8ed3-11001b627cbf','admin_ui_webhooks','ADMIN_UI_WEBHOOKS',0x00),
('dd5b05bd-4d6b-4345-92fd-020687bc9b0d','acs.api','ACS.API',0x00);
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
('d611b0a2-cc82-4d29-9ba0-dce28b1be255','acsstats','','ACSSTATS','ACS STATISTICS APPLICATION',0x00,9),
('e65801b4-a53d-44ef-9cb5-326bef657c71','admin_ui','IdentityExpress Admin UI','ADMIN_UI','ADMIN UI',0x01,0);
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
('3d6347c7-d981-4f32-9b0a-5dfac2318725','profile','PROFILE',0x01),
('51cc7f1e-ef45-42ba-94c8-73c8aa69b4d3','openid','OPENID',0x00),
('7e2043bf-b9b2-45a8-bc5f-2ac774212494','admin_ui_profile','ADMIN_UI_PROFILE',0x00),
('7f63c20b-b1f0-4393-b3da-93377ee22f78','acs.identity','ACS.IDENTITY',0x00);
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
(1,NULL,'Your user identifier',0x00,0x01,'openid',0x01,0x01,'2025-07-16 10:04:26.934611',1,NULL),
(2,'Your user profile information (first name, last name, etc.)','User profile',0x01,0x01,'profile',0x00,0x01,'2025-07-16 10:04:26.975337',1,NULL),
(3,'Identity data required to access AdminUI','Admin UI Profile',0x00,0x01,'admin_ui_profile',0x01,0x00,'2025-07-16 10:04:27.035884',0,NULL),
(4,'','ACS Identity Profile',0x00,0x01,'acs.identity',0x00,0x01,'2025-07-16 10:14:04.717275',0,NULL);
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
('EE6065D8D007AEF2E9F13F5A67FC4C2E',1,'2025-07-16 10:04:31.663046','signing','RS256',0,1,'CfDJ8HITk3_3O5tAi-nKEQmvRY-e3x8q_2Fmz8Aiggs2g8lnY1g3HZSDnHMViI88Ad1vCEKlcJNIBDUrd3rKo_H_k8i8FccfJn-D8U1OW8CCzz0hWYDeRSahg5qt9OaT2K_WkaNOloNH0ufq06-Vj4qjzCmSi_KPq5Y5xxigtD6tUuMMS8iCtq95zQlhE-jjpKQxB20grUWLRk-cqcfFb86ZNHyyKxPAbPSXGTlhVU9PUGozLiTs1ZlG6eJ5H7gElFoC8maMYFGVO-pGgCZ-gunA5PsZDmDk0irQzTEvXh6esnMCAliU-7BWMS1nxWFDdTTa3TrgSgHPV9I8pPachb21T9VINujSifFTrWvOjVJ9O3_S9_mjnQA2zYWND4-5MJ6Mz1uriJsWD26He_Ll8nnRxkRDI_KVvl6Wx5lJxU2IkzWULYGXnS0ReOeCrAve1YZ_nKaeXDehTvisc25pPrzTkehzmDVAcPE_FI0u69mWzpq8CZc-MoPJzz5hfRl3baolARJAK6Qg9QzRTr-ckkfl_7BIbwudWOvfjsuvEMvuSlD1k4Nn7xJ8ugI_3Ea7bDAZ0mvCiJhrBriGGiq4CV9GjD3iIP_yDkPmhrB3b2J-8382YPDYfk-Ure4b-RPNWNYgojJzATjZK9Z9LuyD7ThQ9_e_olFBL9KHV9i9dJ0flSX4DbFpAaFAfTA1aprBdth0WD8BNeqDWUO_NDHDMRhQXMgJyfiFS3RTqVijvcqgWDDlGBq9VXNisG2HDtZzwizCoZKXTP3m1ZQHaW3jwX_16jc0APvXotzk7RMRG8_i94Z2gKvpyqHy1I34eemualtNcb4UueKWX3f8MVFqIifwtiedTdB_HBiUchzQg0CUyFm4PzaoaOITFX6-53J3dsjcLa5nqdYynKN-RULh86wu2k0f1ZHn3YbsHQkEhx4JH_yeMZC-qpZMBy7_drPu8F3WAPFqm167ghYJxSYe0teGQPJ9z2FCeOsXwHnqRIC3RrOEwEpIPJ6iCw_zZ4SGkvJWE5zCB9Xf2U1anPyIYF_k3Rr2Gndx5PLCX8Gd5Yr0wjRTVcVxCiMnoIbGjErqIUZBiWvYZ-2yNoo4fvwEFLkwd-C4_SDrEIZzLIjqrxHqyoSixuSFgH31bxo-Oz_cSvhl0cKzflu3pZxyNqHWW6TYqZgDx_uYUxLCtMP2JI3AX_orSU4O-gfY96CPN7tdg3R8gUWl6v0jrbIEDB003pg56LQ4vB21ir9M4Iw_Pi6v-SVvViVJLL1fksqU32gRUx6QpB9SvtjS5J-ntqOdeVQ-8t86J1iKAWre4bbaq4T2IJpU2Mx7EeDhrZCayq43RZoqFmEOYU2zhvTTT0taCe7kOBNl6Y_nvYRDo1U6PqAm_RLjSLJdTV5sE4-c9TP845z20cQPwdEnUEk1g65fT8o5205tAUchQY3KC5nJanLcJIkd0QV0bdDYl99nNJ_sH0KfFXjfePIFUUNAq2E27reJ4LClvIhnN1NOeZaMH5tgbaUkS_uRAQJvvCD1xYhkmf663VbsO5GgxM2Zhd4SLzGbJamPg3gAQVtUmR-8bH2rSkk9b1swChIcI2WLbkZ866cMTR-iyKgKzDjJN-Tze0MgvdTOnaCg1TGlZguLUI38AXHB1oYmAxADz4o41d9C45Wo9nYlSYjaaw5W-u--aizdWTSpePP2BxioTSe6hUXxXk4r4q9X-ZfSUXDK2TvxehLxem6IkU6PuhKFMiiVh325ky_tZdtEkaa6OkFl9tN-gP_Kujqp7yzEcczISc-32fcvkkBTeipZFdZEB8zKdjrfpQ8jVsDo87JLho8T4XsZlCGLLfYL1PRiUiv47ckkOdh4RQs44uSns4Ojmrx6yGsvvnt7daky5Ifp5zmRT1Tln4o3y6JqlVvLPYlXTW8pxL93fguh49RxStbWOidfdxgduhkIP9mZmwOrXhw9m9LAwhzaE_THTM7HIOv2thSQvg5gGBJOHDRoghZRuPZSt2Pn86dEjdMruNaSFKRni4FQnjf9RggDwm4j3kjc3pMq6ZeclxyiRdVGylS6aTkiijLs24hT7s5yUlwVjqtT0y1bTP6QGZjTZLJBTnzmFVscw9ycrfX8q0OVEAJszoIOBMJdWo8e1YfF-a6lErp9hUc8kvoNTOrpJQN36Ax1s0Q4ixjeUhZUPKDaBbqLsFJceuIXCObgueblg2GOWv_KOGHJMqcDIFbv4LUUrc924v44qq8yev7cfECMs3pogtjtFqWP6vqyumbeJ937dxYtOlGR6xbhLTPTHCDHk57DMmfS8qRVpczNUYYpCTNUQ9WUVctcScJiGVw8DdPsfN8EZNTiLvUUdOJEzvVk-177nlj1TjxuCG6s-XsTcMSAcvqIwvtZuovr2OjbRzHAI4Lv0TwH-Ar_uDK4tfFzbEhDRc-lyTWY-g2PyDvxnFszpo76qedZwhorFbJqjUca5QZuRo_CkqfP');
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
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PersistedGrants`
--

LOCK TABLES `PersistedGrants` WRITE;
/*!40000 ALTER TABLE `PersistedGrants` DISABLE KEYS */;
INSERT INTO `PersistedGrants` VALUES
('F289B071A3F07689ABB82A17F3F35918642739B39F4D1204F9B7EA409C5A6974','admin_ui','2025-07-16 10:05:01.757457','{\"PersistentGrantDataContainerVersion\":1,\"DataProtected\":true,\"Payload\":\"CfDJ8HITk3_3O5tAi-nKEQmvRY8U3OeFPFX-TDV2B_Qu136dwdqlK6eemdVe5V7csEgGXbgEoHqlFB4dXmIj6Z1HJFH38UiBFm4BuwrevYi1HQ5Wc0fVh541sfHl4x5-BqT3KbodDmGMYMYXcp3s8cyTxnxFkce21mRt1mTxrQzyXX9CEaVXRnZR-jlfmu-JTBA7l1RREPbNCmLn78KZb4AB74fNCXRzdGF0EpPGD4YLh7TammtOHYP-AntAu073lWYoKzuVdcupvuDWGFAx4QKuLJgzd5klocPxJdAMoS9_vR_Grwk8KQ2IqdrXUWqDmWazlSoNpyfR_1ws3MLwu2lI29RAaE-FYUoHoCWatqX4yllcwG3ADaBWL7rnsPHVywSc8sSHtMScFAjG_ne61aj4ffGv8kj3lk8HR_4fXm13LZ_B6-HCwSt8917MhSHtpMJW4YsRRy0yXedTh2R3hRLhFZTvKx3EJfVLvHvDRkgFLZXNiIhvDc1fsQali6TRMKXk0QudXFLVj6W042mzp4LhiRfvfSwpBG4UqJbXqFdOkfPVuPg2DdkHCwidNXEplckvtujKed5pF74wu2yVtJ49-fTkcRetbeU8cgIjNCt525SVPfgoYKQJPz0b9EMVzXHxyw0uNeW4WyKNrom6ZgyUKe6DcFCUdcBfXuM-q405YXVxrQK76kxezB8cWZTfoE3PDGEgGPetltBFEnSRMM6hCoBllH4Ikws4JKaGZYO5V7KKJglPRRcosjYcaN5c9dOSNHzptpdTikLF267BACAeC_aZMqTtjdiKVWi-QaLM_8iI8NVNa2rHDnBW6XneOuqlRY2VG4TFtVDJObACfUWmGC6znnFkdPkR98nDFrA0Vja7O3STrgOH3eZhfda_e8obRvLAMWw2sXSx3afD2cTXEX0e2SAeXpZOC77dmDYHoPnR4PQc7gWYDRvpT57FuE2FBaTG_IBor0cDCxH3pC-XwJ_ZYqTHc7u3_ySbaG6AZqNId_5xVrnI5DpdaJZCMOYD_QBzYw6mP8SclAxcRBHdjFAuA_NU859zxNkSFWBTiu7GRM71cvc7GberCW8c341Gsn-ZYfv1d2ceigX3ihhXW13ckFDm3OGigcIBYmv5dLoetRXD6jovx5Fai0myJaD3tmDaZlqOHv_j7-Kdi_H2AYWAgizxC9Hyb8vXXKy-ohWf2WaLTO2YJfkYvPQNzBo-qsUHTaJklJ-CPGQhKdHYXadM084KlfmCldPAnAaNzEtsnrUSnF0CzNCCFwzi0503eYG1EDolB8IC_1rjrPWlBM1S7qD8GSYix1xnxP0dZYnaXI-fwtzRDjxA-9RmUkVOSPbwQDuuqZBot_Yx8se4pZ6ba6WsSkLncoIfRaZWAeqcLlIHNnq0XZbmtd9-DAjx6vW_-MyTMcdCGBHNECfL1gU8frxu1iYiTPKACKtF1kp-ZM9wnYuq086_u7IXiAjlpsZ0sbWblVJGOpGovJ0Kd22ZYuqEB03w7-U6wDtVkFQCgBnWWiWaSuGpuYa5CQOtolYtIdsYRWjYDwK7qYgUxcCelRC8F2yPtqzxmwgIs0HPw7KTGGWnhoKtdyq6-f8pvgg0jjWHrlF5VPdvZEaa7gmbu3kzFoheA5rBGjG9vAIpWcHWuaucM6W7fciF0hNX-rnqw-5q-O2DDwm4m5aVXwu0AlpDx_90On73iHe-U2BbwIyUN4Rt0rNPdRm-6Tq7XNzAH7-AiVRW1btX_OyDDtcE4M2CWGthAGn8H3sBE7Ws1jFCU8YPqQdyICZlSKPQjbwg0T0vGmG1uYBb2H61As-UG6AOuTkGYLWXwmQPPPg_ufBD22VDQNVDf5GnNkrRaYKkFuD_EGCJ8Sh_M2eR7SU884KOAD4E0g70Ewz6A0bDuPp3Ayprb-8Z4SRba1lS3L_THtNK-tuQtLzGQRxUjFI988WCRplJsU0Qmy9mEd52d3WgFLt4xtQeSMgu4o16GJ_rl9blHySXkrqWLWlwBe1y7dfa7QQkvQ47C5C3iXO41ZNg0b_2hjFdG5FLfKlXff30YSXS9cfgQdlBU9ap5Ilmh_fRRjL5skaGKCIntbQb7YeUCNxJdmw0UJSGsLUibzsIlhw29jFYou1K_N4pwof_o8s81KqzlM2ElFCnPg8ZAPdpX-BNsyHO1ggN_3XcxPeBuJR2EBBBlUD0cGIPPGf3iK1iHoxMYhZWw92Ajqsp1NNsrHI4Pl17jv9fxGiAL9xwuL1A4yONhiesbIauIl0qD-MMheeNNMSqapduTEyl_xqBRtv51IiDx6S98ZetfA\"}','2025-07-17 10:05:01.757457','166ae405-1a94-4849-ad2a-55af06e62335','refresh_token','A64148E5E107D5D345C948C7815B0839',NULL,NULL,2),
('E5B58E6F941D3164D166FDC99F006A364DB044E9815A876A4EC69A01C030361B','admin_ui','2025-07-16 10:09:21.807319','{\"PersistentGrantDataContainerVersion\":1,\"DataProtected\":true,\"Payload\":\"CfDJ8HITk3_3O5tAi-nKEQmvRY99oZhY1eCXOtVmfYAzSvDGf9K_iDq6GEUJkSQAdYs6bSNKR9yOdFtYvRAAbR4EigFpL_t1pDBx6_nvvAEJOzxnnXncWPVEHMdgMtESR6GzG96moWsqmp7SlV8YOQE0iSKoHarZ0w1PPCTNJngPZyIMcW_M6tn45eZuMkWfwRdeCNk8yLiKr_XYFzG4AS4Pazv5iCnwivRSb2MV0m0L_cofqveNkQ_EB_ch_peCPSgbQZ9fpRoYSL1mg08s8uGasV3DEZA-JBZ7qvvJ21kOeVkPuzudhjcWPmVLD14WNt2skeU1MrN5_339qFPzfecR3CK_euwZx2DL7fghjzBv-MCutDGtCtSan3hrMYjYyDfpofIdzoIsbKM9iUF_7MUz181XjDnnQCw6EIEZMQIys1xGoLduFqx3oF4iDft-5AnAt0dZUBTMliOjh1QJt1Ro70GyYpR8dZH-FRRYMc1FuaS0fERwPWVXnRFyKMz-TUBUQLhhUHmhdqqENEM-M0rmzfzOjSf0s_2189GvIE05EHCGLTQVrlKwqp9fSVX88rBCTHc9f2RI025S4GLcO5FyU_CiZAveaCK_htJ66pjtzxZbwJthFnjDEn0bYc4ExNb_GNySbxoMR9f-fOPtdRRL29MoWJpI-4kx5Jamrym3tsxUG_Osf1WK9KsUBDREG9A4edusiEBMS7RKWOuYVjXX2FW5pHetr6yxZl6-KtbRAnJ0askwisd098m1hOCNkfclNsCvWffVsNpqSp3Pyx2ts5mkbwwTcxLcc1VI6AEWrSe5XorNBPAgTJYe0iAoXSdeEV7qOriRiRmdajZ8F7KxspgingN5TZEtVRKsVz7iLEwW3G_m3JErsfphpCvWy8-LkCj4y11wXYqA5IgsZgYVv4Y0PYjxXDRW0O4_qHJDgclNHVCIjCqQkBNrKNkwUZDDmpigdm3FsESUxdbeznS8d0NqH67oQNm68o_aYFv2rNsCXP_qBc3x5c_sdJJiZLXZoRI3dCUIcnwPajrsW28Xo6_Q_FV4077R0zUFdAkU2AXnmSDs_GmhahB0fYJxMgVHVMC44KuqBzpyBoctEFHnh0p1CTikngiFr5wb84VRallNWy9u1F2Y5RNq3oETzCjDVq09Fo9mEJqF0wApSEnzdnUDHGtLMMQLc7WfmveaT6ViNheK9vYhm1phVDhTJCbzxJ8SltwCNcQ1XTMbuGbXsiCBXfxlGG5gs4f8Ttq8XPs-qyWQL1nEE9LGHZ4OWOuKjnjVQe-1JfrYtFqxWRhjh4HA3h9XHCzJRhgDLNhFntAzCPVawJfW2ufeyCrDfIZ6PxmRfU_4AHe8IBGi6HumJO6yrFiXvEHVSApibtxchiEVY6L5bLpskIH2PWsiIBtSvPnfZ6Vaqy6O9rm72kv82H2mGh9bhAGUypXFZcYNw8F-xfz2vZQN-K-4GwZGr4QM-TLIv-C2eytK2ZUW0qZz-L0nBxM92BiOtZxuFccH6x8X-3r624bol0D4SXn9nU4IsMJZ5I29oiRdt23Z5i3CRZoTLEzh8vn3sfZGMEBJUQ50-20bs_Ky5CzZvoITFkvAByGqWo18KYCutf18nv905nx-BB03kjc3xWYrwkytxXNS-HmPN2zPcwm6K6GUfYWDXEPsN20GYOpAZdFQrlfdbnwHFG8PRdwajJl_EmW9TR1UY-ag7XPm6YlWlY6sjqKozxWhakS0WctsGujA6gQHF52sFB_OX97g0mVaVqWXzOx1OTuNCQ-M9sjX4lcV_f8TADojmYGSZFeK6wn2EBCo06qLlPGGtIjtDDz8NoDL3GLsjz9Hl_yrmyPzo_b0-5P_wmevKe4tP2SEK4PzJ54imwX82k3jDd1hPTrqBE-Q5mJ-IzTJy8q4I9b3WlDzjt05ryD4HqCcvytvKsuiPgzZ1_JEj7Q27_wWt9yIwv_Bu8-ubMO0KN_s3lhSr3R8Io9A3Z0IlT3uuEd4PpgCqK8NrOwQXEoJ8eUw5cnHHmscRlZI8UKef6m8Q-BW7I4NGSK_kBHO2W5GmoySIAMyor1JW2pYVFzRfUuprMFGaTc_04rXrJmiq19I8lEqkGNt8Gf89n1INLpQ-769XVqM5Ovcc0wVs_iXySEJ4pqZKb0-KccHxSwOm1U-iAQEejRXYjL5tOVerpBO4DM94IX5VX-5pyUxt4tyKceLhB5dYxxBLKETMcvlZRn5LZSjeGxvp2CoZjSzWMqmQWLMQ3nJk2sb8DT8Aj5QWHS6txO67a0DFIjlrnosOvGEyqeo8UxSMdzDHw\"}','2025-07-17 10:09:21.807319','166ae405-1a94-4849-ad2a-55af06e62335','refresh_token','44C8DD276863FAA8DF448A83A36570BA',NULL,NULL,4),
('71EFBA3B8ED1294B5EFD9A45777604859003BB90F1030DE7AADF669E2DC3B9EC','admin_ui','2025-07-16 10:09:50.852981','{\"PersistentGrantDataContainerVersion\":1,\"DataProtected\":true,\"Payload\":\"CfDJ8HITk3_3O5tAi-nKEQmvRY-1TdeR_FqdxY80ueeG_ut1MwEZfTDt6BxCG6AjfkqGkIw8smF6AqWWiQoS_Z-afShfuqr4n9aiAFDZE3WhTOl9olJpNrRzY1OhaLlHcaBEZT-MWp_6gBJGph3CBOXfKNl_ACS0gITwLNPGdwrFnHdQENhIDmY07b7DdbJf8k0iPibIW2xiGApRFNewIXDoeW-qmxZw8QBHOtIZfPNoMG598TwffOq6De40tL4NO5xJiFxUy1L6O65QcJsl2EsmX8OcyZ3FsdEdybAUo3wAb9VEZ736Uw4pzfLZK1WwXQFyHjXlhEzJO9VXHqR6-ERgDbxePzEclYGXZ-GU2pOV-kjHsxCjeE3uY6dKp8ewKJxGBq19Ivym7mYeYZFqioTWkxM1BrTqxuhFlGbpiUTGK8JgStdul2CmHA__V5CLCiCx7-5L-Uwtac99jsKB7t5wJOIFwEsD27GRxh5FfgPBdcgN_DihAF59hzAmhla9Zq-QfEoWCqGXxcKjWSnMUn5mgnDInGieOtfmXoaJucXYH_igA3Hn3s8KcpsorVvFgWweJd2Fqy39bh0l5ICEgu5eyGhJwzOE5_2RfPPZ98rUXwUvbLa_WP929aluw4ObjbuUI-ixs0UiZniDGbgFTLReXzZgwlv6VMEoh_da6o0fATwfXRRqBO9epAaJIZmy5zu-E5KnG-NlnSYGYdpMflSpSZ46y4wa-g9ckDaX-FGZGm8U88KbPZG1lKuFRNKORf5HvjnYcqrrM04VfPnKv0Gy2e-Atz1f87u7dnIwwpZrYo_5mP-fC6Xek63fnmGmHxk5wrhIFbDQUtFO9o9-X4I3se2PXk2NTBefSwqZYqMNJ76WDz0HFtuqMKNGrPtLtyTbAeDhBLeSraAQEMwpCKeaO9Eu6MsIuk-7MhznLGMTDGoI0cm7o4CMUNssg4JYr_Z76OwpOuH2HhSZzf_DQY92mBy1YjCdAKrZQwKGzoywSQ16r9Svqcvc0hga9iHTX68IEZt2uc9_2MYUc9UEgCfa_zEPA5w1T9aw61_bSU-EH_VxKhAEyuIPYhZUnJqb-rOFvZYFhMoAsBPkUmoSPvcHeJG1EctaYzrNZqhA2v8TtBN1Ydz5paVVFtKBY8PjQvSLWzqlH2Zc3vUz_48mAKl7OvlFd7siAvbJxrF-LRuponZYowC8vPczqPA6isW9o3wGbXqoU5iZSR8bRtGcXIHWIfe17GX3pzAimdqhlCm-FnCgh0H6j7dDsaPyH5FHhBOs6ugJzzrzzvRYBatESRMzikr3bMb3A9HieBBlZWBOK3yzos4oneUTnWluNgrLnWorCzYatn0WsO1uh43QoJ18uHEJ04lQzjA_2NZJls7ClK-wocdQGErKFRsDrjSD0SCGjCjDYkNtCpA1hLrW6NHyl-BNxlWlkJkIa2i7of9iWFfOi07GmCGVK4fL18_8BBRQeGGQlzkRXSSgpFOx8VOCXLY-8NLukz81IqS6FLoI4rCb8p2Un7sV5Xhq8mCdAkHGX6DJQu5LUWfcEjnc7gN1fKfPDTps61frn-n7L9O2iJoAhVBKbNnUOv-XdAppQwBm1j_D0S6p7N7B7YpB92zZva8uXIC5ptMx3Ukmu4CyF1IM0t1vwOO7NEt7C1WOgi4Iewzrj8kqM-nD3Jwra3EjxVf_lXyaFQponYGPC3YchrCf2SNmnUNBIrW3V22XM4YHESDS7aSo9UbbURSesqycK8amHJR9iGed_IgiJmCGnaxUdfvkM16w6ckCXwvgU_GL-QZpA3ohfqiG7yTA7-zY3TxDtA9F7ObQ9pV4kQMWcWYg2oiY8LLyMT_YXFdWdyr3mGlgFtqFvtz453Zu0TaM6CE-qLq-4ctGpdWRQbDUMSSeykl4YljazD065U6xxYOWZcL_rPk4FuxhmrGetGytSkO3FXZyK_-BWrORbf5wQOfgnopfl1iOUvOfyCjH2N9iy8rgNxKEP_biST5msCUsBWp4EvHCydo-wI6bgFnPde0SOL3JZoShLgEFSAssiqjWc02RAULVJrzFdpVBpcjd59Wt1MsmM_bARrNKwydwgvGCFKu4cSEYoxcaXKUpcJ2c1Gd9nKNpDNR3_O_1R6bGxQk9MHKMyVHkKfHuJnQwQTqcpGHZAz8FxkRWnhLu6WHO4pKgvWgAo0oFCi37bcoij5T5qlczBJEOEiERtZqgmP5ydBX08az2WvLkmO5THb_yY2QlJQHBqtFoErKBMXVJF0Ku4sI7IFHJeuPXaIxyOdwDr1-8RPcdOaiX1z5g-SlN-Q\"}','2025-07-17 10:09:50.852981','166ae405-1a94-4849-ad2a-55af06e62335','refresh_token','44C8DD276863FAA8DF448A83A36570BA',NULL,NULL,6),
('A7E52ABD51539DA1A4C0F9DBAD37349BC461706A179A6B01C856C817CADCAC52','admin_ui','2025-07-16 10:12:25.676983','{\"PersistentGrantDataContainerVersion\":1,\"DataProtected\":true,\"Payload\":\"CfDJ8HITk3_3O5tAi-nKEQmvRY_2B2Qj1XYfFIZawWT46oOyXxIpvwwc_9vsqEpaLxbB7U7KNU8cqHqN-LPd4ZGvOMcGmV6E8pD20_vq2Jq_yL_pikJ06s8CsLHMtFC_6FMuyIbKk-Wk9IrOF2pmKPrSGl0EeLypFVbVV0hKKi---7oeHrgOyZ5kiNONoteOz3qh5_AYNB-0wEl_J0VvSUjuHVzGBqatiLasDihQK7R2shXvYnMItQi4g2Xx_jSFE23eapA0fjQYi6SYs71zqzvs7K3KOeIHeaz_jECq_ug3ArkLJh9knQl8S62QObFLZcJBQu8ou6kEa6kfMCPrMIYo85Ul77OkeHqqokifKDMAOn8Xj2ji4SOci6Ll9DfogcM97s_TD61QtU8BZc3goTACAtGxDoh0eFYSFQND9pWm2Ni-OxO9bl9HqC5Pb62WbgF37QHHXawhIrFWfs12DhsysrjiNjUCUhDj6jL4zwPbKMM8qz5PK3twvrKlmYkvb3bsuRyiAj_Jcw-DEeNn6nJOZsOtxxwps4ZpsJlJNlFQCw8Sfk8DjH-B8HQ5TyTplCJ7JYU2QfDmItlZT71JqOd2Axy4cYBHpcv6yUdDWQX_cOwpe1LxSqmreOEXqTnP_5lOdEHEhmd11TFOXFg8KmEu_XKeGrVAtKKw6VDTbTnKVlRhmLewDpsQP7nH5MM-Thm8es3L6Fbb8CSs8vjVLPKsmNjLPDRN9dEMAM3oju1Fqf3HPX2wXZLfMMXdGkTqxENh4_Mkhyh8mcK4JAb1aPQzwdF0v11WzP8mo6SVAWuD8mCR7EzspmgFgVKxBL7qwH92JDbaiCZfILeLr0jwS3cstSxd4ljunsQLPppLgcg2Pqh2J4dCqOYoyNRUvj3oirOGT7WMpE3SW7mjhrg1iHl316hRFRyAaVIfW9w1olHRacxL0tHgxjp64vVIHywxoxL3rIiyETCFQBDsVEhdKCiF3q56fzQImLqcmgnrmaUIrEIKHbifg0KcB9LSXpsPGGaDGSy1XxFN1xN6nXyvUS-jxe0JcDWMwBx-xTvuiDgNWWyqWX3Q9rkuF6ElSpeHk3P6wXPDBsjuhLAk8yBS9XZ1eglupd10A3-DIFb1p_fI5PNH83-u9J_YECZ4qfMtzM_yntcS_1zrpG8Uiad-IuPYTX_pVSmPyKuCrhFIViMkZKB284GHWLda1UFS1yaRAmMpRjgb023nNsODh3XAAk1D_bHpQ68TStheB6kr2Vc76KMx-1aJCqnXjcH1m2CJ0MMQCih6G3regOMlUJM3UwLz9xc3rTiCDcYM41oB-yBtge6m-Udh1STa9dC_BR1cigf1JojzWogQQ6cu16xkp3Os8EAGtCqz9-pFtxzUquxkx3zbaGf_19Tidr6ewdjWC448njr1uDB0p-deThzj8H5X675MBnw5AeuRLuwmiRNClPFaV68MKfSqbdiLqzC1xFRFeIfNh0m6NIC-KUW1s5gkzDQQvOs-FxJbvKsWMH2ofWbapZDV7aiI6CYA6IdjO3TsMiirYyk9o4tx42nWUfymIjlhLSnas0nN4CfVgVJD5bFD7GhS7WT4II_2Q9M5vUiscPiM0avFb2up7GoDdyQHzRq5w-hmuchpslx2DvOwx8koJLcnevhJ6akum7yRkKWUO457n39z1lH2vyAKIx4AmlcC_iylFkPziSHalKVr6RYUE7cR9XWUG5Auv_x5YwTvAH0nHFY1v8po6lyJv411C5CPsCMrDHBBWfXZnNhBAgDzv_XJAqS1eqV7P8dMck7Vdr5ElfKEO8Z6Y1Ew7BImVp3_9YYqYauJdecWDmEq3FMB6mHUQ5OIpk2t3iyjg9CoyJMgGndvqiCPmmS1XiSwavDWXt9Gr0HbO_GniafX5s3fleO0qiTKrZKzAkchlr4ePwMdui9d817fwcuOdwl_z6fr17rOlsKrsJcDL-RvMVbra10qMbHp6f2Z9RxolAHzTJLSoLPmlk8qlwZ76FPNrSDhtWFNtCZybdg_3UBBf9SY7dqv4HUzxtWSnc6YutaQ0_sXh3sndgUo9xORTDJTv9wC-yuBS86MjFYwAQy44xRf-NcF1Y6n_VjSr6jGc9WGsG5BlqEfdX20Uubi6EIUfcL4t05fjwxn284pSNzEYrBf7xoHx7PTVTiuiifXnkadpUxHsiM66aO9nko-HnvIQBCiAG3cMOQcoX8LL7g_pXqz2nt36JDEzB6Pc6YPMZUmOd57CRiEQLVTzuqJ1vYPT9afAupXuZWHdBKZEpBGItZMYR8wPh89H6jk_KaZhTE92Q\"}','2025-07-17 10:12:25.676983','166ae405-1a94-4849-ad2a-55af06e62335','refresh_token','A6784C8B7750C3B02B9CE20FB3F0BD55',NULL,NULL,8),
('5D463922579B0842AF2D702CE8DBA25FA8F43E49BFCA9F56E1F24F27FD6C3B6B','admin_ui','2025-07-16 10:13:09.789079','{\"PersistentGrantDataContainerVersion\":1,\"DataProtected\":true,\"Payload\":\"CfDJ8HITk3_3O5tAi-nKEQmvRY_Wpz3FRBW_FFKNgDrnYyE4RS3T5o1YNOFgYScCUI4ej6Q868b5E-OG215jB8c-w7D6PxszkaL5Ag4GIifKF3_DXzcAeeXoEf6sbLKMXlQ_p4-AvcDN8TNiH-T25KDP2z_OVOaKILNpIj6kD-LOVIqG9_1h872ieLBiN0zdCGBJwpvpfvXncb4v1sNAZghOMbCBnMra4nWrO90oYRKXbV-Y7EeERsVg291wh-Ua6X-uLPiahhDOfpefUFjkDA84VwqgOOuOMe0zqb5DeoqXOkXQB9GPJazi9P_fKM5PQdZInvdxyPh14AGrSzdmOGDd-pccFAL8x0HJ6Y0NxhfqWgnrF0qmy9vl94bRbe1oUyfDGqlMIGaevu9M0Bk8uQHq_GxT2FGVusS7wS7SCSaHVUun5LmB0RsmffrzW5D3Ouf7j5pWE-xa3n_KseZvJoc5MOiT2AlMGkx1iVe03wsR78KBE7nBbCE6rz5EeksYbLMxNnMUr9pbaNGx_3yKY13jdkhkJGxCppxNi7N3nCQOoTAzH0_N1OW8ecFZAEmO1C29ZKbj2oejdqco4cVCNqPKtBEEv9W6c9DtdpQV-Nsq-Af25g7Ks0V-QmwrEBv7oPuHIvyFjQDAMZlpTi3YM7Pdfox1EAZVpLna5arx-PKYP6mwaGGa3tOAQJO_5knYSDM8lNK9HRU7hPV1CHOujlsBIbt-EjJpTkEhW9HovQ-nf_nbCaG0M26NwyfDBTyY9rt9s98L838moKxlz7FVcRkfZnZ7KSdkJI7gLbXaM0fAjHmX4zVPJ8_7r6ldv7b3GM9UAdnKZ-NqeE2mf6FmQlnFisTa1L8-7WOy8CoI_35MUBqI_4dEFTNibyXEyuTOk-tD4KVSgt6XN89auQVaUSDFnqG4vjXgOikq__r2dQrV3hSCeidFhDeoViXSYn_kZKXvx7ronYji_jWaKgRGTiqJQTWF5o9oDg5MY-TIhx40MZr038hPaH1ykn1W8ncojHPc4U0_lMvq1Jdbx6KiOOU4OlA3x4OlxFxcaQ9aBTvSSSGcP59Dm0RYJFKjj7C5hDR7P_es84k0Yd-_S1aM-S8qcIxucQ7JlKFW_4buR18vonZ1aAkGO0SZL2J2r0Msa8eXRR0paHkjl1o0Tj4OmNy_TVPR6gXutsfAf7ywTPY4Doff1wplqjNg_EexzHb2EBKtWHXkUowGXtkXr37fQkDkoMgemuqptEMBeFVCB3j7R31fnMmYtrk-zGQMZ4Zn0CLwQEVD_c7H7Q4EUNFmRz9uaJFVj3Imbu_pK5kXmkMbou4PJe3EGLIb2A-qM4nfmdy_rvCrWb08gyHn32smQwkbyRHQTCA51VJq_sBe4Dv0ve-1txT086xT96VuVA3xuEZMM4oLlz-NaIK2XehWaphVXuB_rC_pH6Hjln_O07TBhEA0-HWXbYAklADeZagwTmtB57_cTXP2bFdsE1P_OqDfPS57NjtSHHxGInY500vT35G61FHlBftkuIh6f5KEIPnu6YRmCUG52Vnr_-2iF0yrs34ulp1FHPHcPSFDzn52Ruyv_EbaK2R7MlVy_W6XJuU3B3XeslmWSiXCXGvc7MEZ31v8g__fUDqHWWEX6G0JZYVLBpuAARwM0me_lEJL1dV5STpgoEYuXeqyhMTH5BnNO7LbsRp_u7kN0s7UKUjFcb3tErHd8TXyyqE8NrcaWCEslACOQyFU2T93sb6IgHPRQ3Q5qyzQ7f2SGrd22TCALvl4gWCyGxQeEyhU-ksHO9aK2OJZqE4gZDfsgockNqqlPdU2_rEvtxNcIPrKVCeOscDLWzWfM6ePDI1eql1QwWswuzVPtW7k16j47GUH1uku8KWZON0cnF9exvFXMTr8Cmmoy4eFCaaorzCVJzNGOSGBKct6zAwA42-liD6p3DSRfEz0r9ARIKHLkoOeslJGG0kazYXtGrrMaH-ZEtMxaq-k-1TWuzw_7HVRpYtyuAQxC9eel5JOOZQkfZTshCA7Vu3Nkz8USjYJh8uStu7OhldgY6IM2RlD7Xnc18kPLkpO7oN8-bHl8s55njNBH4fpcTDx8E0_j75LPvb3RvQYnjwHS-skz4we120MkobkYSFQIWCdaByj2QaE2nkCh12-UPqAQ5U9fBHqW8U38pZLszWUyKEPMS4OOApyBFFc2sgGanjT08tx5rkrxbM7yj9FVUZBBk3Z0wNhYv9qAAyRkCPvFjCgI5lXAh64e6o_bxuAPB89BSG15BnsmTCX_ye7Kc-Q\"}','2025-07-17 10:13:09.789079','1b033686-202b-4970-9002-70cb3f1593af','refresh_token','4C901E64EEE3DDC65F5163BD65A148CC',NULL,NULL,10);
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
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
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

-- Dump completed on 2025-07-16 16:24:20
