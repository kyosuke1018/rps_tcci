/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testjerseyclient;

import com.tcci.ec.servlet.responsehandler.ResponseHandler;
import com.tcci.ec.servlet.responsehandler.SSOResponseHandler;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.glassfish.soteria.test.rs.TestData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vo.CusAddr;
import vo.Order;
import vo.OrderDetail;
import vo.OrderTransVO;
import vo.OrderFilter;
import vo.OrderShipInfo;
import vo.OrderStatusEnum;
import vo.Product;
import vo.ProductFilter;
import vo.Shipping;
import vo.Store;
import vo.UseLog;

/**
 *
 * @author Peter.pan
 */
public class TestJerseyClient {
    private static final Logger logger = LoggerFactory.getLogger(TestJerseyClient.class);
//    private static final String SERVER = "http://localhost:8080/ec-gateway";
    private static final String SERVER = "http://192.168.203.51/ec-gateway";
//    private static final String SERVER = "http://192.168.203.62/ec-gateway";
//    private static final String SERVER = "http://192.168.200.8:8080/ec-gateway";
//    private static final String SERVER = "http://tccstore.taiwancement.com/ec-gateway";
//    private static final String SERVER = "http://ecstore.taiwancement.com/ec-gateway";
//    private static final String SERVER = "http://192.168.204.61/ec-gateway";
    
//    private static final String SERVER = "http://192.168.203.62/ec-service";
    private static final String ssoTicketUrl = "http://192.168.203.50/storegateway/login";
//    private static final String ssoTicketUrl = "http://192.168.203.81/ecsso/v1/tickets";
    
    public static String ISO8601Format = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
    public static String dateFormat = "yyyyMMdd";
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //login
//        String authStrWithJWT = testLoginGetJWT("admin", "abcd1234");// ROLE_USER、ROLE_ADMIN
        //String authStrWithJWT = testLoginGetJWT("payara", "abcd1234");// ROLE_USER
        //String authStrWithJWT = testLoginGetJWT("duke", "abcd1234");// NONE
//        String authStrWithJWT = testLoginGetJWT("c2", "abcd1234");
        String authStrWithJWT = testLoginGetJWT("c1", "admin");
//        String authStrWithJWT = testLoginGetJWT("c2", "admin");
//        String authStrWithJWT = testLoginGetJWT("c3", "admin");
//        String authStrWithJWT = testLoginGetJWT("test01", "admin");
//        String authStrWithJWT = testLoginGetJWT("test01", "abcd1234");
//        String authStrWithJWT = testLoginGetJWT("test02", "abcd1234");
//        String authStrWithJWT = testLoginGetJWT("test03", "abcd1234");
        //v1.5
//        String authStrWithJWT = testLoginGetJWT("r2", "abcd1234");
//        String authStrWithJWT = testLoginGetJWT("r3", "abcd1234");
//        String authStrWithJWT = testLoginGetJWT("d1", "abcd1234");
//        String authStrWithJWT = testLoginGetJWT("d2", "abcd1234");
        
//        String authStrWithJWT = testLoginGetJWT("test03", "abcd1234");
//        String authStrWithJWT = testLoginGetJWT("test04", "abcd1234");
//        String authStrWithJWT = testLoginGetJWT("peter.pan", "abcd1234");

//           authStrWithJWT = null;

//        String authStrWithJWT2 = testLoginExecuteServiceJWT("c2", "abcd1234", authStrWithJWT);
//        testServletWithJWT(authStrWithJWT);
//        testExecuteServiceServlettWithJWT(authStrWithJWT, "/bulidshop/servlet");
//        testExecuteServiceServlettWithJWT(authStrWithJWT, "tccStore-dangkou/servlet");
//        testExecuteServiceServlettWithJWT(authStrWithJWT, "/bulidshop/servlet?name=c2&password=abcd1234");
//        testExecuteServiceServlettWithJWT(authStrWithJWT, "/bulidshop/service/sample/read");
        //member
//        testExecuteServiceServlettWithJWT(authStrWithJWT, "/ec/service/member/list");
//        testExecuteServiceServlettWithJWT(authStrWithJWT, "/ec/service/member/memberInfo");
//        testExecuteServiceServlettWithJWT(authStrWithJWT, "/ecLocal/service/member/memberInfo");
//        testExecuteServiceServlettWithJWT(authStrWithJWT, "/ec/service/member/addr/remove?id=113");
//        CusAddr cusAddr = new CusAddr();
//        cusAddr.setCarNo("A12345678;X13467888;G1457889;");
//        cusAddr.setCarNo("A12345678,X13467888,G1457889,");
//        cusAddr.setCarNo("A12345678;X13467888;G1457889;");
//        cusAddr.setPrimary(true);
//        testExecuteServiceServlettWithJWT2(authStrWithJWT, "/ec/service/member/addr/create", cusAddr);
        
//        testExecuteServiceServlettWithJWT(authStrWithJWT, "/ec/service/member/checkMember?loginAccount=test01");
//        testExecuteServiceServlettWithJWT(authStrWithJWT, "/ecLocal/service/member/industryOption?guest=true");

