package com.tcci.ecdemo.sapproxy;

import com.tcci.ecdemo.sapproxy.dto.SapProxyResponseDto;
import java.util.List;
import java.util.Properties;

/**
 * sap related function
 *
 * @author Neo.Fu
 */
public interface SdProxy {

    /**
     * Proxy 初始化
     *
     * @param props
     */
    public void init(Properties props);

    /**
     * 帶出銷售文件內容 (多筆銷售文件) (ZSAP_JAVA_EXP_SD_GET)
     * @param vbelnList 銷售文件號碼清單
     * @param shipped 是否已出貨flag.
     * @return 回傳SAP Table:<br/> SapProxyReponseDto.getResultAsSapTableDto():
     * @throws Exception 
     */
    SapProxyResponseDto findSalesDocument(List<String> vbelnList, Boolean shipped) throws Exception;
    
    /**
     * 帶出已出貨銷售文件內容 (多筆銷售文件) (ZSAP_JAVA_EXP_SD_GET)
     * @param vbelnList 銷售文件號碼清單
     * @return 回傳SAP Table:<br/> SapProxyReponseDto.getResultAsSapTableDto():
     * @throws Exception 
     */
    SapProxyResponseDto findShippedSalesDocument(List<String> vbelnList) throws Exception;

    /**
     * Proxy 關閉，包含連結關閉、資料清除等工作
     */
    public void dispose();
}
