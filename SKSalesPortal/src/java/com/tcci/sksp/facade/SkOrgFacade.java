/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.facade;

import com.tcci.sksp.controller.util.TcUserFilter;
import com.tcci.fc.entity.org.TcGroup;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.AbstractFacade;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Gilbert.Lin
 */
@Stateless
public class SkOrgFacade extends AbstractFacade<TcUser> {

    @Resource
    SessionContext sessionContext;
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public SkOrgFacade() {
        super(TcUser.class);
    }

    public List<TcUser> findByCriteria(TcUserFilter filter) {
        List<TcUser> list = null;
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = builder.createQuery(TcUser.class);
        Root<TcUser> root = cq.from(TcUser.class);
        cq.select(root);
        List<Predicate> predicateList = new ArrayList<Predicate>();
        if (!StringUtils.isEmpty(filter.getCname())) {
            Predicate p = builder.like(root.get("cname").as(String.class), filter.getCname() + "%");
            predicateList.add(p);
        }

        if (!StringUtils.isEmpty(filter.getEmpId())) {
            Predicate p = builder.like(root.get("empId").as(String.class), filter.getEmpId() + "%");
            predicateList.add(p);
        }
        if (!StringUtils.isEmpty(filter.getLoginAccount())) {
            Predicate p = builder.like(root.get("loginAccount").as(String.class), filter.getLoginAccount() + "%");
            predicateList.add(p);
        }
        if (predicateList.size() > 0) {
            Predicate[] predicates = new Predicate[predicateList.size()];
            predicateList.toArray(predicates);
            cq.where(predicates);
        }
        list = getEntityManager().createQuery(cq).getResultList();
        return list;
    }
}
