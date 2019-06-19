/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade.shipping;

import com.tcci.ec.entity.EcShipping;
import com.tcci.ec.entity.EcStore;
import com.tcci.ec.facade.AbstractFacade;
import com.tcci.ec.model.ShippingVO;
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
public class EcShippingFacade extends AbstractFacade<EcShipping> {
    private final static Logger logger = LoggerFactory.getLogger(EcShippingFacade.class);
    
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    public EcShippingFacade() {
        super(EcShipping.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public void save(EcShipping entity) {
        entity.setModifytime(new Date());
        if (entity.getId() == null) {
            entity.setCreatetime(new Date());
            em.persist(entity);
        } else {
            em.merge(entity);
        }
    }
    
    public List<EcShipping> findAll() {
        Query q = em.createNamedQuery("EcShipping.findAll");
        List<EcShipping> list = q.getResultList();
        logger.error("list:"+list);
        return list;
    }
    
    public List<EcShipping> findByStore(EcStore store) {
        Query q = em.createNamedQuery("EcShipping.findByStore");
        q.setParameter("store", store);
        List<EcShipping> list = q.getResultList();
        return list;
    }
    
    public List<EcShipping> findDefault() {
        Query q = em.createNamedQuery("EcShipping.findDefault");
        List<EcShipping> list = q.getResultList();
        return list;
    }
    
    public List<ShippingVO> findByStore(Long storeId) {
        BaseCriteriaVO criteriaVO = new BaseCriteriaVO();
        criteriaVO.setStoreId(storeId);
        List<ShippingVO> list = findByCriteria(criteriaVO);
        return list;
    }
    
    /**
     * @param criteriaVO
     * @return 
     */
    public List<ShippingVO> findByCriteria(BaseCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT S.* \n");
        sql.append("FROM EC_SHIPPING S \n");
        sql.append("WHERE 1=1 \n");
        sql.append("AND DISABLED=0 \n");

        //if( GlobalConstant.SHARE_SHIP_METHOD ){
        //    sql.append("AND S.STORE_ID IS NULL \n");
        //}else{
            if( criteriaVO.getStoreId()!=null ){
                sql.append("AND S.STORE_ID=#STORE_ID \n");   
                params.put("STORE_ID", criteriaVO.getStoreId());
            }
        //}
        if( criteriaVO.getCode()!=null ){
            sql.append("AND S.CODE=#CODE \n");       
            params.put("CODE", criteriaVO.getCode());
        }
               
        if( criteriaVO.getOrderBy()!=null ){
            sql.append("ORDER BY ").append(criteriaVO.getOrderBy());
        }else{
            sql.append("ORDER BY S.SORTNUM, S.TITLE");
        }
        
        List<ShippingVO> list = null;
        if( criteriaVO.getFirstResult()!=null && criteriaVO.getMaxResults()!=null ){
            list = this.selectBySql(ShippingVO.class, sql.toString(), params, criteriaVO.getFirstResult(), criteriaVO.getMaxResults());
        }else if( criteriaVO.getMaxResults()!=null ){
            list = this.selectBySql(ShippingVO.class, sql.toString(), params, 0, criteriaVO.getMaxResults());
        }else{
            list = this.selectBySql(ShippingVO.class, sql.toString(), params);
        }
        return list;
    }
    
}
