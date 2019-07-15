/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.facade.rfq;

import com.tcci.cm.facade.AbstractFacade;
import com.tcci.cm.util.ExtBeanUtils;
import com.tcci.cm.util.NativeSQLUtils;
import com.tcci.et.model.rfq.QuotationVO;
import com.tcci.et.entity.EtQuotationItem;
import com.tcci.et.model.rfq.QuotationItemVO;
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
public class EtQuotationItemFacade extends AbstractFacade<EtQuotationItem> {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EtQuotationItemFacade() {
        super(EtQuotationItem.class);
    }
    
    public void remove(QuotationVO vo, boolean simulated) {       
        EtQuotationItem entity = findByVO(vo);
        
        if( entity!=null ){
            this.remove(entity, simulated);
        }
    }

    public EtQuotationItem findByVO(QuotationVO vo){
        EtQuotationItem entity = this.find(vo.getId());
        return entity;
    }
    /**
     * 單筆儲存
     * @param entity
     * @param operator
     * @param simulated
     */
    public void save(EtQuotationItem entity, TcUser operator, boolean simulated){
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
        EtQuotationItem entity = (vo.getId()!=null)?this.find(vo.getId()):new EtQuotationItem();
        
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
        sql.append("FROM ET_RFQ_EKPO R \n");// 詢價單項目
        // 是否含未報價項目
        String joinInOut = "LEFT OUTER JOIN";// 無報價項目也要回傳
        if( !criteriaVO.isWithNoQuoteItem() ){// 只查有報價
            joinInOut = "JOIN";
        }
        //sql.append(joinInOut).append(" ET_QUOTATION_ITEM S ON S.RFQ_ID=R.RFQ_ID AND S.EBELP=R.EBELP \n");
        //sql.append(joinInOut).append(" ET_QUOTATION Q ON Q.ID=S.QUOTE_ID \n");
        //sql.append(joinInOut).append(" ET_RFQ_VENDER RV ON RV.ID=Q.RFQ_VENDER_ID \n");

        sql.append(joinInOut).append(" ( \n");
        sql.append("  SELECT S.*, Q.CUR_QUO \n");
        sql.append("  , Q.RFQ_VENDER_ID, RV.VENDER_ID, VA.NAME  VENDER_NAME \n");
        sql.append("  , NVL(S.MODIFYTIME, S.CREATETIME) LAST_UPDATE_TIME \n");
        sql.append("  , NVL(UC.CNAME, UM.CNAME) LAST_UPDATE_USER \n"); 
        sql.append("  FROM ET_QUOTATION_ITEM S \n");
        sql.append("  JOIN ET_QUOTATION Q ON Q.ID=S.QUOTE_ID \n"); 
        
        // 有報價
        if( criteriaVO.getQuoteId()!=null ){
            sql.append("    AND Q.ID=#QUOTE_ID \n");
            params.put("QUOTE_ID", criteriaVO.getQuoteId());
        }else if( !sys.isEmpty(criteriaVO.getQuoteList()) ){// 指定多筆報價單 ID
            sql.append("    ").append(NativeSQLUtils.getInSQL("Q.ID", criteriaVO.getQuoteList(), params)).append(" \n");
        }
        
        sql.append("  JOIN ET_RFQ_VENDER RV ON RV.ID=Q.RFQ_VENDER_ID \n");
        sql.append("  JOIN ET_VENDER_ALL VA ON VA.ID=RV.VENDER_ID \n");
        
        // 指定廠商
        if( criteriaVO.getRfqVenderId()!=null ){
            sql.append("    AND RV.ID=#RFQ_VENDER_ID \n");
            params.put("RFQ_VENDER_ID", criteriaVO.getRfqVenderId());
        }
        if( criteriaVO.getVenderId()!=null ){
            sql.append("    AND RV.VENDER_ID=#VENDER_ID \n");
            params.put("VENDER_ID", criteriaVO.getVenderId());
        }

        sql.append("  LEFT OUTER JOIN TC_USER UC ON UC.ID=S.CREATOR \n"); 
        sql.append("  LEFT OUTER JOIN TC_USER UM ON UM.ID=S.MODIFIER \n"); 
        sql.append(") S ON S.TENDER_ID=R.TENDER_ID AND S.RFQ_ID=R.RFQ_ID AND S.EBELP=R.EBELP \n");

        sql.append("WHERE 1=1 \n");
        
        if( criteriaVO.getTenderId()!=null ){
            sql.append("AND R.TENDER_ID=#TENDER_ID \n");
            params.put("TENDER_ID", criteriaVO.getTenderId());
        }
        if( criteriaVO.getRfqId()!=null ){
            sql.append("AND R.RFQ_ID=#RFQ_ID \n");
            params.put("RFQ_ID", criteriaVO.getRfqId());
        }
        if( criteriaVO.getQuoteId()!=null ){
            sql.append("AND R.EBELP=#EBELP \n");
            params.put("EBELP", criteriaVO.getEbelp());
        }
        
        /*if( !criteriaVO.isWithNoQuoteItem() ){// 指定廠商 - 只查有報價
            if( criteriaVO.getRfqVenderId()!=null ){
                sql.append("AND Q.RFQ_VENDER_ID=#RFQ_VENDER_ID \n");
                params.put("RFQ_VENDER_ID", criteriaVO.getRfqVenderId());
            }
            if( criteriaVO.getVenderId()!=null ){
                sql.append("AND RV.VENDER_ID=#VENDER_ID \n");
                params.put("VENDER_ID", criteriaVO.getVenderId());
            }
        }*/
        
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
    public List<QuotationItemVO> findByCriteria(RfqCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        // ET_RFQ_EKPO
        sql.append("SELECT R.TENDER_ID, R.RFQ_ID, R.MANDT, R.EBELP, R.LOEKZ, R.TXZ01, R.MATNR \n"); 
        sql.append(", R.BUKRS, R.WERKS, R.MEINS, R.PSTYP, R.ANFNR, R.ANFPS, R.BANFN, R.BNFPO \n");
        sql.append(", R.PEINH, R.MENGE RFQ_MENGE, R.EINDT RFQ_EINDT \n"); 
        //  ET_QUOTATION、ET_QUOTATION_ITEM
        sql.append(", S.CUR_QUO \n"); 
        sql.append(", S.ID, S.QUOTE_ID, S.MENGE, S.NETPR, S.PEINH, S.NETWR, S.BRTWR, S.MEMO, S.EINDT \n"); 
        sql.append(", S.RFQ_VENDER_ID, S.VENDER_ID, S.VENDER_NAME \n"); 
        sql.append(", S.LAST_UPDATE_TIME, S.LAST_UPDATE_USER \n");
        
        sql.append(findByCriteriaSQL(criteriaVO, params));
        
        if( criteriaVO.getOrderBy()!=null ){
            sql.append("ORDER BY ").append(criteriaVO.getOrderBy());
        }else{
            sql.append("ORDER BY R.RFQ_ID, R.EBELP");
        }
        
        List<QuotationItemVO> list = this.selectBySql(QuotationItemVO.class, sql.toString(), params, criteriaVO.getFirstResult(), criteriaVO.getMaxResults());
        return list;
    }

    /**
     * 依 ID 查詢
     * @param id
     * @return 
     */
    public QuotationItemVO findById( Long id) {
        RfqCriteriaVO criteriaVO = new RfqCriteriaVO();
        criteriaVO.setId(id);
        
        List<QuotationItemVO> list = findByCriteria(criteriaVO);
        return (list!=null && !list.isEmpty())? list.get(0):null;
    }
    
    /**
     * 依 QUOTE_ID 查詢
     * @param quoteId
     * @return 
     */
    public List<QuotationItemVO> findByQuoteId(Long quoteId) {
        if( quoteId==null ){
            return null;
        }
        RfqCriteriaVO criteriaVO = new RfqCriteriaVO();
        criteriaVO.setQuoteId(quoteId);
        criteriaVO.setWithNoQuoteItem(false);
        
        return findByCriteria(criteriaVO);
    }
    
    /**
     *  依標案詢價單查詢
     * @param tenderId
     * @param rfqId
     * @param rfqVenderId
     * @param venderId
     * @param withNoQuoteItem
     * @return 
     */
    public List<QuotationItemVO> findByRfq(Long tenderId, Long rfqId, Long rfqVenderId, Long venderId, boolean withNoQuoteItem){
        RfqCriteriaVO criteriaVO = new RfqCriteriaVO();
        criteriaVO.setTenderId(tenderId);
        criteriaVO.setRfqId(rfqId);
        criteriaVO.setRfqVenderId(rfqVenderId);
        criteriaVO.setVenderId(venderId);
        criteriaVO.setWithNoQuoteItem(withNoQuoteItem);
        
        List<QuotationItemVO> list = findByCriteria(criteriaVO);
        if( list!=null ){
            for(QuotationItemVO vo : list){
                vo.setSelected(sys.isId(vo.getId()));
            }
        }
        return list;
    }

    /**
     * 依相關報價主檔查詢
     * @param venderQuoList
     * @return 
     */
    public List<QuotationItemVO> findByQuoteList(List<QuotationVO> venderQuoList) {
        if( sys.isEmpty(venderQuoList) ){
            return null;
        }
        List<Long> ids = new ArrayList<Long>();
        for(QuotationVO vo : venderQuoList){
            ids.add(vo.getId());
        }
        return findByQuoteIds(ids);
    }
    public List<QuotationItemVO> findByQuoteIds(List<Long> ids) {
        if( sys.isEmpty(ids) ){
            return null;
        }
        RfqCriteriaVO criteriaVO = new RfqCriteriaVO();
        criteriaVO.setQuoteList(ids);
        criteriaVO.setWithNoQuoteItem(false);// 不包含無報價的 RFQ 項目
        
        return findByCriteria(criteriaVO);
    }
}
