package com.tcci.solr.client.conf;

//import org.apache.solr.search.SolrReturnFields;

import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 系統常數設定
 * @author Peter
 */
public class TcSolrConfig {
    private static final Logger logger = LoggerFactory.getLogger(TcSolrConfig.class);
    
    // for JNDI and System Properties
    private static final String PRIVATE_JNDI_NAME = "jndi/solrClient.config";
    private static final String KEY_SOLR_SERVICE_URL = "solrURL";
    private static final String KEY_SOURCE_AP = "sourceAP";
    private static final String KEY_SSO_ENABLED = "ssoEnabled";
    
    // system properties
    private static final String KEY_REST_USER = "com.taiwancement.sso.restfulUser";
    private static final String KEY_REST_PWD = "com.taiwancement.sso.restfulPwd";
    private static final String KEY_SSO_SERVER = "com.taiwancement.sso.serverUrlPrefix";
    private static final String KEY_SOLR_SERVER = "com.taiwancement.solr.serverUrl";
    
    // for Solr Server
    //public final static String DEF_SERVER = "http://localhost:8983/solr";
    public final static String DEF_SOLR_SERVER = "http://localhost:8080/solr";// Default Value // get from System.getProperty or JNDI
    public final static String SOLR_URL_EXTRACT = "/update/extract";
    public final static String SOLR_URL_UPLOAD = "/lazyExtract";
    public final static String SOLR_URL_DELETE = "/safeDelete";

    // SSO
    public final static boolean SSO_ENABLED = true;
    public final static String CAS_SERVER = "http://192.168.203.81/cas-server";// Default Value // System.getProperty
    public final static String CAS_TICKET_URI = "/v1/tickets";
    public final static String SOURCE_AP = "SolrWebDemo";// Default Value // get from ServletContextListener Or JNDI
    public final static String DEF_SSO_USER = "peter.pan";// Default Value // get from System.getProperty
    public final static String DEF_SSO_PWD = "abcD1234";// Default Value // get from System.getProperty
    
    public final static boolean SSO_SUPPORT_POST = false;// SSO 是否支援 POST

    // Solr connetion
    public final static int DEF_CONNECT_TIMEOUT = 10000; // 5 sec.
    public final static int DEF_MAX_CONNECT_PER_HOST = 100;
    public final static int DEF_MAX_TOTAL_CONNECT = 500;
    
    public final static int DEF_MAX_RESULT_ROWS = 100;// 查詢最大回傳筆數(多欄位資料)
    public final static int DEF_MAX_CID_ROWS = 10000;// 查詢最大回傳筆數(只回 custom id 資料)
    
    // solr operator (no defined in core)
    public final static String OP_SOLR_EQUALS = ":";
    public final static String OP_SOLR_BOOTS = "^";
    public static final String[] SOLR_QUERY_OPERATORS 
            = {"+", "-", "&&", "||", "!", "(", ")", "{", "}", "[", "]", "^", "\"", "~", "*", "?", ":", "/"};
    public final static String SOLR_QUERY_ALL = "*:*";
    
    // 輸入關鍵字提供運算元
    public final static String KEYWORD_OP_AND = "+";
    public final static String KEYWORD_OP_NOT = "-";
    
    // zhcoder 繁簡轉換
    public final static boolean ZHCODER_ENABLED = true;
    
    // response statusRESPONSE_SUCCESS
    public final static int RESPONSE_SOLR_OK = 0;
    public final static int RESPONSE_SOLR_FAIL = -1;
    public final static String RESPONSE_SUCCESS = "OK";
    public final static String RESPONSE_FAIL = "FAIL";
        
    // special field define    
    public final static String FIELD_SOLR_KEY = "id";
    public final static String FIELD_SOURCE_AP = "source";
    public final static String FIELD_CUSTOM_ID = "cid";    
    
    public final static String FIELD_GLOBAL_QUERY = "text";
    public final static String FIELD_BOOTS_SCORE = "score";// SolrReturnFields.SCORE
    
    // result field list
    public final static String FIELD_LIST_RESULT = "id,source,cid,title,description,score";// *,score
    public final static String FIELD_LIST_FULL = "*,score";// *,score
    
    // solr query define type
    public final static String FIELD_QUERY_DEFINE_TYPE = "defType";
    // solr query boost
    public final static double DEF_BOOST = 1;
    
    // HIGHLIGHT 欄位
    public final static String[] DEF_HIGHLIGHT_FIELDS = {"title", "description", "content"};
    
    // HIGHLIGHT 片段
    public final static int DEF_HIGHLIGHT_SNIPPET_NUM = 2;
    public final static int DEF_HIGHLIGHT_FRAG_SIZE = 50;
    public final static String DEF_HIGHLIGHT_PER = "#S!#"; // "&lt;b&gt;";
    public final static String DEF_HIGHLIGHT_POST = "#!E#"; //&lt;/b&gt;";
    public final static String HIGHLIGHT_CONCAT_STR = "...";

