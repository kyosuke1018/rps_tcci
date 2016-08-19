/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.facade.article;

import com.tcci.tccstore.entity.EcArticle;
import com.tcci.tccstore.entity.EcMember;
import com.tcci.tccstore.enums.NotifyTypeEnum;
import com.tcci.tccstore.facade.member.EcMemberFacade;
import com.tcci.tccstore.facade.notify.EcNotifyFacade;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

/**
 *
 * @author Jimmy.Lee
 */
@Stateless
public class EcArticleFacade {
    
    @PersistenceContext(unitName = "Model")
    private EntityManager em;
    
    @EJB
    private EcMemberFacade ecMemberFacade;
    @EJB
    private EcNotifyFacade ecNotifyFacade;

    public EcArticle find(Long id) {
        return em.find(EcArticle.class, id);
    }
    
    public List<EcArticle> query(String title, Date startDate, Date endDate)  {
        String sql = "SELECT e FROM EcArticle e WHERE 1=1";
        if (title != null) {
            sql += " AND LOWER(e.title) like LOWER(:title)";
        }
        if (startDate != null) {
            sql += " AND e.createtime>=:startDate";
        }
        if (endDate != null) {
            sql += " AND e.createtime<=:endDate";
        }
        sql += " ORDER BY e.id DESC";
        Query q = em.createQuery(sql);
        if (title != null) {
            q.setParameter("title", "%" + title + "%");
        }
        if (startDate != null) {
            q.setParameter("startDate", startDate, TemporalType.DATE);
        }
        if (endDate != null) {
            q.setParameter("endDate", endDate, TemporalType.DATE);
        }
        return q.getResultList();
    }
    
    public void edit(EcArticle entity) {
        if (null == entity.getId()) {
            em.persist(entity);
        } else {
            em.merge(entity);
        }
    }
    
    public void saveAndPublish(EcArticle entity) {
        entity.setActive(true);
        entity.setPubdate(new Date());
        edit(entity);
        List<EcMember> members = ecMemberFacade.findAllActive();
        for (EcMember member : members) {
            ecNotifyFacade.createNotify(NotifyTypeEnum.ARTICLE_PUBLISH, entity.getTitle(), entity, member);
        }
    }
    
    //最新10筆
    public List<EcArticle> findActive() {
        return em.createNamedQuery("EcArticle.findActive").setMaxResults(10).getResultList();
    }

    public void remove(EcArticle entity) {
        em.remove(em.merge(entity));
    }
    
 }
