/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.fc.facade.log;

import com.tcci.fc.entity.log.TcDownloadLogDetail;
import com.tcci.fc.facade.AbstractFacade;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author nEO.Fu
 */
@Stateless
@Named
public class TcDownloadLogDetailFacade extends AbstractFacade<TcDownloadLogDetail> {
    @PersistenceContext(unitName="Model")
    private EntityManager em;


    protected EntityManager getEntityManager() {
        return em;
    }

    public TcDownloadLogDetailFacade() {
        super(TcDownloadLogDetail.class);
    }
}
