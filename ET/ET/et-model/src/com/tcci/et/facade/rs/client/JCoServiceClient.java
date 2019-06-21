/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.facade.rs.client;

import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.sap.jco.model.RfcOutputTypeEnum;
import com.tcci.sap.jco.model.RfcProxyInput;
import com.tcci.sap.jco.model.RfcProxyOutput;
import com.tcci.sap.jco.model.RfcReadTableInput;
import com.tcci.sap.jco.rest.RfcProxyClient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Call JCoService
 * @author Peter.pan
 */
public class JCoServiceClient {
    public final static Logger logger = LoggerFactory.getLogger(JCoServiceClient.class);
    
    /**
     * 透過 JCoServer 抓單一 PR
     * @param restRootUrl
     * @param sapClientCode
     * @param fileNo 
     */
    public static void getPR(String restRootUrl, String sapClientCode, String fileNo){
        // 準備Proxy輸入參數
        RfcProxyInput input = new RfcProxyInput(GlobalConstant.AP_CODE, "ZSAP_JAVA_EXP_PR_GET", sapClientCode);
        input.setDebugMode(true);
        
        // 準備RFC輸入參數
        Map<String, List<Map<String, Object>>> impTableParams = new HashMap<String, List<Map<String, Object>>>();
        List<Map<String, Object>> tables = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("SCOPE", "I");
        map.put("OPERA", "EQ");
        map.put("LOW", fileNo);
        tables.add(map);
        
        impTableParams.put("ZFLD_IMP_BANFN", tables);
        input.setTables(impTableParams);
        
        // 準備RFC輸出類型參數
        Map<String, RfcOutputTypeEnum> outputType = new HashMap<String, RfcOutputTypeEnum>();
        outputType.put("ZTAB_EXP_EBANTX_HEAD", RfcOutputTypeEnum.TABLE);
        outputType.put("ZTAB_EXP_EBAN_LOA", RfcOutputTypeEnum.TABLE);
        outputType.put("ZTAB_EXP_EBAN", RfcOutputTypeEnum.TABLE);
        outputType.put("ZTAB_EXP_EBKN", RfcOutputTypeEnum.TABLE);
        outputType.put("ZTAB_EXP_EBANTX_ITEM", RfcOutputTypeEnum.TABLE);
        outputType.put("ZTAB_EXP_EBAN_PM", RfcOutputTypeEnum.TABLE);
        input.setOutputTypes(outputType);
        
        // 呼叫 Proxy 執行 RFC // http://localhost:8080/JCoService/resources
        RfcProxyClient client = new RfcProxyClient(restRootUrl);// use jersey restful cilent   
        // client.setJwt(JWT_QAS); // from system properties by tcci-sap.jar
        
        RfcProxyOutput out = client.callRfc(input);
        logger.debug("out = \n"+out.dump());
        client.close();
    }
    
    /**
     * 透過 JCoServer 抓單一 PO
     * @param restRootUrl
     * @param sapClientCode
     * @param fileNo 
     */
    public static void getPO(String restRootUrl, String sapClientCode, String fileNo){
        // 準備Proxy輸入參數
        RfcProxyInput input = new RfcProxyInput(GlobalConstant.AP_CODE, "ZSAP_JAVA_EXP_PO_GET", sapClientCode);
        input.setDebugMode(true);
        
        // 準備RFC輸入參數
        Map<String, List<Map<String, Object>>> impTableParams = new HashMap<String, List<Map<String, Object>>>();
        List<Map<String, Object>> tables = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("SCOPE", "I");
        map.put("OPERA", "EQ");
        map.put("LOW", fileNo);
        tables.add(map);
        
        impTableParams.put("ZFLD_IMP_EBELN", tables);
        input.setTables(impTableParams);
        
        // 準備RFC輸出類型參數
        Map<String, RfcOutputTypeEnum> outputType = new HashMap<String, RfcOutputTypeEnum>();
        outputType.put("ZTAB_EXP_EKKO", RfcOutputTypeEnum.TABLE);
        outputType.put("ZTAB_EXP_EKKOTX", RfcOutputTypeEnum.TABLE);
        outputType.put("ZTAB_EXP_EKKO_LOA", RfcOutputTypeEnum.TABLE);
        outputType.put("ZTAB_EXP_EKPO", RfcOutputTypeEnum.TABLE);
        outputType.put("ZTAB_EXP_EKKN", RfcOutputTypeEnum.TABLE);
        outputType.put("ZTAB_EXP_EKPOTX", RfcOutputTypeEnum.TABLE);
        input.setOutputTypes(outputType);
        
        // 呼叫 Proxy 執行 RFC // http://localhost:8080/JCoService/resources
        RfcProxyClient client = new RfcProxyClient(restRootUrl);// use jersey restful cilent
        // client.setJwt(JWT_QAS); // from system properties by tcci-sap.jar
        
        RfcProxyOutput out = client.callRfc(input);
        logger.debug("out = \n"+out.dump());
        client.close();
    }

    /**
     * 透過 JCoServer Call RFC_READ_TABLE
     * @param restRootUrl
     * @param sapClientCode
     * @param tableName
     * @param offset
     * @param rowCount
     * @param fields
     * @param options
     * @param clazz
     * @return 
     */
    public static List callRfcReadTableByClient(String restRootUrl, 
            String sapClientCode, String tableName, Integer offset, Integer rowCount,
            List<String> fields, String options, Class clazz) {
        RfcProxyClient client = new RfcProxyClient(restRootUrl);// use jersey restful cilent
        // client.setJwt(JWT_QAS); // from system properties by tcci-sap.jar
        
        RfcReadTableInput imp = new RfcReadTableInput();
        imp.setSapClientCode(sapClientCode);
        imp.setClientCode(GlobalConstant.AP_CODE);
        //imp.setDebugMode(true);
        
        imp.setQueryTable(tableName); //"T001W");// 工廠主檔
        //imp.setDelimiter("|");
        //imp.setNoData(" ");
        if( offset!=null ){
            imp.setRowskips(offset);
        }
        if( rowCount!=null ){
            imp.setRowcount(rowCount);
        }
        if( fields!=null ){
            imp.setFields(fields);
            //imp.setFields(Arrays.asList(new String[]{"MANDT", "WERKS", "NAME1"}));// 未輸入傳回所有欄位
        }
        imp.setOptions(options);//"WERKS GE '3511'");
        
        return client.callRfcReadTable(imp, clazz);
    }
    public static List callRfcReadTableByClient(String restRootUrl, String sapClientCode, String tableName,
            List<String> fields, Class clazz) {
        return callRfcReadTableByClient(restRootUrl, sapClientCode, tableName, null, null, fields, null, clazz);
    }
}