        //register
//        testExecuteServiceServlettWithJWT("", "/ec/service/member/register2?register=true");
//        testExecuteServiceServlettWithJWT("", "/ec/service/member/register2?register=true&name=kyle&email=kyle.cheng@tcci.com.tw&phone=0922787149&loginAccount=r2&password=abcd1234");
//        testExecuteServiceServlettWithJWT("", "/ec/service/member/register2?register=true&name=kyle&phone=0922787149&loginAccount=r1&password=abcd1234");
//            testExecuteServiceServlettWithJWT("", "/ec/service/member/resetPassword?PARAM_OLD_PASSWORD=abcd1234&PARAM_NEW_PASSWORD=abcd1234");
//            testExecuteServiceServlettWithJWT(authStrWithJWT, "/ecLocal/service/member/resetPassword?PARAM_OLD_PASSWORD=abcd1234&PARAM_NEW_PASSWORD=abcd1234");
//            testExecuteServiceServlettWithJWT("", "/ec/service/member/forgotPassword?guest=true&account=r3");
//            testExecuteServiceServlettWithJWT("", "/ec/service/member/forgotPassword?account=test03");
//            testExecuteServiceServlettWithJWT("", "/ecLocal/service/member/forgotPassword?account=r3");
            
//        Member member = new Member();
//        member.setName("kyle");
//        member.setEmail("kyle.cheng@tcci.com.tw");
//        member.setPhone("0922787149");
//        member.setLoginAccount("r3");
//        member.setPassword("abcd1234");
//        testExecuteServiceServlettWithJWT2("", "/ec/service/member/register?register=true", member);
        //customer
//        testExecuteServiceServlettWithJWT(authStrWithJWT, "/ec/service/customer/customerInfo");//X
//        testExecuteServiceServlettWithJWT(authStrWithJWT, "/ec/service/customer/list");
//        testExecuteServiceServlettWithJWT(authStrWithJWT, "/ec/service/customer/list2?memberId=1");
//        testExecuteServiceServlettWithJWT(authStrWithJWT, "/ec/service/customer/addr?customerId=1");
        //product
//        testExecuteServiceServlettWithJWT(authStrWithJWT, "/ec/service/product/list2?storeId=1");
        ProductFilter pFilter = new ProductFilter();
//        pFilter.setId(Long.parseLong("19"));
//        pFilter.setKeyword("水泥");
//        pFilter.setKeyword("熟料");
//        pFilter.setPriceLow(BigDecimal.valueOf(500));
//        pFilter.setPriceUpper(BigDecimal.valueOf(600));
//        pFilter.setTypeId(Long.parseLong("101"));
//        pFilter.setTypeId(Long.parseLong("1069"));
        pFilter.setMaxResult(10);
        pFilter.setStartResult(0);
        List<String> pstatusList = new ArrayList<>();
        pstatusList.add("P");
        pFilter.setStatusList(pstatusList);
//        pFilter.setOrder("priceDesc");
        List<Long> areaList = new ArrayList<>();
//        areaList.add(Long.parseLong("53"));
        areaList.add(Long.parseLong("75"));
//        pFilter.setAreaList(areaList);
        
