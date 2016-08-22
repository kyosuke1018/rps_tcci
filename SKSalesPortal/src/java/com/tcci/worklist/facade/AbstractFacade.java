/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.worklist.facade;

import com.tcci.fc.entity.essential.Persistable;
import java.util.List;
import javax.persistence.Cache;
import javax.persistence.EntityManager;

/**
 *
 * @author unicorn
 */
public abstract class AbstractFacade<T> {
    private Class<T> entityClass;

    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    public void create(T entity) {
        getEntityManager().persist(entity);
    }

    public void edit(T entity) {
        getEntityManager().merge(entity);
    }

    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
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
    
    public void clearCache() {
        Cache cache= getEntityManager().getEntityManagerFactory().getCache();
        cache.evictAll();
    }    
    
    public void clearClassCache(Class cls) {
        Cache cache = getEntityManager().getEntityManagerFactory().getCache();
        cache.evict(cls);
    }
    
    public void clearEntityCache(Object obj) {
        Cache cache = getEntityManager().getEntityManagerFactory().getCache();
        cache.evict(obj.getClass(), ((Persistable)obj).getId());
    }
    
    //public Persistable getObject(String oid) throws Exception {
    public Object getObject(String oid) throws Exception {
        int index = oid.indexOf(":");
        if (index < 0) {
            throw new Exception("Incorrect oid format");
        }
        Object obj = null;

        String entityClassName = oid.substring(0, index);
        String primaryKey = oid.substring(index + 1);
        Class entityClass = Class.forName(entityClassName);
        //obj = (Persistable) getEntityManager().find(entityClass, new Long(primaryKey));
        obj = (Object) getEntityManager().find(entityClass, new Long(primaryKey));

        return obj;
    }
}
