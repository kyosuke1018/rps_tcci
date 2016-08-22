/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.vo;

import com.tcci.sksp.entity.enums.SalesAllowancesPageEnum;
import java.math.BigDecimal;
import java.util.List;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Jason.Yu
 */
public class SalesAllowanceVO implements Cloneable {
    private String customerSimpleCode;
    private String customerName;
    private String vat;
    private String city;
    private String street;
    private String address; //internal calculate
    private String shippingCondition;
    private String paymentTerm;
    private String sapid;
    //SkCustomer customer;
    private List<SalesDetailsVO> list;
    private BigDecimal totalSalesDiscount;
    private BigDecimal totalSalesDiscountTax;
    private int page;
    private int item;
    private SalesAllowancesPageEnum salesAllowancesPage;
    private BigDecimal discountRate;
/*
    public SkCustomer getCustomer() {
        return customer;
    }

    public void setCustomer(SkCustomer customer) {
        this.customer = customer;
    }
*/
    public List<SalesDetailsVO> getList() {
        return list;
    }

    public void setList(List<SalesDetailsVO> list) {
        this.list = list;
    }

    public BigDecimal getTotalSalesDiscount() {
        return totalSalesDiscount;
    }

    public void setTotalSalesDiscount(BigDecimal totalSalesDiscount) {
        this.totalSalesDiscount = totalSalesDiscount;
    }

    public BigDecimal getTotalSalesDiscountTax() {
        return totalSalesDiscountTax;
    }

    public void setTotalSalesDiscountTax(BigDecimal totalSalesDiscountTax) {
        this.totalSalesDiscountTax = totalSalesDiscountTax;
    }

    public int getItem() {
        return item;
    }

    public void setItem(int item) {
        this.item = item;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getAddress() {
        address ="";
        if( !StringUtils.isEmpty( this.getCity() ) )
            address = getCity();
        if( !StringUtils.isEmpty( this.getStreet() ) )
            address = address + getStreet();
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerSimpleCode() {
        return customerSimpleCode;
    }

    public void setCustomerSimpleCode(String customerSimpleCode) {
        this.customerSimpleCode = customerSimpleCode;
    }

    public String getPaymentTerm() {
        return paymentTerm;
    }

    public void setPaymentTerm(String paymentTerm) {
        this.paymentTerm = paymentTerm;
    }

    public String getShippingCondition() {
        return shippingCondition;
    }

    public void setShippingCondition(String shippingCondition) {
        this.shippingCondition = shippingCondition;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getVat() {
        return vat;
    }

    public void setVat(String vat) {
        this.vat = vat;
    }

    public String getSapid() {
        return sapid;
    }

    public void setSapid(String sapid) {
        this.sapid = sapid;
    }

    public SalesAllowancesPageEnum getSalesAllowancesPage() {
        return salesAllowancesPage;
    }

    public void setSalesAllowancesPage(SalesAllowancesPageEnum salesAllowancesPage) {
        this.salesAllowancesPage = salesAllowancesPage;
    } 

    public BigDecimal getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
    }
    
    public SalesAllowanceVO clone() {
        try {
            SalesAllowanceVO cloned = (SalesAllowanceVO) super.clone();
            cloned.setPage(0);
            return cloned;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
    
}
