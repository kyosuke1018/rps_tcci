/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.cm.model.admin;

import java.util.List;

/**
 * WebCS 匯入資訊
 * @author Peter
 */
public class WebCSEmpVO {
    long id;
    String adaccount;
    String name;
    String code;
    String email;
    String companyName;
    
    String groups;
    List<String> groupList;

    boolean existed;
    
    //<editor-fold defaultstate="collapsed" desc="getter and setter">
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAdaccount() {
        return adaccount;
    }

    public void setAdaccount(String adaccount) {
        this.adaccount = adaccount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGroups() {
        return groups;
    }

    public void setGroups(String groups) {
        this.groups = groups;
    }

    public List<String> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<String> groupList) {
        this.groupList = groupList;
    }

    public boolean isExisted() {
        return existed;
    }

    public void setExisted(boolean existed) {
        this.existed = existed;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    //</editor-fold>
}
