CREATE TABLE TC_USER 
(	
    ID INT NOT NULL IDENTITY, 
    LOGIN_ACCOUNT NVARCHAR(60) NOT NULL, 
    EMAIL NVARCHAR(60), 	
    EMP_ID NVARCHAR(20),
    CNAME NVARCHAR(20),
    DISABLED BIT, 	
    CREATOR INT ,
    CREATETIMESTAMP DATETIME,	
    CONSTRAINT TC_USER_PK PRIMARY KEY (ID),
    CONSTRAINT TC_USER_UK UNIQUE (LOGIN_ACCOUNT),
    CONSTRAINT TC_USER_FK FOREIGN KEY (CREATOR) REFERENCES TC_USER(ID)
);

CREATE TABLE TC_GROUP 
(	
    ID INT NOT NULL IDENTITY, 
    CODE NVARCHAR(30) NOT NULL, 
    NAME NVARCHAR(90) NOT NULL, 
    CREATOR INT, 
    CREATETIMESTAMP DATETIME,
    CONSTRAINT TC_GROUP_PK PRIMARY KEY (ID),
    CONSTRAINT TC_GROUP_UK UNIQUE (CODE),
    CONSTRAINT TC_GROUP_FK FOREIGN KEY (CREATOR) REFERENCES TC_USER(ID)
);

CREATE TABLE TC_USERGROUP 
(	
    ID INT NOT NULL IDENTITY, 
    USER_ID INT NOT NULL, 
    GROUP_ID INT DEFAULT 1 NOT NULL, 
    CREATOR INT , 
    CREATETIMESTAMP DATETIME,
    CONSTRAINT TC_USERGROUP_PK PRIMARY KEY (ID),
    CONSTRAINT TC_USERGROUP_FK1 FOREIGN KEY (GROUP_ID) REFERENCES TC_GROUP (ID), 
    CONSTRAINT TC_USERGROUP_FK2 FOREIGN KEY (USER_ID) REFERENCES TC_USER (ID),
    CONSTRAINT TC_USERGROUP_FK3 FOREIGN KEY (CREATOR) REFERENCES TC_USER (ID)
);

CREATE TABLE TC_APPLICATIONDATA 
(	
   ID INT NOT NULL IDENTITY, 
	CONTAINERCLASSNAME NVARCHAR(255) DEFAULT NULL, 
	CONTAINERID INT DEFAULT NULL, 
	CONTENTROLE NVARCHAR(1) DEFAULT 'P', 
	CREATOR INT NOT NULL, 
	CREATETIMESTAMP DATETIME DEFAULT GETDATE() NOT NULL, 
	DESCRIPTION NVARCHAR(1000), 
	FVITEM INT NOT NULL, 
	CONSTRAINT TC_APPLICATIONDATA_PK PRIMARY KEY (ID),
	CONSTRAINT TC_APPLICATIONDATA_FK1 FOREIGN KEY (CREATOR) REFERENCES TC_USER (ID) 
);

CREATE TABLE TC_DOMAIN 
(	
  ID INT NOT NULL IDENTITY, 
	NAME NVARCHAR(600) NOT NULL, 
	DESCRIPTION NVARCHAR(100) DEFAULT NULL, 
	CONSTRAINT TC_DOMAIN_PK PRIMARY KEY (ID)
);

CREATE TABLE TC_FVITEM 
(	
  ID INT NOT NULL IDENTITY, 
	NAME NVARCHAR(255) NOT NULL, 
	FILENAME NVARCHAR(255) NOT NULL, 
	FILESIZE INT NOT NULL, 
	CONTENTTYPE NVARCHAR(255) DEFAULT NULL, 
	DOMAIN INT NOT NULL, 
	CREATOR INT NOT NULL, 
	CREATETIMESTAMP DATETIME DEFAULT GETDATE() NOT NULL, 
    CONSTRAINT FUL_FVITEM_PK PRIMARY KEY (ID),
  	CONSTRAINT TC_FVITEM_FK1 FOREIGN KEY (DOMAIN) REFERENCES TC_DOMAIN (ID),
	CONSTRAINT TC_FVITEM_FK2 FOREIGN KEY (CREATOR) REFERENCES TC_USER (ID) 
);

CREATE TABLE TC_FVVAULT 
(	
  ID INT NOT NULL IDENTITY, 
	NAME NVARCHAR(255) DEFAULT NULL, 
	HOSTNAME NVARCHAR(255) NOT NULL, 
	DOMAIN INT NOT NULL, 
	LOCATION NVARCHAR(600) NOT NULL, 
	READONLY BIT DEFAULT 0 NOT NULL, 
	ENABLED BIT DEFAULT 1 NOT NULL, 
	URL NVARCHAR(600), 
	 CONSTRAINT FUL_FVVAULT_PK PRIMARY KEY (ID),
	 CONSTRAINT TC_FVVAULT_FK1 FOREIGN KEY (DOMAIN) REFERENCES TC_DOMAIN (ID)
);

