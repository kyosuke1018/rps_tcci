/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.model.global;

import com.tcci.cm.enums.LocaleEnum;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 公用常數
 * @author Peter
 */
public class GlobalConstant {
    // 系統標題
    public static final String AP_TITLE = "陽光採購平台管理系統";
    public static final String AP_CODE = "ET";
    public static final boolean SMS_ENABLED = true;// 注意：個別語系的SMS是否 ENABLED，還要於 JNDI 設定
    public static final String SMS_PRFIX = "TCC-EC ";
    public static final boolean DEBUG_MODE = false; 
    public static final String DEBUG_USER = "peter.pan"; 
   
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
    
    public static final int MAX_STORE_NUM = 10; // 單一賣家最大商店數
    public static final int COMPLETE_USER_MAX = 15;
            
    // 預設值
    public static final Long DEF_CURRENCY_ID = 1L; // EC_CURRENCY.ID (RMB)
    public static final BigDecimal DEF_PRD_PRICE_AMT = BigDecimal.ONE;
    
    // JWT Security
    public static final int JWT_EXPIRED_MINUTE = 3 * 60;// ? 分鐘後過期
    public static final int JWT_REFRESH_MINUTE = 60;// 剩 ? 分鐘內可換發
    public static final String JWT_REFRESH_FLAG = "refresh_jwt";
    public static final String SECURITY_KEY = "HJ$%^GWQ#T2L<MDQ#6U&JS@#@^JUZ#@@LOOA65XLTD$@#%";// for PDA
    //  Seller Security
    //public static String PASSPHRASE = "the quick brown fox jumps over the lazy dog";
    //public static int ITERATION_COUNT= 100;
    public static int KEY_SIZE = 128;
    //public static String SALT = "3FF2EC019C627B945225DEBAD71A01B6985FE84C95A70EB132882F88C0A59A55";
    //public static String IV = "F27D5C9927726BCEFE7510B1BDD3D137";
    // Admin Security
    //public static String PASSPHRASE = "the quick brown fox jumps over the lazy dog";
    //public static int ADMIN_ITERATION_COUNT= 100;
    //public static int ADMIN_KEY_SIZE = 128;
    //public static String ADMIN_SALT = "3FF2EC019C627B945225DEBAD71A01B6985FE84C95A70EB132882F88C0A59A55";
    //public static String ADMIN_IV = "F27D5C9927726BCEFE7510B1BDD3D137";
    public static String DEF_PWD = "abcd1234";
    
    //client timeout
    public static final int REST_CONNECT_TIMEOUT = 3 * 10 * 000;// 30 seconds
    public static final int REST_READ_TIMEOUT = 2 * 60 * 1000;// 2 minutes
    
    // 長度限制
    public static final int MIN_MEM_ACC_LEN = 6;
    public static final int MAX_MEM_ACC_LEN = 20;
    // PATTEN
    public static final String PATTEN_MEM_ACC = "[A-Za-z0-9_\\.\\-]{6,20}";
    public static final String PATTEN_MEM_NAME = ".{2,60}";
    public static final String PATTEN_MEM_EMAIL = "^(.+)@(.+)$";
    public static final String PATTEN_MEM_PHONE = "[A-Za-z0-9\\+]{6,20}";
    public static final int VERIFY_CODE_LEN = 5;
    public static final int VERIFY_CODE_EXPIRED = 5;// MINUTE
    
    // 動態查詢條件限制
    public static final int DEF_DATE_CRITERIA_NUM = 2;
    public static final int MAX_DATE_CRITERIA_NUM = 6;
    public static final int DEF_CONTACTS_CRITERIA_NUM = 2;
    public static final int MAX_CONTACTS_CRITERIA_NUM = 6;
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
    // 影片資訊
    public static final int WIDTH_SMALL_VIDEO = 220; // 小影片寬度
    public static final int HEIGHT_SMALL_VIDEO = 155; // 小影片高度
    // 上傳筆數限制
    public static final int MAX_IMPORT_NUM = 100;

