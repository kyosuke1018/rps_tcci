/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.facade.deliveryplace;

import com.tcci.tccstore.entity.EcDeliveryPlace;
import com.tcci.tccstore.entity.EcDeliveryPreference;
import com.tcci.tccstore.entity.EcDeliveryPreferencePK;
import com.tcci.tccstore.entity.EcMember;
import com.tcci.tccstore.entity.EcPlant;
import com.tcci.tccstore.entity.EcSalesarea;
import com.tcci.tccstore.facade.AbstractFacade;
import java.util.ArrayList;
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
 * @author Jimmy.Lee
 */
@Named
@Stateless
public class EcDeliveryPlaceFacade extends AbstractFacade {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EcDeliveryPlaceFacade() {
        super(EcDeliveryPlace.class);
    }

    public void save(EcDeliveryPlace entity) {
        if (entity.getId() == null) {
            em.persist(entity);
        } else {
            em.merge(entity);
        }
    }

    public EcDeliveryPlace find(Long id) {
        return em.find(EcDeliveryPlace.class, id);
    }

    public EcDeliveryPlace findByCode(String code) {
        Query q = em.createNamedQuery("EcDeliveryPlace.findByCode");
        q.setParameter("code", code);
        List<EcDeliveryPlace> list = q.getResultList();
        return list.isEmpty() ? null : list.get(0);
    }

    public List<EcDeliveryPlace> findPreference(EcMember ecMember) {
        Query q = em.createNamedQuery("EcDeliveryPreference.findByMember");
        q.setParameter("ecMember", ecMember);
        return q.getResultList();
    }

    public List<EcDeliveryPreference> findPreference2(EcMember ecMember) {
        Query q = em.createNamedQuery("EcDeliveryPreference.findByMember2");
        q.setParameter("ecMember", ecMember);
        return q.getResultList();
    }

    public void addPreference(EcMember ecMember, EcDeliveryPlace ecDeliveryPlace, EcPlant ecPlant) {
        EcDeliveryPreferencePK pk = new EcDeliveryPreferencePK(ecMember.getId(), ecDeliveryPlace.getId(), ecPlant.getId());
        EcDeliveryPreference entity = em.find(EcDeliveryPreference.class, pk);
        if (entity == null) {
            entity = new EcDeliveryPreference(pk);
            entity.setEcMember(ecMember);
            entity.setEcDeliveryPlace(ecDeliveryPlace);
            entity.setEcPlant(ecPlant);
            em.persist(entity);
        }
    }

    public void removePreference(EcMember ecMember, EcDeliveryPlace ecDeliveryPlace, EcPlant ecPlant) {
        EcDeliveryPreferencePK pk = new EcDeliveryPreferencePK(ecMember.getId(), ecDeliveryPlace.getId(), ecPlant.getId());
        EcDeliveryPreference entity = em.find(EcDeliveryPreference.class, pk);
        if (entity != null) {
            em.remove(entity);
        }
    }

    public List<EcDeliveryPlace> query(QueryFilter queryFilter) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<EcDeliveryPlace> root = cq.from(EcDeliveryPlace.class);
        List<Predicate> predicates = new ArrayList<>();
        if (queryFilter.getCodeName() != null) {
            String codeName = queryFilter.getCodeName().toLowerCase();
            // (code like '%<codeName>% or name like '%<codeName>%)
            predicates.add(cb.or(cb.like(cb.lower(root.get("code").as(String.class)), like(codeName)),
                    cb.like(cb.lower(root.get("name").as(String.class)), like(codeName))));
        }
        if (queryFilter.getProvince() != null) {
            predicates.add(cb.like(root.get("province").as(String.class), like(queryFilter.getProvince())));
        }
        if (queryFilter.getCity() != null) {
            predicates.add(cb.like(root.get("city").as(String.class), like(queryFilter.getCity())));
        }
        if (queryFilter.getDistrict() != null) {
            predicates.add(cb.like(root.get("district").as(String.class), like(queryFilter.getDistrict())));
        }
        if (queryFilter.getTown() != null) {
            predicates.add(cb.like(root.get("town").as(String.class), like(queryFilter.getTown())));
        }
        if (queryFilter.getEcSalesarea() != null) {
            predicates.add(cb.equal(root.get("ecSalesarea").as(EcSalesarea.class), queryFilter.getEcSalesarea()));
        }
        if (!predicates.isEmpty()) {
            cq.where(predicates.toArray(new Predicate[predicates.size()]));
        }
        cq.orderBy(cb.asc(root.get("code")));
        return em.createQuery(cq).getResultList();
    }
    
    public List<String> findProvinces() {
        String sql = "SELECT DISTINCT e.province FROM EcDeliveryPlace e ORDER BY e.province";
        Query q = em.createQuery(sql);
        return q.getResultList();
    }

    private static String like(String value) {
        return "%" + value + "%";
    }

}
