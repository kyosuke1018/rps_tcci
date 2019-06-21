/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.controller.admin;

import com.tcci.cm.controller.global.SessionAwareController;
import com.tcci.cm.facade.admin.UserFacade;
import com.tcci.cm.model.admin.UsersCriteriaVO;
import com.tcci.cm.model.admin.UsersVO;
import com.tcci.cm.util.JsfUtils;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.security.AuthFacade;
import com.tcci.security.SecurityConstants;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.security.enterprise.CallerPrincipal;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.AssertionImpl;

/**
 *
 * @author Peter.Pan
 */
@ManagedBean(name = "su")
@ViewScoped
public class SuperUserController extends SessionAwareController implements Serializable {
    public static final long FUNC_OPTION = 11;
    @EJB private UserFacade userFacade;
    @Inject private AuthFacade authFacade;
    
    private final String CONST_CAS_GROUPS = "_const_cas_groups_";
    private final String CONST_CAS_ASSERTION = "_const_cas_assertion_";
    private String userID;

    private List<UsersVO> allUserList; // 可選取全部User 
    private String selectedUserTxt; // AutoCompleted 選取人員
    private Long selectedUser; // AutoCompleted 選取人員
    private String simulateUser;

    public SuperUserController(){}

    @PostConstruct
    public void init() {
        // SessionAwareController.checkAuthorizedByViewId 檢核未通過
        if( functionDenied ){ return; }
        // Get view Id
        viewId = JsfUtils.getViewId();

        // 可選取發訊人的人員列表
        UsersCriteriaVO queryCriteria = new UsersCriteriaVO();        
        allUserList = userFacade.findUsersByCriteria(queryCriteria);        
    }
    
    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    /**
     * 確定切換人員
     * @return 
     */
    public String selectUser() {
        if( !hasSelectUser() ){ // get user by autoCompleted input
            JsfUtils.addErrorMessage("未選取人員。");
            return "su.xhtml";
        }
        simulateUser = getUserID();
        return doSimulateUser();
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
        
        if( groups==null || groups.isEmpty() ){
            logger.error("doSimulateUser error groups==null");
            JsfUtils.addWarningMessage("請確定 "+simulateUser+" 是否擁有此系統權限!");
            return "su.xhtml";
        }else{
            groups.add(SecurityConstants.DEF_VALID_GROUP);
        }
        session.setAttribute(SecurityConstants.CALLER_ATTR, new CallerPrincipal(simulateUser));// CallerPrincipal
        session.setAttribute(SecurityConstants.GROUPS_ATTR, groups);// Set<String>
        session.setAttribute(SecurityConstants.CALLER_ORI_ATTR, sessionController.getLoginAccount());// CallerPrincipal
        
        //sessionController.init(simulateUser);
        //return "home?faces-redirect=true&includeViewParams=true";
        try{
            if( !sessionController.initUserSession(getUserID()) ){
                logger.error("doSimulateUser error initUserSession fail");
                JsfUtils.addWarningMessage("請確定 "+simulateUser+" 是否擁有此系統權限!!");
                return "su.xhtml";
            }
            sessionController.setSimulated(true);
        }catch(Exception e){
            logger.error("selectUser Exception :\n", e);
            JsfUtils.addWarningMessage("selectUser Exception :\n "+e.getMessage());
            return "su.xhtml";
        }
        
        return "/index.xhtml?faces-redirect=true&includeViewParams=true";
        
        /*
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session = request.getSession();
        Assertion assertion = (Assertion) session.getAttribute(CONST_CAS_ASSERTION);
        
        Assertion switchAssertion = new AssertionImpl(getUserID());
        request.setAttribute(CONST_CAS_ASSERTION, switchAssertion);
        session.setAttribute(CONST_CAS_GROUPS, null);
        
        try{
            if( !sessionController.initUserSession(getUserID()) ){
                JsfUtils.addWarningMessage("請確定 "+getUserID()+" 是否擁有此系統權限!");
                return "su.xhtml";
            }
            sessionController.setSimulated(true);
        }catch(Exception e){
            logger.error("selectUser Exception :\n", e);
            JsfUtils.addWarningMessage("selectUser Exception :\n "+e.getMessage());
            return "su.xhtml";
        }
        
        return "/index.xhtml?faces-redirect=true";
        */
    }

    /**
     * 選取收件人 autoComplete 使用者列表
     * @param intxt
     * @return 
     */
    public List<String> autoCompleteUserOptions(String intxt){
        List<String> resList = new ArrayList<String>();
        
        for(UsersVO user : allUserList){// 有權選取的所有User
            String txt = user.getCname() + "(" + user.getLogin_account() + ")";
            if( txt.toUpperCase().indexOf(intxt.toUpperCase()) >= 0 ){// 符合輸入
                resList.add(txt);
            }
        }
        
        return resList;
    }
    
    /**
     * 選取發訊人查詢條件
     * @return 
     */
    public boolean hasSelectUser(){
        // get user name by selectedUserTxt
        TcUser user = userFacade.findUserByTxt(allUserList, selectedUserTxt);
        if( user!=null ){
            this.userID = user.getLoginAccount();
            return true;
        }
        return false;
    }
    
    /**
     * 功能標題
     * @return 
     */
    @Override
    public String getFuncTitle(){
        return sessionController.getFunctionTitle(FUNC_OPTION);
    }  
    
    //<editor-fold defaultstate="collapsed" desc="getter and setter">
    public UserFacade getUserFacade() {
        return userFacade;
    }

    public void setUserFacade(UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    public List<UsersVO> getAllUserList() {
        return allUserList;
    }

    public void setAllUserList(List<UsersVO> allUserList) {
        this.allUserList = allUserList;
    }

    public String getSelectedUserTxt() {
        return selectedUserTxt;
    }

    public void setSelectedUserTxt(String selectedUserTxt) {
        this.selectedUserTxt = selectedUserTxt;
    }

    public Long getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(Long selectedUser) {
        this.selectedUser = selectedUser;
    }
    //</editor-fold>

}
