/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.solr.controller.global;

import java.io.IOException;
import java.io.Serializable;
import java.security.Principal;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Session 管理程式
 * @author Peter
 */
@ManagedBean(name = "tcSessionController")
@SessionScoped
public class TcSessionController implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(TcSessionController.class);
    
    public final boolean DEBUG_MODE = false;
    public final String ROLE_ADMINISTRATORS = "Administrators";
    
    @Resource (mappedName="jndi/global.config")
    private Properties jndiGlobalConfig;

    private String loginAccount;
    private boolean login = false;
    
    @PostConstruct
    public void init(){
        logger.info("init ...");        
        loginAccount = getLoginAccount();
        
        if( DEBUG_MODE ){
            loginAccount = "peter.pan";
        }
        
        logger.info(loginAccount + " session login ...");
        
        if( isAdminstratorsRole() ){
            login = true;
        }else{
            logout();
        }
    }
    
    /**
     * Get Login User Account form HttpServletRequest UserPrincipal
     * @return 
     */
    public String getLoginAccount() {
        String name = null;
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        Principal p = request.getUserPrincipal();
        if (p != null) {
            name = p.getName();
        }
        return name;
    }
    
    /**
     * 登入者是否為系統管理者
     *
     * @return
     */
    public boolean isAdminstratorsRole() {
        if( DEBUG_MODE ){ return true; }
        final ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
        return ctx.isUserInRole(ROLE_ADMINISTRATORS);
    }
    
    /**
     * 登出系統
     * @return 
     */
    public String logout(){
        try {
            login = false;
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            request.logout();
            HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
            session.invalidate();
            
            String url = jndiGlobalConfig.getProperty("CAS_LOGOUT_URL");
            //String url = getHostURL();
            context.getExternalContext().redirect(url);
            context.responseComplete();
        } catch (ServletException e) {
            logger.error("logout ServletException :\n", e);
        } catch (IOException e) {
            logger.error("logout IOException :\n", e);
        }
        
        return "?faces-redirect=true";
    }
    
    /**
     * 維持不過期
     */
    public void keeyAlive(){
        logger.debug("keeyAlive ...");
    }
    
    //<editor-fold defaultstate="collapsed" desc="Getter and Setter">   
    public boolean isLogin(){
        return this.login;
    }
    //</editor-fold>
}
