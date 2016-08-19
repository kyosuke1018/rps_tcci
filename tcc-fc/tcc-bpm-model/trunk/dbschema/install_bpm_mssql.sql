CREATE TABLE  TC_TEAM (
  ID INT NOT NULL IDENTITY,
  NAME NVARCHAR(200) NOT NULL ,
  DESCRIPTION NVARCHAR(1000) DEFAULT NULL,
  CONSTRAINT TC_TEAM_PK PRIMARY KEY  (ID)
) ;

--
-- Table structure for table role
--

CREATE TABLE  TC_ROLE (
  ID INT NOT NULL IDENTITY,
  NAME NVARCHAR(255) NOT NULL ,
  CONSTRAINT TC_ROLE_PK PRIMARY KEY  (ID)
) ;

--
-- Table structure for table tc_roleholderrolemap
--


CREATE TABLE  TC_ROLEHOLDERROLEMAP (
  ID INT NOT NULL IDENTITY,
  HOLDERCLASSNAME NVARCHAR(255) NOT NULL ,
  HOLDERID INT  NOT NULL ,
  ROLE INT  NOT NULL ,
  CONSTRAINT TC_ROLEHOLDERROLEMAP_PK PRIMARY KEY  (ID),
  CONSTRAINT FK_ROLEHOLDERROLEMAP_1 FOREIGN KEY (ROLE) REFERENCES TC_ROLE (ID)
) ;


CREATE TABLE  TC_ROLEPRINCIPALLINK (
  ID INT NOT NULL IDENTITY,
  ROLE INT  NOT NULL ,
  PRINCIPALCLASSNAME NVARCHAR(200) NOT NULL ,
  PRINCIPALID INT  NOT NULL ,
  CONSTRAINT TC_ROLEPRINCIPALLINK_PK PRIMARY KEY  (ID),
  CONSTRAINT UK_ROLEPRINCIPALLINK UNIQUE (ROLE,PRINCIPALCLASSNAME,PRINCIPALID),
  CONSTRAINT FK_ROLEPRINCIPALLINK_1 FOREIGN KEY (ROLE) REFERENCES TC_ROLE (ID) ON DELETE CASCADE
) ;


--
-- Process Template
--
CREATE TABLE TC_PROCESSTEMPLATE (
  ID INT NOT NULL IDENTITY,
  PROCESSNAME NVARCHAR(255) NOT NULL ,
  PROCESSVERSION NVARCHAR(20) NOT NULL ,
  DISABLED BIT DEFAULT NULL,
  CONSTRAINT TC_PROCESSTEMPLATE_PK PRIMARY KEY (ID)
);


--
-- Process instance
--
CREATE TABLE TC_PROCESS (
  ID INT NOT NULL IDENTITY,
  PROCESSNAME NVARCHAR(255) NOT NULL ,
  PROCESSTEMPLATEID INT  NOT NULL ,
  EXECUTIONSTATE NVARCHAR(255) NOT NULL,
  PRIMARYOBJECTCLASSNAME NVARCHAR(255) DEFAULT NULL,
  PRIMARYOBJECTID INT  DEFAULT NULL,
  STARTTIMESTAMP DATETIME DEFAULT NULL,
  ENDTIMESTAMP DATETIME DEFAULT NULL,
  CREATOR INT  DEFAULT NULL,
  TERMINATOR INT,
  TERMINATE_REASON NVARCHAR(500),
  TEAM INT  DEFAULT NULL,
  CONSTRAINT TC_PROCESS_PK PRIMARY KEY  (ID),
  CONSTRAINT FK_PROCESS_1 FOREIGN KEY (CREATOR) REFERENCES TC_USER (ID),
  CONSTRAINT FK_PROCESS_2 FOREIGN KEY (PROCESSTEMPLATEID) REFERENCES TC_PROCESSTEMPLATE (ID),
  CONSTRAINT FK_PROCESS_3 FOREIGN KEY (TEAM) REFERENCES TC_TEAM (ID) ON DELETE CASCADE
  CONSTRAINT FK_PROCESS_4 FOREIGN KEY (TERMINATOR) REFERENCES TC_USER (ID),
) ;


--
-- task template
--

CREATE TABLE  TC_ACTIVITYTEMPLATE (
  ID INT NOT NULL IDENTITY,
  ACTIVITYNAME NVARCHAR(255) NOT NULL ,
  ACTIVITYTYPE NVARCHAR(255) NOT NULL ,
  PROCESSTEMPLATEID INT  NOT NULL ,
  ROLENAME NVARCHAR(255),
  DEADLINE DATETIME,
  DURATION INT,
  EXPRESSION TEXT,    
  CONSTRAINT TC_ACTIVITYTEMPLATE_PK PRIMARY KEY  (ID),
  CONSTRAINT FK_ACTIVITYTEMPLATE_1 FOREIGN KEY (PROCESSTEMPLATEID) REFERENCES TC_PROCESSTEMPLATE (ID)
) ;

--
-- task instance
--

