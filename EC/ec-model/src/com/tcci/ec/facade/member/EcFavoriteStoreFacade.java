/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade.member;

import com.tcci.ec.entity.EcMember;
import com.tcci.ec.entity.EcFavoriteStore;
import com.tcci.ec.entity.EcStore;
import com.tcci.ec.facade.AbstractFacade;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Kyle.Cheng
 */
@Stateless
public class EcFavoriteStoreFacade extends AbstractFacade<EcFavoriteStore> {
    private final static Logger logger = LoggerFactory.getLogger(EcFavoriteStoreFacade.class);
    
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    public EcFavoriteStoreFacade() {
        super(EcFavoriteStore.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public void save(EcFavoriteStore entity) {
        if (entity.getId() == null) {
            entity.setCreatetime(new Date());
            em.persist(entity);
        } else {
            em.merge(entity);
        }
    }
    
    public List<EcFavoriteStore> findByMember(EcMember member) {
        Query q = em.createNamedQuery("EcFavoriteStore.findByMember");
        q.setParameter("member", member);
        List<EcFavoriteStore> list = q.getResultList();
        return list;
    }
    
    public List<EcFavoriteStore> findByPrimary(EcMember member, EcStore store) {
        Query q = em.createNamedQuery("EcFavoriteStore.findByPrimary");
        q.setParameter("member", member);
        q.setParameter("store", store);
        List<EcFavoriteStore> list = q.getResultList();
        return list;
    }
    
    
}
