/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.service.seller;

import com.tcci.ec.entity.EcMember;
import com.tcci.ec.entity.EcOrder;
import com.tcci.ec.entity.EcSeller;
import com.tcci.ec.entity.EcStore;
import com.tcci.ec.enums.OrderStatusEnum;
import com.tcci.ec.facade.order.EcOrderFacade;
import com.tcci.ec.facade.seller.EcSellerFacade;
import com.tcci.ec.facade.store.EcStoreFacade;
import com.tcci.ec.facade.util.OrderFilter;
import com.tcci.ec.service.ServiceBase;
import com.tcci.ec.vo.Order;
import com.tcci.ec.vo.OrderTransVO;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Kyle.Cheng
 */
@Path("seller")
public class SellerService extends ServiceBase {
    private final static Logger logger = LoggerFactory.getLogger(SellerService.class);
    @EJB
    private EcSellerFacade ecSellerFacade;
    @EJB
    private EcStoreFacade ecStoreFacade;
    @EJB
    private EcOrderFacade ecOrderFacade;
    
    @GET
    @Path("inquiryList")
    @Produces("application/json; charset=UTF-8;")
    public OrderTransVO inquiryList() {
        logger.debug("inquiryList ...");
        OrderTransVO result = new OrderTransVO();
        try{
            EcMember member = getAuthMember();
            if(member!=null){
                logger.debug("inquiryList member..."+member.getLoginAccount());
                
                EcSeller seller = ecSellerFacade.findByMember(member);
                List<EcStore> storeList = ecStoreFacade.findBySeller(seller);
                if(seller!=null && CollectionUtils.isNotEmpty(storeList)){
                    List<Order> orderList = new ArrayList<>();
                    OrderFilter filter = new OrderFilter();
                    filter.setStatus(OrderStatusEnum.Inquiry);
                    StringBuffer msg = new StringBuffer("");
                    for(EcStore store:storeList){
                        
                        filter.setStoreId(store.getId());
                        List<EcOrder> ecOrderList = ecOrderFacade.findByCriteria(filter);
                        if(CollectionUtils.isNotEmpty(ecOrderList)){
                            msg.append("賣場:").append(store.getCname()).append(" 共有 ").append(ecOrderList.size()).append(" 筆詢價");
                            for(EcOrder entity:ecOrderList){
                                orderList.add(entityTransforVO.orderTransfor(entity));
                            }
                        }
                    }
                    result.setOrderList(orderList);
                    result.setMsg(msg.toString());
                }else{
                    result.setMsg("該會員查無賣場資訊!");
                }
            }else{
                result.setMsg("查無會員帳號資訊!");
            }
        }catch(Exception e){
            result.setMsg("inquiryList ex:"+e);
            return result;
        }
        return result;
    }
    
    
}
