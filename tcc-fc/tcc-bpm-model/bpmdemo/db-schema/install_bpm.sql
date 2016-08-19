CREATE TABLE  TC_TEAM (
  ID NUMBER(18)  NOT NULL,
  NAME VARCHAR2(200) NOT NULL ,
  DESCRIPTION VARCHAR2(1000 CHAR) DEFAULT NULL,
  CONSTRAINT TC_TEAM_PK PRIMARY KEY  (ID)
) ;

--
-- Table structure for table role
--

CREATE TABLE  TC_ROLE (
  ID NUMBER(18)  NOT NULL,
  NAME VARCHAR2(255) NOT NULL ,
  CONSTRAINT TC_ROLE_PK PRIMARY KEY  (ID)
) ;

--
-- Table structure for table tc_roleholderrolemap
--


CREATE TABLE  TC_ROLEHOLDERROLEMAP (
  ID NUMBER(18)  NOT NULL,
  HOLDERCLASSNAME VARCHAR2(255) NOT NULL ,
  HOLDERID NUMBER(18)  NOT NULL ,
  ROLE NUMBER(18)  NOT NULL ,
  CONSTRAINT TC_ROLEHOLDERROLEMAP_PK PRIMARY KEY  (ID),
  CONSTRAINT FK_ROLEHOLDERROLEMAP_1 FOREIGN KEY (ROLE) REFERENCES TC_ROLE (ID)
) ;


CREATE TABLE  TC_ROLEPRINCIPALLINK (
  ID NUMBER(18)  NOT NULL,
  ROLE NUMBER(18)  NOT NULL ,
  PRINCIPALCLASSNAME VARCHAR2(200) NOT NULL ,
  PRINCIPALID NUMBER(18)  NOT NULL ,
  CONSTRAINT TC_ROLEPRINCIPALLINK_PK PRIMARY KEY  (ID),
  CONSTRAINT UK_ROLEPRINCIPALLINK UNIQUE (ROLE,PRINCIPALCLASSNAME,PRINCIPALID),
  CONSTRAINT FK_ROLEPRINCIPALLINK_1 FOREIGN KEY (ROLE) REFERENCES TC_ROLE (ID) ON DELETE CASCADE
) ;


--
-- Process Template
--
CREATE TABLE TC_PROCESSTEMPLATE (
  ID NUMBER(18)  NOT NULL,
  PROCESSNAME VARCHAR2(255 CHAR) NOT NULL ,
  PROCESSVERSION VARCHAR2(20) NOT NULL ,
  DISABLED NUMBER(1) DEFAULT NULL,
  CONSTRAINT TC_PROCESSTEMPLATE_PK PRIMARY KEY (ID)
);


--
-- Process instance
--
CREATE TABLE TC_PROCESS (
  ID NUMBER(18)  NOT NULL ,
  PROCESSNAME VARCHAR2(255 CHAR) NOT NULL ,
  PROCESSTEMPLATEID NUMBER(18)  NOT NULL ,
  EXECUTIONSTATE VARCHAR2(255) NOT NULL,
  PRIMARYOBJECTCLASSNAME VARCHAR2(255) DEFAULT NULL,
  PRIMARYOBJECTID NUMBER(18)  DEFAULT NULL,
  STARTTIMESTAMP DATE DEFAULT NULL,
  ENDTIMESTAMP DATE DEFAULT NULL,
  CREATOR NUMBER(18)  DEFAULT NULL,
  TERMINATOR NUMBER(18),
  TERMINATE_REASON VARCHAR2(255 CHAR),
  TEAM NUMBER(18)  DEFAULT NULL,
  CONSTRAINT TC_PROCESS_PK PRIMARY KEY  (ID),
  CONSTRAINT FK_PROCESS_1 FOREIGN KEY (CREATOR) REFERENCES TC_USER (ID),
  CONSTRAINT FK_PROCESS_2 FOREIGN KEY (PROCESSTEMPLATEID) REFERENCES TC_PROCESSTEMPLATE (ID),
  CONSTRAINT FK_PROCESS_3 FOREIGN KEY (TEAM) REFERENCES TC_TEAM (ID) ON DELETE CASCADE,
  CONSTRAINT FK_PROCESS_4 FOREIGN KEY (TERMINATOR) REFERENCES TC_USER (ID)
) ;


