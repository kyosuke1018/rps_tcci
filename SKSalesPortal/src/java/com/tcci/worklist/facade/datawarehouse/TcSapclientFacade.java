/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.worklist.facade.datawarehouse;

import com.tcci.fc.facade.AbstractFacade;
import com.tcci.worklist.entity.datawarehouse.TcSapclient;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Root;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Jackson.Lee
 */
@Stateless
public class TcSapclientFacade extends AbstractFacade<TcSapclient> {

    @PersistenceContext(unitName = "datawarehousePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TcSapclientFacade() {
        super(TcSapclient.class);
    }

    public List<TcSapclient> findAllAndSort() {
        List<TcSapclient> list = null;
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<TcSapclient> cq = builder.createQuery(TcSapclient.class);
        Root<TcSapclient> from = cq.from(TcSapclient.class);
        cq.select(from);
        cq.orderBy(builder.asc(from.get("client")));
        list = getEntityManager().createQuery(cq).getResultList();
        return list;
    }

    /**
     * 依Client ID取得TcSapclient
     * @param client
     * @return 
     */
    public TcSapclient getByClient(String client) {
        if (StringUtils.isBlank(client)) {
            return null;
        }

        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder queryString = new StringBuilder();
        queryString.append(" select distinct a from TcSapclient a");

        queryString.append(" where a.client=:client");
        params.put("client", client);

        Query query = em.createQuery(queryString.toString());
        for (String key : params.keySet()) {
            query.setParameter(key, params.get(key));
        }

        List<TcSapclient> list = query.getResultList();
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.get(0);
    }

    /**
     * 依code取得TcSapclient
     * @param code
     * @return 
     */
    public TcSapclient getByCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }


        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder queryString = new StringBuilder();
        queryString.append(" select distinct a from TcSapclient a");

        queryString.append(" where a.code=:code");
        params.put("code", code);

        Query query = em.createQuery(queryString.toString());
        for (String key : params.keySet()) {
            query.setParameter(key, params.get(key));
        }

        List<TcSapclient> list = query.getResultList();
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.get(0);
    }

//--Begin--temp remove by neo.    
//    public TcSapclient getByCompanyCode(String companyCode) {
//        if (StringUtils.isBlank(companyCode)) {
//            return null;
//        }
//        Map<String, Object> params = new HashMap<String, Object>();
//        StringBuilder queryString = new StringBuilder();
//        queryString.append(" select distinct a from TcSapclient a");
//
//        queryString.append(" where a.companyCode=:companyCode");
//        params.put("companyCode", companyCode);
//
//        Query query = em.createQuery(queryString.toString());
//        for (String key : params.keySet()) {
//            query.setParameter(key, params.get(key));
//        }
//
//        List<TcSapclient> list = query.getResultList();
//        if (CollectionUtils.isEmpty(list)) {
//            return null;
//        }
//        return list.get(0);
//    }
//--Begin--temp remove by neo.
}
