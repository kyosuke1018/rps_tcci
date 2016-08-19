/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.facade.repository;

import com.tcci.fc.entity.content.TcFvitem;
import com.tcci.fc.entity.repository.TcDocumentMaster;
import com.tcci.fc.entity.repository.TcFolder;
import com.tcci.fc.facade.AbstractFacade;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
@Named
public class TcDocumentMasterFacade extends AbstractFacade<TcDocumentMaster> {

    @PersistenceContext(unitName="Model")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public TcDocumentMasterFacade() {
        super(TcDocumentMaster.class);
    }
}
