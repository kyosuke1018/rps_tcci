/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ecdemo.service.contract;

import static com.sun.xml.ws.security.addressing.impl.policy.Constants.logger;
import com.tcci.ecdemo.model.contract.Contract;
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
@Path("contract")
public class ContractService extends HMACServiceBase {

    // 列出客戶所有合約
    @GET
    @Path("customer/{customer_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Contract> customer(@PathParam("customer_id") Long customer_id) {
        logger.debug("AuthMember:{}", this.getAuthMember());
        //TODO:驗證是否合法
        List<Contract> result = new ArrayList<>();
        result.add(new Contract("1234", "合約1234"));
        result.add(new Contract("5678", "合約5678"));
        return result;
    }

}
