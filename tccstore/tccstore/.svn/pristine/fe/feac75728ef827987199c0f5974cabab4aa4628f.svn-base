/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.service.sales;

import com.tcci.tccstore.entity.EcCustomer;
import com.tcci.tccstore.entity.EcSales;
import com.tcci.tccstore.service.EntityToModel;
import com.tcci.tccstore.facade.sales.EcSalesFacade;
import com.tcci.tccstore.model.sales.Sales;
import com.tcci.tccstore.service.ServiceBase;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Jimmy.Lee
 */
@Path("sales")
public class SalesService extends ServiceBase {

    @EJB
    private EcSalesFacade ecSalesFacade;
    
    @GET
    @Path("customer/{customer_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Sales> customer(@PathParam("customer_id") Long customer_id) {
        EcCustomer ecCustomer = ecCustomerFacade.find(customer_id);
        if (null == ecCustomer) {
            throw new WebApplicationException("unauthorized customer!", Response.Status.FORBIDDEN);
        }
        List<EcSales> list = ecSalesFacade.findActiveByCustomer(ecCustomer);
        List<Sales> result = new ArrayList<>();
        for (EcSales entity : list) {
            result.add(EntityToModel.buildSales(entity));
        }
        return result;
    }

}
