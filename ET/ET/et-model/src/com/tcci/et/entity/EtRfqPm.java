/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.entity;

import com.tcci.fc.entity.org.TcUser;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
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
@Table(name = "ET_RFQ_PM")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EtRfqPm.findAll", query = "SELECT e FROM EtRfqPm e")
    , @NamedQuery(name = "EtRfqPm.findById", query = "SELECT e FROM EtRfqPm e WHERE e.id = :id")
    , @NamedQuery(name = "EtRfqPm.findByTenderId", query = "SELECT e FROM EtRfqPm e WHERE e.tenderId = :tenderId")
    , @NamedQuery(name = "EtRfqPm.findByRfqId", query = "SELECT e FROM EtRfqPm e WHERE e.rfqId = :rfqId")
    , @NamedQuery(name = "EtRfqPm.findByMandt", query = "SELECT e FROM EtRfqPm e WHERE e.mandt = :mandt")
    , @NamedQuery(name = "EtRfqPm.findByEbeln", query = "SELECT e FROM EtRfqPm e WHERE e.ebeln = :ebeln")
    , @NamedQuery(name = "EtRfqPm.findByEbelp", query = "SELECT e FROM EtRfqPm e WHERE e.ebelp = :ebelp")})
public class EtRfqPm implements Serializable {

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
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "MANDT")
    private String mandt;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "EBELN")
    private String ebeln;
    @Basic(optional = false)
    @NotNull
    @Column(name = "EBELP")
    private long ebelp;
    @Basic(optional = false)
    @NotNull
    @Column(name = "EXTROW")
    private BigInteger extrow;
    @Size(max = 120)
    @Column(name = "KTEXT1")
    private String ktext1;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "MENGE")
    private BigDecimal menge;
    @Size(max = 6)
    @Column(name = "MEINS")
    private String meins;
    @Column(name = "TBTWR")
    private BigDecimal tbtwr;
    @Size(max = 10)
    @Column(name = "WAERS")
    private String waers;
    @Column(name = "NETWR")
    private BigDecimal netwr;
    
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

    
    public EtRfqPm() {
    }

    public EtRfqPm(Long id) {
        this.id = id;
    }

    public EtRfqPm(Long id, String mandt, String ebeln, long ebelp, BigInteger extrow) {
        this.id = id;
        this.mandt = mandt;
        this.ebeln = ebeln;
        this.ebelp = ebelp;
        this.extrow = extrow;
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

    public String getMandt() {
        return mandt;
    }

    public void setMandt(String mandt) {
        this.mandt = mandt;
    }

    public String getEbeln() {
        return ebeln;
    }

    public void setEbeln(String ebeln) {
        this.ebeln = ebeln;
    }

    public long getEbelp() {
        return ebelp;
    }

    public void setEbelp(long ebelp) {
        this.ebelp = ebelp;
    }

    public BigInteger getExtrow() {
        return extrow;
    }

    public void setExtrow(BigInteger extrow) {
        this.extrow = extrow;
    }

    public String getKtext1() {
        return ktext1;
    }

    public void setKtext1(String ktext1) {
        this.ktext1 = ktext1;
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

    public BigDecimal getTbtwr() {
        return tbtwr;
    }

    public void setTbtwr(BigDecimal tbtwr) {
        this.tbtwr = tbtwr;
    }

    public String getWaers() {
        return waers;
    }

    public void setWaers(String waers) {
        this.waers = waers;
    }

    public BigDecimal getNetwr() {
        return netwr;
    }

    public void setNetwr(BigDecimal netwr) {
        this.netwr = netwr;
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
        if (!(object instanceof EtRfqPm)) {
            return false;
        }
        EtRfqPm other = (EtRfqPm) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.et.entity.EtRfqPm[ id=" + id + " ]";
    }
    
}
