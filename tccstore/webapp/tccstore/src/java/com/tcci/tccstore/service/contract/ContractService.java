/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.service.contract;

import com.tcci.tccstore.entity.EcContract;
import com.tcci.tccstore.entity.EcCustomer;
import com.tcci.tccstore.service.EntityToModel;
import com.tcci.tccstore.model.contract.Contract;
import com.tcci.tccstore.service.ServiceBase;
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
@Path("contract")
public class ContractService extends ServiceBase {

    // 列出客戶所有合約
    @GET
    @Path("customer/{customer_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Contract> customer(@PathParam("customer_id") Long customer_id) {
        EcCustomer ecCustomer = getAuthCustomer(customer_id);
        List<EcContract> ecContracts = ecContractFacade.findByCustomer(ecCustomer);
        List<Contract> result = new ArrayList<>();
        for (EcContract entity : ecContracts) {
            result.add(EntityToModel.buildContract(entity));
        }
        return result;
    }

}
