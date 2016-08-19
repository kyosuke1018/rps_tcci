/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.service.order;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.tccstore.entity.EcCustomer;
import com.tcci.tccstore.entity.EcDeliveryVkorg;
import com.tcci.tccstore.entity.EcMember;
import com.tcci.tccstore.entity.EcOrder;
import com.tcci.tccstore.entity.datawarehouse.ZorderCn;
import com.tcci.tccstore.enums.NotifyTypeEnum;
import com.tcci.tccstore.enums.OrderStatusEnum;
import com.tcci.tccstore.enums.RewardEventEnum;
import com.tcci.tccstore.enums.RewardTypeEnum;
import com.tcci.tccstore.facade.datawarehouse.ZorderCnFacade;
import com.tcci.tccstore.facade.deliveryplace.EcDeliveryVkorgFacade;
import com.tcci.tccstore.facade.notify.EcNotifyFacade;
import com.tcci.tccstore.facade.reward.EcRewardFacade;
import com.tcci.tccstore.facade.util.OrderFilter;
import com.tcci.tccstore.model.order.Order;
import com.tcci.tccstore.model.salesarea.Salesarea;
import com.tcci.tccstore.sapproxy.SdProxy;
import com.tcci.tccstore.sapproxy.SdProxyFactory;
import com.tcci.tccstore.sapproxy.dto.SapProxyResponseDto;
import com.tcci.tccstore.sapproxy.dto.SapTableDto;
import com.tcci.tccstore.sapproxy.jco.JcoUtils;
import com.tcci.tccstore.service.EntityToModel;
import com.tcci.tccstore.service.ServiceBase;
import com.tcci.tccstore.service.ServiceException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Neo.Fu
 */
@Path("order")
public class OrderService extends ServiceBase {
    private final static Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Resource(mappedName = "jndi/sapclient.config")
    transient private Properties sapConfig;
    //@Resource(mappedName = "jndi/tccstore.config")
    //transient private Properties tccstoreConfig;

    @Inject
    private EcDeliveryVkorgFacade ecDeliveryVkorgFacade;
    @Inject
    private ZorderCnFacade zorderCnFacade;
    @Inject
    private EntityToModel entityToModel;
    @Inject
    private EcNotifyFacade notifyFacade;
    @Inject
    private EcRewardFacade rewardFacade;
    
    @GET
    @Path("list/{status}/{customer}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Order> findByStatus(@PathParam("status") OrderStatusEnum status, @PathParam("customer") String customer, @QueryParam("year_month") String yearMonth) {
        logger.debug("status={}", status);
        logger.debug("yearMonth={}", yearMonth);
        EcCustomer ecCustomer = this.getAuthCustomer(customer);
        List<Order> orderList;
        if (OrderStatusEnum.CLOSE.equals(status)) {
            orderList = findCloseOrder(customer, yearMonth);
        } else {
            orderList = findOpenOrder(ecCustomer);
        }
        logger.debug("orderList.size()={}", orderList.size());
        return orderList;
    }

    @GET
    @Path("list2/{status}/{customer}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Order> findByStatus(@PathParam("status") OrderStatusEnum status, @PathParam("customer") Long customer_id, @QueryParam("year_month") String yearMonth) {
        logger.debug("status={}", status);
        logger.debug("yearMonth={}", yearMonth);
        EcCustomer ecCustomer = this.getAuthCustomer(customer_id);
        List<Order> orderList;
        if (OrderStatusEnum.CLOSE.equals(status)) {
            orderList = findCloseOrder(ecCustomer.getCode(), yearMonth);
        } else {
            orderList = findOpenOrder(ecCustomer);
        }
        logger.debug("orderList.size()={}", orderList.size());
        return orderList;
    }

