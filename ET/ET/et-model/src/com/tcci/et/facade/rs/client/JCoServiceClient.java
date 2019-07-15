/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.facade.rs.client;

import com.tcci.cm.model.global.GlobalConstant;
import static com.tcci.et.facade.jco.JCoClientFacade.getSapServiceUrlFromJndi;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.sap.jco.model.RfcOutputTypeEnum;
import com.tcci.sap.jco.model.RfcProxyInput;
import com.tcci.sap.jco.model.RfcProxyOutput;
import com.tcci.sap.jco.model.RfcReadTableInput;
import com.tcci.sap.jco.rest.RfcProxyClient;
import com.tcci.sap.jco.util.JCoUtil;
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
        
        List resList = client.callRfcReadTable(imp, clazz);
        client.close();
        
        return resList;
    }
    public static List callRfcReadTableByClient(String restRootUrl, String sapClientCode, String tableName,
            List<String> fields, Class clazz) {
        return callRfcReadTableByClient(restRootUrl, sapClientCode, tableName, null, null, fields, null, clazz);
    }
}
