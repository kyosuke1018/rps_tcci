/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.storegateway.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcci.tccstore.model.ModelConstant;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Map;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author Jimmy.Lee
 */
public class TestBase {

    protected final static String gatewayUrl = "http://localhost:8080/storegateway";
    // protected final static String gatewayUrl = "http://192.168.203.45/storegateway";
    // protected final static String gatewayUrl = "http://192.168.203.50/storegateway";
    // protected final static String gatewayUrl = "http://tccstore.taiwancement.com/storegateway";
    protected final static String PARAM_CLIENT_KEY = "Client-Key";
    protected final static String CLIENT_KEY = "550e8400e29b41d4a716446655440000";

    protected String tgt;
    protected HttpClient httpClient;
    private final ObjectMapper mapper = new ObjectMapper().setDateFormat(new SimpleDateFormat(ModelConstant.ISO8601Format))
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);
            // .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public TestBase() {
        httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());
        httpClient.getParams().setBooleanParameter(HttpClientParams.USE_EXPECT_CONTINUE, false);
        httpClient.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
        // httpClient.getParams().setParameter(HttpClientParams.ALLOW_CIRCULAR_REDIRECTS, true);
        httpClient.getParams().setParameter("http.protocol.content-charset", "UTF-8");
    }

    protected void login(String username, String password) throws SSOClientException {
        PostMethod method = new PostMethod(gatewayUrl + "/login");
        method.addParameter("username", username);
        method.addParameter("password", password);
        try {
            method.setRequestHeader(PARAM_CLIENT_KEY, CLIENT_KEY);
            httpClient.executeMethod(method);
            int statusCode = method.getStatusCode();
            String output = IOUtils.toString(method.getResponseBodyAsStream(), "UTF-8");
            if (201 == statusCode) {
                tgt = output;
            } else {
                throw new SSOClientException(output, statusCode);
            }
        } catch (IOException ex) {
            throw new SSOClientException(ex);
        }
    }
    
    protected <T> T get(String service, Class<T> clazz) throws SSOClientException {
        StringBuilder serviceUrl = new StringBuilder(gatewayUrl); //gatewayUrl + service;
        serviceUrl.append("/tccstore").append(service);
        GetMethod method = new GetMethod(serviceUrl.toString());
        try {
            if (tgt != null) {
                method.setRequestHeader("TGT", tgt);
            }
            method.setRequestHeader("Content-type", "application/json; charset=utf-8");
            method.setRequestHeader(PARAM_CLIENT_KEY, CLIENT_KEY);
            httpClient.executeMethod(method);
            int statusCode = method.getStatusCode();
            String output = IOUtils.toString(method.getResponseBodyAsStream(), "UTF-8");
            if (200 == statusCode) {
                if (output.contains("/login")) {
                    throw new SSOClientException("login required!", 401); // Unauthorized
                }
                return fromJson(output, clazz);
            }
            throw new SSOClientException(output, statusCode);
        } catch (IOException ex) {
            throw new SSOClientException(ex);
        }
    }
    
    protected <T> T postJson(String service, Class<T> clazz, Object model) throws SSOClientException {
        StringBuilder serviceUrl = new StringBuilder(gatewayUrl); //gatewayUrl + service;
        serviceUrl.append("/tccstore").append(service);
        PostMethod method = new PostMethod(serviceUrl.toString());
        try {
            String jsonString = toJson(model);
            if (tgt != null) {
                method.setRequestHeader("TGT", tgt);
            }
            method.setRequestHeader("Content-type", "application/json; charset=utf-8");
            method.setRequestHeader(PARAM_CLIENT_KEY, CLIENT_KEY);
            StringRequestEntity requestEntity = new StringRequestEntity(jsonString, "application/json", "UTF-8");
            method.setRequestEntity(requestEntity);
            httpClient.executeMethod(method);
            int statusCode = method.getStatusCode();
            String output = IOUtils.toString(method.getResponseBodyAsStream(), "UTF-8");
            if (200 == statusCode) {
                if (output.contains("/login")) {
                    throw new SSOClientException("login required!", 401); // Unauthorized
                }
                return fromJson(output, clazz);
            }
            throw new SSOClientException(output, statusCode);
        } catch (IOException ex) {
            throw new SSOClientException(ex);
        }
    }

    protected <T> T postForm(String service, Class<T> clazz, Map<String, String> form) throws SSOClientException {
        StringBuilder serviceUrl = new StringBuilder(gatewayUrl); //gatewayUrl + service;
        serviceUrl.append("/tccstore").append(service);
        PostMethod method = new PostMethod(serviceUrl.toString());
        try {
            for (Map.Entry<String, String> entry : form.entrySet()) {
                if (entry.getValue() != null) {
                    method.addParameter(entry.getKey(), entry.getValue());
                }
            }
            if (tgt != null) {
                method.setRequestHeader("TGT", tgt);
            }
            method.setRequestHeader(PARAM_CLIENT_KEY, CLIENT_KEY);
            httpClient.executeMethod(method);
            int statusCode = method.getStatusCode();
            String output = IOUtils.toString(method.getResponseBodyAsStream(), "UTF-8");
            if (200 == statusCode) {
                if (output.contains("/login")) {
                    throw new SSOClientException("login required!", 401); // Unauthorized
                }
                return fromJson(output, clazz);
            }
            throw new SSOClientException(output, statusCode);
        } catch (IOException ex) {
            throw new SSOClientException(ex);
        }
    }
    
    protected <T> T postParts(String service, Class<T> clazz, Part[] parts) throws SSOClientException {
        StringBuilder serviceUrl = new StringBuilder(gatewayUrl); //gatewayUrl + service;
        serviceUrl.append("/tccstore").append(service);
        PostMethod method = new PostMethod(serviceUrl.toString());
        try {
            method.setRequestEntity(new MultipartRequestEntity(parts, method.getParams()));
            if (tgt != null) {
                method.setRequestHeader("TGT", tgt);
            }
            method.setRequestHeader(PARAM_CLIENT_KEY, CLIENT_KEY);
            httpClient.executeMethod(method);
            int statusCode = method.getStatusCode();
            String output = IOUtils.toString(method.getResponseBodyAsStream(), "UTF-8");
            if (200 == statusCode) {
                if (output.contains("/login")) {
                    throw new SSOClientException("login required!", 401); // Unauthorized
                }
                return fromJson(output, clazz);
            }
            throw new SSOClientException(output, statusCode);
        } catch (IOException ex) {
            throw new SSOClientException(ex);
        }
    }

    public <T> T fromJson(String json, Class<T> clazz) throws SSOClientException {
        try {
            return clazz==String.class ? (T) json : mapper.readValue(json, clazz);
        } catch (IOException ex) {
            throw new SSOClientException(ex);
        }
    }
    
    public String toJson(Object obj) throws SSOClientException {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException ex) {
            throw new SSOClientException(ex);
        }
    }
    
    protected <T> T executeGet(String service, Class<T> clazz, String description) {
        System.out.println(service + " (" + description + ")");
        try {
            T result = this.get(service, clazz);
            System.out.println("output:" + this.toJson(result));
            return result;
        } catch (SSOClientException ex) {
            System.out.println("exception: " + ex.getMessage());
        }
        System.out.println("------");
        return null;
    }
    
    protected <T> T executeForm(String service, Map<String, String> form, Class<T> clazz, String description) {
        System.out.println(service + " (" + description + ")");
        try {
            System.out.println("input: " + this.toJson(form));
            T result = this.postForm(service, clazz, form);
            System.out.println("output:" + this.toJson(result));
            return result;
        } catch (SSOClientException ex) {
            System.out.println("exception: " + ex.getMessage());
        }
        System.out.println("------");
        return null;
    }

    protected <T> T executeJson(String service, Class<T> clazz, Object model, String description) {
        System.out.println(service + " (" + description + ")");
        try {
            System.out.println("input: " + this.toJson(model));
            T result = this.postJson(service, clazz, model);
            System.out.println("output:" + this.toJson(result));
            return result;
        } catch (SSOClientException ex) {
            System.out.println("exception: " + ex.getMessage());
        }
        System.out.println("------");
        return null;
    }
    
    protected <T> T executeParts(String service, Class<T> clazz, Part[] parts, String description) {
        System.out.println(service + " (" + description + ")");
        try {
            T result = this.postParts(service, clazz, parts);
            System.out.println("output:" + this.toJson(result));
            return result;
        } catch (SSOClientException ex) {
            System.out.println("exception: " + ex.getMessage());
        }
        System.out.println("------");
        return null;
    }

}
