/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.facade.clicklog;

import com.tcci.tccstore.entity.EcClickLog;
import com.tcci.tccstore.facade.AbstractFacade;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Jimmy.Lee
 */
@Named
@Stateless
public class EcClickLogFacade extends AbstractFacade {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EcClickLogFacade() {
        super(EcClickLog.class);
    }
    
    public void click(String clientKey, String objType, Long objId) {
        EcClickLog entity = new EcClickLog(clientKey, objType, objId);
        em.persist(entity);
    }

}
