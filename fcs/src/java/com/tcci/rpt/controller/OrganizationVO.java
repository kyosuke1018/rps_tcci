/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.rpt.controller;

import com.tcci.rpt.entity.RptCompanyOrg;
import java.io.Serializable;

/**
 *
 * @author Kyle.Cheng
 */
public class OrganizationVO implements Serializable {
    private long key; // for UI : before save to DB
    private boolean hasChild;
    private RptCompanyOrg companyOrg;

    public OrganizationVO(){    
    }
    
    public OrganizationVO(long key, boolean hasChild, RptCompanyOrg companyOrg){
        this.key = key;
        this.hasChild = hasChild;
        this.companyOrg = companyOrg;
    }
    public long getKey() {
        return key;
    }

    public void setKey(long key) {
        this.key = key;
    }

    public boolean isHasChild() {
        return hasChild;
    }

    public void setHasChild(boolean hasChild) {
        this.hasChild = hasChild;
    }

    public RptCompanyOrg getCompanyOrg() {
        return companyOrg;
    }

    public void setCompanyOrg(RptCompanyOrg companyOrg) {
        this.companyOrg = companyOrg;
    }
    
    @Override
    public String toString(){
        if(companyOrg.getCompany()!=null){
            return companyOrg.getCompany().toString();
        }
        return companyOrg.getCode();
    }

}
