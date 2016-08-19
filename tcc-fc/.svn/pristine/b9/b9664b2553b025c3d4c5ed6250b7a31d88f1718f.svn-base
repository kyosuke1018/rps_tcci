/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.facade.content;

import com.tcci.fc.entity.content.TcFvitem;
import com.tcci.fc.facade.AbstractFacade;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Gilbert.Lin
 */
@Stateless
@Named
 public class TcFvitemFacade extends AbstractFacade<TcFvitem> {
    @PersistenceContext(unitName="Model")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public TcFvitemFacade() {
        super(TcFvitem.class);
    }
}
