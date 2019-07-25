package com.tcci.et.rfq.report.priceLog;

import java.util.Date;

public class QuotationValues {

    private int totalPriceWithoutTax;
    private String payment;

    private int performanceBond; // 履約金
    private int warranty; //質保金

    private int afterTax;// 稅別
    private int taxRate;// 稅率

    private Date deliveryDate;
    private int percentageOfbudgetAndFinalPrice;
    private int percentageOfbudgetAndPrePrice;

    public int getTotalPriceWithoutTax() {
        return totalPriceWithoutTax;
    }

    public void setTotalPriceWithoutTax(int totalPriceWithoutTax) {
        this.totalPriceWithoutTax = totalPriceWithoutTax;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public int getPerformanceBond() {
        return performanceBond;
    }

    public void setPerformanceBond(int performanceBond) {
        this.performanceBond = performanceBond;
    }

    public int getWarranty() {
        return warranty;
    }

    public void setWarranty(int warranty) {
        this.warranty = warranty;
    }

    public int getAfterTax() {
        return afterTax;
    }

    public void setAfterTax(int afterTax) {
        this.afterTax = afterTax;
    }

    public int getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(int taxRate) {
        this.taxRate = taxRate;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public int getPercentageOfbudgetAndFinalPrice() {
        return percentageOfbudgetAndFinalPrice;
    }

    public void setPercentageOfbudgetAndFinalPrice(int percentageOfbudgetAndFinalPrice) {
        this.percentageOfbudgetAndFinalPrice = percentageOfbudgetAndFinalPrice;
    }

    public int getPercentageOfbudgetAndPrePrice() {
        return percentageOfbudgetAndPrePrice;
    }

    public void setPercentageOfbudgetAndPrePrice(int percentageOfbudgetAndPrePrice) {
        this.percentageOfbudgetAndPrePrice = percentageOfbudgetAndPrePrice;
    }

}
