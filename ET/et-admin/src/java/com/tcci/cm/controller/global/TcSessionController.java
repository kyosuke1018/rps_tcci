/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.tcci.cm.controller.global;

import com.tcci.cm.enums.SecurityRoleEnum;
import com.tcci.cm.facade.admin.CmFunctionFacade;
import com.tcci.cm.facade.admin.PermissionFacade;
import com.tcci.cm.facade.admin.UserGroupFacade;
import com.tcci.cm.model.admin.OnlineUsers;
import com.tcci.cm.util.JsfUtils;
import com.tcci.et.model.admin.MenuFunctionVO;
import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.org.TcUserFacade;
import com.tcci.fc.util.DateUtils;
import com.tcci.et.facade.global.CommonUIFacade;
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
import javax.security.enterprise.SecurityContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.AssertionImpl;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.MenuModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Session 管理程式
 *
 * @author Peter
 */
@ManagedBean(name = "tcSessionController")
@SessionScoped
public class TcSessionController implements Serializable {
    private final Logger logger = LoggerFactory.getLogger(TcSessionController.class);    

    private final String CONST_CAS_GROUPS = "_const_cas_groups_";
    private final String CONST_CAS_ASSERTION = "_const_cas_assertion_";    
    public final boolean DEBUG_MODE = false;
    
    @EJB TcUserFacade tcUserFacade;
    @EJB UserGroupFacade userGroupFacade;
    @EJB CmFunctionFacade cmFunctionFacade;
    @EJB PermissionFacade permissionFacade;
    @EJB CommonUIFacade commonUIFacade;

    @Inject SecurityContext securityContext;
    
    @Resource(mappedName = "jndi/global.config")
    private Properties jndiGlobalConfig;
    
    private TcUser loginTcUser;
    private boolean login;
    private boolean simulated;// 模擬使用者
    // original user
    private TcUser origUser;
    private String newUserAccount; // switch new user
    private String adaccount;      // SSO principal name
    
