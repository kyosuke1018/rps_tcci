/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ecdemo.facade.order;

import com.tcci.ecdemo.entity.EcOrder;
import com.tcci.ecdemo.entity.OrderStatusEnum;
import com.tcci.ecdemo.facade.util.OrderFilter;
import com.tcci.ecdeom.facade.AbstractFacade;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Neo.Fu
 */
@Named
public class EcOrderFacade extends AbstractFacade {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EcOrderFacade() {
        super(EcOrder.class);
    }

    public List<EcOrder> findOpenOrderByCustomer(String customerCode) {
        OrderFilter filter = new OrderFilter();
        filter.setCustomerCode(customerCode);
        filter.setStatus(OrderStatusEnum.OPEN);
        return findByCriteria(filter);
    }
    
    public EcOrder editThenReturn(EcOrder ecOrder) {
        return em.merge(ecOrder);
    }

    public List<EcOrder> findByCriteria(OrderFilter filter) {
        List<EcOrder> result = null;
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<EcOrder> cq = cb.createQuery(EcOrder.class);
        Root<EcOrder> root = cq.from(EcOrder.class);
        cq.select(root);

        List<Predicate> predicateList = new ArrayList();
        if (StringUtils.isNotEmpty(filter.getCustomerCode())) {
            predicateList.add(cb.equal(root.get("customerId").get("code"), filter.getCustomerCode()));
        }
        if (filter.getStatus() != null) {
            predicateList.add(cb.equal(root.get("status").as(OrderStatusEnum.class), filter.getStatus()));
        }
        if (predicateList.size() > 0) {
            Predicate[] predicates = new Predicate[predicateList.size()];
            predicateList.toArray(predicates);
            cq.where(predicates);
        }

        result = em.createQuery(cq).getResultList();
        return result;
    }
}
