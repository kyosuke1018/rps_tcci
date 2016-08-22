package com.tcci.sksp.controller.util;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.org.TcUserFacade;
import com.tcci.sksp.entity.enums.AccessLogActionEnum;
import com.tcci.sksp.facade.SkAccessLogFacade;
import java.io.Serializable;
import java.security.Principal;
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
 * @author nEO.Fu
 */
@ManagedBean
@SessionScoped
public class SessionController implements Serializable {

    protected final static Logger logger = LoggerFactory.getLogger(SessionController.class);
    @Resource(mappedName = "jndi/global.config")
    private Properties jndiGlobalConfig;
    // login user data
    private String loginAccount;
    private TcUser user;
    private String errMsg;
    
    @EJB
    private TcUserFacade userFacade;

    @EJB
    private SkAccessLogFacade accessLogFacade;
    
    @PostConstruct
    private void init() {
        initUser();
        accessLogFacade.createLog( user, AccessLogActionEnum.LOGIN );
    }

    public void clear() {
        //System.out.println("UserSession.switchUser");
        logger.debug("switchUser() invoked.");
        loginAccount = null;
        user = null;
    }

    public boolean getValid() {
        boolean valid = (user != null);
        return (valid);
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getAdaccount() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        Principal p = request.getUserPrincipal();
        logger.debug("p={}", p);
        if (p != null) {
            loginAccount = p.getName();
        }
        return loginAccount;
    }

    private void initUser() {
        getAdaccount();
        try {
            logger.debug("initEmployee() loginAccount = {}", loginAccount);
            user = userFacade.findUserByLoginAccount(loginAccount);
            if (user == null) {
                errMsg = "查無員工資料(TcUser), 請聯絡系統管理員!";
                logger.error("TcUser not found, loginAccount = {}", loginAccount);
                return;
            }
            user = userFacade.findUserByLoginAccount(loginAccount);
            if (user == null) {
                errMsg = "查無員工資料(TcUser), 請聯絡系統管理員!";
                logger.error("TcUser not found, loginAccount = {}", loginAccount);
                return;
            }

        } catch (Exception ex) {
            errMsg = "系統忙碌中, 請稍後再試!";
            logger.error("initEmployee() exception", ex);
        }
    }

    public TcUser getUser() {
        if (user == null) {
            initUser();
        }
        return user;
    }

    public String getEmployeeCode() {
        return (getUser() == null) ? "" : user.getEmpId();
    }

    public String getEmployeeCodeName() {
        return (getUser() == null) ? loginAccount : user.getEmpId() + " - " + user.getCname();
    }

    public String getEmployeeName() {
        return (getUser() == null) ? "" : user.getCname();
    }

    public String logout() {
        try {
            clear();
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            request.logout();
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            session.invalidate();
            String url = System.getProperty("com.taiwancement.sso.logoutUrl");
            if (null == url) {
                url = jndiGlobalConfig.getProperty("CAS_LOGOUT_URL");
            }
            context.getExternalContext().redirect(url);
            context.responseComplete();
        } catch (Exception e) {
            logger.error("logout() exception.", e);
            FacesMessage fMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, fMsg);
        }
        return "?faces-redirect=true";
    }

    
    public String getHostURL() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    }
}
