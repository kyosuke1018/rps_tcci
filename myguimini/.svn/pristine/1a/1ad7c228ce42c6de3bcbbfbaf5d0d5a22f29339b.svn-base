/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.myguimini.facade;

import com.tcci.myguimini.entity.MyService;
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
 * @author Jimmy.Lee
 */
@Singleton
@Path("myservice")
public class MyServiceFacade {

    private static final Logger logger = LoggerFactory.getLogger(MyServiceFacade.class);

    private Map<String, String> mapService;

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @PostConstruct
    private void init() {
        mapService = new HashMap<String, String>();
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
        Query q = em.createNamedQuery("MyService.findAllActive");
        List<MyService> serviceList = q.getResultList();
        mapService.clear();
        for (MyService myService : serviceList) {
            mapService.put(myService.getService(), myService.getServiceUrl());
        }
    }

    public MyService find(String service) {
        Query q = em.createNamedQuery("MyService.find");
        q.setParameter("service", service);
        List<MyService> result = q.getResultList();
        return result.isEmpty() ? null : result.get(0);
    }

    public List<MyService> findAll() {
        Query q = em.createNamedQuery("MyService.findAll");
        return q.getResultList();
    }

    public void save(MyService myService) {
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
