/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.service.order;

import com.tcci.cm.enums.LocaleEnum;
import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.ec.entity.EcMember;
import com.tcci.ec.entity.EcOrder;
import com.tcci.ec.entity.EcOrderCarInfo;
import com.tcci.ec.entity.EcOrderDetail;
import com.tcci.ec.entity.EcOrderMessage;
import com.tcci.ec.entity.EcOrderRate;
import com.tcci.ec.entity.EcOrderShipInfo;
import com.tcci.ec.entity.EcPayment;
import com.tcci.ec.entity.EcProduct;
import com.tcci.ec.entity.EcShipping;
import com.tcci.ec.entity.EcStore;
import com.tcci.ec.enums.CreditLogEnum;
import com.tcci.ec.enums.OrderStatusEnum;
import com.tcci.ec.enums.PayMethodEnum;
import com.tcci.ec.enums.PayStatusEnum;
import com.tcci.ec.enums.ShipStatusEnum;
import com.tcci.ec.facade.EcStockLogFacade;
import com.tcci.ec.facade.customer.EcCustomerFacade;
import com.tcci.ec.facade.order.EcOrderCarInfoFacade;
import com.tcci.ec.facade.order.EcOrderDetailFacade;
import com.tcci.ec.facade.order.EcOrderFacade;
import com.tcci.ec.facade.order.EcOrderMessageFacade;
import com.tcci.ec.facade.order.EcOrderRateFacade;
import com.tcci.ec.facade.order.EcOrderShipInfoFacade;
import com.tcci.ec.facade.payment.EcPaymentFacade;
import com.tcci.ec.facade.product.EcProductFacade;
import com.tcci.ec.facade.shipping.EcShippingFacade;
import com.tcci.ec.facade.store.EcStoreFacade;
import com.tcci.ec.facade.util.OrderFilter;
import com.tcci.ec.model.OrderDetailVO;
import com.tcci.ec.model.OrderVO;
import com.tcci.ec.service.ServiceBase;
import com.tcci.ec.service.push.PushService;
import com.tcci.ec.vo.Order;
import com.tcci.ec.vo.OrderTransVO;
import com.tcci.ec.vo.OrderDetail;
import com.tcci.ec.vo.OrderMessage;
import com.tcci.ec.vo.OrderRate;
import com.tcci.fc.util.ResourceBundleUtils;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Kyle.Cheng
 */
@Path("order")
public class OrderService extends ServiceBase {
    
    private final static Logger logger = LoggerFactory.getLogger(OrderService.class);
    private final static String noticeFormat = "订单(序号:{0}, 訂單編號:{1},已送出审核, 待核准后会另行通知, 详细的订单资讯可至帐户明细查询!";
    @EJB
    private EcOrderFacade ecOrderFacade;
    @EJB
    private EcOrderDetailFacade ecOrderDetailFacade;
    @EJB
    private EcOrderMessageFacade ecOrderMessageFacade;
    @EJB
    private EcStoreFacade ecStoreFacade;
    @EJB
    private EcProductFacade ecProductFacade;
    @EJB
    private EcShippingFacade ecShippingFacade;
    @EJB
    private EcOrderShipInfoFacade ecOrderShipInfoFacade;
    @EJB
    private EcOrderCarInfoFacade ecOrderCarInfoFacade;
    @EJB
    private EcPaymentFacade ecPaymentFacade;
    @Inject
    private PushService pushService;
    @EJB
    private EcOrderRateFacade ecOrderRateFacade;
    @EJB
    private EcStockLogFacade ecStockLogFacade;
    @EJB
    protected EcCustomerFacade ecCustomerFacade;
    
    @POST
    @Path("list")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/json; charset=UTF-8;")
    public List<Order> list(OrderFilter filter) {
        logger.debug("order list start");
        List<Order> result = new ArrayList<>();
        Locale locale = getLocale(request);
        try {
            if (filter == null) {
                filter = new OrderFilter();
            } else {
                SimpleDateFormat sdFormat = new SimpleDateFormat(GlobalConstant.FORMAT_DATE_STR);
                if (StringUtils.isNotEmpty(filter.getCreatetimeBeginStr())) {
                    Date begin = sdFormat.parse(filter.getCreatetimeBeginStr());
                    filter.setCreatetimeBegin(begin);
                }
                if (StringUtils.isNotEmpty(filter.getCreatetimeEndStr())) {
                    Date end = sdFormat.parse(filter.getCreatetimeEndStr());
                    filter.setCreatetimeEnd(end);
                }
            }
            List<EcOrder> list = ecOrderFacade.findByCriteria(filter);
            if (CollectionUtils.isNotEmpty(list)) {
                for (EcOrder entity : list) {
                    if(filter.getCarNo()!=null){//20190408 車號查詢
                        List<EcOrderCarInfo> carInfoList = ecOrderCarInfoFacade.findByOrder(entity);
                        boolean matchCarNo = false;
                        for(EcOrderCarInfo carInfo : carInfoList){
//                            if(carInfo.getCarNo().matches(filter.getCarNo())){//錯誤的比對方式
//                            if(carInfo.getCarNo().indexOf(filter.getCarNo())>=0){
                            if(carInfo.getCarNo().contains(filter.getCarNo())){
                                matchCarNo = true;
                                break;
                            }
                        }
                        if(!matchCarNo){
//                            result.add(entityTransforVO.orderTransfor(entity, locale));
                            continue;
                        }
                    }else{
//                        result.add(entityTransforVO.orderTransfor(entity, locale));
                    }
                    
                    
                    if(filter.getProductId()!=null){//20190423 產品查詢
                        List<EcOrderDetail> details = ecOrderDetailFacade.findByOrder(entity);
                        if (CollectionUtils.isNotEmpty(details)) {
                            Long productId = details.get(0).getProduct().getId();
                            if(!filter.getProductId().equals(productId)){
                                continue;
                            }
                        }else{
                            continue;
                        }
                    }
                    
                    
                    result.add(entityTransforVO.orderTransfor(entity, locale));
                }
            }
        } catch (Exception e) {
            logger.error("Exception:" + e);
        }
        return result;
    }
    
