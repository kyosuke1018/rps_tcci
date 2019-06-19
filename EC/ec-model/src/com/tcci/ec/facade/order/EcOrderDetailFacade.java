/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade.order;

import com.tcci.ec.entity.EcOrder;
import com.tcci.ec.entity.EcOrderDetail;
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
public class EcOrderDetailFacade extends AbstractFacade<EcOrderDetail> {
    private final static Logger logger = LoggerFactory.getLogger(EcOrderDetailFacade.class);
    
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    public EcOrderDetailFacade() {
        super(EcOrderDetail.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public void save(EcOrderDetail entity) {
        entity.setModifytime(new Date());
        if (entity.getId() == null) {
            entity.setCreatetime(new Date());
            em.persist(entity);
        } else {
            em.merge(entity);
        }
    }
    
    public List<EcOrderDetail> findByOrder(EcOrder order) {
        Query q = em.createNamedQuery("EcOrderDetail.findByOrder");
        q.setParameter("order", order);
        List<EcOrderDetail> list = q.getResultList();
        return list;
    }
}
