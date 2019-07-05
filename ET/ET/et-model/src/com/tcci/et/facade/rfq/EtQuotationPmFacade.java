/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.facade.rfq;

import com.tcci.cm.facade.AbstractFacade;
import com.tcci.cm.util.ExtBeanUtils;
import com.tcci.et.model.rfq.QuotationVO;
import com.tcci.et.entity.EtQuotationPm;
import com.tcci.et.model.rfq.QuotationPmVO;
import com.tcci.et.model.criteria.RfqCriteriaVO;
import com.tcci.fc.entity.org.TcUser;
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
public class EtQuotationPmFacade extends AbstractFacade<EtQuotationPm> {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EtQuotationPmFacade() {
        super(EtQuotationPm.class);
    }
    
    public void remove(QuotationVO vo, boolean simulated) {       
        EtQuotationPm entity = findByVO(vo);
        
        if( entity!=null ){
            this.remove(entity, simulated);
        }
    }

    public EtQuotationPm findByVO(QuotationVO vo){
        EtQuotationPm entity = this.find(vo.getId());
        return entity;
    }
    /**
     * 單筆儲存
     * @param entity
     * @param operator
     * @param simulated
     */
    public void save(EtQuotationPm entity, TcUser operator, boolean simulated){
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
        EtQuotationPm entity = (vo.getId()!=null)?this.find(vo.getId()):new EtQuotationPm();
        
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
        sql.append("FROM ET_RFQ_PM R \n");
        sql.append("LEFT OUTER JOIN ET_QUOTATION_PM S ON S.RFQ_ID=R.RFQ_ID AND S.EBELP=R.EBELP AND S.EXTROW=R.EXTROW \n");
        //sql.append("LEFT OUTER JOIN ET_QUOTATION_ITEM QI ON QI.ID=S.QUO_ID AND S.EBELP=R.EBELP \n");
        //sql.append("LEFT OUTER JOIN ET_QUOTATION Q ON Q.ID=QI.QUO_ID \n");
        //sql.append("LEFT OUTER JOIN TC_USER UC ON UC.ID=S.CREATOR \n");
        //sql.append("LEFT OUTER JOIN TC_USER UM ON UM.ID=S.MODIFIER \n");
        sql.append("WHERE 1=1 \n");
        
        if( criteriaVO.getTenderId()!=null ){
            sql.append("AND R.TENDER_ID=#TENDER_ID \n");
            params.put("TENDER_ID", criteriaVO.getTenderId());
        }
        if( criteriaVO.getRfqId()!=null ){
            sql.append("AND R.RFQ_ID=#RFQ_ID \n");
            params.put("RFQ_ID", criteriaVO.getRfqId());
        }
        if( criteriaVO.getEbelp()!=null ){
            sql.append("AND R.EBELP=#EBELP \n");
            params.put("EBELP", criteriaVO.getEbelp());
        }
        if( criteriaVO.getQuoteId()!=null ){
            sql.append("AND R.EXTROW=#EXTROW \n");
            params.put("EXTROW", criteriaVO.getQuoteId());
        }
        
        // 有報價
        if( criteriaVO.getQuoteId()!=null ){
            sql.append("AND S.QUO_ID=#QUO_ID \n");
            params.put("QUO_ID", criteriaVO.getQuoteId());
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
    public List<QuotationPmVO> findByCriteria(RfqCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        // ET_RFQ_PM
        sql.append("SELECT R.TENDER_ID, R.RFQ_ID, R.MANDT, R.EBELN, R.EBELP, R.EXTROW, R.KTEXT1, R.MENGE, R.MEINS, R.WAERS \n");
        sql.append(", R.TBTWR RFQ_TBTWR, R.NETWR RFQ_NETWR \n");
        // ET_QUOTATION_PM
        sql.append(", S.TBTWR, S.NETWR \n");
        
        sql.append(findByCriteriaSQL(criteriaVO, params));
        
        if( criteriaVO.getOrderBy()!=null ){
            sql.append("ORDER BY ").append(criteriaVO.getOrderBy());
        }else{
            sql.append("ORDER BY R.EBELP");
        }
        
        List<QuotationPmVO> list = this.selectBySql(QuotationPmVO.class, sql.toString(), params, criteriaVO.getFirstResult(), criteriaVO.getMaxResults());
        return list;
    }

    /**
     * 依 ID 查詢
     * @param id
     * @param fullData
     * @return 
     */
    public QuotationPmVO findById( Long id, boolean fullData) {
        RfqCriteriaVO criteriaVO = new RfqCriteriaVO();
        criteriaVO.setId(id);
        criteriaVO.setFullData(fullData);
        
        List<QuotationPmVO> list = findByCriteria(criteriaVO);
        return (list!=null && !list.isEmpty())? list.get(0):null;
    }
    
    /**
     * 依標案詢價單查詢
     * @param tenderId
     * @param rfqId
     * @return 
     */
    public List<QuotationPmVO> findByRfq(Long tenderId, Long rfqId){
        RfqCriteriaVO criteriaVO = new RfqCriteriaVO();
        criteriaVO.setTenderId(tenderId);
        criteriaVO.setRfqId(rfqId);
        
        return findByCriteria(criteriaVO);
    }
    
    /**
     * 依詢價單項目查詢
     * @param tenderId
     * @param rfqId
     * @param ebelp
     * @return 
     */
    public List<QuotationPmVO> findByRfqItem(Long tenderId, Long rfqId, Long ebelp){
        RfqCriteriaVO criteriaVO = new RfqCriteriaVO();
        criteriaVO.setTenderId(tenderId);
        criteriaVO.setRfqId(rfqId);
        criteriaVO.setEbelp(ebelp);
        
        return findByCriteria(criteriaVO);
    }
    
}
