package com.tcci.fc.controller.dialog.pickuser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import javax.faces.bean.ViewScoped;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ManagedBean(name = "queryEmpDialog")
@ViewScoped
public class QueryEmployeeDialogController implements Serializable {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @ManagedProperty(value = "#{orgUIComponentFactory}")
    private OrgUIComponentFactory orgUIComponentFactory;

    private String queryString = "";
    private String filterGroup = "";
    private List<GroupUIComponent> groups = new ArrayList<GroupUIComponent>();
    private List<UserUIComponent> users = new ArrayList<UserUIComponent>();
    
    private List<UserUIComponent> qualifiedUsers = new ArrayList<UserUIComponent>();
    private UserUIComponent selectedUser;
    private UserUIComponent[] selectedUsers;
    
    private List<UserUIComponent> assignedUsers = new ArrayList<UserUIComponent>();
    private UserUIComponent[] selectedAssignedUsers;

    @PostConstruct
    private void init() {
        loadOrg();

        qualifiedUsers.addAll(users);
    }

    public String getFilterGroup() {
        return filterGroup;
    }

    public void setFilterGroup(String filterGroup) {
        this.filterGroup = filterGroup;
    }
    
    public List<GroupUIComponent> getGroups() {
        return groups;
    }

    public void setGroups(List<GroupUIComponent> groups) {
        this.groups = groups;
    }

    public List<UserUIComponent> getQualifiedUsers() {
        return qualifiedUsers;
    }

    public void setQualifiedUsers(List<UserUIComponent> qualifiedUsers) {
        this.qualifiedUsers = qualifiedUsers;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public UserUIComponent getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(UserUIComponent selectedUser) {
        this.selectedUser = selectedUser;
    }

    public List<UserUIComponent> getUsers() {
        return users;
    }

    public void setUsers(List<UserUIComponent> users) {
        this.users = users;
    }

    public List<UserUIComponent> getAssignedUsers() {
        return assignedUsers;
    }

    public void setAssignedUsers(List<UserUIComponent> assignedUsers) {
        this.assignedUsers = assignedUsers;
    }

    public UserUIComponent[] getSelectedAssignedUsers() {
        return selectedAssignedUsers;
    }

    public void setSelectedAssignedUsers(UserUIComponent[] selectedAssignedUsers) {
        this.selectedAssignedUsers = selectedAssignedUsers;
    }

    public UserUIComponent[] getSelectedUsers() {
        return selectedUsers;
    }

    public void setSelectedUsers(UserUIComponent[] selectedSrcUsers) {
        this.selectedUsers = selectedSrcUsers;
    }

    public void loadOrg() {
        this.groups = orgUIComponentFactory.getGroupList();
        this.users = orgUIComponentFactory.getUserList();
    }

    public void setOrgUIComponentFactory(OrgUIComponentFactory orgUIComponentFactory) {
        this.orgUIComponentFactory = orgUIComponentFactory;
    }

    public void query() {
        List<UserUIComponent> result = new ArrayList<UserUIComponent>();

        for (UserUIComponent u : users) {
            boolean isMatch = (u.getName().startsWith(queryString) || u.getEmployeeNumber().startsWith(queryString))
                        && (filterGroup==null || filterGroup.isEmpty() || u.getDepartment().isMatch(filterGroup));


            if (isMatch) {
                result.add(u);
            }
        }

        qualifiedUsers.clear();
        qualifiedUsers.addAll(result);
    }

    /**
     * Ajax function for return data to dialog
     */
    public void doQuery() throws IllegalArgumentException{
        String empCode = this.selectedUser.getEmployeeNumber();

        RequestContext context = RequestContext.getCurrentInstance();
        context.addCallbackParam("empCode", empCode);

        //reset selected user
        this.selectedUser = null;
        this.queryString = "";
        this.filterGroup = "";        
    }
}
