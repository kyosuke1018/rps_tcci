/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.entity;

import com.tcci.fc.entity.org.TcUser;
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

/**
 *
 * @author Jimmy.Lee
 */
@Entity
@Table(name = "EC_ORDER_DETAIL")
@NamedQueries({
    @NamedQuery(name = "EcOrderDetail.findAll", query = "SELECT e FROM EcOrderDetail e"),
    @NamedQuery(name = "EcOrderDetail.findByOrder", query = "SELECT e FROM EcOrderDetail e WHERE e.order=:order ORDER BY e.createtime ASC")
})
public class EcOrderDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @SequenceGenerator(name="SEQ_ORDER_DETAIL",sequenceName = "SEQ_ORDER_DETAIL", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="SEQ_ORDER_DETAIL")
    private Long id;
    @JoinColumn(name = "ORDER_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EcOrder order;
    @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EcProduct product;
    @Column(name = "TOTAL")
    private BigDecimal total;
    @Column(name = "PRICE")
    private BigDecimal price;
    @Column(name = "SHIPPING")
    private BigDecimal shipping;//20190305單位運費　
    @Column(name = "ORI_UNIT_PRICE")
    private BigDecimal oriUnitPrice;
    @Column(name = "QUANTITY")
    private BigDecimal quantity;
    @Column(name = "ORI_QUANTITY")
    private BigDecimal oriQuantity;
    @Column(name = "SNO")
    private int sno;
    
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
    @Column(name = "STORE_ID")
    private long storeId;

    public EcOrderDetail() {
    }
    public EcOrderDetail(Long storeId) {
        this.storeId = storeId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EcOrder getOrder() {
        return order;
    }

    public void setOrder(EcOrder order) {
        this.order = order;
    }

    public EcProduct getProduct() {
        return product;
    }

    public void setProduct(EcProduct product) {
        this.product = product;
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

    public BigDecimal getOriUnitPrice() {
        return oriUnitPrice;
    }

    public void setOriUnitPrice(BigDecimal oriUnitPrice) {
        this.oriUnitPrice = oriUnitPrice;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public int getSno() {
        return sno;
    }

    public void setSno(int sno) {
        this.sno = sno;
    }

    public EcMember getCreator() {
        return creator;
    }

    public void setCreator(EcMember creator) {
        this.creator = creator;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
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

    public long getStoreId() {
        return storeId;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
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
        return "com.tcci.tccstore.entity.EcOrderDetail[ id=" + id + " ]";
    }
    
}
