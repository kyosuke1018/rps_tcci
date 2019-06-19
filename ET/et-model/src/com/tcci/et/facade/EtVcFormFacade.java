/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.facade;

import com.tcci.cm.facade.AbstractFacade;
import com.tcci.cm.util.NativeSQLUtils;
import com.tcci.ec.model.VenderVO;
import com.tcci.ec.model.criteria.BaseCriteriaVO;
import com.tcci.et.entity.EtVcForm;
import com.tcci.et.enums.FormStatusEnum;
import com.tcci.et.enums.FormTypeEnum;
import com.tcci.fc.entity.bpm.TcProcess;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.bpm.IBPMEngine;
import com.tcci.fc.facade.content.ContentFacade;
import com.tcci.fc.vo.AttachmentVO;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Kyle.Cheng
 */
@Stateless
public class EtVcFormFacade extends AbstractFacade<EtVcForm> {
    
    @EJB
    private ContentFacade contentFacade;
    @Inject
    protected IBPMEngine bpmEngine;
    
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EtVcFormFacade() {
        super(EtVcForm.class);
    }
    
    public void saveWithAttachment(EtVcForm entity, List<AttachmentVO> attachments, TcUser uploader) throws Exception {
        save(entity, uploader, false);
        contentFacade.saveContent(entity, attachments, uploader);
    }
    
    public boolean isStatusChanged(EtVcForm entity) {
        EtVcForm now = find(entity.getId());
        return now.getStatus() != entity.getStatus();
    }
    
    /*
     * 啟動簽核流程
     */
    public void startProcess(EtVcForm form, TcUser operator, Map<String, Object> roleApprovers) {
        TcProcess process = bpmEngine.createProcess(operator, FormTypeEnum.V_C.getCode(), roleApprovers, "", form);
        logger.info("startProcess... processID = " + process.getId());
        if (process != null) {
            form.setProcess(process);
            this.save(form, operator, Boolean.FALSE);
        }
//        return process;
    }
    
