/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.fcs.facade;

import com.tcci.fcs.entity.FcCurrency;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Kyle.Cheng
 */
@Stateless
public class FcCurrencyFacade {
    @PersistenceContext(unitName="Model")
    private EntityManager em;
    
    public FcCurrency find(Long id) {
        return em.find(FcCurrency.class, id);
    }
    
    public FcCurrency findByCode(String code) {
        Query q = em.createNamedQuery("FcCurrency.findByCode");
        q.setParameter("code", code);
        List<FcCurrency> list = q.getResultList();
        return list.isEmpty() ? null : list.get(0);
    }
    
    public List<FcCurrency> findAll() {
        return em.createNamedQuery("FcCurrency.findAll").getResultList();
    }
    
    //20160817 增加港幣轉換
    //20160818 增加人民幣別金額轉換
    public List<FcCurrency> findToCurrency(){
//        String sql = "SELECT distinct c.currency FROM FcCompGroup c";
        String sql = "SELECT c FROM FcCurrency c where c.id in (1,2,5)";//20160817 TWD USD RMB
        Query q = em.createQuery(sql);
        return q.getResultList();
    }
}
