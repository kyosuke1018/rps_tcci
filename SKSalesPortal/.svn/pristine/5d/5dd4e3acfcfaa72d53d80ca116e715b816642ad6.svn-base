/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.facade;

import com.tcci.sksp.vo.AchievementVO;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Jimmy.Lee
 */
@Stateless
public class SkSalesAchievementFacade {
    @PersistenceContext(unitName = "Model")
    private EntityManager em;
    
    // 年度業績月結報表 year:yyyyMM
    public List<AchievementVO> findMonthReport(String year) {
        List<AchievementVO> result = new ArrayList<AchievementVO>();
        String sql ="SELECT SAPID,YEAR_MONTH,AMOUNT,PREMIUM_DISCOUNT,SALES_RETURN,SALES_DISCOUNT,BUDGET,COST"
                + " FROM V_SALES_ACHIEVEMENT_MONTH"
                + " WHERE YEAR_MONTH LIKE #year";
        Query q = em.createNativeQuery(sql);
        q.setParameter("year", year + '%');
        List list = q.getResultList();
        for (Object o : list) {
            Object[] v = (Object[]) o;
            int idx = 0;
            AchievementVO vo = new AchievementVO();
            vo.setChannel((String) v[idx++]);
            vo.setYearMonth((String) v[idx++]);
            vo.setInvoiceAmount((BigDecimal) v[idx++]);
            vo.setPremiumDiscount((BigDecimal) v[idx++]);
            vo.setSalesReturn((BigDecimal) v[idx++]);
            vo.setSalesDiscount((BigDecimal) v[idx++]);
            BigDecimal budget = (BigDecimal) v[idx++];
            vo.setBudget(budget);
            BigDecimal cost = (BigDecimal) v[idx++];
            vo.setCost(cost);
            BigDecimal salesAmount = vo.getInvoiceAmount().
                    subtract(vo.getPremiumDiscount()).
                    subtract(vo.getSalesDiscount()).
                    subtract(vo.getSalesReturn());
            vo.setSalesAmount(salesAmount);
            if (salesAmount.compareTo(BigDecimal.ZERO)==0) {
                vo.setGrossProfitRate(BigDecimal.ZERO);
            } else {
                vo.setGrossProfitRate(salesAmount.subtract(cost).add(vo.getSalesReturn()).divide(salesAmount, 4, RoundingMode.HALF_UP));
            }
            if (budget.compareTo(BigDecimal.ZERO)==0) {
                vo.setAchievementRate(BigDecimal.ZERO);
            } else {
                vo.setAchievementRate(salesAmount.divide(budget, 4, RoundingMode.HALF_UP));
            }
            result.add(vo);
        }
        return result;
    }

    // 年度業績月結報表 sapid, year:yyyy
    public List<AchievementVO> findMonthReport(String sapid, String year) {
        List<AchievementVO> result = new ArrayList<AchievementVO>();
        String sql ="SELECT YEAR_MONTH,AMOUNT,PREMIUM_DISCOUNT,SALES_RETURN,SALES_DISCOUNT,BUDGET,COST"
                + " FROM V_SALES_ACHIEVEMENT_MONTH"
                + " WHERE SAPID=#sapid AND YEAR_MONTH LIKE #year";
        Query q = em.createNativeQuery(sql);
        q.setParameter("sapid", sapid);
        q.setParameter("year", year + '%');
        List list = q.getResultList();
        for (Object o : list) {
            Object[] v = (Object[]) o;
            int idx = 0;
            AchievementVO vo = new AchievementVO();
            vo.setYearMonth((String) v[idx++]);
            vo.setInvoiceAmount((BigDecimal) v[idx++]);
            vo.setPremiumDiscount((BigDecimal) v[idx++]);
            vo.setSalesReturn((BigDecimal) v[idx++]);
            vo.setSalesDiscount((BigDecimal) v[idx++]);
            BigDecimal budget = (BigDecimal) v[idx++];
            vo.setBudget(budget);
            BigDecimal cost = (BigDecimal) v[idx++];
            vo.setCost(cost);
            BigDecimal salesAmount = vo.getInvoiceAmount().
                    subtract(vo.getPremiumDiscount()).
                    subtract(vo.getSalesDiscount()).
                    subtract(vo.getSalesReturn());
            vo.setSalesAmount(salesAmount);
            if (salesAmount.compareTo(BigDecimal.ZERO)==0) {
                vo.setGrossProfitRate(BigDecimal.ZERO);
            } else {
                vo.setGrossProfitRate(salesAmount.subtract(cost).add(vo.getSalesReturn()).divide(salesAmount, 4, RoundingMode.HALF_UP));
            }
            if (budget.compareTo(BigDecimal.ZERO)==0) {
                vo.setAchievementRate(BigDecimal.ZERO);
            } else {
                vo.setAchievementRate(salesAmount.divide(budget, 4, RoundingMode.HALF_UP));
            }
            vo.setChannel(sapid);
            result.add(vo);
        }
        return result;
    }

