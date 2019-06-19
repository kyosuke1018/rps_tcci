/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.facade.rs.client;

import com.tcci.sap.jco.rest.JwtHelper;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;

/**
 *
 * @author Peter.pan
 */
public class CallRfcRestClient {
    public final static Logger logger = LoggerFactory.getLogger(CallRfcRestClient.class);
    // client timeout
    public static final int REST_CONNECT_TIMEOUT = 3 * 10 * 000;// 30 seconds
    public static final int REST_READ_TIMEOUT = 2 * 60 * 1000;// 2 minutes
    
    // TCJCoServer
    public final static String TCJCOSERVER_REST_SERVICE = "callRfcService";
    //public final static String TCJCOSERVER_REST_SERVICE_SSO = "callRfcSsoService";
    public final static String TCJCOSERVER_REST_SAP2DW = "transSAPToDW";
    //public final static String TCJCOSERVER_REST_SAP2DW_VENDOR = "transVendorSAPToDW"; //依供應商建立日期起迄，由 SAP RFC 更新供應商資料至 DW DB (同步處裡)。
    public final static String RESULT_SUCCESS = "OK";
    public final static String RESULT_FAIL = "FAIL";
    public final static int HTTP_STATUS_SUCCESS = 200;
    
    protected WebTarget webTarget;
    protected Client client;
    // 改 JWT 保護 20190312 
    private boolean jwtProtected = true;
    private String jwt;

    public static ClientConfig getClientConfig(){
        ClientConfig config = new ClientConfig();
        config.property(ClientProperties.CONNECT_TIMEOUT, REST_CONNECT_TIMEOUT);
        config.property(ClientProperties.READ_TIMEOUT, REST_READ_TIMEOUT);
        
        return config;
    }
    
    public CallRfcRestClient(String wsroot) {
        String WS_ROOT = wsroot; //"http://localhost:8080/TCJCoServer/resources";

        //client = ClientBuilder.newBuilder().withConfig(getClientConfig()).build();
        client = ClientBuilder.newBuilder().build();
        // 改 JWT 保護 20190312 
        //webTarget = client.target(WS_ROOT).path(ssoProtected ? REST_SERVICE_SSO : REST_SERVICE);
        webTarget = client.target(WS_ROOT);
        jwt = JwtHelper.getJWT();
    }
    
    // SAP 請購單
    public String transPRToDW(String fileType, String fileNoS, String sapClientCode){
        return transSAPToDW("pr", fileNoS, sapClientCode);
    }

    // SAP 採購單
    public String transPOToDW(String fileType, String fileNoS, String sapClientCode){
        return transSAPToDW("po", fileNoS, sapClientCode);
    }
    
    // SAP 供應商
    public String transVendorToDW(String fileType, String fileNoS, String sapClientCode){
        return transSAPToDW("lfa1", fileNoS, sapClientCode);
    }
    
    // SAP 物料名稱
    public String transMaterialToDW(String fileType, String fileNoS, String sapClientCode){
        return transSAPToDW("makt", fileNoS, sapClientCode);
    }
    
    /**
     * 同步 datawarehouse 支援的 SAP 單據、主檔
     * Call /resources/callRfcService/transSAPToDW
     *
     * @param fileType
     * @param fileNoS
     * @param sapClientCode
     * @return
     */
    public String transSAPToDW(String fileType, String fileNoS, String sapClientCode) {
        logger.info("transSAPToDW ... fileType =" + fileType + "; sapClientCode=" + sapClientCode + "; fileNoS=" + fileNoS);

        try {
            //WebResource resource = webResource;
            WebTarget resource = webTarget.path(TCJCOSERVER_REST_SERVICE);
            Response response = null;
            StringBuilder qstr = new StringBuilder();
            // 改 JWT 保護 20190312 
            logger.info("transSAPToDW jwtProtected = " + jwtProtected);
            if( jwtProtected ){
                // 呼叫 service
                resource = resource.path(TCJCOSERVER_REST_SAP2DW);
                resource = (fileType != null)?resource.queryParam("fileType", fileType):resource;
                resource = (fileNoS != null)?resource.queryParam("fileNoS", fileNoS):resource;
                resource = (sapClientCode != null)?resource.queryParam("sapClientCode", sapClientCode):resource;
                
                Invocation.Builder builder = resource.request(MediaType.TEXT_PLAIN_TYPE);
                if( jwtProtected ){
                    builder = builder.header(HttpHeaders.AUTHORIZATION, JwtHelper.JWT_HEADER_PREFIX + jwt);
                }
                logger.info("transSAPToDW url = " + resource.getUri().toURL().toString());

                //response = builder.post(Entity.entity(qstr.toString(), MediaType.TEXT_PLAIN_TYPE));
                Invocation.Builder invocationBuilder = resource.request(MediaType.TEXT_PLAIN_TYPE);
                response = invocationBuilder.get();
            }

            logger.info("transSAPToDW response = " + response);
            if( response!=null ){
                if (response.getStatus() == HTTP_STATUS_SUCCESS) {
                    String outputString = response.readEntity(String.class);
                    logger.info("transSAPToDW outputString = " + outputString);
                    return outputString;
                } else {
                    logger.error("transSAPToDW response.getStatus() = " + response.getStatus());
                }
            }
        } catch (Exception e) {
            logger.error("transSAPToDW exception :\n", e);
            return RESULT_FAIL;
        }
        return RESULT_FAIL;
    }
    
    public void close() {
        client.close();
    }

    //<editor-fold defaultstate="collapsed" desc="for TCJCServer">
    public WebTarget getWebTarget() {
        return webTarget;
    }

    public void setWebTarget(WebTarget webTarget) {
        this.webTarget = webTarget;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public boolean isJwtProtected() {
        return jwtProtected;
    }

    public void setJwtProtected(boolean jwtProtected) {
        this.jwtProtected = jwtProtected;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
    //</editor-fold>
}
