--
-- Table structure for table folder
--
CREATE TABLE TC_FOLDER (
  ID INT NOT NULL IDENTITY,
  NAME NVARCHAR(255) NOT NULL ,
  FOLDER INT  DEFAULT NULL,
  CREATETIMESTAMP DATETIME DEFAULT NULL,
  MODIFYTIMESTAMP DATETIME DEFAULT NULL,
  ICON NVARCHAR(300) DEFAULT NULL,
  ISREMOVED BIT DEFAULT 0, 
  MODIFIER INT  DEFAULT NULL,
  CREATOR INT  DEFAULT NULL,
  PRIMARY KEY  (ID),
  CONSTRAINT FK_FOLDER_3 FOREIGN KEY (MODIFIER) REFERENCES TC_USER (ID),
  CONSTRAINT FK_FOLDER_4 FOREIGN KEY (CREATOR) REFERENCES TC_USER (ID),
  CONSTRAINT FK_FOLDER_1 FOREIGN KEY (FOLDER) REFERENCES TC_FOLDER (ID)
) ;
/

--
-- Table structure for table folder
--
CREATE TABLE TC_DOCUMENT_MASTER (
  ID INT NOT NULL IDENTITY,
  DOCNUMBER NVARCHAR(255) NOT NULL,
  NAME NVARCHAR(255) NOT NULL,
  PRIMARY KEY  (ID)
) ;
/
--
-- Table structure for table document
--
CREATE TABLE  TC_DOCUMENT (
  ID INT NOT NULL IDENTITY,
  MASTER INT NOT NULL,
  VERSIONNUMBER NVARCHAR(255) NOT NULL ,
  ITERATIONNUMBER NVARCHAR(255) NOT NULL ,
  ISLATESTITERATION BIT DEFAULT 1,  
  ISLATESTVERSION BIT DEFAULT 1,  
  DOCTYPE NVARCHAR(255) DEFAULT NULL,
  DESCRIPTION NVARCHAR(4000) DEFAULT NULL,
  NAME NVARCHAR(255) DEFAULT NULL,
  FOLDER INT  NOT NULL ,
  ISREMOVED BIT DEFAULT 0, 
  CREATOR INT  DEFAULT NULL,
  MODIFIER INT  DEFAULT NULL,
  CREATETIMESTAMP DATETIME DEFAULT NULL,
  MODIFYTIMESTAMP DATETIME DEFAULT NULL,
  DOMAIN NUMBER(18),
  PRIMARY KEY  (ID),
  CONSTRAINT FK_DOCUMENT_1 FOREIGN KEY (FOLDER) REFERENCES TC_FOLDER (ID) ON DELETE CASCADE,
  CONSTRAINT FK_DOCUMENT_2 FOREIGN KEY (CREATOR) REFERENCES TC_USER (ID),
  CONSTRAINT FK_DOCUMENT_3 FOREIGN KEY (MODIFIER) REFERENCES TC_USER (ID),
  CONSTRAINT FK_DOCUMENT_4 FOREIGN KEY (MASTER) REFERENCES TC_DOCUMENT_MASTER (ID)
) ;
/

--
-- Table structure for table foldered aclentry
--
CREATE TABLE  TC_FOLDERED_ACLENTRY (
  ID INT NOT NULL,
  ACLTARGETCLASSNAME NVARCHAR(255) NOT NULL ,
  ACLTARGETID INT NOT NULL ,
  PERMISSIONMASK NVARCHAR(45) NOT NULL ,
  INHERITANCEMASK NVARCHAR(45) NOT NULL ,
  ACLPRINCIPALCLASSNAME NVARCHAR(255) NOT NULL ,
  ACLPRINCIPALID INT NOT NULL ,
  CREATOR INT DEFAULT NULL,
  CREATETIMESTAMP DATETIME DEFAULT NULL,
  MODIFIER INT DEFAULT NULL,
  MODIFYTIMESTAMP DATETIME DEFAULT NULL,
  PRIMARY KEY  (ID),
  CONSTRAINT FK_FOLDERED_ACLENTRY_1 FOREIGN KEY (CREATOR) REFERENCES TC_USER (ID),
  CONSTRAINT FK_FOLDERED_ACLENTRY_1 FOREIGN KEY (MODIFIER) REFERENCES TC_USER (ID)
) ;

CREATE INDEX TC_FOLDERED_ACLENTRY_IDX1 ON TC_FOLDERED_ACLENTRY (ACLTARGETCLASSNAME,ACLTARGETID);

CREATE INDEX TC_FOLDERED_ACLENTRY_IDX2 ON TC_FOLDERED_ACLENTRY (ACLPRINCIPALCLASSNAME,ACLPRINCIPALID);