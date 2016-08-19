package com.tcci.tccstore.sapproxy;

import com.tcci.tccstore.sapproxy.dto.SapProxyResponseDto;
import java.util.List;
import java.util.Map;
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
     *  依傳入的plantID ,kunnr ，取得客戶餘額。
     * @param plantID ,kunnr 
     * @return  回傳一筆資料:<br/>
     *                  SapProxyReponseDto.getResultAsSapTableDto():
     * @throws Exception 
     */
    SapProxyResponseDto queryCREDIT(String plant, String kunnr) throws Exception;

    /**
     * 帶出銷售文件內容 (多筆銷售文件) (ZSAP_JAVA_EXP_SD_GET)
     *
     * @param vbelnList 銷售文件號碼清單
     * @param shipped 是否已出貨flag.
     * @return 回傳SAP Table:<br/> SapProxyReponseDto.getResultAsSapTableDto():
     * @throws Exception
     */
    SapProxyResponseDto findSalesDocument(List<String> vbelnList, Boolean shipped) throws Exception;

    /**
     * 帶出已出貨銷售文件內容 (多筆銷售文件) (ZSAP_JAVA_EXP_SD_GET)
     *
     * @param vbelnList 銷售文件號碼清單
     * @return 回傳SAP Table:<br/> SapProxyReponseDto.getResultAsSapTableDto():
     * @throws Exception
     */
    SapProxyResponseDto findShippedSalesDocument(List<String> vbelnList) throws Exception;

    /**
     * 讀取預開訂單之訂單總額 (ZSAP_JAVA_EXP_SO_AMOUNT_GET)
     * @param order 訂單
     * @return 回傳SAP Table:<br/> SapProxyReponseDto.getResultAsSapTableDto():
     * @throws Exception
     */
    // SapProxyResponseDto findOrderPrice(Map<String, Object> order) throws Exception;

    /**
     * 建立訂單 (Z_SD_CREATE_SO_BATCH2)
     * @param order 訂單
     * @return 回傳SAP Table:<br/> SapProxyReponseDto.getResultAsSapTableDto():
     * @throws Exception 
     */
    SapProxyResponseDto createOrder(Map<String, Object> params) throws Exception;

    /**
     * 刪除訂單 (Z_SD_CREATE_SO_BATCH2)
     * @param order
     * @return 回傳SAP Table:<br/> SapProxyReponseDto.getResultAsSapTableDto():
     * @throws Exception 
     */
    SapProxyResponseDto cancelOrder(Map<String, Object> params) throws Exception;

    /**
     * 取得客戶預收款餘額 (ZSAP_JAVA_EXP_CREDIT_LIST)
     *
     * @param kunnr 客戶代碼
     * @return 回傳SAP Table:<br/> SapProxyReponseDto.getResultAsSapTableDto():
     * @throws Exception
     */
    SapProxyResponseDto findCustomerCredits(String kunnr) throws Exception;
    
    // CRM's 
        /**
     *  依傳入的plantID ,kunnr ，取得出貨資料 zorder_cn。
     * @param plantID ,kunnr ,SHIPBDATE, SHIPEDATE,Flag
     * @return  回傳多筆資料:<br/> SapProxyReponseDto.getResultAsSapTableDto():
     * @throws Exception 
     */
    SapProxyResponseDto queryZordercn(String plant, String kunnr, String SHIPBDATE, 
            String SHIPEDATE , String FLAG) throws Exception;
     
    /**
     *  依傳入的plantID ,kunnr ，取得出貨資料 Z_SD_CRM_SODETAIL。
     * @param plantID ,kunnr 
     * @return  回傳多筆資料:<br/>
     *                  SapProxyReponseDto.getResultAsSapTableDto():
     * @throws Exception 
     */
    SapProxyResponseDto querySODETAIL(String plant, String kunnr, String pdate) throws Exception;
    

    /*
    取得有效合約
    */
    SapProxyResponseDto findActiveContract() throws Exception;
    
    /*
    客戶主檔:10000 ~ 99999 (非5碼需人工排除)
    送達地點:S00000 ~ S99999 (非6碼需人工排除)
    */
    SapProxyResponseDto findCustomerList(String low, String high) throws Exception;

    /**
     * Proxy 關閉，包含連結關閉、資料清除等工作
     */
    public void dispose();
}
