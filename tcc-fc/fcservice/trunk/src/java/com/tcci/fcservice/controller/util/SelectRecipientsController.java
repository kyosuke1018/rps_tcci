/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fcservice.controller.util;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.org.TcUserFacade;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Jimmy.Lee
 */
@ManagedBean(name = "selectRecipientsController")
@ViewScoped
public class SelectRecipientsController {
    private String userNames;
    private List<TcUser> leftUsers;
    private List<TcUser> rightUsers;

    @EJB
    private TcUserFacade userFacade;
    
    private List<TcUser> allUsers;
    TcUserComparator comparator = new TcUserComparator();
    
    @PostConstruct
    private void init() {
        allUsers = userFacade.findUsersByGroupCode("MANAGERS");
    }
    
    public void init(List<TcUser> left, List<TcUser> right) {
        leftUsers = left;
        rightUsers = right;
        if (leftUsers == null) {
            leftUsers = new ArrayList<TcUser>();
        }
        if (rightUsers == null) {
            rightUsers = new ArrayList<TcUser>();
        }
        sortUsers();
    }
    
    // actions
    // 加入收件者
    public void addLeftUser(TcUser tcuser) {
        if (null == tcuser) {
            rightUsers.removeAll(leftUsers);
            rightUsers.addAll(leftUsers);
        } else {
            rightUsers.add(tcuser);
        }
        sortUsers();
    }
    
    // 移除目前收件者
    public void removeRightUser(TcUser tcuser) {
        if (null == tcuser) {
            rightUsers.clear();
        } else {
            rightUsers.remove(tcuser);
        }
    }
    
    public void lookupUserNames() {
        leftUsers.clear();
        if (StringUtils.isBlank(userNames))
            return;
        String[] users = userNames.split(",|;|\\s|\\(|\\)");
        HashSet<TcUser> userSet = new HashSet<TcUser>();
        for (String user : users) {
            List<TcUser> userList = lookupUser(user);
            userSet.addAll(userList);
        }
        leftUsers.addAll(userSet);
        sortUsers();
    }
    
    public void addGroup(String group) {
        if (group.length()==0) {
            leftUsers.clear();
            leftUsers.addAll(allUsers);
        } else {
            leftUsers = userFacade.findUsersByGroupCode(group);
        }
        Collections.sort(leftUsers, comparator);
    }
    
    public boolean isInRightUsers(TcUser user) {
        if (user==null)
            return false;
        return rightUsers.contains(user);
    }
    
    // helper    
    // lookup name, adaccount
    private List<TcUser> lookupUser(String user) {
        ArrayList<TcUser> result = new ArrayList<TcUser>();
        if (StringUtils.isBlank(user))
            return result;
        user = user.trim();
        for (TcUser tcUser : allUsers) {
            if (StringUtils.containsIgnoreCase(tcUser.getLoginAccount(), user) ||
                StringUtils.containsIgnoreCase(tcUser.getCname(), user)) {
                result.add(tcUser);
            }
        }
        return result;
    }
    
    private void sortUsers() {
        Collections.sort(leftUsers, comparator);
        Collections.sort(rightUsers, comparator);
    }

    // getter, setter
    public String getUserNames() {
        return userNames;
    }

    public void setUserNames(String userNames) {
        this.userNames = userNames;
    }

    public List<TcUser> getLeftUsers() {
        return leftUsers;
    }

    public void setLeftUsers(List<TcUser> leftUsers) {
        Collections.sort(leftUsers, comparator);
        this.leftUsers = leftUsers;
    }

    public List<TcUser> getRightUsers() {
        return rightUsers;
    }

    public void setRightUsers(List<TcUser> rightUsers) {
        Collections.sort(rightUsers, comparator);
        this.rightUsers = rightUsers;
    }
    
    static class TcUserComparator implements Comparator<TcUser> {
        @Override
        public int compare(TcUser u1, TcUser u2) {
            return u1.getLoginAccount().compareToIgnoreCase(u2.getLoginAccount());
        }
    }
}
