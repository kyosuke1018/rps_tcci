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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.AssertionImpl;

/**
 *
 * @author Peter.Pan
 */
@ManagedBean(name = "switchUserController")
@ViewScoped
public class SwitchUserController extends SessionAwareController implements Serializable {
    public static final long FUNC_OPTION = 6;
    @EJB private UserFacade userFacade;
    
    private final String CONST_CAS_GROUPS = "_const_cas_groups_";
    private final String CONST_CAS_ASSERTION = "_const_cas_assertion_";
    private String userID;

    private List<UsersVO> allUserList; // 可選取全部User 
    private String selectedUserTxt; // AutoCompleted 選取人員
    private Long selectedUser; // AutoCompleted 選取人員

    /** Creates a new instance of SwitchUserManagedBean */
    public SwitchUserController() {
    }

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
    public String switchUser() {
        if( !selectUser() ){ // get user by autoCompleted input
            JsfUtils.addErrorMessage("未選取人員。");
            return "switchUser.xhtml";
        }
        
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session = request.getSession();
        Assertion assertion = (Assertion) session.getAttribute(CONST_CAS_ASSERTION);
        
        Assertion switchAssertion = new AssertionImpl(getUserID());
        request.setAttribute(CONST_CAS_ASSERTION, switchAssertion);
        session.setAttribute(CONST_CAS_ASSERTION, switchAssertion);
        session.setAttribute(CONST_CAS_GROUPS, null);
        
        try{
            if( !sessionController.initUserSession(getUserID()) ){
                JsfUtils.addWarningMessage("請確定 "+getUserID()+" 是否擁有此系統權限!");
                return "switchUser.xhtml";
            }
        }catch(Exception e){
            logger.error("switchUser Exception :\n", e);
            JsfUtils.addWarningMessage("switchUser Exception :\n "+e.getMessage());
            return "switchUser.xhtml";
        }
        
        return "/index.xhtml?faces-redirect=true";
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
    public boolean selectUser(){
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