--
-- task template
--

CREATE TABLE  TC_ACTIVITYTEMPLATE (
  ID NUMBER(18)  NOT NULL ,
  ACTIVITYNAME VARCHAR2(255 CHAR) NOT NULL ,
  ACTIVITYTYPE VARCHAR2(255) NOT NULL ,
  PROCESSTEMPLATEID NUMBER(18)  NOT NULL ,
  ROLENAME VARCHAR2(255),
  DEADLINE DATE,
  DURATION NUMBER(10),
  EXPRESSION CLOB,    
  NOTIFICATION NUMBER(1,0) DEFAULT 1 NOT NULL ENABLE,
  OPTIONS NUMBER(18) DEFAULT 0 NOT NULL,
  CONSTRAINT TC_ACTIVITYTEMPLATE_PK PRIMARY KEY  (ID),
  CONSTRAINT FK_ACTIVITYTEMPLATE_1 FOREIGN KEY (PROCESSTEMPLATEID) REFERENCES TC_PROCESSTEMPLATE (ID)
) ;

--
-- task instance
--

CREATE TABLE TC_ACTIVITY (
  ID NUMBER(18)  NOT NULL,
  PROCESSID NUMBER(18)  NOT NULL ,
  ACTIVITYNAME VARCHAR2(255 CHAR) NOT NULL ,
  ACTIVITYTYPE VARCHAR2(255) NOT NULL ,
  ACTIVITYTEMPLATEID NUMBER(18),
  ROLENAME VARCHAR2(255) DEFAULT NULL,
  EXECUTIONSTATE VARCHAR2(255) NOT NULL,
  DURATION NUMBER(10)  DEFAULT NULL,
  DEADLINE DATE DEFAULT NULL,
  EXPRESSION CLOB,  
  OPTIONS NUMBER(18) DEFAULT 0 NOT NULL,
  STARTTIMESTAMP DATE DEFAULT NULL,
  ENDTIMESTAMP DATE DEFAULT NULL,  
  CONSTRAINT TC_ACTIVITY_PK PRIMARY KEY (ID),
  CONSTRAINT FK_ACTIVITY_1 FOREIGN KEY (PROCESSID) REFERENCES TC_PROCESS (ID) ON DELETE CASCADE,
  CONSTRAINT FK_ACTIVITY_2 FOREIGN KEY (ACTIVITYTEMPLATEID) REFERENCES TC_ACTIVITYTEMPLATE (ID) ON DELETE CASCADE
) ;


--
-- task link in activity template
--

CREATE TABLE TC_ACTIVITYROUTETEMPLATE (
  ID NUMBER(18)  NOT NULL,
  FROMACTIVITY NUMBER(18)  NOT NULL ,
  TOACTIVITY NUMBER(18)  NOT NULL ,
  ROUTENAME VARCHAR2(255 CHAR) DEFAULT NULL,
  CONSTRAINT TC_ACTIVITYROUTETEMPLATE_PK PRIMARY KEY (ID),
  CONSTRAINT FK_ACTIVITYROUTETEMPLATE_1 FOREIGN KEY (FROMACTIVITY) REFERENCES TC_ACTIVITYTEMPLATE (ID) ON DELETE CASCADE,
  CONSTRAINT FK_ACTIVITYROUTETEMPLATE_2 FOREIGN KEY (TOACTIVITY) REFERENCES TC_ACTIVITYTEMPLATE (ID)
) ;

