package com.tcci.sksp.facade;

import com.tcci.fc.facade.AbstractFacade;
import com.tcci.sksp.controller.util.DateUtil;
import com.tcci.sksp.entity.ar.SkOverdueAr;
import com.tcci.sksp.entity.org.SkSalesMember;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Jason.Yu
 */
@Stateless
public class SkOverdueArFacade extends AbstractFacade<SkOverdueAr> {
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SkOverdueArFacade() {
        super(SkOverdueAr.class);
    }
    
    public void save( List<SkOverdueAr> list,Date baselineTimestamp){
        deleteByBaselineTimestamp( baselineTimestamp );
        for(SkOverdueAr overdueAr : list ){
            overdueAr.setBaselineTimestamp(baselineTimestamp);
            overdueAr.setCreatetimestamp(new Date() );
            create(overdueAr);
        }
    }
    
    public int deleteByBaselineTimestamp(Date baselineTimestamp){
        String d = DateUtil.getDateFormat(baselineTimestamp, DateUtil.DATE_FORMAT_YYYYMM);
        String sqlCommand ="delete from SK_OVERDUE_AR t where to_char(t.baseline_Timestamp,'YYYYMM') = ?";
        Query query = getEntityManager().createNativeQuery(sqlCommand);
        //System.out.println("baslineTimestamp=" + d);
        query.setParameter(1, d );
        int count = query.executeUpdate();
        //System.out.println("count=" + count);
        return count;
    }
    
    public List<SkOverdueAr> findByCriteria(SkSalesMember member, String date, Integer overdueDaysNumber){
        List<SkOverdueAr> list = null;

        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<SkOverdueAr> cq = builder.createQuery(SkOverdueAr.class);        
        Root<SkOverdueAr> root = cq.from(SkOverdueAr.class);        
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
        
        if (overdueDaysNumber != null) {
            Predicate p = builder.greaterThanOrEqualTo(root.get("overdueDaysNumber").as(Integer.class), overdueDaysNumber);
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
    
    public List<SkOverdueAr> findByCriteria(SkSalesMember member, Integer overdueDaysNumber, Date startTime, Date endTime){
        List<SkOverdueAr> list = null;

        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<SkOverdueAr> cq = builder.createQuery(SkOverdueAr.class);        
        Root<SkOverdueAr> root = cq.from(SkOverdueAr.class);        
        cq.select(root);
        List<Predicate> predicateList = new ArrayList<Predicate>();
        if (member != null) {
            Predicate p = builder.equal(root.get("sapid"), member.getCode());
            predicateList.add(p);
        }
        
        if (startTime != null && endTime != null) {       
            Predicate p = builder.between(root.get("baselineTimestamp").as(Date.class), startTime, endTime);

            predicateList.add(p);
        }
        
        if (overdueDaysNumber != null) {
            Predicate p = builder.greaterThanOrEqualTo(root.get("overdueDaysNumber").as(Integer.class), overdueDaysNumber);
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
    
}
