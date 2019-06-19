/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade;

import com.tcci.ec.entity.EcMember;
import com.tcci.ec.entity.EcPayment;
import com.tcci.ec.enums.PayMethodEnum;
import com.tcci.ec.enums.ShipMethodEnum;
import com.tcci.ec.model.criteria.BaseCriteriaVO;
import com.tcci.ec.model.rs.LongOptionVO;
import com.tcci.ec.model.PaymentVO;
import java.util.ArrayList;
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
public class EcPaymentFacade extends AbstractFacade<EcPayment> {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EcPaymentFacade() {
        super(EcPayment.class);
    }
    
    /**
     * 單筆儲存
     * @param entity
     * @param operator
     * @param simulated
     */
    public void save(EcPayment entity, EcMember operator, boolean simulated){
        if( entity!=null ){
            // default while null 
            if( entity.getDisabled()==null ){ entity.setDisabled(false); }
            if( entity.getActive()==null ){ entity.setActive(true); }
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
     * @param criteriaVO
     * @return 
     */
    public List<PaymentVO> findByCriteria(BaseCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<String, Object>();
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
    
    public List<LongOptionVO> findPaymentOptions(Long storeId, Locale locale){
        BaseCriteriaVO criteriaVO = new BaseCriteriaVO();
        criteriaVO.setStoreId(storeId);
        List<PaymentVO> list = findByCriteria(criteriaVO);
        
        List<LongOptionVO> ops = new ArrayList<LongOptionVO>();
        if( list!=null ){
            for(PaymentVO vo : list){
                if( locale!=null && vo.getCode()!=null ){
                    PayMethodEnum theEnum = PayMethodEnum.getFromCode(vo.getCode());
                    if( theEnum!=null ){
                        vo.setTitle(theEnum.getDisplayName(locale));
                    }
                }
                ops.add(new LongOptionVO(vo.getId(), vo.getTitle()));
            }
        }
        return ops;
    }

    public List<EcPayment> findStorePayments(Long storeId){
        Map<String, Object> params = new HashMap<String, Object>();
        String sql = "SELECT e FROM EcPayment e WHERE e.storeId = :storeId AND e.disabled = 0";
        params.put("storeId", storeId);
        
        return this.findByJPQLQuery(sql, params);
    }
    
    public List<PaymentVO> findByStore(Long storeId) {
        BaseCriteriaVO criteriaVO = new BaseCriteriaVO();
        criteriaVO.setStoreId(storeId);
        List<PaymentVO> list = findByCriteria(criteriaVO);
        return list;
    }

    public EcPayment genByEnumCode(Long storeId, String code, Integer sortnum, Locale locale){
        PayMethodEnum payEnum = PayMethodEnum.getFromCode(code);
        if( payEnum!=null ){
            return genByEnum(storeId, payEnum, sortnum, locale);
        }else{
            logger.error("genByEnumCode error payEnum = null, code = "+code);
        }
        return null;
    }
    public EcPayment genByEnum(Long storeId, PayMethodEnum payEnum, Integer sortnum, Locale locale){
        EcPayment entity = null;
        if( payEnum!=null ){
            entity = new EcPayment();
            entity.setActive(Boolean.TRUE);
            entity.setCode(payEnum.getCode());
            entity.setDisabled(Boolean.FALSE);
            entity.setSortnum(sortnum);
            entity.setStoreId(storeId);
            entity.setTitle(payEnum.getDisplayName(locale));
            entity.setType(payEnum.getType());
        }else{
            logger.error("genByEnumCode error payEnum = null");
        }
        return entity;
    }
    
    public PaymentVO findByKey(Long storeId, String code) {
        BaseCriteriaVO criteriaVO = new BaseCriteriaVO();
        criteriaVO.setStoreId(storeId);
        criteriaVO.setCode(code);
        List<PaymentVO> list = findByCriteria(criteriaVO);
        
        PaymentVO vo = (list!=null && !list.isEmpty())?list.get(0):null;
        return vo;
    }

    public boolean checkInput(EcPayment entity, EcMember member, Locale locale, List<String> errors) {
        boolean pass = true;
        pass = inputCheckFacade.checkInput(entity, locale, errors);

        return pass;
    }
    
}
