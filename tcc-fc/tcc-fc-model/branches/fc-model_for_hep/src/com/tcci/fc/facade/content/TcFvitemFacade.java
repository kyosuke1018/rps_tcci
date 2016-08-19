/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.facade.content;

import com.tcci.fc.entity.content.TcFvitem;
import com.tcci.fc.facade.AbstractFacade;
import java.math.BigDecimal;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Gilbert.Lin
 */
@Stateless
public class TcFvitemFacade extends AbstractFacade<TcFvitem> {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;
    @Resource
    private javax.transaction.UserTransaction utx;

    protected EntityManager getEntityManager() {
        return em;
    }

    public TcFvitemFacade() {
        super(TcFvitem.class);
    }
}
