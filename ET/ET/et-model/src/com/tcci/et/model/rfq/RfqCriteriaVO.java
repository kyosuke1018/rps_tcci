/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.model.rfq;

import com.tcci.cm.model.global.AbstractCriteriaVO;
import com.tcci.cm.model.interfaces.IQueryCriteria;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Peter.pan
 */
public class RfqCriteriaVO extends AbstractCriteriaVO implements IQueryCriteria, Serializable {
    private Long companyId;
    private Long factoryId;
    private List<Long> factoryList;
    
    private String purOrg;
    private String purGroup;
    
    private String prNo;
    private String rfqNo;
    
    private String matnr;

    //<editor-fold defaultstate="collapsed" desc="getter and setter">
    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(Long factoryId) {
        this.factoryId = factoryId;
    }

    public List<Long> getFactoryList() {
        return factoryList;
    }

    public void setFactoryList(List<Long> factoryList) {
        this.factoryList = factoryList;
    }

    public String getPurOrg() {
        return purOrg;
    }

    public void setPurOrg(String purOrg) {
        this.purOrg = purOrg;
    }

    public String getPurGroup() {
        return purGroup;
    }

    public void setPurGroup(String purGroup) {
        this.purGroup = purGroup;
    }

    public String getPrNo() {
        return prNo;
    }

    public void setPrNo(String prNo) {
        this.prNo = prNo;
    }

    public String getRfqNo() {
        return rfqNo;
    }

    public void setRfqNo(String rfqNo) {
        this.rfqNo = rfqNo;
    }

    public String getMatnr() {
        return matnr;
    }

    public void setMatnr(String matnr) {
        this.matnr = matnr;
    }
    //</editor-fold>
}
