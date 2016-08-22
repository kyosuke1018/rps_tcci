package com.tcci.worklist.rs;

import com.tcci.worklist.enums.CategoryEnum;
import com.tcci.worklist.facade.datawarehouse.ZtabExpRelfilenoSdFacade;
import java.net.InetAddress;
import java.util.Properties;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.mail.internet.InternetAddress;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Neo.Fu
 */
@Stateless
@Path("worklistService")
public class WorkListREST {
    @Resource(mappedName = "jndi/sk.config")
    transient private Properties jndiConfig;

    private static final String SD_PATH = "worklist";
    private static final String SD_APPROVE_PAGE = "approveSalesDocument.xhtml";
    final private Logger logger = LoggerFactory.getLogger(WorkListREST.class);
    @EJB
    ZtabExpRelfilenoSdFacade ztabExpRelfilenoSdFacade;

    @GET
    @Path("getZtabExpRelfilenoSdCount")
    @Produces("text/plain")
    public String getZtabExpRelfilenoSdCount(@QueryParam("bname") String bname) {
        try {
            logger.debug("bname={}", bname);
            logger.debug("ztabExpRelfilenoSdFacade={}", ztabExpRelfilenoSdFacade);
            return String.valueOf(ztabExpRelfilenoSdFacade.countByBname(bname));
        } catch (Exception e) {
            logger.error("getZtabExpRelfilenoSdCount  failed.", e);
            return "";
        }
    }

    @GET
    @Path("getWorklistLink")
    @Produces("text/plain")
    public String getWorklistLink(@QueryParam("category") String category) {
        logger.debug("category={}", category);
        try {
            String url = "";
            if (CategoryEnum.SD.toString().equals(category)) {
                url = SD_PATH + "/" + SD_APPROVE_PAGE;
                logger.debug("url={}", url);
                return "http://" + jndiConfig.getProperty("hostname") + "/SKSalesPortal/faces/" + url;
            }
        } catch (Exception e) {
            logger.error("getWorklistLink(),e={}", e);
        }
        return "";
    }
}
