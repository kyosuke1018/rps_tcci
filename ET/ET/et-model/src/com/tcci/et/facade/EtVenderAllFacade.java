/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.facade;

import com.tcci.cm.facade.AbstractFacade;
import com.tcci.cm.util.ExtBeanUtils;
import com.tcci.et.entity.EtVenderAll;
import com.tcci.et.enums.VenderStatusEnum;
import com.tcci.et.model.criteria.VenderCriteriaVO;
import com.tcci.et.model.VenderAllVO;
import com.tcci.et.model.criteria.VenderCriteriaVO;
import com.tcci.fc.entity.org.TcUser;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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
     * 依輸入條件查詢筆數
     * @param criteriaVO
     * @return 
     */
    public int countByCriteria(VenderCriteriaVO criteriaVO) {
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
    public List<VenderAllVO> findByCriteria(VenderCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT S.* \n");
        if( criteriaVO.isJoinLFA1() ){
            sql.append(", LFA1.NAME1, LFA1.RISK, LFA1.CSPERM, LFA1.SYNC_TIME \n");
        }
        if( criteriaVO.getTenderId()!=null || criteriaVO.getRfqId()!=null ){
            sql.append(", RV.ID RFQ_VENDER_ID, RV.SOURCE \n");
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
     * 依輸入條件查詢 SQL
     * @param criteriaVO
     * @param params
     * @return 
     */
    public String findByCriteriaSQL(VenderCriteriaVO criteriaVO, Map<String, Object> params){
        StringBuilder sql = new StringBuilder();
        sql.append("FROM ET_VENDER_ALL S \n");
        
        // TC_ZTAB_EXP_LFA1
        if( criteriaVO.isJoinLFA1() ){
            sql.append("LEFT OUTER JOIN ( \n");
            sql.append("    SELECT MANDT, LIFNR, NAME1, RISK, CSPERM, MAX(SYNC_TIME_STAMP) SYNC_TIME, COUNT(*) CC \n");
            sql.append("    FROM TC_ZTAB_EXP_LFA1 \n");
            sql.append("    WHERE 1=1 \n");
            sql.append("    GROUP BY MANDT, LIFNR, NAME1, RISK, CSPERM, CSPERM \n");
            sql.append("    HAVING COUNT(*)>1 \n");
            //sql.append("    ORDER BY MANDT, LIFNR \n");
            sql.append(") LFA1 ON LFA1.MANDT=S.MANDT AND LFA1.LIFNR=S.LIFNR \n");
        }

        // ET_RFQ_VENDER
        if( criteriaVO.getTenderId()!=null || criteriaVO.getRfqId()!=null ){
            sql.append(criteriaVO.isOuterJoinRfq()?"LEFT OUTER JOIN":"JOIN").append(" ET_RFQ_VENDER RV ON RV.DISABLED=0 AND RV.VENDER_ID=S.ID \n");
            if( criteriaVO.getTenderId()!=null ){
                sql.append("    AND RV.TENDER_ID=#TENDER_ID \n");
                params.put("TENDER_ID", criteriaVO.getTenderId());
            }
            if( criteriaVO.getRfqId()!=null ){
                sql.append("    AND RV.RFQ_ID=#RFQ_ID \n");
                params.put("RFQ_ID", criteriaVO.getRfqId());
            }
        }
        
        if(criteriaVO.getCategoryId()!=null){//符合物料群組供應商及申請中供應商
            sql.append("JOIN ( \n");
            sql.append("    SELECT S.ID FROM ET_VENDER_ALL S \n");
            sql.append("    JOIN ET_VENDER_CATEGORY VC ON VC.MANDT = S.MANDT AND VC.LIFNR_UI = S.LIFNR_UI AND VC.CATEGORY=#CATEGORYID \n");
            params.put("CATEGORYID", criteriaVO.getCategoryId());
            
            sql.append("    UNION \n");
            sql.append("    SELECT S.ID FROM ET_VENDER_ALL S \n");
            sql.append("    WHERE S.APPLY_ID IS NOT NULL AND S.LIFNR_UI IS NULL \n");
            sql.append(") S2 ON S2.ID = S.ID \n");
        }
        
        sql.append("WHERE 1=1 \n");
        sql.append("AND S.DISABLED=0 \n");
        
        if(criteriaVO.getId()!=null){
            sql.append("AND S.ID=#id \n");
            params.put("id", criteriaVO.getId());
        }
        if(criteriaVO.getApplyId()!=null){
            sql.append("AND S.APPLY_ID=#applyId \n");
            params.put("applyId", criteriaVO.getApplyId());
        }
        
        // 供應商名稱關鍵字
        if( criteriaVO.getNameKeyword()!=null ){
            String kw = "%"+criteriaVO.getNameKeyword().trim().toUpperCase()+"%";
            sql.append("AND UPPER(S.NAME) LIKE #NAME_KW \n");
            params.put("NAME_KW", kw);
        }
        // 供應商代碼關鍵字
        if( criteriaVO.getKeyword()!=null ){
            String kw = "%"+criteriaVO.getKeyword().trim().toUpperCase()+"%";
            sql.append("AND UPPER(S.LIFNR_UI) LIKE #KW \n");
            params.put("KW", kw);
        }
        // 申請編號
        if( criteriaVO.getApplyId()!=null ){
            sql.append("AND S.APPLY_ID=#APPLY_ID \n");
            params.put("APPLY_ID", criteriaVO.getApplyId());
        }
        // 狀態 VenderStatusEnum
        if( criteriaVO.getStatus()!=null ){
            VenderStatusEnum venderStatusEnum = VenderStatusEnum.getFromCode(criteriaVO.getStatus());
            if( venderStatusEnum!=null ){
                if( VenderStatusEnum.ORI == venderStatusEnum ){// SAP舊商
                    sql.append("AND S.LIFNR IS NOT NULL \n");
                }else if( VenderStatusEnum.NEW == venderStatusEnum ){// 申請中
                    sql.append("AND S.APPLY_ID IS NOT NULL AND S.LIFNR IS NULL \n");
                }
            }
        }
        
        return sql.toString();
    }

    /**
     * 依 ID 查詢
     * @param id
     * @param fullData
     * @return 
     */
    public VenderAllVO findById(Long id, boolean fullData) {
        VenderCriteriaVO criteriaVO = new VenderCriteriaVO();
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
        VenderCriteriaVO criteriaVO = new VenderCriteriaVO();
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
        VenderCriteriaVO criteriaVO = new VenderCriteriaVO();
        criteriaVO.setTenderId(tenderId);
        criteriaVO.setDisabled(Boolean.FALSE);
        
        List<VenderAllVO> list = findByCriteria(criteriaVO);
        return list;
    }

    /**
     * 同步 SAP LFA1 名稱
     * @param vo
     * @param operator 
     */
    public void syncVenderName(VenderAllVO vo, TcUser operator) {
        // LFA1 同步欄位
        String mandt = vo.getMandt();
        String lifnr = vo.getLifnr();
        String name1 = vo.getName1();
        
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("mandt", mandt);
        params.put("lifnr", lifnr);
        List<EtVenderAll> list = this.findByNamedQuery("EtVenderAll.findByMandtLifnr", params);
        
        Date syncTime = new Date();
        if( sys.isEmpty(list) ){
            EtVenderAll entity = new EtVenderAll();
            entity.setDisabled(false);
            entity.setLifnr(lifnr);
            entity.setLifnrUi(lifnr!=null && lifnr.startsWith("000")?lifnr.substring(3):lifnr);
            entity.setMandt(mandt);
            entity.setName(name1);
            entity.setStatus(VenderStatusEnum.ORI.getCode());// 舊商
            entity.setSynctimestamp(syncTime);
            this.save(entity, operator, false);
        }else{
            for(EtVenderAll vender : list){
                vender.setName(name1);
                vender.setSynctimestamp(syncTime);
                this.save(vender, operator, false);
            }
        }
    }
}

