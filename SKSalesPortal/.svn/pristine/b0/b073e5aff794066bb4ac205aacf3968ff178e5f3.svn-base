package com.tcci.sksp.facade;

import com.tcci.fc.facade.AbstractFacade;
import com.tcci.sksp.controller.util.DateUtil;
import com.tcci.sksp.entity.ar.SkPaymentItem;
import com.tcci.sksp.entity.org.SkSalesMember;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Jason.Yu
 */
@Stateless
public class SkPaymentItemFacade extends AbstractFacade<SkPaymentItem> {
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public SkPaymentItemFacade() {
        super(SkPaymentItem.class);
    }
    
    public List<SkPaymentItem> findByCriteria(SkSalesMember member, String date){
        List<SkPaymentItem> list = null;

        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<SkPaymentItem> cq = builder.createQuery(SkPaymentItem.class);        
        Root<SkPaymentItem> root = cq.from(SkPaymentItem.class);        
        cq.select(root);
        List<Predicate> predicateList = new ArrayList<Predicate>();
        if (member != null) {
            Predicate p = builder.equal(root.get("sapid"), member.getCode());
            predicateList.add(p);
        }
        
        if( !StringUtils.isEmpty(date) ){
            Expression[] args = {root.get("baselineTimestamp"),builder.literal( "YYYY/MM")};
            Expression<String> yearAndMonth = builder.function("to_char", String.class, args);
            Predicate p = builder.equal( yearAndMonth, date );
            predicateList.add(p);
        }              
        
        if (predicateList.size() > 0) {
            Predicate[] predicates = new Predicate[predicateList.size()];
            predicateList.toArray(predicates);
            cq.where(predicates);
        }

        cq.orderBy( builder.asc(root.get("customer").get("code")), builder.asc(root.get("orderNumber")));
        list = getEntityManager().createQuery(cq).getResultList();
        return list;
               
    }
    
    public int deletePaymentItemByBaselineTimestamp(Date baselineTimestamp){
        String d = DateUtil.getDateFormat(baselineTimestamp, DateUtil.DATE_FORMAT_YYYYMM);
        String sqlCommand ="delete from sk_payment_item t where to_char(t.baseline_Timestamp,'YYYYMM') = ?";
        Query query = getEntityManager().createNativeQuery(sqlCommand);
        query.setParameter(1, d );
        int count = query.executeUpdate();
        return count;
    }

    public int findRowCount(String baselineTimestamp){
        int count = 0;
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<SkPaymentItem> root = cq.from(SkPaymentItem.class);
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
    public Object[] findTotalPaymentAmount(String baselineTimestamp){
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        //javax.persistence.criteria.CriteriaQuery<BigDecimal> cq = cb.createQuery(BigDecimal.class);
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<SkPaymentItem> root = cq.from(SkPaymentItem.class);
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
