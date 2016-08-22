/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.facade;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.AbstractFacade;
import com.tcci.sksp.entity.SkAccessLog;
import com.tcci.sksp.entity.enums.AccessLogActionEnum;
import java.util.Date;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Jason.Yu
 */
@Stateless
public class SkAccessLogFacade extends AbstractFacade<SkAccessLog> {
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public SkAccessLogFacade() {
        super(SkAccessLog.class);
    }
    
    public void createLog(TcUser user, AccessLogActionEnum actionEnum) {
        SkAccessLog log = new SkAccessLog();
        log.setActionName(actionEnum);
        log.setUserId(user);
        log.setCreatetimestamp(new Date());
        create(log);
    }
}