    @POST
    @Path("count")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/json; charset=UTF-8;")
    public int count(OrderFilter filter) throws ParseException {
        logger.debug("order count start");
        if (filter == null) {
            filter = new OrderFilter();
        } else {
            SimpleDateFormat sdFormat = new SimpleDateFormat(GlobalConstant.FORMAT_DATE_STR);
            if (StringUtils.isNotEmpty(filter.getCreatetimeBeginStr())) {
                Date begin = sdFormat.parse(filter.getCreatetimeBeginStr());
                filter.setCreatetimeBegin(begin);
            }
            if (StringUtils.isNotEmpty(filter.getCreatetimeEndStr())) {
                Date end = sdFormat.parse(filter.getCreatetimeEndStr());
                filter.setCreatetimeEnd(end);
            }
        }
        List<EcOrder> list = ecOrderFacade.findByCriteria(filter);
        return list != null ? list.size() : 0;
    }

    /*@GET
    @Path("changeStatus")
    @Produces("application/json; charset=UTF-8;")
    public String changeStatus(@Context HttpServletRequest request,
            @QueryParam(value = "orderId") Long orderId,
            @QueryParam(value = "type") String type,
            @QueryParam(value = "status") String status) {*/
    @POST
    @Path("changeStatus")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/json; charset=UTF-8;")
    public String changeStatus(@Context HttpServletRequest request,
            @QueryParam(value = "type") String type,
            @QueryParam(value = "status") String status,
            Order order
    ) {
//        logger.debug("changeStatus orderId:"+orderId);
//        logger.debug("changeStatus status:"+status);
        EcOrder ecOrder = ecOrderFacade.find(order.getId());
//        EcOrder ecOrder = ecOrderFacade.find(orderId);
        Locale locale = getLocale(request);
        EcMember member = getAuthMember();
        String message = "";
        if (ecOrder == null || member == null) {
            return GlobalConstant.RS_RESULT_FAIL;
        }
        
        Long orderId = order.getId();
        if ("order".equals(type)) {
            OrderStatusEnum enums = OrderStatusEnum.fromString(status);
            if (OrderStatusEnum.Pending.equals(enums)) {
                // [轉訂單] 或 [直接下單] 時 加入商店客戶關聯主檔 交易產生的客戶
                ecCustomerFacade.addCustomerByOrder(member, order.getStore().getId());
            } else if (OrderStatusEnum.Approve.equals(enums)) {
                ecOrder.setApprovetime(new Date());//訂單成立時間
                //庫存 已售出
                OrderVO orderVO = new OrderVO();
                orderVO.setOrderNumber(ecOrder.getOrderNumber());
                orderVO.setStoreId(ecOrder.getStore().getId());
                orderVO.setId(orderId);
                List<OrderDetailVO> items = new ArrayList<>();
                for (EcOrderDetail detail : ecOrder.getOrderDetails()) {
                    OrderDetailVO detailVO = new OrderDetailVO();
                    detailVO.setProductId(detail.getProduct().getId());
                    detailVO.setQuantity(detail.getQuantity());
                    items.add(detailVO);
                }
                orderVO.setItems(items);
                ecStockLogFacade.addByOrder(orderVO, member, null, true);
            } else if (OrderStatusEnum.Cancelled.equals(enums)) {
                //庫存 取消
                ecStockLogFacade.disableByOrder(ecOrder.getStore().getId(), orderId, member, false);
            }
//            message = "狀態變更:["+ecOrder.getStatus().name()+"] ==> ["+status+"]";
            message = ResourceBundleUtils.getMessage(locale, "notify.msg.010")
                    + ":[" + ecOrder.getStatus().name() + "] ==> [" + status + "]";
            ecOrderFacade.changeStatus(ecOrder, enums, message, member);
        } else if ("pay".equals(type)) {
            PayStatusEnum enums = PayStatusEnum.fromCode(status);
            if (PayStatusEnum.D.equals(enums)) {//通知已付款
                //信用額度 付款
                List<String> errors = new ArrayList();
                if (PayMethodEnum.CREDIT.getType().equals(ecOrder.getPayment().getType())) {
                    boolean subtract = true;//扣除
                    BigDecimal changeAmt = order.getTotal();
                    boolean isOk = ecCustomerFacade.changeCreditsByOrder(ecOrder, locale, errors, subtract, changeAmt, CreditLogEnum.ORDER_PRE_PAY);//3
                    if (!isOk) {
                        message = CollectionUtils.isEmpty(errors) ? "" : errors.get(0);

                        //訂單狀態變更失敗!
                        String title = ResourceBundleUtils.getMessage(locale, "error.order.013");
                        // 訂單編號[{0}], {1}
                        String msg = MessageFormat.format(ResourceBundleUtils.getMessage(locale, "error.order.013"), ecOrder.getOrderNumber(), message);
                        String account = ecOrder.getMember().getLoginAccount();
                        pushService.sendPushAlias("order", orderId.toString(), type, status, title, msg, account);
                        return GlobalConstant.RS_RESULT_FAIL;
                    }
                }
            }
//            message = "收款狀態變更:["+ecOrder.getPayStatus().name()+"] ==> ["+status+"]";
            message = ResourceBundleUtils.getMessage(locale, "notify.msg.011")
                    + ":[" + ecOrder.getPayStatus().name() + "] ==> [" + status + "]";
            ecOrderFacade.changeStatus(ecOrder, enums, message, member);
        } else if ("ship".equals(type)) {
            ShipStatusEnum enums = ShipStatusEnum.fromCode(status);
            if (ShipStatusEnum.B.equals(enums)) {//出貨
                ecOrder.setOriTotal(ecOrder.getTotal());//寫入原始訂單金額
                ecOrder = this.updateOrderTotal(ecOrder, order);//出貨 修改數量 訂單金額

                //信用額度 付款
                List<String> errors = new ArrayList();
                if (PayMethodEnum.CREDIT.getType().equals(ecOrder.getPayment().getType())) {
                    //出貨時 信用額度  原始信用額度 + 原始訂單金額 - 訂單金額
//                    boolean subtract = false;//加
//                    BigDecimal changeAmt =  ecOrder.getOriTotal().subtract(ecOrder.getTotal());
//                    if(changeAmt.compareTo(BigDecimal.ZERO) != 0){//有差額
//                        boolean isOk = ecCustomerFacade.changeCreditsByOrder(ecOrder, locale, errors, subtract, changeAmt);
//                    }
                    //依credit log需求 兩段式寫入
                    ecCustomerFacade.changeCreditsByOrder(ecOrder, locale, errors, false, ecOrder.getOriTotal(), CreditLogEnum.ORDER_RETURE);//4
                    ecCustomerFacade.changeCreditsByOrder(ecOrder, locale, errors, true, ecOrder.getTotal(), CreditLogEnum.ORDER_FINAL_PAY);//5
                }

                //庫存 出貨
                ecStockLogFacade.shippingByOrder(ecOrder.getStore().getId(), orderId, member, false);
            }
//            message = "出貨狀態變更:["+ecOrder.getShipStatus().name()+"] ==> ["+status+"]";
            message = ResourceBundleUtils.getMessage(locale, "notify.msg.012")
                    + ":[" + ecOrder.getShipStatus().name() + "] ==> [" + status + "]";
            ecOrderFacade.changeStatus(ecOrder, enums, message, member);
        }
        
        EcOrder entity = ecOrderFacade.find(orderId);
        try {//推播動作 不影響回傳結果
            // 您的訂單狀態已變更! type : order, pay, ship
            String title = ResourceBundleUtils.getMessage(locale, "notify.msg.004");
            // 訂單編號[{0}], {1}
            String msg = MessageFormat.format(ResourceBundleUtils.getMessage(locale, "notify.msg.005"), ecOrder.getOrderNumber(), message);
            if (entity.getMember().equals(member)) {//買方留言
                if (entity.getStore() != null && entity.getStore().getSeller() != null) {
                    String account = entity.getStore().getSeller().getMember().getLoginAccount();
                    pushService.sendPushAlias("order", orderId.toString(), type, status, title, msg, account);
                }
            } else {//賣方改狀態
                String account = entity.getMember().getLoginAccount();
                pushService.sendPushAlias("order", orderId.toString(), type, status, title, msg, account);
            }
        } catch (Exception ex) {
            logger.error("push fail Exception:" + ex);
            return GlobalConstant.RS_RESULT_FAIL;
        }
//        return entityTransforVO.orderTransfor(entity);
        return GlobalConstant.RS_RESULT_SUCCESS;
    }
    
