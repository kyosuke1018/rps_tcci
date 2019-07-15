/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.dw.facade;

import com.tcci.cm.facade.AbstractFacadeNE;
import com.tcci.cm.facade.conf.SysResourcesFacade;
import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *　Do not specify Entity
 * @author Peter Pan
 */
public abstract class AbstractFacadeDW extends AbstractFacadeNE {
    protected @EJB SysResourcesFacade sys;
    
    @PersistenceContext(unitName = "datawarehousePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
