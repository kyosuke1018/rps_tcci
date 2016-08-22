package com.tcci.sksp.facade;

import com.tcci.fc.facade.AbstractFacade;
import com.tcci.sksp.controller.util.DateUtil;
import com.tcci.sksp.entity.ar.SkPaymentItem;
import com.tcci.sksp.entity.ar.SkPaymentRate;
import com.tcci.sksp.entity.org.SkSalesMember;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Jason.Yu
 */
@Stateless
public class SkPaymentRateFacade extends AbstractFacade<SkPaymentRate> {

    @EJB
    SkPaymentItemFacade paymentItemFacade;
    @EJB
    SkPaymentRateFacade paymentRateFacade;
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public SkPaymentRateFacade() {
        super(SkPaymentRate.class);
    }

    public List<SkPaymentRate> findByCriteria(SkSalesMember member, String date) {
        List<SkPaymentRate> list = null;

        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<SkPaymentRate> cq = builder.createQuery(SkPaymentRate.class);
        Root<SkPaymentRate> root = cq.from(SkPaymentRate.class);
        cq.select(root);
        List<Predicate> predicateList = new ArrayList<Predicate>();
        if (member != null) {
            Predicate p = builder.equal(root.get("sapid"), member.getCode());
            predicateList.add(p);
        }

        if (!StringUtils.isEmpty(date)) {
            Expression[] args = {root.get("baselineTimestamp"), builder.literal("YYYY")};
            Expression<String> year = builder.function("to_char", String.class, args);
            Predicate p = builder.equal(year, date);
            predicateList.add(p);
        }

        if (predicateList.size() > 0) {
            Predicate[] predicates = new Predicate[predicateList.size()];
            predicateList.toArray(predicates);
            cq.where(predicates);
        }

        cq.orderBy(builder.asc(root.get("baselineTimestamp")));
        list = getEntityManager().createQuery(cq).getResultList();
        return list;

    }

    public int deletePaymentRateByBaselineTimestamp(Date baselineTimestamp) {
        String d = DateUtil.getDateFormat(baselineTimestamp, DateUtil.DATE_FORMAT_YYYYMM);
        String sqlCommand = "delete from sk_payment_rate t where to_char(t.baseline_Timestamp,'YYYYMM') = ?";
        Query query = getEntityManager().createNativeQuery(sqlCommand);
        query.setParameter(1, d);
        int count = query.executeUpdate();
        return count;
    }

    public void deletePaymentItemAndRate(Date baselineTimestamp) {
        paymentItemFacade.deletePaymentItemByBaselineTimestamp(baselineTimestamp);
        deletePaymentRateByBaselineTimestamp(baselineTimestamp);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void syncPaymentItemAndRate(Date baselineTimestamp, List<SkPaymentItem> itemList, List<SkPaymentRate> rateList) throws Exception {
        paymentRateFacade.deletePaymentItemAndRate(baselineTimestamp);
        savePaymentRateList(baselineTimestamp, rateList);
        savePaymentItemList(baselineTimestamp, itemList);
    }

    //@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void savePaymentItemList(Date baselineTimestamp, List<SkPaymentItem> itemList) throws SQLException {
        if (itemList != null) {
            for (SkPaymentItem item : itemList) {
                item.setCreatetimestamp(new Date());
                paymentItemFacade.create(item);
            };
        };
    }

    //@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    
    public void savePaymentRateList(Date baselineTimestamp, List<SkPaymentRate> rateList) throws Exception {
        if (rateList != null) {
            for (SkPaymentRate rate : rateList) {
                rate.setCreatetimestamp(new Date());
                /*System.out.println("aramount=" + rate.getArAmount() +",discount1=" + rate.getPremiumDiscount()
                        +",salesreturn="+ rate.getSalesReturn() +",slaesdiscount=" + rate.getSalesDiscount()
                        +",payment=" + rate.getPaymentAmount() + ",rate="+ rate.getPaymentRate() 
                        +",weight="+ rate.getWeight());
                        */
                paymentRateFacade.create(rate);
            };
        };
    }
    public int findRowCount(String baselineTimestamp){
        int count = 0;
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<SkPaymentRate> root = cq.from(SkPaymentRate.class);
        cq.select(cb.count(root));
        Expression[] args = {root.get("baselineTimestamp"), cb.literal("YYYY/MM")};
        Expression<String> year = cb.function("to_char", String.class, args);
        Predicate p = cb.equal(year, baselineTimestamp);
        cq.where(p);
        Long countLong = getEntityManager().createQuery(cq).getSingleResult();
        if( countLong != null){
            count = countLong.intValue();
        }
        return count;
    }
    /*
    public BigDecimal findTotalPaymentAmount(String baselineTimestamp){
        int count = 0;
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<BigDecimal> cq = cb.createQuery(BigDecimal.class);
        Root<SkPaymentRate> root = cq.from(SkPaymentRate.class);
        cq.select(cb.sum(root.get("paymentAmount").as(BigDecimal.class)));
        Expression[] args = {root.get("baselineTimestamp"), cb.literal("YYYY/MM")};
        Expression<String> year = cb.function("to_char", String.class, args);
        Predicate p = cb.equal(year, baselineTimestamp);
        cq.where(p);
        BigDecimal amount = getEntityManager().createQuery(cq).getSingleResult();
        return amount;
    }
    */
    public Object[] findTotalPaymentAmount(String baselineTimestamp){
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<SkPaymentRate> root = cq.from(SkPaymentRate.class);
        cq.multiselect(
                cb.sum(root.get("arAmount").as(BigDecimal.class)),
                cb.sum(root.get("premiumDiscount").as(BigDecimal.class)),
                cb.sum(root.get("salesReturn").as(BigDecimal.class)),
                cb.sum(root.get("salesDiscount").as(BigDecimal.class)),
                cb.sum(root.get("paymentAmount").as(BigDecimal.class))
                );
        Expression[] args = {root.get("baselineTimestamp"), cb.literal("YYYY/MM")};
        Expression<String> year = cb.function("to_char", String.class, args);
        Predicate p = cb.equal(year, baselineTimestamp);
        cq.where(p);
        Object[] amountList = getEntityManager().createQuery(cq).getSingleResult();
        return amountList;
    }
}
