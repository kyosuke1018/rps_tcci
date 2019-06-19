/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testjerseyclient;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import static org.apache.http.HttpVersion.HTTP;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.glassfish.soteria.test.rs.TestData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Peter.pan
 */
public class TestJerseyClient {
    private static final Logger logger = LoggerFactory.getLogger(TestJerseyClient.class);
    private static final String SERVER = "http://localhost:8080/ec-service";
//    private static final String SERVER = "http://192.168.203.62/tccStore-dangkou";
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        String authStrWithJWT = testLoginGetJWT("admin", "abcd1234");// ROLE_USER、ROLE_ADMIN
//        String authStrWithJWT = testLoginGetJWT("payara", "abcd1234");// ROLE_USER
        //String authStrWithJWT = testLoginGetJWT("duke", "abcd1234");// NONE
//        String authStrWithJWT = testLoginGetJWT("kyle.cheng", "admin");// ADMINISTRATORS
//        String authStrWithJWT = testLoginGetJWT("c2", "admin");
        String authStrWithJWT = testLoginGetJWT("c1", "abcd1234");
//        String authStrWithJWT = testLoginGetJWT("c2", "abcd1234");
//        String authStrWithJWT = testLoginGetJWT("0917052192", "abcd1234");//世明贸易公司（經銷商）
//         String authStrWithJWT = testLoginGetJWT("13471565005", "abcd1234");//正顺建材（檔口）
//         String authStrWithJWT = testLoginGetJWT("0938503225", "abcd1234");//毓扬企业社（檔口）
        
//        try {
            //        testServletWithJWT(authStrWithJWT);
        
//        testRestWithJWT(authStrWithJWT, "/sample/read", false);
//        testRestWithJWT(authStrWithJWT, "/sample/write", true);
//        testRestWithJWT(authStrWithJWT, "/sample/delete", true);


        testRestWithJWT(authStrWithJWT, "/member/list", false);
        testRestWithJWT(authStrWithJWT, "/customer/list", false);
//        testRestWithJWT(authStrWithJWT, "/customer/list2?memberId=1", false);
//        testRestWithJWT(authStrWithJWT, "/customer/list%3FmemberId%3D1", false);
//        testRestWithJWT2(authStrWithJWT, "/customer/list?memberId=1", false);
        testRestWithJWT(authStrWithJWT, "/customer/addr?customerId=1", false);
        
