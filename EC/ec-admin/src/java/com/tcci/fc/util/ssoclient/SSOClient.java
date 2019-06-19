/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.util.ssoclient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jimmy.Lee
 */
public class SSOClient {

    private final static Logger logger = LoggerFactory.getLogger(SSOClient.class);

    // sso
    private String server;
    private String username;
    private String password;
    private String tgt;
    private long tgtTimestamp = 0;

    // http client & json mapper
    private HttpClient httpClient;
    private ObjectMapper mapper;

    // singleton
    private static SSOClient instance = null;

    public SSOClient() {
    }

    public static SSOClient getInstance() {
        if (instance == null) {
            synchronized (SSOClient.class) {
                if (instance == null) {
                    instance = new SSOClient();
                    instance.init();
                }
            }
        }
        return instance;
    }

    public void init() {
        server = System.getProperty("com.taiwancement.sso.serverUrlPrefix") + "/v1/tickets";
        username = System.getProperty("com.taiwancement.sso.restfulUser");
        password = System.getProperty("com.taiwancement.sso.restfulPwd");
        httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());
        httpClient.getParams().setParameter("http.protocol.content-charset", "UTF-8");
        httpClient.getParams().setParameter(HttpClientParams.ALLOW_CIRCULAR_REDIRECTS, true);
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000); // 5秒內無法建立連線, timeout
        httpClient.getHttpConnectionManager().getParams().setSoTimeout(15000); // 15秒內未收到response, timeout
        httpClient.getHttpConnectionManager().getParams().setDefaultMaxConnectionsPerHost(50);
        httpClient.getHttpConnectionManager().getParams().setMaxTotalConnections(50);
        mapper = new ObjectMapper().setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"))
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(Date.class, new MultiDateDeserializer());
        mapper.registerModule(simpleModule);
    }

    // API
    public synchronized void getTicketGrantingTicket() throws SSOClientException {
        // 避免短時間內一直取TGT
        if ((System.currentTimeMillis() - tgtTimestamp) < 5000) {
            return;
        }
        PostMethod method = new PostMethod(server);
        method.addParameter("username", username);
        method.addParameter("password", password);
        try {
            int status = httpClient.executeMethod(HostConfiguration.ANY_HOST_CONFIGURATION, method, new HttpState());
            if (201 == status) { // 認證成功
                String output = streamToString(method.getResponseBodyAsStream(), "UTF-8");
                Matcher matcher = Pattern.compile(".*action=\".*/(.*?)\".*")
                        .matcher(output);
                if (matcher.matches()) {
                    tgt = matcher.group(1);
                    tgtTimestamp = System.currentTimeMillis();
                    logger.info("getTicketGrantingTicket success! username={}", username);
                    logger.debug("tgt={}", tgt);
                    return;
                }
            }
            logger.error("getTicketGrantingTicket fail! username={}, status={}", username, status);
            throw new SSOClientException("無法取得TGT! 狀態碼:" + status, status);
        } catch (IOException ex) {
            logger.error("getTicketGrantingTicket fail! username={}", username, ex);
            throw new SSOClientException("無法取得TGT!", ex);
        } finally {
            method.releaseConnection();
        }
    }

    public String getServiceTicket(String service) throws SSOClientException {
        PostMethod method = new PostMethod(server + "/" + tgt);
        method.addParameter("service", service);
        try {
            int status = httpClient.executeMethod(HostConfiguration.ANY_HOST_CONFIGURATION, method, new HttpState());
            if (200 == status) {
                return streamToString(method.getResponseBodyAsStream(), "UTF-8");
            } else if (404 == status) {
                throw new SSOClientExpiredException();
            } else {
                logger.error("getServiceTicket fail! status={}, service={}", status, service);
                throw new SSOClientException("無法取得service ticket! 狀態碼:" + status, status);
            }
        } catch (IOException ex) {
            logger.error("getServiceTicket fail! service={}", service, ex);
            throw new SSOClientException("無法取得service ticket!", ex);
        } finally {
            method.releaseConnection();
        }
    }

    public <T> T executeGetService(String service, Class<T> clazz) throws SSOClientException {
        return executeGetService(service, null, clazz, true);
    }

    public <T> T executeGetService(String service, Map<String, String> query, Class<T> clazz) throws SSOClientException {
        return executeGetService(service, query, clazz, true);
    }

    public <T> T executeGetService(String service, Class<T> clazz, boolean ssoProtected) throws SSOClientException {
        return executeGetService(service, null, clazz, ssoProtected);
    }

    public <T> T executeGetService(String service, Map<String, String> query, Class<T> clazz, boolean ssoProtected) throws SSOClientException {
        try {
            return _executeGetService(service, query, clazz, ssoProtected);
        } catch (SSOClientExpiredException ex) {
            logger.warn("tgt expired! tgtTimestamp is {}.", tgtTimestamp);
            getTicketGrantingTicket();
            return _executeGetService(service, query, clazz, ssoProtected);
        }
    }

    public <T> T executePostService(String service, Map<String, String> form, Class<T> clazz) throws SSOClientException {
        return executePostService(service, null, form, clazz, true);
    }

    public <T> T executePostService(String service, Map<String, String> form, Class<T> clazz, boolean ssoProtected) throws SSOClientException {
        return executePostService(service, null, form, clazz, ssoProtected);
    }

    public <T> T executePostService(String service, Map<String, String> query, Map<String, String> form, Class<T> clazz) throws SSOClientException {
        return executePostService(service, query, form, clazz, true);
    }

    public <T> T executePostService(String service, Map<String, String> query, Map<String, String> form, Class<T> clazz, boolean ssoProtected) throws SSOClientException {
        try {
            return _executePostService(service, query, form, clazz, ssoProtected);
        } catch (SSOClientExpiredException ex) {
            logger.warn("tgt expired! tgtTimestamp is {}.", tgtTimestamp);
            getTicketGrantingTicket();
            return _executePostService(service, query, form, clazz, ssoProtected);
        }
    }
    
    public <T> T executePostJsonService(String service, Map<String, String> query, Object pojo, Class<T> clazz, boolean ssoProtected) throws SSOClientException {
        try {
            return _executePostJsonService(service, query, pojo, clazz, ssoProtected);
        } catch (SSOClientExpiredException ex) {
            logger.warn("tgt expired! tgtTimestamp is {}.", tgtTimestamp);
            getTicketGrantingTicket();
            return _executePostJsonService(service, query, pojo, clazz, ssoProtected);
        }
    }

    private <T> T _executeGetService(String service, Map<String, String> query, Class<T> clazz, boolean ssoProtected) throws SSOClientException {
        long beginTime = System.currentTimeMillis();
        String serviceUrl = createServiceUrl(service, query, ssoProtected);
        GetMethod method = new GetMethod(serviceUrl);
        method.setRequestHeader("Content-Type", "application/json; charset=utf-8");
        try {
            int status = httpClient.executeMethod(HostConfiguration.ANY_HOST_CONFIGURATION, method, new HttpState());
            long endTime = System.currentTimeMillis();
            long difference = endTime - beginTime;
            logger.info("executeGetService takes {} millis. ({})", difference, service);
            String output = streamToString(method.getResponseBodyAsStream(), "UTF-8");
            if (200 == status) {
                if (output.contains("/cas-server/login")) { // SSO
                    throw new SSOClientExpiredException(); // tgt expired?
                }
                return fromJson(output, clazz);
            }
            logger.error("executeGetService fail! status={}, service={}", status, service);
            if (output.length() > 100) {
                output = output.substring(0, 100) + "...";
            }
            System.out.println(output);
            throw new SSOClientException("執行失敗! 狀態碼:" + status + ", " + output, status);
        } catch (IOException ex) {
            logger.error("executeGetService IOException! service={}", service, ex);
            throw new SSOClientException("執行失敗!", ex);
        }
    }

    private <T> T _executePostService(String service, Map<String, String> query, Map<String, String> form, Class<T> clazz, boolean ssoProtected) throws SSOClientException {
        long beginTime = System.currentTimeMillis();
        String serviceUrl = createServiceUrl(service, query, ssoProtected);
        PostMethod method = new PostMethod(serviceUrl);
        if (form != null && !form.isEmpty()) {
            for (Map.Entry<String, String> entry : form.entrySet()) {
                method.addParameter(entry.getKey(), entry.getValue());
            }
        }
        method.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
        try {
            int status = httpClient.executeMethod(HostConfiguration.ANY_HOST_CONFIGURATION, method, new HttpState());
            long endTime = System.currentTimeMillis();
            long difference = endTime - beginTime;
            logger.info("executePostService takes {} millis. ({})", difference, service);
            String output = streamToString(method.getResponseBodyAsStream(), "UTF-8");
            if (200 == status) {
                if (output.contains("/cas-server/login")) { // SSO
                    throw new SSOClientExpiredException(); // tgt expired?
                }
                return fromJson(output, clazz);
            }
            logger.error("executePostService fail! status={}, service={}", status, service);
            if (output.length() > 100) {
                output = output.substring(0, 100) + "...";
            }
            throw new SSOClientException("執行失敗! 狀態碼:" + status + ", " + output, status);
        } catch (IOException ex) {
            logger.error("executePostService IOException! service={}", service, ex);
            throw new SSOClientException("執行失敗!", ex);
        }
    }

    private <T> T _executePostJsonService(String service, Map<String, String> query, Object pojo, Class<T> clazz, boolean ssoProtected) throws SSOClientException {
        long beginTime = System.currentTimeMillis();
        String serviceUrl = createServiceUrl(service, query, ssoProtected);
        PostMethod method = new PostMethod(serviceUrl);
        try {
            StringRequestEntity requestEntity = new StringRequestEntity(toJson(pojo), "application/json", "UTF-8");
            method.setRequestEntity(requestEntity);
            method.setRequestHeader("Content-Type", "application/json; charset=utf-8");
            int status = httpClient.executeMethod(HostConfiguration.ANY_HOST_CONFIGURATION, method, new HttpState());
            long endTime = System.currentTimeMillis();
            long difference = endTime - beginTime;
            logger.info("executePostJsonService takes {} millis. ({})", difference, service);
            String output = streamToString(method.getResponseBodyAsStream(), "UTF-8");
            if (200 == status) {
                if (output.contains("/cas-server/login")) { // SSO
                    throw new SSOClientExpiredException(); // tgt expired?
                }
                return fromJson(output, clazz);
            }
            logger.error("executePostJsonService fail! status={}, service={}", status, service);
            if (output.length() > 100) {
                output = output.substring(0, 100) + "...";
            }
            throw new SSOClientException("執行失敗! 狀態碼:" + status + ", " + output, status);
        } catch (IOException ex) {
            logger.error("executePostJsonService IOException! service={}", service, ex);
            throw new SSOClientException("執行失敗!", ex);
        }
    }

    private String createServiceUrl(String service, Map<String, String> query, boolean ssoProtected) throws SSOClientException {
        StringBuilder sb = new StringBuilder(service);
        boolean qmark = service.contains("?");
        if (query != null && !query.isEmpty()) {
            for (Map.Entry<String, String> entry : query.entrySet()) {
                if (qmark) {
                    sb.append("&");
                } else {
                    sb.append("?");
                    qmark = true;
                }
                String key = entry.getKey();
                String value = entry.getValue();
                if (null == value) {
                    value = "";
                }
                try {
                    sb.append(key)
                            .append("=")
                            .append(URLEncoder.encode(value, "UTF-8"));
                } catch (UnsupportedEncodingException ex) {
                    throw new SSOClientException(ex);
                }
            }
        }
        if (ssoProtected && null == tgt) {
            getTicketGrantingTicket();
        }
        if (ssoProtected) {
            String scTicket = getServiceTicket(sb.toString());
            sb.append(qmark ? '&' : '?').append("ticket=").append(scTicket);
        }
        return sb.toString();
    }

    public <T> T fromJson(String json, Class<T> clazz) throws SSOClientException {
        try {
            return clazz == String.class ? (T) json : mapper.readValue(json, clazz);
        } catch (IOException ex) {
            throw new SSOClientException("JSON轉換失敗!", ex);
        }
    }

    public String toJson(Object obj) throws SSOClientException {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException ex) {
            throw new SSOClientException("JSON轉換失敗!", ex);
        }
    }

    private static String streamToString(InputStream is, String encoding) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = is.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toString(encoding);
    }

    // getter, setter
    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTgt() {
        return tgt;
    }

    public void setTgt(String tgt) {
        this.tgt = tgt;
    }

    public long getTgtTimestamp() {
        return tgtTimestamp;
    }

    public void setTgtTimestamp(long tgtTimestamp) {
        this.tgtTimestamp = tgtTimestamp;
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public ObjectMapper getMapper() {
        return mapper;
    }

    public void setMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

}
