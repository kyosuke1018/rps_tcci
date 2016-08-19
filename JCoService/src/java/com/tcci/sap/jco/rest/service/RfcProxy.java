/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.tcci.sap.jco.rest.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoFunction;
import com.tcci.sap.jco.conf.JCoConfigManager;
import com.tcci.sap.jco.enums.SapLanguageEnum;
import com.tcci.sap.jco.model.RfcOutputTypeEnum;
import com.tcci.sap.jco.model.RfcProxyInput;
import com.tcci.sap.jco.model.RfcProxyOutput;
import com.tcci.sap.jco.monitor.ProxyLogHandler;
import com.tcci.sap.jco.util.JCoUtil;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RFC 代理呼叫程式
 *
 * 注意: JAXB 處理複雜物件過於繁雜，改直接使用 jackson JSON library
 * @author Peter.pan
 */
@Stateless
@Path("rfcProxy")
public class RfcProxy {
    private static final Logger logger = LoggerFactory.getLogger(RfcProxy.class);
    @Inject ProxyLogHandler logHandler;
    
    /**
     * run RFC by JCoService
     * Call by RESTful Client
     * @param request
     * @param inputJsonStr
     * @return 
     */
    @POST
    @Path("callRfc")
    @Consumes({MediaType.TEXT_PLAIN})
    @Produces({MediaType.TEXT_PLAIN})
    public Response callRfc(@Context HttpServletRequest request, String inputJsonStr) {
        Response resp = execRfc(request, inputJsonStr);
        return resp;
    }

    /**
     * run RFC by JCoService
     * Call by HTTP POST Client
     * @param request
     * @param inputJsonStr
     * @return 
     */
    @POST
    @Path("callRfcForm")
    @Produces({MediaType.TEXT_PLAIN})
    public Response callRfcForm(@Context HttpServletRequest request, @FormParam("input")String inputJsonStr) {
        Response resp = execRfc(request, inputJsonStr);
        return resp;
    }
    
    /**
     * http://localhost:8080/JCoService/resources/rfcProxy/testSSO
     * @return 
     */
    @GET
    @Path("testSSO")
    @Produces({MediaType.TEXT_PLAIN})
    public Response testSSO() {
        return Response.ok().entity("TEST SSO GET OK").build();
    }
    
    /**
     * 執行 SAP RFC
     * @param request
     * @param inputJsonStr
     * @return 
     */
    public Response execRfc(HttpServletRequest request, String inputJsonStr) {
        logger.info("callRfc ...");
        String jsonResStr;
        RfcProxyInput input = null;

        try{
            // 輸入轉換
            ObjectMapper mapper = new ObjectMapper();
            input = mapper.readValue(inputJsonStr, RfcProxyInput.class);
            if( input.isDebugMode() ){
                logger.info("callRfc inputJsonStr = \n" +inputJsonStr);
            }
            
            if( input.getFunctionName()==null || input.getSapClientCode()==null ){
                logger.error("callRfc input.getFunctionName()==null || input.getSapClientCode()==null");
                return Response.noContent().build();
            }
            
            // == BEGIN : 代理執行RFC =============
            // 取得連線資訊
            JCoDestination destination;
            SapLanguageEnum lang = SapLanguageEnum.fromCode(input.getLanguage());
            if( lang==null){
                // 沿用 JNDI 設定
                destination = JCoConfigManager.getJCoDestinationByClient(input.getSapClientCode()); // JCoConfigManager.getDestinationMap().get(destinationName);
            }else{
                // 需動態指定語系
                destination = JCoConfigManager.getJCoDestinationByClient(input.getSapClientCode(), true, lang.getCode());
            }
            // 取得 function 資訊
            JCoFunction function = destination.getRepository().getFunction(input.getFunctionName());
            if( function==null ){
                logger.error("proxy destination.getRepository().getFunction = null !");
                return null;
            }
            // 輸入參數處理
            prepareRfcInput(input, function);
            
            // 呼叫 RFC
            Date startTime = logBeforeRFC(function);
            function.execute(destination);
            long timeConsuming = logAfterRFC(function, startTime);
            // == END : 代理執行RFC =============
            
            // 輸出轉換
            RfcProxyOutput out = convertRfcOutput(input, function);
            out.setRunTime(timeConsuming);

            // Convert object to JSON string and pretty print
            jsonResStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(out);
            
            if( input.isDebugMode() ){
                logger.info("callRfc jsonResStr = \n" +jsonResStr);
            }
            
            logHandler.handleRequest(request, inputJsonStr, input, timeConsuming, true, null);
        }catch(Exception e){
            logger.error("callRfc exception:\n", e);
            String errMsg = logHandler.getErrorStackTrace(e);
            logHandler.handleRequest(request, inputJsonStr, input, 0, false, errMsg);
            return Response.serverError().build();
        }
        
        return Response.ok().entity(jsonResStr).build();
    }
    
