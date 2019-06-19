/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.dw.facade;

import com.tcci.et.model.criteria.MmCriteriaVO;
import com.tcci.et.model.dw.T006aVO;
import com.tcci.et.model.dw.T024VO;
import com.tcci.et.model.dw.T024eVO;
import com.tcci.et.model.dw.T052uVO;
import com.tcci.et.model.dw.T161tVO;
import com.tcci.ec.model.rs.StrOptionVO;
import com.tcci.et.model.criteria.PrCriteriaVO;
import com.tcci.fc.util.StringUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;

/**
 *
 * @author Peter.pan
 */
@Stateless
public class MmDwFacade extends AbstractFacadeNE {
    
    /**
     * TC_ZTAB_EXP_T024 採購群組
     * @param criteriaVO
     * @return 
     */
    public List<T024VO> findPurGroups(MmCriteriaVO criteriaVO){
        if( criteriaVO==null ){
            return null;
        }
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT S.* \n");
        sql.append("FROM TC_ZTAB_EXP_T024 S \n");
        sql.append("WHERE 1=1 \n");
        sql.append("AND S.MANDT=#MANDT \n");
        
        params.put("MANDT", criteriaVO.getMandt());
        
        if( StringUtils.isNotBlank(criteriaVO.getKeyword()) ){
            String kw = "%"+criteriaVO.getKeyword()+"%";
            sql.append("AND (S.EKNAM LIKE #KW OR S.EKGRP LIKE #KW) \n");
            params.put("KW", kw);
        }
        
        sql.append("ORDER BY S.EKNAM, S.EKGRP \n");
        
        return this.selectBySql(T024VO.class, sql.toString(), params);
    }
    
    /**
     * TC_ZTAB_EXP_T024E 採購組織
     * @param criteriaVO
     * @return 
     */
    public List<T024eVO> findPurOrgs(MmCriteriaVO criteriaVO){
        if( criteriaVO==null ){
            return null;
        }
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT S.* \n");
        sql.append("FROM TC_ZTAB_EXP_T024E S \n");
        sql.append("WHERE 1=1 \n");
        sql.append("AND S.MANDT=#MANDT \n");
        params.put("MANDT", criteriaVO.getMandt());
        
        if( criteriaVO.getBukrs()!=null ){
            sql.append("AND S.BUKRS=#BUKRS \n");
            params.put("BUKRS", criteriaVO.getBukrs());
        }
        
        if( StringUtils.isNotBlank(criteriaVO.getKeyword()) ){
            String kw = "%"+criteriaVO.getKeyword()+"%";
            sql.append("AND (S.EKOTX LIKE #KW OR S.EKORG LIKE #KW) \n");
            params.put("KW", kw);
        }
        
        sql.append("ORDER BY S.EKOTX, S.EKORG \n");
        
        return this.selectBySql(T024eVO.class, sql.toString(), params);
    }
    
    /**
     * TC_ZTAB_EXP_T006A 單位
     * @param criteriaVO
     * @return 
     */
    public List<T006aVO> findUnits(MmCriteriaVO criteriaVO){
        if( criteriaVO==null ){
            return null;
        }
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT S.* \n");
        sql.append("FROM TC_ZTAB_EXP_T006A S \n");
        sql.append("WHERE 1=1 \n");
        sql.append("AND S.MANDT=#MANDT \n");
        params.put("MANDT", criteriaVO.getMandt());
        
        if( criteriaVO.getSpras()!=null ){
            sql.append("AND S.SPRAS=#SPRAS \n");
            params.put("SPRAS", criteriaVO.getSpras());
        }
        
        if( criteriaVO.getDimid()!=null ){
            sql.append("AND S.DIMID=#DIMID \n");
            params.put("DIMID", criteriaVO.getDimid());
        }

        sql.append("ORDER BY S.MSEHL, S.MSEHI \n");
        
        return this.selectBySql(T006aVO.class, sql.toString(), params);
    }

