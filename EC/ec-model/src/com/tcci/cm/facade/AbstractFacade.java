/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.facade;

import com.tcci.fc.util.ResultSetHelper;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Peter Pan
 */
public abstract class AbstractFacade {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected abstract EntityManager getEntityManager();
    
    public void flush(){
        getEntityManager().flush();
    }
    
    /**
     * Count for Native SQL 
     * select count(*) from ....
     * @param sql
     * @param params
     * @return 
     */
    public int count(String sql, Map<String, Object> params){
        logger.debug("count sql = \n" + sql);
        Query q = getEntityManager().createNativeQuery(sql);
        if( params!=null ){
            for(String name : params.keySet()){
                q.setParameter(name, params.get(name));
                logger.debug(name + " = " + params.get(name));
            }
        }
        
        return ((BigDecimal) q.getSingleResult()).intValue();// for Oracle
        //return ((Integer) q.getSingleResult());// for MSSQL
    }
    
    /**
     * Sum for Native SQL 
     * select count(*) from ....
     * @param sql
     * @param params
     * @return 
     */
    public int sum(String sql, Map<String, Object> params){
        logger.debug("sum sql = \n" + sql);
        Query q = getEntityManager().createNativeQuery(sql);
        if( params!=null ){
            for(String name : params.keySet()){
                q.setParameter(name, params.get(name));
                logger.debug(name + " = " + params.get(name));
            }
        }
        
        return ((BigDecimal) q.getSingleResult()).intValue();// for Oracle
        //return ((Long) q.getSingleResult()).intValue();// for MSSQL
    }

    /**
     * 單一日期時間結果
     * @param sql
     * @param params
     * @return 
     */
    public Date getDateResult(String sql, Map<String, Object> params){
        logger.debug("getResultDate sql = \n" + sql);
        Query q = getEntityManager().createNativeQuery(sql);
        if( params!=null ){
            for(String name : params.keySet()){
                q.setParameter(name, params.get(name));
                logger.debug(name + " = " + params.get(name));
            }
        }
        
        return ((Date) q.getSingleResult());
    }
    
    /**
     * 取得 ID List
     * @param sql
     * @param params
     * @return 
     */
    public List<Long> findIdList(String sql, Map<String, Object> params){
        logger.debug("findIdList sql = \n" + sql);
        Query q = getEntityManager().createNativeQuery(sql);
        if( params!=null ){
            for(String name : params.keySet()){
                q.setParameter(name, params.get(name));
                logger.debug(name + " = " + params.get(name));
            }
        }
        
        List<BigDecimal> list = q.getResultList();
        List<Long> res = new ArrayList<Long>();
        if( list!=null ){
            for(BigDecimal item: list){
                res.add(item.longValue());
            }
        }
        logger.debug("list = " + (list!=null? list.size():0));
        return res;
    }    
    
    /**
     * 取得 string List
     * @param sql
     * @param params
     * @return 
     */
    public List<String> findStringList(String sql, Map<String, Object> params){
        logger.debug("findStringList sql = \n" + sql);
        Query q = getEntityManager().createNativeQuery(sql);
        setParamsToQuery("findStringList", params, q);
        
        logger.debug("list = " + (q.getResultList()!=null? q.getResultList().size():0));
        return (List<String>)q.getResultList();
    }    
    
    /**
     * 取得 date List
     * @param sql
     * @param params
     * @return 
     */
    public List<Date> findDateList(String sql, Map<String, Object> params){
        logger.debug("findDateList sql = \n" + sql);
        Query q = getEntityManager().createNativeQuery(sql);
        setParamsToQuery("findDateList", params, q);
        
        logger.debug("list = " + (q.getResultList()!=null? q.getResultList().size():0));
        return (List<Date>)q.getResultList();
    }    

    /**
     * 執行查詢 SQL
     * @param clazz
     * @param sql
     * @param params
     * @return 
     */
    public List selectBySql(Class clazz, String sql, Map<String, Object> params) {
        return selectBySql(clazz, sql, params, null, null, false);
    }
    public List selectBySql(Class clazz, String sql, Map<String, Object> params, boolean ignoreParamPrefix) {
        return selectBySql(clazz, sql, params, null, null, ignoreParamPrefix);
    }
    public List selectBySql(Class clazz, String sql, Map<String, Object> params, Integer start, Integer maxRes, boolean ignoreParamPrefix){
        ResultSetHelper resultSetHelper = new ResultSetHelper(clazz);
        //ResultSetHelper resultSetHelper = new ResultSetHelper(clazz, "MSSQL"); // for MSSQL
        List resList;
        if( start!=null && maxRes!=null ){
            resList = resultSetHelper.queryToPOJOList(getEntityManager(), sql, params, start, maxRes, ignoreParamPrefix);
        }else if( maxRes!=null ){
            resList = resultSetHelper.queryToPOJOList(getEntityManager(), sql, params, 0, maxRes, ignoreParamPrefix);
        }else{
            resList = resultSetHelper.queryToPOJOList(getEntityManager(), sql, params, ignoreParamPrefix);
        }
        return resList;
    }

    public void setParamsToQuery(String method, Map<String, Object> params, Query q){
        if( params!=null ){
            for(String key : params.keySet()){
                q.setParameter(key, params.get(key));
                logger.debug(method+" params "+key+" = "+params.get(key));
            }
        }
    }
}