        pFilter.setStoreId(Long.parseLong("141"));
        
//        testExecuteServiceServlettWithJWT2(authStrWithJWT, "/ec/service/product/list", pFilter);
//        testExecuteServiceServlettWithJWT2(authStrWithJWT, "/ecLocal/service/product/list", pFilter);
//       testExecuteServiceServlettWithJWT2("", "/ecLocal/service/product/list?guest=true", pFilter);
//        testExecuteServiceServlettWithJWT2("", "/ec/service/product/list?guest=true", pFilter);
//        testExecuteServiceServlettWithJWT(authStrWithJWT, "/ecLocal/service/product/typeLevel1");
//        testExecuteServiceServlettWithJWT("", "/ec/service/product/typeLevel1?guest=true");
//        testExecuteServiceServlettWithJWT("", "/ec/service/product/typeLevel1?guest=true");

//        /services/products/full/{id}
//        testExecuteServiceServlettWithJWT(authStrWithJWT, "/ecSeller/service/products/full/101");
//        testExecuteServiceServlettWithJWT("", "/ecLocal/service/product/full?productId=101&guest=true");
//        testExecuteServiceServlettWithJWT("", "/ec/service/product/full?productId=6&guest=true");
//        testExecuteServiceServlettWithJWT(authStrWithJWT, "/ecLocal/service/product/favorite?productId=102&flag=true");
        
//        testExecuteServiceServlettWithJWT(authStrWithJWT, "/ecLocal/service/image?oid=1");
       
        
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            
            //order
            OrderFilter oFilter = new OrderFilter();
//        oFilter.setCreatetimeEnd(new Date());
//        oFilter.setStatus(OrderStatusEnum.Inquiry);
//        oFilter.setMemberId(Long.parseLong("2"));
//        oFilter.setStoreId(Long.parseLong("141"));

            List<OrderStatusEnum> statusList = new ArrayList<>();
//        statusList.add(OrderStatusEnum.Inquiry);
            statusList.add(OrderStatusEnum.Pending);
            statusList.add(OrderStatusEnum.Approve);
//            statusList.add(OrderStatusEnum.Authorized);
//            oFilter.setStatusList(statusList);
            oFilter.setCarNo("A123");
//            logger.debug("matches b to s :"+"A1234567788".indexOf("A123"));
//            logger.debug("matches b to s :"+"A1234567788".indexOf("78"));
//            logger.debug("matches b to s :"+"A1234567788".contains("78"));
//            Date createtimeBegin = sdf.parse("20181105");
//            oFilter.setCreatetimeBegin(createtimeBegin);
//        oFilter.setCustomerId(Long.parseLong("1"));
            testExecuteServiceServlettWithJWT2(authStrWithJWT, "/ec/service/order/list", oFilter);
//        testExecuteServiceServlettWithJWT2(authStrWithJWT, "/ecLocal/service/order/list", oFilter);

        OrderTransVO transVO = new OrderTransVO();
        List<Order> olist =  new ArrayList<>();
        Order order = new Order();
        order.setId(Long.parseLong("96"));
        order.setOrderNumber("20170720-123");
        order.setStatus(OrderStatusEnum.Inquiry);
        
        Store store = new Store();
        store.setId(Long.parseLong("1"));
        order.setStore(store);
        //ship
        Shipping ship = new Shipping();
        ship.setId(Long.parseLong("6"));
        order.setShipping(ship);
        
        OrderShipInfo shipInfo = new OrderShipInfo();
//        shipInfo.setId(Long.parseLong("102"));
        shipInfo.setRecipient("test");
        shipInfo.setAddress("台泥");
        order.setShipInfo(shipInfo);
        //details
        List<OrderDetail> odlist =  new ArrayList<>();
        OrderDetail orderDetail = new OrderDetail();
        Product product = new Product();
        product.setId(Long.parseLong("6"));
        orderDetail.setProduct(product);
        orderDetail.setTotal(BigDecimal.ZERO);
        orderDetail.setPrice(BigDecimal.ZERO);
        orderDetail.setQuantity(BigDecimal.ONE);
        odlist.add(orderDetail);
        order.setOrderDetails(odlist);
        order.setTotal(BigDecimal.ZERO);
        
