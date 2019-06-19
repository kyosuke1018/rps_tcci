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
 * @author Peter.pan
 */
@Entity
@Table(name = "EC_ORDER_DETAIL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EcOrderDetail.findAll", query = "SELECT e FROM EcOrderDetail e")
    , @NamedQuery(name = "EcOrderDetail.findById", query = "SELECT e FROM EcOrderDetail e WHERE e.id = :id")})
public class EcOrderDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_ORDER_DETAIL", sequenceName = "SEQ_ORDER_DETAIL", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ORDER_DETAIL")        
    private Long id;
    @Column(name = "SNO")
    private Integer sno;
    @Basic(optional = false)
    @Column(name = "ORDER_ID")
    private Long orderId;
    @Basic(optional = false)
    @Column(name = "PRODUCT_ID")
    private long productId;
    
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "TOTAL")
    private BigDecimal total;
    @Column(name = "PRICE")
    private BigDecimal price;
    @Column(name = "ORI_UNIT_PRICE")
    private BigDecimal oriUnitPrice;
    @Column(name = "ORI_QUANTITY")
    private BigDecimal oriQuantity;

    @Basic(optional = false)
    @Column(name = "QUANTITY")
    private BigDecimal quantity;
    @Basic(optional = false)
    @Column(name = "SHIPPABLE")
    private Boolean shippable;
    @Basic(optional = false)
    @Column(name = "ON_SALE")
    private Boolean onSale;

    @Column(name = "SHIPPING")
    private BigDecimal shipping;
    
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
    
    public EcOrderDetail() {
    }

    public EcOrderDetail(Long id) {
        this.id = id;
    }

    public EcOrderDetail(Long id, long productId, BigDecimal total, BigDecimal price, BigDecimal quantity, Boolean shippable, Boolean onSale) {
        this.id = id;
        this.productId = productId;
        this.total = total;
        this.price = price;
        this.quantity = quantity;
        this.shippable = shippable;
        this.onSale = onSale;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getOriQuantity() {
        return oriQuantity;
    }

    public void setOriQuantity(BigDecimal oriQuantity) {
        this.oriQuantity = oriQuantity;
    }

    public BigDecimal getShipping() {
        return shipping;
    }

    public void setShipping(BigDecimal shipping) {
        this.shipping = shipping;
    }

    public Integer getSno() {
        return sno;
    }

    public void setSno(Integer sno) {
        this.sno = sno;
    }

    public BigDecimal getOriUnitPrice() {
        return oriUnitPrice;
    }

    public void setOriUnitPrice(BigDecimal oriUnitPrice) {
        this.oriUnitPrice = oriUnitPrice;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public Boolean getShippable() {
        return shippable;
    }

    public void setShippable(Boolean shippable) {
        this.shippable = shippable;
    }

    public Boolean getOnSale() {
        return onSale;
    }

    public void setOnSale(Boolean onSale) {
        this.onSale = onSale;
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
        if (!(object instanceof EcOrderDetail)) {
            return false;
        }
        EcOrderDetail other = (EcOrderDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.ec.entity.EcOrderDetail[ id=" + id + " ]";
    }
    
}
