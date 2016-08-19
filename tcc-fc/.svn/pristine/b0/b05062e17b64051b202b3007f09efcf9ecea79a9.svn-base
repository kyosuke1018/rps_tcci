/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fcservice.controller.login;

import com.tcci.fc.entity.org.TcGroup;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.entity.org.TcUsergroup;
import com.tcci.fc.facade.org.TcUserFacade;

import java.io.IOException;
import java.io.Serializable;
import java.security.Principal;
import java.util.List;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jimmy.Lee
 */
@ManagedBean(name = "userSession")
@SessionScoped
public class UserSession implements Serializable {

    @Resource(mappedName = "jndi/mygui.config")
    private Properties jndiMyguiConfig;
    @EJB
    private TcUserFacade tcUserFacade;
 
    protected final static Logger logger = LoggerFactory.getLogger(UserSession.class);
    // login user data
    private TcUser tcUser; // 登入的 user
    private String adaccount;
    private String errMsg;
    //personalization
 
    private List<TcUsergroup> userGroups;
    private TcGroup defaultsGrp;

    @PostConstruct
    private void init() {
        initTcUser();
    }

    /**
     * 清空相關登入資訊
     */
    public void clear() {
        //System.out.println("UserSession.switchUser");
        logger.info("clear() invoked.");
        adaccount = null;
        tcUser = null;
    }

    /**
     * Session是否合法
     * @return boolean
     */
    public boolean getValid() {
        boolean valid = (tcUser != null);
        return (valid);
    }

    /**
     * getAdaccount
     * @return Adaccount
     */
    public String getAdaccount() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        Principal p = request.getUserPrincipal();
        if (p != null) {
            adaccount = p.getName();
        }
        return adaccount;
    }

    /**
     * getTcUser
     * @return TcUser
     */
    public TcUser getTcUser() {
        /* PostConstruct 已執行過initTcUser, 避免一直呼叫(TC_USER無資料時) */
        if (tcUser == null) {
            initTcUser();
        }
        return tcUser;
    }

    /**
     * getUserCodeName
     * @return LoginAccount+"-"+Cname
     */
    public String getUserCodeName() {
        return (getTcUser() == null) ? adaccount : tcUser.getLoginAccount() + " - " + tcUser.getCname();
    }

    /**
     * 登出
     * @return 
     */
    public String logout() {
        try {
            this.clear();
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            request.logout();
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            session.invalidate();
            String url = jndiMyguiConfig.getProperty("CAS_LOGOUT_URL");
            context.getExternalContext().redirect(url);
            context.responseComplete();
        } catch (Exception e) {
            logger.error("logout() exception.", e);
            FacesMessage fMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, fMsg);
        }
        return "?faces-redirect=true";
    }

    /**
     * usage 
     * java isUserInRole("super-hr,super-role")
     * UI #{!userSession.isUserInRole('super-hr,super-role')}
     * @param roleNameList
     * @return 
     */
    public boolean isUserInRole(String roleNameList) {
        boolean flag = Boolean.FALSE;
        String[] roles = roleNameList.split(",");
        for (int i = 0; i < roles.length; i++) {
            String roleName = roles[i];

            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            flag = Boolean.valueOf(request.isUserInRole(roleName));
            if (flag) {
                break;
            }
        }
        return flag;
    }

    /**
     * getHostURL
     * @return http://<serverName>:<ServerPort>
     */
    public String getHostURL() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    }

    public void checkTcUser() {
        if (tcUser == null) {
            initTcUser();
        }
    }
//private method

    private void initTcUser() {
        getAdaccount();
        try {
            logger.info("initTcUser() adaccount = {}", adaccount);
            //tcUser = tcUserFacade.getSessionUser();
            tcUser = tcUserFacade.findUserByLoginAccount(adaccount);
            if (tcUser == null) {
                errMsg = "查無員工資料!";
                logger.error("initTcUser() adaccount not found, ad = {}", adaccount);
                FacesContext context = FacesContext.getCurrentInstance();
                context.getExternalContext().getFlash().setKeepMessages(true);
                try {

                    context.getExternalContext().redirect("/mygui/faces/deny.xhtml");
                } catch (IOException ex) {
//                logger.error("redirect reportItemView.xhtml exception", ex);
                }
                context.responseComplete();
            }
         
            userGroups = (List) tcUser.getTcUsergroupCollection();
            for (TcUsergroup userGrp : userGroups) {
                if (userGrp.getGroupId() != null) {
                    if ("ADMINISTRATORS".equalsIgnoreCase(userGrp.getGroupId().getCode()) || "GRP_TCC".equalsIgnoreCase(userGrp.getGroupId().getCode())) {
                        this.setDefaultsGrp(userGrp.getGroupId());
                        break;
                    }
                    if ("GRP_CSRC".equalsIgnoreCase(userGrp.getGroupId().getCode()) || "GRP_TPCC".equalsIgnoreCase(userGrp.getGroupId().getCode()) || "GRP_SKNG".equalsIgnoreCase(userGrp.getGroupId().getCode())) {
                        this.setDefaultsGrp(userGrp.getGroupId());
                    }
                }
            }
        } catch (Exception ex) {
            errMsg = "系統忙碌中, 請稍後再試!";
            logger.error("initEmployee() exception", ex);
        }
    }
//get,set method

    public Properties getJndiMyguiConfig() {
        return jndiMyguiConfig;
    }

    public void setJndiMyguiConfig(Properties jndiMyguiConfig) {
        this.jndiMyguiConfig = jndiMyguiConfig;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
    public List<TcUsergroup> getUserGroups() {
        return userGroups;
    }

    public void setUserGroups(List<TcUsergroup> userGroups) {
        this.userGroups = userGroups;
    }

    public TcGroup getDefaultsGrp() {
        return defaultsGrp;
    }

    public void setDefaultsGrp(TcGroup defaultsGrp) {
        this.defaultsGrp = defaultsGrp;
    }
}
