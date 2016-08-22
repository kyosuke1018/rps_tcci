/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.facade.bulletin;

import com.tcci.tccstore.entity.EcBulletin;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Jimmy.Lee
 */
@Stateless
public class EcBulletinFacade {
        
    @PersistenceContext(unitName = "Model")
    private EntityManager em;
    
    public void edit(EcBulletin entity) {
        if (entity.getId() == null) {
            em.persist(entity);
        } else {
            em.merge(entity);
        }
    }
    
    public void remove(EcBulletin entity) {
        em.remove(em.merge(entity));
    }
    
    public List<EcBulletin> findEffective() {
        Query q = em.createNamedQuery("EcBulletin.findEffective");
        q.setParameter("now", new Date());
        return q.getResultList();
    }
    
    public List<EcBulletin> findAll() {
        Query q = em.createNamedQuery("EcBulletin.findAll");
        return q.getResultList();
    }
}
