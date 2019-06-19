/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade.seller;

import com.tcci.ec.entity.EcMember;
import com.tcci.ec.entity.EcSeller;
import com.tcci.ec.entity.EcStore;
import com.tcci.ec.facade.AbstractFacade;
import com.tcci.ec.facade.member.EcMemberFacade;
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
public class EcSellerFacade extends AbstractFacade<EcSeller> {
    private final static Logger logger = LoggerFactory.getLogger(EcSellerFacade.class);
    
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    public EcSellerFacade() {
        super(EcSeller.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public void save(EcSeller entity) {
        entity.setModifytime(new Date());
        if (entity.getId() == null) {
            entity.setCreatetime(new Date());
            em.persist(entity);
        } else {
            em.merge(entity);
        }
    }
    
    public EcSeller findByMember(EcMember member) {
        Query q = em.createNamedQuery("EcSeller.findByMember");
        q.setParameter("member", member);
        List<EcSeller> list = q.getResultList();
//        return list;
        return list.isEmpty() ? null : list.get(0);
    }
}
