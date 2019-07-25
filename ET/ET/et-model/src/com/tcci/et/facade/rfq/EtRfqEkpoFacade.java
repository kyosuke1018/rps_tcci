/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.facade.rfq;

import com.tcci.cm.facade.AbstractFacade;
import com.tcci.cm.util.ExtBeanUtils;
import com.tcci.et.entity.EtRfqEkpo;
import com.tcci.et.model.rfq.RfqEkpoVO;
import com.tcci.et.model.criteria.RfqCriteriaVO;
import com.tcci.et.model.rfq.RfqVO;
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
public class EtRfqEkpoFacade extends AbstractFacade<EtRfqEkpo> {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EtRfqEkpoFacade() {
        super(EtRfqEkpo.class);
    }
    
    public void remove(RfqEkpoVO vo, boolean simulated) {       
        EtRfqEkpo entity = findByVO(vo);
        
        if( entity!=null ){
            this.remove(entity, simulated);
        }
    }

    public EtRfqEkpo findByVO(RfqEkpoVO vo){
        EtRfqEkpo entity = this.find(vo.getId());
        return entity;
    }
    /**
     * 單筆儲存
     * @param entity
     * @param operator
     * @param simulated
     */
    public void save(EtRfqEkpo entity, TcUser operator, boolean simulated){
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
    public void saveVO(RfqEkpoVO vo, TcUser operator, boolean simulated){
        if( vo==null ){
            logger.error("saveVO vo==null");
            return;
        }
        EtRfqEkpo entity = (vo.getId()!=null)?this.find(vo.getId()):new EtRfqEkpo();
        
        ExtBeanUtils.copyProperties(entity, vo);
        save(entity, operator, simulated);
        ExtBeanUtils.copyProperties(vo, entity);
    }

    /**
     * 儲存完整 RFQ
     * @param vo
     * @param operator
     * @param simulated 
     */
    public void saveRfqEkpo(RfqVO vo, TcUser operator, boolean simulated){
        logger.debug("saveRfqEkpo ... ");
        if( vo==null || vo.getEkko()==null || vo.getEkko().getId()==null ){
            logger.error("saveRfqEkpo error vo.getEkko().getId()==null");
            return;
        }
        if( sys.isEmpty(vo.getEkpoList()) ){
            logger.error("saveRfqEkpo error getEkpoList isEmpty");
            return;
        }
        
        for(RfqEkpoVO ekpo : vo.getEkpoList()){
            saveVO(ekpo, operator, simulated);
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
        
        sql.append("FROM ET_RFQ_EKPO S \n");
        sql.append("JOIN TC_SAPCLIENT SC ON SC.CLIENT=S.MANDT \n");
        sql.append("JOIN CM_FACTORY F ON F.SAP_CLIENT_CODE=SC.CODE AND F.CODE=S.WERKS \n");
        // 已決標資訊
        if( criteriaVO.isIncAwardInfo() ){
            sql.append("LEFT OUTER JOIN ( \n");
            sql.append("  SELECT A.TENDER_ID, A.RFQ_ID, AI.EBELP, SUM(MENGE) AWARD_MENGE \n");
            sql.append("  FROM ET_AWARD A \n");
            sql.append("  JOIN ET_AWARD_ITEM AI ON AI.AWARD_ID=A.ID \n");
            sql.append("  WHERE A.DISABLED=0 \n");
            sql.append("  GROUP BY A.TENDER_ID, A.RFQ_ID, AI.EBELP \n");
            sql.append(") AW ON AW.TENDER_ID=S.TENDER_ID AND AW.RFQ_ID=S.RFQ_ID AND AW.EBELP=S.EBELP \n");
        }
        sql.append("WHERE 1=1 \n");
        
        if( criteriaVO.getTenderId()!=null ){
            sql.append("AND S.TENDER_ID=#TENDER_ID \n");
            params.put("TENDER_ID", criteriaVO.getTenderId());
        }
        if( criteriaVO.getRfqId()!=null ){
            sql.append("AND S.RFQ_ID=#RFQ_ID \n");
            params.put("RFQ_ID", criteriaVO.getRfqId());
        }
        
        // 已決標資訊
        if( criteriaVO.isIncAwardInfo() ){    
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
    public List<RfqEkpoVO> findByCriteria(RfqCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT S.* \n");
        sql.append(", F.SAP_CLIENT_CODE, F.NAME FACTORY_NAME \n");
        // 已決標資訊
        if( criteriaVO.isIncAwardInfo() ){
            sql.append(", NVL(AW.AWARD_MENGE, 0) AWARD_QUANTITY, S.MENGE-NVL(AW.AWARD_MENGE, 0) REMAINING \n");
        }
        sql.append(findByCriteriaSQL(criteriaVO, params));
        
        if( criteriaVO.getOrderBy()!=null ){
            sql.append("ORDER BY ").append(criteriaVO.getOrderBy());
        }else{
            sql.append("ORDER BY S.EBELP");
        }
        
        List<RfqEkpoVO> list =  selectBySql(RfqEkpoVO.class, sql.toString(), params, criteriaVO.getFirstResult(), criteriaVO.getMaxResults());
        return list;
    }

    /**
     *  find By RfqId
     * @param tenderId
     * @param rfqId
     * @param incAwardInfo
     * @return 
     */
    public List<RfqEkpoVO> findByRfqId( Long tenderId, Long rfqId, boolean incAwardInfo){
        if( tenderId==null || rfqId==null ){
            logger.error("findByRfqId tenderId==null || rfqId==null");
            return null;
        }
        
        RfqCriteriaVO criteriaVO = new RfqCriteriaVO();
        criteriaVO.setTenderId(tenderId);
        criteriaVO.setRfqId(rfqId);
        criteriaVO.setIncAwardInfo(incAwardInfo);
        
        return this.findByCriteria(criteriaVO);
    }
    
    /**
     * 依 ID 查詢
     * @param id
     * @param fullData
     * @return 
     */
    public RfqEkpoVO findById( Long id, boolean fullData) {
        RfqCriteriaVO criteriaVO = new RfqCriteriaVO();
        criteriaVO.setId(id);
        criteriaVO.setFullData(fullData);
        
        List<RfqEkpoVO> list = findByCriteria(criteriaVO);
        return (list!=null && !list.isEmpty())? list.get(0):null;
    }
}
