/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.test;

import com.tcci.tccstore.client.SSOClient;
import com.tcci.tccstore.client.SSOClientException;
import com.tcci.tccstore.client.SSOClientImpl;
import java.util.Map;

/**
 *
 * @author Jimmy.Lee
 */
public class TestBase {

    protected String tgt;
    protected SSOClient ssoClient = new SSOClientImpl();

    protected void login(String username, String password) throws SSOClientException {
        tgt = ssoClient.getTicketGrantingTicket(username, password);
    }

    protected <T> T get(String service, Class<T> clazz) throws SSOClientException {
        String st = tgt == null ? null : ssoClient.getServiceTicket(service, tgt);
        return ssoClient.get(service, clazz, st);
    }

    protected <T> T postJson(String service, Class<T> clazz, Object model) throws SSOClientException {
        String st = tgt == null ? null : ssoClient.getServiceTicket(service, tgt);
        return ssoClient.postJson(service, clazz, model, st);
    }

    protected <T> T postForm(String service, Class<T> clazz, Map<String, String> form) throws SSOClientException {
        String st = tgt == null ? null : ssoClient.getServiceTicket(service, tgt);
        return ssoClient.postForm(service, clazz, form, st);
    }
    
    protected <T> T fromJson(String json, Class<T> clazz) throws SSOClientException {
        return ssoClient.fromJson(json, clazz);
    }
    
    protected String toJson(Object obj) throws SSOClientException {
        return ssoClient.toJson(obj);
    }

}
