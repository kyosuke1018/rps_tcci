/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.vo;

import java.math.BigDecimal;

/**
 *
 * @author Jimmy.Lee
 */
public class AchievementChannelVO {
    private String channel;
    private BigDecimal invoiceAmount;
    private BigDecimal premiumDiscount;
    private BigDecimal salesReturn;
    private BigDecimal salesDiscount;
    private BigDecimal salesAmount;
    private BigDecimal grossProfitRate;
    private BigDecimal monthAchievementRate;
    private BigDecimal budgetMonth;

    // getter, setter
    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public BigDecimal getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(BigDecimal invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public BigDecimal getPremiumDiscount() {
        return premiumDiscount;
    }

    public void setPremiumDiscount(BigDecimal premiumDiscount) {
        this.premiumDiscount = premiumDiscount;
    }

    public BigDecimal getSalesReturn() {
        return salesReturn;
    }

    public void setSalesReturn(BigDecimal salesReturn) {
        this.salesReturn = salesReturn;
    }

    public BigDecimal getSalesDiscount() {
        return salesDiscount;
    }

    public void setSalesDiscount(BigDecimal salesDiscount) {
        this.salesDiscount = salesDiscount;
    }

    public BigDecimal getSalesAmount() {
        return salesAmount;
    }

    public void setSalesAmount(BigDecimal salesAmount) {
        this.salesAmount = salesAmount;
    }

    public BigDecimal getGrossProfitRate() {
        return grossProfitRate;
    }

    public void setGrossProfitRate(BigDecimal grossProfitRate) {
        this.grossProfitRate = grossProfitRate;
    }

    public BigDecimal getMonthAchievementRate() {
        return monthAchievementRate;
    }

    public void setMonthAchievementRate(BigDecimal monthAchievementRate) {
        this.monthAchievementRate = monthAchievementRate;
    }

    public BigDecimal getBudgetMonth() {
        return budgetMonth;
    }

    public void setBudgetMonth(BigDecimal budgetMonth) {
        this.budgetMonth = budgetMonth;
    }
}