    // 業績日報表 sapid, year:yyyy
    public List<AchievementVO> findDailyReport(String sapid, String yearMonth) {
        List<AchievementVO> result = new ArrayList<AchievementVO>();
        String sql ="SELECT TO_CHAR(INVOICE_TIMESTAMP,'YYYYMMDD') AS DAY,AMOUNT,PREMIUM_DISCOUNT,SALES_RETURN,SALES_DISCOUNT,COST"
                + " FROM V_SALES_ACHIEVEMENT_DAY"
                + " WHERE SAPID=#sapid AND TO_CHAR(INVOICE_TIMESTAMP,'YYYYMM')=#yearMonth";
        Query q = em.createNativeQuery(sql);
        q.setParameter("sapid", sapid);
        q.setParameter("yearMonth", yearMonth);
        List list = q.getResultList();
        for (Object o : list) {
            Object[] v = (Object[]) o;
            int idx = 0;
            AchievementVO vo = new AchievementVO();
            vo.setYearMonth((String) v[idx++]); // yyyyMMdd
            vo.setInvoiceAmount((BigDecimal) v[idx++]);
            vo.setPremiumDiscount((BigDecimal) v[idx++]);
            vo.setSalesReturn((BigDecimal) v[idx++]);
            vo.setSalesDiscount((BigDecimal) v[idx++]);
            BigDecimal salesAmount = vo.getInvoiceAmount().
                    subtract(vo.getPremiumDiscount()).
                    subtract(vo.getSalesDiscount()).
                    subtract(vo.getSalesReturn());
            vo.setSalesAmount(salesAmount);
            result.add(vo);
        }
        return result;
    }

    // 年度業績月結總表 yearMonth:yyyyMM
    public List<AchievementVO> findChannelReport(String yearMonth) {
        List<AchievementVO> result = new ArrayList<AchievementVO>();
        String sql ="SELECT YEAR_MONTH,CHANNEL,AMOUNT,PREMIUM_DISCOUNT,SALES_RETURN,SALES_DISCOUNT,BUDGET,COST"
                + " FROM V_SALES_ACHIEVEMENT_CHANNEL"
                + " WHERE YEAR_MONTH = #yearMonth";
        Query q = em.createNativeQuery(sql);
        q.setParameter("yearMonth", yearMonth);
        List list = q.getResultList();
        for (Object o : list) {
            Object[] v = (Object[]) o;
            int idx = 0;
            AchievementVO vo = new AchievementVO();
            vo.setYearMonth((String) v[idx++]);
            vo.setChannel((String) v[idx++]);
            vo.setInvoiceAmount((BigDecimal) v[idx++]);
            vo.setPremiumDiscount((BigDecimal) v[idx++]);
            vo.setSalesReturn((BigDecimal) v[idx++]);
            vo.setSalesDiscount((BigDecimal) v[idx++]);
            BigDecimal budget = (BigDecimal) v[idx++];
            vo.setBudget(budget);
            BigDecimal cost = (BigDecimal) v[idx++];
            vo.setCost(cost);
            BigDecimal salesAmount = vo.getInvoiceAmount().
                    subtract(vo.getPremiumDiscount()).
                    subtract(vo.getSalesDiscount()).
                    subtract(vo.getSalesReturn());
            vo.setSalesAmount(salesAmount);
            if (salesAmount.compareTo(BigDecimal.ZERO)==0) {
                vo.setGrossProfitRate(BigDecimal.ZERO);
            } else {
                vo.setGrossProfitRate(salesAmount.subtract(cost).add(vo.getSalesReturn()).divide(salesAmount, 4, RoundingMode.HALF_UP));
            }
            if (budget.compareTo(BigDecimal.ZERO)==0) {
                vo.setAchievementRate(BigDecimal.ZERO);
            } else {
                vo.setAchievementRate(salesAmount.divide(budget, 4, RoundingMode.HALF_UP));
            }
            result.add(vo);
        }
        return result;
    }
}     
