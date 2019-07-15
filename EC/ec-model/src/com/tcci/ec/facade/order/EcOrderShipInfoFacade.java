/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade.order;

import com.tcci.ec.entity.EcOrder;
import com.tcci.ec.entity.EcOrderShipInfo;
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
public class EcOrderShipInfoFacade extends AbstractFacade<EcOrderShipInfo> {
    private final static Logger logger = LoggerFactory.getLogger(EcOrderShipInfoFacade.class);
    
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    public EcOrderShipInfoFacade() {
        super(EcOrderShipInfo.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public void save(EcOrderShipInfo entity) {
        entity.setModifytime(new Date());
        if (entity.getId() == null) {
            entity.setCreatetime(new Date());
            em.persist(entity);
        } else {
            em.merge(entity);
        }
    }
    
    public EcOrderShipInfo findByOrder(EcOrder order) {
        Query q = em.createNamedQuery("EcOrderShipInfo.findByOrder");
        q.setParameter("order", order);
        List<EcOrderShipInfo> list = q.getResultList();
        return list.isEmpty() ? null : list.get(0);
//        return list;
    }
}