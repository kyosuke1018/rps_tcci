/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade.push;

import com.tcci.cm.util.SQLUtils;
import com.tcci.ec.entity.EcMember;
import com.tcci.ec.entity.EcPushLog;
import com.tcci.ec.vo.Member;
import com.tcci.fc.util.DateUtils;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Kyle.Cheng
 */
@Stateless
public class EcPushLogFacade {
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    public void save(EcPushLog pushLog) {
        if (null == pushLog.getId()) {
            em.persist(pushLog);
        } else {
            em.merge(pushLog);
        }
    }
    
    public BigDecimal countBySms(EcMember member){
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ID FROM EC_PUSH_LOG WHERE SUCCESS=1 AND CATEGORY=#CATEGORY ");
        sql.append("AND TITLE=#TITLE AND AUDIENCE=#AUDIENCE AND CREATETIME>=#TODAY ");
        params.put("CATEGORY", "sms");
        params.put("TITLE", "forgetPassword");
        params.put("AUDIENCE", member.getLoginAccount());
        params.put("TODAY", DateUtils.getToday());
        
        return SQLUtils.countBySQL(em, sql.toString(), params);
    }
}
