/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade;

import com.tcci.cm.util.NativeSQLUtils;
import com.tcci.ec.entity.EcMember;
import com.tcci.ec.entity.EcPrdIntro;
import com.tcci.ec.model.criteria.BaseCriteriaVO;
import com.tcci.ec.model.PrdIntroVO;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Peter.pan
 */
@Stateless
public class EcPrdIntroFacade extends AbstractFacade<EcPrdIntro> {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EcPrdIntroFacade() {
        super(EcPrdIntro.class);
    }
    
    /**
     * 單筆儲存
     * @param entity
     * @param operator
     * @param simulated
     */
    public void save(EcPrdIntro entity, EcMember operator, boolean simulated){
        if( entity!=null ){
            // default while null 
            if( entity.getSortnum()==null ){ entity.setSortnum(0); } 

            if( entity.getId()!=null && entity.getId()>0 ){
                entity.setModifier(operator);
                entity.setModifytime(new Date());
                this.edit(entity, simulated);
                logger.info("save update "+entity);
            }else{
                entity.setCreator(operator);
                entity.setCreatetime(new Date());
                this.create(entity, simulated);
                logger.info("save new "+entity);
            }
        }
    }
    
    /**
     * 依商品查詢
     * @param criteriaVO
     * @return 
     */
    public List<PrdIntroVO> findByCriteria(BaseCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT S.* \n");
        sql.append("FROM EC_PRD_INTRO S \n");
        sql.append("WHERE 1=1 \n");
        sql.append("AND S.STORE_ID=#STORE_ID \n");
        
        params.put("STORE_ID", criteriaVO.getStoreId());
        
        if( criteriaVO.getPrdId()!=null ){
            sql.append("AND S.PRD_ID=#PRD_ID \n");
            params.put("PRD_ID", criteriaVO.getPrdId());
        }else if( criteriaVO.getPrdList()!=null && !criteriaVO.getPrdList().isEmpty() ){
            sql.append(NativeSQLUtils.getInSQL("S.PRD_ID", criteriaVO.getPrdList(), params)).append(" \n");
        }
        
        if( criteriaVO.getOrderBy()!=null ){
            sql.append("ORDER BY ").append(criteriaVO.getOrderBy());
        }else{
            sql.append("ORDER BY S.PRD_ID, S.SORTNUM, S.CREATETIME");
        }
        
        List<PrdIntroVO> list = null;
        if( criteriaVO.getFirstResult()!=null && criteriaVO.getMaxResults()!=null ){
            list = this.selectBySql(PrdIntroVO.class, sql.toString(), params, criteriaVO.getFirstResult(), criteriaVO.getMaxResults());
        }else if( criteriaVO.getMaxResults()!=null ){
            list = this.selectBySql(PrdIntroVO.class, sql.toString(), params, 0, criteriaVO.getMaxResults());
        }else{
            list = this.selectBySql(PrdIntroVO.class, sql.toString(), params);
        }
        return list;
    }
    
    public List<PrdIntroVO> findByPrd(Long storeId, Long prdId){
        if( storeId==null || prdId==null ){
            logger.error("findByPrd error storeId="+storeId+", prdId="+prdId);
            return null;
        }
        BaseCriteriaVO criteriaVO = new BaseCriteriaVO();
        criteriaVO.setStoreId(storeId);
        criteriaVO.setPrdId(prdId);
        
        return findByCriteria(criteriaVO);
    }

    /**
     * 多筆刪除
     * @param storeId
     * @param prdId
     * @param idList
     * @param include 
     */
    public void batchRemove(Long storeId, Long prdId, List<Long> idList, boolean include) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("BEGIN \n");

        sql.append("DELETE FROM EC_PRD_INTRO \n");
        sql.append("WHERE 1=1 \n");
        sql.append("AND STORE_ID=#STORE_ID \n");
        sql.append("AND PRD_ID=#PRD_ID; \n");

        params.put("STORE_ID", storeId);
        params.put("PRD_ID", prdId);        
        
        if( idList!=null && !idList.isEmpty() ){
            if( include ){
                sql.append(NativeSQLUtils.getInSQL("ID", idList, params));
            }else{
                sql.append(NativeSQLUtils.getNotInSQL("ID", idList, params));
            }
        }
        sql.append("END; \n");
        
        logger.debug("batchRemove sql =\n"+sql.toString());
        Query q = em.createNativeQuery(sql.toString());
        setParamsToQuery("batchRemove", params, q);
        
        q.executeUpdate();
    }

    public boolean checkInput(EcPrdIntro entity, EcMember member, Locale locale, List<String> errors) {
        boolean pass = true;
        pass = inputCheckFacade.checkInput(entity, locale, errors);

        return pass;
    }
}
