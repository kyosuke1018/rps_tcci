/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.controller.report;

import com.tcci.sksp.controller.util.QueryCriteriaController;
import com.tcci.sksp.facade.SkSalesAchievementFacade;
import com.tcci.sksp.vo.AchievementVO;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jimmy.Lee
 */
@ManagedBean
@ViewScoped
public class SalesAchievementReport {

    private List<AchievementVO> channelReport;
    private List<AchievementVO> monthReport;
    private List<AchievementVO> dailyReport;
    private BigDecimal totalInvoiceAmount,
            totalPremiumDiscount,
            totalSalesReturn,
            totalSalesDiscount,
            totalSalesAmount,
            totalgrossProfitRate,
            totalAchievementRate,
            totalBudget;
    @ManagedProperty(value = "#{queryCriteriaController}")
    private QueryCriteriaController queryCriteriaController;
    @EJB
    private SkSalesAchievementFacade salesAchievementFacade;
    private ResourceBundle rb = ResourceBundle.getBundle("messages");

    @PostConstruct
    private void init() {
        queryCriteriaController.initYearList();
        queryCriteriaController.initMonthList();
    }

    // action
    public void monthReportQuery() {
        String sapid = queryCriteriaController.getFilter().getSales().getCode();
        String year = queryCriteriaController.getFilter().getYear();
        monthReport = salesAchievementFacade.findMonthReport(sapid, year);

        // count total
        countTotal(monthReport);
    }

    public void countTotal(List<AchievementVO> monthVO) {
        totalInvoiceAmount = BigDecimal.ZERO;
        totalPremiumDiscount = BigDecimal.ZERO;
        totalSalesReturn = BigDecimal.ZERO;
        totalSalesDiscount = BigDecimal.ZERO;
        totalSalesAmount = BigDecimal.ZERO;
        totalgrossProfitRate = BigDecimal.ZERO;
        totalAchievementRate = BigDecimal.ZERO;
        totalBudget = BigDecimal.ZERO;
        for (AchievementVO vo : monthVO) {
            totalInvoiceAmount = totalInvoiceAmount.add(vo.getInvoiceAmount());
            totalPremiumDiscount = totalPremiumDiscount.add(vo.getPremiumDiscount());
            totalSalesReturn = totalSalesReturn.add(vo.getSalesReturn());
            totalSalesDiscount = totalSalesDiscount.add(vo.getSalesDiscount());
            totalSalesAmount = totalSalesAmount.add(vo.getSalesAmount());
            totalBudget = totalBudget.add(vo.getBudget());

            if (totalBudget.compareTo(BigDecimal.ZERO) > 0) {
                totalAchievementRate = totalSalesAmount
                        .divide(totalBudget, 4, RoundingMode.HALF_UP);
            }

            totalgrossProfitRate = vo.getSalesAmount()
                    .subtract(vo.getCost())
                    .add(vo.getSalesReturn())
                    .divide(vo.getSalesAmount(), 4, RoundingMode.HALF_UP);

//            totalgrossProfitRate = totalgrossProfitRate.multiply(new BigDecimal("100")
//                    .setScale(0, RoundingMode.HALF_UP));

        }
    }

    public void dailyReportQuery(AchievementVO monthVO) {
        String sapid = queryCriteriaController.getFilter().getSales().getCode();
        String yearMonth = monthVO.getYearMonth();
        dailyReport = salesAchievementFacade.findDailyReport(sapid, yearMonth);
        BigDecimal budget = monthVO.getBudget();
        boolean isBudgetZero = (budget.compareTo(BigDecimal.ZERO) == 0);
        for (AchievementVO dailyVO : dailyReport) {
            if (isBudgetZero) {
                dailyVO.setBudget(BigDecimal.ZERO);
                dailyVO.setAchievementRate(BigDecimal.ZERO);
            } else {
                // budget: 月預算/工作天, 但目前無工作天資訊, 暫時/20
                // 達成率: 銷貨淨額/月預算
                dailyVO.setBudget(budget.divide(BigDecimal.valueOf(20.0), 0, RoundingMode.HALF_UP));
                dailyVO.setAchievementRate(dailyVO.getSalesAmount().divide(budget, 4, RoundingMode.HALF_UP));
            }
        }
    }

