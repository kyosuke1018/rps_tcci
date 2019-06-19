/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.model.global;

import com.tcci.cm.enums.LocaleEnum;
import com.tcci.ec.enums.SmsProviderEnum;
import java.math.BigDecimal;

/**
 * 
 * 公用常數
 * @author Peter
 */
public class GlobalConstant {
    // 系統標題
    public static final String AP_NAME = "ec-admin";
    public static final String AP_TITLE = "台泥商城後台系統";
    public static final boolean SMS_ENABLED = true;// 注意：個別語系的SMS是否 ENABLED，還要於 JNDI 設定
    public static final String SMS_PRFIX = "TCC-EC ";
    public static final SmsProviderEnum SMS_PROVIDER = SmsProviderEnum.JIGUANG;
    
    // 功能開關
    public static final boolean SYNC_PASSWORD_EC10 = true;// 同步 EC1.0 密碼
    public static final boolean OWN_SINGLE_STORE = true;// 一帳號只可擁有單一商店 (仍可管理多家商店)
    public static final boolean SIMULATE_DENIED_UPDATE = true;// 模擬使用者不可異動資料  
    public static final boolean JWT_ENABLED = true;// RESTful JWT 保護
    public static final boolean QUOTE_TOTAL = false;// 訂單報總價
    public static final boolean QUANTITY_MODIFY = true;// 訂單改量
    public static final boolean TCC_DEALER_ENABLED = true;// 台泥經銷商專屬功能
    public static final boolean SHARE_SHIP_METHOD = true;// 商品共用商店送貨方式
    public static final boolean SHARE_PAY_METHOD = true;// 商品共用商店付款方式
    public static final boolean SHARE_PRD_TYPE = true;// 商店共用商品類型
    public static final boolean ACC_CASE_SENSITIVE = false;// 會員帳號區分大小
    
    public static final Long SHARE_STORE_ID = 0L;// 共用商店ID
    public static final int PRD_TYPE_LEVEL = 3;
    public static final String TCC_PRD_TYPE = "TCC";
    public static final int ENCRYPT_NUM = 20;
    public static final int MIN_PWD_LEN = 6; // 密碼最小長度
    public static final int MAX_PWD_LEN = 16; // 密碼最大長度
    public static final LocaleEnum DEF_LOCALE = LocaleEnum.TRADITIONAL_CHINESE;// 預設語系
    public static final String INTERNAL_PREFIX_SK = "IN-";// internal session key prefix (ec-admin EC_SESSION.SESSION_KEY)
    
    public static final int MAX_STORE_NUM = 10; // 單一賣家最大商店數
    public static final int DEF_FACTORY_GROUP_ID = 100; // 保留給特殊群組 < 100 (直接於DB建立的預設群祖)
    // 預設值
    public static final Long DEF_CURRENCY_ID = 1L; // EC_CURRENCY.ID (RMB)
    public static final BigDecimal DEF_PRD_PRICE_AMT = BigDecimal.ONE;

    public static final String JNDI_GLOBAL = "jndi/global.config";
    public static final String JNDI_NAME_PRIVATE = "jndi/ec-admin.config";
    public static final String JNDI_ADMIN_EMAIL = "admin.notify.email";// 系統管理員通知 EMAIL
    public static final String JNDI_PNAME_DIR_BIMAGE = "dir.image.big";
    public static final String JNDI_PNAME_DIR_SIMAGE = "dir.image.small";
    // jndi/global
    public static final String JNDI_DOCPUB_URL = "DOCPUB_URL";
    public static String DEF_PWD = "abcd1234";
    
    // 上傳檔資訊
    public static final int MAX_UPLOAD_IMG = 10 * 1024 * 1024;// 10MB
    public static final int MAX_UPLOAD_DOC = 10 * 1024 * 1024;// 10MB
    public static final int MAX_UPLOAD_HTML_IMG = 2 * 1024 * 1024;// 10MB
    // 圖片資訊
    public static final int WIDTH_SMALL_IMG = 300; // 自動縮圖小圖標準寬度
    public static final int HEIGHT_SMALL_IMG = 300; // 自動縮圖小圖標準高度
    public static final int WIDTH_BIG_IMG = 1600; // 自動縮圖大圖標準寬度
    public static final int HEIGHT_BIG_IMG = 1200; // 自動縮圖大圖標準高度
    
    public static final int WIDTH_HTML_IMG = 200;// 插入圖片預設縮放圖寬
    public static final int HEIGHT_HTML_IMG = 200;// 插入圖片預設縮放圖高
    public static final int WIDTH_ADMIN_IMG = 120;// 管理介面顯示圖寬
    public static final int HEIGHT_ADMIN_IMG = 90;// 管理介面顯示圖高
    // WEB_CS
    public static final String[] CS_EMP_FILTERS = 
            new String[]{"共用", "公用", "SAP", "會議室", "信箱", "人才召募"};
        
    // URL
    public static final String HOME_PAGE = "/";
    public static final String LOGOUT_PAGE = "/logout";
    public static final String URL_GET_IMAGE = "/ImageServlet";
    public static final String URL_WEB_ADMIN_LOGIN = "/loginAdmin.html";
    // PATH
    public static final String DIR_STORE_IMG = "/opt/FileUpload/ec/store";
    public static final String DIR_ADMIN_IMG = "/opt/FileUpload/ec/admin";
    public static final String DIR_IMPORT = "/opt/FileUpload/ec/temp";
    public static final String DIR_EXPORT = "/opt/FileTransfer/ec/temp";
    
