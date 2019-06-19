/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade.product;

import com.tcci.ec.entity.EcMember;
import com.tcci.ec.entity.EcPrdAttrVal;
import com.tcci.ec.facade.AbstractFacade;
import com.tcci.ec.model.criteria.BaseCriteriaVO;
import com.tcci.ec.model.PrdAttrValVO;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Peter.pan
 */
@Stateless
public class EcPrdAttrValFacade extends AbstractFacade<EcPrdAttrVal> {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EcPrdAttrValFacade() {
        super(EcPrdAttrVal.class);
    }
    
    /**
     * 單筆儲存
     * @param entity
     * @param operator
     * @param simulated
     */
//    public void save(EcPrdAttrVal entity, EcMember operator, boolean simulated){
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
    public void save(EcPrdAttrVal entity) {
        if (entity.getId() == null) {
            em.persist(entity);
        } else {
            em.merge(entity);
        }
    }
    
    /**
     * 依條件查詢商品
     * @param criteriaVO
     * @return 
     */
    public List<PrdAttrValVO> findByCriteria(BaseCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT S.*, A.CNAME ATTRNAME, \n");
        sql.append("CASE WHEN A.TYPE_ID IS NULL THEN 'P' ELSE 'T' END ATTRSCOPE \n");
        sql.append("FROM EC_PRD_ATTR_VAL S \n");
        sql.append("JOIN EC_PRD_ATTR A ON A.ID=S.ATTR_ID \n");
        sql.append("WHERE 1=1 \n");
        sql.append("AND S.STORE_ID=#STORE_ID \n");
        sql.append("AND S.PRD_ID=#PRD_ID \n");

        if( criteriaVO.getStoreId()!=null ){
            sql.append("AND S.STORE_ID=#STORE_ID \n");
            params.put("STORE_ID", criteriaVO.getStoreId());
        }
        if( criteriaVO.getPrdId()!=null ){
            sql.append("AND S.PRD_ID=#PRD_ID \n");
            params.put("PRD_ID", criteriaVO.getPrdId());
        }
        if( criteriaVO.getId()!=null ){
            sql.append("AND S.ID=#ID \n");
            params.put("ID", criteriaVO.getId());
        }

        if( criteriaVO.getOrderBy()!=null ){
            sql.append("ORDER BY ").append(criteriaVO.getOrderBy());
        }else{
            sql.append("ORDER BY S.SORTNUM, A.SORTNUM \n");
        }
        
        List<PrdAttrValVO> list = null;
        if( criteriaVO.getFirstResult()!=null && criteriaVO.getMaxResults()!=null ){
            list = this.selectBySql(PrdAttrValVO.class, sql.toString(), params, criteriaVO.getFirstResult(), criteriaVO.getMaxResults());
        }else if( criteriaVO.getMaxResults()!=null ){
            list = this.selectBySql(PrdAttrValVO.class, sql.toString(), params, 0, criteriaVO.getMaxResults());
        }else{
            list = this.selectBySql(PrdAttrValVO.class, sql.toString(), params);
        }
        return list;
    }
    
    /**
     * 依商品類別查詢
     * @param storeId
     * @param prdId
     * @return 
     */
    public List<PrdAttrValVO> findByPrd(Long storeId, Long prdId){
        BaseCriteriaVO criteriaVO = new BaseCriteriaVO();
        criteriaVO.setStoreId(storeId);
        criteriaVO.setPrdId(prdId);
        return findByCriteria(criteriaVO);
    }
    
    public PrdAttrValVO findById(Long storeId, Long prdId, Long id){
        BaseCriteriaVO criteriaVO = new BaseCriteriaVO();
        criteriaVO.setStoreId(storeId);
        criteriaVO.setPrdId(prdId);
        criteriaVO.setId(id);
        List<PrdAttrValVO> list = findByCriteria(criteriaVO);
        
        return (list!=null && !list.isEmpty())?list.get(0):null;
    }

    public boolean checkInput(EcPrdAttrVal entity, EcMember member, Locale locale, List<String> errors) {
        boolean pass = true;
        pass = inputCheckFacade.checkInput(entity, locale, errors);

        return pass;
    }
}
