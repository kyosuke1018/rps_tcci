/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.storegateway.test;

import com.tcci.tccstore.model.TestModel;
import com.tcci.tccstore.model.customer.Customer;
import com.tcci.tccstore.model.deliveryplace.DeliveryPlace;
import com.tcci.tccstore.model.notify.Notify;
import com.tcci.tccstore.model.order.Order;
import com.tcci.tccstore.model.plant.Plant;
import com.tcci.tccstore.model.product.Product;
import com.tcci.tccstore.model.sales.Sales;
import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;

/**
 *
 * @author Jimmy.Lee
 */
public class Test extends TestBase {
    
    public static void main(String[] args) throws SSOClientException, ParseException {
        String ordernum = "0064252080";
        System.out.println(ordernum.replaceAll("^0+", ""));
        Test test = new Test();
        test.login("c1", "admin");
        //test.login("zhaoxiang9158@163.com", "admin");
        TestModel model = new TestModel();
        model.setName("client端中文");
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd");
        //isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = isoFormat.parse("2010-05-23");
        //Date date = new Date();
        model.setCreatetime(date);
        String service = "/test";
        TestModel result = test.postJson(service, TestModel.class, model);
        System.out.println(result.getName());
        System.out.println(isoFormat.format(result.getCreatetime()));
        
        // TODO:訂單紅利
        /*
        Order order = new Order();
        order.setQuantity(new BigDecimal("56"));
        String bonus = test.postJson("/order/calcbonus", String.class, order);
        System.out.println(bonus);
        */
        
        /*
        String json = "{\"contract\":{\"code\":\"50003906\",\"id\":\"10\",\"name\":\"广州地铁8号线北延线8、9标\"},\"customer\":{\"code\":\"15554\",\"id\":\"2\",\"name\":\"广东顺安投资发展有限公司\"},\"deliveryDate\":\"20150901\",\"method\":\"EXW\",\"plant\":{\"code\":\"2011\",\"id\":\"1\",\"incoFlag\":\"3\",\"name\":\"英德厂\"},\"product\":{\"code\":\"100C39431000\",\"id\":\"16\",\"name\":\"P.O 42.5R 散装\"},\"quantity\":26,\"sales\":{\"code\":\"12000305\",\"id\":\"10\",\"name\":\"林 柏君\"},\"salesarea\":{\"code\":\"010030\",\"id\":\"15\",\"name\":\"粵北\"},\"vehicle\":\"hi\",\"bonus\":0}";
        order = test.fromJson(json, Order.class);
        // System.out.println(test.toJson(order));
        service ="/order/create";
        String orderResult = test.postJson(service, String.class, order);
        System.out.println(orderResult);
        */
        
        //test.createOrder();
        test.orderCancel();
        // test.orderCreate();
        //test.orderListOpen();
        //test.orderListClose();
        //test.login("617413659@qq.com", "admin");
        //test.fileUpload();
        //test.notifyQuery();
//        String plantCode = "2011";
//        String prodCode = "100C39402000";
//        System.out.println(plantCode.matches("^25..$"));
//        System.out.println(prodCode.matches("^.{8}2...$"));
        // System.out.println(!plantCode.matches("^25..$") && prodCode.matches("2...$"));
        
    }
    
    public void notifyQuery() {
        String service = "/notify/query";
        this.executeGet(service, Notify[].class, "通知訊息");
    }
    
