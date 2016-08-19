/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.facade.deliveryplace;

import com.tcci.tccstore.entity.EcDeliveryPlace;
import com.tcci.tccstore.entity.EcDeliveryVkorg;
import com.tcci.tccstore.entity.EcDeliveryVkorgPK;
import com.tcci.tccstore.entity.EcSalesarea;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Jimmy.Lee
 */
@Named
@Stateless
public class EcDeliveryVkorgFacade {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    public EcDeliveryVkorg find(EcDeliveryPlace ecDeliveryPlace, String vkorg) {
        EcDeliveryVkorgPK pk = new EcDeliveryVkorgPK(ecDeliveryPlace.getId(), vkorg);
        return em.find(EcDeliveryVkorg.class, pk);
    }
    
    public EcDeliveryVkorg find(long delivery_id, String vkorg) {
        EcDeliveryVkorgPK pk = new EcDeliveryVkorgPK(delivery_id, vkorg);
        return em.find(EcDeliveryVkorg.class, pk);
    }
    
    public List<EcDeliveryVkorg> findAll() {
        return em.createNamedQuery("EcDeliveryVkorg.findAll").getResultList();
    }
    
    public List<EcDeliveryVkorg> findByDelivery(EcDeliveryPlace delivery) {
        Query q = em.createNamedQuery("EcDeliveryVkorg.findByDelivery");
        q.setParameter("delivery", delivery);
        return q.getResultList();
    }
    
    public EcDeliveryVkorg insert(EcDeliveryPlace ecDeliveryPlace, String vkorg, EcSalesarea ecSalesarea) {
        EcDeliveryVkorgPK pk = new EcDeliveryVkorgPK(ecDeliveryPlace.getId(), vkorg);
        EcDeliveryVkorg entity = new EcDeliveryVkorg(pk);
        entity.setEcDeliveryPlace(ecDeliveryPlace);
        entity.setEcSalesarea(ecSalesarea);
        em.persist(entity);
        return entity;
    }
    
    public void edit(EcDeliveryVkorg entity) {
        em.merge(entity);
    }
    
    public void remove(EcDeliveryVkorg entity) {
        em.remove(em.merge(entity));
    }

}
