/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.facade.schedule;

import java.util.Date;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jimmy.Lee
 */
@Stateless
public class TcScheduleFacade {
    private final static Logger logger = LoggerFactory.getLogger(TcScheduleFacade.class);

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    // 排程能否執行
    // true: name 要存在, 且 EXEC_TIME 是null 或 與現在時間差大於minMinutesDiff
//    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public boolean canExecute(String name, int minMinutesDiff) {
//        String sql = "SELECT EXEC_TIME FROM TC_SCHEDULE WHERE NAME=#name AND ACTIVE=1 FOR UPDATE NOWAIT";
//        Query q = em.createNativeQuery(sql);
//        q.setParameter("name", name);
        Date now = new Date();
        try {
//            q.getResultList();
//            Date execTime = (Date) q.getSingleResult();
            Date execTime = findExecTime(name);
            boolean canExcute = (null==execTime) ? true :
                    (now.getTime() - execTime.getTime()) > minMinutesDiff*60*1000L;
            if (canExcute) {
//                sql = "UPDATE TC_SCHEDULE SET EXEC_TIME=#now WHERE NAME=#name";
//                q = em.createNativeQuery(sql);
//                q.setParameter("now", now);
//                q.setParameter("name", name);
//                q.executeUpdate();
                updateExecTime(name);
                logger.warn("{} schedule canExecute true", name);
                return true;
            } else {
                logger.warn("{} schedule canExecute false, already executed at {}", name, execTime);
            }
        } catch (NoResultException ex) {
            logger.warn("{} schedule not active or not exist", name);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("{} schedule canExecute exception", name, ex);
        }
        return false;
    }
    
    public Date findExecTime(String name){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT EXEC_TIME FROM TC_SCHEDULE ")
           .append("where NAME='").append(name).append("' ")
           .append("AND ACTIVE=1 FOR UPDATE NOWAIT \n");
        
        Query q = em.createNativeQuery(sql.toString());
        return (Date) q.getSingleResult();
    }
    
    public void updateExecTime(String name){
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE TC_SCHEDULE SET EXEC_TIME=SYSDATE ")
           .append("WHERE NAME='").append(name).append("' \n") ;  
        
        Query q = em.createNativeQuery(sql.toString());
        q.executeUpdate();
    }
}
