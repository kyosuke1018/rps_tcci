/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.facade;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.AbstractFacade;
import com.tcci.sksp.entity.SkCustomer;
import com.tcci.sksp.entity.ar.SkAccountsReceivable;
import com.tcci.sksp.entity.org.SkSalesMember;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Lynn.Huang
 */
@Stateless
public class SkAccountsReceivableFacade extends AbstractFacade<SkAccountsReceivable> {
    
    @EJB SkSalesMemberFacade salesMemberFacade;
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public SkAccountsReceivableFacade() {
        super(SkAccountsReceivable.class);
    }
    
    public List<SkAccountsReceivable> findARByCriteria(TcUser user, SkCustomer customer,Date startTime, Date endTime){
        SkSalesMember member = salesMemberFacade.findByMember(user);
        return findARByCriteria( member,customer, startTime, endTime);
    }
    public List<SkAccountsReceivable> findARByCriteria(SkSalesMember member, SkCustomer customer, Date startTime, Date endTime){
        List<SkAccountsReceivable> list = null;

        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<SkAccountsReceivable> cq = builder.createQuery(SkAccountsReceivable.class);
        Root<SkAccountsReceivable> root = cq.from(SkAccountsReceivable.class);
        cq.select(root);
        List<Predicate> predicateList = new ArrayList<Predicate>();
        if (member != null) {
            // bug: 應該以 SkAccountsReceivable.sapid 為準
            // Predicate p = builder.equal(root.get("customer").get("sapid"), member.getCode());
            Predicate p = builder.equal(root.get("sapid"), member.getCode());
            predicateList.add(p);
        }
        if (customer != null) {
            Predicate p = builder.equal(root.get("customer"), customer);
            predicateList.add(p);
        }
        if ( (startTime != null) && (endTime ==null) ) {
            Predicate p = builder.greaterThanOrEqualTo(root.get("invoiceTimestamp").as(Date.class), startTime);
            predicateList.add(p);
        }else if ( (startTime == null) && (endTime !=null) ) {
            Predicate p = builder.lessThanOrEqualTo(root.get("invoiceTimestamp").as(Date.class), endTime);
            predicateList.add(p);
        }else if ( (startTime != null) && (endTime !=null) ) {
            Predicate p = builder.between(root.get("invoiceTimestamp").as(Date.class), startTime, endTime);
            predicateList.add(p);
        }
        if (predicateList.size() > 0) {
            Predicate[] predicates = new Predicate[predicateList.size()];
            predicateList.toArray(predicates);
            cq.where(predicates);
        }
        //cq.orderBy(builder.asc(root.get("orderNumber")), builder.asc(root.get("invoiceTimestamp")));
        //cq.orderBy(builder.asc(root.get("customer").get("code")), builder.asc(root.get("invoiceNumber")));
        cq.orderBy(builder.asc(root.get("customer").get("zipCode")), builder.asc(root.get("customer").get("code")), builder.asc(root.get("invoiceTimestamp")), builder.asc(root.get("invoiceNumber")));
        list = getEntityManager().createQuery(cq).getResultList();
        return list;
    }
    
    public List<SkAccountsReceivable> findARByCriteria(SkCustomer customer){
        return findARByCriteria(customer, null);
    }
    
     public List<SkAccountsReceivable> findARByCriteria(SkCustomer customer, String invoiceNumber) {
        List<SkAccountsReceivable> list = null;
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<SkAccountsReceivable> cq = builder.createQuery(SkAccountsReceivable.class);
        Root<SkAccountsReceivable> root = cq.from(SkAccountsReceivable.class);
        cq.select(root);
        List<Predicate> predicateList = new ArrayList<Predicate>();        
        if (customer != null) {
            Predicate p = builder.equal(root.get("customer"), customer);
            predicateList.add(p);
        }        
        if (!StringUtils.isEmpty(invoiceNumber)) {
            Predicate p = builder.equal(root.get("invoiceNumber"), invoiceNumber);
            predicateList.add(p);            
        }
        if (predicateList.size() > 0) {
            Predicate[] predicates = new Predicate[predicateList.size()];
            predicateList.toArray(predicates);
            cq.where(predicates);
        }
        cq.orderBy(builder.asc(root.get("orderNumber")), builder.asc(root.get("invoiceTimestamp")));
        list = getEntityManager().createQuery(cq).getResultList();
        return list;         
     }
}
