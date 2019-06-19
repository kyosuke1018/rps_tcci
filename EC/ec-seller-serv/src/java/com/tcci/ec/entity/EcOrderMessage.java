/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.entity;

import javax.validation.constraints.NotNull;
import com.tcci.cm.annotation.InputCheckMeta;
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
@Table(name = "EC_ORDER_MESSAGE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EcOrderMessage.findAll", query = "SELECT e FROM EcOrderMessage e")
    , @NamedQuery(name = "EcOrderMessage.findById", query = "SELECT e FROM EcOrderMessage e WHERE e.id = :id")
    , @NamedQuery(name = "EcOrderMessage.findByMessage", query = "SELECT e FROM EcOrderMessage e WHERE e.message = :message")
    , @NamedQuery(name = "EcOrderMessage.findByCreatetime", query = "SELECT e FROM EcOrderMessage e WHERE e.createtime = :createtime")})
public class EcOrderMessage implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_ORDER_MESSAGE", sequenceName = "SEQ_ORDER_MESSAGE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ORDER_MESSAGE")        
    private Long id;

    @Column(name = "STORE_ID")
    private Long storeId;
    @Column(name = "ORDER_ID")
    private Long orderId;
    
    @Column(name = "DISABLED")
    private Boolean disabled = false;
    @Column(name = "BUYER")
    private Boolean buyer;
    
    @Column(name = "MESSAGE")
    @InputCheckMeta(key="EC_ORDER_MESSAGE.MESSAGE")
    private String message;
    
    @JoinColumn(name = "CREATOR", referencedColumnName = "ID")
    @ManyToOne
    private EcMember creator;
    @Column(name = "CREATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtime;

    public EcOrderMessage() {
    }

    public EcOrderMessage(Long id) {
        this.id = id;
    }

    public Boolean getBuyer() {
        return buyer;
    }

    public void setBuyer(Boolean buyer) {
        this.buyer = buyer;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public EcMember getCreator() {
        return creator;
    }

    public void setCreator(EcMember creator) {
        this.creator = creator;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
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
        if (!(object instanceof EcOrderMessage)) {
            return false;
        }
        EcOrderMessage other = (EcOrderMessage) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.ec.entity.EcOrderMessage[ id=" + id + " ]";
    }
    
}
