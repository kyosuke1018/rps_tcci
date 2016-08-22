/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.facade;

import com.tcci.fc.facade.AbstractFacade;
import com.tcci.sksp.entity.SkCustomer;
import com.tcci.sksp.entity.ar.SkSalesOrderDetail;
import com.tcci.sksp.entity.org.SkSalesMember;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Jason.Yu
 */
@Stateless
public class SkSalesOrderDetailFacade extends AbstractFacade<SkSalesOrderDetail> {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public SkSalesOrderDetailFacade() {
        super(SkSalesOrderDetail.class);
    }

    public List<SkSalesOrderDetail> findByCriteria(SkSalesMember member, String customerSimpleCode, Date startTime, Date endTime) {
        return findByCriteria(member, customerSimpleCode, startTime, endTime, "");
    }

    public List<SkSalesOrderDetail> findByCriteria(SkSalesMember member, String customerSimpleCode, Date startTime, Date endTime, String productNumber) {
        List<SkSalesOrderDetail> list = null;

        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<SkSalesOrderDetail> cq = builder.createQuery(SkSalesOrderDetail.class);
        Root<SkSalesOrderDetail> root = cq.from(SkSalesOrderDetail.class);
        cq.select(root);
        List<Predicate> predicateList = new ArrayList<Predicate>();
        
        //sapid
        if (member != null) {
            //Predicate p = builder.equal(root.get("sapid"), member.getCode());
            Predicate p = builder.equal(root.get("salesOrder").get("customer").get("sapid"), member.getCode());
            predicateList.add(p);
        }
        
        //customer code
        if (!StringUtils.isEmpty(customerSimpleCode)) {
            Predicate p = builder.equal(root.get("salesOrder").get("customer").get("simpleCode"), customerSimpleCode);
            predicateList.add(p);
        }
        
        //order date
        if ((startTime != null) && (endTime == null)) {
            Predicate p = builder.greaterThanOrEqualTo(root.get("salesOrder").get("orderTimestamp").as(Date.class), startTime);
            predicateList.add(p);
        } else if ((startTime == null) && (endTime != null)) {
            Predicate p = builder.lessThanOrEqualTo(root.get("salesOrder").get("orderTimestamp").as(Date.class), endTime);
            predicateList.add(p);
        } else if ((startTime != null) && (endTime != null)) {
            Predicate p = builder.between(root.get("salesOrder").get("orderTimestamp").as(Date.class), startTime, endTime);
            predicateList.add(p);
        }

        //product number
        if (StringUtils.isNotEmpty(productNumber)) {
            Predicate p = builder.equal(root.get("productNumber"), productNumber);
            predicateList.add(p);
        }
        
        if (predicateList.size() > 0) {
            Predicate[] predicates = new Predicate[predicateList.size()];
            predicateList.toArray(predicates);
            cq.where(predicates);
        }
        cq.orderBy(builder.asc(root.get("salesOrder").get("customer").get("code")), builder.desc(root.get("salesOrder").get("orderTimestamp")));
        list = getEntityManager().createQuery(cq).getResultList();
        return list;
    }
}
