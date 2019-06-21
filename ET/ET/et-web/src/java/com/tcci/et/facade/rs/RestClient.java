/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.facade.rs;

import com.tcci.cm.model.global.GlobalConstant;
//import com.tcci.cm.util.KeyGenerator;
//import com.tcci.cm.util.TokenUtils;
//import com.tcci.pda.model.AssessSettingListRsVO;
//import com.tcci.pda.model.MonitorStatusVO;
//import com.tcci.pda.model.UserProfile;
import java.util.concurrent.Future;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.HttpHeaders;
//import com.tcci.pda.model.AuthResponseVO;
import java.util.concurrent.CompletionStage;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Peter.pan
 */
public class RestClient {
    public final static Logger logger = LoggerFactory.getLogger(RestClient.class);
    
    //public final static int MONITOR_TIMEOUT = 15000;
    
    public static ClientConfig getClientConfig(){
        ClientConfig config = new ClientConfig();
        config.property(ClientProperties.CONNECT_TIMEOUT, GlobalConstant.REST_CONNECT_TIMEOUT);
        config.property(ClientProperties.READ_TIMEOUT, GlobalConstant.REST_READ_TIMEOUT); //GlobalConstant.REST_READ_TIMEOUT);
        
        return config;
    }

    /**
     * 查詢指定主機狀態 : for Reactive Client API
     * @param hostPort
     * @param restPath
     * @param protect
     * @param issuer
     * @param user
     * @param keyGenerator
     * @return
     * @throws Exception 
     */
//    public static CompletionStage<Response> sendMonitorRequestRx(String hostPort, String restPath, boolean protect, 
//            String issuer, UserProfile user, KeyGenerator keyGenerator) throws Exception{
//        String url = hostPort + GlobalConstant.REST_ROOT + restPath;
//        logger.info("sendMonitorRequestRx url = "+url);
//        BaseRESTClient restClient = new BaseRESTClient(url, getClientConfig());
//        logger.info("sendMonitorRequestRx restClient = "+restClient);
//        Invocation.Builder reqBuilder = restClient.getWebTarget().request();
//        if( protect ){
//            String token = TokenUtils.issueUserToken(user, issuer, null, GlobalConstant.JWT_EXPIRED_MINUTES, keyGenerator);
//            logger.debug("sendMonitorRequestRx token = "+token);
//            reqBuilder.header(HttpHeaders.AUTHORIZATION, TokenUtils.JWT_HEADER_PREFIX+token);
//        }
//
//        logger.info("sendMonitorRequestRx before get ...");
//        return reqBuilder.rx().get();
//    }
    
    /**
     * 查詢指定主機狀態
     * @param hostPort
     * @param restPath
     * @param issuer
     * @param protect
     * @param user
     * @param keyGenerator
     * @return
     * @throws Exception 
     */
//    public static Future<MonitorStatusVO> sendMonitorRequest(String hostPort, String restPath, boolean protect, 
//            String issuer, UserProfile user, KeyGenerator keyGenerator) throws Exception{
//        String url = hostPort + GlobalConstant.REST_ROOT + restPath;
//        BaseRESTClient restClient = new BaseRESTClient(url, getClientConfig());
//        logger.debug("sendMonitorRequest url = "+url);
//
//        Invocation.Builder reqBuilder = restClient.getWebTarget().request();
//        if( protect ){
//            String token = TokenUtils.issueUserToken(user, issuer, null, GlobalConstant.JWT_EXPIRED_MINUTES, keyGenerator);
//            logger.debug("sendMonitorRequest token = "+token);
//            reqBuilder.header(HttpHeaders.AUTHORIZATION, TokenUtils.JWT_HEADER_PREFIX+token);
//        }
//        
//        return reqBuilder.async().get(MonitorStatusVO.class);
//    }
    
    /**
     * 
     * @param restUrl // /verify4Oversee/{id}/{password}
     * @param empId
     * @param encryptPwd
     * @return
     * @throws Exception 
     */
//    public static AuthResponseVO sendAuthRequest(String restUrl, String empId, String encryptPwd) throws Exception{
//        String url = restUrl + "/" + empId + "/" + encryptPwd + "?encrypted=Y";
//        logger.debug("sendAuthRequest url = "+url);
//        BaseRESTClient restClient = new BaseRESTClient(url, getClientConfig());
//        Invocation.Builder reqBuilder = restClient.getWebTarget().request();
//        
//        AuthResponseVO res = reqBuilder.get(AuthResponseVO.class);
//        return res;
//    }

    /**
     * for 日考日清
     * 廠端有可能隨時改 ASSESS_SETTING 設定，改用 RESTful 至廠端抓設定
     * @param hostPort
     * @param restPath
     * @param protect
     * @param issuer
     * @param user
     * @param keyGenerator
     * @return 
     */
//    public static AssessSettingListRsVO getAssessSetting(String hostPort, String restPath, boolean protect, 
//            String issuer, UserProfile user, KeyGenerator keyGenerator){
//        String url = hostPort + GlobalConstant.REST_ROOT + restPath;
//        BaseRESTClient restClient = new BaseRESTClient(url, getClientConfig());
//        logger.debug("getAssessSetting url = "+url);
//        Invocation.Builder reqBuilder = restClient.getWebTarget().request();
//
//        if( protect ){
//            String token = TokenUtils.issueUserToken(user, issuer, null, GlobalConstant.JWT_EXPIRED_MINUTES, keyGenerator);
//            logger.debug("getAssessSetting token = "+token);
//            reqBuilder.header(HttpHeaders.AUTHORIZATION, TokenUtils.JWT_HEADER_PREFIX+token);
//        }
//        
//        AssessSettingListRsVO vo = reqBuilder.get(AssessSettingListRsVO.class);
//        
//        return vo;
//    }
}
