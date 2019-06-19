/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.service.image;

import com.tcci.ec.entity.EcFile;
import com.tcci.ec.facade.product.EcProductFacade;
import com.tcci.fc.entity.content.ContentHolder;
import com.tcci.fc.entity.content.TcApplicationdata;
import com.tcci.fc.entity.essential.Persistable;
import com.tcci.fc.facade.content.ContentFacade;
import com.tcci.fc.facade.essential.EssentialFacade;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jimmy.Lee
 */
@Path("image")
public class ImageService {
    private final static Logger logger = LoggerFactory.getLogger(ImageService.class);

//    @EJB
//    private EssentialFacade essentialFacade;
//    @EJB
//    private ContentFacade contentFacade;
    @EJB
    private EcProductFacade ecProductFacade;
    @Context
    private ServletContext context;
    
//    private static final Map<String, String> oidMapping = new HashMap<>();
//    static {
//        oidMapping.put("partner", EcPartner.class.getCanonicalName());
//    }
    
    //http://localhost:8080/ec-service/resources/image?oid=1
    //http://192.168.203.62/ec-service/resources/image?oid=1
    @GET
    @Produces("image/jpeg")
    public Response getImage(@Context HttpServletRequest request,
            @QueryParam(value = "oid") Long oid) {
//        String oid = request.getParameter("oid");
        logger.debug("image oid:"+oid);
        try {
            EcFile ecFile = ecProductFacade.findImageById(oid);
            if (ecFile != null) {
//                String folder = "D:\\FileVault\\ec";
                String folder = ecFile.getSavedir();//  /opt/FileUpload/ec/
                String fname = folder + "/" + ecFile.getSavename();
                logger.debug("image fname:"+fname);
                File file = new File(fname);
                if (file.exists()) {
                    ResponseBuilder response = Response.ok((Object) file);
//                    response.type(data.getFvitem().getContenttype());
                    return response.build();
                }
            }
        } catch (Exception ex) {
            logger.error("image ex:"+ex);
        }
        File file = new File(context.getRealPath("/resources/images/unknown.png"));
        ResponseBuilder response = Response.ok((Object) file);
        response.type("image/jpeg");
        return response.build();
    }
    
}
