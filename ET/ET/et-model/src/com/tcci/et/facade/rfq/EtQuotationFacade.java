/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.facade.rfq;

import com.tcci.cm.facade.AbstractFacade;
import com.tcci.cm.util.ExtBeanUtils;
import com.tcci.et.model.rfq.QuotationVO;
import com.tcci.et.entity.EtQuotation;
import com.tcci.et.model.criteria.RfqCriteriaVO;
import com.tcci.et.model.rfq.QuotationItemVO;
import com.tcci.et.model.rfq.RfqVO;
import com.tcci.fc.entity.org.TcUser;
import java.math.BigDecimal;
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
public class EtQuotationFacade extends AbstractFacade<EtQuotation> {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EtQuotationFacade() {
        super(EtQuotation.class);
    }
    
    public void remove(QuotationVO vo, boolean simulated) {       
        EtQuotation entity = findByVO(vo);
        
        if( entity!=null ){
            this.remove(entity, simulated);
        }
    }

    public EtQuotation findByVO(QuotationVO vo){
        EtQuotation entity = this.find(vo.getId());
        return entity;
    }
    
    /**
     * 單筆儲存
     * @param entity
     * @param operator
     * @param simulated
     */
    public void save(EtQuotation entity, TcUser operator, boolean simulated){
        if( entity!=null ){
            // default while null 
           
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
    public void saveVO(QuotationVO vo, TcUser operator, boolean simulated){
        if( vo==null ){
            logger.error("saveVO vo==null");
            return;
        }
        EtQuotation entity = (vo.getId()!=null)?this.find(vo.getId()):new EtQuotation();
        
        ExtBeanUtils.copyProperties(entity, vo);
        save(entity, operator, simulated);
    }
    
    /**
     * 依輸入條件查詢 SQL
     * @param criteriaVO
     * @param params
     * @return 
     */
    public String findByCriteriaSQL(RfqCriteriaVO criteriaVO, Map<String, Object> params){
        StringBuilder sql = new StringBuilder();
        
        sql.append("FROM ET_QUOTATION S \n");
        sql.append("WHERE 1=1 \n");
        
        if( criteriaVO.getTenderId()!=null ){
            sql.append("AND S.TENDER_ID=#TENDER_ID \n");
            params.put("TENDER_ID", criteriaVO.getTenderId());
        }
        if( criteriaVO.getRfqId()!=null ){
            sql.append("AND S.RFQ_ID=#RFQ_ID \n");
            params.put("RFQ_ID", criteriaVO.getRfqId());
        }
        
        return sql.toString();
    }
    
    /**
     * 依輸入條件查詢筆數
     * @param criteriaVO
     * @return 
     */
    public int countByCriteria(RfqCriteriaVO criteriaVO) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(S.ID) COUNTS \n");
        sql.append(findByCriteriaSQL(criteriaVO, params));
        
        return this.count(sql.toString(), params);
    }
    
    /**
     * 依輸入條件查詢
     * @param criteriaVO
     * @return 
     */
    public List<QuotationVO> findByCriteria(RfqCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT S.* \n");
        
        sql.append(findByCriteriaSQL(criteriaVO, params));
        
        if( criteriaVO.getOrderBy()!=null ){
            sql.append("ORDER BY ").append(criteriaVO.getOrderBy());
        }else{
            sql.append("ORDER BY ID DESC");
        }
        
        List<QuotationVO> list = null;
        list = this.selectBySql(QuotationVO.class, sql.toString(), params, criteriaVO.getFirstResult(), criteriaVO.getMaxResults());
        return list;
    }

    /**
     * 依 ID 查詢
     * @param id
     * @param fullData
     * @return 
     */
    public QuotationVO findById(Long id, boolean fullData) {
        RfqCriteriaVO criteriaVO = new RfqCriteriaVO();
        criteriaVO.setId(id);
        criteriaVO.setFullData(fullData);
        
        List<QuotationVO> list = findByCriteria(criteriaVO);
        return (list!=null && !list.isEmpty())? list.get(0):null;
    }
    
    /**
     *  依標案詢價單查詢
     * @param tenderId
     * @param rfqId
     * @return 
     */
    public List<QuotationVO> findByRfq(Long tenderId, Long rfqId){
        RfqCriteriaVO criteriaVO = new RfqCriteriaVO();
        criteriaVO.setTenderId(tenderId);
        criteriaVO.setRfqId(rfqId);
        
        return findByCriteria(criteriaVO);
    }

    /**
     * 完整報價儲存
     * @param rfqVO
     * @param quoteVO
     * @param quoteList
     * @param operator
     * @param simulated
     */
    public void saveByRfq(RfqVO rfqVO, QuotationVO quoteVO, List<QuotationItemVO> quoteList, TcUser operator, boolean simulated) {
        if( rfqVO==null || quoteList==null ){
            logger.error("saveByRfq rfqVO==null || quoteList==null");
            return;
        }
        logger.debug("saveByRfq rfqVO = "+rfqVO.getTenderId());
        
        // ET_QUOTATION
        EtQuotation entity = (quoteVO!=null && sys.isId(quoteVO.getId()))?this.find(quoteVO.getId()):new EtQuotation();
        if( entity==null ){
            logger.error("saveByRfq EtQuotation entity==null");
        }
        /*
        entity.setCurQuo(curQuo);
        entity.setCurRfq(curRfq);
        entity.setDisabled(simulated);
        entity.setDiscount(BigDecimal.ONE);
        entity.setExRate(BigDecimal.ZERO);
        entity.setExpiretime(expiretime);
        entity.setInvoice(simulated);
        entity.setLast(simulated);
        entity.setMemberId(Long.MIN_VALUE);
        entity.setMemo(memo);
        entity.setNetAmtQuo(BigDecimal.ZERO);
        entity.setNetAmtRfq(BigDecimal.ZERO);
        entity.setQuotetime(quotetime);
        entity.setRfqId(Long.MIN_VALUE);
        entity.setRfqVenderId(Long.MIN_VALUE);
        entity.setStatue(statue);
        entity.setTaxRate(BigDecimal.ZERO);
        entity.setTaxQuo(BigDecimal.ZERO);
        entity.setTaxRfq(BigDecimal.ZERO);
        entity.setTenderId(Long.MIN_VALUE);
        entity.setTotalAmtQuo(BigDecimal.ZERO);
        entity.setTotalAmtRfq(BigDecimal.ZERO);
        */
    }
}
