/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.facade.contract;

import com.tcci.tccstore.entity.EcContract;
import com.tcci.tccstore.entity.EcContractProduct;
import com.tcci.tccstore.entity.EcContractProductPK;
import com.tcci.tccstore.entity.EcCustomer;
import com.tcci.tccstore.entity.EcPlant;
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
public class EcContractFacade extends AbstractFacade {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EcContractFacade() {
        super(EcContract.class);
    }

    public void save(EcContract entity) {
        if (entity.getId() == null) {
            em.persist(entity);
        } else {
            em.merge(entity);
        }
    }
    
    public EcContract find(Long id) {
        return em.find(EcContract.class, id);
    }
    
    public EcContract findByCode(String code) {
        Query q = em.createNamedQuery("EcContract.findByCode");
        q.setParameter("code", code);
        List<EcContract> list = q.getResultList();
        return list.isEmpty() ? null : list.get(0);
    }
    
    public List<EcContract> findByCustomer(EcCustomer ecCustomer) {
        Query q = em.createNamedQuery("EcContract.findByCustomer");
        q.setParameter("ecCustomer", ecCustomer);
        return q.getResultList();
    }
    
    public List<EcContractProduct> findByContract(EcContract ecContract) {
        Query q = em.createNamedQuery("EcContractProduct.findByContract");
        q.setParameter("ecContract", ecContract);
        return q.getResultList();
    }
    
    public List<EcContractProduct> findByContractPlant(EcContract ecContract, EcPlant ecPlant) {
        Query q = em.createNamedQuery("EcContractProduct.findByContractPlant");
        q.setParameter("ecContract", ecContract);
        q.setParameter("ecPlant", ecPlant);
        return q.getResultList();
    }
    
    /*
    public EcContractProduct find(EcContract ecContract, EcProduct ecProduct) {
        String sql = "SELECT e FROM EcContractProduct e WHERE e.ecContract=:ecContract AND e.ecProduct=:ecProduct";
        Query q = em.createQuery(sql);
        q.setParameter("ecContract", ecContract);
        q.setParameter("ecProduct", ecProduct);
        List<EcContractProduct> list = q.getResultList();
        return list.isEmpty() ? null : list.get(0);
    }
    */
    
    public EcContractProduct find(EcContract ecContract, Integer posnr) {
        EcContractProductPK pk = new EcContractProductPK(ecContract.getId(), posnr);
        EcContractProduct entity = em.find(EcContractProduct.class, pk);
        return entity;
    }

    public void save(EcContractProduct entity, int posnr) {
        if (null == entity.getEcContractProductPK()) {
            long contractId = entity.getEcContract().getId();
            EcContractProductPK pk = new EcContractProductPK(contractId, posnr);
            entity.setEcContractProductPK(pk);
            em.persist(entity);
        } else {
            em.merge(entity);
        }
    }
    
    public void removeContractProduct(EcContractProduct entity) {
        em.remove(em.merge(entity));
    }
    
    public List<EcContractProduct> findAllProduct() {
        return em.createNamedQuery("EcContractProduct.findAll").getResultList();
    }

}
