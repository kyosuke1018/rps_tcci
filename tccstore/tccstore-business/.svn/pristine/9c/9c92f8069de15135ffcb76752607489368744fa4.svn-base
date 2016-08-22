/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.facade.reward;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.tccstore.entity.EcMember;
import com.tcci.tccstore.entity.EcOrder;
import com.tcci.tccstore.entity.EcReward;
import com.tcci.tccstore.entity.EcRewardLog;
import com.tcci.tccstore.entity.EcRewardPK;
import com.tcci.tccstore.enums.RewardEventEnum;
import com.tcci.tccstore.enums.RewardTypeEnum;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

/**
 *
 * @author Jimmy.Lee
 */
@Named
@Stateless
public class EcRewardFacade {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    public EcReward find(EcMember ecMember, RewardTypeEnum rewardType) {
        return em.find(EcReward.class, new EcRewardPK(ecMember.getId(), rewardType.getValue()));
    }
    
    public List<EcReward> find(EcMember ecMember) {
        Query q = em.createNamedQuery("EcReward.findByMember");
        q.setParameter("ecMember", ecMember);
        return q.getResultList();
    }
    
    public List<EcReward> findAll() {
        Query q = em.createNamedQuery("EcReward.findAll");
        return q.getResultList();
    }
    
    public EcReward awardPoints(EcMember ecMember, RewardTypeEnum rewardType, int points, RewardEventEnum rewardEvent, String eventDetail, TcUser creator, EcOrder ecOrder) {
        Date eventTime = new Date();
        EcRewardPK pk = new EcRewardPK(ecMember.getId(), rewardType.getValue());
        // PESSIMISTIC_WRITE 同一個transaction 會有問題, 已新增但find卻是null造成PK重複
        // EcReward reward = em.find(EcReward.class, pk, LockModeType.PESSIMISTIC_WRITE);
        EcReward reward = em.find(EcReward.class, pk);
        int pointBalance = 0;
        if (null == reward) {
            pointBalance = points;
            reward = new EcReward(pk, ecMember);
            reward.setModifytime(eventTime);
            reward.setPoints(pointBalance);
            em.persist(reward);
        } else {
            pointBalance = reward.getPoints() + points;
            reward.setModifytime(eventTime);
            reward.setPoints(pointBalance);
            em.merge(reward);
        }
        EcRewardLog rewardLog = new EcRewardLog(ecMember, rewardType.getValue(), points, pointBalance, rewardEvent.name(), eventDetail, creator, ecOrder);
        rewardLog.setEventTime(eventTime);
        em.persist(rewardLog);
        return reward;
    }
    
    public List<EcRewardLog> findLog(EcMember ecMember, RewardTypeEnum rewardType, String yearMonth) {
        //Query q = em.createNamedQuery("EcRewardLog.findByMemberType");
        String sql = "SELECT e FROM EcRewardLog e"
                + " WHERE e.ecMember=:ecMember"
                + " AND e.type=:type"
                + " AND EXTRACT(YEAR FROM e.eventTime)=:year"
                + " AND EXTRACT(MONTH FROM e.eventTime)=:month"
                + " ORDER BY e.eventTime DESC";
        Query q = em.createQuery(sql);
        q.setParameter("ecMember", ecMember);
        q.setParameter("type", rewardType.getValue());
        q.setParameter("year", yearMonth.substring(0, 4));
        q.setParameter("month", yearMonth.substring(4, 6));
        return q.getResultList();
    }
    
    public void bonusClean(TcUser operator) {
        Date eventTime = new Date();
        // 不為0的紅利
        Query q = em.createQuery("SELECT e FROM EcReward e WHERE e.points<>0 AND e.ecRewardPK.type=1");
        List<EcReward> list = q.getResultList();
        for (EcReward e : list) {
            int balance = e.getPoints();
            e.setPoints(0);
            em.merge(e);
            String eventDetail = "紅利結清歸零(原紅利餘額:" + balance + ")";
            EcRewardLog rewardLog = new EcRewardLog(e.getEcMember(), RewardTypeEnum.BONUS.getValue(), 0, 0, RewardEventEnum.BONUS_CLEAN.name(), eventDetail, operator, null);
            rewardLog.setEventTime(eventTime);
            em.persist(rewardLog);
        }
    }
    
    public List<EcRewardLog> query(RewardFilter filter) {
        StringBuilder sql = new StringBuilder("SELECT e FROM EcRewardLog e WHERE 1=1");
        Map<String, Object> params = new HashMap<>();
        if (filter.getEcMember() != null) {
            sql.append(" AND e.ecMember=:ecMember");
            params.put("ecMember", filter.getEcMember());
        }
        if (filter.getRewardType() != null) {
            sql.append(" AND e.type=:type");
            params.put("type", filter.getRewardType().getValue());
        }
        if (filter.getEventType() != null) {
            sql.append(" AND e.eventType=:eventType");
            params.put("eventType", filter.getEventType());
        }
        if (filter.getDateStart() != null) {
            sql.append(" AND e.eventTime>=:dateStart");
            params.put("dateStart", filter.getDateStart());
        }
        if (filter.getDateEnd() != null) {
            Calendar c = Calendar.getInstance();
            c.setTime(filter.getDateEnd());
            c.add(Calendar.DATE, 1);
            sql.append(" AND e.eventTime<:dateEnd");
            params.put("dateEnd", c.getTime());
        }
        sql.append(" ORDER BY e.eventTime DESC");
        System.out.println(sql.toString());
        Query q = em.createQuery(sql.toString());
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (key.matches("dateStart|dateEnd")) {
                q.setParameter(key, (Date) value, TemporalType.DATE);
            } else {
                q.setParameter(key, value);
            }
        }
        return q.getResultList();
    }
    
}