    /**
     * TC_ZTAB_EXP_T052U 付款條件
     * @param criteriaVO
     * @return 
     */
    public List<T052uVO> findPayConds(MmCriteriaVO criteriaVO){
        if( criteriaVO==null ){
            return null;
        }
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT S.* \n");
        sql.append("FROM TC_ZTAB_EXP_T052U S \n");
        sql.append("WHERE 1=1 \n");
        sql.append("AND S.MANDT=#MANDT \n");
        params.put("MANDT", criteriaVO.getMandt());
        
        if( criteriaVO.getSpras()!=null ){
            sql.append("AND S.SPRAS=#SPRAS \n");
            params.put("SPRAS", criteriaVO.getSpras());
        }

        sql.append("ORDER BY S.ZTERM, S.TEXT1 \n");
        
        return this.selectBySql(T052uVO.class, sql.toString(), params);
    }
    
    public List<StrOptionVO> findCountryOptionsByCriteria(PrCriteriaVO criteriaVO){
        if( criteriaVO==null ){
            return null;
        }
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT distinct S.LAND1 as value, S.LANDX as label  \n");
        sql.append("FROM TC_ZTAB_EXP_T005T S \n");
        sql.append("join TC_SAPCLIENT TS on S.MANDT = TS.CLIENT and S.SPRAS = TS.LANGUAGE \n");
        sql.append("WHERE 1=1 \n");
        if( StringUtils.isNotBlank(criteriaVO.getCode()) ){
            sql.append("AND TS.CODE=#CODE \n");
        }
        if( StringUtils.isNotBlank(criteriaVO.getLand1()) ){
            sql.append("AND S.LAND1=#LAND1 \n");
        }
        sql.append("ORDER BY S.LAND1 \n");
        
        if( StringUtils.isNotBlank(criteriaVO.getCode()) ){
            params.put("CODE", criteriaVO.getCode());
        }
        if( StringUtils.isNotBlank(criteriaVO.getLand1()) ){
            params.put("LAND1", criteriaVO.getLand1());
        }
        
        return this.selectBySql(StrOptionVO.class, sql.toString(), params);
    }
    
    public List<StrOptionVO> findCurrencyOptionsByCriteria(PrCriteriaVO criteriaVO){
        if( criteriaVO==null ){
            return null;
        }
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT distinct S.WAERS as value, S.LTEXT as label \n");
        sql.append("FROM TC_ZTAB_EXP_TCURT S \n");
        sql.append("join TC_SAPCLIENT TS on S.MANDT = TS.CLIENT and S.SPRAS = TS.LANGUAGE \n");
        sql.append("WHERE 1=1 \n");
        if( StringUtils.isNotBlank(criteriaVO.getCode()) ){
            sql.append("AND TS.CODE=#CODE \n");
        }
        if( StringUtils.isNotBlank(criteriaVO.getWaers()) ){
            sql.append("AND S.WAERS=#WAERS \n");
        }
        sql.append("ORDER BY S.WAERS \n");
        
        if( StringUtils.isNotBlank(criteriaVO.getCode()) ){
            params.put("CODE", criteriaVO.getCode());
        }
        if( StringUtils.isNotBlank(criteriaVO.getWaers()) ){
            params.put("WAERS", criteriaVO.getWaers());
        }
        
        return this.selectBySql(StrOptionVO.class, sql.toString(), params);
    }

    /**
     * TC_ZTAB_EXP_T161T 採購文件類型
     * @param criteriaVO
     * @return 
     */
    public List<T161tVO> findDocTypes(MmCriteriaVO criteriaVO){
        if( criteriaVO==null ){
            return null;
        }
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT S.* \n");
        sql.append("FROM TC_ZTAB_EXP_T161T S \n");
        sql.append("WHERE 1=1 \n");
        sql.append("AND S.MANDT=#MANDT \n");
        params.put("MANDT", criteriaVO.getMandt());
        
        if( criteriaVO.getSpras()!=null ){
            sql.append("AND S.SPRAS=#SPRAS \n");
            params.put("SPRAS", criteriaVO.getSpras());
        }
        if( criteriaVO.getBstyp()!=null ){
            sql.append("AND S.BSTYP=#BSTYP \n");
            params.put("BSTYP", criteriaVO.getBstyp());
        }

        sql.append("ORDER BY S.BSART \n");
        
        return this.selectBySql(T161tVO.class, sql.toString(), params);
    }
    
/*
SELECT * FROM TC_ZTAB_EXP_TCURT -- 幣別
SELECT * FROM TC_ZTAB_EXP_T005T -- 國別   
*/    
}
