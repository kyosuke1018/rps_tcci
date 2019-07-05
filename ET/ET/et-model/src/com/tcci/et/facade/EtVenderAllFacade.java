/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.facade;

import com.tcci.cm.facade.AbstractFacade;
import com.tcci.cm.util.ExtBeanUtils;
import com.tcci.et.entity.EtVenderAll;
import com.tcci.et.model.criteria.RfqCriteriaVO;
import com.tcci.et.model.VenderAllVO;
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
 * @author peter.pan
 */
@Stateless
public class EtVenderAllFacade extends AbstractFacade<EtVenderAll> {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EtVenderAllFacade() {
        super(EtVenderAll.class);
    }
    
    public void remove(VenderAllVO vo, boolean simulated) {       
        EtVenderAll entity = findByVO(vo);
        
        if( entity!=null ){
            this.remove(entity, simulated);
        }
    }

    public EtVenderAll findByVO(VenderAllVO vo){
        EtVenderAll entity = this.find(vo.getId());
        return entity;
    }
    
    /**
     * 單筆儲存
     * @param entity
     * @param operator
     * @param simulated
     */
    public void save(EtVenderAll entity, TcUser operator, boolean simulated){
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
    public void saveVO(VenderAllVO vo, TcUser operator, boolean simulated){
        if( vo==null ){
            logger.error("saveVO vo==null");
            return;
        }
        EtVenderAll entity = (vo.getId()!=null)?this.find(vo.getId()):new EtVenderAll();
        
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
        sql.append("FROM ET_VENDER_ALL S \n");
        if( criteriaVO.getTenderId()!=null || criteriaVO.getRfqId()!=null ){
            sql.append("JOIN ET_RFQ_VENDER RV ON RV.VENDER_ID=S.ID \n");
        }
        
        if(criteriaVO.getCategoryId()!=null){//符合物料群組供應商及申請中供應商
            sql.append("join ( \n");
            sql.append(" select S.id from ET_VENDER_ALL S \n");
            sql.append(" join ET_VENDER_CATEGORY vc on vc.mandt = S.mandt and vc.LIFNR_UI = S.LIFNR_UI \n");
            sql.append(" AND vc.CATEGORY=#categoryId \n");
            params.put("categoryId", criteriaVO.getCategoryId());
            sql.append("union \n");
            sql.append(" select S.ID from ET_VENDER_ALL S \n");
            sql.append(" where S.APPLY_ID is not null and S.LIFNR_UI is null \n");
            sql.append(") S2 on S2.ID = S.ID \n");
        }
        
        sql.append("WHERE 1=1 \n");
        if(criteriaVO.getId()!=null){
            sql.append("AND S.ID=#id \n");
            params.put("id", criteriaVO.getId());
        }
        if(criteriaVO.getApplyId()!=null){
            sql.append("AND S.APPLY_ID=#applyId \n");
            params.put("applyId", criteriaVO.getApplyId());
        }

        if( criteriaVO.getTenderId()!=null || criteriaVO.getRfqId()!=null ){
            if( criteriaVO.getTenderId()!=null ){
                sql.append("AND RV.TENDER_ID=#TENDER_ID \n");
                params.put("TENDER_ID", criteriaVO.getTenderId());
            }
            if( criteriaVO.getRfqId()!=null ){
                sql.append("AND RV.RFQ_ID=#RFQ_ID \n");
                params.put("RFQ_ID", criteriaVO.getRfqId());
            }
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
    public List<VenderAllVO> findByCriteria(RfqCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT S.* \n");
        if( criteriaVO.getTenderId()!=null || criteriaVO.getRfqId()!=null ){
            sql.append(", RV.ID RFQ_VENDER_ID \n");
        }
        
        sql.append(findByCriteriaSQL(criteriaVO, params));
        
        if( criteriaVO.getOrderBy()!=null ){
            sql.append("ORDER BY ").append(criteriaVO.getOrderBy());
        }else{
            sql.append("ORDER BY S.ID DESC");
        }
        
        List<VenderAllVO> list = null;
        list = this.selectBySql(VenderAllVO.class, sql.toString(), params, criteriaVO.getFirstResult(), criteriaVO.getMaxResults());
        return list;
    }

    /**
     * 依 ID 查詢
     * @param id
     * @param fullData
     * @return 
     */
    public VenderAllVO findById(Long id, boolean fullData) {
        RfqCriteriaVO criteriaVO = new RfqCriteriaVO();
        criteriaVO.setId(id);
        criteriaVO.setFullData(fullData);
        
        List<VenderAllVO> list = findByCriteria(criteriaVO);
        return (list!=null && !list.isEmpty())? list.get(0):null;
    }
    
    /**
     * 依 APPLY_ID 查詢
     * @param applyId
     * @return 
     */
    public VenderAllVO findByApplyId(Long applyId) {
        RfqCriteriaVO criteriaVO = new RfqCriteriaVO();
        criteriaVO.setApplyId(applyId);
        criteriaVO.setDisabled(Boolean.FALSE);
        List<VenderAllVO> list = findByCriteria(criteriaVO);
        return (list!=null && !list.isEmpty())? list.get(0):null;
    }
    
    /**
     * 依 TENDER_ID 查詢
     * @param tenderId
     * @return 
     */
    public List<VenderAllVO> findByTenderId(Long tenderId) {
        RfqCriteriaVO criteriaVO = new RfqCriteriaVO();
        criteriaVO.setTenderId(tenderId);
        criteriaVO.setDisabled(Boolean.FALSE);
        
        List<VenderAllVO> list = findByCriteria(criteriaVO);
        return list;
    }
}

