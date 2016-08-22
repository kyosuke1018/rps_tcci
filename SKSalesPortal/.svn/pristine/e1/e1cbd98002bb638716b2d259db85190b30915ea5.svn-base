/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.rs;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import org.apache.log4j.Logger;

/**
 *
 * @author Jason.Yu
 */
@Stateless
@Path("rs/entityCacheREST")
public class EntityCacheREST {
    protected static final Logger logger = Logger.getLogger( EntityCacheREST.class);
    
    @PersistenceContext(unitName = "Model")
    private EntityManager em;
 
    protected EntityManager getEntityManager() {
        return em;
    }
    
    @GET
    @Path("clear")
    @Produces("text/plain")
    public String clearCache() {
        try {
            evictAllCache();
            logger.info("SK Sales Portal system,Model persist unit clear cache ok!");
            return "OK";
        } catch (Exception ex) {
            logger.error("Error: exception:", ex);
            return "Error:" + ex.getMessage();
        }
    }
    
    public void evictCache(String entityName) throws ClassNotFoundException{
        Class cls = Class.forName(entityName);
        evictCache(cls);
    }
    
    public void evictCache(Class cls){
        getEntityManager().getEntityManagerFactory().getCache().evict(cls);
    }
    
    public void evictAllCache(){
        getEntityManager().getEntityManagerFactory().getCache().evictAll();
    }    
}
