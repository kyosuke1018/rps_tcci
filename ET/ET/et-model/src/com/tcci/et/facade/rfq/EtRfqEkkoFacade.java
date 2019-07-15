/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.facade.rfq;

import com.tcci.cm.exception.CmCustomException;
import com.tcci.cm.facade.AbstractFacade;
import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.cm.util.ExtBeanUtils;
import com.tcci.et.model.rfq.QuotationVO;
import com.tcci.et.entity.EtRfqEkko;
import com.tcci.et.model.rfq.RfqEkkoVO;
import com.tcci.et.model.criteria.RfqCriteriaVO;
import com.tcci.et.model.rfq.RfqEkpoVO;
import com.tcci.et.model.rfq.RfqVO;
import com.tcci.fc.entity.org.TcUser;
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
public class EtRfqEkkoFacade extends AbstractFacade<EtRfqEkko> {
    @EJB EtRfqEkpoFacade rfqEkpoFacade;

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EtRfqEkkoFacade() {
        super(EtRfqEkko.class);
    }
    
    public void remove(QuotationVO vo, boolean simulated) {       
        EtRfqEkko entity = findByVO(vo);
        
        if( entity!=null ){
            this.remove(entity, simulated);
        }
    }

    public EtRfqEkko findByVO(QuotationVO vo){
        EtRfqEkko entity = this.find(vo.getId());
        return entity;
    }
    /**
     * 單筆儲存
     * @param entity
     * @param operator
     * @param simulated
     */
    public void save(EtRfqEkko entity, TcUser operator, boolean simulated){
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
    public void saveVO(RfqEkkoVO vo, TcUser operator, boolean simulated){
        if( vo==null ){
            logger.error("saveVO vo==null");
            return;
        }
        EtRfqEkko entity = (vo.getId()!=null)?this.find(vo.getId()):new EtRfqEkko();
        
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
        sql.append("FROM ET_RFQ_EKKO S \n");
        sql.append("WHERE 1=1 \n");
        // sql.append("AND S.DISABLED=0 \n");

        if( criteriaVO.getId()!=null ){
            sql.append("AND S.ID=#ID \n");
            params.put("ID", criteriaVO.getId());
        }
        if( criteriaVO.getTenderId()!=null ){
            sql.append("AND S.TENDER_ID=#TENDER_ID \n");
            params.put("TENDER_ID", criteriaVO.getTenderId());
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
    public List<RfqEkkoVO> findByCriteria(RfqCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT S.* \n");
        
        sql.append(findByCriteriaSQL(criteriaVO, params));
        
        if( criteriaVO.getOrderBy()!=null ){
            sql.append("ORDER BY ").append(criteriaVO.getOrderBy());
        }else{
            sql.append("ORDER BY ID DESC");
        }
        
        List<RfqEkkoVO> list = this.selectBySql(RfqEkkoVO.class, sql.toString(), params, criteriaVO.getFirstResult(), criteriaVO.getMaxResults());
        return list;
    }

    /**
     * 依 ID 查詢
     * @param id
     * @param fullData
     * @return 
     */
    public RfqEkkoVO findById(Long id, boolean fullData) {
        RfqCriteriaVO criteriaVO = new RfqCriteriaVO();
        criteriaVO.setId(id);
        criteriaVO.setFullData(fullData);
        
        List<RfqEkkoVO> list = findByCriteria(criteriaVO);
        return (list!=null && !list.isEmpty())? list.get(0):null;
    }
    
    /**
     * 依標案ID 查詢
     * @param tenderId
     * @return 
     */
    public RfqEkkoVO findByTenderId(Long tenderId) {
        RfqCriteriaVO criteriaVO = new RfqCriteriaVO();
        criteriaVO.setTenderId(tenderId);
        
        List<RfqEkkoVO> list = findByCriteria(criteriaVO);
        
        if( sys.isEmpty(list) ){
            return null;
        }else if( GlobalConstant.SINGLE_RFQ && list.size()>1 ){
            logger.error("findByTenderId list.size()>1, tenderId = "+tenderId);
        }
        
        return list.get(0);
    }

    /**
     * 依標案ID 查詢 (含明細資訊)
     * @param tenderId
     * @return 
     */
    public RfqVO findRfqVOByTenderId(Long tenderId) {
        RfqVO rfq = new RfqVO();
        
        RfqEkkoVO rfqEkkoVO = findByTenderId(tenderId);
        if( rfqEkkoVO!=null ){
            rfq.setEkko(rfqEkkoVO);
            
            List<RfqEkpoVO> ekpoList = rfqEkpoFacade.findByRfqId(tenderId, rfqEkkoVO.getId());
            rfq.setEkpoList(ekpoList);
        }

        return rfq;
    }
}
