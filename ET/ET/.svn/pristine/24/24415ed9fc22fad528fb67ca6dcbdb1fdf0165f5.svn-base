/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.facade.rs;

import com.tcci.cm.facade.rs.filter.JWTTokenNeeded;
import com.tcci.et.entity.EtMember;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Peter.pan
 */
@Path("/rfq")
public class RfqREST extends AbstractWebREST {
    @GET
    @Path("/get")
    @Produces(MediaType.TEXT_PLAIN+";charset=utf-8")
    @JWTTokenNeeded
    public Response findOptions(@Context HttpServletRequest request, @QueryParam("keys")String keys
            , @QueryParam("memberId")Long memberId, @QueryParam("lang")String lang){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("findOptions keys = "+keys+", memberId = "+memberId);
        List<String> errors = new ArrayList<>();
        EtMember member = null;
        try{
            member = getReqUser(request);
            Locale locale = getLocale(request);
            
            return this.genSuccessRepsone(request, null);
        }catch(Exception e){
            //sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
}
