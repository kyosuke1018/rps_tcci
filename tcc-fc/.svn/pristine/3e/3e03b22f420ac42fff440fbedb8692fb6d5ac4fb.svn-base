/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.facade.org;

import com.tcci.fc.entity.org.TcGroup;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.AbstractFacade;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
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
public class TcGroupFacade extends AbstractFacade<TcGroup> {
    @PersistenceContext(unitName="Model")
    private EntityManager em;


    protected EntityManager getEntityManager() {
        return em;
    }

    public TcGroupFacade() {
        super(TcGroup.class);
    }
    
    public TcGroup findGroupByCode(String code) {
        TcGroup obj = null;
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery();
        Root<TcGroup> root = criteriaQuery.from(TcGroup.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("code"), code));
        try {
            obj = (TcGroup) getEntityManager().createQuery(criteriaQuery).getSingleResult();
        } catch (NoResultException e) {
        }
        return obj;
    }
    public List<TcGroup> findAll() {
        List<TcGroup> list = null;
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = criteriaBuilder.createQuery();
        Root<TcGroup> root = cq.from(TcGroup.class);
        cq.select(root);
        list = getEntityManager().createQuery(cq).getResultList();

        return list;
    }    
}
