/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.facade;

import com.tcci.fc.facade.AbstractFacade;
import com.tcci.sksp.entity.ar.SkSalesMonthAchievement;
import com.tcci.sksp.entity.org.SkSalesMember;
import com.tcci.sksp.vo.AchievementChannelVO;
import java.math.BigDecimal;
import java.util.ArrayList;
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
public class SkSalesMonthAchievementFacade extends AbstractFacade<SkSalesMonthAchievement> {
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public SkSalesMonthAchievementFacade() {
        super(SkSalesMonthAchievement.class);
    }
    
    public List<SkSalesMonthAchievement> findByCriteria(SkSalesMember member, String year ){
        List<SkSalesMonthAchievement> list = null;

        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<SkSalesMonthAchievement> cq = builder.createQuery(SkSalesMonthAchievement.class);
        Root<SkSalesMonthAchievement> root = cq.from(SkSalesMonthAchievement.class);
        cq.select(root);
        List<Predicate> predicateList = new ArrayList<Predicate>();
        if (member != null) {
            Predicate p = builder.equal(root.get("sapid"), member.getCode());
            predicateList.add(p);
        }
        if (!StringUtils.isEmpty(year)) {
            Expression[] args = {root.get("baselineTimestamp"),builder.literal( "YYYY")};
            Expression<String> expression = builder.function("to_char", String.class, args);
            Predicate p = builder.equal( expression, year );
            predicateList.add(p);
        }
        
        if (predicateList.size() > 0) {
            Predicate[] predicates = new Predicate[predicateList.size()];
            predicateList.toArray(predicates);
            cq.where(predicates);
        }
        cq.orderBy(builder.asc(root.get("sapid")), builder.asc(root.get("baselineTimestamp")));
        list = getEntityManager().createQuery(cq).getResultList();
        return list;
    }
    
    public void callMonthAchievement(String yearmm){
        String sqlCommand ="call p_sales_month_achievement(?)";
        Query query = getEntityManager().createNativeQuery(sqlCommand);
        query.setParameter(1, yearmm );
        int count = query.executeUpdate();
    }

    public List<AchievementChannelVO> findChannelReport(String year, String month) {
        List<AchievementChannelVO> result = new ArrayList<AchievementChannelVO>();
        String sql = "SELECT t2.CHANNEL,"
                + " SUM(t2.INVOICE_AMOUNT) INVOICE_AMOUNT,"
                + " SUM(t2.PREMIUM_DISCOUNT) PREMIUM_DISCOUNT,"
                + " SUM(t2.SALES_RETURN) SALES_RETURN,"
                + " SUM(t2.SALES_DISCOUNT) SALES_DISCOUNT,"
                + " SUM(t2.SALES_AMOUNT) SALES_AMOUNT,"
                + " (SUM(t2.SALES_AMOUNT) - SUM(t2.COST) + SUM(t2.RETURN_COST))/SUM(t2.SALES_AMOUNT) G1,"
                + " SUM(t2.SALES_AMOUNT)/SUM(t2.BUDGET_MONTH) A1,"
                + " SUM(t2.BUDGET_MONTH) BUDGET_MONTH"
                + " FROM"
                + " (SELECT t1.*,"
                + " CASE"
                + " WHEN SAPID BETWEEN '910' AND '939'  THEN 'KA'"
                + " WHEN SAPID='999' THEN 'ZZ'"
                + " ELSE SUBSTR(SAPID, 0, 2)"
                + " END CHANNEL"
                + " FROM SK_SALES_MONTH_ACHIEVEMENT t1"
                + " WHERE TO_CHAR(baseline_timestamp,'yyyyMM')=#yyyyMM) t2"
                + " GROUP BY t2.CHANNEL"
                + " ORDER BY t2.CHANNEL";
        Query q = em.createNativeQuery(sql);
        q.setParameter("yyyyMM", year + month);
        List list = q.getResultList();
        for (Object o : list) {
            Object[] v = (Object[]) o;
            int idx = 0;
            AchievementChannelVO vo = new AchievementChannelVO();
            vo.setChannel((String) v[idx++]);
            vo.setInvoiceAmount((BigDecimal) v[idx++]);
            vo.setPremiumDiscount((BigDecimal) v[idx++]);
            vo.setSalesReturn((BigDecimal) v[idx++]);
            vo.setSalesDiscount((BigDecimal) v[idx++]);
            vo.setSalesAmount((BigDecimal) v[idx++]);
            vo.setGrossProfitRate((BigDecimal) v[idx++]);
            vo.setMonthAchievementRate((BigDecimal) v[idx++]);
            vo.setBudgetMonth((BigDecimal) v[idx++]);
            result.add(vo);
        }
        return result;
    }
}
