package com.tcci.fc.controller;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.org.TcUserFacade;
import java.security.Principal;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Lynn.Huang
 */
@ManagedBean
@SessionScoped
public class SessionController {
    private TcUser loginUser;
    
    @EJB
    private TcUserFacade tcUserFacade;
    
    @PostConstruct
    public void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        Principal p = request.getUserPrincipal();
        if (p != null){
            loginUser = tcUserFacade.findUserByLoginAccount(p.getName());          
        }
    }    

    public TcUser getLoginUser() {
        return loginUser;
    }
}
