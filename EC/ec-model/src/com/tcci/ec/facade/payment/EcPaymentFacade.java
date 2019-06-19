/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade.payment;

import com.tcci.ec.entity.EcPayment;
import com.tcci.ec.entity.EcStore;
import com.tcci.ec.facade.AbstractFacade;
import com.tcci.ec.model.PaymentVO;
import com.tcci.ec.model.criteria.BaseCriteriaVO;
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
public class EcPaymentFacade extends AbstractFacade<EcPayment> {
    private final static Logger logger = LoggerFactory.getLogger(EcPaymentFacade.class);
    
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    public EcPaymentFacade() {
        super(EcPayment.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public void save(EcPayment entity) {
        entity.setModifytime(new Date());
        if (entity.getId() == null) {
            entity.setCreatetime(new Date());
            em.persist(entity);
        } else {
            em.merge(entity);
        }
    }
    
    public List<EcPayment> findAll() {
        Query q = em.createNamedQuery("EcPayment.findAll");
        List<EcPayment> list = q.getResultList();
        logger.error("list:"+list);
        return list;
    }
    
    public List<EcPayment> findByStore(EcStore store) {
        Query q = em.createNamedQuery("EcPayment.findByStore");
        q.setParameter("store", store);
        List<EcPayment> list = q.getResultList();
        return list;
    }
    
    public List<EcPayment> findDefault() {
        Query q = em.createNamedQuery("EcPayment.findDefault");
        List<EcPayment> list = q.getResultList();
        return list;
    }
    
    public List<PaymentVO> findByStore(Long storeId) {
        BaseCriteriaVO criteriaVO = new BaseCriteriaVO();
        criteriaVO.setStoreId(storeId);
        List<PaymentVO> list = findByCriteria(criteriaVO);
        return list;
    }
    
    /**
     * @param criteriaVO
     * @return 
     */
    public List<PaymentVO> findByCriteria(BaseCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT S.* \n");
        sql.append("FROM EC_PAYMENT S \n");
        sql.append("WHERE 1=1 \n");
        sql.append("AND DISABLED=0 \n");

        if( criteriaVO.getStoreId()!=null ){
            sql.append("AND S.STORE_ID=#STORE_ID \n");       
            params.put("STORE_ID", criteriaVO.getStoreId());
        }
        if( criteriaVO.getCode()!=null ){
            sql.append("AND S.CODE=#CODE \n");       
            params.put("CODE", criteriaVO.getCode());
        }

        if( criteriaVO.getOrderBy()!=null ){
            sql.append("ORDER BY ").append(criteriaVO.getOrderBy());
        }else{
            sql.append("ORDER BY S.SORTNUM, S.TITLE \n");
        }
        
        List<PaymentVO> list = null;
        if( criteriaVO.getFirstResult()!=null && criteriaVO.getMaxResults()!=null ){
            list = this.selectBySql(PaymentVO.class, sql.toString(), params, criteriaVO.getFirstResult(), criteriaVO.getMaxResults());
        }else if( criteriaVO.getMaxResults()!=null ){
            list = this.selectBySql(PaymentVO.class, sql.toString(), params, 0, criteriaVO.getMaxResults());
        }else{
            list = this.selectBySql(PaymentVO.class, sql.toString(), params);
        }
        return list;
    }
    
}
