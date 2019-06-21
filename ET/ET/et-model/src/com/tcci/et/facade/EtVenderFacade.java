/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.facade;

import com.tcci.cm.facade.AbstractFacade;
import com.tcci.cm.util.NativeSQLUtils;
import com.tcci.et.model.MemberVO;
import com.tcci.et.model.VenderVO;
import com.tcci.et.model.rs.LongOptionVO;
import com.tcci.et.entity.EtVender;
import com.tcci.et.model.criteria.BaseCriteriaVO;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.util.StringUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.collections.CollectionUtils;

/**
 *
 * @author Kyle.Cheng
 */
@Stateless
public class EtVenderFacade extends AbstractFacade<EtVender> {
    
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EtVenderFacade() {
        super(EtVender.class);
    }
    
    /**
     * 單筆儲存
     * @param entity
     * @param operator
     * @param simulated
     */
    public void save(EtVender entity, TcUser operator, boolean simulated){
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
        sql.append("SELECT S.ID, S.MAIN_ID, S.MANDT, S.LIFNR_UI venderCode, S.CNAME, S.ENAME, S.NICKNAME \n");
        sql.append(", M.ID MEMBER_ID, M.LOGIN_ACCOUNT, M.NAME, M.PHONE, M.ACTIVE \n");
        sql.append(", vc.C_IDS as cids, vc.C_NAMES as cnames \n"); //ET_VENDER_CATEGORY, ET_OPTION
        
//        if( criteriaVO.isFullData() ){
//            sql.append(", RT.PRATE, RT.NRATE \n");
//            sql.append(", NVL(FS.CC, 0) FAV_COUNT \n");
//            sql.append(", NVL(FP.CC, 0) FAV_PRD_COUNT \n");
//        }
        
        sql.append(findByCriteriaSQL(criteriaVO, params));

        if( criteriaVO.getOrderBy()!=null ){
            sql.append("ORDER BY ").append(criteriaVO.getOrderBy());
        }else{
            sql.append("ORDER BY S.MANDT, S.LIFNR_UI");
        }
        
        logger.debug("findByCriteria sql:"+sql.toString());
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
    
    /**
     * 依輸入條件查詢
     * @param criteriaVO
     * @param params
     * @return 
     */
    public String findByCriteriaSQL(BaseCriteriaVO criteriaVO, Map<String, Object> params){
        StringBuilder sql = new StringBuilder();
        sql.append("FROM ET_VENDER S \n");
//        sql.append("JOIN ET_MEMBER M ON M.ID=S.MEMBER_ID AND M.DISABLED=0 AND M.ACTIVE=1 \n");
        sql.append("JOIN ET_MEMBER M ON M.ID=S.MAIN_ID \n");
        //供應商 分類
        // for Oracle
        sql.append("LEFT OUTER JOIN ( \n");//系統角色 groups TC_GROUP.NAME
        sql.append("  SELECT A.MANDT, A.LIFNR_UI \n");
        sql.append("  , LISTAGG(A.CATEGORY, ',') WITHIN GROUP (ORDER BY A.CATEGORY) C_IDS \n");
        sql.append("  , LISTAGG(to_char(o.CNAME), ',') WITHIN GROUP (ORDER BY o.SORTNUM) C_NAMES \n");
        sql.append("  FROM ET_VENDER_CATEGORY A \n");
        sql.append("  join ET_OPTION o on o.id = A.CATEGORY AND o.TYPE = 'tenderCategory' \n");
        sql.append("  GROUP BY A.MANDT, A.LIFNR_UI \n");
        sql.append(") vc on vc.MANDT = S.MANDT and vc.LIFNR_UI=S.LIFNR_UI \n");
//        sql.append("LEFT OUTER JOIN TC_ZTAB_EXP_LFA1 L ON L.MANDT=S.MANDT AND L.LIFNR_UI=S.LIFNR_UI \n");
//        sql.append("LEFT OUTER JOIN ( \n");
//        sql.append(" SELECT DISTINCT MANDT, LIFNR_UI \n");
//        sql.append(", CSPERM, SPERR, ZAHLS \n");
//        sql.append(" FROM TC_ZTAB_EXP_LFA1 \n");
//        sql.append(") L ON L.MANDT=S.MANDT AND L.LIFNR_UI=S.LIFNR_UI \n");
        sql.append("WHERE 1=1 \n");
        if( criteriaVO.getId()!=null ){
            sql.append("AND S.ID=#ID \n");
            params.put("ID", criteriaVO.getId());
        }
        if( criteriaVO.getMemberId()!=null ){
            sql.append("AND M.ID=#MEMBER_ID \n");
            params.put("MEMBER_ID", criteriaVO.getMemberId());
        }
        if( !StringUtils.isBlank(criteriaVO.getType()) ){
            sql.append("AND S.MANDT=#MANDT \n");
            params.put("MANDT", criteriaVO.getType());
        }
        if( !StringUtils.isBlank(criteriaVO.getCode()) ){
            sql.append("AND S.LIFNR_UI=#LIFNR_UI \n");
            params.put("LIFNR_UI", criteriaVO.getCode());
        }
//        if( criteriaVO.getDisabled()!=null ){
//            sql.append("AND S.DISABLED=#DISABLED \n");
//            params.put("DISABLED", criteriaVO.getDisabled());
//        }
        
        if( !StringUtils.isBlank(criteriaVO.getKeyword()) ){
            String kw = "%" + criteriaVO.getKeyword().trim() + "%";
            sql.append("AND ( \n");
            sql.append("S.CNAME LIKE #KW OR S.ENAME LIKE #KW \n");
            sql.append("OR S.LIFNR_UI LIKE #KW \n");
            sql.append(") \n");
            params.put("KW", kw);
        }
        
        return sql.toString();
    }
    
    /**
     * 指定會員的供應商
     * @param memberVO
     * @return 
     */
    public List<LongOptionVO> findByMemberVenderOptions(MemberVO memberVO){
        BaseCriteriaVO criteriaVO = new BaseCriteriaVO();
        criteriaVO.setMemberId(memberVO.getMemberId());
        criteriaVO.setActive(Boolean.TRUE);
        criteriaVO.setDisabled(Boolean.FALSE);
        
        List<LongOptionVO> ops = new ArrayList<>();
        List<VenderVO> list = this.findByCriteria(criteriaVO);
        if( list!=null ){
            logger.debug("findByMemberVenderOptions list = "+list.size());
            for(VenderVO vo : list){
                ops.add(new LongOptionVO(vo.getId(), vo.getLabel()));
            }
        }
        
        memberVO.setVenders(ops.isEmpty()?null:ops);
        return ops;
    }
    
    public List<VenderVO> findByMemberId(Long memberId){
        BaseCriteriaVO criteriaVO = new BaseCriteriaVO();
        criteriaVO.setMemberId(memberId);
        criteriaVO.setActive(Boolean.TRUE);
        criteriaVO.setDisabled(Boolean.FALSE);
        return this.findByCriteria(criteriaVO);
    }
    public List<EtVender> findByMainId(Long mainId){
        Query q = em.createNamedQuery("EtVender.findByMainId");
        q.setParameter("mainId", mainId);
        return q.getResultList();
    }
    
    public List<VenderVO> findLfa1ByCriteria(BaseCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append(findLfa1ByCriteriaSQL(criteriaVO, params));

        if( criteriaVO.getOrderBy()!=null ){
            sql.append("ORDER BY ").append(criteriaVO.getOrderBy());
        }else{
            sql.append("ORDER BY S.MANDT, S.LIFNR_UI");
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
    public String findLfa1ByCriteriaSQL(BaseCriteriaVO criteriaVO, Map<String, Object> params){
        boolean countOnly = (criteriaVO!=null)? criteriaVO.isCountOnly():false;
        
        StringBuilder sql = new StringBuilder();
        if( countOnly ){
            sql.append("SELECT count(1) \n");
        }else{
            sql.append("SELECT  \n");
            sql.append("distinct S.MANDT, S.LIFNR_UI venderCode, S.NAME1 CNAME \n");
        }
        sql.append("FROM TC_ZTAB_EXP_LFA1 S \n");
        sql.append("WHERE 1=1 \n");
        
        //排除黑名單 disabled == false  
        if( criteriaVO.getDisabled()!=null && !criteriaVO.getDisabled() ){
            //排除 "黑名單凍結廠商"(中央採購凍結 CSPERM='X' AND SPERQ='99')
            sql.append("AND S.CSPERM is null  \n");
            sql.append("AND (S.SPERQ!='99' or S.SPERQ is null ) \n");
        }

        if( !StringUtils.isBlank(criteriaVO.getType()) ){
            sql.append("AND S.MANDT=#MANDT \n");
            params.put("MANDT", criteriaVO.getType());
        }
        if( !StringUtils.isBlank(criteriaVO.getCode()) ){
            sql.append("AND S.LIFNR_UI=#LIFNR_UI \n");
            params.put("LIFNR_UI", criteriaVO.getCode());
        }
        //廠別
        if (CollectionUtils.isNotEmpty(criteriaVO.getFactoryList())) {
            String pColumnName = "SUBSTR(S.EKORG,0,2)";
            List<String> pValueList = new ArrayList();
            for(String factoryCode:criteriaVO.getFactoryList()){
                logger.info("findByCriteriaSQL factoryCode:"+factoryCode);
                String columnValue = factoryCode.substring(0, 2);//取前兩碼
                pValueList.add(columnValue);
            }
            sql.append((NativeSQLUtils.getInSQL(pColumnName, pValueList, params))).append(" \n");
        }
//        if (!criteriaVO.isQueryAllFactories()) {
//            List<TcFactoryVO> queryFactories = criteriaVO.getQueryFactories();
//            if (CollectionUtils.isNotEmpty(queryFactories)) {
//                String pColumnName = "SUBSTR(S.EKORG,0,2)";
//                List<String> pValueList = new ArrayList();
//
//                for (TcFactoryVO tcFactoryVO : queryFactories) {
//                    String columnValue = tcFactoryVO.getFactoryCode().substring(0, 2);//取前兩碼
//
//                    pValueList.add(columnValue);
//                }
//                sql.append((NativeSQLUtils.getInSQL(pColumnName, pValueList, params))).append(" \n");
//            }
//        }
        
        if( !StringUtils.isBlank(criteriaVO.getKeyword()) ){
            String kw = "%" + criteriaVO.getKeyword().trim() + "%";
            sql.append("AND ( \n");
            sql.append("S.CNAME LIKE #KW OR S.ENAME LIKE #KW \n");
            sql.append("OR S.LIFNR_UI LIKE #KW \n");
            sql.append(") \n");
            params.put("KW", kw);
        }
        
        return sql.toString();
    }
    
    public int countLfa1ByCriteria(BaseCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        criteriaVO.setCountOnly(true);
        sql.append(findLfa1ByCriteriaSQL(criteriaVO, params));
        
        return this.count(sql.toString(), params);
    }
    
}
