/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade.order;

import com.tcci.ec.entity.EcMember;
import com.tcci.ec.entity.EcOrder;
import com.tcci.ec.entity.EcOrderMessage;
import com.tcci.ec.facade.AbstractFacade;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class EcOrderMessageFacade extends AbstractFacade<EcOrderMessage> {
    private final static Logger logger = LoggerFactory.getLogger(EcOrderMessageFacade.class);
    
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    public EcOrderMessageFacade() {
        super(EcOrderMessage.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public void save(EcOrderMessage entity) {
        if (entity.getId() == null) {
            entity.setCreatetime(new Date());
            em.persist(entity);
        } else {
            em.merge(entity);
        }
    }
    
    public List<EcOrderMessage> findByOrder(EcOrder order) {
        Query q = em.createNamedQuery("EcOrderMessage.findByOrder");
        q.setParameter("order", order);
        List<EcOrderMessage> list = q.getResultList();
        return list;
    }
    
    
    public void updateReadtime(EcOrder order, EcMember member){
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("BEGIN \n");

        sql.append("UPDATE EC_ORDER_MESSAGE \n");
        sql.append("SET READTIME=#readtime \n");
        
        sql.append("WHERE 1=1 \n");
        sql.append("AND READTIME is null \n");
        sql.append("AND ORDER_ID=#ORDER_ID \n");
        sql.append("AND CREATOR!=#MEMBER_ID ; \n");

        params.put("readtime", new Date());
        params.put("ORDER_ID", order.getId());
        params.put("MEMBER_ID", member.getId());

        sql.append("END; \n");
        
        logger.debug("updateReadtime sql =\n"+sql.toString());
        Query q = em.createNativeQuery(sql.toString());
        setParamsToQuery("updateReadtime", params, q);
        
        q.executeUpdate();
    }
}
