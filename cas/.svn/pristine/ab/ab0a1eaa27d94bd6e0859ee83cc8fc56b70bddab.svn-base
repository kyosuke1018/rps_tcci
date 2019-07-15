/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.security;

import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jasig.cas.client.util.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Peter.pan
 */
public class CasUtils {
    private final static Logger LOG = LoggerFactory.getLogger(SecurityUtils.class);
    
    /**
     * 產生導向 CAS Login Page 的 URL
     * @param request
     * @param response
     * @param jndiConfig
     * @return 
     */
    public static String constructRedirectUrl(HttpServletRequest request, HttpServletResponse response, Properties jndiConfig){
        //String casServerLoginUrl = SecurityUtils.getContextParam(request, PN_CAS_LOGIN, DEF_CAS_LOGIN);
        String casServerLoginUrl = SecurityUtils.getFromJNDI(jndiConfig, SecurityConstants.PN_CAS_LOGIN, SecurityConstants.DEF_CAS_LOGIN);
        if( casServerLoginUrl==null ){
            LOG.error("constructRedirectUrl casServerLoginUrl == null !");
            return null;
        }
        
        // CAS 尚無應用的變數，如下固定即可
        String serviceParameterName = "service";
        boolean renew = false;
        boolean gateway = false;

        return CommonUtils.constructRedirectUrl(casServerLoginUrl,
                serviceParameterName,
                constructServiceUrl(request, response, jndiConfig), 
                renew,
                gateway);
    }

    /**
     * 產生目標 Service 的 URL
     * @param request
     * @param response
     * @param jndiConfig
     * @return 
     */
    public static String constructServiceUrl(HttpServletRequest request, HttpServletResponse response, Properties jndiConfig){
        String serverName = SecurityUtils.getFromJNDI(jndiConfig, SecurityConstants.PN_SERVER_NAME, null);
        // CAS 尚無應用的變數，如下固定即可
        String service = null;
        String artifactParameterName = "ticket";
        boolean encodeServiceUrl = false;

        LOG.info("constructServiceUrl serverName = {}", serverName);
        if( serverName == null ){
            String forward = request.getHeader("x-forwarded-host");
            LOG.info("constructServiceUrl forward = {}", forward);
            String host = request.getHeader("host");
            LOG.info("constructServiceUrl host = {}", host);
            serverName = forward != null ? forward : host;
        }

        return CommonUtils.constructServiceUrl(request,
                response,
                service,
                serverName,
                artifactParameterName,
                encodeServiceUrl);
    }
    
}
