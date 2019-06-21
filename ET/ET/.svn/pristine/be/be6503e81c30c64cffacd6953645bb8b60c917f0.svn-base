/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.dw.facade;

import com.tcci.et.model.criteria.PrCriteriaVO;
import com.tcci.et.model.dw.PrEbanPmVO;
import com.tcci.et.model.dw.PrEbanVO;
import com.tcci.et.model.dw.PrEbantxHeadVO;
import com.tcci.et.model.dw.PrEbantxItemVO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;

/**
 * SAP PR
 * @author Peter.pan
 */
@Stateless
public class PrDwFacade extends AbstractFacadeNE {
    
    /**
     * TC_PR_ZTAB_EXP_EBAN
     * @param criteriaVO
     * @return 
     */
    public List<PrEbanVO> findByEbanByCriteria(PrCriteriaVO criteriaVO){
        if( criteriaVO==null ){
            return null;
        }
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT S.* \n");
        sql.append("FROM TC_PR_ZTAB_EXP_EBAN S \n");
        // LEFT OUTER JOIN TC_PR_ZTAB_EXP_EBKN K ON K.MANDT=S.MANDT AND K.BANFN=S.BANFN AND K.BNFPO=S.BNFPO \n");
        sql.append("WHERE 1=1 \n");
        sql.append("AND S.MANDT=#MANDT \n");
        sql.append("AND S.BANFN=#BANFN \n"); // AND S.BANFN='1130011226'
        sql.append("AND S.LOEKZ IS NULL \n"); // 刪除指示碼
        //sql.append("AND S.STATU<>'B' \n"); // 請購處理狀態
        sql.append("AND (S.FRGKZ='R' OR S.FRGKZ IS NULL) \n"); // 核發指示碼 為R 或 不需簽核
        
        params.put("MANDT", criteriaVO.getMandt());
        params.put("BANFN", criteriaVO.getBanfn());

        if( criteriaVO.getWerks()!=null ){// 廠別
            sql.append("AND S.WERKS=#WERKS \n");
            params.put("WERKS", criteriaVO.getWerks());
        }
        if( criteriaVO.getBadatS()!=null ){
            sql.append("AND S.BADAT>=#BADAT_S"); //TO_DATE('20190501', 'yyyyMMdd') \n");
            params.put("BADAT_S", criteriaVO.getBadatS());
        }
        if( criteriaVO.getBadatE()!=null ){
            sql.append("AND S.BADAT<=#BADAT_E");
            params.put("BADAT_E", criteriaVO.getBadatE());
        }
        
        sql.append("ORDER BY S.BNFPO \n");

        return this.selectBySql(PrEbanVO.class, sql.toString(), params);
    }
    
    /**
     * TC_PR_ZTAB_EXP_EBANTX_HEAD
     * @param criteriaVO
     * @return 
     */
    public List<PrEbantxHeadVO> findByEbantxHeadByCriteria(PrCriteriaVO criteriaVO){
        if( criteriaVO==null ){
            return null;
        }
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT S.* \n");
        sql.append("FROM TC_PR_ZTAB_EXP_EBANTX_HEAD S \n");
        sql.append("WHERE 1=1 \n");
        sql.append("AND S.MANDT=#MANDT \n");
        sql.append("AND S.BANFN=#BANFN \n");
        sql.append("ORDER BY S.TDID, S.LINE_NO \n");
        
        params.put("MANDT", criteriaVO.getMandt());
        params.put("BANFN", criteriaVO.getBanfn());
        
        return this.selectBySql(PrEbantxHeadVO.class, sql.toString(), params);
    }
    
    /**
     * TC_PR_ZTAB_EXP_EBANTX_HEAD
     * @param criteriaVO
     * @return 
     */
    public List<PrEbantxItemVO> findByEbantxItemByCriteria(PrCriteriaVO criteriaVO){
        if( criteriaVO==null ){
            return null;
        }
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT S.* \n");
        sql.append("FROM TC_PR_ZTAB_EXP_EBANTX_ITEM S \n");
        sql.append("WHERE 1=1 \n");

        sql.append("AND S.MANDT=#MANDT \n");
        sql.append("AND S.BANFN=#BANFN \n");
        params.put("MANDT", criteriaVO.getMandt());
        params.put("BANFN", criteriaVO.getBanfn());
        
        if( criteriaVO.getBnfpo()!=null ){
            sql.append("AND S.BNFPO=#BNFPO \n");
            params.put("BNFPO", criteriaVO.getBnfpo());
        }

        sql.append("ORDER BY S.TDID, S.LINE_NO \n");
        
        return this.selectBySql(PrEbantxItemVO.class, sql.toString(), params);
    }
    
    /**
     * TC_PR_ZTAB_EXP_EBAN_PM
     * @param criteriaVO
     * @return 
     */
    public List<PrEbanPmVO> findByEbanPmByCriteria(PrCriteriaVO criteriaVO){
        if( criteriaVO==null ){
            return null;
        }
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT S.* \n");
        sql.append("FROM TC_PR_ZTAB_EXP_EBAN_PM S \n");
        sql.append("WHERE 1=1 \n");
        
        sql.append("AND S.MANDT=#MANDT \n");
        sql.append("AND S.BANFN=#BANFN \n");
        params.put("MANDT", criteriaVO.getMandt());
        params.put("BANFN", criteriaVO.getBanfn());

        if( criteriaVO.getBnfpo()!=null ){
            sql.append("AND S.BNFPO=#BNFPO \n");
            params.put("BNFPO", criteriaVO.getBnfpo());
        }
        
        sql.append("ORDER BY S.EXTROW \n");
        
        return this.selectBySql(PrEbanPmVO.class, sql.toString(), params);
    }
    public List<PrEbanPmVO> findByEbanPmByKey(String mandt, String banfn, Integer bnfpo){
        PrCriteriaVO criteriaVO = new PrCriteriaVO();
        criteriaVO.setMandt(mandt);
        criteriaVO.setBanfn(banfn);
        criteriaVO.setBnfpo(bnfpo);
        
        return findByEbanPmByCriteria(criteriaVO);
    }
}
