/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.facade;

import com.tcci.fc.facade.AbstractFacade;
import com.tcci.sksp.entity.SkBank;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Lynn.Huang
 */
@Stateless
public class SkBankFacade extends AbstractFacade<SkBank> {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public SkBankFacade() {
        super(SkBank.class);
    }

    public List<SkBank> findByCriteria(String code, String name) {
        List<SkBank> list = null;

        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<SkBank> cq = builder.createQuery(SkBank.class);
        Root<SkBank> root = cq.from(SkBank.class);
        cq.select(root);
        List<Predicate> predicateList = new ArrayList<Predicate>();
        if (!StringUtils.isEmpty(code)) {
            Predicate p = builder.like(root.get("code").as(String.class), "%" + code + "%");
            predicateList.add(p);
        }

        if (!StringUtils.isEmpty(name)) {
            Predicate p = builder.like(root.get("name").as(String.class), "%" + name + "%");
            predicateList.add(p);
        }

        if (predicateList.size() > 0) {
            Predicate[] predicates = new Predicate[predicateList.size()];
            predicateList.toArray(predicates);
            cq.where(predicates);
        }
        cq.orderBy(builder.asc(root.get("code")));
        list = getEntityManager().createQuery(cq).getResultList();
        return list;
    }

    public SkBank findByCode(String code) {
        List<SkBank> list = null;

        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<SkBank> cq = builder.createQuery(SkBank.class);
        Root<SkBank> root = cq.from(SkBank.class);
        cq.select(root);
        List<Predicate> predicateList = new ArrayList<Predicate>();
        if (!StringUtils.isEmpty(code)) {
            Predicate p = builder.equal(root.get("code").as(String.class), code);
            predicateList.add(p);
        }
        if (predicateList.size() > 0) {
            Predicate[] predicates = new Predicate[predicateList.size()];
            predicateList.toArray(predicates);
            cq.where(predicates);
        }
        cq.orderBy(builder.asc(root.get("code")));
        list = getEntityManager().createQuery(cq).getResultList();
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }
}
