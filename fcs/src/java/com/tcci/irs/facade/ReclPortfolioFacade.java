/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.irs.facade;

import com.tcci.fcs.entity.FcCompany;
import com.tcci.irs.entity.ReclPortfolio;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.collections.CollectionUtils;

/**
 *
 * @author David.Jen
 */
@Stateless
public class ReclPortfolioFacade extends AbstractFacade<ReclPortfolio>{
    @PersistenceContext(unitName="Model")
    private EntityManager em;
    //
    public ReclPortfolioFacade(){
        super(ReclPortfolio.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return this.em;
    }
    
    public ReclPortfolio findByYearMonth(int year, int month, FcCompany company1, FcCompany company2){
        //
        List<ReclPortfolio> matched = null;
        //
        StringBuilder buffer = new StringBuilder();
        
        String sql = buffer.append(
                "SELECT pf FROM ReclPortfolio pf ").append(
                "WHERE ").append(
                "pf.year = :year and pf.month like :month ").append(
                "and ").append(
                "( ").append(
                "( pf.company1 = :company1 and pf.company2 = :company2 ) ").append(
                "or ").append(
                "( pf.company1 = :company2 and pf.company2 = :company1 ) ").append(             
                ") ").append(
                "").toString() ;
        
        Query query = em.createQuery(sql);
        query.setParameter("year", year);
        query.setParameter("month", month);
        query.setParameter("company1", company1);
        query.setParameter("company2", company2);
        
        matched = query.getResultList();
        if (CollectionUtils.isEmpty(matched)){
            return null;
        }
        
        return matched.get(0);
    }
}