    @POST
    @Path("create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/json; charset=UTF-8;")
    public OrderTransVO create(@Context HttpServletRequest request,
            OrderTransVO orderTransVO
    //            List<Order> orderList
    ) {
        logger.debug("create start");
        OrderTransVO result = new OrderTransVO();
        StringBuffer msg = new StringBuffer("");
        try {
            List<Order> orderList = orderTransVO.getOrderList();
            List<Order> resultOrderList = new ArrayList<>();
            
            Locale locale = getLocale(request);
            EcMember member = getAuthMember();
            if (member == null) {
//                msg.append("訂單建立失敗! 無登入者資訊");
                msg.append(ResourceBundleUtils.getMessage(locale, "error.order.001"));
            }
            if (CollectionUtils.isEmpty(orderList)) {
//                msg.append("訂單建立失敗! 無訂單");
                msg.append(ResourceBundleUtils.getMessage(locale, "error.order.002"));
            }
            logger.debug("create orderlist:" + orderList.size());
            for (Order order : orderList) {
                if (StringUtils.isBlank(order.getOrderNumber())) {
//                    msg.append("訂單建立失敗! 無訂單編號");
                    msg.append(ResourceBundleUtils.getMessage(locale, "error.order.003"));
                    continue;
                }
                //詢價時不需填
//                if (order.getPaymentType()==null) {
//                     msg.append("訂單建立失敗! 訂單編號:").append(order.getOrderNumber()).append(" 請選擇付款方式");
//                    continue;
//                }
                List<OrderDetail> list = order.getOrderDetails();
                List<EcOrderDetail> ecOrderDetailList = new ArrayList<>();
                if (CollectionUtils.isEmpty(order.getOrderDetails())) {
//                    msg.append("訂單建立失敗! 訂單編號:").append(order.getOrderNumber()).append(" 無商品資訊");
                    msg.append(MessageFormat.format(ResourceBundleUtils.getMessage(locale, "error.order.004"), order.getOrderNumber()));
                    continue;
                } else {
                    boolean error = false;
                    if (order.getId() != null) {
                        ecOrderDetailList = ecOrderDetailFacade.findByOrder(ecOrderFacade.find(order.getId()));
                    }
                    for (OrderDetail detail : list) {
                        if (detail.getProduct() == null || detail.getProduct().getId() == null) {
//                            msg.append("訂單建立失敗! 訂單編號:").append(order.getOrderNumber()).append(" 無商品資訊");
                            msg.append(MessageFormat.format(ResourceBundleUtils.getMessage(locale, "error.order.004"), order.getOrderNumber()));
                            error = true;
                            break;
                        } else {
                            EcProduct ecProduct = ecProductFacade.find(detail.getProduct().getId());
                            if (ecProduct == null) {
//                                msg.append("訂單建立失敗! 訂單編號:").append(order.getOrderNumber()).append(" 無商品資訊");
                                msg.append(MessageFormat.format(ResourceBundleUtils.getMessage(locale, "error.order.004"), order.getOrderNumber()));
                                error = true;
                                break;
                            } else {
                                if (order.getId() != null && CollectionUtils.isNotEmpty(ecOrderDetailList)) {
                                    for (EcOrderDetail ecOrderDetail : ecOrderDetailList) {
                                        if (detail.getId() != null && detail.getId().equals(ecOrderDetail.getId())) {
                                            ecOrderDetail.setPrice(detail.getPrice());
                                            ecOrderDetail.setQuantity(detail.getQuantity());
                                            ecOrderDetail.setTotal(detail.getTotal());
                                            ecOrderDetail.setSno(detail.getSno());
                                        }
                                    }
                                } else {
                                    EcOrderDetail ecOrderDetail = new EcOrderDetail(ecProduct.getStore().getId());//20180926 EC_ORDER_DETAIL.STORE_ID
                                    ecOrderDetail.setProduct(ecProduct);
                                    ecOrderDetail.setPrice(detail.getPrice());
                                    ecOrderDetail.setQuantity(detail.getQuantity());
                                    ecOrderDetail.setTotal(detail.getTotal());
                                    ecOrderDetail.setSno(detail.getSno());
                                    ecOrderDetail.setOriUnitPrice(detail.getPrice());
                                    ecOrderDetail.setOriQuantity(detail.getQuantity());
                                    
                                    ecOrderDetailList.add(ecOrderDetail);
                                }
                            }
                        }
                    }
                    if (error) {
                        continue;
                    }
                }
                EcStore ecStore;
                if (order.getStore() == null || order.getStore().getId() == null) {
//                    msg.append("訂單建立失敗! 訂單編號:").append(order.getOrderNumber()).append(" 無賣場資訊");
                    msg.append(MessageFormat.format(ResourceBundleUtils.getMessage(locale, "error.order.005"), order.getOrderNumber()));
                    continue;
                } else {
                    ecStore = ecStoreFacade.find(order.getStore().getId());
                    if (ecStore == null) {
//                        msg.append("訂單建立失敗! 訂單編號:").append(order.getOrderNumber()).append(" 查無賣場資訊");
                        msg.append(MessageFormat.format(ResourceBundleUtils.getMessage(locale, "error.order.005"), order.getOrderNumber()));
                        continue;
                    }
                }
                //詢價時不需填
                //EC 1.5 不需出貨方式 付款方式
                EcShipping ecShipping = null;
                EcPayment ecPayment = null;
                
                if (order.getShipping() == null || order.getShipping().getId() == null) {
                } else {
                    ecShipping = ecShippingFacade.find(order.getShipping().getId());
                }
                /*if (ecShipping==null) {
//                    msg.append("訂單建立失敗! 訂單編號:").append(order.getOrderNumber()).append(" 無出貨方式");
                    msg.append(MessageFormat.format(ResourceBundleUtils.getMessage(locale, "error.order.006"), order.getOrderNumber()));
                    continue;
                }*/
                if (order.getPayment() == null || order.getPayment().getId() == null) {
                } else {
                    ecPayment = ecPaymentFacade.find(order.getPayment().getId());
                }
                /*if(order.getStatus() == null){
//                    msg.append("訂單建立失敗! 訂單編號:").append(order.getOrderNumber()).append(" 訂單狀態有誤");
                    msg.append(MessageFormat.format(ResourceBundleUtils.getMessage(locale, "error.order.007"), order.getOrderNumber()));
                    continue;
                }else if(order.getStatus().equals(OrderStatusEnum.Pending)){//詢價時不需填
                    if (order.getShipInfo() == null
                            || StringUtils.isBlank(order.getShipInfo().getRecipient())
//                            || StringUtils.isBlank(order.getShipInfo().getAddress())
                            || StringUtils.isBlank(order.getShipInfo().getPhone())) {
//                        msg.append("訂單建立失敗! 訂單編號:").append(order.getOrderNumber()).append(" 訂單出貨資訊不完整");
                        msg.append(MessageFormat.format(ResourceBundleUtils.getMessage(locale, "error.order.008"), order.getOrderNumber()));
                        continue;
                    }
                    
//                    if (ecShipping==null) {
//                        msg.append("訂單建立失敗! 訂單編號:").append(order.getOrderNumber()).append(" 無出貨方式");
//                        continue;
//                    }
                    if (ecPayment==null) {
//                        msg.append("訂單建立失敗! 訂單編號:").append(order.getOrderNumber()).append(" 無付款方式");
                        msg.append(MessageFormat.format(ResourceBundleUtils.getMessage(locale, "error.order.009"), order.getOrderNumber()));
                        continue;
                    }
                }*/
                
                logger.debug("create vaild finish");
                logger.debug("create action start");
                
                EcOrder ecOrder = new EcOrder();
                if (order.getId() != null) {
                    ecOrder = ecOrderFacade.find(order.getId());
                } else {
                    ecOrder.setOrderNumber(order.getOrderNumber());
                    ecOrder.setMember(member);
                    ecOrder.setStore(ecStore);
//                    ecOrder.setShipping(ecShipping);
                    //default
                    ecOrder.setPayStatus(PayStatusEnum.A);
                    ecOrder.setShipStatus(ShipStatusEnum.A);
                    ecOrder.setCurrencyId(Long.parseLong("1"));
                    
                    ecOrder.setCreator(member);
                    ecOrder.setCreatetime(new Date());
                }
                ecOrder.setStatus(order.getStatus());
//                ecOrder.setPaymentType(order.getPaymentType());Ｘ
//                ecOrder.setPayStatus(order.getPayStatus());
//                ecOrder.setShipStatus(order.getShipStatus());
                ecOrder.setTotal(order.getTotal());
                ecOrder.setSubTotal(order.getSubTotal());
                ecOrder.setShippingTotal(order.getShippingTotal());
//                ecOrder.setRecipient(order.getRecipient());
//                ecOrder.setPhone(order.getPhone());
//                ecOrder.setAddress(order.getAddress());
                ecOrder.setMessage(order.getMessage());
                ecOrder.setDeliveryId(order.getDeliveryId());
                ecOrder.setSalesareaId(order.getSalesareaId());
                if (StringUtils.isNotBlank(order.getDeliveryDate())) {
                    SimpleDateFormat sdf = new SimpleDateFormat(GlobalConstant.FORMAT_DATE_STR);
                    Date deliveryDate = sdf.parse(order.getDeliveryDate());
                    ecOrder.setDeliveryDate(deliveryDate);
                }
                if (ecPayment != null) {
                    ecOrder.setPayment(ecPayment);
                }
                
                ecOrder = ecOrderFacade.createOrder(ecOrder, noticeFormat);
                
                if (ecOrder.getId() != null) {
                    for (EcOrderDetail ecOrderDetail : ecOrderDetailList) {
                        if (order.getId() != null) {
                            ecOrderDetail.setModifier(member);
                        } else {
                            ecOrderDetail.setOrder(ecOrder);
                            ecOrderDetail.setCreator(member);
                        }
                        ecOrderDetailFacade.save(ecOrderDetail);
                    }
                    
                    if (order.getShipInfo() != null) {
                        EcOrderShipInfo ecOrderShipInfo;
                        if (order.getShipInfo().getId() != null) {
                            ecOrderShipInfo = ecOrderShipInfoFacade.find(order.getShipInfo().getId());
                        } else {
                            ecOrderShipInfo = new EcOrderShipInfo();
                            ecOrderShipInfo.setOrder(ecOrder);
                            ecOrderShipInfo.setShipping(ecShipping);
                            ecOrderShipInfo.setCreator(member);
                        }
                        ecOrderShipInfo.setRecipient(order.getShipInfo().getRecipient());
                        ecOrderShipInfo.setPhone(order.getShipInfo().getPhone());
                        ecOrderShipInfo.setAddress(order.getShipInfo().getAddress());
                        ecOrderShipInfo.setCarNo(order.getShipInfo().getCarNo());//v1.5 只填車號 非必填
                        
                        ecOrderShipInfo.setDriver(order.getShipInfo().getDriver());
                        ecOrderShipInfo.setPatrolLatitude(order.getShipInfo().getPatrolLatitude());
                        ecOrderShipInfo.setPatrolLongitude(order.getShipInfo().getPatrolLongitude());
                        
                        ecOrderShipInfo.setModifier(member);

//                        if(order.getShipInfo().getId() != null){
//                        }else{
                        ecOrderShipInfoFacade.save(ecOrderShipInfo);
//                        }

                        //20190408 車號多筆 寫入新table
                        this.changeCarInfo(order.getShipInfo().getCarNo(), ecOrder);
                    }

                    // [轉訂單] 或 [直接下單] 時 加入商店客戶關聯主檔 交易產生的客戶
                    if (OrderStatusEnum.Pending.equals(order.getStatus())) {
                        ecCustomerFacade.addCustomerByOrder(member, order.getStore().getId());
                    }
                }

//                msg.append("訂單建立完成! 訂單編號:").append(order.getOrderNumber());
                resultOrderList.add(entityTransforVO.orderTransfor(ecOrder));
                try {//推播動作 不影響回傳結果
                    String account = ecStore.getSeller().getMember().getLoginAccount();
                    String title = ResourceBundleUtils.getMessage(locale, "notify.msg.013");
                    String alert = MessageFormat.format(ResourceBundleUtils.getMessage(locale, "notify.msg.014"), order.getOrderNumber());
                    pushService.sendPushAlias("order", ecOrder.getId().toString(), "order", ecOrder.getStatus().getCode(),
                            title, alert, account);
                } catch (Exception ex) {
                    logger.error("push fail Exception:" + ex);
                }
            }
            result.setOrderList(resultOrderList);
//            result.setMsg(msg.toString());
            logger.debug("order create msg:" + msg.toString());
            if (StringUtils.isBlank(msg.toString())) {
                result.setMsg("SUCCESS");
            } else {
                result.setMsg(msg.toString());
            }
            return result;
        } catch (Exception ex) {
            logger.error("order create ex ..." + ex);
            msg.append("訂單建立失敗! ex ...").append(ex.toString());
            ex.printStackTrace();
            
            orderTransVO.setMsg(msg.toString());
            return orderTransVO;
        }
    }
    
