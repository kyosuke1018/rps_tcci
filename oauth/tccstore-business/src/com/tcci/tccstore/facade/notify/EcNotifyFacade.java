/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.facade.notify;

import com.tcci.fc.entity.essential.Persistable;
import com.tcci.tccstore.entity.EcMember;
import com.tcci.tccstore.entity.EcNotify;
import com.tcci.tccstore.entity.EcOrder;
import com.tcci.tccstore.entity.EcPartner;
import com.tcci.tccstore.enums.NotifyTypeEnum;
import com.tcci.tccstore.facade.AbstractFacade;
import java.util.Calendar;
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
    
    public EcNotify find(Long id) {
        return em.find(EcNotify.class, id);
    }
    
    public EcNotify createThenReturn(EcNotify notify) {
        em.persist(notify);
        return em.merge(notify);
    }
    
    public EcNotify editThenReturn(EcNotify notify) {
        return em.merge(notify);
    }
    
    public void removeNotify(EcNotify notify) {
        em.remove(em.merge(notify));
    }
    
    public EcNotify createNotify(NotifyTypeEnum notifyType,String subject, Persistable notifyObject, EcMember member) {
        EcNotify notify = new EcNotify();
        notify.setType(notifyType);
        notify.setSubject(subject);
        notify.setNotifyClassname(notifyObject.getClass().getCanonicalName());
        notify.setNotifyClassid(notifyObject.getId());
        notify.setReadCount(0);
//        if(notifyObject instanceof EcOrder) {
//            notify.setMemberId(((EcOrder)notifyObject).getMemberId());
//        }else if (notifyObject instanceof EcPartner) {
//            notify.setMemberId(((EcPartner)notifyObject).getOwner());
//        }
        notify.setMemberId(member);
        em.persist(notify);
        return em.merge(notify);
    }
    
    /*
    public List<EcNotify> findByCriteria(EcMember member, Boolean readFlag) {
        List<EcNotify> result = null;
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<EcNotify> cq = cb.createQuery(EcNotify.class);
        Root<EcNotify> root = cq.from(EcNotify.class);
        cq.select(root);
        List<Predicate> predicateList = new ArrayList();
        if (member != null) {
            predicateList.add(cb.equal(root.get("memberId"), member));
        }
        if (readFlag != null) {
            Predicate predicate;
            if (readFlag) {
                predicate = cb.gt(root.get("readCount").as(Integer.class), 0);
            } else {
                predicate = cb.equal(root.get("readCount").as(Integer.class), 0);
            }
            predicateList.add(predicate);
        }
        if (predicateList.size() > 0) {
            Predicate[] predicates = new Predicate[predicateList.size()];
            predicateList.toArray(predicates);
            cq.where(predicates);
        }
        cq.orderBy(cb.desc(root.get("createtime")));
        result = em.createQuery(cq).getResultList();
        return result;
        
    }
    */
    
    // 最近7天的的資料
    // readFlag: null->全部, true->已讀取的通知, false->未讀取的通知
    public List<EcNotify> findByCriteria(EcMember member, Boolean readFlag) {
        String sql = "SELECT e FROM EcNotify e WHERE e.memberId=:member AND e.createtime>=:createtime";
        if (readFlag != null) {
            if (readFlag) {
                sql += " AND e.readCount>0"; // 已讀取的通知
            } else {
                sql += " AND e.readCount=0"; // 未讀取的通知
            }
        }
        sql += " ORDER BY e.createtime DESC";
        Query q = em.createQuery(sql);
        Calendar createtime = Calendar.getInstance();
        createtime.add(Calendar.DATE, -6); // 含今天共7天
        q.setParameter("member", member);
        q.setParameter("createtime", createtime, TemporalType.DATE);
        return q.getResultList();
    }

}
