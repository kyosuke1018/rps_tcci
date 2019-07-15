/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.tcci.security.test;

import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.sse.SseEventSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author penpl
 */
//@Singleton
//@Startup
public class SSEClient {
    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());
    
    Client sseClient;
    WebTarget target;
    SseEventSource eventSource;
    @Resource TimerService timerService;

    public ArrayList<String> listUsers;

    @PostConstruct
    public void init() {
        this.sseClient = ClientBuilder.newClient();
        this.target = this.sseClient.target("http://localhost:8080/JavaEESecSSO/resources/sse/items/events");

        timerService.createSingleActionTimer(5000, new TimerConfig());
        LOG.info("SSE client timer created");

        // Server side event source 
        eventSource = SseEventSource.target(target).build();
        LOG.info("SSE Event source created........");
    }

    public void addUser(String username) {
        listUsers.add(username);
    }

    public void removeUser(String username) {
        listUsers.remove(username);
    }

    public ArrayList<String> getListUsers() {
        return listUsers;
    }

    @Timeout
    public void onTimeout() {
        try {
            LOG.info("onTimeout ...");
            
            eventSource.register((sseEvent) -> {
                    LOG.info("SSE event received ----- " + sseEvent.readData());
            }, (e) -> e.printStackTrace());

            eventSource.open();
        } catch (Exception e) {
            LOG.error("onTimeout Exception:\n", e);
        }
    }

    @PreDestroy
    public void close() {
        eventSource.close();
        LOG.info("Closed SSE Event source..");
        sseClient.close();
        LOG.info("Closed JAX-RS client..");
    }
}
