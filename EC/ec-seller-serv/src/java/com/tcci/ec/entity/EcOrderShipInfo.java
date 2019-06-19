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
@Table(name = "EC_ORDER_SHIP_INFO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EcOrderShipInfo.findAll", query = "SELECT e FROM EcOrderShipInfo e")
    , @NamedQuery(name = "EcOrderShipInfo.findById", query = "SELECT e FROM EcOrderShipInfo e WHERE e.id = :id")
    , @NamedQuery(name = "EcOrderShipInfo.findByRecipient", query = "SELECT e FROM EcOrderShipInfo e WHERE e.recipient = :recipient")
    , @NamedQuery(name = "EcOrderShipInfo.findByAddress", query = "SELECT e FROM EcOrderShipInfo e WHERE e.address = :address")
    , @NamedQuery(name = "EcOrderShipInfo.findByPhone", query = "SELECT e FROM EcOrderShipInfo e WHERE e.phone = :phone")
    , @NamedQuery(name = "EcOrderShipInfo.findByCarNo", query = "SELECT e FROM EcOrderShipInfo e WHERE e.carNo = :carNo")
    , @NamedQuery(name = "EcOrderShipInfo.findByDriver", query = "SELECT e FROM EcOrderShipInfo e WHERE e.driver = :driver")
    , @NamedQuery(name = "EcOrderShipInfo.findByCreatetime", query = "SELECT e FROM EcOrderShipInfo e WHERE e.createtime = :createtime")
    , @NamedQuery(name = "EcOrderShipInfo.findByModifytime", query = "SELECT e FROM EcOrderShipInfo e WHERE e.modifytime = :modifytime")})
public class EcOrderShipInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_ORDER_SHIP_INFO", sequenceName = "SEQ_ORDER_SHIP_INFO", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ORDER_SHIP_INFO")        
    private Long id;
    @Column(name = "ORDER_ID")
    private Long orderId;
    @Column(name = "SHIPPING_ID")
    private Long shippingId;
    @Column(name = "RECIPIENT")
    private String recipient;
    @Column(name = "ADDRESS")
    private String address;
    @Column(name = "PHONE")
    private String phone;
    @Column(name = "CAR_NO")
    private String carNo;
    @Column(name = "DRIVER")
    private String driver;
    
    @Column(name = "SHIPPING_CODE") // 物流編號
    private String shippingCode;
    @Column(name = "PATROL_LATITUDE") // 緯度
    private BigDecimal patrolLatitude;
    @Column(name = "PATROL_LONGITUDE") // 經度
    private BigDecimal patrolLongitude;

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

    public EcOrderShipInfo() {
    }

    public EcOrderShipInfo(Long id) {
        this.id = id;
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

    public Long getShippingId() {
        return shippingId;
    }

    public void setShippingId(Long shippingId) {
        this.shippingId = shippingId;
    }

    public String getShippingCode() {
        return shippingCode;
    }

    public void setShippingCode(String shippingCode) {
        this.shippingCode = shippingCode;
    }

    public BigDecimal getPatrolLatitude() {
        return patrolLatitude;
    }

    public void setPatrolLatitude(BigDecimal patrolLatitude) {
        this.patrolLatitude = patrolLatitude;
    }

    public BigDecimal getPatrolLongitude() {
        return patrolLongitude;
    }

    public void setPatrolLongitude(BigDecimal patrolLongitude) {
        this.patrolLongitude = patrolLongitude;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
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
        if (!(object instanceof EcOrderShipInfo)) {
            return false;
        }
        EcOrderShipInfo other = (EcOrderShipInfo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.ec.entity.EcOrderShipInfo[ id=" + id + " ]";
    }
    
}
