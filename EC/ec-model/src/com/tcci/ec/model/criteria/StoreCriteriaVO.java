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
    protected Boolean opened;

    public String getPrdKeyword() {
        return prdKeyword;
    }

    public void setPrdKeyword(String prdKeyword) {
        this.prdKeyword = prdKeyword;
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

    public Boolean getOpened() {
        return opened;
    }

    public void setOpened(Boolean opened) {
        this.opened = opened;
    }
}
