/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.facade.rfq;

import com.tcci.cm.exception.CmCustomException;
import com.tcci.cm.facade.AbstractFacade;
import com.tcci.cm.util.ExtBeanUtils;
import com.tcci.et.model.rfq.QuotationVO;
import com.tcci.et.entity.EtQuotation;
import com.tcci.et.entity.EtQuotationItem;
import com.tcci.et.entity.EtQuotationPm;
import com.tcci.et.enums.QuoteStatusEnum;
import com.tcci.et.model.criteria.RfqCriteriaVO;
import com.tcci.et.model.rfq.QuotationItemVO;
import com.tcci.et.model.rfq.QuotationPmVO;
import com.tcci.et.model.rfq.RfqVO;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.AttachmentFacade;
import com.tcci.fc.vo.AttachmentVO;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Peter.pan
 */
@Stateless
public class EtQuotationFacade extends AbstractFacade<EtQuotation> {
    @EJB EtRfqVenderFacade rfqVenderFacade;
    @EJB EtQuotationItemFacade quotationItemFacade;
    @EJB EtQuotationPmFacade quotationPmFacade;
    @EJB AttachmentFacade attachmentFacade;

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
        ExtBeanUtils.copyProperties(vo, entity);// return ID
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

        // 指定報價截止時間的最後報價
        if( criteriaVO.getEndAt()!=null ){
            sql.append("JOIN ( \n");
            sql.append("    SELECT TENDER_ID, RFQ_ID, RFQ_VENDER_ID, MAX(TIMES) TIMES \n");
            sql.append("    FROM ET_QUOTATION \n"); 
            sql.append("    WHERE 1=1 \n");
            sql.append("    AND QUOTETIME <= #QUOTETIME \n");
            //sql.append("  AND QUOTETIME <= TO_DATE('2019-07-8 13:00:00', 'yyyy-MM-dd HH24:MI:SS') \n");
            sql.append("    GROUP BY TENDER_ID, RFQ_ID, RFQ_VENDER_ID \n");
            sql.append(") LT ON LT.TENDER_ID=S.TENDER_ID AND LT.RFQ_ID=S.RFQ_ID AND LT.RFQ_VENDER_ID=S.RFQ_VENDER_ID AND LT.TIMES=S.TIMES \n");
            
            params.put("QUOTETIME", criteriaVO.getEndAt());
        }

        sql.append("JOIN ET_RFQ_VENDER RV ON RV.DISABLED=0 AND RV.ID=S.RFQ_VENDER_ID \n");// 投標供應商
        sql.append("JOIN ET_VENDER VA ON VA.ID=RV.VENDER_ID \n");// 供應商
        sql.append("WHERE 1=1 \n");
        sql.append("AND S.DISABLED=0 \n");
        sql.append("AND RV.DISABLED=0 \n");
        sql.append("AND VA.DISABLED=0 \n");
        
        if( criteriaVO.getTenderId()!=null ){// 標案
            sql.append("AND S.TENDER_ID=#TENDER_ID \n");
            params.put("TENDER_ID", criteriaVO.getTenderId());
        }
        if( criteriaVO.getRfqId()!=null ){// 詢價單
            sql.append("AND S.RFQ_ID=#RFQ_ID \n");
            params.put("RFQ_ID", criteriaVO.getRfqId());
        }
        if( criteriaVO.getRfqVenderId()!=null ){// 投標供應商
            sql.append("AND S.RFQ_VENDER_ID=#RFQ_VENDER_ID \n");
            params.put("RFQ_VENDER_ID", criteriaVO.getRfqVenderId());
        }
        if( criteriaVO.getLastQuote()!=null && criteriaVO.getLastQuote() ){// 最終報價 (廠商確認)
            sql.append("AND S.TIMES=RV.LAST_QUOTE \n");
        }
        if( criteriaVO.getQuoteTimes()!=null ){// 第幾次報價
            sql.append("AND S.TIMES=#TIMES \n");
            params.put("TIMES", criteriaVO.getQuoteTimes());
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
        sql.append(", RV.VENDER_ID \n");
        sql.append(", VA.NAME VENDER_NAME, VA.LIFNR, VA.LIFNR_UI, VA.APPLY_ID \n");
        sql.append(", CASE WHEN S.TIMES=RV.LAST_QUOTE THEN 1 ELSE 0 END LAST \n");
        
        sql.append(findByCriteriaSQL(criteriaVO, params));
        
        if( criteriaVO.getOrderBy()!=null ){
            sql.append("ORDER BY ").append(criteriaVO.getOrderBy());
        }else{
            sql.append("ORDER BY S.ID DESC");
        }

        return this.selectBySql(QuotationVO.class, sql.toString(), params, criteriaVO.getFirstResult(), criteriaVO.getMaxResults());
    }

