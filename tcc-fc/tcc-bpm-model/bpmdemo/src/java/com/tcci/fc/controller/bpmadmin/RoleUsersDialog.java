/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.controller.bpmadmin;

import com.tcci.fc.controller.util.JsfUtil;
import com.tcci.fc.entity.org.TcGroup;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.org.TcGroupFacade;
import com.tcci.fc.facade.org.TcUserFacade;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apache.commons.lang.StringUtils;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Jimmy.Lee
 */
@ManagedBean(name = "roleUsersDialog")
@ViewScoped
public class RoleUsersDialog {
    
    private String rolename;
    private int option;
    private String principals;

    @EJB
    private TcUserFacade userFacade;
    @EJB
    private TcGroupFacade groupFacade;
    
    private RoleUsersDialogListener listener;
    
    public void init(RoleUsersDialogListener listener, String rolename, Object users) {
        this.listener = listener;
        option = 0;
        principals = null;
        this.rolename = rolename;
        if (users instanceof TcUser) {
            principals = ((TcUser) users).getLoginAccount();
        } else if (users instanceof TcGroup) {
            option = 1;
            principals = ((TcGroup) users).getCode();
        } else if (users instanceof Collection<?>) {
            option = 2;
            Collection<TcUser> userList = (Collection<TcUser>) users;
            StringBuilder sb = new StringBuilder();
            boolean first = true;
            for (TcUser user : userList) {
                if (first) {
                    first = false;
                } else {
                    sb.append(',');
                }
                sb.append(user.getLoginAccount());
            }
            principals = sb.toString();
        }
    }
    
    public void dialog_OK() {
        RequestContext rc = RequestContext.getCurrentInstance();
        try {
            Object result = null;
            if (0==option) { // TcUser
                result = findUser(principals);
            } else if (1==option) { // TcGroup
                result = findGroup(principals);
            } else if (2==option) { // List<TcUsr>
                result = findUsers(principals);
            }
            rc.addCallbackParam("error", "false");
            listener.roleUsersDialog_OK(result);
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex, "findUser/findGroup exception!");
            rc.addCallbackParam("error", "true");
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

    private TcGroup findGroup(String group) throws Exception {
        String str = StringUtils.trimToEmpty(group);
        if (str.isEmpty()) {
            throw new Exception("群組是空的!");
        }
        TcGroup tcGroup = groupFacade.findGroupByCode(str);
        if (null == tcGroup) {
            throw new Exception("群組:" + group + "不存在!");
        }
        return tcGroup;
    }

    // getter, setter
    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }

    public int getOption() {
        return option;
    }

    public void setOption(int option) {
        this.option = option;
    }

    public String getPrincipals() {
        return principals;
    }

    public void setPrincipals(String principals) {
        this.principals = principals;
    }

}
