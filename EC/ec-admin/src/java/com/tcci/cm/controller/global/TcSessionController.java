/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.controller.global;

import com.tcci.cm.facade.admin.CmFactorygroupFacade;
import com.tcci.cm.facade.admin.CmFunctionFacade;
import com.tcci.cm.facade.admin.PermissionFacade;
import com.tcci.cm.facade.conf.SysResourcesFacade;
import com.tcci.cm.model.admin.CmFactoryGroupVO;
import com.tcci.cm.model.admin.OnlineUsers;
import com.tcci.cm.util.JsfUtils;
import com.tcci.cm.model.admin.MenuFunctionVO;
import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.ec.entity.EcMember;
import com.tcci.ec.enums.MemberTypeEnum;
import com.tcci.ec.facade.EcMemberFacade;
import com.tcci.ec.facade.EcSessionFacade;
import com.tcci.ec.model.MemberVO;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.org.TcUserFacade;
import com.tcci.fc.util.DateUtils;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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

    private static final Logger logger = LoggerFactory.getLogger(TcSessionController.class);
    private final String CONST_CAS_GROUPS = "_const_cas_groups_";
    private final String CONST_CAS_ASSERTION = "_const_cas_assertion_";    
    public final boolean DEBUG_MODE = false;

    @Inject SecurityContext securityContext;
    
    @EJB TcUserFacade tcUserFacade;
    @EJB CmFunctionFacade cmFunctionFacade;
    @EJB CmFactorygroupFacade cmFactorygroupFacade;
    @EJB PermissionFacade permissionFacade;
    @EJB EcSessionFacade sessionFacade;
    @EJB EcMemberFacade memberFacade;
    @EJB SysResourcesFacade sys;

    @Resource(mappedName = "jndi/global.config")
    private Properties jndiGlobalConfig;

    private EcMember member;
    private TcUser loginTcUser;
    private boolean login;
    // original user
    private TcUser origUser;
    private String newUserAccount; // switch new user
    private String adaccount;      // SSO principal name

    private List<MenuFunctionVO> functions;// 可執行功能
    private MenuModel menuModel;// for megamenu

    // fro 提升效率
    private Map<String, List<CmFactoryGroupVO>> specGroupFacoryMap;// 特殊廠群組下轄廠資訊

    private boolean fromInternet;
    private String homePage = GlobalConstant.HOME_PAGE;
    private String logoutPage = GlobalConstant.LOGOUT_PAGE;

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
            logger.info("loginAccount = " + loginAccount + " login fail !");
        } else {
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
        logger.info("getLoginAccount ... securityContext = "+securityContext);
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
        Boolean disabled = null;
        loginTcUser = tcUserFacade.findUserByLoginAccount(loginAccount, disabled);

        if (loginTcUser != null) {
            //logger.debug("getCmUsercompanyList() =" + ((loginTcUser.getCmUsercompanyList()!=null)?loginTcUser.getCmUsercompanyList().size():0));
            //logger.debug("getCmUserfactoryList() =" + ((loginTcUser.getCmUserfactoryList()!=null)?loginTcUser.getCmUserfactoryList().size():0));
            //logger.debug("getCmUserFactorygroupRList() =" + ((loginTcUser.getCmUserFactorygroupRList()!=null)?loginTcUser.getCmUserFactorygroupRList().size():0));
            //logger.debug("getTcUsergroupCollection() =" + ((loginTcUser.getTcUsergroupCollection()!=null)?loginTcUser.getTcUsergroupCollection().size():0));
            if (!loginTcUser.getDisabled()) {
                login = true;
                // for ec-seller
                // Administrator 若無 EC_MEMBER 資料則新增
                if( this.isAdministrator() && loginTcUser.getMemberId()==null ){
                    // EC_MEMBER 新增系統管理人員，回傳 loginTcUser.getMemberId()
                    addAdminUser(loginTcUser);
                }
                if( loginTcUser.getMemberId()!=null ){
                    member = memberFacade.find(loginTcUser.getMemberId());//.fundForInternal(loginAccount);
                }
                logger.debug("initUserSession member = " + member);
                //
                logger.debug("initUserSession Before findUserCompany ... " + DateUtils.format(new Date()));
                //company = cmOrgFacade.findById(loginTcUser.getOrgId()); // userFacade.findUserCompany(loginTcUser);
                logger.debug("initUserSession After findUserCompany ... " + DateUtils.format(new Date()));
                //if( company == null ){// 公司資訊非必要
                //    JsfUtils.addErrorMessage("帳號 "+loginAccount+ " 無所屬公司資訊，請洽台訊客服人員。");
                //    return false;
                //}
                initMenuByUser(loginTcUser); // 功能選單
                logger.debug("initUserSession After initMenuByUser ... " + DateUtils.format(new Date()));

                // 準備特殊廠別群組與下轄廠對應
                /*
                prepareSpecGroupFacoryMap();
                logger.debug("After prepareSpecGroupFacoryMap ... " + DateUtils.format(new Date()));
                 */
                // 新增線上使用者
                OnlineUsers.action(OnlineUsers.ACTION_ADD, loginTcUser.getLoginAccount(), loginTcUser);
                logger.debug("initUserSession After OnlineUsers.ACTION_ADD ... " + DateUtils.format(new Date()));
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
            logger.error("logout exception : ", e.getMessage());
            // ignore;
            logger.debug("logout exception :\n", e);
        }

        return "?faces-redirect=true";
    }

    /**
     * for ec-amin link to ec-seller
     * @return 
     */
    public String toEcSellerAdmin(){
        try {
            if( !isAdministrator()
                || member==null || member.getId()==null 
                || !sys.isFalse(member.getDisabled()) 
                || !sys.isTrue(member.getActive()) 
                || !sys.isTrue(member.getAdminUser()) ){
                logger.error("toEcSellerAdmin error not adminUser in ec-seller !");
                // 您無此功能權限，若有此功能需求請提出申請。
                return JsfUtils.getResourceTxt("label.noPermission");
            }            
            // ADD EC_SESSION (期限 10 Seconds)
            String sessionKey = sessionFacade.newSession(member.getId(), DateUtils.addSeconds(new Date(), 10), GlobalConstant.INTERNAL_PREFIX_SK);
            
            FacesContext context = FacesContext.getCurrentInstance();
            if (context != null && context.getExternalContext() != null) {
                String url = sys.getSellerWebUrl(); // http://localhost:8383/ec-seller
                url = url + GlobalConstant.URL_WEB_ADMIN_LOGIN + "?from=internal&token="+sessionKey+"&loginAccount="+member.getLoginAccount();
                context.getExternalContext().redirect(url);
                context.responseComplete();
            }
        } catch (Exception e) {
            logger.error("toEcSellerAdmin exception : ", e.getMessage());
            // ignore;
            logger.debug("toEcSellerAdmin exception :\n", e);
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
        //List<Long> groupIds = new ArrayList<Long>();
        if (user != null && user.getTcUsergroupCollection() != null) {
            // TC_GROUP項目多時，有效能問題
            //groupIds = new ArrayList();
            //for(TcUsergroup group: user.getTcUsergroupCollection()){
            //    groupIds.add(group.getGroupId().getId());
            //}
            // functions = permissionFacade.fetchFunctionInfoByGroupList(groupIds);

            functions = permissionFacade.fetchFunctionInfoByUser(user.getId());
            logger.debug("functions = " + ((functions != null) ? functions.size() : 0));
        }
        //logger.debug("groupIds = "+groupIds.size());

        // 依USER權限建置功能選單
        buildMenuByUser();
    }

    /**
     * 依USER權限建置功能選單
     */
    public void buildMenuByUser() {
        // initialize the model
        String urlPrefix = JsfUtils.getContextPath();
        menuModel = new DefaultMenuModel();
        if( !cmFunctionFacade.createMenu(functions, menuModel, urlPrefix, false, fromInternet, this.isAdministrator()) ){
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
        return cmFunctionFacade.showTitle(func_id, functions, locale);
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

    /**
     * 廠端使用者 (PLANT_USER 需設定於 *.xml)
     *
     * @return
     */
    public boolean isPlantUser() {
        return isUserInRole(GlobalConstant.UG_PLANT_USER);
    }
    
    public boolean isAdministrator() {
        return isUserInRole(GlobalConstant.UG_ADMINISTRATORS);
    }

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

    private void clearUserInfo() {
        adaccount = null;
        loginTcUser = null;
    }    
    
    /**
     * EC_MEMBER 新增系統管理人員
     */
    public void addAdminUser(TcUser user){
        logger.info("addAdminUser ...");
        MemberVO memberVO = new MemberVO();
        
        memberVO.setCname(user.getName());
        memberVO.setLoginAccount(user.getLoginAccount());
        memberVO.setEmail(user.getEmail());
        memberVO.setActive(true);
        memberVO.setAdminUser(true);
        memberVO.setDisabled(false);
        memberVO.setMemberType(MemberTypeEnum.PERSON.getCode());
        memberVO.setName(user.getName());
        memberVO.setNickname(user.getName());
        memberVO.setTccDealer(false);
        memberVO.setTccDs(false);
        memberVO.setVerifyCode(DateUtils.formatDateString(new Date(), GlobalConstant.FORMAT_DATETIME_STR));
        
        memberFacade.saveVO(memberVO, null, JsfUtils.getRequestLocale(), false);
        user.setMemberId(memberVO.getMemberId());// 回傳
        tcUserFacade.edit(user);// 記錄到 TC_USER
    }

    //<editor-fold defaultstate="collapsed" desc="Getter and Setter">
    public List<MenuFunctionVO> getFunctions() {
        return functions;
    }

    public void setFunctions(List<MenuFunctionVO> functions) {
        this.functions = functions;
    }

    public EcMember getMember() {
        return member;
    }

    public void setMember(EcMember member) {
        this.member = member;
    }

    public String getAdaccount() {
        return adaccount;
    }

    public void setAdaccount(String adaccount) {
        this.adaccount = adaccount;
    }

    public boolean isFromInternet() {
        return fromInternet;
    }

    public void setFromInternet(boolean fromInternet) {
        this.fromInternet = fromInternet;
    }

    public Map<String, List<CmFactoryGroupVO>> getSpecGroupFacoryMap() {
        return specGroupFacoryMap;
    }

    public void setSpecGroupFacoryMap(Map<String, List<CmFactoryGroupVO>> specGroupFacoryMap) {
        this.specGroupFacoryMap = specGroupFacoryMap;
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
    //</editor-fold>

}