        //ProductList
//        testProductList(authStrWithJWT, "/dangkou/product/list", true);
//        testProductList(authStrWithJWT, "/dangkou/product2/list", true);
//        testProductList2(authStrWithJWT, "/dangkou/product2/list", true);
        //ReaetPassword
//        testReaetPassword(authStrWithJWT, "/ecsso/resetPassword", true);
//        } catch (UnsupportedEncodingException ex) {
//            logger.error("ex = "+ex);
//        }
    }
    
    public static String testLoginGetJWT(String account, String password){
        Client client = ClientBuilder.newBuilder().build();
//        WebTarget target = client.target("http://localhost:8080/jwtEx2");
//        WebTarget target = client.target("http://localhost:8080/tccStore-dangkou");
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
        WebTarget target = client.target("http://localhost:8080/tccStore-dangkou");
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
//        WebTarget target = client.target("http://localhost:8080/tccStore-dangkou");
        WebTarget target = client.target(SERVER);
        WebTarget resource = target.path("/resources").path(path);

        Invocation.Builder builder = resource.request(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, authStr);
        
        Response res;
        if( post ){
            TestData entity = new TestData("p1", "p2");
            res = builder.post(Entity.entity(entity, MediaType.APPLICATION_JSON_TYPE));
            
            logger.debug("MediaType = "+res.getMediaType().toString());//application/json
        }else{
            res = builder.get();
        }
        
        int status = res.getStatus();
        String msg = res.readEntity(String.class);
        
        logger.debug("status = "+status);
        logger.debug("msg = "+msg);
        
        client.close();
    }
    
    public static void testRestWithJWT2(String authStr, String path, boolean post){
        Client client = ClientBuilder.newBuilder().build();
        WebTarget target = client.target(SERVER);
        WebTarget resource = target.path("/resources").path(path);
        
        Invocation.Builder builder = resource.request(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, authStr);
        
        Response res = builder.get();
        
        int status = res.getStatus();
        String msg = res.readEntity(String.class);
        
        logger.debug("status = "+status);
        logger.debug("msg = "+msg);
        
        client.close();
    }
    
    //OK!
    public static void testProductList(String authStr, String path, boolean post) throws UnsupportedEncodingException{
        Client client = ClientBuilder.newBuilder().build();
//        WebTarget target = client.target("http://localhost:8080/tccStore-dangkou").path("/resources").path(path);
        WebTarget target = client.target(SERVER).path("/resources").path(path);
        
//        Response res = target.request(MediaType.APPLICATION_JSON_TYPE)
//                .header(HttpHeaders.AUTHORIZATION, authStr)
//                .post(Entity.entity(form,MediaType.APPLICATION_FORM_URLENCODED_TYPE));
        Response res;
        if( post ){
            Form form = new Form();
            form.param("dangkou_id", "1");// /dangkou/product/list
            
            String str = Entity.entity(form,MediaType.APPLICATION_FORM_URLENCODED_TYPE).toString();
            logger.debug("str = "+str);
            res = target.request(MediaType.APPLICATION_JSON_TYPE)
                    .header(HttpHeaders.AUTHORIZATION, authStr)
                    .post(Entity.entity(form,MediaType.APPLICATION_FORM_URLENCODED_TYPE));
        }else{
            res = target.request(MediaType.APPLICATION_JSON_TYPE)
                .header(HttpHeaders.AUTHORIZATION, authStr)
                .get();
        }
        
        int status = res.getStatus();
        String msg = res.readEntity(String.class);
        
        logger.debug("MediaType = "+res.getMediaType().toString());
        logger.debug("status = "+status);
        logger.debug("msg = "+msg);//[{"code":"100C38331000","id":9,"name":"P.C 32.5R 散装"},{"code":"100C38332090","id":13456,"name":"P.C 32.5R 袋装(纸袋)"},{"code":"100C38332000","id":10,"name":"P.C 32.5R 袋装(编织袋)"},{"code":"100C39409000","id":13471,"name":"P.O 42.5 太空包"},{"code":"100C39431000","id":16,"name":"P.O 42.5R 散装"},{"code":"100C39432090","id":13476,"name":"P.O 42.5R 袋装(纸袋)"},{"code":"100C39432000","id":17,"name":"P.O 42.5R 袋装(编织袋)"},{"code":"100C20001001","id":13433,"name":"熟料"}]
        client.close();
    }
    
    
    public static void testProductList2(String authStr, String path, boolean post) throws UnsupportedEncodingException{
        Client client = ClientBuilder.newBuilder().build();
        WebTarget target = client.target("http://localhost:8080/tccStore-dangkou");
        WebTarget resource = target.path("/resources").path(path);
        
        Invocation.Builder builder = resource.request(MediaType.APPLICATION_JSON) //application/json
//        Invocation.Builder builder = resource.request(MediaType.APPLICATION_FORM_URLENCODED) 
//                                .property("Content-Type", "application/x-www-form-urlencoded")
//                                .property("Content-Type", MediaType.APPLICATION_FORM_URLENCODED)
//                                .property("charset", "utf-8")
//                                .header("Content-Type", "application/x-www-form-urlencoded")
//                                .header("charset", "utf-8")
                                .header(HttpHeaders.AUTHORIZATION, authStr);
//        Invocation.Builder builder = resource.request(MediaType.APPLICATION_FORM_URLENCODED)
//                                .header(HttpHeaders.AUTHORIZATION, authStr);

        Response res;
        if( post ){
//            List<ProductListTestData> testData = new ArrayList<>();
//            ProductListTestData productListTestData = new ProductListTestData(new Long("1"));
//            testData.add(productListTestData);
//            List<NameValuePair> postParams = new ArrayList<>();
//            postParams.add(new BasicNameValuePair("dangkou_id", "1"));
//            res = builder.post(Entity.entity(new UrlEncodedFormEntity(postParams, "utf-8"), MediaType.APPLICATION_FORM_URLENCODED_TYPE));
//            StringEntity entity = new UrlEncodedFormEntity(postParams, "utf-8");
//            res = builder.post(entity);
//            res = builder.post(Entity.entity(entity, MediaType.APPLICATION_JSON_TYPE));
            Form form = new Form();
            form.param("dangkou_id", "1");
//            res = builder.post(Entity.form(form));
            res = builder.post(Entity.entity(form,MediaType.APPLICATION_FORM_URLENCODED_TYPE));
//            res = builder.post(Entity.entity("[dangkou_id=1]", MediaType.APPLICATION_JSON_TYPE));
//            res = builder.post(Entity.entity("[dangkou_id=1]", MediaType.APPLICATION_FORM_URLENCODED_TYPE));
//            res = builder.post(Entity.entity("{dangkou_id=1}", MediaType.APPLICATION_FORM_URLENCODED_TYPE));
//res = builder.post(Entity.entity(new UrlEncodedFormEntity(testData), MediaType.APPLICATION_JSON_TYPE));
//res = builder.accept(MediaType.APPLICATION_JSON_TYPE)
//            res = builder.post(Entity.entity(entity, MediaType.APPLICATION_JSON_TYPE));
//            res = builder.post(Entity.entity(entity, MediaType.APPLICATION_JSON_TYPE));
//            res = builder.post(Entity.entity(entity, MediaType.APPLICATION_FORM_URLENCODED_TYPE));
//            res = builder.post(Entity.entity(entity, MediaType.APPLICATION_JSON_TYPE));
//            logger.debug("Authorization = "+res.getHeaderString("Authorization"));
            logger.debug("MediaType = "+res.getMediaType().toString());
        }else{
            res = builder.get();
        }
        
        int status = res.getStatus();
        String msg = res.readEntity(String.class);
        
        logger.debug("status = "+status);
        logger.debug("msg = "+msg);
        
        client.close();
    }
    
    public static void testReaetPassword(String authStr, String path, boolean post) throws UnsupportedEncodingException{
        Client client = ClientBuilder.newBuilder().build();
        WebTarget target = client.target(SERVER).path("/resources").path(path);
        
        Response res;
        if( post ){
            Form form = new Form();
            form.param("old_password", "abcd1234");// /ecsso/resetPassword
            form.param("new_password", "abcd1234");
            String str = Entity.entity(form,MediaType.APPLICATION_FORM_URLENCODED_TYPE).toString();
            logger.debug("str = "+str);
            res = target.request(MediaType.TEXT_PLAIN)
//            res = target.request(MediaType.APPLICATION_JSON_TYPE)
                    .header(HttpHeaders.AUTHORIZATION, authStr)
                    .post(Entity.entity(form,MediaType.APPLICATION_FORM_URLENCODED_TYPE));
        }else{
            res = target.request(MediaType.APPLICATION_JSON_TYPE)
                .header(HttpHeaders.AUTHORIZATION, authStr)
                .get();
        }
        
        int status = res.getStatus();
        String msg = res.readEntity(String.class);
        
        logger.debug("MediaType = "+res.getMediaType().toString());
        logger.debug("status = "+status);
        logger.debug("msg = "+msg);//[{"code":"100C38331000","id":9,"name":"P.C 32.5R 散装"},{"code":"100C38332090","id":13456,"name":"P.C 32.5R 袋装(纸袋)"},{"code":"100C38332000","id":10,"name":"P.C 32.5R 袋装(编织袋)"},{"code":"100C39409000","id":13471,"name":"P.O 42.5 太空包"},{"code":"100C39431000","id":16,"name":"P.O 42.5R 散装"},{"code":"100C39432090","id":13476,"name":"P.O 42.5R 袋装(纸袋)"},{"code":"100C39432000","id":17,"name":"P.O 42.5R 袋装(编织袋)"},{"code":"100C20001001","id":13433,"name":"熟料"}]
        client.close();
    }
    
//    public static void testProductList2(String authStr, String path, boolean post){
//        RequestHandler requestHandler = RequestHandlerFactory
//                .createRequestMethod(httpRequest.getMethod());
//        HttpMethod method = null;
//    }
    
}