INSERT INTO TC_DOMAIN (NAME,DESCRIPTION) VALUES ('Default','Default');
INSERT INTO TC_DOMAIN (NAME,DESCRIPTION) VALUES ('TrashCan','TrashCan');

INSERT INTO TC_FVVAULT (NAME,HOSTNAME,DOMAIN,LOCATION,READONLY,ENABLED,URL) values ('Default','localhost',1,'D:\FileVault\APP\Default\',0,1,null);
INSERT INTO TC_FVVAULT (NAME,HOSTNAME,DOMAIN,LOCATION,READONLY,ENABLED,URL) VALUES ('Trash Can','localhost',2,'E:\FileVault\APP\TrashCan\',0,1,NULL);

INSERT INTO TC_USER (LOGIN_ACCOUNT,CNAME,DISABLED,CREATOR,CREATETIMESTAMP) VALUES ('administrator','',0,1,GETDATE());
INSERT INTO TC_USER (LOGIN_ACCOUNT,CNAME,DISABLED,CREATOR,CREATETIMESTAMP) VALUES ('jimmy.lee',N'������',0,1,GETDATE());
INSERT INTO TC_USER (LOGIN_ACCOUNT,CNAME,DISABLED,CREATOR,CREATETIMESTAMP) VALUES ('gilbert.lin',N'�L����',0,1,GETDATE());
INSERT INTO TC_USER (LOGIN_ACCOUNT,CNAME,DISABLED,CREATOR,CREATETIMESTAMP) VALUES ('jackson.lee',N'�����M',0,1,GETDATE());
INSERT INTO TC_USER (LOGIN_ACCOUNT,CNAME,DISABLED,CREATOR,CREATETIMESTAMP) VALUES ('neo.fu',N'�Ŭf�Q',0,1,GETDATE());
INSERT INTO TC_USER (LOGIN_ACCOUNT,CNAME,DISABLED,CREATOR,CREATETIMESTAMP) VALUES ('wayne.hu',N'�J�˺�',0,1,GETDATE());
INSERT INTO TC_USER (LOGIN_ACCOUNT,CNAME,DISABLED,CREATOR,CREATETIMESTAMP) VALUES ('edwin.ho',N'�󪢹B',0,1,GETDATE());
INSERT INTO TC_USER (LOGIN_ACCOUNT,CNAME,DISABLED,CREATOR,CREATETIMESTAMP) VALUES ('yen-chi.sun',N'�]���Q',0,1,GETDATE());
INSERT INTO TC_USER (LOGIN_ACCOUNT,CNAME,DISABLED,CREATOR,CREATETIMESTAMP) VALUES ('david.jen',N'���j��',0,1,GETDATE());
INSERT INTO TC_USER (LOGIN_ACCOUNT,CNAME,DISABLED,CREATOR,CREATETIMESTAMP) VALUES ('kyle.cheng',N'�G�ǻ�',0,1,GETDATE());
INSERT INTO TC_USER (LOGIN_ACCOUNT,CNAME,DISABLED,CREATOR,CREATETIMESTAMP) VALUES ('peter.pan',N'��۬F',0,1,GETDATE());

INSERT INTO TC_GROUP (CODE,NAME,CREATOR,CREATETIMESTAMP) values ('ADMINISTRATORS','Administrators Group',1,GETDATE());

INSERT INTO TC_USERGROUP (USER_ID,GROUP_ID,CREATOR,CREATETIMESTAMP) values (1,1,1,GETDATE());
INSERT INTO TC_USERGROUP (USER_ID,GROUP_ID,CREATOR,CREATETIMESTAMP) values (2,1,1,GETDATE());
INSERT INTO TC_USERGROUP (USER_ID,GROUP_ID,CREATOR,CREATETIMESTAMP) values (3,1,1,GETDATE());
INSERT INTO TC_USERGROUP (USER_ID,GROUP_ID,CREATOR,CREATETIMESTAMP) values (4,1,1,GETDATE());
INSERT INTO TC_USERGROUP (USER_ID,GROUP_ID,CREATOR,CREATETIMESTAMP) values (5,1,1,GETDATE());
INSERT INTO TC_USERGROUP (USER_ID,GROUP_ID,CREATOR,CREATETIMESTAMP) values (6,1,1,GETDATE());
INSERT INTO TC_USERGROUP (USER_ID,GROUP_ID,CREATOR,CREATETIMESTAMP) values (7,1,1,GETDATE());
INSERT INTO TC_USERGROUP (USER_ID,GROUP_ID,CREATOR,CREATETIMESTAMP) values (8,1,1,GETDATE());
INSERT INTO TC_USERGROUP (USER_ID,GROUP_ID,CREATOR,CREATETIMESTAMP) values (9,1,1,GETDATE());
INSERT INTO TC_USERGROUP (USER_ID,GROUP_ID,CREATOR,CREATETIMESTAMP) values (10,1,1,GETDATE());
INSERT INTO TC_USERGROUP (USER_ID,GROUP_ID,CREATOR,CREATETIMESTAMP) values (11,1,1,GETDATE());
