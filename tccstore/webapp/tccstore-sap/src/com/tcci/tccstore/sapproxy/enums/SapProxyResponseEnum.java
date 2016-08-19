package com.tcci.tccstore.sapproxy.enums;

/**
 * 
 * @author Neo.Fu
 */
public enum SapProxyResponseEnum {

    /*
     * 執行成功
     */
    SUCCESS, 
    /*
     * 執行失敗，錯誤原因參考ReturnCode及Description
     */    
    ERROR_LEVEL_APPLICATION, 
    /**
     * 連結失敗。
     */
    ERROR_LEVEL_CONNECTION;

}
