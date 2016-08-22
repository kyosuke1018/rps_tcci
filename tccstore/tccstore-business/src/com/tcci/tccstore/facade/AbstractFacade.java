/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.facade;

import com.tcci.fc.entity.essential.Persistable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author Neo.Fu
 * @param <T>
 */
public abstract class AbstractFacade<T> {
    
    private final Class<T> entityClass;

    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    public void create(T entity) {
        getEntityManager().persist(entity);
    }

    public T edit(T entity) {
        return getEntityManager().merge(entity);
    }

    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    public T find(Object id) {
        return (T) getEntityManager().find(entityClass, id);
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

    public void flush() {
        getEntityManager().flush();
    }

    /**
     * Count for Native SQL
     *
     * @param sql
     * @param params
     * @return
     */
    public int count(String sql, Map<String, Object> params) {
        Query q = getEntityManager().createNativeQuery(sql);
        if (params != null) {
            for (Map.Entry<String, Object> param : params.entrySet()) {
                q.setParameter(param.getKey(), param.getValue());
            }
        }

        return ((BigDecimal) q.getSingleResult()).intValue();
    }

    /**
     * 取得 entities by in condition
     *
     * @param colName
     * @param idList
     * @return
     */
    public List<T> findByInList(String colName, List<?> idList) {
        CriteriaQuery<T> cq = getEntityManager().getCriteriaBuilder().createQuery(entityClass);

        Root<T> pet = cq.from(entityClass);
        cq.where(pet.get(colName).in(idList));
        TypedQuery<T> q = getEntityManager().createQuery(cq);
        List<T> results = q.getResultList();

        return results;
    }

    /**
     * 取得 ID List
     *
     * @param sql
     * @param params
     * @return
     */
    public List<Long> findIdList(String sql, Map<String, Object> params) {
        Query q = getEntityManager().createNativeQuery(sql);
        if (params != null) {
            for (Map.Entry<String, Object> param : params.entrySet()) {
                q.setParameter(param.getKey(), param.getValue());
            }
        }

        List<BigDecimal> list = q.getResultList();
        List res = new ArrayList<Long>();
        if (list != null) {
            for (BigDecimal item : list) {
                res.add(item.longValue());
            }
        }
        return res;
    }

    /**
     * find By JPA Predicates
     *
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
     *
     * @param typeCode
     * @return
     */
    public List<T> findByNamedQuery(String jpql, Map<String, Object> params) {
        Query q = getEntityManager().createNamedQuery(jpql);

        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, Object> param : params.entrySet()) {
                q.setParameter(param.getKey(), param.getValue());
            }
        }

        return q.getResultList();
    }

    /**
     * safe remove by id
     *
     * @param id
     * @return
     */
    public boolean remove(long id) {
        T obj = this.find(id);
        if (obj != null) {
            this.remove(obj);
            return true;
        }
        return false;
    }
    
    public Persistable getObject(String oid) throws Exception {
        int index = oid.indexOf(":");
        if (index < 0) {
            throw new Exception("Incorrect oid format");
        }
        Persistable obj = null;

        String entityClassName = oid.substring(0, index);
        String primaryKey = oid.substring(index + 1);
        Class entityClazz = Class.forName(entityClassName);
        obj = (Persistable) getEntityManager().find(entityClazz, new Long(primaryKey));

        return obj;
    }
    
    public String getOid(Persistable persistable) {
        return persistable.getClass().getCanonicalName() + ":" + persistable.getId();
    }
}