    // File System
    public static final String FILE_SEPARATOR = "/"; // File.separator;
    public static final long DOMAIN_ID = 1;// Default TC_DOMAIN ID
    public static final String FVVAULT_HOST = "localhost";// fundation file vault host
    public static final String DOMAIN_NAME_DEF = "Default";
    
    public static final String FILE_ENCODING = "UTF-8";
    // 圖片FV DOMAIN_NAME
    public static final String DOMAIN_NAME_CUSTOM_IMG = "CustomImages";
    public static final String DOMAIN_NAME_DOC_IMG = "DocImages";

    public static final String ENCODING_DEF = "UTF-8";
    public static final String CONTENTTYPE_HTML = "text/html";
    
    // 文管系統 -- 使用手冊
    public static final String DOCPUB_PATH = "view.xhtml?file=";
    public static final String DOC_PATH_PREFIX = "/台訊/系統使用手冊/AP/電商1.5管理系統/";

    // Default Resource Bundle
    public static final String DEF_RESOURCE_BUNDLE = "messages";
    public static final String ENUM_RESOURCE = "enum";

    // 特殊 User Group
    public static final long UG_ADMINISTRATORS_ID = 1;
    public static final String UG_ADMINISTRATORS = "ADMINISTRATORS";
    public static final String UG_PLANT_USER = "PLANT_USER";// 廠端使用者(不可看到控管明細)

    // special restful result
    public static final String RS_RESULT_PARAM_ERROR = "查詢參數有誤";
    public static final String RS_RESULT_NO_PERMISSION = "NO_PERMISSION";
    public static final String RS_RESULT_NO_DATA = "NO_DATA";
    public static final String RS_RESULT_SUCCESS = "SUCCESS";
    public static final String RS_RESULT_FAIL = "FAIL";

    // 產生 Common Report 檔案副檔名
    public static final String EXCEL_FILEEXT = "xls";
    public static final String PDF_FILEEXT = "pdf";
    public static final String PDF_CONTENT_TYPE = "application/pdf";

    public static final int DEF_MAX_RESULT_SIZE = 10000;// 預設最大查詢筆數
    public static final int MAX_EXCEL_EXPORT_SIZE = 65535;

    public static final String FORMAT_DATE = "yyyy/MM/dd";
    public static final String FORMAT_TIME = "HH:mm:ss";
    public static final String FORMAT_DATETIME = "yyyy/MM/dd HH:mm:ss";
    public static final String FORMAT_DATETIME_STR = "yyyyMMddHHmmss";
    public static final String FORMAT_DATE_STR = "yyyyMMdd";
    public static final String FORMAT_TIME_STR = "HHmmss";
    public static final String FORMAT_YEAR = "yyyy";
    public static final String FORMAT_MONTH = "MM";
    public static final String FORMAT_DATE_HHMM = "yyyy/MM/dd HH:mm";
    public static final String FORMAT_CMNRPT_DATETIME = "yyyy-MM-dd-HH-mm";
    
    public static final String FORMAT_INTEGER = "###,###";
    public static final String FORMAT_NUMBER2 = "###,###.00";
               
    public static final String SYS_ORG_ROOT = "《所有組織》";

    public static final String SYS_BATCH_USER = "web.restful";// 系統排程程式執行者
    public static final int ALIVE_POLL_INTERVAL = 600; // 10 min
            
    public static final int MAX_RECURSION_LEVEL = 10;
    public static final int STR_MAX_SHOW_LEN = 12; // 內容最多顯示字數
    public static final int TXT_MAX_SHOW_LEN = 24; // 內容最多顯示字數
    public static final int MSG_MAX_SHOW_LEN = 30; // 訊息最多顯示字數
    
    // for import
    public static final String YES = "是";
    public static final String NO = "否";
    public static final String YES_CN = "是";
    public static final String NO_CN = "否";
    
    // for Cookie
    public static final int COOKIE_MAXAGE = 31 * 24 * 60 * 60; // 1 month
    public static final String COOKIE_NAME_OIL_CTRL = "OIL_CTRL";
    
    // for 公告檔目錄
    public static final String ANNOUNCEMENT_FOLDER = "announcement";
    
    // 總經理要求責任人相關欄位禁止輸入字眼
    public static final String[] DENIED_WORDS = new String[]{
        "台訊", "台泥資訊", "辜成允", "王琪玫", "黃健強", "呂克甫", "章健志", "董事長", "呂副總", "黃副總", "王副總", "呂資深副總", "黃資深副總", "王資深副總",
        "台讯", "台泥资讯", "辜成允", "王琪玫", "黄健强", "吕克甫", "章健志", "董事长", "吕副总", "黄副总", "王副总", "吕资深副总", "黄资深副总", "王资深副总"
    };
    
    
    // for 本機連至QAS (多筆TC_DOMAIN)
    public static long domain = DOMAIN_ID;
    public static String fvVaultHost = FVVAULT_HOST;
    public static String setFvVaultHost(String host){
        return fvVaultHost = host;
    }
    public static String getFvVaultHost(){
        return fvVaultHost;
    }
    public static long setDomain(long fvDomain){
        return domain = fvDomain;
    }
    public static long getDomain(){
        return domain;
    }
}
