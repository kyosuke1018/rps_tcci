/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.facade.rfq;

import com.tcci.cm.facade.AbstractFacade;
import com.tcci.cm.util.ExtBeanUtils;
import com.tcci.et.entity.EtRfqVender;
import com.tcci.et.enums.RfqVenderSrcEnum;
import com.tcci.et.model.VenderAllVO;
import com.tcci.et.model.rfq.RfqVenderVO;
import com.tcci.et.model.criteria.RfqCriteriaVO;
import com.tcci.et.model.rfq.TenderingVO;
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
public class EtRfqVenderFacade extends AbstractFacade<EtRfqVender> {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EtRfqVenderFacade() {
        super(EtRfqVender.class);
    }
    
    public void remove(RfqVenderVO vo, boolean simulated) {       
        EtRfqVender entity = findByVO(vo);
        
        if( entity!=null ){
            this.remove(entity, simulated);
        }
    }

    public EtRfqVender findByVO(RfqVenderVO vo){
        EtRfqVender entity = this.find(vo.getId());
        return entity;
    }
    /**
     * 單筆儲存
     * @param entity
     * @param operator
     * @param simulated
     */
    public void save(EtRfqVender entity, TcUser operator, boolean simulated){
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
    public void saveVO(RfqVenderVO vo, TcUser operator, boolean simulated){
        if( vo==null ){
            logger.error("saveVO vo==null");
            return;
        }
        EtRfqVender entity = (vo.getId()!=null)?this.find(vo.getId()):new EtRfqVender();
        
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
        
        sql.append("FROM ET_RFQ_VENDER S \n");
        //sql.append("JOIN ET_TENDER t on t.ID = S.TENDER_ID \n");
        //sql.append("JOIN ET_RFQ_EKKO rfq on rfq.ID = S.RFQ_ID \n");
        sql.append("JOIN ET_VENDER v on v.ID = S.VENDER_ID \n");
        sql.append("LEFT OUTER JOIN ET_MEMBER m on m.ID = S.MEMBER_ID \n");// 投標會員
        sql.append("LEFT OUTER JOIN ( \n");
        sql.append("    SELECT COUNT(q.ID) as TIMES, q.TENDER_ID, q.RFQ_ID ,q.RFQ_VENDER_ID \n");
        sql.append("    FROM ET_QUOTATION q \n");
        sql.append("    WHERE q.DISABLED=0 \n");
        sql.append("    GROUP BY q.TENDER_ID, q.RFQ_ID, q.RFQ_VENDER_ID \n");
        sql.append(") qt on qt.TENDER_ID = S.TENDER_ID AND qt.RFQ_ID = S.RFQ_ID AND qt.RFQ_VENDER_ID = S.ID \n");
        
        sql.append("WHERE 1=1 \n");
        sql.append("AND S.DISABLED=0 \n"); 
        
        if(criteriaVO.getId()!=null){
            sql.append("AND S.ID=#id \n");
            params.put("id", criteriaVO.getId());
        }
        if(criteriaVO.getTenderId()!=null){
            sql.append("AND S.TENDER_ID=#tenderId \n");
            params.put("tenderId", criteriaVO.getTenderId());
        }
        if(criteriaVO.getVenderId()!=null){
            sql.append("AND S.VENDER_ID=#venderId \n");
            params.put("venderId", criteriaVO.getVenderId());
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
    public List<RfqVenderVO> findByCriteria(RfqCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT S.* \n");
        //quotation
        sql.append(", qt.TIMES \n");
        
        sql.append(findByCriteriaSQL(criteriaVO, params));
        
        if( criteriaVO.getOrderBy()!=null ){
            sql.append("ORDER BY ").append(criteriaVO.getOrderBy());
        }else{
            sql.append("ORDER BY ID DESC");
        }
        
        List<RfqVenderVO> list = null;
        list = this.selectBySql(RfqVenderVO.class, sql.toString(), params, criteriaVO.getFirstResult(), criteriaVO.getMaxResults());
        return list;
    }

    /**
     * 依 ID 查詢
     * @param id
     * @param fullData
     * @return 
     */
    public RfqVenderVO findById(Long id, boolean fullData) {
        RfqCriteriaVO criteriaVO = new RfqCriteriaVO();
        criteriaVO.setId(id);
        criteriaVO.setFullData(fullData);
        
        List<RfqVenderVO> list = findByCriteria(criteriaVO);
        return (list!=null && !list.isEmpty())? list.get(0):null;
    }
    
    /**
     * 
     * @param rfqVenderId
     * @param times
     * @param checkTimes - true:檢查 times 是否大於 lastQuote, 大於才可變更; false:不檢查
     */
    public void setLastQuote(Long rfqVenderId, Integer times, boolean checkTimes){
        EtRfqVender entity = this.find(rfqVenderId);
        if( !checkTimes || entity.getLastQuote()==null || entity.getLastQuote() < times ){
            entity.setLastQuote(times);
            this.edit(entity, false);
            logger.info("setLastQuote update rfqVenderId="+rfqVenderId+", times="+times);
        }else{
            logger.info("setLastQuote not update rfqVenderId="+rfqVenderId+", times="+times);
        }
        
    }

    /**
     * 移除投標廠商
     * @param tenderId
     * @param rfqId
     * @param vo
     * @param operator
     * @param simulated 
     */
    public void removeByVender(Long tenderId, Long rfqId, VenderAllVO vo, TcUser operator, boolean simulated) {
        logger.info("removeByVender update rfqId="+rfqId+", getRfqVenderId="+vo.getRfqVenderId());
        EtRfqVender entity = this.find(vo.getRfqVenderId());
        entity.setDisabled(true);
        this.save(entity, operator, simulated);
        logger.info("setOpenQuote id = "+entity.getId());
    }

    /**
     * 新增投標廠商
     * @param tenderId
     * @param rfqId
     * @param source
     * @param vo
     * @param operator
     * @param simulated 
     */
    public void addByVender(Long tenderId, Long rfqId, RfqVenderSrcEnum source, VenderAllVO vo, TcUser operator, boolean simulated) {
        logger.info("removeByVender update rfqId="+rfqId+", getRfqVenderId="+vo.getRfqVenderId());
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("tenderId", tenderId);
        params.put("rfqId", rfqId);
        params.put("venderId", vo.getId());
        
        EtRfqVender entity;
        List<EtRfqVender> list = this.findByNamedQuery("EtRfqVender.findByKey", params);
        if( sys.isEmpty(list) ){
            entity = new EtRfqVender();
            ExtBeanUtils.copyProperties(entity, vo);
            entity.setId(null);
            entity.setTenderId(tenderId);
            entity.setRfqId(rfqId);
            entity.setVenderId(vo.getId());
            entity.setSource(source.getCode());
            entity.setName1(vo.getName1());
            entity.setLifnr(vo.getLifnr());
            entity.setOpenQuote(true);// 開放報價
            entity.setOpenTime(new Date());
            entity.setDisabled(false);
        }else{
            entity = list.get(0);
            entity.setOpenTime(new Date());
            entity.setOpenQuote(true);// 開放報價
            entity.setDisabled(false);
        }
        this.save(entity, operator, simulated);
    }

    /**
     * 開放/關閉 報價
     * @param rfqVenderId
     * @param openQuote
     * @param operator
     * @param simulated 
     */
    public void setOpenQuote(Long rfqVenderId, boolean openQuote, TcUser operator, boolean simulated) {
        logger.info("setOpenQuote update rfqVenderId="+rfqVenderId+", openQuote="+openQuote);
        EtRfqVender entity = this.find(rfqVenderId);
        entity.setOpenQuote(openQuote);
        entity.setOpenTime(new Date());
        
        this.save(entity, operator, simulated);
        logger.info("setOpenQuote id = "+entity.getId());
    }

    /**
     * 已投標標案
     * @param criteriaVO
     * @return 
     */
    public List<TenderingVO> findTendered(RfqCriteriaVO criteriaVO) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT S.* \n");
        sql.append(", VQ.VENDER_ID, VQ.RFQ_VENDER_ID, VQ.QUOTE_ID, VQ.TIMES \n"); 
        sql.append(", VQ.NAME VENDER_NAME, VQ.LIFNR_UI \n"); 
        sql.append(", VQ.MEMBER_ID \n");
        sql.append("FROM ET_TENDER S \n");// 標案
        sql.append("LEFT OUTER JOIN ET_RFQ_EKKO R ON R.TENDER_ID=S.ID \n");// 詢價單
        ///////////
        sql.append("JOIN ( \n"); // 會員(供應商) 投標/報價資訊
        sql.append("     SELECT RV.TENDER_ID, RV.RFQ_ID, RV.ID RFQ_VENDER_ID, RV.VENDER_ID, Q.ID QUOTE_ID, Q.TIMES \n");
        sql.append("     , VA.NAME, VA.LIFNR_UI \n"); 
        sql.append("     , MV.MAIN_ID MEMBER_ID \n"); 
        sql.append("     FROM ET_RFQ_VENDER RV \n");// 投標廠商
        sql.append("     JOIN ET_VENDER VA ON VA.ID=RV.VENDER_ID \n"); 
        sql.append("     JOIN ET_MEMBER_VENDER MV ON MV.VENDER_ID=VA.ID \n");// 會員廠商關連
        sql.append("     LEFT OUTER JOIN ET_QUOTATION Q ON Q.RFQ_VENDER_ID=RV.ID AND Q.TIMES=RV.LAST_QUOTE AND Q.DISABLED=0 \n");// 報價
        sql.append("     WHERE 1=1 \n"); 
        sql.append("     AND RV.DISABLED=0 \n"); 
        // 指定供應商
        if(criteriaVO.getVenderId()!=null){
            sql.append("     AND RV.VENDER_ID=#VENDER_ID \n");
            params.put("VENDER_ID", criteriaVO.getVenderId());
        }
        if(criteriaVO.getRfqVenderId()!=null){
            sql.append("     AND RV.ID=#RFQ_VENDER_ID \n");
            params.put("RFQ_VENDER_ID", criteriaVO.getVenderId());
        }
        // 指定會員 // 必要條件
        sql.append("     AND MV.MAIN_ID=#MEMBER_ID \n");
        params.put("MEMBER_ID", criteriaVO.getMemberId());

        sql.append(") VQ ON VQ.TENDER_ID=R.TENDER_ID AND VQ.RFQ_ID=R.ID \n"); 
        ///////////
        
        sql.append("WHERE 1=1 \n");
        
        List<TenderingVO> list = this.selectBySql(TenderingVO.class, sql.toString(), params, criteriaVO.getFirstResult(), criteriaVO.getMaxResults());
        
        return list;
    }
}