    // 檔案類型
    public final static String DEF_EXTRACT_CONTENTTYPE = "application/octet-stream";
    public final static String CONTENT_TYPE_PDF = "application/pdf";
    public final static String CONTENT_TYPE_XLS = "application/vnd.ms-excel";
    public final static String CONTENT_TYPE_XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    public final static String CONTENT_TYPE_DOC = "application/msword";
    public final static String CONTENT_TYPE_DOCX = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
    public final static String CONTENT_TYPE_PPT = "application/vnd.ms-powerpoint";
    public final static String CONTENT_TYPE_PPTX = "application/vnd.openxmlformats-officedocument.presentationml.presentation";
    
    // private properties
    private static String ssoTicketURL = CAS_SERVER + CAS_TICKET_URI;
    private static String solrURL = DEF_SOLR_SERVER;
    private static String restUser = DEF_SSO_USER;
    private static String restPwd = DEF_SSO_PWD;
    private static String sourceAP = SOURCE_AP;
    private static boolean ssoEnabled = SSO_ENABLED;
    
    // init config 
    static {
        // printSysProperties();
        prepareSolrConfig();
    }
    
    /**
     * LOG印出 Syetem Properties
     */
    public static void printSysProperties(){
        for (String n : System.getProperties().stringPropertyNames()) {
            logger.info(n + "=" + System.getProperty(n));
        }
    }
    
    /**
     * 讀取 AP SERVER 設定
     */
    public static void prepareSolrConfig(){
        try{
            // restful sso user
            restUser = setValueWithDef(System.getProperty(KEY_REST_USER), DEF_SSO_USER);
            restPwd = setValueWithDef(System.getProperty(KEY_REST_PWD), DEF_SSO_PWD);
            // sso server
            String ssoServer = setValueWithDef(System.getProperty(KEY_SSO_SERVER), CAS_SERVER);
            ssoTicketURL = ssoServer + CAS_TICKET_URI;
            // solr server
            solrURL = setValueWithDef(System.getProperty(KEY_SOLR_SERVER), DEF_SOLR_SERVER);
                 
            // 有自訂 JNDI，可複寫 solrURL、sourceAP
            Context ctx = new InitialContext();
            Properties jndiProperties = (Properties)ctx.lookup(PRIVATE_JNDI_NAME);
            if( jndiProperties==null ){
                logger.warn("prepareSolrConfig warn: "+PRIVATE_JNDI_NAME+" jndiProperties is null !");
            }else{
                // url
                solrURL = setValueWithDef(jndiProperties.getProperty(KEY_SOLR_SERVICE_URL), DEF_SOLR_SERVER);
                // source ap 
                sourceAP = setValueWithDef(jndiProperties.getProperty(KEY_SOURCE_AP), SOURCE_AP);
                // sso enabled
                try{
                    String tmp = jndiProperties.getProperty(KEY_SSO_ENABLED);
                    ssoEnabled = (tmp!=null)? Boolean.parseBoolean(tmp):SSO_ENABLED;
                }catch(Exception e){
                    logger.warn("prepareSolrConfig warn: no jndi property "+KEY_SSO_ENABLED);
                    ssoEnabled = SSO_ENABLED;
                }
            }
        }catch(NamingException e){
            logger.warn("prepareSolrConfig warn: no jndi = "+PRIVATE_JNDI_NAME);
        }
    }
    
    /**
     * 遇 NULL 設定預設值
     * @param value
     * @param defaultValue
     * @return 
     */
    private static String setValueWithDef(String value, String defaultValue){
        return (value==null)? defaultValue:value;
    }

    //<editor-fold defaultstate="collapsed" desc="Getter and Setter">
    public static String getSsoTicketURL() {
        return ssoTicketURL;
    }

    public static void setSsoTicketURL(String ssoTicketURL) {
        TcSolrConfig.ssoTicketURL = ssoTicketURL;
    }

    public static boolean isSsoEnabled() {
        return ssoEnabled;
    }

    public static void setSsoEnabled(boolean ssoEnabled) {
        TcSolrConfig.ssoEnabled = ssoEnabled;
    }

    public static String getSourceAP() {
        return sourceAP;
    }

    public static void setSourceAP(String sourceAP) {
        TcSolrConfig.sourceAP = sourceAP;
    }

    public static String getSolrURL() {
        return solrURL;
    }

    public static void setSolrURL(String solrURL) {
        TcSolrConfig.solrURL = solrURL;
    }   

    public static String getRestUser() {
        return restUser;
    }

    public static void setRestUser(String restUser) {
        TcSolrConfig.restUser = restUser;
    }

    public static String getRestPwd() {
        return restPwd;
    }

    public static void setRestPwd(String restPwd) {
        TcSolrConfig.restPwd = restPwd;
    }
    //</editor-fold>
    
}
