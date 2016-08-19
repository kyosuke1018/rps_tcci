/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test;

import com.tcci.sap.jco.model.RfcOutputTypeEnum;
import com.tcci.sap.jco.model.RfcProxyInput;
import com.tcci.sap.jco.model.RfcProxyOutput;
import com.tcci.sap.jco.rest.RfcProxyClient;
import com.tcci.sap.jco.util.JCoUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Peter.pan
 */
public class TestClient {
    private static final Logger logger = LoggerFactory.getLogger(TestClient.class);

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        getPoByJCoService("3130000013", "tcc_cn");
        // signPR("tcc_cn", "1130000069");
    }
    
    //<editor-fold defaultstate="collapsed" desc="for 測試 RFC Proxy">
    /**
     * RFC Proxy Run ZSAP_JAVA_EXP_PO_GET
     * @param fileNo
     * @param sapClientCode 
     */
    public static void getPoByJCoService(String fileNo, String sapClientCode){
        // 準備Proxy輸入參數
        RfcProxyInput input = new RfcProxyInput("TEST", "ZSAP_JAVA_EXP_PO_GET", sapClientCode);
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
        String restRootUrl = "http://localhost:8080/JCoService/resources";
        //String restRootUrl = "http://192.168.203.50/JCoService/resources";
        //String restRootUrl = "http://192.168.204.45/JCoService/resources";
        //String restRootUrl = "http://jcoservice.taiwancement.com/JCoService/resources";
        RfcProxyClient client = new RfcProxyClient(restRootUrl);// use jersey restful cilent
        //RfcHttpClient client = new RfcHttpClient(restRootUrl);// only use apache http cilent
        
        // ==== START : only need in java *.exe ap ==========
        client.setSsoProtected(true);
        client.setSsoParamsFromJNDI(false);
        client.setSsoServiceUrl("http://soa-dev-test.taiwancement.com/cas-server/v1/tickets");
        //client.setSsoServiceUrl("http://sso.taiwancement.com/cas-server/v1/tickets");
        client.setSsoUser("web.restful");
        client.setSsoPwd("Edaw34$n");
        // ==== END : only need in java *.exe ap ==========
        
        RfcProxyOutput out = client.callRfc(input);
        logger.debug("out = \n"+out.dump());
        client.close();
    }
    
    /**
     * Sign PR L2
     * @param sapClientCode
     * @param banfn 
     */
    public static void signPR(String sapClientCode, String banfn){
        // 準備Proxy輸入參數
        RfcProxyInput input = new RfcProxyInput("TEST", "ZSAP_JAVA_IMP_PR_RELEASE_CODE", sapClientCode);
        input.setDebugMode(true);

        // 準備RFC輸入參數
        Map<String, List<Map<String, Object>>> impTableParams = new HashMap<String, List<Map<String, Object>>>();
        List<Map<String, Object>> tables = JCoUtil.buildSimpleEQImpList(banfn);
        impTableParams.put("ZFLD_IMP_BANFN", tables);
        
        List<Map<String, Object>> tables2 = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("FRGLE", "2");
        map.put("FRGCO", "L2");
        map.put("BNAME", "peter.pan");
        tables2.add(map);
        impTableParams.put("ZFLD_IMP_FRGCO", tables2);
        
        input.setTables(impTableParams);
        
        // 準備RFC輸出類型參數        
        Map<String, RfcOutputTypeEnum> outputType = new HashMap<String, RfcOutputTypeEnum>();
        outputType.put("ZTAB_EXP_PR_RELEASE", RfcOutputTypeEnum.TABLE);
        input.setOutputTypes(outputType);
        
        // 呼叫 Proxy 執行 RFC // http://localhost:8080/JCoService/resources
        String restRootUrl = "http://localhost:8080/JCoService/resources";
        //String restRootUrl = "http://192.168.203.50/JCoService/resources";
        //String restRootUrl = "http://192.168.204.45/JCoService/resources";
        //String restRootUrl = "http://jcoservice.taiwancement.com/JCoService/resources";
        RfcProxyClient client = new RfcProxyClient(restRootUrl);
        //client.setSsoProtected(false);
        client.setSsoParamsFromJNDI(false);
        client.setSsoServiceUrl("http://soa-dev-test.taiwancement.com/cas-server/v1/tickets");
        //client.setSsoServiceUrl("http://sso.taiwancement.com/cas-server/v1/tickets");
        client.setSsoUser("web.restful");
        client.setSsoPwd("Edaw34$n");
        
        RfcProxyOutput out = client.callRfc(input);
        client.close();                
    }
    //</editor-fold>
    
}
