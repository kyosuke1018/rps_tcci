package com.tcci.sksp.facade;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.AbstractFacade;
import com.tcci.sksp.entity.ar.SkPremiumDiscount;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author nEO.Fu
 */
@Stateless
public class SkPremiumDiscountFacade extends AbstractFacade<SkPremiumDiscount> {

    protected final static Logger logger = LoggerFactory.getLogger(SkPremiumDiscountFacade.class);
    //@EJB
    //SkSalesOrderFacade salesOrderFacade;
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public SkPremiumDiscountFacade() {
        super(SkPremiumDiscount.class);
    }

    public SkPremiumDiscount editAndReturn(SkPremiumDiscount discount) {
        super.edit(discount);
        discount = getEntityManager().merge(discount);
        return discount;
    }

    public List<SkPremiumDiscount> findByCriteria(PremiumDiscountFilter filter) {
        HashMap<SkPremiumDiscount, SkPremiumDiscount> uniqueDiscount = new HashMap<SkPremiumDiscount, SkPremiumDiscount>();
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<SkPremiumDiscount> cq = cb.createQuery(SkPremiumDiscount.class);
        Root<SkPremiumDiscount> root = cq.from(SkPremiumDiscount.class);
        cq.select(root);
        List<Predicate> predicateList = new ArrayList<Predicate>();
        //search by order number & date first.
        logger.debug("filter.getNumber()={}", filter.getNumber());
        if (!StringUtils.isEmpty(filter.getNumber())) {
            logger.debug("find by orderNumber");
            Predicate p = cb.equal(root.get("orderNumber"), filter.getNumber());
            predicateList.add(p);
        }
        logger.debug("filter.getBeginDate()={}", filter.getBeginDate());
        logger.debug("filter.getEndDate()={}", filter.getEndDate());
        if (filter.getBeginDate() != null && filter.getEndDate() == null) {
            logger.debug("find by greaterThanOrEqualTo");
            Predicate p = cb.greaterThanOrEqualTo(root.get("orderTimestamp").as(Date.class), filter.getBeginDate());
            predicateList.add(p);
        } else if (filter.getEndDate() != null && filter.getBeginDate() == null) {
            logger.debug("find by lessThanOrEqualTo");
            Predicate p = cb.lessThanOrEqualTo(root.get("orderTimestamp").as(Date.class), filter.getEndDate());
            predicateList.add(p);
        } else if (filter.getBeginDate() != null && filter.getEndDate() != null) {
            logger.debug("find by between");
            Predicate p = cb.between(root.get("orderTimestamp").as(Date.class), filter.getBeginDate(), filter.getEndDate());
            predicateList.add(p);
        }
        logger.debug("filter.getBeginModifytimestamp()={}", filter.getBeginModifytimestamp());
        logger.debug("filter.getEndModifytimestamp()={}", filter.getEndModifytimestamp());
        if (filter.getBeginModifytimestamp() != null && filter.getEndModifytimestamp() == null) {
            Predicate p = cb.greaterThanOrEqualTo(root.get("modifytimestamp").as(Date.class), filter.getBeginModifytimestamp());
            predicateList.add(p);
        } else if (filter.getBeginModifytimestamp() == null && filter.getEndModifytimestamp() != null) {
            Predicate p = cb.lessThanOrEqualTo(root.get("modifytimestamp").as(Date.class), filter.getEndModifytimestamp());
            predicateList.add(p);
        } else {
            Predicate p = cb.between(root.get("modifytimestamp").as(Date.class), filter.getBeginModifytimestamp(), filter.getEndModifytimestamp());
            predicateList.add(p);
        }
        logger.debug("filter.getModifier()={}", filter.getModifier());
        if (filter.getModifier() != null) {
            logger.debug("find by modifier");
            Predicate p = cb.equal(root.get("modifier").as(TcUser.class), filter.getModifier());
            predicateList.add(p);
        }
        Predicate[] predicates = new Predicate[predicateList.size()];
        predicateList.toArray(predicates);
        cq.where(predicates);
        List<SkPremiumDiscount> discountsByOrder = getEntityManager().createQuery(cq).getResultList();
        logger.debug("discountsByOrder={}", discountsByOrder);
        for (SkPremiumDiscount discount : discountsByOrder) {
            uniqueDiscount.put(discount, discount);
        }

        //then search by invoice number & date.
        predicateList = new ArrayList<Predicate>();
        if (!StringUtils.isEmpty(filter.getNumber())) {
            Predicate p = cb.equal(root.get("invoiceNumber"), filter.getNumber());
            predicateList.add(p);
        }
        if (filter.getBeginDate() != null && filter.getEndDate() == null) {
            Predicate p = cb.greaterThanOrEqualTo(root.get("invoiceTimestamp").as(Date.class), filter.getBeginDate());
            predicateList.add(p);
        } else if (filter.getEndDate() != null && filter.getBeginDate() == null) {
            Predicate p = cb.lessThan(root.get("invoiceTimestamp").as(Date.class), filter.getEndDate());
        } else if (filter.getBeginDate() != null && filter.getEndDate() != null) {
            Predicate p = cb.between(root.get("invoiceTimestamp").as(Date.class), filter.getBeginDate(), filter.getEndDate());
            predicateList.add(p);
        }
        if (filter.getBeginModifytimestamp() != null && filter.getEndModifytimestamp() == null) {
            Predicate p = cb.greaterThanOrEqualTo(root.get("modifytimestamp").as(Date.class), filter.getBeginModifytimestamp());
            predicateList.add(p);
        } else if (filter.getBeginModifytimestamp() == null && filter.getEndModifytimestamp() != null) {
            Predicate p = cb.lessThan(root.get("modifytimestamp").as(Date.class), filter.getEndModifytimestamp());
            predicateList.add(p);
        } else if (filter.getBeginModifytimestamp() != null && filter.getEndModifytimestamp() != null) {
            Predicate p = cb.between(root.get("modifytimestamp").as(Date.class), filter.getBeginModifytimestamp(), filter.getEndModifytimestamp());
            predicateList.add(p);
        }
        if (filter.getModifier() != null) {
            logger.debug("find by modifier");
            Predicate p = cb.equal(root.get("modifier").as(TcUser.class), filter.getModifier());
            predicateList.add(p);
        }
        predicates = new Predicate[predicateList.size()];
        predicateList.toArray(predicates);
        cq.where(predicates);
        List<SkPremiumDiscount> discountsByInvoice = getEntityManager().createQuery(cq).getResultList();
        logger.debug("discountsByInvoice={}", discountsByInvoice);
        for (SkPremiumDiscount discount : discountsByInvoice) {
            uniqueDiscount.put(discount, discount);
        }
        List<SkPremiumDiscount> resultDiscounts = new ArrayList<SkPremiumDiscount>();
        resultDiscounts.addAll(uniqueDiscount.values());
        return resultDiscounts;
    }
}
