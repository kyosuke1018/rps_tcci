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
@Table(name = "EC_ORDER_RATE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EcOrderRate.findAll", query = "SELECT e FROM EcOrderRate e")
    , @NamedQuery(name = "EcOrderRate.findById", query = "SELECT e FROM EcOrderRate e WHERE e.id = :id")
    , @NamedQuery(name = "EcOrderRate.findByOrderId", query = "SELECT e FROM EcOrderRate e WHERE e.storeId = :storeId and e.orderId = :orderId")})
public class EcOrderRate implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_ORDER_RATE", sequenceName = "SEQ_ORDER_RATE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ORDER_RATE")        
    private Long id;
    @Column(name = "ORDER_ID")
    private Long orderId;
    @Column(name = "STORE_ID")
    private Long storeId;
    @Column(name = "CUSTOMER_RATE")
    private Integer customerRate;
    
    @InputCheckMeta(key="EC_ORDER_RATE.CUSTOMER_MESSAGE")
    @Column(name = "CUSTOMER_MESSAGE")
    private String customerMessage;
    
    @Column(name = "SELLER_RATE")
    private Integer sellerRate;
    
    @InputCheckMeta(key="EC_ORDER_RATE.SELLER_MESSAGE")
    @Column(name = "SELLER_MESSAGE")
    private String sellerMessage;
    
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

    public EcOrderRate() {
    }

    public EcOrderRate(Long id) {
        this.id = id;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
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

    public Integer getCustomerRate() {
        return customerRate;
    }

    public void setCustomerRate(Integer customerRate) {
        this.customerRate = customerRate;
    }

    public String getCustomerMessage() {
        return customerMessage;
    }

    public void setCustomerMessage(String customerMessage) {
        this.customerMessage = customerMessage;
    }

    public Integer getSellerRate() {
        return sellerRate;
    }

    public void setSellerRate(Integer sellerRate) {
        this.sellerRate = sellerRate;
    }

    public String getSellerMessage() {
        return sellerMessage;
    }

    public void setSellerMessage(String sellerMessage) {
        this.sellerMessage = sellerMessage;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EcOrderRate)) {
            return false;
        }
        EcOrderRate other = (EcOrderRate) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.ec.entity.EcOrderRate[ id=" + id + " ]";
    }
    
}
