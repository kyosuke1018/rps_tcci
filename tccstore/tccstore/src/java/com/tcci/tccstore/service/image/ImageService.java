/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.service.image;

import com.tcci.fc.entity.content.ContentHolder;
import com.tcci.fc.entity.content.TcApplicationdata;
import com.tcci.fc.entity.essential.Persistable;
import com.tcci.fc.facade.content.ContentFacade;
import com.tcci.fc.facade.essential.EssentialFacade;
import com.tcci.tccstore.entity.EcPartner;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

/**
 *
 * @author Jimmy.Lee
 */
@Path("image")
public class ImageService {

    @EJB
    private EssentialFacade essentialFacade;
    @EJB
    private ContentFacade contentFacade;
    @Context
    private ServletContext context;
    
    private static final Map<String, String> oidMapping = new HashMap<>();
    static {
        oidMapping.put("partner", EcPartner.class.getCanonicalName());
    }
    
    @GET
    @Produces("image/jpeg")
    public Response getImage(@Context HttpServletRequest request) {
        String oid = request.getParameter("oid");
        String[] array = oid==null ? null : oid.split(":");
        if (array != null && array.length==2 && oidMapping.containsKey(array[0])) {
            oid = oidMapping.get(array[0]) + ":" + array[1];
            try {
                Persistable obj = essentialFacade.getObject(oid);
                if (obj != null) {
                    TcApplicationdata data = contentFacade.getPrimaryApplicationdata((ContentHolder) obj);
                    String fname = contentFacade.getPhysicalPathname(data.getFvitem());
                    File file = new File(fname);
                    if (file.exists()) {
                        ResponseBuilder response = Response.ok((Object) file);
                        response.type(data.getFvitem().getContenttype());
                        return response.build();
                    }
                }
            } catch (Exception ex) {
            }
        }
        File file = new File(context.getRealPath("/resources/images/unknown.png"));
        ResponseBuilder response = Response.ok((Object) file);
        response.type("image/jpeg");
        return response.build();
    }
    
}
