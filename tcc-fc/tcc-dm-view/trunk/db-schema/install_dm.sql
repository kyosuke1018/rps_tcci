--
-- Table structure for table folder
--


CREATE TABLE TC_FOLDER (
  id number(18)  NOT NULL,
  name varchar2(255) NOT NULL ,
  folder number(18)  default NULL,
  createtimestamp date default NULL,
  modifytimestamp date default NULL,
  icon varchar2(300) default NULL,
  isremoved number(1) default '0', 
  modifier number(18)  default NULL,
  creator number(18)  default NULL,
  PRIMARY KEY  (id),
  CONSTRAINT FK_folder_3 FOREIGN KEY (modifier) REFERENCES TC_USER (id),
  CONSTRAINT FK_folder_4 FOREIGN KEY (creator) REFERENCES TC_USER (id),
  CONSTRAINT FK_folder_1 FOREIGN KEY (folder) REFERENCES TC_FOLDER (id)
) ;
/

--
-- Table structure for table folder
--


CREATE TABLE TC_DOCUMENT_MASTER (
  id number(18) NOT NULL,
  docnumber varchar2(255) NOT NULL,
  name varchar2(255) NOT NULL,
  PRIMARY KEY  (id)
) ;
/
--
-- Table structure for table document
--

CREATE TABLE  TC_DOCUMENT (
  id number(18)  NOT NULL ,
  master number(18) NOT NULL,
  versionNumber varchar2(255) NOT NULL ,
  iterationNumber varchar2(255) NOT NULL ,
  islatestIteration number(1) default '1',  
  islatestVersion number(1) default '1',  
  doctype varchar2(255) default NULL,
  description varchar2(4000) default NULL,
  name varchar2(255) default NULL,
  folder number(18)  NOT NULL ,
  isremoved number(1) default '0', 
  creator number(18)  default NULL,
  modifier number(18)  default NULL,
  createtimestamp date default NULL,
  modifytimestamp date default NULL,  
  PRIMARY KEY  (id),
  CONSTRAINT FK_document_1 FOREIGN KEY (folder) REFERENCES tc_folder (id) ON DELETE CASCADE,
  CONSTRAINT FK_document_2 FOREIGN KEY (creator) REFERENCES tc_user (id),
  CONSTRAINT FK_document_3 FOREIGN KEY (modifier) REFERENCES tc_user (id),
  CONSTRAINT FK_document_4 FOREIGN KEY (master) REFERENCES tc_document_master (id)
) ;
/

--
-- Table structure for table foldered aclentry
--

CREATE TABLE  TC_FOLDERED_ACLENTRY (
  ID NUMBER(18) NOT NULL,
  ACLTARGETCLASSNAME NVARCHAR2(255) NOT NULL ,
  ACLTARGETID NUMBER(18) NOT NULL ,
  PERMISSIONMASK NVARCHAR2(45) NOT NULL ,
  INHERITANCEMASK NVARCHAR2(45) NOT NULL,
  ACLPRINCIPALCLASSNAME NVARCHAR2(255) NOT NULL ,
  ACLPRINCIPALID NUMBER(18) NOT NULL ,
  CREATOR NUMBER(18) DEFAULT NULL,
  CREATETIMESTAMP DATE DEFAULT NULL,
  MODIFIER NUMBER(18) DEFAULT NULL,
  MODIFYTIMESTAMP DATE DEFAULT NULL,
  PRIMARY KEY  (ID),
  CONSTRAINT FK_FOLDERED_ACLENTRY_1 FOREIGN KEY (CREATOR) REFERENCES TC_USER (ID),
  CONSTRAINT FK_FOLDERED_ACLENTRY_2 FOREIGN KEY (MODIFIER) REFERENCES TC_USER (ID)
) ;

COMMENT ON COLUMN "TC_FOLDERED_ACLENTRY"."ID" IS 'PK';
COMMENT ON COLUMN "TC_FOLDERED_ACLENTRY"."ACLTARGETCLASSNAME" IS 'Target object class name';
COMMENT ON COLUMN "TC_FOLDERED_ACLENTRY"."ACLTARGETID" IS 'Target object id';
COMMENT ON COLUMN "TC_FOLDERED_ACLENTRY"."PERMISSIONMASK" IS 'permission mask (CRUDMXX, 目前只用到前5碼但預留兩碼).';
COMMENT ON COLUMN "TC_FOLDERED_ACLENTRY"."INHERITANCEMASK" IS 'permission mask 是否繼承自Parent(CRUDMXX, 目前只用到前5碼但預留兩碼).';
COMMENT ON COLUMN "TC_FOLDERED_ACLENTRY"."ACLPRINCIPALCLASSNAME" IS 'principal class name';
COMMENT ON COLUMN "TC_FOLDERED_ACLENTRY"."ACLPRINCIPALID" IS 'principal id';
COMMENT ON COLUMN "TC_FOLDERED_ACLENTRY"."CREATOR" IS '建立者 (FK:TC_USER.ID)';
COMMENT ON COLUMN "TC_FOLDERED_ACLENTRY"."CREATETIMESTAMP" IS '建立時間';
COMMENT ON COLUMN "TC_FOLDERED_ACLENTRY"."CREATOR" IS '修改者 (FK:TC_USER.ID)';
COMMENT ON COLUMN "TC_FOLDERED_ACLENTRY"."CREATETIMESTAMP" IS '修改時間';

CREATE SEQUENCE SEQ_DOC INCREMENT BY 1 START WITH 1 NOMAXVALUE CACHE 3 NOORDER NOCYCLE;

INSERT INTO TC_FOLDER VALUES (1,'ROOT',NULL,SYSDATE,SYSDATE,'',0,1,1);