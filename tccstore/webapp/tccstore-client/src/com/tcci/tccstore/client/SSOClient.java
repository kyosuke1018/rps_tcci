/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.client;

import java.util.Map;

/**
 *
 * @author Jimmy.Lee
 */
public interface SSOClient {

    public String getTicketGrantingTicket(String username, String password) throws SSOClientException;

    public String getServiceTicket(String service, String tgt) throws SSOClientException;

    public <T> T get(String service, Class<T> clazz, String st) throws SSOClientException;

    public <T> T postForm(String service, Class<T> clazz, Map<String, String> form, String st) throws SSOClientException;
    
    public <T> T postJson(String service, Class<T> clazz, Object model, String st) throws SSOClientException;

    public <T> T fromJson(String json, Class<T> clazz) throws SSOClientException;
    
    public String toJson(Object obj) throws SSOClientException;

}
