/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade.rs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcci.cm.annotation.util.AnnotationExportUtils;
import com.tcci.cm.enums.NumericPatternEnum;
import com.tcci.cm.facade.rs.filter.JWTTokenNeeded;
import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.cm.util.ExportUtils;
import com.tcci.cm.util.ExtBeanUtils;
import com.tcci.cm.util.WebUtils;
import com.tcci.ec.entity.EcMember;
import com.tcci.ec.entity.EcOrderMessage;
import com.tcci.ec.entity.EcOrderProcess;
import com.tcci.ec.entity.EcOrderRate;
import com.tcci.ec.entity.EcStore;
import com.tcci.ec.enums.MessageTypeEnum;
import com.tcci.ec.enums.OrderLogEnum;
import com.tcci.ec.enums.OrderStatusEnum;
import com.tcci.ec.enums.PayStatusEnum;
import com.tcci.ec.enums.RfqStatusEnum;
import com.tcci.ec.enums.ShipStatusEnum;
import com.tcci.ec.enums.ec10.ShipMethodEC10Enum;
import com.tcci.ec.enums.ec10.TranModeEC10Enum;
import com.tcci.ec.enums.ec10.TranTypeEC10Enum;
import com.tcci.ec.enums.rs.ResStatusEnum;
import com.tcci.ec.facade.EcCompanyFacade;
import com.tcci.ec.facade.EcOrderCarInfoFacade;
import com.tcci.ec.facade.EcOrderFacade;
import com.tcci.ec.facade.EcOrderLogFacade;
import com.tcci.ec.facade.EcOrderMessageFacade;
import com.tcci.ec.facade.EcOrderProcessFacade;
import com.tcci.ec.facade.EcOrderRateFacade;
import com.tcci.ec.facade.EcPersonFacade;
import com.tcci.ec.facade.EcStockLogFacade;
import com.tcci.ec.facade.EcTccOrderFacade;
import com.tcci.ec.model.FileVO;
import com.tcci.ec.model.OrderCarInfoVO;
import com.tcci.ec.model.OrderMessageVO;
import com.tcci.ec.model.criteria.OrderCriteriaVO;
import com.tcci.ec.model.OrderVO;
import com.tcci.ec.model.OrderProcessVO;
import com.tcci.ec.model.OrderVO15;
import com.tcci.ec.model.TccOrderVO;
import com.tcci.ec.model.e10.ContractE10VO;
import com.tcci.ec.model.e10.ContractProductVO;
import com.tcci.ec.model.e10.CustomerE10VO;
import com.tcci.ec.model.e10.DeliveryPlacesEC10VO;
import com.tcci.ec.model.e10.PlantE10VO;
import com.tcci.ec.model.e10.ProductE10VO;
import com.tcci.ec.model.e10.SalesAreaE10VO;
import com.tcci.ec.model.e10.SalesE10VO;
import com.tcci.ec.model.rs.Ec10ReqVO;
import com.tcci.ec.model.rs.Ec10ResVO;
import com.tcci.ec.model.rs.SubmitVO;
import com.tcci.fc.util.DateUtils;
import com.tcci.fc.util.ResourceBundleUtils;
import com.tcci.security.TokenProvider;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.poi.ss.usermodel.Workbook;
import org.jboss.weld.util.collections.Sets;

/**
 * 訂單相關服務
 * @author peter.pan
 */
@Path("/orders")
public class OrderREST extends AbstractWebREST {
    @EJB EcOrderFacade orderFacade;
    @EJB EcPersonFacade personFacade;
    @EJB EcCompanyFacade companyFacade;
    @EJB EcOrderProcessFacade orderProcessFacade;
    @EJB EcOrderMessageFacade orderMessageFacade;
    @EJB EcOrderRateFacade orderRateFacade;
    @EJB EcOrderLogFacade orderLogFacade;
    @EJB EcStockLogFacade stockLogFacade;
    @EJB EcTccOrderFacade tccOrderFacade;
    @EJB EcOrderCarInfoFacade orderCarInfoFacade;
    
    protected Map<String, String> sortFieldMapRFQ;
    
    public OrderREST(){
        logger.debug("OrderREST init ...");
        // for 支援排序
        // PO
        // sortField : PrimeUI column fields map to SQL fields
        sortFieldMap = new HashMap<String, String>();
        sortFieldMap.put("id", "S.ID");// ID
        sortFieldMap.put("statusLabel", "S.STATUS");// 狀態
        sortFieldMap.put("orderNumber", "S.ORDER_NUMBER");// 訂單編號
        sortFieldMap.put("approvetime", "S.APPROVETIME");// 訂單時間
        sortFieldMap.put("itemCount", "D.ITEM_COUNT");// 商品筆數
        sortFieldMap.put("subTotal", "S.SUB_TOTAL");// 商品總金額
        sortFieldMap.put("shippingTotal", "S.SHIPPING_TOTAL");// 運費
        sortFieldMap.put("discountTotal", "S.DISCOUNT_TOTAL");// 折扣金額
        sortFieldMap.put("total", "S.TOTAL");// 應付總金額
        sortFieldMap.put("curName", "CUR.NAME");// 幣別
        sortFieldMap.put("payment", "PAY.TITLE");// 付款方式
        sortFieldMap.put("cname", "CUS.CNAME");// 客戶姓名
        sortFieldMap.put("phone", "CUS.PHONE");// 聯絡電話
        sortFieldMap.put("email1", "CUS.EMAIL");// 聯絡E-Mail
        sortFieldMap.put("message", "S.MESSAGE");// 備註事項

        // sortOrder : PrimeUI sortOrder 1/-1
        sortOrderMap = new HashMap<String, String>();
        sortOrderMap.put("-1", "DESC");
        sortOrderMap.put("1", "");
        
        // RFQ
        sortFieldMapRFQ = new HashMap<String, String>();
        sortFieldMapRFQ.put("id", "S.ID");// ID
        sortFieldMapRFQ.put("statusLabel", "S.STATUS");// 狀態
        sortFieldMapRFQ.put("orderNumber", "S.ORDER_NUMBER");// 編號
        sortFieldMapRFQ.put("createtime", "S.CREATETIME");// 詢價時間
        sortFieldMapRFQ.put("itemCount", "D.ITEM_COUNT");// 商品筆數
        sortFieldMapRFQ.put("subTotal", "S.SUB_TOTAL");// 原商品總價
        sortFieldMapRFQ.put("total", "S.TOTAL");// 報價總金額
        sortFieldMapRFQ.put("curName", "CUR.NAME");// 幣別
        sortFieldMapRFQ.put("cname", "CUS.CNAME");// 客戶姓名
        sortFieldMapRFQ.put("phone", "CUS.PHONE");// 聯絡電話
        sortFieldMapRFQ.put("email1", "CUS.EMAIL");// 聯絡E-Mail
        sortFieldMapRFQ.put("message", "S.MESSAGE");// 備註事項       
    }
    
    //<editor-fold defaultstate="collapsed" desc="for RFQ Main">
    /**
     * 查詢 - 先抓總筆數
     * /services/orders/rfq/count
     * @param request
     * @param formVO
     * @return 
     */
    @POST
    @Path("/rfq/count")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response countRfqs(@Context HttpServletRequest request, SubmitVO formVO){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("countRfqs ...");
        if( formVO==null ){
            formVO = new SubmitVO();
        }
        formVO.setRfq(Boolean.TRUE);
        formVO.setStatusList(RfqStatusEnum.getCodes());
        return countOrdersAll(request, formVO);
    }
    
    /**
     * 查詢
     * @param request
     * @param formVO
     * @param offset
     * @param rows
     * @param sortField
     * @param sortOrder
     * @return 
     */
    @POST
    @Path("/rfq/list")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response findRfqs(@Context HttpServletRequest request, SubmitVO formVO,
            @QueryParam("offset")Integer offset, @QueryParam("rows")Integer rows,
            @QueryParam("sortField")String sortField, @QueryParam("sortOrder")String sortOrder
    ){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("findRfqs ...");
        if( formVO==null ){
            formVO = new SubmitVO();
        }
        formVO.setRfq(Boolean.TRUE);
        formVO.setStatusList(RfqStatusEnum.getCodes());
        return findOrdersAll(request, formVO, offset, rows, sortField, sortOrder);
    }
    
