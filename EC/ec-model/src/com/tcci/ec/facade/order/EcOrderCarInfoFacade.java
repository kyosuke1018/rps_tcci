/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade.order;

import com.tcci.ec.entity.EcOrder;
import com.tcci.ec.entity.EcOrderCarInfo;
import com.tcci.ec.facade.AbstractFacade;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Kyle.Cheng
 */
@Stateless
public class EcOrderCarInfoFacade extends AbstractFacade<EcOrderCarInfo> {
    private final static Logger logger = LoggerFactory.getLogger(EcOrderDetailFacade.class);
    
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    public EcOrderCarInfoFacade() {
        super(EcOrderCarInfo.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public void save(EcOrderCarInfo entity) {
        entity.setModifytime(new Date());
        if (entity.getId() == null) {
            entity.setCreatetime(new Date());
            em.persist(entity);
        } else {
            em.merge(entity);
        }
    }
    
    public List<EcOrderCarInfo> findByOrder(EcOrder order) {
        Query q = em.createNamedQuery("EcOrderCarInfo.findByOrder");
        q.setParameter("orderId", order.getId());
        List<EcOrderCarInfo> list = q.getResultList();
        return list;
    }
    
    public void removeByOrder(EcOrder order) {
        List<EcOrderCarInfo> list = this.findByOrder(order);
        for(EcOrderCarInfo entity:list){
            em.remove(entity);
        }
    }
    
    
    public void deleteAndInsertCarInfo(String carNo, EcOrder order) {
        if(order.getStore()==null || order.getOrderDetails()==null){
            return;
        }
        Long storeId = order.getStore().getId();
        Long productId = order.getOrderDetails().get(0).getProduct().getId();
        if(storeId==null || productId==null){
            return;
        }
        this.removeByOrder(order);
        
        String[] cars = carNo.split(";");
        if(cars!=null){
            for (String no : cars) {
                if (StringUtils.isNotBlank(no)) {
                    EcOrderCarInfo entity = new EcOrderCarInfo(order.getId(), productId, storeId);
                    entity.setCarNo(no);
                    this.save(entity);
                }
            }
        }
        
    }
    
}