    public void channelReportQuery() {
        String yearMonth = queryCriteriaController.getFilter().getYear()
                + queryCriteriaController.getFilter().getMonth();
        channelReport = salesAchievementFacade.findChannelReport(yearMonth);
        
        // count total
        countTotal( channelReport);
    }

    // helper
    public String getPageTitleMonth() {
        // 年度業績月結報表
        return rb.getString("menu.sales.report.sales.achievement");
    }

    public String getPageTitleChannel() {
        // 年度業績月結總表
        return rb.getString("menu.sales.report.sales.achievement.channel");
    }

    public String showYMD(AchievementVO monthVO) {
        if (monthVO != null && monthVO.getYearMonth() != null) {
            String yearMonth = monthVO.getYearMonth();
            int len = yearMonth.length();
            if (6 == len) {
                return yearMonth.substring(0, 4) + "/" + yearMonth.substring(4);
            } else if (8 == len) {
                return yearMonth.substring(0, 4) + "/" + yearMonth.substring(4, 6) + "/" + yearMonth.substring(6);
            }
            return yearMonth;
        }
        return "";
    }

    public String getChannelName(AchievementVO vo) {
        String name = "";
        try {
            name = rb.getString("achievement.channel." + vo.getChannel());
        } catch (MissingResourceException ex) {
        }
        return name;
    }

    // getter, setter
    public BigDecimal getTotalInvoiceAmount() {
        return totalInvoiceAmount;
    }

    public void setTotalInvoiceAmount(BigDecimal totalInvoiceAmount) {
        this.totalInvoiceAmount = totalInvoiceAmount;
    }

    public BigDecimal getTotalPremiumDiscount() {
        return totalPremiumDiscount;
    }

    public void setTotalPremiumDiscount(BigDecimal totalPremiumDiscount) {
        this.totalPremiumDiscount = totalPremiumDiscount;
    }

    public BigDecimal getTotalSalesReturn() {
        return totalSalesReturn;
    }

    public void setTotalSalesReturn(BigDecimal totalSalesReturn) {
        this.totalSalesReturn = totalSalesReturn;
    }

    public BigDecimal getTotalSalesDiscount() {
        return totalSalesDiscount;
    }

    public void setTotalSalesDiscount(BigDecimal totalSalesDiscount) {
        this.totalSalesDiscount = totalSalesDiscount;
    }

    public BigDecimal getTotalSalesAmount() {
        return totalSalesAmount;
    }

    public void setTotalSalesAmount(BigDecimal totalSalesAmount) {
        this.totalSalesAmount = totalSalesAmount;
    }

    public BigDecimal getTotalgrossProfitRate() {
        return totalgrossProfitRate;
    }

    public void setTotalgrossProfitRate(BigDecimal totalgrossProfitRate) {
        this.totalgrossProfitRate = totalgrossProfitRate;
    }

    public BigDecimal getTotalAchievementRate() {
        return totalAchievementRate;
    }

    public void setTotalAchievementRate(BigDecimal totalAchievementRate) {
        this.totalAchievementRate = totalAchievementRate;
    }

    public BigDecimal getTotalBudget() {
        return totalBudget;
    }

    public void setTotalBudget(BigDecimal totalBudget) {
        this.totalBudget = totalBudget;
    }

    public List<AchievementVO> getChannelReport() {
        return channelReport;
    }

    public void setChannelReport(List<AchievementVO> channelReport) {
        this.channelReport = channelReport;
    }

    public List<AchievementVO> getMonthReport() {
        return monthReport;
    }

    public void setMonthReport(List<AchievementVO> monthReport) {
        this.monthReport = monthReport;
    }

    public List<AchievementVO> getDailyReport() {
        return dailyReport;
    }

    public void setDailyReport(List<AchievementVO> dailyReport) {
        this.dailyReport = dailyReport;
    }

    public QueryCriteriaController getQueryCriteriaController() {
        return queryCriteriaController;
    }

    public void setQueryCriteriaController(QueryCriteriaController queryCriteriaController) {
        this.queryCriteriaController = queryCriteriaController;
    }
}
