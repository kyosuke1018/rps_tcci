/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.vo;

import com.tcci.sksp.entity.ar.SkSalesOrderDetail;
import com.tcci.sksp.entity.ar.SkSalesOrderMaster;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *
 * @author Jason.Yu
 */
public class SalesOrderVO implements Serializable {
    private SkSalesOrderMaster salesOrderMaster;
    private BigDecimal premiumDiscount;
    private BigDecimal premiumDiscountTax;
    private BigDecimal salesAllowances;
    private boolean selected;
    private boolean printed;
    private int printNumber;
    
    public SkSalesOrderMaster getSalesOrderMaster() {
        return salesOrderMaster;
    }

    public void setSalesOrderMaster(SkSalesOrderMaster salesOrderMaster) {
        this.salesOrderMaster = salesOrderMaster;
        if( salesOrderMaster.getSkSalesOrderDetailCollection() != null){
            premiumDiscount = BigDecimal.ZERO;
            premiumDiscountTax = BigDecimal.ZERO;
            for( SkSalesOrderDetail d : salesOrderMaster.getSkSalesOrderDetailCollection()){
                if( d.getPremiumDiscount() != null){
                    premiumDiscount = premiumDiscount.add( d.getPremiumDiscount() );
                }
                if( d.getPremiumDiscountTax() != null){
                    premiumDiscountTax = premiumDiscountTax.add( d.getPremiumDiscountTax() );
                }
            }
        }        
    }
    
    public BigDecimal getSalesOrderAmount(){
        return salesOrderMaster.getAmount();
    }

    public BigDecimal getPremiumDiscount() {
        return premiumDiscount;
    }

    public BigDecimal getPremiumDiscountTax() {
        return premiumDiscountTax;
    }
    
    public Long getId(){
        return salesOrderMaster.getId();
    }
    
    public BigDecimal getSalesAllowances(){
        if( premiumDiscount == null )
            premiumDiscount = BigDecimal.ZERO;
        if( premiumDiscountTax == null )
            premiumDiscountTax = BigDecimal.ZERO;
        BigDecimal five = BigDecimal.valueOf(5);
        BigDecimal hundred = BigDecimal.valueOf(100);
        salesAllowances = salesOrderMaster.getAmount().subtract( premiumDiscount.add(premiumDiscountTax) ).multiply(five).divide(hundred,0,RoundingMode.HALF_UP);
        return salesAllowances;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isPrinted() {
        return printed;
    }

    public void setPrinted(boolean printed) {
        this.printed = printed;
    }

    public int getPrintNumber() {
        return printNumber;
    }

    public void setPrintNumber(int printNumber) {
        this.printNumber = printNumber;
    }
    
}
