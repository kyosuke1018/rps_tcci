/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.facade.rfq;

import com.tcci.cm.facade.AbstractFacade;
import com.tcci.cm.util.ExtBeanUtils;
import com.tcci.et.model.rfq.AwardVO;
import com.tcci.et.entity.EtAward;
import com.tcci.et.model.VenderAllVO;
import com.tcci.et.model.criteria.RfqCriteriaVO;
import com.tcci.et.model.rfq.AwardItemVO;
import com.tcci.fc.entity.org.TcUser;
import java.util.ArrayList;
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
public class EtAwardFacade extends AbstractFacade<EtAward> {
    @EJB EtRfqVenderFacade rfqVenderFacade;
    @EJB EtAwardItemFacade awardItemFacade;

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EtAwardFacade() {
        super(EtAward.class);
    }
    
    public void remove(AwardVO vo, boolean simulated) {       
        EtAward entity = findByVO(vo);
        
        if( entity!=null ){
            this.remove(entity, simulated);
        }
    }

    public EtAward findByVO(AwardVO vo){
        EtAward entity = this.find(vo.getId());
        return entity;
    }
    
    /**
     * 單筆儲存
     * @param entity
     * @param operator
     * @param simulated
     */
    public void save(EtAward entity, TcUser operator, boolean simulated){
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
    public void saveVO(AwardVO vo, TcUser operator, boolean simulated){
        if( vo==null ){
            logger.error("saveVO vo==null");
            return;
        }
        EtAward entity = (vo.getId()!=null)?this.find(vo.getId()):new EtAward();
        
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
        
        sql.append("FROM ET_AWARD S \n");
        sql.append("JOIN ET_QUOTATION Q ON Q.ID=S.QUOTE_ID \n");
        sql.append("JOIN ET_RFQ_VENDER RV ON RV.ID=Q.RFQ_VENDER_ID \n");
        sql.append("JOIN ET_VENDER_ALL VA ON VA.ID=RV.VENDER_ID \n");
        sql.append("WHERE 1=1 \n");
        sql.append("AND S.DISABLED=0 \n");
        sql.append("AND Q.DISABLED=0 \n");
        sql.append("AND RV.DISABLED=0 \n");
        sql.append("AND VA.DISABLED=0 \n");
        
        if( criteriaVO.getTenderId()!=null ){
            sql.append("AND S.TENDER_ID=#TENDER_ID \n");
            params.put("TENDER_ID", criteriaVO.getTenderId());
        }
        if( criteriaVO.getRfqId()!=null ){
            sql.append("AND S.RFQ_ID=#RFQ_ID \n");
            params.put("RFQ_ID", criteriaVO.getRfqId());
        }
        if( criteriaVO.getCode()!=null ){
            sql.append("AND S.CODE=#CODE \n");
            params.put("CODE", criteriaVO.getCode());
        }
        if( criteriaVO.getVenderId()!=null ){
            sql.append("AND S.VENDER_ID=#VENDER_ID \n");
            params.put("VENDER_ID", criteriaVO.getVenderId());
        }
       
        if( criteriaVO.getRfqVenderId()!=null ){
            sql.append("AND Q.RFQ_VENDER_ID=#RFQ_VENDER_ID \n");
            params.put("RFQ_VENDER_ID", criteriaVO.getRfqVenderId());
        }
        if( criteriaVO.getLastQuote()!=null && criteriaVO.getLastQuote() ){// 最終報價 (廠商確認)
            sql.append("AND Q.TIMES=RV.LAST_QUOTE \n");
        }
        if( criteriaVO.getQuoteTimes()!=null ){// 第幾次報價
            sql.append("AND Q.TIMES=#TIMES \n");
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
    public List<AwardVO> findByCriteria(RfqCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT S.* \n");
        sql.append(", VA.NAME, VA.LIFNR, VA.LIFNR_UI, VA.APPLY_ID \n");
        sql.append(", CASE WHEN S.TIMES=RV.LAST_QUOTE THEN 1 ELSE 0 END LAST \n");
        
        sql.append(findByCriteriaSQL(criteriaVO, params));
        
        if( criteriaVO.getOrderBy()!=null ){
            sql.append("ORDER BY ").append(criteriaVO.getOrderBy());
        }else{
            sql.append("ORDER BY S.ID DESC");
        }

        return this.selectBySql(AwardVO.class, sql.toString(), params, criteriaVO.getFirstResult(), criteriaVO.getMaxResults());
    }

    /**
     * 投標次數
     * @param tenderId
     * @param rfqId
     * @param rfqVenderId
     * @return 
     */
    public int getAwardTimes(Long tenderId, Long rfqId, Long rfqVenderId){
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
    public AwardVO findById(Long id, boolean fullData) {
        RfqCriteriaVO criteriaVO = new RfqCriteriaVO();
        criteriaVO.setId(id);
        criteriaVO.setFullData(fullData);
        
        List<AwardVO> list = findByCriteria(criteriaVO);
        return (list!=null && !list.isEmpty())? list.get(0):null;
    }
    
    /**
     *  依標案詢價單查詢
     * @param tenderId
     * @param rfqId
     * @return 
     */
    public List<AwardVO> findByRfq(Long tenderId, Long rfqId){
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
    public List<AwardVO> findLastByRfq(Long tenderId, Long rfqId){
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
    public List<AwardVO> findByRfqVender(Long tenderId, Long rfqId, Long rfqVenderId){
        RfqCriteriaVO criteriaVO = new RfqCriteriaVO();
        criteriaVO.setTenderId(tenderId);
        criteriaVO.setRfqId(rfqId);
        criteriaVO.setRfqVenderId(rfqVenderId);
        criteriaVO.setOrderBy("S.TIMES");
        
        List<AwardVO> list = findByCriteria(criteriaVO);
        
        return list;
    }
    
    /**
     * 廠商最後報價查詢
     * @param tenderId
     * @param rfqId
     * @param rfqVenderId
     * @return 
     */
    public AwardVO findByRfqVenderLast(Long tenderId, Long rfqId, Long rfqVenderId){
        RfqCriteriaVO criteriaVO = new RfqCriteriaVO();
        criteriaVO.setTenderId(tenderId);
        criteriaVO.setRfqId(rfqId);
        criteriaVO.setRfqVenderId(rfqVenderId);
        criteriaVO.setLastQuote(true);
        
        List<AwardVO> list = findByCriteria(criteriaVO);
        
        return (sys.isEmpty(list))?null:list.get(0);
    }

    public List<AwardVO> findAwardTimeList(Long tenderId, Long rfqId, Long venderId) {
        // TODO
        return new ArrayList<AwardVO>();
    }

    public List<VenderAllVO> findAwardVenderList(Long tenderId, Long rfqId, String awardCode) {
         // TODO
        return new ArrayList<VenderAllVO>();
    }

    /**
     * 儲存多筆決標單
     * @param awardList
     * @param loginUser 
     */
    public void saveAwardRecords(List<AwardVO> awardList, TcUser loginUser) {
        if( sys.isEmpty(awardList) ){
            logger.error("saveAwardRecords awardList is empty !");
            return;
        }
        
        for(AwardVO awardVO : awardList){
            this.saveVO(awardVO, loginUser, false);
            logger.info("saveAwardRecords save "+awardVO);
            for(AwardItemVO itemVO : awardVO.getAwardItemList()){
                itemVO.setAwardId(awardVO.getId());
                awardItemFacade.saveVO(awardVO, loginUser, false);
                logger.info("saveAwardRecords save "+awardVO);
            }
        }
    }

}