    /**
     * 準備
     * @param input
     * @param function
     * @throws JsonProcessingException
     */
    private void prepareRfcInput(RfcProxyInput input, JCoFunction function) throws JsonProcessingException{
        logger.info("prepareRfcInput getSapClientCode = "+input.getSapClientCode());
        logger.info("prepareRfcInput getFunctionName = "+input.getFunctionName());
        // 輸入參數處理
        // tables
        if( input.getTables()!=null ){
            for(String impTableName : input.getTables().keySet()){
                logger.info("prepareRfcInput impTableName = "+impTableName);
                if( input.isDebugMode() ){
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.writerWithDefaultPrettyPrinter().writeValueAsString(input.getTables().get(impTableName));
                }
                JCoUtil.setInputJCoTable(function, impTableName, input.getTables().get(impTableName));
            }
        }
        // structures
        if( input.getStructures()!=null ){
            for(String impName : input.getStructures().keySet()){
                logger.info("prepareRfcInput impName = "+impName);
                if( input.isDebugMode() ){
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.writerWithDefaultPrettyPrinter().writeValueAsString(input.getStructures().get(impName));
                }
                JCoUtil.setInputJCoStructure(function, impName, input.getStructures().get(impName));
            }
        }
        // values
        if( input.getValues()!=null ){
            JCoUtil.setInputValues(function, input.getValues());
        }
    }
    
    /**
     * 轉換RFC輸出，準備傳給 Client
     * @param input
     * @param function
     * @return
     * @throws JsonProcessingException 
     */
    private RfcProxyOutput convertRfcOutput(RfcProxyInput input, JCoFunction function) throws JsonProcessingException{
        logger.info("convertRfcOutput ...");
        if( input==null || function==null || input.getOutputTypes()==null ){
            logger.error("convertRfcOutput .. input==null || function==null || input.getOutputTypes()==null");
            return null;
        }
        // 輸出處理
        RfcProxyOutput out = new RfcProxyOutput();
        Map<String, List<Map<String, Object>>> tables = new HashMap<String, List<Map<String, Object>>>();
        Map<String, Map<String, Object>> structures = new HashMap<String, Map<String, Object>> ();
        
        if( input.getOutputTypes()!=null || !input.getOutputTypes().isEmpty() ){// 指定回傳
            for(String tableName : input.getOutputTypes().keySet()){
                RfcOutputTypeEnum outputType = input.getOutputTypes().get(tableName);
                if( outputType == RfcOutputTypeEnum.TABLE ){
                    logger.info("TABLE : "+tableName);
                    List<Map<String, Object>> list = JCoUtil.getOutputJCoTable(function, tableName);
                    tables.put(tableName, list);
                }else if( outputType == RfcOutputTypeEnum.STRUCTURE ){
                    logger.info("STRUCTURE : "+tableName);
                    Map<String, Object> map = JCoUtil.getOutputJCoStructure(function, tableName);
                    structures.put(tableName, map);
                }
            }
        }else{// 未指定回傳
            tables = JCoUtil.getOutputJCoTables(function);
            structures = JCoUtil.getOutputJCoStructures(function);
        }
        
        out.setTables(tables);
        out.setStructures(structures);
        if( input.isDebugMode() ){
            ObjectMapper mapper = new ObjectMapper();
            mapper.writerWithDefaultPrettyPrinter().writeValueAsString(out);
        }
        return out;
    }
    
    /**
     * RFC 執行前 LOG
     * @param function
     * @param showDetail
     * @return 
     */
    private Date logBeforeRFC(JCoFunction function){
        logger.info("BEGIN : " + function.getName());
        return new Date();
    }
    
    /**
     * RFC 執行後 LOG
     * @param function
     * @param showDetail
     * @return 
     */
    private long logAfterRFC(JCoFunction function, Date startTime){
        Date endTime = new Date();
        long runTime = endTime.getTime()-startTime.getTime(); // ms
        StringBuilder msg = new StringBuilder().append("END : ").append(function.getName()).append(" (").append(runTime).append(" ms)");
        logger.info(msg.toString());
        return runTime;
    }
    
    /**
     * 檢查 Principal
     * @param request
     * @return
     */
    private boolean checkUserPrincipal(HttpServletRequest request){
        if( request!=null && request.getUserPrincipal()!=null && request.getUserPrincipal().getName()!=null ){
            logger.info("checkUserPrincipal request.getUserPrincipal()={}", request.getUserPrincipal());
            if( request.getUserPrincipal().getName()!=null && !request.getUserPrincipal().getName().isEmpty() ){
                return true;
            }
        }
        return false;
    }

    /**
     * 檢查登入帳號
     * @param request
     * @return 
     */
    private boolean checkLogin(HttpServletRequest request){
        if( checkUserPrincipal(request) ){
            String loginAccount = request.getUserPrincipal().getName();
            logger.info("checkLogin loginAccount = "+loginAccount);
            // 帳號檢查
            //List<TcUser> userList = userFacade.findByLoginAccount(loginAccount);
            //if( userList==null || userList.isEmpty() ){
            //    return false;
            //}
            return true;
        }
        return false;
    }
}