    public void createOrder() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            //String json = "{\"contract\":{\"code\":\"50003906\",\"id\":\"10\",\"name\":\"广州地铁8号线北延线8、9标\"},\"customer\":{\"code\":\"15554\",\"id\":\"2\",\"name\":\"广东顺安投资发展有限公司\"},\"deliveryDate\":\"20150901\",\"method\":\"EXW\",\"plant\":{\"code\":\"2011\",\"id\":\"1\",\"incoFlag\":\"3\",\"name\":\"英德厂\"},\"product\":{\"code\":\"100C39431000\",\"id\":\"16\",\"name\":\"P.O 42.5R 散装\"},\"quantity\":26,\"sales\":{\"code\":\"12000305\",\"id\":\"10\",\"name\":\"林 柏君\"},\"salesarea\":{\"code\":\"010030\",\"id\":\"15\",\"name\":\"粵北\"},\"vehicle\":\"hi\",\"bonus\":0}";
            //String json = "{\"contract\":{\"code\":\"50003954\",\"id\":\"21\",\"name\":\"深圳轨道交通11号线工程\"},\"customer\":{\"code\":\"19685\",\"id\":\"5446\",\"name\":\"中铁物贸（深圳）有限公司\"},\"deliveryDate\":\"20150925\",\"method\":\"EXW\",\"plant\":{\"code\":\"2011\",\"id\":\"1\",\"incoFlag\":\"3\",\"name\":\"英德厂\"},\"product\":{\"code\":\"100C38371000\",\"id\":\"12\",\"name\":\"P.C 32.5 散装\"},\"quantity\":1.5,\"sales\":{\"code\":\"12000305\",\"id\":\"10\",\"name\":\"林 柏君\"},\"salesarea\":{\"code\":\"010040\",\"id\":\"24\",\"name\":\"深圳\"},\"vehicle\":\"fhhjjj\",\"bonus\":0}";
            //Order order = this.fromJson(json, Order.class);
            Product product = this.fromJson("{\"id\":14,\"code\":\"100C39401000\",\"name\":\"P.O 42.5 散装\"}", Product.class);
            Customer cusomer = this.fromJson("{\"id\":3039,\"code\":\"16624\",\"name\":\"英德市鸿盛贸易有限公司\"}", Customer.class);
            Plant plant = this.fromJson("{\"id\":1,\"code\":\"2011\",\"name\":\"英德厂\"}", Plant.class);
            DeliveryPlace deliveryPlace = this.fromJson("{\"id\":815,\"code\":\"S2AH01\",\"name\":\"广东省广州市萝岗区萝岗街道\",\"salesarea\":{\"id\":108,\"code\":\"010276\",\"name\":\"廣州蘿崗區\"}}", DeliveryPlace.class);
            Sales sales = this.fromJson("{\"id\":21,\"code\":\"12005061\",\"name\":\"林 智敏\"}", Sales.class);
            Order order = new Order();
            order.setCustomer(cusomer);
            order.setProduct(product);
            order.setPlant(plant);
            order.setDelivery(deliveryPlace);
            order.setSales(sales);
            order.setQuantity(BigDecimal.valueOf(1.5));
            order.setVehicle("粤RQ8030");
            order.setDeliveryDate(sdf.format(new Date()));
            order.setMethod("FCA");
            String service ="/order/create";
            System.out.println(service);
            String orderResult = this.postJson(service, String.class, order);
            System.out.println(orderResult);
        } catch (SSOClientException ex) {
            System.out.println(ex.getMessage());
        }
        
    }

    public void orderListOpen() {
        try {
            String service = "/order/list2/OPEN/5446"; // 5446	19685	中铁物贸（深圳）有限公司
            System.out.println(service);
            Order[] result = this.get(service, Order[].class);
            System.out.println(this.toJson(result));
            
            service = "/order/list2/OPEN/10"; // 10	14454	广东新永达投资实业有限公司
            System.out.println(service);
            result = this.get(service, Order[].class);
            System.out.println(this.toJson(result));
            
            service = "/order/list2/OPEN/2"; // 2	15554	广东顺安投资发展有限公司
            System.out.println(service);
            result = this.get(service, Order[].class);
            System.out.println(this.toJson(result));
            
            service = "/order/list2/OPEN/3039"; // 3039	16624	英德市鸿盛贸易有限公司
            System.out.println(service);
            result = this.get(service, Order[].class);
            System.out.println(this.toJson(result));
        } catch (SSOClientException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public void orderListClose() {
        String service = "/order/list2/CLOSE/8321?year_month=201509"; // 8321	21899	上海实泓实业有限公司
        this.executeGet(service, Order[].class, "歷史訂單");
    }
    
    public void orderCreate() throws SSOClientException {
        String service = "/order/create";
        String orderJson = "{\"quantity\":30.0,\"vehicle\":\"1127trsty\",\"deliveryDate\":\"20151127\",\"method\":\"EXW\",\"bonus\":0,\"salesarea\":{\"id\":8357,\"code\":\"010043\",\"name\":\"深圳西區\"},\"sales\":{\"id\":8107,\"code\":\"12000305\",\"name\":\"林 柏君\"},\"product\":{\"id\":9682,\"code\":\"100C39432000\",\"name\":\"P.O 42.5R 袋装(编织袋)\"},\"plant\":{\"id\":8305,\"code\":\"2543\",\"name\":\"深圳慧生源中转站\",\"incoFlag\":1},\"delivery\":{\"id\":8694,\"code\":\"S2BD04\",\"name\":\"广东省深圳市福田区福田街道\",\"province\":\"广东省\",\"city\":\"深圳市\",\"district\":\"福田区\",\"town\":\"\",\"salesarea\":{\"id\":8357,\"code\":\"010043\",\"name\":\"深圳西區\"}},\"customer\":{\"id\":5769,\"code\":\"19685\",\"name\":\"中铁物贸（深圳）有限公司\"},\"contract\":{\"id\":9742,\"code\":\"50004057\",\"name\":\"深圳轨道交通11号线工程\"}}";
        Order order = this.fromJson(orderJson, Order.class);
        this.postJson(service, String.class, order);
    }
    
    public void orderCancel() {
        String service = "/order/cancel/2266";
        this.executeGet(service, String.class, "取消訂單");
    }
    
    public void fileUpload() {
        try {
            String service = "/test/fileUpload";
            File f = new File("C:\\tmp\\image001.png");
            Part[] parts = {
                new StringPart("info", "partner json string"),
                new FilePart("pic", f)};
            String result = this.postParts(service, String.class, parts);
            System.out.println(result);
        } catch (FileNotFoundException | SSOClientException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
