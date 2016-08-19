-- 群組
UPDATE TC_GROUP SET NAME='系統管理員' WHERE ID=1;
INSERT INTO TC_GROUP (ID,CODE,NAME,CREATOR,CREATETIMESTAMP) values (2, 'switch-user','切換使用者', 1, sysdate);
-- 自訂群組

-- 檔案上傳路徑
-- 測試機: D:\FileVault\<yourapp>\
-- QA機:   /opt/FileUpload/<yourapp>/
-- 正式機: /opt/FileUpload/<yourapp>/
-- UPDATE TC_FVVAULT SET LOCATION='D:\FileVault\MyGUI\';
UPDATE TC_FVVAULT SET LOCATION='/opt/FileUpload/tccstore/';

-- 排程控管
INSERT INTO TC_SCHEDULE (NAME, ACTIVE) VALUES ('autoOrderApprove', 1);
INSERT INTO TC_SCHEDULE (NAME, ACTIVE) VALUES ('cancelExpiredOrder', 1);
INSERT INTO TC_SCHEDULE (NAME, ACTIVE) VALUES ('dailyContractSync', 1);
INSERT INTO TC_SCHEDULE (NAME, ACTIVE) VALUES ('dailyDeliveryPlaceSync', 1);
INSERT INTO TC_SCHEDULE (NAME, ACTIVE) VALUES ('dailyLoginReward', 1);
INSERT INTO TC_SCHEDULE (NAME, ACTIVE) VALUES ('dailySyncOrderStatus', 1);
INSERT INTO TC_SCHEDULE (NAME, ACTIVE) VALUES ('openOrdersNotify', 1);
COMMIT;

-- 台泥夥伴上傳圖片, TC_USER是NULL
ALTER TABLE TC_APPLICATIONDATA MODIFY CREATOR NULL;
ALTER TABLE TC_FVITEM MODIFY CREATOR NULL;