    @POST
    @Path("quotation")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/json; charset=UTF-8;")
    public OrderTransVO quotation(@Context HttpServletRequest request,
            OrderTransVO orderTransVO
    ) {
        logger.debug("quotation start");
        OrderTransVO result = new OrderTransVO();
        StringBuffer msg = new StringBuffer("");
        try {
            List<Order> orderList = orderTransVO.getOrderList();
            List<Order> resultOrderList = new ArrayList<>();
            Locale locale = getLocale(request);
            EcMember member = getAuthMember();
            if (member == null) {
//                msg.append("訂單報價失敗! 查無登入者資訊");
                msg.append(ResourceBundleUtils.getMessage(locale, "error.order.010"));
            }
            if (CollectionUtils.isEmpty(orderList)) {
//                msg.append("訂單報價失敗! 無訂單");
                msg.append(ResourceBundleUtils.getMessage(locale, "error.order.011"));
            }
            logger.debug("create orderlist:" + orderList.size());
            for (Order order : orderList) {
                if (order.getId() != null && order.getTotal() != null && order.getStatus().equals(OrderStatusEnum.Quotation)) {
                    if (CollectionUtils.isNotEmpty(order.getOrderDetails())) {
                        for (OrderDetail orderDetail : order.getOrderDetails()) {//修改單價
                            EcOrderDetail ecOrderDetail = ecOrderDetailFacade.find(orderDetail.getId());
                            ecOrderDetail.setPrice(orderDetail.getPrice());
                            ecOrderDetail.setTotal(orderDetail.getTotal());
                            ecOrderDetail.setQuantity(orderDetail.getQuantity());
                            ecOrderDetail.setModifier(member);
                            ecOrderDetailFacade.save(ecOrderDetail);
                        }
                    }
                    EcOrder ecOrder = ecOrderFacade.find(order.getId());
                    ecOrder.setTotal(order.getTotal());
                    ecOrder.setSubTotal(order.getSubTotal());
                    ecOrder.setShippingTotal(order.getShippingTotal());
                    ecOrder.setStatus(OrderStatusEnum.Quotation);
                    ecOrder.setModifier(member);
                    ecOrderFacade.save(ecOrder);
                    
                    resultOrderList.add(entityTransforVO.orderTransfor(ecOrderFacade.find(order.getId())));
                    try {//推播動作 不影響回傳結果
                        String account = ecOrder.getMember().getLoginAccount();
                        String title = ResourceBundleUtils.getMessage(locale, "notify.msg.001");
                        String alert = MessageFormat.format(ResourceBundleUtils.getMessage(locale, "notify.msg.002"), ecOrder.getOrderNumber());
                        pushService.sendPushAlias("order", ecOrder.getId().toString(), "order", OrderStatusEnum.Quotation.getCode(),
                                title, alert, account);
                    } catch (Exception ex) {
                        logger.error("push fail Exception:" + ex);
                    }
                } else {
//                    msg.append("訂單報價失敗! 訂單編號:").append(order.getOrderNumber()).append(" 訂單資訊不完整");
                    msg.append(MessageFormat.format(ResourceBundleUtils.getMessage(locale, "error.order.012"), order.getOrderNumber()));
                    continue;
                }
            }
            
            result.setOrderList(resultOrderList);
            if (StringUtils.isBlank(msg.toString())) {
                result.setMsg("SUCCESS");
            } else {
                result.setMsg(msg.toString());
            }
            return result;
        } catch (Exception ex) {
            logger.error("order create ex ..." + ex);
            msg.append("訂單報價失敗! ex ...").append(ex.toString());
            
            orderTransVO.setMsg(msg.toString());
            return orderTransVO;
        }
    }
    
