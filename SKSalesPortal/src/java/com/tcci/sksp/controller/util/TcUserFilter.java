package com.tcci.sksp.controller.util;

/**
 *
 * @author nEO.Fu
 */
public class TcUserFilter {

    private String cname;
    private String empId;
    private String loginAccount;

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }

    public void reset() {
        cname = "";
        empId = "";
        loginAccount = "";
    }
}
