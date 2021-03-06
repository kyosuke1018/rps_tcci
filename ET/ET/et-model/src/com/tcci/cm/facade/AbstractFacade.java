/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.facade;

import com.tcci.cm.facade.conf.SysResourcesFacade;
import com.tcci.cm.facade.global.InputCheckFacade;
import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.fc.util.ResultSetHelper;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Peter Pan
 * @param <T>
 */
public abstract class AbstractFacade<T> {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    protected @EJB InputCheckFacade inputCheckFacade;
    protected @EJB SysResourcesFacade sys;
    
    private final Class<T> entityClass;
    
    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    public void create(T entity, boolean simulated) {
        if( GlobalConstant.SIMULATE_DENIED_UPDATE && simulated ){ // 模擬使用者
            throw new RuntimeException("*** Simulate user can not update data! ***");
        }
        getEntityManager().persist(entity);
//        getEntityManager().flush();// for MSSQL @GeneratedValue(strategy = GenerationType.IDENTITY)
    }

    public void edit(T entity, boolean simulated) {
        if( GlobalConstant.SIMULATE_DENIED_UPDATE && simulated ){ // 模擬使用者
            throw new RuntimeException("*** Simulate user can not update data! ***");
        }
        getEntityManager().merge(entity);
//        getEntityManager().flush();// for MSSQL @GeneratedValue(strategy = GenerationType.IDENTITY)
    }

    public void remove(T entity, boolean simulated) {
        if( GlobalConstant.SIMULATE_DENIED_UPDATE && simulated ){ // 模擬使用者
            throw new RuntimeException("*** Simulate user can not update data! ***");
        }
        getEntityManager().remove(getEntityManager().merge(entity));
        // getEntityManager().flush();// for MSSQL @GeneratedValue(strategy = GenerationType.IDENTITY)
    }

    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    public List<T> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<T> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
    
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
//        return ((Integer) q.getSingleResult());// for MSSQL
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
//        return ((Long) q.getSingleResult()).intValue();// for MSSQL
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
        
        List list = q.getResultList();
        List<Long> res = new ArrayList<Long>();
        if( list!=null ){
            for(Object item: list){
                if(item instanceof BigDecimal){
                    res.add(((BigDecimal)item).longValue());
                }else{
                    res.add((Long)item);
                }
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
        if( params!=null ){
            for(String key : params.keySet()){
                q.setParameter(key, params.get(key));
                logger.debug(key + " = " + params.get(key));
            }
        }
        
        logger.debug("list = " + (q.getResultList()!=null? q.getResultList().size():0));
        return (List<String>)q.getResultList();
    }    
    
    /**
     * 取得 int List
     * @param sql
     * @param params
     * @return 
     */
    public List<Integer> findIntegerList(String sql, Map<String, Object> params){
        logger.debug("findIntegerList sql = \n" + sql);
        Query q = getEntityManager().createNativeQuery(sql);
        if( params!=null ){
            for(String key : params.keySet()){
                q.setParameter(key, params.get(key));
                logger.debug(key + " = " + params.get(key));
            }
        }
        
        logger.debug("list = " + (q.getResultList()!=null? q.getResultList().size():0));
        return (List<Integer>)q.getResultList();
    }    

    /**
     * find By JPA Predicates
     * @param cq
     * @param predicateList
     * @return 
     */
    public List<T> findByPredicates(CriteriaQuery<T> cq, List<Predicate> predicateList) {
        if (predicateList.size() > 0) {
            Predicate[] predicates = new Predicate[predicateList.size()];
            predicateList.toArray(predicates);
            cq.where(predicates);
        }

        return getEntityManager().createQuery(cq).getResultList();
    }
    
    /**
     * Named Query
     * @param namedSql
     * @param params
     * @return 
     */
    public List<T> findByNamedQuery(String namedSql, Map<String, Object> params){
        logger.debug("findByNamedQuery namedSql = " + namedSql);
        Query q = getEntityManager().createNamedQuery(namedSql);
        
        if( params!=null && !params.isEmpty() ){
            for(String key : params.keySet()){
                q.setParameter(key, params.get(key));
                logger.debug(key + " = " + params.get(key));
            }
        }

        logger.debug("list = " + (q.getResultList()!=null? q.getResultList().size():0));
        return q.getResultList();
    }
   
    /**
     * JPQL Query
     * @param jpql
     * @param params
     * @return 
     */
    public List<T> findByJPQLQuery(String jpql, Map<String, Object> params){
        logger.debug("findByJPQLQuery jpql = \n" + jpql);
        Query q = getEntityManager().createQuery(jpql);
        
        if( params!=null && !params.isEmpty() ){
            for(String key : params.keySet()){
                q.setParameter(key, params.get(key));
                logger.debug(key + " = " + params.get(key));
            }
        }

        logger.debug("list = " + (q.getResultList()!=null? q.getResultList().size():0));
        return q.getResultList();
    }
    
    /**
     * safe remove by id
     * @param id
     * @param simulated
     * @return 
     */
    public boolean remove(long id, boolean simulated){
        T obj = this.find(id);
        if( obj!=null ){
            this.remove(obj, simulated);
            return true;
        }
        return false;
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
    public List selectBySql(Class clazz, String sql, Map<String, Object> params, Integer start, Integer maxRes){
        return selectBySql(clazz, sql, params, start, maxRes, false);
    }
    public List selectBySql(Class clazz, String sql, Map<String, Object> params, Integer start, Integer maxRes, boolean ignoreParamPrefix){
        //logger.debug("selectBySql sql = \n"+sql);
        ResultSetHelper resultSetHelper = new ResultSetHelper(clazz);
        //ResultSetHelper resultSetHelper = new ResultSetHelper(clazz, "MSSQL"); // for MSSQL
        List resList;
        if( start!=null && maxRes!=null ){
            resList = resultSetHelper.queryToPOJOList(getEntityManager(), sql, params, start, maxRes);
        }else if( maxRes!=null ){
            resList = resultSetHelper.queryToPOJOList(getEntityManager(), sql, params, 0, maxRes);
        }else{
            resList = resultSetHelper.queryToPOJOList(getEntityManager(), sql, params);
        }

        //logger.debug("selectBySql resList = "+((resList!=null)? resList.size():0));
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
