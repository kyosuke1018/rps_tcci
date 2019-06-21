/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.entity;

import com.tcci.fc.entity.org.TcUser;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Peter.pan
 */
@Entity
@Table(name = "ET_QUOTATION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EtQuotation.findAll", query = "SELECT e FROM EtQuotation e")
    , @NamedQuery(name = "EtQuotation.findById", query = "SELECT e FROM EtQuotation e WHERE e.id = :id")
    , @NamedQuery(name = "EtQuotation.findByTenderId", query = "SELECT e FROM EtQuotation e WHERE e.tenderId = :tenderId")
    , @NamedQuery(name = "EtQuotation.findByRfqId", query = "SELECT e FROM EtQuotation e WHERE e.rfqId = :rfqId")
    , @NamedQuery(name = "EtQuotation.findByRfqVenderId", query = "SELECT e FROM EtQuotation e WHERE e.rfqVenderId = :rfqVenderId")})
public class EtQuotation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;
    @Column(name = "TENDER_ID")
    private Long tenderId;
    @Column(name = "RFQ_ID")
    private Long rfqId;
    @Column(name = "RFQ_VENDER_ID")
    private Long rfqVenderId;
    @Column(name = "TIMES")
    private Long times;
    @Column(name = "LAST")
    private Boolean last;
    @Column(name = "EXPIRETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiretime;
    @Column(name = "INVOICE")
    private Boolean invoice;
    @Size(max = 10)
    @Column(name = "CUR_RFQ")
    private String curRfq;
    @Size(max = 10)
    @Column(name = "CUR_QUO")
    private String curQuo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "EX_RATE")
    private BigDecimal exRate;
    @Column(name = "TOTAL_AMT_RFQ")
    private BigDecimal totalAmtRfq;
    @Column(name = "TAX_RFQ")
    private BigDecimal taxRfq;
    @Column(name = "NET_AMT_RFQ")
    private BigDecimal netAmtRfq;
    @Column(name = "TOTAL_AMT_QUO")
    private BigDecimal totalAmtQuo;
    @Column(name = "TAX_QUO")
    private BigDecimal taxQuo;
    @Column(name = "NET_AMT_QUO")
    private BigDecimal netAmtQuo;
    @Column(name = "DISCOUNT")
    private BigDecimal discount;
    @Size(max = 1024)
    @Column(name = "MEMO")
    private String memo;
    
    @JoinColumn(name = "CREATOR", referencedColumnName = "ID")
    @ManyToOne
    private TcUser creator;
    @Column(name = "CREATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtime;
    @JoinColumn(name = "MODIFIER", referencedColumnName = "ID")
    @ManyToOne
    private TcUser modifier;
    @Column(name = "MODIFYTIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifytime;

    public EtQuotation() {
    }

    public EtQuotation(Long id) {
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

    public TcUser getCreator() {
        return creator;
    }

    public void setCreator(TcUser creator) {
        this.creator = creator;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public TcUser getModifier() {
        return modifier;
    }

    public void setModifier(TcUser modifier) {
        this.modifier = modifier;
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
        if (!(object instanceof EtQuotation)) {
            return false;
        }
        EtQuotation other = (EtQuotation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.et.entity.EtQuotation[ id=" + id + " ]";
    }
    
}