    @GET
    @Path("messageCreate")
    @Produces("application/json; charset=UTF-8;")
    public String messageCreate(@Context HttpServletRequest request,
            @QueryParam(value = "orderId") Long orderId,
            @QueryParam(value = "message") String message) {
        logger.debug("messageCreate orderId:" + orderId);
        logger.debug("messageCreate message:" + message);
        
        EcOrder ecOrder = ecOrderFacade.find(orderId);
        Locale locale = getLocale(request);
        EcMember member = getAuthMember();
        if (ecOrder == null || member == null || message == null) {
//            return null;
            return GlobalConstant.RS_RESULT_FAIL;
        }
        boolean buyer = ecOrder.getMember().equals(member);//買方留言
        EcOrderMessage entity = new EcOrderMessage();
        entity.setOrder(ecOrder);
        entity.setStore(ecOrder.getStore());
        entity.setMessage(message);
        entity.setCreator(member);
        entity.setBuyer(buyer);
        ecOrderMessageFacade.save(entity);
        
        try {//推播動作 不影響回傳結果
            String title = ResourceBundleUtils.getMessage(locale, "notify.msg.003");
            String alert = MessageFormat.format(ResourceBundleUtils.getMessage(locale, "notify.msg.005"), ecOrder.getOrderNumber(), message);
            if (buyer) {//買方留言
                if (ecOrder.getStore() != null && ecOrder.getStore().getSeller() != null) {
                    String account = ecOrder.getStore().getSeller().getMember().getLoginAccount();
                    pushService.sendPushAlias("order", orderId.toString(), title, alert, account);
                }
            } else {//賣方留言
                String account = ecOrder.getMember().getLoginAccount();
                pushService.sendPushAlias("order", orderId.toString(), title, alert, account);
            }
        } catch (Exception ex) {
            logger.error("push fail Exception:" + ex);
        }

//        ecOrder = ecOrderFacade.find(orderId);
//        return entityTransforVO.orderTransfor(ecOrder);
        return GlobalConstant.RS_RESULT_SUCCESS;
    }
    
