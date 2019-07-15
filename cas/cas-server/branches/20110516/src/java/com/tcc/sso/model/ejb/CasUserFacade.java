/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcc.sso.model.ejb;

import com.tcc.sso.model.entity.CasUser;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Root;

/**
 *
 * @author Jason.Yu
 */
//@Stateless
public class CasUserFacade extends AbstractFacade<CasUser> {

    private EntityManager em;
    private EntityManagerFactory emf = null;
    protected EntityManager getEntityManager() {
        em = emf.createEntityManager();
        return em;
    }

    public CasUserFacade() {
        super(CasUser.class);
        emf = Persistence.createEntityManagerFactory("cas-modelPU");

    }
    public CasUser getUserByUserId(String userId){
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<CasUser> cq = cb.createQuery(CasUser.class);
        Root<CasUser> root = cq.from(CasUser.class);
        cq.select(root);
        cq.where(cb.equal( root.get("userid"), userId));
        TypedQuery<CasUser> q = em.createQuery(cq);
        try{
            return (CasUser)q.getSingleResult();
        } catch(NoResultException e) {
            return null;
        }
    }
}
