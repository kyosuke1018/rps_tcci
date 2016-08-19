/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.irs.facade;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.fcs.entity.FcCompany;
import com.tcci.irs.entity.IrsReconcilCompanyR;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.collections.CollectionUtils;

/**
 *
 * @author kyle.cheng
 */
@Stateless
public class IrsReconcilCompanyRFacade {
    @PersistenceContext(unitName="Model")
    private EntityManager em;
    
    public IrsReconcilCompanyR find(Long id) {
        return em.find(IrsReconcilCompanyR.class, id);
    }
    //使用者對帳公司權限
    public List<IrsReconcilCompanyR> findByUser(TcUser tcUser) {
        String sql = "SELECT r FROM IrsReconcilCompanyR r"
                + " WHERE r.tcUser=:tcUser"
                + " AND r.reconcilCompany.active=true AND r.company.active=true"
                + " ORDER BY r.company.code, r.reconcilCompany.code";
        Query q = em.createQuery(sql);
        q.setParameter("tcUser", tcUser);
        return q.getResultList();
    }
    
    public boolean isDuplicate(FcCompany company, TcUser tcUser, FcCompany reconcilCompany) {
        String sql = "SELECT r FROM IrsReconcilCompanyR r"
                + " WHERE r.tcUser=:tcUser"
                + " AND r.reconcilCompany=:reconcilCompany AND r.company=:company";
        Query q = em.createQuery(sql);
        q.setParameter("company", company);
        q.setParameter("tcUser", tcUser);
        q.setParameter("reconcilCompany", reconcilCompany);
        
        return CollectionUtils.isNotEmpty(q.getResultList());
    }
    
    public void save(IrsReconcilCompanyR irsReconcilCompanyR) {
        if (irsReconcilCompanyR.getId()==null) {
            irsReconcilCompanyR.setCreatetimestamp(new Date());
            em.persist(irsReconcilCompanyR);
        } else {
            em.merge(irsReconcilCompanyR);
        }
    }
    
    public void remove(Long id){
        if (id!=null) {
            em.remove(this.find(id));
        }
    }
    
}
