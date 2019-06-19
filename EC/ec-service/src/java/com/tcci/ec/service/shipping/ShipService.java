/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.service.shipping;

import com.tcci.ec.entity.EcShipping;
import com.tcci.ec.entity.EcStore;
import com.tcci.ec.facade.shipping.EcShippingFacade;
import com.tcci.ec.facade.store.EcStoreFacade;
import com.tcci.ec.service.ServiceBase;
import com.tcci.ec.vo.Shipping;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Kyle.Cheng
 */
@Path("shipping")
public class ShipService extends ServiceBase {
    private final static Logger logger = LoggerFactory.getLogger(ShipService.class);
    
    @EJB
    private EcShippingFacade ecShippingFacade;
    @EJB
    private EcStoreFacade ecStoreFacade;
    
    @GET
    @Path("list")
    @Produces("application/json; charset=UTF-8;")
    public List<Shipping> list(@Context HttpServletRequest request,
            @QueryParam(value = "storeId") Long storeId) {
        List<Shipping> result = new ArrayList<>();
        try{
            EcStore store = ecStoreFacade.find(storeId);
            List<EcShipping> list = ecShippingFacade.findByStore(store);
            
            if(CollectionUtils.isNotEmpty(list)){
                for(EcShipping entity:list){
                    result.add(entityTransforVO.shippingTransfor(entity));
                }
            }
        }catch(Exception e){
            logger.error("Exception:"+e);
        }
        return result;
    }
//    public List<Shipping> list() {
//        List<Shipping> result = new ArrayList<>();
//        try{
//            List<EcShipping> list = ecShippingFacade.findDefault();
//            
//            if(CollectionUtils.isNotEmpty(list)){
//                for(EcShipping entity:list){
//                    result.add(entityTransforVO.shippingTransfor(entity));
//                }
//            }
//        }catch(Exception e){
//            logger.error("Exception:"+e);
//        }
//        return result;
//    }
}
