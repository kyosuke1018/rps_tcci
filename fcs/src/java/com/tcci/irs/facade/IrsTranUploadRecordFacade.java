/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.irs.facade;

import com.tcci.fcs.entity.FcCompany;
import com.tcci.irs.entity.IrsTranUploadRecord;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Kyle.Cheng
 */
@Stateless
public class IrsTranUploadRecordFacade extends AbstractFacade<IrsTranUploadRecord> {
    @PersistenceContext(unitName="Model")
    private EntityManager em;
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public IrsTranUploadRecordFacade() {
        super(IrsTranUploadRecord.class);
    }
    
    public IrsTranUploadRecord findByYearmonthCompany(String yearmonth, FcCompany company) {
        Query q = em.createNamedQuery("IrsTranUploadRecord.findByYearmonthCompany");
        q.setParameter("yearmonth", yearmonth);
        q.setParameter("companyCode", company.getCode());
        List<IrsTranUploadRecord> list = q.getResultList();
        return list.isEmpty() ? null : list.get(0);
    }
    
    public IrsTranUploadRecord findByYearmonthCompany(String yearmonth, String companyCode) {
        Query q = em.createNamedQuery("IrsTranUploadRecord.findByYearmonthCompany");
        q.setParameter("yearmonth", yearmonth);
        q.setParameter("companyCode", companyCode);
        List<IrsTranUploadRecord> list = q.getResultList();
        return list.isEmpty() ? null : list.get(0);
    }
    
    public void sapUpdate(String yearmonth, String companyCode, Date updateTime, String sapUserName) throws Exception {
        IrsTranUploadRecord irsTranUploadRecord = this.findByYearmonthCompany(yearmonth, companyCode);
        if (irsTranUploadRecord == null) {
            irsTranUploadRecord = new IrsTranUploadRecord();
            irsTranUploadRecord.setYearmonth(yearmonth);
            irsTranUploadRecord.setCompanyCode(companyCode);
            irsTranUploadRecord.setModifytimestamp(updateTime);
            irsTranUploadRecord.setSapUserName(sapUserName);
            em.persist(irsTranUploadRecord);
        } else {
            irsTranUploadRecord.setModifier(null);//以識別SAP覆蓋紀錄
            if(irsTranUploadRecord.getModifytimestamp()!=null){
                if(irsTranUploadRecord.getModifytimestamp().before(updateTime)){
                    irsTranUploadRecord.setModifytimestamp(updateTime);
                    irsTranUploadRecord.setSapUserName(sapUserName);
                    em.merge(irsTranUploadRecord);
                }
            }else{
                irsTranUploadRecord.setModifytimestamp(updateTime);
                irsTranUploadRecord.setSapUserName(sapUserName);
                em.merge(irsTranUploadRecord);
            }
        }
    }
}
