/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.entity;

import com.tcci.fc.entity.org.TcUser;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
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
@Table(name = "ET_BARGAIN_VENDER")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EtBargainVender.findAll", query = "SELECT e FROM EtBargainVender e")
    , @NamedQuery(name = "EtBargainVender.findById", query = "SELECT e FROM EtBargainVender e WHERE e.id = :id")
    , @NamedQuery(name = "EtBargainVender.findByTenderId", query = "SELECT e FROM EtBargainVender e WHERE e.tenderId = :tenderId")
    , @NamedQuery(name = "EtBargainVender.findByRfqId", query = "SELECT e FROM EtBargainVender e WHERE e.rfqId = :rfqId")
    , @NamedQuery(name = "EtBargainVender.findByRfqVenderId", query = "SELECT e FROM EtBargainVender e WHERE e.rfqVenderId = :rfqVenderId")
    , @NamedQuery(name = "EtBargainVender.findByBargainId", query = "SELECT e FROM EtBargainVender e WHERE e.bargainId = :bargainId")
    , @NamedQuery(name = "EtBargainVender.findByKey", query = "SELECT e FROM EtBargainVender e WHERE e.tenderId = :tenderId and e.rfqId = :rfqId and e.rfqVenderId = :rfqVenderId and e.bargainId = :bargainId")
})
public class EtBargainVender implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_BARGAIN_VENDER", sequenceName = "SEQ_BARGAIN_VENDER", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_BARGAIN_VENDER")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TENDER_ID")
    private Long tenderId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "RFQ_ID")
    private Long rfqId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "RFQ_VENDER_ID")
    private Long rfqVenderId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "BARGAIN_ID")
    private Long bargainId;
    @Size(max = 300)
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

    public EtBargainVender() {
    }

    public EtBargainVender(Long id) {
        this.id = id;
    }

    public EtBargainVender(Long id, Long tenderId, Long rfqId, Long rfqVenderId, Long bargainId) {
        this.id = id;
        this.tenderId = tenderId;
        this.rfqId = rfqId;
        this.rfqVenderId = rfqVenderId;
        this.bargainId = bargainId;
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

    public Long getBargainId() {
        return bargainId;
    }

    public void setBargainId(Long bargainId) {
        this.bargainId = bargainId;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
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

    public TcUser getCreator() {
        return creator;
    }

    public void setCreator(TcUser creator) {
        this.creator = creator;
    }

    public TcUser getModifier() {
        return modifier;
    }

    public void setModifier(TcUser modifier) {
        this.modifier = modifier;
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
        if (!(object instanceof EtBargainVender)) {
            return false;
        }
        EtBargainVender other = (EtBargainVender) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.et.entity.EtBargainVender[ id=" + id + " ]";
    }
    
}
