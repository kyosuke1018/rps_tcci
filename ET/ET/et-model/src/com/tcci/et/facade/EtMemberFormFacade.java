/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.facade;

import com.tcci.cm.facade.AbstractFacade;
import com.tcci.cm.util.NativeSQLUtils;
import com.tcci.dw.facade.MmDwFacade;
import com.tcci.et.model.MemberVO;
import com.tcci.et.model.criteria.MemberCriteriaVO;
import com.tcci.et.entity.EtMemberForm;
import com.tcci.fc.entity.bpm.TcProcess;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.bpm.IBPMEngine;
import com.tcci.fc.util.StringUtils;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.collections.CollectionUtils;

/**
 *
 * @author Kyle.Cheng
 */
@Stateless
public class EtMemberFormFacade extends AbstractFacade<EtMemberForm> {
    
    @Inject
    protected IBPMEngine bpmEngine;
    
    @EJB MmDwFacade mmDwFacade;
    
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EtMemberFormFacade() {
        super(EtMemberForm.class);
    }
    
    /**
     * 單筆儲存
     * @param entity
     * @param operator
     * @param simulated
     */
    public void save(EtMemberForm entity, TcUser operator, boolean simulated){
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
    
    public List<EtMemberForm> findByMember(Long memberId) {
        Query q = em.createNamedQuery("EtMemberForm.findByMember");
        q.setParameter("memberId", memberId);
        return q.getResultList();
    }
    
    
    /*
     * 啟動簽核流程
     */
    public void startProcess(EtMemberForm form, TcUser operator, Map<String, Object> roleApprovers, String formTypeCode) {
        TcProcess process = bpmEngine.createProcess(operator, formTypeCode, roleApprovers, "", form);
        logger.info("startProcess... processID = " + process.getId());
        if (process != null) {
            form.setProcess(process);
            this.save(form, operator, Boolean.FALSE);
        }
//        return process;
    }
    
    /**
     * 查詢
     * @param criteriaVO
     * @param locale
     * @return 
     */
    public List<MemberVO> findByCriteria(MemberCriteriaVO criteriaVO, Locale locale){
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT S.ID, S.MEMBER_ID, S.TYPE \n");
        sql.append(", S.FACTORY_ID as factoryId, F.NAME as factoryName , S.MANDT \n");
        sql.append(", S.PROCESS as processId, S.STATUS \n");
        sql.append(", P.STARTTIMESTAMP as starttime, P.ENDTIMESTAMP as endtime, P.EXECUTIONSTATE as executionstate \n");
        sql.append(", S.APPLY_VENDER_CODE,  S.APPLY_VENDER_NAME \n");
        sql.append(", M.LOGIN_ACCOUNT, M.NAME, M.EMAIL, M.PHONE, M.ACTIVE, M.DISABLED \n");
        //新商 公司資訊
        sql.append(", C.ID_CODE, C.OWNER1, C.START_AT \n");
//        sql.append(", C.COUNTRY, concat(country.CODE, country.CNAME) as countryName \n");
//        sql.append(", C.CURRENCY, concat(curr.CODE, curr.CNAME) as currencyName \n");
//        sql.append(", C.COUNTRY, country.CODE as countryCode, country.CNAME as countryName \n");
//        sql.append(", C.CURRENCY, curr.CODE  as currencyCode, curr.CNAME as currencyName \n");
        sql.append(", C.COUNTRY_CODE as countryCode \n");
        sql.append(", C.CURRENCY_CODE as currencyCode \n");
        sql.append(", C.CAPITAL, C.YEAR_REVENUE, C.EMPLOYEE \n");
        sql.append(", C.TYPE as venderType, C.INDUSTRY \n");
        sql.append(", C.BRIEF, C.BRIEF2, C.BRIEF3 \n");
        //聯絡人
        sql.append(", C.OWNER2, C.OWNER2_TITLE, C.TEL1, C.TEL2, C.TEL3, C.TEL4, C.URL2, C.ADDR1, C.ADDR2 \n");
        //銀行
        sql.append(", C.BANK1, C.BANK1_BENEFICIARY, C.BANK1_CODE, C.BANK1_ACCOUNT, C.BANK1_ADDR \n");

        sql.append(findByCriteriaSQL(criteriaVO, params));
        
        if( criteriaVO.getOrderBy()!=null ){
            sql.append("ORDER BY ").append(criteriaVO.getOrderBy());
        }else{
            sql.append("ORDER BY S.MODIFYTIME desc");
        }
        
        List<MemberVO> list = null;
        if( criteriaVO.getFirstResult()!=null && criteriaVO.getMaxResults()!=null ){
            list = this.selectBySql(MemberVO.class, sql.toString(), params, criteriaVO.getFirstResult(), criteriaVO.getMaxResults());
        }else if( criteriaVO.getMaxResults()!=null ){
            list = this.selectBySql(MemberVO.class, sql.toString(), params, 0, criteriaVO.getMaxResults());
        }else{
            list = this.selectBySql(MemberVO.class, sql.toString(), params);
        }
        
        return list;
    }
    
    /**
     * 依輸入條件查詢
     * @param criteriaVO
     * @param params
     * @return 
     */
    public String findByCriteriaSQL(MemberCriteriaVO criteriaVO, Map<String, Object> params){
        StringBuilder sql = new StringBuilder();
        sql.append("FROM ET_MEMBER_FORM S \n");
        sql.append("INNER JOIN ET_MEMBER M on M.ID = S.MEMBER_ID \n");
        sql.append("INNER JOIN CM_FACTORY F on F.ID = S.FACTORY_ID \n");
        sql.append("INNER JOIN TC_PROCESS P on P.ID = S.PROCESS \n");
        sql.append("LEFT OUTER JOIN ET_COMPANY C on C.ID = S.COMPANY_ID \n");
//        sql.append("LEFT OUTER JOIN ET_OPTION country on country.ID = C.COUNTRY \n");
//        sql.append("LEFT OUTER JOIN ET_OPTION curr on curr.ID = C.CURRENCY \n");
        
        sql.append("WHERE 1=1 \n");
//        sql.append("AND S.DISABLED=0 \n");

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
        
        return sql.toString();
    }
    
    public MemberVO findById(Long id, boolean fullData, Locale locale) {
        if( id==null ){
            logger.error("findById error id="+id);
            return null;
        }
        MemberCriteriaVO criteriaVO = new MemberCriteriaVO();
        criteriaVO.setId(id);
        criteriaVO.setFullData(fullData);
        
        List<MemberVO> list = findByCriteria(criteriaVO, locale);
//        MemberVO vo = (list!=null && !list.isEmpty() && list.get(0).getMemberId().equals(id))? list.get(0):null;
        MemberVO vo = (list!=null && !list.isEmpty())? list.get(0):null;
        if( vo!=null ){
            
        }
        return vo;
    }
    
    public boolean isStatusChanged(EtMemberForm entity) {
        EtMemberForm now = find(entity.getId());
        return now.getStatus() != entity.getStatus();
    }
    
}
