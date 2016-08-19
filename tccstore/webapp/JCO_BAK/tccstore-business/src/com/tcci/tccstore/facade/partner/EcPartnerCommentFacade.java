/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.facade.partner;

import com.tcci.tccstore.entity.EcMember;
import com.tcci.tccstore.entity.EcPartner;
import com.tcci.tccstore.entity.EcPartnerComment;
import com.tcci.tccstore.enums.CommentStatusEnum;
import com.tcci.tccstore.facade.AbstractFacade;
import com.tcci.tccstore.facade.util.PartnerCommentFilter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author Neo.Fu
 */
@Named
@Stateless
public class EcPartnerCommentFacade extends AbstractFacade {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EcPartnerCommentFacade() {
        super(EcPartnerComment.class);
    }

    public EcPartnerComment find(Long id) {
        return em.find(EcPartnerComment.class, id);
    }

    public boolean isStatusApply(EcPartnerComment entity) {
        EcPartnerComment e = em.find(EcPartnerComment.class, entity.getId());
        return CommentStatusEnum.APPLY == e.getStatus();
    }
    
    /*
    public EcPartnerComment creatThenReturn(EcPartnerComment partnerComment) {
        em.persist(partnerComment);
        partnerComment = em.merge(partnerComment);
        EcPartner partner = partnerComment.getPartner();
        if (partner.getEcAllPartnerCommentList() == null) {
            partner.setEcAllPartnerCommentList(new ArrayList());
        }
        partner.getEcAllPartnerCommentList().add(partnerComment);
        em.merge(partner);
        return partnerComment;
    }
    */

    public List<EcPartnerComment> findByPartner(EcPartner ecPartner) {
        Query q = em.createNamedQuery("EcPartnerComment.findByPartner");
        q.setParameter("ecPartner", ecPartner);
        // 筆數限制?
        // q.setMaxResults(50);
        return (List<EcPartnerComment>) q.getResultList();
    }

    public EcPartnerComment editThenReturn(EcPartnerComment partnerComment) {
        return em.merge(partnerComment);
    }

    public void remove(EcPartnerComment partnerComment) {
        em.remove(em.merge(partnerComment));
    }
    
    public EcPartnerComment addComment(EcMember ecMember, EcPartner ecPartner, double rate, String message) {
        EcPartnerComment partnerComment = new EcPartnerComment(ecMember, ecPartner, rate, message);
        em.persist(partnerComment);
        return partnerComment;
    }
    
    public void approve(EcPartnerComment partnerComment) {
        partnerComment.setStatus(CommentStatusEnum.APPROVE);
        partnerComment.setActive(true);
        em.merge(partnerComment);
        updateParterRate(partnerComment.getEcPartner());
    }

    public void reject(EcPartnerComment partnerComment) {
        partnerComment.setStatus(CommentStatusEnum.REJECT);
        partnerComment.setActive(false);
        em.merge(partnerComment);
        updateParterRate(partnerComment.getEcPartner());
    }
    
    public void updateParterRate(EcPartner ecPartner) {
        Query q = em.createQuery("SELECT AVG(e.rate) FROM EcPartnerComment e WHERE e.ecPartner=:ecPartner AND e.active=TRUE");
        q.setParameter("ecPartner", ecPartner);
        Double rate = null;
        try {
            rate = (Double) q.getSingleResult();
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        // 僅更新 rate
        EcPartner entity = em.find(EcPartner.class, ecPartner.getId());
        entity.setAverageRate(rate);
        em.merge(entity);
        ecPartner.setAverageRate(rate);
    }

    public List<EcPartnerComment> findByCriteria(PartnerCommentFilter filter) {
        List<EcPartnerComment> result = null;
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<EcPartnerComment> cq = cb.createQuery(EcPartnerComment.class);
        Root<EcPartnerComment> root = cq.from(EcPartnerComment.class);
        cq.select(root);

        List<Predicate> predicateList = new ArrayList();

        if (filter.getActive() != null) {
            predicateList.add(cb.equal(root.get("active"), filter.getActive()));
        }

        if (filter.getPartner() != null) {
            predicateList.add(cb.equal(root.get("ecPartner"), filter.getPartner()));
        }
        
        if (filter.getCreatetimeBegin() != null) {
            predicateList.add(cb.greaterThanOrEqualTo(root.get("createtime").as(Date.class), filter.getCreatetimeBegin()));
        }
        
        if (filter.getCreatetimeEnd() != null) {
            Calendar endTime = Calendar.getInstance();
            endTime.setTime(filter.getCreatetimeEnd());
            endTime.add(Calendar.DATE, 1);
            predicateList.add(cb.lessThan(root.get("createtime").as(Date.class), endTime.getTime()));
        }
        
        if (filter.getStatus() != null) {
            predicateList.add(cb.equal(root.get("status"), filter.getStatus()));
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

}
