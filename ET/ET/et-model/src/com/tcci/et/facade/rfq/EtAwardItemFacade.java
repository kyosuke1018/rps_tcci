/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.facade.rfq;

import com.tcci.cm.facade.AbstractFacade;
import com.tcci.cm.util.ExtBeanUtils;
import com.tcci.cm.util.NativeSQLUtils;
import com.tcci.et.model.rfq.AwardVO;
import com.tcci.et.entity.EtAwardItem;
import com.tcci.et.model.rfq.AwardItemVO;
import com.tcci.et.model.criteria.RfqCriteriaVO;
import com.tcci.fc.entity.org.TcUser;
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
public class EtAwardItemFacade extends AbstractFacade<EtAwardItem> {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EtAwardItemFacade() {
        super(EtAwardItem.class);
    }
    
    public void remove(AwardItemVO vo, boolean simulated) {       
        EtAwardItem entity = findByVO(vo);
        
        if( entity!=null ){
            this.remove(entity, simulated);
        }
    }

    public EtAwardItem findByVO(AwardItemVO vo){
        EtAwardItem entity = this.find(vo.getId());
        return entity;
    }
    /**
     * 單筆儲存
     * @param entity
     * @param operator
     * @param simulated
     */
    public void save(EtAwardItem entity, TcUser operator, boolean simulated){
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
    public void saveVO(AwardItemVO vo, TcUser operator, boolean simulated){
        if( vo==null ){
            logger.error("saveVO vo==null");
            return;
        }
        EtAwardItem entity = (vo.getId()!=null)?this.find(vo.getId()):new EtAwardItem();
        
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
        
        sql.append("FROM ET_AWARD_ITEM S \n");// 得標項目
        sql.append("JOIN ET_AWARD A ON A.ID=S.AWARD_ID \n");// 得標主檔
        sql.append("JOIN ET_VENDER_ALL VA ON VA.ID=A.VENDER_ID \n");// 供應商主檔
        sql.append("JOIN ET_RFQ_EKPO R ON R.RFQ_ID=S.RFQ_ID AND R.EBELP=S.EBELP \n");// RFQ 項目
        sql.append("JOIN ( \n");
        sql.append("  SELECT QI.*, QH.TIMES, QH.EXPIRETIME, QH.CUR_QUO, QH.RFQ_VENDER_ID \n");
        sql.append("  FROM ET_QUOTATION_ITEM QI \n");// 報價項目
        sql.append("  JOIN ET_QUOTATION QH ON QH.ID=QI.QUOTE_ID \n"); // AND Q.ID=26
        sql.append(") Q ON Q.TENDER_ID=S.TENDER_ID AND Q.RFQ_ID=S.RFQ_ID AND Q.QUOTE_ID=S.QUOTE_ID AND Q.EBELP=S.EBELP \n"); 
        sql.append("LEFT OUTER JOIN TC_USER UC ON UC.ID=S.CREATOR \n"); 
        sql.append("LEFT OUTER JOIN TC_USER UM ON UM.ID=S.MODIFIER \n"); 
        sql.append("WHERE 1=1 \n");
        sql.append("AND S.DISABLED=0 \n");
        sql.append("AND A.DISABLED=0 \n");

        if( criteriaVO.getId()!=null ){
            sql.append("AND S.ID=#ID \n");
            params.put("ID", criteriaVO.getId());
        }
        if( criteriaVO.getTenderId()!=null ){
            sql.append("AND S.TENDER_ID=#TENDER_ID \n");
            params.put("TENDER_ID", criteriaVO.getTenderId());
        }
        if( criteriaVO.getRfqId()!=null ){
            sql.append("AND S.RFQ_ID=#RFQ_ID \n");
            params.put("RFQ_ID", criteriaVO.getRfqId());
        }
        if( criteriaVO.getEbelp()!=null ){
            sql.append("AND S.EBELP=#EBELP \n");
            params.put("EBELP", criteriaVO.getEbelp());
        }
        
        // 報價
        if( criteriaVO.getQuoteId()!=null ){
            sql.append("AND A.QUOTE_ID=#QUOTE_ID \n");
            params.put("QUOTE_ID", criteriaVO.getQuoteId());
        }else if( !sys.isEmpty(criteriaVO.getQuoteList()) ){// 指定多筆報價單 ID
            sql.append("    ").append(NativeSQLUtils.getInSQL("A.QUOTE_ID", criteriaVO.getQuoteList(), params)).append(" \n");
        }
        
        // 決標批號
        if( criteriaVO.getCode()!=null ){
            sql.append("AND A.CODE=#CODE \n");
            params.put("CODE", criteriaVO.getCode());
        }
        // 指定廠商
        if( criteriaVO.getRfqVenderId()!=null ){
            sql.append("AND Q.RFQ_VENDER_ID=#RFQ_VENDER_ID \n");
            params.put("RFQ_VENDER_ID", criteriaVO.getRfqVenderId());
        }
        if( criteriaVO.getVenderId()!=null ){
            sql.append("AND A.VENDER_ID=#VENDER_ID \n");
            params.put("VENDER_ID", criteriaVO.getVenderId());
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
    public List<AwardItemVO> findByCriteria(RfqCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        // ET_AWARD_ITEM
        sql.append("SELECT S.ID, S.AWARD_ID, S.TENDER_ID, S.RFQ_ID, S.VENDER_ID, S.QUOTE_ID, S.EBELP, S.MENGE WINNING_QUANTITY \n"); 
        // ET_AWARD
        sql.append(", A.CODE AWARD_CODE, A.AWARD_TIME, A.STATUS AWARD_STATUS \n"); 
        // ET_VENDER_ALL
        // sql.append(", VA.NAME VENDER_NAME, VA.LIFNR, VA.LIFNR_UI, VA.APPLY_ID \n");
        // ET_RFQ_EKPO
        sql.append(", R.MANDT, R.TXZ01, R.MATNR, R.BUKRS, R.WERKS, R.MEINS, R.PSTYP, R.BANFN, R.BNFPO \n"); 
        // ET_QUOTATION, ET_QUOTATION_ITEM
        sql.append(", Q.CUR_QUO, Q.MENGE, Q.NETPR, Q.PEINH, Q.NETWR, Q.BRTWR, Q.MEMO, Q.EINDT \n"); 
        
        sql.append(findByCriteriaSQL(criteriaVO, params));
        
        if( criteriaVO.getOrderBy()!=null ){
            sql.append("ORDER BY ").append(criteriaVO.getOrderBy());
        }else{
            sql.append("ORDER BY S.QUOTE_ID, S.EBELP");
        }
        
        List<AwardItemVO> list = this.selectBySql(AwardItemVO.class, sql.toString(), params, criteriaVO.getFirstResult(), criteriaVO.getMaxResults());
        return list;
    }

    /**
     * 依 ID 查詢
     * @param id
     * @param fullData
     * @return 
     */
    public AwardItemVO findById( Long id, boolean fullData) {
        RfqCriteriaVO criteriaVO = new RfqCriteriaVO();
        criteriaVO.setId(id);
        criteriaVO.setFullData(fullData);
        
        List<AwardItemVO> list = findByCriteria(criteriaVO);
        return (list!=null && !list.isEmpty())? list.get(0):null;
    }
    
    /**
     *  依標案詢價單查詢
     * @param tenderId
     * @param rfqId
     * @param awardCode
     * @param rfqVenderId
     * @param venderId
     * @return 
     */
    public List<AwardItemVO> findByRfq(Long tenderId, Long rfqId, String awardCode, Long rfqVenderId, Long venderId){
        RfqCriteriaVO criteriaVO = new RfqCriteriaVO();
        criteriaVO.setTenderId(tenderId);
        criteriaVO.setRfqId(rfqId);
        criteriaVO.setCode(awardCode);
        criteriaVO.setRfqVenderId(rfqVenderId);
        criteriaVO.setVenderId(venderId);
        
        List<AwardItemVO> list = findByCriteria(criteriaVO);
        return list;
    }

    /**
     * 依相關報價主檔查詢
     * @param venderAwardList
     * @return 
     */
    /*public List<AwardItemVO> findByAwardList(List<AwardVO> venderAwardList) {
        if( sys.isEmpty(venderAwardList) ){
            return null;
        }
        List<Long> ids = new ArrayList<Long>();
        for(AwardVO vo : venderAwardList){
            ids.add(vo.getId());
        }
        return findByAwardIds(ids);
    }
    public List<AwardItemVO> findByAwardIds(List<Long> ids) {
        if( sys.isEmpty(ids) ){
            return null;
        }
        RfqCriteriaVO criteriaVO = new RfqCriteriaVO();
        criteriaVO.setAwardList(ids);
        return findByCriteria(criteriaVO);
    }*/
}