        olist.add(order);
        transVO.setOrderList(olist);
//            testExecuteServiceServlettWithJWT2(authStrWithJWT, "/ec/service/order/create", transVO);
//            testExecuteServiceServlettWithJWT2(authStrWithJWT, "/ecLocal/service/order/create", transVO);
//        testExecuteServiceServlettWithJWT(authStrWithJWT, "/ec/service/order/changeStatus?orderId=174&type=order&status=Cancelled");
//        testExecuteServiceServlettWithJWT(authStrWithJWT, "/ec/service/order/changeStatus?orderId=340&type=order&status=Deleted");
//        testExecuteServiceServlettWithJWT(authStrWithJWT, "/ec/service/order/changeStatus?orderId=2&type=order&status=Rejected");
//        Order order = new Order();
        order.setId(Long.parseLong("398"));
//        testExecuteServiceServlettWithJWT2(authStrWithJWT, "/ec/service/order/changeStatus?type=order&status=Approve", order);

//            testExecuteServiceServlettWithJWT(authStrWithJWT, "/ecLocal/service/order/messageCreate?orderId=1&message=test");
//            testExecuteServiceServlettWithJWT(authStrWithJWT, 
//                    URLEncoder.encode("/ecLocal/service/order/messageCreate?orderId=212&message=test!", "UTF-8"));
//            String service = "/ecLocal/service/order/messageCreate?orderId=1&message=" + URLEncoder.encode("喜歡的話算你便宜!", "UTF-8");
//            testExecuteServiceServlettWithJWT(authStrWithJWT, 
//                    "/ecLocal/service/order/messageCreate?orderId=212&message="+URLEncoder.encode("兇屁!", "UTF-8"));
//            testExecuteServiceServlettWithJWT(authStrWithJWT, 
//                    "/ec/service/order/messageCreate?orderId=73&message="+URLEncoder.encode("留言留言!", "UTF-8"));
//            testExecuteServiceServlettWithJWT(authStrWithJWT, "/ec/service/order/findMessage?orderId=358");
//            testExecuteServiceServlettWithJWT(authStrWithJWT, "/ecLocal/service/order/findMessage?orderId=358");
//                testExecuteServiceServlettWithJWT(authStrWithJWT, "/ec/service/order/1.5/findForEC1.0?account=test03&guest=true");
//                testExecuteServiceServlettWithJWT(authStrWithJWT, "/ecLocal/service/order/1.5/findForEC1.0?account=test03");
//            testExecuteServiceServlettWithJWT(authStrWithJWT, "/ec/service/order/1.5/changeCarInfo?orderId=661&carNo=123;456;798;");
//            testExecuteServiceServlettWithJWT(authStrWithJWT, "/ecLocal/service/order/1.5/changeCarInfo?orderId=661&carNo=A1234567788;B13467889;C13578865788;D567864467;");
            
//            String tgt = testLoginGetTGT("c1", "admin");
//            testLoginGetTGT2("c1", "admin");


//        testExecuteServiceServlettWithJWT(authStrWithJWT2, "/bulidshop/service/sample/read");

            //ship
//            testExecuteServiceServlettWithJWT("", "/ecLocal/service/shipping/list?guest=true");
//            testExecuteServiceServlettWithJWT("", "/ec/service/shipping/list?storeId=12&guest=true");
            //delivery
//            testExecuteServiceServlettWithJWT("", "/ecLocal/service/delivery/list?guest=true&seller_account=c1");
//            testExecuteServiceServlettWithJWT("", "/ecLocal/service/delivery/list?guest=true&seller_account=c1&delivery_code=S2TA02");
//            testExecuteServiceServlettWithJWT("", "/ec/service/delivery/list?guest=true");
//            testExecuteServiceServlettWithJWT("", "/ec/service/delivery/list?guest=true&province="+URLEncoder.encode("广东省", "UTF-8"));
//            testExecuteServiceServlettWithJWT("", "/ec/service/delivery/list?guest=true&province="+URLEncoder.encode("贵州省", "UTF-8")+"&city="+URLEncoder.encode("凯里市", "UTF-8"));
//            testExecuteServiceServlettWithJWT("", "/ec/service/delivery/list?guest=true&province="+URLEncoder.encode("贵州省", "UTF-8")+"&city="+URLEncoder.encode("凯里市", "UTF-8")+"&district="+URLEncoder.encode("凯里市", "UTF-8"));
//            testExecuteServiceServlettWithJWT("", "/ecLocal/service/delivery/list?guest=true&province="+URLEncoder.encode("贵州省", "UTF-8")+"&city="+URLEncoder.encode("凯里市", "UTF-8")+"&district="+URLEncoder.encode("凯里市", "UTF-8"));
//               testExecuteServiceServlettWithJWT("", "/ec/service/delivery/list");
//               testExecuteServiceServlettWithJWT(authStrWithJWT, "/ec/service/delivery/list?guest=true&deliveryId=43830");
            //payment
//            testExecuteServiceServlettWithJWT("", "/ecLocal/service/payment/list?storeId=1&guest=true");
//            testExecuteServiceServlettWithJWT("", "/ec/service/payment/list?storeId=1&guest=true");
            
