/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.vo;

import com.tcci.sksp.entity.SkCustomer;
import com.tcci.sksp.entity.ar.SkAccountsReceivable;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;

/**
 *
 * @author Jason.Yu
 */
public class AccountsReceivableVO {
    private SkCustomer customer;
    private List<SkAccountsReceivable> list;
    private BigDecimal totalAmount;
    private BigDecimal totalPremiumDiscount;
    private BigDecimal totalARAmount;
    private String formatTotalARAmount;
    private Long advancedReceiptsA;
    private Long advancedReceiptsJ;
    public SkCustomer getCustomer() {
        return customer;
    }

    public void setCustomer(SkCustomer customer) {
        this.customer = customer;
    }

    public List<SkAccountsReceivable> getList() {
        return list;
    }

    public void setList(List<SkAccountsReceivable> list) {
        this.list = list;
    }

    public BigDecimal getTotalARAmount() {
        return totalARAmount;
    }

    public void setTotalARAmount(BigDecimal totalARAmount) {
        this.totalARAmount = totalARAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalPremiumDiscount() {
        return totalPremiumDiscount;
    }

    public void setTotalPremiumDiscount(BigDecimal totalPremiumDiscount) {
        this.totalPremiumDiscount = totalPremiumDiscount;
    }

    public Long getAdvancedReceiptsA() {
        return advancedReceiptsA;
    }

    public void setAdvancedReceiptsA(Long advancedReceiptsA) {
        this.advancedReceiptsA = advancedReceiptsA;
    }

    public Long getAdvancedReceiptsJ() {
        return advancedReceiptsJ;
    }

    public void setAdvancedReceiptsJ(Long advancedReceiptsJ) {
        this.advancedReceiptsJ = advancedReceiptsJ;
    }

    public String getFormatTotalARAmount() {
        if( totalARAmount == null)
            formatTotalARAmount ="0";
        else{
            formatTotalARAmount = NumberFormat.getNumberInstance().format(totalARAmount);
        }
        return formatTotalARAmount;
    }
    
}
