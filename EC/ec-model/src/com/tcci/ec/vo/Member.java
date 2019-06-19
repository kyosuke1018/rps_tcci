/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.vo;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author Kyle.Cheng
 */
public class Member {
    private Long id;
    private String loginAccount;
    private String password;
    private String name;
    private String email;
    private String phone;
    private boolean sellerApply;//已申請賣家身份
    private boolean sellerApprove;//已核准賣家身份
    private Date applytime;//申請時間
    private Date approvetime;//核准時間
    private BigDecimal prate;//正評
    private BigDecimal nrate;//負評
    private String type;//帳號類型(P:個人;C:公司)MemberTypeEnum
    private Company company;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isSellerApply() {
        return sellerApply;
    }

    public void setSellerApply(boolean sellerApply) {
        this.sellerApply = sellerApply;
    }

    public boolean isSellerApprove() {
        return sellerApprove;
    }

    public void setSellerApprove(boolean sellerApprove) {
        this.sellerApprove = sellerApprove;
    }

    public Date getApplytime() {
        return applytime;
    }

    public void setApplytime(Date applytime) {
        this.applytime = applytime;
    }

    public Date getApprovetime() {
        return approvetime;
    }

    public void setApprovetime(Date approvetime) {
        this.approvetime = approvetime;
    }

    public BigDecimal getPrate() {
        return prate;
    }

    public void setPrate(BigDecimal prate) {
        this.prate = prate;
    }

    public BigDecimal getNrate() {
        return nrate;
    }

    public void setNrate(BigDecimal nrate) {
        this.nrate = nrate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
