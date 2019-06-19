/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade.member;

import com.tcci.ec.entity.EcFavoritePrd;
import com.tcci.ec.entity.EcMember;
import com.tcci.ec.entity.EcProduct;
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
public class EcFavoritePrdFacade extends AbstractFacade<EcFavoritePrd> {
    private final static Logger logger = LoggerFactory.getLogger(EcFavoritePrdFacade.class);
    
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    public EcFavoritePrdFacade() {
        super(EcFavoritePrd.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public void save(EcFavoritePrd entity) {
        if (entity.getId() == null) {
            entity.setCreatetime(new Date());
            em.persist(entity);
        } else {
            em.merge(entity);
        }
    }
    
    public List<EcFavoritePrd> findByMember(EcMember member) {
        Query q = em.createNamedQuery("EcFavoritePrd.findByMember");
        q.setParameter("member", member);
        List<EcFavoritePrd> list = q.getResultList();
        return list;
    }
    
    
    public List<EcFavoritePrd> findByPrimary(EcMember member, EcProduct product) {
        Query q = em.createNamedQuery("EcFavoritePrd.findByPrimary");
        q.setParameter("member", member);
        q.setParameter("product", product);
        List<EcFavoritePrd> list = q.getResultList();
        return list;
    }
    
    
}
