/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.facade.sap;

import com.tcci.cm.facade.AbstractFacadeNE;
import com.tcci.et.model.dw.T006aVO;
import com.tcci.et.model.dw.T024VO;
import com.tcci.et.model.dw.T024eVO;
import com.tcci.et.model.dw.T052uVO;
import com.tcci.fc.util.ResultSetHelper;
import com.tcci.sap.jco.entity.TcRfcZtab;
import com.tcci.sap.jco.model.StringRegionVO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * 
SELECT * FROM TC_SAPCLIENT -- 522
SELECT * FROM TC_ZTAB_EXP_T024 WHERE MANDT='522' -- 採購群組 545
SELECT * FROM TC_ZTAB_EXP_T024E WHERE MANDT='522' -- 採購組織 89
SELECT * FROM TC_ZTAB_EXP_T006A WHERE MANDT='268' AND SPRAS='1' -- 單位 1037, 268
SELECT * FROM TC_ZTAB_EXP_T052U WHERE MANDT='268' AND SPRAS='1' -- 付款條件 291, 133
SELECT * FROM TC_ZTAB_EXP_T161T WHERE MANDT='268' AND SPRAS='1' -- 採購文件類型 112
SELECT * FROM TC_ZTAB_EXP_TCURT WHERE MANDT='268' AND SPRAS='1' -- 幣別 31044, 7488
SELECT * FROM TC_ZTAB_EXP_T005T WHERE MANDT='268' AND SPRAS='1' -- 國別 988, 241
 * @author Peter.pan
 */
@Stateless
public class SapDataFacade extends AbstractFacadeNE {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    /**
     * 採購群組
     * @param sapClientCode
     * @param incStopItem
     * @return 
     */
    public List<T024VO> findPurGroup(String sapClientCode, boolean incStopItem){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT S.* \n");
        sql.append("FROM TC_ZTAB_EXP_T024 S \n");
        sql.append("JOIN TC_SAPCLIENT C ON C.CLIENT=S.MANDT \n");
        sql.append("WHERE 1=1 \n");
        sql.append("AND C.CODE=#CODE \n");
        
        if( !incStopItem ){
            sql.append("AND S.EKNAM NOT LIKE '%(停用)' \n");
        }
        
        params.put("CODE", sapClientCode);

        sql.append("ORDER BY S.EKNAM");
        
        List<T024VO> list = this.selectBySql(T024VO.class, sql.toString(), params);
        
        return list;
    }

    /**
     * 採購組織
     * @param sapClientCode
     * @param bukrs
     * @return 
     */
    public List<T024eVO> findPurOrg(String sapClientCode, String bukrs){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT S.* \n");
        sql.append("FROM TC_ZTAB_EXP_T024E S \n");
        sql.append("JOIN TC_SAPCLIENT C ON C.CLIENT=S.MANDT \n");
        sql.append("WHERE 1=1 \n");
        sql.append("AND C.CODE=#CODE \n");
        sql.append("AND S.BUKRS=#BUKRS \n");
        
        params.put("CODE", sapClientCode);
        params.put("BUKRS", bukrs);

        sql.append("ORDER BY S.EKORG");
        
        List<T024eVO> list = this.selectBySql(T024eVO.class, sql.toString(), params);
        
        return list;
    }

    /**
     * 計價單位
     * @param sapClientCode
     * @param lang  ( M, 1, E )
     * @param dimid
     * @return 
     */
    public List<T006aVO> findPriceUnit(String sapClientCode, String lang, String dimid){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT S.* \n");
        sql.append("FROM TC_ZTAB_EXP_T006A S \n");
        sql.append("JOIN TC_SAPCLIENT C ON C.CLIENT=S.MANDT \n");
        sql.append("WHERE 1=1 \n");
        sql.append("AND C.CODE=#CODE \n");
        sql.append("AND S.SPRAS=#SPRAS \n");
        
        params.put("CODE", sapClientCode);
        params.put("SPRAS", lang);
        params.put("DIMID", dimid);

        sql.append("ORDER BY S.MSEHI");
        
        List<T006aVO> list = this.selectBySql(T006aVO.class, sql.toString(), params);
        
        return list;
    }

    /**
     * 付款條件
     * @param sapClientCode
     * @param lang  ( M, 1, E )
     * @return 
     */
    public List<T052uVO> findPayCond(String sapClientCode, String lang){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT S.* \n");
        sql.append("FROM TC_ZTAB_EXP_T052U S \n");
        sql.append("JOIN TC_SAPCLIENT C ON C.CLIENT=S.MANDT \n");
        sql.append("WHERE 1=1 \n");
        sql.append("AND C.CODE=#CODE \n");
        sql.append("AND S.SPRAS=#SPRAS \n");
        
        params.put("CODE", sapClientCode);
        params.put("SPRAS", lang);

        sql.append("ORDER BY S.ZTERM");
        
        List<T052uVO> list = this.selectBySql(T052uVO.class, sql.toString(), params);
        
        return list;
    }
   
