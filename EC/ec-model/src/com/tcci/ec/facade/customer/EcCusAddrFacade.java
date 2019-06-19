/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade.customer;

import com.tcci.ec.entity.EcCusAddr;
import com.tcci.ec.entity.EcMember;
import com.tcci.ec.facade.AbstractFacade;
import com.tcci.ec.facade.AbstractFacade;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author kyle.cheng
 */
@Stateless
public class EcCusAddrFacade  extends AbstractFacade {
    private final static Logger logger = LoggerFactory.getLogger(EcCustomerFacade.class);
    
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EcCusAddrFacade() {
        super(EcCusAddr.class);
    }

    public EcCusAddr save(EcCusAddr entity) {
        if (entity.getId() == null) {
            em.persist(entity);
        } else {
            em.merge(entity);
        }
        return entity;
    }
    
    public EcCusAddr find(Long id) {
        return em.find(EcCusAddr.class, id);
    }
    
    @Override
    public List<EcCusAddr> findAll() {
        return em.createNamedQuery("EcCusAddr.findAll").getResultList();
    }
    
//    public List<EcCusAddr> findByCustomer(EcCustomer customer) {
//        Query q = em.createNamedQuery("EcCusAddr.findByCustomer");
//        q.setParameter("customer", customer);
//        return q.getResultList();
//    }
//    public List<EcCusAddr> findByCustomer(EcCustomer customer) {
//        String sql = "SELECT e FROM EcCusAddr e WHERE e.customer=:customer";
//        Query q = em.createQuery(sql);
//        q.setParameter("customer", customer);
//        return q.getResultList();
//    }
    public List<EcCusAddr> findByMember(EcMember member) {
        String sql = "SELECT e FROM EcCusAddr e WHERE e.member=:member";
        Query q = em.createQuery(sql);
        q.setParameter("member", member);
        return q.getResultList();
    }
    
}
