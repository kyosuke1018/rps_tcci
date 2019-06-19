/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.model.rfq;

import com.tcci.cm.model.admin.CmCompanyVO;
import com.tcci.cm.model.admin.CmFactoryVO;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Peter.pan
 */
public class RfqVO {
    private Long companyId;
    private Long companyOri;
    
    private Long factoryId;
    private Long factoryOri;
    private List<Long> factoryIds;
    
    private Date rfqDate;
    private Date Deadline;
    private String purOrg;
    private String purGroup;
    
    private CmCompanyVO company;
    private CmFactoryVO factory;
    private List<CmFactoryVO> factorys;
    
    private List<String> prNos;
    private List<RfqItemVO> items;
    
    private String rfqNo;

    //<editor-fold defaultstate="collapsed" desc="getter and setter">
    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getFactoryOri() {
        return factoryOri;
    }

    public void setFactoryOri(Long factoryOri) {
        this.factoryOri = factoryOri;
    }

    public Long getCompanyOri() {
        return companyOri;
    }

    public void setCompanyOri(Long companyOri) {
        this.companyOri = companyOri;
    }

    public Long getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(Long factoryId) {
        this.factoryId = factoryId;
    }

    public List<Long> getFactoryIds() {
        return factoryIds;
    }

    public void setFactoryIds(List<Long> factoryIds) {
        this.factoryIds = factoryIds;
    }

    public Date getRfqDate() {
        return rfqDate;
    }

    public void setRfqDate(Date rfqDate) {
        this.rfqDate = rfqDate;
    }

    public Date getDeadline() {
        return Deadline;
    }

    public void setDeadline(Date Deadline) {
        this.Deadline = Deadline;
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

    public CmCompanyVO getCompany() {
        return company;
    }

    public void setCompany(CmCompanyVO company) {
        this.company = company;
    }

    public CmFactoryVO getFactory() {
        return factory;
    }

    public void setFactory(CmFactoryVO factory) {
        this.factory = factory;
        if( factory!=null ){
            this.factoryId = factory.getId();
        }
    }

    public List<CmFactoryVO> getFactorys() {
        return factorys;
    }

    public void setFactorys(List<CmFactoryVO> factorys) {
        this.factorys = factorys;
    }

    public List<String> getPrNos() {
        return prNos;
    }

    public void setPrNos(List<String> prNos) {
        this.prNos = prNos;
    }

    public List<RfqItemVO> getItems() {
        return items;
    }

    public void setItems(List<RfqItemVO> items) {
        this.items = items;
    }

    public String getRfqNo() {
        return rfqNo;
    }

    public void setRfqNo(String rfqNo) {
        this.rfqNo = rfqNo;
    }
    
    //</editor-fold>
}
