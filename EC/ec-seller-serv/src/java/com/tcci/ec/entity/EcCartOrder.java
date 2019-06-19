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
@Table(name = "EC_CART_ORDER")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EcCartOrder.findAll", query = "SELECT e FROM EcCartOrder e")
    , @NamedQuery(name = "EcCartOrder.findById", query = "SELECT e FROM EcCartOrder e WHERE e.id = :id")
    , @NamedQuery(name = "EcCartOrder.findByCreatetime", query = "SELECT e FROM EcCartOrder e WHERE e.createtime = :createtime")
    , @NamedQuery(name = "EcCartOrder.findByModifytime", query = "SELECT e FROM EcCartOrder e WHERE e.modifytime = :modifytime")})
public class EcCartOrder implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_CART_ORDER", sequenceName = "SEQ_CART_ORDER", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CART_ORDER")        
    private Long id;
    @Column(name = "CREATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtime;
    @Column(name = "MODIFYTIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifytime;
    @JoinColumn(name = "MODIFIER", referencedColumnName = "ID")
    @ManyToOne
    private EcMember modifier;
    @JoinColumn(name = "CREATOR", referencedColumnName = "ID")
    @ManyToOne
    private EcMember creator;
    @JoinColumn(name = "ORDER_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EcOrder orderId;

    public EcCartOrder() {
    }

    public EcCartOrder(Long id) {
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

    public EcOrder getOrderId() {
        return orderId;
    }

    public void setOrderId(EcOrder orderId) {
        this.orderId = orderId;
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
        if (!(object instanceof EcCartOrder)) {
            return false;
        }
        EcCartOrder other = (EcCartOrder) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.ec.entity.EcCartOrder[ id=" + id + " ]";
    }
    
}
