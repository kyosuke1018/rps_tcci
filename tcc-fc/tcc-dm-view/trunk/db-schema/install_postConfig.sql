-- 群組
UPDATE TC_GROUP SET NAME='系統管理員' WHERE ID=1;
INSERT INTO TC_GROUP (ID,CODE,NAME,CREATOR,CREATETIMESTAMP) values (2, 'switch-user','切換使用者', 1, sysdate);
-- 自訂群組

-- 檔案上傳路徑
-- 測試機: D:\FileVault\<yourapp>\
-- QA機:   /opt/FileUpload/<yourapp>/
-- 正式機: /opt/FileUpload/<yourapp>/
UPDATE TC_FVVAULT SET LOCATION='D:\FileVault\dm\' WHERE ID=1;
UPDATE TC_FVVAULT SET LOCATION='D:\FileVault\dm\TrashCan' where id=2;

-- 排程控管
-- INSERT INTO TC_SCHEDULE (NAME) VALUES ('CommonReportNotify');

COMMIT;
