/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.rpt.facade;

import com.tcci.fcs.entity.FcCompany;
import com.tcci.rpt.entity.RptDataUploadRecord;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Kyle.Cheng
 */
@Stateless
public class RptDataUploadRecordFacade {
    private static final Logger logger = LoggerFactory.getLogger(RptDataUploadRecordFacade.class);
    @PersistenceContext(unitName="Model")
    private EntityManager em;
    
    public void save(RptDataUploadRecord rptDataUploadRecord) throws Exception {
        if (rptDataUploadRecord.getId() == null) {
            em.persist(rptDataUploadRecord);
        } else {
            em.merge(rptDataUploadRecord);
        }
    }
    public RptDataUploadRecord findByYearmonthCompany(String yearmonth, FcCompany company) {
        Query q = em.createNamedQuery("RptDataUploadRecord.findByYearmonthCompany");
        q.setParameter("yearmonth", yearmonth);
        q.setParameter("companyCode", company.getCode());
        List<RptDataUploadRecord> list = q.getResultList();
        return list.isEmpty() ? null : list.get(0);
    }
    public RptDataUploadRecord findByYearmonthCompany(String yearmonth, String companyCode) {
        Query q = em.createNamedQuery("RptDataUploadRecord.findByYearmonthCompany");
        q.setParameter("yearmonth", yearmonth);
        q.setParameter("companyCode", companyCode);
        List<RptDataUploadRecord> list = q.getResultList();
        return list.isEmpty() ? null : list.get(0);
    }
    
    public List<RptDataUploadRecord> findByYearmonth(String yearmonth) {
        Query q = em.createNamedQuery("RptDataUploadRecord.findByYearmonth");
        q.setParameter("yearmonth", yearmonth);
        return q.getResultList();
    }
    
    public void sapUpdate(String yearmonth, String companyCode, Date updateTime, String sapUserName) throws Exception {
        RptDataUploadRecord rptDataUploadRecord = this.findByYearmonthCompany(yearmonth, companyCode);
        if (rptDataUploadRecord == null) {
            rptDataUploadRecord = new RptDataUploadRecord();
            rptDataUploadRecord.setYearmonth(yearmonth);
            rptDataUploadRecord.setCompanyCode(companyCode);
            rptDataUploadRecord.setModifytimestamp(updateTime);
            rptDataUploadRecord.setSapUserName(sapUserName);
            em.persist(rptDataUploadRecord);
        } else {
            rptDataUploadRecord.setModifier(null);//以識別SAP覆蓋紀錄
            if(rptDataUploadRecord.getModifytimestamp()!=null){
                if(rptDataUploadRecord.getModifytimestamp().before(updateTime)){
                    rptDataUploadRecord.setModifytimestamp(updateTime);
                    rptDataUploadRecord.setSapUserName(sapUserName);
                    em.merge(rptDataUploadRecord);
                }
            }else{
                rptDataUploadRecord.setModifytimestamp(updateTime);
                rptDataUploadRecord.setSapUserName(sapUserName);
                em.merge(rptDataUploadRecord);
            }
        }
    }
}
