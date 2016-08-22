package com.tcci.worklist.rs;

import com.tcci.worklist.facade.datawarehouse.ZtabExpRelfilenoSdFacade;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jackson.Lee
 */
@Stateless
@Path("com.tcci.sksp.rs.datawarehouse")
public class JpaFacadeREST {
    final private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @EJB
    ZtabExpRelfilenoSdFacade ztabExpRelfilenoSdFacade;
    
    @GET
    @Path("evictall")
    @Produces("text/plain")
    public String evictAllREST() {
        try {
            ztabExpRelfilenoSdFacade.clearCache();
        } catch (Exception e) {
            logger.error("evictAllREST  failed.", e);
            return "-1";
        }
        return "0";
    }
}
