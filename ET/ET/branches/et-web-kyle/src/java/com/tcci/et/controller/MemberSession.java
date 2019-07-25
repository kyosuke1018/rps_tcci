/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.controller;

import com.tcci.et.entity.EtMember;
import com.tcci.et.facade.EtMemberFacade;
import com.tcci.fc.controller.util.JsfUtil;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.org.TcUserFacade;
import com.tcci.security.SecurityConstants;
import java.io.IOException;
import java.io.Serializable;
import java.security.Principal;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.security.enterprise.CallerPrincipal;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.AssertionImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jimmy.Lee
 */
@Named
@SessionScoped
public class MemberSession implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private final static Logger logger = LoggerFactory.getLogger(MemberSession.class);
    // private final static String CAS_LOGOUT_URL = "http://sso.taiwancement.com/cas-server/logout";
    private final static String CONST_CAS_GROUPS = "_const_cas_groups_";       // SSO
    private final static String CONST_CAS_ASSERTION = "_const_cas_assertion_"; // SSO

    // session data
    private String adaccount;      // SSO principal name
    private String newUserAccount; // switch new user
    private String errMsg;         // user not found, permission error, ...
    private EtMember etMember;         // adaccount -> ET_MEMBER 

    @Resource(mappedName = "jndi/global.config")
    private Properties jndiGlobalConfig;

    @EJB
    private TcUserFacade tcUserFacade;
    @EJB
    private EtMemberFacade memberFacade;

    private transient ResourceBundle rb = ResourceBundle.getBundle("msgApp",
            FacesContext.getCurrentInstance().getViewRoot().getLocale());

    @PostConstruct
    private void init() {
        logger.info("init");
        logger.debug("init");
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        Principal p = request.getUserPrincipal();
//        if (null == p) {
//            logger.warn("user principal is null!");
//            errMsg = "user principal is null!";
//            return;
//        }
//        Principal p = request.getUserPrincipal()==null?securityContext.getCallerPrincipal():request.getUserPrincipal();
        HttpSession session = request.getSession(false);
        CallerPrincipal caller = (CallerPrincipal)session.getAttribute(SecurityConstants.CALLER_ATTR);
        if (null == p && null == caller) {
            logger.warn("user principal is null!");
            errMsg = "user principal is null!";
            return;
        }
        adaccount = (null != p)?p.getName():caller.getName();
        loadUserInfo();
    }

    // action
    // switch user action
    public void doSwitchUser() {
        logger.info("doSwitchUser");
        if (!isUserInRole("ADMINISTRATORS")) {
            logger.warn("no permission to switch user!");
            String msg = rb.getString("app.error.nopermissionsimulateuser");
            JsfUtil.addErrorMessage(msg);
            return;
        }
        
        TcUser newTcUser = tcUserFacade.findUserByLoginAccount(newUserAccount);
        if (null == newTcUser) {
            logger.info("tc user not found:{}", newUserAccount);
            String msg = rb.getString("app.error.tcusernotfound");
            JsfUtil.addErrorMessage(msg);
            return;
        }
        
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session = request.getSession();
        Assertion switchAssertion = new AssertionImpl(newUserAccount);
        request.setAttribute(CONST_CAS_ASSERTION, switchAssertion);
        session.setAttribute(CONST_CAS_ASSERTION, switchAssertion);
        session.setAttribute("_SC_CALLER_", new CallerPrincipal(newUserAccount));
        Set<String> groups = newTcUser.getTcUsergroupCollection()
                .stream()
                .map(e -> e.getGroupId().getCode())
                .collect(Collectors.toSet());
        groups.add("Authenticated");
        session.setAttribute("_SC_GROUPS_", groups);
        
        logger.info("switch user from {} to {}", adaccount, newUserAccount);
        
        // switch user
        clearUserInfo();
        adaccount = newUserAccount;
        loadUserInfo();
        newUserAccount = null;
        
        // redirect to home
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            context.getExternalContext().redirect(context.getExternalContext().getRequestContextPath());
        } catch (IOException e) {
            logger.error(e.getMessage());
            JsfUtil.addErrorMessage(e.getMessage());
        }
        context.responseComplete();
    }
    
    // 登出
    public String logout() {
        try {
            clearUserInfo();
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            request.logout();
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            session.invalidate();
//            String logoutURL = System.getProperty("com.taiwancement.sso.logoutUrl");
//            if (null == logoutURL) {
//                logoutURL = jndiGlobalConfig.getProperty("CAS_LOGOUT_URL");
//            }
//            String logoutURL = "logoff.xhtml";
            String logoutURL = context.getExternalContext().getRequestContextPath();
            logger.debug("logoutURL() ", logoutURL);
            context.getExternalContext().redirect(logoutURL);
            context.responseComplete();
        } catch (Exception e) {
            logger.error("logout() exception.", e);
            FacesMessage fMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, fMsg);
        }
        return null;
    }

    // 顯示使用者名稱
    public String getDisplayName() {
        return (null == etMember) ?  adaccount : etMember.getName();
    }

    // 使用者帳號是否存在於TC_USER資料庫
    public boolean isValid() {
//        return (etMember != null && !etMember.getDisabled());
        return (etMember != null && etMember.getActive());
    }

    // 判斷使用者角色是否存在
    public boolean isUserInRole(String roleNameList) {
        String[] roles = roleNameList.split(",");
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        boolean roleFound = false;
        for (int i = 0; i < roles.length; i++) {
            String roleName = roles[i];
            if (request.isUserInRole(roleName)) {
                roleFound = true;
                break;
            }
        }
        return roleFound;
    }

    // helper
    private void loadUserInfo() {
        if (null == adaccount || adaccount.isEmpty()) {
            logger.warn("adaccount is null or blank!");
            errMsg = "adaccount is null or blank!";
            return;
        }
        etMember = memberFacade.findByLoginAccount(adaccount);
        if (null == etMember) {
            logger.warn("etMember not found, adaccount = {}", adaccount);
        }
    }

    private void clearUserInfo() {
        adaccount = null;
        errMsg = null;
        etMember = null;
    }
    
    // getter, setter

    public String getAdaccount() {
        return adaccount;
    }

    public void setAdaccount(String adaccount) {
        this.adaccount = adaccount;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getNewUserAccount() {
        return newUserAccount;
    }

    public void setNewUserAccount(String newUserAccount) {
        this.newUserAccount = newUserAccount;
    }

    public EtMember getEtMember() {
        return etMember;
    }

    public void setEtMember(EtMember etMember) {
        this.etMember = etMember;
    }
}