    /**
     * 投標次數
     * @param tenderId
     * @param rfqId
     * @param rfqVenderId
     * @return 
     */
    public int getQuotationTimes(Long tenderId, Long rfqId, Long rfqVenderId){
        RfqCriteriaVO criteriaVO = new RfqCriteriaVO();
        criteriaVO.setTenderId(tenderId);
        criteriaVO.setRfqId(rfqId);
        criteriaVO.setRfqVenderId(rfqVenderId);

        return countByCriteria(criteriaVO);
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
     *  依標案詢價單 最終報價 查詢
     * @param tenderId
     * @param rfqId
     * @return 
     */
    public List<QuotationVO> findLastByRfq(Long tenderId, Long rfqId){
        RfqCriteriaVO criteriaVO = new RfqCriteriaVO();
        criteriaVO.setTenderId(tenderId);
        criteriaVO.setRfqId(rfqId);
        criteriaVO.setLastQuote(Boolean.TRUE);
        
        return findByCriteria(criteriaVO);
    }
    
    /**
     * 報價廠商查詢
     * @param tenderId
     * @param rfqId
     * @param rfqVenderId
     * @return 
     */
    public List<QuotationVO> findByRfqVender(Long tenderId, Long rfqId, Long rfqVenderId){
        RfqCriteriaVO criteriaVO = new RfqCriteriaVO();
        criteriaVO.setTenderId(tenderId);
        criteriaVO.setRfqId(rfqId);
        criteriaVO.setRfqVenderId(rfqVenderId);
        criteriaVO.setOrderBy("S.TIMES");
        
        List<QuotationVO> list = findByCriteria(criteriaVO);
        
        return list;
    }
    
    /**
     * 廠商最後報價查詢
     * @param tenderId
     * @param rfqId
     * @param rfqVenderId
     * @return 
     */
    public QuotationVO findByRfqVenderLast(Long tenderId, Long rfqId, Long rfqVenderId){
        RfqCriteriaVO criteriaVO = new RfqCriteriaVO();
        criteriaVO.setTenderId(tenderId);
        criteriaVO.setRfqId(rfqId);
        criteriaVO.setRfqVenderId(rfqVenderId);
        criteriaVO.setLastQuote(true);
        
        List<QuotationVO> list = findByCriteria(criteriaVO);
        
        return (sys.isEmpty(list))?null:list.get(0);
    }
    
    /**
     * 指定報價截止時間的最後報價
     * @param tenderId
     * @param rfqId
     * @param endTime
     * @param rfqVenderId
     * @return 
     */
    public List<QuotationVO> findByRfqVenderLastByTime(Long tenderId, Long rfqId, Date endTime, Long rfqVenderId){
        RfqCriteriaVO criteriaVO = new RfqCriteriaVO();
        criteriaVO.setTenderId(tenderId);
        criteriaVO.setRfqId(rfqId);
        criteriaVO.setRfqVenderId(rfqVenderId);
        criteriaVO.setEndDate(endTime);
        
        List<QuotationVO> list = findByCriteria(criteriaVO);
        return list;
    }

    /**
     * 完整報價儲存
     * @param rfqVO
     * @param quoteVO
     * @param quoteList // 包含所有詢價項目，不管有無報價
     * @param operator
     * @param simulated
     * @return 
     * @throws com.tcci.cm.exception.CmCustomException 
     */
    public boolean saveQuote(RfqVO rfqVO, QuotationVO quoteVO, List<QuotationItemVO> quoteList, TcUser operator, boolean simulated) throws CmCustomException {
        if( rfqVO==null || quoteVO==null || quoteList==null ){
            throw new CmCustomException("saveByRfq rfqVO==null || quoteVO==null || quoteList==null");
        }
        logger.debug("saveByRfq rfqVO = "+rfqVO.getRfqId()+", quoteVO = "+quoteVO.getId());
        
        // ET_QUOTATION
        EtQuotation entity = sys.isId(quoteVO.getId())?this.find(quoteVO.getId()):new EtQuotation();
        if( entity==null ){
            throw new CmCustomException("saveByRfq EtQuotation entity==null");
        }
        
        // 新報價 - 第幾次報價
        if( entity.getTimes() == null ){
            int times = getQuotationTimes(quoteVO.getTenderId(), quoteVO.getRfqId(), quoteVO.getRfqVenderId());
            quoteVO.setTimes(times+1);
        }
        ExtBeanUtils.copyProperties(entity, quoteVO);
        this.saveVO(quoteVO, operator, simulated);// save ET_QUOTATION

        if( QuoteStatusEnum.CONFIRM == quoteVO.getStatusEnum() ){// 確認報價
            // 變更 ET_RFQ_VENDER.LAST_QUOTE
            rfqVenderFacade.setLastQuote(quoteVO.getRfqVenderId(), quoteVO.getTimes(), true);
        }
        
        Long quoteId = quoteVO.getId();
        logger.debug("saveByRfq quoteId = "+quoteId);
        if( !sys.isId(quoteId) ){
            throw new CmCustomException("saveByRfq !sys.isId(quoteId)");
        }
        
        // ET_QUOTATION_ITEM & ET_QUOTATION_PM
        for(QuotationItemVO vo : quoteList){
            vo.setQuoteId(quoteId);
            if( vo.isSelected() ){// 新增/修改
                if( vo.getId()!=null ){// 修改
                    // ET_QUOTATION_ITEM
                    EtQuotationItem itemEntity = (vo.getId()!=null)?quotationItemFacade.find(vo.getId()):new EtQuotationItem();
                    if( itemEntity==null ){
                        throw new CmCustomException("saveByRfq itemEntity==null");
                    }
                    ExtBeanUtils.copyProperties(itemEntity, vo);
                    quotationItemFacade.save(itemEntity, operator, simulated);

                    if( vo.isSrvItem() ){// 服務類
                        if( vo.getPmList()!=null ){// 空值表示本次無修改明細
                            for(QuotationPmVO pm : vo.getPmList()){// ET_QUOTATION_PM
                                EtQuotationPm pmEntity = (pm.getId()!=null)?quotationPmFacade.find(pm.getId()):new EtQuotationPm();
                                if( pmEntity!=null ){
                                    ExtBeanUtils.copyProperties(pmEntity, pm);
                                    pmEntity.setQuoteId(quoteId);
                                    quotationPmFacade.save(pmEntity, operator, simulated);
                                }
                            }
                        }
                    }
                }else{// 新增
                    // ET_QUOTATION_ITEM
                    EtQuotationItem itemEntity = new EtQuotationItem();
                    ExtBeanUtils.copyProperties(itemEntity, vo);
                    quotationItemFacade.save(itemEntity, operator, simulated);
                    if( vo.isSrvItem() ){// 服務類
                        if( sys.isEmpty(vo.getPmList()) ){// 新增不可能為空
                            throw new CmCustomException("saveByRfq sys.isEmpty(vo.getPmList())");
                        }
                        for(QuotationPmVO pm : vo.getPmList()){// ET_QUOTATION_PM
                            EtQuotationPm pmEntity = new EtQuotationPm();
                            ExtBeanUtils.copyProperties(pmEntity, pm);
                            pmEntity.setQuoteId(quoteId);
                            quotationPmFacade.save(pmEntity, operator, simulated);
                        }
                    }
                }
            }else{// (若存在)刪除
                if( vo.getId()!=null ){
                    if( vo.isSrvItem() ){// 服務類
                        quotationPmFacade.removeByQuoteItem(vo, simulated);// ET_QUOTATION_PM
                    }
                    quotationItemFacade.remove(vo.getId(), simulated);// ET_QUOTATION_ITEM
                }
            }
        }
        
        return true;
    }
    
    /**
     * QuotationVO 關聯檔案
     * @param vo
     * @return 
     */
    public List<AttachmentVO> findFiles(QuotationVO vo){
        EtQuotation entity = new EtQuotation();
        ExtBeanUtils.copyProperties(entity, vo);

        List<AttachmentVO> files = attachmentFacade.loadContent(entity);// 關聯上傳檔
        return files;
    }
}
