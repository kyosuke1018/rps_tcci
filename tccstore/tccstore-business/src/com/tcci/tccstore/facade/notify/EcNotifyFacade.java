/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.facade.notify;

import com.tcci.fc.entity.essential.Persistable;
import com.tcci.tccstore.entity.EcMember;
import com.tcci.tccstore.entity.EcNotify;
import com.tcci.tccstore.entity.EcNotifyMember;
import com.tcci.tccstore.entity.EcNotifyMemberPK;
import com.tcci.tccstore.enums.NotifyTypeEnum;
import com.tcci.tccstore.facade.AbstractFacade;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

/**
 *
 * @author Neo.Fu
 */
@Named
@Stateless
public class EcNotifyFacade extends AbstractFacade {
    
    @PersistenceContext(unitName = "Model")
    private EntityManager em;
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public EcNotifyFacade() {
        super(EcNotify.class);
    }
    
    public void createNotify(NotifyTypeEnum notifyType,String subject, Persistable notifyObject, EcMember member) {
        List<EcMember> members = new ArrayList<>();
        members.add(member);
        createNotify(notifyType, subject, notifyObject, members);
    }
    
    public void createNotify(NotifyTypeEnum notifyType,String subject, Persistable notifyObject, List<EcMember> members) {
        EcNotify notify = new EcNotify();
        notify.setType(notifyType);
        notify.setSubject(subject);
        notify.setNotifyClassname(notifyObject.getClass().getCanonicalName());
        notify.setNotifyClassid(notifyObject.getId());
        notify.setCreatetime(new Date());
        em.persist(notify);
        for (EcMember member : members) {
            EcNotifyMemberPK pk = new EcNotifyMemberPK(notify.getId(), member.getId());
            EcNotifyMember notifyMember = new EcNotifyMember(pk);
            notifyMember.setEcNotify(notify);
            notifyMember.setEcMember(member);
            em.persist(notifyMember);
        }
        
    }

    // 最近7天的通知
    public List<EcNotifyMember> findLastDays(EcMember member) {
        Query q = em.createNamedQuery("EcNotifyMember.findLastDays");
        Calendar createtime = Calendar.getInstance();
        createtime.add(Calendar.DATE, -6); // 含今天共7天
        q.setParameter("member", member);
        q.setParameter("createtime", createtime, TemporalType.DATE);
        return q.getResultList();
    }
    
    public int findUnreadCount(EcMember member) {
        List<EcNotifyMember> notifyMembers = findLastDays(member);
        int count = 0;
        for (EcNotifyMember notifyMember : notifyMembers) {
            if (notifyMember.getReadTimestamp() == null) {
                count++;
            }
        }
        return count;
    }
    
    public void updateReadTimestamp(List<EcNotifyMember> notifyMembers) {
        Date now = new Date();
        for (EcNotifyMember notifyMember : notifyMembers) {
            if (notifyMember.getReadTimestamp() == null) {
                notifyMember.setReadTimestamp(now);
                em.merge(notifyMember);
            }
        }
    }

}
