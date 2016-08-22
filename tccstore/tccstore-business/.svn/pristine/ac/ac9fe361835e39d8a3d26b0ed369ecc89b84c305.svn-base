/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.facade.sales;

import com.tcci.tccstore.entity.EcCustomer;
import com.tcci.tccstore.entity.EcSales;
import com.tcci.tccstore.facade.AbstractFacade;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Neo.Fu
 */
@Named
@Stateless
public class EcSalesFacade extends AbstractFacade {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EcSalesFacade() {
        super(EcSales.class);
    }

    public void save(EcSales entity) {
        if (entity.getId() == null) {
            em.persist(entity);
        } else {
            em.merge(entity);
        }
    }
    
    public EcSales find(Long id) {
        return em.find(EcSales.class, id);
    }
    
    public EcSales findByCode(String code) {
        Query q = em.createNamedQuery("EcSales.findByCode");
        q.setParameter("code", code);
        List<EcSales> list = q.getResultList();
        return list.isEmpty() ? null : list.get(0);
    }
    
    public List<EcSales> findByCustomer(EcCustomer ecCustomer) {
        String sql = "SELECT e.ecSales FROM EcCustomerSales e WHERE e.ecCustomer=:ecCustomer";
        Query q = em.createQuery(sql);
        q.setParameter("ecCustomer", ecCustomer);
        return q.getResultList();
    }

    public List<EcSales> findActiveByCustomer(EcCustomer ecCustomer) {
        String sql = "SELECT e.ecSales FROM EcCustomerSales e WHERE e.ecCustomer=:ecCustomer AND e.ecSales.active=TRUE";
        Query q = em.createQuery(sql);
        q.setParameter("ecCustomer", ecCustomer);
        return q.getResultList();
    }

}