            //push
//            testExecuteServiceServlettWithJWT(authStrWithJWT, "/ecLocal/service/push/test?guest=true&alias=c1");
//            testExecuteServiceServlettWithJWT(authStrWithJWT, "/ecLocal/service/push/test?guest=true&alias=c1&title=test&alert=test123");
//            testExecuteServiceServlettWithJWT(authStrWithJWT, "/ec/service/push/test?guest=true&alias=jackson.lee");
            //store
//            testExecuteServiceServlettWithJWT(authStrWithJWT, "/ec/service/store/myList");
//            testExecuteServiceServlettWithJWT(authStrWithJWT, "/ecLocal/service/store/list");
//            testExecuteServiceServlettWithJWT("", "/ec/service/store/findStore?storeId=103&guest=true");
//            testExecuteServiceServlettWithJWT(authStrWithJWT, "/ecLocal/service/store/disabled?storeId=103");
//            testExecuteServiceServlettWithJWT("", "/ec/service/store/full?storeId=1&guest=true");
//            testExecuteServiceServlettWithJWT("", "/ec/service/store/full?storeId=1");
//            testExecuteServiceServlettWithJWT(authStrWithJWT, "/ecLocal/service/store/favorite?storeId=1&flag=true");
//            testExecuteServiceServlettWithJWT(authStrWithJWT, "/ec/service/store/unitOption?storeId=1");
//            testExecuteServiceServlettWithJWT("", "/ec/service/store/salesAreaOption?guest=true");
//            testExecuteServiceServlettWithJWT(authStrWithJWT, "/ec/service/store/newOpen?guest=true");
//            testExecuteServiceServlettWithJWT(authStrWithJWT, "/ecLocal/service/store/newOpen?guest=true");
        
//        testRestWithJWT(authStrWithJWT, "/member/list", false);
//        testRestWithJWT(authStrWithJWT, "/sample/read", false);
//        testRestWithJWT(authStrWithJWT, "/sample/write", true);
//        testRestWithJWT(authStrWithJWT, "/sample/delete", true);

            //memberMsg
//            testExecuteServiceServlettWithJWT(authStrWithJWT,
//                    "/ec/service/member/messageCreate?type=S&id=3&message="+URLEncoder.encode("姆斯姆斯夜裡哭哭!", "UTF-8"));
//            testExecuteServiceServlettWithJWT(authStrWithJWT,
//                    "/ec/service/member/messageCreate?type=S&id=2&message="+URLEncoder.encode("73勝 1-3!", "UTF-8"));
//            testExecuteServiceServlettWithJWT(authStrWithJWT,
//                    "/ec/service/member/messageCreate?type=S&id=2&parentId=243&message="+URLEncoder.encode("73勝總亞軍", "UTF-8"));
//            testExecuteServiceServlettWithJWT(authStrWithJWT,
//                    "/ec/service/member/messageCreate?type=S&id=101&message="+URLEncoder.encode("今天下單 多久可以出貨?", "UTF-8"));
//            testExecuteServiceServlettWithJWT(authStrWithJWT, "/ec/service/member/findMessage?type=S&id=2");
//            testExecuteServiceServlettWithJWT(authStrWithJWT, "/ecLocal/service/member/findMessage?type=P&id=101");
//testExecuteServiceServlettWithJWT(authStrWithJWT, "/ec/service/member/findMessageByMember");

