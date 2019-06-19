/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.model.criteria;

/**
 *
 * @author Peter.pan
 */
public class MemberCriteriaVO extends BaseCriteriaVO {
    private String loginAccount;
    private String encryptedPwd;
    private Boolean active;

    private String telKeyword;
    private String emailKeyword;
    
    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }

    public String getTelKeyword() {
        return telKeyword;
    }

    public void setTelKeyword(String telKeyword) {
        this.telKeyword = telKeyword;
    }

    public String getEmailKeyword() {
        return emailKeyword;
    }

    public void setEmailKeyword(String emailKeyword) {
        this.emailKeyword = emailKeyword;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getEncryptedPwd() {
        return encryptedPwd;
    }

    public void setEncryptedPwd(String encryptedPwd) {
        this.encryptedPwd = encryptedPwd;
    }
    
    
}
