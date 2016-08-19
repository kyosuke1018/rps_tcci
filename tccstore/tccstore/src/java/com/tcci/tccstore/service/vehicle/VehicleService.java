/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.service.vehicle;

import com.tcci.tccstore.entity.EcMember;
import com.tcci.tccstore.facade.vehicle.EcVehicleFacade;
import com.tcci.tccstore.service.ServiceBase;
import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Jimmy.Lee
 */
@Path("vehicle")
public class VehicleService extends ServiceBase {

    @EJB
    private EcVehicleFacade ecVehicleFacade;

    @POST
    @Path("preference/add")
    @Produces(MediaType.TEXT_PLAIN)
    public String preferenceAdd(@FormParam("vehicle") String vehicle) {
        EcMember loginMember = getAuthMember();
        ecVehicleFacade.addPreference(loginMember, vehicle);
        return "OK";
    }

    @POST
    @Path("preference/remove")
    @Produces(MediaType.TEXT_PLAIN)
    public String preferenceRemove(@FormParam("vehicle") String vehicle) {
        EcMember loginMember = getAuthMember();
        ecVehicleFacade.removePreference(loginMember, vehicle);
        return "OK";
    }

    @GET
    @Path("preference/list")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> preferenceList() {
        EcMember loginMember = getAuthMember();
        return ecVehicleFacade.findByMember(loginMember);
    }

}
