/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ecdemo.service.salesarea;

import com.tcci.ecdemo.model.salesarea.Salesarea;
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
@Path("salesarea")
public class SalesareaService extends HMACServiceBase {
    
    // 取得廠的銷售區
    @GET
    @Path("plant/{plant_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Salesarea> plant(@PathParam("plant_id") Long plant_id) {
        List<Salesarea> result = new ArrayList<>();
        result.add(new Salesarea("010240", "東莞東區"));
        result.add(new Salesarea("010250", "東莞西區"));
        return result;
    }

}
