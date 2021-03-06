CREATE TABLE "TC_DOWNLOAD_LOG" 
(
"ID" NUMBER(19) NOT NULL, 
"TC_APPLICATIONDATA_ID" NUMBER(19),
"TOTAL_COUNT" NUMBER(6) default 0, 
"CREATOR" NUMBER(19) ,
"CREATETIMESTAMP" DATE,
"MODIFIER" NUMBER(19),
"MODIFYTIMESTAMP" DATE,
CONSTRAINT "TC_DOWNLOAD_LOG_PK" PRIMARY KEY ("ID"),
CONSTRAINT "TC_DOWNLOAD_LOG_FK1" FOREIGN KEY ("CREATOR") REFERENCES "TC_USER"("ID"),
CONSTRAINT "TC_DOWNLOAD_LOG_FK2" FOREIGN KEY ("MODIFIER") REFERENCES "TC_USER"("ID")
);

COMMENT ON COLUMN "TC_DOWNLOAD_LOG"."ID" IS 'PK';
COMMENT ON COLUMN "TC_DOWNLOAD_LOG"."TC_APPLICATIONDATA_ID" IS 'FK:TC_APPLICATIONDATA.ID';
COMMENT ON COLUMN "TC_DOWNLOAD_LOG"."TOTAL_COUNT" IS '總下載次數';
COMMENT ON COLUMN "TC_DOWNLOAD_LOG"."CREATOR" IS '建立者';
COMMENT ON COLUMN "TC_DOWNLOAD_LOG"."CREATETIMESTAMP" IS '建立時間';
COMMENT ON COLUMN "TC_DOWNLOAD_LOG"."MODIFIER" IS '修改者';
COMMENT ON COLUMN "TC_DOWNLOAD_LOG"."MODIFYTIMESTAMP" IS '修改時間';

CREATE TABLE "TC_DOWNLOAD_LOG_DETAIL" 
(
"ID" NUMBER(19),
"TC_DOWNLOAD_LOG_ID" NUMBER(19),
"CREATOR" NUMBER(19),
"CREATETIMESTAMP" DATE,
CONSTRAINT "TC_DOWNLOAD_LOG_DETAIL_PK" PRIMARY KEY ("ID"),
CONSTRAINT "TC_DOWNLOAD_LOG_DETAIL_FK1" FOREIGN KEY ("TC_DOWNLOAD_LOG_ID") REFERENCES "TC_DOWNLOAD_LOG"("ID"),
CONSTRAINT "TC_DOWNLOAD_LOG_DETAIL_FK2" FOREIGN KEY ("CREATOR") REFERENCES "TC_USER"("ID")
);

COMMENT ON COLUMN "TC_DOWNLOAD_LOG_DETAIL"."ID" IS 'PK';
COMMENT ON COLUMN "TC_DOWNLOAD_LOG_DETAIL"."TC_DOWNLOAD_LOG_ID" IS 'FK:TC_DOWNLOAD_LOG.ID';
COMMENT ON COLUMN "TC_DOWNLOAD_LOG_DETAIL"."CREATOR" IS '建立者';
COMMENT ON COLUMN "TC_DOWNLOAD_LOG_DETAIL"."CREATETIMESTAMP" IS '建立時間';
