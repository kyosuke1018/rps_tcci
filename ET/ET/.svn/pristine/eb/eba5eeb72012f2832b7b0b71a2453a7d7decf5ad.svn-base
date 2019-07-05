/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.facade.rfq;

import com.tcci.cm.facade.AbstractFacade;
import com.tcci.cm.util.ExtBeanUtils;
import com.tcci.et.model.rfq.QuotationVO;
import com.tcci.et.entity.EtRfqVender;
import com.tcci.et.model.rfq.RfqVenderVO;
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
    
    public void remove(QuotationVO vo, boolean simulated) {       
        EtRfqVender entity = findByVO(vo);
        
        if( entity!=null ){
            this.remove(entity, simulated);
        }
    }

    public EtRfqVender findByVO(QuotationVO vo){
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
    public void saveVO(QuotationVO vo, TcUser operator, boolean simulated){
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
        
        //quotation
        sql.append(", qt.TIMES \n");
        
        sql.append("FROM ET_RFQ_VENDER S \n");
        
        sql.append("JOIN ET_TENDER t on t.ID = S.TENDER_ID \n");
        sql.append("JOIN ET_RFQ_EKKO rfq on rfq.ID = S.RFQ_ID \n");
        sql.append("JOIN ET_VENDER_ALL v on v.ID = S.VENDER_ID \n");
        sql.append("LEFT OUTER JOIN ET_MEMBER m on m.ID = S.MEMBER_ID \n");
        sql.append("LEFT OUTER JOIN ( \n");
        sql.append(" SELECT COUNT(q.ID) as TIMES, q.TENDER_ID, q.RFQ_ID ,q.RFQ_VENDER_ID \n");
        sql.append(" FROM ET_QUOTATION q \n");
        sql.append(" GROUP BY q.TENDER_ID, q.RFQ_ID, q.RFQ_VENDER_ID \n");
        sql.append(") qt on qt.TENDER_ID = S.TENDER_ID AND qt.RFQ_ID = S.RFQ_ID AND qt.RFQ_VENDER_ID = S.ID \n");
        
        sql.append("WHERE 1=1 \n");
        if(criteriaVO.getId()!=null){
            sql.append("AND S.ID=#id \n");
            params.put("id", criteriaVO.getId());
        }
        if(criteriaVO.getTenderId()!=null){
            sql.append("AND S.TENDER_ID=#tenderId \n");
            params.put("tenderId", criteriaVO.getTenderId());
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
}
