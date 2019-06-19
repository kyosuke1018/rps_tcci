/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade;

import com.tcci.ec.entity.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Kyle.Cheng
 */
@Path("service")
@Singleton
public class ServiceFacade {
    private static final Logger logger = LoggerFactory.getLogger(ServiceFacade.class);

    private Map<String, String> mapService;
    
    @PersistenceContext(unitName = "Model")
    private EntityManager em;
    
    @PostConstruct
    private void init() {
        mapService = new HashMap<>();
        syncData();
    }
    
    @GET
    @Path("reload")
    @Produces("text/plain")
    public String reload() {
        syncData();
        return "OK";
    }
    
    public void syncData() {
        Query q = em.createNamedQuery("Service.findAllActive");
        List<Service> serviceList = q.getResultList();
        mapService.clear();
        for (Service myService : serviceList) {
            mapService.put(myService.getService(), myService.getServiceUrl());
        }
    }
    
     public Service find(String service) {
        Query q = em.createNamedQuery("Service.findByService");
        q.setParameter("service", service);
        List<Service> result = q.getResultList();
        return result.isEmpty() ? null : result.get(0);
    }
    
    public List<Service> findAll() {
        Query q = em.createNamedQuery("Service.findAll");
        return q.getResultList();
    }
    
    public void save(Service myService) {
        if (myService.getId() == null) {
            em.persist(myService);
        } else {
            em.merge(myService);
        }
    }
    
    
    public String findServiceUrl(String service) {
        String serviceUrl = null;
        for (Map.Entry<String, String> entry : mapService.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (StringUtils.startsWith(service, key)) {
                serviceUrl = value + service.substring(key.length());
                break;
            }
        }
        return serviceUrl;
    }
}
