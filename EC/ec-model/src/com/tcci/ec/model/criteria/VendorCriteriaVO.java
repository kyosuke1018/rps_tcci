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
public class VendorCriteriaVO extends BaseCriteriaVO {
    private String telKeyword;
    private String emailKeyword;
    private String addrKeyword;
    private String prdKeyword;

    public String getPrdKeyword() {
        return prdKeyword;
    }

    public void setPrdKeyword(String prdKeyword) {
        this.prdKeyword = prdKeyword;
    }    

    public String getAddrKeyword() {
        return addrKeyword;
    }

    public void setAddrKeyword(String addrKeyword) {
        this.addrKeyword = addrKeyword;
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
}
