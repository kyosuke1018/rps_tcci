CREATE SEQUENCE  "SEQ_LOG"  MINVALUE 1 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE ;

CREATE TABLE "MY_LOGIN_LOG" 
(
	"ID" NUMBER(18,0) NOT NULL ENABLE, 
	"USERNAME" VARCHAR2(50 BYTE) NOT NULL ENABLE, 
	"TGT" VARCHAR2(255 CHAR) NOT NULL ENABLE, 
	"CREATETIME" DATE NOT NULL ENABLE, 
	CONSTRAINT "MY_LOGIN_LOG_PK" PRIMARY KEY ("ID") ENABLE
);

CREATE TABLE "MY_SERVICE_LOG" 
(
	"ID" NUMBER(18,0) NOT NULL ENABLE, 
	"SERVICE" VARCHAR2(255 BYTE) NOT NULL ENABLE, 
	"TGT" VARCHAR2(255 CHAR) NOT NULL ENABLE, 
	"CREATETIME" DATE NOT NULL ENABLE, 
	CONSTRAINT "MY_SERVICE_LOG_PK" PRIMARY KEY ("ID") ENABLE
);

/*

select u.username,
case 
when INSTR(s.service, '/csrcprod')>0 then '中橡生產資訊'
when INSTR(s.service, '/dashboard/sales')>0 then '台泥業務業績預警'
when INSTR(s.service, '/dashboard/service/cementprod')>0 then '台泥生產資訊'
when INSTR(s.service, '/dashboard/service/cementsales')>0 then '台泥業務銷售'
when INSTR(s.service, '/dashboard/service/pdapatrolsum')>0 then 'PDA巡檢資訊'
when INSTR(s.service, '/dashboard/service/swlsService/list')>0 then '台泥採購預警'
when INSTR(s.service, '/dashboard/service/swlsService/shm')>0 then '物流運力資訊'
when INSTR(s.service, '/dashboard/service/vpphour')>0 then '窯磨運轉'
when INSTR(s.service, '/ics')>0 then '內控預警'
when INSTR(s.service, '/lms')>0 then '證照預警'
when INSTR(s.service, '/shipmentMonitor')>0 then '出貨車船監控'
when INSTR(s.service, '/SKSalesPortal')>0 then '景德業績預警'
when INSTR(s.service, '/tccprod')>0 then '環保資料預警'
else 'misc'
end as widget,
s.createtime,
s.service
from my_service_log s
left join my_login_log u on u.tgt=s.tgt
order by s.id desc;

update my_service_log
set tgt=REGEXP_REPLACE(tgt, '\s');

*/