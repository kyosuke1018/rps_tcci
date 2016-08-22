/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.vo;

import com.tcci.sksp.entity.ar.SkArRemitItem;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;

/**
 *
 * @author Jimmy.Lee
 */
public class RemitExcelVO {
    // master
    private String customer;
    private String sapid;
    private Long masterId;
    private BigDecimal remittanceAmount;
    private BigDecimal checkAmount;
    private String financeReviewer;
    private String reviewTimestamp;
    private String status;
    private String bank;
    // item
    private String orderNumber;
    private String invoiceNumber;
    private String invoiceTimestamp;
    private BigDecimal arAmount;
    private BigDecimal premiumDiscount;
    private BigDecimal salesDiscount;
    private BigDecimal salesReturn;
    private BigDecimal negativeAr;
    private Short differenceCharge;
    private BigDecimal advanceReceiptsA;
    private BigDecimal advanceReceiptsJ;
    private String paymentType;
    private String paymentType2;
    private String checkNumber;
    private String checkNumber2;
    private BigDecimal amount;
    private BigDecimal amount2;

    public RemitExcelVO(SkArRemitItem item) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        // master
        customer = item.getArRemitMaster().getCustomer().getDisplayIdentifier();
        sapid = item.getArRemitMaster().getSapid();
        masterId = item.getArRemitMaster().getId();
        remittanceAmount = item.getArRemitMaster().getRemittanceAmount();
        checkAmount = item.getArRemitMaster().getCheckAmount();
        financeReviewer = item.getArRemitMaster().getFinanceReviewer()==null ? "" : item.getArRemitMaster().getFinanceReviewer().getDisplayIdentifier();
        reviewTimestamp = item.getArRemitMaster().getReviewTimestamp()==null ? "" : sdf.format(item.getArRemitMaster().getReviewTimestamp());
        status = item.getArRemitMaster().getStatus().getDisplayName();
        bank = item.getArRemitMaster().getBank().getDisplayName();
        // item
        orderNumber = item.getOrderNumber();
        invoiceNumber = item.getInvoiceNumber();
        invoiceTimestamp = sdf.format(item.getInvoiceTimestamp());
        arAmount = item.getArAmount();
        premiumDiscount = item.getPremiumDiscount();
        salesDiscount = item.getSalesDiscount();
        salesReturn = item.getSalesReturn();
        negativeAr = item.getNegativeAr();
        differenceCharge = item.getDifferenceCharge();
        advanceReceiptsA = item.getAdvanceReceiptsA();
        advanceReceiptsJ = item.getAdvanceReceiptsJ();
        paymentType = item.getPaymentType()==null ? "" : item.getPaymentType().getDisplayName();
        paymentType2 = item.getPaymentType2()==null ? "" : item.getPaymentType2().getDisplayName();
        checkNumber = item.getCheckNumber();
        checkNumber2 = item.getCheckNumber2();
        amount = item.getAmount();
        amount2 = item.getAmount2();
    }

    // getter, setter
    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getSapid() {
        return sapid;
    }

    public void setSapid(String sapid) {
        this.sapid = sapid;
    }

    public Long getMasterId() {
        return masterId;
    }

    public void setMasterId(Long masterId) {
        this.masterId = masterId;
    }

    public BigDecimal getRemittanceAmount() {
        return remittanceAmount;
    }

    public void setRemittanceAmount(BigDecimal remittanceAmount) {
        this.remittanceAmount = remittanceAmount;
    }

    public BigDecimal getCheckAmount() {
        return checkAmount;
    }

    public void setCheckAmount(BigDecimal checkAmount) {
        this.checkAmount = checkAmount;
    }

    public String getFinanceReviewer() {
        return financeReviewer;
    }

    public void setFinanceReviewer(String financeReviewer) {
        this.financeReviewer = financeReviewer;
    }

    public String getReviewTimestamp() {
        return reviewTimestamp;
    }

    public void setReviewTimestamp(String reviewTimestamp) {
        this.reviewTimestamp = reviewTimestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getInvoiceTimestamp() {
        return invoiceTimestamp;
    }

    public void setInvoiceTimestamp(String invoiceTimestamp) {
        this.invoiceTimestamp = invoiceTimestamp;
    }

    public BigDecimal getArAmount() {
        return arAmount;
    }

    public void setArAmount(BigDecimal arAmount) {
        this.arAmount = arAmount;
    }

    public BigDecimal getPremiumDiscount() {
        return premiumDiscount;
    }

    public void setPremiumDiscount(BigDecimal premiumDiscount) {
        this.premiumDiscount = premiumDiscount;
    }

    public BigDecimal getSalesDiscount() {
        return salesDiscount;
    }

    public void setSalesDiscount(BigDecimal salesDiscount) {
        this.salesDiscount = salesDiscount;
    }

    public BigDecimal getSalesReturn() {
        return salesReturn;
    }

    public void setSalesReturn(BigDecimal salesReturn) {
        this.salesReturn = salesReturn;
    }

    public BigDecimal getNegativeAr() {
        return negativeAr;
    }

    public void setNegativeAr(BigDecimal negativeAr) {
        this.negativeAr = negativeAr;
    }

    public Short getDifferenceCharge() {
        return differenceCharge;
    }

    public void setDifferenceCharge(Short differenceCharge) {
        this.differenceCharge = differenceCharge;
    }

    public BigDecimal getAdvanceReceiptsA() {
        return advanceReceiptsA;
    }

    public void setAdvanceReceiptsA(BigDecimal advanceReceiptsA) {
        this.advanceReceiptsA = advanceReceiptsA;
    }

    public BigDecimal getAdvanceReceiptsJ() {
        return advanceReceiptsJ;
    }

    public void setAdvanceReceiptsJ(BigDecimal advanceReceiptsJ) {
        this.advanceReceiptsJ = advanceReceiptsJ;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getPaymentType2() {
        return paymentType2;
    }

    public void setPaymentType2(String paymentType2) {
        this.paymentType2 = paymentType2;
    }

    public String getCheckNumber() {
        return checkNumber;
    }

    public void setCheckNumber(String checkNumber) {
        this.checkNumber = checkNumber;
    }

    public String getCheckNumber2() {
        return checkNumber2;
    }

    public void setCheckNumber2(String checkNumber2) {
        this.checkNumber2 = checkNumber2;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount2() {
        return amount2;
    }

    public void setAmount2(BigDecimal amount2) {
        this.amount2 = amount2;
    }

}
