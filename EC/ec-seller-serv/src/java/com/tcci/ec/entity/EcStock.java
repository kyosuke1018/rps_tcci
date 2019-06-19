/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.entity;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author peter.pan
 */
@Entity
@Table(name = "EC_STOCK")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EcStock.findAll", query = "SELECT e FROM EcStock e")
    , @NamedQuery(name = "EcStock.findById", query = "SELECT e FROM EcStock e WHERE e.id = :id")
    , @NamedQuery(name = "EcStock.findByStoreId", query = "SELECT e FROM EcStock e WHERE e.storeId = :storeId")
    , @NamedQuery(name = "EcStock.findByPrdId", query = "SELECT e FROM EcStock e WHERE e.prdId = :prdId")
    , @NamedQuery(name = "EcStock.findByVarId", query = "SELECT e FROM EcStock e WHERE e.varId = :varId")})
public class EcStock implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @com.sun.istack.NotNull
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_STOCK", sequenceName = "SEQ_STOCK", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STOCK")        
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "STORE_ID")
    private long storeId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PRD_ID")
    private long prdId;
    @Column(name = "VAR_ID")
    private Long varId;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "QUANTITY")
    private BigDecimal quantity;
    @Column(name = "SETTLE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date settleDate;
    @Column(name = "MEMO")
    private String memo;
    
    @Column(name = "CREATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtime;
    @Column(name = "MODIFYTIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifytime;
    @JoinColumn(name = "CREATOR", referencedColumnName = "ID")
    @ManyToOne
    private EcMember creator;
    @JoinColumn(name = "MODIFIER", referencedColumnName = "ID")
    @ManyToOne
    private EcMember modifier;

    public EcStock() {
    }

    public EcStock(Long id) {
        this.id = id;
    }

    public EcStock(Long id, long storeId, long prdId, BigDecimal quantity) {
        this.id = id;
        this.storeId = storeId;
        this.prdId = prdId;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getStoreId() {
        return storeId;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }

    public long getPrdId() {
        return prdId;
    }

    public void setPrdId(long prdId) {
        this.prdId = prdId;
    }

    public Long getVarId() {
        return varId;
    }

    public void setVarId(Long varId) {
        this.varId = varId;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public Date getSettleDate() {
        return settleDate;
    }

    public void setSettleDate(Date settleDate) {
        this.settleDate = settleDate;
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

    public EcMember getCreator() {
        return creator;
    }

    public void setCreator(EcMember creator) {
        this.creator = creator;
    }

    public EcMember getModifier() {
        return modifier;
    }

    public void setModifier(EcMember modifier) {
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
        if (!(object instanceof EcStock)) {
            return false;
        }
        EcStock other = (EcStock) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.ec.entity.EcStock[ id=" + id + " ]";
    }
    
}
