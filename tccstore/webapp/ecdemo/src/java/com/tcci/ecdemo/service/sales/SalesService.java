/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ecdemo.service.sales;

import com.tcci.ecdemo.model.sales.Sales;
import com.tcci.ecdemo.service.HMACServiceBase;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Jimmy.Lee
 */
@Path("sales")
public class SalesService extends HMACServiceBase {

    @GET
    @Path("customer/{customer_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Sales> customer(@PathParam("customer_id") Long customer_id) {
        List<Sales> result = new ArrayList<>();
        result.add(new Sales(1L, "800067", "李振銘"));
        return result;
    }

}
