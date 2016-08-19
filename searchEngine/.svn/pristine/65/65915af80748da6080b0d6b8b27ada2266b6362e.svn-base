package com.tcci.solr.client.exception;

/**
 * Customize Exception for Solr Proxy
 * @author Peter
 */
public class SolrProxyException extends Exception {
    public final static String ERROR_NOSYS = "無系統代號!";
    public final static String ERROR_NO_QUERY_PARAM = "未輸入查詢條件!";

    public final static String ERROR_NO_KEYWORD = "共用關鍵字查詢，請設定 QueryBuilder 的 queryValue!";
    
    public final static String ERROR_SOURCE_LIMIT = "目前應用一定要加[來源系統]欄位比對限制";
    public final static String ERROR_NO_SOURCE = "未輸入[來源系統]條件!";
    
    public final static String ERROR_RESPONSE_NULL = "查詢結果為 NULL!";

    public final static String ERROR_NO_UPDATE_PARAM = "未輸入Solr異動參數!";
    public final static String ERROR_NO_UPDATE_SOURCE = "未輸入Solr異動[來源系統代號]參數!";
    public final static String ERROR_NO_UPDATE_CID = "未輸入Solr異動[客製文件代號]參數!";
    public final static String ERROR_NO_UPDATE_TITLE = "未輸入Solr異動[標題]參數!";
    public final static String ERROR_NO_UPDATE_FILENAME = "未輸入Solr異動[檔案名稱]參數!";
    public final static String ERROR_NO_UPDATE_PATH = "未輸入Solr異動[檔案路徑]參數!";
    
    public final static String ERROR_INJECT_QUEUE_CONN_FACTORY = "Inject QueueConnectionFactory is NULL";
    public final static String ERROR_INJECT_QUEUE = "Inject Queue is NULL";
    
    public SolrProxyException(String message, Throwable cause) {
        super(message, cause);
    }

    public SolrProxyException(String message) {
        super(message);
    }

    public SolrProxyException(Throwable cause) {
        super(cause);
    }

    public Throwable getRootCause() {
        Throwable t = this;
        while (true) {
            Throwable cause = t.getCause();
            if (cause != null) {
                t = cause;
            } else {
                break;
            }
        }
        return t;
    }
}
