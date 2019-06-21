/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.facade.rs.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.et.model.rs.ApproverVO;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * for call hrorg
 * @author Peter.pan
 */
public class HttpRESTClient {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    public final static String REST_SERVICE = GlobalConstant.REST_HRORG_APPROVER;
    public final static int HTTP_STATUS_SUCCESS = 200;
    
    private String serviceRoot;
    
    private boolean ssoProtected = false;// 不使用 SSO
    private boolean ssoParamsFromJNDI = true;
    private String ssoServiceUrl; // "http://soa-dev-test.taiwancement.com/cas-server/v1/tickets";
    private String ssoUser; // "web.restful";
    private String ssoPwd;
    private String ticketGrantingTicket;
    private boolean renewTGT = true;
    
    HttpGet httpget;

    public HttpRESTClient(String wsroot) {//ex "http://hrorg.taiwancement.com/orguser/service/approver?adaccount=jimmy.lee";
        serviceRoot = wsroot + "/" + REST_SERVICE;
    }
    
    public ApproverVO callApproverService(String adaccount){
        try{
            String url = serviceRoot+"?adaccount="+adaccount;
            logger.info("callApproverService url = " +url);
            logger.info("callApproverService ssoProtected = " +ssoProtected);

            HttpResponse response = null;
            response = getService(url);
            
            if( response!=null ){
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
                // 取得輸出結果
                String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
                int statusCode = response.getStatusLine().getStatusCode();
                logger.info("callApproverService response statusCode =" + statusCode);
                logger.debug("callApproverService response entity = \n" + responseString);
                
                if( statusCode==HTTP_STATUS_SUCCESS ){
                    ApproverVO output = mapper.readValue(responseString, ApproverVO.class);
                    logger.debug("callApproverService output = \n"+mapper.writerWithDefaultPrettyPrinter().writeValueAsString(output));
                    return output;
                }
            }
        }catch(Exception e){
            logger.error("callApproverService Exception :\n", e);
        }
        return null;
    }
    
    /**
     * 無 SSO 保護使用
     * @param url
     * @return 
     */
    public HttpResponse getService(String url) {
        logger.debug("getService ... ");
        
        HttpResponse response = null;
        //HttpPost httpost = null;
        try {       
            DefaultHttpClient client = new DefaultHttpClient();
            // client.setRedirectStrategy(new LaxRedirectStrategy());// *** enabled redirect ***
            httpget = new HttpGet(url);
            //if( nvps!=null ){// 設定 Post 參數
            //    printPostParams(nvps);
            //    httpost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
            //}
            
            // 發送 HTTP GET Request
            response = client.execute(httpget);
        } catch (Exception ex) {
            logger.debug("getService exception:\n", ex);
        } finally {
            // can't releaseConnection, which cause response getEntity error. (socket is closed)
            //if( httpost!=null ){
            //    httpost.releaseConnection();
            //}
        }
        return response;
    }
    
    public void close(){
        try {
            // releaseConnection after response getEntity
            if( httpget!=null ){
                httpget.releaseConnection();
            }
        }catch(Exception e){
            logger.debug("close exception :\n", e);
        }
    }

    public String getServiceRoot() {
        return serviceRoot;
    }

    public void setServiceRoot(String serviceRoot) {
        this.serviceRoot = serviceRoot;
    }

    public boolean isSsoProtected() {
        return ssoProtected;
    }

    public void setSsoProtected(boolean ssoProtected) {
        this.ssoProtected = ssoProtected;
    }

    public boolean isSsoParamsFromJNDI() {
        return ssoParamsFromJNDI;
    }

    public void setSsoParamsFromJNDI(boolean ssoParamsFromJNDI) {
        this.ssoParamsFromJNDI = ssoParamsFromJNDI;
    }

    public String getSsoServiceUrl() {
        return ssoServiceUrl;
    }

    public void setSsoServiceUrl(String ssoServiceUrl) {
        this.ssoServiceUrl = ssoServiceUrl;
    }

    public String getSsoUser() {
        return ssoUser;
    }

    public void setSsoUser(String ssoUser) {
        this.ssoUser = ssoUser;
    }

    public String getSsoPwd() {
        return ssoPwd;
    }

    public void setSsoPwd(String ssoPwd) {
        this.ssoPwd = ssoPwd;
    }

    public String getTicketGrantingTicket() {
        return ticketGrantingTicket;
    }

    public void setTicketGrantingTicket(String ticketGrantingTicket) {
        this.ticketGrantingTicket = ticketGrantingTicket;
    }

    public boolean isRenewTGT() {
        return renewTGT;
    }

    public void setRenewTGT(boolean renewTGT) {
        this.renewTGT = renewTGT;
    }
}
