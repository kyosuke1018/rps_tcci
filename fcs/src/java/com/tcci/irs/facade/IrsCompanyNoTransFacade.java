/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.irs.facade;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.fcs.entity.FcCompany;
import com.tcci.irs.entity.IrsCompanyNoTrans;
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
public class IrsCompanyNoTransFacade {
    @PersistenceContext(unitName="Model")
    private EntityManager em;
    
    public IrsCompanyNoTrans find(Long id) {
        return em.find(IrsCompanyNoTrans.class, id);
    }
    
    public List<IrsCompanyNoTrans> findByYearmonth(String yearmonth) {
        Query q = em.createNamedQuery("IrsCompanyNoTrans.findByYearmonth");
        q.setParameter("yearmonth", yearmonth);
        return q.getResultList();
    }
    public IrsCompanyNoTrans findByYearmonthCompany(String yearmonth, FcCompany company) {
        Query q = em.createNamedQuery("IrsCompanyNoTrans.findByYearmonthCompany");
        q.setParameter("yearmonth", yearmonth);
        q.setParameter("company", company);
        List<IrsCompanyNoTrans> list = q.getResultList();
        return list.isEmpty() ? null : list.get(0);
    }
    
    public void insert(String yearmonth, FcCompany company, TcUser user){
        IrsCompanyNoTrans entity = new IrsCompanyNoTrans();
        entity.setYearmonth(yearmonth);
        entity.setCompany(company);
        entity.setUser(user);
        this.save(entity);
    }
    
    public void save(IrsCompanyNoTrans irsCompanyNoTrans) {
        if (irsCompanyNoTrans.getId()==null) {
            irsCompanyNoTrans.setCreatetimestamp(new Date());
            em.persist(irsCompanyNoTrans);
        } else {
            em.merge(irsCompanyNoTrans);
        }
    }
    
    public void remove(Long id){
        if (id!=null) {
            em.remove(this.find(id));
        }
    }
//    public void remove(String yearmonth, FcCompany company){
//        IrsCompanyNoTrans entity = this.findByYearmonthCompany(yearmonth, company);
//        if (entity!=null) {
//            em.remove(entity);
//        }
//    }
    
}
