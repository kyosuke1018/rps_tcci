/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.model.admin;

/**
 *
 * @author Jimmy.Lee
 * 　20130502 Peter.Pan 新增 cname 
 */
public class QueryFilter {
    private long userId;
    private String loginAccount;
    private String empId;
    private String email;
    private Long factoryId;
    private String cname;
    private String company;
    private Long factoryGroupId;
    private String mailUsers;
    private Long usergroupId; 

    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }

    public Long getUsergroupId() {
        return usergroupId;
    }

    public void setUsergroupId(Long usergroupId) {
        this.usergroupId = usergroupId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
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

    public Long getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(Long factoryId) {
        this.factoryId = factoryId;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }    

    public Long getFactoryGroupId() {
        return factoryGroupId;
    }

    public void setFactoryGroupId(Long factoryGroupId) {
        this.factoryGroupId = factoryGroupId;
    }

    public String getMailUsers() {
        return mailUsers;
    }

    public void setMailUsers(String mailUsers) {
        this.mailUsers = mailUsers;
    }

}
