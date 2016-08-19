/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fcservice.controller.login;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.org.TcUserFacade;
import com.tcci.fcservice.controller.util.JsfUtil;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.StringUtils;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.AssertionImpl;

/**
 *
 * @author Gilbert.Lin
 */
@ManagedBean(name = "switchUserController")
@RequestScoped
public class SwitchUserController {

    public final String CONST_CAS_GROUPS = "_const_cas_groups_";
    public final String CONST_CAS_ASSERTION = "_const_cas_assertion_";
    @EJB
    transient private TcUserFacade tcUserFacade;
    @ManagedProperty(value = "#{userSession}")
    private UserSession userSession;
    private static final Logger logger = Logger.getLogger(SwitchUserController.class.getName());
    private String userID;

    public String doSwitchUser() {
        logger.info("doSwitchUser");
        if (!userSession.isUserInRole("Administrators")) {
            String msg = "您沒有權限變更登入者!";
            logger.info(msg);
            JsfUtil.addErrorMessage(msg);
            clear();
            return null;
        }
        
        logger.info(userID + " is Administrators role");
        TcUser newTcUser = tcUserFacade.findUserByLoginAccount(userID);
        if (newTcUser == null) {
            String errMsg = "查無員工資料!";
            logger.info(errMsg);
            JsfUtil.addErrorMessage(errMsg);
            return null;
        }
        String newAdaccount = newTcUser.getLoginAccount();
        if (StringUtils.isEmpty(newAdaccount)) {
            String errMsg = "員工資料查無登入帳號,請先補建資料!";
            logger.info(errMsg);
            JsfUtil.addErrorMessage(errMsg);
            clear();
            return null;
        }
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session = request.getSession();
        Assertion assertion = (Assertion) session.getAttribute(CONST_CAS_ASSERTION);
        AttributePrincipal princpal = assertion.getPrincipal();
        String origialname = princpal.getName();
        Assertion switchAssertion = new AssertionImpl(newTcUser.getLoginAccount());
        request.setAttribute(CONST_CAS_ASSERTION, switchAssertion);
        session.setAttribute(CONST_CAS_ASSERTION, switchAssertion);
        session.setAttribute(CONST_CAS_GROUPS, null);
        logger.info("switch user origial user=" + origialname + ", new user=" + newAdaccount);
        //
        userSession.clear();
        /* 首頁並未 show messages
        String msg = "變更成功!";
        JsfUtil.addSuccessMessage(msg);
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getFlash().setKeepMessages(true);
         */
        clear();
        return "../myGUI.xhtml?faces-redirect=true";
    }

    /**
     * 清空相關登入資訊
     */
    public void clear() {
        //System.out.println("UserSession.switchUser");
        logger.info("clear() invoked.");
        userID = null;
    }
//get,set method

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public UserSession getUserSession() {
        return userSession;
    }

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }
}
