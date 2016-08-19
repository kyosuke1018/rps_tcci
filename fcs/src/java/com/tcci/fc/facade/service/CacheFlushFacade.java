/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.facade.service;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jimmy.Lee
 */
@Stateless
@Path("cache")
public class CacheFlushFacade {
    protected static final Logger logger = LoggerFactory.getLogger(CacheFlushFacade.class);

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @GET
    @Path("flush")
    @Produces("text/plain")
    public String cacheFlush() {
        logger.info("cacheFlush invoked.");
        try {
            em.getEntityManagerFactory().getCache().evictAll();
            return "OK";
        } catch (Exception ex) {
            logger.error("flushCache() exception", ex);
            return "Exception:" + ex.getMessage();
        }
    }

}
