/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.et.model.admin;

import com.tcci.cm.model.global.AbstractCriteriaVO;
import com.tcci.cm.model.interfaces.IQueryCriteria;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Peter
 */
public class PermissionCriteriaVO extends AbstractCriteriaVO implements IQueryCriteria, Serializable {
    private String keyword;
    private String loginAccount;
    
    private boolean selPlant = false;
    private List<String> companyList;
    private Long plant;
    private Long company;
    private String rptType;
    private String sapClientCode;

    private boolean includeTCCI;
    private boolean allowOnly;
    private boolean plantUser;
    
    @Override
    public void clear(){
        keyword = null;
        loginAccount = null;
        companyList = null;
        company = null;
        plant = null;
        sapClientCode = null;
    }
    
    //<editor-fold defaultstate="collapsed" desc="getter and setter">
    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getSapClientCode() {
        return sapClientCode;
    }

    public void setSapClientCode(String sapClientCode) {
        this.sapClientCode = sapClientCode;
    }

    public boolean isPlantUser() {
        return plantUser;
    }

    public void setPlantUser(boolean plantUser) {
        this.plantUser = plantUser;
    }

    public boolean isSelPlant() {
        return selPlant;
    }

    public void setSelPlant(boolean selPlant) {
        this.selPlant = selPlant;
    }

    public boolean isAllowOnly() {
        return allowOnly;
    }

    public void setAllowOnly(boolean allowOnly) {
        this.allowOnly = allowOnly;
    }

    public String getRptType() {
        return rptType;
    }

    public void setRptType(String rptType) {
        this.rptType = rptType;
    }

    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }

    public List<String> getCompanyList() {
        return companyList;
    }

    public void setCompanyList(List<String> companyList) {
        this.companyList = companyList;
    }

    public Long getPlant() {
        return plant;
    }

    public void setPlant(Long plant) {
        this.plant = plant;
    }

    public Long getCompany() {
        return company;
    }

    public void setCompany(Long company) {
        this.company = company;
    }

    public boolean isIncludeTCCI() {
        return includeTCCI;
    }

    public void setIncludeTCCI(boolean includeTCCI) {
        this.includeTCCI = includeTCCI;
    }
    //</editor-fold>
}
