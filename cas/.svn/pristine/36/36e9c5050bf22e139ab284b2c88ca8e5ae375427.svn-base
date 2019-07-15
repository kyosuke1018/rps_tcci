/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.security.rs;

import com.tcci.security.test.ResVO;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Peter.pan
 */
@Path("/protect")
public class ProtectREST extends AbstractREST {
    /**
     * /resources/protect/getLoginInfo
     * @param request
     * @return 
     */
    @GET
    @Path("/getLoginInfo")
    @Produces(MediaType.TEXT_PLAIN+";charset=utf-8")
    public Response getLoginInfo(@Context HttpServletRequest request){
        // 確認 Session 機制仍可運作
        ResVO resVO = checkSession(request);
        LOG.debug("getLoginInfo resVO.getLoginAccount()="+resVO.getLoginAccount());
        LOG.debug("getLoginInfo resVO.getCaller()="+resVO.getCaller());
        
        return Response.ok(resVO, MediaType.APPLICATION_JSON).encoding("UTF-8").build();
    }
}
