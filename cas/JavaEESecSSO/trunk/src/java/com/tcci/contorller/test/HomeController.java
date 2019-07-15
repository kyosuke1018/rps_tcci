/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.contorller.test;

import com.tcci.security.AuthFacade;
import com.tcci.security.SecurityConstants;
import java.io.Serializable;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.security.enterprise.CallerPrincipal;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import static org.jasig.cas.client.util.AbstractCasFilter.CONST_CAS_ASSERTION;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.AssertionImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author peter.pan
 */
@ManagedBean(name = "home")
@ViewScoped
public class HomeController implements Serializable {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Inject
    private AuthFacade authFacade;
    
    @ManagedProperty(value="#{sessionController}")
    protected TcSessionController sessionController;
    public TcSessionController getSessionController() {
        return sessionController;
    }
    public void setSessionController(TcSessionController sessionController) {
        this.sessionController = sessionController;
    }
       
    // test PRG Pattern : POST-REDIRECT-GET
    private Long id1;
    private String val1 = "test post data 1";
    private String simulateUser = "";
    
    @PostConstruct
    private void init() {
        logger.debug("init ...");
    }

    public String save(){
        logger.debug("save val1 = " +val1);
        // todo save ...
        id1 = System.currentTimeMillis();
        return "result?faces-redirect=true&includeViewParams=true";
    }

    public void load(){
        // todo load ...
        logger.debug("load id1 = "+id1+", val1 = " +val1);
    }
    
    public String doSimulateUser(){
        logger.debug("doSimulateUser simulateUser = "+simulateUser);
        // 參考 TcAuthenticationMechanism 的 keepCallerInfoInSession()
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session = request.getSession();
        Assertion assertion = new AssertionImpl(simulateUser);
        session.setAttribute(CONST_CAS_ASSERTION, assertion);
        
        // add for TcAuthenticationMechanism
        Set<String> groups = authFacade.getCallerGroups(simulateUser);
        // TODO check groups is empty
        session.setAttribute(SecurityConstants.CALLER_ATTR, new CallerPrincipal(simulateUser));// CallerPrincipal
        session.setAttribute(SecurityConstants.GROUPS_ATTR, groups);// Set<String>
        session.setAttribute(SecurityConstants.CALLER_ORI_ATTR, sessionController.getLoginAccount());// CallerPrincipal
        
        sessionController.init(simulateUser);
        
        return "home?faces-redirect=true&includeViewParams=true";
    }
    
    public String getUser() {
        return sessionController.getLoginAccount();
    }

    public boolean isHasAdminRole() {
        return sessionController.isUserInRole("ROLE_ADMIN");
    }

    public boolean isHasUserRole() {
        return sessionController.isUserInRole("ROLE_USER");
    } 

    public String getSimulateUser() {
        return simulateUser;
    }

    public void setSimulateUser(String simulateUser) {
        this.simulateUser = simulateUser;
    }

    public Long getId1() {
        return id1;
    }

    public void setId1(Long id1) {
        this.id1 = id1;
    }

    public String getVal1() {
        return val1;
    }

    public void setVal1(String val1) {
        this.val1 = val1;
    }
}
