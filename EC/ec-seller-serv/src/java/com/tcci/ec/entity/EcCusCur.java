/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.entity;

import javax.validation.constraints.NotNull;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Peter.pan
 */
@Entity
@Table(name = "EC_CUS_CUR")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EcCusCur.findAll", query = "SELECT e FROM EcCusCur e")
    , @NamedQuery(name = "EcCusCur.findById", query = "SELECT e FROM EcCusCur e WHERE e.id = :id")
    , @NamedQuery(name = "EcCusCur.findByCreatetime", query = "SELECT e FROM EcCusCur e WHERE e.createtime = :createtime")
    , @NamedQuery(name = "EcCusCur.findByModifytime", query = "SELECT e FROM EcCusCur e WHERE e.modifytime = :modifytime")})
public class EcCusCur implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_CUS_CUR", sequenceName = "SEQ_CUS_CUR", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CUS_CUR")        
    private Long id;
    @JoinColumn(name = "CURRENCY_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EcCurrency currencyId;
    @JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EcCustomer customerId;

    @JoinColumn(name = "CREATOR", referencedColumnName = "ID")
    @ManyToOne
    private EcMember creator;
    @Column(name = "CREATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtime;
    @JoinColumn(name = "MODIFIER", referencedColumnName = "ID")
    @ManyToOne
    private EcMember modifier;
    @Column(name = "MODIFYTIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifytime;
    
    public EcCusCur() {
    }

    public EcCusCur(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public EcCurrency getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(EcCurrency currencyId) {
        this.currencyId = currencyId;
    }

    public EcCustomer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(EcCustomer customerId) {
        this.customerId = customerId;
    }

    public EcMember getModifier() {
        return modifier;
    }

    public void setModifier(EcMember modifier) {
        this.modifier = modifier;
    }

    public EcMember getCreator() {
        return creator;
    }

    public void setCreator(EcMember creator) {
        this.creator = creator;
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
        if (!(object instanceof EcCusCur)) {
            return false;
        }
        EcCusCur other = (EcCusCur) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.ec.entity.EcCusCur[ id=" + id + " ]";
    }
    
}
