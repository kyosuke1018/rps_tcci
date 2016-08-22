package com.tcci.sksp.facade;

import com.tcci.fc.facade.AbstractFacade;
import com.tcci.sksp.entity.ar.SkSalesAllowances;
import com.tcci.sksp.entity.enums.ReasonEnum;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author nEO.Fu
 */
@Stateless
public class SkSalesAllowancesFacade extends AbstractFacade<SkSalesAllowances> {

    private static final Logger logger = LoggerFactory.getLogger(SkSalesAllowancesFacade.class);
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public SkSalesAllowancesFacade() {
        super(SkSalesAllowances.class);
    }

    public SkSalesAllowances createThenReturn(SkSalesAllowances salesAllowances) {
        logger.debug("createThenReturn(),salesAllowances={}", salesAllowances);
        super.create(salesAllowances);
        return getEntityManager().merge(salesAllowances);
    }

    public SkSalesAllowances editThenReturn(SkSalesAllowances salesAllowances) {
        super.edit(salesAllowances);
        return getEntityManager().merge(salesAllowances);
    }

    /**
     * A function to query SkSalesAllowances for reason code "101" or "105".
     * 用來判斷折讓單品項是否重複申請 5%, 5%特殊 3%僅能申請一次
     *
     * @param orderNumber 訂單編號
     * @param item 訂單項次
     * @return
     */
    public List<SkSalesAllowances> findByOrderNumberAndItem(String orderNumber, String item) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(SkSalesAllowances.class);
        Root<SkSalesAllowances> root = cq.from(SkSalesAllowances.class);
        List<Predicate> predicateList = new ArrayList<Predicate>();
        if (StringUtils.isNotEmpty(orderNumber)) {
            Predicate p1 = cb.equal(root.get("orderNumber"), orderNumber);
            predicateList.add(p1);
        }
        if (StringUtils.isNotEmpty(item)) {
            Predicate p2 = cb.equal(root.get("item"), item);
            predicateList.add(p2);
        }
        Predicate pp1 = cb.equal(root.get("reason"), ReasonEnum.PRECENT_5.getCode());
        Predicate pp2 = cb.equal(root.get("reason"), ReasonEnum.PRECENT_5_SPECIAL.getCode());
        Predicate p3 = cb.or(pp1, pp2);
        predicateList.add(p3);
        Predicate[] predicates = new Predicate[predicateList.size()];
        predicates = predicateList.toArray(predicates);
        cq.where(predicates);
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<SkSalesAllowances> findByCriteria(String sapid, String orderNumber, String invoiceNumber, String returnNumber) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(SkSalesAllowances.class);
        Root<SkSalesAllowances> root = cq.from(SkSalesAllowances.class);
        List<Predicate> predicateList = new ArrayList<Predicate>();
        if (StringUtils.isNotEmpty(sapid)) {
            logger.debug("add criteria sapid");
            Predicate p1 = cb.equal(root.get("sapid"), sapid);
            predicateList.add(p1);
        }
        if (StringUtils.isNotEmpty(orderNumber)) {
            logger.debug("add criteria orderNumber");
            Predicate p2 = cb.like(root.get("orderNumber").as(String.class), orderNumber + "%");
            predicateList.add(p2);
        }
        if (StringUtils.isNotEmpty(invoiceNumber)) {
            logger.debug("add criteria invoiceNumber");
            Predicate p3 = cb.like(root.get("invoiceNumber").as(String.class), invoiceNumber + "%");
            predicateList.add(p3);
        }
        if (StringUtils.isNotEmpty(returnNumber)) {
            logger.debug("add criteria returnNumber");
            Predicate p4 = cb.like(root.get("returnNumber").as(String.class), returnNumber + "%");
            predicateList.add(p4);
        }
        Predicate[] predicates = new Predicate[predicateList.size()];
        predicates = predicateList.toArray(predicates);
        cq.where(predicates);
        cq.orderBy(cb.desc(root.get("id")));
        return getEntityManager().createQuery(cq).getResultList();
    }

    /**
     * query sales allowances for upload to SAP (exclude it has been uploaded
     * and get return number already).
     *
     * @return SkSalesAllowances list (not include which has return number
     * already).
     */
    public List<SkSalesAllowances> findForExport() {
        String sql = "SELECT object(a) from SkSalesAllowances a " +
                 "WHERE a.orderNumber NOT IN (SELECT b.orderNumber from SkSalesAllowances b " +
                                               "WHERE LENGTH(TRIM(b.returnNumber))>=0) " +
                "ORDER BY a.id asc";
        Query q = getEntityManager().createQuery(sql);
        return q.getResultList();
    }
}
