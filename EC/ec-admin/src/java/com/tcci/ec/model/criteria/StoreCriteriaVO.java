/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.model.criteria;

import java.util.List;

/**
 *
 * @author Peter.pan
 */
public class StoreCriteriaVO extends BaseCriteriaVO {
    private String telKeyword;
    private String emailKeyword;
    private String addrKeyword;
    private String prdKeyword;
    private Long areaId;
    private List<Long> areaList;
    
    private Boolean tccDealer;// 此商店是否為台泥經銷商建立
    
    private Boolean storeManager;// 可管理商店(非原賣家)
    private Long managerId;// 商店管理員會員ID

    private String idCode;
    
    public String getPrdKeyword() {
        return prdKeyword;
    }

    public void setPrdKeyword(String prdKeyword) {
        this.prdKeyword = prdKeyword;
    }    

    public String getIdCode() {
        return idCode;
    }

    public void setIdCode(String idCode) {
        this.idCode = idCode;
    }

    public Boolean getTccDealer() {
        return tccDealer;
    }

    public void setTccDealer(Boolean tccDealer) {
        this.tccDealer = tccDealer;
    }

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }

    public Boolean getStoreManager() {
        return storeManager;
    }

    public void setStoreManager(Boolean storeManager) {
        this.storeManager = storeManager;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public List<Long> getAreaList() {
        return areaList;
    }

    public void setAreaList(List<Long> areaList) {
        this.areaList = areaList;
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