    // for JNDI
//    public static final String JNDI_NAME_PRIVATE = "jndi/ec-seller-serv.config";
    public static final String JNDI_NAME_PRIVATE = "jndi/et.config";
    public static final String JNDI_ADMIN_EMAIL = "admin.notify.email";// 系統管理員通知 EMAIL
    public static final String JNDI_PNAME_DIR_BIMAGE = "dir.image.big";
    public static final String JNDI_PNAME_DIR_SIMAGE = "dir.image.small";
    public static final String JNDI_URL_WEB_PREFIX = "url.web.prefix";// 前端網頁 URL 的前置詞
    public static final String JNDI_URL_SRV_PREFIX = "url.service.prefix";// 前端網頁連結的 Services 使用的 URL 的前置詞
    public static final String JNDI_URL_DOC_PREVIEW = "url.doc.preview";// 線上編輯文章，模擬真實網頁預覽
    public static final String JNDI_RECORD_DIR = "dir.record.upload"; // 保種紀錄上傳檔路徑
    public static final String JNDI_SSO_URL = "sso.auth.url";

    public static final String JNDI_GLOBAL = "jndi/global.config";
    public static final String JNDI_SAP_SERVICE_REST = "SAP_REST_ROOT";// for JCoService
    public static final String JNDI_SAP_FILE_DOWNLOAD_FOLDER = "SAP_FILE.DOWNLOAD_FOLDER"; //for JCoService Download Sap Files
    public static final String JNDI_PATH_JCOSERVER_REST = "jcoserver.rest.root";// for TCJCoServer in jndi/pp.config
    public static final String JNDI_PATH_HRORG_REST = "hrorg.rest.root";// for hrorg in jndi/pp.config
    public static final String REST_HRORG_APPROVER = "approver";
    
    public static final String NOTIFY_ADMIN_EMAIL = "kyle.cheng@tcci.com.tw";// utils 中Exception通知EMAIL
    // jndi/global
    public static final String JNDI_DOCPUB_URL = "DOCPUB_URL";
    
    // restful
    public static final String EMAIL_SUBJECT_PREFIX = "【陽光採購平台】";// EMAIL 主旨前置詞
    //public static final String EMAIL_NOTICE_TEMPLATE_FILE = "contract_summary.vm";// Email 樣板
        
    // URL
    public static final String URL_HOME_PAGE = "home.html";
    public static final String URL_SELLER_LOGIN_PAGE = "/login.html";
    public static final String URL_DEALER_LOGIN_PAGE = "/loginDealer.html";
    public static final String URL_LOGOUT_PAGE = "/logout";
    public static final String URL_GET_IMAGE = "/ImageServlet";
    public static final String URL_GET_FILE = "/resources/download/fv";
                    
    // PATH
//    public static final String DIR_STORE_IMG = "/opt/FileUpload/ec/store";
    public static final String DIR_ADMIN_IMG = "/opt/FileUpload/et/admin";
    public static final String DIR_IMPORT = "/opt/FileUpload/et/temp";
    public static final String DIR_EXPORT = "/opt/FileTransfer/et/temp";

    // 文管系統 -- 使用手冊
    public static final String DOCPUB_PATH = "view.xhtml?file=";
    public static final String DOC_PATH_PREFIX = "/台訊/系統使用手冊/AP/信昌網站資訊管理系統/";

    // Default Resource Bundle
    public static final String DEF_RESOURCE_BUNDLE = "messages";
    public static final String ENUM_RESOURCE = "enum";

    // 特殊 User Group
    public static final long UG_ADMINISTRATORS_ID = 1;
    public static final String UG_ADMINISTRATORS = "ADMINISTRATORS";

    // special restful result
    public static final String RS_RESULT_PARAM_ERROR = "查詢參數有誤";
    public static final String RS_RESULT_NO_PERMISSION = "NO_PERMISSION";
    public static final String RS_RESULT_NO_DATA = "NO_DATA";
    public static final String RS_RESULT_SUCCESS = "SUCCESS";
    public static final String RS_RESULT_FAIL = "FAIL";
    public static final int RS_RESULT_MAX_ROWS = 100;

    // 其他相關 AP 代號

    // 產生 Report 檔案副檔名
    public static final String EXCEL_FILEEXT = "xls";
    public static final String PDF_FILEEXT = "pdf";
    public static final String PDF_CONTENT_TYPE = "application/pdf";
    public static final String IMG_JPG_CONTENT_TYPE = "image/jpeg";