    @GET
    @Path("findMessage")
    @Produces("application/json; charset=UTF-8;")
    public List<OrderMessage> findMessage(@Context HttpServletRequest request,
            @QueryParam(value = "orderId") Long orderId) {
        logger.debug("findMessage orderId:" + orderId);
        
        EcOrder ecOrder = ecOrderFacade.find(orderId);
        EcMember member = getAuthMember();
        if (ecOrder == null || member == null) {
            return null;
        }
        ecOrderMessageFacade.updateReadtime(ecOrder, member);//寫入已讀時間

        List<EcOrderMessage> messageList = ecOrderMessageFacade.findByOrder(ecOrder);
        List<OrderMessage> result = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(messageList)) {
            for (EcOrderMessage message : messageList) {
//                logger.debug("findMessage message:"+message.getCreatetime());
//                logger.debug("findMessage OrderMessage:"+entityTransforVO.orderMessageTransfor(message).getCreatetime());
                result.add(entityTransforVO.orderMessageTransfor(message));
            }
        }
        
        return result;
    }
    
    @GET
    @Path("orderRate")
    @Produces("application/json; charset=UTF-8;")
    public String orderRate(@Context HttpServletRequest request,
            @QueryParam(value = "orderId") Long orderId,
            @QueryParam(value = "rate") Integer rate,
            @QueryParam(value = "message") String message) {
        logger.debug("orderRate orderId:" + orderId);
        logger.debug("orderRate message:" + message);
        
        EcOrder ecOrder = ecOrderFacade.find(orderId);
        Locale locale = getLocale(request);
        EcMember member = getAuthMember();
        if (ecOrder == null || member == null || message == null || ecOrder.getStore() == null) {
            return "FAIL";
        }
        boolean buyer = ecOrder.getMember().equals(member);//買方給評

        EcOrderRate entity = ecOrderRateFacade.findByOrderId(ecOrder.getStore().getId(), orderId);
        if (entity != null) {
        } else {
            entity = new EcOrderRate();
            entity.setOrderId(orderId);
            entity.setCreator(member);
            entity.setStoreId(ecOrder.getStore().getId());
        }
        if (buyer) {//買方給賣家評價
            if (entity.getSellerRate() != null) {
                return "FAIL";
            }
            entity.setSellerRate(rate);
            entity.setSellerMessage(message);
        } else {
            if (entity.getCustomerRate() != null) {
                return "FAIL";
            }
            entity.setCustomerRate(rate);
            entity.setCustomerMessage(message);
        }
        ecOrderRateFacade.save(entity);
        
        try {//推播動作 不影響回傳結果
            String title = ResourceBundleUtils.getMessage(locale, "notify.msg.015");
            String alert = MessageFormat.format(ResourceBundleUtils.getMessage(locale, "notify.msg.005"), ecOrder.getOrderNumber(), message);
            if (buyer) {//買方
                if (ecOrder.getStore() != null && ecOrder.getStore().getSeller() != null) {
                    String account = ecOrder.getStore().getSeller().getMember().getLoginAccount();
                    pushService.sendPushAlias("order", orderId.toString(), title, alert, account);
                }
            } else {//賣方
                String account = ecOrder.getMember().getLoginAccount();
                pushService.sendPushAlias("order", orderId.toString(), title, alert, account);
            }
        } catch (Exception ex) {
            logger.error("push fail Exception:" + ex);
        }
        
        return "SUCCESS";
    }
    
    @GET
    @Path("findOrderRate")
    @Produces("application/json; charset=UTF-8;")
    public OrderRate findOrderRate(@Context HttpServletRequest request,
            @QueryParam(value = "orderId") Long orderId) {
        logger.debug("findOrderRate orderId:" + orderId);
        
        EcOrder ecOrder = ecOrderFacade.find(orderId);
        EcMember member = getAuthMember();
        if (ecOrder == null || member == null) {
            return null;
        }
        EcOrderRate entity = ecOrderRateFacade.findByOrderId(ecOrder.getStore().getId(), orderId);
        OrderRate result = new OrderRate();
        result.setOrderId(orderId);
        result.setStoreId(ecOrder.getStore().getId());
        if (entity.getCustomerRate() != null) {
            result.setCustomerRate(entity.getCustomerRate());
            result.setCustomerMessage(entity.getCustomerMessage());
        }
        if (entity.getSellerRate() != null) {
            result.setSellerRate(entity.getSellerRate());
            result.setSellerMessage(entity.getSellerMessage());
        }
        
        return result;
    }

    //<editor-fold defaultstate="collapsed" desc="EC 1.5">
    //賣方確認
    //出貨
    //到貨
    //賣方最後修改
    //買方最後確認
    //結案
    @POST
    @Path("1.5/changeStatus")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/json; charset=UTF-8;")
    public String changeStatus_15(@Context HttpServletRequest request,
            @QueryParam(value = "type") String type,
            @QueryParam(value = "status") String status,
            Order order
    ) {
        logger.debug("approve start");
        Long orderId = order.getId();
        EcOrder ecOrder = ecOrderFacade.find(orderId);
        Locale locale = getLocale(request);
        EcMember member = getAuthMember();
        String message = "";
        if (ecOrder == null || member == null) {
            return GlobalConstant.RS_RESULT_FAIL;
        }
        
        if ("order".equals(type)) {
            OrderStatusEnum enums = OrderStatusEnum.fromString(status);
            order.setStatus(enums);
            if (OrderStatusEnum.Approve.equals(enums)
                    && ShipStatusEnum.A.equals(order.getShipStatus())) {//訂單確認 修改單價 運費 訂單金額
                ecOrder.setOriTotal(order.getTotal());//寫入原始訂單金額  訂單確認 已修改單價、運費
                this.updateOrderTotal(ecOrder, order);
            } else if (OrderStatusEnum.Declined.equals(enums)
                    || OrderStatusEnum.Returned.equals(enums)
                    || OrderStatusEnum.Cancelled.equals(enums)) {
                //買方到貨後 不可執行Declined Returned
                //買方最後確認後 不可執行Declined Returned
//                if(ShipStatusEnum.D.equals(ecOrder.getShipStatus())){
            }
            
            message = ResourceBundleUtils.getMessage(locale, "notify.msg.010")
                    + ":[" + ecOrder.getStatus().name() + "] ==> [" + status + "]";
            ecOrderFacade.changeStatus(ecOrder, enums, message, member);
//        }else if("pay".equals(type)){
        } else if ("ship".equals(type)) {
            if (!ecOrder.getBuyerCheck()) {
                logger.error("買方確認前 不可修改出貨狀態!");
                return GlobalConstant.RS_RESULT_FAIL;
            }
            ShipStatusEnum enums = ShipStatusEnum.fromCode(status);
            if (ShipStatusEnum.B.equals(enums)) {//20190129 只有出貨 改數量
//        if(OrderStatusEnum.Close.equals(order.getStatus()) 
//                || ShipStatusEnum.B.equals(order.getShipStatus())
//                || ShipStatusEnum.D.equals(order.getShipStatus())){//出貨 到貨 訂單結案 修改數量 訂單金額 
                this.updateOrderTotal(ecOrder, order);
                
                ecOrder.setShippingTime(new Date());//出貨日
            }else if(ShipStatusEnum.D.equals(enums)){
                ecOrder.setDeliveryDate(new Date());//到貨日
            }
            
            message = ResourceBundleUtils.getMessage(locale, "notify.msg.012")
                    + ":[" + ecOrder.getShipStatus().name() + "] ==> [" + status + "]";
            ecOrderFacade.changeStatus(ecOrder, enums, message, member);
        }
        EcOrder entity = ecOrderFacade.find(orderId);
        try {//推播動作 不影響回傳結果
            // 您的訂單狀態已變更! type : order, pay, ship
            String title = ResourceBundleUtils.getMessage(locale, "notify.msg.004");
            // 訂單編號[{0}], {1}
            String msg = MessageFormat.format(ResourceBundleUtils.getMessage(locale, "notify.msg.005"), ecOrder.getOrderNumber(), message);
            if (entity.getMember().equals(member)) {//買方留言
                if (entity.getStore() != null && entity.getStore().getSeller() != null) {
                    String account = entity.getStore().getSeller().getMember().getLoginAccount();
                    pushService.sendPushAlias("order", orderId.toString(), type, status, title, msg, account);
                }
            } else {//賣方改狀態
                String account = entity.getMember().getLoginAccount();
                pushService.sendPushAlias("order", orderId.toString(), type, status, title, msg, account);
            }
        } catch (Exception ex) {
            logger.error("push fail Exception:" + ex);
            return GlobalConstant.RS_RESULT_FAIL;
        }
        
        return GlobalConstant.RS_RESULT_SUCCESS;
    }

    //更新 訂單 明細 金額數量
    private EcOrder updateOrderTotal(EcOrder ecOrder, Order order) {
        List<OrderDetail> detailList = order.getOrderDetails();
        if (order.getId() != null && CollectionUtils.isNotEmpty(detailList)) {
            for (OrderDetail detail : detailList) {
                EcOrderDetail ecOrderDetail = ecOrderDetailFacade.find(detail.getId());
                ecOrderDetail.setPrice(detail.getPrice());
                ecOrderDetail.setQuantity(detail.getQuantity());
                ecOrderDetail.setTotal(detail.getTotal());
                ecOrderDetail.setShipping(detail.getShipping());
                
                ecOrderDetailFacade.save(ecOrderDetail);
            }
        }
        
        if (OrderStatusEnum.Approve.equals(order.getStatus())
                && ShipStatusEnum.A.equals(order.getShipStatus())) {//訂單確認 修改單價 運費 訂單金額
            ecOrder.setApprovetime(new Date());//訂單成立時間
            ecOrder.setBuyerCheck(Boolean.FALSE);
        }
        if (ShipStatusEnum.B.equals(order.getShipStatus())) {
            ecOrder.setShippingTime(new Date());//確認出貨時間
        }
        ecOrder.setSubTotal(order.getSubTotal());
        ecOrder.setShippingTotal(order.getShippingTotal());
        ecOrder.setTotal(order.getTotal());
        
        return ecOrderFacade.save(ecOrder);
    }

    //賣方修改訂單 訂單 明細 金額數量
    @POST
    @Path("1.5/orderModify")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/json; charset=UTF-8;")
    public String orderModify(@Context HttpServletRequest request,
            Order order
    ) {
//    private EcOrder updateOrderTotal(EcOrder ecOrder, Order order) {
        logger.debug("orderModify start");
        Long orderId = order.getId();
        EcOrder ecOrder = ecOrderFacade.find(orderId);
        Locale locale = getLocale(request);
        EcMember member = getAuthMember();
        String message = "";
        if (ecOrder == null || member == null
                || OrderStatusEnum.Close.equals(ecOrder.getStatus())) {
            return GlobalConstant.RS_RESULT_FAIL;
        }
        
        List<OrderDetail> detailList = order.getOrderDetails();
        if (order.getId() != null && CollectionUtils.isNotEmpty(detailList)) {
            for (OrderDetail detail : detailList) {
                EcOrderDetail ecOrderDetail = ecOrderDetailFacade.find(detail.getId());
                ecOrderDetail.setPrice(detail.getPrice());
                ecOrderDetail.setQuantity(detail.getQuantity());
                ecOrderDetail.setTotal(detail.getTotal());
                ecOrderDetail.setShipping(detail.getShipping());
                ecOrderDetailFacade.save(ecOrderDetail);
            }
        }
        ecOrder.setSubTotal(order.getSubTotal());
        ecOrder.setShippingTotal(order.getShippingTotal());
        ecOrder.setTotal(order.getTotal());
        ecOrder.setBuyerCheck(Boolean.FALSE);
        ecOrderFacade.save(ecOrder);
        try {//推播動作 不影響回傳結果
            // 您的訂單已修改! type : order, pay, ship
            String title = ResourceBundleUtils.getMessage(locale, "notify.msg.016");
            // 訂單編號[{0}], {1}
            String msg = MessageFormat.format(ResourceBundleUtils.getMessage(locale, "notify.msg.005"), ecOrder.getOrderNumber(), message);
            String account = ecOrder.getMember().getLoginAccount();
            pushService.sendPushAlias("order", orderId.toString(), "order", "", title, msg, account);
            
            ecOrderFacade.addLog(ecOrder, "orderModify", member, "賣方修改訂單");
        } catch (Exception ex) {
            logger.error("push fail Exception:" + ex);
//            return GlobalConstant.RS_RESULT_FAIL;
        }
        
        return GlobalConstant.RS_RESULT_SUCCESS;
    }

    //買方確認
    @GET
    @Path("1.5/buyerCheck")
    @Produces("application/json; charset=UTF-8;")
    public String buyerCheck(@Context HttpServletRequest request,
            @QueryParam(value = "orderId") Long orderId) {
        EcOrder ecOrder = ecOrderFacade.find(orderId);
        Locale locale = getLocale(request);
        EcMember member = getAuthMember();
        String message = "";
        if (ecOrder == null || member == null
                || OrderStatusEnum.Close.equals(ecOrder.getStatus())) {
            return GlobalConstant.RS_RESULT_FAIL;
        }
        
        ecOrder.setBuyerCheck(Boolean.TRUE);
        ecOrderFacade.save(ecOrder);
        
        try {//推播動作 不影響回傳結果
            // 您的訂單修改已確認! 
            String title = ResourceBundleUtils.getMessage(locale, "notify.msg.017");
            // 訂單編號[{0}], {1}
            String msg = MessageFormat.format(ResourceBundleUtils.getMessage(locale, "notify.msg.005"), ecOrder.getOrderNumber(), message);
            String account = ecOrder.getStore().getSeller().getMember().getLoginAccount();
            pushService.sendPushAlias("order", orderId.toString(), "order", "", title, msg, account);
            
            ecOrderFacade.addLog(ecOrder, "orderModify", member, "買方確認");
        } catch (Exception ex) {
            logger.error("push fail Exception:" + ex);
//            return GlobalConstant.RS_RESULT_FAIL;
        }
        
        return GlobalConstant.RS_RESULT_SUCCESS;
    }

    //EC1.0 下單時查詢1.5的訂單
    @GET
