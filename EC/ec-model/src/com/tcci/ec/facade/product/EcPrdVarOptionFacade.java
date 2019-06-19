/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade.product;

import com.tcci.cm.util.NativeSQLUtils;
import com.tcci.ec.entity.EcMember;
import com.tcci.ec.entity.EcPrdVarOption;
import com.tcci.ec.enums.ProductVariantEnum;
import com.tcci.ec.facade.AbstractFacade;
import com.tcci.ec.model.criteria.BaseCriteriaVO;
import com.tcci.ec.model.LongOptionVO;
import com.tcci.ec.model.PrdVarOptionVO;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Peter.pan
 */
@Stateless
public class EcPrdVarOptionFacade extends AbstractFacade<EcPrdVarOption> {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EcPrdVarOptionFacade() {
        super(EcPrdVarOption.class);
    }
    
    /**
     * 單筆儲存
     * @param entity
     * @param operator
     * @param simulated
     */
//    public void save(EcPrdVarOption entity, EcMember operator, boolean simulated){
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
    public void save(EcPrdVarOption entity) {
        if (entity.getId() == null) {
            em.persist(entity);
        } else {
            em.merge(entity);
        }
    }
    
    /**
     * 依商品查詢
     * @param criteriaVO
     * @return 
     */
    public List<PrdVarOptionVO> findByCriteria(BaseCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT S.* \n");
        sql.append("FROM EC_PRD_VAR_OPTION S \n");
        sql.append("WHERE 1=1 \n");
        sql.append("AND S.DISABLED=0 \n");
        sql.append("AND S.STORE_ID=#STORE_ID \n");
        
        params.put("STORE_ID", criteriaVO.getStoreId());
        
        if( criteriaVO.getPrdId()!=null ){
            sql.append("AND S.PRD_ID=#PRD_ID \n");
            params.put("PRD_ID", criteriaVO.getPrdId());
        }else if( criteriaVO.getPrdList()!=null && !criteriaVO.getPrdList().isEmpty() ){
            sql.append(NativeSQLUtils.getInSQL("S.PRD_ID", criteriaVO.getPrdList(), params)).append(" \n");
        }
        // ProductVariantEnum
        if( StringUtils.isNotBlank(criteriaVO.getType()) ){
            sql.append("AND S.TYPE=#TYPE \n");
            params.put("TYPE", criteriaVO.getType());
        }
        
        if( criteriaVO.getOrderBy()!=null ){
            sql.append("ORDER BY ").append(criteriaVO.getOrderBy());
        }else{
            sql.append("ORDER BY S.PRD_ID, S.SORTNUM, S.CREATETIME");
        }
        
        List<PrdVarOptionVO> list = null;
        if( criteriaVO.getFirstResult()!=null && criteriaVO.getMaxResults()!=null ){
            list = this.selectBySql(PrdVarOptionVO.class, sql.toString(), params, criteriaVO.getFirstResult(), criteriaVO.getMaxResults());
        }else if( criteriaVO.getMaxResults()!=null ){
            list = this.selectBySql(PrdVarOptionVO.class, sql.toString(), params, 0, criteriaVO.getMaxResults());
        }else{
            list = this.selectBySql(PrdVarOptionVO.class, sql.toString(), params);
        }
        return list;
    }
    
    public List<PrdVarOptionVO> findByPrd(Long storeId, Long prdId, String type){
        if( storeId==null || prdId==null ){
            logger.error("findByPrd error storeId="+storeId+", prdId="+prdId);
            return null;
        }
        BaseCriteriaVO criteriaVO = new BaseCriteriaVO();
        criteriaVO.setStoreId(storeId);
        criteriaVO.setPrdId(prdId);
        criteriaVO.setType(type);
        
        return findByCriteria(criteriaVO);
    }

    public List<LongOptionVO> findVarOptions(Long storeId, Long prdId, ProductVariantEnum productVariantEnum) {
        List<PrdVarOptionVO> list = findByPrd(storeId, prdId, productVariantEnum.getCode());
        List<LongOptionVO> ops = new ArrayList<LongOptionVO>();
        if( list!=null ){
            for(PrdVarOptionVO vo : list){
                ops.add(new LongOptionVO(vo.getId(), vo.getCname()));
            }
        }
        return ops;
    }

    public boolean checkInput(EcPrdVarOption entity, EcMember member, Locale locale, List<String> errors) {
        boolean pass = true;
        pass = inputCheckFacade.checkInput(entity, locale, errors);

        return pass;
    }
    
}
