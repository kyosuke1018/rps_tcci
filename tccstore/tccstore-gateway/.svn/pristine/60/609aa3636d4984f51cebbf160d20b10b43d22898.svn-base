/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.storegateway.test;

import com.tcci.tccstore.model.contract.Contract;
import com.tcci.tccstore.model.customer.Customer;
import com.tcci.tccstore.model.deliveryplace.DeliveryPlace;
import com.tcci.tccstore.model.order.Order;
import com.tcci.tccstore.model.plant.Plant;
import com.tcci.tccstore.model.product.Product;
import com.tcci.tccstore.model.sales.Sales;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Jimmy.Lee
 */
public class OrderTest extends TestBase {

    public static void main(String[] args) throws SSOClientException {
        OrderTest test = new OrderTest();
        test.login("c1", "admin");
        //test.login("lin6511@126.com", "admin");

        test.orderCreate();
        test.orderCancel();
        //test.orderListOpen();
        //test.orderListClose();
    }

    public void orderCreate() throws SSOClientException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            //String json = "{\"contract\":{\"code\":\"50003906\",\"id\":\"10\",\"name\":\"广州地铁8号线北延线8、9标\"},\"customer\":{\"code\":\"15554\",\"id\":\"2\",\"name\":\"广东顺安投资发展有限公司\"},\"deliveryDate\":\"20150901\",\"method\":\"EXW\",\"plant\":{\"code\":\"2011\",\"id\":\"1\",\"incoFlag\":\"3\",\"name\":\"英德厂\"},\"product\":{\"code\":\"100C39431000\",\"id\":\"16\",\"name\":\"P.O 42.5R 散装\"},\"quantity\":26,\"sales\":{\"code\":\"12000305\",\"id\":\"10\",\"name\":\"林 柏君\"},\"salesarea\":{\"code\":\"010030\",\"id\":\"15\",\"name\":\"粵北\"},\"vehicle\":\"hi\",\"bonus\":0}";
        //String json = "{\"contract\":{\"code\":\"50003954\",\"id\":\"21\",\"name\":\"深圳轨道交通11号线工程\"},\"customer\":{\"code\":\"19685\",\"id\":\"5446\",\"name\":\"中铁物贸（深圳）有限公司\"},\"deliveryDate\":\"20150925\",\"method\":\"EXW\",\"plant\":{\"code\":\"2011\",\"id\":\"1\",\"incoFlag\":\"3\",\"name\":\"英德厂\"},\"product\":{\"code\":\"100C38371000\",\"id\":\"12\",\"name\":\"P.C 32.5 散装\"},\"quantity\":1.5,\"sales\":{\"code\":\"12000305\",\"id\":\"10\",\"name\":\"林 柏君\"},\"salesarea\":{\"code\":\"010040\",\"id\":\"24\",\"name\":\"深圳\"},\"vehicle\":\"fhhjjj\",\"bonus\":0}";
        //Order order = this.fromJson(json, Order.class);
        Product product = this.fromJson("{\"id\":14,\"code\":\"100C39401000\",\"name\":\"P.O 42.5 散装\"}", Product.class);
        Customer cusomer = this.fromJson("{\"id\":3039,\"code\":\"16624\",\"name\":\"英德市鸿盛贸易有限公司\"}", Customer.class);
        // Contract contract = this.fromJson("{\"id\":12,\"code\":\"50003923\",\"name\":\"江番高速工程\"}", Contract.class);
        Contract contract = this.fromJson("{\"id\":21,\"code\":\"50003954\",\"name\":\"深圳轨道交通11号线工程\"}", Contract.class);
        Plant plant = this.fromJson("{\"id\":1,\"code\":\"2011\",\"name\":\"英德厂\"}", Plant.class);
        // Plant plant = this.fromJson("{\"id\":53,\"code\":\"3311\",\"name\":\"重庆厂\"}", Plant.class);
        DeliveryPlace deliveryPlace = this.fromJson("{\"id\":815,\"code\":\"S2AH01\",\"name\":\"广东省广州市萝岗区萝岗街道\",\"salesarea\":{\"id\":108,\"code\":\"010276\",\"name\":\"廣州蘿崗區\"}}", DeliveryPlace.class);
        Sales sales = this.fromJson("{\"id\":21,\"code\":\"12005061\",\"name\":\"林 智敏\"}", Sales.class);
        Order order = new Order();
        order.setCustomer(cusomer);
        //order.setContract(contract);
        //order.setPosnr(10);
        order.setProduct(product);
        order.setPlant(plant);
        order.setDelivery(deliveryPlace);
        order.setSales(sales);
        order.setQuantity(BigDecimal.valueOf(1.5));
        //order.setVehicle("粤RQ8030");
        order.setVehicle("粤RQ1234");
        order.setDeliveryDate(sdf.format(new Date()));
        order.setMethod("FCA");
        String service = "/order/create";
        this.executeJson(service, String.class, order, "新增訂單");
    }

    public void orderCancel() {
        String service = "/order/cancel/2340";
        this.executeGet(service, String.class, "取消訂單");
    }

    public void orderListOpen() {
        String service = "/order/list2/OPEN/5446"; // 5446	19685	中铁物贸（深圳）有限公司
        this.executeGet(service, Order[].class, "訂單資訊");

        service = "/order/list2/OPEN/10"; // 10	14454	广东新永达投资实业有限公司
        this.executeGet(service, Order[].class, "訂單資訊");

        service = "/order/list2/OPEN/2"; // 2	15554	广东顺安投资发展有限公司
        this.executeGet(service, Order[].class, "訂單資訊");

        service = "/order/list2/OPEN/3039"; // 3039	16624	英德市鸿盛贸易有限公司
        this.executeGet(service, Order[].class, "訂單資訊");
    }

    public void orderListClose() {
        String service = "/order/list2/CLOSE/8321?year_month=201509"; // 8321	21899	上海实泓实业有限公司
        this.executeGet(service, Order[].class, "歷史訂單");
    }

}
