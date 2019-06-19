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
@Table(name = "EC_CUS_ADDR")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EcCusAddr.findAll", query = "SELECT e FROM EcCusAddr e")
    , @NamedQuery(name = "EcCusAddr.findById", query = "SELECT e FROM EcCusAddr e WHERE e.id = :id")})
public class EcCusAddr implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_CUS_ADDR", sequenceName = "SEQ_CUS_ADDR", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CUS_ADDR")        
    private Long id;
    @Column(name = "MEMBER_ID")
    private Long memberId;
    @Column(name = "ADDRESS")
    private String address;
    @Column(name = "ADDRESS_2")
    private String address2;
    @Column(name = "TYPE")
    private String type;
    @Column(name = "ALIAS")
    private String alias;
    @Column(name = "COMPANY")
    private String company;
    @Column(name = "CITY")
    private String city;
    @Column(name = "STATE_REGION")
    private String stateRegion;
    @Column(name = "COUNTRY")
    private String country;
    @Column(name = "POSTAL_CODE")
    private String postalCode;
    @Column(name = "FAX")
    private String fax;
    @Column(name = "PHONE")
    private String phone;
    @Column(name = "PHONE_EXTENSION")
    private String phoneExtension;
    @Basic(optional = false)
    @Column(name = "PRIMARY")
    private Boolean primary;
    @Column(name = "RECIPIENT")
    private String recipient;
    
    @Column(name = "STORE_ID")
    private Long storeId;
    @Column(name = "CAR_NO")
    private String carNo;
    @Column(name = "PATROL_LATITUDE")
    private BigDecimal patrolLatitude;
    @Column(name = "PATROL_LONGITUDE")
    private BigDecimal patrolLongitude;
    @Column(name = "DELIVERY_ID")
    private Long deliveryId;
    
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

    public EcCusAddr() {
    }

    public EcCusAddr(Long id) {
        this.id = id;
    }

    public EcCusAddr(Long id, Boolean primary) {
        this.id = id;
        this.primary = primary;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Boolean getPrimary() {
        return primary;
    }

    public void setPrimary(Boolean primary) {
        this.primary = primary;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
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

    public Long getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(Long deliveryId) {
        this.deliveryId = deliveryId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStateRegion() {
        return stateRegion;
    }

    public void setStateRegion(String stateRegion) {
        this.stateRegion = stateRegion;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoneExtension() {
        return phoneExtension;
    }

    public void setPhoneExtension(String phoneExtension) {
        this.phoneExtension = phoneExtension;
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

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
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
        if (!(object instanceof EcCusAddr)) {
            return false;
        }
        EcCusAddr other = (EcCusAddr) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.ec.entity.EcCusAddr[ id=" + id + " ]";
    }
    
}
