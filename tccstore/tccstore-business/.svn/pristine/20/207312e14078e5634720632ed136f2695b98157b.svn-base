/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.facade.product;

import com.tcci.tccstore.entity.EcMember;
import com.tcci.tccstore.entity.EcPlant;
import com.tcci.tccstore.entity.EcProduct;
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
public class EcProductFacade extends AbstractFacade<EcProduct> {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EcProductFacade() {
        super(EcProduct.class);
    }

    public void save(EcProduct entity) {
        if (entity.getId() == null) {
            em.persist(entity);
        } else {
            em.merge(entity);
        }
    }
    
    public EcProduct find(Long id) {
        return em.find(EcProduct.class, id);
    }

    public EcProduct findByCode(String code) {
        Query q = em.createNamedQuery("EcProduct.findByCode");
        q.setParameter("code", code);
        List<EcProduct> list = q.getResultList();
        return list.isEmpty() ? null : list.get(0);
    }

    public List<EcProduct> findByPlant(EcPlant ecPlant) {
        Query q = em.createNamedQuery("EcPlantProduct.findByPlant");
        q.setParameter("ecPlant", ecPlant);
        return q.getResultList();
    }
    
    public List<EcProduct> findAllPartnerProduct() {
        Query q = em.createNamedQuery("EcPartnerProduct.findAllPartnerProducts");
        return q.getResultList();
    }
    
}
