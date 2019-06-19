/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.model.rs;

import java.util.List;

/**
 *
 * @author Peter.pan
 */
public class AuthVO extends BaseResponseVO {
    private String loginAccount;
    private String name;
    private String token;
    private Long memberId;
    private Long sellerId;
    private Long storeId;
    private Boolean adminUser;
    private Boolean login;
    private List<LongOptionVO> stores;

    private Boolean tccDealer;// 台泥經銷商
    private Boolean tccDs;// 台泥經銷商下游客戶

    // 一商店多人管理
    private boolean manager;// 目前是否為商店管理員身分(對應 storeId)
    private Boolean storeOwner;// 商店最高權限人
    private Boolean fiUser;// 財務人員=可執行訂單結案人
    
    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Boolean getStoreOwner() {
        return storeOwner;
    }

    public void setStoreOwner(Boolean storeOwner) {
        this.storeOwner = storeOwner;
    }

    public Boolean getFiUser() {
        return fiUser;
    }

    public void setFiUser(Boolean fiUser) {
        this.fiUser = fiUser;
    }

    public boolean isManager() {
        return manager;
    }

    public void setManager(boolean manager) {
        this.manager = manager;
    }

    public Boolean getTccDs() {
        return tccDs;
    }

    public void setTccDs(Boolean tccDs) {
        this.tccDs = tccDs;
    }

    public Boolean getTccDealer() {
        return tccDealer;
    }

    public void setTccDealer(Boolean tccDealer) {
        this.tccDealer = tccDealer;
    }

    public List<LongOptionVO> getStores() {
        return stores;
    }

    public void setStores(List<LongOptionVO> stores) {
        this.stores = stores;
    }

    public Boolean getAdminUser() {
        return adminUser;
    }

    public void setAdminUser(Boolean adminUser) {
        this.adminUser = adminUser;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getLogin() {
        return login;
    }

    public void setLogin(Boolean login) {
        this.login = login;
    }
    
    
}