    @GET
    @Path("cancel/{order_id}")
    @Produces(MediaType.TEXT_PLAIN)
    public String cancalOrder(@Context HttpServletRequest request, @PathParam("order_id") Long orderId) throws ServiceException {
        EcMember loginMember = getAuthMember();
        ResourceBundle rb = ResourceBundle.getBundle("/msgOrder", request.getLocale());
        String result = "";
        logger.debug("orderId={}", orderId);
        EcOrder order = getAuthOrder(orderId);
        OrderStatusEnum status = order.getStatus();
        // 已出貨不允許取消
        if (OrderStatusEnum.CLOSE == status) {
            String error = rb.getString("order.err.orderClosed");
            logger.error(error);
            throw new ServiceException(error);
        } else if (OrderStatusEnum.CANCEL == status) { // 已取消時視為成功
            result = rb.getString("order.msg.cancelSuccess");
        } else if (OrderStatusEnum.OPEN == status || OrderStatusEnum.OPEN_AUTO == status || OrderStatusEnum.FAIL == status) {
            // 審核中, 失敗: 允許取消
            order.setStatus(OrderStatusEnum.CANCEL);
            orderFacade.edit(order);
            orderFacade.addLog(order, OrderStatusEnum.CANCEL.name(), loginMember, null);
            //取消通知
            notifyFacade.createNotify(NotifyTypeEnum.ORDER_CANCEL,
                    MessageFormat.format(rb.getString("order.msg.orderCancelNotify"),
                            new Object[]{String.valueOf(order.getId()), order.getQuantity(), order.getVehicle()}),
                    order, order.getMemberId());
            result = rb.getString("order.msg.cancelSuccess");
        } else if (OrderStatusEnum.APPROVE == status && StringUtils.isNotEmpty(order.getSapOrdernum())) {
            // 已核准且有SAP單號，先檢查是否已出貨
            List<String> vbelnList = new ArrayList();
            vbelnList.add(order.getSapOrdernum());
            List<String> sapVbelnList = findSapShippedSalesDocument(vbelnList);
            if (sapVbelnList.contains(order.getSapOrdernum())) {
                order.setStatus(OrderStatusEnum.CLOSE);
                orderFacade.edit(order);
                orderFacade.addLog(order, OrderStatusEnum.CLOSE.name(), (TcUser) null, null);
                logger.debug("order closed before cancel, id={}", order.getId());
                //出貨通知
                notifyFacade.createNotify(NotifyTypeEnum.ORDER_CLOSE,
                        MessageFormat.format(rb.getString("order.msg.orderCloseNotify"),
                                new Object[]{order.getSapOrdernum(), String.valueOf(order.getId()), order.getQuantity(), order.getVehicle()}),
                        order, order.getMemberId());
                //紅利
                String eventDetail = "订单(" + order.getSapOrdernum() + ")红利";
                rewardFacade.awardPoints(order.getMemberId(), RewardTypeEnum.BONUS, 1, RewardEventEnum.ORDER_CLOSE, eventDetail, null, order);
                logger.warn("add 1 bonus point, member id:{}", order.getMemberId().getId());
                String error = rb.getString("order.err.orderClosed");
                logger.error(error);
                throw new ServiceException(error);
            }
            cancelSapOrder(order);
            order.setStatus(OrderStatusEnum.CANCEL);
            orderFacade.edit(order);
            orderFacade.addLog(order, OrderStatusEnum.CANCEL.name(), loginMember, null);
            //取消通知
            notifyFacade.createNotify(NotifyTypeEnum.ORDER_CANCEL,
                    MessageFormat.format(rb.getString("order.msg.orderCancelNotify"),
                            new Object[]{String.valueOf(order.getId()), order.getQuantity(), order.getVehicle()}),
                    order, order.getMemberId());
            result = rb.getString("order.msg.cancelSuccess");
        } else {
            logger.error("cancalOrder unknown status, order id:{}", order.getId());
            throw new ServiceException("unknown status");
        }
        return result;
    }

//不需要修改
//    @POST
//    @Path("edit")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public String editOrder(@Context HttpServletRequest request, Order order) {
//        ResourceBundle rb = ResourceBundle.getBundle("/msgOrder", request.getLocale());
//        String result = "";
//        logger.debug("order={}", order);
//        List<String> errorList = validate(order);
//        if (order.getId() != null) {
//            EcOrder existsEcOrder = orderFacade.find(order.getId());
//            if (existsEcOrder != null) {
//                if (errorList.isEmpty()) {
//                    EcOrder ecOrder = convertOrderToEcOrder(order, existsEcOrder);
//                    orderFacade.editThenReturn(ecOrder);
//                    result = rb.getString("order.msg.editSuccess");
//                } else {
//                    for (String errorMessage : errorList) {
//                        result += errorMessage + "\n";
//                    }
//                }
//            } else {
//                result = rb.getString("order.err.orderNotExists");
//            }
//        } else {
//            result = rb.getString("order.err.idIsRequired");
//        }
//        return result;
//    }
    @POST
    @Path("create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String createOrder(@Context HttpServletRequest request, Order order) throws ServiceException {
        EcMember loginMember = getAuthMember();
        ResourceBundle rb = ResourceBundle.getBundle("/msgOrder", request.getLocale());
        String result = "";
        // 手機端僅傳 embedded object, 由service展開
        logger.debug("create order:{}", toJson(order));
        if (order.getProduct() != null) {
            order.setProductCode(order.getProduct().getCode());
            order.setProductName(order.getProduct().getName());
        }
        if (order.getContract() != null) {
            order.setContractCode(order.getContract().getCode());
            order.setContractName(order.getContract().getName());
        }
        if (order.getPlant() != null) {
            order.setPlantCode(order.getPlant().getCode());
            order.setPlantName(order.getPlant().getName());
        }
        // 20151022: 以後手機僅傳 delivery, salesarea 會含在 delivery 裡面
        // 20160112: 改由 ec_delivery_vkorg 決定 salesarea
        //if (order.getSalesarea() != null) {
        //    order.setSalesareaCode(order.getSalesarea().getCode());
        //    order.setSalesareaName(order.getSalesarea().getName());
        //}
        if (order.getDelivery() != null) {
            order.setDeliveryCode(order.getDelivery().getCode());
            order.setDeliveryName(order.getDelivery().getName());
            Salesarea salesarea = order.getDelivery().getSalesarea();
            if (salesarea != null) {
                order.setSalesarea(salesarea);
                order.setSalesareaCode(salesarea.getCode());
                order.setSalesareaName(salesarea.getName());
            }
            long delivery_id = order.getDelivery().getId();
            String vkorg = order.getPlantCode().substring(0, 2) + "00";
            EcDeliveryVkorg devk = ecDeliveryVkorgFacade.find(delivery_id, vkorg);
            if (devk != null) {
                salesarea = EntityToModel.buildSalesarea(devk.getEcSalesarea());
                logger.debug("salesarea from EcDeliveryVkorg:{}", toJson(salesarea));
                order.setSalesarea(salesarea);
                order.setSalesareaCode(salesarea.getCode());
                order.setSalesareaName(salesarea.getName());
            }
        }
        if (order.getSales() != null) {
            order.setSalesCode(order.getSales().getCode());
            order.setSalesName(order.getSales().getName());
        }
        // logger.debug("order={}", order);
        List<String> errorList = validate(order, rb);
        if (errorList.isEmpty()) {
            EcOrder ecOrder = entityToModel.convertOrderToEcOrder(order, null);
            ecOrder.setMemberId(getAuthMember());
            ecOrder.setCreatetime(new Date());
            // 自動審核: 非c1且是(貴港或英德的單)
            boolean isC1 = "c1".equals(loginMember.getLoginAccount());
            // boolean autoOrder = ecOrder.getPlantCode().matches("20..|25..|33.."); // 英德,貴港,重慶
            boolean autoOrder = ecOrder.getPlantId().isAutoOrder();
            OrderStatusEnum status = (!isC1 && autoOrder) ? OrderStatusEnum.OPEN_AUTO : OrderStatusEnum.OPEN;
            ecOrder = orderFacade.createThenReturn(ecOrder, status);
            orderFacade.addLog(ecOrder, status.name(), loginMember, null);
            // OPEN_AUTO, OPEN 使用相同通知
            notifyFacade.createNotify(NotifyTypeEnum.ORDER_CREATE,
                    MessageFormat.format(rb.getString("order.msg.orderCreateNotify"),
                            new Object[]{String.valueOf(ecOrder.getId()), ecOrder.getQuantity(), ecOrder.getVehicle()}),
                    ecOrder, ecOrder.getMemberId());
            result = rb.getString("order.msg.createSuccess");
            // mail notify
            // TODO: mail hang, 導致 service 等很久, 改成批次通次(後台)
            // sendmailOrderCreate(ecOrder);
        } else {
            for (String errorMessage : errorList) {
                result += errorMessage + "\n";
                logger.error("createOrder validate error:{}", result);
                throw new ServiceException(result);
            }
        }
        return result;
    }

//    @POST
//    @Path("price")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.TEXT_PLAIN)
//    public BigDecimal getOrderPrice(@Context HttpServletRequest request, Order order) throws ServiceException {
//        BigDecimal price = BigDecimal.ZERO;
//        ResourceBundle rb = ResourceBundle.getBundle("/msgOrder", request.getLocale());
//        try {
//            Properties jcoProp = JcoUtils.getJCoProp(jndiConfig, "tcc_cn"); //取得相關Jco連結參數
//            SdProxy sdProxy = SdProxyFactory.createProxy(jcoProp);//建立連線
//            SapProxyResponseDto result = sdProxy.findOrderPrice(order);
//            SapTableDto sapTableDto = (SapTableDto) result.getResult();
//            if (sapTableDto.getDataMapList().size() > 0) {
//                List<Map<String, Object>> dataMapList = sapTableDto.getDataMapList();
//                for (Map<String, Object> dataMap : dataMapList) {
//                    BigDecimal unitPrice = (BigDecimal) dataMap.get("NETWR");
//                    BigDecimal tax = (BigDecimal) dataMap.get("MWSBP");
//                    price = unitPrice.add(tax);
//                }
//            }
//            sdProxy.dispose();
//        } catch (Exception e) {
//            logger.error("query(), e={}", e);
//            throw new ServiceException("server exception!", e);
//        }
//        logger.debug("price={}", price);
//        return price;
//    }
    // 計算紅利
    @POST
    @Path("calcbonus")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public int calcbonus(Order order) {
        try {
            return order.getQuantity().intValue();
        } catch (Exception ex) {
        }
        return 0;
    }

//    //TODO: validateForFindPrice not implement.
//    private List<String> validateForFindPrice(Order order, ResourceBundle rb) {
//        List<String> errorList = new ArrayList();
//
//        return errorList;
//    }
    private List<String> validate(Order order, ResourceBundle rb) {
        List<String> errorList = new ArrayList();

        //車號
        if (StringUtils.isEmpty(order.getVehicle())) {
            errorList.add(rb.getString("order.err.vehicleIsRequired"));
        }

        //發貨工廠
        if (order.getPlant() == null) {
            errorList.add(rb.getString("order.err.plantIsRequired"));
        }

        //國貿條件
        if (StringUtils.isEmpty(order.getMethod())) {
            errorList.add(rb.getString("order.err.methodIsRequired"));
        }

        //業務員
        if (order.getSales() == null) {
            errorList.add(rb.getString("order.err.salesIsRequired"));
        }
        //數量
        if (order.getQuantity() == null || order.getQuantity().equals(BigDecimal.ZERO)) {
            errorList.add(rb.getString("order.err.quantityIsRequired"));
        }

        //買方
        if (order.getCustomer() == null) {
            errorList.add(rb.getString("order.err.customerIsRequired"));
        }

        //送達地點
        if (order.getDelivery() == null) {
            errorList.add(rb.getString("order.err.deliveryPlanceIsRequired"));
        } else if (order.getDelivery().getSalesarea() == null) {
            errorList.add(rb.getString("order.err.salesareaIsRequired"));
        }

        //銷售地區
        if (order.getSalesarea() == null) {
            errorList.add(rb.getString("order.err.salesareaIsRequired"));
        }
        
        // 有合約的話, posnr必填
        if (order.getContract() != null && order.getPosnr() == null) {
            errorList.add(rb.getString("order.err.contractProductPosnrIsRquired"));
        }

        //物料號碼
        if (order.getProduct() == null) {
            errorList.add(rb.getString("order.err.productIsRequired"));
        }

        // 紅利是否異動
        // 修正: 訂單close後直接用噸數計算紅利
        /*
         if (order.getBonus() != calcbonus(order)) {
         errorList.add(rb.getString("order.err.bonusChanged"));
         }
         */
        String deliverDate = order.getDeliveryDate();
        if (StringUtils.isEmpty(deliverDate)) {
            errorList.add(rb.getString("order.err.deliveryDateIsRequired"));
        } else {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                Calendar cal = Calendar.getInstance();
                String today = sdf.format(cal.getTime());
                cal.add(Calendar.DATE, 1);
                String tomorrow = sdf.format(cal.getTime());
                if (!deliverDate.equals(today) && !deliverDate.equals(tomorrow)) {
                    errorList.add(rb.getString("order.err.deliveryDateNG"));
                }
            } catch (Exception e) {
                errorList.add(rb.getString("order.err.deliveryDateFormatNG"));
            }
        }
        return errorList;
    }

    private List<Order> findCloseOrder(String customerCode, String yearMonth) {
        List<Order> orderList = new ArrayList();
        List<ZorderCn> zorderCnList = zorderCnFacade.findByCustomerYearMonth(customerCode, yearMonth);
        //取得紅利 
        OrderFilter filter = new OrderFilter();
        filter.setCustomerCode(customerCode);
        filter.setYearMonth(yearMonth);
        Map<String, EcOrder> orderMap = new HashMap();
        for (EcOrder order : orderFacade.findByCriteria(filter)) {
            if (StringUtils.isNotEmpty(order.getSapOrdernum())) {
                orderMap.put(order.getSapOrdernum(), order);
            }
        }
        for (ZorderCn zorderCn : zorderCnList) {
            EcOrder ecOrder = orderMap.get(zorderCn.getVbeln());
            Order order = convertZorderCnToOrder(zorderCn);
            if (ecOrder != null) {
                order.setBonus(ecOrder.getBonus());
            }
            orderList.add(order);
        }
        return orderList;
    }

    private List<Order> findOpenOrder(EcCustomer ecCustomer) {
        List<Order> orderList = new ArrayList();
        // List<EcOrder> ecOrderList = orderFacade.findOrderByCustomer(customerCode);
        List<EcOrder> ecOrderList = orderFacade.findNotClosedOrder(ecCustomer);
        for (EcOrder ecOrder : ecOrderList) {
            Order order = entityToModel.convertEcOrderToOrder(ecOrder);
            orderList.add(order);
        }
        /*
        List<String> vbelnList = new ArrayList();
        for (EcOrder ecOrder : ecOrderList) {
            vbelnList.add(ecOrder.getSapOrdernum());
        }
        List<String> sapVbelnList = findSapShippedSalesDocument(vbelnList);
        for (EcOrder ecOrder : ecOrderList) {
            Order order = entityToModel.convertEcOrderToOrder(ecOrder);
            if (sapVbelnList.contains(ecOrder.getSapOrdernum())) {
                order.setStatus(OrderStatusEnum.CLOSE.toString());
            }
            orderList.add(order);
        }
        Collections.sort(orderList, new Comparator<Order>() {
            @Override
            public int compare(Order o1, Order o2) {
                int comp = o2.getDeliveryDate().compareTo(o1.getDeliveryDate());
                if (0 == comp) {
                    comp = o2.getCreatetime().compareTo(o1.getCreatetime());
                }
                return comp;
            }
        });
        */
        return orderList;
    }

    private List<String> findSapShippedSalesDocument(List<String> vbelnList) {
        List<String> sapVbelnList = new ArrayList();
        try {
            Properties jcoProp = JcoUtils.getJCoProp(sapConfig, "tcc_cn"); //取得相關Jco連結參數
            SdProxy sdProxy = SdProxyFactory.createProxy(jcoProp);//建立連線
            SapProxyResponseDto result = sdProxy.findShippedSalesDocument(vbelnList);
            Map<String, SapTableDto> sapTableDtoMap = (Map<String, SapTableDto>) result.getResult();
            if (sapTableDtoMap != null && sapTableDtoMap.size() > 0) {
                SapTableDto sapTableDto = sapTableDtoMap.get("ZTAB_EXP_VBAK");
                logger.debug("sapTableDto.getDataMapList().size()={}", sapTableDto.getDataMapList().size());
                if (sapTableDto.getDataMapList().size() > 0) {
                    List<Map<String, Object>> dataMapList = sapTableDto.getDataMapList();
                    for (Map<String, Object> dataMap : dataMapList) {
                        String sapOrderNum = ((String) dataMap.get("VBELN")).replaceAll("^0+", "");
                        sapVbelnList.add(sapOrderNum);
                    }
                }
            }
            sdProxy.dispose();
        } catch (Exception e) {
            logger.error("query(), e={}", e);
            throw new ServiceException(e.getMessage());
        }
        return sapVbelnList;
    }
    
    private void cancelSapOrder(EcOrder order) {
        try {
            Properties jcoProp = JcoUtils.getJCoProp(sapConfig, "tcc_cn"); //取得相關Jco連結參數
            SdProxy sdProxy = SdProxyFactory.createProxy(jcoProp);//建立連線
            Map<String, Object> params = new HashMap<>();
            params.put("ZRECORD_COUNT", order.getId());
            params.put("VBELV", order.getSapOrdernum());
            SapProxyResponseDto resultDto = sdProxy.cancelOrder(params);
            SapTableDto sapTableDto = (SapTableDto) resultDto.getResult();
            if (sapTableDto != null && sapTableDto.getDataMapList().size() > 0) {
                List<Map<String, Object>> dataMapList = sapTableDto.getDataMapList();
                for (Map<String, Object> dataMap : dataMapList) {
                    logger.debug("vbeln={}", dataMap.get("VBELN"));
                    logger.debug("message={}", dataMap.get("MESSAGE"));
                }
            }
            sdProxy.dispose();
        } catch (Exception e) {
            logger.error("e={}", e);
            throw new ServiceException(e.getMessage());
        }
    }

    private Order convertZorderCnToOrder(ZorderCn zorderCn) {
        Order order = new Order();
        order.setProductCode(zorderCn.getMatnr());
        order.setProductName(zorderCn.getArktx());
        // order.setUnitPrice(zorderCn.getCmpre());
        order.setQuantity(zorderCn.getFkimg());
        // order.setAmount(zorderCn.getKwert());
        order.setVehicle(zorderCn.getXblnr());
        order.setDeliveryDate(zorderCn.getFkdat()); // CRM用fkdat當提貨日, wadat有可能出現00000000
        order.setMethod(zorderCn.getInco1());
        String contractCode = StringUtils.trimToNull(zorderCn.getVgbel());
        if (contractCode != null) {
            contractCode = contractCode.replaceAll("^0+", "");
            order.setContractCode(contractCode);
        }
        // TODO: contract name ?
        // order.setContractName();
        order.setPlantCode(zorderCn.getWerks());
        order.setPlantName(zorderCn.getWerksTx());
        order.setSalesareaCode(zorderCn.getBzirk());
        order.setSalesareaName(zorderCn.getBztxt());
        order.setDeliveryCode(StringUtils.trimToNull(zorderCn.getZ4Kunnr()));
        order.setDeliveryName(StringUtils.trimToNull(zorderCn.getZ4Desc()));
        order.setSalesCode(zorderCn.getPernr());
        order.setSalesName(zorderCn.getEname());
        order.setStatus(OrderStatusEnum.CLOSE.toString());
        order.setSapOrdernum(zorderCn.getVbeln());
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        Date createtime = null;
        try {
            createtime = df.parse(zorderCn.getErdat());
        } catch (Exception e) {
            logger.error("e={}", e);
        }
        order.setCreatetime(createtime);
        return order;
    }

    /*
    private void sendmailOrderCreate(EcOrder ecOrder) {
        // c1 開單不寄給實際審核員
        String to;
        if ("c1".equals(ecOrder.getMemberId().getLoginAccount())) {
            to = "jimmy.lee@tcci.com.tw";
        } else {
            List<TcUser> approvers = orderFacade.findApprover(ecOrder);
            StringBuilder sb = new StringBuilder();
            for (TcUser u : approvers) {
                if (u.getEmail() == null) {
                    logger.warn("user {} email is null!", u.getLoginAccount());
                    continue;
                }
                if (sb.length() > 0) {
                    sb.append(',');
                }
                sb.append(u.getEmail());
            }
            if (sb.length() == 0) {
                sb.append("jimmy.lee@tcci.com.tw");
            }
            to = sb.toString();
        }
        String orderLink = tccstoreConfig.getProperty("adminURL") + "/faces/sales/order.xhtml";
        MailNotify.orderCreate(ecOrder, to, orderLink);
    }
    */
}
