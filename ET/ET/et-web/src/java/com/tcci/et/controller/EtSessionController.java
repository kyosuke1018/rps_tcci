/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.controller;

import com.tcci.cm.enums.SecurityRoleEnum;
import com.tcci.cm.util.JsfUtils;
import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.fc.util.DateUtils;
import com.tcci.et.entity.EtMember;
import com.tcci.et.facade.EtMemberFacade;
import com.tcci.security.SecurityConstants;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.security.enterprise.CallerPrincipal;
import javax.security.enterprise.SecurityContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.primefaces.model.menu.MenuModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Session 管理程式
 *
 * @author Peter
 */
@ManagedBean(name = "etSessionController")
@SessionScoped
public class EtSessionController implements Serializable {
    private final Logger logger = LoggerFactory.getLogger(EtSessionController.class);

    public final boolean DEBUG_MODE = false;

    @EJB EtMemberFacade memberFacade;
    
    @Inject SecurityContext securityContext;
    
    @Resource(mappedName = "jndi/global.config")
    private Properties jndiGlobalConfig;

    private EtMember loginMember;
    private boolean login;
    private boolean simulated;// 模擬使用者

    private MenuModel megaModel;// for megamenu
    
    private List<String> userGroups;
    
    // for Internet access (SD only)
    private boolean fromInternet;
    private String homePage = GlobalConstant.URL_HOME_PAGE;
    private String logoutPage = GlobalConstant.URL_LOGOUT_PAGE;
    // private String feedbackPage = GlobalConstant.FB_CALLBACK_URL;

    @PostConstruct
    public void init() {
        logger.info("init ...");
        String loginAccount = getLoginAccount();

        if (DEBUG_MODE) {
            loginAccount = "peter.pan";
        }

        if (loginAccount == null || loginAccount.isEmpty()) {
            logger.info("initUserSession loginAccount is null!");
            return;
        }

        if (!initUserSession(loginAccount)) {
            // 無權限仍可看到 Z.說明
//            buildMenuByUser();
            //logout();//不豋出SSO，顯示無帳號
            logger.info("loginAccount = "+loginAccount+" login fail !");
        }
    }

    @PreDestroy
    public void autoLogout() {
        if (login && loginMember != null) {
            logger.info(loginMember.getLoginAccount() + " auto logout !");
            logout();
        }
    }

    /**
     * Get Login User Account form HttpServletRequest UserPrincipal
     *
     * @return
     */
    public String getLoginAccount() {
        logger.debug("getLoginAccount ... securityContext = "+securityContext);
        if( securityContext!=null ){
            logger.debug("getLoginAccount ... CallerPrincipal = "+securityContext.getCallerPrincipal());
            if( securityContext.getCallerPrincipal()!=null ){
                String account = securityContext.getCallerPrincipal().getName();
                return account;
            }
        }
        //第二段取account
        HttpServletRequest request = JsfUtils.getRequest();
        HttpSession session = request.getSession(false);
        CallerPrincipal caller = (CallerPrincipal)session.getAttribute(SecurityConstants.CALLER_ATTR);
        if(caller!=null){
            String account = caller.getName();
            return account;
        }
        logger.error("getLoginAccount error account = null");
        return null;
        /* for GlassFish 
        String name = null;
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        Principal p = request.getUserPrincipal();
        if (p != null) {
            name = p.getName();
        }
        return name;
        */
    }

    /**
     * 初始因 Session User 不同，需重設的關聯資訊
     *
     * @param loginAccount
     * @return
     */
    public boolean initUserSession(String loginAccount) {
        // Boolean disabled = null;
        loginMember = memberFacade.findByLoginAccount(loginAccount);

        if (loginMember != null) {
//            if (!loginMember.getDisabled()) {
            if (loginMember.getActive()) {
                login = true;
//                initMenuByUser(loginTcUser); // 功能選單
                logger.debug("After initMenuByUser ... " + DateUtils.format(new Date()));
                // 取得角色權限
                refleshUserGroups();

            } else {
                // 此帳號無本系統權限, 請填[帳號權限申請單]申請或洽台訊客服 (service@tcci.com.tw)
                JsfUtils.addErrorMessage(JsfUtils.getResourceTxt("label.noAccount"));
                return false;
            }
        } else {
            JsfUtils.addErrorMessage(JsfUtils.getResourceTxt("label.noAccount"));
            return false;
        }

        return true;
    }

