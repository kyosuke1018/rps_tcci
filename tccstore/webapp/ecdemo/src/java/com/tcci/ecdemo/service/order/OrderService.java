/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ecdemo.service.order;

import com.tcci.ecdemo.entity.EcOrder;
import com.tcci.ecdemo.entity.datawarehouse.ZorderCn;
import com.tcci.ecdemo.entity.OrderStatusEnum;
import com.tcci.ecdemo.facade.datawarehouse.ZorderCnFacade;
import com.tcci.ecdemo.facade.order.EcOrderFacade;
import com.tcci.ecdemo.model.order.Order;
import com.tcci.ecdemo.sapproxy.SdProxy;
import com.tcci.ecdemo.sapproxy.SdProxyFactory;
import com.tcci.ecdemo.sapproxy.dto.SapProxyResponseDto;
import com.tcci.ecdemo.sapproxy.dto.SapTableDto;
import com.tcci.ecdemo.sapproxy.jco.JcoUtils;
import com.tcci.ecdemo.service.HMACServiceBase;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Neo.Fu
 */
@Path("order")
@Stateless
public class OrderService extends HMACServiceBase {

    Logger logger = LoggerFactory.getLogger(OrderService.class);
    @Resource(mappedName = "jndi/sapclient.config")
    transient private Properties jndiConfig;

    @Inject
    private ZorderCnFacade zorderCnFacade;
    @Inject
    private EcOrderFacade ecOrderFacade;

    @GET
    @Path("list/{status}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Order> findByStatus(@PathParam("status") OrderStatusEnum status) {
        return findByStatus(status, "");
    }

    @GET
    @Path("list/{status}/{customer}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Order> findByStatus(@PathParam("status") OrderStatusEnum status, @PathParam("customer") String customerCode) {
        logger.debug("status={}", status);
        List<Order> orderList = new ArrayList();
        if (OrderStatusEnum.CLOSE.equals(status)) {
            orderList = findCloseOrder(customerCode);
        } else if (OrderStatusEnum.OPEN.equals(status)) {
            orderList = findOpenOrder(customerCode);
        }
        logger.debug("orderList.size()={}", orderList.size());
        return orderList;
    }

    private List<Order> findCloseOrder(String customerCode) {
        List<Order> orderList = new ArrayList();
        List<ZorderCn> zorderCnList = zorderCnFacade.findByCustomer(customerCode);
        for (ZorderCn zorderCn : zorderCnList) {
            orderList.add(convertZorderCnToOrder(zorderCn));
        }
        return orderList;
    }

    private List<Order> findOpenOrder(String customerCode) {
        List<Order> orderList = new ArrayList();
        List<EcOrder> ecOrderList = ecOrderFacade.findOpenOrderByCustomer(customerCode);
        List<String> vbelnList = new ArrayList();
        for (EcOrder ecOrder : ecOrderList) {
            vbelnList.add(ecOrder.getSapOrdernum());
        }
        Set<String> vbelnSet = new HashSet();
        try {
            Properties jcoProp = JcoUtils.getJCoProp(jndiConfig, "tcc_cn"); //取得相關Jco連結參數
            SdProxy sdProxy = SdProxyFactory.createProxy(jcoProp);//建立連線
            //SapProxyResponseDto result = sdProxy.findShippedSalesDocument(vbelnList);
            SapProxyResponseDto result = sdProxy.findShippedSalesDocument(vbelnList);
            SapTableDto sapTableDto = (SapTableDto) result.getResult();
            logger.debug("sapTableDto.getDataMapList().size()={}", sapTableDto.getDataMapList().size());
            if (sapTableDto.getDataMapList().size() > 0) {
                List<Map<String, Object>> dataMapList = sapTableDto.getDataMapList();
                for (Map<String, Object> dataMap : dataMapList) {
                    vbelnSet.add((String) dataMap.get("VBELN"));
                }
            }
            sdProxy.dispose();
        } catch (Exception e) {
            logger.error("query(), e={}", e);
        }

        for (EcOrder ecOrder : ecOrderList) {
            Order order = convertEcOrderToOrder(ecOrder);
            if (vbelnSet.contains(ecOrder.getSapOrdernum())) {
                order.setStatus(OrderStatusEnum.CLOSE);
            }
            orderList.add(order);
        }
        return orderList;
    }

    private Order convertZorderCnToOrder(ZorderCn zorderCn) {
        Order order = new Order();
        order.setProductCode(zorderCn.getMatnr());
        order.setProductName(zorderCn.getArktx());
        order.setUnitPrice(zorderCn.getCmpre());
        order.setQuantity(zorderCn.getFkimg());
        order.setAmount(zorderCn.getKwert());
        order.setVehicle(zorderCn.getXblnr());
        order.setDeliveryDate(zorderCn.getWadat());
        order.setMethod(zorderCn.getInco1Tx());
        order.setContractCode(zorderCn.getVgbel());
        order.setPlantCode(zorderCn.getWerks());
        order.setPlantName(zorderCn.getWerksTx());
        order.setSalesareaCode(zorderCn.getBzirk());
        order.setSalesareaName(zorderCn.getBztxt());
        order.setDeliveryCode(zorderCn.getInco1());
        order.setDeliveryName(zorderCn.getInco1Tx());
        order.setSalesCode(zorderCn.getPernr());
        order.setSalesName(zorderCn.getEname());
        //order.setBonus();
        order.setStatus(OrderStatusEnum.CLOSE);
        order.setSapOrdernum(zorderCn.getVbeln());
        DateFormat df = new SimpleDateFormat("yyyymmdd");
        Date createtime = null;
        try {
            createtime = df.parse(zorderCn.getErdat());
        } catch (Exception e) {
            logger.error("e={}", e);
        }
        order.setCreatetime(createtime);
        return order;
    }
    
    private Order convertEcOrderToOrder(EcOrder ecOrder) {
        Order order = new Order();
        order.setId(ecOrder.getId());
        order.setProductCode(ecOrder.getProductCode());
        order.setProductName(ecOrder.getProductName());
        order.setUnitPrice(ecOrder.getUnitPrice());
        order.setQuantity(ecOrder.getQuantity());
        order.setAmount(ecOrder.getAmount());
        order.setVehicle(ecOrder.getVehicle());
        order.setDeliveryDate(ecOrder.getDeliveryDate());
        order.setMethod(ecOrder.getMethod());
        order.setContractCode(ecOrder.getContractCode());
        order.setContractName(ecOrder.getContractName());
        order.setPlantCode(ecOrder.getPlantCode());
        order.setPlantName(ecOrder.getPlantName());
        order.setSalesareaCode(ecOrder.getSalesareaCode());
        order.setSalesareaName(ecOrder.getSalesareaName());
        order.setDeliveryCode(ecOrder.getDeliveryCode());
        order.setDeliveryName(ecOrder.getDeliveryName());
        order.setSalesCode(ecOrder.getSalesCode());
        order.setSalesName(ecOrder.getSalesName());
        order.setBonus(ecOrder.getBonus());
        order.setStatus(ecOrder.getStatus());
        order.setSapOrdernum(ecOrder.getSapOrdernum());
        order.setCreatetime(ecOrder.getCreatetime());
        return order;
    }
}
