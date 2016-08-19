/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.service;

import com.tcci.tccstore.entity.EcMember;
import com.tcci.tccstore.model.TestModel;
import java.io.File;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author Jimmy.Lee
 */
@Path("test")
public class TestService extends ServiceBase {

    public TestService() {
    }

    @GET
    @Produces("application/json")
    public TestModel getJson() {
        //throw new ServiceException("中文錯誤");
        TestModel model = new TestModel("jimmy.lee", "李四", "jimmy.lee@taiwancement.com");
        model.setCreatetime(new Date());
        return model;
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public TestModel postJson(TestModel model) {
        EcMember authUser = this.getAuthMember();
        System.out.println("postJson ...");
        System.out.println("authUser:" + (authUser==null ? "" : authUser.getName()));
        if (model != null) {
            System.out.println(model.getName());
            System.out.println(model.getCreatetime());
            model.setName("server端中文");
        } else {
            model = new TestModel();
            model.setName("空的model!!");
            model.setCreatetime(new Date());
        }
        return model;
    }
    
    @POST
    @Path("fileUpload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response formPost(@Context HttpServletRequest request) {
        System.out.println("fileUpload service...");
        MultipartRequestMap map = new MultipartRequestMap(request);
        String info = map.getStringParameter("info");
        File file = map.getFileParameter("pic");
        System.out.println(info);
        System.out.println(file);
        return Response.ok(info).build();
    }

}
