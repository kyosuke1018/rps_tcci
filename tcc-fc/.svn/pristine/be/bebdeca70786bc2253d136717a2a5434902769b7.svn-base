/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.facade.org;

import com.tcci.fc.entity.org.TcGroup;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.entity.org.TcUsergroup;
import com.tcci.fc.facade.AbstractFacade;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author Jason.Yu
 */

@Stateless
@Named
public class TcUsergroupFacade extends AbstractFacade<TcUsergroup> {
    @PersistenceContext(unitName="Model")
    private EntityManager em;


    protected EntityManager getEntityManager() {
        return em;
    }

    public TcUsergroupFacade() {
        super(TcUsergroup.class);
    }
    
    public TcUsergroup findUserGroup(TcGroup group, TcUser tcuser) {
        TcUsergroup obj = null;
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = builder.createQuery();
        Root<TcUsergroup> root = cq.from(TcUsergroup.class);
        cq.select(root);
        List<Predicate> predicateList = new ArrayList<Predicate>();
        if (group != null) {
            Predicate p = builder.equal(root.get("groupId"), group);
            predicateList.add(p);
        }
        if (tcuser != null) {
            Predicate p = builder.equal(root.get("userId"), tcuser);
            predicateList.add(p);
        }
        if (predicateList.size() > 0) {
            Predicate[] predicates = new Predicate[predicateList.size()];
            predicateList.toArray(predicates);
            cq.where(predicates);
        }
        try {
            obj = (TcUsergroup) getEntityManager().createQuery(cq).getSingleResult();
        } catch (NoResultException e) {
        }
        return obj;
    }
    
    public List<TcUser> findMembersByGroup(TcGroup group) {
        String queryString = "select ug.userId from TcUsergroup ug where ug.groupId=:group";
        Query query = getEntityManager().createQuery(queryString);
        query.setParameter("group", group);
        return query.getResultList();
    }
    
    public List<TcUsergroup> findByGroup(TcGroup group) {
        String queryString = "select ug from TcUsergroup ug where ug.groupId=:group";
        Query query = getEntityManager().createQuery(queryString);
        query.setParameter("group", group);
        return query.getResultList();
    }
}