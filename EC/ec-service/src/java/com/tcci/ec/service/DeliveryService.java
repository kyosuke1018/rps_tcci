/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.service;

import com.tcci.ec.facade.DeliveryFacade;
import com.tcci.ec.vo.DeliveryPlace;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Kyle.Cheng
 */
@Path("delivery")
public class DeliveryService extends ServiceBase {
    private final static Logger logger = LoggerFactory.getLogger(DeliveryService.class);
    
    @EJB
    private DeliveryFacade deliveryFacade;
    
    @GET
    @Path("list")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/json; charset=UTF-8;")
    public List<DeliveryPlace> fintList(@Context HttpServletRequest request, 
            @QueryParam(value = "seller_account") String account,//賣家帳號 
            @QueryParam(value = "deliveryId") Long deliveryId,
            @QueryParam(value = "delivery_code") String delivery_code,
            @QueryParam(value = "province") String province,//省
            @QueryParam(value = "city") String city,//市
            @QueryParam(value = "district") String district//區
            ){
        logger.debug("delivery list seller_account:"+account);
        logger.debug("delivery list deliveryId:"+deliveryId);
        try{
            return deliveryFacade.findByCriteria(account, deliveryId, delivery_code, province, city, district);
        }catch(Exception e){
            logger.error("DeliveryPlace list Exception:\n", e);
        }
        return null;
    }
    
}
