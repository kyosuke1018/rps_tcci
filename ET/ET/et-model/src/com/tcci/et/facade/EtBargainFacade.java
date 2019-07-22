/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.facade;

import com.tcci.cm.facade.AbstractFacade;
import com.tcci.cm.util.ExtBeanUtils;
import com.tcci.et.entity.EtBargain;
import com.tcci.et.entity.EtBargainVender;
import com.tcci.et.facade.rfq.EtRfqVenderFacade;
import com.tcci.et.model.rfq.BargainVO;
import com.tcci.et.model.criteria.RfqCriteriaVO;
import com.tcci.et.model.rfq.BargainVenderVO;
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
public class EtBargainFacade extends AbstractFacade<EtBargain> {
    @EJB EtBargainVenderFacade bargainVenderFacade;
    @EJB EtRfqVenderFacade rfqVenderFacade;
    
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EtBargainFacade() {
        super(EtBargain.class);
    }
    
    public void remove(BargainVO vo, boolean simulated) {       
        EtBargain entity = findByVO(vo);
        
        if( entity!=null ){
            this.remove(entity, simulated);
        }
    }

    public EtBargain findByVO(BargainVO vo){
        EtBargain entity = this.find(vo.getId());
        return entity;
    }
    /**
     * 單筆儲存
     * @param entity
     * @param operator
     * @param simulated
     */
    public void save(EtBargain entity, TcUser operator, boolean simulated){
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
    public void saveVO(BargainVO vo, TcUser operator, boolean simulated){
        if( vo==null ){
            logger.error("saveVO vo==null");
            return;
        }
        EtBargain entity = (vo.getId()!=null)?this.find(vo.getId()):new EtBargain();
        
        ExtBeanUtils.copyProperties(entity, vo);
        save(entity, operator, simulated);
        ExtBeanUtils.copyProperties(vo, entity);
    }
    
    /**
     * 完整儲存 ET_BARGAIN、ET_BARGAIN_VENDER
     * @param vo
     * @param operator
     * @param simulated 
     */
    public void saveFullBargain(BargainVO vo, TcUser operator, boolean simulated){
        if( vo==null ){
            logger.error("saveFullBargain vo==null");
            return;
        }
        // ET_BARGAIN
        EtBargain entity = (vo.getId()!=null)?this.find(vo.getId()):new EtBargain();
        
        ExtBeanUtils.copyProperties(entity, vo);
        logger.info("saveFullBargain getEdate = "+entity.getEdate());
        save(entity, operator, simulated);
        ExtBeanUtils.copyProperties(vo, entity);
        
        logger.info("saveFullBargain BargainVO = "+vo.getId());
        
        // 設定廠商 ET_BARGAIN_VENDER
        if( vo.getBargainVenderList()!=null ){
            for(BargainVenderVO vender : vo.getBargainVenderList()){
                if( vo.getNewRfqVenderIds()==null || !vo.getNewRfqVenderIds().contains(vender.getRfqVenderId()) ){// 本次未勾選
                    vo.setDisabled(true);// 移除
                    bargainVenderFacade.saveVO(vender, operator, simulated);
                }
            }
        }
        if( !sys.isEmpty(vo.getNewRfqVenderIds()) ){
            for(Long newId : vo.getNewRfqVenderIds()){
                if( vo.getRfqVenderIds()==null || !vo.getRfqVenderIds().contains(newId) ){// 新增
                    EtBargainVender venderEntity = new EtBargainVender();
                    venderEntity.setBargainId(vo.getId());
                    venderEntity.setRfqId(vo.getRfqId());
                    venderEntity.setRfqVenderId(newId);
                    venderEntity.setTenderId(vo.getTenderId());
                    bargainVenderFacade.save(venderEntity, operator, simulated);
                    
                    // 新增時，開放報價 & 通知廠商
                    // 開放報價
                    rfqVenderFacade.setOpenQuote(newId, true, operator, simulated);
                    // TODO 通知廠商
                    
                }
            }
        }
    }

    /**
     * 依輸入條件查詢 SQL
     * @param criteriaVO
     * @param params
     * @return 
     */
    public String findByCriteriaSQL(RfqCriteriaVO criteriaVO, Map<String, Object> params){
        StringBuilder sql = new StringBuilder();
        
        sql.append("FROM ET_BARGAIN S \n");
        sql.append("WHERE 1=1 \n");
        
        if( criteriaVO.getTenderId()!=null ){
            sql.append("AND S.TENDER_ID=#TENDER_ID \n");
            params.put("TENDER_ID", criteriaVO.getTenderId());
        }
        if( criteriaVO.getRfqId()!=null ){
            sql.append("AND S.RFQ_ID=#RFQ_ID \n");
            params.put("RFQ_ID", criteriaVO.getRfqId());
        }
        if( criteriaVO.getBargainTimes()!=null ){
            sql.append("AND S.TIMES=#TIMES \n");
            params.put("RFQ_ID", criteriaVO.getBargainTimes());
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
    public List<BargainVO> findByCriteria(RfqCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT S.* \n");
        
        sql.append(findByCriteriaSQL(criteriaVO, params));
        
        if( criteriaVO.getOrderBy()!=null ){
            sql.append("ORDER BY ").append(criteriaVO.getOrderBy());
        }else{
            sql.append("ORDER BY S.TIMES");
        }
        
        List<BargainVO> list =  selectBySql(BargainVO.class, sql.toString(), params, criteriaVO.getFirstResult(), criteriaVO.getMaxResults());
        
        if( list!=null && criteriaVO.isFullData() ){
            for(BargainVO vo : list){
                vo.setBargainVenderList(bargainVenderFacade.findByBargainId(vo.getId()));
                logger.debug("findByCriteria BargainVenderList = "+sys.size(vo.getBargainVenderList()));
            }
        }
        
        return list;
    }

    /**
     *  find By RfqId
     * @param tenderId
     * @param rfqId
     * @return 
     */
    public List<BargainVO> findByRfqId( Long tenderId, Long rfqId){
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
     * @param fullData
     * @return 
     */
    public BargainVO findById( Long id, boolean fullData) {
        RfqCriteriaVO criteriaVO = new RfqCriteriaVO();
        criteriaVO.setId(id);
        criteriaVO.setFullData(fullData);
        
        List<BargainVO> list = findByCriteria(criteriaVO);
        return (list!=null && !list.isEmpty())? list.get(0):null;
    }
    
    /**
     *  find By RfqId
     * @param tenderId
     * @return 
     */
    public List<EtBargain> findByTenderId(Long tenderId){
        if( tenderId==null){
            logger.error("findByTenderId tenderId==null");
            return null;
        }
        
        Map<String, Object> params = new HashMap<>();
        params.put("tenderId", tenderId);
        List<EtBargain> list = this.findByNamedQuery("EtBargain.findByTenderId", params);
        
        return list;
    }
}
