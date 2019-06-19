/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.facade.schedule;

import com.tcci.cm.facade.AbstractFacade;
import com.tcci.cm.model.LongOptionVO;
import com.tcci.ec.entity.EcOption;
import com.tcci.ec.entity.EcStore;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Jimmy.Lee
 */
@Stateless
public class TcScheduleFacade extends AbstractFacade<EcOption> {
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
       return em;
    }

    public TcScheduleFacade() {
        super(EcOption.class);
    }

    // 排程能否執行
    // true: name 要存在, 且 EXEC_TIME 是null 或 與現在時間差大於minMinutesDiff
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public boolean canExecute(String name, int minMinutesDiff) {
        String sql = "SELECT EXEC_TIME FROM TC_SCHEDULE WHERE NAME=#name AND ACTIVE=1 FOR UPDATE NOWAIT";
        Query q = em.createNativeQuery(sql);
        q.setParameter("name", name);
        Date now = new Date();
        try {
            Date execTime = (Date) q.getSingleResult();
            boolean canExcute = (null==execTime) ? true :
                    (now.getTime() - execTime.getTime()) > minMinutesDiff*60*1000L;// 未執行過或超過一分鐘，才執行
            if (canExcute) {
                sql = "UPDATE TC_SCHEDULE SET EXEC_TIME=#now WHERE NAME=#name";
                q = em.createNativeQuery(sql);
                q.setParameter("now", now);
                q.setParameter("name", name);
                q.executeUpdate();
                logger.warn("{} schedule canExecute true", name);
                return true;
            } else {
                logger.warn("{} schedule canExecute false, already executed at {}", name, execTime);
            }
        } catch (NoResultException ex) {
            logger.warn("{} schedule not active or not exist", name);
        } catch (Exception ex) {
            logger.error("{} schedule canExecute exception", name, ex);
        }
        return false;
    }
    
    public List<LongOptionVO> checkAlive(){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        List<LongOptionVO> list = this.selectBySql(LongOptionVO.class, sql.toString(), params);
        
        return list;
    }
}