    /**
     * 單筆儲存
     * @param entity
     * @param operator
     * @param simulated
     */
    public void save(EtVcForm entity, TcUser operator, boolean simulated){
        if( entity!=null ){
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
    
    public List<VenderVO> findByCriteria(BaseCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT  \n");
        sql.append("S.ID ");// ET_VC_FORM
        sql.append(", S.MANDT, S.LIFNR_UI as venderCode, S.CNAME, S.CIDS, S.CNAMES \n");
        sql.append(", S.FACTORY_ID as factoryId, F.NAME as factoryName \n");
        sql.append(", S.STATUS, S.PROCESS as processId \n");
        sql.append(", P.STARTTIMESTAMP as starttime, P.ENDTIMESTAMP as endtime, P.EXECUTIONSTATE as executionstate \n");
        sql.append(", TU.LOGIN_ACCOUNT as applicantAd, TU.CNAME as applicantName  \n");
        sql.append(", vc.C_IDS as cidsOri, vc.C_NAMES as cnamesOri \n"); //ET_VENDER_CATEGORY, EC_OPTION
        
        sql.append(findByCriteriaSQL(criteriaVO, params));

        if( criteriaVO.getOrderBy()!=null ){
            sql.append("ORDER BY ").append(criteriaVO.getOrderBy());
        }else{
            sql.append("ORDER BY vc.C_IDS, S.LIFNR_UI, S.MANDT");
        }
        
//        logger.info("findLfa1ByCriteria sql = "+sql.toString());
        List<VenderVO> list = null;
        if( criteriaVO.getFirstResult()!=null && criteriaVO.getMaxResults()!=null ){
            list = this.selectBySql(VenderVO.class, sql.toString(), params, criteriaVO.getFirstResult(), criteriaVO.getMaxResults());
        }else if( criteriaVO.getMaxResults()!=null ){
            list = this.selectBySql(VenderVO.class, sql.toString(), params, 0, criteriaVO.getMaxResults());
        }else{
            list = this.selectBySql(VenderVO.class, sql.toString(), params);
        }
        return list;
    }
    public String findByCriteriaSQL(BaseCriteriaVO criteriaVO, Map<String, Object> params){
        StringBuilder sql = new StringBuilder();
        sql.append("FROM ET_VC_FORM S \n");
        sql.append("INNER JOIN CM_FACTORY F on F.ID = S.FACTORY_ID \n");
        sql.append("INNER JOIN TC_PROCESS P on P.ID = S.PROCESS \n");
        sql.append("INNER JOIN TC_USER TU on TU.ID = S.CREATOR \n");
        
        //供應商 分類
        // for Oracle
        sql.append("LEFT OUTER JOIN ( \n");//系統角色 groups TC_GROUP.NAME
        sql.append("  SELECT A.MANDT, A.LIFNR_UI \n");
        sql.append("  , LISTAGG(A.CATEGORY, ',') WITHIN GROUP (ORDER BY A.CATEGORY) C_IDS \n");
        sql.append("  , LISTAGG(to_char(o.CNAME), ',') WITHIN GROUP (ORDER BY o.SORTNUM) C_NAMES \n");
        sql.append("  FROM ET_VENDER_CATEGORY A \n");
        sql.append("  join EC_OPTION o on o.id = A.CATEGORY AND o.TYPE = 'tenderCategory' \n");
//        if(criteriaVO.getTypeId()!=null){//供應商分類
//            sql.append("AND o.id=#cId \n");
//            params.put("cId", criteriaVO.getTypeId());
//        }
        sql.append("  GROUP BY A.MANDT, A.LIFNR_UI \n");
        sql.append(") vc on vc.MANDT = S.MANDT and vc.LIFNR_UI=S.LIFNR_UI \n");
        
        
        sql.append("WHERE 1=1 \n");

        if( criteriaVO.getId()!=null ){
            sql.append("AND S.ID=#ID \n");
            params.put("ID", criteriaVO.getId());
        }
        //廠別
        if( criteriaVO.getFactoryId()!=null ){
            sql.append("AND S.FACTORY_ID=#factoryId \n");
            params.put("factoryId", criteriaVO.getFactoryId());
        }else{
            if (CollectionUtils.isNotEmpty(criteriaVO.getIdList())) {
                String pColumnName = "S.FACTORY_ID";
                List<Long> pValueList = criteriaVO.getIdList();
                sql.append((NativeSQLUtils.getInSQL(pColumnName, pValueList, params))).append(" \n");
            }
        }
        if( StringUtils.isNotBlank(criteriaVO.getStatus()) ){
            sql.append("AND S.STATUS=#STATUS \n");
            params.put("STATUS", criteriaVO.getStatus());
        }
        //申請日期
        if (criteriaVO.getStartAt() != null) {
//            sql.append("AND FUNCTION('TRUNC',S.createtime)>=#createStart \n");
            sql.append("AND S.createtime>=#createStart \n");
            params.put("createStart", criteriaVO.getStartAt());
        }
        if (criteriaVO.getEndAt() != null) {
//            sql.append("AND FUNCTION('TRUNC',S.createtime)<=#createEnd \n");
            sql.append("AND S.createtime<=#createEnd \n");
            params.put("createEnd", criteriaVO.getEndAt());
        }
        
        if( !StringUtils.isBlank(criteriaVO.getType()) ){//sap client
            sql.append("AND S.MANDT=#MANDT \n");
            params.put("MANDT", criteriaVO.getType());
        }
        if( !StringUtils.isBlank(criteriaVO.getCode()) ){
            sql.append("AND S.LIFNR_UI=#LIFNR_UI \n");
            params.put("LIFNR_UI", criteriaVO.getCode());
        }
        
        if( !StringUtils.isBlank(criteriaVO.getKeyword()) ){
            String kw = "%" + criteriaVO.getKeyword().trim() + "%";
            sql.append("AND ( \n");
//            sql.append("S.CNAME LIKE #KW OR S.ENAME LIKE #KW \n");
            sql.append("S.NAME1 LIKE #KW \n");
            sql.append("OR S.LIFNR_UI LIKE #KW \n");
            sql.append(") \n");
            params.put("KW", kw);
        }
        
        if(criteriaVO.getTypeId()!=null){//供應商分類
            String cId = "%" + criteriaVO.getTypeId() + "%";
            sql.append("AND vc.C_IDS LIKE #cId \n");
            params.put("cId", cId);
        }
        if( !StringUtils.isBlank(criteriaVO.getStatus()) ){
            sql.append("AND S.STATUS=#STATUS \n");
            params.put("STATUS", criteriaVO.getStatus());
        }
        //只顯示已維護
        if( criteriaVO.getActive()!=null && criteriaVO.getActive() ){
            sql.append("AND vc.C_IDS is not null \n");
        }
        
        return sql.toString();
    }
    
    public VenderVO findByVenderVO(VenderVO vo){
        BaseCriteriaVO criteriaVO = new BaseCriteriaVO();
        criteriaVO.setType(vo.getMandt());
        criteriaVO.setCode(vo.getVenderCode());
        List<VenderVO> list = this.findByCriteria(criteriaVO);
        
        return (list!=null && !list.isEmpty())? list.get(0):null;
    }
    
    public boolean findRunningByVenderVO(VenderVO vo){
        BaseCriteriaVO criteriaVO = new BaseCriteriaVO();
        criteriaVO.setType(vo.getMandt());
        criteriaVO.setCode(vo.getVenderCode());
        criteriaVO.setStatus(FormStatusEnum.SIGNING.name());
        List<VenderVO> list = this.findByCriteria(criteriaVO);
        
        return (list!=null && !list.isEmpty());
    }
    
    public VenderVO findById(Long id, boolean fullData) {
        if( id==null ){
            logger.error("findById error id="+id);
            return null;
        }
        BaseCriteriaVO criteriaVO = new BaseCriteriaVO();
        criteriaVO.setId(id);
        criteriaVO.setFullData(fullData);
        
        List<VenderVO> list = findByCriteria(criteriaVO);
//        MemberVO vo = (list!=null && !list.isEmpty() && list.get(0).getMemberId().equals(id))? list.get(0):null;
        VenderVO vo = (list!=null && !list.isEmpty())? list.get(0):null;
        if( vo!=null ){
            
        }
        return vo;
    }
    
}