    public void persist(TcRfcZtab tcRfcZtab){
        //logger.debug("persist clazz = "+tcRfcZtab.getClass());
        //logger.debug("persist tcRfcZtab.getPrimaryKey() = "+tcRfcZtab.getPrimaryKey());
        Object existedObject = em.find(tcRfcZtab.getClass(), tcRfcZtab.getPrimaryKey());
        if( existedObject==null ){
            em.persist(tcRfcZtab);
        }else{
            em.merge(tcRfcZtab);
        }
    }
    
    /**
     * 抓取各段代號區間的起訖代號
     * @param mandt
     * @param tableName
     * @param fieldName
     * @param regionNum
     * @return 
     */
    public List<StringRegionVO> findRegions(String mandt, String tableName, String fieldName, int regionNum){
        logger.debug("findRegions ...");
        if( regionNum<2 ){
            logger.error("regionNum 需大於 2 !");
            return null;
        }
        
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select rownum, ").append(fieldName).append(" as highValue \n");
        sql.append("from ( \n");
        sql.append("     select distinct ").append(fieldName).append(" from ").append(tableName).append(" where mandt=#mandt order by ").append(fieldName).append(" \n");
        sql.append(") A");
        params.put("mandt", mandt);
        
        ResultSetHelper<StringRegionVO> resultSetHelper = new ResultSetHelper(StringRegionVO.class);
        List<StringRegionVO> allList = resultSetHelper.queryToPOJOList(em, sql.toString(), params);
        
        List<StringRegionVO> resList = new ArrayList<StringRegionVO>();
        if( allList!=null && !allList.isEmpty() ){
            int idx, sno = 0;
            int num = allList.size()/(regionNum-2);
            
            String lastValue = "0";
            // 設定 0 ~ regionNum 區間 Low Value
            for(idx=0; idx<allList.size(); idx=idx+num){
                StringRegionVO regionVO = allList.get(idx);// regionVO.getHighValue()
                logger.info("son ="+Integer.toString(sno)+"; low ="+lastValue+"; high ="+regionVO.getHighValue());
                regionVO.setSno(sno++);
                regionVO.setLowValue(lastValue);
                
                resList.add(regionVO);
                lastValue = regionVO.getHighValue();
            }
            
            if( idx-num > allList.size()-1 ){// 非整除的狀況
                StringRegionVO regionVO = allList.get(allList.size()-1);// regionVO.getHighValue()
                logger.info("son ="+regionVO.getSno()+"; low ="+lastValue+"; high ="+regionVO.getHighValue());
                regionVO.setSno(sno++);
                regionVO.setLowValue(lastValue);
                
                resList.add(regionVO);
                lastValue = regionVO.getHighValue();
            }
            
            // 增加一最後區間
            StringRegionVO lastVO = new StringRegionVO();
            lastVO.setSno(sno);
            lastVO.setLowValue(lastValue);
            StringBuilder lastHighValue = new StringBuilder();
            for(int i=0; i<lastValue.length(); i++){
                lastHighValue.append("Z");
            }
            lastVO.setHighValue(lastHighValue.toString());
            resList.add(lastVO);
            logger.info("son ="+lastVO.getSno()+"; low ="+lastVO.getLowValue()+"; high ="+lastVO.getHighValue());
        }
        
        return resList;
    }

    /**
     * 刪除非本日同步資料 (例如：收貨資料SAP可能已迴轉，在新資料已不存在。)
     * @param mandt
     * @param tableName 
     */
    public void deleteOldData(String mandt, String tableName){
        StringBuilder sql = new StringBuilder();       
        sql.append("BEGIN \n");
        sql.append("    delete from ").append(tableName).append(" where mandt=#mandt and sync_time_stamp<trunc(sysdate); \n");
        sql.append("END;");
        
        logger.info("deleteOldData sql = \n" + sql.toString());
        logger.info("deleteOldData mandt = " + mandt);
        
        Query q = em.createNativeQuery(sql.toString());
        q.setParameter("mandt", mandt);
        q.executeUpdate();
    }
    
    /**
     * 
     * @param fileType
     * @param mandt
     * @param fileNo 
     */
    /*public void deleteByKey(String fileType, String mandt, String fileNo){
        logger.debug("deleteByKey fileType="+fileType+", mandt="+mandt+", fileNo="+fileNo);
        // 依單據 KEY 欄位刪除關聯 Table (RFC 回傳多 Table 時使用)
        if( ConfigManager.ENABLE_DEL_BY_KEY 
            && ConfigManager.DEL_BY_KEY!=null
            && Arrays.asList(ConfigManager.DEL_BY_KEY).contains(fileType) 
            && ConfigManager.getDelByKeyMap()!=null ){
            String sql = ConfigManager.getDelByKeyMap().get(fileType);
            if( sql!=null && !sql.isEmpty() ){
                logger.debug("deleteByKey sql = \n"+sql);
                Query q = em.createNativeQuery(sql);
                q.setParameter("MANDT", mandt);
                q.setParameter("DEL_KEY", fileNo);
                
                q.executeUpdate();
            }
        }
    }*/
    
}
