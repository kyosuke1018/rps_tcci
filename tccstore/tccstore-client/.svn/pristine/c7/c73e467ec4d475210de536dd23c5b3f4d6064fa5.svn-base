/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcci.tccstore.model.ModelConstant;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author Jimmy.Lee
 */
public class SSOClientImpl implements SSOClient {

    private final static String PARAM_CLIENT_KEY = "Client-Key";
    private final static String CLIENT_KEY = "550e8400e29b41d4a716446655440000";
    
    private String ssoTicketUrl = "http://192.168.204.50/ecsso/v1/tickets";
    private String serviceBaseUrl = "http://localhost/tccstore/service";
    private final CloseableHttpClient httpclient = HttpClients.createDefault();
    private final ObjectMapper mapper = new ObjectMapper().setDateFormat(new SimpleDateFormat(ModelConstant.ISO8601Format))
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    public SSOClientImpl() {
    }

    public SSOClientImpl(String ssoTickerUrl, String serviceBaseUrl) {
        this.ssoTicketUrl = ssoTickerUrl;
        this.serviceBaseUrl = serviceBaseUrl;
    }

    @Override
    public String getTicketGrantingTicket(String username, String password) throws SSOClientException {
        HttpPost httpPost = new HttpPost(ssoTicketUrl);
        CloseableHttpResponse response = null;
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("username", username));
        nvps.add(new BasicNameValuePair("password", password));
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
            httpPost.setHeader(PARAM_CLIENT_KEY, CLIENT_KEY);
            response = httpclient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (201 == statusCode) {
                String output = EntityUtils.toString(response.getEntity());
                Matcher matcher = Pattern.compile(".*action=\".*/(.*?)\".*")
                        .matcher(output);
                if (matcher.matches()) {
                    return matcher.group(1);
                }
            }
            throw new SSOClientException("getTicketGrantingTicket error! " + response.getStatusLine().toString(), statusCode);
        } catch (UnsupportedEncodingException ex) {
            throw new SSOClientException(ex);
        } catch (IOException ex) {
            throw new SSOClientException(ex);
        } finally {
            if (null != response) {
                try {
                    response.close();
                } catch (IOException ex) {
                }
            }
        }
    }

    @Override
    public String getServiceTicket(String service, String tgt) throws SSOClientException {
        HttpPost httpPost = new HttpPost(ssoTicketUrl + "/" + tgt);
        CloseableHttpResponse response = null;
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("service", serviceBaseUrl + service));
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
            httpPost.setHeader(PARAM_CLIENT_KEY, CLIENT_KEY);
            response = httpclient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (200 == statusCode) {
                return EntityUtils.toString(response.getEntity(), "UTF-8");
            }
            throw new SSOClientException("getServiceTicket error! " + response.getStatusLine().toString(), statusCode);
        } catch (UnsupportedEncodingException ex) {
            throw new SSOClientException(ex);
        } catch (IOException ex) {
            throw new SSOClientException(ex);
        } finally {
            if (null != response) {
                try {
                    response.close();
                } catch (IOException ex) {
                }
            }
        }
    }

    @Override
    public <T> T get(String service, Class<T> clazz, String st) throws SSOClientException {
        String serviceUrl = st==null ? (serviceBaseUrl + service) :
                service.contains("?") ? (serviceBaseUrl + service + "&ticket=" + st) :
                (serviceBaseUrl + service + "?ticket=" + st);
        HttpGet httpGet = new HttpGet(serviceUrl);
        CloseableHttpResponse response = null;
        try {
            httpGet.setHeader(PARAM_CLIENT_KEY, CLIENT_KEY);
            response = httpclient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (200 == statusCode) {
                String json = EntityUtils.toString(response.getEntity(), "UTF-8");
                // 認證失敗sso會導回login, statuc code是200
                if (json.contains("/login")) {
                    throw new SSOClientException("login required!", 401); // Unauthorized
                }
                return fromJson(json, clazz);
            }
            String errorMessage = EntityUtils.toString(response.getEntity(), "UTF-8");
            throw new SSOClientException(errorMessage, statusCode);
        } catch (IOException ex) {
            throw new SSOClientException(ex);
        } finally {
            if (null != response) {
                try {
                    response.close();
                } catch (IOException ex) {
                }
            }
        }
    }

    @Override
    public <T> T postForm(String service, Class<T> clazz, Map<String, String> form, String st) throws SSOClientException {
        String serviceUrl = st==null ? (serviceBaseUrl + service) :
                service.contains("?") ? (serviceBaseUrl + service + "&ticket=" + st) :
                (serviceBaseUrl + service + "?ticket=" + st);
        List<NameValuePair> nvps = new ArrayList<>();
        for (Map.Entry<String, String> entry : form.entrySet()) {
            nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        HttpPost httpPost = new HttpPost(serviceUrl);
        CloseableHttpResponse response = null;
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
            httpPost.setHeader(PARAM_CLIENT_KEY, CLIENT_KEY);
            response = httpclient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (200 == statusCode) {
                String json = EntityUtils.toString(response.getEntity(), "UTF-8");
                // 認證失敗sso會導回login, statuc code是200
                if (json.contains("/login")) {
                    throw new SSOClientException("login required!", 401); // Unauthorized
                }
                return fromJson(json, clazz);
            }
            String errorMessage = EntityUtils.toString(response.getEntity(), "UTF-8");
            throw new SSOClientException(errorMessage, statusCode);
        } catch (UnsupportedEncodingException ex) {
            throw new SSOClientException(ex);
        } catch (IOException ex) {
            throw new SSOClientException(ex);
        } finally {
            if (null != response) {
                try {
                    response.close();
                } catch (IOException ex) {
                }
            }
        }
    }

    @Override
    public <T> T postJson(String service, Class<T> clazz, Object model, String st) throws SSOClientException {
        String serviceUrl = st==null ? (serviceBaseUrl + service) :
                service.contains("?") ? (serviceBaseUrl + service + "&ticket=" + st) :
                (serviceBaseUrl + service + "?ticket=" + st);
        HttpPost httpPost = new HttpPost(serviceUrl);
        CloseableHttpResponse response = null;
        try {
            String jsonString = toJson(model);
            // System.out.println(jsonString);
            StringEntity postingString = new StringEntity(jsonString, "UTF-8");
            httpPost.setHeader("Content-type", "application/json; charset=utf-8");
            httpPost.setEntity(postingString);
            httpPost.setHeader(PARAM_CLIENT_KEY, CLIENT_KEY);
            response = httpclient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (200 == statusCode) {
                String json = EntityUtils.toString(response.getEntity(), "UTF-8");
                // 認證失敗sso會導回login, statuc code是200
                if (json.contains("/login")) {
                    throw new SSOClientException("login required!", 401); // Unauthorized
                }
                return fromJson(json, clazz);
            }
            String errorMessage = EntityUtils.toString(response.getEntity(), "UTF-8");
            throw new SSOClientException(errorMessage, statusCode);
        } catch (UnsupportedEncodingException ex) {
            throw new SSOClientException(ex);
        } catch (IOException ex) {
            throw new SSOClientException(ex);
        } finally {
            if (null != response) {
                try {
                    response.close();
                } catch (IOException ex) {
                }
            }
        }
    }
    
    @Override
    public <T> T fromJson(String json, Class<T> clazz) throws SSOClientException {
        try {
            return clazz==String.class ? (T) json : mapper.readValue(json, clazz);
        } catch (IOException ex) {
            throw new SSOClientException(ex);
        }
    }
    
    @Override
    public String toJson(Object obj) throws SSOClientException {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException ex) {
            throw new SSOClientException(ex);
        }
    }
    
}
