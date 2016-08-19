/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.fc.facade.essential;

import com.tcci.fc.entity.essential.TcDomain;
import com.tcci.fc.facade.AbstractFacade;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Gilbert.Lin
 */
@Stateless
@Named
public class TcDomainFacade extends AbstractFacade<TcDomain> {
    @PersistenceContext(unitName="Model")
    private EntityManager em;


    protected EntityManager getEntityManager() {
        return em;
    }

    public TcDomainFacade() {
        super(TcDomain.class);
    }
    
    public TcDomain getDefaultDomain() {
        Query q = em.createQuery("select object(d) from TcDomain d where d.name = :name");
        q.setParameter("name", "Default");
        return (TcDomain)q.getSingleResult();
    }

}