--
-- task link in activity instance
--
CREATE TABLE TC_ACTIVITYROUTE (
  ID NUMBER(18)  NOT NULL,
  FROMACTIVITY NUMBER(18)  NOT NULL,
  TOACTIVITY NUMBER(18)  NOT NULL,
  ROUTENAME VARCHAR2(255 CHAR) DEFAULT NULL,  
  TALLY NUMBER(10) DEFAULT NULL,
  CONSTRAINT TC_ACTIVITYROUTE_PK PRIMARY KEY (ID),
  CONSTRAINT FK_ACTIVITYLINK_1 FOREIGN KEY (FROMACTIVITY) REFERENCES TC_ACTIVITY (ID) ON DELETE CASCADE,
  CONSTRAINT FK_ACTIVITYLINK_2 FOREIGN KEY (TOACTIVITY) REFERENCES TC_ACTIVITY (ID) ON DELETE CASCADE
) ;


--
-- workitem in worklist
--
CREATE TABLE TC_WORKITEM (
  ID NUMBER(18)  NOT NULL ,
  ACTIVITYNAME VARCHAR2(255 CHAR) DEFAULT NULL,
  ACTIVITYID NUMBER(18)  NOT NULL ,
  EXECUTIONSTATE VARCHAR2(255) NOT NULL ,
  STARTTIMESTAMP DATE DEFAULT NULL,
  ENDTIMESTAMP DATE DEFAULT NULL,
  OWNER NUMBER(18)  DEFAULT NULL,
  BALLOT VARCHAR2(255) DEFAULT NULL,  
  CONSTRAINT TC_WORKITEM_PK PRIMARY KEY (ID),
  CONSTRAINT FK_WORKITEM_1 FOREIGN KEY (OWNER) REFERENCES TC_USER (ID),
  CONSTRAINT FK_WORKITEM_2 FOREIGN KEY (ACTIVITYID) REFERENCES TC_ACTIVITY (ID) ON DELETE CASCADE
) ;

CREATE TABLE TC_SIGNATURE (
  ID NUMBER(18)  NOT NULL ,
  PRIMARYOBJECTCLASSNAME VARCHAR2(255) DEFAULT NULL,
  PRIMARYOBJECTID NUMBER(18)  DEFAULT NULL,  
  BALLOT VARCHAR2(255) NOT NULL ,
  COMMENTS VARCHAR2(500 CHAR) DEFAULT NULL,
  CREATETIMESTAMP DATE DEFAULT NULL,
  CREATOR NUMBER(18)  DEFAULT NULL,
  WORKITEM NUMBER(18)  DEFAULT NULL,
  CONSTRAINT TC_SIGNATURE_PK PRIMARY KEY  (ID),
  CONSTRAINT FK_SIGNATURE_1 FOREIGN KEY (CREATOR) REFERENCES TC_USER (ID),
  CONSTRAINT FK_SIGNATURE_2 FOREIGN KEY (WORKITEM) REFERENCES TC_WORKITEM (ID)
) ;

CREATE TABLE TC_VARIABLE
   (	
    ID NUMBER(18,0) NOT NULL, 
	NAME VARCHAR2(255), 
	VALUE VARCHAR2(255), 
	CONTAINERCLASSNAME VARCHAR2(200)  NOT NULL, 
	CONTAINERID NUMBER(18,0)  NOT NULL,
	VISIBLE NUMBER(1,0)  DEFAULT 0 NOT NULL,
	CONSTRAINT TC_VARIABLE_PK PRIMARY KEY  (ID),
	CONSTRAINT UK_VARIABLE UNIQUE (CONTAINERCLASSNAME,CONTAINERID,NAME)
   ) ;



CREATE INDEX TC_ROLEHOLDERROLEMAP_INDEX1 ON TC_ROLEHOLDERROLEMAP ("HOLDERID", "HOLDERCLASSNAME");
CREATE INDEX TC_WORKITEM_INDEX1 ON TC_WORKITEM ("ACTIVITYID", "EXECUTIONSTATE", "OWNER");
CREATE INDEX TC_VARIABLE_INDEX1 ON TC_VARIABLE ("CONTAINERID", "CONTAINERCLASSNAME");
