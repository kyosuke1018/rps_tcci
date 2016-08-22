/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.storeadmin.service;

import com.tcci.storeadmin.schedule.OrderJobs;
import javax.inject.Inject;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

/**
 * REST Web Service
 *
 * @author Jimmy.Lee
 */
@Path("test")
public class TestService {

    @Inject
    private OrderJobs orderJobs;

    public TestService() {
    }

    @GET
    @Path("orderstatus")
    @Produces("text/plain")
    public String orderStatus(@QueryParam("count") Integer count) {
        if (null == count) {
            count = 10;
        }
        orderJobs.tmpOrderStatusSync(count);
        return "OK";
    }

}