    /**
     * 拒絕總價
     * or
     * 報價(總價) - 目前不使用，改報單價
     * /services/orders/rfq/save
     * @param request
     * @param formVO
     * @return 
     */
    @POST
    @Path("/rfq/save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response saveRfq(@Context HttpServletRequest request, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("saveRfq ...");
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            boolean admin = hasAdminRights(request, member);
            // 賣家專屬 RESTful
            if( !checkPermissions(methodName, member, store, false, true, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            if( formVO==null || formVO.getId()==null || formVO.getStatus()==null ){
                logger.error("saveRfq formVO==null");
                return Response.notAcceptable(null).build();
            }
            RfqStatusEnum rfqStatusEnum = RfqStatusEnum.getFromCode(formVO.getStatus());
            if( rfqStatusEnum==null ){
                logger.error("saveRfq rfqStatusEnum==null");
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR, errors);
            }
            
            Long orderId = formVO.getId();
            OrderVO orderVO = orderFacade.findById(store.getId(), orderId, false, locale, sys.isTrue(member.getTccDealer()));
            if( orderVO==null ){
                logger.error("saveRfq orderVO==null");
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR, errors);
            }
            
            formVO.setStoreId(store.getId());
            
            BigDecimal shippingTotal = null;
            BigDecimal total = null;
            boolean update = false;
            if( rfqStatusEnum==RfqStatusEnum.Quotation ){// 報價(總價)
                if( !GlobalConstant.QUOTE_TOTAL ){// 改報單價
                    logger.error("saveRfq QUOTE_TOTAL=="+GlobalConstant.QUOTE_TOTAL);
                    return Response.notAcceptable(null).build();
                }
                if( formVO.getShippingTotal()!=null && formVO.getSubTotal()!=null 
                    && formVO.getShippingTotal().doubleValue()>=0 && formVO.getSubTotal().doubleValue()>0 ){// 輸入檢查
                    update = true;
                    orderVO.setShippingTotal(formVO.getShippingTotal());
                    orderVO.setSubTotal(formVO.getSubTotal());
                    
                    shippingTotal = formVO.getShippingTotal();
                    total = formVO.getShippingTotal().add(formVO.getSubTotal());
                    
                    orderVO.setTotal(total);
                }
            }else if( rfqStatusEnum==RfqStatusEnum.Rejected ){// 拒絕
                update = true;
            }
            
            if( update ){
                orderVO.setStatus(rfqStatusEnum.getCode());
                orderFacade.saveVO(orderVO, member, false);

                // EC_ORDER_LOG
                Long logId = orderLogFacade.save(store.getId(), orderId, rfqStatusEnum.getDisplayName(locale), false, 
                                    OrderLogEnum.ORDER.getCode(), orderVO.getStatus(), shippingTotal, total, 
                                    member, false);
                logger.info("saveRfq orderLog = "+logId);
                
                // return
                orderVO = orderFacade.findById(store.getId(), orderVO.getId(), true, locale, sys.isTrue(member.getTccDealer()));
                
                if( rfqStatusEnum==RfqStatusEnum.Quotation ){// 報價
                    msg.quotePrice(orderVO, member, locale);
                }
                return this.genSuccessRepsone(request, orderVO);
            }else{
                logger.error("saveRfq no support status = " + rfqStatusEnum);
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR, errors);
            }
        }catch(Exception e){
            logger.error("saveRfq Exception:\n", e);
            sys.processUnknowException(member, methodName, e);
        }

        return this.genFailRepsone(request);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="for Order Main">
    /**
     * 查詢 - 先抓總筆數
     * /services/orders/count
     * @param request
     * @param formVO
     * @return 
     */
    @POST
    @Path("/count")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response countOrders(@Context HttpServletRequest request, SubmitVO formVO){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("countOrders ...");
        if( formVO==null ){
            formVO = new SubmitVO();
        }
        formVO.setRfq(Boolean.FALSE);
        formVO.setStatusList(OrderStatusEnum.getCodes());
        return countOrdersAll(request, formVO);
    }
    
    /**
     * 查詢
     * /services/orders/list
     * @param request
     * @param formVO
     * @param offset
     * @param rows
     * @param sortField
     * @param sortOrder
     * @return 
     */
    @POST
    @Path("/list")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response findOrders(@Context HttpServletRequest request, SubmitVO formVO,
            @QueryParam("offset")Integer offset, @QueryParam("rows")Integer rows,
            @QueryParam("sortField")String sortField, @QueryParam("sortOrder")String sortOrder
    ){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("findOrders ...");
        if( formVO==null ){
            formVO = new SubmitVO();
        }
        formVO.setRfq(Boolean.FALSE);
        formVO.setStatusList(OrderStatusEnum.getCodes());
        return findOrdersAll(request, formVO, offset, rows, sortField, sortOrder);
    }

    /**
     * 訂單狀態變更(正常流程)
     * --------------------
     * 確認訂單(無改價功能 for EC2.0 Only) 
     * 賣家回絕訂單（賣家未確認）
     * 賣家取消訂單（賣家已確認 ＆ 未付款 ＆ 未出貨）
     * 賣家結案訂單（賣家已確認 ＆ 已付款 ＆ 已出貨）
     * 
     * /services/orders/status/save
     * @param request
     * @param formVO
     * @return 
     */
    @POST
    @Path("/status/save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response savePoStatus(@Context HttpServletRequest request, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("savePoStatus ...");
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            boolean admin = hasAdminRights(request, member);
            boolean tccDealer = isTccDealer(request, member);
            // 賣家專屬 RESTful
            if( !checkPermissions(methodName, member, store, false, true, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            if( formVO==null || formVO.getId()==null || formVO.getStatus()==null ){
                logger.error("savePoStatus formVO==null");
                return Response.notAcceptable(null).build();
            }
            if( store==null || store.getId()==null ){
                logger.error("savePoStatus store==null || store.getId()==null");
                return Response.status(Response.Status.NOT_ACCEPTABLE).build();
            }
            OrderStatusEnum newStatus = OrderStatusEnum.getFromCode(formVO.getStatus());
            
            Long orderId = formVO.getId();
            OrderVO orderVO = orderFacade.findById(store.getId(), orderId, false, locale, sys.isTrue(member.getTccDealer()));
            // 原狀態
            OrderStatusEnum oriStatus = OrderStatusEnum.getFromCode(orderVO.getStatus());
            ShipStatusEnum oriShipStatus = ShipStatusEnum.getFromCode(orderVO.getShipStatus());
            PayStatusEnum oriPayStatus = PayStatusEnum.getFromCode(orderVO.getPayStatus());
            
            logger.info("savePoStatus orderId = "+orderId+", newStatus = "+newStatus+", oriStatus = "+oriStatus);
            // 輸入檢查
            if( tccDealer && OrderStatusEnum.Approve==newStatus ){// 確認訂單 for EC2.0 Only 
                logger.error("savePoStatus error only for EC2.0 tccDealer="+tccDealer+", newStatus="+newStatus);
                return Response.notAcceptable(null).build();
            }
            if(// 確認(EC2.0 Only)、拒絕 - (原狀態為 待確認)
               (OrderStatusEnum.Pending==oriStatus && 
               (OrderStatusEnum.Approve==newStatus || OrderStatusEnum.Declined==newStatus))
               // 取消 (原狀態為 未出貨、未付款 才可取消)
            || ( OrderStatusEnum.Approve==oriStatus 
                && ShipStatusEnum.NOT_SHIPPED==oriShipStatus 
                && (PayStatusEnum.NOT_PAID==oriPayStatus || tccDealer)// EC1.5不考慮收付款狀態
                && OrderStatusEnum.Cancelled==newStatus)
               // 結案（買家已確認 ＆ 已付款 ＆ 已到貨 & buyerCheck=1）
            || ( OrderStatusEnum.Approve==oriStatus 
                && ShipStatusEnum.ARRIVED==oriShipStatus // 已到貨
                && (PayStatusEnum.PAID==oriPayStatus || tccDealer)// EC1.5不考慮收付款狀態
                && !sys.isFalse(orderVO.getBuyerCheck()) // buyerCheck=1
                && OrderStatusEnum.Close==newStatus)
            ){
                orderVO.setStatus(newStatus.getCode());
                if( OrderStatusEnum.Approve==newStatus ){// 確認
                    orderVO.setApprovetime(new Date());
                }
                orderFacade.saveVO(orderVO, member, false);
                logger.info("savePoStatus orderVO = "+orderVO.getId());
                
                // EC_ORDER_LOG
                Long logId = orderLogFacade.save(store.getId(), orderId, newStatus.getDisplayName(locale), false, 
                                    OrderLogEnum.ORDER.getCode(), orderVO.getStatus(), member, false);
                logger.info("savePoStatus orderLog = "+logId);
                
                // return
                orderVO = orderFacade.findById(store.getId(), orderVO.getId(), true, locale, sys.isTrue(member.getTccDealer()));
                
                // 影響庫存：EC_STOCK_LOG
                if( !tccDealer ){
                    if( OrderStatusEnum.Approve==newStatus ){// 確認
                        stockLogFacade.minusByOrderConfirm(orderVO, member, locale, false);
                        logger.info("savePoStatus minusByOrderConfirm ...");
                    }else if( OrderStatusEnum.Cancelled==newStatus ){// 取消
                        stockLogFacade.cancelOrder(store.getId(), orderId, member, false);
                        logger.info("savePoStatus cancelOrder ...");
                    }
                }
                
                if( OrderStatusEnum.Close!=newStatus ){// 結案不需通知
                    // push message
                    msg.changeOrderStatusBySeller(orderVO, "order", newStatus.getCode(), member, locale);
                }
                return this.genSuccessRepsone(request, orderVO);
            }else{
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR, errors);
            }
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }

        return this.genFailRepsone(request);
    }
    
    /**
     * 確認出貨 (賣家已確認 & buyerCheck=1；只適用於不可改量) 
     * 
     * /services/orders/ship/save
     * @param request
     * @param formVO
     * @return 
     */
    @POST
    @Path("/ship/save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response savePoShipping(@Context HttpServletRequest request, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("savePoShipping ...");
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            boolean admin = hasAdminRights(request, member);
            boolean tccDealer = isTccDealer(request, member);
            // 賣家專屬 RESTful
            if( !checkPermissions(methodName, member, store, false, true, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            if( formVO==null || formVO.getId()==null || formVO.getStatus()==null ){
                logger.error("savePoShipping formVO==null");
                return Response.notAcceptable(null).build();
            }
            if( store==null || store.getId()==null ){
                logger.error("savePoShipping store==null || store.getId()==null");
                return Response.status(Response.Status.NOT_ACCEPTABLE).build();
            }
            ShipStatusEnum newShipStatus = ShipStatusEnum.getFromCode(formVO.getStatus());
            
            Long orderId = formVO.getId();
            OrderVO orderVO = orderFacade.findById(store.getId(), orderId, false, locale, sys.isTrue(member.getTccDealer()));
            // 原狀態
            OrderStatusEnum oriStatus = OrderStatusEnum.getFromCode(orderVO.getStatus());
            ShipStatusEnum oriShipStatus = ShipStatusEnum.getFromCode(orderVO.getShipStatus());
            
            logger.info("savePoShipping orderId = "+orderId+", newShipStatus = "+newShipStatus
                    +", oriStatus = "+oriStatus+", oriShipStatus = "+oriShipStatus);
            // 須 賣家已確認 & buyerCheck=1
            if( oriStatus!=OrderStatusEnum.Approve || sys.isFalse(orderVO.getBuyerCheck()) ){
                logger.error("savePoShipping ori status error!");
                return Response.notAcceptable(null).build();
            }
            
            if( ShipStatusEnum.SHIPPED==newShipStatus || ShipStatusEnum.INSTALLMENT==newShipStatus ){// 輸入檢查
                orderVO.setShipStatus(newShipStatus.getCode());
                orderVO.setShippingTime(new Date());
                orderFacade.saveVO(orderVO, member, false);
                
                // EC_ORDER_LOG
                Long logId = orderLogFacade.save(store.getId(), orderId, newShipStatus.getDisplayName(locale), false, 
                                    OrderLogEnum.SHIPPING.getCode(), newShipStatus.getCode(), member, false);
                logger.info("savePoShipping orderLog = "+logId);
                
                // EC_STOCK_LOG
                if( !tccDealer ){
                    stockLogFacade.shippingByOrder(store.getId(), orderId, null, member, false);
                }
                
                orderVO = orderFacade.findById(store.getId(), orderId, true, locale, sys.isTrue(member.getTccDealer()));
                // push message
                msg.changeOrderStatusBySeller(orderVO, "ship", newShipStatus.getCode(), member, locale);
                
                return this.genSuccessRepsone(request, orderVO);
            }else{
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR, errors);
            }
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }

        return this.genFailRepsone(request);
    }

    /**
     * 訂單結案 (多筆)
     * /services/orders/close
     * 
     * @param request
     * @param formVO
     * @return 
     */
    @POST
    @Path("/close")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response closeMultiOrder(@Context HttpServletRequest request, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("closeMultiOrder ...");
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            boolean admin = hasAdminRights(request, member);
            boolean tccDealer = isTccDealer(request, member);
            // 賣家專屬 RESTful
            if( !checkPermissions(methodName, member, store, false, true, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            if( store==null || store.getId()==null ){
                logger.error("savePoStatus store==null || store.getId()==null");
                return Response.status(Response.Status.NOT_ACCEPTABLE).build();
            }
            if( formVO==null || sys.isEmpty(formVO.getOrderList()) || formVO.getStatus()==null ){
                logger.error("savePoStatus formVO==null");
                return Response.notAcceptable(null).build();
            }
            
            OrderStatusEnum newStatus = OrderStatusEnum.getFromCode(formVO.getStatus());
            
            int count = 0;
            for(Long orderId : formVO.getOrderList()){
                OrderVO orderVO = orderFacade.findById(store.getId(), orderId, false, locale, sys.isTrue(member.getTccDealer()));
                OrderStatusEnum oriStatus = OrderStatusEnum.getFromCode(orderVO.getStatus());
                ShipStatusEnum oriShipStatus = ShipStatusEnum.getFromCode(orderVO.getShipStatus());
                PayStatusEnum oriPayStatus = PayStatusEnum.getFromCode(orderVO.getPayStatus());

                logger.info("savePoStatus orderId = "+orderId+", newStatus = "+newStatus+", oriStatus = "+oriStatus);
                // 須 buyerCheck=1
                if( sys.isFalse(orderVO.getBuyerCheck()) ){
                    logger.error("savePoStatus ori buyerCheck error!");
                    return Response.notAcceptable(null).build();
                }
                
                // 結案（買家已確認 ＆ 已付款 ＆ 已到貨）
                if( OrderStatusEnum.Approve==oriStatus 
                    && ShipStatusEnum.ARRIVED==oriShipStatus 
                    && (tccDealer || PayStatusEnum.PAID==oriPayStatus)// EC1.5不管收付款狀態
                    && OrderStatusEnum.Close==newStatus ){// 結案
                    
                    orderVO.setStatus(newStatus.getCode());
                    orderFacade.saveVO(orderVO, member, false);
                    logger.info("savePoStatus orderVO = "+orderVO.getId());

                    // EC_ORDER_LOG
                    Long logId = orderLogFacade.save(store.getId(), orderId, newStatus.getDisplayName(locale), false, 
                                        OrderLogEnum.ORDER.getCode(), orderVO.getStatus(), member, false);
                    logger.info("savePoStatus orderLog = "+logId);

                    count++;
                }
            }

            return this.genSuccessRepsoneWithCount(request, count);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }

        return this.genFailRepsone(request);
    }    

    /**
     * 訂單改單價、改量 (for EC1.5 報單價 only)
     * ==============================
     * 1. 賣家訂單確認，首次輸入單價
     * 2. 賣家已確認的訂單(在途)，隨時都可改價、改量 - 20190326 EC1.5
     * /services/orders/change
     * 
     * @param request
     * @param formVO
     * @return 
     */
    @POST
    @Path("/change")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response changeOrder(@Context HttpServletRequest request, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("changeOrder ...");
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            boolean admin = hasAdminRights(request, member);
            boolean tccDealer = isTccDealer(request, member);
            // 賣家專屬 RESTful
            if( !checkPermissions(methodName, member, store, false, true, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            if( formVO==null || formVO.getOrderId()==null || formVO.getQuoteList()==null ){
                logger.error("changeOrder formVO==null");
                return Response.notAcceptable(null).build();
            }
            if( GlobalConstant.QUOTE_TOTAL ){// 報總價
                logger.error("changeOrder QUOTE_TOTAL=="+GlobalConstant.QUOTE_TOTAL);
                return Response.notAcceptable(null).build();
            }

            Long storeId = store.getId();
            Long orderId = formVO.getOrderId();

            OrderVO orderVO = orderFacade.findById(storeId, orderId, false, locale, sys.isTrue(member.getTccDealer()));
            if( orderVO==null ){
                logger.error("changeOrder orderVO==null");
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_NOT_EXIST, errors);
            }
            OrderStatusEnum newStatus = OrderStatusEnum.getFromCode(formVO.getStatus());
            OrderStatusEnum oriStatus = OrderStatusEnum.getFromCode(orderVO.getStatus());
            // 賣家已確認
            logger.info("changeOrder orderId = "+orderId+", newStatus = "+newStatus+", oriStatus = "+oriStatus);
            if( !((oriStatus==OrderStatusEnum.Pending && newStatus==OrderStatusEnum.Approve)// 賣家核准
                || oriStatus==OrderStatusEnum.Approve)// 已核准
            ){
                logger.error("changeOrder status error!");
                return Response.notAcceptable(null).build();
            }

            orderVO.setTotal(formVO.getTotal());
            orderVO.setSubTotal(formVO.getSubTotal());
            orderVO.setShippingTotal(formVO.getShippingTotal());
            boolean doApprove = (oriStatus==OrderStatusEnum.Pending && newStatus==OrderStatusEnum.Approve);
            logger.info("changeOrder doApprove = "+doApprove);
            // 改價、改量、改 BuyerCheck (若用[信用額度]購買須額外處理)
            boolean res = orderFacade.changeOrder(orderVO, formVO.getQuoteList(), doApprove, member, locale, errors);

            if( res ){
                // result
                orderVO = orderFacade.findById(store.getId(), orderVO.getId(), true, locale, sys.isTrue(member.getTccDealer()));
                // push message
                msg.changeOrderBySeller(orderVO, MessageTypeEnum.ORDER.getCode(), orderVO.getStatus(), member, locale);
                logger.info("changeOrder push message");
                return this.genSuccessRepsone(request, orderVO);
            }else{
                return this.genFailRepsone(request, ResStatusEnum.FAIL, errors);
            }
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        
        return this.genFailRepsone(request);
    }
    
    /**
     * 訂單儲存(目前沒用到)
     * /services/orders/save
     * 
     * @param request
     * @param formVO
     * @return 
     */
    @POST
    @Path("/save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response saveOrder(@Context HttpServletRequest request, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("saveOrder ...");
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            boolean admin = hasAdminRights(request, member);
            // 賣家專屬 RESTful
            if( !checkPermissions(methodName, member, store, false, true, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            if( formVO==null ){
                logger.error("saveOrder formVO==null");
                return Response.notAcceptable(null).build();
            }
            formVO.setStoreId(store.getId());

            OrderVO orderVO = (formVO.getId()==null || formVO.getId()==0)?
                    new OrderVO():orderFacade.findById(store.getId(), formVO.getId(), false, locale, sys.isTrue(member.getTccDealer()));
            ExtBeanUtils.copyProperties(orderVO, formVO);
            
            if( orderFacade.checkInput(orderVO, member, locale, errors) ){// 輸入檢查
                orderFacade.saveVO(orderVO, member, false);

                orderVO = orderFacade.findById(store.getId(), orderVO.getId(), true, locale, sys.isTrue(member.getTccDealer()));
                return this.genSuccessRepsone(request, orderVO);
            }else{
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR, errors);
            }
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }

        return this.genFailRepsone(request);
    }

    /**
     * 訂單改單價、改量 (for 報單價 only) - (EC1.5已不用)
     * ==============================
     *    可改單價
     *      訂單報價 for EC2.0
     *      訂單確認 for EC1.5 (已不用)
     *    可改量
     *      出貨 for EC2.0 及 EC1.5 (已不用)
     * ==============================
     * /services/orders/quote
     * 
     * @param request
     * @param formVO
     * @return 
     */
    @POST
    @Path("/quote")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response quoteOrder(@Context HttpServletRequest request, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("quoteOrder ...");
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            boolean admin = hasAdminRights(request, member);
            boolean tccDealer = isTccDealer(request, member);
            // 賣家專屬 RESTful
            if( !checkPermissions(methodName, member, store, false, true, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            if( tccDealer ){// EC1.5已不用
                logger.error("quoteOrder not support for EC1.5 !");
                return Response.notAcceptable(null).build(); 
            }
            if( formVO==null || formVO.getOrderId()==null 
                 || formVO.getStatusType()==null || formVO.getQuoteList()==null ){
                logger.error("quoteOrder formVO==null");
                return Response.notAcceptable(null).build();
            }
            if( GlobalConstant.QUOTE_TOTAL ){// 報總價
                logger.error("quoteOrder QUOTE_TOTAL=="+GlobalConstant.QUOTE_TOTAL);
                return Response.notAcceptable(null).build();
            }

            Long storeId = store.getId();
            Long orderId = formVO.getOrderId();

            OrderVO orderVO = orderFacade.findById(storeId, orderId, false, locale, sys.isTrue(member.getTccDealer()));
            if( orderVO==null ){
                logger.error("quoteOrder orderVO==null");
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_NOT_EXIST, errors);
            }
            OrderStatusEnum oriStatus = OrderStatusEnum.getFromCode(orderVO.getStatus());

            if( oriStatus==null || !oriStatus.isCanChange() ){
                logger.error("quoteOrder ori status error! orderId = "+orderId);
                return Response.notAcceptable(null).build();
            }
            // 輸入
            orderVO.setStatus(formVO.getStatus());
            orderVO.setPayStatus(formVO.getPayStatus());
            orderVO.setShipStatus(formVO.getShipStatus());
            orderVO.setTotal(formVO.getTotal());
            orderVO.setSubTotal(formVO.getSubTotal());
            orderVO.setShippingTotal(formVO.getShippingTotal());
            // 改價、改量、改狀態 (若用[信用額度]購買須額外處理)
            boolean res = orderFacade.quoteOrder(orderVO, formVO.getQuoteList(), formVO.getStatusType(), member, locale, errors);
            
            if( res ){
                // result
                orderVO = orderFacade.findById(store.getId(), orderVO.getId(), true, locale, sys.isTrue(member.getTccDealer()));
                // push message
                if( RfqStatusEnum.getFromCode(formVO.getStatus())==RfqStatusEnum.Quotation ){// EC2.0 RFQ報價階段
                    msg.quotePrice(orderVO, member, locale);
                }else if( ShipStatusEnum.getFromCode(formVO.getShipStatus())==ShipStatusEnum.SHIPPED ){// 出貨 for EC2.0 及 EC1.5
                    msg.changeOrderStatusBySeller(orderVO, MessageTypeEnum.SHIPPING.getCode(), formVO.getShipStatus(), member, locale);
                }
                return this.genSuccessRepsone(request, orderVO);
            }else{
                return this.genFailRepsone(request, ResStatusEnum.FAIL, errors);
            }
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }

        return this.genFailRepsone(request);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="for Order Main (EC2.0 Only)">
    /**
     * 付款確認 (買家確認買家已付款) for EC2.0 Only
     * /services/orders/pay/save
     * @param request
     * @param formVO
     * @return 
     */
    @POST
    @Path("/pay/save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response savePoPay(@Context HttpServletRequest request, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("savePoPay ...");
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            boolean admin = hasAdminRights(request, member);
            boolean tccDealer = isTccDealer(request, member);
            // 賣家專屬 RESTful
            if( !checkPermissions(methodName, member, store, false, true, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            if( tccDealer ){// for EC2.0 Only 
                logger.error("savePoPay error only for EC2.0 tccDealer="+tccDealer);
                return Response.notAcceptable(null).build();
            }
            // 輸入檢查
            if( formVO==null || formVO.getId()==null || formVO.getStatus()==null ){
                logger.error("savePoPay formVO==null");
                return Response.notAcceptable(null).build();
            }
            if( store==null || store.getId()==null ){
                logger.error("savePoPay store==null || store.getId()==null");
                return Response.status(Response.Status.NOT_ACCEPTABLE).build();
            }
            PayStatusEnum newStatus = PayStatusEnum.getFromCode(formVO.getStatus());
            
            Long orderId = formVO.getId();
            OrderVO orderVO = orderFacade.findById(store.getId(), orderId, false, locale, sys.isTrue(member.getTccDealer()));
            PayStatusEnum oriStatus = PayStatusEnum.getFromCode(orderVO.getPayStatus());
            
            logger.info("savePoPay orderId = "+orderId+", newStatus = "+newStatus+", oriStatus = "+oriStatus);
            if( PayStatusEnum.PAID==newStatus || PayStatusEnum.NOTIFY_PAID==newStatus ){// 輸入檢查
                orderVO.setPayStatus(newStatus.getCode());
                //orderVO.setPayTime(new Date());
                orderFacade.saveVO(orderVO, member, false);
                
                // EC_ORDER_LOG
                Long logId = orderLogFacade.save(store.getId(), orderId, newStatus.getDisplayName(locale), false, 
                                    OrderLogEnum.PAYMENT.getCode(), newStatus.getCode(), member, false);
                logger.info("savePoPay orderLog = "+logId);
                
                orderVO = orderFacade.findById(store.getId(), orderId, true, locale, sys.isTrue(member.getTccDealer()));

                // push message
                msg.changeOrderStatusBySeller(orderVO, MessageTypeEnum.PAYMENT.getCode(), newStatus.getCode(), member, locale);
                
                return this.genSuccessRepsone(request, orderVO);
            }else{
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR, errors);
            }
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }

        return this.genFailRepsone(request);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="for Order & RFQ ">
    /**
     * 查詢 - 先抓總筆數
     * @param request
     * @param formVO
     * @return 
     */
    public Response countOrdersAll(@Context HttpServletRequest request, SubmitVO formVO){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("countOrdersAll ...");
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            boolean admin = hasAdminRights(request, member);
            // 賣家專屬 RESTful
            if( !checkPermissions(methodName, member, store, false, true, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }

            OrderCriteriaVO criteriaVO = new OrderCriteriaVO();
            ExtBeanUtils.copyProperties(criteriaVO, formVO);
            criteriaVO.setStoreId(store.getId());
            // 訂單 與 詢價單 欄位不同
            /*if( formVO.getRfq() ){
                criteriaVO.setRfqStatus(formVO.getStatus());
            }else{
                criteriaVO.setOrderStatus(formVO.getStatus());
            }*/
            int totalRows = orderFacade.countByCriteria(criteriaVO, sys.isTrue(member.getTccDealer()));
            logger.debug("countOrdersAll totalRows = "+totalRows);

            return this.genSuccessRepsoneWithCount(request, totalRows);
        }catch(Exception e){
            logger.error("countOrdersAll Exception:\n", e);
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }

    /**
     * @param request
     * @param formVO
     * @param offset
     * @param rows
     * @param sortField
     * @param sortOrder
     * @return 
     */
    public Response findOrdersAll(@Context HttpServletRequest request, SubmitVO formVO,
            @QueryParam("offset")Integer offset, @QueryParam("rows")Integer rows,
            @QueryParam("sortField")String sortField, @QueryParam("sortOrder")String sortOrder
    ){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.info("findOrdersAll offset = "+offset+", rows = "+rows+", sortField = "+sortField+", sortOrder = "+sortOrder);
        offset = (offset==null)?0:offset;
        rows = (rows==null)?1:((rows>GlobalConstant.RS_RESULT_MAX_ROWS)?GlobalConstant.RS_RESULT_MAX_ROWS:rows);

        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            boolean admin = hasAdminRights(request, member);
            // 賣家專屬 RESTful
            if( !checkPermissions(methodName, member, store, false, true, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }

            OrderCriteriaVO criteriaVO = new OrderCriteriaVO();
            ExtBeanUtils.copyProperties(criteriaVO, formVO);
            criteriaVO.setStoreId(store.getId());

            criteriaVO.setFirstResult(offset);
            criteriaVO.setMaxResults(rows);
            // 訂單 與 詢價單 欄位不同
            if( formVO.getRfq() ){
                //criteriaVO.setRfqStatus(formVO.getStatus());
                criteriaVO.setOrderBy(sortFieldMapRFQ.get(sortField), sortOrderMap.get(sortOrder));
            }else{
                //criteriaVO.setOrderStatus(formVO.getStatus());
                criteriaVO.setOrderBy(sortFieldMap.get(sortField), sortOrderMap.get(sortOrder));
            }
            List<OrderVO> list = orderFacade.findByCriteria(criteriaVO, locale, sys.isTrue(member.getTccDealer()));

            return this.genSuccessRepsoneWithList(request, list);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }

    /**
     * /services/orders/full/{id}
     * @param request
     * @param id
     * @return 
     */
    @GET
    @Path("/full/{id}")
    @Produces(MediaType.TEXT_PLAIN+";charset=utf-8")
    @JWTTokenNeeded
    public Response findOrderFullInfo(@Context HttpServletRequest request, @PathParam("id")Long id){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("findOrderFullInfo ...");
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            boolean admin = hasAdminRights(request, member);
            logger.info("findOrderFullInfo id = "+id);
            // 賣家專屬 RESTful
            if( !checkPermissions(methodName, member, store, false, true, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            
            OrderVO orderVO = orderFacade.findById(store.getId(), id, true, locale, sys.isTrue(member.getTccDealer()));
            
            return this.genSuccessRepsone(request, orderVO);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="for Order Rating ">
    /**
     * /services/orders/rate
     * @param request
     * @param formVO
     * @return 
     */
    @POST
    @Path("/rate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response saveOrderRate(@Context HttpServletRequest request, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("saveOrderRate ...");
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            boolean admin = hasAdminRights(request, member);
            // 賣家專屬 RESTful
            if( !checkPermissions(methodName, member, store, false, true, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }

            if( formVO==null || formVO.getOrderId()==null ){
                logger.error("saveOrderRate formVO==null");
                return Response.notAcceptable(null).build();
            }
            
            EcOrderRate entity = orderRateFacade.findByOrderId(store.getId(), formVO.getOrderId());
            if( entity==null ){
                entity = new EcOrderRate();
                entity.setStoreId(store.getId());
                entity.setOrderId(formVO.getOrderId());
            }
            entity.setSellerRate(formVO.getSellerRate());
            entity.setSellerMessage(formVO.getSellerMessage());
            
            if( orderRateFacade.checkInput(entity, member, locale, errors) ){
                orderRateFacade.save(entity, member, false);
                logger.debug("saveOrderRate entity.getId() = "+entity.getId());

                return this.genSuccessRepsone(request);
            }else{
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR, errors);
            }
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }

        return this.genFailRepsone(request);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="for Order Process Records">
    /**
     * /services/orders/{orderId}/processs
     * @param request
     * @param orderId
     * @return 
     */
    @GET
    @Path("/{orderId}/processs")
    @Produces(MediaType.TEXT_PLAIN+";charset=utf-8")
    @JWTTokenNeeded
    public Response findOrderProcessRecords(@Context HttpServletRequest request, @PathParam("orderId")Long orderId){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("findOrderProcessRecords ... orderId = "+orderId);
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            boolean admin = hasAdminRights(request, member);
            // 賣家專屬 RESTful
            if( !checkPermissions(methodName, member, store, false, true, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            List<OrderProcessVO> list = orderProcessFacade.findByOrderId(store.getId(), orderId);

            return this.genSuccessRepsoneWithList(request, list);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    
    /**
     * 儲存
     * /services/orders/{orderId}/process/save
     * @param request
     * @param orderId
     * @param formVO
     * @return 
     */
    @POST
    @Path("/{orderId}/process/save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response saveOrderProcess(@Context HttpServletRequest request, @PathParam("orderId")Long orderId, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("saveOrderProcess ...");
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            boolean admin = hasAdminRights(request, member);
            // 賣家專屬 RESTful
            if( !checkPermissions(methodName, member, store, false, true, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }

            if( formVO==null ){
                logger.error("saveOrderProcess formVO==null");
                return Response.notAcceptable(null).build();
            }
            
            boolean isNew = (formVO.getId()==null || formVO.getId()==0);
            EcOrderProcess entity = null;
            if( isNew ){
                entity = new EcOrderProcess();
                ExtBeanUtils.copyProperties(entity, formVO);
            }else{
                entity = orderProcessFacade.find(formVO.getId());
                if( entity!=null 
                    && store.getId().equals(entity.getStoreId()) 
                    && orderId.equals(entity.getOrderId()) ){
                    ExtBeanUtils.copyProperties(entity, formVO);
                }else{
                    logger.error("saveOrderProcess invalid id = "+formVO.getId());
                    return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_NOT_EXIST, null);
                }
            }
            
            entity.setStoreId(store.getId());
            entity.setOrderId(orderId);
            //entity.setDisabled(formVO.getDisabled()==null? false:formVO.getDisabled());
            if( orderProcessFacade.checkInput(entity, member, locale, errors) ){
                orderProcessFacade.save(entity, member, false);
                logger.debug("saveOrderProcess entity.getId() = "+entity.getId());

                return findOrderProcessRecords(request, orderId);
            }else{
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR, errors);
            }
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }

        return this.genFailRepsone(request);
    }

    /**
     * 刪除
     * /services/orders/{orderId}/process/remove
     * @param request
     * @param orderId
     * @param formVO
     * @return 
     */
    @POST
    @Path("/{orderId}/process/remove")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response removeOrderProcess(@Context HttpServletRequest request, @PathParam("orderId")Long orderId, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("removeOrderProcess ...");
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            boolean admin = hasAdminRights(request, member);
            // 賣家專屬 RESTful
            if( !checkPermissions(methodName, member, store, false, true, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }

            if( formVO==null || formVO.getId()==null || orderId==null ){
                logger.error("removeOrderProcess formVO==null");
                return Response.notAcceptable(null).build();
            }
            
            EcOrderProcess entity = orderProcessFacade.find(formVO.getId());
            if( entity!=null 
                && store.getId().equals(entity.getStoreId()) 
                && orderId.equals(entity.getOrderId()) ){
                
                entity.setDisabled(true);
                orderProcessFacade.save(entity, member, false);
                logger.debug("removeOrderProcess entity.getId() = "+entity.getId());

                return findOrderProcessRecords(request, orderId);
            }else{
                logger.error("removeOrderProcess invalid id = "+formVO.getId()+", orderId="+orderId+", storeId="+store.getId());
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_NOT_EXIST, null);
            }
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }

        return this.genFailRepsone(request);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="for Order Message">
    /**
     * /services/orders/{orderId}/messages
     * @param request
     * @param orderId
     * @return 
     */
    @GET
    @Path("/{orderId}/messages")
    @Produces(MediaType.TEXT_PLAIN+";charset=utf-8")
    @JWTTokenNeeded
    public Response findOrderMessageRecords(@Context HttpServletRequest request, @PathParam("orderId")Long orderId){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("findOrderMessageRecords ... orderId = "+orderId);
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            boolean admin = hasAdminRights(request, member);
            // 賣家專屬 RESTful
            if( !checkPermissions(methodName, member, store, false, true, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            List<OrderMessageVO> list = orderMessageFacade.findByOrderId(store.getId(), orderId);
            return this.genSuccessRepsoneWithList(request, list);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    
    /**
     * 儲存
     * /services/orders/{orderId}/message/save
     * @param request
     * @param orderId
     * @param formVO
     * @return 
     */
    @POST
    @Path("/{orderId}/message/save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response saveOrderMessage(@Context HttpServletRequest request, @PathParam("orderId")Long orderId, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("saveOrderMessage ...");
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            boolean admin = hasAdminRights(request, member);
            // 賣家專屬 RESTful
            if( !checkPermissions(methodName, member, store, false, true, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }

            if( formVO==null ){
                logger.error("saveOrderMessage formVO==null");
                return Response.notAcceptable(null).build();
            }
            
            boolean isNew = (formVO.getId()==null || formVO.getId()==0);
            EcOrderMessage entity = null;
            if( isNew ){
                entity = new EcOrderMessage();
                entity.setMessage(formVO.getMessage());
                //ExtBeanUtils.copyProperties(entity, formVO);
            }else{
                entity = orderMessageFacade.find(formVO.getId());
                if( entity!=null 
                    && store.getId().equals(entity.getStoreId()) 
                    && orderId.equals(entity.getOrderId()) ){
                    entity.setMessage(formVO.getMessage());
                    //ExtBeanUtils.copyProperties(entity, formVO);
                }else{
                    logger.error("saveOrderMessage invalid id = "+formVO.getId()+", orderId="+orderId+", storeId="+store.getId());
                    return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_NOT_EXIST, null);
                }
            }
            
            entity.setBuyer(false);// 賣家
            entity.setStoreId(store.getId());
            entity.setOrderId(orderId);
            //entity.setDisabled(formVO.getDisabled()==null? false:formVO.getDisabled());
            if( orderMessageFacade.checkInput(entity, member, locale, errors) ){
                orderMessageFacade.save(entity, member, false);
                logger.debug("saveOrderMessage entity.getId() = "+entity.getId());
                
                // push message
                OrderVO orderVO = orderFacade.findById(store.getId(), orderId, true, locale, sys.isTrue(member.getTccDealer()));
                msg.replyOrderMsg(orderVO, entity.getMessage(), member, locale);

                return findOrderMessageRecords(request, orderId);
            }else{
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR, errors);
            }
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }

        return this.genFailRepsone(request);
    }

    /**
     * 刪除 (暫時無用，洽談記錄為即時，不可刪除)
     * /services/orders/{orderId}/message/remove
     * @param request
     * @param orderId
     * @param formVO
     * @return 
     */
    @POST
    @Path("/{orderId}/message/remove")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response removeOrderMessage(@Context HttpServletRequest request, @PathParam("orderId")Long orderId, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("removeOrderMessage ...");
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            boolean admin = hasAdminRights(request, member);
            // 賣家專屬 RESTful
            if( !checkPermissions(methodName, member, store, false, true, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }

            if( formVO==null || formVO.getId()==null || orderId==null ){
                logger.error("removeOrderMessage formVO==null");
                return Response.notAcceptable(null).build();
            }
            
            EcOrderMessage entity = orderMessageFacade.find(formVO.getId());
            if( entity!=null 
                    && store.getId().equals(entity.getStoreId()) 
                    && orderId.equals(entity.getOrderId()) ){
                entity.setDisabled(true);
                orderMessageFacade.save(entity, member, false);
                logger.debug("removeOrderMessage entity.getId() = "+entity.getId());

                return findOrderMessageRecords(request, orderId);
            }else{
                logger.error("removeOrderMessage invalid id = "+formVO.getId()+", orderId="+orderId+", storeId="+store.getId());
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_NOT_EXIST, null);
            }
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }

        return this.genFailRepsone(request);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="for No Paid">
    @POST
    @Path("/sum")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response sumNoPaidOrders(@Context HttpServletRequest request, SubmitVO formVO){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("sumNoPaidOrders ...");
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            boolean admin = hasAdminRights(request, member);
            // 賣家專屬 RESTful
            if( !checkPermissions(methodName, member, store, false, true, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }

            if( formVO==null ){
                formVO = new SubmitVO();
            }
            
            OrderCriteriaVO criteriaVO = new OrderCriteriaVO();
            ExtBeanUtils.copyProperties(criteriaVO, formVO);
            criteriaVO.setStoreId(store.getId());
            
            Double totalPrice = orderFacade.sumByCriteria(criteriaVO, "S.TOTAL");
            logger.debug("sumNoPaidOrders totalPrice = "+totalPrice);

            return this.genSuccessRepsoneWithTotal(request, totalPrice);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="for Order Export">
    @POST
    @Path("/exp")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response expOrders(@Context HttpServletRequest request, SubmitVO formVO){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.info("expOrders ...");
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            boolean admin = hasAdminRights(request, member);
            boolean tccDealer = isTccDealer(request, member);
            // 賣家、管理員共用 RESTful
            if( !checkPermissions(methodName, member, store, admin, false, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            // 訂單
            formVO.setRfq(Boolean.FALSE);
            formVO.setStatusList(OrderStatusEnum.getCodes());
            
            Long storeId = admin?formVO.getStoreId():store.getId();
            
            OrderCriteriaVO criteriaVO = new OrderCriteriaVO();
            ExtBeanUtils.copyProperties(criteriaVO, formVO);
            criteriaVO.setStoreId(storeId);

            List<OrderVO> list = orderFacade.findByCriteria(criteriaVO, locale, sys.isTrue(member.getTccDealer()));

            String outputFileName = member.getLoginAccount() + "-" + DateUtils.formatDateString(new Date(), GlobalConstant.FORMAT_DATETIME_STR)
                    + "." + GlobalConstant.EXCEL_FILEEXT;
            String outputFileFullName = GlobalConstant.DIR_EXPORT + "/" + outputFileName;

            List<String> headers = formVO.getHeaders();
            Workbook wb = tccDealer
                    ?AnnotationExportUtils.gereateExcelReport(OrderVO15.class, list, null, headers)
                    :AnnotationExportUtils.gereateExcelReport(OrderVO.class, list, null, headers);
            // format excel
            Map<Integer, Integer> colsWidth = new HashMap<Integer, Integer>();
            colsWidth.put(0, 4);
            // count from 0, OrderVO exportIndex 報價總金額, 原商品總價, 運費
            int[] numCols = tccDealer?new int[]{6, 7, 8}:new int[]{7, 8, 9};
            Map<String, String> numericPatterns = new HashMap<String, String>();
            for(int col : numCols){
                numericPatterns.put(Integer.toString(col), NumericPatternEnum.Digits3.getCode());
            }
            
            ExportUtils.formatExcel(wb, 0, colsWidth, null, null, null, numCols, numericPatterns, false, 1);

            // 儲存
            ExportUtils.saveWorkbook(wb, outputFileFullName);
            
            FileVO fileVO = new FileVO();
            fileVO.setFilename(outputFileName);
            return this.genSuccessRepsone(request, fileVO);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    /*
    @GET
    @Path("/exp/get")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/vnd.ms-excel")
    public Response getExcelFile(@Context HttpServletRequest request, @QueryParam("filename")String filename){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.info("getExcelFile ...");
        File file = new File(GlobalConstant.DIR_EXPORT + "/" + filename);
        //String expfilename = "export-" + DateUtils.formatDateString(new Date(), GlobalConstant.FORMAT_DATETIME_STR)+".xlsx";
        ResponseBuilder response = Response.ok((Object) file);
        response.header("Content-Disposition", "attachment; filename="+filename);
        return response.build();
    }
    */
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="for Car No">
    /**
     * 儲存車號
     * @param request
     * @param formVO
     * @return 
     */
    @POST
    @Path("/car/save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response savePoCarNo(@Context HttpServletRequest request, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("savePoCarNo ...");
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            boolean admin = hasAdminRights(request, member);
            boolean tccDealer = isTccDealer(request, member);
            // 賣家專屬 RESTful
            if( !checkPermissions(methodName, member, store, false, true, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            if( store==null || store.getId()==null ){
                logger.error("savePoCarNo error store==null || store.getId()==null");
                return Response.status(Response.Status.NOT_ACCEPTABLE).build();
            }
            if( formVO.getOrderId()==null || formVO.getCarNoList()==null ){
                logger.error("savePoCarNo error formVO.getOrderId()==null || formVO.getCarNoList()==null");
                return Response.status(Response.Status.NOT_ACCEPTABLE).build();
            }
            Long storeId = store.getId();
            Long orderId = formVO.getOrderId();
            List<String> carNoList = formVO.getCarNoList();
            
            OrderVO orderVO = orderFacade.findById(storeId, orderId, false, locale, sys.isTrue(member.getTccDealer()));
            if( orderVO==null ){
                logger.error("savePoCarNo error orderVO==null");
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_NOT_EXIST, errors);
            }
            if( ShipStatusEnum.ARRIVED==ShipStatusEnum.getFromCode(orderVO.getShipStatus()) ){
                logger.error("savePoCarNo error orderVO.getShipStatus() = "+orderVO.getShipStatus());
                // [已到貨]不可變更車號!
                return this.genFailMsgRepsone(request, ResStatusEnum.FAIL, this.getResourceMsg(locale, "msg.0001"));
            }
            
            // 原車號
            List<OrderCarInfoVO> oriList = orderCarInfoFacade.findByOrderId(storeId, orderId);
            List<String> carNoListOri = new ArrayList<String>();                 
            for(OrderCarInfoVO vo : oriList){
                if( vo.getCarNo()!=null ){
                    carNoListOri.add(vo.getCarNo().toUpperCase().trim());
                }
            }
            
            // save EC_ORDER_CAR_INFO
            // 新增
            for(String carNo : carNoList){
                if( !carNoListOri.contains(carNo.toUpperCase().trim()) ){
                    OrderCarInfoVO newVO = new OrderCarInfoVO();
                    newVO.setCarNo(carNo.toUpperCase().trim());
                    newVO.setStoreId(storeId);
                    newVO.setOrderId(formVO.getOrderId());
                    newVO.setProductId(formVO.getPrdId());
                    orderCarInfoFacade.saveVO(newVO, member, false);
                    logger.info("tranToEC10 orderCarInfoFacade.saveVO newVO = "+newVO);
                }
            }            
            // 刪除沒用車號
            orderCarInfoFacade.deleteByCarNo(storeId, orderId, carNoList, member, false);
            logger.info("tranToEC10 deleteByCarNo ...");
            
            // 回傳結果
            List<OrderCarInfoVO> list = orderCarInfoFacade.findByOrderId(storeId, orderId);
            
            return this.genSuccessRepsoneWithList(request, list);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }

        return this.genFailRepsone(request);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="for Transfer To EC1.0 Order ">
    /**
     * 轉 EC1.0 訂單
     * @param request
     * @param formVO
     * @return 
     */
    @POST
    @Path("/ec10/send")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response tranToEC10(@Context HttpServletRequest request, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("tranToEC10 ...");
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            boolean admin = hasAdminRights(request, member);
            boolean tccDealer = isTccDealer(request, member);
            // 賣家專屬 RESTful
            if( !tccDealer || !checkPermissions(methodName, member, store, false, true, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            if( store==null || store.getId()==null ){
                logger.error("tranToEC10 store==null || store.getId()==null");
                return Response.status(Response.Status.NOT_ACCEPTABLE).build();
            }
            Long storeId = store.getId();
            
            // 輸入檢查
            if( formVO==null 
             || formVO.getProductCode()==null
             || formVO.getProvince()==null || formVO.getCity()==null || formVO.getDistrict()==null || formVO.getTown()==null
             || formVO.getTranMode()==null
             || formVO.getShipMethod()==null
             || formVO.getDeliveryDate()==null
             || formVO.getTranType()==null
             || formVO.getTranType()==null
             || (!sys.isValidId(formVO.getOrderId()) && sys.isEmpty(formVO.getOrderList())) 
             || !sys.isValidId(formVO.getCustomerId())
             || !sys.isValidId(formVO.getPlantId())
             || !sys.isValidId(formVO.getSalesId())
             || sys.isEmpty(formVO.getCarList())
            ){
                logger.error("tranToEC10 formVO==null");
                return Response.notAcceptable(null).build();
            }
            
            // O:一對一; S:拆單; C:併單
            TranModeEC10Enum tranMode = TranModeEC10Enum.getFromCode(formVO.getTranMode());
            TranTypeEC10Enum tranType = TranTypeEC10Enum.getFromCode(formVO.getTranType());
            ShipMethodEC10Enum shipMethod = ShipMethodEC10Enum.getFromCode(formVO.getShipMethod());
            if( tranMode==null || tranType==null || shipMethod==null ){
                logger.error("tranToEC10 tranMode==null || tranType==null || shipMethod==null");
                return Response.notAcceptable(null).build();
            }
            
            // 一對一檢查
            if( tranMode==TranModeEC10Enum.ONE && !sys.isValidId(formVO.getOrderId()) ){
                logger.error("tranToEC10 ONE orderId error!");
                return Response.notAcceptable(null).build();
            }
            
            List<OrderCarInfoVO> carInfoList = formVO.getCarList();
            // 拆單檢查
            if( tranMode==TranModeEC10Enum.SPLIT && !sys.isValidId(formVO.getOrderId()) ){
                logger.error("tranToEC10 SPLIT carInfoList.size error!");
                return Response.notAcceptable(null).build();
            }
            List<String> carNoList = new ArrayList<String>();// for 重複檢查 與 後續刪除
            for(OrderCarInfoVO vo : carInfoList){
                if( vo.getQuantity()==null || BigDecimal.ZERO.compareTo(vo.getQuantity())>=0 ){
                    logger.error("tranToEC10 error vo.getQuantity()==null ");
                    return Response.status(Response.Status.NOT_ACCEPTABLE).build();
                }
                if( sys.isBlank(vo.getCarNo()) ){
                    logger.error("tranToEC10 error sys.isBlank(vo.getCarNo())==null ");
                    return Response.status(Response.Status.NOT_ACCEPTABLE).build();
                }
                String carNo = vo.getCarNo().trim().toUpperCase();
                if( carNoList.contains(carNo) ){
                    logger.error("tranToEC10 error carNoList.contains(carNo) ");
                    return Response.status(Response.Status.NOT_ACCEPTABLE).build();
                }
                carNoList.add(carNo);
            }
            
            List<Long> orderList = formVO.getOrderList();
            // 併單檢查
            if( tranMode==TranModeEC10Enum.COMBINE && sys.isEmpty(orderList) ){
                logger.error("tranToEC10 COMBINE orderList.size error!");
                return Response.notAcceptable(null).build();
            }
            // 併單只能一個車號 (不支援併單又拆單)
            if( tranMode==TranModeEC10Enum.COMBINE && carInfoList.size()>1 ){
                logger.error("tranToEC10 COMBINE carInfoList.size()>1 error!");
                return Response.notAcceptable(null).build();
            }
            
            // 出貨廠
            PlantE10VO plant = tccOrderFacade.findPlantById(formVO.getPlantId());
            if( plant==null ){
                logger.error("tranToEC10 error plant==null");
                return Response.status(Response.Status.NOT_ACCEPTABLE).build();
            }
            // 送達地點
            DeliveryPlacesEC10VO deliveryPlaces = tccOrderFacade.findDeliveryPlacesByTown(formVO.getProvince(), formVO.getCity(), formVO.getDistrict(), formVO.getTown());
            if( deliveryPlaces==null ){
                logger.error("tranToEC10 error deliveryPlaces==null");
                return Response.status(Response.Status.NOT_ACCEPTABLE).build();
            }
            // 銷售區域
            SalesAreaE10VO salesArea = tccOrderFacade.findSalesAreaById(deliveryPlaces.getSalesareaId());
            if( salesArea==null ){
                logger.error("tranToEC10 error salesArea==null");
                return Response.status(Response.Status.NOT_ACCEPTABLE).build();
            }
            // 客戶帳號
            CustomerE10VO customer = tccOrderFacade.findCustomerById(formVO.getCustomerId());
            if( customer==null ){
                logger.error("tranToEC10 error customer==null");
                return Response.status(Response.Status.NOT_ACCEPTABLE).build();
            }
            // 業務
            SalesE10VO sales = tccOrderFacade.findSalesById(formVO.getSalesId());
            if( sales==null ){
                logger.error("tranToEC10 error sales==null");
                return Response.status(Response.Status.NOT_ACCEPTABLE).build();
            }
            // 商品
            ProductE10VO product = null;
            String productCode = formVO.getProductCode();
            // 合約
            ContractE10VO contract = null;
            ContractProductVO contractProduct = null;
            if( TranTypeEC10Enum.CONTRACT==tranType ){
                contract = tccOrderFacade.findContractById(formVO.getContractId());
                if( contract==null ){
                    logger.error("tranToEC10 error contract==null");
                    return Response.status(Response.Status.NOT_ACCEPTABLE).build();
                }
                // 商品
                product = tccOrderFacade.findProductByCode(productCode, formVO.getPlantId(), formVO.getContractId());
                if( product==null ){
                    logger.error("tranToEC10 error contract product==null");
                    return Response.status(Response.Status.NOT_ACCEPTABLE).build();
                }
                // 合約限制條件
                contractProduct = tccOrderFacade.findContractProductInfo(contract.getId(),
                           product.getId(), plant.getId(), salesArea.getId(), shipMethod.getCode());
                if( contractProduct==null ){
                    logger.error("tranToEC10 error contract==null");
                    // 指定的合約，不符合您設定的其他條件(產品、出貨廠、銷售區域、提貨方式)。
                    return this.genSuccessRepsoneWithMsg(request, ResourceBundleUtils.getMessage(locale, "ec10.msg001"));
                }
            }else{
                // 商品
                product = tccOrderFacade.findProductByCode(productCode, formVO.getPlantId(), null);
                if( product==null ){
                    logger.error("tranToEC10 error product==null");
                    return Response.status(Response.Status.NOT_ACCEPTABLE).build();
                }
            }

            // EC1.0資訊
            TccOrderVO tccOrderVO = new TccOrderVO();
            // 合約
            if( TranTypeEC10Enum.CONTRACT==tranType && contract!=null && contractProduct!=null ){
                tccOrderVO.setContractCode(contract.getCode());
                tccOrderVO.setContractId(contract.getId());
                tccOrderVO.setContractName(contract.getName());
                tccOrderVO.setPosnr(contractProduct.getPosnr());
            }
            tccOrderVO.setBonus(0);// EC1.0必填
            tccOrderVO.setCustomerId(customer.getId());
            tccOrderVO.setCustomerCode(customer.getCode());
            tccOrderVO.setCustomerName(customer.getName());
            tccOrderVO.setDeliveryCode(deliveryPlaces.getCode());
            tccOrderVO.setDeliveryId(deliveryPlaces.getId());
            tccOrderVO.setDeliveryName(deliveryPlaces.getName());
            tccOrderVO.setDeliveryDate(formVO.getDeliveryDate().replaceAll("-", ""));
            tccOrderVO.setTranMode(tranMode.getCode());
            tccOrderVO.setTranType(tranType.getCode());
            tccOrderVO.setMemberId(customer.getMemberId());
            //tccOrderVO.setMessage("");
            tccOrderVO.setMethod(shipMethod.getCode());
            tccOrderVO.setPlantCode(plant.getCode());
            tccOrderVO.setPlantId(plant.getId());
            tccOrderVO.setPlantName(plant.getName());
            tccOrderVO.setProductCode(product.getCode());
            tccOrderVO.setProductId(product.getId());
            tccOrderVO.setProductName(product.getName());
            //tccOrderVO.setSiteLoc("");
            tccOrderVO.setSalesCode(sales.getCode());
            tccOrderVO.setSalesId(sales.getId());
            tccOrderVO.setSalesName(sales.getName());
            tccOrderVO.setSalesareaCode(salesArea.getCode());
            tccOrderVO.setSalesareaId(salesArea.getId());
            tccOrderVO.setSalesareaName(salesArea.getName());
            tccOrderVO.setStatus("OPEN");// 狀態 (OPEN:未出貨, CANCEL: 取消, CLOSE:已出貨)

            // EC1.5資訊 
            if( tranMode!=TranModeEC10Enum.COMBINE ){
                orderList = new ArrayList<Long>();
                orderList.add(formVO.getOrderId());// for 共用下列程式
            }
            
            // 逐筆處理
            // 1對1 : EC_ORDER 1 筆 / EC_TCC_ORDER 1 筆 / TCCSTORE_USER.EC_ORDER 1 筆
            // 拆單 : EC_ORDER 1 筆 / EC_TCC_ORDER 多 筆 / TCCSTORE_USER.EC_ORDER 多 筆
            // 併單 : EC_ORDER 多 筆 / EC_TCC_ORDER 多 筆 / TCCSTORE_USER.EC_ORDER 1 筆
            List<Ec10ResVO> resList = new ArrayList<Ec10ResVO>();
            Ec10ResVO resVO = null;
            for(Long orderId : orderList){
                logger.info("tranToEC10 process orderId = "+orderId);
                // 訂單
                OrderVO orderVO = orderFacade.findById(storeId, orderId, true, locale, sys.isTrue(member.getTccDealer()));
                if( orderVO==null || !productCode.equals(orderVO.getProductCode()) ){
                    logger.error("tranToEC10 error orderVO==null || !productCode.equals(orderVO.getProductCode())");
                    return Response.status(Response.Status.NOT_ACCEPTABLE).build();
                }
                if( !OrderStatusEnum.Approve.getCode().equals(orderVO.getStatus()) // 非 已核准
                 || !ShipStatusEnum.NOT_SHIPPED.getCode().equals(orderVO.getShipStatus()) // 非 未出貨
                 || sys.isFalse(orderVO.getBuyerCheck()) ){ // 買方未確認變更
                    logger.error("tranToEC10 status error orderId = "+orderId);
                    return Response.status(Response.Status.NOT_ACCEPTABLE).build();
                }

                tccOrderVO.setSrcBuyerId(orderVO.getMemberId());// 買家
                tccOrderVO.setSrcMemberId(member.getId());// 賣家
                tccOrderVO.setSrcOrderId(orderId);// EC1.5 orderId
                tccOrderVO.setSrcProductId(orderVO.getProductId());// EC1.5 productId
                tccOrderVO.setSrcStoreId(storeId);

                // 儲存
                for(OrderCarInfoVO vo : carInfoList){
                    logger.info("tranToEC10 process car = "+vo.getCarNo()+", quantity = "+vo.getQuantity());
                    /* 不回寫 EC_ORDER_CAR_INFO
                    // save EC_ORDER_CAR_INFO
                    String carNo = vo.getCarNo().trim().toUpperCase();
                    vo.setStoreId(storeId);
                    vo.setOrderId(orderId);
                    vo.setProductId(orderVO.getProductId());
                    vo.setCarNo(carNo);
                    orderCarInfoFacade.saveVO(vo, member, false);
                    logger.info("tranToEC10 orderCarInfoFacade.saveVO vo = "+vo);
                    */
                    // 車號、數量
                    tccOrderVO.setQuantity(vo.getQuantity());
                    tccOrderVO.setVehicle(vo.getCarNo().trim().toUpperCase());
                    // save EC_TCC_ORDER
                    tccOrderFacade.saveVO(tccOrderVO, member, false);
                    logger.info("tranToEC10 tccOrderFacade.saveVO create tccOrderVO = "+tccOrderVO);
                    
                    // transfer to EC1.0 // 單筆轉一次；拆單轉多次；併單只需轉一次
                    if( sys.isEmpty(resList) || tranMode!=TranModeEC10Enum.COMBINE ){
                        resVO = tranOrderToEc10(request, tccOrderVO, member);
                        resList.add(resVO);
                    }
                    // 補入 EC1.0 的 orderId
                    if( resVO!=null && sys.isValidId(resVO.getOrderId()) ){
                        Long tccOrderId = resVO.getOrderId();
                        tccOrderVO.setTccOrderId(tccOrderId);// EC1.0 訂單ID  
                        // EC_TCC_ORDER
                        tccOrderFacade.saveVO(tccOrderVO, member, false);
                        logger.info("tranToEC10 tccOrderFacade.saveVO update tccOrderVO = "+tccOrderVO);
                    }else{
                        logger.info("tranToEC10 tranOrderToEc10 fail tccOrderVO = "+tccOrderVO);
                    }

                    tccOrderVO.setId(null);// for 存下一筆(其他欄位可共用)
                }
                /* 不回寫 EC_ORDER_CAR_INFO
                // 刪除沒用車號
                if( tranMode!=TranModeEC10Enum.COMBINE ){
                    logger.info("tranToEC10 deleteByCarNo ...");
                    orderCarInfoFacade.deleteByCarNo(storeId, orderId, carNoList, member, false);
                }
                */
            }

            return this.genSuccessRepsoneWithList(request, resList);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }

        return this.genFailRepsone(request);
    }
    
    /**
     * Call EC1.0 RESTful
     * @param tccOrderVO
     * @return 
     */
    private Ec10ResVO tranOrderToEc10(HttpServletRequest request, TccOrderVO tccOrderVO, EcMember operator) throws IOException{
        logger.info("tranOrderToEc10 tccOrderVO = "+tccOrderVO);
        Ec10ReqVO reqVO = new Ec10ReqVO();
        ExtBeanUtils.copyProperties(reqVO, tccOrderVO);
        String jsonInString = serialize(reqVO, operator);
        logger.info("tranOrderToEc10 jsonInString = \n" + jsonInString);
        
        // call EC1.0
        Ec10ResVO resVO = new Ec10ResVO();
        String url = sys.getTranToEC10Url();
        logger.info("tranOrderToEc10 url = \n" + url);
        String jwt = genJwtForEC10(request);
        if( url==null || jwt==null ){
            logger.error("tranOrderToEc10 url==null || jwt==null !! url = "+url);
            return null;
        }
        
        Map<String, Object> reqHeaders = new HashMap<String, Object>();
        reqHeaders.put(TokenProvider.HEADER_NAME_AUTH, TokenProvider.BEARER + jwt);// JWT 保護
        String resStr = WebUtils.callService("POST", url, jsonInString, reqHeaders, null);
        ObjectMapper objectMapper = new ObjectMapper();
        resVO = objectMapper.readValue(resStr, Ec10ResVO.class);
        resVO.setCarNo(reqVO.getVehicle());// 對應車號 for UI 顯示結果

        return resVO;
    }
    
    /**
     * 產生呼叫 EC1.0 的 JWT 
     * @param request
     * @return 
     */
    private String genJwtForEC10(HttpServletRequest request){
        String jwt = getJWT(request);
        if( jwt!=null ){
            String loginAccount = tokenProvider.getSubject(jwt);
            String roles = (String) tokenProvider.getClaim(jwt, TokenProvider.AUTH_ROLES);
            Long memberId = (Long) tokenProvider.getClaim(jwt, TokenProvider.MEMBER_ID);
            Long storeId = (Long) tokenProvider.getClaim(jwt, TokenProvider.STORE_ID);
            Boolean forAdmin = (Boolean) tokenProvider.getClaim(jwt, TokenProvider.ADMIN_USER);
            Boolean forDealer = (Boolean) tokenProvider.getClaim(jwt, TokenProvider.TCC_DEALER);
            Boolean storeOwner = (Boolean) tokenProvider.getClaim(jwt, TokenProvider.STORE_OWNER);
            Boolean fiUser = (Boolean) tokenProvider.getClaim(jwt, TokenProvider.FI_USER);
            //String fromIP = WebUtils.getClientIP(request);
            String fromIP = WebUtils.getHostAddress();
            Set<String> groups = sys.isBlank(roles)?null:Sets.newHashSet(roles.split(","));
            String sessionKey = (String) tokenProvider.getClaim(jwt, TokenProvider.SESSION_KEY);
            /*
            long tokenValidity = TimeUnit.MINUTES.toMillis(60 * 24 * 3);// 3 days
            String jwtEC10 = tokenProvider.createToken(GlobalConstant.SECURITY_KEY_EC10, tokenValidity,
                   loginAccount, groups, memberId, storeId, 
                   forAdmin, forDealer, storeOwner, fiUser, fromIP, sessionKey);
            logger.debug("genJwtForEC10 3days jwtEC10 = "+jwtEC10);
            */
            long tokenValidity = TimeUnit.MINUTES.toMillis(1);// 1 分鐘
            String jwtEC10 = tokenProvider.createToken(GlobalConstant.SECURITY_KEY_EC10, tokenValidity,
                   loginAccount, groups, memberId, storeId, 
                   forAdmin, forDealer, storeOwner, fiUser, fromIP, sessionKey);
            logger.debug("genJwtForEC10 jwtEC10 = \n"+jwtEC10);
            return jwtEC10;
        }
        return null;
    }

    /**
     * /services/ec10/simulate
     * 
     * 轉 EC1.0 訂單
     * @param request
     * @param formVO
     * @return 
     */
    @POST
    @Path("/ec10/simulate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response simulateEC10(@Context HttpServletRequest request, Ec10ReqVO reqVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("simulateEC10 ...");
        EcMember member = null;
        List<String> errors = new ArrayList<String>();
        Ec10ResVO resVO = new Ec10ResVO();
        resVO.setStatus("FAIL");
        try{
            String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            if( authHeader!=null && authHeader.startsWith(TokenProvider.BEARER) ){
                String jwt = authHeader.substring(TokenProvider.BEARER.length());

                Long memberId = (Long)tokenProvider.getClaim(jwt, TokenProvider.MEMBER_ID);
                logger.debug("simulateEC10 memberId = "+memberId);
                member = memberFacade.find(memberId);

                resVO.setStatus("OK");
                resVO.setOrderId(123L);// TEST
                resVO.setCarNo(reqVO.getVehicle());// for UI 顯示結果
            }else{
                resVO.setMsg("authHeader = null");
            }
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
            resVO.setMsg(e.toString());
        }

        return Response.ok(resVO, MediaType.APPLICATION_JSON).encoding("UTF-8").build();
    }
    //</editor-fold>
}