    private List<MenuFunctionVO> functions;// 可執行功能
    //private MenuModel megaModel;// for megamenu
    private MenuModel menuModel;//RWD
    
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
            buildMenuByUser();
            //logout();//不豋出SSO，顯示無帳號
            logger.info("loginAccount = "+loginAccount+" login fail !");
        }else{
            origUser = loginTcUser;
        }
    }
    
    @PreDestroy
    public void autoLogout() {
        if (login && loginTcUser != null) {
            logger.info(loginTcUser.getLoginAccount() + " auto logout !");
            logout();
        }
    }
    
    /**
     * Get Login User Account form HttpServletRequest UserPrincipal
     *
     * @return
     */
    public String getLoginAccount() {
        logger.info("init ... securityContext = "+securityContext);
        if( securityContext!=null ){
            if( securityContext.getCallerPrincipal()!=null ){
                String account = securityContext.getCallerPrincipal().getName();
                return account;
            }
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
        loginTcUser = tcUserFacade.findUserByLoginAccount(loginAccount);
        
        if (loginTcUser != null) {
            if (!loginTcUser.getDisabled()) {
                login = true;
                logger.debug("Before findUserCompany ... " + DateUtils.format(new Date()));
                //company = cmOrgFacade.findById(loginTcUser.getOrgId()); // userFacade.findUserCompany(loginTcUser);
                logger.debug("After findUserCompany ... " + DateUtils.format(new Date()));
                //if( company == null ){// 公司資訊非必要
                //    JsfUtils.addErrorMessage("帳號 "+loginAccount+ " 無所屬公司資訊，請洽台訊客服人員。");
                //}else{
                initMenuByUser(loginTcUser); // 功能選單
                logger.debug("After initMenuByUser ... " + DateUtils.format(new Date()));
                // 取得角色權限
                refleshUserGroups();
                
                // 新增線上使用者
                OnlineUsers.action(OnlineUsers.ACTION_ADD, loginTcUser.getLoginAccount(), loginTcUser);
                logger.debug("After OnlineUsers.ACTION_ADD ... " + DateUtils.format(new Date()));
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
        if( loginTcUser!=null && loginTcUser.getId()!=null ){
            userGroups = userGroupFacade.findUserGroupCodes(loginTcUser.getId());
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
            if (loginTcUser != null) {
                OnlineUsers.action(OnlineUsers.ACTION_REMOVE, loginTcUser.getLoginAccount());
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
                
                String url = jndiGlobalConfig.getProperty("CAS_LOGOUT_URL");// SSO logout page
                //String url = getHostURL();
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
     * init user menu
     *
     * @param user
     */
    public void initMenuByUser(TcUser user) {
        // 有權執行的功能
        if (user != null && user.getTcUsergroupCollection() != null) {
            functions = permissionFacade.fetchFunctionInfoByUser(user.getId());
            logger.debug("functions = " + ((functions != null) ? functions.size() : 0));
        }
        
        // 依USER權限建置功能選單
        buildMenuByUser();
    }
    
    /**
     * 依USER權限建置功能選單
     */
    public void buildMenuByUser() {
        // initialize the model
        //megaModel = new DefaultMenuModel();
        menuModel = new DefaultMenuModel();
        String urlPrefix = "";// JsfUtils.getContextPath();
        menuModel = new DefaultMenuModel();
        if( !commonUIFacade.createMenu(functions, menuModel, urlPrefix, false, fromInternet, this.isAdministrator()) ){
            JsfUtils.addErrorMessage(JsfUtils.getResourceTxt("label.nomenu"));// 無可執行的選單資訊!
        }
    }
    
    /**
     * 頁面顯示功能標題
     *
     * @param func_id
     * @return
     */
    public String getFunctionTitle(long func_id) {
        Locale locale = JsfUtils.getRequestLocale();
        return commonUIFacade.showTitle(func_id, functions, locale);
    }
    
    /**
     * 維持不過期
     */
    public void keeyAlive() {
        logger.debug("keeyAlive ...");
    }
    
    /**
     * 可用功能包含特定模組
     *
     * @param moduleCode
     * @return
     */
    public boolean containModule(String moduleCode) {
        if (functions != null && !functions.isEmpty()) {
            for (MenuFunctionVO vo : functions) {
                if (moduleCode.equals(vo.getMcode())) {
                    return true;
                }
            }
        }
        return false;
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

    private void clearUserInfo() {
        adaccount = null;
        loginTcUser = null;
    }    
    
    //<editor-fold defaultstate="collapsed" desc="模擬使用者">
    /**
     * Simulate User
     */
    public void doSimulateUser() {
        logger.info("doSimulateUser");
        if( !isAdministrator() ){
            logger.warn("no permission to switch user!");
            JsfUtils.addErrorMessage(JsfUtils.getResourceTxt("app.error.nopermissionsimulateuser"));
            return;
        }
        
        TcUser newTcUser = tcUserFacade.findUserByLoginAccount(newUserAccount);
        if (null == newTcUser) {
            logger.info("tc user not found:{}", newUserAccount);
            JsfUtils.addErrorMessage(JsfUtils.getResourceTxt("app.error.tcusernotfound"));
            return;
        }
        
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session = request.getSession();
        Assertion switchAssertion = new AssertionImpl(newUserAccount);
        request.setAttribute(CONST_CAS_ASSERTION, switchAssertion);
        session.setAttribute(CONST_CAS_ASSERTION, switchAssertion);
        session.setAttribute(CONST_CAS_GROUPS, null);
        logger.info("switch user from {} to {}", adaccount, newUserAccount);
        
        clearUserInfo();
        
        if( !initUserSession(newUserAccount) ){
            adaccount = newUserAccount;
            newUserAccount = null;
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Getter and Setter">
    public List<MenuFunctionVO> getFunctions() {
        return functions;
    }
    
    public void setFunctions(List<MenuFunctionVO> functions) {
        this.functions = functions;
    }

    public String getAdaccount() {
        return adaccount;
    }

    public void setAdaccount(String adaccount) {
        this.adaccount = adaccount;
    }
    
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
        return loginTcUser.getLoginAccount();
    }
    
    public TcUser getLoginTcUser() {
        if (null == loginTcUser) {
            init();
        }
        return loginTcUser;
    }
    
    public void setLoginTcUser(TcUser loginUser) {
        this.loginTcUser = loginUser;
    }
    
    public MenuModel getMenuModel() {
        return menuModel;
    }
    
    public void setMenuModel(MenuModel menuModel) {
        this.menuModel = menuModel;
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
    
    public TcUser getOrigUser() {
        return origUser;
    }
    
    public void setOrigUser(TcUser origUser) {
        this.origUser = origUser;
    }
    
    public String getNewUserAccount() {
        return newUserAccount;
    }
    
    public void setNewUserAccount(String newUserAccount) {
        this.newUserAccount = newUserAccount;
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
            return JsfUtils.getResourceTxt("label.hello") + " " + loginTcUser.getName() + "(" + loginTcUser.getLoginAccount() + ")"; // 您好
        } else {
            return JsfUtils.getResourceTxt("label.no.account"); // 本系統無此帳號";
        }
    }
    //</editor-fold>
    
}
