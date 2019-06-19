/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade;

import com.tcci.ec.entity.EcMember;
import com.tcci.ec.entity.EcCurrency;
import com.tcci.ec.model.rs.LongOptionVO;
import com.tcci.ec.model.CurrencyVO;
import com.tcci.ec.model.criteria.BaseCriteriaVO;
import com.tcci.fc.util.StringUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Peter.pan
 */
@Stateless
public class EcCurrencyFacade extends AbstractFacade<EcCurrency> {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EcCurrencyFacade() {
        super(EcCurrency.class);
    }
    
    /**
     * 單筆儲存
     * @param entity
     * @param operator
     * @param simulated
     */
    public void save(EcCurrency entity, EcMember operator, boolean simulated){
        if( entity!=null ){
            // default while null 
            if( entity.getDisabled()==null ){ entity.setDisabled(false); }

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
     * @param criteriaVO
     * @return 
     */
    public List<CurrencyVO> findByCriteria(BaseCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT S.* \n");
        sql.append("FROM EC_CURRENCY S \n");
        sql.append("WHERE 1=1 \n");
        sql.append("AND DISABLED=0 \n");

        /*if( criteriaVO.getStoreId()!=null ){
            sql.append("AND S.STORE_ID=#STORE_ID \n");       
            params.put("STORE_ID", criteriaVO.getStoreId());
        }*/

        if( criteriaVO.getOrderBy()!=null ){
            sql.append("ORDER BY ").append(criteriaVO.getOrderBy());
        }else{
            sql.append("ORDER BY S.SORTNUM, S.TITLE \n");
        }
        
        List<CurrencyVO> list = null;
        if( criteriaVO.getFirstResult()!=null && criteriaVO.getMaxResults()!=null ){
            list = this.selectBySql(CurrencyVO.class, sql.toString(), params, criteriaVO.getFirstResult(), criteriaVO.getMaxResults());
        }else if( criteriaVO.getMaxResults()!=null ){
            list = this.selectBySql(CurrencyVO.class, sql.toString(), params, 0, criteriaVO.getMaxResults());
        }else{
            list = this.selectBySql(CurrencyVO.class, sql.toString(), params);
        }
        return list;
    }

    public List<LongOptionVO> findCurrencyOptions(Long storeId, String opLang) {
        BaseCriteriaVO criteriaVO = new BaseCriteriaVO();
        criteriaVO.setStoreId(storeId);
        List<CurrencyVO> list = findByCriteria(criteriaVO);
        
        List<LongOptionVO> ops = new ArrayList<LongOptionVO>();
        if( list!=null ){
            for(CurrencyVO vo : list){
                // use cname or ename by opLang (C/E)
                String name = "E".equals(opLang)?(StringUtils.isBlank(vo.getCode())?vo.getName():vo.getCode()):vo.getName();
                ops.add(new LongOptionVO(vo.getId(), name));
            }
        }
        return ops;
    }
    
}
