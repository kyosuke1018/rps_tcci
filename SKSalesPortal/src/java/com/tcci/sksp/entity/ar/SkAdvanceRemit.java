/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.entity.ar;

import com.tcci.sksp.entity.SkCustomer;
import com.tcci.sksp.entity.enums.AdvanceRemitTypeEnum;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Jason.Yu
 */
@Entity
@Table(name = "SK_ADVANCE_REMIT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SkAdvanceRemit.findAll", query = "SELECT s FROM SkAdvanceRemit s"),
    @NamedQuery(name = "SkAdvanceRemit.findById", query = "SELECT s FROM SkAdvanceRemit s WHERE s.id = :id"),
    @NamedQuery(name = "SkAdvanceRemit.findByAmount", query = "SELECT s FROM SkAdvanceRemit s WHERE s.amount = :amount"),
    @NamedQuery(name = "SkAdvanceRemit.findByRemitTimestamp", query = "SELECT s FROM SkAdvanceRemit s WHERE s.remitTimestamp = :remitTimestamp"),
    @NamedQuery(name = "SkAdvanceRemit.findByRemitType", query = "SELECT s FROM SkAdvanceRemit s WHERE s.remitType = :remitType"),
    @NamedQuery(name = "SkAdvanceRemit.findByCreatetimestamp", query = "SELECT s FROM SkAdvanceRemit s WHERE s.createtimestamp = :createtimestamp")})
public class SkAdvanceRemit implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @GeneratedValue(generator="SEQ_TCC")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "AMOUNT")
    private BigDecimal amount;
    @Column(name = "REMIT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date remitTimestamp;
    @Column(name = "REMIT_TYPE")
    @Enumerated(EnumType.STRING)
    private AdvanceRemitTypeEnum remitType;
    @Column(name = "CREATETIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtimestamp;
    @JoinColumn(name = "CUSTOMER", referencedColumnName = "ID")
    @ManyToOne
    private SkCustomer customer;

    public SkAdvanceRemit() {
    }

    public SkAdvanceRemit(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getRemitTimestamp() {
        return remitTimestamp;
    }

    public void setRemitTimestamp(Date remitTimestamp) {
        this.remitTimestamp = remitTimestamp;
    }

    public AdvanceRemitTypeEnum getRemitType() {
        return remitType;
    }

    public void setRemitType(AdvanceRemitTypeEnum remitType) {
        this.remitType = remitType;
    }

    public Date getCreatetimestamp() {
        return createtimestamp;
    }

    public void setCreatetimestamp(Date createtimestamp) {
        this.createtimestamp = createtimestamp;
    }

    public SkCustomer getCustomer() {
        return customer;
    }

    public void setCustomer(SkCustomer customer) {
        this.customer = customer;
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
        if (!(object instanceof SkAdvanceRemit)) {
            return false;
        }
        SkAdvanceRemit other = (SkAdvanceRemit) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getClass().getCanonicalName() + ":" + getId();
    }
    
}
