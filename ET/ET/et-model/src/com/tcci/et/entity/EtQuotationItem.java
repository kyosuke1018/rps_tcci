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
@Table(name = "ET_QUOTATION_ITEM")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EtQuotationItem.findAll", query = "SELECT e FROM EtQuotationItem e")
    , @NamedQuery(name = "EtQuotationItem.findById", query = "SELECT e FROM EtQuotationItem e WHERE e.id = :id")
    , @NamedQuery(name = "EtQuotationItem.findByTenderId", query = "SELECT e FROM EtQuotationItem e WHERE e.tenderId = :tenderId")
    , @NamedQuery(name = "EtQuotationItem.findByRfqId", query = "SELECT e FROM EtQuotationItem e WHERE e.rfqId = :rfqId")
    , @NamedQuery(name = "EtQuotationItem.findByQuoId", query = "SELECT e FROM EtQuotationItem e WHERE e.quoId = :quoId")
    , @NamedQuery(name = "EtQuotationItem.findByEbelp", query = "SELECT e FROM EtQuotationItem e WHERE e.ebelp = :ebelp")})
public class EtQuotationItem implements Serializable {

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
    @Column(name = "QUO_ID")
    private Long quoId;
    @Column(name = "EBELP")
    private Long ebelp;
    @Size(max = 2)
    @Column(name = "LOEKZ")
    private String loekz;
    @Size(max = 36)
    @Column(name = "MATNR")
    private String matnr;
    @Size(max = 80)
    @Column(name = "TXZ01")
    private String txz01;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "MENGE")
    private BigDecimal menge;
    @Size(max = 6)
    @Column(name = "MEINS")
    private String meins;
    @Column(name = "NETPR")
    private BigDecimal netpr;
    @Column(name = "PEINH")
    private BigDecimal peinh;
    @Column(name = "NETWR")
    private BigDecimal netwr;
    @Column(name = "BRTWR")
    private BigDecimal brtwr;
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

    public EtQuotationItem() {
    }

    public EtQuotationItem(Long id) {
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

    public Long getQuoId() {
        return quoId;
    }

    public void setQuoId(Long quoId) {
        this.quoId = quoId;
    }

    public Long getEbelp() {
        return ebelp;
    }

    public void setEbelp(Long ebelp) {
        this.ebelp = ebelp;
    }

    public String getLoekz() {
        return loekz;
    }

    public void setLoekz(String loekz) {
        this.loekz = loekz;
    }

    public String getMatnr() {
        return matnr;
    }

    public void setMatnr(String matnr) {
        this.matnr = matnr;
    }

    public String getTxz01() {
        return txz01;
    }

    public void setTxz01(String txz01) {
        this.txz01 = txz01;
    }

    public BigDecimal getMenge() {
        return menge;
    }

    public void setMenge(BigDecimal menge) {
        this.menge = menge;
    }

    public String getMeins() {
        return meins;
    }

    public void setMeins(String meins) {
        this.meins = meins;
    }

    public BigDecimal getNetpr() {
        return netpr;
    }

    public void setNetpr(BigDecimal netpr) {
        this.netpr = netpr;
    }

    public BigDecimal getPeinh() {
        return peinh;
    }

    public void setPeinh(BigDecimal peinh) {
        this.peinh = peinh;
    }

    public BigDecimal getNetwr() {
        return netwr;
    }

    public void setNetwr(BigDecimal netwr) {
        this.netwr = netwr;
    }

    public BigDecimal getBrtwr() {
        return brtwr;
    }

    public void setBrtwr(BigDecimal brtwr) {
        this.brtwr = brtwr;
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
        if (!(object instanceof EtQuotationItem)) {
            return false;
        }
        EtQuotationItem other = (EtQuotationItem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.et.entity.EtQuotationItem[ id=" + id + " ]";
    }
    
}
