/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.service.customer;

import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.ec.entity.EcCustomer;
import com.tcci.ec.entity.EcMember;
import com.tcci.ec.enums.CustomerEnum;
import com.tcci.ec.facade.customer.EcCusAddrFacade;
import com.tcci.ec.service.ServiceBase;
import com.tcci.ec.vo.Customer;
import com.tcci.fc.util.ResourceBundleUtils;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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
 * @author kyle.cheng
 */
@Path("customer")
public class CustomerService extends ServiceBase {
    private final static Logger logger = LoggerFactory.getLogger(CustomerService.class);
    
    @EJB
    private EcCusAddrFacade ecCusAddrFacade;
    
    @GET
    @Path("list")
//    @Produces(MediaType.APPLICATION_JSON)
    @Produces("application/json; charset=UTF-8;")
    public List<Customer> list() {
        EcMember member = getAuthMember();
        if(member!=null){
            logger.debug("list member..."+member);
        }
//        return ecCustomerFacade.findAllByMember(member);
        List<EcCustomer> list = ecCustomerFacade.findAllByMember(member);
        List<Customer> result = new ArrayList<>();
        try{
            if(CollectionUtils.isNotEmpty(list)){
                for(EcCustomer entity:list){
                    result.add(entityTransforVO.customerTransfor(entity));
                }
            }
        }catch(Exception e){
            logger.error("Exception:"+e);
        }
        return result;
    }
    
    @GET
    @Path("list/applied")
//    @Produces(MediaType.APPLICATION_JSON)
    @Produces("application/json; charset=UTF-8;")
    public List<Customer> listApplied() {
        EcMember member = getAuthMember();
        if(member!=null){
            logger.debug("list member..."+member);
        }
        List<EcCustomer> list = ecCustomerFacade.findByMemberApplied(member);
        List<Customer> result = new ArrayList<>();
        try{
            if(CollectionUtils.isNotEmpty(list)){
                for(EcCustomer entity:list){
                    result.add(entityTransforVO.customerTransfor(entity));
                }
            }
        }catch(Exception e){
            logger.error("Exception:"+e);
        }
        return result;
    }
    
    @GET
    @Path("checkCredits")
    @Produces("application/json; charset=UTF-8;")
    public Customer checkCredits(@Context HttpServletRequest request,
            @QueryParam(value = "storeId") Long storeId) {
        Locale locale = getLocale(request);
        EcMember member = getAuthMember();
        if(member==null || storeId==null){
//                return "查無會員及賣場資訊!";
//            return ResourceBundleUtils.getMessage(locale, "varify.member.store");
            return null;
        }
        try{
            EcCustomer entity = ecCustomerFacade.findByApplied(member, storeId);
            return entityTransforVO.customerTransfor(entity);
        }catch(Exception e){
            logger.error("Exception:"+e);
        }
        return null;
    }
    
    @GET
    @Path("apply")
    @Produces("application/json; charset=UTF-8;")
    public String apply(@Context HttpServletRequest request,
            @QueryParam(value = "storeId") Long storeId,
            @QueryParam(value = "expectedCredits") BigDecimal expectedCredits) {
        Locale locale = getLocale(request);
        EcMember member = getAuthMember();
        if(member==null || storeId==null){
//                return "查無會員及賣場資訊!";
//            return ResourceBundleUtils.getMessage(locale, "varify.member.store");
            logger.warn("apply FAIL:"+ ResourceBundleUtils.getMessage(locale, "varify.member.store"));
            return GlobalConstant.RS_RESULT_FAIL;
        }
        try{
            EcCustomer entity = ecCustomerFacade.findByMemberAndStore(member, storeId);
            if(entity==null){
                entity = new EcCustomer();
                entity.setMember(member);
                entity.setStoreId(storeId);
                
                entity.setCusType(CustomerEnum.CREDITS.getCode());//C
                entity.setExpectedCredits(expectedCredits);
                entity.setApplyTime(new Date());
                
                ecCustomerFacade.save(entity);
//            
            }else{
//            }else if(entity.getApplyTime()!=null){
                entity.setExpectedCredits(expectedCredits);
                entity.setApplyTime(new Date());
                
                ecCustomerFacade.save(entity);
//            }else{
//                logger.warn("apply FAIL:已申請");
//                return GlobalConstant.RS_RESULT_FAIL;//已申請過
            }
            return GlobalConstant.RS_RESULT_SUCCESS;
        }catch(Exception e){
            logger.error("Exception:"+e);
        }
        return GlobalConstant.RS_RESULT_FAIL;
    }
    
    /*
    @GET
    @Path("addr")
    @Produces(MediaType.APPLICATION_JSON)
    public List<CusAddr> addr(@QueryParam(value = "customerId") Long customerId) {
        logger.debug("addr ...");
        EcCustomer customer = ecCustomerFacade.find(customerId);
        if(customer!=null){
            logger.debug("addr customer..."+customer);
        }
//        return ecCusAddrFacade.findByCustomer(customer);
        List<EcCusAddr> list = ecCusAddrFacade.findByCustomer(customer);
        List<CusAddr> result = new ArrayList<>();
        try{
            if(CollectionUtils.isNotEmpty(list)){
                for(EcCusAddr entity:list){
                    result.add(entityTransforVO.cusAddrTransfor(entity));
                }
            }
        }catch(Exception e){
            logger.error("Exception:"+e);
        }
        return result;
    }*/
    
}
