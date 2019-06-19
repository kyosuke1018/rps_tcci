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
@Table(name = "EC_ORDER_LOG")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EcOrderLog.findAll", query = "SELECT e FROM EcOrderLog e")
    , @NamedQuery(name = "EcOrderLog.findById", query = "SELECT e FROM EcOrderLog e WHERE e.id = :id")
    , @NamedQuery(name = "EcOrderLog.findByEventType", query = "SELECT e FROM EcOrderLog e WHERE e.eventType = :eventType")
    , @NamedQuery(name = "EcOrderLog.findByOrderId", query = "SELECT e FROM EcOrderLog e WHERE e.orderId = :orderId")
})
public class EcOrderLog implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_ORDER_LOG", sequenceName = "SEQ_ORDER_LOG", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ORDER_LOG")        
    private Long id;
    @Column(name = "STORE_ID")
    private Long storeId;
    @Column(name = "ORDER_ID")
    private Long orderId;
    @Column(name = "TYPE")
    private String type;
    @Column(name = "EVENT_TYPE")
    private String eventType;
    @Column(name = "MESSAGE")
    private String message;

    @Column(name = "BUYER")
    private Boolean buyer;
    
    @Column(name = "TOTAL")
    private BigDecimal total;
    @Column(name = "SHIPPING_TOTAL")
    private BigDecimal shippingTotal;

    @Column(name = "MEMBER_ID")
    private Long memberId;
    @Column(name = "USER_ID")
    private Long userId;
    
    @Column(name = "CREATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtime;

    public EcOrderLog() {
    }

    public EcOrderLog(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Boolean getBuyer() {
        return buyer;
    }

    public void setBuyer(Boolean buyer) {
        this.buyer = buyer;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getShippingTotal() {
        return shippingTotal;
    }

    public void setShippingTotal(BigDecimal shippingTotal) {
        this.shippingTotal = shippingTotal;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
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

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
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
        if (!(object instanceof EcOrderLog)) {
            return false;
        }
        EcOrderLog other = (EcOrderLog) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.ec.entity.EcOrderLog[ id=" + id + " ]";
    }
    
}
