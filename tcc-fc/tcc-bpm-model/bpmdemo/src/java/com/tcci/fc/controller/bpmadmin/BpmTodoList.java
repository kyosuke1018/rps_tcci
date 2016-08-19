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

    private List<TcWorkitem> workitems = new ArrayList<TcWorkitem>();
    private String ownerAccount;
    private String ballot;
    private String route;
    private String comments;
    private String newAccounts; // 改派,加簽,先後會
    private boolean addString = true; // true:串簽, false:並簽 (for 先後會)
    private String filter;

    @ManagedProperty(value = "#{userSession}")
    private UserSession userSession;

    @Inject
    private IBPMEngine bpmEngine;
    @EJB
    private TcUserFacade userFacade;

    private boolean allowAgent = true;
    private List<TcWorkitem> _workitems;

    @PostConstruct
    private void init() {
        query();
    }
    
    // actions
    public void query() {
        String account = StringUtils.trimToNull(ownerAccount);
        try {
            if (null == account) {
                _workitems = bpmEngine.allRunningWorkitems();
            } else {
                _workitems = bpmEngine.myRunningWorkitems(findUser(ownerAccount));
            }
            filterChange();
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex, "query exception!");
        }
    }
    
    public void filterChange() {
        workitems.clear();
        if (_workitems != null) {
            for (TcWorkitem w : _workitems) {
                if (filter(w)) {
                    workitems.add(w);
                }
            }
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
    
    public void addApprovers(TcWorkitem workitem) {
        try {
            List<TcUser> newUsers = findUsers(newAccounts);
            bpmEngine.addActivityWorkitems(workitem.getActivityid(), newUsers);
            JsfUtil.addSuccessMessage("加簽成功!");
            query();
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex, "加簽失敗!");
        }
    }

    public void addActivitiesBefore(TcWorkitem workitem) {
        try {
            List<TcUser> newUsers = findUsers(newAccounts);
            bpmEngine.addActivitiesBefore(workitem, newUsers, addString);
            JsfUtil.addSuccessMessage("先會成功!");
            query();
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex, "先會失敗!");
        }
    }

    public void addActivitiesAfter(TcWorkitem workitem) {
        try {
            List<TcUser> newUsers = findUsers(newAccounts);
            bpmEngine.addActivitiesAfter(workitem, newUsers, addString);
            JsfUtil.addSuccessMessage("後會成功!");
            query();
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
    
    // by 表單ID,表單class,簽核項目,簽核人
    private boolean filter(TcWorkitem wo) {
        String tmp = StringUtils.trimToNull(filter);
        if (null == tmp) {
            return true;
        }
        String formId = wo.getActivityid().getProcessid().getPrimaryobjectid().toString();
        String formClass = wo.getActivityid().getProcessid().getPrimaryobjectclassname();
        if (StringUtils.containsIgnoreCase(formId, tmp) ||
                StringUtils.containsIgnoreCase(formClass, tmp) ||
                StringUtils.containsIgnoreCase(wo.getActivityname(), tmp) ||
                StringUtils.containsIgnoreCase(wo.getOwner().getDisplayIdentifier(), tmp)) {
            return true;
        }
        return false;
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

    public boolean isAddString() {
        return addString;
    }

    public void setAddString(boolean addString) {
        this.addString = addString;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public UserSession getUserSession() {
        return userSession;
    }

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }

}