//    @POST
    @Path("1.5/findForEC1.0")
//    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/json; charset=UTF-8;")
    public List<Order> findForEC_10(@QueryParam(value = "account") String account) {
        logger.debug("order findForEC_10 start");
        List<Order> result = new ArrayList<>();
//        Locale locale = getLocale(request);
        Locale locale = LocaleEnum.SIMPLIFIED_CHINESE.getLocale();//EC1.5就固定用簡體
        try {
            if (StringUtils.isBlank(account)) {
                return result;
            }
            EcMember member = ecMemberFacade.findByLoginAccount(account);
            if (member == null) {
                return result;
            }
            List<Long> idlist = ecStoreFacade.findIdListByMember(member);
            if (CollectionUtils.isEmpty(idlist)) {
                return result;
            }
            
            OrderFilter filter = new OrderFilter();
//            SimpleDateFormat sdFormat = new SimpleDateFormat(GlobalConstant.FORMAT_DATE_STR);
//            if (StringUtils.isNotEmpty(filter.getCreatetimeBeginStr())) {
//                Date begin = sdFormat.parse(filter.getCreatetimeBeginStr());
//                filter.setCreatetimeBegin(begin);
//            }
//            if (StringUtils.isNotEmpty(filter.getCreatetimeEndStr())) {
//                Date end = sdFormat.parse(filter.getCreatetimeEndStr());
//                filter.setCreatetimeEnd(end);
//            }
            //訂單已確認 未出貨
            filter.setStatus(OrderStatusEnum.Approve);
            filter.setShipStatus(ShipStatusEnum.A);
            filter.setStoreId(idlist.get(0));
            
            List<EcOrder> list = ecOrderFacade.findByCriteria(filter);
            if (CollectionUtils.isNotEmpty(list)) {
                for (EcOrder entity : list) {
                    result.add(entityTransforVO.orderTransfor(entity, locale));
                }
            }
        } catch (Exception e) {
            logger.error("Exception:" + e);
        }
        return result;
    }
    
    //修改車號
    public void changeCarInfo(String carNo, EcOrder order) {
        this.changeCarInfo(null, carNo, order.getId());
    }
    @GET
    @Path("1.5/changeCarInfo")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/json; charset=UTF-8;")
    public String changeCarInfo(@Context HttpServletRequest request,
            @QueryParam(value = "carNo") String carNo,
            @QueryParam(value = "orderId") Long orderId) {
        try{
            EcOrder order = ecOrderFacade.find(orderId);
            EcMember member = getAuthMember();
            if (StringUtils.isNotBlank(carNo) && order!=null && member!=null) {
                EcOrderShipInfo shipInfo = ecOrderShipInfoFacade.findByOrder(order);
                if(shipInfo!=null){
                    shipInfo.setCarNo(carNo);
                    shipInfo.setModifier(member);
                    shipInfo.setModifytime(new Date());
                    ecOrderShipInfoFacade.save(shipInfo);
                    logger.info("changeCarInfo carNo:" + carNo);
                    logger.info("changeCarInfo carNo size:" + carNo.split(";").length);
                    ecOrderCarInfoFacade.deleteAndInsertCarInfo(carNo, order);
                }
            }else{
                return GlobalConstant.RS_RESULT_FAIL;
            }
        } catch (Exception e) {
            logger.error("Exception:" + e);
        }
                                
        return GlobalConstant.RS_RESULT_SUCCESS;
    }

    //</editor-fold>
}
