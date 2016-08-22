/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.facade.swls;

import com.tcci.sksp.entity.swls.ComplainUpload;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Jimmy.Lee
 */
@Stateless
public class ComplainUploadFacade {
    
    @PersistenceContext(unitName = "Model")
    private EntityManager em;
    
    public ComplainUpload load(Long id) {
        return em.find(ComplainUpload.class, id);
    }
    
    public void save(ComplainUpload complain) {
        if (complain.getId() == null) {
            em.persist(complain);
        } else {
            em.merge(complain);
        }
    }
    
    public List<ComplainUpload> findAll() {
        Query q = em.createNamedQuery("ComplainUpload.findAll");
        return q.getResultList();
    }
    
    public List<ComplainUpload> find(QueryFilter filter) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root root = cq.from(ComplainUpload.class);
        List<Predicate> predicates = new ArrayList<Predicate>();
        String startDate = filter.getStartDate();
        String endDate = filter.getEndDate();
        String customer = StringUtils.upperCase(StringUtils.trimToNull(filter.getCustomer()));
        String product = StringUtils.upperCase(StringUtils.trimToNull(filter.getProduct()));
        String batchNo = StringUtils.upperCase(StringUtils.trimToNull(filter.getBatchNo()));
        String sales = StringUtils.upperCase(StringUtils.trimToNull(filter.getSales()));
        if (startDate != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("ymd"), startDate));
        }
        if (endDate != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("ymd"), endDate));
        }
        if (customer != null) {
            predicates.add(cb.or(cb.like(root.get("custId"), "%" + customer + "%"),
                  cb.like(cb.upper(root.get("custName")), "%" + customer + "%")));
        }
        if (product != null) {
            predicates.add(cb.or(cb.like(root.get("matNo"), "%" + product + "%"),
                  cb.like(cb.upper(root.get("matName")), "%" + product + "%")));
        }
        if (batchNo != null) {
            predicates.add(cb.like(cb.upper(root.get("batchNo")), "%" + batchNo + "%"));
        }
        if (sales != null) {
            predicates.add(cb.like(cb.upper(root.get("sales")), "%" + sales + "%"));
        }
        if (!predicates.isEmpty()) {
            Predicate[] wheres = new Predicate[predicates.size()];
            predicates.toArray(wheres);
            cq.where(wheres);
        }
        cq.orderBy(cb.desc(root.get("id")));
        Query q = em.createQuery(cq);
        return q.getResultList();
    }
}
