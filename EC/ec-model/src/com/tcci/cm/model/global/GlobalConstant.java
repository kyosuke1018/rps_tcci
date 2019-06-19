/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.model.global;

import com.tcci.cm.enums.LocaleEnum;
import com.tcci.ec.enums.SmsProviderEnum;
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
    public static final String AP_TITLE = "台泥電商賣家後台管理系統";
    public static final boolean SMS_ENABLED = true;// 注意：個別語系的SMS是否 ENABLED，還要於 JNDI 設定
    public static final String SMS_PRFIX = "TCC-EC ";
    
    // 功能開關
    public static final SmsProviderEnum SMS_PROVIDER = SmsProviderEnum.JIGUANG;
    public static final boolean OWN_SINGLE_STORE = true;// 一帳號只可擁有單一商店 (仍可管理多家商店)
    public static final boolean SIMULATE_DENIED_UPDATE = true;// 模擬使用者不可異動資料  
    public static final boolean JWT_ENABLED = true;// RESTful JWT 保護
    public static final boolean SHARE_PRD_TYPE = true;// 共用商品類型
    
    public static final Long SHARE_STORE_ID = 0L;// 共用商店ID
    public static final int PRD_TYPE_LEVEL = 3;
    public static final String TCC_PRD_TYPE = "TCC";
    public static final int ENCRYPT_NUM = 20;
    public static final int MIN_PWD_LEN = 6; // 密碼最小長度
    public static final int MAX_PWD_LEN = 16; // 密碼最大長度
    public static final LocaleEnum DEF_LOCALE = LocaleEnum.TRADITIONAL_CHINESE;// 預設語系
    
    public static final int MAX_STORE_NUM = 10; // 單一賣家最大商店數
            
    // 預設值
    public static final Long DEF_CURRENCY_ID = 1L; // EC_CURRENCY.ID (RMB)
    public static final BigDecimal DEF_PRD_PRICE_AMT = BigDecimal.ONE;
    
    // JWT Security
    public static final String SECURITY_KEY = "HJ$%^GWQ#T2L<MDQ#6U&JS@#@^JUZ#@@LOOA65XLTD$@#%";// for PDA
    //  Seller Security
    //public static String PASSPHRASE = "the quick brown fox jumps over the lazy dog";
    public static int ITERATION_COUNT= 100;
    public static int KEY_SIZE = 128;
    public static String SALT = "3FF2EC019C627B945225DEBAD71A01B6985FE84C95A70EB132882F88C0A59A55";
    public static String IV = "F27D5C9927726BCEFE7510B1BDD3D137";
    
    // Admin Security
    //public static String PASSPHRASE = "the quick brown fox jumps over the lazy dog";
    public static int ADMIN_ITERATION_COUNT= 100;
    public static int ADMIN_KEY_SIZE = 128;
    public static String ADMIN_SALT = "3FF2EC019C627B945225DEBAD71A01B6985FE84C95A70EB132882F88C0A59A55";
    public static String ADMIN_IV = "F27D5C9927726BCEFE7510B1BDD3D137";
    
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
    public static final int MAX_IMPORT_NUM = 2000;
    
    // 功能上線開關

    // for JNDI
    public static final String JNDI_NAME_PRIVATE = "jndi/ec.config";
    public static final String JNDI_ADMIN_EMAIL = "admin.notify.email";// 系統管理員通知 EMAIL
    public static final String JNDI_PNAME_DIR_BIMAGE = "dir.image.big";
    public static final String JNDI_PNAME_DIR_SIMAGE = "dir.image.small";
    public static final String JNDI_URL_WEB_PREFIX = "url.web.prefix";// 前端網頁 URL 的前置詞
    public static final String JNDI_URL_SRV_PREFIX = "url.service.prefix";// 前端網頁連結的 Services 使用的 URL 的前置詞
    public static final String JNDI_URL_DOC_PREVIEW = "url.doc.preview";// 線上編輯文章，模擬真實網頁預覽
    public static final String JNDI_RECORD_DIR = "dir.record.upload"; // 保種紀錄上傳檔路徑
    public static final String JNDI_SSO_URL = "sso.auth.url";
    // jndi/global
    public static final String JNDI_DOCPUB_URL = "DOCPUB_URL";
    
    // restful
    public static final String EMAIL_SUBJECT_PREFIX = "【電商賣家後台系統】";// EMAIL 主旨前置詞
    //public static final String EMAIL_NOTICE_SUBJECT = "待辦事項通知(合約維護)";
    //public static final String EMAIL_NOTICE_TEMPLATE_FILE = "contract_summary.vm";// Email 樣板
        
    // URL
    public static final String URL_HOME_PAGE = "home.html";
    public static final String URL_LOGOUT_PAGE = "/logout";
    public static final String URL_GET_IMAGE = "/ImageServlet";
    public static final String URL_GET_FILE = "/resources/download/fv";
    public static final String URL_VIDEO_VIEW1 = "://youtu.be/"; // ignore http or https
    public static final String URL_VIDEO_VIEW2 = "://www.youtube.com/watch?v="; // ignore http or https
    public static final String URL_VIDEO_EMBED = "://www.youtube.com/embed/"; // ignore http or https
                    
    // PATH
    public static final String DIR_STORE_IMG = "/opt/FileUpload/ec/store";
    public static final String DIR_ADMIN_IMG = "/opt/FileUpload/ec/admin";
    public static final String DIR_IMPORT = "/opt/FileUpload/ec/temp";
    
    // 文管系統 -- 使用手冊
    public static final String DOCPUB_PATH = "view.xhtml?file=";
    public static final String DOC_PATH_PREFIX = "/台訊/系統使用手冊/AP/信昌網站資訊管理系統/";

    // Default Resource Bundle
    public static final String DEF_RESOURCE_BUNDLE = "messages";
    public static final String ENUM_RESOURCE = "enum";

    // 特殊 User Group
    public static final long UG_ADMINISTRATORS_ID = 1;
    public static final String UG_ADMINISTRATORS = "ADMINISTRATORS";
    public static final String UG_TPCC_USER = "TPCC_USER";// 保重中心使用者

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

    // WEB_CS
    public static final String[] CS_EMP_FILTERS = 
            new String[]{"共用", "公用", "SAP", "會議室", "信箱", "人才召募"};
    public static final String TPCC_CS_COM_NAME = "信昌化學工業股份有限公司";
    
    // for import
    public static final String YES = "是";
    public static final String NO = "否";
    public static final String YES_CN = "是";
    public static final String NO_CN = "否";

    // for Cookie
    public static final int COOKIE_MAXAGE = 31 * 24 * 60 * 60; // 1 month
    //public static final String COOKIE_NAME_OIL_CTRL = "OIL_CTRL";
    
    // for 公告檔目錄
    public static final String ANNOUNCEMENT_FOLDER = "announcement";
    
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