    /**
     * 取得角色權限
     */
    public void refleshUserGroups(){
        if( loginMember!=null && loginMember.getId()!=null ){
//            userGroups = userGroupFacade.findUserGroupCodes(loginTcUser.getId());
        }else{
            logger.error("refleshUserGroups error no userGroups!");
        }
    }
    
    /**
     * 登出系統
     *
     * @return
     */
    public String logout() {
        try {
            // 刪除線上使用者
            if (loginMember != null) {
//                OnlineUsers.action(OnlineUsers.ACTION_REMOVE, loginTcUser.getLoginAccount());
            }

            login = false;
            FacesContext context = FacesContext.getCurrentInstance();
            if (context != null && context.getExternalContext() != null) {
                if (context.getExternalContext().getRequest() != null) {
                    HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
                    request.logout();
                }

                if (context.getExternalContext().getSession(false) != null) {
                    HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
                    session.invalidate();
                }

//                String url = jndiGlobalConfig.getProperty("CAS_LOGOUT_URL");// SSO logout page
//                String url = getHostURL();
                String url = context.getExternalContext().getRequestContextPath();
                context.getExternalContext().redirect(url);
                context.responseComplete();
            }
        } catch (Exception e) {
            // ignore;
            logger.debug("logout exception :\n", e);
        }

        return "?faces-redirect=true";
    }


    /**
     * 維持不過期
     */
    public void keeyAlive() {
        logger.debug("keeyAlive ...");
    }

    public boolean isAdministrator(){
        return (userGroups!=null && userGroups.contains(SecurityRoleEnum.ADMINISTRATORS.getCode()));
    }
    
    public boolean isUserInGroup(String groupCode) {
        return (userGroups!=null && userGroups.contains(groupCode));
    }
    
    /**
     * 判斷使用者角色是否存在 (設定於 *.xml)
     *
     * @param roleNameList
     * @return
     */
    public boolean isUserInRole(String roleNameList) {
        String[] roles = roleNameList.split(",");
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        boolean roleFound = false;
        for (String roleName : roles) {
            if (request.isUserInRole(roleName)) {
                roleFound = true;
                break;
            }
        }
        return roleFound;
    }   
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Getter and Setter">

    public boolean isSimulated() {
        return simulated;
    }

    public void setSimulated(boolean simulated) {
        this.simulated = simulated;
    }

    public List<String> getUserGroups() {
        return userGroups;
    }

    public void setUserGroups(List<String> userGroups) {
        this.userGroups = userGroups;
    }

    public boolean isFromInternet() {
        return fromInternet;
    }

    public void setFromInternet(boolean fromInternet) {
        this.fromInternet = fromInternet;
    }

    public String getLoginUser() {
        return loginMember.getLoginAccount();
    }

    public EtMember getLoginMember() {
        if (null == loginMember) {
            init();
        }
        return loginMember;
    }

    public void setLoginTcUser(EtMember loginUser) {
        this.loginMember = loginUser;
    }

    public MenuModel getMegaModel() {
        return megaModel;
    }

    public void setMegaModel(MenuModel megaModel) {
        this.megaModel = megaModel;
    }

    public boolean isLogin() {
        return this.login;
    }

    public Properties getJndiGlobalConfig() {
        return jndiGlobalConfig;
    }

    public String getHomePage() {
        return homePage;
    }

    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }

    public String getLogoutPage() {
        return logoutPage;
    }

    public void setLogoutPage(String logoutPage) {
        this.logoutPage = logoutPage;
    }

    public void setJndiGlobalConfig(Properties jndiGlobalConfig) {
        this.jndiGlobalConfig = jndiGlobalConfig;
    }

    public String getLoginWelcomeMsg() {
        if (login) {
            return JsfUtils.getResourceTxt("label.hello") + " " + loginMember.getName() + "(" + loginMember.getLoginAccount() + ")"; // 您好
        } else {
            return JsfUtils.getResourceTxt("label.no.account"); // 本系統無此帳號";
        }
    }
    
    // 顯示使用者名稱
    public String getDisplayName() {
        return (null == loginMember) ?  "" : loginMember.getName();
    }
    //</editor-fold>

}
