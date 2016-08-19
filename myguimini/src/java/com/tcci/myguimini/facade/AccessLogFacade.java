/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.myguimini.facade;

import com.tcci.myguimini.entity.MyLoginLog;
import com.tcci.myguimini.entity.MyServiceLog;
import java.util.Date;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Jimmy.Lee
 */
@Stateless
public class AccessLogFacade {
    
    @PersistenceContext(unitName="Model")
    private EntityManager em;
    
    public void addLoginLog(String username, String tgt) {
        MyLoginLog log = new MyLoginLog(username, tgt, new Date());
        em.persist(log);
    }
    
    public void addServiceLog(String service, String tgt) {
        if (service.length()>255) {
            service = service.substring(0, 255);
        }
        MyServiceLog log = new MyServiceLog(service, tgt, new Date());
        em.persist(log);
    }
}
