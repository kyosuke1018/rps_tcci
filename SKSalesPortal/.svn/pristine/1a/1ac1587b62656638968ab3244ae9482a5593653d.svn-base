package com.tcci.sksp.vo;

import com.tcci.sksp.entity.SkCustomer;
import com.tcci.sksp.entity.ar.SkOverdueAr;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;

public class OverdueArVO {
    private SkCustomer customer;
    private List<SkOverdueAr> list;    
    private Long id;
    private Date baselineTimestamp;
    private String invoiceNumber;
    private Date invoiceTimestamp;
    private BigDecimal invoiceAmount;
    private String orderNumber;
    private String sapid;
    private Integer overdueDaysNumber;
    private BigDecimal totalAmount;
    private BigDecimal totalARAmount;
    private String formatTotalARAmount;
    public SkCustomer getCustomer() {
        return customer;
    }

    public void setCustomer(SkCustomer customer) {
        this.customer = customer;
    }

    public List<SkOverdueAr> getList() {
        return list;
    }

    public void setList(List<SkOverdueAr> list) {
        this.list = list;
    }

   

    public String getFormatTotalARAmount() {
        if( getTotalARAmount() == null)
            setFormatTotalARAmount("0");
        else{
            setFormatTotalARAmount(NumberFormat.getNumberInstance().format(getTotalARAmount()));
        }
        return formatTotalARAmount;
    }
   
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getBaselineTimestamp() {
        return baselineTimestamp;
    }

    public void setBaselineTimestamp(Date baselineTimestamp) {
        this.baselineTimestamp = baselineTimestamp;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }
   
    public Date getInvoiceTimestamp() {
        return invoiceTimestamp;
    }

    public void setInvoiceTimestamp(Date invoiceTimestamp) {
        this.invoiceTimestamp = invoiceTimestamp;
    }

    public BigDecimal getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(BigDecimal invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getSapid() {
        return sapid;
    }

    public void setSapid(String sapid) {
        this.sapid = sapid;
    }

    public Integer getOverdueDaysNumber() {
        return overdueDaysNumber;
    }

    public void setOverdueDaysNumber(Integer overdueDaysNumber) {
        this.overdueDaysNumber = overdueDaysNumber;
    }
    
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }  

    public BigDecimal getTotalARAmount() {
        return totalARAmount;
    }

    public void setTotalARAmount(BigDecimal totalARAmount) {
        this.totalARAmount = totalARAmount;
    }   
    
    public void setFormatTotalARAmount(String formatTotalARAmount) {
        this.formatTotalARAmount = formatTotalARAmount;
    }
    
}
