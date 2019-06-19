/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade.order;

import com.tcci.ec.entity.EcMember;
import com.tcci.ec.entity.EcOrderRate;
import com.tcci.ec.facade.AbstractFacade;
import com.tcci.ec.vo.Member;
import com.tcci.ec.vo.Store;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.collections.CollectionUtils;

/**
 *
 * @author Peter.pan
 */
@Stateless
public class EcOrderRateFacade extends AbstractFacade<EcOrderRate> {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EcOrderRateFacade() {
        super(EcOrderRate.class);
    }
    
    /**
     * 單筆儲存
     * @param entity
     * @param operator
     * @param simulated
     */
//    public void save(EcOrderRate entity, EcMember operator, boolean simulated){
//        if( entity!=null ){
//            if( entity.getId()!=null && entity.getId()>0 ){
//                entity.setModifier(operator);
//                entity.setModifytime(new Date());
//                this.edit(entity, simulated);
//                logger.info("save update "+entity);
//            }else{
//                entity.setCreator(operator);
//                entity.setCreatetime(new Date());
//                this.create(entity, simulated);
//                logger.info("save new "+entity);
//            }
//        }
//    }
    public void save(EcOrderRate entity) {
        entity.setModifytime(new Date());
        if (entity.getId() == null) {
            entity.setCreatetime(new Date());
            em.persist(entity);
        } else {
            em.merge(entity);
        }
    }

    public EcOrderRate findByOrderId(Long storeId, Long orderId){
        Map<String, Object> params = new HashMap<>();
        params.put("storeId", storeId);
        params.put("orderId", orderId);
        List<EcOrderRate> list = this.findByNamedQuery("EcOrderRate.findByOrderId", params);
        
        return (list!=null && !list.isEmpty())?list.get(0):null;
    }
    
    /**
     * 輸入檢查
     * @param entity
     * @param member
     * @param locale
     * @param errors
     * @return 
     */
    public boolean checkInput(EcOrderRate entity, EcMember member, Locale locale, List<String> errors) {
        boolean pass = true;
        pass = inputCheckFacade.checkInput(entity, locale, errors);

        return pass;
    }
    
    public Store countRateByStore(Store store){
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT p.rate,n.rate FROM \n");
        sql.append("(SELECT COUNT(1) as rate from ec_order_rate WHERE store_id = #STORE_ID AND SELLER_RATE = 1) p, \n");
        sql.append("(SELECT COUNT(1) as rate from ec_order_rate WHERE store_id = #STORE_ID AND SELLER_RATE = -1) n ");
        params.put("STORE_ID", store.getId());
        
        Query query = em.createNativeQuery(sql.toString());
        for (String key : params.keySet()) {
            query.setParameter(key, params.get(key));
        }
        List list = query.getResultList();
        if (CollectionUtils.isEmpty(list)) {
            return store;
        }
        Object[] columns = (Object[]) list.get(0);
        store.setPrate((BigDecimal) columns[0]);
        store.setNrate((BigDecimal) columns[1]);
        
        return store;
    }
    
    public Member countRateByMember(Member member){
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT p.rate,n.rate FROM \n");
        sql.append("(SELECT COUNT(1) as rate from ec_order_rate r join ec_order o on o.id = r.ORDER_ID WHERE o.MEMBER_ID = #MEMBER_ID AND CUSTOMER_RATE = 1) p, \n");
        sql.append("(SELECT COUNT(1) as rate from ec_order_rate r join ec_order o on o.id = r.ORDER_ID WHERE o.MEMBER_ID = #MEMBER_ID AND CUSTOMER_RATE = -1) n ");
        params.put("MEMBER_ID", member.getId());
        
        Query query = em.createNativeQuery(sql.toString());
        for (String key : params.keySet()) {
            query.setParameter(key, params.get(key));
        }
        List list = query.getResultList();
        if (CollectionUtils.isEmpty(list)) {
            return member;
        }
        Object[] columns = (Object[]) list.get(0);
        member.setPrate((BigDecimal) columns[0]);
        member.setNrate((BigDecimal) columns[1]);
        
        return member;
    }
    
}
