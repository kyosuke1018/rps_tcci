/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.facade;

import com.tcci.cm.facade.AbstractFacade;
import com.tcci.cm.util.ExtBeanUtils;
import com.tcci.et.entity.EtBargainVender;
import com.tcci.et.model.criteria.RfqCriteriaVO;
import com.tcci.et.model.rfq.BargainVenderVO;
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
public class EtBargainVenderFacade extends AbstractFacade<EtBargainVender> {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EtBargainVenderFacade() {
        super(EtBargainVender.class);
    }
    
    /*public void remove(BargainVenderVO vo, boolean simulated) {       
        EtBargainVender entity = findByVO(vo);
        
        if( entity!=null ){
            this.remove(entity, simulated);
        }
    }

    public EtBargainVender findByVO(BargainVenderVO vo){
        EtBargainVender entity = this.find(vo.getId());
        return entity;
    }*/
    /**
     * 單筆儲存
     * @param entity
     * @param operator
     * @param simulated
     */
    public void save(EtBargainVender entity, TcUser operator, boolean simulated){
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
    public void saveVO(BargainVenderVO vo, TcUser operator, boolean simulated){
        if( vo==null ){
            logger.error("saveVO vo==null");
            return;
        }
        EtBargainVender entity = (vo.getId()!=null)?this.find(vo.getId()):new EtBargainVender();
        
        ExtBeanUtils.copyProperties(entity, vo);
        save(entity, operator, simulated);
        ExtBeanUtils.copyProperties(vo, entity);
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
    public List<BargainVenderVO> findByCriteria(RfqCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT S.* \n");
        
        sql.append(findByCriteriaSQL(criteriaVO, params));
        
        if( criteriaVO.getOrderBy()!=null ){
            sql.append("ORDER BY ").append(criteriaVO.getOrderBy());
        }else{
            sql.append("ORDER BY S.BARGAIN_ID");
        }
        
        List<BargainVenderVO> list = selectBySql(BargainVenderVO.class, sql.toString(), params, criteriaVO.getFirstResult(), criteriaVO.getMaxResults());
        return list;
    }

    /**
     * 依輸入條件查詢 SQL
     * @param criteriaVO
     * @param params
     * @return 
     */
    public String findByCriteriaSQL(RfqCriteriaVO criteriaVO, Map<String, Object> params){
        StringBuilder sql = new StringBuilder();
        
        sql.append("FROM ET_BARGAIN_VENDER S \n");// 議價設定廠商
        sql.append("JOIN ET_RFQ_VENDER RV ON RV.DISABLED=0 AND RV.ID=S.RFQ_VENDER_ID \n");// 招標/邀標廠商
        sql.append("JOIN ET_VENDER_ALL VA ON VA.ID=RV.VENDER_ID \n");// 廠商主檔
        sql.append("WHERE 1=1 \n");
        sql.append("AND S.DISABLED=0 \n");
        
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
        
        if( criteriaVO.getRfqVenderId()!=null ){
            sql.append("AND S.RFQ_VENDER_ID=#RFQ_VENDER_ID \n");
            params.put("RFQ_VENDER_ID", criteriaVO.getRfqVenderId());
        }
        if( criteriaVO.getBargainId()!=null ){
            sql.append("AND S.BARGAIN_ID=#BARGAIN_ID \n");
            params.put("BARGAIN_ID", criteriaVO.getBargainId());
        }
        
        if( criteriaVO.getVenderId()!=null ){
            sql.append("AND RV.VENDER_ID=#VENDER_ID \n");
            params.put("VENDER_ID", criteriaVO.getVenderId());
        }
        
        return sql.toString();
    }

    /**
     *  find By RfqId
     * @param tenderId
     * @param rfqId
     * @return 
     */
    public List<BargainVenderVO> findByRfqId( Long tenderId, Long rfqId){
        if( tenderId==null || rfqId==null ){
            logger.error("findByRfqId tenderId==null || rfqId==null");
            return null;
        }
        
        RfqCriteriaVO criteriaVO = new RfqCriteriaVO();
        criteriaVO.setTenderId(tenderId);
        criteriaVO.setRfqId(rfqId);
        
        return this.findByCriteria(criteriaVO);
    }
    
    /**
     * 依 ID 查詢
     * @param id
     * @return 
     */
    public BargainVenderVO findById( Long id) {
        RfqCriteriaVO criteriaVO = new RfqCriteriaVO();
        criteriaVO.setId(id);
        criteriaVO.setFullData(true);
        
        List<BargainVenderVO> list = findByCriteria(criteriaVO);
        return (list!=null && !list.isEmpty())? list.get(0):null;
    }
    
    /**
     * 依 BARGAIN_ID 查詢
     * @param bargainId
     * @return 
     */
    public List<BargainVenderVO> findByBargainId(long bargainId) {
        RfqCriteriaVO criteriaVO = new RfqCriteriaVO();
        criteriaVO.setBargainId(bargainId);
        criteriaVO.setFullData(true);
        
        List<BargainVenderVO> list = findByCriteria(criteriaVO);
        return list;
    }
    
    /**
     * find By unikey
     * @param tenderId
     * @param rfqId
     * @param rfqVenderId
     * @param bargainId
     * @return 
     */
    public List<EtBargainVender> findByKey(Long tenderId, Long rfqId, Long rfqVenderId, Long bargainId){
        if( tenderId==null && rfqId==null && rfqVenderId==null && bargainId==null ){
            logger.error("findByKey tenderId==null && rfqId==null && rfqVenderId==null && bargainId==null");
            return null;
        }
        
        Map<String, Object> params = new HashMap<>();
        params.put("tenderId", tenderId);
        params.put("rfqId", rfqId);
        params.put("rfqVenderId", rfqVenderId);
        params.put("bargainId", bargainId);
        List<EtBargainVender> list = this.findByNamedQuery("EtBargainVender.findByKey", params);
        
        return list;
    }
}
