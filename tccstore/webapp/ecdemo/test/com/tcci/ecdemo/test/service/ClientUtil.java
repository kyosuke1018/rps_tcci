/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ecdemo.test.service;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jimmy.Lee
 */
public class ClientUtil {

    private final static Logger logger = LoggerFactory.getLogger(ClientUtil.class);

    private final static String SERVICE_URI = "http://localhost:8080/ecdemo/service";

    public static <T> T get(Class<T> clazz, String path, String authorization) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(SERVICE_URI).path(path);
        try {
            T t = target.request(MediaType.APPLICATION_JSON)
                    .header("Authorization", authorization)
                    .get(clazz);
            return t;
        } catch (WebApplicationException ex) {
            logger.error("get exception, {}", ex.getMessage());
            return null;
        }
    }

    public static <T> T post(Class<T> clazz, String path, String authorization, Form form) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(SERVICE_URI).path(path);
        try {
            T t = target.request(MediaType.APPLICATION_JSON)
                    .header("Authorization", authorization)
                    .post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE), clazz);
            return t;
        } catch (WebApplicationException ex) {
            logger.error("post exception, {}", ex.getMessage());
            return null;
        }
    }

}
