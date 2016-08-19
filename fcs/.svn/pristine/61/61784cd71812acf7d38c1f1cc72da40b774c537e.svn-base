/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.irs.facade;

import com.tcci.fcs.entity.FcCompany;
import com.tcci.fcs.enums.CompanyGroupEnum;
import com.tcci.irs.entity.IrsCompanyType;
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
public class IrsCompanyTypeFacade {
    @PersistenceContext(unitName="Model")
    private EntityManager em;
    
    public IrsCompanyType find(Long id) {
        return em.find(IrsCompanyType.class, id);
    }
    
    public List<IrsCompanyType> findAll(CompanyGroupEnum group) {
        Query q = em.createNamedQuery("IrsCompanyType.findAll");
        q.setParameter("group", group.getCode());
        return q.getResultList();
    }
    
    public IrsCompanyType findByCompany(FcCompany company, CompanyGroupEnum group) {
        Query q = em.createNamedQuery("IrsCompanyType.findByCompany");
        q.setParameter("group", group.getCode());
        q.setParameter("company", company);
        List<IrsCompanyType> list = q.getResultList();
        return list.isEmpty() ? null : list.get(0);
    }
    
    public void save(IrsCompanyType irsCompanyType) {
        if (irsCompanyType.getId()==null) {
            em.persist(irsCompanyType);
        } else {
            em.merge(irsCompanyType);
        }
    }
    
    public void remove(Long id){
        if (id!=null) {
            em.remove(this.find(id));
        }
    }
}
