/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ecdemo.facade.datawarehouse;

import com.tcci.ecdemo.entity.datawarehouse.ZorderCn;
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
public class ZorderCnFacade extends AbstractFacade {

    @PersistenceContext(unitName = "DatawarehousePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ZorderCnFacade() {
        super(ZorderCn.class);
    }

    public List<ZorderCn> findByCustomer(String kunnr) {
        List<ZorderCn> result = null;
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<ZorderCn> cq = cb.createQuery(ZorderCn.class);
        Root<ZorderCn> root = cq.from(ZorderCn.class);
        cq.select(root);

        List<Predicate> predicateList = new ArrayList();
        if (StringUtils.isNotEmpty(kunnr)) {
            predicateList.add(cb.equal(root.get("kunnr"),kunnr));
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
