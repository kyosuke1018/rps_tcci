/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.facade.usermgmt;

/**
 *
 * @author Jimmy.Lee
 */
public class QueryFilter {

    private String loginAccount;
    private String empId;
    private String email;
    private Long groupId;
    private String cname;
    private Boolean disabled;

    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

}
