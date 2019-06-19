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
    private String name;
    private String encryptedPwd;
    private String telKeyword;
    private String emailKeyword;
    private Boolean reply;
    
    private Boolean hasPic;
    
    private Long dealerId;
    private Long dsId;// downstream Id
    
    // 身分別查詢條件
    private Boolean adminUser;
    private Boolean sellerApply;
    private Boolean sellerApprove;
    private Boolean tccDealer;
    private Boolean tccDs;
    private Boolean identityUnion;// 身分別查詢條件 AND or OR
    
    // for 商店多人管理
    private Boolean storeManager;// 商店管理員
    private Long manageStoreId;
    
    public Boolean getReply() {
        return reply;
    }

    public void setReply(Boolean reply) {
        this.reply = reply;
    }

    public Boolean getAdminUser() {
        return adminUser;
    }

    public void setAdminUser(Boolean adminUser) {
        this.adminUser = adminUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getManageStoreId() {
        return manageStoreId;
    }

    public void setManageStoreId(Long manageStoreId) {
        this.manageStoreId = manageStoreId;
    }

    public Boolean getStoreManager() {
        return storeManager;
    }

    public void setStoreManager(Boolean storeManager) {
        this.storeManager = storeManager;
    }

    public Boolean getHasPic() {
        return hasPic;
    }

    public void setHasPic(Boolean hasPic) {
        this.hasPic = hasPic;
    }

    public Boolean getIdentityUnion() {
        return identityUnion;
    }

    public void setIdentityUnion(Boolean identityUnion) {
        this.identityUnion = identityUnion;
    }

    public Boolean getSellerApply() {
        return sellerApply;
    }

    public void setSellerApply(Boolean sellerApply) {
        this.sellerApply = sellerApply;
    }

    public Boolean getSellerApprove() {
        return sellerApprove;
    }

    public void setSellerApprove(Boolean sellerApprove) {
        this.sellerApprove = sellerApprove;
    }

    public Long getDsId() {
        return dsId;
    }

    public void setDsId(Long dsId) {
        this.dsId = dsId;
    }

    public Long getDealerId() {
        return dealerId;
    }

    public void setDealerId(Long dealerId) {
        this.dealerId = dealerId;
    }

    public Boolean getTccDealer() {
        return tccDealer;
    }

    public void setTccDealer(Boolean tccDealer) {
        this.tccDealer = tccDealer;
    }

    public Boolean getTccDs() {
        return tccDs;
    }

    public void setTccDs(Boolean tccDs) {
        this.tccDs = tccDs;
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

    public String getEncryptedPwd() {
        return encryptedPwd;
    }

    public void setEncryptedPwd(String encryptedPwd) {
        this.encryptedPwd = encryptedPwd;
    }
    
    
}