    public static final int DEF_MAX_RESULT_SIZE = 10000;// 預設最大查詢筆數
    public static final int MAX_EXCEL_EXPORT_SIZE = 65535;
    public static final int DEF_FACTORY_GROUP_ID = 100; // 保留給特殊群組 < 100 (直接於DB建立的預設群祖)

    public static final String FORMAT_DATE = "yyyy-MM-dd";
    public static final String FORMAT_TIME = "HH:mm:ss";
    public static final String FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_DATETIME_STR = "yyyyMMddHHmmss";
    public static final String FORMAT_DATETIME_HM_STR = "yyyyMMddHHmm";
    public static final String FORMAT_DATE_STR = "yyyyMMdd";
    public static final String FORMAT_TIME_STR = "HHmmss";
    public static final String FORMAT_YEAR = "yyyy";
    public static final String FORMAT_MONTH = "MM";
    public static final String FORMAT_DATE_HHMM = "yyyy-MM-dd HH:mm";
    public static final String FORMAT_CMNRPT_DATETIME = "yyyy-MM-dd-HH-mm";
    
    public static final String FORMAT_INTEGER = "###,###";
    public static final String FORMAT_NUMBER2 = "###,###.00";

    public static final int MAX_RECURSION_LEVEL = 10;
    public static final int STR_MAX_SHOW_LEN = 12; // 內容最多顯示字數
    public static final int TXT_MAX_SHOW_LEN = 24; // 內容最多顯示字數
    public static final int MSG_MAX_SHOW_LEN = 30; // 訊息最多顯示字數
    
    // File System
    public static final String FILE_SEPARATOR = "/"; // File.separator;
    //public static final long DOMAIN_ID = 1;// Default TC_DOMAIN ID
    public static final String FVVAULT_HOST = "localhost";// fundation file vault host
    public static final String DOMAIN_NAME_DEF = "Default";
    // 圖片FV DOMAIN_NAME
    public static final String DOMAIN_NAME_CUSTOM_IMG = "CustomImages";
    public static final String DOMAIN_NAME_DOC_IMG = "DocImages";

    public static final String ENCODING_DEF = "UTF-8";
    public static final String CONTENTTYPE_HTML = "text/html";
    public static final String RICHCONTENT_FILENAME = "RichContent";
    public static final String FILE_EXT_HTML = ".html";
    public static final int RICHCONTENT_SUMMARY_LEN = 3000;// for admin UI query (DB 只存過濾 TAG 後的部分資料BYTR長度)
    
    // for import
    public static final String[] YES = new String[]{"是", "是", "YES", "Y"};
    public static final String[] NO = new String[]{"否", "否", "NO", "N"};

    // for Cookie
    public static final int COOKIE_MAXAGE = 31 * 24 * 60 * 60; // 1 month
    
    // for 公告檔目錄
    public static final String ANNOUNCEMENT_FOLDER = "announcement";

    public static final boolean FIX_IMG_EDITABLED = false;// 固定網站圖片可後台維護
    public static final String URL_VIDEO_VIEW1 = "://youtu.be/"; // ignore http or https
    public static final String URL_VIDEO_VIEW2 = "://www.youtube.com/watch?v="; // ignore http or https
    public static final String URL_VIDEO_EMBED = "://www.youtube.com/embed/"; // ignore http or https

    // WEB_CS
    public static final String[] CS_EMP_FILTERS = 
            new String[]{"共用", "公用", "SAP", "會議室", "信箱", "人才召募"};
    
    // for 本機連至QAS (多筆TC_DOMAIN)
    //public static long domain = DOMAIN_ID;
    public static String fvVaultHost = FVVAULT_HOST;

    // 系統預設選項 KB_PLANTOPTIONS.READONLY = 1
    public static final List<Long> SYS_OPTIONS = new ArrayList<Long>(); 

    static {
    }
    
    public static String setFvVaultHost(String host){
        return fvVaultHost = host;
    }
    public static String getFvVaultHost(){
        return fvVaultHost;
    }
}
