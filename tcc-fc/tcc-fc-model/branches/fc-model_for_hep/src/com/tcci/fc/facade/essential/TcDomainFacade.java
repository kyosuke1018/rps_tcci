/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.fc.facade.essential;

import com.tcci.fc.entity.essential.TcDomain;
import com.tcci.fc.facade.AbstractFacade;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Gilbert.Lin
 */
@Stateless
public class TcDomainFacade extends AbstractFacade<TcDomain> {
    @PersistenceContext(unitName = "Model")
    private EntityManager em;
    @Resource
    private javax.transaction.UserTransaction utx;

    protected EntityManager getEntityManager() {
        return em;
    }

    public TcDomainFacade() {
        super(TcDomain.class);
    }

    public TcDomain findDomainByName(String name){
        TcDomain obj = null;
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery();
        Root<TcDomain> root = criteriaQuery.from(TcDomain.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("name"), name));
        try {
            obj = (TcDomain) getEntityManager().createQuery(criteriaQuery).getSingleResult();
        } catch (NoResultException e) {
        }
        return obj;
    }
}
