/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.facade;

import com.tcci.cm.entity.admin.CmFactory;
import com.tcci.cm.facade.AbstractFacade;
import com.tcci.cm.facade.admin.CmFactoryFacade;
import com.tcci.cm.util.NativeSQLUtils;
import com.tcci.et.model.MemberVenderVO;
import com.tcci.et.entity.EtVenderCategory;
import com.tcci.et.model.criteria.BaseCriteriaVO;
import com.tcci.fc.entity.org.TcUser;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Kyle.Cheng
 */
@Stateless
public class EtVenderCategoryFacade extends AbstractFacade<EtVenderCategory> {
    
    @EJB private CmFactoryFacade cmFactoryFacade;
    
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EtVenderCategoryFacade() {
        super(EtVenderCategory.class);
    }
    
    /**
     * 單筆儲存
     * @param entity
     * @param operator
     * @param simulated
     */
    public void save(EtVenderCategory entity, TcUser operator, boolean simulated){
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
    
    /**
     * 儲存 供應商與分類關聯
     * @param vo
     * @param selectedCategorys
     * @param operator 
     */
    public List<EtVenderCategory> saveCategory(MemberVenderVO vo, List<Long> selectedCategorys, TcUser operator){
        ArrayList<EtVenderCategory> removedC = null;
        
        if( selectedCategorys!=null ){
            Map<String, Object> params = new HashMap<>();
            params.put("mandt", vo.getMandt());
            params.put("venderCode", vo.getVenderCode());
            List<EtVenderCategory> cColl = this.findByNamedQuery("EtVenderCategory.findByVender", params);
            if (cColl == null) {
                cColl = new ArrayList<>();
            }
            // remove unselected category
            removedC = new ArrayList<>();
            if (!cColl.isEmpty()) {
                for (EtVenderCategory entity : cColl) {
                    Long cid = entity.getCategory();
                    boolean bSelected = false;
                    for (Long cidSelected : selectedCategorys) {
                        if (cid.compareTo(cidSelected)==0) {
                            bSelected = true;
                            break;
                        }
                    }
                    if (!bSelected) {
                        removedC.add(entity);
                    }
                }
            }
            
            for (EtVenderCategory entity : removedC) {
                cColl.remove(entity);
                em.remove(em.merge(entity));
            }
                    
            // add new category
            ArrayList<EtVenderCategory> addedC = new ArrayList<>();
            for (Long cidSelected : selectedCategorys) {
                boolean bExisted = false;
                for (EtVenderCategory entity : cColl) {
                    Long cid = entity.getCategory();
                    if (cid.compareTo(cidSelected)==0) {
                        bExisted = true;
                        break;
                    }
                }
                if (!bExisted) {
                    EtVenderCategory newC = new EtVenderCategory();
                    newC.setMandt(vo.getMandt());
                    newC.setVenderCode(vo.getVenderCode());
                    newC.setCategory(cidSelected);
                    addedC.add(newC);
                }
            }
            for (EtVenderCategory entity : addedC) {
                this.save(entity, operator, false);
                cColl.add(entity);
            }
        }
     
        return removedC;
    }
    
    public List<MemberVenderVO> findByCriteria(BaseCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT  \n");
        sql.append("distinct S.MANDT, S.LIFNR_UI venderCode, S.NAME1 CNAME \n");// TC_ZTAB_EXP_LFA1
        sql.append(", vc.C_IDS as cids, vc.C_NAMES as cnames \n"); //ET_VENDER_CATEGORY, ET_OPTION
        
        sql.append(findByCriteriaSQL(criteriaVO, params));

        if( criteriaVO.getOrderBy()!=null ){
            sql.append("ORDER BY ").append(criteriaVO.getOrderBy());
        }else{
            sql.append("ORDER BY vc.C_IDS, S.LIFNR_UI, S.MANDT");
        }
        
        logger.debug("findByCriteria sql = "+sql.toString());
        List<MemberVenderVO> list = null;
        if( criteriaVO.getFirstResult()!=null && criteriaVO.getMaxResults()!=null ){
            list = this.selectBySql(MemberVenderVO.class, sql.toString(), params, criteriaVO.getFirstResult(), criteriaVO.getMaxResults());
        }else if( criteriaVO.getMaxResults()!=null ){
            list = this.selectBySql(MemberVenderVO.class, sql.toString(), params, 0, criteriaVO.getMaxResults());
        }else{
            list = this.selectBySql(MemberVenderVO.class, sql.toString(), params);
        }
        return list;
    }
    public String findByCriteriaSQL(BaseCriteriaVO criteriaVO, Map<String, Object> params){
        StringBuilder sql = new StringBuilder();
        sql.append("FROM TC_ZTAB_EXP_LFA1 S \n");
        
        //供應商 分類
        // for Oracle
        sql.append("LEFT OUTER JOIN ( \n");//系統角色 groups TC_GROUP.NAME
        sql.append("  SELECT A.MANDT, A.LIFNR_UI \n");
        sql.append("  , LISTAGG(A.CATEGORY, ',') WITHIN GROUP (ORDER BY A.CATEGORY) C_IDS \n");
        sql.append("  , LISTAGG(to_char(o.CNAME), ',') WITHIN GROUP (ORDER BY o.SORTNUM) C_NAMES \n");
        sql.append("  FROM ET_VENDER_CATEGORY A \n");
        sql.append("  join ET_OPTION o on o.id = A.CATEGORY AND o.TYPE = 'tenderCategory' \n");
//        if(criteriaVO.getTypeId()!=null){//供應商分類
//            sql.append("AND o.id=#cId \n");
//            params.put("cId", criteriaVO.getTypeId());
//        }
        sql.append("  GROUP BY A.MANDT, A.LIFNR_UI \n");
        sql.append(") vc on vc.MANDT = S.MANDT and vc.LIFNR_UI=S.LIFNR_UI \n");
//        if(criteriaVO.getCategoryId()!=null){
//            sql.append("INNER JOIN ( \n");
//            sql.append("  SELECT A.MANDT, A.LIFNR_UI \n");
//            sql.append("  FROM ET_VENDER_CATEGORY A \n");
//            sql.append("WHERE A.CATEGORY=#cId \n");
//            params.put("cId", criteriaVO.getCategoryId());
//            sql.append(") vc1 on vc1.MANDT = S.MANDT and vc1.LIFNR_UI=S.LIFNR_UI \n");
//        }
        
        sql.append("WHERE 1=1 \n");
        
        //排除黑名單 disabled == false  
        if( criteriaVO.getDisabled()!=null && !criteriaVO.getDisabled() ){
            //排除 "黑名單凍結廠商"(中央採購凍結 CSPERM='X' AND SPERQ='99')
            sql.append("AND S.CSPERM is null  \n");
            sql.append("AND (S.SPERQ!='99' or S.SPERQ is null ) \n");
        }

        if( !StringUtils.isBlank(criteriaVO.getType()) ){//sap client
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
//            sql.append("S.CNAME LIKE #KW OR S.ENAME LIKE #KW \n");
            sql.append("S.NAME1 LIKE #KW \n");
            sql.append("OR S.LIFNR_UI LIKE #KW \n");
            sql.append(") \n");
            params.put("KW", kw);
        }
        
        if(criteriaVO.getTypeId()!=null){//供應商分類
            String cId = "%" + criteriaVO.getTypeId() + "%";
            sql.append("AND vc.C_IDS LIKE #tId \n");
            params.put("tId", cId);
        }
//        if(criteriaVO.getCategoryId()!=null){
//        }
        //只顯示已維護
        if( criteriaVO.getActive()!=null && criteriaVO.getActive() ){
            sql.append("AND vc.C_IDS is not null \n");
        }
        
        return sql.toString();
    }
    
    public MemberVenderVO findByMemberVenderVO(MemberVenderVO vo){
        BaseCriteriaVO criteriaVO = new BaseCriteriaVO();
        criteriaVO.setType(vo.getMandt());
        criteriaVO.setCode(vo.getVenderCode());
        List<MemberVenderVO> list = this.findByCriteria(criteriaVO);
        
        return (list!=null && !list.isEmpty())? list.get(0):null;
    }
    
    public CmFactory findApplyFactory(BaseCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT  \n");
        sql.append("F.ID, F.CODE, S.EKORG \n");
        
        sql.append("FROM TC_ZTAB_EXP_LFA1 S \n");// TC_ZTAB_EXP_LFA1
        sql.append("join CM_FACTORY F on substr(F.CODE, 0, 2) = substr(S.EKORG, 0, 2) \n");
        
        sql.append("WHERE 1=1 \n");
        
        //排除黑名單 disabled == false  
        if( criteriaVO.getDisabled()!=null && !criteriaVO.getDisabled() ){
            //排除 "黑名單凍結廠商"(中央採購凍結 CSPERM='X' AND SPERQ='99')
            sql.append("AND S.CSPERM is null  \n");
            sql.append("AND (S.SPERQ!='99' or S.SPERQ is null ) \n");
        }

        if( !StringUtils.isBlank(criteriaVO.getType()) ){//sap client
            sql.append("AND S.MANDT=#MANDT \n");
            params.put("MANDT", criteriaVO.getType());
        }
        if( !StringUtils.isBlank(criteriaVO.getCode()) ){
            sql.append("AND S.LIFNR_UI=#LIFNR_UI \n");
            params.put("LIFNR_UI", criteriaVO.getCode());
        }
        
        sql.append("ORDER BY S.ERDAT asc");//紀錄建立日期
        
        Query query = em.createNativeQuery(sql.toString());
        for (String key : params.keySet()) {
            query.setParameter(key, params.get(key));
        }
        List list = query.getResultList();
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        
        
        Object[] columns = (Object[]) list.get(0);
//        return (String) columns[1];
        String factoryCode = (String) columns[1];
        CmFactory factory = cmFactoryFacade.getByCode(factoryCode);
        return factory;
    }
    
}
