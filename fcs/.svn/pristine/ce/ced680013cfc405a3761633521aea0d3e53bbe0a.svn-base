/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.fcs.facade;

import com.tcci.fcs.entity.FcCompGroup;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Kyle.Cheng
 */
@Stateless
public class FcCompGroupFacade {
    
    @EJB
    protected FcCurrencyFacade fcCurrencyFacade;
    
    @PersistenceContext(unitName="Model")
    private EntityManager em;
    
    public FcCompGroup find(Long id) {
        return em.find(FcCompGroup.class, id);
    }
    
    public FcCompGroup findByCode(String code) {
        Query q = em.createNamedQuery("FcCompGroup.findByCode");
        q.setParameter("code", code);
        List<FcCompGroup> list = q.getResultList();
        return list.isEmpty() ? null : list.get(0);
    }
    
    public List<FcCompGroup> findAll() {
        return em.createNamedQuery("FcCompGroup.findAll").getResultList();
    }
    
    
//    public List<FcCurrency> findToCurrency(){
//        StringBuilder sql = new StringBuilder();
//        sql.append("SELECT ");
//        sql.append("distinct CURRENCY ");
//        sql.append("from FC_COMP_GROUP ");
//        
//        Query query = em.createNativeQuery(sql.toString());
//        List list = query.getResultList();
//        
//        List<FcCurrency> resultList = new ArrayList<>();
//        for (Object row : list) {
//            long id = (Long)row[0];
//            FcCurrency entity = fcCurrencyFacade.find(id);
//            resultList.add(entity);
//        }
//        return resultList;
//    }
}