            //sys
//            testExecuteServiceServlettWithJWT(authStrWithJWT, "/ec/service/sys/ad/now?type=C&guest=true");
//            testExecuteServiceServlettWithJWT(authStrWithJWT, "/ec/service/sys/ad/now?type=H&guest=true");
//            testExecuteServiceServlettWithJWT(authStrWithJWT, "/ec/service/sys/ad/now?type=H");
//            testExecuteServiceServlettWithJWT(authStrWithJWT, "/ec/service/sys/ad/now?type=S&guest=true");
//            testExecuteServiceServlettWithJWT(authStrWithJWT, "/ec/service/sys/ad/now?guest=true");
//            testExecuteServiceServlettWithJWT(authStrWithJWT, "/ec/service/sys/bulletin/list");
//                testExecuteServiceServlettWithJWT(authStrWithJWT, "/ecLocal/service/sys/1.5/tccArticle?guest=true");
            
            UseLog useLog = new UseLog();
            useLog.setCategory("LOGIN_APP");
//            useLog.setClientInfo(SERVER);
            useLog.setPatrolLatitude((float) 25.060274);
            useLog.setPatrolLongitude((float) 121.52483);
//            testExecuteServiceServlettWithJWT2(authStrWithJWT, "/ec/service/sys/saveUseLog", useLog);
//            testExecuteServiceServlettWithJWT2(authStrWithJWT, "/ecLocal/service/sys/saveUseLog", useLog);


        } catch (Exception ex) {
        }
    }
    
    public static String testLoginGetJWT(String account, String password){
        Client client = ClientBuilder.newBuilder().build();
        WebTarget target = client.target(SERVER);
        WebTarget resource = target.path("/servlet")
                .queryParam("name", account)
                .queryParam("password", password);

        Response res = resource.request(MediaType.TEXT_PLAIN).get();
        int status = res.getStatus();
        String authStr = res.getHeaderString(HttpHeaders.AUTHORIZATION);
        String msg = res.readEntity(String.class);
        
        logger.debug("status = "+status);
        logger.debug("authStr = "+authStr);
        logger.debug("msg = "+msg);
        
        client.close();
        return authStr;
    }
    
    public static void testServletWithJWT(String authStr){
        Client client = ClientBuilder.newBuilder().build();
        WebTarget target = client.target(SERVER);
        WebTarget resource = target.path("/servlet");

        Response res = resource.request(MediaType.TEXT_PLAIN)
                                .header(HttpHeaders.AUTHORIZATION, authStr)
                                .get();
        int status = res.getStatus();
        String msg = res.readEntity(String.class);
        
        logger.debug("status = "+status);
        logger.debug("msg = "+msg);
        
        client.close();
    }

    public static void testRestWithJWT(String authStr, String path, boolean post){
        Client client = ClientBuilder.newBuilder().build();
        WebTarget target = client.target(SERVER);
        WebTarget resource = target.path("/resources").path(path);

        Invocation.Builder builder = resource.request(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, authStr);
        
        Response res;
        if( post ){
            TestData entity = new TestData("p1", "p2");
            res = builder.post(Entity.entity(entity, MediaType.APPLICATION_JSON_TYPE));
        }else{
            res = builder.get();
        }
        
        int status = res.getStatus();
        String msg = res.readEntity(String.class);
        
        logger.debug("status = "+status);
        logger.debug("msg = "+msg);
        
        client.close();
    }
    
    public static String testLoginExecuteServiceJWT(String account, String password,String authStr){
        Client client = ClientBuilder.newBuilder().build();
        WebTarget target = client.target(SERVER);
        WebTarget resource = target.path("/servlet/executeservice");
        Response res = resource.request(MediaType.TEXT_PLAIN)
                                .header(HttpHeaders.AUTHORIZATION, authStr)
                                .header("service", "/bulidshop/servlet?name=c2&password=abcd1234")
                                .get();
//        WebTarget resource = target.path("/servlet")
//                .queryParam("name", account)
//                .queryParam("password", password);

//        Response res = resource.request(MediaType.TEXT_PLAIN).get();
        int status = res.getStatus();
        String authStr2 = res.getHeaderString(HttpHeaders.AUTHORIZATION);
        String msg = res.readEntity(String.class);
        
        logger.debug("status = "+status);
        logger.debug("authStr2 = "+authStr2);
        logger.debug("msg = "+msg);
        
        client.close();
        return authStr2;
    }
    
    //get
    public static void testExecuteServiceServlettWithJWT(String authStr, String service){
        Client client = ClientBuilder.newBuilder().build();
        WebTarget target = client.target(SERVER);
        WebTarget resource = target.path("/servlet/executeservice");

        Response res = resource.request(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, authStr)
                                .header("service", service)
                                .get();
        int status = res.getStatus();
        String msg = res.readEntity(String.class);
        
        logger.debug("status = "+status);
        logger.debug("msg = "+msg);
        
        client.close();
    }
    
    //post
    public static void testExecuteServiceServlettWithJWT2(String authStr, String service, Object entity){
        Client client = ClientBuilder.newBuilder().build();
        WebTarget target = client.target(SERVER);
        WebTarget resource = target.path("/servlet/executeservice");

        Invocation.Builder builder = resource.request(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, authStr)
                                .header("service", service);
        
        Response res = builder.post(Entity.entity(entity, MediaType.APPLICATION_JSON_TYPE));
        int status = res.getStatus();
        String msg = res.readEntity(String.class);
        
        logger.debug("status = "+status);
        logger.debug("msg = "+msg);
        
        client.close();
    }
    
    public static String testLoginGetTGT2(String account, String password){
        Client client = ClientBuilder.newBuilder().build();
        WebTarget target = client.target(ssoTicketUrl);
        ////        WebTarget resource = target.path("/servlet/executeservice");
        WebTarget resource = target
        ////        WebTarget resource = target.path("/login")
                .queryParam("username", account)
                .queryParam("password", password);
        Response res = resource.request(MediaType.APPLICATION_FORM_URLENCODED_TYPE).get();
        //Response res = resource.request(MediaType.APPLICATION_FORM_URLENCODED_TYPE).post(null);
        int status = res.getStatus();
        String msg = res.readEntity(String.class);
        logger.debug("status = "+status);
        logger.debug("msg = "+msg);
        //
        client.close();
        return "";
    }
    
    public static String testLoginGetTGT(String account, String password){
        PostMethod method = new PostMethod(ssoTicketUrl);
        method.addParameter("username", account);
        method.addParameter("password", password);
//        
        HttpClient httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());
        try {
            httpClient.executeMethod(HostConfiguration.ANY_HOST_CONFIGURATION, method, new HttpState());
            ResponseHandler responseHandler = (ResponseHandler) new SSOResponseHandler(method);
//            responseHandler.process(httpResponse);


logger.debug("status = "+responseHandler.getStatusCode());
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(TestJerseyClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
//        Client client = ClientBuilder.newBuilder().build();
//        WebTarget target = client.target(ssoTicketUrl);
////        WebTarget resource = target.path("/servlet/executeservice");
//        WebTarget resource = target
////        WebTarget resource = target.path("/login")
//                .queryParam("username", account)
//                .queryParam("password", password);
//
////        Response res = resource.request(MediaType.TEXT_PLAIN).get();
//        Response res = resource.request(MediaType.APPLICATION_JSON_TYPE).post(null);
//        int status = res.getStatus();
//        String msg = res.readEntity(String.class);

//        Invocation.Builder builder = resource.request(MediaType.APPLICATION_JSON)
//                                .header(HttpHeaders.AUTHORIZATION, authStr)
//                                .header("service", service);
//        
//        Response res = builder.post(Entity.entity(entity, MediaType.APPLICATION_JSON_TYPE));
//        int status = res.getStatus();
//        String msg = res.readEntity(String.class);
        
//        logger.debug("status = "+status);
//        logger.debug("msg = "+msg);
//        
//        client.close();


        return "";
    }
    
}
