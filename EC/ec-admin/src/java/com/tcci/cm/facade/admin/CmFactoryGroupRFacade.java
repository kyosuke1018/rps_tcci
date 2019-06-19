/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.facade.admin;

import com.tcci.cm.entity.admin.CmFactoryGroupR;
import com.tcci.cm.facade.AbstractFacade;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author gilbert
 */
@Stateless
public class CmFactoryGroupRFacade extends AbstractFacade<CmFactoryGroupR> {
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CmFactoryGroupRFacade() {
        super(CmFactoryGroupR.class);
    }
}
