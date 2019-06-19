/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.model.admin;

import com.tcci.cm.model.interfaces.IPresentationVO;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Peter
 */
public class UsersVO implements IPresentationVO, Serializable {
    private long id;
    private String login_account;
    private String cname;
    private String email;
    private String emp_id;
    private boolean disabled;
    private String creator;
    private Date createTimestamp;
    
    private String groups;//user所屬系統角色
    private String userOrg;//user所屬組織名稱
    private long userOrgId;//user所屬組織id
    private String orgs;//user可存取組織名稱
    private String orgIds;//user可存取組織id

    //<editor-fold defaultstate="collapsed" desc="getter and setter">
    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public String getLogin_account() {
        return login_account;
    }

    public void setLogin_account(String login_account) {
        this.login_account = login_account;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreateTimestamp() {
        return createTimestamp;
    }

    public void setCreateTimestamp(Date createTimestamp) {
        this.createTimestamp = createTimestamp;
    }

    public String getGroups() {
        return groups;
    }

    public void setGroups(String groups) {
        this.groups = groups;
    }
    
    public String getOrgs() {
        return orgs;
    }

    public void setOrgs(String orgs) {
        this.orgs = orgs;
    }

    public String getOrgIds() {
        return orgIds;
    }

    public void setOrgIds(String orgIds) {
        this.orgIds = orgIds;
    }
    
    public String getUserOrg() {
        return userOrg;
    }

    public void setUserOrg(String userOrg) {
        this.userOrg = userOrg;
    }

    public long getUserOrgId() {
        return userOrgId;
    }

    public void setUserOrgId(long userOrgId) {
        this.userOrgId = userOrgId;
    }
    
    //</editor-fold>

    @Override
    public boolean equals(Object other){
        if( other!=null ){
            if (!(other instanceof UsersVO)) {
                return false;
            }
            
            return (this.getId() == ((UsersVO)other).getId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }
}
