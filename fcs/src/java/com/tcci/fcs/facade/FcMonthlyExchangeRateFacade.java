/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.fcs.facade;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.fcs.entity.FcCurrency;
import com.tcci.fcs.entity.FcMonthlyExchangeRate;
import java.util.Date;
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
public class FcMonthlyExchangeRateFacade {
    @PersistenceContext(unitName="Model")
    private EntityManager em;
    
    public List<FcMonthlyExchangeRate> findByYM(String ym) {
        Query q = em.createNamedQuery("FcMonthlyExchangeRate.findByYM");
        q.setParameter("yearmonth", ym);
        return q.getResultList();
    }
    
    public FcMonthlyExchangeRate findByYMAndCurrency(String ym, FcCurrency currency, FcCurrency toCurrency) {
        Query q = em.createNamedQuery("FcMonthlyExchangeRate.findByYMAndCurrency");
        q.setParameter("yearmonth", ym);
        q.setParameter("currency", currency);
        q.setParameter("toCurrency", toCurrency);
        List<FcMonthlyExchangeRate> list = q.getResultList();
        return list.isEmpty() ? null : list.get(0);
    }
    
    public FcMonthlyExchangeRate findByYMAndCurrency(String ym, String currCode, String toCurrCode) {
        Query q = em.createNamedQuery("FcMonthlyExchangeRate.findByYMAndCurrencyCode");
        q.setParameter("yearmonth", ym);
        q.setParameter("currCode", currCode);
        q.setParameter("toCurrCode", toCurrCode);
        List<FcMonthlyExchangeRate> list = q.getResultList();
        return list.isEmpty() ? null : list.get(0);
    }
    
    public List<FcMonthlyExchangeRate> findByYMAndToCurrency(String ym, FcCurrency toCurrency) {
        Query q = em.createNamedQuery("FcMonthlyExchangeRate.findByYMAndToCurrency");
        q.setParameter("yearmonth", ym);
        q.setParameter("toCurrency", toCurrency);
        return q.getResultList();
    }
    
    public List<FcMonthlyExchangeRate> findByYMAndToCurrencyCode(String ym, String toCurrCode) {
        Query q = em.createNamedQuery("FcMonthlyExchangeRate.findByYMAndToCurrencyCode");
        q.setParameter("yearmonth", ym);
        q.setParameter("toCurrCode", toCurrCode);
        return q.getResultList();
    }
    
    public void save(FcMonthlyExchangeRate fcMonthlyExchangeRate, TcUser user) {
        fcMonthlyExchangeRate.setModifier(user);
        fcMonthlyExchangeRate.setModifytimestamp(new Date());
        if (fcMonthlyExchangeRate.getId()==null) {
            em.persist(fcMonthlyExchangeRate);
        } else {
            em.merge(fcMonthlyExchangeRate);
        }
    }
}
