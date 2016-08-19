/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.controller.usermgmt;

import com.tcci.fc.controller.login.UserSession;
import com.tcci.fc.entity.org.TcGroup;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.entity.org.TcUsergroup;
import com.tcci.fc.facade.usermgmt.QueryFilter;
import com.tcci.fc.facade.usermgmt.UsermgmtFacade;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apache.commons.lang.StringUtils;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jimmy.Lee
 */
@ManagedBean(name="usermgmtController")
@ViewScoped
public class UsermgmtController {
    private final static Logger logger = LoggerFactory.getLogger(UsermgmtController.class);

    // page data
    private QueryFilter filter;     // 查詢條件
    private List<TcGroup> allGroup; // 所有群組
    private String nameFilter;      // 搜尋使用者 (姓名,工號,帳號,EMAIL)
    private List<TcUser> listUser;  // 過濾後的使用者 (顯示於頁面)

    // 編輯使用者
    private TcUser editUser;
    private List<String> groupSelected; // group.id

    // managed bean
    @ManagedProperty(value = "#{userSession}")
    private UserSession userSession;

    // ejb
    @EJB
    private UsermgmtFacade usermgmtFacade;

    // internal data
    private List<TcUser> queryUsers; // 查詢得到的 user
    private ResourceBundle rb = ResourceBundle.getBundle("msgUsermgmt",
            FacesContext.getCurrentInstance().getViewRoot().getLocale());

    @PostConstruct
    private void init() {
        filter = new QueryFilter();
        listUser = new ArrayList<TcUser>();
        allGroup = usermgmtFacade.findAllGroup();
        Collections.sort(allGroup, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                TcGroup g1 = (TcGroup) o1;
                TcGroup g2 = (TcGroup) o2;
                return g1.getName().compareTo(g2.getName());
            }
        });
        groupSelected = new ArrayList<String>();
    }

    // action
    public void query() {
        queryUsers = usermgmtFacade.findUserByFilter(filter);
        nameFilterChange();
    }

    public void nameFilterChange() {
        listUser.clear();
        if (queryUsers==null || queryUsers.isEmpty())
            return;
        String str = StringUtils.trimToEmpty(nameFilter);
        if (str.isEmpty()) {
            listUser.addAll(queryUsers);
        } else {
            for (TcUser user : queryUsers) {
                if (StringUtils.containsIgnoreCase(user.getCname(), str)
                        || StringUtils.containsIgnoreCase(user.getEmail(), str)
                        || StringUtils.containsIgnoreCase(user.getEmpId(), str)
                        || StringUtils.containsIgnoreCase(user.getLoginAccount(), str)) {
                    listUser.add(user);
                }
            }
        }
    }

    public void editUser(TcUser user) {
        groupSelected.clear();
        if (null == user) {
            editUser = new TcUser();
            editUser.setCreator(userSession.getTcUser());
            editUser.setCreatetimestamp(new Date());
            editUser.setDisabled(false);
        } else {
            editUser = user;
            Collection<TcUsergroup> ugColl = editUser.getTcUsergroupCollection();
            if (ugColl != null && !ugColl.isEmpty()) {
                for (TcUsergroup ug : ugColl) {
                    groupSelected.add(String.valueOf(ug.getGroupId().getId()));
                }
            }
        }
    }

    public void saveUser() {
        RequestContext rc = RequestContext.getCurrentInstance();
        if (editUser.getId() == null) {
            // 新增使用者: 檢查 loginAccount 是否已存在。
            TcUser checkUser = usermgmtFacade.findUserByLoginAccount(editUser.getLoginAccount());
            if (checkUser != null) {
                String error = "LOGIN_ACCOUNT " + editUser.getLoginAccount() + " already existed!";
                rc.addCallbackParam("error", error);
                return;
            }
        }
        rc.addCallbackParam("error", "");
        List<TcGroup> listGroupNew = new ArrayList<TcGroup>();
        for (String str : groupSelected) {
            long gid = Long.valueOf(str).longValue();
            for (TcGroup group : allGroup) {
                if (gid == group.getId().longValue())
                    listGroupNew.add(group);
            }
        }
        usermgmtFacade.save(editUser, listGroupNew, userSession.getTcUser());
        logger.info("user {} saved.", editUser.getLoginAccount());
        query();
    }

    public void disableUser(TcUser user) {
        if (user != null) {
            logger.info("disable user {}", user.getLoginAccount());
            user.setDisabled(true);
            // 保留 usergroup 資料
            // usermgmtFacade.save(user, new ArrayList<TcGroup>(), userSession.getTcUser());
            usermgmtFacade.save(user);
            query();
        }
    }

    // helper
    public String getPageTitle() {
        return rb.getString("usermgmt.title");
    }

    public boolean isAdmin(TcUser user) {
        return (user == null) ? false : user.getLoginAccount().equalsIgnoreCase("administrator");
    }

    // getter, setter
    public QueryFilter getFilter() {
        return filter;
    }

    public void setFilter(QueryFilter filter) {
        this.filter = filter;
    }

    public List<TcGroup> getAllGroup() {
        return allGroup;
    }

    public void setAllGroup(List<TcGroup> allGroup) {
        this.allGroup = allGroup;
    }

    public String getNameFilter() {
        return nameFilter;
    }

    public void setNameFilter(String nameFilter) {
        this.nameFilter = nameFilter;
    }

    public List<TcUser> getListUser() {
        return listUser;
    }

    public void setListUser(List<TcUser> listUser) {
        this.listUser = listUser;
    }

    public UserSession getUserSession() {
        return userSession;
    }

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }

    public TcUser getEditUser() {
        return editUser;
    }

    public void setEditUser(TcUser editUser) {
        this.editUser = editUser;
    }

    public List<String> getGroupSelected() {
        return groupSelected;
    }

    public void setGroupSelected(List<String> groupSelected) {
        this.groupSelected = groupSelected;
    }

}