CREATE TABLE TC_ACTIVITY (
  ID INT NOT NULL IDENTITY,
  PROCESSID INT  NOT NULL ,
  ACTIVITYNAME NVARCHAR(255) NOT NULL ,
  ACTIVITYTYPE NVARCHAR(255) NOT NULL ,
  ACTIVITYTEMPLATEID INT ,
  ROLENAME NVARCHAR(255) DEFAULT NULL,
  EXECUTIONSTATE NVARCHAR(255) NOT NULL,
  DURATION INT  DEFAULT NULL,
  DEADLINE DATETIME DEFAULT NULL,
  EXPRESSION TEXT,  
  STARTTIMESTAMP DATETIME DEFAULT NULL,
  ENDTIMESTAMP DATETIME DEFAULT NULL,  
  CONSTRAINT TC_ACTIVITY_PK PRIMARY KEY (ID),
  CONSTRAINT FK_ACTIVITY_1 FOREIGN KEY (PROCESSID) REFERENCES TC_PROCESS (ID) ON DELETE CASCADE,
  CONSTRAINT FK_ACTIVITY_2 FOREIGN KEY (ACTIVITYTEMPLATEID) REFERENCES TC_ACTIVITYTEMPLATE (ID) ON DELETE CASCADE,
) ;


--
-- task link in activity template
--

CREATE TABLE TC_ACTIVITYROUTETEMPLATE (
  ID INT NOT NULL,
  FROMACTIVITY INT  NOT NULL ,
  TOACTIVITY INT  NOT NULL ,
  ROUTENAME NVARCHAR(255) DEFAULT NULL,
  CONSTRAINT TC_ACTIVITYROUTETEMPLATE_PK PRIMARY KEY (ID),
  CONSTRAINT FK_ACTIVITYROUTETEMPLATE_1 FOREIGN KEY (FROMACTIVITY) REFERENCES TC_ACTIVITYTEMPLATE (ID) ON DELETE CASCADE,
  CONSTRAINT FK_ACTIVITYROUTETEMPLATE_2 FOREIGN KEY (TOACTIVITY) REFERENCES TC_ACTIVITYTEMPLATE (ID)
) ;

--
-- task link in activity instance
--
CREATE TABLE TC_ACTIVITYROUTE (
  ID INT NOT NULL,
  FROMACTIVITY INT  NOT NULL,
  TOACTIVITY INT  NOT NULL,
  ROUTENAME NVARCHAR(255) DEFAULT NULL,  
  TALLY INT DEFAULT NULL,
  CONSTRAINT TC_ACTIVITYROUTE_PK PRIMARY KEY (ID),
  CONSTRAINT FK_ACTIVITYROUTE_1 FOREIGN KEY (FROMACTIVITY) REFERENCES TC_ACTIVITY (ID) ON DELETE CASCADE,
  CONSTRAINT FK_ACTIVITYROUTE_2 FOREIGN KEY (TOACTIVITY) REFERENCES TC_ACTIVITY (ID) 
) ;


--
-- workitem in worklist
--
CREATE TABLE TC_WORKITEM (
  ID INT NOT NULL IDENTITY,
  ACTIVITYNAME NVARCHAR(255) DEFAULT NULL,
  ACTIVITYID INT  NOT NULL ,
  EXECUTIONSTATE NVARCHAR(255) NOT NULL ,
  STARTTIMESTAMP DATETIME DEFAULT NULL,
  ENDTIMESTAMP DATETIME DEFAULT NULL,
  OWNER INT  DEFAULT NULL,
  BALLOT NVARCHAR(255) DEFAULT NULL,  
  CONSTRAINT TC_WORKITEM_PK PRIMARY KEY (ID),
  CONSTRAINT FK_WORKITEM_1 FOREIGN KEY (OWNER) REFERENCES TC_USER (ID),
  CONSTRAINT FK_WORKITEM_2 FOREIGN KEY (ACTIVITYID) REFERENCES TC_ACTIVITY (ID) ON DELETE CASCADE
) ;

CREATE TABLE TC_SIGNATURE (
  ID INT NOT NULL IDENTITY,
  PRIMARYOBJECTCLASSNAME NVARCHAR(255) DEFAULT NULL,
  PRIMARYOBJECTID INT  DEFAULT NULL,  
  BALLOT NVARCHAR(255) NOT NULL ,
  COMMENTS NVARCHAR(500) DEFAULT NULL,
  CREATETIMESTAMP DATETIME DEFAULT NULL,
  CREATOR INT  DEFAULT NULL,
  WORKITEM INT  DEFAULT NULL,
  CONSTRAINT TC_SIGNATURE_PK PRIMARY KEY  (ID),
  CONSTRAINT FK_SIGNATURE_1 FOREIGN KEY (CREATOR) REFERENCES TC_USER (ID),
  CONSTRAINT FK_SIGNATURE_2 FOREIGN KEY (WORKITEM) REFERENCES TC_WORKITEM (ID)
) ;


CREATE TABLE TC_VARIABLE
   (	
    ID INT NOT NULL IDENTITY, 
	NAME NVARCHAR(200), 
	VALUE NVARCHAR(255), 
	CONTAINERCLASSNAME NVARCHAR(200)  NOT NULL, 
	CONTAINERID INT  NOT NULL,
	VISIBLE BIT  DEFAULT 0 NOT NULL,
	CONSTRAINT TC_VARIABLE_PK PRIMARY KEY  (ID),
	CONSTRAINT UK_VARIABLE UNIQUE (CONTAINERCLASSNAME,CONTAINERID,NAME)
   ) ;



CREATE INDEX TC_ROLEHOLDERROLEMAP_INDEX1 ON TC_ROLEHOLDERROLEMAP ("HOLDERID", "HOLDERCLASSNAME");
CREATE INDEX TC_WORKITEM_INDEX1 ON TC_WORKITEM ("ACTIVITYID", "EXECUTIONSTATE", "OWNER");
CREATE INDEX TC_VARIABLE_INDEX1 ON TC_VARIABLE ("CONTAINERID", "CONTAINERCLASSNAME");

