/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.util;

import java.net.UnknownHostException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Peter.pan
 */
public class WebUtils {
    private static final Logger logger = LoggerFactory.getLogger(WebUtils.class);
    
    public static void setSessionAttr(HttpServletRequest request, String name, Object value, boolean create){
        if( request!=null ){
            HttpSession session = request.getSession(create);
            if( session!=null ){
                session.setAttribute(name, value);
                logger.info("setSessionAttr ..."+ name + " = " + value);
            }
        }
    }
    public static Object getSessionAttr(HttpServletRequest request, String name){
        if( request!=null ){
            HttpSession session = request.getSession();
            if( session!=null ){
                return session.getAttribute(name);
            }
        }
        return null;
    }
    public static void removeSessionAttr(HttpServletRequest request, String name){
        if( request!=null ){
            HttpSession session = request.getSession();
            if( session!=null ){
                session.removeAttribute(name);
            }
        }
    }
    
    public static String getUrlPrefix(HttpServletRequest request){
        String host = WebUtils.getRequestServerURL(request);
        String contentPath = request.getContextPath();
        
        return host+contentPath;
    }
    
    /**
     * EX. RETURN http://localhost:8080
     * @param request
     * @return 
     */
    public static String getRequestServerURL(HttpServletRequest request){// for RESTful
        //logger.debug("request.getProtocol() = "+request.getProtocol());
        //logger.debug("request.getServerName() = "+request.getServerName());
        //logger.debug("request.getServerPort() = "+request.getServerPort());
        //logger.debug("request.getRequestURL() = "+request.getRequestURL().toString());
        //logger.debug("request.getRequestURI = "+request.getRequestURI());
        // request.getRequestURL() = http://localhost:8080/ics/service/icsNoticeREST/rest/notifyCancel
        // request.getRequestURI = /ics/service/icsNoticeREST/rest/notifyCancel
        String fullurl = request.getRequestURL().toString();
        String serverinfo = fullurl.replaceAll(request.getRequestURI(), "");
        
        return serverinfo;
    }
    
    /**
     * 取得Server端HostName
     *
     * @return
     */
    public static String getHostName() {
        java.net.InetAddress server;
        String serverName = "";
        try {
            server = java.net.InetAddress.getLocalHost();
            serverName = server.getCanonicalHostName();
        } catch (UnknownHostException ex) {
            logger.error("getHostName", ex);
        }
        return serverName;
    }
    
    /**
     * 取得Server端IP 
     *
     * @return
     */
    public static String getHostAddress() {
        java.net.InetAddress server;
        String serverIp = "";
        try {
            server = java.net.InetAddress.getLocalHost();
            serverIp = server.getHostAddress();
        } catch (UnknownHostException ex) {
            logger.error("getHostAddress", ex);
        }
        return serverIp;
    }
}
