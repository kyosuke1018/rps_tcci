/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.controller.bpmadmin;

import com.tcci.fc.controller.login.UserSession;
import com.tcci.fc.controller.util.JsfUtil;
import com.tcci.fc.entity.bpm.TcWorkitem;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.bpm.IBPMEngine;
import com.tcci.fc.facade.org.TcUserFacade;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Jimmy.Lee
 */
@ManagedBean(name = "bpmTodoList")
@ViewScoped
public class BpmTodoList {

    private List<TcWorkitem> workitems;
    private String ownerAccount;
    private String ballot;
    private String route;
    private String comments;
    private String newAccounts; // 改派,先會,後會

    @ManagedProperty(value = "#{userSession}")
    private UserSession userSession;

    @Inject
    private IBPMEngine bpmEngine;
    @EJB
    private TcUserFacade userFacade;

    private boolean allowAgent = true;

    @PostConstruct
    private void init() {
        query();
    }
    
    // actions
    public void query() {
        String account = StringUtils.trimToNull(ownerAccount);
        try {
            if (null == account) {
                workitems = bpmEngine.allRunningWorkitems();
            } else {
                workitems = bpmEngine.myRunningWorkitems(findUser(ownerAccount));
            }
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex, "query exception!");
        }
    }

    public void sign(TcWorkitem workitem) {
        try {
            bpmEngine.completeWorkitem(workitem, ballot, route, comments, userSession.getTcUser(), allowAgent);
            JsfUtil.addSuccessMessage("簽核成功!");
            query();
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex, "簽核失敗!");
        }
    }

    public void reassign(TcWorkitem workitem) {
        try {
            TcUser newOwner = findUser(newAccounts);
            bpmEngine.reassign(workitem, newOwner, comments, userSession.getTcUser(), allowAgent);
            JsfUtil.addSuccessMessage("改派成功!");
            query();
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex, "改派失敗!");
        }
    }

    public void addActivitiesBefore(TcWorkitem workitem) {
        try {
            List<TcUser> newUsers = findUsers(newAccounts);
            bpmEngine.addActivitiesBefore(workitem, newUsers);
            JsfUtil.addSuccessMessage("先會成功!");
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex, "先會失敗!");
        }
    }

    public void addActivitiesAfter(TcWorkitem workitem) {
        try {
            List<TcUser> newUsers = findUsers(newAccounts);
            bpmEngine.addActivitiesAfter(workitem, newUsers);
            JsfUtil.addSuccessMessage("後會成功!");
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex, "後會失敗!");
        }
    }
    
    public void notify(TcWorkitem workitem) {
        try {
            bpmEngine.onWorkitemStartNotification(workitem);
            JsfUtil.addSuccessMessage("已發送通知!");
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex, "通知失敗!");
        }
    }

    // helper
    private TcUser findUser(String account) throws Exception {
        String str = StringUtils.trimToEmpty(account).toLowerCase();
        if (str.isEmpty()) {
            throw new Exception("帳號是空的!");
        }
        TcUser user = userFacade.findUserByLoginAccount(str);
        if (null == user) {
            throw new Exception("帳號:" + account + "不存在!");
        }
        return user;
    }

    private List<TcUser> findUsers(String accounts) throws Exception {
        String[] aryUser = StringUtils.split(accounts, ',');
        if (aryUser == null || aryUser.length == 0) {
            throw new Exception("帳號是空的!");
        }
        List<TcUser> listUser = new ArrayList<TcUser>();
        for (String str : aryUser) {
            listUser.add(findUser(str));
        }
        return listUser;
    }

    // getter, setter
    public List<TcWorkitem> getWorkitems() {
        return workitems;
    }

    public void setWorkitems(List<TcWorkitem> workitems) {
        this.workitems = workitems;
    }

    public String getOwnerAccount() {
        return ownerAccount;
    }

    public void setOwnerAccount(String ownerAccount) {
        this.ownerAccount = ownerAccount;
    }

    public String getBallot() {
        return ballot;
    }

    public void setBallot(String ballot) {
        this.ballot = ballot;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getNewAccounts() {
        return newAccounts;
    }

    public void setNewAccounts(String newAccounts) {
        this.newAccounts = newAccounts;
    }

    public UserSession getUserSession() {
        return userSession;
    }

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }

}
