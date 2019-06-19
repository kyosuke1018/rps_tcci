/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.entity;

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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Peter.pan
 */
@Entity
@Table(name = "EC_ORDER_CAR_INFO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EcOrderCarInfo.findAll", query = "SELECT e FROM EcOrderCarInfo e")
    , @NamedQuery(name = "EcOrderCarInfo.findById", query = "SELECT e FROM EcOrderCarInfo e WHERE e.id = :id")
    , @NamedQuery(name = "EcOrderCarInfo.findByStoreId", query = "SELECT e FROM EcOrderCarInfo e WHERE e.storeId = :storeId")
    , @NamedQuery(name = "EcOrderCarInfo.findByOrderId", query = "SELECT e FROM EcOrderCarInfo e WHERE e.orderId = :orderId")
    , @NamedQuery(name = "EcOrderCarInfo.findByProductId", query = "SELECT e FROM EcOrderCarInfo e WHERE e.productId = :productId")
    , @NamedQuery(name = "EcOrderCarInfo.findByCarNo", query = "SELECT e FROM EcOrderCarInfo e WHERE e.carNo = :carNo")
    , @NamedQuery(name = "EcOrderCarInfo.findByQuantity", query = "SELECT e FROM EcOrderCarInfo e WHERE e.quantity = :quantity")
    , @NamedQuery(name = "EcOrderCarInfo.findByCreator", query = "SELECT e FROM EcOrderCarInfo e WHERE e.creator = :creator")
    , @NamedQuery(name = "EcOrderCarInfo.findByCreatetime", query = "SELECT e FROM EcOrderCarInfo e WHERE e.createtime = :createtime")
    , @NamedQuery(name = "EcOrderCarInfo.findByModifier", query = "SELECT e FROM EcOrderCarInfo e WHERE e.modifier = :modifier")
    , @NamedQuery(name = "EcOrderCarInfo.findByModifytime", query = "SELECT e FROM EcOrderCarInfo e WHERE e.modifytime = :modifytime")})
public class EcOrderCarInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_ORDER_CAR_INFO", sequenceName = "SEQ_ORDER_CAR_INFO", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ORDER_CAR_INFO")
    private Long id;
    @Column(name = "STORE_ID")
    private Long storeId;
    @Column(name = "ORDER_ID")
    private Long orderId;
    @Column(name = "PRODUCT_ID")
    private Long productId;
    @Size(max = 20)
    @Column(name = "CAR_NO")
    private String carNo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "QUANTITY")
    private BigDecimal quantity;
    
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

    public EcOrderCarInfo() {
    }

    public EcOrderCarInfo(Long id) {
        this.id = id;
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

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EcOrderCarInfo)) {
            return false;
        }
        EcOrderCarInfo other = (EcOrderCarInfo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.ec.entity.EcOrderCarInfo[ id=" + id + " ]";
    }
    
}
