/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.model.rfq;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *　ET_QUOTATION
 * @author Peter.pan
 */
public class QuotationVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long tenderId;
    private Long rfqId;
    private Long rfqVenderId;
    private Long times;
    private Boolean last;
    private Date expiretime;
    private Boolean invoice;
    private String curRfq;
    private String curQuo;
    private BigDecimal exRate;
    private BigDecimal totalAmtRfq;
    private BigDecimal taxRfq;
    private BigDecimal netAmtRfq;
    private BigDecimal totalAmtQuo;
    private BigDecimal taxQuo;
    private BigDecimal netAmtQuo;
    private BigDecimal discount;
    private String memo;

    private Long creatorId;
    private Date createtime;
    private Long modifierId;
    private Date modifytime;

    public QuotationVO() {
    }

    public QuotationVO(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTenderId() {
        return tenderId;
    }

    public void setTenderId(Long tenderId) {
        this.tenderId = tenderId;
    }

    public Long getRfqId() {
        return rfqId;
    }

    public void setRfqId(Long rfqId) {
        this.rfqId = rfqId;
    }

    public Long getRfqVenderId() {
        return rfqVenderId;
    }

    public void setRfqVenderId(Long rfqVenderId) {
        this.rfqVenderId = rfqVenderId;
    }

    public Long getTimes() {
        return times;
    }

    public void setTimes(Long times) {
        this.times = times;
    }

    public Boolean getLast() {
        return last;
    }

    public void setLast(Boolean last) {
        this.last = last;
    }

    public Date getExpiretime() {
        return expiretime;
    }

    public void setExpiretime(Date expiretime) {
        this.expiretime = expiretime;
    }

    public Boolean getInvoice() {
        return invoice;
    }

    public void setInvoice(Boolean invoice) {
        this.invoice = invoice;
    }

    public String getCurRfq() {
        return curRfq;
    }

    public void setCurRfq(String curRfq) {
        this.curRfq = curRfq;
    }

    public String getCurQuo() {
        return curQuo;
    }

    public void setCurQuo(String curQuo) {
        this.curQuo = curQuo;
    }

    public BigDecimal getExRate() {
        return exRate;
    }

    public void setExRate(BigDecimal exRate) {
        this.exRate = exRate;
    }

    public BigDecimal getTotalAmtRfq() {
        return totalAmtRfq;
    }

    public void setTotalAmtRfq(BigDecimal totalAmtRfq) {
        this.totalAmtRfq = totalAmtRfq;
    }

    public BigDecimal getTaxRfq() {
        return taxRfq;
    }

    public void setTaxRfq(BigDecimal taxRfq) {
        this.taxRfq = taxRfq;
    }

    public BigDecimal getNetAmtRfq() {
        return netAmtRfq;
    }

    public void setNetAmtRfq(BigDecimal netAmtRfq) {
        this.netAmtRfq = netAmtRfq;
    }

    public BigDecimal getTotalAmtQuo() {
        return totalAmtQuo;
    }

    public void setTotalAmtQuo(BigDecimal totalAmtQuo) {
        this.totalAmtQuo = totalAmtQuo;
    }

    public BigDecimal getTaxQuo() {
        return taxQuo;
    }

    public void setTaxQuo(BigDecimal taxQuo) {
        this.taxQuo = taxQuo;
    }

    public BigDecimal getNetAmtQuo() {
        return netAmtQuo;
    }

    public void setNetAmtQuo(BigDecimal netAmtQuo) {
        this.netAmtQuo = netAmtQuo;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Long getModifierId() {
        return modifierId;
    }

    public void setModifierId(Long modifierId) {
        this.modifierId = modifierId;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getModifytime() {
        return modifytime;
    }

    public void setModifytime(Date modifytime) {
        this.modifytime = modifytime;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof QuotationVO)) {
            return false;
        }
        QuotationVO other = (QuotationVO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.et.model.QuotationVO[ id=" + id + " ]";
    }
    
}
