/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.test;

import com.tcci.tccstore.model.customer.Customer;
import com.tcci.tccstore.model.deliveryplace.DeliveryPlace;
import com.tcci.tccstore.model.member.Member;
import com.tcci.tccstore.model.order.Order;
import com.tcci.tccstore.model.plant.Plant;
import com.tcci.tccstore.model.product.Product;
import com.tcci.tccstore.model.sales.Sales;
import com.tcci.tccstore.model.salesarea.Salesarea;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author Neo.Fu
 */
public class OrderTest extends TestBase {

    public static void main(String[] args) {
        OrderTest test = new OrderTest();
        try {
            test.login("jimmy.lee", "admin");
            //查歷史訂單
            System.out.println("Test: /order/list/CLOSE/0000019706");
            Order[] closeOrders = test.get("/order/list/CLOSE/0000019706", Order[].class);
            showResult(closeOrders);

            //查歷史訂單
            System.out.println("Test: /order/list/CLOSE/0000019706?year_month=201410");
            Order[] closeOrders2 = test.get("/order/list/CLOSE/0000019706?year_month=201410", Order[].class);
            showResult(closeOrders2);

            //查新訂單查詢
            System.out.println("Test: /order/list/OPEN/15261");
            Order[] openOrders = test.get("/order/list/OPEN/15261", Order[].class);
            showResult(openOrders);

            //查新訂單查詢:SAP已出貨
            System.out.println("Test: /order/list/OPEN/15261");
            Order[] shippedOrder = test.get("/order/list/OPEN/15261", Order[].class);
            showResult(shippedOrder);

            //建立訂單
            System.out.println("Test: /order/create/");
            String target = "/order/create";
            Order order = new Order();
            Member member = new Member();
            member.setLoginAccount("neo.fu");
            order.setMember(member);
            Product product = new Product();
            product.setId(new Long(7));
            product.setCode("100C37531000");
            product.setName("P.Ⅱ 52.5R 散装");
            order.setProduct(product);
            order.setProductCode(product.getCode());
            order.setProductName(product.getName());
            order.setQuantity(BigDecimal.valueOf(2));
            order.setVehicle("123-ABC");
            order.setDeliveryDate("20150807");
            order.setMethod("FCA");
//            Contract contract = new Contract();
//            contract.setId(new Long(1));
//            contract.setCode("C001");
//            order.setContract(contract);
//            order.setContractCode(contract.getCode());
            Plant plant = new Plant();
            plant.setId(new Long(25));
            plant.setCode("2511");
            plant.setName("贵港厂");
            order.setPlant(plant);
            order.setPlantCode(plant.getCode());
            order.setPlantName(plant.getName());
            Salesarea salesarea = new Salesarea();
            salesarea.setId(new Long(200));
            salesarea.setCode("012160");
            salesarea.setName("桂平");
            order.setSalesarea(salesarea);
            order.setSalesareaCode(salesarea.getCode());
            order.setSalesareaName(salesarea.getName());
            DeliveryPlace delivery = new DeliveryPlace();
            delivery.setId(new Long(701));
            delivery.setCode("S1RD01");
            delivery.setName("广西省贵港市桂平市西山镇");
            order.setDelivery(delivery);
            order.setDeliveryCode(delivery.getCode());
            order.setDeliveryName(delivery.getName());
            Sales sales = new Sales();
            sales.setId(new Long(146));
            sales.setCode("52011066");
            sales.setName("谢 国翠");
            order.setSales(sales);
            order.setSalesCode(sales.getCode());
            order.setSalesName(sales.getName());
            order.setBonus(2);
            order.setStatus("OPEN");
            order.setCreatetime(new Date());
            Customer customer = new Customer();
            customer.setId(new Long(2907));
            order.setCustomer(customer);
            String result = test.postJson(target, String.class, order);
            System.out.println("result=" + result);

            //取消訂單
            System.out.println("Test: /order/cancel/15");
            String result5 = test.get("/order/cancel/15", String.class);
            System.out.println("result=" + result5);

            //取消訂單 (已取消訂單)
            System.out.println("Test: /order/cancel/16");
            String result6 = test.get("/order/cancel/16", String.class);
            System.out.println("result=" + result6);

            //取消訂單 (已出貨訂單)
            System.out.println("Test: /order/cancel/1");
            String result7 = test.get("/order/cancel/1", String.class);
            System.out.println("result=" + result7);

            //查詢訂單價格
            System.out.println("Test: order/price");
            Order order3 = new Order();
            Product product3 = new Product();
            product3.setId(new Long(1));
            product3.setCode("100C38372000");
            order3.setProduct(product3);
            order3.setProductCode(product3.getCode());
            order3.setProductName(product3.getName());
            order3.setQuantity(BigDecimal.ONE);
            Plant plant3 = new Plant();
            plant3.setId(new Long(1));
            plant3.setCode("2511");
            order3.setPlant(plant3);
            Salesarea salesarea3 = new Salesarea();
            salesarea3.setId(new Long(1));
            salesarea3.setCode("12010");
            order3.setSalesarea(salesarea3);
            order3.setSalesareaCode(salesarea3.getCode());
            order3.setSalesareaName(salesarea3.getName());
            DeliveryPlace delivery3 = new DeliveryPlace();
            delivery3.setId(new Long(1));
            delivery3.setCode("EXW");
            delivery3.setName("厂交自提");
            order3.setDelivery(delivery3);
            Sales sales3 = new Sales();
            sales3.setId(new Long(1));
            sales3.setCode("1");
            sales3.setName("1");
            order3.setSales(sales3);
            order3.setSalesCode(sales3.getCode());
            order3.setSalesName(sales3.getName());
            order3.setBonus(1);
            order3.setStatus("OPEN");
            order3.setCreatetime(new Date());
            Customer customer3 = new Customer();
            customer3.setId(new Long(1));
            customer3.setCode("0000015261");
            order3.setCustomer(customer3);
            BigDecimal unitPrice = test.postJson("/order/price", BigDecimal.class, order3);
            System.out.println("unitPrice=" + unitPrice);
        } catch (Exception e) {
            System.out.println("e=" + e);
        }
    }

    public static void showResult(Order[] orders) {
        if (null == orders || 0 == orders.length) {
            System.out.println("empty");
        } else {
            for (Order order : orders) {
                System.out.println("order=" + order);
            }
        }
        System.out.println("--------------------");
    }

}
