/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.facade.salesarea;

import com.tcci.tccstore.entity.EcSalesarea;
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
public class EcSalesareaFacade extends AbstractFacade {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EcSalesareaFacade() {
        super(EcSalesarea.class);
    }

    public void save(EcSalesarea entity) {
        if (entity.getId() == null) {
            em.persist(entity);
        } else {
            em.merge(entity);
        }
    }
    
    public EcSalesarea find(Long id) {
        return em.find(EcSalesarea.class, id);
    }

    public EcSalesarea findByCode(String code) {
        Query q = em.createNamedQuery("EcSalesarea.findByCode");
        q.setParameter("code", code);
        List<EcSalesarea> list = q.getResultList();
        return list.isEmpty() ? null : list.get(0);
    }
    
    public List<EcSalesarea> findAll() {
        Query q = em.createNamedQuery("EcSalesarea.findAll");
        return q.getResultList();
    }

}
