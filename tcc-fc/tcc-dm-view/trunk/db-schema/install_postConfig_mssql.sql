-- 群組
UPDATE TC_GROUP SET NAME='系統管理員' WHERE ID=1;
INSERT INTO TC_GROUP (CODE,NAME,CREATOR,CREATETIMESTAMP) values ('switch-user','切換使用者', 1, getdate());
-- 自訂群組

-- 檔案上傳路徑
-- 測試機: D:\FileVault\<yourapp>\
-- QA機:   /opt/FileUpload/<yourapp>/
-- 正式機: /opt/FileUpload/<yourapp>/
UPDATE TC_FVVAULT SET LOCATION='D:\FileVault\dm\' where id=1;
UPDATE TC_FVVAULT SET LOCATION='E:\FileVault\dm\' where id=2;

-- 排程控管
-- INSERT INTO TC_SCHEDULE (NAME) VALUES ('CommonReportNotify');

