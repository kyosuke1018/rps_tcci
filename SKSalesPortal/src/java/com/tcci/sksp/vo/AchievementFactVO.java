/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.vo;

import java.math.BigDecimal;

/**
 *
 * @author carl.lin
 */
public class AchievementFactVO {
    private String yearMonth;           // 年月
    private String sapId;             // 通路
    private String channel;             // 通路
    private String channelName;         // 通路名稱
    private String saleName;            //業務人員  
    private BigDecimal invoiceAmount;   // 金額
    private BigDecimal premiumDiscount; // 事前溢折
    private BigDecimal salesReturn;     // 退貨金額
    private BigDecimal salesDiscount;   // 事後折讓
    private BigDecimal salesAmount;     // 銷貨淨額
    private BigDecimal grossProfitRate; // 毛利率
    private BigDecimal achievementRate; // 達成率
    private BigDecimal budget;          // 預算
    private BigDecimal cost;            // 成本  
    private BigDecimal targetRate;      // 目標率
    private String createDate;          // 建立日期
    private String achieColor;        // 達成率顏色
    private String achieSubColor;        // 達成率顏色
    private String own;
    private BigDecimal cumulateBudget; //累計預算
    private BigDecimal cumulateAR; //累計達成率

    // getter, setter
    public BigDecimal getCumulateBudget() {
        return cumulateBudget;
    }

    public void setCumulateBudget(BigDecimal cumulateBudget) {
        this.cumulateBudget = cumulateBudget;
    }

    public BigDecimal getCumulateAR() {
        return cumulateAR;
    }

    public void setCumulateAR(BigDecimal cumulateAR) {
        this.cumulateAR = cumulateAR;
    }
    
    public String getOwn() {
        return own;
    }

    public void setOwn(String own) {
        this.own = own;
    }
    
    public String getAchieSubColor() {
        return achieSubColor;
    }

    public void setAchieSubColor(String achieSubColor) {
        this.achieSubColor = achieSubColor;
    }
    
    public String getAchieColor() {
        return achieColor;
    }

    public void setAchieColor(String achieColor) {
        this.achieColor = achieColor;
    }
    
    public String getSapId() {
        return sapId;
    }

    public void setSapId(String sapId) {
        this.sapId = sapId;
    }
    
    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getSaleName() {
        return saleName;
    }

    public void setSaleName(String saleName) {
        this.saleName = saleName;
    }
    
    public BigDecimal getTargetRate() {
        return targetRate;
    }

    public void setTargetRate(BigDecimal targetRate) {
        this.targetRate = targetRate;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
    
    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(String yearMonth) {
        this.yearMonth = yearMonth;
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

    public BigDecimal getAchievementRate() {
        return achievementRate;
    }

    public void setAchievementRate(BigDecimal achievementRate) {
        this.achievementRate = achievementRate;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }
}
