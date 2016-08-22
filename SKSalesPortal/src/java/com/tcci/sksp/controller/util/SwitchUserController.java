package com.tcci.sksp.controller.util;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.org.TcUserFacade;
import java.io.IOException;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
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
 * @author nEO.Fu
 */
@ManagedBean(name = "switchUserController")
@RequestScoped
public class SwitchUserController {

    public final String CONST_CAS_GROUPS = "_const_cas_groups_";
    public final String CONST_CAS_ASSERTION = "_const_cas_assertion_";
    @EJB
    private TcUserFacade userFacade;
    @ManagedProperty(value = "#{sessionController}")
    private SessionController userSession;
    private String userID;
    private static final Logger logger = Logger.getLogger(SwitchUserController.class.getName());
    private TcUser user;

    public void doSwitchUser() throws IOException {
        //System.out.println("doSwitchUser");
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        if (request.isUserInRole("Administrators")) {
            //System.out.println(userID + " is Administrators");
            user = userFacade.findUserByLoginAccount(userID);
            if (user == null) {
                String errMsg = "查無員工帳號資料!";
                logger.info(errMsg);
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, errMsg, errMsg);
                FacesContext.getCurrentInstance().addMessage(null, message);
                return;
            }
            //if (StringUtils.isStringNotNull(user.getLoginAccount())) {
            if( StringUtils.isEmpty( user.getLoginAccount() ) ){
                String errMsg = "員工資料查無登入帳號,請先補建資料!";
                logger.info(errMsg);
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, errMsg, errMsg);
                FacesContext.getCurrentInstance().addMessage(null, message);
                return;
            }
            HttpSession session = request.getSession();
            Assertion assertion = (Assertion) session.getAttribute(CONST_CAS_ASSERTION);
            AttributePrincipal princpal = assertion.getPrincipal();
            String origialname = princpal.getName();
            Assertion switchAssertion = new AssertionImpl(user.getLoginAccount());
            request.setAttribute(CONST_CAS_ASSERTION, switchAssertion);
            session.setAttribute(CONST_CAS_ASSERTION, switchAssertion);
            session.setAttribute(CONST_CAS_GROUPS, null);
            logger.info("switch user origial user=" + origialname + ", new user=" + user.getLoginAccount());
            //
            userSession.clear();
            String msg = "變更成功!";
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
            FacesContext.getCurrentInstance().addMessage(null, message);

            FacesContext context = FacesContext.getCurrentInstance();
//            context.getExternalContext().getFlash().setKeepMessages(true);   
            context.getExternalContext().redirect("switchUser.xhtml");
            context.responseComplete();
//            return "switchUser.xhtml?faces-redirect=true";
        } else {
            String msg = "您沒有權限變更登入者!";
            logger.info(msg);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }
    }
//get,set method

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public SessionController getUserSession() {
        return userSession;
    }

    public void setUserSession(SessionController userSession) {
        this.userSession = userSession;
    }
}
