/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.facade.rfq;

import com.tcci.cm.facade.AbstractFacade;
import com.tcci.cm.util.ExtBeanUtils;
import com.tcci.et.model.rfq.QuotationVO;
import com.tcci.et.entity.EtQuotationItem;
import com.tcci.et.model.rfq.QuotationItemVO;
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
        sql.append("FROM ET_RFQ_EKPO R \n");
        sql.append("LEFT OUTER JOIN ET_QUOTATION_ITEM S ON S.RFQ_ID=R.RFQ_ID AND S.EBELP=R.EBELP \n");
        sql.append("LEFT OUTER JOIN ET_QUOTATION Q ON Q.ID=S.QUO_ID \n");
        if( criteriaVO.getVenderId()!=null ){
            sql.append("LEFT OUTER JOIN ET_RFQ_VENDER RV ON RV.ID=Q.RFQ_VENDER_ID \n");
        }
        sql.append("LEFT OUTER JOIN TC_USER UC ON UC.ID=S.CREATOR \n");
        sql.append("LEFT OUTER JOIN TC_USER UM ON UM.ID=S.MODIFIER \n");
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
        
        // 指定廠商
        if( criteriaVO.getRfqVenderId()!=null ){
            sql.append("AND Q.RFQ_VENDER_ID=#RFQ_VENDER_ID \n");
            params.put("RFQ_VENDER_ID", criteriaVO.getRfqVenderId());
        }
        if( criteriaVO.getVenderId()!=null ){
            sql.append("AND RV.VENDER_ID=#VENDER_ID \n");
            params.put("VENDER_ID", criteriaVO.getVenderId());
        }
        
        // 有報價
        if( criteriaVO.getQuoteId()!=null ){
            sql.append("AND Q.ID=#QUO_ID \n");
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
    public List<QuotationItemVO> findByCriteria(RfqCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        // sql.append("SELECT S.* \n");
        // ET_RFQ_EKPO
        sql.append("SELECT R.TENDER_ID, R.RFQ_ID, R.MANDT, R.EBELP, R.LOEKZ, R.TXZ01, R.MATNR \n");
        sql.append(", R.BUKRS, R.WERKS, R.MEINS, R.PSTYP, R.ANFNR, R.ANFPS, R.BANFN, R.BNFPO \n");
        sql.append(", R.PEINH, R.MENGE RFQ_MENGE, R.EINDT RFQ_EINDT \n");
        //  ET_QUOTATION
        sql.append(", Q.CUR_QUO \n");
        // ET_QUOTATION_ITEM
        sql.append(", S.ID, S.QUO_ID, S.MENGE, S.NETPR, S.PEINH, S.NETWR, S.BRTWR, S.MEMO, S.EINDT \n");
        sql.append(", NVL(S.MODIFYTIME, S.CREATETIME) LAST_UPDATE_TIME \n");
        sql.append(", NVL(UM.CNAME, UC.CNAME) LAST_UPDATE_USER \n");
        
        sql.append(findByCriteriaSQL(criteriaVO, params));
        
        if( criteriaVO.getOrderBy()!=null ){
            sql.append("ORDER BY ").append(criteriaVO.getOrderBy());
        }else{
            sql.append("ORDER BY R.EBELP");
        }
        
        List<QuotationItemVO> list = this.selectBySql(QuotationItemVO.class, sql.toString(), params, criteriaVO.getFirstResult(), criteriaVO.getMaxResults());
        return list;
    }

    /**
     * 依 ID 查詢
     * @param id
     * @param fullData
     * @return 
     */
    public QuotationItemVO findById( Long id, boolean fullData) {
        RfqCriteriaVO criteriaVO = new RfqCriteriaVO();
        criteriaVO.setId(id);
        criteriaVO.setFullData(fullData);
        
        List<QuotationItemVO> list = findByCriteria(criteriaVO);
        return (list!=null && !list.isEmpty())? list.get(0):null;
    }
    
    /**
     *  依標案詢價單查詢
     * @param tenderId
     * @param rfqId
     * @param rfqVenderId
     * @param venderId
     * @return 
     */
    public List<QuotationItemVO> findByRfq(Long tenderId, Long rfqId, Long rfqVenderId, Long venderId){
        RfqCriteriaVO criteriaVO = new RfqCriteriaVO();
        criteriaVO.setTenderId(tenderId);
        criteriaVO.setRfqId(rfqId);
        criteriaVO.setRfqVenderId(venderId);
        criteriaVO.setVenderId(venderId);
        
        return findByCriteria(criteriaVO);
    }
    
}
