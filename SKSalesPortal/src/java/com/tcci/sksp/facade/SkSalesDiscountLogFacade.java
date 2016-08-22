/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.facade;

import com.tcci.fc.facade.AbstractFacade;
import com.tcci.sksp.entity.ar.SkSalesDiscountLog;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author Jason.Yu
 */
@Stateless
public class SkSalesDiscountLogFacade extends AbstractFacade<SkSalesDiscountLog> {
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public SkSalesDiscountLogFacade() {
        super(SkSalesDiscountLog.class);
    }
    
    
    public int findRowCount(String invoiceNumber){
        int count = 0;
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<SkSalesDiscountLog> root = cq.from(SkSalesDiscountLog.class);
        cq.select(cb.count(root));
        Predicate p = cb.equal(root.get("invoiceNumber"), invoiceNumber);
        cq.where(p);
        Long countLong = getEntityManager().createQuery(cq).getSingleResult();
        if( countLong != null){
            count = countLong.intValue();
        }
        return count;
    }
}
