/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.service.payment;

import com.tcci.ec.entity.EcPayment;
import com.tcci.ec.entity.EcShipping;
import com.tcci.ec.entity.EcStore;
import com.tcci.ec.facade.payment.EcPaymentFacade;
import com.tcci.ec.facade.store.EcStoreFacade;
import com.tcci.ec.service.ServiceBase;
import com.tcci.ec.vo.Payment;
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
@Path("payment")
public class PaymentService extends ServiceBase {
    private final static Logger logger = LoggerFactory.getLogger(PaymentService.class);
    
    @EJB
    private EcPaymentFacade ecPaymentFacade;
    @EJB
    private EcStoreFacade ecStoreFacade;
    
    @GET
    @Path("list")
    @Produces("application/json; charset=UTF-8;")
    public List<Payment> list(@Context HttpServletRequest request,
            @QueryParam(value = "storeId") Long storeId) {
        List<Payment> result = new ArrayList<>();
        try{
            EcStore store = ecStoreFacade.find(storeId);
            List<EcPayment> list = ecPaymentFacade.findByStore(store);
            
            if(CollectionUtils.isNotEmpty(list)){
                for(EcPayment entity:list){
                    result.add(entityTransforVO.paymentTransfor(entity));
                }
            }
        }catch(Exception e){
            logger.error("Exception:"+e);
        }
        return result;
    }
//    public List<Payment> list(@Context HttpServletRequest request) {
//        List<Payment> result = new ArrayList<>();
//        try{
//            List<EcPayment> list = ecPaymentFacade.findDefault();
//            
//            if(CollectionUtils.isNotEmpty(list)){
//                for(EcPayment entity:list){
//                    result.add(entityTransforVO.paymentTransfor(entity));
//                }
//            }
//        }catch(Exception e){
//            logger.error("Exception:"+e);
//        }
//        return result;
//    }
}
